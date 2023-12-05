package com.socatra.intellitrack.activity.main_dash_board;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.DeviceDealerID;
import static com.socatra.intellitrack.constants.AppConstant.DeviceProcessorId;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserName;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.socatra.intellitrack.BaseActivity;
import com.socatra.intellitrack.R;
import com.socatra.intellitrack.adapters.PlotsByDealerIdAdapter;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.models.get.GetPlotsByDealerId;
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

public class PlotsMainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = PlotsMainActivity.class.getSimpleName();

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    public ViewModelProvider.Factory viewModelFactory;


    public AppViewModel viewModel;

    String strDealerID, strDealerName, strProcessorId,strLanguageId;

    ImageView ImgCancel;

    LinearLayout imgempty;

    SearchView searchByName;


    RecyclerView recyclePlotsmain;

    ArrayList<GetPlotsByDealerId> plotsList;

    PlotsByDealerIdAdapter adapter;


    TextView txt_header_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plots_main);

        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");
        strProcessorId = appHelper.getSharedPrefObj().getString(DeviceProcessorId, "");


        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");


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

        txt_header_view = findViewById(R.id.txt_plot_main_list_view);

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
        strDealerName = appHelper.getSharedPrefObj().getString(DeviceUserName, "");
        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");

        ImgCancel.setOnClickListener(view -> onBackPressed());

        plotsList = new ArrayList<>();

        recyclePlotsmain.setLayoutManager(new LinearLayoutManager(this));


        if (appHelper.isNetworkAvailable())
        {
            getPlotsList();
        }else {
            Toast.makeText(PlotsMainActivity.this,"please check your internet connection",Toast.LENGTH_LONG).show();
        }



        if (searchByName != null) {


            SearchManager searchManager = (SearchManager) PlotsMainActivity.this.getSystemService(Context.SEARCH_SERVICE);
            searchByName.setSearchableInfo(searchManager
                    .getSearchableInfo(PlotsMainActivity.this.getComponentName()));
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
        AndroidInjection.inject(PlotsMainActivity.this);
    }

    public void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
    }


    private void getPlotsList() {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        final ProgressDialog progressDialog = new ProgressDialog(PlotsMainActivity.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading  data...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        Call<JsonElement> callRetrofit;

        if (strDealerID.isEmpty()) {
            callRetrofit = service.getplotDataByProcessoridFromServer(
                    strProcessorId,
                    appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, "")
            );
        } else {
            callRetrofit = service.getplotDataByDealeridFromServer(
                    strDealerID,
                    appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, "")
            );
        }

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
                            ArrayList<GetPlotsByDealerId> getFarmerListTableArrayList = new ArrayList<>();


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                GetPlotsByDealerId Plotslist = new GetPlotsByDealerId();
                                Plotslist.setFarmerCode(jsonObjectFarmerPD.getString("FarmerCode"));
                                Plotslist.setFarmerName(jsonObjectFarmerPD.getString("FarmerName"));
                                Plotslist.setTotalPlots(Integer.valueOf(jsonObjectFarmerPD.getString("TotalPlots")));
                                getFarmerListTableArrayList.add(Plotslist);

                            }

                            // Update the adapter with the new data
                            plotsList.addAll(getFarmerListTableArrayList);

                            adapter = new PlotsByDealerIdAdapter(appHelper,plotsList);


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

                        Toast.makeText(PlotsMainActivity.this, "no records found", Toast.LENGTH_LONG).show();
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

    private void getLanguageDataList() {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        final ProgressDialog progressDialog = new ProgressDialog(PlotsMainActivity.this, R.style.AppCompatAlertDialogStyle);
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
                                    if (getResources().getString(R.string.plot_main_list).equals(jsonEngWord)) {
                                        txt_header_view.setText(jsonEngWord);
                                    }else if (getResources().getString(R.string.search_by_farmer_name_code).equals(jsonEngWord)) {
                                        searchByName.setQueryHint(jsonEngWord);
                                    }


                                } else {
                                    if (getResources().getString(R.string.plot_main_list).equals(jsonEngWord)) {
                                        txt_header_view.setText(strConvertedWord);
                                    }else if (getResources().getString(R.string.search_by_farmer_name_code).equals(jsonEngWord)) {
                                        searchByName.setQueryHint(strConvertedWord);
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

                        Toast.makeText(PlotsMainActivity.this, "no records found", Toast.LENGTH_LONG).show();
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

