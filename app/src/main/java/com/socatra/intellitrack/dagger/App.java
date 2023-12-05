package com.socatra.intellitrack.dagger;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;


import java.io.File;
import java.io.FileOutputStream;
import javax.inject.Inject;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import static com.socatra.intellitrack.constants.AppConstant.APP_FOLDER;
import static com.socatra.intellitrack.constants.AppConstant.DB_FOLDER;
import static com.socatra.intellitrack.constants.AppConstant.DB_NAME;
import static com.socatra.intellitrack.constants.AppConstant.DB_SUB_FOLDER;

import com.socatra.intellitrack.di.component.DaggerAppComponent;


public class App extends Application implements HasActivityInjector {


    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        this.initDagger();
        context = getApplicationContext();
    }


    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;


    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    private void initDagger() {
        DaggerAppComponent.builder().application(this).build().inject(this);
    }

    public static String createDBPath(){
        String filePath="";
        try {

            // TODO: DATABASE NAME
            String dbName=DB_NAME;
            String appFolderName=APP_FOLDER;
            String appDBFolderName=DB_FOLDER;
            String appEnvFolderName=DB_SUB_FOLDER;


            // TODO: For LEAD
           /* String rootPath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/LOS_DB/";*/

            // TODO: FOR LOS
            String appFolderPath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + File.separator + appFolderName + File.separator ;
            String appDBPath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + File.separator + appFolderName + File.separator + appDBFolderName + File.separator ;
            String appEnvPath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + File.separator + appFolderName + File.separator + appDBFolderName + File.separator
                    + appEnvFolderName + File.separator ;
            // TODO: FULL FILE PATH
            filePath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + File.separator + appFolderName + File.separator + appDBFolderName + File.separator
                    + appEnvFolderName + File.separator + dbName ;


            File folder = new File(appFolderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            folder = new File(appDBPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            folder = new File(appEnvPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            if( !TextUtils.isEmpty(filePath)) {
                File f = new File(filePath);
                if (!f.exists()) {
                    boolean isFileCreated= f.createNewFile();
                    if(isFileCreated) {
                        FileOutputStream out = new FileOutputStream(f);
                        out.flush();
                        out.close();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }






}





