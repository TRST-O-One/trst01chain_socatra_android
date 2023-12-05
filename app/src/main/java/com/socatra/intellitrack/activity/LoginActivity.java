package com.socatra.intellitrack.activity;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.App_PackageName;
import static com.socatra.intellitrack.constants.AppConstant.DeviceCustomerAddress;
import static com.socatra.intellitrack.constants.AppConstant.DeviceCustomerId;
import static com.socatra.intellitrack.constants.AppConstant.DeviceCustomerName;
import static com.socatra.intellitrack.constants.AppConstant.DeviceDealerAddress;
import static com.socatra.intellitrack.constants.AppConstant.DeviceDealerID;
import static com.socatra.intellitrack.constants.AppConstant.DeviceDealerName;
import static com.socatra.intellitrack.constants.AppConstant.DeviceProcessorAddress;
import static com.socatra.intellitrack.constants.AppConstant.DeviceProcessorId;
import static com.socatra.intellitrack.constants.AppConstant.DeviceProcessorName;
import static com.socatra.intellitrack.constants.AppConstant.DeviceTraderAddress;
import static com.socatra.intellitrack.constants.AppConstant.DeviceTraderId;
import static com.socatra.intellitrack.constants.AppConstant.DeviceTraderName;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserID;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserName;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserPwd;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserRole;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonElement;
import com.socatra.intellitrack.activity.Traderflow.TraderDashboardActivity;
import com.socatra.intellitrack.activity.customerflow.CustomerDashboard;
import com.socatra.intellitrack.activity.grnflow.DealerAddGrnActivity;
import com.socatra.intellitrack.activity.grnflow.ProcessorAddGRNActivity;
import com.socatra.intellitrack.activity.main_dash_board.MainDashBoardActivity;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.constants.AppConstant;
import com.socatra.intellitrack.BaseActivity;
import com.socatra.intellitrack.constants.CommonUtils;
import com.socatra.intellitrack.R;

import com.socatra.intellitrack.database.entity.UserLoginTable;
import com.socatra.intellitrack.models.SyncLoginDetailsSubmitRequestDTO;
import com.socatra.intellitrack.repositories.Retrofit_funtion_class;
import com.socatra.intellitrack.view_models.AppViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {
    private static final String TAG = LoginActivity.class.getCanonicalName();
    private static final int PERMISSIONS_REQUESTS_CODE = 2000;
    public static String SuserId;
    public String strUserDeviceId;
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    public AppViewModel viewModel;
    String appVersion;
    SharedPreferences prefs = null;
    //    ProgressBar progressDialog;
    ActivityResultLauncher<Intent> mGetPermission;
    //    public AppViewModel viewModel;
    String TokenAccess = "";
    ProgressBar progressBar;
    String strTodayDate;
    //String strDealerID, strTraderID, strProcessorid, strCustomerId, strUserName, strRoleName;
    /*   "id": 29,
               "dealerid": 10,
               "traderid": null,
               "manufacturerid": null,
               "userName": "Prateek",
               "role": "Dealer",
               "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjksImRlYWxlcmlkIjoxMCwidHJhZGVyaWQiOm51bGwsIm1hbnVmYWN0dXJlcmlkIjpudWxsLCJmaXJzdE5hbWUiOiJLYXJ0aGlrIiwibGFzdE5hbWUiOiJKYW5kYSIsInVzZXJOYW1lIjoiUHJhdGVlayIsInJvbGUiOiJEZWFsZXIiLCJpYXQiOjE2OTI3NzAwNjYsImV4cCI6MTY5NTM2MjA2Nn0.KxdvVqmJ9gXMCHDqF0g-r0qxKkXDcwP9eAMoH-iO_mM",
               "firstName": "Karthik",
               "lastName": "Janda"*/
    TextView txtDeviceId, txtDbNo, txtAppVersion, txtDate;
    TextInputEditText txtUserName, txtPassword;
    Dialog dialog;
    TextView txtLogin;
    String string_to_be_converted_to_MD5 = "REPLACE_WITH YOUR_OWN_STRING";
    // TODO: List of all permissions
    private String[] PERMISSIONS_STORAGE = {
//            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            /*   Manifest.permission.ACCESS_FINE_LOCATION,
               Manifest.permission.ACCESS_COARSE_LOCATION,
               Manifest.permission.READ_PHONE_STATE,*/
    };

    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getMD5EncryptedString(String encTarget) {
        MessageDigest mdEnc = null;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception while encrypting to md5");
            e.printStackTrace();
        } // Encryption algorithm
        mdEnc.update(encTarget.getBytes(), 0, encTarget.length());
        String md5 = new BigInteger(1, mdEnc.digest()).toString(16);
        while (md5.length() < 32) {
            md5 = "0" + md5;
        }
        return md5;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        strTodayDate = appHelper.getCurrentDateTime(AppConstant.DATE_FORMAT_YYYY_MM_DD);
        prefs = getSharedPreferences(App_PackageName, MODE_PRIVATE);

        //Permission intent
        mGetPermission = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == LoginActivity.RESULT_OK) {
                    Toast.makeText(LoginActivity.this, "Android 11 permission ok", Toast.LENGTH_SHORT).show();
                }
            }
        });
        intilalizeUI();
        takePermission();
        configureDagger();
        configureViewModel();
        // String MD5_Hash_String = md5(string_to_be_converted_to_MD5);
        //takePermission();

    }


    private void intilalizeUI() {

        // progressBar = findViewById(R.id.progress);

        //  progressBar = findViewById(R.id.progress);

        txtDeviceId = findViewById(R.id.txtDeviceId);
        txtDbNo = findViewById(R.id.txtDbNo);
        txtAppVersion = findViewById(R.id.txtAppVersion);
        txtUserName = findViewById(R.id.edt_login_id);
        txtPassword = findViewById(R.id.edt_password);
        txtLogin = findViewById(R.id.txt_login);
        txtDate = findViewById(R.id.txtDate);
        txtLogin.setOnClickListener(new View.OnClickListener() {//With first time validation
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(txtUserName.getText().toString().trim())) {
                    txtUserName.setError("Enter User Name");
                    // appHelper.getDialogHelper().getConfirmationDialog().show(ConfirmationDialog.ALERT, ERROR_MESSAGE_ENTER_LASTNAME);
                } else if (TextUtils.isEmpty(txtPassword.getText().toString().trim())) {
                    txtPassword.setError("Enter Password");
                } else {
                    String strUserId, strPwd;
                    strUserId = txtUserName.getText().toString();
                    strPwd = txtPassword.getText().toString();

                    String MD5_Hash_String_pwd = md5(strPwd);
                    UserLoginTable userLoginTable = new UserLoginTable();
                    userLoginTable.setUsername(strUserId);
                    userLoginTable.setPassword(MD5_Hash_String_pwd);

                    appHelper.getSharedPrefObj().edit().remove(DeviceUserID).apply();//user id
                    appHelper.getSharedPrefObj().edit().remove(DeviceDealerID).apply();
                    appHelper.getSharedPrefObj().edit().remove(DeviceProcessorId).apply();
                    appHelper.getSharedPrefObj().edit().remove(DeviceCustomerId).apply();
                    appHelper.getSharedPrefObj().edit().remove(DeviceTraderId).apply();
                    appHelper.getSharedPrefObj().edit().remove(AUTHORIZATION_TOKEN_KEY).apply();
                    appHelper.getSharedPrefObj().edit().remove(DeviceUserName).apply();
                    appHelper.getSharedPrefObj().edit().remove(DeviceUserPwd).apply();
                    appHelper.getSharedPrefObj().edit().remove(DeviceUserRole).apply();

                    if (!MD5_Hash_String_pwd.isEmpty()) {
                        if (appHelper.isNetworkAvailable()) {
                            // prefs.edit().putBoolean("firstrun", false).commit();
                            checkLoginDetails(userLoginTable);
                            //getLoginDetailsByUserId(userLoginTable);
                        }

                    } else {
                        Toast.makeText(LoginActivity.this, "please check your  password", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


    }

    /* public String md5(String s) {
         try {
             // Create MD5 Hash
             MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
             digest.update(s.getBytes());
             byte messageDigest[] = digest.digest();

             // Create Hex String
             StringBuffer hexString = new StringBuffer();
             for (int i=0; i<messageDigest.length; i++)
                 hexString.append(String.format(“%02X”, messageDigest[i]));

             return hexString.toString();
         }catch (NoSuchAlgorithmException e) {
             e.printStackTrace();
         }
         return "";
     }*/
    private void proceedToDashboard() {
        Intent intent = new Intent(LoginActivity.this, DealerAddGrnActivity.class);
        startActivity(intent);
        //finish();
    }

    private void proceedToManufacture() {
        Intent intent = new Intent(LoginActivity.this, ProcessorAddGRNActivity.class);
        startActivity(intent);
        //finish();
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    public void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
        //viewModel = ViewModelProviders.of(this, viewModelFactory).get(AppViewModel.class);
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
            Log.d(TAG, "onCreate: " + CommonUtils.getIMEInumber(this));
            try {
                PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                appVersion = packageInfo.versionName;
                //  getLoginDetailsByUserId();
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
//      takePermission();//commented for test
    }


    public void checkLoginDetails(UserLoginTable userLoginTable) {
        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;

        final SyncLoginDetailsSubmitRequestDTO syncLoginReqDTO = new SyncLoginDetailsSubmitRequestDTO();

        if (!TextUtils.isEmpty(userLoginTable.getUsername())) {
            syncLoginReqDTO.setUsername(userLoginTable.getUsername());
        } else {
            syncLoginReqDTO.setUsername("");
        }

        if (!TextUtils.isEmpty(userLoginTable.getPassword())) {
            syncLoginReqDTO.setPassword(userLoginTable.getPassword());
        } else {
            syncLoginReqDTO.setPassword("");
        }

        callRetrofit = service.postlogInServiceNew(syncLoginReqDTO);
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("checking credentials...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    //  JSONObject json_data =  new JSONObject(response.body());
                    String strResponse = String.valueOf(response.body());
                    Log.d(TAG, "onResponse: >>>" + strResponse);
                    JSONObject jsonResponse = new JSONObject(response.body().toString());
                    // Extract the "data" JSON array
                    String message = jsonResponse.optString("messgae");
                    int status = jsonResponse.optInt("status");
                    Log.d(TAG, "onResponse status: " + status);
                    JSONArray jsonArray = jsonResponse.getJSONArray("data");
                    if (status == 201) {
                        progressDialog.dismiss();
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                prefs.edit().putBoolean("firstrun", false).commit();
                                JSONObject jsonData = jsonArray.getJSONObject(i);
                                txtUserName.setText(jsonData.getString("userName"));//AM
                                String token = "Bearer " + jsonData.getString("token");
//                                // TODO: Inserting into key store
                                appHelper.getSharedPrefObj().edit().putString(AUTHORIZATION_TOKEN_KEY, token).apply();
                                appHelper.getSharedPrefObj().edit().putString(DeviceUserID, jsonData.getString("id")).apply();
                                appHelper.getSharedPrefObj().edit().putString(DeviceUserName, jsonData.getString("userName")).apply();
                                appHelper.getSharedPrefObj().edit().putString(DeviceUserRole, jsonData.getString("role")).apply();

                                if (jsonData.getString("role").equalsIgnoreCase("Dealer")) {
                                    appHelper.getSharedPrefObj().edit().putString(DeviceDealerID, jsonData.getString("dealerid")).apply();
                                    appHelper.getSharedPrefObj().edit().putString(DeviceDealerName, jsonData.getString("dealername")).apply();
                                    appHelper.getSharedPrefObj().edit().putString(DeviceDealerAddress, jsonData.getString("dealeraddress")).apply();

                                    appHelper.getSharedPrefObj().edit().remove(DeviceProcessorName).apply();
                                    appHelper.getSharedPrefObj().edit().remove(DeviceCustomerName).apply();
                                    appHelper.getSharedPrefObj().edit().remove(DeviceTraderName).apply();

                                    appHelper.getSharedPrefObj().edit().remove(DeviceProcessorAddress).apply();
                                    appHelper.getSharedPrefObj().edit().remove(DeviceCustomerAddress).apply();
                                    appHelper.getSharedPrefObj().edit().remove(DeviceTraderAddress).apply();

                                    appHelper.getSharedPrefObj().edit().remove(DeviceTraderId).apply();
                                    appHelper.getSharedPrefObj().edit().remove(DeviceProcessorId).apply();
                                    appHelper.getSharedPrefObj().edit().remove(DeviceCustomerId).apply();
                                    appHelper.getSharedPrefObj().edit().remove(DeviceTraderId).apply();



                                    progressDialog.dismiss();
                                    Intent intent = new Intent(LoginActivity.this, MainDashBoardActivity.class);
                                    startActivity(intent);
                                    finish();
                                    // proceedToDashboard();
                                } else if (jsonData.getString("role").equalsIgnoreCase("Customer")) {
                                    appHelper.getSharedPrefObj().edit().putString(DeviceCustomerId, jsonData.getString("customerid")).apply();
                                    appHelper.getSharedPrefObj().edit().putString(DeviceCustomerName, jsonData.getString("customername")).apply();
                                    appHelper.getSharedPrefObj().edit().putString(DeviceCustomerAddress, jsonData.getString("customeraddress")).apply();

                                    appHelper.getSharedPrefObj().edit().remove(DeviceProcessorName).apply();
                                    appHelper.getSharedPrefObj().edit().remove(DeviceDealerName).apply();
                                    appHelper.getSharedPrefObj().edit().remove(DeviceTraderName).apply();

                                    appHelper.getSharedPrefObj().edit().remove(DeviceProcessorAddress).apply();
                                    appHelper.getSharedPrefObj().edit().remove(DeviceDealerAddress).apply();
                                    appHelper.getSharedPrefObj().edit().remove(DeviceTraderAddress).apply();

                                    appHelper.getSharedPrefObj().edit().remove(DeviceTraderId).apply();
                                    appHelper.getSharedPrefObj().edit().remove(DeviceDealerID).apply();
                                    appHelper.getSharedPrefObj().edit().remove(DeviceProcessorId).apply();
                                    //   appHelper.getSharedPrefObj().edit().remove(DeviceProcessorId).apply();

                                    appHelper.getSharedPrefObj().edit().remove(DeviceTraderId).apply();
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(LoginActivity.this, CustomerDashboard.class);
                                    startActivity(intent);
                                    finish();

                                } else if (jsonData.getString("role").equalsIgnoreCase("Processor")) {
                                    appHelper.getSharedPrefObj().edit().putString(DeviceProcessorId, jsonData.getString("processorid")).apply();
                                    appHelper.getSharedPrefObj().edit().putString(DeviceProcessorName, jsonData.getString("processorname")).apply();
                                    appHelper.getSharedPrefObj().edit().putString(DeviceProcessorAddress, jsonData.getString("processoraddress")).apply();

                                    appHelper.getSharedPrefObj().edit().remove(DeviceCustomerName).apply();
                                    appHelper.getSharedPrefObj().edit().remove(DeviceDealerName).apply();
                                    appHelper.getSharedPrefObj().edit().remove(DeviceTraderName).apply();

                                    appHelper.getSharedPrefObj().edit().remove(DeviceCustomerAddress).apply();
                                    appHelper.getSharedPrefObj().edit().remove(DeviceDealerAddress).apply();
                                    appHelper.getSharedPrefObj().edit().remove(DeviceTraderAddress).apply();

                                    appHelper.getSharedPrefObj().edit().remove(DeviceTraderId).apply();
                                    appHelper.getSharedPrefObj().edit().remove(DeviceDealerID).apply();
                                    appHelper.getSharedPrefObj().edit().remove(DeviceCustomerId).apply();
                                    appHelper.getSharedPrefObj().edit().remove(DeviceTraderId).apply();

                                    progressDialog.dismiss();
                                    Intent intent = new Intent(LoginActivity.this, MainDashBoardActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    appHelper.getSharedPrefObj().edit().putString(DeviceTraderId, jsonData.getString("traderid")).apply();
                                    appHelper.getSharedPrefObj().edit().putString(DeviceTraderName, jsonData.getString("tradername")).apply();
                                    appHelper.getSharedPrefObj().edit().putString(DeviceTraderAddress, jsonData.getString("traderaddress")).apply();

                                    appHelper.getSharedPrefObj().edit().remove(DeviceCustomerName).apply();
                                    appHelper.getSharedPrefObj().edit().remove(DeviceDealerName).apply();
                                    appHelper.getSharedPrefObj().edit().remove(DeviceProcessorName).apply();

                                    appHelper.getSharedPrefObj().edit().remove(DeviceCustomerAddress).apply();
                                    appHelper.getSharedPrefObj().edit().remove(DeviceDealerAddress).apply();
                                    appHelper.getSharedPrefObj().edit().remove(DeviceProcessorAddress).apply();

                                    appHelper.getSharedPrefObj().edit().remove(DeviceDealerID).apply();
                                    appHelper.getSharedPrefObj().edit().remove(DeviceCustomerId).apply();
                                    appHelper.getSharedPrefObj().edit().remove(DeviceProcessorId).apply();

                                    progressDialog.dismiss();
                                    Intent intent = new Intent(LoginActivity.this, TraderDashboardActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }
                        }
                    } else if (status == 404) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    progressDialog.dismiss();
                    prefs.edit().putBoolean("firstrun", true).commit();
                    Toast.makeText(LoginActivity.this, "invalid data credentials please contact admin", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "invalidUser", Toast.LENGTH_LONG).show();
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());

            }
        });
    }

}

