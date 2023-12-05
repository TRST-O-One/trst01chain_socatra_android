package com.socatra.excutivechain.activity;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

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
import com.socatra.excutivechain.adapters.LabourSurveyAdapter;
import com.socatra.excutivechain.database.entity.PlantationLabourSurvey;
import com.socatra.excutivechain.view_models.AppViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class LabourSurveyHomeActivity extends BaseActivity implements HasSupportFragmentInjector {

    String TAG = "LabourSurveyHomeActivity";
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    public AppViewModel viewModel;
    TextView laboursurvey1;

    String farmerCode = "";

    ImageView imgBack;

    TextView txtAdd;

    RecyclerView recyclerView;

    LabourSurveyAdapter labourSurveyAdapter;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labour_survey_home);

        farmerCode = getIntent().getStringExtra("mFarmerCode");
//        getAreaValue();
        Log.e(TAG, farmerCode);

        initializeUI();
        initializeValues();
        configureDagger();
        configureViewModel();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        updateTextLabels();

    }

    @SuppressLint("SetTextI18n")
    private void updateTextLabels() {
        String selectedLanguage = getSelectedLanguage();

        String hdAdd = getResources().getString(R.string.add);
        String hdLabSurveyDet = getResources().getString(R.string.labour_survey_details);


        if (selectedLanguage.equals("English")) {
            txtAdd.setText(hdAdd);
            laboursurvey1.setText(hdLabSurveyDet);
        } else {
            txtAdd.setText(getLanguageFromLocalDb(selectedLanguage, hdAdd) + "/" + hdAdd);
            laboursurvey1.setText(getLanguageFromLocalDb(selectedLanguage, hdLabSurveyDet) + "/" + hdLabSurveyDet);
        }

    }

    private String getSelectedLanguage() {
        return preferences.getString("selected_language", "English");
    }

    private void initializeUI() {
        imgBack = findViewById(R.id.imgBackLabHome);
        txtAdd = findViewById(R.id.txtAddLabHome);
        recyclerView = findViewById(R.id.recyclerViewLabHome);

        //labels
        laboursurvey1 = findViewById(R.id.laboursurvey1);
    }

    private void initializeValues() {

        //Add
        txtAdd.setOnClickListener(view -> {
            Intent intent = new Intent(LabourSurveyHomeActivity.this, LabourSurveyActivity.class);
            intent.putExtra("mFarmerCode", farmerCode);
            startActivity(intent);
        });

        //Back
        imgBack.setOnClickListener(view -> {
            finish();
        });
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return null;
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    private void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPlantationData(farmerCode);
    }

    private void getPlantationData(String mFarmerCode) {
        try {
            viewModel.getPlantationLabourSurveyDetailsFromLocalDbById(mFarmerCode);
            if (viewModel.getPlantationLabourSurveyDetailsByIdLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<PlantationLabourSurvey> plantations = (List<PlantationLabourSurvey>) o;
                        viewModel.getPlantationLabourSurveyDetailsByIdLiveData().removeObserver(this);
                        if (plantations != null && plantations.size() > 0) {
                            labourSurveyAdapter = new LabourSurveyAdapter(appHelper, viewModel, plantations);
                            recyclerView.setAdapter(labourSurveyAdapter);
                        } else {
                            Toast.makeText(LabourSurveyHomeActivity.this, "No data!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                viewModel.getPlantationLabourSurveyDetailsByIdLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getLanguageFromLocalDb(String stLanguage, String stWord) {

        try {
            if (viewModel.getLanguageDataVM(stLanguage, stWord) != null) {
                return viewModel.getLanguageDataVM(stLanguage, stWord);
            } else {
                return stWord;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return stWord;
        }

    }
}