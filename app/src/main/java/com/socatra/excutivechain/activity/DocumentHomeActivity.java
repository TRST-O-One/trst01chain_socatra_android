package com.socatra.excutivechain.activity;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.socatra.excutivechain.BaseActivity;
import com.socatra.excutivechain.R;
import com.socatra.excutivechain.adapters.DocHomeAdapter;
import com.socatra.excutivechain.database.entity.FarmersTable;
import com.socatra.excutivechain.database.entity.PlantationDocuments;
import com.socatra.excutivechain.view_models.AppViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class DocumentHomeActivity extends BaseActivity implements HasSupportFragmentInjector, DocHomeAdapter.SyncDocCallbackInterface {

    String TAG = "DocumentHomeActivityTAG";

    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    public AppViewModel viewModel;
    private SharedPreferences preferences;
    String farmerCode = "",villageId="";

    FarmersTable farmersTable;
    TextView txtAddDocHome;
    TextView documentationdetails1;

    TextView txtAddDoc;

    ImageView imgBack;

    RecyclerView recyclerView;

    DocHomeAdapter homeAdapter;

    ArrayList<PlantationDocuments> plantationDocuments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_home);

        farmerCode = getIntent().getStringExtra("mFarmerCode");
        villageId = getIntent().getStringExtra("villageId");
        farmersTable = (FarmersTable) getIntent().getSerializableExtra("farmData");

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

        String hdAdd=getResources().getString(R.string.add);
        String hdDocDetails=getResources().getString(R.string.document_details);

        if (selectedLanguage.equals("English")) {
            txtAddDoc.setText(hdAdd);
            documentationdetails1.setText(hdDocDetails);
        } else {
            txtAddDoc.setText(getLanguageFromLocalDb(selectedLanguage,hdAdd)+ "/" + hdAdd);
            documentationdetails1.setText(getLanguageFromLocalDb(selectedLanguage,hdDocDetails)+ "/" + hdDocDetails);
        }

//        switch (selectedLanguage) {
//            case "English":
//                txtAddDoc.setText(R.string.add);
//                documentationdetails1.setText(R.string.document_details);
//                break;
//            case "Hindi":
//                txtAddDoc.setText(getString(R.string.add_H) + " / " + getString(R.string.add));
//                documentationdetails1.setText(getString(R.string.document_details_H) + " / " + getString(R.string.document_details));
//                break;
//            case "Vietnamese":
//                txtAddDoc.setText(getString(R.string.add_V) + " / " + getString(R.string.add));
//                documentationdetails1.setText(getString(R.string.document_details_V) + " / " + getString(R.string.document_details));
//                break;
//            case "Thai":
//                txtAddDoc.setText(getString(R.string.add_T) + " / " + getString(R.string.add));
//                documentationdetails1.setText(getString(R.string.document_details_T) + " / " + getString(R.string.document_details));
//                break;
//            case "Malay":
//                txtAddDoc.setText(getString(R.string.add_M) + " / " + getString(R.string.add));
//                documentationdetails1.setText(getString(R.string.document_details_M) + " / " + getString(R.string.document_details));
//                break;
//            case "Indonesian":
//                txtAddDoc.setText(getString(R.string.add_I) + " / " + getString(R.string.add));
//                documentationdetails1.setText(getString(R.string.document_details_I) + " / " + getString(R.string.document_details));
//                break;
//    }

}
    private String getSelectedLanguage() {
        return preferences.getString("selected_language", "English");
    }
    private void initializeUI() {
        txtAddDoc=findViewById(R.id.txtAddDocHome);
        imgBack=findViewById(R.id.imgBackDocHome);
        recyclerView=findViewById(R.id.recyclerViewDocHome);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();

        //Labels
        txtAddDocHome = findViewById(R.id.txtAddDocHome);
        documentationdetails1 = findViewById(R.id.documentationdetails1);
    }

    private void initializeValues() {
        //Adapter
        homeAdapter=new DocHomeAdapter(appHelper,viewModel,plantationDocuments,DocumentHomeActivity.this);
        recyclerView.setAdapter(homeAdapter);

        //Add button
        txtAddDoc.setOnClickListener(view->{
            Intent intent=new Intent(this, DocumentDetailsActivity.class);
            intent.putExtra("mFarmerCode",farmerCode);
            intent.putExtra("farmData", farmersTable);
            intent.putExtra("villageId", villageId);
            startActivity(intent);
        });


        //Back button
        imgBack.setOnClickListener(view->{
            finish();
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
    public void addDocDetailsCallback(int position, PlantationDocuments applicationStatusTable, String strFarmerCode, String mDocId) {

    }

    //"National Identity Document","Legal Document","Consent Document"
    private void getDocumentData(String farmer_id){
        try {
            viewModel.getLocalDocIdentificationFromLocalDBByFidandDtype(farmer_id,"National Identity Document");
            if (viewModel.getDocIdentiFicationDeatilsTableFromLocalLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<PlantationDocuments> odVisitSurveyTableList = (List<PlantationDocuments>) o;
                        viewModel.getDocIdentiFicationDeatilsTableFromLocalLiveData().removeObserver(this);
                        if (odVisitSurveyTableList != null && odVisitSurveyTableList.size() > 0) {
//                            for (int i=0;i<odVisitSurveyTableList.size();i++){
//
//                            }
                            plantationDocuments.addAll(odVisitSurveyTableList);
                            homeAdapter.notifyDataSetChanged();

//                            Log.e(mTag,odVisitSurveyTableList.toString());
                        } else {
//                            Toast.makeText(DocumentHomeActivity.this, "No data!!", Toast.LENGTH_SHORT).show();
//                            Log.e(mTag,"No data for farmerCode Img 1");
                        }
                    }
                };
                viewModel.getDocIdentiFicationDeatilsTableFromLocalLiveData().observe(DocumentHomeActivity.this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getDocumentData1(String farmer_id){
        try {
            viewModel.getLocalDocIdentificationFromLocalDBByFidandDtype(farmer_id,"Legal Document");
            if (viewModel.getDocIdentiFicationDeatilsTableFromLocalLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<PlantationDocuments> odVisitSurveyTableList = (List<PlantationDocuments>) o;
                        viewModel.getDocIdentiFicationDeatilsTableFromLocalLiveData().removeObserver(this);
                        if (odVisitSurveyTableList != null && odVisitSurveyTableList.size() > 0) {
                            plantationDocuments.addAll(odVisitSurveyTableList);
                            homeAdapter.notifyDataSetChanged();
//                            recyclerView.setAdapter(homeAdapter);
//                            Log.e(mTag,odVisitSurveyTableList.toString());
                        } else {
//                            Toast.makeText(DocumentHomeActivity.this, "No data!!", Toast.LENGTH_SHORT).show();
//                            Log.e(mTag,"No data for farmerCode Img 1");
                        }
                    }
                };
                viewModel.getDocIdentiFicationDeatilsTableFromLocalLiveData().observe(DocumentHomeActivity.this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getDocumentData2(String farmer_id){
        try {
            viewModel.getLocalDocIdentificationFromLocalDBByFidandDtype(farmer_id,"Consent Document");
            if (viewModel.getDocIdentiFicationDeatilsTableFromLocalLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<PlantationDocuments> odVisitSurveyTableList = (List<PlantationDocuments>) o;
                        viewModel.getDocIdentiFicationDeatilsTableFromLocalLiveData().removeObserver(this);
                        if (odVisitSurveyTableList != null && odVisitSurveyTableList.size() > 0) {
                            plantationDocuments.addAll(odVisitSurveyTableList);
                            homeAdapter.notifyDataSetChanged();
//                            recyclerView.setAdapter(homeAdapter);
//                            Log.e(mTag,odVisitSurveyTableList.toString());
                        } else {
//                            Toast.makeText(DocumentHomeActivity.this, "No data!!", Toast.LENGTH_SHORT).show();
//                            Log.e(mTag,"No data for farmerCode Img 1");
                        }
                    }
                };
                viewModel.getDocIdentiFicationDeatilsTableFromLocalLiveData().observe(DocumentHomeActivity.this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        plantationDocuments.clear();
        getDocumentData(farmerCode);
        getDocumentData1(farmerCode);
        getDocumentData2(farmerCode);
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