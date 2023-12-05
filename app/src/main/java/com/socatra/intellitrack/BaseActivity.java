package com.socatra.intellitrack;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.socatra.intellitrack.helper.AppHelper;
import com.socatra.intellitrack.helper.PreferenceUtils;


public class BaseActivity extends AppCompatActivity {


    public AppHelper appHelper;
    public PreferenceUtils preferenceUtils;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        appHelper = new AppHelper(this);
        preferenceUtils = new PreferenceUtils(this);
    }
}





