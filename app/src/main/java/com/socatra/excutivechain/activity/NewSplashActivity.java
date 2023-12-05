package com.socatra.excutivechain.activity;

//import static com.google.android.play.core.install.model.ActivityResult.RESULT_IN_APP_UPDATE_FAILED;

import static com.google.android.play.core.install.model.ActivityResult.RESULT_IN_APP_UPDATE_FAILED;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.socatra.excutivechain.R;

import java.util.ArrayList;
import java.util.List;

public class NewSplashActivity extends AppCompatActivity {

    public String strUserDeviceId;
    String userName, userPWd, strToken;
    private String[] PERMISSIONS_STORAGE = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
    };

    private static final int PERMISSIONS_REQUESTS_CODE = 3000;
    private AppUpdateManager appUpdateManager;
    private static final int APP_UPDATE_REQUEST_CODE = 123;
    InstallStateUpdatedListener listener;

    private AppUpdateManager mAppUpdateManager;
    private static final int RC_APP_UPDATE = 11;


//    private AppUpdateManager mAppUpdateManager;
//    private int RC_APP_UPDATE = 999;
    private int inAppUpdateType;
    private com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask;
    private InstallStateUpdatedListener installStateUpdatedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        appUpdateManager = AppUpdateManagerFactory.create(this);
        mAppUpdateManager = AppUpdateManagerFactory.create(this);
        // Returns an intent object that you use to check for an update.
        appUpdateInfoTask = mAppUpdateManager.getAppUpdateInfo();
        //lambda operation used for below listener
        //For flexible update
        installStateUpdatedListener = installState -> {
            if (installState.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdate();
            }
        };
        mAppUpdateManager.registerListener(installStateUpdatedListener);
        if (Build.VERSION.SDK_INT < 30) {
            checkAllPermissions();
           if (checkAllPermissions())
           {
               //For Immediate
//               inAppUpdateType = AppUpdateType.IMMEDIATE; //1
//               inAppUpdate();

//For Flexible
               inAppUpdateType = AppUpdateType.FLEXIBLE; //0
               inAppUpdate();
           }
        }else {
            //For Immediate
//            inAppUpdateType = AppUpdateType.IMMEDIATE; //1
//            inAppUpdate();

//For Flexible
            inAppUpdateType = AppUpdateType.FLEXIBLE; //0
            inAppUpdate();
        }
        moveToLoginActivity();



//        listener = new InstallStateUpdatedListener() {
//            @Override
//            public void onStateUpdate(InstallState state) {
//                if (state.installStatus() == InstallStatus.DOWNLOADED) {
//                    // The update has been downloaded and is ready to install.
//                    // Show a confirmation dialog to the user.
//                    appUpdateManager.completeUpdate();
//                }
//            }
//        };
//
//        // Register the listener
//        appUpdateManager.registerListener(listener);
//        checkForAppUpdate();
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                updatedApp();
//
//                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(i);
//                finish();
//            }
//
//        }, 1 * 2500);


    }




//    @Override
//    protected void onStart() {
//        super.onStart();
//        mAppUpdateManager = AppUpdateManagerFactory.create(this);
//        InstallStateUpdatedListener installStateUpdatedListener = new
//                InstallStateUpdatedListener() {
//                    @Override
//                    public void onStateUpdate(InstallState state) {
//                        if (state.installStatus() == InstallStatus.DOWNLOADED){
//                            //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
//                            popupSnackbarForCompleteUpdate();
//                        } else if (state.installStatus() == InstallStatus.INSTALLED){
//                            if (mAppUpdateManager != null){
//                                mAppUpdateManager.unregisterListener(installStateUpdatedListener);
//                            }
//
//                        } else {
//                            Log.i(String.valueOf(SplashActivity.this), "InstallStateUpdatedListener: state: " + state.installStatus());
//                        }
//                    }
//                };
//
//        mAppUpdateManager.registerListener(installStateUpdatedListener);
//
//        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
//
//            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
//                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE /*AppUpdateType.IMMEDIATE*/)){
//
//                try {
//                    mAppUpdateManager.startUpdateFlowForResult(
//                            appUpdateInfo, AppUpdateType.FLEXIBLE /*AppUpdateType.IMMEDIATE*/, MainActivity.this, RC_APP_UPDATE);
//
//                } catch (IntentSender.SendIntentException e) {
//                    e.printStackTrace();
//                }
//
//            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED){
//                //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
//                popupSnackbarForCompleteUpdate();
//            } else {
//                Log.e(String.valueOf(SplashActivity.this), "checkForAppUpdateAvailability: something else");
//            }
//        });
//    }

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == APP_UPDATE_REQUEST_CODE) {
//            if (resultCode != RESULT_OK) {
//                // If the update is not completed, you can handle it here.
//                // You may choose to retry or continue with the app.
//            }
//            // Continue to the login screen or next activity
//            moveToLoginActivity();
//        }

        if (requestCode == RC_APP_UPDATE) {
            //when user clicks update button
            if (resultCode == RESULT_OK) {
                Toast.makeText(NewSplashActivity.this, "App download starts...", Toast.LENGTH_LONG).show();
            } else if (resultCode != RESULT_CANCELED) {
                //if you want to request the update again just call checkUpdate()
                Toast.makeText(NewSplashActivity.this, "App download canceled.", Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_IN_APP_UPDATE_FAILED) {
                Toast.makeText(NewSplashActivity.this, "App download failed.", Toast.LENGTH_LONG).show();
            }else {
                moveToLoginActivity();
            }
        }
//        if (requestCode == RC_APP_UPDATE) {
//            if (resultCode != RESULT_OK) {
//                Log.e(String.valueOf(SplashActivity.this), "onActivityResult: app download failed");
//            }
//            moveToLoginActivity();
//        }
    }

    private void moveToLoginActivity() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                //updatedApp();

                Intent i = new Intent(NewSplashActivity.this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }

        }, 1 * 1800);
    }

//    private void popupSnackbarForCompleteUpdate() {
//
//        Snackbar snackbar =
//                Snackbar.make(
//                        findViewById(R.id.coordinatorLayout_main),
//                        "New app is ready!",
//                        Snackbar.LENGTH_INDEFINITE);
//
//        snackbar.setAction("Install", view -> {
//            if (mAppUpdateManager != null){
//                mAppUpdateManager.completeUpdate();
//            }
//        });
//
//
//        snackbar.setActionTextColor(getResources().getColor(R.color.install_color));
//        snackbar.show();
//    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
       // appUpdateManager.unregisterListener(listener);
        mAppUpdateManager.unregisterListener(installStateUpdatedListener);

    }
    @Override
    protected void onResume() {
        try {
            mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
                if (appUpdateInfo.updateAvailability() ==
                        UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    // If an in-app update is already running, resume the update.
                    try {
                        mAppUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo,
                                inAppUpdateType,
                                this,
                                RC_APP_UPDATE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            });


            mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
                //For flexible update
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdate();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onResume();
    }
    private void inAppUpdate() {

        try {
            // Checks that the platform will allow the specified type of update.
            appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                @Override
                public void onSuccess(AppUpdateInfo appUpdateInfo) {
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                            // For a flexible update, use AppUpdateType.FLEXIBLE
                            && appUpdateInfo.isUpdateTypeAllowed(inAppUpdateType)) {
                        // Request the update.

                        try {
                            mAppUpdateManager.startUpdateFlowForResult(
                                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                                    appUpdateInfo,
                                    // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                                    inAppUpdateType,
                                    // The current activity making the update request.
                                    NewSplashActivity.this,
                                    // Include a request code to later monitor this update request.
                                    RC_APP_UPDATE);
                        } catch (IntentSender.SendIntentException ignored) {

                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void popupSnackbarForCompleteUpdate() {
        try {
            Snackbar snackbar =
                    Snackbar.make(
                            findViewById(R.id.rootLayout),
                            "An update has just been downloaded.\nRestart to update",
                            Snackbar.LENGTH_INDEFINITE);

            snackbar.setAction("INSTALL", view -> {
                if (mAppUpdateManager != null){
                    mAppUpdateManager.completeUpdate();
                }
            });
            snackbar.setActionTextColor(getResources().getColor(R.color.grey));
            snackbar.show();

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
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
                                NewSplashActivity.this,
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

}