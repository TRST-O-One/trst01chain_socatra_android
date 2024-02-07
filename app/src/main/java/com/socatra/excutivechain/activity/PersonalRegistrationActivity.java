package com.socatra.excutivechain.activity;

import static com.socatra.excutivechain.AppConstant.DeviceUserID;
import static com.socatra.excutivechain.AppConstant.ERROR_MESSAGE_ENTER_ADDRESS;
import static com.socatra.excutivechain.AppConstant.ERROR_MESSAGE_ENTER_FATHERNAME;
import static com.socatra.excutivechain.AppConstant.ERROR_MESSAGE_ENTER_FIRSTNAME;
import static com.socatra.excutivechain.AppConstant.ERROR_MESSAGE_ENTER_LASTNAME;
import static com.socatra.excutivechain.AppConstant.ERROR_MESSAGE_ENTER_PINCODE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.socatra.excutivechain.AppConstant;
import com.socatra.excutivechain.BaseActivity;
import com.socatra.excutivechain.BuildConfig;
import com.socatra.excutivechain.CommonUtils;
import com.socatra.excutivechain.R;
import com.socatra.excutivechain.database.entity.Country;
import com.socatra.excutivechain.database.entity.DistrictorRegency;
import com.socatra.excutivechain.database.entity.FarmersTable;
import com.socatra.excutivechain.database.entity.PlantationDocuments;
import com.socatra.excutivechain.database.entity.StateorProvince;
import com.socatra.excutivechain.database.entity.SubDistrict;
import com.socatra.excutivechain.database.entity.VillageTable;
import com.socatra.excutivechain.helper.CircleImageView;
import com.socatra.excutivechain.location.BoundLocationManager;
import com.socatra.excutivechain.view_models.AppViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class PersonalRegistrationActivity extends BaseActivity implements HasSupportFragmentInjector {

    String TAG = "PersonalRegistrationActivity";
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    public AppViewModel viewModel;
    TextView farmer_code;
    TextView txt_Personal_Details;
    TextView title1;
    TextView firstname1;
    TextView lastname1;
    TextView fathername1;
    TextView age1;
    TextView Gender1;
    TextView addressdetails1;
    TextView country1;
    TextView state1;
    TextView district1;

    TextView subdistrict1;
    TextView village1;
    TextView pincode1;
    TextView address1;

    TextView noofplots1;

    TextView contactdetails1;
    TextView primary1;
    TextView secondary1;
    TextView identitydetails1;

    TextView national1;
    TextView nationalcode1;
    TextView txt_save_Button;
    TextView txtSaveButton, txtFarmerCode;

    ImageView imgCancel;

    String[] titleArr = new String[]{"Title*", "Mr", "Mrs", "Ms"};


    String[] genderArr = new String[]{"Select Gender*", "Male", "Female", "Others"};

    Spinner spTitle, spGender, spVillageName;

    String strFarmerCode, strFarmerTitle, strFarmerGender, strVillage;

    EditText etPincode;

    Spinner etSubDistrict, etDistrict, etState, etCountry;
    EditText etFirstName, etLastName, etFatherName, etAge, etPrimaryContact, etAddress, etNoOfPlot, etNationalIdentityCode;

    CircleImageView imgFarmer;

    ImageView imgNationalIdentity;

    //Camera
    Integer testPictureCount = 0;

    private String[] PERMISSIONS_STORAGE = {//cam
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static final int REQUEST_CAM_PERMISSIONS = 1;//cam
    private static final int CAMERA_REQUEST = 1888;//cam
//    private static final int SECOND_CAMERA_REQUEST = 1889;//cam

    static File f = null;//cam

    private String strFarmerImageLocalImagePath = "";

    private String strFarmerRUri = "";
    private String strNationalIdLocalImagePath = "";

    String strFileExtension1 = null;//cam
    String strFileExtension2 = null;//cam

    Bitmap bitmapFarmer = null;//cam
    Bitmap bitmapNationalId = null;//cam

    private byte[] bytes = null;//cam

    String imgStatus1;

    String crLatTxt = "", crLongTxt = "";

    FusedLocationProviderClient client;

    LocationRequest locationRequest;

    private SharedPreferences preferences;

    int imageTakenFar = 0;

    int imageTakenDoc = 0;

    private static final int FILE_SELECT_CODE = 101;

    Dialog dialog, imagePreviewDialog;

    int pickHandle1 = 0;
    int pickHandle2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_registration);

        initializeUI();
        configureDagger();
        configureViewModel();
        initializeValues();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        client = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(200);//1hr3600000
        locationRequest.setFastestInterval(100);//360000
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        updateTextLabels();

    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);

            if (locationResult != null) {

                //initialise latlang
                LatLng latLng = new LatLng(locationResult.getLocations().get(0).getLatitude(), locationResult.getLocations().get(0).getLongitude());

                crLatTxt = String.valueOf(locationResult.getLocations().get(0).getLatitude());
                crLongTxt = String.valueOf(locationResult.getLocations().get(0).getLongitude());
                Log.e("AccLatLong", crLatTxt + "," + crLongTxt);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stopLocationUpdate();
                        Log.e("AccLatLongStop", "stop for loc");
                    }
                }, 1 * 200);


            } else {
                Log.e("AccLatLong", "Loc Null");
            }

        }
    };

    @SuppressLint("SetTextI18n")
    private void updateTextLabels() {
        String selectedLanguage = getSelectedLanguage();

        farmer_code = findViewById(R.id.farmer_code1);
        txt_Personal_Details = findViewById(R.id.txt_Personal_Details);
        title1 = findViewById(R.id.title1);
        firstname1 = findViewById(R.id.firstname1);
        lastname1 = findViewById(R.id.lastname1);
        fathername1 = findViewById(R.id.fathername1);
        age1 = findViewById(R.id.age1);
        Gender1 = findViewById(R.id.Gender1);
        addressdetails1 = findViewById(R.id.addressdetails1);
        country1 = findViewById(R.id.country1);
        state1 = findViewById(R.id.state1);
        district1 = findViewById(R.id.district1);
        subdistrict1 = findViewById(R.id.subdistrict1);
        village1 = findViewById(R.id.village1);
        pincode1 = findViewById(R.id.pincode1);
        address1 = findViewById(R.id.address1);
        noofplots1 = findViewById(R.id.noofplots1);
        contactdetails1 = findViewById(R.id.contactdetails1);
        primary1 = findViewById(R.id.primary1);
        secondary1 = findViewById(R.id.secondary1);
        identitydetails1 = findViewById(R.id.identitydetails1);
        national1 = findViewById(R.id.national1);
        nationalcode1 = findViewById(R.id.nationalcode1);
        txt_save_Button = findViewById(R.id.txt_save_Button);

        //Todo : For dynamic
        String hdFarmercode = getResources().getString(R.string.farmer_code);
        String hdPersonal = getResources().getString(R.string.personal_details);
        String hdTitle = getResources().getString(R.string.title);
        String hdFirstName = getResources().getString(R.string.first_name);
        String hdLastName = getResources().getString(R.string.last_name);
        String hdFatherName = getResources().getString(R.string.father_name);
        String hdAge = getResources().getString(R.string.age);
        String hdGender = getResources().getString(R.string.gender);
        String hdAddressDetails = getResources().getString(R.string.address_details);
        String hdCountry = getResources().getString(R.string.country);
        String hdState = getResources().getString(R.string.state_province);
        String hdDistrict = getResources().getString(R.string.district);
        String hdSubDistrict = getResources().getString(R.string.sub_district);
        String hdVillage = getResources().getString(R.string.village);
        String hdPincode = getResources().getString(R.string.pincode);
        String hdAddress = getResources().getString(R.string.address);
        String hdNoOfPlot = getResources().getString(R.string.num_plots);
        String hdContactDetails = getResources().getString(R.string.contact_details);
        String hdPrimaryContact = getResources().getString(R.string.primary_number);
        String hdSecondaryNo = getResources().getString(R.string.secondary_number);
        String hdIdentityDetails = getResources().getString(R.string.identity_details);
        String hdNationalId = getResources().getString(R.string.national_identity_code);
        String hdNationalIdImage = getResources().getString(R.string.national_identity_image);
        String hdSave = getResources().getString(R.string.save);

        if (selectedLanguage.equals("English")) {
            //Default English
            farmer_code.setText(hdFarmercode);
            txt_Personal_Details.setText(hdPersonal);
            title1.setText(hdTitle);
            firstname1.setText(hdFirstName);
            lastname1.setText(hdLastName);
            fathername1.setText(hdFatherName);
            age1.setText(hdAge);
            Gender1.setText(hdGender);
            addressdetails1.setText(hdAddressDetails);
            country1.setText(hdCountry);
            state1.setText(hdState);
            district1.setText(hdDistrict);
            subdistrict1.setText(hdSubDistrict);
            village1.setText(hdVillage);
            pincode1.setText(hdPincode);
            address1.setText(hdAddress);
            noofplots1.setText(hdNoOfPlot);
            contactdetails1.setText(hdContactDetails);
            primary1.setText(hdPrimaryContact);
            secondary1.setText(hdSecondaryNo);
            identitydetails1.setText(hdIdentityDetails);
            national1.setText(hdNationalId);
            nationalcode1.setText(hdNationalIdImage);
            txt_save_Button.setText(hdSave);
        } else {
            //Other Languages
            farmer_code.setText(getLanguageFromLocalDb(selectedLanguage, hdFarmercode) + "/" + hdFarmercode);
            txt_Personal_Details.setText(getLanguageFromLocalDb(selectedLanguage, hdPersonal) + "/" + hdPersonal);
            title1.setText(getLanguageFromLocalDb(selectedLanguage, hdTitle) + "/" + hdTitle);
            firstname1.setText(getLanguageFromLocalDb(selectedLanguage, hdFirstName) + "/" + hdFirstName);
            lastname1.setText(getLanguageFromLocalDb(selectedLanguage, hdLastName) + "/" + hdLastName);
            fathername1.setText(getLanguageFromLocalDb(selectedLanguage, hdFatherName) + "/" + hdFatherName);
            age1.setText(getLanguageFromLocalDb(selectedLanguage, hdAge) + "/" + hdAge);
            Gender1.setText(getLanguageFromLocalDb(selectedLanguage, hdGender) + "/" + hdGender);
            addressdetails1.setText(getLanguageFromLocalDb(selectedLanguage, hdAddressDetails) + "/" + hdAddressDetails);
            country1.setText(getLanguageFromLocalDb(selectedLanguage, hdCountry) + "/" + hdCountry);
            state1.setText(getLanguageFromLocalDb(selectedLanguage, hdState) + "/" + hdState);
            district1.setText(getLanguageFromLocalDb(selectedLanguage, hdDistrict) + "/" + hdDistrict);
            subdistrict1.setText(getLanguageFromLocalDb(selectedLanguage, hdSubDistrict) + "/" + hdSubDistrict);
            village1.setText(getLanguageFromLocalDb(selectedLanguage, hdVillage) + "/" + hdVillage);
            pincode1.setText(getLanguageFromLocalDb(selectedLanguage, hdPincode) + "/" + hdPincode);
            address1.setText(getLanguageFromLocalDb(selectedLanguage, hdAddress) + "/" + hdAddress);
            noofplots1.setText(getLanguageFromLocalDb(selectedLanguage, hdNoOfPlot) + "/" + hdNoOfPlot);
            contactdetails1.setText(getLanguageFromLocalDb(selectedLanguage, hdContactDetails) + "/" + hdContactDetails);
            primary1.setText(getLanguageFromLocalDb(selectedLanguage, hdPrimaryContact) + "/" + hdPrimaryContact);
            secondary1.setText(getLanguageFromLocalDb(selectedLanguage, hdSecondaryNo) + "/" + hdSecondaryNo);
            identitydetails1.setText(getLanguageFromLocalDb(selectedLanguage, hdIdentityDetails) + "/" + hdIdentityDetails);
            national1.setText(getLanguageFromLocalDb(selectedLanguage, hdNationalId) + "/" + hdNationalId);
            nationalcode1.setText(getLanguageFromLocalDb(selectedLanguage, hdNationalIdImage) + "/" + hdNationalIdImage);
            txt_save_Button.setText(getLanguageFromLocalDb(selectedLanguage, hdSave) + "/" + hdSave);
        }
    }

    private String getSelectedLanguage() {
        return preferences.getString("selected_language", "English");
    }

    private void initializeUI() {
        txtSaveButton = findViewById(R.id.txt_save_Button);
        imgCancel = findViewById(R.id.img_cancel_personal);
        txtFarmerCode = findViewById(R.id.et_farmer_code);
        spTitle = findViewById(R.id.sp_select_title);
        spGender = findViewById(R.id.sp_gender_selection);
        etPincode = findViewById(R.id.et_pin_code_add);
        spVillageName = findViewById(R.id.sp_village_name_add);
        etSubDistrict = findViewById(R.id.et_subdistrict_add);
        etDistrict = findViewById(R.id.et_district_name_add);
        etState = findViewById(R.id.et_state_name_add);
        etCountry = findViewById(R.id.et_country_add);
        etFirstName = findViewById(R.id.et_first_name_add);
        etLastName = findViewById(R.id.et_last_name_add);
        etFatherName = findViewById(R.id.et_father_name_add);
        etAge = findViewById(R.id.et_farmer_age);
        etPrimaryContact = findViewById(R.id.et_mobile_number_add);
        etAddress = findViewById(R.id.et_address_add);
        etNoOfPlot = findViewById(R.id.et_noofplot_add);
        etNationalIdentityCode = findViewById(R.id.et_NationalIdentityCode_add);
        imgFarmer = findViewById(R.id.img_picture_farmer);
        imgNationalIdentity = findViewById(R.id.et_NationalIdentityImg_add);

    }

    private void initializeValues() {

        //Location
        checkSettingsAndStartLocationUpdates();

        //For cancel button
        imgCancel.setOnClickListener(view -> {
            finish();
        });


        //Farmer Code
        long millis = 0;
        try {
            String myDate = appHelper.getCurrentDateTime(AppConstant.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
            android.icu.text.SimpleDateFormat sdf = new android.icu.text.SimpleDateFormat(AppConstant.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
            Date date = sdf.parse(myDate);
            millis = date.getTime();
//            Toast.makeText(AddFarmerActivity.this, millis+"", Toast.LENGTH_SHORT).show();
        } catch (Exception exception) {
//            Toast.makeText(AddFarmerActivity.this, exception.getMessage()+"", Toast.LENGTH_SHORT).show();
            exception.printStackTrace();
        }
        strFarmerCode = "F_" + millis + "_" + appHelper.getSharedPrefObj().getString(DeviceUserID, "");
        txtFarmerCode.setText(strFarmerCode);


        //For title
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(PersonalRegistrationActivity.this,
                android.R.layout.simple_spinner_item, titleArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spTitle.setAdapter(dataAdapter);
        spTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strFarmerTitle = parent.getItemAtPosition(position).toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Category
//        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(PersonalRegistrationActivity.this,
//                android.R.layout.simple_spinner_item, categArr);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
//        spCateg.setAdapter(dataAdapter3);
//        spCateg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////                strFarmerTitle = parent.getItemAtPosition(position).toString().trim();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


        //For Gender
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(PersonalRegistrationActivity.this,
                android.R.layout.simple_spinner_item, genderArr);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spGender.setAdapter(dataAdapter1);
        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                strFarmerGender = parent.getItemAtPosition(position).toString().trim();
                String st = (String) parent.getItemAtPosition(position);
                strFarmerGender = st;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Country spinner
//        getCountryListFromLocalDb();
        try {
            viewModel.getCountryDetailsListFromLocalDB();
            if (viewModel.getCountryDetailsByIdLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<Country> countryList = (List<Country>) o;
                        viewModel.getCountryDetailsByIdLiveData().removeObserver(this);
                        if (countryList != null && countryList.size() > 0) {
                            ArrayAdapter<Country> dataAdapterCont = new ArrayAdapter<>(PersonalRegistrationActivity.this,
                                    android.R.layout.simple_spinner_item, countryList);
                            dataAdapterCont.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            dataAdapterCont.notifyDataSetChanged();
                            etCountry.setAdapter(dataAdapterCont);

                            etCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    Country data = (Country) parent.getItemAtPosition(position);
                                    Log.e(TAG, String.valueOf(data.getId()));
                                    getStateListFromLocalDbById(data.getId());
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        } else {
                            Log.e(TAG, "no data");
                            List<Country> emptyList = new ArrayList<>();
                            ArrayAdapter<Country> dataAdapterCont1 = new ArrayAdapter<>(PersonalRegistrationActivity.this,
                                    android.R.layout.simple_spinner_item, emptyList);
                            dataAdapterCont1.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            etCountry.setAdapter(dataAdapterCont1);
                            dataAdapterCont1.notifyDataSetChanged();
                        }
                    }
                };
                viewModel.getCountryDetailsByIdLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, "null");
        }


        //Farmer Img
        imgFarmer.setOnClickListener(view -> {
            if (imageTakenFar == 0) {
                checkSettingsAndStartLocationUpdates();
//                openCameraPermission(true,0);
                pickImageDialog(0);
            } else if (imageTakenFar == 1) {
                previewImageDialog(strFarmerImageLocalImagePath);
//                Log.e(TAG, "for preview");
            }
        });


        //National doc img
        imgNationalIdentity.setOnClickListener(view -> {
            if (imgStatus1 == null) {
                Toast.makeText(PersonalRegistrationActivity.this, "Please take Image-1 first!!", Toast.LENGTH_SHORT).show();
            } else {
                if (imageTakenDoc == 0) {
                    checkSettingsAndStartLocationUpdates();
                    pickImageDialog(1);
//                    openCameraPermission(true,1);
                } else if (imageTakenDoc==1){
                    previewImageDialog(strNationalIdLocalImagePath);
                    Log.e(TAG, "for preview 2");
                }
            }
        });


        //Save button
        txtSaveButton.setOnClickListener(view -> {
            if (spTitle.getSelectedItemPosition() == 0) {
                Toast.makeText(this, "Select Title!!", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(etFirstName.getText().toString().trim())) {
                etFirstName.setError(ERROR_MESSAGE_ENTER_FIRSTNAME);
            } else if (TextUtils.isEmpty(etLastName.getText().toString().trim())) {
                etLastName.setError(ERROR_MESSAGE_ENTER_LASTNAME);
            } else if (TextUtils.isEmpty(etFatherName.getText().toString().trim())) {
                etFatherName.setError(ERROR_MESSAGE_ENTER_FATHERNAME);
            } else if (TextUtils.isEmpty(etAge.getText().toString().trim())) {
                etAge.setError("Enter Age");
            } else if (spGender.getSelectedItemPosition() == 0) {
                Toast.makeText(this, "Select Gender!!", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(etPincode.getText().toString().trim())) {
                etPincode.setError(ERROR_MESSAGE_ENTER_PINCODE);
            } else if (TextUtils.isEmpty(etAddress.getText().toString().trim())) {
                etAddress.setError(ERROR_MESSAGE_ENTER_ADDRESS);
            } else if (TextUtils.isEmpty(etNoOfPlot.getText().toString().trim())) {
                //|| !validateMobile(etPrimaryContact.getText().toString())
                etNoOfPlot.setError("please enter plot number");
            } else if (TextUtils.isEmpty(etPrimaryContact.getText().toString().trim())) {
                //|| !validateMobile(etPrimaryContact.getText().toString())
                etPrimaryContact.setError("please enter primary contact");
            } else if (strFarmerImageLocalImagePath.length() < 10 && bitmapFarmer == null) {
                Toast.makeText(this, "Take Farmer Image!!", Toast.LENGTH_SHORT).show();
            } else if (strNationalIdLocalImagePath.length() < 10 && bitmapNationalId == null) {
                Toast.makeText(this, "Take National Identity Image!!", Toast.LENGTH_SHORT).show();
            } else {
                String strMFarmerCode = strFarmerCode;
                String strFirstName = etFirstName.getText().toString().trim();
                String strLastName = etLastName.getText().toString().trim();
                String strFatherName = etFatherName.getText().toString().trim();
                String strPrimaryContact = etPrimaryContact.getText().toString().trim();
                String strNoOfPlots = etNoOfPlot.getText().toString().trim();
                String strAddress = etAddress.getText().toString().trim();
                String strAge = etAge.getText().toString().trim();
                String strVillageId = strVillage;
                String strNationalIdentityCode = etNationalIdentityCode.getText().toString().trim();
//                String strPinCode = etPincode.getText().toString().trim();
//                String strSubDistrict = etSubDistrict.getText().toString().trim();
//                String strDistrict = etDistrict.getText().toString().trim();
//                String strState = etState.getText().toString().trim();
//                String strCountry = etCountry.getText().toString().trim();

                FarmersTable farmersTable = new FarmersTable();
                farmersTable.setFarmerCode(strMFarmerCode);
                farmersTable.setFirstName(strFirstName);
                farmersTable.setLastName(strLastName);
                farmersTable.setFatherName(strFatherName);
                farmersTable.setPrimaryContactNo(strPrimaryContact);
                farmersTable.setNoOfPlots(strNoOfPlots);
                farmersTable.setAddress(strAddress);
                farmersTable.setAge(strAge);
                farmersTable.setGender(strFarmerGender);
                farmersTable.setVillageId(strVillageId);
                farmersTable.setNationalIdentityCode(strNationalIdentityCode);
                //image
                farmersTable.setNationalIdentityCodeDocument("null");//need to add
                farmersTable.setImage("null");//need to add

                farmersTable.setIsActive("1");
                farmersTable.setSync(false);
                farmersTable.setServerSync("0");
                farmersTable.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                farmersTable.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                String dateTime = appHelper.getCurrentDateTime(AppConstant.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
                farmersTable.setCreatedDate(dateTime);
                farmersTable.setUpdatedDate(dateTime);

                viewModel.insertFarmerDetailListTableLocal(farmersTable);

                //For Farmer doc
                PlantationDocuments farmerDocuments = new PlantationDocuments();
                farmerDocuments.setFarmerCode(strMFarmerCode);
                farmerDocuments.setPlotCode("");
                farmerDocuments.setDocURL(strFarmerImageLocalImagePath);
                farmerDocuments.setDocType("farmer image");
                farmerDocuments.setDocUrlValue("");
                farmerDocuments.setIsActive("true");
                farmerDocuments.setSync(false);
                farmerDocuments.setServerSync("0");
                farmerDocuments.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                farmerDocuments.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                farmerDocuments.setCreatedDate(dateTime);
                farmerDocuments.setUpdatedDate(dateTime);
                viewModel.insertDoctable(farmerDocuments);


                //For Primary Identity doc
                PlantationDocuments primaryIdDocuments = new PlantationDocuments();
                primaryIdDocuments.setFarmerCode(strMFarmerCode);
                primaryIdDocuments.setPlotCode("");
                primaryIdDocuments.setDocURL(strNationalIdLocalImagePath);
                primaryIdDocuments.setDocType("Primary identity");
                primaryIdDocuments.setDocUrlValue(strNationalIdentityCode);
                primaryIdDocuments.setIsActive("true");
                primaryIdDocuments.setSync(false);
                primaryIdDocuments.setServerSync("0");
                primaryIdDocuments.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                primaryIdDocuments.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                primaryIdDocuments.setCreatedDate(dateTime);
                primaryIdDocuments.setUpdatedDate(dateTime);
                viewModel.insertDoctable(primaryIdDocuments);

                successfulBack();
            }
        });
    }

    private void previewImageDialog(String  stImg) {

        imagePreviewDialog = new Dialog(PersonalRegistrationActivity.this, R.style.MyAlertDialogThemeNew);
        imagePreviewDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        imagePreviewDialog.setContentView(R.layout.preview_image_dialog);
        imagePreviewDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        imagePreviewDialog.setCanceledOnTouchOutside(false);
        imagePreviewDialog.setCancelable(true);

        TextView backPreview = imagePreviewDialog.findViewById(R.id.backPreview);
        ImageView imagePreview = imagePreviewDialog.findViewById(R.id.imagePreview);

        Log.e(TAG,"preview Img str:"+stImg);

//        Picasso.get()
//                .load(stImg)
//                .error(R.drawable.baseline_broken_image_24)
//                .into(imagePreview);

        Glide.with(this).load(stImg)
                        .error(R.drawable.baseline_broken_image_24)
                                .into(imagePreview);

        backPreview.setOnClickListener(v -> {
            imagePreviewDialog.dismiss();
        });

        imagePreviewDialog.show();
    }

    private void pickImageDialog(int i) {
        dialog = new Dialog(PersonalRegistrationActivity.this, R.style.MyAlertDialogThemeNew);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.take_image_dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);


        LinearLayout fromGallery = dialog.findViewById(R.id.ll_gallery);
        LinearLayout fromCamera = dialog.findViewById(R.id.ll_camera);

        fromGallery.setOnClickListener(v -> {
            dialog.dismiss();
            showFileChooser(i);
        });

        fromCamera.setOnClickListener(v -> {
            dialog.dismiss();
            openCameraPermission(true, i);
        });

        dialog.show();
    }

    private void successfulBack() {
        Toast.makeText(this, "Successful!!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DashBoardFarmerListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
//        finish();
    }

    private void getVillageListFromLocalDbById(String id) {

        try {
            viewModel.getVillageDetailsListFromLocalDB(id);
            if (viewModel.getvillageDetailsByPincodeLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<VillageTable> villageTableList = (List<VillageTable>) o;
                        viewModel.getvillageDetailsByPincodeLiveData().removeObserver(this);
                        if (villageTableList != null && villageTableList.size() > 0) {


                            ArrayAdapter<VillageTable> dataAdapter = new ArrayAdapter<>(PersonalRegistrationActivity.this,
                                    android.R.layout.simple_spinner_item, villageTableList);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            dataAdapter.notifyDataSetChanged();
                            spVillageName.setAdapter(dataAdapter);

                            spVillageName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    VillageTable data = (VillageTable) parent.getItemAtPosition(position);
                                    strVillage = String.valueOf(data.getId());
                                    etPincode.setText(data.getPinCode());

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        } else {
                            List<VillageTable> emptyList = new ArrayList<>();
                            ArrayAdapter<VillageTable> dataAdapter = new ArrayAdapter<>(PersonalRegistrationActivity.this,
                                    android.R.layout.simple_spinner_item, emptyList);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            spVillageName.setAdapter(dataAdapter);
                            dataAdapter.notifyDataSetChanged();
                            strVillage = null;

                        }
                    }
                };
                viewModel.getvillageDetailsByPincodeLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
//            Log.e("Villagelist","Null catch");
        }
    }

    private void getSubDistrictListFromLocalDbById(String id) {
        try {
            viewModel.getSubDistrictDetailsListFromLocalDB(id);
            if (viewModel.getSubDistrictDetailsByIdLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<SubDistrict> subDistrictList = (List<SubDistrict>) o;
                        viewModel.getSubDistrictDetailsByIdLiveData().removeObserver(this);
                        if (subDistrictList != null && subDistrictList.size() > 0) {
                            ArrayAdapter<SubDistrict> dataAdapterCont = new ArrayAdapter<>(PersonalRegistrationActivity.this,
                                    android.R.layout.simple_spinner_item, subDistrictList);
                            dataAdapterCont.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            dataAdapterCont.notifyDataSetChanged();
                            etSubDistrict.setAdapter(dataAdapterCont);

                            etSubDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    SubDistrict data = (SubDistrict) parent.getItemAtPosition(position);
                                    Log.e(TAG, String.valueOf(data.getId()));
                                    getVillageListFromLocalDbById(String.valueOf(data.getId()));
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        } else {
                            List<SubDistrict> emptyList = new ArrayList<>();
                            ArrayAdapter<SubDistrict> dataAdapterCont1 = new ArrayAdapter<>(PersonalRegistrationActivity.this,
                                    android.R.layout.simple_spinner_item, emptyList);
                            dataAdapterCont1.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            etSubDistrict.setAdapter(dataAdapterCont1);
                            dataAdapterCont1.notifyDataSetChanged();
                            getVillageListFromLocalDbById("99999");
                        }
                    }
                };
                viewModel.getSubDistrictDetailsByIdLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
//            Log.e("Villagelist","Null catch");
        }
    }

    private void getDistrictListFromLocalDbById(int id) {
        try {
            viewModel.getDistrictDetailsListFromLocalDB(id);
            if (viewModel.getDistrictDetailsByIdLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<DistrictorRegency> districtorRegencies = (List<DistrictorRegency>) o;
                        viewModel.getDistrictDetailsByIdLiveData().removeObserver(this);
                        if (districtorRegencies != null && districtorRegencies.size() > 0) {
                            ArrayAdapter<DistrictorRegency> dataAdapter = new ArrayAdapter<>(PersonalRegistrationActivity.this,
                                    android.R.layout.simple_spinner_item, districtorRegencies);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            dataAdapter.notifyDataSetChanged();
                            etDistrict.setAdapter(dataAdapter);

                            etDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    DistrictorRegency data = (DistrictorRegency) parent.getItemAtPosition(position);
                                    Log.e(TAG, String.valueOf(data.getId()));
                                    getSubDistrictListFromLocalDbById(String.valueOf(data.getId()));
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        } else {
                            List<DistrictorRegency> emptyList = new ArrayList<>();
                            ArrayAdapter<DistrictorRegency> dataAdapter = new ArrayAdapter<>(PersonalRegistrationActivity.this,
                                    android.R.layout.simple_spinner_item, emptyList);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            etDistrict.setAdapter(dataAdapter);
                            dataAdapter.notifyDataSetChanged();
                            getSubDistrictListFromLocalDbById("9999");
                        }
                    }
                };
                viewModel.getDistrictDetailsByIdLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
//            Log.e("Villagelist","Null catch");
        }
    }

    private void getStateListFromLocalDbById(int id) {
        try {
            viewModel.getStateorProvinceDetailsListFromLocalDB(id);
            if (viewModel.getStateOrProvinceDetailsByIdLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<StateorProvince> stateorProvinces = (List<StateorProvince>) o;
                        viewModel.getStateOrProvinceDetailsByIdLiveData().removeObserver(this);
                        if (stateorProvinces != null && stateorProvinces.size() > 0) {
                            ArrayAdapter<StateorProvince> dataAdapter = new ArrayAdapter<>(PersonalRegistrationActivity.this,
                                    android.R.layout.simple_spinner_item, stateorProvinces);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            dataAdapter.notifyDataSetChanged();
                            etState.setAdapter(dataAdapter);

                            etState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    StateorProvince data = (StateorProvince) parent.getItemAtPosition(position);
                                    Log.e(TAG, String.valueOf(data.getStateId()));
                                    getDistrictListFromLocalDbById(data.getId());
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        } else {
                            List<StateorProvince> emptyList = new ArrayList<>();
                            ArrayAdapter<StateorProvince> dataAdapter = new ArrayAdapter<>(PersonalRegistrationActivity.this,
                                    android.R.layout.simple_spinner_item, emptyList);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            etState.setAdapter(dataAdapter);
                            dataAdapter.notifyDataSetChanged();
                            getDistrictListFromLocalDbById(99999);
                        }
                    }
                };
                viewModel.getStateOrProvinceDetailsByIdLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    private void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return null;
    }

    private void openCameraPermission(Boolean land, Integer pictureCount) {
        testPictureCount = pictureCount;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (!CommonUtils.isPermissionAllowed(PersonalRegistrationActivity.this, Manifest.permission.CAMERA))) {
            android.util.Log.v(TAG, "Location Permissions Not Granted");
            ActivityCompat.requestPermissions(
                    PersonalRegistrationActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_CAM_PERMISSIONS
            );
        } else {

            firstDispatchTakePictureIntent(CAMERA_REQUEST, land);


        }
    }

    private void firstDispatchTakePictureIntent(int actionCode, Boolean land) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        switch (actionCode) {
            case CAMERA_REQUEST:
                if (testPictureCount == 0) {
                    f = null;
                    strFarmerImageLocalImagePath = null;
                    try {
                        f = setUpPhotoFile();

                        strFarmerImageLocalImagePath = f.getAbsolutePath();

                        strFileExtension1 = f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf("."));


                        Uri photoURI = FileProvider.getUriForFile(PersonalRegistrationActivity.this,
                                BuildConfig.APPLICATION_ID + ".provider",
                                f);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    } catch (IOException e) {
                        e.printStackTrace();
                        f = null;
                        strFarmerImageLocalImagePath = null;
                    }
                } else if (testPictureCount == 1) {
                    f = null;
                    strNationalIdLocalImagePath = null;
                    try {
                        f = setUpPhotoFile();

                        strNationalIdLocalImagePath = f.getAbsolutePath();

                        strFileExtension2 = f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf("."));


                        Uri photoURI = FileProvider.getUriForFile(PersonalRegistrationActivity.this,
                                BuildConfig.APPLICATION_ID + ".provider",
                                f);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    } catch (IOException e) {
                        e.printStackTrace();
                        f = null;
                        strNationalIdLocalImagePath = null;
                    }
                }

                break;

            default:
                break;
        } // switch
        startActivityForResult(takePictureIntent, actionCode);

    }


    private File setUpPhotoFile() throws IOException {

        File f;
        f = createImageFileFirst();

        if (testPictureCount == 0) {
            strFarmerImageLocalImagePath = f.getAbsolutePath();
        } else if (testPictureCount == 1) {
            strNationalIdLocalImagePath = f.getAbsolutePath();
        }
        return f;
    }

    private File createImageFileFirst() {
        File image = null;
        File mediaStorageDir = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mediaStorageDir = new File(PersonalRegistrationActivity.this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "Chain_Personal");
        } else
            mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "Chain_Personal");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("App", "failed to create directory");
            }
        }
//            File image = null;
        try {
            image = File.createTempFile("imageFiles", ".jpg", mediaStorageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
//
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUEST:
                if (resultCode == RESULT_OK) {
                    try {
                        if (testPictureCount == 0) {//for strWaterCycleLocalPath
//                            imgFarmer.setClickable(false);
                            imgStatus1 = "1";//for second img
                            imageTakenFar = 1;//Taken status
                            pickHandle1 = 1; //by camera
                            handleBigCameraPhoto();
                        } else if (testPictureCount == 1) {
                            imageTakenDoc = 1;//Taken status
                            pickHandle2 = 1;//by camera
//                            imgNationalIdentity.setClickable(false);
                            handleBigCameraPhoto();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
//                    strImageOnePath = null;
//                    strImageOnePath = null;
                }
                break;

            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    try {
                        if (testPictureCount == 0) {//for strWaterCycleLocalPath
//                            imgFarmer.setClickable(false);
                            imageTakenFar = 1;
                            imgStatus1 = "1";
                            Uri uri = data.getData();
//                            String realUriSt = getRealPathFromUri(uri);
//                            strFarmerImageLocalImagePath = realUriSt;//String.valueOf(uri);
//                            strFarmerRUri=realUriSt;
//                            handleBigCameraPhoto();
                            File IMAGE_COPY_PATH=createImageFileFirst();
                            try {
                                InputStream inputStream = getContentResolver().openInputStream(uri);//Uri.parse(realUriSt)
                                OutputStream outputStream = new FileOutputStream(IMAGE_COPY_PATH);
                                copyFile(inputStream, outputStream);
                                strFarmerImageLocalImagePath=IMAGE_COPY_PATH.getAbsolutePath();
                                inputStream.close();
                                outputStream.close();
                                handleBigCameraPhoto();
//                                Toast.makeText(this, "Image copied successfully", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
//                                Toast.makeText(this, "Error copying image", Toast.LENGTH_SHORT).show();
                            }


                        } else if (testPictureCount == 1) {
//                            imgNationalIdentity.setClickable(false);
                            imageTakenDoc = 1;//Taken status
                            Uri uri = data.getData();
//                            String realUriSt = getRealPathFromUri(uri);
//                            strNationalIdLocalImagePath = realUriSt;
//                            handleBigCameraPhoto();
                            File IMAGE_COPY_PATH=createImageFileFirst();
                            try {
                                InputStream inputStream = getContentResolver().openInputStream(uri);//Uri.parse(realUriSt)
                                OutputStream outputStream = new FileOutputStream(IMAGE_COPY_PATH);
                                copyFile(inputStream, outputStream);
                                strNationalIdLocalImagePath=IMAGE_COPY_PATH.getAbsolutePath();
                                inputStream.close();
                                outputStream.close();
                                handleBigCameraPhoto();
//                                Toast.makeText(this, "Image copied successfully", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
//                                Toast.makeText(this, "Error copying image", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        } // switch need to add
    }

    private void copyFile(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
    }

    private void showFileChooser(int i) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, FILE_SELECT_CODE);
    }

    public String getRealPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }
        return uri.getPath(); // If cursor is null, fall back to the original Uri path
    }


    private void handleBigCameraPhoto() throws Exception {

        if (testPictureCount == 0) {

            if (strFarmerImageLocalImagePath != null) {
                setPic();
//                galleryAddPic();
            }
        } else if (testPictureCount == 1) {

            if (strNationalIdLocalImagePath != null) {
                setPic();
//                galleryAddPic();
            }
        }
    }

    private void setPic() throws Exception {

        /* There isn't enough memory to open up more than a couple camera photos */
        /* So pre-scale the target bitmap into which the file is decoded */

        /* Get the size of the ImageView */

        if (testPictureCount == 0) {
            int targetW = imgFarmer.getWidth();
            int targetH = imgFarmer.getHeight();

            /* Get the size of the image */
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(strFarmerImageLocalImagePath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            /* Figure out which way needs to be reduced less */
            int scaleFactor = 1;
            if ((targetW > 0) || (targetH > 0)) {
                scaleFactor = Math.min(photoW / targetW, photoH / targetH);
            }

            /* Set bitmap options to scale the image decode target */
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            bitmapFarmer = BitmapFactory.decodeFile(strFarmerImageLocalImagePath, bmOptions);

            getBytesFromBitmap(bitmapFarmer);

            ExifInterface ei = new ExifInterface(strFarmerImageLocalImagePath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            /* Decode the JPEG file into a Bitmap */

            Bitmap rotatedBitmap = null;
            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmapFarmer, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmapFarmer, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmapFarmer, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmapFarmer;
            }

            if (pickHandle1 == 1) {
                bitmapFarmer = addDateAndLocationToImage1(rotatedBitmap, crLatTxt, crLongTxt);
                saveNewImage(bitmapFarmer, strFarmerImageLocalImagePath);
                imgFarmer.setImageBitmap(bitmapFarmer);
                imgFarmer.invalidate();
                testPictureCount = 1;//increment image
            } else if (pickHandle1 == 0) {//picker
                bitmapFarmer = addDateAndLocationToImage1(rotatedBitmap, crLatTxt, crLongTxt);
//                saveCopyOfImage(bitmapFarmer,strFarmerRUri);
                saveNewImage(bitmapFarmer, strFarmerImageLocalImagePath);
                imgFarmer.setImageBitmap(bitmapFarmer);
                imgFarmer.invalidate();
                testPictureCount = 1;//increment image
            }
            /* Decode the JPEG file into a Bitmap */
//        bitmapLand = BitmapFactory.decodeFile(strLandImagePath, bmOptions);
//        getBytesFromBitmap(bitmapLand);
//        bitmapLand = ImageUtility.rotatePicture(90, bitmapLand);
//

            /* There isn't enough memory to open up more than a couple camera photos */
            /* So pre-scale the target bitmap into which the file is decoded */

            /* Get the size of the ImageView */
        } else if (testPictureCount == 1) {
            int targetW = imgNationalIdentity.getWidth();
            int targetH = imgNationalIdentity.getHeight();

            /* Get the size of the image */
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(strNationalIdLocalImagePath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            /* Figure out which way needs to be reduced less */
            int scaleFactor = 1;
            if ((targetW > 0) || (targetH > 0)) {
                scaleFactor = Math.min(photoW / targetW, photoH / targetH);
            }

            /* Set bitmap options to scale the image decode target */
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            bitmapNationalId = BitmapFactory.decodeFile(strNationalIdLocalImagePath, bmOptions);

            getBytesFromBitmap(bitmapNationalId);

            ExifInterface ei = new ExifInterface(strNationalIdLocalImagePath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            /* Decode the JPEG file into a Bitmap */

            Bitmap rotatedBitmap = null;
            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmapNationalId, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmapNationalId, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmapNationalId, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmapNationalId;
            }
            if (pickHandle2 == 1) {
                bitmapNationalId = addDateAndLocationToImage(rotatedBitmap, crLatTxt, crLongTxt);
                saveNewImage1(bitmapNationalId, strNationalIdLocalImagePath);
                imgNationalIdentity.setImageBitmap(bitmapNationalId);
                imgNationalIdentity.invalidate();
            } else if (pickHandle2 == 0) {
                bitmapNationalId = addDateAndLocationToImage2(rotatedBitmap, crLatTxt, crLongTxt);
                saveNewImage1(bitmapNationalId, strNationalIdLocalImagePath);
                imgNationalIdentity.setImageBitmap(bitmapNationalId);
                imgNationalIdentity.invalidate();
            }

            /* Decode the JPEG file into a Bitmap */
//        bitmapLand = BitmapFactory.decodeFile(strLandImagePath, bmOptions);
//        getBytesFromBitmap(bitmapLand);
//        bitmapLand = ImageUtility.rotatePicture(90, bitmapLand);
//

            /* There isn't enough memory to open up more than a couple camera photos */
            /* So pre-scale the target bitmap into which the file is decoded */

            /* Get the size of the ImageView */
        }
    }

    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private void galleryAddPic() {
        if (testPictureCount == 0) {
            Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            File f;
            f = new File(strFileExtension1);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            sendBroadcast(mediaScanIntent);
        } else if (testPictureCount == 1) {
            Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            File f;
            f = new File(strNationalIdLocalImagePath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            sendBroadcast(mediaScanIntent);
        }
    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        bytes = stream.toByteArray();
        return stream.toByteArray();
    }

    private void getLocationDetails() {
        try {
            BoundLocationManager.getInstance(PersonalRegistrationActivity.this).observe(this, new Observer<Location>() {
                @Override
                public void onChanged(@Nullable Location location) {
                    if (location != null) {

                        crLatTxt = String.valueOf(location.getLatitude());
                        crLongTxt = String.valueOf(location.getLongitude());

                    } else {
                        Toast.makeText(PersonalRegistrationActivity.this, "Location is null", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    //National Id
    public Bitmap addDateAndLocationToImage(Bitmap imageBitmap, String lat, String longi) {
        Bitmap bitmapWithDateAndLocation = imageBitmap.copy(imageBitmap.getConfig(), true);

        Canvas canvas = new Canvas(bitmapWithDateAndLocation);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(36);//30

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDate = dateFormat.format(new Date());
        String latitude = lat;
        String longitude = longi;


        String txtLat = "Latitude: " + latitude;

        String txtLongi = "Longitude: " + longitude;

        String txtDate = "Date: " + currentDate;
//                + ", Longitude: " + longitude;

        String txtFarmer = "Farmer Code: " + strFarmerCode;

        int x2 = 20; // X-coordinate (left)
        int y2 = bitmapWithDateAndLocation.getHeight() - 35;//lat35=long

        int x1 = 20; // X-coordinate (left)
        int y1 = bitmapWithDateAndLocation.getHeight() - 78;//date78=lat

        int x3 = 20; // X-coordinate (left)
        int y3 = bitmapWithDateAndLocation.getHeight() - 122;//farm122=date

        int x4 = 20; // X-coordinate (left)
        int y4 = bitmapWithDateAndLocation.getHeight() - 160;//=fcode
//
        canvas.drawText(txtLat, x1, y1, paint);
        canvas.drawText(txtLongi, x2, y2, paint);
        canvas.drawText(txtDate, x3, y3, paint);
        canvas.drawText(txtFarmer, x4, y4, paint);

//        Bitmap overlayBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.sow);//145
//        Bitmap resizedBitmap=Bitmap.createScaledBitmap(overlayBitmap,50,50,true);
//        canvas.drawBitmap(resizedBitmap, x3, y3, null);

        return bitmapWithDateAndLocation;
    }

    public Bitmap addDateAndLocationToImage1(Bitmap imageBitmap, String lat, String longi) {
        Bitmap bitmapWithDateAndLocation = imageBitmap.copy(imageBitmap.getConfig(), true);

        Canvas canvas = new Canvas(bitmapWithDateAndLocation);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(15);//30

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDate = dateFormat.format(new Date());
        String latitude = lat;
        String longitude = longi;


        String txtLat = "Latitude: " + latitude;

        String txtLongi = "Longitude: " + longitude;

        String txtDate = "Date: " + currentDate;
//                + ", Longitude: " + longitude;

        String txtFarmer = "Farmer Code: " + strFarmerCode;

        int x2 = 10; // X-coordinate (left)
        int y2 = bitmapWithDateAndLocation.getHeight() - 20;//long

        int x1 = 10; // X-coordinate (left)
        int y1 = bitmapWithDateAndLocation.getHeight() - 40;//date78=lat

        int x3 = 10; // X-coordinate (left)
        int y3 = bitmapWithDateAndLocation.getHeight() - 60;//farm122=date

        int x4 = 10; // X-coordinate (left)
        int y4 = bitmapWithDateAndLocation.getHeight() - 80;//=fcode
//
        canvas.drawText(txtLat, x1, y1, paint);
        canvas.drawText(txtLongi, x2, y2, paint);
        canvas.drawText(txtDate, x3, y3, paint);
        canvas.drawText(txtFarmer, x4, y4, paint);

//        Bitmap overlayBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.sow);//145
//        Bitmap resizedBitmap=Bitmap.createScaledBitmap(overlayBitmap,50,50,true);
//        canvas.drawBitmap(resizedBitmap, x3, y3, null);

        return bitmapWithDateAndLocation;
    }

    public Bitmap addDateAndLocationToImage2(Bitmap imageBitmap, String lat, String longi) {
        Bitmap bitmapWithDateAndLocation = imageBitmap.copy(imageBitmap.getConfig(), true);

        Canvas canvas = new Canvas(bitmapWithDateAndLocation);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(11);//30

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDate = dateFormat.format(new Date());
        String latitude = lat;
        String longitude = longi;


        String txtLat = "Latitude: " + latitude;

        String txtLongi = "Longitude: " + longitude;

        String txtDate = "Date: " + currentDate;
//                + ", Longitude: " + longitude;

        String txtFarmer = "Farmer Code: " + strFarmerCode;

        int x2 = 10; // X-coordinate (left)
        int y2 = bitmapWithDateAndLocation.getHeight() - 20;//long

        int x1 = 10; // X-coordinate (left)
        int y1 = bitmapWithDateAndLocation.getHeight() - 35;//date78=lat

        int x3 = 10; // X-coordinate (left)
        int y3 = bitmapWithDateAndLocation.getHeight() - 55;//farm122=date

        int x4 = 10; // X-coordinate (left)
        int y4 = bitmapWithDateAndLocation.getHeight() - 75;//=fcode
//
        canvas.drawText(txtLat, x1, y1, paint);
        canvas.drawText(txtLongi, x2, y2, paint);
        canvas.drawText(txtDate, x3, y3, paint);
        canvas.drawText(txtFarmer, x4, y4, paint);

//        Bitmap overlayBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.sow);//145
//        Bitmap resizedBitmap=Bitmap.createScaledBitmap(overlayBitmap,50,50,true);
//        canvas.drawBitmap(resizedBitmap, x3, y3, null);

        return bitmapWithDateAndLocation;
    }


    //New replace image
    private void saveNewImage(Bitmap existingBitmap, String imagePath) {
        try {
            FileOutputStream outputStream = new FileOutputStream(imagePath);
            existingBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveNewImage1(Bitmap existingBitmap, String imagePath) {
        try {
            FileOutputStream outputStream = new FileOutputStream(imagePath);
            existingBitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private void saveCopyOfImage(Bitmap existingBitmap, String originalImagePath) {
//        try {
//            // Extract the file extension from the original image path (e.g., ".jpg")
//            String fileExtension = originalImagePath.substring(originalImagePath.lastIndexOf("."));
//
//            // Generate a unique identifier (e.g., timestamp)
//            String uniqueIdentifier = String.valueOf(System.currentTimeMillis());
//
//            // Create a new filename by combining the original filename, unique identifier, and file extension
//            String newImagePath = originalImagePath.replace(fileExtension, "_" + uniqueIdentifier + fileExtension);
//
//            // Create a new file with the generated filename
//            FileOutputStream outputStream = new FileOutputStream(newImagePath);
//

//            strFarmerImageLocalImagePath=newImagePath;//Todo test
//
//            // Compress and save the bitmap to the new file
//            existingBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//            outputStream.flush();
//            outputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    //New
    private void saveCopyOfImage(Bitmap existingBitmap, String existingImagePath) {
        try {
            // Extract the file name and extension from the existingImagePath
            File existingFile = new File(existingImagePath);
            String fileNameWithExtension = existingFile.getName();
            String fileName = fileNameWithExtension.substring(0, fileNameWithExtension.lastIndexOf("."));
            String fileExtension = fileNameWithExtension.substring(fileNameWithExtension.lastIndexOf("."));

            // Create a new file name for the copy
            String copyFileName = fileName + "_copy" + fileExtension;

            // Construct the path for the copy
            String copyImagePath = existingFile.getParent() + File.separator + copyFileName;

            strFarmerImageLocalImagePath=copyImagePath;//todo test
            Log.e(TAG,"copy"+copyImagePath.toString()+" "+strFarmerImageLocalImagePath);

            FileOutputStream outputStream = new FileOutputStream(copyImagePath);
            existingBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Current loc
    private void checkSettingsAndStartLocationUpdates() {
        LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build();
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);

        Task<LocationSettingsResponse> locationSettingsResponseTask = settingsClient.checkLocationSettings(request);
        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startLocationUpdate();
            }
        });

        locationSettingsResponseTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                    try {
                        resolvableApiException.startResolutionForResult(PersonalRegistrationActivity.this, 2000001);
                    } catch (IntentSender.SendIntentException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }

    private void startLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        client.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdate() {
        client.removeLocationUpdates(locationCallback);

    }

    public String getLanguageFromLocalDb(String stLanguage, String stWord) {

        try {
            if (viewModel.getLanguageDataVM(stLanguage, stWord)!=null){
                return viewModel.getLanguageDataVM(stLanguage, stWord);
            } else{
                return stWord;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return stWord;
        }

    }

}