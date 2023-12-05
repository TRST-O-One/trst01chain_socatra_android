package com.socatra.excutivechain.activity;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static com.socatra.excutivechain.AppConstant.AgentId;
import static com.socatra.excutivechain.AppConstant.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS;
import static com.socatra.excutivechain.AppConstant.DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_SSS;
import static com.socatra.excutivechain.AppConstant.DB_NAME;
import static com.socatra.excutivechain.AppConstant.DB_VERSION;
import static com.socatra.excutivechain.AppConstant.DeviceUserID;
import static com.socatra.excutivechain.AppConstant.DeviceUserName;
import static com.socatra.excutivechain.AppConstant.DeviceUserPwd;
import static com.socatra.excutivechain.AppConstant.IsFirst;
import static com.socatra.excutivechain.AppConstant.accessToken;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.JsonElement;
import com.socatra.excutivechain.App;
import com.socatra.excutivechain.AppConstant;
import com.socatra.excutivechain.BaseActivity;
import com.socatra.excutivechain.CommonUtils;
import com.socatra.excutivechain.R;
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
import com.socatra.excutivechain.database.entity.RefreshTableDateCheck;
import com.socatra.excutivechain.database.entity.RiskAssessment;
import com.socatra.excutivechain.database.entity.RiskAssessmentQuestion;
import com.socatra.excutivechain.database.entity.StateorProvince;
import com.socatra.excutivechain.database.entity.SubDistrict;
import com.socatra.excutivechain.database.entity.VillageTable;
import com.socatra.excutivechain.models.LoginResponseDTO;
import com.socatra.excutivechain.repositories.Retrofit_funtion_class;
import com.socatra.excutivechain.view_models.AppViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
public class LoginActivity extends BaseActivity {
    private static final String TAG = LoginActivity.class.getCanonicalName();
    String appVersion;
    public static String SuserId;
    public String strUserDeviceId;
    // TODO: List of all permissions
    private String[] PERMISSIONS_STORAGE = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.POST_NOTIFICATIONS
    };

    private static final int PERMISSIONS_REQUESTS_CODE = 2000;
    SharedPreferences prefs = null;
    //    ProgressBar progressDialog;
    ActivityResultLauncher<Intent> mGetPermission;
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    public AppViewModel viewModel;
    //    public AppViewModel viewModel;
    String TokenAccess = "";
    ProgressBar progressBar;
    String strTodayDate;

    TextView txtDeviceId,txtDbNo,txtAppVersion,txtUserName,txtPassword,txtLogin,txtDate;
    Dialog dialog;
    TextView txtOk;
    String strFirbaseToken;
    String strAppPackageName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        intilalizeUI();
        strTodayDate = appHelper.getCurrentDateTime(AppConstant.DATE_FORMAT_YYYY_MM_DD);

        prefs = getSharedPreferences("com.trst01.excutivechain", MODE_PRIVATE);

        mGetPermission = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == LoginActivity.RESULT_OK) {
                    Toast.makeText(LoginActivity.this, "Android 11 permission ok", Toast.LENGTH_SHORT).show();
                }
            }
        });
        appHelper.getSharedPrefObj().edit().remove(DB_NAME).apply();

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            strAppPackageName = packageInfo.packageName;
            appHelper.getSharedPrefObj().edit().putString(DB_NAME, strAppPackageName).apply();
            Log.d(TAG, "onCreate: package name" + strAppPackageName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }


        takePermission();


        LoginActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                App.createDBPath();
            }
        });

        LoginActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO: Configure Dagger
                configureDagger();
            }
        });

        LoginActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO: Configure View Model
                configureViewModel();
            }
        });

    }

    private void intilalizeUI() {
        progressBar = findViewById(R.id.progress);
        txtDeviceId = findViewById(R.id.txtDeviceId);
        txtDbNo = findViewById(R.id.txtDbNo);
        txtAppVersion = findViewById(R.id.txtAppVersion);
        txtUserName = findViewById(R.id.txtUserName);
        txtPassword = findViewById(R.id.txtPassword);
        txtLogin = findViewById(R.id.txtLogin);
        txtDate = findViewById(R.id.txtDate);

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prefs.getBoolean("firstrun", true)) {
                    if (!txtUserName.getText().toString().isEmpty() && !txtPassword.getText().toString().isEmpty()) {
                        progressBar.setVisibility(View.VISIBLE);
                        getMasterSyncFromServerDetails(TokenAccess); // Pass the token here
                    } else {
                        Toast.makeText(LoginActivity.this, "Please wait for credentials to be fetched.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Subsequent logins, proceed to the dashboard directly
                    proceedToDashboard();
                }
            }
        });


    }
    private void proceedToDashboard() {
        Intent intent = new Intent(LoginActivity.this, DashBoardFarmerListActivity.class);
        startActivity(intent);
        finish();
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    public void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);

        if (checkAllPermissions()) {
            if (prefs.getBoolean("firstrun", true)) {
                if (appHelper.isNetworkAvailable()) {
                    appHelper.getSharedPrefObj().edit().remove(DeviceUserID).apply();
                    appHelper.getSharedPrefObj().edit().remove(accessToken).apply();
                    appHelper.getSharedPrefObj().edit().remove(DeviceUserName).apply();
                    appHelper.getSharedPrefObj().edit().remove(DeviceUserPwd).apply();
                    appHelper.getSharedPrefObj().edit().remove(AgentId).apply();

                    getLoginDetailsByImeiNumber(CommonUtils.getIMEInumber(LoginActivity.this));
                    //getFireBaseTokenValue();
                } else {
                    Toast.makeText(this, "no internet connection", Toast.LENGTH_SHORT).show();
                }
            } else {
                getTodayRecordExist();
              //  getFireBaseTokenValue();
            }

        }

    }

    private boolean checkAllPermissions() {
        try {

            // TODO: Check which permissions are granted
            List<String> listPermissionsNeeded = new ArrayList<>();
            for (String permission : PERMISSIONS_STORAGE) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(permission);
                }
            }

            // TODO: Ask for non granted permissions
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERMISSIONS_REQUESTS_CODE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public void requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                mGetPermission.launch(intent);
                checkAllPermissions();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            checkAllPermissions();
        }
    }

    private boolean isPermissionGranted() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int readExternalStoragePermission = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
            return readExternalStoragePermission == PackageManager.PERMISSION_DENIED;
        }

    }

    public void takePermission() {
        if (isPermissionGranted()) {
            App.createDBPath();
            txtDeviceId.setText("Device ID   : " + CommonUtils.getIMEInumber(this));
            txtDbNo.setText("DB  Version : " + String.valueOf(DB_VERSION));
            txtDate.setText("Date : "+ strTodayDate);
            Log.d(TAG, "onCreate: " + CommonUtils.getIMEInumber(this));
            try {
                PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                appVersion = packageInfo.versionName;
                if (!TextUtils.isEmpty(appVersion)) {
                    txtAppVersion.setText("App Version : " + appVersion);

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else {
            requestPermission();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            if (requestCode == 101) {
                boolean readExternalStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (readExternalStorage) {
                    Toast.makeText(LoginActivity.this, "permission taken already", Toast.LENGTH_SHORT).show();
                } else {
                    takePermission();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        takePermission();
    }

    public void getLoginDetailsByImeiNumber(String userId) {
        try {
            Log.e(TAG, "getLoginDetailsByImeiNumber: " + userId);
            viewModel.logInServiceList(userId);
            if (viewModel.getloginResponseDTOFromServerLiveData() != null) {
                Observer observer = new Observer() {
                    @Override
                    public void onChanged(Object o) {
                        LoginResponseDTO loginResponseDTOList = (LoginResponseDTO) o;
                        viewModel.getloginResponseDTOFromServerLiveData().removeObserver(this);
                        if (loginResponseDTOList != null) {
//                            for (int i = 0; i < loginResponseDTOList.size(); i++) {
                                App.createDBPath();
                                Log.d(TAG, "onChanged: " + App.createDBPath());
                                appHelper.getSharedPrefObj().edit().putString(DeviceUserID, loginResponseDTOList.getData().get(0).getId().toString()).apply();
                                // appHelper.getSharedPrefObj().edit().putString(DeviceUserID, "10").apply();
//
                                strUserDeviceId = loginResponseDTOList.getData().get(0).getId().toString();
//
                                appHelper.getSharedPrefObj().edit().putString(DeviceUserName, loginResponseDTOList.getData().get(0).getUserName()).apply();
                                appHelper.getSharedPrefObj().edit().putString(DeviceUserPwd, loginResponseDTOList.getData().get(0).getPassword()).apply();
//                                appHelper.getSharedPrefObj().edit().putString(AgentId, loginResponseDTOList.get(i).getAgentId()).apply();
                                txtUserName.setText(loginResponseDTOList.getData().get(0).getUserName());//AM
                                txtPassword.setText(loginResponseDTOList.getData().get(0).getPassword());
//
//
                                Log.d(TAG, "onChanged: " + loginResponseDTOList.getData().get(0).getId() + loginResponseDTOList.getData().get(0).getUserName());
                                String token = "Bearer " + loginResponseDTOList.getData().get(0).getToken();
                                TokenAccess = token;
                                App.createDBPath();
//                                // TODO: Inserting into key store
                                appHelper.getSharedPrefObj().edit().putString(accessToken, token).apply();
                                App.createDBPath();
//                                //   if (prefs.getBoolean("firstrun", true)) {
//
                                try {
                                    testDialog(token);
                                } catch (Exception ex){
                                    Toast.makeText(LoginActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                                }
//                            }


                        } else {

                            Toast.makeText(LoginActivity.this, "Unable to fetch Details please contact Admin", Toast.LENGTH_SHORT).show();
                        }

                    }
                };
                viewModel.getloginResponseDTOFromServerLiveData().observe(this, observer);
            }

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
            // appHelper.getDialogHelper().getLoadingDialog().closeDialog();
        }
    }

    public void insertDailyRecord(String token) {
        RefreshTableDateCheck refreshTableDateCheck = new RefreshTableDateCheck();
        refreshTableDateCheck.setDate(strTodayDate);
        refreshTableDateCheck.setDeviceID(CommonUtils.getIMEInumber(this));
        String dateTime = appHelper.getCurrentDateTime(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
        refreshTableDateCheck.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
        refreshTableDateCheck.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
        refreshTableDateCheck.setCreatedDate(dateTime);
        refreshTableDateCheck.setUpdatedDate(dateTime);
        insertionOfDailyLoginData(refreshTableDateCheck, token);
    }

    //Todo: Refresh data
    public void insertionOfDailyLoginData(RefreshTableDateCheck refreshTableDateCheck, String token) {
        try {
            viewModel.insertRefreshTableDateCheckTableLocalDb(refreshTableDateCheck);
            if (viewModel.getRefreshTableDateCheckLiveDataFromLocalDB() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        RefreshTableDateCheck refreshTableDateCheck1 = (RefreshTableDateCheck) o;
                        viewModel.getRefreshTableDateCheckLiveDataFromLocalDB().removeObserver(this);
                        if (refreshTableDateCheck1 != null) {
                            progressBar.setVisibility(View.VISIBLE);
//                            getCropDetailsFromServer();  // check Table

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    txtLogin.setEnabled(false);
                                    viewModel.deleteAllTablesFromLocalMaster();//Todo : master added
                                    getMasterSyncFromServerDetails(token);

                                }
                            }, 100);
//                            progressBar.setVisibility(View.GONE);//only for test
//                            txtLogin.setEnabled(true);//only for test
                        }

                    }
                };
                viewModel.getRefreshTableDateCheckLiveDataFromLocalDB().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
//            appHelper.getDialogHelper().getLoadingDialog().closeDialog();
        }
    }

    //Todo: Main Master Sync
    public void getMasterSyncFromServerDetails(String token) {
        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getMasterSyncDetailsFromServer(token);

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    String strResponse = String.valueOf(response.body());
                    JSONObject jsonArray = new JSONObject(strResponse);
                    Log.d(TAG, "onResponse: Json_array" + jsonArray);
                    if (jsonArray.length() > 0) {
                        try {
                            prefs.edit().putBoolean("firstrun", false).commit();

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

                                Log.d(TAG, "onResponse: subDistrict" + jsonObjectSubDistrict);
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
                                // JSONObject json_Object = json_arry.getJSONObject(27);
                                Log.d(TAG, "onResponse: village" + jsonObjectVillage);
                                VillageTable village_value = new VillageTable();
                                village_value.setId(jsonObjectVillage.getString("Id"));
                                village_value.setCode(jsonObjectVillage.getString("Code"));
                                village_value.setName(jsonObjectVillage.getString("Name"));
                                village_value.setSubDistrictId(jsonObjectVillage.getString("SubDistrictId"));
                                village_value.setPinCode(jsonObjectVillage.getString("PinCode"));

                                village_value.setCreatedDate(jsonObjectVillage.getString("CreatedDate"));
                                village_value.setUpdatedDate(jsonObjectVillage.getString("UpdatedDate"));
                                //  clusterHDr_value.setIsActive(jsonObjectDistric.getString("IsActive"));
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


                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Master Sync Successfully", Toast.LENGTH_LONG).show();
                            txtLogin.setEnabled(true);
                            if (appHelper.isNetworkAvailable()) {
                                if(appHelper.getSharedPrefObj().getBoolean(IsFirst,true)){
                                    progressBar.setVisibility(View.VISIBLE);
                                    txtLogin.setEnabled(false);
                                    viewModel.deleteTablesFromLocalTransactionData();//fixed but need to check
                                    getSyncFarmerAllDataFromServer();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "no internet connection for transaction sync", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Log.d("Error", ">>>>" + ex.toString());
                            Toast.makeText(LoginActivity.this, "Master Sync Failed, please contact admin", Toast.LENGTH_SHORT).show();
                        }

                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    Log.d("Error", ">>>>" + ex.toString());
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
//                progressBar.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());

            }
        });
    }


    private void getFireBaseTokenValue() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("ggg", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        strFirbaseToken = task.getResult().getToken();
                        Log.d(TAG, "onComplete: FirbaseToken" +  strFirbaseToken);
                        Log.e(TAG, "onComplete:Token "+ strFirbaseToken );

                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d("tag", msg);
//                        Toast.makeText(SignInActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    //Todo: All farmer records
    public void getSyncFarmerAllDataFromServer() {
        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getFarmerAllSyncDataDetailsFromServer(CommonUtils.getIMEInumber(LoginActivity.this), appHelper.getSharedPrefObj().getString(accessToken, ""));
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppCompatAlertDialogStyle);
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
                                try {//Todo Login sync : Response sequence
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

                                            plantation.setAreaInHectors(jsonObjectFarmerPD.getDouble("AreaInHectors"));//need to check
                                            plantation.setLatitude(jsonObjectFarmerPD.getDouble("Latitude"));
                                            plantation.setLongitude(jsonObjectFarmerPD.getDouble("Longitude"));
//                                            plantation.setGeoboundariesArea(jsonObjectFarmerPD.getDouble("GeoboundariesArea"));

//                                            if (jsonObjectFarmerPD.getString("GeoboundariesArea")!=null) {
//                                                plantation.setGeoboundariesArea(jsonObjectFarmerPD.getDouble("GeoboundariesArea"));
//                                            }else {
//                                                plantation.setGeoboundariesArea(0.0);
//                                            }

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
                                           // plantationLabourSurvey.setPlantationId(jsonObjectLabourSurvey.optInt("PlantationId"));
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

                                    txtLogin.setEnabled(true);
                                    progressBar.setVisibility(View.GONE);
                                    progressDialog.dismiss();
                                    Toast.makeText(LoginActivity.this, "Fetched All Data From Server SuccessFully", Toast.LENGTH_LONG).show();

                                    appHelper.getSharedPrefObj().edit().putBoolean(IsFirst,false).apply();
                                }
                                catch (Exception ex) {
                                    ex.printStackTrace();
                                    Toast.makeText(LoginActivity.this, String.valueOf(ex.getMessage()), Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    progressDialog.dismiss();
                                    Log.d("Error", ">>>>" + ex.toString());
                                }
                            }
                        }, 2000);
                    } else {
                        Toast.makeText(LoginActivity.this, "no records found", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Transaction Sync Failed, please contact admin", Toast.LENGTH_SHORT).show();
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


    //Todo: Today data check
    public void getTodayRecordExist() {
        try {
            viewModel.getRefreshTableDateCheckFromLocalDBByDate(CommonUtils.getIMEInumber(this), strTodayDate);
            if (viewModel.getRefreshTableDateCheckByDateFromLocalDBLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        RefreshTableDateCheck refreshTableDateCheck = (RefreshTableDateCheck) o;
                        viewModel.getRefreshTableDateCheckByDateFromLocalDBLiveData().removeObserver(this);
                        if (refreshTableDateCheck != null) {
                            Log.d(TAG, "onChanged: " + refreshTableDateCheck);
                            Log.d(TAG, "onChanged: Date" + refreshTableDateCheck.getDate());
                            txtUserName.setText(appHelper.getSharedPrefObj().getString(DeviceUserName, ""));
                            txtPassword.setText(appHelper.getSharedPrefObj().getString(DeviceUserPwd, ""));
                        } else {
                            if (appHelper.isNetworkAvailable()) {
                                appHelper.getSharedPrefObj().edit().remove(DeviceUserID).apply();
                                appHelper.getSharedPrefObj().edit().remove(accessToken).apply();
                                appHelper.getSharedPrefObj().edit().remove(DeviceUserName).apply();
                                appHelper.getSharedPrefObj().edit().remove(DeviceUserPwd).apply();
//                                getLoginDetailsByImeiNumber("Voluntary");
                                getLoginDetailsByImeiNumber(CommonUtils.getIMEInumber(LoginActivity.this));
                            } else {
                                Toast.makeText(LoginActivity.this, "no internet", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }


                };
                viewModel.getRefreshTableDateCheckByDateFromLocalDBLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Todo: Ok dialog
    private void testDialog(String token) {


        dialog = new Dialog(LoginActivity.this, R.style.MyAlertDialogThemeNew);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.master_success);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        txtOk= dialog.findViewById(R.id.txtTest);


        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                insertDailyRecord(token);

            }
        });

        if(appHelper.getSharedPrefObj().getBoolean(IsFirst,true)){
            dialog.show();
        }

    }
}