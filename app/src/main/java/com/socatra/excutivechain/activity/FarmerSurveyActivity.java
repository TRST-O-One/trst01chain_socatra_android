package com.socatra.excutivechain.activity;

import static com.socatra.excutivechain.AppConstant.DeviceUserID;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.socatra.excutivechain.AppConstant;
import com.socatra.excutivechain.BaseActivity;
import com.socatra.excutivechain.R;
import com.socatra.excutivechain.adapters.ChildSurveyAdapter;
import com.socatra.excutivechain.database.entity.FarmerHouseholdChildrenSurvey;
import com.socatra.excutivechain.database.entity.FarmerHouseholdParentSurvey;
import com.socatra.excutivechain.database.entity.FarmersTable;
import com.socatra.excutivechain.view_models.AppViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class FarmerSurveyActivity extends BaseActivity implements HasSupportFragmentInjector {
    private SharedPreferences preferences;

    TextView farmerSurveyAddH;

    TextView txt_farmer_code, txt_family_numbers, txt_occupation, txt_age, txt_gender, txt_marital_status, txt_spousename, txt_no_of_children, txt_childrendetails;
    String TAG = "FarmerSurveyActivityTAG";
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    public AppViewModel viewModel;

    String farmerCode = "";
    FarmersTable farmersTable;

    Spinner spStatus;

    String[] titleArr = new String[]{"Single", "Married"};

    String[] genderArr = new String[]{"Male", "Female", "Others"};

    String strStatus;

    TextView btnSave, addChildBtn;

    LinearLayout noOfChildrenLayout, childrenTableLayout, spouseNameLayout;


    EditText etAge, etGender, etFarmerCode, etOccupation, etNoOfChildren, etFamilyCount, etSpouseName;

    String stAge, stGender, stOccupation, stFamilyCount, stChildGender;

    String stSpouseName = "";

    int stNoOfChildren = 0;

    RecyclerView recycleChild;

    Dialog dialog;

    ArrayList<FarmerHouseholdChildrenSurvey> farmerHouseholdChildrenSurveyArrayList;

    ChildSurveyAdapter adapter;

    ImageView imgBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_survey);

        farmerCode = getIntent().getStringExtra("mFarmerCode");
        farmersTable = (FarmersTable) getIntent().getSerializableExtra("mFarmerObj");
//        getAreaValue();
        Log.e(TAG, farmerCode);
        Log.e(TAG, farmersTable.toString());

        initializeUI();
        configureDagger();
        configureViewModel();
        initializeValues();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        updateButtonLabels();
    }

    private String getSelectedLanguage() {
        return preferences.getString("selected_language", "English");
    }


    @SuppressLint("SetTextI18n")
    private void updateButtonLabels() {
        String selectedLanguage = getSelectedLanguage();

        String hdfarmerSurveyAddH = getResources().getString(R.string.farmer_survey_add);
        String hdtxt_farmer_code = getResources().getString(R.string.farmer_code);
        String hdtxt_family_numbers = getResources().getString(R.string.familytotal);
        String hdtxt_occupation = getResources().getString(R.string.occupation1);
        String hdtxt_age = getResources().getString(R.string.age);
        String hdtxt_gender = getResources().getString(R.string.gender);
        String hdtxt_marital_status = getResources().getString(R.string.marital);
        String hdtxt_spousename = getResources().getString(R.string.spousename);
        String hdtxt_no_of_children = getResources().getString(R.string.number_of_child);
        String hdtxt_childrendetails = getResources().getString(R.string.childrendetails);
        String hdaddChildBtn = getResources().getString(R.string.add);
        String hdbtnSave = getResources().getString(R.string.save);

        if (selectedLanguage.equals("English")) {
            farmerSurveyAddH.setText(hdfarmerSurveyAddH);
            txt_farmer_code.setText(hdtxt_farmer_code);
            txt_family_numbers.setText(hdtxt_family_numbers);
            txt_occupation.setText(hdtxt_occupation);
            txt_age.setText(hdtxt_age);
            txt_gender.setText(hdtxt_gender);
            txt_marital_status.setText(hdtxt_marital_status);
            txt_spousename.setText(hdtxt_spousename);
            txt_no_of_children.setText(hdtxt_no_of_children);
            txt_childrendetails.setText(hdtxt_childrendetails);
            addChildBtn.setText(hdaddChildBtn);
            btnSave.setText(hdbtnSave);
        } else {
            farmerSurveyAddH.setText(getLanguageFromLocalDb(selectedLanguage, hdfarmerSurveyAddH) + "/" + hdfarmerSurveyAddH);
            txt_farmer_code.setText(getLanguageFromLocalDb(selectedLanguage, hdtxt_farmer_code) + "/" + hdtxt_farmer_code);
            txt_family_numbers.setText(getLanguageFromLocalDb(selectedLanguage, hdtxt_family_numbers) + "/" + hdtxt_family_numbers);
            txt_occupation.setText(getLanguageFromLocalDb(selectedLanguage, hdtxt_occupation) + "/" + hdtxt_occupation);
            txt_age.setText(getLanguageFromLocalDb(selectedLanguage, hdtxt_age) + "/" + hdtxt_age);
            txt_gender.setText(getLanguageFromLocalDb(selectedLanguage, hdtxt_gender) + "/" + hdtxt_gender);
            txt_marital_status.setText(getLanguageFromLocalDb(selectedLanguage, hdtxt_marital_status) + "/" + hdtxt_marital_status);
            txt_spousename.setText(getLanguageFromLocalDb(selectedLanguage, hdtxt_spousename) + "/" + hdtxt_spousename);
            txt_no_of_children.setText(getLanguageFromLocalDb(selectedLanguage, hdtxt_no_of_children) + "/" + hdtxt_no_of_children);
            txt_childrendetails.setText(getLanguageFromLocalDb(selectedLanguage, hdtxt_childrendetails) + "/" + hdtxt_childrendetails);
            addChildBtn.setText(getLanguageFromLocalDb(selectedLanguage, hdaddChildBtn) + "/" + hdaddChildBtn);
            btnSave.setText(getLanguageFromLocalDb(selectedLanguage, hdbtnSave) + "/" + hdbtnSave);
        }

    }

    private void initializeUI() {
        spStatus = findViewById(R.id.spMaritalStatusFsur);
        noOfChildrenLayout = findViewById(R.id.llNoOfChildren);
        spouseNameLayout = findViewById(R.id.llSpouseNameFsur);
        childrenTableLayout = findViewById(R.id.llChildrenTable);
        btnSave = findViewById(R.id.txtSaveFsur);
        imgBack = findViewById(R.id.imgBackAddFsur);

        etAge = findViewById(R.id.etAgeFsur);
        etGender = findViewById(R.id.etGenderFsur);
        etSpouseName = findViewById(R.id.etSpouseNameFsur);
        etFarmerCode = findViewById(R.id.etFarmerCodeFsur);
        etOccupation = findViewById(R.id.etOccupationFsur);
        etNoOfChildren = findViewById(R.id.etNoOfChildrenFsur);
        etFamilyCount = findViewById(R.id.etFamilyCountFsur);
        recycleChild = findViewById(R.id.recycleFsur);
        addChildBtn = findViewById(R.id.addChildBtnFsur);

        //labels
        farmerSurveyAddH = findViewById(R.id.farmerSurveyAddH);
        txt_farmer_code = findViewById(R.id.txt_farmer_code);
        txt_family_numbers = findViewById(R.id.txt_family_numbers);
        txt_occupation = findViewById(R.id.txt_occupation);
        txt_age = findViewById(R.id.txt_age);
        txt_gender = findViewById(R.id.txt_gender);
        txt_marital_status = findViewById(R.id.txt_marital_status);
        txt_spousename = findViewById(R.id.txt_spousename);
        txt_no_of_children = findViewById(R.id.txt_no_of_children);
        txt_childrendetails = findViewById(R.id.txt_childrendetails);


    }

    private void initializeValues() {

        //back
        imgBack.setOnClickListener(view -> {
            finish();
        });

        //List init
        farmerHouseholdChildrenSurveyArrayList = new ArrayList<>();

        //adapter
        setMyChildAdapter();
        adapter = new ChildSurveyAdapter(appHelper, viewModel, farmerHouseholdChildrenSurveyArrayList);

        //recyclerview
        recycleChild.setLayoutManager(new LinearLayoutManager(this));
        recycleChild.hasFixedSize();
        recycleChild.setAdapter(adapter);

        //Age
        stAge = farmersTable.getAge();
        etAge.setText(stAge);

        //Gender
        stGender = farmersTable.getGender();
        etGender.setText(stGender);

        //Farmer code
        etFarmerCode.setText(farmerCode);

        //Child detail button
        addChildBtn.setOnClickListener(view -> {

            if (etNoOfChildren.getText().toString().isEmpty()) {
                Toast.makeText(this, "No. of Children cannot be empty!!", Toast.LENGTH_SHORT).show();
            } else {
                stNoOfChildren = Integer.parseInt(etNoOfChildren.getText().toString().trim());
                if (etNoOfChildren.getText().toString().equals("0")) {

                } else if (farmerHouseholdChildrenSurveyArrayList.size() < stNoOfChildren) {
                    addChildDialog();
                }
            }

        });


        //Save
        btnSave.setOnClickListener(view -> {
            if (etNoOfChildren.getText().toString().trim().isEmpty()) {
                stNoOfChildren = 0;
            } else {
                stNoOfChildren = Integer.parseInt(etNoOfChildren.getText().toString().trim());
            }

            //Main validation
            if (etFamilyCount.getText().toString().isEmpty()) {
                Toast.makeText(this, "Count cannot be empty", Toast.LENGTH_SHORT).show();
            } else if (etOccupation.getText().toString().isEmpty()) {
                Toast.makeText(this, "Occupation cannot be empty", Toast.LENGTH_SHORT).show();
            } else if (farmerHouseholdChildrenSurveyArrayList.size() != stNoOfChildren) {
                Toast.makeText(this, "Add children details!!", Toast.LENGTH_SHORT).show();
            } else {
                stOccupation = etOccupation.getText().toString().trim();
//                if (etNoOfChildren.getText().toString().trim().isEmpty()){
//                    stNoOfChildren=0;
//                } else {
//                    stNoOfChildren=Integer.parseInt(etNoOfChildren.getText().toString().trim());
//                }
                stFamilyCount = etFamilyCount.getText().toString().trim();
                stSpouseName = etSpouseName.getText().toString().trim();

                FarmerHouseholdParentSurvey farmerParentSurvey = new FarmerHouseholdParentSurvey();
                farmerParentSurvey.setFarmerCode(farmerCode);
                farmerParentSurvey.setFarmerId(String.valueOf(farmersTable.getId()));
                farmerParentSurvey.setFamilyCount(Integer.parseInt(stFamilyCount));
                farmerParentSurvey.setMaritalStatus(strStatus);
                farmerParentSurvey.setSpouseName(stSpouseName);//need to check for default
                farmerParentSurvey.setAge(Integer.parseInt(stAge));
                farmerParentSurvey.setGender(stGender);
                farmerParentSurvey.setOccupation(stOccupation);
                farmerParentSurvey.setNoofChildren(Integer.valueOf(stNoOfChildren));

                farmerParentSurvey.setIsActive("true");
                farmerParentSurvey.setSync(false);
                farmerParentSurvey.setServerSync("0");
                farmerParentSurvey.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                farmerParentSurvey.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                String dateTime = appHelper.getCurrentDateTime(AppConstant.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
                farmerParentSurvey.setCreatedDate(dateTime);
                farmerParentSurvey.setUpdatedDate(dateTime);

                Log.e(TAG, farmerParentSurvey.toString());
                viewModel.insertFarmerHouseholdParentSurveyDataLocalDB(farmerParentSurvey);

                for (int i = 0; i < farmerHouseholdChildrenSurveyArrayList.size(); i++) {
                    Log.e(TAG, farmerHouseholdChildrenSurveyArrayList.get(i).toString());
                    viewModel.insertFarmerHouseholdChildrenSurveyDataLocalDB(farmerHouseholdChildrenSurveyArrayList.get(i));
                }

                Toast.makeText(this, "Farmer Survey Saved!!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        //Status
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, titleArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStatus.setAdapter(dataAdapter);

        spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strStatus = parent.getItemAtPosition(position).toString().trim();
                if (position == 0) {
                    noOfChildrenLayout.setVisibility(View.GONE);
                    childrenTableLayout.setVisibility(View.GONE);
                    spouseNameLayout.setVisibility(View.GONE);
                    stSpouseName = "";
                    etSpouseName.setText("");
                    stNoOfChildren = 0;
                    etNoOfChildren.setText("");
                    farmerHouseholdChildrenSurveyArrayList.clear();
                } else {
                    noOfChildrenLayout.setVisibility(View.VISIBLE);
                    childrenTableLayout.setVisibility(View.VISIBLE);
                    spouseNameLayout.setVisibility(View.VISIBLE);
                }
                Log.e(TAG, strStatus);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setMyChildAdapter() {

    }

    private void addChildDialog() {
        dialog = new Dialog(this, R.style.MyAlertDialogThemeNew);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.child_survey_dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        TextView etChildName, etChildOccupation, etChildAge, txtSaveChild, txtBackChild;
        TextView nameHd,occHd,ageHd,genderHd,childAddDialHd;
        Spinner spChildGender;

        etChildName = dialog.findViewById(R.id.etChildNameCdial);
        etChildOccupation = dialog.findViewById(R.id.etChildOccuCdial);
        etChildAge = dialog.findViewById(R.id.etChildAgeCdial);
        spChildGender = dialog.findViewById(R.id.spChildGenderCdial);
        txtSaveChild = dialog.findViewById(R.id.txtSaveCdial);
        txtBackChild = dialog.findViewById(R.id.txtBackCdial);

        //labels
        childAddDialHd= dialog.findViewById(R.id.childAddDialHd);
        nameHd= dialog.findViewById(R.id.nameHd);
        occHd= dialog.findViewById(R.id.occHd);
        ageHd= dialog.findViewById(R.id.ageHd);
        genderHd= dialog.findViewById(R.id.genderHd);

        updateDialogLabels(nameHd,occHd,ageHd,genderHd,txtSaveChild, txtBackChild,childAddDialHd);

        txtSaveChild.setOnClickListener(view -> {
            if (etChildName.getText().toString().isEmpty()) {
                Toast.makeText(FarmerSurveyActivity.this, "Name Cannot be empty!!", Toast.LENGTH_SHORT).show();
            } else if (etChildOccupation.getText().toString().isEmpty()) {
                Toast.makeText(FarmerSurveyActivity.this, "Occupation Cannot be empty!!", Toast.LENGTH_SHORT).show();
            } else if (etChildAge.getText().toString().isEmpty()) {
                Toast.makeText(FarmerSurveyActivity.this, "Age Cannot be empty!!", Toast.LENGTH_SHORT).show();
            } else {
                FarmerHouseholdChildrenSurvey farmerChildSurvey = new FarmerHouseholdChildrenSurvey();
                farmerChildSurvey.setFarmerCode(farmerCode);
                farmerChildSurvey.setFarmerHouseholdSurveyId(0);
                farmerChildSurvey.setFarmerId(String.valueOf(farmersTable.getId()));
                farmerChildSurvey.setChildrenName(etChildName.getText().toString().trim());
                farmerChildSurvey.setChildrenGender(stChildGender);
                farmerChildSurvey.setChildrenAge(Integer.parseInt(etChildAge.getText().toString().trim()));
                farmerChildSurvey.setChildrenOccupation(etChildOccupation.getText().toString().trim());

                farmerChildSurvey.setIsActive("true");
                farmerChildSurvey.setSync(false);
                farmerChildSurvey.setServerSync("0");
                farmerChildSurvey.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                farmerChildSurvey.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                String dateTime = appHelper.getCurrentDateTime(AppConstant.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
                farmerChildSurvey.setCreatedDate(dateTime);
                farmerChildSurvey.setUpdatedDate(dateTime);

                farmerHouseholdChildrenSurveyArrayList.add(farmerChildSurvey);
                adapter.notifyDataSetChanged();

                Toast.makeText(FarmerSurveyActivity.this, "Child Added!!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        txtBackChild.setOnClickListener(view -> {
            dialog.dismiss();
        });

        //For Gender
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(FarmerSurveyActivity.this,
                android.R.layout.simple_spinner_item, genderArr);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spChildGender.setAdapter(dataAdapter1);
        spChildGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                strFarmerGender = parent.getItemAtPosition(position).toString().trim();
                String st = (String) parent.getItemAtPosition(position);
                stChildGender = st;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void updateDialogLabels(TextView nameHd, TextView occHd, TextView ageHd, TextView genderHd, TextView txtSaveChild, TextView txtBackChild, TextView childAddDialHd) {
        String selectedLanguage = getSelectedLanguage();

        String HdchildAddDialHd=getResources().getString(R.string.add_child_details);
        String HdnameHd=getResources().getString(R.string.name);
        String HdoccHd=getResources().getString(R.string.occupation1);
        String HdageHd=getResources().getString(R.string.age);
        String HdgenderHd=getResources().getString(R.string.gender);
        String HdtxtSaveChild=getResources().getString(R.string.save);
        String HdtxtBackChild=getResources().getString(R.string.m_back);

        if (selectedLanguage.equals("English")) {
            childAddDialHd.setText(HdchildAddDialHd);
            nameHd.setText(HdnameHd);
            occHd.setText(HdoccHd);
            ageHd.setText(HdageHd);
            genderHd.setText(HdgenderHd);
            txtSaveChild.setText(HdtxtSaveChild);
            txtBackChild.setText(HdtxtBackChild);
        } else {
            childAddDialHd.setText(getLanguageFromLocalDb(selectedLanguage, HdchildAddDialHd) + "/" + HdchildAddDialHd);
//            childAddDialHd.setText(getLanguageFromLocalDb(selectedLanguage, HdchildAddDialHd));
            nameHd.setText(getLanguageFromLocalDb(selectedLanguage, HdnameHd) + "/" + HdnameHd);
            occHd.setText(getLanguageFromLocalDb(selectedLanguage, HdoccHd) + "/" + HdoccHd);
            ageHd.setText(getLanguageFromLocalDb(selectedLanguage, HdageHd) + "/" + HdageHd);
            genderHd.setText(getLanguageFromLocalDb(selectedLanguage, HdgenderHd) + "/" + HdgenderHd);
            txtSaveChild.setText(getLanguageFromLocalDb(selectedLanguage, HdtxtSaveChild) + "/" + HdtxtSaveChild);
            txtBackChild.setText(getLanguageFromLocalDb(selectedLanguage, HdtxtBackChild) + "/" + HdtxtBackChild);
        }
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
//        try {
//            adapter.notifyDataSetChanged();
////            recycleChild.setAdapter(adapter);
//            Log.e(TAG,"on Resume Called");
//            Log.e(TAG,farmerHouseholdChildrenSurveyArrayList.toString());
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        setMyChildAdapter();
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