package com.socatra.excutivechain.helper;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressBar {

    private static final String LOG_TAG = ProgressBar.class.getName();
    private static ProgressDialog mProgressDialog;
    private static ProgressDialog mNewProgressDialog;
    private static android.widget.ProgressBar mProgressBar;

    public static ProgressDialog showProgressBar(final Context context, final String msg) {
        hideProgressBar();


        ApplicationThread.uiPost(LOG_TAG, "hiding progress bar", new Runnable() {
            @Override
            public void run() {
                try {
                    if (mProgressDialog == null) {
                        mProgressDialog = new ProgressDialog(context);
                        mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
                        mProgressDialog.setMessage(msg);
                        mProgressDialog.setIndeterminate(true);
                        mProgressDialog.setCancelable(false);
                        mProgressDialog.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        return mProgressDialog;
    }

    public static void hideProgressBar() {
        ApplicationThread.uiPost(LOG_TAG, "hiding progress bar", new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    try {
                        mProgressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                mProgressDialog = null;
            }
        });
    }

}
