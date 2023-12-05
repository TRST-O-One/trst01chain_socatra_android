package com.socatra.intellitrack.activity;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.DeviceDealerID;
import static com.socatra.intellitrack.constants.AppConstant.DeviceProcessorId;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserName;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.socatra.intellitrack.BaseActivity;
import com.socatra.intellitrack.R;
import com.socatra.intellitrack.adapters.RiskAssesmentAdapter;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.models.get.GetRiskAssessmentForViewByFarmerCode;
import com.socatra.intellitrack.models.get.GetRiskAssessmentHeaderListByFarmerCode;
import com.socatra.intellitrack.view_models.AppViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RiskAssesmentActivity extends BaseActivity implements View.OnClickListener {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    public AppViewModel viewModel;

    SearchView svFarmer,searchByName;

    LinearLayout imgempty;

    private static final String TAG = RiskAssesmentActivity.class.getCanonicalName();

    String Token, strDealerID, strDealerName, strProcessorId;

    ImageView ImgCancel;




    RecyclerView recycleSurvey;

    ArrayList<GetRiskAssessmentForViewByFarmerCode> Surveylist;

    RiskAssesmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risk_assesment);

        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");
        strProcessorId = appHelper.getSharedPrefObj().getString(DeviceProcessorId, "");

        initializeUI();
        initializeValues();
        configureDagger();
        configureViewModel();
    }




    private void initializeUI() {

        ImgCancel = findViewById(R.id.img_cancel);
        recycleSurvey = findViewById(R.id.recycler_survey);
        searchByName = findViewById(R.id.svSurvey);
//        imgempty = findViewById(R.id.emptyListImage);
    }

    private void initializeValues() {
        strDealerName = appHelper.getSharedPrefObj().getString(DeviceUserName, "");
        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");
        Token = appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, "");

        ImgCancel.setOnClickListener(view -> onBackPressed());





        if (searchByName != null) {

            SearchManager searchManager = (SearchManager) RiskAssesmentActivity.this.getSystemService(Context.SEARCH_SERVICE);
            searchByName.setSearchableInfo(searchManager
                    .getSearchableInfo(RiskAssesmentActivity.this.getComponentName()));
            searchByName.setMaxWidth(Integer.MAX_VALUE);
// listening to search query text change

            searchByName.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (adapter != null && adapter.getFilter() != null) {
                        adapter.getFilter().filter(query);
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    if (adapter != null && adapter.getFilter() != null) {
                        adapter.getFilter().filter(query);
                    }
                    return false;
                }
            });

        }




        Surveylist = new ArrayList<>();
        recycleSurvey.setLayoutManager(new LinearLayoutManager(this));





        String FarmerCode = getIntent().getStringExtra("FarmerCode");
        Log.d("FarmerCode","FarmerCode" +FarmerCode);

        if (appHelper.isNetworkAvailable())
        {
            getCustomerList(FarmerCode);
        }else {
            Toast.makeText(RiskAssesmentActivity.this,"please check your internet connection",Toast.LENGTH_LONG).show();
        }



    }

    private void configureDagger() {
        AndroidInjection.inject(RiskAssesmentActivity.this);
    }

    public void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
    }

    private void getCustomerList(String FarmerCode) {

        AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);

        Call<JsonElement> callRetrofit = service.getRiskAssessmentForViewByFarmerCodeFromServer(FarmerCode,  appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    String strResponse = String.valueOf(response.body());
                    Log.d(TAG, "onResponse: >>>" + strResponse);
                    JSONObject jsonObject = new JSONObject(strResponse);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        try {
                            ArrayList<GetRiskAssessmentForViewByFarmerCode> getSurveyListTableArrayList = new ArrayList<>();
                            ArrayList<GetRiskAssessmentHeaderListByFarmerCode> getRiskAssessmentHeaderListByFarmerCodes = new ArrayList<>();

                           for (int j=0;j<jsonArray.length();j++)
                           {
                               JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(j);
                               GetRiskAssessmentHeaderListByFarmerCode getRiskAssessmentHeaderListByFarmerCode = new GetRiskAssessmentHeaderListByFarmerCode();

                               getRiskAssessmentHeaderListByFarmerCode.setQuestionCategory("QuestionCategory");
                           }


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);

                                for (int j =0 ;j< getRiskAssessmentHeaderListByFarmerCodes.size() ;j++)
                                {
                                    if (getRiskAssessmentHeaderListByFarmerCodes.get(i).getQuestionCategory().matches(jsonObjectFarmerPD.getString("QuestionCategory")))
                                    {
                                        GetRiskAssessmentForViewByFarmerCode SurveyList = new GetRiskAssessmentForViewByFarmerCode();
                                        SurveyList.setQuestionCategory((jsonObjectFarmerPD.getString("QuestionCategory")));
                                        SurveyList.setQuestions(jsonObjectFarmerPD.getString("Questions"));
                                        SurveyList.setAnswers(jsonObjectFarmerPD.getString("Answers"));
                                        SurveyList.setRn(jsonObjectFarmerPD.getString("Rn"));

                                        getSurveyListTableArrayList.add(SurveyList);
                                    }

                                }



                            }

                            // Update the adapter with the new data
                            Surveylist.addAll(getSurveyListTableArrayList);
                            adapter = new RiskAssesmentAdapter( Surveylist);


                            recycleSurvey.hasFixedSize();
                            recycleSurvey.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Log.d("Error", ">>>>" + ex.toString());
                        }
                    } else {
                        Toast.makeText(RiskAssesmentActivity.this, "no records found", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Log.d("Error", ">>>>" + ex.toString());
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
            }
        });
    }


    @Override
    public void onClick(View view) {

    }



//    @Override
//    public void onItemClick(GetDealerDetailsbyProcessorId Dealer) {
//        Toast.makeText(this, "DealerId: " + Dealer.getDealerId(), Toast.LENGTH_SHORT).show();
//        Intent DealerProcurementIntent = new Intent(this, DealerProcurementListActivity.class);
//        DealerProcurementIntent.putExtra("DealerId", Dealer.getDealerId());
//        Log.d("DealerId","Dealer" + Dealer.getDealerId());
//        startActivity(DealerProcurementIntent);
//    }






}