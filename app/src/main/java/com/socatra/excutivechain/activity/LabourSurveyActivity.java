package com.socatra.excutivechain.activity;

import static com.socatra.excutivechain.AppConstant.DeviceUserID;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.socatra.excutivechain.AppConstant;
import com.socatra.excutivechain.BaseActivity;
import com.socatra.excutivechain.R;
import com.socatra.excutivechain.database.entity.Plantation;
import com.socatra.excutivechain.database.entity.PlantationLabourSurvey;
import com.socatra.excutivechain.view_models.AppViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class LabourSurveyActivity extends BaseActivity implements HasSupportFragmentInjector {

    String TAG = "LabourSurveyActivity";
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    public AppViewModel viewModel;
    TextView laboursurvey1;
    TextView farmer_code1;

    TextView plotcode1;

    TextView noofworkers1;
    TextView noofmale1;
    TextView nooffemale1;
    TextView nofofresident1;
    TextView noofmigrants1;
    TextView childoccupation1;
    private SharedPreferences preferences;
    String farmerCode = "";

    EditText etFarmerCode, etNoOfFieldWorker, etNoOfMaleWorker, etNoOfFemaleWorker, etNoOfResident, etNoOfMigrant, etChildrenOcc;

    Spinner spPlot;

    TextView txtSave;

    String pltPos = "0";

    int pltId = 0;

    String strPlantCode = "Select plot";

    ImageView imgBack;

    List<String> plotListCode = new ArrayList<>();
    List<String> plotListIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labour_survey);

        farmerCode = getIntent().getStringExtra("mFarmerCode");
//        getAreaValue();
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

        String hdLabourSurveyAdd = getResources().getString(R.string.labour_survey_add);
        String hdFarmerCode = getResources().getString(R.string.farmer_code);
        String hdPlotCode = getResources().getString(R.string.plot_code);
        String hdNoOfWorkers = getResources().getString(R.string.number_of_workers);
        String hdNoOfMale = getResources().getString(R.string.male_workers);
        String hdNoOfFemale = getResources().getString(R.string.female_workers);
        String hdNoOfResident = getResources().getString(R.string.residents);
        String hdNoOfMigrants = getResources().getString(R.string.migrants);
        String hdChildOccupation = getResources().getString(R.string.children_occupation);
        String hdSave = getResources().getString(R.string.save);

        if (selectedLanguage.equals("English")) {
            laboursurvey1.setText(hdLabourSurveyAdd);
            farmer_code1.setText(hdFarmerCode);
            plotcode1.setText(hdPlotCode);
            noofworkers1.setText(hdNoOfWorkers);
            noofmale1.setText(hdNoOfMale);
            nooffemale1.setText(hdNoOfFemale);
            nofofresident1.setText(hdNoOfResident);
            noofmigrants1.setText(hdNoOfMigrants);
            childoccupation1.setText(hdChildOccupation);
            txtSave.setText(hdSave);
        } else {
            laboursurvey1.setText(getLanguageFromLocalDb(selectedLanguage, hdLabourSurveyAdd) + "/" + hdLabourSurveyAdd);
            farmer_code1.setText(getLanguageFromLocalDb(selectedLanguage, hdFarmerCode) + "/" + hdFarmerCode);
            plotcode1.setText(getLanguageFromLocalDb(selectedLanguage, hdPlotCode) + "/" + hdPlotCode);
            noofworkers1.setText(getLanguageFromLocalDb(selectedLanguage, hdNoOfWorkers) + "/" + hdNoOfWorkers);
            noofmale1.setText(getLanguageFromLocalDb(selectedLanguage, hdNoOfMale) + "/" + hdNoOfMale);
            nooffemale1.setText(getLanguageFromLocalDb(selectedLanguage, hdNoOfFemale) + "/" + hdNoOfFemale);
            nofofresident1.setText(getLanguageFromLocalDb(selectedLanguage, hdNoOfResident) + "/" + hdNoOfResident);
            noofmigrants1.setText(getLanguageFromLocalDb(selectedLanguage, hdNoOfMigrants) + "/" + hdNoOfMigrants);
            childoccupation1.setText(getLanguageFromLocalDb(selectedLanguage, hdChildOccupation) + "/" + hdChildOccupation);
            txtSave.setText(getLanguageFromLocalDb(selectedLanguage, hdSave) + "/" + hdSave);
        }

    }

    private String getSelectedLanguage() {
        return preferences.getString("selected_language", "English");
    }

    private void initializeUI() {
        spPlot = findViewById(R.id.spPlotLabSur);
        etFarmerCode = findViewById(R.id.etFarmerCodeAddLabSur);
        etNoOfFieldWorker = findViewById(R.id.etNoOfFieldWorker);
        etNoOfMaleWorker = findViewById(R.id.etNoOfMaleWorker);
        etNoOfFemaleWorker = findViewById(R.id.etNoOfFemaleWorker);
        etNoOfResident = findViewById(R.id.etNoOfResident);
        etNoOfMigrant = findViewById(R.id.etNoOfMigrant);
        etChildrenOcc = findViewById(R.id.etChildrenOcc);
        txtSave = findViewById(R.id.txtSaveLabour);
        imgBack = findViewById(R.id.imgBackAddLabSur);

        //Lables
        laboursurvey1 = findViewById(R.id.laboursurvey1);
        farmer_code1 = findViewById(R.id.farmer_code1);
        plotcode1 = findViewById(R.id.plotcode1);
        noofworkers1 = findViewById(R.id.noofworkers1);
        noofmale1 = findViewById(R.id.noofmale1);
        nooffemale1 = findViewById(R.id.nooffemale1);
        nofofresident1 = findViewById(R.id.nofofresident1);
        noofmigrants1 = findViewById(R.id.noofmigrants1);
        childoccupation1 = findViewById(R.id.childoccupation1);

    }

    private void initializeValues() {
        etFarmerCode.setText(farmerCode);

        //Plot Spinner
        getPlantationData(farmerCode);

        //back
        imgBack.setOnClickListener(v -> {
            finish();
        });


        spPlot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Plantation plantation= (Plantation) parent.getItemAtPosition(position);
//                pltId=plantation.getId();
//                strPlantCode=plantation.getPlotCode();
                strPlantCode = plotListCode.get(position);
                //Log.e("lbsa",String.valueOf(pltId));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //pltId=0;
                // Log.e("lbsa",String.valueOf(pltId));
            }
        });

        //Save btn
        txtSave.setOnClickListener(v -> {

            if (strPlantCode.equals("Select plot")) {
                Toast.makeText(this, "Please select plot!!", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(etNoOfFieldWorker.getText().toString().trim())) {
                Toast.makeText(this, "No Of Field Worker empty!!", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(etNoOfMaleWorker.getText().toString().trim())) {
                Toast.makeText(this, "No Of Male Worker empty!!", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(etNoOfFemaleWorker.getText().toString().trim())) {
                Toast.makeText(this, "No Of Female Worker empty!!", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(etNoOfResident.getText().toString().trim())) {
                Toast.makeText(this, "No Of Resident Worker empty!!", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(etNoOfMigrant.getText().toString().trim())) {
                Toast.makeText(this, "No Of Migrant Worker empty!!", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(etChildrenOcc.getText().toString().trim())) {
                Toast.makeText(this, "Children Occupation empty!!", Toast.LENGTH_SHORT).show();
            } else {
                PlantationLabourSurvey plantationLabourSurvey = new PlantationLabourSurvey();
                plantationLabourSurvey.setFarmerCode(farmerCode);
                plantationLabourSurvey.setPlantationCode(strPlantCode);
                plantationLabourSurvey.setNoOfFieldWorkers(Integer.valueOf(etNoOfFieldWorker.getText().toString().trim()));
                plantationLabourSurvey.setNoOfMaleWorkers(Integer.valueOf(etNoOfMaleWorker.getText().toString().trim()));
                plantationLabourSurvey.setNoOfFemaleWorkers(Integer.valueOf(etNoOfFemaleWorker.getText().toString().trim()));
                plantationLabourSurvey.setNoOfResident(Integer.valueOf(etNoOfResident.getText().toString().trim()));
                plantationLabourSurvey.setNoOfMigrant(Integer.valueOf(etNoOfMigrant.getText().toString().trim()));
                plantationLabourSurvey.setOccupationOfChildren(etChildrenOcc.getText().toString().trim());

                plantationLabourSurvey.setIsActive("true");
                plantationLabourSurvey.setSync(false);
                plantationLabourSurvey.setServerSync("0");
                plantationLabourSurvey.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                plantationLabourSurvey.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                String dateTime = appHelper.getCurrentDateTime(AppConstant.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
                plantationLabourSurvey.setCreatedDate(dateTime);
                plantationLabourSurvey.setUpdatedDate(dateTime);

                viewModel.insertPlantationLabourSurveyDataLocalDB(plantationLabourSurvey);
                //Update Plant
                viewModel.updatePlatDetailListTableForLabStatus("0", false, "true", dateTime,
                        appHelper.getSharedPrefObj().getString(DeviceUserID, ""), strPlantCode);
                Toast.makeText(this, "Successful!!", Toast.LENGTH_SHORT).show();
                finish();
            }
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

    private void getPlantationData(String mFarmerCode) {
        try {
            viewModel.getPlantationDetailsFromLocalDbByIdAndStatus(mFarmerCode);
            if (viewModel.getPlantationDetailsByIdLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<Plantation> plantations = (List<Plantation>) o;
                        viewModel.getPlantationDetailsByIdLiveData().removeObserver(this);
                        plotListCode.clear();
                        plotListCode.add("Select plot");
                        if (plantations != null && plantations.size() > 0) {
                            for (int i = 0; i < plantations.size(); i++) {
                                plotListCode.add(plantations.get(i).getPlotCode());
                            }
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(LabourSurveyActivity.this,
                                    android.R.layout.simple_spinner_item, plotListCode);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            dataAdapter.notifyDataSetChanged();
                            spPlot.setAdapter(dataAdapter);
                        } else {
                            Log.e(TAG, "no data");
                            Toast.makeText(LabourSurveyActivity.this, "no plot data exist", Toast.LENGTH_SHORT).show();
//                            List<Plantation> emptyList=new ArrayList<>();
//                            ArrayAdapter<Plantation> dataAdapterCont1 = new ArrayAdapter<>(LabourSurveyActivity.this,
//                                    android.R.layout.simple_spinner_item, emptyList);
//                            dataAdapterCont1.setDropDownViewResource(android.R.layout.simple_spinner_item);
//                            spPlot.setAdapter(dataAdapterCont1);
//                            dataAdapterCont1.notifyDataSetChanged();
                        }
                    }
                };
                viewModel.getPlantationDetailsByIdLiveData().observe(this, getLeadRawDataObserver);
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