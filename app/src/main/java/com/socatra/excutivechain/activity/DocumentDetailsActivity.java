package com.socatra.excutivechain.activity;

import static com.socatra.excutivechain.AppConstant.DeviceUserID;

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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.gcacace.signaturepad.views.SignaturePad;
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
import com.socatra.excutivechain.database.entity.AppLanguageHDRTable;
import com.socatra.excutivechain.database.entity.DistrictorRegency;
import com.socatra.excutivechain.database.entity.FarmersTable;
import com.socatra.excutivechain.database.entity.PlantationDocuments;
import com.socatra.excutivechain.database.entity.StateorProvince;
import com.socatra.excutivechain.database.entity.SubDistrict;
import com.socatra.excutivechain.database.entity.VillageTable;
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

public class DocumentDetailsActivity extends BaseActivity implements HasSupportFragmentInjector {

    String TAG = "DocumentDetailsActivityTag";
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    public AppViewModel viewModel;

    String farmerCode = "";
    TextView adddoc1;
    TextView farmer_code1;
    TextView doctype1;
    TextView txtIdTypeName;
    TextView docphoto1;


    ImageView imgBack, imgDocUpload;

    TextView txtFarmerCode, txtSave;

    EditText txtUid;

    Spinner spDocType;

    String[] docTypeArr = new String[]{"--Select Doc Type--", "National Identity Document", "Legal Document", "Consent Document"};

    String strDocType = "";

    int consentVisibility = 0;

    //Camera
    Integer testPictureCount = 0;

    private String[] PERMISSIONS_STORAGE = {//cam
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static final int REQUEST_CAM_PERMISSIONS = 1;//cam
    private static final int CAMERA_REQUEST = 1888;//cam

    static File f = null;//cam

    private String strDocumentImageLocalImagePath = "";

    String strFileExtension1 = null;//cam

    Bitmap bitmapDocument = null;//cam

    private byte[] bytes = null;//cam

    String crLatTxt, crLongTxt;

    private SharedPreferences preferences;

    FusedLocationProviderClient client;

    LocationRequest locationRequest;

    int imageTaken = 0;

    Dialog imagePreviewDialog, dialog, dialogSign;

    private static final int FILE_SELECT_CODE = 101;

    private static final int PICK_PDF_REQUEST = 1000001;

    int pickHandle1 = 0;

    FarmersTable farmersTable;

    String villageId = "0";

    //Farmer Consent
    RadioButton r1, r2;

    RadioGroup radioGroup;

    LinearLayout formLayout;

    Spinner spSelectLanguage;

    String stConsentHead, stConsentBody, stConsentSign, stFarmerName;
    String stVillage, stDistrict, stProvince;

    TextView signConsent, bodyConsent, headingConsent, txtSavePreview;//txtClearSign

//    SignaturePad signaturePad;

    Bitmap bitmapSign = null;

    int count = 0;

    EditText etNameConsent, etVillageConsent, etDistrictConsent, etProvinceConsent;


    TextView lang1, farmerInfo, consentInfo, farmerNameHd, villageNameHd, districtHd, stateHd;

    TextView txtUploadPdf,txtHdPdf;

    LinearLayout uploadPdfLL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_details);

        farmerCode = getIntent().getStringExtra("mFarmerCode");
        farmersTable = (FarmersTable) getIntent().getSerializableExtra("farmData");
        villageId = getIntent().getStringExtra("villageId");

        Log.e(TAG, farmerCode);

        stFarmerName = farmersTable.getFirstName() + " " + farmersTable.getLastName();

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

        String hdAddDocDetails = getResources().getString(R.string.add_document_details);
        String hdFarmerCode = getResources().getString(R.string.farmer_code);
        String hdDocType = getResources().getString(R.string.doc_type);
        String hdIdNo = getResources().getString(R.string.id_number);
        String hdDocPhoto = getResources().getString(R.string.document_photo);
        String hdSave = getResources().getString(R.string.save);
        String hdRadioDocPhoto = getResources().getString(R.string.document_photo);
        String hdRadioForm = getResources().getString(R.string.submit_form);
        String hdSelectLang = getResources().getString(R.string.select_language);
        String hdFarmerInfo = getResources().getString(R.string.farmer_information);
        String hdConsentInfo = getResources().getString(R.string.consent_information);
        String hdFarmerName = getResources().getString(R.string.farmer_name);
        String hdVillageName = getResources().getString(R.string.village);
        String hdDistrict = getResources().getString(R.string.district);
        String hdState = getResources().getString(R.string.state_province);
        String hdpdf = getResources().getString(R.string.upload_pdf);
        String hdPrevSave = getResources().getString(R.string.sign_and_save);

        if (selectedLanguage.equals("English")) {
            txtHdPdf.setText(hdpdf);
            adddoc1.setText(hdAddDocDetails);
            farmer_code1.setText(hdFarmerCode);
            doctype1.setText(hdDocType);
            txtIdTypeName.setText(hdIdNo);
            docphoto1.setText(hdDocPhoto);
            txtSave.setText(hdSave);
            r1.setText(hdRadioDocPhoto);
            r2.setText(hdRadioForm);
            lang1.setText(hdSelectLang);
            farmerInfo.setText(hdFarmerInfo);
            consentInfo.setText(hdConsentInfo);
            farmerNameHd.setText(hdFarmerName);
            villageNameHd.setText(hdVillageName);
            districtHd.setText(hdDistrict);
            stateHd.setText(hdState);
//            txtClearSign.setText(hdClear);
            txtSavePreview.setText(hdPrevSave);
        } else {
            txtHdPdf.setText(getLanguageFromLocalDb(selectedLanguage, hdpdf) + "/" + hdpdf);
            adddoc1.setText(getLanguageFromLocalDb(selectedLanguage, hdAddDocDetails) + "/" + hdAddDocDetails);
            farmer_code1.setText(getLanguageFromLocalDb(selectedLanguage, hdFarmerCode) + "/" + hdFarmerCode);
            doctype1.setText(getLanguageFromLocalDb(selectedLanguage, hdDocType) + "/" + hdDocType);
            txtIdTypeName.setText(getLanguageFromLocalDb(selectedLanguage, hdIdNo) + "/" + hdIdNo);
            docphoto1.setText(getLanguageFromLocalDb(selectedLanguage, hdDocPhoto) + "/" + hdDocPhoto);
            txtSave.setText(getLanguageFromLocalDb(selectedLanguage, hdSave) + "/" + hdSave);
            r1.setText(getLanguageFromLocalDb(selectedLanguage, hdRadioDocPhoto) + "/" + hdRadioDocPhoto);
            r2.setText(getLanguageFromLocalDb(selectedLanguage, hdRadioForm) + "/" + hdRadioForm);
            lang1.setText(getLanguageFromLocalDb(selectedLanguage, hdSelectLang) + "/" + hdSelectLang);
            farmerInfo.setText(getLanguageFromLocalDb(selectedLanguage, hdFarmerInfo) + "/" + hdFarmerInfo);
            consentInfo.setText(getLanguageFromLocalDb(selectedLanguage, hdConsentInfo) + "/" + hdConsentInfo);
            farmerNameHd.setText(getLanguageFromLocalDb(selectedLanguage, hdFarmerName) + "/" + hdFarmerName);
            villageNameHd.setText(getLanguageFromLocalDb(selectedLanguage, hdVillageName) + "/" + hdVillageName);
            districtHd.setText(getLanguageFromLocalDb(selectedLanguage, hdDistrict) + "/" + hdDistrict);
            stateHd.setText(getLanguageFromLocalDb(selectedLanguage, hdState) + "/" + hdState);
//            txtClearSign.setText(getLanguageFromLocalDb(selectedLanguage, hdClear) + "/" + hdClear);
            txtSavePreview.setText(getLanguageFromLocalDb(selectedLanguage, hdPrevSave) + "/" + hdPrevSave);
        }

    }

    private String getSelectedLanguage() {
        return preferences.getString("selected_language", "English");
    }

    private void initializeUI() {
        imgBack = findViewById(R.id.imgBackAddDoc);
        txtFarmerCode = findViewById(R.id.et_farmer_code_AddDoc);
        spDocType = findViewById(R.id.sp_select_type_AddDoc);
        txtSave = findViewById(R.id.txt_save_AddDoc);
        txtUid = findViewById(R.id.et_type_textID_AddDoc);
        imgDocUpload = findViewById(R.id.img_IdentityImg_AddDoc);
        txtUploadPdf=findViewById(R.id.txt_pdf_uri);
        txtHdPdf=findViewById(R.id.txt_head_pdf_upload);
        uploadPdfLL=findViewById(R.id.uploadPdfLL);
//        signaturePad = findViewById(R.id.signaturePad);
        //label
        docphoto1 = findViewById(R.id.docphoto1);

        //consent
        radioGroup = findViewById(R.id.radio_group);
        r1 = findViewById(R.id.radio_image);
        r2 = findViewById(R.id.radio_form);
        formLayout = findViewById(R.id.formLayout);
        spSelectLanguage = findViewById(R.id.spSelectLanguageConsent);
        headingConsent = findViewById(R.id.headingConsent);
        bodyConsent = findViewById(R.id.bodyConsent);
        signConsent = findViewById(R.id.signConsent);
//        txtClearSign = findViewById(R.id.txtClearSign);

        etNameConsent = findViewById(R.id.etNameConsent);
        etVillageConsent = findViewById(R.id.etVillageConsent);
        etDistrictConsent = findViewById(R.id.etDistrictConsent);
        etProvinceConsent = findViewById(R.id.etProvinceConsent);
        txtSavePreview = findViewById(R.id.txtSavePreview);

        //Labels
        adddoc1 = findViewById(R.id.adddoc1);
        farmer_code1 = findViewById(R.id.farmer_code1);
        doctype1 = findViewById(R.id.doctype1);
        txtIdTypeName = findViewById(R.id.txtIdTypeName);
        lang1 = findViewById(R.id.lang1);
        farmerInfo = findViewById(R.id.farmerInfo);
        consentInfo = findViewById(R.id.consentInfo);
        farmerNameHd = findViewById(R.id.farmerNameHd);
        villageNameHd = findViewById(R.id.villageNameHd);
        districtHd = findViewById(R.id.districtHd);
        stateHd = findViewById(R.id.stateHd);


    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (bitmapSign==null){
//            signaturePad.clear();
//            bitmapSign=null;
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (bitmapSign==null){
//            signaturePad.clear();
//            bitmapSign=null;
//        }
//
//    }

    private void initializeValues() {

        //Todo for consent
        formLayout.setVisibility(View.GONE);
        r1.setChecked(true);
        radioFormVisibility(consentVisibility);
        etNameConsent.setText(stFarmerName);


        //Pdf picker
        txtUploadPdf.setOnClickListener(v -> {
            if (strDocumentImageLocalImagePath.length()<10){
                openPdfPicker();
            } else {
                Toast.makeText(this, "Already added!!", Toast.LENGTH_SHORT).show();
            }
        });
        //Todo Pdf view Visibility
//        uploadPdfLL.setVisibility(View.GONE);


        txtSavePreview.setOnClickListener(v -> {
            openDialogForSign(headingConsent.getText().toString(), bodyConsent.getText().toString(), signConsent.getText().toString());
        });

        //location
        checkSettingsAndStartLocationUpdates();

        //Farmer code
        txtFarmerCode.setText(farmerCode);

        //Doc type spinner
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(DocumentDetailsActivity.this,
                android.R.layout.simple_spinner_item, docTypeArr);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spDocType.setAdapter(dataAdapter1);
        spDocType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                strFarmerGender = parent.getItemAtPosition(position).toString().trim();
                if (position == 0) {
                    strDocType = "0";
                } else {
                    String st = (String) parent.getItemAtPosition(position);
                    strDocType = st;
                    if (st.equals("Consent Document")) {
                        consentVisibility = 1;
                        txtUid.setText("Consent_"+farmerCode);
                        radioFormVisibility(consentVisibility);

                    } else {
                        consentVisibility = 0;
                        radioFormVisibility(consentVisibility);

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imgDocUpload.setOnClickListener(view -> {
            if (strDocumentImageLocalImagePath.length()<10){
                if (imageTaken == 0) {
                    checkSettingsAndStartLocationUpdates();
//                openCameraPermission(true,0);
                    pickImageDialog(0);
                } else if (imageTaken == 1) {
                    previewImageDialog(strDocumentImageLocalImagePath);
//                Log.e(TAG, "for preview");
                }
            } else {
                if (imageTaken == 1) {
                    previewImageDialog(strDocumentImageLocalImagePath);
//                Log.e(TAG, "for preview");
                }
                Toast.makeText(this, "Already added!!", Toast.LENGTH_SHORT).show();
            }

        });


        //Save button
        txtSave.setOnClickListener(view -> {
            if (strDocType == "0") {
                Toast.makeText(this, "Please select Doc Type!!", Toast.LENGTH_SHORT).show();
            } else if (txtUid.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Please insert ID Number!!", Toast.LENGTH_SHORT).show();
            } else if (strDocumentImageLocalImagePath.length() < 10 ) {//&& bitmapDocument == null
                Toast.makeText(this, "Add Document Image!!", Toast.LENGTH_SHORT).show();
            } else {
                String strNationalIdentityCode = txtUid.getText().toString().trim();
                //For Primary Identity doc
                PlantationDocuments primaryIdDocuments = new PlantationDocuments();
                primaryIdDocuments.setFarmerCode(farmerCode);
                primaryIdDocuments.setPlotCode("");
                primaryIdDocuments.setDocURL(strDocumentImageLocalImagePath);
                primaryIdDocuments.setDocType(strDocType);
                primaryIdDocuments.setDocUrlValue(strNationalIdentityCode);
                primaryIdDocuments.setIsActive("true");
                primaryIdDocuments.setSync(false);
                primaryIdDocuments.setServerSync("0");
                primaryIdDocuments.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                primaryIdDocuments.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                String dateTime = appHelper.getCurrentDateTime(AppConstant.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
                primaryIdDocuments.setCreatedDate(dateTime);
                primaryIdDocuments.setUpdatedDate(dateTime);
                viewModel.insertDoctable(primaryIdDocuments);
                Toast.makeText(this, "Successful!!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        //Back button
        imgBack.setOnClickListener(view -> {
            finish();
        });
    }

    //Todo for sign Dialog
    @SuppressLint("SetTextI18n")
    private void openDialogForSign(String s1, String s2, String s3) {
        dialogSign = new Dialog(DocumentDetailsActivity.this, R.style.MyAlertDialogThemeNew);
        dialogSign.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSign.setContentView(R.layout.preview_consent_dialog_sign);
        dialogSign.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        dialogSign.setCanceledOnTouchOutside(false);
        dialogSign.setCancelable(true);


        SignaturePad signaturePad=dialogSign.findViewById(R.id.signaturePad);


        TextView savePreviewDiaS=dialogSign.findViewById(R.id.savePreviewDiaS);
        TextView txtClearSignS = dialogSign.findViewById(R.id.txtClearSignS);
        TextView txtBackSignS = dialogSign.findViewById(R.id.txtBackSignS);
        TextView headAddSignS= dialogSign.findViewById(R.id.headAddSignS);

        String selectedLanguage = getSelectedLanguage();

        String hdSavePr = getResources().getString(R.string.preview_and_save);
        String hdClearPr = getResources().getString(R.string.clear);
        String hdBackPr = getResources().getString(R.string.m_back);
        String hdAddSignPr = getResources().getString(R.string.sign_and_save);

        if (selectedLanguage.equals("English")) {
            savePreviewDiaS.setText(hdSavePr);
            txtClearSignS.setText(hdClearPr);
            txtBackSignS.setText(hdBackPr);
            headAddSignS.setText(hdAddSignPr);
        } else {
            savePreviewDiaS.setText(getLanguageFromLocalDb(selectedLanguage, hdSavePr) + "/" + hdSavePr);
            txtClearSignS.setText(getLanguageFromLocalDb(selectedLanguage, hdClearPr) + "/" + hdClearPr);
            txtBackSignS.setText(getLanguageFromLocalDb(selectedLanguage, hdBackPr) + "/" + hdBackPr);
            headAddSignS.setText(getLanguageFromLocalDb(selectedLanguage, hdAddSignPr) + "/" + hdAddSignPr);
        }

        //Todo SignaturePad
//        bitmapSign=Bitmap.createBitmap(100,100,Bitmap.Config.ARGB_8888);

        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {

            @Override
            public void onStartSigning() {
                //Event triggered when the pad is touched
            }

            @Override
            public void onSigned() {
                //Event triggered when the pad is signed
//                bitmapSign = signaturePad.getTransparentSignatureBitmap();
//                    Bitmap bitmapSign=null;
//                    if (bitmapSign != null) {
//                        bitmapSign.recycle(); // Release the previous bitmap if it exists
//                    }

                bitmapSign = signaturePad.getTransparentSignatureBitmap();
//                    signState=1;

//                    Bitmap newBitmapSign = signaturePad.getTransparentSignatureBitmap();
//                    if (newBitmapSign != null && newBitmapSign.getWidth() > 0 && newBitmapSign.getHeight() > 0) {
//                        if (bitmapSign != null) {
//                            bitmapSign.recycle(); // Release the previous bitmap if it exists
//                        }
//                        bitmapSign = newBitmapSign;
//                        // Your other code here
//                    }

            }

            @Override
            public void onClear() {
                //Event triggered when the pad is cleared
                bitmapSign = null;
            }
        });


        txtClearSignS.setOnClickListener(v -> {
            signaturePad.clear();
//                bitmapSign = null;
        });

        savePreviewDiaS.setOnClickListener(v -> {
            if (bitmapSign != null) {
//                openDialogPreview(headingConsent.getText().toString(),bodyConsent.getText().toString(),signConsent.getText().toString(),bitmapSign);
                openDialogPreview(s1, s2, s3, bitmapSign);
                dialogSign.dismiss();
            } else {
                Toast.makeText(this, "Add Signature before preview!!", Toast.LENGTH_SHORT).show();
            }
        });

        txtBackSignS.setOnClickListener(v -> {
            dialogSign.dismiss();
        });



        dialogSign.show();
    }

    private void radioFormVisibility(int vis) {
        if (vis == 1) {
            docphoto1.setVisibility(View.GONE);
            radioGroup.setVisibility(View.VISIBLE);

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton selectedRadioButton = findViewById(checkedId);
                    if (selectedRadioButton != null) {
                        if (selectedRadioButton == r1) {
                            imgDocUpload.setVisibility(View.VISIBLE);
                            formLayout.setVisibility(View.GONE);
                        } else {

                            imgDocUpload.setVisibility(View.GONE);
                            formLayout.setVisibility(View.VISIBLE);

                        }
                    }
                }
            });
        } else if (vis == 0) {
            docphoto1.setVisibility(View.VISIBLE);
            radioGroup.setVisibility(View.GONE);
        }

    }

    private void previewImageDialog(String stImg) {

        imagePreviewDialog = new Dialog(DocumentDetailsActivity.this, R.style.MyAlertDialogThemeNew);
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

    private void pickImageDialog(int i) {
        dialog = new Dialog(DocumentDetailsActivity.this, R.style.MyAlertDialogThemeNew);
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

    private void showFileChooser(int i) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, FILE_SELECT_CODE);
    }

    private void openPdfPicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, PICK_PDF_REQUEST);
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    private void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
        getAllLanguagesFromHDR();
        getVillageById(villageId);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return null;
    }

    private void openCameraPermission(Boolean land, Integer pictureCount) {
        testPictureCount = pictureCount;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (!CommonUtils.isPermissionAllowed(DocumentDetailsActivity.this, Manifest.permission.CAMERA))) {
            android.util.Log.v(TAG, "Location Permissions Not Granted");
            ActivityCompat.requestPermissions(
                    DocumentDetailsActivity.this,
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
                    strDocumentImageLocalImagePath = null;
                    try {
                        f = setUpPhotoFile();

                        strDocumentImageLocalImagePath = f.getAbsolutePath();

                        strFileExtension1 = f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf("."));


                        Uri photoURI = FileProvider.getUriForFile(DocumentDetailsActivity.this,
                                BuildConfig.APPLICATION_ID + ".provider",
                                f);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    } catch (IOException e) {
                        e.printStackTrace();
                        f = null;
                        strDocumentImageLocalImagePath = null;
                    }
                } else {

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
            strDocumentImageLocalImagePath = f.getAbsolutePath();
        }
        return f;
    }

    private File createImageFileFirst() {
        File image = null;
        File mediaStorageDir = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mediaStorageDir = new File(DocumentDetailsActivity.this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "Chain_Documents");
        } else
            mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "Chain_Documents");
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


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CAMERA_REQUEST:
                if (resultCode == RESULT_OK) {
                    try {
//                        imgDocUpload.setClickable(false);
                        pickHandle1 = 1;
                        imageTaken = 1;
                        handleBigCameraPhoto();
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
                            imageTaken = 1;
                            Uri uri = data.getData();
//                            String realUriSt = getRealPathFromUri(uri);
//                            strFarmerImageLocalImagePath = realUriSt;//String.valueOf(uri);
//                            strFarmerRUri=realUriSt;
//                            handleBigCameraPhoto();
                            File IMAGE_COPY_PATH = createImageFileFirst();
                            try {
                                InputStream inputStream = getContentResolver().openInputStream(uri);//Uri.parse(realUriSt)
                                OutputStream outputStream = new FileOutputStream(IMAGE_COPY_PATH);
                                copyFile(inputStream, outputStream);
                                strDocumentImageLocalImagePath = IMAGE_COPY_PATH.getAbsolutePath();
                                inputStream.close();
                                outputStream.close();
                                handleBigCameraPhoto();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            case PICK_PDF_REQUEST:
                if (resultCode==RESULT_OK){
                    try {
                        Uri uri=data.getData();
                        txtUploadPdf.setText(uri.toString());
                        Log.e(TAG,"uriPdf:"+uri.toString());
//                        strDocumentImageLocalImagePath=appHelper.convertUriToFile(this,uri);
                        File f=appHelper.convertUriToFile1(this,uri);
                        strDocumentImageLocalImagePath=f.getAbsolutePath();//uri.toString();//todo pdf //uri.getPath()
//                        getAcPath(uri.getPath());
                        Log.e(TAG,"uriPdfPath:"+strDocumentImageLocalImagePath);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;

        } // switch need to add
    }

    private String getFilePathFromContentUri(Uri contentUri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
        }
        return filePath;
    }


    public void getAcPath(String uri1){
        String filePath = null;
        Uri uri = Uri.parse(uri1); // Your content URI here

        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            filePath = cursor.getString(columnIndex);
            cursor.close();
        }

        if (filePath != null) {
            // Now, filePath contains the actual file path
            // You can use it as needed
            Log.e(TAG,"uriAcFun:"+filePath.toString());
        }
    }

    public String getRealPathFromPdfUri(Uri uri) {
        String[] projection = {MediaStore.Files.FileColumns.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
            cursor.moveToFirst();
            String filePath = cursor.getString(columnIndex);
            cursor.close();

            if (filePath != null) {
                return filePath; // If the cursor returned a valid file path, use it.
            }
        }

        // If the cursor is null or the file path is not found, try a different method for PDF Uris.
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String path = null;
            try (Cursor pdfCursor = getContentResolver().query(uri, projection, null, null, null)) {
                if (pdfCursor != null) {
                    int columnIndex = pdfCursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
                    pdfCursor.moveToFirst();
                    path = pdfCursor.getString(columnIndex);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (path != null) {
                return path; // If the PDF cursor returned a valid file path, use it.
            }
        }

        return uri.getPath(); // If no valid file path is found, fall back to the original Uri path.
    }

    private void copyFile(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
    }

    private void handleBigCameraPhoto() throws Exception {

        if (testPictureCount == 0) {
            if (strDocumentImageLocalImagePath != null) {
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
            int targetW = imgDocUpload.getWidth();
            int targetH = imgDocUpload.getHeight();

            /* Get the size of the image */
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(strDocumentImageLocalImagePath, bmOptions);
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

            bitmapDocument = BitmapFactory.decodeFile(strDocumentImageLocalImagePath, bmOptions);

            getBytesFromBitmap(bitmapDocument);

            ExifInterface ei = new ExifInterface(strDocumentImageLocalImagePath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            /* Decode the JPEG file into a Bitmap */

            Bitmap rotatedBitmap = null;
            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmapDocument, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmapDocument, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmapDocument, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmapDocument;
            }


//            bitmapDocument =addDateAndLocationToImage(rotatedBitmap,crLatTxt,crLongTxt);
//            saveNewImage(bitmapDocument, strDocumentImageLocalImagePath);
//            imgDocUpload.setImageBitmap(bitmapDocument);
//            imgDocUpload.invalidate();

            if (pickHandle1 == 1) {
                bitmapDocument = addDateAndLocationToImage(rotatedBitmap, crLatTxt, crLongTxt);
                saveNewImage(bitmapDocument, strDocumentImageLocalImagePath);
                imgDocUpload.setImageBitmap(bitmapDocument);
                imgDocUpload.invalidate();
                testPictureCount = 1;//increment image
            } else if (pickHandle1 == 0) {//picker
                bitmapDocument = addDateAndLocationToImage2(rotatedBitmap, crLatTxt, crLongTxt);
                saveNewImage(bitmapDocument, strDocumentImageLocalImagePath);
                imgDocUpload.setImageBitmap(bitmapDocument);
                imgDocUpload.invalidate();
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
            BoundLocationManager.getInstance(DocumentDetailsActivity.this).observe(this, new Observer<Location>() {
                @Override
                public void onChanged(@Nullable Location location) {
                    if (location != null) {

                        crLatTxt = String.valueOf(location.getLatitude());
                        crLongTxt = String.valueOf(location.getLongitude());

                    } else {
                        Toast.makeText(DocumentDetailsActivity.this, "Location is null", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


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

        String txtFarmer = "Farmer Code: " + farmerCode;

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

        String txtFarmer = "Farmer Code: " + farmerCode;

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

    public Bitmap addDateAndLocationToImageConsent(Bitmap imageBitmap, String lat, String longi) {
        Bitmap bitmapWithDateAndLocation = imageBitmap.copy(imageBitmap.getConfig(), true);

        Canvas canvas = new Canvas(bitmapWithDateAndLocation);
        Paint paint = new Paint();
        paint.setColor(Color.DKGRAY);
        paint.setTextSize(32);//30

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDate = dateFormat.format(new Date());
        String latitude = lat;
        String longitude = longi;


        String txtLat = "Latitude: " + latitude;

        String txtLongi = "Longitude: " + longitude;

        String txtDate = "Date: " + currentDate;
//                + ", Longitude: " + longitude;

        String txtFarmer = "Farmer Code: " + farmerCode;

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
                        resolvableApiException.startResolutionForResult(DocumentDetailsActivity.this, 2000001);
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

    //Todo for consent
    //All Languages
    private void getAllLanguagesFromHDR() {
        try {
            viewModel.getAllLanguagesFromHDR();
            if (viewModel.getAllLanguagesFromHDRLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<AppLanguageHDRTable> list = (List<AppLanguageHDRTable>) o;
                        viewModel.getAllLanguagesFromHDRLiveData().removeObserver(this);
                        if (list != null && list.size() > 0) {
                            ArrayAdapter<AppLanguageHDRTable> dataAdapter = new ArrayAdapter<>(DocumentDetailsActivity.this,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            dataAdapter.notifyDataSetChanged();
                            spSelectLanguage.setAdapter(dataAdapter);

                            spSelectLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    AppLanguageHDRTable data = (AppLanguageHDRTable) parent.getItemAtPosition(position);
//                                    Log.e(TAG, "Spinner select name : " + data.getLanguageName());
                                    String word = getLanguageCap(data.getLanguageName());
                                    updateConsentLang(word);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        } else {
                            List<AppLanguageHDRTable> emptyList = new ArrayList<>();
                            ArrayAdapter<AppLanguageHDRTable> dataAdapter = new ArrayAdapter<>(DocumentDetailsActivity.this,
                                    android.R.layout.simple_spinner_item, emptyList);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            spSelectLanguage.setAdapter(dataAdapter);
                            dataAdapter.notifyDataSetChanged();
                        }
                    }
                };
                viewModel.getAllLanguagesFromHDRLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateConsentLang(String selectedLang) {

        stConsentHead = getResources().getString(R.string.consent_head);
        stConsentBody = getResources().getString(R.string.consent_body_en);//en
        stConsentSign = getResources().getString(R.string.consent_sign);

        if (selectedLang == null || selectedLang.equals("English")) {
//            Log.e(TAG,"langUpd "+selectedLang);

            headingConsent.setText(stConsentHead);

            if (count == 0) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String filledText = String.format(stConsentBody, stFarmerName, stVillage, stDistrict, stProvince);
                        bodyConsent.setText(filledText);
                        count++;
                    }
                }, 500);

            } else {
                String filledText = String.format(stConsentBody, stFarmerName, stVillage, stDistrict, stProvince);
                bodyConsent.setText(filledText);
            }

            signConsent.setText(stConsentSign);
        } else {

//            Log.e(TAG,"langUpdOt"+selectedLang);
            headingConsent.setText(getLanguageFromLocalDb(selectedLang, stConsentHead));

            String temp = getLanguageFromLocalDb(selectedLang, stConsentBody);
//            Log.e(TAG,"textHindi"+"temp:"+temp);
            String filledText = String.format(temp, stFarmerName, stVillage, stDistrict, stProvince);
//            Log.e(TAG,"textHindi"+filledText);
            bodyConsent.setText(filledText);

            signConsent.setText(getLanguageFromLocalDb(selectedLang, stConsentSign));
        }
    }

    private String getLanguageCap(String word) {
        word = word.toLowerCase().trim();
        word = Character.toUpperCase(word.charAt(0)) + word.substring(1);
        return word;
    }

    public String getLanguageFromLocalDb(String stLanguage, String stWord) {
//        Log.e(TAG,"wordRec"+stLanguage+"::"+stWord);

        try {
            if (viewModel.getLanguageDataVM(stLanguage, stWord) != null) {
                return viewModel.getLanguageDataVM(stLanguage, stWord);
            } else {
                return stWord;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return stWord;
        }

    }

    //Village
    private void getVillageById(String id) {
        try {
            viewModel.getDistricIDFromVillageTableDetailsByVillageId(id);
            if (viewModel.getDissIdFromVillageTableLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        VillageTable villageTable = (VillageTable) o;
                        viewModel.getDissIdFromVillageTableLiveData().removeObserver(this);
                        if (villageTable != null) {

                            stVillage = villageTable.getName().toString();
//                            Log.e(TAG, "vSubid" + villageTable.getSubDistrictId());
                            etVillageConsent.setText(stVillage);
                            getDisIDFromSubDistricIdFromLocalDb(villageTable.getSubDistrictId().toString());

                        }
                    }
                };
                viewModel.getDissIdFromVillageTableLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Sub dist
    public void getDisIDFromSubDistricIdFromLocalDb(String strSubDistricId) {
        try {
            viewModel.getDistricIDFromSubDistricId(strSubDistricId);
            if (viewModel.getDissIdFromSubDistrictTableLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        SubDistrict subDistrict = (SubDistrict) o;
                        viewModel.getDissIdFromVillageTableLiveData().removeObserver(this);
//                        Log.e(TAG, "onChanged: data" + subDistrict);
                        if (subDistrict != null) {
//                            Log.e(TAG, "vSubid" + subDistrict.getSubDistrictId());
                            getDistrictTableFromLocalDb(subDistrict.getDistrictId());
                        }
                    }
                };
                viewModel.getDissIdFromSubDistrictTableLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //District
    public void getDistrictTableFromLocalDb(int strDisId) {
        try {
            viewModel.getDistricDetailsFromLocalDBById(strDisId);
            if (viewModel.getDistrictorRegencyLiveDataLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        DistrictorRegency districtorRegency = (DistrictorRegency) o;
                        viewModel.getDissIdFromVillageTableLiveData().removeObserver(this);
                        if (districtorRegency != null) {
                            stDistrict = districtorRegency.getName().trim();
//                            Log.e(TAG, "sDistid" + stDistrict);
                            etDistrictConsent.setText(districtorRegency.getName());
                            getStateDetailsDistricTableFromLocalDb(districtorRegency.getStateId());
                        }
                    }
                };
                viewModel.getDistrictorRegencyLiveDataLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Province
    public void getStateDetailsDistricTableFromLocalDb(int strStateID) {
        try {
            viewModel.getStateorProvinceDetailsFromLocalDBById(strStateID);
            if (viewModel.getStateorProvinceLiveDataLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        StateorProvince stateorProvince = (StateorProvince) o;
                        viewModel.getStateorProvinceLiveDataLiveData().removeObserver(this);

                        if (stateorProvince != null) {
                            stProvince = stateorProvince.getName();
//                            Log.e(TAG, "dStateId" + stProvince);
                            etProvinceConsent.setText(stateorProvince.getName());
                        }
                    }
                };
                viewModel.getStateorProvinceLiveDataLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Todo Preview Consent form
    private void openDialogPreview(String stConsentHead1, String stConsentBody1, String stConsentSign1, Bitmap bitmapSign1) {
        dialog = new Dialog(DocumentDetailsActivity.this, R.style.MyAlertDialogThemeNew);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.preview_consent_dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

//        Log.e(TAG,"Dial data:"+stConsentHead1+" "+stConsentBody1+" "+stConsentSign1+" "+String.valueOf(bitmapSign1));

        LinearLayout previewLL = dialog.findViewById(R.id.previewConsentll);

        TextView headingConsentDia = dialog.findViewById(R.id.headingConsentDia);
        TextView bodyConsentDia = dialog.findViewById(R.id.bodyConsentDia);
        TextView signConsentDia = dialog.findViewById(R.id.signConsentDia);

        TextView savePreviewDia = dialog.findViewById(R.id.savePreviewDia);

//        TextView addDialogPrev = dialog.findViewById(R.id.addDialogPrev);

        ImageView imageViewPreview = dialog.findViewById(R.id.imageViewPreviewDia);

        headingConsentDia.setText(stConsentHead1);
        bodyConsentDia.setText("  " + stConsentBody1);
        signConsentDia.setText(stConsentSign1);

//        addDialogPrev.setOnClickListener(v -> {
//
//        });

        Glide.with(this).load(bitmapSign1).into(imageViewPreview);


        savePreviewDia.setOnClickListener(v -> {
            dialog.dismiss();
            captureAndSaveScreenshot(previewLL);
        });


        dialog.show();
    }


    private void captureAndSaveScreenshot(LinearLayout layout) {
        layout.setDrawingCacheEnabled(true);
        layout.buildDrawingCache();
        Bitmap bitmap = layout.getDrawingCache();

        File imageFile = createImageFileFirst();

        try {
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            strDocumentImageLocalImagePath = imageFile.getAbsolutePath();
            setImageConsent(bitmap);
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        layout.setDrawingCacheEnabled(false);
    }

    private void setImageConsent(Bitmap bitmap) {
        r1.setChecked(true);
        r2.setClickable(false);
        imgDocUpload.setVisibility(View.VISIBLE);
        formLayout.setVisibility(View.GONE);
        bitmapDocument = addDateAndLocationToImageConsent(bitmap, crLatTxt, crLongTxt);
        saveNewImage(bitmapDocument, strDocumentImageLocalImagePath);
//        bitmapDocument=bitmap;
        imageTaken = 1;
        Glide.with(this).load(strDocumentImageLocalImagePath).into(imgDocUpload);
    }
}