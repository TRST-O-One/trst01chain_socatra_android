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
import com.socatra.excutivechain.database.entity.DealerFarmer;
import com.socatra.excutivechain.database.entity.DealerMaster;
import com.socatra.excutivechain.database.entity.FarmersTable;
import com.socatra.excutivechain.view_models.AppViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class DealerActivity extends BaseActivity implements HasSupportFragmentInjector {
    private SharedPreferences preferences;
    String TAG="DealerActivityTAG";
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    public AppViewModel viewModel;
    TextView txt_farmer_code,Dealern,txt_plot_address,primary_number,dealerMapHd;

    String farmerCode;

    FarmersTable farmersTable;

    ImageView imgBack;

    EditText txtFarmerCode,etPrimaryContact,etAddress;

    TextView txtSave;

    Spinner spCategory;

    int spPos=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer);

        farmerCode=getIntent().getStringExtra("mFarmerCode");
        farmersTable=(FarmersTable) getIntent().getSerializableExtra("mFarmerObj");

        Log.e(TAG,farmerCode);

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

        String hdFarmerCode=getResources().getString(R.string.farmer_code);
        String hdDealerName=getResources().getString(R.string.Dealern);
        String hdAddress=getResources().getString(R.string.address);
        String hdPrimary=getResources().getString(R.string.primary_number);
        String hdSave=getResources().getString(R.string.save);
        String hdDealerMap=getResources().getString(R.string.dealer_mapping);

        if (selectedLanguage.equals("English")) {
            txt_farmer_code.setText(hdFarmerCode);
            Dealern.setText(hdDealerName);
            txt_plot_address.setText(hdAddress);
            primary_number.setText(hdPrimary);
            txtSave.setText(hdSave);
            dealerMapHd.setText(hdDealerMap);
        } else {
            txt_farmer_code.setText(getLanguageFromLocalDb(selectedLanguage,hdFarmerCode)+ "/" + hdFarmerCode);
            Dealern.setText(getLanguageFromLocalDb(selectedLanguage,hdDealerName)+ "/" + hdDealerName);
            txt_plot_address.setText(getLanguageFromLocalDb(selectedLanguage,hdAddress)+ "/" + hdAddress);
            primary_number.setText(getLanguageFromLocalDb(selectedLanguage,hdPrimary)+ "/" + hdPrimary);
            txtSave.setText(getLanguageFromLocalDb(selectedLanguage,hdSave)+ "/" + hdSave);
            dealerMapHd.setText(getLanguageFromLocalDb(selectedLanguage,hdDealerMap)+ "/" + hdDealerMap);
        }



//        switch (selectedLanguage) {
//            case "English":
//
//                break;
//            case "Hindi":
//                txt_farmer_code.setText(getString(R.string.farmer_code_H) + "/" + getString(R.string.farmer_code));
//                Dealern.setText(getString(R.string.Dealern_H) + "/" + getString(R.string.Dealern));
//                txt_plot_address.setText(getString(R.string.address_H) + "/" + getString(R.string.address));
//                primary_number.setText(getString(R.string.primary_contact_H) + "/" + getString(R.string.primary_number));
//
//                break;
//            case "Vietnamese":
//                txt_farmer_code.setText(getString(R.string.farmer_code_V) + "/" + getString(R.string.farmer_code));
//                Dealern.setText(getString(R.string.Dealern_V) + "/" + getString(R.string.Dealern));
//                txt_plot_address.setText(getString(R.string.address_V) + "/" + getString(R.string.address));
//                primary_number.setText(getString(R.string.primary_number_V) + "/" + getString(R.string.primary_number));
//
//                break;
//            case "Thai":
//                txt_farmer_code.setText(getString(R.string.farmer_code_T) + "/" + getString(R.string.farmer_code));
//                Dealern.setText(getString(R.string.Dealern_T) + "/" + getString(R.string.Dealern));
//                txt_plot_address.setText(getString(R.string.address_T) + "/" + getString(R.string.address));
//                primary_number.setText(getString(R.string.main_number_T) + "/" + getString(R.string.primary_number));
//
//                break;
//            case "Malay":
//                txt_farmer_code.setText(getString(R.string.farmer_code_M) + "/" + getString(R.string.farmer_code));
//                Dealern.setText(getString(R.string.Dealern_M) + "/" + getString(R.string.Dealern));
//                txt_plot_address.setText(getString(R.string.address_M) + "/" + getString(R.string.address));
//                primary_number.setText(getString(R.string.primary_number_M) + "/" + getString(R.string.primary_number));
//
//                break;
//            case "Indonesian":
//                txt_farmer_code.setText(getString(R.string.farmer_code_I) + "/" + getString(R.string.farmer_code));
//                Dealern.setText(getString(R.string.Dealern_I) + "/" + getString(R.string.Dealern));
//                txt_plot_address.setText(getString(R.string.address_I) + "/" + getString(R.string.address));
//                primary_number.setText(getString(R.string.primary_number_I) + "/" + getString(R.string.primary_number));
//
//                break;
//        }
    }


    private String getSelectedLanguage() {
        return preferences.getString("selected_language", "English");
    }
    private void initializeUI() {
        imgBack=findViewById(R.id.imgBackDMap);
        txtFarmerCode=findViewById(R.id.etFarmerCodeDMap);
        spCategory=findViewById(R.id.spCategoryDMap);
        etPrimaryContact=findViewById(R.id.etPrimaryContactDMap);
        etAddress=findViewById(R.id.etAddressDMap);
        txtSave=findViewById(R.id.txtSaveDMap);

        //Labels
        txt_farmer_code = findViewById(R.id.txt_farmer_code);
        Dealern = findViewById(R.id.Dealern);
        txt_plot_address = findViewById(R.id.txt_plot_address);
        primary_number = findViewById(R.id.primary_number);
        dealerMapHd = findViewById(R.id.dealerMapHd);
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
            viewModel.getDealerMasterDetailsListFromLocalDB();
            if (viewModel.getDealerMasterDetailsLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<DealerMaster> dealerMasters = (List<DealerMaster>) o;
                        viewModel.getDealerMasterDetailsLiveData().removeObserver(this);
                        if (dealerMasters != null && dealerMasters.size() > 0) {

                            DealerMaster master=new DealerMaster();
                            master.setName("--Select--");
                            dealerMasters.add(0,master);

                            ArrayAdapter<DealerMaster> dataAdapterCont = new ArrayAdapter<>(DealerActivity.this,
                                    android.R.layout.simple_spinner_item, dealerMasters);
                            dataAdapterCont.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            dataAdapterCont.notifyDataSetChanged();
                            spCategory.setAdapter(dataAdapterCont);

                            spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    DealerMaster data = (DealerMaster) parent.getItemAtPosition(position);
                                    etPrimaryContact.setText(data.getPrimaryContactNo());
                                    etAddress.setText(data.getAddress());
                                    if (position==0){
                                        spPos=0;
                                    } else {
                                        spPos=data.getId();
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        } else {
                            Log.e(TAG, "no data");
                            List<DealerMaster> emptyList=new ArrayList<>();
                            ArrayAdapter<DealerMaster> dataAdapterCont1 = new ArrayAdapter<>(DealerActivity.this,
                                    android.R.layout.simple_spinner_item, emptyList);
                            dataAdapterCont1.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            spCategory.setAdapter(dataAdapterCont1);
                            dataAdapterCont1.notifyDataSetChanged();
                            spPos=0;
                        }
                    }
                };
                viewModel.getDealerMasterDetailsLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        //Save
        txtSave.setOnClickListener(v -> {
            if (spPos==0){
                Toast.makeText(this, "Please Select Dealer!!", Toast.LENGTH_SHORT).show();
            } else {
                //Save to DealerFarmer
                DealerFarmer dealerFarmer=new DealerFarmer();
                dealerFarmer.setFarmerCode(farmerCode);
                dealerFarmer.setDealerId(spPos);

                dealerFarmer.setIsActive("true");
                dealerFarmer.setSync(false);
                dealerFarmer.setServerSync("0");
                dealerFarmer.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                dealerFarmer.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                String dateTime = appHelper.getCurrentDateTime(AppConstant.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
                dealerFarmer.setCreatedDate(dateTime);
                dealerFarmer.setUpdatedDate(dateTime);

                viewModel.insertDealerFarmerDataLocalDB(dealerFarmer);



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