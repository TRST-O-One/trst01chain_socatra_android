package com.socatra.intellitrack.activity.Traderflow;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.DeviceDealerID;
import static com.socatra.intellitrack.constants.AppConstant.DeviceProcessorId;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserName;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.socatra.intellitrack.BaseActivity;
import com.socatra.intellitrack.R;
import com.socatra.intellitrack.adapters.FarmerByDealerIdAdapter;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.models.get.GetFarmerbyDealerid;
import com.socatra.intellitrack.view_models.AppViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FarmerDetailsbyDealerinTrader extends BaseActivity implements FarmerByDealerIdAdapter.OnItemClickListener {

    String mTag = "FarmerActivityTag";



    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    public AppViewModel viewModel;

    SearchView svFarmer,searchByName;

    private static final String TAG = TraderProcessorFarmerList.class.getCanonicalName();

    String Token, strDealerID, strDealerName, strProcessorId;

    ImageView ImgCancel;

    LinearLayout imgempty;




    RecyclerView recycleFarmer;

    ArrayList<GetFarmerbyDealerid> farmerList;

    FarmerByDealerIdAdapter adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_farmer_details_list);
        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");
        strProcessorId = appHelper.getSharedPrefObj().getString(DeviceProcessorId, "");

        initializeUI();
        initializeValues();
        configureDagger();
        configureViewModel();
    }



    private void initializeUI() {
        ImgCancel = findViewById(R.id.img_cancel);
        recycleFarmer = findViewById(R.id.recycler_farmer);
//        svFarmer = findViewById(R.id.svFarmer);
        searchByName = findViewById(R.id.svFarmer);
        imgempty = findViewById(R.id.emptyListImage);


    }

    @Override
    public void onBackPressed() {

        finish();
        // overridePendingTransition(R.anim.rotate_out, R.anim.rotate_in);
    }

    private void initializeValues() {
        strDealerName = appHelper.getSharedPrefObj().getString(DeviceUserName, "");
        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");
        Token = appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, "");

        ImgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //   ImgCancel.setOnClickListener(view -> onBackPressed());





        if (searchByName != null) {

//            SearchManager searchManager = (SearchManager) getApplicationContext().getSystemService(Context.SEARCH_SERVICE);
//            searchByName.setSearchableInfo(searchManager
//                    .getSearchableInfo(getComponentName()));
//            searchByName.setMaxWidth(Integer.MAX_VALUE);
            // listening to search query text change

            SearchManager searchManager = (SearchManager) FarmerDetailsbyDealerinTrader.this.getSystemService(Context.SEARCH_SERVICE);
            searchByName.setSearchableInfo(searchManager
                    .getSearchableInfo(FarmerDetailsbyDealerinTrader.this.getComponentName()));
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



        farmerList = new ArrayList<>();
        recycleFarmer.setLayoutManager(new LinearLayoutManager(this));
        //recycleFarmer.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true));


        String DealerId = getIntent().getStringExtra("DealerId");
        Log.d("DealerId", "DealerId" + DealerId);

        if (appHelper.isNetworkAvailable())
        {
            getFarmerList(DealerId);
        }else {
            Toast.makeText(FarmerDetailsbyDealerinTrader.this,"please check your internet connection",Toast.LENGTH_LONG).show();
        }

    }

    private void configureDagger() {
        AndroidInjection.inject(FarmerDetailsbyDealerinTrader.this);
    }

    public void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
    }



    private void getFarmerList(String DealerId) {

        AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);

        final ProgressDialog progressDialog = new ProgressDialog(FarmerDetailsbyDealerinTrader.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading data...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Call<JsonElement> callRetrofit = service.getFarmerListbyDealerIdFromserver(DealerId,  appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));

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
                            progressDialog.dismiss();
                            ArrayList<GetFarmerbyDealerid> getFarmerListTableArrayList = new ArrayList<>();
                            farmerList.clear();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                GetFarmerbyDealerid farmerlist = new GetFarmerbyDealerid();
                                farmerlist.setId(Integer.valueOf(jsonObjectFarmerPD.getString("Id")));
                                farmerlist.setFarmerCode(jsonObjectFarmerPD.getString("FarmerCode"));
                                farmerlist.setFarmerName(jsonObjectFarmerPD.getString("FarmerName"));
                                farmerlist.setPrimaryContactNo(jsonObjectFarmerPD.getString("PrimaryContactNo"));
                                farmerlist.setAddress(jsonObjectFarmerPD.getString("Address"));
                                farmerlist.setPinCode(jsonObjectFarmerPD.getString("PinCode"));
                                farmerlist.setVillageName(jsonObjectFarmerPD.getString("VillageName"));
                                getFarmerListTableArrayList.add(farmerlist);
                            }

                            // Update the adapter with the new data
                            farmerList.addAll(getFarmerListTableArrayList);
                            adapter = new FarmerByDealerIdAdapter(appHelper, viewModel, (List<GetFarmerbyDealerid>) farmerList, "farmer");
                            if (adapter.getItemCount() == 0) {
                                imgempty.setVisibility(View.VISIBLE);
                                recycleFarmer.setVisibility(View.GONE);
                            } else {
                                imgempty.setVisibility(View.GONE);
                                recycleFarmer.setVisibility(View.VISIBLE);
                            }

                            recycleFarmer.hasFixedSize();
                            recycleFarmer.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

//                            if (adapter.getItemCount() == 0) {
//                                Toast.makeText(FarmerDetailsListActivity.this, "Adapter is empty", Toast.LENGTH_LONG).show();
//                            }


                        } catch (Exception ex) {
                            progressDialog.dismiss();
                            ex.printStackTrace();
                            Log.d("Error", ">>>>" + ex.toString());
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(FarmerDetailsbyDealerinTrader.this, "no records found", Toast.LENGTH_LONG).show();
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

    @Override
    public void onClick(View view) {
        // Handle click events here
        if (view.getId() == R.id.svFarmer) {
            // This is the search view click event
            // Add your handling logic here
        }
    }

    @Override
    public void onItemClick(GetFarmerbyDealerid farmer) {
//
//        Intent grnDetailsIntent = new Intent(this, GrnDetailsList.class);
//        grnDetailsIntent.putExtra("farmerCode", farmer.getFarmerCode());
//        grnDetailsIntent.putExtra("farmerName",farmer.getFarmerName());
//        startActivity(grnDetailsIntent);

    }




}
