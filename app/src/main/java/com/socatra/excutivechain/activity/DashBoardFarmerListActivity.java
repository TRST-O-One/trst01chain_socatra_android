package com.socatra.excutivechain.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.socatra.excutivechain.utils.BaseActivity;

import com.socatra.excutivechain.utils.CommonUtils;
import com.socatra.excutivechain.R;
import com.socatra.excutivechain.adapters.FarmerDetailsListAdapter;

import com.socatra.excutivechain.database.entity.DealerFarmer;
import com.socatra.excutivechain.database.entity.FarmersTable;
import com.socatra.excutivechain.database.entity.ManfacturerFarmer;
import com.socatra.excutivechain.view_models.AppViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
//import com.socatra.excutivechain.BuildConfig;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import letsrock.areaviewlib.BuildConfig;

public class DashBoardFarmerListActivity extends BaseActivity implements View.OnClickListener, HasSupportFragmentInjector,
        FarmerDetailsListAdapter.SyncCallbackInterface {
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    public AppViewModel viewModel;
    private SharedPreferences preferences;
    private TextView refreshButton;
    private TextView addButton;
    private Button syncButton;
    TextView txtAddFarmer, txtSync, txtRefList;
    SearchView svFarmer;
    Dialog dialog;
    RecyclerView recycler;
    SearchView searchByName;
    FarmerDetailsListAdapter farmerDetailsListAdapter;
    ImageView imageOne;
    String farmerCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board_farmer_list);

        initializeUI();
        configureDagger();
        configureViewModel();
        initializeValues();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        refreshButton = findViewById(R.id.txtRefList);
        addButton = findViewById(R.id.txtAddFarmer);
        syncButton = findViewById(R.id.txtSync);


        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        updateButtonLabels();

    }


    private String getSelectedLanguage() {
        return preferences.getString("selected_language", "English");
    }

    private void updateButtonLabels() {
        String selectedLanguage = getSelectedLanguage();

        String hdRefresh = getResources().getString(R.string.refresh);
        String hdAdd = getResources().getString(R.string.add);
        String hdSync = getResources().getString(R.string.sync);

        if (selectedLanguage.equals("English")) {
            //Default English
            refreshButton.setText(hdRefresh);
            addButton.setText(hdAdd);
            syncButton.setText(hdSync);
        } else {
            refreshButton.setText(getLanguageFromLocalDb(selectedLanguage, hdRefresh) + "/" + hdRefresh);
            addButton.setText(getLanguageFromLocalDb(selectedLanguage, hdAdd) + "/" + hdAdd);
            syncButton.setText(getLanguageFromLocalDb(selectedLanguage, hdSync) + "/" + hdSync);
        }

    }

    private void initializeUI() {
        txtAddFarmer = findViewById(R.id.txtAddFarmer);
        txtRefList = findViewById(R.id.txtRefList);
        svFarmer = findViewById(R.id.svFarmer);
        txtSync = findViewById(R.id.txtSync);
        searchByName = findViewById(R.id.svFarmer);
        recycler = findViewById(R.id.recycler);
    }

    private void initializeValues() {


        txtAddFarmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoardFarmerListActivity.this, PersonalRegistrationActivity.class);
                startActivity(intent);
//                finish();
            }
        });

        txtRefList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFarmerlistFromLocalDb();
            }
        });

        txtSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoardFarmerListActivity.this, SyncActivity.class);
                startActivity(intent);
//                finish();
            }
        });

        searchByName.setOnClickListener(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler.setLayoutManager(mLayoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setNestedScrollingEnabled(false);
        recycler.setHasFixedSize(true);
        ((SimpleItemAnimator) recycler.getItemAnimator()).setSupportsChangeAnimations(false);


        SearchManager searchManager = (SearchManager) getApplicationContext().getSystemService(Context.SEARCH_SERVICE);
        searchByName.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchByName.setMaxWidth(Integer.MAX_VALUE);
        // listening to search query text change

        searchByName.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (farmerDetailsListAdapter != null && farmerDetailsListAdapter.getFilter() != null) {
                    farmerDetailsListAdapter.getFilter().filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                if (farmerDetailsListAdapter != null && farmerDetailsListAdapter.getFilter() != null) {
                    farmerDetailsListAdapter.getFilter().filter(query);
                }
                return false;
            }
        });


    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    private void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
        getFarmerlistFromLocalDb();

    }


    public void getFarmerlistFromLocalDb() {
        try {

            viewModel.getFarmerListFromLocalDBStatus();
            if (viewModel.getFarmerDetailsListLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<FarmersTable> odVisitSurveyTableList = (List<FarmersTable>) o;
                        viewModel.getFarmerDetailsListLiveData().removeObserver(this);
                        if (odVisitSurveyTableList != null && odVisitSurveyTableList.size() > 0) {

                            farmerDetailsListAdapter = new FarmerDetailsListAdapter(DashBoardFarmerListActivity.this,
                                    odVisitSurveyTableList, DashBoardFarmerListActivity.this, appHelper, viewModel);
                            recycler.setAdapter(farmerDetailsListAdapter);
                            farmerDetailsListAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(DashBoardFarmerListActivity.this, "no farmer list", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                viewModel.getFarmerDetailsListLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Todo:Dialog from recyclerView
    @SuppressLint("SetTextI18n")
    private void mainNavDialog(FarmersTable farmerTable1, String villageId) {
        String selectedLanguage = getSelectedLanguage();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        dialog = new Dialog(DashBoardFarmerListActivity.this, R.style.MyAlertDialogThemeNew);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.farmer_dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        TextView selectedFarm = dialog.findViewById(R.id.selectedFarm);
        TextView adddetails = dialog.findViewById(R.id.adddetailsd);
        TextView editpersonald = dialog.findViewById(R.id.editpersonald);
        TextView plantationd = dialog.findViewById(R.id.plantationd);
        TextView documend = dialog.findViewById(R.id.documend);
        TextView surveyd = dialog.findViewById(R.id.surveyd);
        TextView farmer_mapping = dialog.findViewById(R.id.farmer_mapping);

        selectedFarm.setText("Farmer Code : " + farmerCode);

        updateButtonLabels(selectedLanguage, adddetails, editpersonald, plantationd, documend, surveyd, farmer_mapping);

        LinearLayout farmerEdit = dialog.findViewById(R.id.ll_farmer_reg);
        LinearLayout plantationDetails = dialog.findViewById(R.id.ll_land_details);
        LinearLayout docDetails = dialog.findViewById(R.id.ll_doc_details);
        LinearLayout surveyDetails = dialog.findViewById(R.id.ll_survey_details);
        LinearLayout farmerMapping = dialog.findViewById(R.id.ll_mapping_details);

        farmerEdit.setOnClickListener(view -> {
            Intent intent = new Intent(DashBoardFarmerListActivity.this, EditPersonalDetailsActivity.class);
            intent.putExtra("mFarmerCode", farmerCode);
            intent.putExtra("farmData", farmerTable1);
            intent.putExtra("villageId", villageId);
            startActivity(intent);
            dialog.dismiss();
        });

        plantationDetails.setOnClickListener(view -> {
            Intent intent = new Intent(DashBoardFarmerListActivity.this, PlantationHomeActivity.class);
            intent.putExtra("mFarmerCode", farmerCode);
            startActivity(intent);
            dialog.dismiss();
        });

        docDetails.setOnClickListener(view -> {
            Intent intent = new Intent(DashBoardFarmerListActivity.this, DocumentHomeActivity.class);
            intent.putExtra("mFarmerCode", farmerCode);
            intent.putExtra("farmData", farmerTable1);
            intent.putExtra("villageId", villageId);
            startActivity(intent);
            dialog.dismiss();
        });

        surveyDetails.setOnClickListener(view -> {
            Intent intent = new Intent(DashBoardFarmerListActivity.this, SurveyActivity.class);
            intent.putExtra("mFarmerCode", farmerCode);
            intent.putExtra("mFarmerObj", farmerTable1);
            startActivity(intent);
            dialog.dismiss();
        });

        farmerMapping.setOnClickListener(view -> {
            getFarmerDealerStatus(farmerCode, farmerTable1);
        });


        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void updateButtonLabels(String selectedLanguage, TextView adddetails, TextView editpersonald, TextView plantationd, TextView documend, TextView surveyd, TextView farmer_mapping) {

        //Todo lang
        String hdAddDetails = getResources().getString(R.string.adddetails);
        String hdEditPersonal = getResources().getString(R.string.editpersonald);
        String hdPlantation = getResources().getString(R.string.plantationd);
        String hdDocument = getResources().getString(R.string.documend);
        String hdSurvey = getResources().getString(R.string.surveyd);
        String hdFarmerMapping = getResources().getString(R.string.farmer_mapping);

        if (selectedLanguage.equals("English")) {
            adddetails.setText(hdAddDetails);
            editpersonald.setText(hdEditPersonal);
            plantationd.setText(hdPlantation);
            documend.setText(hdDocument);
            surveyd.setText(hdSurvey);
            farmer_mapping.setText(hdFarmerMapping);
        } else {
            adddetails.setText(getLanguageFromLocalDb(selectedLanguage, hdAddDetails) + "/" + hdAddDetails);
            editpersonald.setText(getLanguageFromLocalDb(selectedLanguage, hdEditPersonal) + "/" + hdEditPersonal);
            plantationd.setText(getLanguageFromLocalDb(selectedLanguage, hdPlantation) + "/" + hdPlantation);
            documend.setText(getLanguageFromLocalDb(selectedLanguage, hdDocument) + "/" + hdDocument);
            surveyd.setText(getLanguageFromLocalDb(selectedLanguage, hdSurvey) + "/" + hdSurvey);
            farmer_mapping.setText(getLanguageFromLocalDb(selectedLanguage, hdFarmerMapping) + "/" + hdFarmerMapping);
        }

    }


    private void getFarmerDealerStatus(String fid, FarmersTable farmerTable1) {
        try {
            viewModel.getDealerFarmerDetailsFromLocalDbByFId(fid);
            viewModel.getDealerFarmerDetailsByIdLiveData().observe(this, dealerFarmers -> {
                //dealer mapping
                if (dealerFarmers.size() > 0) {
                    Log.e("validatDash", "Dealer not exist");
                    dialog.dismiss();
                    Toast.makeText(DashBoardFarmerListActivity.this, "Mapping already done!!", Toast.LENGTH_SHORT).show();
                } else {
                    // producer mapping
                    viewModel.getManfacturerFarmerDetailsFromLocalDbByFId(fid);
                    viewModel.getManfacturerFarmerDetailsByIdLiveData().observe(DashBoardFarmerListActivity.this, manfacturerFarmers -> {
                        if (manfacturerFarmers.size() > 0) {
                            Log.e("validatDash", "Producer exist");
                            dialog.dismiss();
                            Toast.makeText(DashBoardFarmerListActivity.this, "Mapping already done!!", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("validatDash", "Producer not exist");
                            Intent intent = new Intent(DashBoardFarmerListActivity.this, FarmerMappingActivity.class);
                            intent.putExtra("mFarmerCode", farmerCode);
                            intent.putExtra("mFarmerObj", farmerTable1);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    });
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("validatDash", "1st catch");
        }
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void openScreenCallback(int position, FarmersTable farmerTable, List<FarmersTable> farmer, String applicationType) {

    }

    @Override
    public void updateItemCallback(int position, FarmersTable applicationStatusTable, String strFarmerID) {

    }

    @Override
    public void addPlotDetailsCallback(int position, FarmersTable applicationStatusTable, String strFarmercode, ImageView imgFarmer) {
        imageOne = imgFarmer;
        farmerCode = applicationStatusTable.getFarmerCode();
        mainNavDialog(applicationStatusTable, applicationStatusTable.getVillageId());
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return null;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (farmerDetailsListAdapter != null) {
            farmerDetailsListAdapter.notifyDataSetChanged();//update view
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