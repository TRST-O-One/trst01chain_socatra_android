package com.socatra.intellitrack.activity.Traderflow;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.DeviceDealerID;
import static com.socatra.intellitrack.constants.AppConstant.DeviceProcessorId;
import static com.socatra.intellitrack.constants.AppConstant.DeviceTraderId;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserName;

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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.socatra.intellitrack.BaseActivity;
import com.socatra.intellitrack.R;
import com.socatra.intellitrack.adapters.PlotsByTraderIdAdapter;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.models.get.GetPlotDetailsBasedOnTraderId;
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

public class PlotListbyTraderId extends BaseActivity implements View.OnClickListener {

    private static final String TAG = PlotListbyTraderId.class.getSimpleName();

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    public ViewModelProvider.Factory viewModelFactory;


    public AppViewModel viewModel;

    String strDealerID, strDealerName, strTraderId,strProcessorId;

    ImageView ImgCancel;

    LinearLayout imgempty;

    SearchView searchByName;


    RecyclerView recyclePlotsmain;

    ArrayList<GetPlotDetailsBasedOnTraderId> plotsList;

    PlotsByTraderIdAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plots_main);

        strTraderId = appHelper.getSharedPrefObj().getString(DeviceTraderId, "");
        strProcessorId = appHelper.getSharedPrefObj().getString(DeviceProcessorId, "");

        initializeUI();
        initializeValues();
        configureDagger();
        configureViewModel();
    }


    private void initializeUI() {
        ImgCancel = findViewById(R.id.img_cancel);
        recyclePlotsmain = findViewById(R.id.recycler_plots);
        searchByName = findViewById(R.id.svPlotsMain);
        imgempty = findViewById(R.id.emptyListImage);

    }

    private void initializeValues() {
        strDealerName = appHelper.getSharedPrefObj().getString(DeviceUserName, "");
        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");

        ImgCancel.setOnClickListener(view -> onBackPressed());

        plotsList = new ArrayList<>();

        recyclePlotsmain.setLayoutManager(new LinearLayoutManager(this));


        if (appHelper.isNetworkAvailable())
        {
            getPlotsList();
        }else {
            Toast.makeText(PlotListbyTraderId.this,"please check your internet connection",Toast.LENGTH_LONG).show();
        }



        if (searchByName != null) {


            SearchManager searchManager = (SearchManager) PlotListbyTraderId.this.getSystemService(Context.SEARCH_SERVICE);
            searchByName.setSearchableInfo(searchManager
                    .getSearchableInfo(PlotListbyTraderId.this.getComponentName()));
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
        AndroidInjection.inject(PlotListbyTraderId.this);
    }

    public void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
    }


    private void getPlotsList() {


        AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);

        final ProgressDialog progressDialog = new ProgressDialog(PlotListbyTraderId.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading data...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Call<JsonElement> callRetrofit = service.getPlotDetailsBasedOnTraderIdFromServer(strTraderId,  appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));

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
                            ArrayList<GetPlotDetailsBasedOnTraderId> getFarmerListTableArrayList = new ArrayList<>();


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                GetPlotDetailsBasedOnTraderId Plotslist = new GetPlotDetailsBasedOnTraderId();
                                Plotslist.setFarmerCode(jsonObjectFarmerPD.getString("FarmerCode"));
                                Plotslist.setTotalPlots(Integer.valueOf(jsonObjectFarmerPD.getString("TotalPlots")));
                                getFarmerListTableArrayList.add(Plotslist);

                            }

                            // Update the adapter with the new data
                            plotsList.addAll(getFarmerListTableArrayList);

                            adapter = new PlotsByTraderIdAdapter(appHelper,plotsList);


                            if (adapter.getItemCount() == 0) {
                                imgempty.setVisibility(View.VISIBLE);
                                recyclePlotsmain.setVisibility(View.GONE);
                            } else {
                                imgempty.setVisibility(View.GONE);
                                recyclePlotsmain.setVisibility(View.VISIBLE);
                            }
                            recyclePlotsmain.setHasFixedSize(true);
                            recyclePlotsmain.setAdapter(adapter);

                            adapter.notifyDataSetChanged();


                        } catch (Exception ex) {
                            progressDialog.dismiss();

                            ex.printStackTrace();
                            Log.d("Error", ">>>>" + ex.toString());
                        }
                    } else {
                        progressDialog.dismiss();

                        Toast.makeText(PlotListbyTraderId.this, "no records found", Toast.LENGTH_LONG).show();
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
    public void onClick(View v) {

    }
}

