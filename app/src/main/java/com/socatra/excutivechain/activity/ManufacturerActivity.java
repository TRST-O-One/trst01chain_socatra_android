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
import com.socatra.excutivechain.database.entity.FarmersTable;
import com.socatra.excutivechain.database.entity.ManfacturerFarmer;
import com.socatra.excutivechain.database.entity.ManufacturerMaster;
import com.socatra.excutivechain.view_models.AppViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class ManufacturerActivity extends BaseActivity implements HasSupportFragmentInjector {
    private SharedPreferences preferences;

    TextView txt_farmer_code, processorName, processorContactName, processorPrimary, processorAddr,processorMapHd;
    String TAG = "ManufacturerActivityTAG";
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    public AppViewModel viewModel;

    String farmerCode;

    FarmersTable farmersTable;

    ImageView imgBack;

    EditText txtFarmerCode, etPrimaryContact, etAddress, etPrimaryContactName;

    TextView txtSave;

    Spinner spCategory;

    int spPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manufacturer);

        farmerCode = getIntent().getStringExtra("mFarmerCode");
        farmersTable = (FarmersTable) getIntent().getSerializableExtra("mFarmerObj");

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

        String hdFarmerCode = getResources().getString(R.string.farmer_code);
        String hdProcessorName = getResources().getString(R.string.Processor_name);
        String hdContactName = getResources().getString(R.string.contact_name);
        String hdPrimary = getResources().getString(R.string.primary_number);
        String hdAddress = getResources().getString(R.string.address);
        String hdSave = getResources().getString(R.string.save);
        String hdProcessorMap = getResources().getString(R.string.processor_mapping);

        if (selectedLanguage.equals("English")) {
            txt_farmer_code.setText(hdFarmerCode);
            processorName.setText(hdProcessorName);
            processorContactName.setText(hdContactName);
            processorPrimary.setText(hdPrimary);
            processorAddr.setText(hdAddress);
            txtSave.setText(hdSave);
            processorMapHd.setText(hdProcessorMap);
        } else {
            txt_farmer_code.setText(getLanguageFromLocalDb(selectedLanguage,hdFarmerCode)+ "/" + hdFarmerCode);
            processorName.setText(getLanguageFromLocalDb(selectedLanguage,hdProcessorName)+ "/" + hdProcessorName);
            processorContactName.setText(getLanguageFromLocalDb(selectedLanguage,hdContactName)+ "/" + hdContactName);
            processorPrimary.setText(getLanguageFromLocalDb(selectedLanguage,hdPrimary)+ "/" + hdPrimary);
            processorAddr.setText(getLanguageFromLocalDb(selectedLanguage,hdAddress)+ "/" + hdAddress);
            txtSave.setText(getLanguageFromLocalDb(selectedLanguage,hdSave)+ "/" + hdSave);
            processorMapHd.setText(getLanguageFromLocalDb(selectedLanguage,hdProcessorMap)+ "/" + hdProcessorMap);
        }

    }

    private String getSelectedLanguage() {
        return preferences.getString("selected_language", "English");
    }

    private void initializeUI() {
        imgBack = findViewById(R.id.imgBackMMap);
        txtFarmerCode = findViewById(R.id.etFarmerCodeMMap);
        spCategory = findViewById(R.id.spCategoryMMap);
        etPrimaryContact = findViewById(R.id.etPrimaryContactMMap);
        etAddress = findViewById(R.id.etAddressMMap);
        etPrimaryContactName = findViewById(R.id.etPrimaryContactNameMMap);
        txtSave = findViewById(R.id.txtSaveMMap);

        //Labels
        txt_farmer_code = findViewById(R.id.txt_farmer_code);
        processorName = findViewById(R.id.processor_name);
        processorContactName = findViewById(R.id.processor_contact_name);
        processorPrimary = findViewById(R.id.primary_number);
        processorAddr = findViewById(R.id.txt_plot_address);
        processorMapHd = findViewById(R.id.processorMapHd);


    }

    private void initializeValues() {

        //FarmerCode
        txtFarmerCode.setText(farmerCode);

        //back
        imgBack.setOnClickListener(v -> {
            finish();
        });

        //Spinner
        try {
            viewModel.getManufacturerMasterDetailsListFromLocalDB();
            if (viewModel.getManufacturerMasterDetailsLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<ManufacturerMaster> manufacturerMasters = (List<ManufacturerMaster>) o;
                        viewModel.getManufacturerMasterDetailsLiveData().removeObserver(this);
                        if (manufacturerMasters != null && manufacturerMasters.size() > 0) {

                            ManufacturerMaster master = new ManufacturerMaster();
                            master.setEntityName("--Select--");
                            manufacturerMasters.add(0, master);

                            ArrayAdapter<ManufacturerMaster> dataAdapterCont = new ArrayAdapter<>(ManufacturerActivity.this,
                                    android.R.layout.simple_spinner_item, manufacturerMasters);
                            dataAdapterCont.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            dataAdapterCont.notifyDataSetChanged();
                            spCategory.setAdapter(dataAdapterCont);

                            spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    ManufacturerMaster data = (ManufacturerMaster) parent.getItemAtPosition(position);
                                    etPrimaryContact.setText(data.getPrimaryContactNo());
                                    etPrimaryContactName.setText(data.getPrimaryContactName());
                                    etAddress.setText(data.getAddress());
                                    if (position == 0) {
                                        spPos = 0;
                                    } else {
                                        spPos = data.getId();
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        } else {
                            Log.e(TAG, "no data");
                            List<ManufacturerMaster> emptyList = new ArrayList<>();
                            ArrayAdapter<ManufacturerMaster> dataAdapterCont1 = new ArrayAdapter<>(ManufacturerActivity.this,
                                    android.R.layout.simple_spinner_item, emptyList);
                            dataAdapterCont1.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            spCategory.setAdapter(dataAdapterCont1);
                            dataAdapterCont1.notifyDataSetChanged();
                            spPos = 0;
                        }
                    }
                };
                viewModel.getManufacturerMasterDetailsLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        //Save
        txtSave.setOnClickListener(v -> {
            if (spPos == 0) {
                Toast.makeText(this, "Please Select Manufacturer!!", Toast.LENGTH_SHORT).show();
            } else {
                //Save to ManuFarm
                ManfacturerFarmer manfacturerFarmer = new ManfacturerFarmer();
                manfacturerFarmer.setFarmerCode(farmerCode);
                manfacturerFarmer.setManfacturerId(spPos);

                manfacturerFarmer.setIsActive("true");
                manfacturerFarmer.setSync(false);
                manfacturerFarmer.setServerSync("0");
                manfacturerFarmer.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                manfacturerFarmer.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                String dateTime = appHelper.getCurrentDateTime(AppConstant.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
                manfacturerFarmer.setCreatedDate(dateTime);
                manfacturerFarmer.setUpdatedDate(dateTime);

                viewModel.insertManfacturerFarmerDataLocalDB(manfacturerFarmer);


                Toast.makeText(this, "Successful!!", Toast.LENGTH_SHORT).show();
                finish();
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