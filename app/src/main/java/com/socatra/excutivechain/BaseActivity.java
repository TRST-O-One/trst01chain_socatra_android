package com.socatra.excutivechain;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class BaseActivity extends AppCompatActivity {

    
    public AppHelper appHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        appHelper=new AppHelper(this);
    }

//    @Override
//    public void onBackPressed() {
//        appHelper.getDialogHelper().getConfirmationDialog().showTwoButtons("Do you want to leave from this screen ? ", new ConfirmationDialog.ActionCallback() {
//            @Override
//            public void onAction() {
//                finish();
//            }
//        });
//    }
}
