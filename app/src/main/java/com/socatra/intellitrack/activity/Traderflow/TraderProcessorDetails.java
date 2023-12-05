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
import com.socatra.intellitrack.adapters.TraderProcessorListAdapter;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.models.get.GetProcessorDetailsByTraderId;
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

public class TraderProcessorDetails extends BaseActivity implements View.OnClickListener {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    public AppViewModel viewModel;

    SearchView svFarmer,searchByName;

    LinearLayout imgempty;

    private static final String TAG = TraderProcessorDetails.class.getCanonicalName();

    String strTraderId;

    ImageView ImgCancel;




    RecyclerView recycleProcessor;

    ArrayList<GetProcessorDetailsByTraderId> Processorlist;

    TraderProcessorListAdapter traderProcessorListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processor_detailsby_trader);

        strTraderId = appHelper.getSharedPrefObj().getString(DeviceTraderId, "");

        initializeUI();
        initializeValues();
        configureDagger();
        configureViewModel();
    }




    private void initializeUI() {

        ImgCancel = findViewById(R.id.img_cancel);
        recycleProcessor = findViewById(R.id.recycler_Processor_trader);
        searchByName = findViewById(R.id.svTraderProcessor);
        imgempty = findViewById(R.id.emptyListImage);
    }

    private void initializeValues() {


        ImgCancel.setOnClickListener(view -> onBackPressed());





        if (searchByName != null) {

            SearchManager searchManager = (SearchManager) TraderProcessorDetails.this.getSystemService(Context.SEARCH_SERVICE);
            searchByName.setSearchableInfo(searchManager
                    .getSearchableInfo(TraderProcessorDetails.this.getComponentName()));
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
                    if (traderProcessorListAdapter != null && traderProcessorListAdapter.getFilter() != null) {
                        traderProcessorListAdapter.getFilter().filter(query);
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    if (traderProcessorListAdapter != null && traderProcessorListAdapter.getFilter() != null) {
                        traderProcessorListAdapter.getFilter().filter(query);
                    }
                    return false;
                }
            });

        }




        Processorlist = new ArrayList<>();
        recycleProcessor.setLayoutManager(new LinearLayoutManager(this));

//        adapter.setOnItemClickListener(new DealerListAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(GetDealerDetailsbyProcessorId item) {
//                Intent dealerProcurementIntent = new Intent(DealerlistActivity.this, DealerProcurementListActivity.class);
//                dealerProcurementIntent.putExtra("DealerId", item.getDealerId());
//                Log.d("DealerId", "DealerId: " + item.getDealerId());
//                startActivity(dealerProcurementIntent);
//            }
//        });





        if (appHelper.isNetworkAvailable())
        {
           getProcessorList();
        }else {
            Toast.makeText(TraderProcessorDetails.this,"please check your internet connection",Toast.LENGTH_LONG).show();
        }
        
    }

    private void configureDagger() {
        AndroidInjection.inject(TraderProcessorDetails.this);
    }

    public void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
    }

    private void getProcessorList() {

        AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);

        final ProgressDialog progressDialog = new ProgressDialog(TraderProcessorDetails.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading data...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Call<JsonElement> callRetrofit = service.getProcessorDetailsByTraderIdFromServer(strTraderId,  appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));

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
                            ArrayList<GetProcessorDetailsByTraderId> getProcessorListTableArrayList = new ArrayList<>();


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                GetProcessorDetailsByTraderId ProcessorList = new GetProcessorDetailsByTraderId();
                                ProcessorList.setProcessorName(jsonObjectFarmerPD.getString("ProcessorName"));
                                ProcessorList.setProcessorId(Integer.valueOf(jsonObjectFarmerPD.getString("ProcessorId")));

                                ProcessorList.setDealerCount(Integer.valueOf(jsonObjectFarmerPD.getString("DealerCount")));
                                ProcessorList.setFarmerCount(Integer.valueOf(jsonObjectFarmerPD.getString("FarmerCount")));


                                getProcessorListTableArrayList.add(ProcessorList);


                            }

                            Processorlist.addAll(getProcessorListTableArrayList);
                            traderProcessorListAdapter = new TraderProcessorListAdapter( Processorlist);

                            if (traderProcessorListAdapter.getItemCount() == 0) {
                                imgempty.setVisibility(View.VISIBLE);
                                recycleProcessor.setVisibility(View.GONE);
                            } else {
                                imgempty.setVisibility(View.GONE);
                                recycleProcessor.setVisibility(View.VISIBLE);
                            }
                            recycleProcessor.hasFixedSize();
                            recycleProcessor.setAdapter(traderProcessorListAdapter);
                            traderProcessorListAdapter.notifyDataSetChanged();
                        } catch (Exception ex) {
                            progressDialog.dismiss();

                            ex.printStackTrace();
                            Log.d("Error", ">>>>" + ex.toString());
                        }
                    } else {
                        progressDialog.dismiss();

                        Toast.makeText(TraderProcessorDetails.this, "no records found", Toast.LENGTH_LONG).show();
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