package com.socatra.intellitrack.activity.main_dash_board;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.DeviceDealerID;
import static com.socatra.intellitrack.constants.AppConstant.DeviceProcessorId;
import static com.socatra.intellitrack.constants.AppConstant.LanguageId;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.socatra.intellitrack.BaseActivity;
import com.socatra.intellitrack.R;
import com.socatra.intellitrack.adapters.DealerProcurementListAdapter;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.models.get.GetDealerProcurementDetails;
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

public class DealerProcumentData extends BaseActivity {

    private static final String TAG = PlotsListActivity.class.getSimpleName();

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    public AppViewModel viewModel;

    TextView FarmerCode, FarmerName;

    String strDealerID,strProcessorId , strLanguageId;

    ImageView ImgCancel;

    SearchView searchByName;

    RecyclerView recycleDealerprocurement;

    ArrayList<GetDealerProcurementDetails> DealerProcurementList;

    DealerProcurementListAdapter adapter;


    TextView txt_l_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_procurement_list);

        String dealerId = getIntent().getStringExtra("DealerId");
        Log.d("DealerId","D:Id" +dealerId);


        recycleDealerprocurement = findViewById(R.id.recycler_dealer_procurement);

        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");
        strProcessorId = appHelper.getSharedPrefObj().getString(DeviceProcessorId, "");
        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycleDealerprocurement.setLayoutManager(layoutManager);
        DealerProcurementList = new ArrayList<>(); // Initialize the list

        // Create the adapter and set it to the RecyclerView
        adapter = new DealerProcurementListAdapter (appHelper,DealerProcurementList);
        recycleDealerprocurement.setAdapter(adapter);

        if (appHelper.isNetworkAvailable())
        {
            getDealerProcurementdetailsbyDealerId(dealerId);
        }else {
            Toast.makeText(DealerProcumentData.this,"please check your internet connection",Toast.LENGTH_LONG).show();
        }


        initializeUI();
        initializeValues();
        configureDagger();
        configureViewModel();



    }




    private void initializeUI() {
        ImgCancel = findViewById(R.id.img_cancel);
        recycleDealerprocurement = findViewById(R.id.recycler_plotdetails);
        searchByName = findViewById(R.id.svPlots);


        //language
        txt_l_header = findViewById(R.id.txt_dealer_procurement_title);
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
           // getLanguageDataList();
        } else {
            Toast.makeText(getApplicationContext(), "please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeValues() {

        ImgCancel.setOnClickListener(view -> onBackPressed());



// listening to search query text change

        if (searchByName != null) {

            SearchManager searchManager = (SearchManager) DealerProcumentData.this.getSystemService(Context.SEARCH_SERVICE);
            searchByName.setSearchableInfo(searchManager
                    .getSearchableInfo(DealerProcumentData.this.getComponentName()));
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

    }

    private void configureDagger() {
        AndroidInjection.inject(DealerProcumentData.this);
    }

    public void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
    }

    private void getDealerProcurementdetailsbyDealerId(String dealerId) {
        AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        final ProgressDialog progressDialog = new ProgressDialog(DealerProcumentData.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading  data...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Call<JsonElement> callRetrofit = service.getDealerProcurementByDealerIdFromServer(dealerId, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    progressDialog.dismiss();
                    if (response.isSuccessful()) {
                        JsonElement jsonElement = response.body();
                        if (jsonElement != null && jsonElement.isJsonObject()) {
                            JsonObject jsonObject = jsonElement.getAsJsonObject();
                            if (jsonObject.has("data") && jsonObject.get("data").isJsonArray()) {
                                JsonArray jsonArray = jsonObject.getAsJsonArray("data");
                                ArrayList<GetDealerProcurementDetails> getDealerProcurementTableArrayList = new ArrayList<>();

                                for (int i = 0; i < jsonArray.size(); i++) {
                                    JsonObject jsonObjectFarmerPD = jsonArray.get(i).getAsJsonObject();
                                    GetDealerProcurementDetails DealerProcurementList = new GetDealerProcurementDetails();
                                    DealerProcurementList.setNumberofTotalProcurement(Integer.valueOf(jsonObjectFarmerPD.get("NumberofTotalProcurement").getAsString()));
                                    DealerProcurementList.setSumofTotalProcurement(Integer.valueOf(jsonObjectFarmerPD.get("SumofTotalProcurement").getAsString()));


                                    getDealerProcurementTableArrayList.add(DealerProcurementList);
                                }

                                // Update the adapter with the new data
                                DealerProcurementList.clear();
                                DealerProcurementList.addAll(getDealerProcurementTableArrayList);
                                adapter.notifyDataSetChanged();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(DealerProcumentData.this, "No records found", Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(DealerProcumentData.this, "Response not successful", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    progressDialog.dismiss();
                    ex.printStackTrace();
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

    private void getLanguageDataList() {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        final ProgressDialog progressDialog = new ProgressDialog(DealerProcumentData.this, R.style.AppCompatAlertDialogStyle);
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
                                    if (getResources().getString(R.string.dealer_procurement_list).equals(jsonEngWord)) {
                                        txt_l_header.setText(jsonEngWord);
                                    }


                                } else {
                                    if (getResources().getString(R.string.dealer_procurement_list).equals(jsonEngWord)) {
                                        txt_l_header.setText(strConvertedWord);
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

                        Toast.makeText(DealerProcumentData.this, "no records found", Toast.LENGTH_LONG).show();
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

}