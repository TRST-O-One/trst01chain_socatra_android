package com.socatra.excutivechain.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.socatra.excutivechain.BaseActivity;

import com.socatra.excutivechain.CommonUtils;
import com.socatra.excutivechain.R;
import com.socatra.excutivechain.adapters.FarmerDetailsListAdapter;

import com.socatra.excutivechain.database.entity.DealerFarmer;
import com.socatra.excutivechain.database.entity.FarmersTable;
import com.socatra.excutivechain.database.entity.ManfacturerFarmer;
import com.socatra.excutivechain.view_models.AppViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
//import com.socatra.excutivechain.BuildConfig;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import letsrock.areaviewlib.BuildConfig;

public class DashBoardFarmerListActivity extends BaseActivity implements View.OnClickListener, HasSupportFragmentInjector, FarmerDetailsListAdapter.SyncCallbackInterface {

    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    public AppViewModel viewModel;
    private SharedPreferences preferences;
    private TextView refreshButton;
    private TextView addButton;
    private Button syncButton;
    TextView txtAddFarmer, txtSync, txtRefList;
    SearchView svFarmer;
    Dialog dialog;

    RecyclerView recycler;
    SearchView searchByName;
    FarmerDetailsListAdapter farmerDetailsListAdapter;
    private static final int CAMERA_REQUEST = 1888;
    Integer testPictureCount = 0;

    String pltNo = "";

    private String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static final int REQUEST_CAM_PERMISSIONS = 1;
    static File f = null;
    private String strFarmerLocalImage = "";
    Bitmap bitmapFarmer = null;
    private String strImageOnePath = "";
    private byte[] bytes = null;
    String strFileExtension = null;
    ImageView imageOne;
    String farmerCode = "";

    int farmerManufacturerStatus = 0;
    int farmerDealerStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board_farmer_list);

        initializeUI();
        configureDagger();
        configureViewModel();
        initializeValues();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        refreshButton = findViewById(R.id.txtRefList);
        addButton = findViewById(R.id.txtAddFarmer);
        syncButton = findViewById(R.id.txtSync);


//
//        updateButtonLabels();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        updateButtonLabels();

    }


    private String getSelectedLanguage() {
        return preferences.getString("selected_language", "English");
    }

    private void restartDashboardActivity() {
        Intent intent = new Intent(this, DashBoardFarmerListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void updateButtonLabels() {
        String selectedLanguage = getSelectedLanguage();

        String hdRefresh=getResources().getString(R.string.refresh);
        String hdAdd=getResources().getString(R.string.add);
        String hdSync=getResources().getString(R.string.sync);

        if (selectedLanguage.equals("English")) {
            //Default English
            refreshButton.setText(hdRefresh);
            addButton.setText(hdAdd);
            syncButton.setText(hdSync);
        } else {
            refreshButton.setText(getLanguageFromLocalDb(selectedLanguage, hdRefresh) + "/" + hdRefresh);
            addButton.setText(getLanguageFromLocalDb(selectedLanguage, hdAdd) + "/" + hdAdd);
            syncButton.setText(getLanguageFromLocalDb(selectedLanguage, hdSync) + "/" + hdSync);
        }

    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void initializeUI() {
        txtAddFarmer = findViewById(R.id.txtAddFarmer);
        txtRefList = findViewById(R.id.txtRefList);
        svFarmer = findViewById(R.id.svFarmer);
        txtSync = findViewById(R.id.txtSync);
        searchByName = findViewById(R.id.svFarmer);
        recycler = findViewById(R.id.recycler);
    }

    private void initializeValues() {


        txtAddFarmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoardFarmerListActivity.this, PersonalRegistrationActivity.class);
                startActivity(intent);
//                finish();
            }
        });

        txtRefList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFarmerlistFromLocalDb();
            }
        });

        txtSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoardFarmerListActivity.this, SyncActivity.class);
                startActivity(intent);
//                finish();
            }
        });

        searchByName.setOnClickListener(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler.setLayoutManager(mLayoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setNestedScrollingEnabled(false);
        recycler.setHasFixedSize(true);
        ((SimpleItemAnimator) recycler.getItemAnimator()).setSupportsChangeAnimations(false);


        SearchManager searchManager = (SearchManager) getApplicationContext().getSystemService(Context.SEARCH_SERVICE);
        searchByName.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchByName.setMaxWidth(Integer.MAX_VALUE);
        // listening to search query text change

        searchByName.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (farmerDetailsListAdapter != null && farmerDetailsListAdapter.getFilter() != null) {
                    farmerDetailsListAdapter.getFilter().filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                if (farmerDetailsListAdapter != null && farmerDetailsListAdapter.getFilter() != null) {
                    farmerDetailsListAdapter.getFilter().filter(query);
                }
                return false;
            }
        });


    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    private void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
        getFarmerlistFromLocalDb();
//        getFarmerManufacturerStatus(farmerCode);
//        getFarmerDealerStatus(farmerCode);

    }


    public void getFarmerlistFromLocalDb() {
        try {

            viewModel.getFarmerListFromLocalDBStatus();
            if (viewModel.getFarmerDetailsListLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<FarmersTable> odVisitSurveyTableList = (List<FarmersTable>) o;
                        viewModel.getFarmerDetailsListLiveData().removeObserver(this);
                        if (odVisitSurveyTableList != null && odVisitSurveyTableList.size() > 0) {

                            farmerDetailsListAdapter = new FarmerDetailsListAdapter(DashBoardFarmerListActivity.this,
                                    odVisitSurveyTableList, DashBoardFarmerListActivity.this, appHelper, viewModel);
                            recycler.setAdapter(farmerDetailsListAdapter);
                            farmerDetailsListAdapter.notifyDataSetChanged();

//                            if (odVisitSurveyTableList != null && odVisitSurveyTableList.size() > 0) {
//
//                                shimmerFrameLayout.stopShimmerAnimation();
//                                shimmerFrameLayout.setVisibility(View.GONE);
//
//                                farmerDetailsListAdapter = new FarmerDetailsListAdapter(RegisterdFarmerListActivity.this,
//                                        odVisitSurveyTableList, RegisterdFarmerListActivity.this, appHelper, viewModel);
//                                rvFarmerList.setAdapter(farmerDetailsListAdapter);
//                                farmerDetailsListAdapter.notifyDataSetChanged();
//                            }else {
//                                shimmerFrameLayout.startShimmerAnimation();
//                                shimmerFrameLayout.setVisibility(View.VISIBLE);
//                            }

                        } else {
                            Toast.makeText(DashBoardFarmerListActivity.this, "no farmer list", Toast.LENGTH_SHORT).show();
                            // progressDialog.dismiss();
                            // appHelper.getDialogHelper().getLoadingDialog().closeDialog();
//                            appHelper.getDialogHelper().getConfirmationDialog().show(ConfirmationDialog.ALERT, "No Farmer list");
                        }
                    }
                };
                viewModel.getFarmerDetailsListLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();

//            shimmerFrameLayout.startShimmerAnimation();
//            shimmerFrameLayout.setVisibility(View.VISIBLE);
            //progressDialog.dismiss();
            // appHelper.getDialogHelper().getLoadingDialog().closeDialog();
//            INSERT_LOG("getFarmerlistFromLocalDb", "Exception : " + ex.getMessage());
        }
    }

    //Todo:Dialog from recyclerView
    @SuppressLint("SetTextI18n")
    private void mainNavDialog(FarmersTable farmerTable1, String villageId) {
        String selectedLanguage = getSelectedLanguage();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        dialog = new Dialog(DashBoardFarmerListActivity.this, R.style.MyAlertDialogThemeNew);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.farmer_dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        TextView selectedFarm = dialog.findViewById(R.id.selectedFarm);
        TextView adddetails = dialog.findViewById(R.id.adddetailsd);
        TextView editpersonald = dialog.findViewById(R.id.editpersonald);
        TextView plantationd = dialog.findViewById(R.id.plantationd);
        TextView documend = dialog.findViewById(R.id.documend);
        TextView surveyd = dialog.findViewById(R.id.surveyd);
        TextView farmer_mapping = dialog.findViewById(R.id.farmer_mapping);

        selectedFarm.setText("Farmer Code : " + farmerCode);

        updateButtonLabels(selectedLanguage, adddetails, editpersonald, plantationd, documend, surveyd,farmer_mapping);

        LinearLayout farmerEdit = dialog.findViewById(R.id.ll_farmer_reg);
        LinearLayout plantationDetails = dialog.findViewById(R.id.ll_land_details);
        LinearLayout docDetails = dialog.findViewById(R.id.ll_doc_details);
        LinearLayout surveyDetails = dialog.findViewById(R.id.ll_survey_details);
        LinearLayout farmerMapping = dialog.findViewById(R.id.ll_mapping_details);

        farmerEdit.setOnClickListener(view -> {
            Intent intent = new Intent(DashBoardFarmerListActivity.this, EditPersonalDetailsActivity.class);
            intent.putExtra("mFarmerCode", farmerCode);
            intent.putExtra("farmData", farmerTable1);
            intent.putExtra("villageId",villageId);
            startActivity(intent);
            dialog.dismiss();
        });

        plantationDetails.setOnClickListener(view -> {
            Intent intent = new Intent(DashBoardFarmerListActivity.this, PlantationHomeActivity.class);
            intent.putExtra("mFarmerCode", farmerCode);
            startActivity(intent);
            dialog.dismiss();
        });

        docDetails.setOnClickListener(view -> {
            Intent intent = new Intent(DashBoardFarmerListActivity.this, DocumentHomeActivity.class);
            intent.putExtra("mFarmerCode", farmerCode);
            intent.putExtra("farmData", farmerTable1);
            intent.putExtra("villageId",villageId);
            startActivity(intent);
            dialog.dismiss();
        });

        surveyDetails.setOnClickListener(view -> {
            Intent intent = new Intent(DashBoardFarmerListActivity.this, SurveyActivity.class);
            intent.putExtra("mFarmerCode", farmerCode);
            intent.putExtra("mFarmerObj", farmerTable1);
            startActivity(intent);
            dialog.dismiss();
        });

        farmerMapping.setOnClickListener(view -> {
            Intent intent = new Intent(DashBoardFarmerListActivity.this, FarmerMappingActivity.class);
            intent.putExtra("mFarmerCode", farmerCode);
            intent.putExtra("mFarmerObj", farmerTable1);
            startActivity(intent);
            dialog.dismiss();

//            if (farmerManufacturerStatus ==0) {
//                if (farmerDealerStatus==0){
//                } else {
//                    Toast.makeText(this, "Mapping already done!!", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Toast.makeText(this, "Mapping already done!!", Toast.LENGTH_SHORT).show();
//            }
        });


        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void updateButtonLabels(String selectedLanguage, TextView adddetails, TextView editpersonald, TextView plantationd, TextView documend, TextView surveyd,TextView farmer_mapping) {

        //Todo lang
        String hdAddDetails=getResources().getString(R.string.adddetails);
        String hdEditPersonal=getResources().getString(R.string.editpersonald);
        String hdPlantation=getResources().getString(R.string.plantationd);
        String hdDocument=getResources().getString(R.string.documend);
        String hdSurvey=getResources().getString(R.string.surveyd);
        String hdFarmerMapping=getResources().getString(R.string.farmer_mapping);

        if (selectedLanguage.equals("English")) {
            adddetails.setText(hdAddDetails);
            editpersonald.setText(hdEditPersonal);
            plantationd.setText(hdPlantation);
            documend.setText(hdDocument);
            surveyd.setText(hdSurvey);
            farmer_mapping.setText(hdFarmerMapping);
        } else {
            adddetails.setText(getLanguageFromLocalDb(selectedLanguage, hdAddDetails) + "/" + hdAddDetails);
            editpersonald.setText(getLanguageFromLocalDb(selectedLanguage, hdEditPersonal) + "/" + hdEditPersonal);
            plantationd.setText(getLanguageFromLocalDb(selectedLanguage, hdPlantation) + "/" + hdPlantation);
            documend.setText(getLanguageFromLocalDb(selectedLanguage, hdDocument) + "/" + hdDocument);
            surveyd.setText(getLanguageFromLocalDb(selectedLanguage, hdSurvey) + "/" + hdSurvey);
            farmer_mapping.setText(getLanguageFromLocalDb(selectedLanguage, hdFarmerMapping) + "/" + hdFarmerMapping);
        }

    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void openScreenCallback(int position, FarmersTable farmerTable, List<FarmersTable> farmer, String applicationType) {

    }

    @Override
    public void updateItemCallback(int position, FarmersTable applicationStatusTable, String strFarmerID) {

    }

    @Override
    public void addPlotDetailsCallback(int position, FarmersTable applicationStatusTable, String strFarmercode, ImageView imgFarmer) {
        imageOne = imgFarmer;
        farmerCode = applicationStatusTable.getFarmerCode();
        mainNavDialog(applicationStatusTable,applicationStatusTable.getVillageId());
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return null;
    }


    @Override
    public void onResume() {
        super.onResume();
//        shimmerFrameLayout.startShimmerAnimation();
//        For refresh farmer list
        try {
//            searchByName.setQuery("", false);
//            searchByName.setIconified(true);
//            getFarmerlistFromLocalDb();
//             farmerDetailsListAdapter.notifyDataSetChanged();//update view
        } catch (Exception ex) {
            ex.printStackTrace();
//            INSERT_LOG("onResume", "Exception : " + ex.getMessage());
        }

//        getFarmerDealerStatus(farmerCode);
//        getFarmerManufacturerStatus(farmerCode);
    }

    private void openCameraPermission(Boolean land, Integer pictureCount) {
        testPictureCount = pictureCount;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (!CommonUtils.isPermissionAllowed(DashBoardFarmerListActivity.this, Manifest.permission.CAMERA))) {
            android.util.Log.v("AddSoilHealthActivity", "Location Permissions Not Granted");
            ActivityCompat.requestPermissions(
                    DashBoardFarmerListActivity.this,
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
                    strFarmerLocalImage = null;
                    try {
                        f = setUpPhotoFile();
                        strFarmerLocalImage = f.getAbsolutePath();

                        strFileExtension = f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf("."));

                        Uri photoURI = FileProvider.getUriForFile(DashBoardFarmerListActivity.this,
                                BuildConfig.APPLICATION_ID + ".provider",
                                f);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    } catch (IOException e) {
                        e.printStackTrace();
                        f = null;
                        strFarmerLocalImage = null;
                    }
                }
                break;

            default:
                break;
        } // switch
        startActivityForResult(takePictureIntent, actionCode);
//        Intent i = new Intent();
//        i.setType("image/*");
//        i.setAction(Intent.ACTION_GET_CONTENT);
//
//        launchSomeActivity.launch(i);
    }

    private File setUpPhotoFile() throws IOException {

        File f;
        f = createImageFileFirst();

        if (testPictureCount == 0) {
            strFarmerLocalImage = f.getAbsolutePath();
        }

        return f;
    }

    private File createImageFileFirst() {
        File image = null;
        File mediaStorageDir = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mediaStorageDir = new File(DashBoardFarmerListActivity.this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "SRLand");
        } else
            mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "SRLandPictures");
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
                        handleBigCameraPhoto();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
//                    strImageOnePath = null;
//                    strImageOnePath = null;
                }
                break;


        } // switch
    }

    private void handleBigCameraPhoto() throws Exception {

        if (testPictureCount == 0) {

            if (strFarmerLocalImage != null) {
//                setPic();//When needed add
//                galleryAddPic();
            }
        }


    }

//    private void setPic() throws Exception {
//
//        /* There isn't enough memory to open up more than a couple camera photos */
//        /* So pre-scale the target bitmap into which the file is decoded */
//
//        /* Get the size of the ImageView */
//
//        if(testPictureCount==0){
////            int targetW = imageOne.getWidth();
////            int targetH = imageOne.getHeight();
//
//            /* Get the size of the image */
//            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//            bmOptions.inJustDecodeBounds = true;
//            BitmapFactory.decodeFile(strFarmerLocalImage, bmOptions);
////            int photoW = bmOptions.outWidth;
////            int photoH = bmOptions.outHeight;
//
//            /* Figure out which way needs to be reduced less */
////            int scaleFactor = 1;
////            if ((targetW > 0) || (targetH > 0)) {
////                scaleFactor = Math.min(photoW / targetW, photoH / targetH);
////            }
//
//            /* Set bitmap options to scale the image decode target */
////            bmOptions.inJustDecodeBounds = false;
////            bmOptions.inSampleSize = scaleFactor;
////            bmOptions.inPurgeable = true;
//
//            bitmapFarmer = BitmapFactory.decodeFile(strFarmerLocalImage, bmOptions);
////        if(!isLand){
////            bitmapPatta = BitmapFactory.decodeFile(strLandImagePath, bmOptions);
////        } else {
////
////        }
//            getBytesFromBitmap(bitmapFarmer);
//
////            ExifInterface ei = new ExifInterface(strFarmerLocalImage);
////            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
////                    ExifInterface.ORIENTATION_UNDEFINED);
//            /* Decode the JPEG file into a Bitmap */
//
//            //bitmapLand = ImageUtility.rotatePicture(90, bitmapLand);
////        imageLand.setImageBitmap(rotatedBitmap);
//            Bitmap rotatedBitmap = null;
//            rotatedBitmap = bitmapFarmer;
////            switch (orientation) {
////
////                case ExifInterface.ORIENTATION_ROTATE_90:
////                    rotatedBitmap = rotateImage(bitmapFarmer, 90);
////                    break;
////
////                case ExifInterface.ORIENTATION_ROTATE_180:
////                    rotatedBitmap = rotateImage(bitmapFarmer, 180);
////                    break;
////
////                case ExifInterface.ORIENTATION_ROTATE_270:
////                    rotatedBitmap = rotateImage(bitmapFarmer, 270);
////                    break;
////
////                case ExifInterface.ORIENTATION_NORMAL:
////                default:
////                    rotatedBitmap = bitmapFarmer;
////            }
//
//
////        bitmapLand = rotatedBitmap;
////        if(isLand){
//            bitmapFarmer = rotatedBitmap;
////            imageOne.setImageBitmap(rotatedBitmap);
////            imageOne.invalidate();
//
//            //Insert farmer image
//            viewModel.updateFarmerImage(strFarmerLocalImage,farmerCode);
//            {
//                // CommonConstants.FARMER_CODE = etFarmerCode.getText().toString().trim();
////                        String strFarmerCode = etFarmerCode.getText().toString().trim();
//                String strDocUrl = "data:image/png;base64," + ImageUtility.convertBitmapToString(bitmapFarmer);
//                String strLocalDoc = strFarmerLocalImage;
////                        Log.d("TAG", "onClick: file" + strImageFileExtension +strIdtype);
//                DocIdentiFicationDeatilsTable docIdentiFicationDeatilsTable = new DocIdentiFicationDeatilsTable();
//
//                docIdentiFicationDeatilsTable.setDocUrl(strDocUrl);
////                        savingFarmerProfieImagesTable.setDocUrl(strFarmerPic);
//                docIdentiFicationDeatilsTable.setFarmerCode(farmerCode);
//                docIdentiFicationDeatilsTable.setDocLocal(strFarmerLocalImage);
//                docIdentiFicationDeatilsTable.setDocExtension(strFileExtension);
//                docIdentiFicationDeatilsTable.setDocType("photo");
//                docIdentiFicationDeatilsTable.setSync(false);
//                docIdentiFicationDeatilsTable.setServerPost("0");
//                docIdentiFicationDeatilsTable.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
//                docIdentiFicationDeatilsTable.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
//                docIdentiFicationDeatilsTable.setIsActive("1");
//                String dateTime = appHelper.getCurrentDateTime(AppConstant.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
//                docIdentiFicationDeatilsTable.setCreatedDate(dateTime);
//                docIdentiFicationDeatilsTable.setUpdatedDate(dateTime);
//
//
//                viewModel.insertDoctable(docIdentiFicationDeatilsTable);
//
//                farmerDetailsListAdapter.notifyDataSetChanged();
//
//            }
//
////            txtFarmerImage.setText(strFarmerLocalImage);
////        } else {
////            bitmapPatta = rotatedBitmap;
////            imagePatta.setImageBitmap(rotatedBitmap);
////            imagePatta.invalidate();
////        }
//
//            /* Decode the JPEG file into a Bitmap */
////        bitmapLand = BitmapFactory.decodeFile(strLandImagePath, bmOptions);
////        getBytesFromBitmap(bitmapLand);
////        bitmapLand = ImageUtility.rotatePicture(90, bitmapLand);
////
//
//            /* There isn't enough memory to open up more than a couple camera photos */
//            /* So pre-scale the target bitmap into which the file is decoded */
//
//            /* Get the size of the ImageView */
//
//
//        }
//
//    }

    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private void galleryAddPic() {
        if (testPictureCount == 1) {
            Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            File f;
            f = new File(strImageOnePath);
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

    public void getDealerStatus(String fid){
        try {
            viewModel.getDealerFarmerDetailsFromLocalDbByFId(fid);
            if (viewModel.getDealerFarmerDetailsByIdLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<DealerFarmer> dealerFarmers = (List<DealerFarmer>) o;
                        viewModel.getDealerFarmerDetailsByIdLiveData().removeObserver(this);
                        if (dealerFarmers != null && dealerFarmers.size() > 0) {
//                            farmerDealerStatus =1;
//                            Log.e("dashListStat",dealerFarmers.toString());

                        } else {
//                            Toast.makeText(FarmerMappingActivity.this, "Mapping already done!!", Toast.LENGTH_SHORT).show();
//                            farmerDealerStatus=0;

                        }
                    }
                };
                viewModel.getDealerFarmerDetailsByIdLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getManufacturerStatus(String fid){
        try {
            viewModel.getManfacturerFarmerDetailsFromLocalDbByFId(fid);
            if (viewModel.getManfacturerFarmerDetailsByIdLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<ManfacturerFarmer> manfacturerFarmers = (List<ManfacturerFarmer>) o;
                        viewModel.getManfacturerFarmerDetailsByIdLiveData().removeObserver(this);
                        if (manfacturerFarmers != null && manfacturerFarmers.size() > 0) {
//                            farmerManufacturerStatus =1;
//                            Log.e("dashListStat",manfacturerFarmers.toString());
                        } else {
//                            farmerManufacturerStatus=0;
//                            Toast.makeText(FarmerMappingActivity.this, "Mapping already done!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                viewModel.getManfacturerFarmerDetailsByIdLiveData().observe(DashBoardFarmerListActivity.this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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