package com.socatra.excutivechain.activity;

import androidx.annotation.Nullable;
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
import com.socatra.excutivechain.database.entity.DealerFarmer;
import com.socatra.excutivechain.database.entity.FarmersTable;
import com.socatra.excutivechain.database.entity.ManfacturerFarmer;
import com.socatra.excutivechain.view_models.AppViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class FarmerMappingActivity extends BaseActivity implements HasSupportFragmentInjector {
    private SharedPreferences preferences;

    TextView mapfarmer;

    String TAG="FarmerMappingActivityTAG";
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    public AppViewModel viewModel;

    String farmerCode;

    FarmersTable farmersTable;

    ImageView imgBack;
    TextView txtDealer,txtManufacturer,txtFarmerCode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_mapping);

        farmerCode=getIntent().getStringExtra("mFarmerCode");
        farmersTable=(FarmersTable) getIntent().getSerializableExtra("mFarmerObj");

        Log.e(TAG,farmerCode);

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

        String hdMapFarmerAs=getResources().getString(R.string.mapfarmer);
        String hdProcessor=getResources().getString(R.string.Processor);
        String hdDealer=getResources().getString(R.string.Dealer);

        if (selectedLanguage.equals("English")) {
            mapfarmer.setText(hdMapFarmerAs);
            txtManufacturer.setText(hdProcessor);
            txtDealer.setText(hdDealer);
        } else {
            mapfarmer.setText(getLanguageFromLocalDb(selectedLanguage,hdMapFarmerAs)+ "/" + hdMapFarmerAs);
            txtManufacturer.setText(getLanguageFromLocalDb(selectedLanguage,hdProcessor)+ "/" + hdProcessor);
            txtDealer.setText(getLanguageFromLocalDb(selectedLanguage,hdDealer)+ "/" + hdDealer);
        }


    }
        private String getSelectedLanguage() {
            return preferences.getString("selected_language", "English");
        }

    private void initializeUI() {

        imgBack=findViewById(R.id.imgBackFMap);
        txtFarmerCode=findViewById(R.id.txtFarmerCodeFMap);
        txtManufacturer=findViewById(R.id.txtManufacturerFMap);
        txtDealer=findViewById(R.id.txtDealerFMap);

        //Labels
        mapfarmer = findViewById(R.id.mapfarmer);

    }

    private void initializeValues() {

        //FarmerCode
        txtFarmerCode.setText("Farmer Code:"+farmerCode);

        //back
        imgBack.setOnClickListener(v -> {
            finish();
        });

        //Manufacturer btn
        txtManufacturer.setOnClickListener(v -> {
            Intent intent = new Intent(FarmerMappingActivity.this, ManufacturerActivity.class);
            intent.putExtra("mFarmerCode", farmerCode);
            intent.putExtra("mFarmerObj",farmersTable);
            startActivity(intent);
            finish();
        });

        //Dealer btn
        txtDealer.setOnClickListener(v -> {
            Intent intent = new Intent(FarmerMappingActivity.this, DealerActivity.class);
            intent.putExtra("mFarmerCode", farmerCode);
            intent.putExtra("mFarmerObj",farmersTable);
            startActivity(intent);
            finish();
        });
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    private void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
        getFarmerDealerStatus(farmerCode);
        getFarmerManufacturerStatus(farmerCode);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return null;
    }

    //Manufacturer
    private void getFarmerManufacturerStatus(String fid) {
        try {
            viewModel.getManfacturerFarmerDetailsFromLocalDbByFId(fid);
            if (viewModel.getManfacturerFarmerDetailsByIdLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<ManfacturerFarmer> manfacturerFarmers = (List<ManfacturerFarmer>) o;
                        viewModel.getManfacturerFarmerDetailsByIdLiveData().removeObserver(this);
                        if (manfacturerFarmers != null && manfacturerFarmers.size() > 0) {
//                            farmerManufacturerStatus =1;
//                            Log.e("dashListStat",manfacturerFarmers.toString());
                            Toast.makeText(FarmerMappingActivity.this, "Mapping already done!!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
//                            farmerManufacturerStatus=0;
//                            Toast.makeText(FarmerMappingActivity.this, "Mapping already done!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                viewModel.getManfacturerFarmerDetailsByIdLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    //Dealer
    private void getFarmerDealerStatus(String fid) {
        try {
            viewModel.getDealerFarmerDetailsFromLocalDbByFId(fid);
            if (viewModel.getDealerFarmerDetailsByIdLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<DealerFarmer> dealerFarmers = (List<DealerFarmer>) o;
                        viewModel.getDealerFarmerDetailsByIdLiveData().removeObserver(this);
                        if (dealerFarmers != null && dealerFarmers.size() > 0) {
//                            farmerDealerStatus =1;
//                            Log.e("dashListStat",dealerFarmers.toString());
                            Toast.makeText(FarmerMappingActivity.this, "Mapping already done!!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
//                            Toast.makeText(FarmerMappingActivity.this, "Mapping already done!!", Toast.LENGTH_SHORT).show();
//                            farmerDealerStatus=0;
                        }
                    }
                };
                viewModel.getDealerFarmerDetailsByIdLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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