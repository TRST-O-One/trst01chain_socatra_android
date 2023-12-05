package com.socatra.intellitrack.activity.main_dash_board;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.DeviceDealerID;
import static com.socatra.intellitrack.constants.AppConstant.DeviceDealerName;
import static com.socatra.intellitrack.constants.AppConstant.DeviceProcessorId;
import static com.socatra.intellitrack.constants.AppConstant.LanguageId;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.socatra.intellitrack.BaseActivity;
import com.socatra.intellitrack.R;
import com.socatra.intellitrack.adapters.ProcessorListAdapter;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.database.entity.GetProcessorDataFromServer;
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

public class DashBoardProcessorListActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = DashBoardProcessorListActivity.class.getCanonicalName();
    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    public AppViewModel viewModel;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    SearchView svFarmer, searchByName;
    String Token, strDealerID, strDealerName, strLanguageId,strProcessorId;

    ImageView ImgCancel;


    TextView txt_processor_title;


    RecyclerView recycleProcessor;

    LinearLayout imgempty;


    ProcessorListAdapter processorListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processor_list);

        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");
        strProcessorId = appHelper.getSharedPrefObj().getString(DeviceProcessorId, "");
        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");

        initializeUI();
        initializeValues();
        configureDagger();
        configureViewModel();
    }


    private void initializeUI() {

        ImgCancel = findViewById(R.id.img_back_press);
        recycleProcessor = findViewById(R.id.recycler_processor);
        searchByName = findViewById(R.id.svProcessor);
        imgempty = findViewById(R.id.emptyListImage);


        //language
        txt_processor_title = findViewById(R.id. txt_processor_title);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (appHelper.isNetworkAvailable()) {
            //getLanguageDataList();
        } else {
            Toast.makeText(getApplicationContext(), "please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeValues() {
        strDealerName = appHelper.getSharedPrefObj().getString(DeviceDealerName, "");
        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");
        Token = appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, "");

        ImgCancel.setOnClickListener(view -> onBackPressed());


        if (searchByName != null) {

            SearchManager searchManager = (SearchManager) DashBoardProcessorListActivity.this.getSystemService(Context.SEARCH_SERVICE);
            searchByName.setSearchableInfo(searchManager
                    .getSearchableInfo(DashBoardProcessorListActivity.this.getComponentName()));
            searchByName.setMaxWidth(Integer.MAX_VALUE);
// listening to search query text change

            searchByName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Expand the SearchView when clicked
                    searchByName.setIconified(false);
                }
            });

            searchByName.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (processorListAdapter != null && processorListAdapter.getFilter() != null) {
                        processorListAdapter.getFilter().filter(query);
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    if (processorListAdapter != null && processorListAdapter.getFilter() != null) {
                        processorListAdapter.getFilter().filter(query);
                    }
                    return false;
                }
            });

        }


        recycleProcessor.setLayoutManager(new LinearLayoutManager(this));

        if (appHelper.isNetworkAvailable()) {
            getProcessorList();
        } else {
            Toast.makeText(DashBoardProcessorListActivity.this, "please check your internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void configureDagger() {
        AndroidInjection.inject(DashBoardProcessorListActivity.this);
    }

    public void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
    }


    private void getProcessorList() {

        AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        final ProgressDialog progressDialog = new ProgressDialog(DashBoardProcessorListActivity.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading  data...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Call<JsonElement> callRetrofit = service.getProcessorListByDealerId(strDealerID, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));

        callRetrofit.enqueue(new Callback<JsonElement>() {
                                 @Override
                                 public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                                     try {
                                         progressDialog.dismiss();
                                         if (response.isSuccessful()) {
                                             String strResponse = String.valueOf(response.body());
                                             Log.d(TAG, "onResponse: >>>" + strResponse);
                                             JSONObject jsonObject = new JSONObject(strResponse);
                                             JSONArray jsonArray = jsonObject.getJSONArray("data");
                                             if (jsonArray.length() > 0) {
                                                 ArrayList<GetProcessorDataFromServer> ProcessorData = new ArrayList<>();

                                                 for (int i = 0; i < jsonArray.length(); i++) {
                                                     JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                                     GetProcessorDataFromServer ProcessorList = new GetProcessorDataFromServer();

                                                     ProcessorList.setProcessor(jsonObjectFarmerPD.getString("Processor"));
                                                     ProcessorList.setAddress(jsonObjectFarmerPD.getString("Address"));
                                                     ProcessorList.setPrimaryContactNo(jsonObjectFarmerPD.getString("PrimaryContactNo"));
                                                     ProcessorData.add(ProcessorList);

                                                 }
                                                 processorListAdapter = new ProcessorListAdapter(appHelper, viewModel, ProcessorData);
                                                 recycleProcessor.hasFixedSize();
                                                 recycleProcessor.setAdapter(processorListAdapter);
                                                 processorListAdapter.notifyDataSetChanged();
                                                 if (processorListAdapter.getItemCount() == 0) {
                                                     imgempty.setVisibility(View.VISIBLE);
                                                     recycleProcessor.setVisibility(View.GONE);
                                                 } else {
                                                     imgempty.setVisibility(View.GONE);
                                                     recycleProcessor.setVisibility(View.VISIBLE);
                                                 }

                                             }

                                         } else {
                                             progressDialog.dismiss();
                                             Toast.makeText(DashBoardProcessorListActivity.this, "no data found", Toast.LENGTH_SHORT).show();
                                         }
                                     } catch (Exception ex) {
                                         progressDialog.dismiss();
                                         ex.printStackTrace();
                                         Log.d("Error", ">>>>" + ex.toString());
                                         Toast.makeText(DashBoardProcessorListActivity.this, "no data found", Toast.LENGTH_SHORT).show();

                                     }
                                 }

                                 @Override
                                 public void onFailure(Call<JsonElement> call, Throwable t) {
                                     progressDialog.dismiss();
                                     Log.d("Error Call", ">>>>" + call.toString());
                                     Log.d("Error", ">>>>" + t.toString());
                                     Toast.makeText(DashBoardProcessorListActivity.this, "some thing error", Toast.LENGTH_SHORT).show();

                                 }
                             }
        );
    }


    @Override
    public void onClick(View view) {

    }


    private void getLanguageDataList() {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        final ProgressDialog progressDialog = new ProgressDialog(DashBoardProcessorListActivity.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading data...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        callRetrofit = service.getTransalteLanguageWordsByLanguageIdFromServer(strLanguageId,appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    String strResponse = String.valueOf(response.body());
                    Log.d(TAG, "onResponse: >>> Farmer" + strResponse);
                    JSONObject jsonObject = new JSONObject(strResponse);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        try {
                            progressDialog.dismiss();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);

                                String jsonEngWord = jsonObjectFarmerPD.getString("SelectedWord");
                                String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");


                                if (strLanguageId.equals("1")) {
                                    if (getResources().getString(R.string.processor_list).equals(jsonEngWord)) {
                                        txt_processor_title.setText(jsonEngWord);
                                    }


                                } else {
                                    if (getResources().getString(R.string.processor_list).equals(jsonEngWord)) {
                                        txt_processor_title.setText(strConvertedWord);
                                    }

                                }


                            }


                        } catch (Exception ex) {
                            ex.printStackTrace();
                            progressDialog.dismiss();
                            Log.d("Error", ">>>>" + ex.toString());
                        }
                    } else {
                        progressDialog.dismiss();

                        Toast.makeText(DashBoardProcessorListActivity.this, "no records found", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    progressDialog.dismiss();

                    Log.d("Error", ">>>>" + ex.toString());
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();

                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
            }
        });
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