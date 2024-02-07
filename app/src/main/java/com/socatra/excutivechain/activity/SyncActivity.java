package com.socatra.excutivechain.activity;


import static android.provider.ContactsContract.Directory.PACKAGE_NAME;
import static com.socatra.excutivechain.AppConstant.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS;
import static com.socatra.excutivechain.AppConstant.DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_SSS;
import static com.socatra.excutivechain.AppConstant.DB_NAME;
import static com.socatra.excutivechain.AppConstant.DB_VERSION;
import static com.socatra.excutivechain.AppConstant.DeviceUserID;
import static com.socatra.excutivechain.AppConstant.SUCCESS_RESPONSE_MESSAGE;
import static com.socatra.excutivechain.AppConstant.accessToken;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.socatra.excutivechain.BaseActivity;
import com.socatra.excutivechain.CommonUtils;
import com.socatra.excutivechain.R;
import com.socatra.excutivechain.adapters.LanguageAdapter;
import com.socatra.excutivechain.api.AppAPI;
import com.socatra.excutivechain.database.entity.AppLanguageHDRTable;
import com.socatra.excutivechain.database.entity.AppLanguageTable;
import com.socatra.excutivechain.database.entity.Country;
import com.socatra.excutivechain.database.entity.DealerFarmer;
import com.socatra.excutivechain.database.entity.DealerMaster;
import com.socatra.excutivechain.database.entity.DistrictorRegency;
import com.socatra.excutivechain.database.entity.FarmerHouseholdChildrenSurvey;
import com.socatra.excutivechain.database.entity.FarmerHouseholdParentSurvey;
import com.socatra.excutivechain.database.entity.FarmersTable;
import com.socatra.excutivechain.database.entity.ManfacturerFarmer;
import com.socatra.excutivechain.database.entity.ManufacturerMaster;
import com.socatra.excutivechain.database.entity.Plantation;
import com.socatra.excutivechain.database.entity.PlantationDocuments;
import com.socatra.excutivechain.database.entity.PlantationGeoBoundaries;
import com.socatra.excutivechain.database.entity.PlantationLabourSurvey;
import com.socatra.excutivechain.database.entity.RiskAssessment;
import com.socatra.excutivechain.database.entity.RiskAssessmentQuestion;
import com.socatra.excutivechain.database.entity.StateorProvince;
import com.socatra.excutivechain.database.entity.SubDistrict;
import com.socatra.excutivechain.database.entity.VillageTable;
import com.socatra.excutivechain.repositories.Retrofit_funtion_class;
import com.socatra.excutivechain.view_models.AppViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncActivity extends BaseActivity implements LanguageAdapter.LanguageCallbackInterface {
    private static final String TAG = SyncActivity.class.getCanonicalName();
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    public AppViewModel viewModel;
    private SharedPreferences preferences;
    ImageView imgBack;
    TextView txtFarmerSync,txtPlotSync,txtBankSync,txtDocSync,txtGpsSync, txtSurvey, txtParentSync,txtRiskSync,
            txtMasterSync,txtSendData,txtGetData,txtUploadDb, txtChildSync,txtDealerSync,txtManufacturerSync,
            changeLanguageButton;
    ProgressBar progressBar;
    Dialog dialog;

    LanguageAdapter languageAdapter;
    List<AppLanguageHDRTable> appLanguageHDRTable;

    boolean languageChanged=false;

    long nanoTime;
    String strDBName,strDBSubName,strMainDbFile;

    TextView txtAppVersion;

    String appVersion="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);

        initializeUI();
        initializeValues();
        configureDagger();
        configureViewModel();


        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        strDBName = appHelper.getSharedPrefObj().getString(DB_NAME,"");
        strDBSubName = strDBName + "_DB";
        strMainDbFile = strDBName + ".db";
        appLanguageHDRTable=new ArrayList<>();

        getAllLanguagesFromHDR();

        changeLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLanguageSelectionDialog();
            }
        });

        updateButtonLabels();


    }

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
                            appLanguageHDRTable=list;

                        } else {

                        }
                    }
                };
                viewModel.getAllLanguagesFromHDRLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showLanguageSelectionDialog() {
        final Dialog languageDialog = new Dialog(this,R.style.MyAlertDialogThemeNew);
        languageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        languageDialog.setContentView(R.layout.dialog_language_selection);
        languageDialog.setContentView(R.layout.dialog_language_selection_new);//for dynamic list
        languageDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        languageDialog.setCanceledOnTouchOutside(true);
        languageDialog.setCancelable(true);

        //For dynamic list
        RecyclerView recycleLang=languageDialog.findViewById(R.id.selectLangRecycle);
        recycleLang.setLayoutManager(new LinearLayoutManager(this));
        languageAdapter=new LanguageAdapter(appLanguageHDRTable,preferences,getSelectedLanguage(),languageDialog,this);//set list and content
        recycleLang.setAdapter(languageAdapter);

//        Button englishButton = languageDialog.findViewById(R.id.englishButtonDialog);
//        englishButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                saveSelectedLanguage("English");
//                showToast("English selected");
//                languageDialog.dismiss();
//                recreateApp();
//            }
//        });
//
//        Button hindiButton = languageDialog.findViewById(R.id.hindiButtonDialog);
//        hindiButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                saveSelectedLanguage("Hindi");
//                showToast("Hindi selected");
//                languageDialog.dismiss();
//                recreateApp();
//            }
//        });
//
//        Button vietnamButton = languageDialog.findViewById(R.id.vietnamButtonDialog);
//        vietnamButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                saveSelectedLanguage("Vietnamese");
//                showToast("Vietnamese selected");
//                languageDialog.dismiss();
//                recreateApp();
//            }
//        });
//
//        Button thaiButton = languageDialog.findViewById(R.id.thaiButtonDialog);
//        thaiButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                saveSelectedLanguage("Thai");
//                showToast("Thai selected");
//                languageDialog.dismiss();
//                recreateApp();
//            }
//        });
//        Button malayButton = languageDialog.findViewById(R.id.MalayButtonDialog);
//        malayButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                saveSelectedLanguage("Malay");
//                showToast("Malay selected");
//                languageDialog.dismiss();
//                recreateApp();
//            }
//        });
//        Button indonesianButton = languageDialog.findViewById(R.id.IndonesianButtonDialog);
//        indonesianButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                saveSelectedLanguage("Indonesian");
//                showToast("Indonesian selected");
//                languageDialog.dismiss();
//                recreateApp();
//            }
//        });

        languageDialog.show();
    }



    private void recreateApp() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }



//    private void saveSelectedLanguage(String language) {
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString("selected_language", language);
//        editor.apply();
////refresh activity
//        recreateApp();
//    }
    @Override
    public void onBackPressed() {
        if (languageChanged==true){
            Intent intent = new Intent(this, DashBoardFarmerListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            startActivity(intent);
//            finish(); // Finish the SyncActivity
        } else {
//            Intent intent = new Intent(this, DashBoardFarmerListActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
            finish(); // Finish the SyncActivity
        }
    }


    private void initializeUI() {
        progressBar = findViewById(R.id.progress);
        imgBack = findViewById(R.id.imgBack);
        txtFarmerSync = findViewById(R.id.txtFarmerSync);
        txtPlotSync = findViewById(R.id.txtPlotSync);
        txtSurvey = findViewById(R.id.txtSurvey);
        txtBankSync = findViewById(R.id.txtBankSync);
        txtDocSync = findViewById(R.id.txtDocSync);
        txtGpsSync = findViewById(R.id.txtGpsSync);
        txtChildSync = findViewById(R.id.txtChildSync);
        txtParentSync = findViewById(R.id.txtParentSync);
        txtRiskSync = findViewById(R.id.txtRiskSync);
//        txtMasterSync = findViewById(R.id.txtMasterSync);
//        txtSendData = findViewById(R.id.txtSendData);
//        txtGetData = findViewById(R.id.txtGetData);
//        txtUploadDb = findViewById(R.id.txtUploadDb);
        txtDealerSync = findViewById(R.id.txtDealerSync);
        txtManufacturerSync = findViewById(R.id.txtManufacturerSync);

        //Labels
        txtMasterSync = findViewById(R.id.txtMasterSync);
        txtSendData = findViewById(R.id.txtSendData);
        txtGetData = findViewById(R.id.txtGetData);
        txtUploadDb = findViewById(R.id.txtUploadDb);
        changeLanguageButton = findViewById(R.id.changeLanguageButton);
        txtAppVersion = findViewById(R.id.versionTxt);

    }



    private String getSelectedLanguage() {
        return preferences.getString("selected_language", "English");
    }

    @SuppressLint("SetTextI18n")
    private void updateButtonLabels() {
        String selectedLanguage = getSelectedLanguage();

        String hdMasterSync=getResources().getString(R.string.masterdata1);
        String hdSendDataSync=getResources().getString(R.string.senddata1);
        String hdGetDataSync=getResources().getString(R.string.getdata1);
        String hdUploadDataSync=getResources().getString(R.string.uploaddata1);
        String hdChangeLanguageSync=getResources().getString(R.string.change_language);

        if (selectedLanguage.equals("English")) {
            txtMasterSync.setText(hdMasterSync);
            txtSendData.setText(hdSendDataSync);
            txtGetData.setText(hdGetDataSync);
            txtUploadDb.setText(hdUploadDataSync);
            changeLanguageButton.setText(hdChangeLanguageSync);
            // Update your UI elements' text accordingly
        } else {
            txtMasterSync.setText(getLanguageFromLocalDb(selectedLanguage,hdMasterSync) + "/" + hdMasterSync);
            txtSendData.setText(getLanguageFromLocalDb(selectedLanguage,hdSendDataSync) + "/" + hdSendDataSync);
            txtGetData.setText(getLanguageFromLocalDb(selectedLanguage,hdGetDataSync) + "/" + hdGetDataSync);
            txtUploadDb.setText(getLanguageFromLocalDb(selectedLanguage,hdUploadDataSync) + "/" + hdUploadDataSync);
            changeLanguageButton.setText(getLanguageFromLocalDb(selectedLanguage,hdChangeLanguageSync) + "/" + hdChangeLanguageSync);
        }
    }



    private void initializeValues() {
        //App version and DB version
        try {
                PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                appVersion = packageInfo.versionName;
                if (!TextUtils.isEmpty(appVersion)) {
                    txtAppVersion.setText("*App Version : "+appVersion+" || DB Version : "+ String.valueOf(DB_VERSION)+"*");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        txtMasterSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.deleteAllTablesFromLocalMaster();
                getMasterSyncFromServerDetails(appHelper.getSharedPrefObj().getString(accessToken, ""));
            }
        });
        txtGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.deleteTablesFromLocalTransactionData();
                getSyncFarmerAllDataFromServer();
            }
        });
        txtSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                syncFarmerData();
            }
        });
        txtUploadDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(SyncActivity.this, R.style.MyAlertDialogThemeNew);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.upload_data_base_dialog);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);
                dialog.show();

                TextView txtYes = dialog.findViewById(R.id.txt_yes_bt);
                TextView txtNo = dialog.findViewById(R.id.txt_cancel_bt);

                txtYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ProgressDialog progressDialog = new ProgressDialog(SyncActivity.this, R.style.AppCompatAlertDialogStyle);
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage("fetch db and uploading to server...");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.show();
                        newUploadDataBase(getDbFileToUpload(), progressDialog,dialog);
                    }
                });

              txtNo.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      dialog.dismiss();
                  }
              });
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                finish();
                onBackPressed();
            }
        });
    }

    public void newUploadDataBase(final File uploadNewFile, ProgressDialog progressDialog, Dialog dialog) {
        nanoTime = System.nanoTime();
        if (null != uploadNewFile) {

          //  final String filePathToSave = "/sdcard/SupplyChain_" + appHelper.getSharedPrefObj().getString(DeviceUserID, "") + "_" + nanoTime + "_v_" + CommonUtils.getAppVersion(SyncActivity.this) + ".gzip";

            final String filePathToSave = "/sdcard/"+"SCE_" + appHelper.getSharedPrefObj().getString(DeviceUserID, "") + "_" + nanoTime + "_v_" + CommonUtils.getAppVersion(SyncActivity.this) + ".gzip";

            final File toZipFile = getDbFileToUpload();
            getzipFile(toZipFile, filePathToSave, progressDialog,dialog);
        } else {
            Toast.makeText(SyncActivity.this, "file upload failed", Toast.LENGTH_SHORT).show();
        }
    }

    public File getDbFileToUpload() {
        try {
//            File dir = Environment.getExternalStorageDirectory();
//            File dbFileToUpload = new File("/sdcard/"+strDBName+strDBSubName+strMainDbFile);
//            File dbFileToUpload=new File("/sdcard/Android/data/"+ appHelper.getSharedPrefObj().getString(PACKAGE_NAME, "")+"com.socatra.excutivechain/db/"+DB_NAME);
            File dbFileToUpload=new File("/sdcard/Android/data/com.socatra.excutivechain/db/"+DB_NAME);
       //     File dbFileToUpload = new File("/sdcard/SupplyChain/SupplyChain_DB/SupplyChain_Prod_DB/SupplyChain.db");
            return dbFileToUpload;
        } catch (Exception e) {
            android.util.Log.w("Settings Backup", e);
        }
        return null;
    }
    public void getzipFile(File sourceFile, String destinaton_zip_filepath, ProgressDialog progressDialog, Dialog dialog) {
        //  final long nanoTime = System.nanoTime();
        byte[] buffer = new byte[1024];
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(destinaton_zip_filepath);
            GZIPOutputStream gzipOuputStream = new GZIPOutputStream(fileOutputStream);
            FileInputStream fileInput = new FileInputStream(sourceFile);
            int bytes_read;
            while ((bytes_read = fileInput.read(buffer)) > 0) {
                gzipOuputStream.write(buffer, 0, bytes_read);
            }
            fileInput.close();
            gzipOuputStream.finish();
            gzipOuputStream.close();
            System.out.println("The file was compressed successfully!");
            File dir = Environment.getExternalStorageDirectory();
            File uploadFile = new File(dir, "SCE_" + appHelper.getSharedPrefObj().getString(DeviceUserID, "") + "_" + nanoTime + "_v_" + CommonUtils.getAppVersion(SyncActivity.this) + ".gzip");
            if (uploadFile != null) {

                if (uploadFile.exists()) {

                    long fileSizeInBytes = uploadFile.length();
                    long fileSizeInKB = fileSizeInBytes / 1024;
//                    Log.e("SyncActTag","File size: " + fileSizeInKB + " KB");
                    if (fileSizeInKB<=10000){

                        uploadDataBaseTOServerAPI(uploadFile, progressDialog, dialog);

                    } else {
                        dialog.dismiss();
                        progressDialog.dismiss();
                        Toast.makeText(this, "File too big upload through portal!!", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Log.e("SyncActTag","File does not exist.");
                }

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void uploadDataBaseTOServerAPI(File uploadFile, ProgressDialog progressDialog, Dialog dialog) {

        Log.d(TAG, "uploadDataBaseTOServerAPI: " + uploadFile.getAbsolutePath());
        MultipartBody.Part file_pathDB = null;
        File file = new File(uploadFile.getAbsolutePath());
        // Parsing any Media type file
        RequestBody requestBody = RequestBody.create(MediaType.parse(getMimeType(uploadFile.getAbsolutePath())), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        Log.d("Filepath", ">>>>>>>>>>" + fileToUpload);
        RequestBody r_acces_token = RequestBody.create(MediaType.parse("multipart/form-data"),
                appHelper.getSharedPrefObj().getString(accessToken, ""));

        RequestBody r_userID = RequestBody.create(MediaType.parse("multipart/form-data"),
                appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
        Log.d(TAG, "uploadDataBaseTOServerAPIUserId: " + appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<ResponseBody> callRetrofit = null;
        callRetrofit = service.uploadDatabasefileDataToServer(r_userID, fileToUpload);
        callRetrofit.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    progressDialog.dismiss();
                    dialog.dismiss();
                    String strResponse = response.body().string();
                    Log.d(TAG, "onResponse: AppData " + response);
                    JSONObject json_object = new JSONObject(strResponse);
                    String message = "", status = "";
                    Log.e(TAG, "onResponse: data json" + json_object);
                    message = json_object.getString("message");
                    status = json_object.getString("status");
                    Log.d(TAG, "status " + status);
                    if (status.equals("1")) {
                        Toast.makeText(SyncActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else if (status.equals("0")) {
                        Toast.makeText(SyncActivity.this, message, Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                    Log.d("Error Call", ">>>>" + ex.toString());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
            }
        });
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    private void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
        try {
            refreshDBModuleCall();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void syncFarmerData() {
        //New Main
        getFarmerlistFromLocalDbCheckDBNotSync();
        getPlantationlistFromLocalDbCheckDBNotSync();
        getGeoBoundariesDetails();
        getIdentificationDetailsFromLocalDB();
        getSurveylistFromLocalDbCheckDBNotSync();
        getParentSurveylistFromLocalDbCheckDBNotSync();
        getChildSurveylistFromLocalDbCheckDBNotSync();
        getRisklistFromLocalDbCheckDBNotSync();
        getManufacturerlistFromLocalDbCheckDBNotSync();
        getDealerlistFromLocalDbCheckDBNotSync();

        refreshDBModuleCall();
    }

    private void refreshDBModuleCall() {

        //New
        getNotSyncFarmerCountFromLocalDB();
        getNotSyncPlantationCountFromLocalDB();
        getNotSyncGeoCountFromLocalDB();
        getNotSyncDocCountFromLocalDB();
        getNotSyncSurveyCountFromLocalDB();
        getNotSyncParentSurveyCountFromLocalDB();
        getNotSyncChildSurveyCountFromLocalDB();
        getNotSyncRiskCountFromLocalDB();
        getNotSyncManufacturerCountFromLocalDB();
        getNotSyncDealerCountFromLocalDB();

    }

    public void getNotSyncFarmerCountFromLocalDB() {
        try {
            viewModel.getNotSyncFarmerCountDataFromLocalDB().observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(@Nullable Integer integer) {
//                    notSyncFarmerCount = integer;
                    txtFarmerSync.setText(String.valueOf(integer));
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    // TODO: Plant Not sync count

    public void getNotSyncPlantationCountFromLocalDB() {
        try {
            viewModel.getNotSyncPlantationCountDataFromLocalDB().observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(@Nullable Integer integer) {
//                    notSyncPlotCount = integer;
                    txtPlotSync.setText(String.valueOf(integer));
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Labour Survey
    public void getNotSyncSurveyCountFromLocalDB() {

//        int[] total = {0};
//        int[] observationsCompleted = {0};
//
//        Observer<Integer> observer = new Observer<Integer>() {
//            @Override
//            public void onChanged(@Nullable Integer integer) {
//                total[0] += integer;
//                observationsCompleted[0]++;
//                if (observationsCompleted[0] == 3) { // Assuming there are 3 observations
//                    txtSurvey.setText(String.valueOf(total[0]));
//                }
//            }
//        };
//
//        viewModel.getNotSyncSurveyCountDataFromLocalDB().observe(this, observer);
//        viewModel.getNotSyncFarmerHouseholdParentSurveyCountDataFromLocalDB().observe(this, observer);
//        viewModel.getNotSyncFarmerHouseholdChildrenSurveyCountDataFromLocalDB().observe(this, observer);

        try {
            viewModel.getNotSyncSurveyCountDataFromLocalDB().observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(@Nullable Integer integer) {
//                    notSyncPlotCount = integer;
                    txtSurvey.setText(String.valueOf(integer));
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Parent Survey
    public void getNotSyncParentSurveyCountFromLocalDB() {
        try {
            viewModel.getNotSyncFarmerHouseholdParentSurveyCountDataFromLocalDB().observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(@Nullable Integer integer) {
//                    notSyncPlotCount = integer;
                    txtParentSync.setText(String.valueOf(integer));
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Child sync
    public void getNotSyncChildSurveyCountFromLocalDB() {
        try {
            viewModel.getNotSyncFarmerHouseholdChildrenSurveyCountDataFromLocalDB().observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(@Nullable Integer integer) {
//                    notSyncPlotCount = integer;
                    txtChildSync.setText(String.valueOf(integer));
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //RiskSync
    public void getNotSyncRiskCountFromLocalDB() {
        try {
            viewModel.getNotSyncRiskCountDataFromLocalDB().observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(@Nullable Integer integer) {
//                    notSyncPlotCount = integer;
                    txtRiskSync.setText(String.valueOf(integer));
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Manufacturer sync
    public void getNotSyncManufacturerCountFromLocalDB() {
        try {
            viewModel.getNotSyncManufacturerCountDataFromLocalDB().observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(@Nullable Integer integer) {
//                    notSyncPlotCount = integer;
                    txtManufacturerSync.setText(String.valueOf(integer));
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Dealer sync
    public void getNotSyncDealerCountFromLocalDB() {
        try {
            viewModel.getNotSyncDealerCountDataFromLocalDB().observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(@Nullable Integer integer) {
//                    notSyncPlotCount = integer;
                    txtDealerSync.setText(String.valueOf(integer));
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void getNotSyncDocCountFromLocalDB() {
        try {
            viewModel.getNotSyncDocCountDataFromLocalDB().observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(@Nullable Integer integer) {
//                    notSyncDocCount = integer;
                    txtDocSync.setText( String.valueOf(integer));
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    // TODO: Geo Not sync count
    public void getNotSyncGeoCountFromLocalDB() {
        try {
            viewModel.getNotSyncGeoCountDataFromLocalDB().observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(@Nullable Integer integer) {
//                    notSyncGeoCount = integer;
                    txtGpsSync.setText( String.valueOf(integer));
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void getMasterSyncFromServerDetails(String token) {
        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getMasterSyncDetailsFromServer(token);
        final ProgressDialog progressDialog = new ProgressDialog(SyncActivity.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Master Data From Server please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    String strResponse = String.valueOf(response.body());
                    JSONObject jsonArray = new JSONObject(strResponse);
                    Log.d("TAG", "onResponse: Json_array" + jsonArray);
                    if (jsonArray.length() > 0) {
                        try {
//                            prefs.edit().putBoolean("firstrun", false).commit();

                            JSONArray jsonCountryArray = jsonArray.getJSONArray("data").getJSONArray(0);
                            JSONArray jsonStateArray = jsonArray.getJSONArray("data").getJSONArray(1);
                            JSONArray jsonDistrictArray = jsonArray.getJSONArray("data").getJSONArray(2);
                            JSONArray jsonSubDistrictArray = jsonArray.getJSONArray("data").getJSONArray(3);
                            JSONArray jsonVillageArray = jsonArray.getJSONArray("data").getJSONArray(4);
                            JSONArray jsonRiskQuestionsArray = jsonArray.getJSONArray("data").getJSONArray(5);
                            JSONArray jsonDealerArray = jsonArray.getJSONArray("data").getJSONArray(6);
                            JSONArray jsonManufacturerArray = jsonArray.getJSONArray("data").getJSONArray(7);
                            JSONArray jsonAppLanguageHDRArray = jsonArray.getJSONArray("data").getJSONArray(8);
                            JSONArray jsonAppLanguageArray = jsonArray.getJSONArray("data").getJSONArray(9);

                            Log.e("jsonAppLanguageHDRArray",jsonAppLanguageHDRArray.toString());
                            Log.e("jsonAppLanguageArray",jsonAppLanguageArray.toString());

                            //For Country Master
                            for (int cont = 0; cont < jsonCountryArray.length(); cont++) {
                                JSONObject jsonObjectCountry = jsonCountryArray.getJSONObject(cont);

                                Log.d(TAG, "onResponse: country" + jsonObjectCountry);
                                Country countryTb = new Country();
                                countryTb.setId(jsonObjectCountry.getInt("Id"));
                                countryTb.setCode(jsonObjectCountry.getString("Code"));
                                countryTb.setName(jsonObjectCountry.getString("Name"));

                                countryTb.setCreatedDate(jsonObjectCountry.getString("CreatedDate"));
                                countryTb.setUpdatedDate(jsonObjectCountry.getString("UpdatedDate"));
                                countryTb.setIsActive(jsonObjectCountry.getBoolean("IsActive"));
                                countryTb.setCreatedByUserId(jsonObjectCountry.getInt("CreatedByUserId"));
                                countryTb.setUpdatedByUserId(jsonObjectCountry.getInt("UpdatedByUserId"));

                                viewModel.insertCountryListDetailIntoLocalDBQuery(countryTb);
                            }

                            //For StateorProvince Master
                            for (int st = 0; st < jsonStateArray.length(); st++) {
                                JSONObject jsonObjectState = jsonStateArray.getJSONObject(st);

                                Log.d(TAG, "onResponse: state" + jsonObjectState);
                                StateorProvince state = new StateorProvince();
                                state.setId(jsonObjectState.getInt("Id"));
                                state.setCode(jsonObjectState.getString("Code"));
                                state.setName(jsonObjectState.getString("Name"));
                                state.setCountryId(jsonObjectState.getInt("CountryId"));
                                state.setCreatedDate(jsonObjectState.getString("CreatedDate"));
                                state.setUpdatedDate(jsonObjectState.getString("UpdatedDate"));
                                state.setIsActive(jsonObjectState.getBoolean("IsActive"));
                                state.setCreatedByUserId(jsonObjectState.getInt("CreatedByUserId"));
                                state.setUpdatedByUserId(jsonObjectState.getInt("UpdatedByUserId"));

                                viewModel.insertStateListDetailIntoLocalDBQuery(state);
                            }

                            //For DistrictorRegency Master
                            for (int dst = 0; dst < jsonDistrictArray.length(); dst++) {
                                JSONObject jsonObjectDistrict = jsonDistrictArray.getJSONObject(dst);

                                Log.d(TAG, "onResponse: district" + jsonObjectDistrict);
                                DistrictorRegency districtorRegency = new DistrictorRegency();
                                districtorRegency.setId(jsonObjectDistrict.getInt("Id"));
                                districtorRegency.setCode(jsonObjectDistrict.getString("Code"));
                                districtorRegency.setName(jsonObjectDistrict.getString("Name"));
                                districtorRegency.setStateId(jsonObjectDistrict.getInt("StateId"));
                                districtorRegency.setCreatedDate(jsonObjectDistrict.getString("CreatedDate"));
                                districtorRegency.setUpdatedDate(jsonObjectDistrict.getString("UpdatedDate"));
                                districtorRegency.setIsActive(jsonObjectDistrict.getBoolean("IsActive"));
                                districtorRegency.setCreatedByUserId(jsonObjectDistrict.getInt("CreatedByUserId"));
                                districtorRegency.setUpdatedByUserId(jsonObjectDistrict.getInt("UpdatedByUserId"));

                                viewModel.insertDistrictListDetailIntoLocalDBQuery(districtorRegency);
                            }

                            //For SubDistrict Master
                            for (int sd = 0; sd < jsonSubDistrictArray.length(); sd++) {
                                JSONObject jsonObjectSubDistrict = jsonSubDistrictArray.getJSONObject(sd);

                                Log.d(TAG, "onResponse: subDidtrict" + jsonObjectSubDistrict);
                                SubDistrict subDistrict = new SubDistrict();
                                subDistrict.setId(jsonObjectSubDistrict.getInt("Id"));
                                subDistrict.setCode(jsonObjectSubDistrict.getString("Code"));
                                subDistrict.setName(jsonObjectSubDistrict.getString("Name"));
                                subDistrict.setDistrictId(jsonObjectSubDistrict.getInt("DistrictId"));
                                subDistrict.setCreatedDate(jsonObjectSubDistrict.getString("CreatedDate"));
                                subDistrict.setUpdatedDate(jsonObjectSubDistrict.getString("UpdatedDate"));
                                subDistrict.setIsActive(jsonObjectSubDistrict.getBoolean("IsActive"));
                                subDistrict.setCreatedByUserId(jsonObjectSubDistrict.getInt("CreatedByUserId"));
                                subDistrict.setUpdatedByUserId(jsonObjectSubDistrict.getInt("UpdatedByUserId"));

                                viewModel.insertSubDistrictListDetailIntoLocalDBQuery(subDistrict);
                            }

                            //For Village Master
                            for (int village = 0; village < jsonVillageArray.length(); village++) {
                                JSONObject jsonObjectVillage = jsonVillageArray.getJSONObject(village);

                                Log.d(TAG, "onResponse: village" + jsonObjectVillage);

                                VillageTable village_value = new VillageTable();
                                village_value.setId(jsonObjectVillage.getString("Id"));
                                village_value.setCode(jsonObjectVillage.getString("Code"));
                                village_value.setName(jsonObjectVillage.getString("Name"));
                                village_value.setSubDistrictId(jsonObjectVillage.getString("SubDistrictId"));
                                village_value.setPinCode(jsonObjectVillage.getString("PinCode"));

                                village_value.setCreatedDate(jsonObjectVillage.getString("CreatedDate"));
                                village_value.setUpdatedDate(jsonObjectVillage.getString("UpdatedDate"));
                                village_value.setIsActive("1");
                                village_value.setCreatedByUserId(jsonObjectVillage.getString("CreatedByUserId"));
                                village_value.setUpdatedByUserId(jsonObjectVillage.getString("UpdatedByUserId"));

                                viewModel.insertVillageListDetailIntoLocalDBQuery(village_value);
                            }


                            //For Risk Question Master
                            for (int quest = 0; quest < jsonRiskQuestionsArray.length(); quest++) {
                                JSONObject jsonObjectRisk = jsonRiskQuestionsArray.getJSONObject(quest);
                                // JSONObject json_Object = json_arry.getJSONObject(27);
                                Log.d(TAG, "onResponse: RiskAssessmentQuestions" + jsonObjectRisk);
                                RiskAssessmentQuestion riskAssessmentQuestion = new RiskAssessmentQuestion();

                                riskAssessmentQuestion.setId(jsonObjectRisk.getInt("Id"));
                                riskAssessmentQuestion.setQuestionCategory(jsonObjectRisk.getString("QuestionCategory"));
                                riskAssessmentQuestion.setSerialNo(jsonObjectRisk.getInt("SerialNo"));
                                riskAssessmentQuestion.setQuestions(jsonObjectRisk.getString("Questions"));

                                riskAssessmentQuestion.setIsActive(jsonObjectRisk.getBoolean("IsActive"));
                                riskAssessmentQuestion.setCreatedDate(jsonObjectRisk.getString("CreatedDate"));
                                riskAssessmentQuestion.setUpdatedDate(jsonObjectRisk.getString("UpdatedDate"));
                                riskAssessmentQuestion.setCreatedByUserId(jsonObjectRisk.getInt("CreatedByUserId"));
                                riskAssessmentQuestion.setUpdatedByUserId(jsonObjectRisk.getInt("UpdatedByUserId"));

                                viewModel.insertRiskAssessmentQuestionListDetailIntoLocalDBQuery(riskAssessmentQuestion);
                            }

                            //For Manufacturer Master
                            for (int manu = 0; manu < jsonManufacturerArray.length(); manu++) {
                                JSONObject jsonObjectManufacturer = jsonManufacturerArray.getJSONObject(manu);
                                // JSONObject json_Object = json_arry.getJSONObject(27);
                                Log.d(TAG, "onResponse: jsonObjectManufacturer" + jsonObjectManufacturer);
                                ManufacturerMaster manufacturerMaster = new ManufacturerMaster();

                                manufacturerMaster.setId(jsonObjectManufacturer.getInt("Id"));
                                manufacturerMaster.setEntityName(jsonObjectManufacturer.getString("EntityName"));
                                manufacturerMaster.setPrimaryContactName(jsonObjectManufacturer.getString("PrimaryContactName"));
                                manufacturerMaster.setPrimaryContactNo(jsonObjectManufacturer.getString("PrimaryContactNo"));
                                manufacturerMaster.setSecondaryContactNo(jsonObjectManufacturer.getString("SecondaryContactNo"));

                                manufacturerMaster.setLatitude(jsonObjectManufacturer.getDouble("Latitude"));
                                manufacturerMaster.setLongitude(jsonObjectManufacturer.getDouble("Longitude"));

                                manufacturerMaster.setAddress(jsonObjectManufacturer.getString("Address"));
                                manufacturerMaster.setVillageId(jsonObjectManufacturer.getInt("VillageId"));



                                manufacturerMaster.setIsActive(jsonObjectManufacturer.getBoolean("IsActive"));
                                manufacturerMaster.setCreatedDate(jsonObjectManufacturer.getString("CreatedDate"));
                                manufacturerMaster.setUpdatedDate(jsonObjectManufacturer.getString("UpdatedDate"));
                                manufacturerMaster.setCreatedByUserId(jsonObjectManufacturer.getInt("CreatedByUserId"));
                                manufacturerMaster.setUpdatedByUserId(jsonObjectManufacturer.getInt("UpdatedByUserId"));

                                viewModel.insertManufacturerMasterDetailIntoLocalDBQuery(manufacturerMaster);
                            }


                            //For Dealer Master
                            for (int del = 0; del < jsonDealerArray.length(); del++) {
                                JSONObject jsonObjectDealer = jsonDealerArray.getJSONObject(del);
                                // JSONObject json_Object = json_arry.getJSONObject(27);
                                Log.d(TAG, "onResponse: jsonObjectDealer" + jsonObjectDealer);
                                DealerMaster dealerMaster = new DealerMaster();

                                dealerMaster.setId(jsonObjectDealer.getInt("Id"));
                                dealerMaster.setName(jsonObjectDealer.getString("Name"));
                                dealerMaster.setPrimaryContactNo(jsonObjectDealer.getString("PrimaryContactNo"));
                                dealerMaster.setAddress(jsonObjectDealer.getString("Address"));
                                dealerMaster.setVillage(jsonObjectDealer.getInt("Village"));
                                dealerMaster.setLatitude(jsonObjectDealer.getDouble("Latitude"));
                                dealerMaster.setLongitute(jsonObjectDealer.getDouble("Longitute"));

                                dealerMaster.setIsSubDealer(jsonObjectDealer.getString("IsSubDealer"));
                                dealerMaster.setIsMapping(jsonObjectDealer.getString("IsMapping"));


                                dealerMaster.setIsActive(jsonObjectDealer.getBoolean("IsActive"));
                                dealerMaster.setCreatedDate(jsonObjectDealer.getString("CreatedDate"));
                                dealerMaster.setUpdatedDate(jsonObjectDealer.getString("UpdatedDate"));
                                dealerMaster.setCreatedByUserId(jsonObjectDealer.getInt("CreatedByUserId"));
                                dealerMaster.setUpdatedByUserId(jsonObjectDealer.getInt("UpdatedByUserId"));

                                viewModel.insertDealerMasterDetailIntoLocalDBQuery(dealerMaster);
                            }

                            //For App language HDR
                            for (int i=0;i<jsonAppLanguageHDRArray.length();i++){
                                JSONObject jsonObjectAppLangHDR = jsonAppLanguageHDRArray.getJSONObject(i);

                                Log.d(TAG, "onResponse: jsonObjectAppLangHDR" + jsonObjectAppLangHDR);

                                AppLanguageHDRTable appLanguageHDRTable= new AppLanguageHDRTable();

                                appLanguageHDRTable.setLanguageId(jsonObjectAppLangHDR.getInt("Id"));
                                appLanguageHDRTable.setLanguageName(jsonObjectAppLangHDR.getString("LanguageName"));

                                appLanguageHDRTable.setIsActive(jsonObjectAppLangHDR.getBoolean("IsActive"));
                                appLanguageHDRTable.setCreatedByUserId(jsonObjectAppLangHDR.getInt("CreatedByUserId"));
                                appLanguageHDRTable.setUpdatedByUserId(jsonObjectAppLangHDR.getInt("UpdatedByUserId"));
                                appLanguageHDRTable.setCreatedDate(jsonObjectAppLangHDR.getString("CreatedDate"));
                                appLanguageHDRTable.setUpdatedDate(jsonObjectAppLangHDR.getString("UpdatedDate"));

                                viewModel.insertLanguageHDRMasterDetailIntoLocalDBQuery(appLanguageHDRTable);
                            }

                            //For App Language
                            for (int i=0;i< jsonAppLanguageArray.length();i++){

                                JSONObject jsonObjectAppLanguage = jsonAppLanguageArray.getJSONObject(i);
                                Log.d(TAG, "onResponse: jsonObjectAppLanguage" + jsonObjectAppLanguage);

                                AppLanguageTable appLanguageTable= new AppLanguageTable();

                                appLanguageTable.setLanguageId(jsonObjectAppLanguage.getInt("languageid"));
                                appLanguageTable.setSelectedLang(jsonObjectAppLanguage.getString("selectedLanguage"));
                                appLanguageTable.setSelectedWord(jsonObjectAppLanguage.getString("SelectedWord"));
                                appLanguageTable.setConvertedWord(jsonObjectAppLanguage.getString("TranslatedWord"));

                                appLanguageTable.setIsActive(jsonObjectAppLanguage.getBoolean("IsActive"));
                                appLanguageTable.setCreatedDate(jsonObjectAppLanguage.getString("CreatedDate"));
                                appLanguageTable.setUpdatedDate(jsonObjectAppLanguage.getString("UpdatedDate"));
                                appLanguageTable.setCreatedByUserId(jsonObjectAppLanguage.getInt("CreatedByUserId"));
                                appLanguageTable.setUpdatedByUserId(jsonObjectAppLanguage.getInt("UpdatedByUserId"));

                                viewModel.insertLanguageMasterDetailIntoLocalDBQuery(appLanguageTable);
                            }

                            progressDialog.dismiss();
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(SyncActivity.this, "Master Sync Successfully", Toast.LENGTH_LONG).show();

                            if (appHelper.isNetworkAvailable()) {
//                                progressBar.setVisibility(View.VISIBLE);
//                                txtLogin.setEnabled(false);
                            } else {
                                Toast.makeText(SyncActivity.this, "no internet connection for transaction sync", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Log.d("Error", ">>>>" + ex.toString());
                            progressDialog.dismiss();
                            Toast.makeText(SyncActivity.this, "Master Sync Failed, please contact admin", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    progressDialog.dismiss();
                    Log.d("Error", ">>>>" + ex.toString());
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());

            }
        });
    }

    public void getSyncFarmerAllDataFromServer() {
        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getFarmerAllSyncDataDetailsFromServer(CommonUtils.getIMEInumber(SyncActivity.this), appHelper.getSharedPrefObj().getString(accessToken, ""));
        final ProgressDialog progressDialog = new ProgressDialog(SyncActivity.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching All Data From Server please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    String strResponse = String.valueOf(response.body());
                    Log.d(TAG, "onResponse: >>>"+strResponse);
                    JSONObject jsonArray = new JSONObject(strResponse);
                    if (jsonArray.length() > 0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {//Todo Sync : Response sequence

                                    JSONArray jsonFarmerPDArray = jsonArray.getJSONArray("data").getJSONArray(0);
                                    JSONArray jsonPlotDetailsArray = jsonArray.getJSONArray("data").getJSONArray(1);
                                    JSONArray jsonPlantationDocDetailsArray = jsonArray.getJSONArray("data").getJSONArray(2);
                                    JSONArray jsonPlantationGeoDetailsArray = jsonArray.getJSONArray("data").getJSONArray(3);
                                    JSONArray jsonPlantationLabourSurveyArray = jsonArray.getJSONArray("data").getJSONArray(4);
                                    JSONArray jsonPlantationParentSurveyArray = jsonArray.getJSONArray("data").getJSONArray(5);
                                    JSONArray jsonPlantationChildSurveyArray = jsonArray.getJSONArray("data").getJSONArray(6);
                                    JSONArray jsonRiskAssessmentArray = jsonArray.getJSONArray("data").getJSONArray(7);
                                    JSONArray jsonDealerArray = jsonArray.getJSONArray("data").getJSONArray(8);
                                    JSONArray jsonManufacturerArray = jsonArray.getJSONArray("data").getJSONArray(9);

                                    //For Farmer
                                    if (jsonFarmerPDArray.length() != 0 || jsonFarmerPDArray.length() > 0) {
//                                        getPersoanlEmptyDataFromServer = false;

                                        for (int farmerPD = 0; farmerPD < jsonFarmerPDArray.length(); farmerPD++) {
                                            JSONObject jsonObjectFarmerPD = jsonFarmerPDArray.getJSONObject(farmerPD);
                                            FarmersTable farmerTable = new FarmersTable();

                                            farmerTable.setId(jsonObjectFarmerPD.getInt("Id"));
                                            farmerTable.setFarmerCode(jsonObjectFarmerPD.getString("FarmerCode"));
                                            farmerTable.setFirstName(jsonObjectFarmerPD.getString("FirstName"));
                                            farmerTable.setLastName(jsonObjectFarmerPD.getString("LastName"));
                                            farmerTable.setFatherName(jsonObjectFarmerPD.getString("FatherName"));
                                            farmerTable.setGender(jsonObjectFarmerPD.getString("Gender"));
                                            farmerTable.setAge(jsonObjectFarmerPD.getString("Age"));
                                            farmerTable.setPrimaryContactNo(jsonObjectFarmerPD.getString("PrimaryContactNo"));
                                            farmerTable.setAddress(jsonObjectFarmerPD.getString("Address"));

                                            farmerTable.setVillageId(jsonObjectFarmerPD.getString("VillageId"));
                                            farmerTable.setNoOfPlots(jsonObjectFarmerPD.getString("NoOfPlots"));
                                            try {
                                                farmerTable.setNationalIdentityCode(jsonObjectFarmerPD.getString("NationalIdentityCode"));
                                            } catch (Exception e){
                                                e.printStackTrace();
                                            }
                                            try {
                                                farmerTable.setNationalIdentityCodeDocument(jsonObjectFarmerPD.getString("NationalIdentityCodeDocument"));
                                            } catch (Exception e){
                                                e.printStackTrace();
                                            }

                                            farmerTable.setSync(true);
                                            farmerTable.setServerSync("1");


                                            try {
                                                farmerTable.setImage(jsonObjectFarmerPD.getString("Image"));
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }

                                            String dateTime = appHelper.getCurrentDateTime(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
                                            Log.d(TAG, "onClick: date" + dateTime);
                                            farmerTable.setIsActive("1");
                                            farmerTable.setUpdatedByUserId(jsonObjectFarmerPD.getString("UpdatedByUserId"));
                                            farmerTable.setCreatedByUserId(jsonObjectFarmerPD.getString("CreatedByUserId"));

                                            try {
                                                //  TimeZone utc = TimeZone.getTimeZone("UTC");
                                                SimpleDateFormat sourceFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_SSS);
                                                SimpleDateFormat destFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
                                                // sourceFormat.setTimeZone(utc);
                                                Date createDate = sourceFormat.parse(jsonObjectFarmerPD.getString("CreatedDate"));
                                                Date updatedDate = sourceFormat.parse(jsonObjectFarmerPD.getString("UpdatedDate"));
                                                farmerTable.setCreatedDate(destFormat.format(createDate));
                                                farmerTable.setUpdatedDate(destFormat.format(updatedDate));

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            viewModel.insertFarmerDetailListTableLocal(farmerTable);

                                        }
                                    } else {
                                    }


                                    //For Plantation
                                    if (jsonPlotDetailsArray.length() != 0 || jsonPlotDetailsArray.length() > 0) {
//                                        getPersoanlEmptyDataFromServer = false;

                                        for (int plt = 0; plt < jsonPlotDetailsArray.length(); plt++) {
                                            JSONObject jsonObjectFarmerPD = jsonPlotDetailsArray.getJSONObject(plt);
                                            Plantation plantation=new Plantation();

                                            plantation.setId(jsonObjectFarmerPD.getInt("Id"));
                                            plantation.setPlotCode(jsonObjectFarmerPD.getString("PlotCode"));
                                            plantation.setFarmerCode(jsonObjectFarmerPD.getString("FarmerCode"));
                                            plantation.setTypeOfOwnership(jsonObjectFarmerPD.getString("TypeOfOwnership"));

                                            plantation.setLabourStatus(jsonObjectFarmerPD.getString("LabourStatus"));
                                            plantation.setAreaInHectors(jsonObjectFarmerPD.getDouble("AreaInHectors"));
                                            plantation.setLatitude(jsonObjectFarmerPD.getDouble("Latitude"));
                                            plantation.setLongitude(jsonObjectFarmerPD.getDouble("Longitude"));
                                            //GeoboundariesArea
                                            try {
                                                plantation.setGeoboundariesArea(Double.valueOf(jsonObjectFarmerPD.getString("GeoboundariesArea")));
                                            } catch (Exception e){
                                                e.printStackTrace();
                                            }

                                            plantation.setAddress(jsonObjectFarmerPD.getString("Address"));
                                            plantation.setVillageId(jsonObjectFarmerPD.getString("VillageId"));
                                            plantation.setIsActive(jsonObjectFarmerPD.getString("IsActive"));


                                            plantation.setSync(true);
                                            plantation.setServerSync("1");


                                            try {

                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }

                                            String dateTime = appHelper.getCurrentDateTime(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
                                            Log.d(TAG, "onClick: date" + dateTime);

                                            plantation.setUpdatedByUserId(jsonObjectFarmerPD.getString("UpdatedByUserId"));
                                            plantation.setCreatedByUserId(jsonObjectFarmerPD.getString("CreatedByUserId"));

                                            try {
                                                //  TimeZone utc = TimeZone.getTimeZone("UTC");
                                                SimpleDateFormat sourceFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_SSS);
                                                SimpleDateFormat destFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
                                                // sourceFormat.setTimeZone(utc);
                                                Date createDate = sourceFormat.parse(jsonObjectFarmerPD.getString("CreatedDate"));
                                                Date updatedDate = sourceFormat.parse(jsonObjectFarmerPD.getString("UpdatedDate"));
                                                plantation.setCreatedDate(destFormat.format(createDate));
                                                plantation.setUpdatedDate(destFormat.format(updatedDate));

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            viewModel.insertPlantationDetailListTableLocal(plantation);

                                        }
                                    } else {
                                    }

                                    //For PlantationDoc
                                    if (jsonPlantationDocDetailsArray.length() != 0 || jsonPlantationDocDetailsArray.length() > 0) {
//                                        getPersoanlEmptyDataFromServer = false;

                                        for (int pltDoc = 0; pltDoc < jsonPlantationDocDetailsArray.length(); pltDoc++) {
                                            JSONObject jsonObjectPlantationDoc = jsonPlantationDocDetailsArray.getJSONObject(pltDoc);
                                            PlantationDocuments plantationDocuments=new PlantationDocuments();

                                            plantationDocuments.setFarmerCode(jsonObjectPlantationDoc.getString("FarmerCode"));
                                            plantationDocuments.setPlotCode(jsonObjectPlantationDoc.getString("PlotCode"));
                                            plantationDocuments.setDocURL(jsonObjectPlantationDoc.getString("DocURL"));
                                            plantationDocuments.setDocType(jsonObjectPlantationDoc.getString("DocType"));
                                            plantationDocuments.setDocUrlValue(jsonObjectPlantationDoc.getString("DocUrlValue"));

                                            plantationDocuments.setIsActive(jsonObjectPlantationDoc.getString("IsActive"));


                                            plantationDocuments.setSync(true);
                                            plantationDocuments.setServerSync("1");


                                            try {

                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }

                                            String dateTime = appHelper.getCurrentDateTime(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
                                            Log.d(TAG, "onClick: date" + dateTime);

                                            plantationDocuments.setUpdatedByUserId(jsonObjectPlantationDoc.getString("UpdatedByUserId"));
                                            plantationDocuments.setCreatedByUserId(jsonObjectPlantationDoc.getString("CreatedByUserId"));

                                            try {
                                                //  TimeZone utc = TimeZone.getTimeZone("UTC");
                                                SimpleDateFormat sourceFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_SSS);
                                                SimpleDateFormat destFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
                                                // sourceFormat.setTimeZone(utc);
                                                Date createDate = sourceFormat.parse(jsonObjectPlantationDoc.getString("CreatedDate"));
                                                Date updatedDate = sourceFormat.parse(jsonObjectPlantationDoc.getString("UpdatedDate"));
                                                plantationDocuments.setCreatedDate(destFormat.format(createDate));
                                                plantationDocuments.setUpdatedDate(destFormat.format(updatedDate));

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            viewModel.insertPlantationDocDetailListTableLocal(plantationDocuments);

                                        }
                                    } else {
                                    }

                                    //For Plantation Geo
                                    if (jsonPlantationGeoDetailsArray.length() != 0 || jsonPlantationGeoDetailsArray.length() > 0) {
//                                        getPersoanlEmptyDataFromServer = false;

                                        for (int pltGeo = 0; pltGeo < jsonPlantationGeoDetailsArray.length(); pltGeo++) {
                                            JSONObject jsonObjectPlantationGeo = jsonPlantationGeoDetailsArray.getJSONObject(pltGeo);
                                            PlantationGeoBoundaries plantationGeoBoundaries=new PlantationGeoBoundaries();

                                            plantationGeoBoundaries.setPlotCode(jsonObjectPlantationGeo.getString("PlotCode"));
                                            plantationGeoBoundaries.setFarmerCode(jsonObjectPlantationGeo.getString("FarmerCode"));
                                            plantationGeoBoundaries.setLatitude(jsonObjectPlantationGeo.getDouble("Latitude"));
                                            plantationGeoBoundaries.setLongitude(jsonObjectPlantationGeo.getDouble("Longitude"));
                                            plantationGeoBoundaries.setSeqNo(jsonObjectPlantationGeo.getInt("SeqNo"));
                                            plantationGeoBoundaries.setPlotCount(jsonObjectPlantationGeo.getInt("PlotCount"));

                                            plantationGeoBoundaries.setIsActive(jsonObjectPlantationGeo.getString("IsActive"));



                                            plantationGeoBoundaries.setSync(true);
                                            plantationGeoBoundaries.setServerSync("1");


                                            try {

                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }

                                            String dateTime = appHelper.getCurrentDateTime(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
                                            Log.d(TAG, "onClick: date" + dateTime);

                                            plantationGeoBoundaries.setUpdatedByUserId(jsonObjectPlantationGeo.getString("UpdatedByUserId"));
                                            plantationGeoBoundaries.setCreatedByUserId(jsonObjectPlantationGeo.getString("CreatedByUserId"));

                                            try {
                                                //  TimeZone utc = TimeZone.getTimeZone("UTC");
                                                SimpleDateFormat sourceFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_SSS);
                                                SimpleDateFormat destFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
                                                // sourceFormat.setTimeZone(utc);
                                                Date createDate = sourceFormat.parse(jsonObjectPlantationGeo.getString("CreatedDate"));
                                                Date updatedDate = sourceFormat.parse(jsonObjectPlantationGeo.getString("UpdatedDate"));
                                                plantationGeoBoundaries.setCreatedDate(destFormat.format(createDate));
                                                plantationGeoBoundaries.setUpdatedDate(destFormat.format(updatedDate));

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            viewModel.insertPlantationGeoDetailListTableLocal(plantationGeoBoundaries);

                                        }
                                    } else {
                                    }

                                    //For Labour Survey
                                    if (jsonPlantationLabourSurveyArray.length() != 0 || jsonPlantationLabourSurveyArray.length() > 0) {
//                                        getPersoanlEmptyDataFromServer = false;

                                        for (int lbr = 0; lbr < jsonPlantationLabourSurveyArray.length(); lbr++) {
                                            JSONObject jsonObjectLabourSurvey = jsonPlantationLabourSurveyArray.getJSONObject(lbr);
                                            PlantationLabourSurvey plantationLabourSurvey=new PlantationLabourSurvey();

                                            plantationLabourSurvey.setFarmerCode(jsonObjectLabourSurvey.getString("FarmerCode"));
                                            //plantationLabourSurvey.setPlantationId(jsonObjectLabourSurvey.getInt("PlantationId"));
                                            plantationLabourSurvey.setPlantationCode(jsonObjectLabourSurvey.getString("PlantationCode"));
                                            plantationLabourSurvey.setNoOfFieldWorkers(jsonObjectLabourSurvey.getInt("NoOfFieldWorkers"));

                                            plantationLabourSurvey.setNoOfFieldWorkers(jsonObjectLabourSurvey.getInt("NoOfFieldWorkers"));
                                            plantationLabourSurvey.setNoOfMaleWorkers(jsonObjectLabourSurvey.getInt("NoOfMaleWorkers"));
                                            plantationLabourSurvey.setNoOfFemaleWorkers(jsonObjectLabourSurvey.getInt("NoOfFemaleWorkers"));
                                            plantationLabourSurvey.setNoOfResident(jsonObjectLabourSurvey.getInt("NoOfResident"));
                                            plantationLabourSurvey.setNoOfMigrant(jsonObjectLabourSurvey.getInt("NoOfMigrant"));
                                            plantationLabourSurvey.setOccupationOfChildren(jsonObjectLabourSurvey.getString("OccupationOfChildren"));



                                            plantationLabourSurvey.setIsActive(jsonObjectLabourSurvey.getString("IsActive"));


                                            plantationLabourSurvey.setSync(true);
                                            plantationLabourSurvey.setServerSync("1");


                                            try {

                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }

                                            String dateTime = appHelper.getCurrentDateTime(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
                                            Log.d(TAG, "onClick: date" + dateTime);

                                            plantationLabourSurvey.setUpdatedByUserId(jsonObjectLabourSurvey.getString("UpdatedByUserId"));
                                            plantationLabourSurvey.setCreatedByUserId(jsonObjectLabourSurvey.getString("CreatedByUserId"));

                                            try {
                                                //  TimeZone utc = TimeZone.getTimeZone("UTC");
                                                SimpleDateFormat sourceFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_SSS);
                                                SimpleDateFormat destFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
                                                // sourceFormat.setTimeZone(utc);
                                                Date createDate = sourceFormat.parse(jsonObjectLabourSurvey.getString("CreatedDate"));
                                                Date updatedDate = sourceFormat.parse(jsonObjectLabourSurvey.getString("UpdatedDate"));
                                                plantationLabourSurvey.setCreatedDate(destFormat.format(createDate));
                                                plantationLabourSurvey.setUpdatedDate(destFormat.format(updatedDate));

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            viewModel.insertPlantationLabourSurveyListTableLocal(plantationLabourSurvey);

                                        }
                                    } else {
                                    }

                                    //For Parent Survey
                                    if (jsonPlantationParentSurveyArray.length() != 0 || jsonPlantationParentSurveyArray.length() > 0) {
//                                        getPersoanlEmptyDataFromServer = false;

                                        for (int pltGeo = 0; pltGeo < jsonPlantationParentSurveyArray.length(); pltGeo++) {
                                            JSONObject jsonObjectParentSurvey = jsonPlantationParentSurveyArray.getJSONObject(pltGeo);
                                            FarmerHouseholdParentSurvey farmerHouseholdParentSurvey=new FarmerHouseholdParentSurvey();

                                            farmerHouseholdParentSurvey.setFarmerCode(jsonObjectParentSurvey.getString("FarmerCode"));
                                            farmerHouseholdParentSurvey.setFarmerId(String.valueOf(jsonObjectParentSurvey.getInt("FarmerId")));
                                            farmerHouseholdParentSurvey.setFamilyCount(jsonObjectParentSurvey.getInt("FamilyCount"));
                                            farmerHouseholdParentSurvey.setMaritalStatus(jsonObjectParentSurvey.getString("MaritalStatus"));
                                            farmerHouseholdParentSurvey.setSpouseName(jsonObjectParentSurvey.getString("SpouseName"));
                                            farmerHouseholdParentSurvey.setAge(jsonObjectParentSurvey.getInt("Age"));
                                            farmerHouseholdParentSurvey.setGender(jsonObjectParentSurvey.getString("Gender"));
                                            farmerHouseholdParentSurvey.setOccupation(jsonObjectParentSurvey.getString("Occupation"));
                                            farmerHouseholdParentSurvey.setNoofChildren(jsonObjectParentSurvey.getInt("NoofChildren"));

                                            farmerHouseholdParentSurvey.setIsActive(jsonObjectParentSurvey.getString("IsActive"));

                                            farmerHouseholdParentSurvey.setSync(true);
                                            farmerHouseholdParentSurvey.setServerSync("1");


                                            try {

                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }

                                            String dateTime = appHelper.getCurrentDateTime(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
                                            Log.d(TAG, "onClick: date" + dateTime);

                                            farmerHouseholdParentSurvey.setUpdatedByUserId(jsonObjectParentSurvey.getString("UpdatedByUserId"));
                                            farmerHouseholdParentSurvey.setCreatedByUserId(jsonObjectParentSurvey.getString("CreatedByUserId"));

                                            try {
                                                //  TimeZone utc = TimeZone.getTimeZone("UTC");
                                                SimpleDateFormat sourceFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_SSS);
                                                SimpleDateFormat destFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
                                                // sourceFormat.setTimeZone(utc);
                                                Date createDate = sourceFormat.parse(jsonObjectParentSurvey.getString("CreatedDate"));
                                                Date updatedDate = sourceFormat.parse(jsonObjectParentSurvey.getString("UpdatedDate"));
                                                farmerHouseholdParentSurvey.setCreatedDate(destFormat.format(createDate));
                                                farmerHouseholdParentSurvey.setUpdatedDate(destFormat.format(updatedDate));

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            viewModel.insertFarmerHouseholdParentSurveyListTableLocal(farmerHouseholdParentSurvey);

                                        }
                                    } else {
                                    }

                                    //For Child Survey
                                    if (jsonPlantationChildSurveyArray.length() != 0 || jsonPlantationChildSurveyArray.length() > 0) {
//                                        getPersoanlEmptyDataFromServer = false;

                                        for (int pltGeo = 0; pltGeo < jsonPlantationChildSurveyArray.length(); pltGeo++) {
                                            JSONObject jsonObjectChildServey = jsonPlantationChildSurveyArray.getJSONObject(pltGeo);
                                            FarmerHouseholdChildrenSurvey farmerHouseholdChildrenSurvey=new FarmerHouseholdChildrenSurvey();

                                            farmerHouseholdChildrenSurvey.setFarmerCode(jsonObjectChildServey.getString("FarmerCode"));
                                            farmerHouseholdChildrenSurvey.setFarmerHouseholdSurveyId(jsonObjectChildServey.getInt("FarmerHouseholdSurveyId"));
                                            farmerHouseholdChildrenSurvey.setFarmerId(jsonObjectChildServey.getString("FarmerId"));
                                            farmerHouseholdChildrenSurvey.setChildrenName(jsonObjectChildServey.getString("ChildrenName"));
                                            farmerHouseholdChildrenSurvey.setChildrenGender(jsonObjectChildServey.getString("ChildrenGender"));
                                            farmerHouseholdChildrenSurvey.setChildrenAge(jsonObjectChildServey.getInt("ChildrenAge"));
                                            farmerHouseholdChildrenSurvey.setChildrenOccupation(jsonObjectChildServey.getString("ChildrenOccupation"));

                                            farmerHouseholdChildrenSurvey.setIsActive(jsonObjectChildServey.getString("IsActive"));


                                            farmerHouseholdChildrenSurvey.setSync(true);
                                            farmerHouseholdChildrenSurvey.setServerSync("1");


                                            try {

                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }

                                            String dateTime = appHelper.getCurrentDateTime(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
                                            Log.d(TAG, "onClick: date" + dateTime);

                                            farmerHouseholdChildrenSurvey.setUpdatedByUserId(jsonObjectChildServey.getString("UpdatedByUserId"));
                                            farmerHouseholdChildrenSurvey.setCreatedByUserId(jsonObjectChildServey.getString("CreatedByUserId"));

                                            try {
                                                //  TimeZone utc = TimeZone.getTimeZone("UTC");
                                                SimpleDateFormat sourceFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_SSS);
                                                SimpleDateFormat destFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
                                                // sourceFormat.setTimeZone(utc);
                                                Date createDate = sourceFormat.parse(jsonObjectChildServey.getString("CreatedDate"));
                                                Date updatedDate = sourceFormat.parse(jsonObjectChildServey.getString("UpdatedDate"));
                                                farmerHouseholdChildrenSurvey.setCreatedDate(destFormat.format(createDate));
                                                farmerHouseholdChildrenSurvey.setUpdatedDate(destFormat.format(updatedDate));

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            viewModel.insertFarmerHouseholdChildrenSurveyListTableLocal(farmerHouseholdChildrenSurvey);

                                        }
                                    } else {
                                    }


                                    //Risk Assessment Answers
                                    if (jsonRiskAssessmentArray.length() != 0 || jsonRiskAssessmentArray.length() > 0) {

                                        for (int pltGeo = 0; pltGeo < jsonRiskAssessmentArray.length(); pltGeo++) {
                                            JSONObject jsonObjectRiskAssessment = jsonRiskAssessmentArray.getJSONObject(pltGeo);
                                            RiskAssessment riskAssessment=new RiskAssessment();

                                            riskAssessment.setRiskAssesmentQuestionHdrId(jsonObjectRiskAssessment.getInt("RiskAssesmentQuestionHdrId"));
                                            riskAssessment.setFarmerCode(jsonObjectRiskAssessment.getString("FarmerCode"));
                                            riskAssessment.setAnswers(jsonObjectRiskAssessment.getString("Answers"));

                                            riskAssessment.setIsActive(jsonObjectRiskAssessment.getString("IsActive"));
                                            riskAssessment.setSync(true);
                                            riskAssessment.setServerSync("1");


                                            try {

                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }

                                            String dateTime = appHelper.getCurrentDateTime(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
                                            Log.d(TAG, "onClick: date" + dateTime);

                                            riskAssessment.setUpdatedByUserId(jsonObjectRiskAssessment.getString("UpdatedByUserId"));
                                            riskAssessment.setCreatedByUserId(jsonObjectRiskAssessment.getString("CreatedByUserId"));

                                            try {
                                                //  TimeZone utc = TimeZone.getTimeZone("UTC");
                                                SimpleDateFormat sourceFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_SSS);
                                                SimpleDateFormat destFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
                                                // sourceFormat.setTimeZone(utc);
                                                Date createDate = sourceFormat.parse(jsonObjectRiskAssessment.getString("CreatedDate"));
                                                Date updatedDate = sourceFormat.parse(jsonObjectRiskAssessment.getString("UpdatedDate"));
                                                riskAssessment.setCreatedDate(destFormat.format(createDate));
                                                riskAssessment.setUpdatedDate(destFormat.format(updatedDate));

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            viewModel.insertRiskAssessmentDataLocalDB(riskAssessment);

                                        }
                                    } else {
                                    }

                                    //Dealer sync
                                    if (jsonDealerArray.length() != 0 || jsonDealerArray.length() > 0) {

                                        for (int pltGeo = 0; pltGeo < jsonDealerArray.length(); pltGeo++) {
                                            JSONObject jsonObjectDealer = jsonDealerArray.getJSONObject(pltGeo);
                                            DealerFarmer dealerFarmer=new DealerFarmer();

                                            dealerFarmer.setDealerId(jsonObjectDealer.getInt("DealerId"));
                                            dealerFarmer.setFarmerCode(jsonObjectDealer.getString("FarmerCode"));

                                            dealerFarmer.setIsActive(jsonObjectDealer.getString("IsActive"));
                                            dealerFarmer.setSync(true);
                                            dealerFarmer.setServerSync("1");


                                            try {

                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }

                                            String dateTime = appHelper.getCurrentDateTime(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
                                            Log.d(TAG, "onClick: date" + dateTime);

                                            dealerFarmer.setUpdatedByUserId(jsonObjectDealer.getString("UpdatedByUserId"));
                                            dealerFarmer.setCreatedByUserId(jsonObjectDealer.getString("CreatedByUserId"));

                                            try {
                                                //  TimeZone utc = TimeZone.getTimeZone("UTC");
                                                SimpleDateFormat sourceFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_SSS);
                                                SimpleDateFormat destFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
                                                // sourceFormat.setTimeZone(utc);
                                                Date createDate = sourceFormat.parse(jsonObjectDealer.getString("CreatedDate"));
                                                Date updatedDate = sourceFormat.parse(jsonObjectDealer.getString("UpdatedDate"));
                                                dealerFarmer.setCreatedDate(destFormat.format(createDate));
                                                dealerFarmer.setUpdatedDate(destFormat.format(updatedDate));

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            viewModel.insertDealerFarmerDataLocalDB(dealerFarmer);

                                        }
                                    } else {
                                    }

                                    //Manufacturer sync
                                    if (jsonManufacturerArray.length() != 0 || jsonManufacturerArray.length() > 0) {

                                        for (int pltGeo = 0; pltGeo < jsonManufacturerArray.length(); pltGeo++) {
                                            JSONObject jsonObjectManufacturer = jsonManufacturerArray.getJSONObject(pltGeo);
                                            ManfacturerFarmer manfacturerFarmer=new ManfacturerFarmer();

                                            //for gaja
                                            manfacturerFarmer.setManfacturerId(jsonObjectManufacturer.getInt("ProcessorId"));
//                                            manfacturerFarmer.setManfacturerId(jsonObjectManufacturer.getInt("ManfacturerId"));
                                            manfacturerFarmer.setFarmerCode(jsonObjectManufacturer.getString("FarmerCode"));

                                            manfacturerFarmer.setIsActive(jsonObjectManufacturer.getString("IsActive"));
                                            manfacturerFarmer.setSync(true);
                                            manfacturerFarmer.setServerSync("1");


                                            try {

                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }

                                            String dateTime = appHelper.getCurrentDateTime(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
                                            Log.d(TAG, "onClick: date" + dateTime);

                                            manfacturerFarmer.setUpdatedByUserId(jsonObjectManufacturer.getString("UpdatedByUserId"));
                                            manfacturerFarmer.setCreatedByUserId(jsonObjectManufacturer.getString("CreatedByUserId"));

                                            try {
                                                //  TimeZone utc = TimeZone.getTimeZone("UTC");
                                                SimpleDateFormat sourceFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_SSS);
                                                SimpleDateFormat destFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
                                                // sourceFormat.setTimeZone(utc);
                                                Date createDate = sourceFormat.parse(jsonObjectManufacturer.getString("CreatedDate"));
                                                Date updatedDate = sourceFormat.parse(jsonObjectManufacturer.getString("UpdatedDate"));
                                                manfacturerFarmer.setCreatedDate(destFormat.format(createDate));
                                                manfacturerFarmer.setUpdatedDate(destFormat.format(updatedDate));

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            viewModel.insertManfacturerFarmerDataLocalDB(manfacturerFarmer);

                                        }
                                    } else {
                                    }


                                    Toast.makeText(SyncActivity.this, "Fetched All Data From Server SuccessFully", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();

                                }
                                catch (Exception ex) {
                                    ex.printStackTrace();
                                    Toast.makeText(SyncActivity.this, String.valueOf(ex.getMessage()), Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    progressDialog.dismiss();
                                    Log.d("Error", ">>>>" + ex.toString());
                                }
                            }
                        }, 2000);
                    } else {
                        Toast.makeText(SyncActivity.this, "no records found", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    progressDialog.dismiss();
                    Toast.makeText(SyncActivity.this, "Transaction Sync Failed, please contact admin", Toast.LENGTH_SHORT).show();
                    Log.d("Error", ">>>>" + ex.toString());
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
            }
        });
    }


    //Farmer not sync count
    public void getFarmerlistFromLocalDbCheckDBNotSync() {
        try {
            viewModel.getFarmerListFromLocalDBNotSync();
            if (viewModel.getFarmerDetailsListNotSyncLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<FarmersTable> odVisitSurveyTableList = (List<FarmersTable>) o;
                        viewModel.getFarmerDetailsListNotSyncLiveData().removeObserver(this);
                        if (odVisitSurveyTableList != null && odVisitSurveyTableList.size() > 0) {
                            for (FarmersTable farmerDetailListTable : odVisitSurveyTableList) {
                                // TODO: Need to remove for lead
                                syncFarmerPersonalDetailsToServer(farmerDetailListTable, odVisitSurveyTableList);
                            }
                        } else {

                        }
                    }

                };
                viewModel.getFarmerDetailsListNotSyncLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    // Farmer To server sync
    public void syncFarmerPersonalDetailsToServer(FarmersTable farmerTable, List<FarmersTable> odVisitSurveyTableList) {
        try {
            viewModel.syncFarmerDetailsDataToServer(farmerTable);
            if (viewModel.getFarmerLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        viewModel.getFarmerLiveData().removeObserver(this);
                        String dataResponse = (String) o;
                        if (dataResponse.equals(SUCCESS_RESPONSE_MESSAGE)) {
                            Toast.makeText(SyncActivity.this, "Farmer Personal Details are Submitted", Toast.LENGTH_SHORT).show();
                            Log.e("testProgress","Farmer Personal Details are Submitted 1");
//                            getFarmerProfileImageFromLocalDB();
                        } else {
//                            syncProgressDialog.dismiss();
//                            appHelper.getDialogHelper().getConfirmationDialog().show(ConfirmationDialog.ERROR, dataResponse);
                        }

                    }
                };
                viewModel.getFarmerLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    //Plantation not sync
    public void getPlantationlistFromLocalDbCheckDBNotSync() {
        try {
            viewModel.getPlantationListFromLocalDBNotSync();
            if (viewModel.getPlantationDetailsListNotSyncLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<Plantation> odVisitSurveyTableList = (List<Plantation>) o;
                        viewModel.getPlantationDetailsListNotSyncLiveData().removeObserver(this);
                        if (odVisitSurveyTableList != null && odVisitSurveyTableList.size() > 0) {
                            for (Plantation plantationListTable : odVisitSurveyTableList) {
                                // TODO: Need to remove for lead
                                syncPlantationDetailsToServer(plantationListTable, odVisitSurveyTableList);//need to change
                            }
                        } else {

                        }
                    }

                };
                viewModel.getPlantationDetailsListNotSyncLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Plantation To server sync
    public void syncPlantationDetailsToServer(Plantation plantation, List<Plantation> odVisitSurveyTableList) {
        try {
            viewModel.syncPlantationDataToServer(plantation);
            if (viewModel.getPlantationLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        viewModel.getPlantationLiveData().removeObserver(this);
                        String dataResponse = (String) o;
                        if (dataResponse.equals(SUCCESS_RESPONSE_MESSAGE)) {
                            Toast.makeText(SyncActivity.this, "Plantation Details are Submitted", Toast.LENGTH_SHORT).show();
                            Log.e("testProgress","Plantation Details are Submitted 1");
//                            getFarmerProfileImageFromLocalDB();
                        } else {
//                            syncProgressDialog.dismiss();
//                            appHelper.getDialogHelper().getConfirmationDialog().show(ConfirmationDialog.ERROR, dataResponse);
                        }

                    }
                };
                viewModel.getPlantationLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Todo : Image to server
    public void getIdentificationDetailsFromLocalDB() {
        try {
            viewModel.getLocalDocIdentificationFromLocalDB();
            if (viewModel.getDocIdentiFicationDeatilsTableFromLocalLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<PlantationDocuments> odVisitSurveyTableList = (List<PlantationDocuments>) o;
                        viewModel.getDocIdentiFicationDeatilsTableFromLocalLiveData().removeObserver(this);
                        if (odVisitSurveyTableList != null && odVisitSurveyTableList.size() > 0) {
//                            for (DocIdentiFicationDeatilsTable docIdentiFicationDeatilsTable : odVisitSurveyTableList) {
//                                syncDocIdentificationToServer(odVisitSurveyTableList.get(0));
                            final ProgressDialog progressDialog = new ProgressDialog(SyncActivity.this, R.style.AppCompatAlertDialogStyle);
                            progressDialog.setCancelable(false);
                            progressDialog.setMessage("fetch identity images and uploading to server...");
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progressDialog.show();
//                                    File f = new File(docIdentiFicationDeatilsTable.getDocLocal());
//                                    uploadImagesTOServerAPI(f,progressDialog,docIdentiFicationDeatilsTable);
                            try {
                                if (odVisitSurveyTableList.get(0).getDocURL().endsWith(".jpeg")){
                                    File f = new File(odVisitSurveyTableList.get(0).getDocURL());
                                    uploadImagesTOServerAPI(f,progressDialog,odVisitSurveyTableList.get(0));
                                } else if (odVisitSurveyTableList.get(0).getDocURL().endsWith(".jpg")){
                                    File f = new File(odVisitSurveyTableList.get(0).getDocURL());
                                    uploadImagesTOServerAPI(f,progressDialog,odVisitSurveyTableList.get(0));
                                } else if (odVisitSurveyTableList.get(0).getDocURL().endsWith(".png")) {
                                    File f = new File(odVisitSurveyTableList.get(0).getDocURL());
                                    uploadImagesTOServerAPI(f,progressDialog,odVisitSurveyTableList.get(0));
                                } else {
                                    File f = new File(odVisitSurveyTableList.get(0).getDocURL());
                                    uploadPDFTOServerAPI(f,progressDialog,odVisitSurveyTableList.get(0));
                                }
//                                File f = new File(odVisitSurveyTableList.get(0).getDocURL());
//                                uploadImagesTOServerAPI(f,progressDialog,odVisitSurveyTableList.get(0));
                            } catch (Exception exception){
                                Toast.makeText(SyncActivity.this, "no file found", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }


//                            }
                        } else {
//                            farmerDocImageCountZero = true;
                        }
                    }
                };
                viewModel.getDocIdentiFicationDeatilsTableFromLocalLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void syncDocIdentificationToServer(PlantationDocuments landDetailsListTable) {
        try {
            viewModel.syncDocIdentifcationDetailsDataToServer(landDetailsListTable);
            if (viewModel.getDocLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        viewModel.getDocLiveData().removeObserver(this);
                        String dataResponse = (String) o;
                        if (dataResponse.equals(SUCCESS_RESPONSE_MESSAGE)) {
                            Log.e("testProgress","Doc Identification Details are Submitted 3");
//                            Toast.makeText(SyncActivity.this, "Farmer Identification  Details are Submitted", Toast.LENGTH_SHORT).show();
//                            getBankDetailsFromLocalDB();
                            getIdentificationDetailsFromLocalDB();
                        } else {
//                            syncProgressDialog.dismiss();
//                            appHelper.getDialogHelper().getConfirmationDialog().show(ConfirmationDialog.ERROR, dataResponse);
                        }

                    }
                };
                viewModel.getDocLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void uploadImagesTOServerAPI(File uploadFile, ProgressDialog progressDialog,PlantationDocuments docIdentiFicationDeatilsTable) {

        Log.d(TAG, "uploadDataBaseTOServerAPI: " + uploadFile.getAbsolutePath());
        MultipartBody.Part file_pathDB = null;

//        File file = new File(uploadFile.getAbsolutePath());
        // Parsing any Media type file
        RequestBody requestBody = RequestBody.create(MediaType.parse(getMimeType(String.valueOf(uploadFile))), uploadFile);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", uploadFile.getName(), requestBody);
//        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        Log.d("Filepath", ">>>>>>>>>>" + fileToUpload);
        RequestBody r_acces_token = RequestBody.create(MediaType.parse("multipart/form-data"),
                appHelper.getSharedPrefObj().getString(accessToken, ""));
        RequestBody r_userID = RequestBody.create(MediaType.parse("multipart/form-data"),
                appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<ResponseBody> callRetrofit = null;
        callRetrofit = service.uploadFileDataToServer(r_userID, fileToUpload);
        callRetrofit.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    progressDialog.dismiss();
                    String strResponse = response.body().string();
                    Log.d(TAG, "onResponse: AppData " + response);
                    JSONObject json_object = new JSONObject(strResponse);
                    String message = "";
                    int status = 0;
                    Log.e(TAG, "onResponse: data json" + json_object);
                    message = json_object.getString("message");
                    status = json_object.getInt("status");
                    String  location = json_object.getString("location");
                    Log.e(TAG, "status of location in new method" + location);
                    Toast.makeText(SyncActivity.this, location, Toast.LENGTH_SHORT).show();
                    if (status==1) {
//                        PlantationDocuments docIdentiFicationDeatilsTable1 = new PlantationDocuments();
//                        docIdentiFicationDeatilsTable1=docIdentiFicationDeatilsTable;
//                        docIdentiFicationDeatilsTable1.setDocLocal(docIdentiFicationDeatilsTable.getDocUrl());
//                        docIdentiFicationDeatilsTable.setDocLocal(docIdentiFicationDeatilsTable.getDocUrl());
                        //as we are altering the docurl here we got to assign its value to doc local

//                        docIdentiFicationDeatilsTable1.setDocURL(location);
                        docIdentiFicationDeatilsTable.setDocURL(location);

                        Log.e("test",json_object.getString("location"));
//                        Log.e("contract", String.valueOf(docIdentiFicationDeatilsTable));
//                        Log.e("contract", String.valueOf(docIdentiFicationDeatilsTable1));

                        syncDocIdentificationToServer(docIdentiFicationDeatilsTable);
                        Toast.makeText(SyncActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else if (status==0) {
                        Log.e(TAG, "status of location in new method error message");
                        Toast.makeText(SyncActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "status of location in new method error message");
                        Toast.makeText(SyncActivity.this, message, Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                    Log.e(TAG, "status of location in new method error" );
                    Log.d("Error Call", ">>>>" + ex.toString());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
            }
        });
    }


    private void uploadPDFTOServerAPI(File uploadFile, ProgressDialog progressDialog,PlantationDocuments docIdentiFicationDeatilsTable) {

        Log.d(TAG, "uploadDataBaseTOServerAPI: " + uploadFile.getAbsolutePath());
        MultipartBody.Part file_pathDB = null;

//        File file = new File(uploadFile.getAbsolutePath());
        // Parsing any Media type file
        RequestBody requestBody = RequestBody.create(MediaType.parse(getMimeType(String.valueOf(uploadFile))), uploadFile);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", uploadFile.getName(), requestBody);
//        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        Log.d("Filepath", ">>>>>>>>>>" + fileToUpload);
        RequestBody r_acces_token = RequestBody.create(MediaType.parse("multipart/form-data"),
                appHelper.getSharedPrefObj().getString(accessToken, ""));
        RequestBody r_userID = RequestBody.create(MediaType.parse("multipart/form-data"),
                appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<ResponseBody> callRetrofit = null;
        callRetrofit = service.uploadPdfDataToServer(r_userID, fileToUpload);
        callRetrofit.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    progressDialog.dismiss();
                    String strResponse = response.body().string();
                    Log.d(TAG, "onResponse: AppData " + response);
                    JSONObject json_object = new JSONObject(strResponse);
                    String message = "";
                    int status = 0;
                    Log.e(TAG, "onResponse: data json" + json_object);
                    message = json_object.getString("message");
                    status = json_object.getInt("status");
                    String  location = json_object.getString("location");
                    Log.e(TAG, "status of location in new method" + location);
                    Toast.makeText(SyncActivity.this, location, Toast.LENGTH_SHORT).show();
                    if (status==1) {
//                        PlantationDocuments docIdentiFicationDeatilsTable1 = new PlantationDocuments();
//                        docIdentiFicationDeatilsTable1=docIdentiFicationDeatilsTable;
//                        docIdentiFicationDeatilsTable1.setDocLocal(docIdentiFicationDeatilsTable.getDocUrl());
//                        docIdentiFicationDeatilsTable.setDocLocal(docIdentiFicationDeatilsTable.getDocUrl());
                        //as we are altering the docurl here we got to assign its value to doc local

//                        docIdentiFicationDeatilsTable1.setDocURL(location);
                        docIdentiFicationDeatilsTable.setDocURL(location);

                        Log.e("PdfPost",json_object.getString("location"));
//                        Log.e("contract", String.valueOf(docIdentiFicationDeatilsTable));
//                        Log.e("contract", String.valueOf(docIdentiFicationDeatilsTable1));

                        syncDocIdentificationToServer(docIdentiFicationDeatilsTable);
                        Toast.makeText(SyncActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else if (status==0) {
                        Log.e(TAG, "status of location in new method error message");
                        Toast.makeText(SyncActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "status of location in new method error message");
                        Toast.makeText(SyncActivity.this, message, Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                    Log.e(TAG, "status of location in new method error" );
                    Log.d("Error Call", ">>>>" + ex.toString());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
            }
        });
    }



    //Todo :Sync Geo Main
    public void  getGeoBoundariesDetails() {
        try {
            viewModel.getGeoBoudariesFromLocalDB();
            if (viewModel.getGeoBoundariesFromLocalLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<PlantationGeoBoundaries> odVisitSurveyTableList = (List<PlantationGeoBoundaries>) o;
                        viewModel.getGeoBoundariesFromLocalLiveData().removeObserver(this);
                        if (odVisitSurveyTableList != null && odVisitSurveyTableList.size() > 0) {
//                            for (GeoBoundariesTable geoBoundariesTable : odVisitSurveyTableList) {
//                                syncGeoBoundariesDetailsToServer(geoBoundariesTable);
//                            }
                            for (int i =0;i<odVisitSurveyTableList.size();i++) {
                                syncGeoBoundariesDetailsToServer(odVisitSurveyTableList.get(i),i);//Commented to test
                            }
                        } else {
//                            PlotGeoBoundiresCountZero = true;
                            //getFertlizerListFromLocalDbCheckDBNotSync();
                        }

                    }
                };
                viewModel.getGeoBoundariesFromLocalLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();


//            INSERT_LOG("getGeoBoundariesFromLocalLiveData", "Exception : " + ex.getMessage());
        }
    }

    //Todo Sync Geo Main
    public void syncGeoBoundariesDetailsToServer(PlantationGeoBoundaries geoBoundariesTable,int index) {
        try {

            viewModel.syncGeoBoundariesDetailsSubmitTableDataToServer(geoBoundariesTable,index);
            if (viewModel.getStringLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        viewModel.getStringLiveData().removeObserver(this);
                        String dataResponse = (String) o;
                        if (dataResponse.equals(SUCCESS_RESPONSE_MESSAGE)) {
                            Log.e("testProgress","GeoBoundaries Details are Submitted 8");
//                            Toast.makeText(SyncActivity.this, "Geo boundaries details submitted", Toast.LENGTH_SHORT).show();
//                            syncProgressDialog.dismiss();
                            // Toast.makeText(SyncFunctionalityDeatilsActivity.this, "Geo Boundaries Sync Successfully", Toast.LENGTH_SHORT).show();
                            //getFertlizerListFromLocalDbCheckDBNotSync();
                        } else {
//                            syncProgressDialog.dismiss();
//                            appHelper.getDialogHelper().getConfirmationDialog().show(ConfirmationDialog.ERROR, dataResponse);
                        }
                    }
                };
                viewModel.getStringLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    //Labour Survey not sync count
    public void getSurveylistFromLocalDbCheckDBNotSync() {
        try {
            viewModel.getLabourListFromLocalDBNotSync();
            if (viewModel.getLabourDetailsListNotSyncLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<PlantationLabourSurvey> odVisitSurveyTableList = (List<PlantationLabourSurvey>) o;
                        viewModel.getLabourDetailsListNotSyncLiveData().removeObserver(this);
                        if (odVisitSurveyTableList != null && odVisitSurveyTableList.size() > 0) {
                            for (PlantationLabourSurvey plantationLabourSurvey : odVisitSurveyTableList) {
                                // TODO: Need to remove for lead
                                syncSurveyPersonalDetailsToServer(plantationLabourSurvey, odVisitSurveyTableList);
                            }
                        } else {

                        }
                    }

                };
                viewModel.getLabourDetailsListNotSyncLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    //Labour Survey To server sync
    public void syncSurveyPersonalDetailsToServer(PlantationLabourSurvey plantationLabourSurvey, List<PlantationLabourSurvey> odVisitSurveyTableList) {
        try {
            viewModel.syncSurveyDetailsDataToServer(plantationLabourSurvey);
            if (viewModel.getStringLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        viewModel.getStringLiveData().removeObserver(this);
                        String dataResponse = (String) o;
                        if (dataResponse.equals(SUCCESS_RESPONSE_MESSAGE)) {
                            Toast.makeText(SyncActivity.this, "Labour Survey Details are Submitted", Toast.LENGTH_SHORT).show();
                            Log.e("testProgress","Labour Survey Details are Submitted 1");
//                            getFarmerProfileImageFromLocalDB();
                        } else {
//                            syncProgressDialog.dismiss();
//                            appHelper.getDialogHelper().getConfirmationDialog().show(ConfirmationDialog.ERROR, dataResponse);
                        }

                    }
                };
                viewModel.getStringLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    //Parent Survey not sync count
    public void getParentSurveylistFromLocalDbCheckDBNotSync() {
        try {
            viewModel.getParentListFromLocalDBNotSync();
            if (viewModel.getFarmerHouseholdParentSurveyDetailsByIdLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<FarmerHouseholdParentSurvey> odVisitSurveyTableList = (List<FarmerHouseholdParentSurvey>) o;
                        viewModel.getFarmerHouseholdParentSurveyDetailsByIdLiveData().removeObserver(this);
                        if (odVisitSurveyTableList != null && odVisitSurveyTableList.size() > 0) {
                            for (FarmerHouseholdParentSurvey farmerHouseholdParentSurvey : odVisitSurveyTableList) {
                                // TODO: Need to remove for lead
                                syncParentSurveyDetailsToServer(farmerHouseholdParentSurvey, odVisitSurveyTableList);
                            }
                        } else {

                        }
                    }

                };
                viewModel.getFarmerHouseholdParentSurveyDetailsByIdLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    //Parent Survey To server sync
    public void syncParentSurveyDetailsToServer(FarmerHouseholdParentSurvey farmerHouseholdParentSurvey, List<FarmerHouseholdParentSurvey> odVisitSurveyTableList) {
        try {
            viewModel.syncParentSurveyDetailsDataToServer(farmerHouseholdParentSurvey);
            if (viewModel.getStringLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        viewModel.getStringLiveData().removeObserver(this);
                        String dataResponse = (String) o;
                        if (dataResponse.equals(SUCCESS_RESPONSE_MESSAGE)) {
                            Toast.makeText(SyncActivity.this, "Parent Survey Details are Submitted", Toast.LENGTH_SHORT).show();
                            Log.e("testProgress","Labour Survey Details are Submitted 1");
//                            getFarmerProfileImageFromLocalDB();
                        } else {
//                            syncProgressDialog.dismiss();
//                            appHelper.getDialogHelper().getConfirmationDialog().show(ConfirmationDialog.ERROR, dataResponse);
                        }

                    }
                };
                viewModel.getStringLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    //Child Survey not sync count
    public void getChildSurveylistFromLocalDbCheckDBNotSync() {
        try {
            viewModel.getChildrenListFromLocalDBNotSync();
            if (viewModel.getFarmerHouseholdChildrenSurveyDetailsByIdLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<FarmerHouseholdChildrenSurvey> odVisitSurveyTableList = (List<FarmerHouseholdChildrenSurvey>) o;
                        viewModel.getFarmerHouseholdChildrenSurveyDetailsByIdLiveData().removeObserver(this);
                        if (odVisitSurveyTableList != null && odVisitSurveyTableList.size() > 0) {
                            for (FarmerHouseholdChildrenSurvey farmerHouseholdChildrenSurvey : odVisitSurveyTableList) {
                                // TODO: Need to remove for lead
                                syncChildSurveyDetailsToServer(farmerHouseholdChildrenSurvey, odVisitSurveyTableList);
                            }
                        } else {

                        }
                    }

                };
                viewModel.getFarmerHouseholdChildrenSurveyDetailsByIdLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Child Survey To server sync
    public void syncChildSurveyDetailsToServer(FarmerHouseholdChildrenSurvey farmerHouseholdChildrenSurvey, List<FarmerHouseholdChildrenSurvey> odVisitSurveyTableList) {
        try {
            viewModel.syncChildrenSurveyDetailsDataToServer(farmerHouseholdChildrenSurvey);
            if (viewModel.getStringLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        viewModel.getStringLiveData().removeObserver(this);
                        String dataResponse = (String) o;
                        if (dataResponse.equals(SUCCESS_RESPONSE_MESSAGE)) {
                            Toast.makeText(SyncActivity.this, "Child Survey Details are Submitted", Toast.LENGTH_SHORT).show();
                            Log.e("testProgress","Labour Survey Details are Submitted 1");
//                            getFarmerProfileImageFromLocalDB();
                        } else {
//                            syncProgressDialog.dismiss();
//                            appHelper.getDialogHelper().getConfirmationDialog().show(ConfirmationDialog.ERROR, dataResponse);
                        }

                    }
                };
                viewModel.getStringLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    //Risk not sync count list
    public void getRisklistFromLocalDbCheckDBNotSync() {
        try {
            viewModel.getRiskListFromLocalDBNotSync();
            if (viewModel.getRiskAssessmentDetailsByIdLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<RiskAssessment> odVisitSurveyTableList = (List<RiskAssessment>) o;
                        viewModel.getRiskAssessmentDetailsByIdLiveData().removeObserver(this);
                        if (odVisitSurveyTableList != null && odVisitSurveyTableList.size() > 0) {
                            for (RiskAssessment riskAssessment : odVisitSurveyTableList) {
                                // TODO: Need to remove for lead
                                syncRiskDetailsToServer(riskAssessment, odVisitSurveyTableList);
                            }
                        } else {

                        }
                    }

                };
                viewModel.getRiskAssessmentDetailsByIdLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Risk To server sync
    public void syncRiskDetailsToServer(RiskAssessment riskAssessment, List<RiskAssessment> odVisitSurveyTableList) {
        try {
            viewModel.syncRiskDetailsDataToServer(riskAssessment);
            if (viewModel.getRiskLiveDataLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        viewModel.getRiskLiveDataLiveData().removeObserver(this);
                        String dataResponse = (String) o;
                        if (dataResponse.equals(SUCCESS_RESPONSE_MESSAGE)) {
                            Toast.makeText(SyncActivity.this, "Risk Details are Submitted", Toast.LENGTH_SHORT).show();
                            Log.e("testProgress","Labour Survey Details are Submitted 1");
//                            getFarmerProfileImageFromLocalDB();
                        } else {
//                            syncProgressDialog.dismiss();
//                            appHelper.getDialogHelper().getConfirmationDialog().show(ConfirmationDialog.ERROR, dataResponse);
                        }

                    }
                };
                viewModel.getRiskLiveDataLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Manufacturer not sync count list
    public void getManufacturerlistFromLocalDbCheckDBNotSync() {
        try {
            viewModel.getManufacturerListFromLocalDBNotSync();
            if (viewModel.getManfacturerFarmerDetailsByIdLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<ManfacturerFarmer> odVisitSurveyTableList = (List<ManfacturerFarmer>) o;
                        viewModel.getManfacturerFarmerDetailsByIdLiveData().removeObserver(this);
                        if (odVisitSurveyTableList != null && odVisitSurveyTableList.size() > 0) {
                            for (ManfacturerFarmer manfacturerFarmer : odVisitSurveyTableList) {
                                // TODO: Need to remove for lead
                                syncManufacturerDetailsToServer(manfacturerFarmer, odVisitSurveyTableList);
                            }
                        } else {

                        }
                    }

                };
                viewModel.getManfacturerFarmerDetailsByIdLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Manufacturer To server sync
    public void syncManufacturerDetailsToServer(ManfacturerFarmer manfacturerFarmer, List<ManfacturerFarmer> odVisitSurveyTableList) {
        try {
            viewModel.syncManufacturerDetailsDataToServer(manfacturerFarmer);
            if (viewModel.getManufacturerLiveDataLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        viewModel.getManufacturerLiveDataLiveData().removeObserver(this);
                        String dataResponse = (String) o;
                        if (dataResponse.equals(SUCCESS_RESPONSE_MESSAGE)) {
                            Toast.makeText(SyncActivity.this, "Manufacturer Details are Submitted", Toast.LENGTH_SHORT).show();
                            Log.e("testProgress","Manu Details are Submitted 1");
//                            getFarmerProfileImageFromLocalDB();
                        } else {
//                            syncProgressDialog.dismiss();
//                            appHelper.getDialogHelper().getConfirmationDialog().show(ConfirmationDialog.ERROR, dataResponse);
                        }

                    }
                };
                viewModel.getManufacturerLiveDataLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    //Dealer not sync count list
    public void getDealerlistFromLocalDbCheckDBNotSync() {
        try {
            viewModel.getDealerListFromLocalDBNotSync();
            if (viewModel.getDealerFarmerDetailsByIdLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<DealerFarmer> odVisitSurveyTableList = (List<DealerFarmer>) o;
                        viewModel.getDealerFarmerDetailsByIdLiveData().removeObserver(this);
                        if (odVisitSurveyTableList != null && odVisitSurveyTableList.size() > 0) {
                            for (DealerFarmer dealerFarmer : odVisitSurveyTableList) {
                                // TODO: Need to remove for lead
                                syncDealerDetailsToServer(dealerFarmer, odVisitSurveyTableList);
                            }
                        } else {

                        }
                    }

                };
                viewModel.getDealerFarmerDetailsByIdLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Dealer To server sync
    public void syncDealerDetailsToServer(DealerFarmer dealerFarmer, List<DealerFarmer> odVisitSurveyTableList) {
        try {
            viewModel.syncDealerDetailsDataToServer(dealerFarmer);
            if (viewModel.getDealerLiveDataLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        viewModel.getDealerLiveDataLiveData().removeObserver(this);
                        String dataResponse = (String) o;
                        if (dataResponse.equals(SUCCESS_RESPONSE_MESSAGE)) {
                            Toast.makeText(SyncActivity.this, "Dealer Details are Submitted", Toast.LENGTH_SHORT).show();
                            Log.e("testProgress","Dealer Details are Submitted 1");
//                            getFarmerProfileImageFromLocalDB();
                        } else {
//                            syncProgressDialog.dismiss();
//                            appHelper.getDialogHelper().getConfirmationDialog().show(ConfirmationDialog.ERROR, dataResponse);
                        }

                    }
                };
                viewModel.getDealerLiveDataLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        try {
            refreshDBModuleCall();
        } catch (Exception ex) {
            ex.printStackTrace();
//            INSERT_LOG("onResume", "Exception : " + ex.getMessage());
        }
    }

    public static String getMimeType(String url) {

        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File file = new File(path, url);

        try {
            // Make sure the Pictures directory exists.
            path.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }

//            String type = null;
//        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
//        Log.d("MIME_TYPE_EXT", extension);
//        if (extension != null && extension != "") {
//            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
//            //  Log.d("MIME_TYPE", type);
//        } else {
//            FileNameMap fileNameMap = URLConnection.getFileNameMap();
//            type = fileNameMap.getContentTypeFor(url);
//        }
        return String.valueOf(file);
    }


    @Override
    public void languageCallback(int position, AppLanguageHDRTable appLanguageHDRTable) {
        languageChanged=true;
        Log.e(TAG,"interface clicked");
//        recreateApp();
        updateButtonLabels();
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