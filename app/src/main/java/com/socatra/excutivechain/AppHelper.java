package com.socatra.excutivechain;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AppHelper {

    private static final String TAG = AppHelper.class.getCanonicalName();
    private Context context;
    Dialog dialog;

    public AppHelper(Context context) {
        this.context = context;
    }

//    public DialogHelper getDialogHelper() {
//        if (dialogHelper == null) {
//            dialogHelper = new DialogHelper(context);
//        }
//        return dialogHelper;
//    }

    public String getCurrentDateTime(String strDateFormat) {
        String strCurrDate = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
            Calendar c1 = Calendar.getInstance(); // today
            strCurrDate = sdf.format(c1.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strCurrDate;

    }

    public boolean isNetworkAvailable() {
        boolean isAvailable = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                @SuppressLint("MissingPermission")
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    isAvailable = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isAvailable;
    }

    public SharedPreferences getSharedPrefObj() {
        SharedPreferences sharedPrefObj = null;
        try {
            sharedPrefObj = context.getSharedPreferences(AppConstants.App_SHARED_PREF_NAME , Context.MODE_PRIVATE);
        } catch(Exception ex) {
            Log.e(TAG, "Exception getAndCreateSessionObj" + ex);
        }
        return sharedPrefObj;
    }


    public File convertUriToFile(Context context, Uri uri) throws IOException {
        // Create a temporary file to save the content of the URI
        File tempFile = File.createTempFile("temp_file", null, context.getCacheDir());

        try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
             OutputStream outputStream = new FileOutputStream(tempFile)) {

            if (inputStream == null) {
                throw new IOException("Failed to open input stream for URI: " + uri.toString());
            }

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            return tempFile;
        } catch (IOException e) {
            // Handle any exceptions that may occur during file conversion
            e.printStackTrace();
            throw e;
        }
    }

    //For pdf
    public File convertUriToFile1(Context context, Uri uri) throws IOException {
        // Create a temporary file to save the content of the URI
        File tempFile = File.createTempFile("temp_file", ".pdf", context.getCacheDir());

        try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
             OutputStream outputStream = new FileOutputStream(tempFile)) {

            if (inputStream == null) {
                throw new IOException("Failed to open input stream for URI: " + uri.toString());
            }

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            return tempFile;
        } catch (IOException e) {
            // Handle any exceptions that may occur during file conversion
            e.printStackTrace();
            throw e;
        }
    }

    public static String getFilePathFromContentUri(Context context, Uri contentUri) {
        String filePath = null;

        String[] projection = {MediaStore.MediaColumns.DATA};

        Cursor cursor = context.getContentResolver().query(contentUri, projection, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            filePath = cursor.getString(columnIndex);
            cursor.close();
        }

        return filePath;
    }
}