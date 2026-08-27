package com.socatra.excutivechain.activity;

import static com.socatra.excutivechain.utils.AppConstant.DeviceUserID;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.socatra.excutivechain.utils.BaseActivity;
import com.socatra.excutivechain.R;
import com.socatra.excutivechain.adapters.PlantationAdapter;
import com.socatra.excutivechain.database.entity.FarmersTable;
import com.socatra.excutivechain.database.entity.Plantation;
import com.socatra.excutivechain.database.entity.PlantationGeoBoundaries;
import com.socatra.excutivechain.database.entity.VillageTable;
import com.socatra.excutivechain.view_models.AppViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class PlantationHomeActivity extends BaseActivity implements HasSupportFragmentInjector, PlantationAdapter.SyncPlantationCallbackInterface {

    String TAG = "PlantationHomeActivity";
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    public AppViewModel viewModel;
    private SharedPreferences preferences;
    TextView txtAddPlantation, txtShowPlotsLimit;
    TextView plantationhome1;
    String strGetFarmerCode = "";

    ImageView imgBack;
    RecyclerView recyclerView;

    PlantationAdapter plantationAdapter;

    Dialog dialog;

    String plotCode = "";

    int gpsCat = 0;

    Integer intTotalPlotsNotSyncCount, intNumberOfPlotsForFarmer;
    private static final int FILE_SELECT_CODE = 0;

    String strPlotCode, strFarmerSendCode, strSendArea;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantation_home);

        strGetFarmerCode = getIntent().getStringExtra("mFarmerCode");
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

        String hdAdd = getResources().getString(R.string.add);
        String hdPlantationHome = getResources().getString(R.string.plantationhome1);

        if (selectedLanguage.equals("English")) {
            txtAddPlantation.setText(hdAdd);
            plantationhome1.setText(hdPlantationHome);
        } else {
            txtAddPlantation.setText(getLanguageFromLocalDb(selectedLanguage, hdAdd) + "/" + hdAdd);
            plantationhome1.setText(getLanguageFromLocalDb(selectedLanguage, hdPlantationHome) + "/" + hdPlantationHome);
        }
    }

    private String getSelectedLanguage() {
        return preferences.getString("selected_language", "English");
    }

    private void initializeUI() {
        txtAddPlantation = findViewById(R.id.txtAddPlantation);
        txtShowPlotsLimit = findViewById(R.id.txt_plot_Error);
        imgBack = findViewById(R.id.imgBackPlantHome);
        recyclerView = findViewById(R.id.recyclerViewPlant);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();
        //Labels
        txtAddPlantation = findViewById(R.id.txtAddPlantation);
        plantationhome1 = findViewById(R.id.plantationhome1);
    }

    private void initializeValues() {
        txtAddPlantation.setOnClickListener(view -> {
            Intent intent = new Intent(PlantationHomeActivity.this, PlantationActivity.class);
            intent.putExtra("mFarmerCode", strGetFarmerCode);
            startActivity(intent);
        });

        imgBack.setOnClickListener(view -> {
            finish();
        });
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    private void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
    }

    private void getPlantationData(String mFarmerCode) {
        try {
            viewModel.getPlantationDetailsFromLocalDbById(mFarmerCode);
            if (viewModel.getPlantationDetailsByIdLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<Plantation> plantations = (List<Plantation>) o;
                        viewModel.getPlantationDetailsByIdLiveData().removeObserver(this);
                        if (plantations != null && plantations.size() > 0) {
                            plantationAdapter = new PlantationAdapter(appHelper, viewModel, plantations, PlantationHomeActivity.this);
                            recyclerView.setAdapter(plantationAdapter);
                        } else {
                            Toast.makeText(PlantationHomeActivity.this, "No data!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                viewModel.getPlantationDetailsByIdLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void getCountForPlotsListByFarmerCode() {
        try {
            viewModel.getPlotListCountWhichNotSyncByStrFarmerCode(strGetFarmerCode).observe(this, integer -> intTotalPlotsNotSyncCount = integer);
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    public void getFramerDetailsByFarmerCodeFromLocalDb() {
        try {
            viewModel.getFarmerDetailsByFarmerCode(strGetFarmerCode);
            if (viewModel.getFarmersTableByFarmerCodeLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        FarmersTable farmersTables = (FarmersTable) o;
                        viewModel.getFarmersTableByFarmerCodeLiveData().removeObserver(this);
                        if (farmersTables != null) {
                            intNumberOfPlotsForFarmer = Integer.valueOf(farmersTables.getNoOfPlots());

                            if (intNumberOfPlotsForFarmer.equals(intTotalPlotsNotSyncCount)) {
                                txtShowPlotsLimit.setVisibility(View.VISIBLE);
                                txtAddPlantation.setVisibility(View.GONE);
                            } else {
                                txtShowPlotsLimit.setVisibility(View.GONE);
                                txtAddPlantation.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(PlantationHomeActivity.this, "data is empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                viewModel.getFarmersTableByFarmerCodeLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPlantationData(strGetFarmerCode);
        getCountForPlotsListByFarmerCode();
        getFramerDetailsByFarmerCodeFromLocalDb();
        getGeoCategDetails(plotCode);
    }

    private void getGeoCategDetails(String mPlotCode) {
        try {
            viewModel.getGeoBoudariesFromLocalDB(mPlotCode);
            if (viewModel.getGeoBoundariesFromLocalLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<PlantationGeoBoundaries> geoBoundariesList = (List<PlantationGeoBoundaries>) o;
                        viewModel.getGeoBoundariesFromLocalLiveData().removeObserver(this);
                        if (geoBoundariesList != null && geoBoundariesList.size() > 0) {
                            gpsCat = geoBoundariesList.get(0).getPlotCount();
                        } else {
                            gpsCat = 0;
                        }
                    }
                };
                viewModel.getGeoBoundariesFromLocalLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
//            Log.e("Villagelist","Null catch");
        }
    }

    @Override
    public void addPlantDetailsCallback(int position, Plantation applicationStatusTable, String strFarmercode, String mPlotCode) {
        Log.e(TAG, "testPlt" + applicationStatusTable.getPlotCode());
        Log.e(TAG, "testPlt:" + applicationStatusTable.getGeoboundariesArea());
        if (applicationStatusTable.getGeoboundariesArea().equals(0.0)) {
            dialogForGeoBoundaries(applicationStatusTable);
        } else {
            Toast.makeText(this, "Geo-boundaries already submitted!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getVillageNameCallback(String villageId, TextView txtVillageName) {
        getVillageDetailsFromLocalDb(villageId, txtVillageName);
    }

    public void getVillageDetailsFromLocalDb(String strvillageId, TextView txtVilageName) {
        try {
            viewModel.getDistricIDFromVillageTableDetailsByVillageId(strvillageId);
            if (viewModel.getDissIdFromVillageTableLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        VillageTable villageTable = (VillageTable) o;
                        viewModel.getDissIdFromVillageTableLiveData().removeObserver(this);
                        if (villageTable != null) {
                            String strvillageName = villageTable.getName();

                        } else {
                            Toast.makeText(PlantationHomeActivity.this, "data is empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                viewModel.getDissIdFromVillageTableLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void dialogForGeoBoundaries(Plantation applicationStatusTable1) {
        dialog = new Dialog(this, R.style.MyAlertDialogThemeNew);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.geo_bound_dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        LinearLayout byWalk = dialog.findViewById(R.id.ll_walk);
        LinearLayout byMap = dialog.findViewById(R.id.ll_map);
        LinearLayout upLoadKMl = dialog.findViewById(R.id.ll_kml_reader);
        TextView selectedPlotGeo = dialog.findViewById(R.id.selectedPlotGeo);
        LinearLayout byWalkSmap = dialog.findViewById(R.id.ll_sec_map);

        selectedPlotGeo.setText("Plot Code : " + applicationStatusTable1.getPlotCode());

        upLoadKMl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appHelper.isNetworkAvailable()) {

                    dialog.dismiss();

                    Intent intent = new Intent(PlantationHomeActivity.this, KMLMapsActivity.class);
                    intent.putExtra("PlotId", applicationStatusTable1.getPlotCode());
                    intent.putExtra("gpsCat", gpsCat);
                    intent.putExtra("FarmerCode", applicationStatusTable1.getFarmerCode());
                    intent.putExtra("ProvideSize", String.valueOf(applicationStatusTable1.getAreaInHectors()));
                    intent.putExtra("id", appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                    startActivityForResult(intent, RESULT_OK);
                } else {
                    dialog.dismiss();
                    Toast.makeText(PlantationHomeActivity.this, "please check your internet connection ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        byWalk.setOnClickListener(view -> {
            dialog.dismiss();
//            Toast.makeText(this, "Coming Soon!!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PlantationHomeActivity.this, FieldCalculatorActivity.class);
            intent.putExtra("PlotId", applicationStatusTable1.getPlotCode());
            intent.putExtra("gpsCat", gpsCat);
            intent.putExtra("FarmerCode", applicationStatusTable1.getFarmerCode());
            intent.putExtra("ProvideSize", String.valueOf(applicationStatusTable1.getAreaInHectors()));
            intent.putExtra("id", appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
            startActivityForResult(intent, RESULT_OK);
        });

        byWalkSmap.setOnClickListener(v -> {
            dialog.dismiss();
            //Todo : Map 2 test
            Intent intent = new Intent(PlantationHomeActivity.this, SecondMap.class);
            intent.putExtra("PlotId", applicationStatusTable1.getPlotCode());
            intent.putExtra("gpsCat", gpsCat);
            intent.putExtra("FarmerCode", applicationStatusTable1.getFarmerCode());
            intent.putExtra("ProvideSize", String.valueOf(applicationStatusTable1.getAreaInHectors()));
            intent.putExtra("id", appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
            startActivityForResult(intent, RESULT_OK);
        });

        byMap.setOnClickListener(view -> {
            dialog.dismiss();
            Intent intent = new Intent(PlantationHomeActivity.this, MapsActivity.class);
            intent.putExtra("PlotId", applicationStatusTable1.getPlotCode());
            intent.putExtra("gpsCat", gpsCat);
            intent.putExtra("FarmerCode", applicationStatusTable1.getFarmerCode());
            intent.putExtra("ProvideSize", String.valueOf(applicationStatusTable1.getAreaInHectors()));
            intent.putExtra("id", appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
            startActivityForResult(intent, RESULT_OK);
        });

        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_SELECT_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Intent intent = new Intent(PlantationHomeActivity.this, KMLMapsActivity.class);
            intent.putExtra("PlotId", strPlotCode);
            intent.putExtra("gpsCat", gpsCat);
            intent.putExtra("selected_uri", uri.toString());
            intent.putExtra("FarmerCode", strFarmerSendCode);
            intent.putExtra("ProvideSize", String.valueOf(strSendArea));
            intent.putExtra("id", appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
            startActivityForResult(intent, RESULT_OK);
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