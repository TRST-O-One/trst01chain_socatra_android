package com.socatra.intellitrack.activity.Traderflow;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.DeviceTraderId;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.socatra.intellitrack.BaseActivity;
import com.socatra.intellitrack.R;
import com.socatra.intellitrack.adapters.DealerListAdapter;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.models.get.GetDealerDetailsbyProcessorId;
import com.socatra.intellitrack.view_models.AppViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DealerDetailsinTrader extends BaseActivity implements View.OnClickListener {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    public AppViewModel viewModel;

    SearchView svFarmer,searchByName;

    LinearLayout imgempty;

    private static final String TAG = DealerDetailsinTrader.class.getCanonicalName();

    String strTraderId;

    String Token, strDealerID, strDealerName, strProcessorId;

    ImageView ImgCancel;




    RecyclerView recycleDealer;



    ArrayList<GetDealerDetailsbyProcessorId> Dealerlist;

    DealerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealerlist);

        strTraderId = appHelper.getSharedPrefObj().getString(DeviceTraderId, "");

        initializeUI();
        initializeValues();
        configureDagger();
        configureViewModel();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initializeUI() {

        ImgCancel = findViewById(R.id.img_cancel);
        recycleDealer = findViewById(R.id.recycler_dealer);
        searchByName = findViewById(R.id.svDealer);
        imgempty = findViewById(R.id.emptyListImage);

        ImgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initializeValues() {


        if (searchByName != null) {

            SearchManager searchManager = (SearchManager) DealerDetailsinTrader.this.getSystemService(Context.SEARCH_SERVICE);
            searchByName.setSearchableInfo(searchManager
                    .getSearchableInfo(DealerDetailsinTrader.this.getComponentName()));
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



        Dealerlist = new ArrayList<>();
        recycleDealer.setLayoutManager(new LinearLayoutManager(this));






        String ProcessorId = getIntent().getStringExtra("ProcessorId");
        Log.d("ProcessorId", "ProcessorId" + ProcessorId);

        if (appHelper.isNetworkAvailable())
        {
            getDealerList(ProcessorId);
        }else {
            Toast.makeText(DealerDetailsinTrader.this,"please check your internet connection",Toast.LENGTH_LONG).show();
        }

    }

    private void configureDagger() {
        AndroidInjection.inject(DealerDetailsinTrader.this);
    }

    public void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
    }

    private void getDealerList(String ProcessorId) {

        AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);

        final ProgressDialog progressDialog = new ProgressDialog(DealerDetailsinTrader.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading data...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Call<JsonElement> callRetrofit = service.getDealerDetailsByProcessorIdFromServer(ProcessorId,  appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));

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
                            ArrayList<GetDealerDetailsbyProcessorId> getDealerListTableArrayList = new ArrayList<>();


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                GetDealerDetailsbyProcessorId DealerList = new GetDealerDetailsbyProcessorId();
                                DealerList.setId(Integer.valueOf(jsonObjectFarmerPD.getString("Id")));
                                DealerList.setDealerName(jsonObjectFarmerPD.getString("DealerName"));
                                DealerList.setAddress(jsonObjectFarmerPD.getString("Address"));
                                DealerList.setPrimaryContactNo(jsonObjectFarmerPD.getInt("PrimaryContactNo"));

                                getDealerListTableArrayList.add(DealerList);


                            }

                            // Update the adapter with the new data
                            Dealerlist.addAll(getDealerListTableArrayList);

                            adapter = new DealerListAdapter(DealerDetailsinTrader.this,appHelper,viewModel, (List<GetDealerDetailsbyProcessorId>) Dealerlist);
                            recycleDealer.hasFixedSize();
                            recycleDealer.setAdapter(adapter);
                            if (adapter.getItemCount() == 0) {
                                imgempty.setVisibility(View.VISIBLE);
                                recycleDealer.setVisibility(View.GONE);
                            } else {
                                imgempty.setVisibility(View.GONE);
                                recycleDealer.setVisibility(View.VISIBLE);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (Exception ex) {
                            progressDialog.dismiss();
                            ex.printStackTrace();
                            Log.d("Error", ">>>>" + ex.toString());
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(DealerDetailsinTrader.this, "no records found", Toast.LENGTH_LONG).show();
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
