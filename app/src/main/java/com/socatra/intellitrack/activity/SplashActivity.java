package com.socatra.intellitrack.activity;

import static com.socatra.intellitrack.constants.AppConstant.App_PackageName;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserRole;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.socatra.intellitrack.BaseActivity;
import com.socatra.intellitrack.R;
import com.socatra.intellitrack.activity.Traderflow.TraderDashboardActivity;
import com.socatra.intellitrack.activity.customerflow.CustomerDashboard;
import com.socatra.intellitrack.activity.main_dash_board.MainDashBoardActivity;
import com.socatra.intellitrack.helper.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SplashActivity extends BaseActivity {

    private static final int PERMISSIONS_REQUESTS_CODE = 3000;
    public String strUserDeviceId;
    String userName, userPWd, strToken;
    SharedPreferences preferences;

    String deviceRoleName;
    private String[] PERMISSIONS_STORAGE = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
    };
    String strSelectLanguage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        preferences = getSharedPreferences(App_PackageName, MODE_PRIVATE);
        deviceRoleName = appHelper.getSharedPrefObj().getString(DeviceUserRole, "");
        preferenceUtils = new PreferenceUtils(SplashActivity.this);
        strSelectLanguage = preferenceUtils.getStringFromPreference(PreferenceUtils.Language, "");
        changLang();

        if (Build.VERSION.SDK_INT < 30) {
            checkAllPermissions();
        }
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (preferences.getBoolean("firstrun", true)) {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                   // overridePendingTransition(R.anim.rotate_out,R.anim.rotate_in);
                    finish();
                } else {
                    if (deviceRoleName.equalsIgnoreCase("Dealer") || deviceRoleName.equalsIgnoreCase("Processor") ) {
                        // Start MainDashBoardActivity for "Dealer" or "Processor"
                        Intent intent = new Intent(SplashActivity.this, MainDashBoardActivity.class);
                        startActivity(intent);
                      //  overridePendingTransition(R.anim.rotate_out,R.anim.rotate_in);
                        finish();
                    } else if (deviceRoleName.equalsIgnoreCase("Customer")) {
                        // Start CustomerDashboard for "Customer"
                        Intent intent = new Intent(SplashActivity.this, CustomerDashboard.class);
                        startActivity(intent);
                       // overridePendingTransition(R.anim.rotate_out,R.anim.rotate_in);
                        finish();
                    }  else if (deviceRoleName.equalsIgnoreCase("Trader")) {
                        // Start CustomerDashboard for "Customer"
                        Intent intent = new Intent(SplashActivity.this, TraderDashboardActivity.class);

                        startActivity(intent);
                        // overridePendingTransition(R.anim.rotate_out,R.anim.rotate_in);
                        finish();
                    }
                }


            }

        }, 1 * 1000);


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


    public void changLang()
    {
        if (strSelectLanguage == null || strSelectLanguage.isEmpty()) {
            String languageToLoad = "en"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            preferenceUtils.setLanguage("en");
        } else if (strSelectLanguage.equalsIgnoreCase("en")) {
            String languageToLoad = "en"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            preferenceUtils.setLanguage("en");
        } else if (strSelectLanguage.equalsIgnoreCase("in")) {
            String languageToLoad = "in"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            preferenceUtils.setLanguage("in");
        }  else if (strSelectLanguage.equalsIgnoreCase("fr")) {
            String languageToLoad = "fr"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            preferenceUtils.setLanguage("fr");
        } else if (strSelectLanguage.equalsIgnoreCase("zh")) {
            String languageToLoad = "zh"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            preferenceUtils.setLanguage("zh");
        }else {
            String languageToLoad = "en"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            preferenceUtils.setLanguage("en");
        }
    }

}