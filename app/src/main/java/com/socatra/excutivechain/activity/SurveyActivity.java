package com.socatra.excutivechain.activity;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.socatra.excutivechain.BaseActivity;
import com.socatra.excutivechain.R;
import com.socatra.excutivechain.database.entity.FarmerHouseholdParentSurvey;
import com.socatra.excutivechain.database.entity.FarmersTable;
import com.socatra.excutivechain.database.entity.RiskAssessment;
import com.socatra.excutivechain.view_models.AppViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class SurveyActivity extends BaseActivity implements HasSupportFragmentInjector {

    String TAG = "SurveyActivity";
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    public AppViewModel viewModel;


    TextView surveyselection1;
    TextView laboursurvey1;
    TextView farmersurvey1;
    TextView risksurvey1;
    String farmerCode = "";

    ImageView imgBack;

    TextView txtFarmerCode;
    private SharedPreferences preferences;
    CardView cardLabourSurvey, cardFarmerSurvey, cardRiskSurvey;

    FarmersTable farmersTable;

    String farmerSurveyStatus="0";

    String riskSurveyStatus="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        farmerCode = getIntent().getStringExtra("mFarmerCode");
        farmersTable=(FarmersTable) getIntent().getSerializableExtra("mFarmerObj");
        Log.e(TAG, farmerCode);

        initializeUI();
        configureDagger();
        configureViewModel();
        initializeValues();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        updateTextLabels();
    }

    @SuppressLint("SetTextI18n")
    private void updateTextLabels() {
        String selectedLanguage = getSelectedLanguage();

        String hdSurveyHead=getResources().getString(R.string.surveyselection1);
        String hdLabour=getResources().getString(R.string.laboursurvey1);
        String hdFarmer=getResources().getString(R.string.farmersurvey1);
        String hdRisk=getResources().getString(R.string.risksurvey1);

        if (selectedLanguage.equals("English")) {
            surveyselection1.setText(hdSurveyHead);
            laboursurvey1.setText(hdLabour);
            farmersurvey1.setText(hdFarmer);
            risksurvey1.setText(hdRisk);
        } else {
            surveyselection1.setText(getLanguageFromLocalDb(selectedLanguage,hdSurveyHead)+ "/" + hdSurveyHead);
            laboursurvey1.setText(getLanguageFromLocalDb(selectedLanguage,hdLabour)+ "/" + hdLabour);
            farmersurvey1.setText(getLanguageFromLocalDb(selectedLanguage,hdFarmer)+ "/" + hdFarmer);
            risksurvey1.setText(getLanguageFromLocalDb(selectedLanguage,hdRisk)+ "/" + hdRisk);
        }

}



    private String getSelectedLanguage() {
        return preferences.getString("selected_language", "English");
    }
    private void initializeUI() {
        imgBack=findViewById(R.id.imgBackAddSurveyHome);
        txtFarmerCode=findViewById(R.id.txtFarmerCodeSurvey);
        cardLabourSurvey=findViewById(R.id.cardLabourSurvey);
        cardFarmerSurvey=findViewById(R.id.cardFarmerSurvey);
        cardRiskSurvey=findViewById(R.id.cardRiskSurvey);

        //Labels
        surveyselection1 = findViewById(R.id.surveyselection1);
        laboursurvey1 = findViewById(R.id.laboursurvey1);
        farmersurvey1 = findViewById(R.id.farmersurvey1);
        risksurvey1 = findViewById(R.id.risksurvey1);


    }

    private void initializeValues() {

        txtFarmerCode.setText("Farmer Code:"+farmerCode);

        imgBack.setOnClickListener(view->{
            finish();
        });

        cardLabourSurvey.setOnClickListener(view->{
            Toast.makeText(this, "Not applicable for Gajahruku!!", Toast.LENGTH_SHORT).show();

//            Intent intent=new Intent(this,LabourSurveyHomeActivity.class);
//            intent.putExtra("mFarmerCode",farmerCode);
//            startActivity(intent);
        });


        cardFarmerSurvey.setOnClickListener(view->{
            Toast.makeText(this, "Not applicable for Gajahruku!!", Toast.LENGTH_SHORT).show();
//            if (farmerSurveyStatus=="0") {
//                Intent intent = new Intent(this, FarmerSurveyActivity.class);
//                intent.putExtra("mFarmerCode", farmerCode);
//                intent.putExtra("mFarmerObj", farmersTable);
//                startActivity(intent);
//            } else {
//                Toast.makeText(SurveyActivity.this, "Already submitted!!", Toast.LENGTH_SHORT).show();
//            }
        });

        cardRiskSurvey.setOnClickListener(view->{
            if (riskSurveyStatus=="0") {
                Intent intent = new Intent(this, RiskAssessmentActivity.class);
                intent.putExtra("mFarmerCode", farmerCode);
                intent.putExtra("mFarmerObj", farmersTable);
                startActivity(intent);

            }else {
            Toast.makeText(SurveyActivity.this, "Already submitted!!!!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    private void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFarmerParentSurveyStatus(farmerCode);
        getRiskSurveyStatus(farmerCode);
    }

    public void getFarmerParentSurveyStatus(String fid){
        try {
            viewModel.getFarmerHouseholdParentSurveyDetailsFromLocalDbById(fid);
            if (viewModel.getFarmerHouseholdParentSurveyDetailsByIdLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<FarmerHouseholdParentSurvey> farmerParentSurveys = (List<FarmerHouseholdParentSurvey>) o;
                        viewModel.getFarmerHouseholdParentSurveyDetailsByIdLiveData().removeObserver(this);
                        if (farmerParentSurveys != null && farmerParentSurveys.size() > 0) {
                            farmerSurveyStatus="1";
                        } else {
                            farmerSurveyStatus="0";
                        }
                    }
                };
                viewModel.getFarmerHouseholdParentSurveyDetailsByIdLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void getRiskSurveyStatus(String fid){
        try {
            viewModel.getRiskDetailsFromLocalDbById(fid);
            if (viewModel.getRiskAssessmentDetailsByIdLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<RiskAssessment> riskAssessments = (List<RiskAssessment>) o;
                        viewModel.getRiskAssessmentDetailsByIdLiveData().removeObserver(this);
                        if (riskAssessments != null && riskAssessments.size() > 0) {
                            riskSurveyStatus="1";
                        } else {
                            riskSurveyStatus="0";
                        }
                    }
                };
                viewModel.getRiskAssessmentDetailsByIdLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getLanguageFromLocalDb(String stLanguage, String stWord) {

        try {
            if (viewModel.getLanguageDataVM(stLanguage, stWord)!=null){
                return viewModel.getLanguageDataVM(stLanguage, stWord);
            } else{
                return stWord;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return stWord;
        }

    }
}