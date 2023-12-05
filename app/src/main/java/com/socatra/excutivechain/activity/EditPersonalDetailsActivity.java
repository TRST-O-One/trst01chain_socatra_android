package com.socatra.excutivechain.activity;

import static com.socatra.excutivechain.AppConstant.DeviceUserID;
import static com.socatra.excutivechain.AppConstant.ERROR_MESSAGE_ENTER_ADDRESS;
import static com.socatra.excutivechain.AppConstant.ERROR_MESSAGE_ENTER_PINCODE;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.socatra.excutivechain.AppConstant;
import com.socatra.excutivechain.BaseActivity;
import com.socatra.excutivechain.R;
import com.socatra.excutivechain.database.entity.Country;
import com.socatra.excutivechain.database.entity.DistrictorRegency;
import com.socatra.excutivechain.database.entity.FarmersTable;
import com.socatra.excutivechain.database.entity.PlantationDocuments;
import com.socatra.excutivechain.database.entity.StateorProvince;
import com.socatra.excutivechain.database.entity.SubDistrict;
import com.socatra.excutivechain.database.entity.VillageTable;
import com.socatra.excutivechain.helper.CircleImageView;
import com.socatra.excutivechain.view_models.AppViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class EditPersonalDetailsActivity extends BaseActivity implements HasSupportFragmentInjector {

    String TAG = "EditPersonalDetailsActivity";
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

    Spinner spTitle, spGender;

    String strFarmerCode, strFarmerTitle, strFarmerGender, strVillage;

    EditText etPincode;

    EditText etFirstName, etLastName, etFatherName, etAge, etPrimaryContact, etAddress, etNoOfPlot, etNationalIdentityCode;

    String farmerCode = "";
    int mId;
    String strMFarmerCode, strFirstName, strLastName, strFatherName, strPrimaryContact, strNoOfPlots, strAddress, strAge, strSaveVID, strNationalIdentityCode;

    String strSelectDistricId, strSubDistricId, strGetVillageId;
    FarmersTable farmersTable;

    int genderPos = 0;

    CircleImageView imgFarmer;

    ImageView imgNationalIdentity;

    String stImgNat = "", stImgFar = "";
    private SharedPreferences preferences;


    Integer strGetVillageIdPos, strGetDistricIdPos, strGetSubDistricIdPos, strGetRegionPosId, strGetCountryPosId;

    String strGetCountryName;

    AutoCompleteTextView at_cmpt_txt_sp;

    ArrayList<String> array_countryList = new ArrayList<>();
    ArrayList<Integer> array_countryIds = new ArrayList<>();

    TextView txtTitle;

    Spinner spSelectSubDistrict, spSelectDistrict, spSelectState, spSelectCountry, spSelectVillageName;
    EditText edtVillageName, edtDistricName, edtSubDistricName, edtGender, edtCountryName, edtStateName;


    String strStaticKeyForEditAdd;
    ImageView imgSaveAddBt, imgEditBt, imgCancelEditBt;
    Dialog dialog, imagePreviewDialog;

    String test1 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_details);

        farmerCode = getIntent().getStringExtra("mFarmerCode");
        farmersTable = (FarmersTable) getIntent().getSerializableExtra("farmData");
        strGetVillageId = getIntent().getStringExtra("villageId");
        strStaticKeyForEditAdd = "default";

        Log.e(TAG, farmerCode);
        Log.e(TAG, farmersTable.toString());
        Log.d(TAG, "onCreate: villageId" + strGetVillageId);

        initializeUI();
        configureDagger();
        configureViewModel();
        getSubDisIDFromVillageIdFromLocalDb(strGetVillageId);
        initializeValues();
        getCountryDetailsBySpinner();
        spinnerClicks();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        updateTextLabels();
        saveData();

    }

    private void saveData() {
        txtSaveButton.setOnClickListener(view -> {

            //if (spTitle.getSelectedItemPosition() == 0) {
            //                Toast.makeText(this, "Select Title!!", Toast.LENGTH_SHORT).show();
            //            } else

//            if (TextUtils.isEmpty(etFirstName.getText().toString().trim())) {
//                etFirstName.setError(ERROR_MESSAGE_ENTER_FIRSTNAME);
//            } else if (TextUtils.isEmpty(etLastName.getText().toString().trim())) {
//                etLastName.setError(ERROR_MESSAGE_ENTER_LASTNAME);
//            } else if (TextUtils.isEmpty(etFatherName.getText().toString().trim())) {
//                etFatherName.setError(ERROR_MESSAGE_ENTER_FATHERNAME);
//            } else


//                else if (spGender.getSelectedItemPosition() == 0) {
//                Toast.makeText(this, "Select Gender!!", Toast.LENGTH_SHORT).show();
//            }
            if (TextUtils.isEmpty(etAge.getText().toString().trim())) {
                etAge.setError("Enter Age");
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
            } else {
                strMFarmerCode = strFarmerCode;
                strFirstName = etFirstName.getText().toString().trim();
                strLastName = etLastName.getText().toString().trim();
                strFatherName = etFatherName.getText().toString().trim();
                strPrimaryContact = etPrimaryContact.getText().toString().trim();
                strNoOfPlots = etNoOfPlot.getText().toString().trim();
                strAddress = etAddress.getText().toString().trim();
                strAge = etAge.getText().toString().trim();
                //  strVillageId = strVillage;
                //strVillageId = strVillage
                strNationalIdentityCode = etNationalIdentityCode.getText().toString().trim();

//                String strPinCode = etPincode.getText().toString().trim();
//                String strSubDistrict = spSelectSubDistrict.getText().toString().trim();
//                String strDistrict = spSelectDistrict.getText().toString().trim();
//                String strState = spSelectState.getText().toString().trim();
//                String strCountry = etCountry.getText().toString().trim();

                if (!strStaticKeyForEditAdd.equalsIgnoreCase("save")) {
                    strSaveVID = strGetVillageId;
                }

                FarmersTable farmersTable = new FarmersTable();
                farmersTable.setFarmerId(mId);
                farmersTable.setFarmerCode(strMFarmerCode);
                farmersTable.setFirstName(strFirstName);
                farmersTable.setLastName(strLastName);
                farmersTable.setFatherName(strFatherName);
                farmersTable.setPrimaryContactNo(strPrimaryContact);
                farmersTable.setNoOfPlots(strNoOfPlots);
                farmersTable.setAddress(strAddress);
                farmersTable.setAge(strAge);
                farmersTable.setGender(edtGender.getText().toString());
//                farmersTable.setDistricId(strSelectDistricId);
//                farmersTable.setSubDistricId(strSubDistricId);
                farmersTable.setVillageId(strSaveVID);
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
                successfulBack();
            }

            //else if (strLocalFarmerImagePath.equals("null")) {
            //                appHelper.getDialogHelper().getConfirmationDialog().show(ConfirmationDialog.ALERT, "select farmer picture");
            //            }
        });
    }

    private void spinnerClicks() {

    }

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


        // TODO: 9/18/2023 select spinner
        spSelectSubDistrict = findViewById(R.id.et_subdistrict_add);
        spSelectDistrict = findViewById(R.id.et_district_name_add);
        spSelectState = findViewById(R.id.et_state_name_add);
        spSelectCountry = findViewById(R.id.et_country_add);
        spSelectVillageName = findViewById(R.id.sp_village_name_add);
        // TODO: 9/14/2023 edit text of address


        edtVillageName = findViewById(R.id.et_village_name_edit);
        edtDistricName = findViewById(R.id.et_district_name_edit);
        edtSubDistricName = findViewById(R.id.et_sub_district_name_edit);
        edtCountryName = findViewById(R.id.et_country_name);
        edtStateName = findViewById(R.id.et_state_name);

        at_cmpt_txt_sp = findViewById(R.id.auto_complete_txt_sp);
        txtTitle = findViewById(R.id.txt_title);
        edtGender = findViewById(R.id.et_gender);


        // TODO: 9/18/2023 image  save edit part
        imgEditBt = findViewById(R.id.img_add_edit);
        imgSaveAddBt = findViewById(R.id.img_save);
        imgCancelEditBt = findViewById(R.id.img_cancel_add);
    }

    private void initializeValues() {

        imgFarmer.setOnClickListener(v -> {
            previewImageDialog(stImgFar);
        });

        imgNationalIdentity.setOnClickListener(v -> {
            previewImageDialog(stImgNat);
        });


        //For cancel button
        imgCancel.setOnClickListener(view -> {
            Intent intent = new Intent(EditPersonalDetailsActivity.this, DashBoardFarmerListActivity.class);
            startActivity(intent);
            finish();
        });

        if (Objects.equals(farmersTable.getGender(), "Male")) {
            genderPos = 1;
        } else if (Objects.equals(farmersTable.getGender(), "Female")) {
            genderPos = 2;
        } else if (Objects.equals(farmersTable.getGender(), "Others")) {
            genderPos = 3;
        } else {
            genderPos = 0;
        }

        //Farmer Code
        strFarmerCode = farmerCode;
        txtFarmerCode.setText(strFarmerCode);

        //Other details
        mId = farmersTable.getFarmerId();
        etFirstName.setText(farmersTable.getFirstName());
        etLastName.setText(farmersTable.getLastName());
        etFatherName.setText(farmersTable.getFatherName());
        etPrimaryContact.setText(farmersTable.getPrimaryContactNo());
        etNoOfPlot.setText(farmersTable.getNoOfPlots());
        etAddress.setText(farmersTable.getAddress());
        etAge.setText(farmersTable.getAge());
        edtGender.setText(farmersTable.getGender());

//        spSelectVillageName. = farmersTable.getVillageId();
        etNationalIdentityCode.setText(farmersTable.getNationalIdentityCode());


        //For title
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(EditPersonalDetailsActivity.this,
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


        //For Gender
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(EditPersonalDetailsActivity.this,
                android.R.layout.simple_spinner_item, genderArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spGender.setAdapter(dataAdapter1);

        spGender.setSelection(genderPos);
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


        // TODO: 9/18/2023 visibility of spinners
        imgEditBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    openChoiceDialogue();
                } catch (Exception ex) {
                    // Toast.makeText(LoginActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
                //strStaticKeyForEditAdd = "";
                // TODO: 9/18/2023 spinner data
//                spSelectCountry.setVisibility(View.VISIBLE);
//                spSelectState.setVisibility(View.VISIBLE);
//                spSelectDistrict.setVisibility(View.VISIBLE);
//                spSelectSubDistrict.setVisibility(View.VISIBLE);
//                spSelectVillageName.setVisibility(View.VISIBLE);
//
//                // TODO: 9/18/2023 edit text data
//                edtCountryName.setVisibility(View.GONE);
//                edtStateName.setVisibility(View.GONE);
//                edtDistricName.setVisibility(View.GONE);
//                edtSubDistricName.setVisibility(View.GONE);
//                edtVillageName.setVisibility(View.GONE);
//                etAddress.setEnabled(true);

            }
        });


        imgSaveAddBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSaveDialogue();
//                strStaticKeyForEditAdd = "save";
//                // TODO: 9/18/2023 spinner data
//                spSelectCountry.setVisibility(View.GONE);
//                spSelectState.setVisibility(View.GONE);
//                spSelectDistrict.setVisibility(View.GONE);
//                spSelectSubDistrict.setVisibility(View.GONE);
//                spSelectVillageName.setVisibility(View.GONE);
//
//                // TODO: 9/18/2023 edit text data
//                edtCountryName.setVisibility(View.VISIBLE);
//                edtStateName.setVisibility(View.VISIBLE);
//                edtDistricName.setVisibility(View.VISIBLE);
//                edtSubDistricName.setVisibility(View.VISIBLE);
//                edtVillageName.setVisibility(View.VISIBLE);
//                etAddress.setEnabled(false);
//                viewModel.updateVillageIdInFarmerTable(etAddress.getText().toString(),strSaveVID,strFarmerCode);
//                getSubDisIDFromVillageIdFromLocalDb(strSaveVID);
                // saveAddress();

            }
        });

        imgCancelEditBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 9/18/2023 spinner data
                imgSaveAddBt.setVisibility(View.GONE);
                imgCancelEditBt.setVisibility(View.GONE);
                imgEditBt.setVisibility(View.VISIBLE);
                strStaticKeyForEditAdd = "noEdit";

                spSelectCountry.setVisibility(View.GONE);
                spSelectState.setVisibility(View.GONE);
                spSelectDistrict.setVisibility(View.GONE);
                spSelectSubDistrict.setVisibility(View.GONE);
                spSelectVillageName.setVisibility(View.GONE);

                // TODO: 9/18/2023 edit text data
                etAddress.setEnabled(false);
                edtCountryName.setVisibility(View.VISIBLE);
                edtStateName.setVisibility(View.VISIBLE);
                edtDistricName.setVisibility(View.VISIBLE);
                edtSubDistricName.setVisibility(View.VISIBLE);
                edtVillageName.setVisibility(View.VISIBLE);

            }
        });


        //Country spinner
//        getCountryListFromLocalDb();


        //Save button


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(EditPersonalDetailsActivity.this, DashBoardFarmerListActivity.class);
//        startActivity(intent);
        finish();
    }

    private void openChoiceDialogue() {


        dialog = new Dialog(EditPersonalDetailsActivity.this, R.style.MyAlertDialogThemeNew);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.show_select_yes_no_dialogue);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        TextView txtTitle = dialog.findViewById(R.id.txt_title_dig);
        TextView txtYes = dialog.findViewById(R.id.txt_yes);
        TextView txtNo = dialog.findViewById(R.id.txt_No);

        txtTitle.setText(getString(R.string.are_you_sure_want_s_to_edit_your_address));
        txtYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                imgSaveAddBt.setVisibility(View.VISIBLE);
                imgCancelEditBt.setVisibility(View.VISIBLE);
                imgEditBt.setVisibility(View.GONE);

                spSelectCountry.setVisibility(View.VISIBLE);
                spSelectState.setVisibility(View.VISIBLE);
                spSelectDistrict.setVisibility(View.VISIBLE);
                spSelectSubDistrict.setVisibility(View.VISIBLE);
                spSelectVillageName.setVisibility(View.VISIBLE);

                // TODO: 9/18/2023 edit text data
                edtCountryName.setVisibility(View.GONE);
                edtStateName.setVisibility(View.GONE);
                edtDistricName.setVisibility(View.GONE);
                edtSubDistricName.setVisibility(View.GONE);
                edtVillageName.setVisibility(View.GONE);
                etAddress.setEnabled(true);
            }
        });
        txtNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
//                imgSaveAddBt.setClickable(false);
//                imgEditBt.setClickable(true);
            }
        });

        dialog.show();

    }

    private void openSaveDialogue() {

        dialog = new Dialog(EditPersonalDetailsActivity.this, R.style.MyAlertDialogThemeNew);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.show_select_yes_no_dialogue);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        TextView txtTitle = dialog.findViewById(R.id.txt_title_dig);
        TextView txtYes = dialog.findViewById(R.id.txt_yes);
        TextView txtNo = dialog.findViewById(R.id.txt_No);

        txtTitle.setText(getString(R.string.are_you_sure_want_to_save_add));
        txtYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgSaveAddBt.setVisibility(View.GONE);
                imgCancelEditBt.setVisibility(View.GONE);
                imgEditBt.setVisibility(View.VISIBLE);
                dialog.dismiss();
                strStaticKeyForEditAdd = "save";
                // TODO: 9/18/2023 spinner data
                spSelectCountry.setVisibility(View.GONE);
                spSelectState.setVisibility(View.GONE);
                spSelectDistrict.setVisibility(View.GONE);
                spSelectSubDistrict.setVisibility(View.GONE);
                spSelectVillageName.setVisibility(View.GONE);

                // TODO: 9/18/2023 edit text data
                edtCountryName.setVisibility(View.VISIBLE);
                edtStateName.setVisibility(View.VISIBLE);
                edtDistricName.setVisibility(View.VISIBLE);
                edtSubDistricName.setVisibility(View.VISIBLE);
                edtVillageName.setVisibility(View.VISIBLE);
                etAddress.setEnabled(false);
                viewModel.updateVillageIdInFarmerTable(etAddress.getText().toString(), strSaveVID, strFarmerCode);
                getSubDisIDFromVillageIdFromLocalDb(strSaveVID);
            }
        });
        txtNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
//                imgSaveAddBt.setVisibility(View.GONE);
//                imgEditBt.setVisibility(View.VISIBLE);
                //imgEditBt.setClickable(false);

            }
        });

        dialog.show();

    }
    // TODO: 9/18/2023 get country details for spinner

    public void getCountryDetailsBySpinner() {
        try {
            viewModel.getCountryDetailsListFromLocalDB();
            if (viewModel.getCountryDetailsByIdLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<Country> countryList = (List<Country>) o;
                        viewModel.getCountryDetailsByIdLiveData().removeObserver(this);
                        if (countryList != null && countryList.size() > 0) {

//                            for (int i = 0; i < countryList.size(); i++)
//                            {
//                                array_countryList.add(countryList.get(i).getName());
//                                array_countryIds.add(countryList.get(i).getId());
//                            }
                            ArrayAdapter<Country> dataAdapterCont = new ArrayAdapter<>(EditPersonalDetailsActivity.this,
                                    android.R.layout.simple_spinner_item, countryList);
                            dataAdapterCont.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            dataAdapterCont.notifyDataSetChanged();
                            spSelectCountry.setAdapter(dataAdapterCont);
                            // at_cmpt_txt_sp.setAdapter(dataAdapterCont);
                            //   spSelectCountry.setSelection();
                            spSelectCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                            Log.d(TAG, "onChanged: valueLatest" + at_cmpt_txt_sp.getText().toString() + strGetCountryName);


//                            int position = 0;
//                            for(int i = 0 ; i<countryList.size();i++)
//                            {
//                                int pos = countryList.get(i).getId();
//                                int cId = countryList.get(i).getCountryId();
//                                if (strGetCountryPosId.equals(pos))
//                                {
//                                    position = countryList.get(i).getCountryId();
//                                }
//
//                            }
//                            if (strGetCountryPosId!=null)
//                            {
//                                etCountry.setSelection(position);
//                            }


                        } else {
                            Log.e(TAG, "no data");
                            List<Country> emptyList = new ArrayList<>();
                            ArrayAdapter<Country> dataAdapterCont1 = new ArrayAdapter<>(EditPersonalDetailsActivity.this,
                                    android.R.layout.simple_spinner_item, emptyList);
                            dataAdapterCont1.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            spSelectCountry.setAdapter(dataAdapterCont1);
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

    }

    public void getSubDisIDFromVillageIdFromLocalDb(String strVillageId) {
        try {
            viewModel.getDistricIDFromVillageTableDetailsByVillageId(strVillageId);
            if (viewModel.getDissIdFromVillageTableLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        VillageTable villageTable = (VillageTable) o;
                        viewModel.getDissIdFromVillageTableLiveData().removeObserver(this);
                        Log.e(TAG, "onChanged: data" + villageTable);
                        if (villageTable != null) {
                            strGetVillageIdPos = Integer.valueOf(villageTable.getId());
                            edtVillageName.setText(villageTable.getName());
                            etPincode.setText(villageTable.getPinCode());
                            String strSubDisId = villageTable.getSubDistrictId();
                            Log.d(TAG, "onChanged: strSubDisId" + strSubDisId);
                            getDisIDFromSubDistricIdFromLocalDb(strSubDisId);

                        } else {
                            Toast.makeText(EditPersonalDetailsActivity.this, "data is empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                viewModel.getDissIdFromVillageTableLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getDisIDFromSubDistricIdFromLocalDb(String strSubDistricId) {
        try {
            viewModel.getDistricIDFromSubDistricId(strSubDistricId);
            if (viewModel.getDissIdFromSubDistrictTableLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        SubDistrict subDistrict = (SubDistrict) o;
                        viewModel.getDissIdFromVillageTableLiveData().removeObserver(this);
                        Log.e(TAG, "onChanged: data" + subDistrict);
                        if (subDistrict != null) {
                            strGetSubDistricIdPos = subDistrict.getId();
                            edtSubDistricName.setText(subDistrict.getName());
                            int strDisId = subDistrict.getDistrictId();
                            Log.d(TAG, "onChanged: strDistID" + strDisId);
                            getDistricTableFromLocalDb(strDisId);

                        } else {
                            Toast.makeText(EditPersonalDetailsActivity.this, "data is empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                viewModel.getDissIdFromSubDistrictTableLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getDistricTableFromLocalDb(int strDisId) {
        try {
            viewModel.getDistricDetailsFromLocalDBById(strDisId);
            if (viewModel.getDistrictorRegencyLiveDataLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        DistrictorRegency districtorRegency = (DistrictorRegency) o;
                        viewModel.getDissIdFromVillageTableLiveData().removeObserver(this);
                        Log.e(TAG, "onChanged: data" + districtorRegency);
                        if (districtorRegency != null) {
                            strGetDistricIdPos = districtorRegency.getId();
                            edtDistricName.setText(districtorRegency.getName());
                            int strStateId = districtorRegency.getStateId();
                            Log.d(TAG, "onChanged: strStateID" + strStateId);
                            getStateDetailsDistricTableFromLocalDb(strStateId);

                        } else {
                            Toast.makeText(EditPersonalDetailsActivity.this, "data is empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                viewModel.getDistrictorRegencyLiveDataLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getStateDetailsDistricTableFromLocalDb(int strStateID) {
        try {
            viewModel.getStateorProvinceDetailsFromLocalDBById(strStateID);
            if (viewModel.getStateorProvinceLiveDataLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        StateorProvince stateorProvince = (StateorProvince) o;
                        viewModel.getStateorProvinceLiveDataLiveData().removeObserver(this);
                        Log.e(TAG, "onChanged: data" + stateorProvince);
                        if (stateorProvince != null) {
                            strGetRegionPosId = stateorProvince.getId();
                            edtStateName.setText(stateorProvince.getName());
                            //   edtDistricName.setText(districtorRegency.getName());
                            int strCuntryId = stateorProvince.getCountryId();
                            Log.d(TAG, "onChanged: strCountryId" + strCuntryId);
                            getCountryDetailsFromLocalDb(strCuntryId);

                        } else {
                            Toast.makeText(EditPersonalDetailsActivity.this, "data is empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                viewModel.getStateorProvinceLiveDataLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void getCountryDetailsFromLocalDb(int countryId) {
        try {
            viewModel.getCountryDetailsFromLocalDBById(countryId);
            if (viewModel.getCountryRegencyLiveDataLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        Country country = (Country) o;
                        viewModel.getCountryRegencyLiveDataLiveData().removeObserver(this);
                        Log.e(TAG, "onChanged: data" + country);
                        if (country != null) {
                            strGetCountryPosId = country.getId();
                            strGetCountryName = country.getName();
                            edtCountryName.setText(strGetCountryName);
                            Log.d(TAG, "onChanged: countryIData" + strGetCountryPosId + strGetCountryName);
                            //   edtDistricName.setText(districtorRegency.getName());
                            //    int strCuntryId = country.getCountryId();
                            //  Log.d(TAG, "onChanged: strSubDisId" + strCuntryId);
                            //getVillageListFromLocalDbById(strSubDisId);

                        } else {
                            Toast.makeText(EditPersonalDetailsActivity.this, "data is empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                viewModel.getCountryRegencyLiveDataLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void successfulBack() {
        Toast.makeText(this, "Successful!!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DashBoardFarmerListActivity.class);
        startActivity(intent);
        finish();
    }

    private void getVillageListFromLocalDbById(String subDistricId) {

        try {
            viewModel.getVillageDetailsListFromLocalDB(subDistricId);
            if (viewModel.getvillageDetailsByPincodeLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<VillageTable> villageTableList = (List<VillageTable>) o;
                        viewModel.getvillageDetailsByPincodeLiveData().removeObserver(this);
                        if (villageTableList != null && villageTableList.size() > 0) {
                            ArrayAdapter<VillageTable> dataAdapter = new ArrayAdapter<>(EditPersonalDetailsActivity.this,
                                    android.R.layout.simple_spinner_item, villageTableList);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            dataAdapter.notifyDataSetChanged();

                            spSelectVillageName.setAdapter(dataAdapter);

//                            if (strGetVillageIdPos!=null)
//                            {
//                                spSelectVillageName.setSelection(strGetVillageIdPos);
//                            }


                            spSelectVillageName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    VillageTable data = (VillageTable) parent.getItemAtPosition(position);
                                    strVillage = String.valueOf(data.getId());
                                    strSaveVID = String.valueOf(data.getId());
                                    etPincode.setText(data.getPinCode());

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        } else {
                            List<VillageTable> emptyList = new ArrayList<>();
                            ArrayAdapter<VillageTable> dataAdapter = new ArrayAdapter<>(EditPersonalDetailsActivity.this,
                                    android.R.layout.simple_spinner_item, emptyList);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            spSelectVillageName.setAdapter(dataAdapter);
                            dataAdapter.notifyDataSetChanged();
                            strVillage = null;
                            //  strSaveVID = null;

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

    private void getSubDistrictListFromLocalDbById(String districId) {
        try {
            viewModel.getSubDistrictDetailsListFromLocalDB(districId);
            if (viewModel.getSubDistrictDetailsByIdLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<SubDistrict> subDistrictList = (List<SubDistrict>) o;
                        viewModel.getSubDistrictDetailsByIdLiveData().removeObserver(this);
                        if (subDistrictList != null && subDistrictList.size() > 0) {
                            ArrayAdapter<SubDistrict> dataAdapterCont = new ArrayAdapter<>(EditPersonalDetailsActivity.this,
                                    android.R.layout.simple_spinner_item, subDistrictList);
                            dataAdapterCont.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            dataAdapterCont.notifyDataSetChanged();
                            spSelectSubDistrict.setAdapter(dataAdapterCont);
//                            if (strGetSubDistricIdPos!=null)
//                            {
//                                etSubDistrict.setSelection(strGetSubDistricIdPos);
//                            }

                            spSelectSubDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    SubDistrict data = (SubDistrict) parent.getItemAtPosition(position);
                                    strSubDistricId = String.valueOf(data.getId());
                                    Log.e(TAG, String.valueOf(data.getId()));
                                    getVillageListFromLocalDbById(String.valueOf(data.getId()));
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        } else {
                            List<SubDistrict> emptyList = new ArrayList<>();
                            ArrayAdapter<SubDistrict> dataAdapterCont1 = new ArrayAdapter<>(EditPersonalDetailsActivity.this,
                                    android.R.layout.simple_spinner_item, emptyList);
                            dataAdapterCont1.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            spSelectSubDistrict.setAdapter(dataAdapterCont1);
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
                            ArrayAdapter<DistrictorRegency> dataAdapter = new ArrayAdapter<>(EditPersonalDetailsActivity.this,
                                    android.R.layout.simple_spinner_item, districtorRegencies);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            dataAdapter.notifyDataSetChanged();
                            spSelectDistrict.setAdapter(dataAdapter);
//                            if (strGetDistricIdPos!=null)
//                            {
//                                spSelectDistrict.setSelection(strGetDistricIdPos);
//                            }


                            spSelectDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    DistrictorRegency data = (DistrictorRegency) parent.getItemAtPosition(position);
                                    Log.e(TAG, String.valueOf(data.getId()));
                                    strSelectDistricId = String.valueOf(data.getId());
                                    getSubDistrictListFromLocalDbById(String.valueOf(data.getId()));
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        } else {
                            List<DistrictorRegency> emptyList = new ArrayList<>();
                            ArrayAdapter<DistrictorRegency> dataAdapter = new ArrayAdapter<>(EditPersonalDetailsActivity.this,
                                    android.R.layout.simple_spinner_item, emptyList);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            spSelectDistrict.setAdapter(dataAdapter);
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
                            ArrayAdapter<StateorProvince> dataAdapter = new ArrayAdapter<>(EditPersonalDetailsActivity.this,
                                    android.R.layout.simple_spinner_item, stateorProvinces);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            dataAdapter.notifyDataSetChanged();
                            spSelectState.setAdapter(dataAdapter);
//                            if (strGetRegionPosId!=null)
//                            {
//                                etState.setSelection(strGetRegionPosId);
//                            }


                            spSelectState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                            ArrayAdapter<StateorProvince> dataAdapter = new ArrayAdapter<>(EditPersonalDetailsActivity.this,
                                    android.R.layout.simple_spinner_item, emptyList);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            spSelectState.setAdapter(dataAdapter);
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
        getDocTableFromLocalForFamerImg(farmerCode);
        getDocTableFromLocalForIdentityImg(farmerCode);
    }


    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return null;
    }

    private void getDocTableFromLocalForFamerImg(String farmer_id) {
        try {
            viewModel.getLocalDocIdentificationFromLocalDBByFidandDtype(farmer_id, "farmer image");
            if (viewModel.getDocIdentiFicationDeatilsTableFromLocalLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<PlantationDocuments> odVisitSurveyTableList = (List<PlantationDocuments>) o;
                        viewModel.getDocIdentiFicationDeatilsTableFromLocalLiveData().removeObserver(this);
                        if (odVisitSurveyTableList != null && odVisitSurveyTableList.size() > 0) {


//                            Log.e(mTag,odVisitSurveyTableList.toString());
                            stImgFar = odVisitSurveyTableList.get(0).getDocURL();

                            if (odVisitSurveyTableList.get(0).getDocURL().contains("https://")) {

                                Picasso.get()
                                        .load(odVisitSurveyTableList.get(0).getDocURL())
                                        .placeholder(R.drawable.baseline_downloading_24)
                                        .error(R.drawable.ic_baseline_agriculture_24)
                                        .into(imgFarmer);
//                                imgStatus1 = odVisitSurveyTableList.get(0).getDocUrl();

                            } else if (odVisitSurveyTableList.get(0).getDocURL().contains("/storage/emulated/")) {
                                File imgFile = new File(odVisitSurveyTableList.get(0).getDocURL());
                                if (imgFile.exists()) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                    imgFarmer.setImageBitmap(bitmap);
                                } else {
                                    Picasso.get()
                                            .load(R.drawable.baseline_broken_image_24)
                                            .into(imgFarmer);
                                }
                            }
//                            else if(odVisitSurveyTableList.get(0).getDocLocal()!=null) {
//                                File imgFile = new  File(odVisitSurveyTableList.get(0).getDocLocal());
//                                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//                                waterImg1.setImageBitmap(myBitmap);
//                                imgStatus1=odVisitSurveyTableList.get(0).getDocLocal();
//                            }


                        } else {
//                            Log.e(mTag,"No data for farmerCode Img 1");
                        }
                    }
                };
                viewModel.getDocIdentiFicationDeatilsTableFromLocalLiveData().observe(EditPersonalDetailsActivity.this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
//            Log.e(mTag,"Getting Null Img 1");
        }
    }

    private void getDocTableFromLocalForIdentityImg(String farmer_id) {
        try {
            viewModel.getLocalDocIdentificationFromLocalDBByFidandDtype(farmer_id, "Primary identity");
            if (viewModel.getDocIdentiFicationDeatilsTableFromLocalLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<PlantationDocuments> odVisitSurveyTableList = (List<PlantationDocuments>) o;
                        viewModel.getDocIdentiFicationDeatilsTableFromLocalLiveData().removeObserver(this);
                        if (odVisitSurveyTableList != null && odVisitSurveyTableList.size() > 0) {


//                            Log.e(mTag,odVisitSurveyTableList.toString());
                            stImgNat = odVisitSurveyTableList.get(0).getDocURL();

                            if (odVisitSurveyTableList.get(0).getDocURL().contains("https://")) {

                                Picasso.get()
                                        .load(odVisitSurveyTableList.get(0).getDocURL())
                                        .placeholder(R.drawable.baseline_downloading_24)
                                        .error(R.drawable.ic_baseline_agriculture_24)
                                        .into(imgNationalIdentity);
//                                imgStatus1 = odVisitSurveyTableList.get(0).getDocUrl();

                            } else if (odVisitSurveyTableList.get(0).getDocURL().contains("/storage/emulated/")) {
                                File imgFile = new File(odVisitSurveyTableList.get(0).getDocURL());
                                if (imgFile.exists()) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                    imgNationalIdentity.setImageBitmap(bitmap);
                                } else {
                                    Picasso.get()
                                            .load(R.drawable.baseline_broken_image_24)
                                            .into(imgNationalIdentity);
                                }
                            }
//                            else if(odVisitSurveyTableList.get(0).getDocLocal()!=null) {
//                                File imgFile = new  File(odVisitSurveyTableList.get(0).getDocLocal());
//                                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//                                waterImg1.setImageBitmap(myBitmap);
//                                imgStatus1=odVisitSurveyTableList.get(0).getDocLocal();
//                            }


                        } else {
//                            Log.e(mTag,"No data for farmerCode Img 1");
                        }
                    }
                };
                viewModel.getDocIdentiFicationDeatilsTableFromLocalLiveData().observe(EditPersonalDetailsActivity.this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
//            Log.e(mTag,"Getting Null Img 1");
        }
    }

    private void previewImageDialog(String stImg) {

        imagePreviewDialog = new Dialog(EditPersonalDetailsActivity.this, R.style.MyAlertDialogThemeNew);
        imagePreviewDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        imagePreviewDialog.setContentView(R.layout.preview_image_dialog);
        imagePreviewDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        imagePreviewDialog.setCanceledOnTouchOutside(false);
        imagePreviewDialog.setCancelable(true);

        TextView backPreview = imagePreviewDialog.findViewById(R.id.backPreview);
        ImageView imagePreview = imagePreviewDialog.findViewById(R.id.imagePreview);

        Log.e(TAG, "preview Img str:" + stImg);

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


    public String getLanguageFromLocalDb(String stLanguage, String stWord) {

        try {
//            viewModel.getLanguageDataViewModel(stLanguage,stWord);
//            viewModel.getLanguageLiveData().observe(this, new Observer<String>() {
//                @Override
//                public void onChanged(String s) {
//                    if (listener != null) {
//                        listener.onLanguageFetched(s);
//                    }
//                }
//            });
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