package com.socatra.excutivechain.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.socatra.excutivechain.R;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
public class SplashActivity extends AppCompatActivity {

    public String strUserDeviceId;
    String userName, userPWd, strToken;
    VersionChecker versionChecker;
    private String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.READ_PHONE_STATE,
    };

    private static final int PERMISSIONS_REQUESTS_CODE = 3000;
    private AppUpdateManager appUpdateManager;
    private static final int APP_UPDATE_REQUEST_CODE = 123;
    InstallStateUpdatedListener listener;
    String strPlayStoreVersion,strCurrentVersion;

//    private AppUpdateManager mAppUpdateManager;
//    private static final int RC_APP_UPDATE = 11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
      //  appUpdateManager = AppUpdateManagerFactory.create(this);


        if (Build.VERSION.SDK_INT < 30) {
            checkAllPermissions();
        }

//        versionChecker = new VersionChecker();
//        try {
//            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
//            strCurrentVersion = packageInfo.versionName;
//            strPlayStoreVersion = versionChecker.execute().get();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//
//        if (!strPlayStoreVersion.isEmpty() && strPlayStoreVersion.equalsIgnoreCase(strCurrentVersion))
//        {
//            updatedApp();
//        }else {
//            moveToLoginActivity();
//        }

        moveToLoginActivity();



    }

    private void updatedApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Available");
        builder.setMessage("A new version of the app is available. Please update to the latest version.");
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Redirect the user to the Play Store for the update.
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.trst01.excutivechain"));
                startActivity(intent);
            }
        });
        builder.setCancelable(false); // Prevent the user from dismissing the dialog
        builder.show();

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

    private void checkForAppUpdate() {
        // Create an instance of the AppUpdateInfo
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                        appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    // Prompt the user to update
                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo,
                                AppUpdateType.IMMEDIATE,
                                SplashActivity.this,
                                APP_UPDATE_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Continue to the login screen or next activity
                    moveToLoginActivity();
                }
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == APP_UPDATE_REQUEST_CODE) {
//            if (resultCode != RESULT_OK) {
//                // If the update is not completed, you can handle it here.
//                // You may choose to retry or continue with the app.
//                moveToLoginActivity();
//            }
//            // Continue to the login screen or next activity
//
//        }
//
//    }

    private void moveToLoginActivity() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
               // updatedApp();

                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }

        }, 1 * 2500);
    }


//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        appUpdateManager.unregisterListener(listener);
//    }
}