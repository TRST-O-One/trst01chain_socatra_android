package com.socatra.intellitrack.activity.main_dash_board;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserRole;
import static com.socatra.intellitrack.constants.AppConstant.LanguageId;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.socatra.intellitrack.BaseActivity;
import com.socatra.intellitrack.R;
import com.socatra.intellitrack.adapters.PlotsDetailsByFarmercodeAdapter;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.models.get.GetPlotsDetailsByFarmercode;
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


public class PlotsListActivity extends BaseActivity {
    private static final String TAG = PlotsListActivity.class.getSimpleName();

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    public AppViewModel viewModel;



    String deviceRoleName , strLanguageId;

    TextView FarmerCode, FarmerName;
    ImageView ImgCancel;

    LinearLayout imgempty;

    RelativeLayout rlFarmerName;

    TextView txtl_headerView,txtl_farmername,txtl_farmercode;

    SearchView searchByName;
    RecyclerView recycleplots;
    ArrayList<GetPlotsDetailsByFarmercode> PlotsdetailsList;
    PlotsDetailsByFarmercodeAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plots_list);

        deviceRoleName = appHelper.getSharedPrefObj().getString(DeviceUserRole, "");

        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");



        FarmerCode = findViewById(R.id.txtFarmerCode);
        FarmerName = findViewById(R.id.txtfarmerName);
        rlFarmerName = findViewById(R.id.rl_farmerName);

        String farmerCode = getIntent().getStringExtra("FarmerCode");
        String farmerName = getIntent().getStringExtra("FarmerName");

        FarmerCode.setText(farmerCode);
        FarmerName.setText(farmerName);

        Log.d("Nandini2", "Farmer Code: " + farmerCode);
        initializeUI();
        initializeValues();
        configureDagger();
        configureViewModel();

        recycleplots = findViewById(R.id.recycler_plotdetails);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycleplots.setLayoutManager(layoutManager);
        PlotsdetailsList = new ArrayList<>();




        if (deviceRoleName.equalsIgnoreCase("Trader")) {

            rlFarmerName.setVisibility(View.GONE);


        }





        if (appHelper.isNetworkAvailable())
        {
            getPlotDetailsByFarmerCode(farmerCode);
        }else {
            Toast.makeText(PlotsListActivity.this,"please check your internet connection",Toast.LENGTH_LONG).show();
        }

    }





    private void initializeUI() {
        ImgCancel = findViewById(R.id.img_cancel);
        recycleplots = findViewById(R.id.recycler_plotdetails);
        searchByName = findViewById(R.id.svPlots);
        imgempty = findViewById(R.id.emptyListImage);


        //language
        txtl_headerView = findViewById(R.id.txt_headerview);
        txtl_farmername = findViewById(R.id.txt_farmer_name_view);
        txtl_farmercode = findViewById(R.id.txt_farmer_code_view);



    }




    private void initializeValues() {

        ImgCancel.setOnClickListener(view -> onBackPressed());
        SearchManager searchManager = (SearchManager) PlotsListActivity.this.getSystemService(Context.SEARCH_SERVICE);
        searchByName.setSearchableInfo(searchManager
                .getSearchableInfo(PlotsListActivity.this.getComponentName()));
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


    @Override
    protected void onResume() {
        super.onResume();
        if (appHelper.isNetworkAvailable()) {
           // getLanguageDataList();
        } else {
            Toast.makeText(getApplicationContext(), "please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void configureDagger() {
        AndroidInjection.inject(PlotsListActivity.this);
    }

    public void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
    }

    private void getPlotDetailsByFarmerCode(String farmerCode) {
        AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        final ProgressDialog progressDialog = new ProgressDialog(PlotsListActivity.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading  data...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Call<JsonElement> callRetrofit = service.getplotDetailsByFarmerCodeFromServer(farmerCode, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));

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
                                ArrayList<GetPlotsDetailsByFarmercode> getplotListTableArrayList = new ArrayList<>();

                                for (int i = 0; i < jsonArray.size(); i++) {
                                    JsonObject jsonObjectFarmerPD = jsonArray.get(i).getAsJsonObject();
                                    GetPlotsDetailsByFarmercode plotList = new GetPlotsDetailsByFarmercode();
                                    plotList.setId(jsonObjectFarmerPD.get("Id").getAsInt());
                                    plotList.setFarmerCode(jsonObjectFarmerPD.get("FarmerCode").getAsString());
                                    plotList.setFarmerName(jsonObjectFarmerPD.get("FarmerName").getAsString());
                                    plotList.setAddress(jsonObjectFarmerPD.get("Address").getAsString());
                                    plotList.setPlotCode(jsonObjectFarmerPD.get("PlotCode").getAsString());
                                    plotList.setAreaInHectors(jsonObjectFarmerPD.get("AreaInHectors").getAsInt());
                                    plotList.setGeoboundariesArea(jsonObjectFarmerPD.get("GeoboundariesArea").getAsString());
                                    plotList.setTypeOfOwnership(jsonObjectFarmerPD.get("TypeOfOwnership").getAsString());

                                    getplotListTableArrayList.add(plotList);
                                }

                                // Update the adapter with the new data
                                PlotsdetailsList.clear();
                                PlotsdetailsList.addAll(getplotListTableArrayList);

                                adapter = new PlotsDetailsByFarmercodeAdapter(appHelper,PlotsdetailsList);
                                if (adapter.getItemCount() == 0) {
                                    imgempty.setVisibility(View.VISIBLE);
                                    recycleplots.setVisibility(View.GONE);
                                } else {
                                    imgempty.setVisibility(View.GONE);
                                    recycleplots.setVisibility(View.VISIBLE);
                                }
                                recycleplots.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(PlotsListActivity.this, "No records found", Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(PlotsListActivity.this, "Response not successful", Toast.LENGTH_LONG).show();
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
        final ProgressDialog progressDialog = new ProgressDialog(PlotsListActivity.this, R.style.AppCompatAlertDialogStyle);
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
                                    if (getResources().getString(R.string.plot_list).equals(jsonEngWord)) {
                                        txtl_headerView.setText(jsonEngWord);
                                    }else if(getResources().getString(R.string.farmer_name).equals(jsonEngWord)){
                                        txtl_farmername.setText(jsonEngWord);

                                    }else if(getResources().getString(R.string.farmer_code).equals(jsonEngWord)){
                                        txtl_farmercode.setText(jsonEngWord);

                                    }else if(getResources().getString(R.string.search_by_plot_code_address).equals(jsonEngWord)){
                                        searchByName.setQueryHint(jsonEngWord);

                                    }


                                } else {
                                    if (getResources().getString(R.string.plot_list).equals(jsonEngWord)) {
                                        txtl_headerView.setText(strConvertedWord);
                                    }else if(getResources().getString(R.string.farmer_name).equals(jsonEngWord)){
                                        txtl_farmername.setText(strConvertedWord);

                                    }else if(getResources().getString(R.string.farmer_code).equals(jsonEngWord)){
                                        txtl_farmercode.setText(strConvertedWord);

                                    }else if(getResources().getString(R.string.farmer_code).equals(jsonEngWord)){
                                        txtl_farmercode.setText(strConvertedWord);

                                    }else if(getResources().getString(R.string.search_by_plot_code_address).equals(jsonEngWord)){
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

                        Toast.makeText(PlotsListActivity.this, "no records found", Toast.LENGTH_LONG).show();
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
