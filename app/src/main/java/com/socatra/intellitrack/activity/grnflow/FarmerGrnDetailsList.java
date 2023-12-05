package com.socatra.intellitrack.activity.grnflow;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.LanguageId;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.socatra.intellitrack.BaseActivity;
import com.socatra.intellitrack.R;
import com.socatra.intellitrack.activity.main_dash_board.FarmerDetailsListActivity;
import com.socatra.intellitrack.adapters.GrnByFarmercodeAdapter;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.models.get.GrnByFarmercode;
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

public class FarmerGrnDetailsList extends BaseActivity {
    private static final String TAG = FarmerGrnDetailsList.class.getCanonicalName();
    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    public AppViewModel viewModel;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    TextView FarmerCode, FarmerName , villageName;

    SearchView svFarmer, searchByName;

    RecyclerView recyclegrn;

    String strLanguageId;

    ImageView imgCancel;

    LinearLayout imgempty;


    TextView txtfarmercode,txtfarmername,txtvillagename,txtGrnlist;


    Button addgrnbtn;
    ArrayList<GrnByFarmercode> grnList;
    GrnByFarmercodeAdapter grnByFarmercodeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grn_details_list);


        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");

        FarmerCode = findViewById(R.id.txtFarmerCode);
        FarmerName = findViewById(R.id.txtFarmerName);
        villageName = findViewById(R.id.txtVillageName);

        String farmerCode = getIntent().getStringExtra("FarmerCode");
        String farmerName = getIntent().getStringExtra("FarmerName");
        String VillageName = getIntent().getStringExtra("VillageName");

        FarmerCode.setText(farmerCode);
        FarmerName.setText(farmerName);
        villageName.setText(VillageName);

        Log.d("Nandini2", "Farmer Code: " + farmerCode);
        initializeUI();
        initializeValues();
        configureDagger();
        configureViewModel();

        if (appHelper.isNetworkAvailable()) {
            getGrnDataByFarmerCode(farmerCode);
        } else {
            Toast.makeText(FarmerGrnDetailsList.this, "please check your internet connection", Toast.LENGTH_SHORT).show();
        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FarmerGrnDetailsList.this, FarmerDetailsListActivity.class);
        startActivity(intent);
        finish();
    }

    private void initializeUI() {
        recyclegrn = findViewById(R.id.recycler_grn);
        searchByName = findViewById(R.id.svGRN);
        imgCancel = findViewById(R.id.img_cancel);
        imgempty = findViewById(R.id.emptyListImage);


        //language
        addgrnbtn = findViewById(R.id.add_grn_btn1);
        txtGrnlist= findViewById(R.id.txt_grn_list_View);
        txtfarmercode = findViewById(R.id.txt_farmer_code_view);
        txtfarmername = findViewById(R.id.txt_farmer_name_view);
        txtvillagename = findViewById(R.id.txt_village_name_view);



    }

    private void initializeValues() {

        grnList = new ArrayList<>();




        addgrnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String farmerName = getIntent().getStringExtra("FarmerName");
                String farmerCode = getIntent().getStringExtra("FarmerCode");
                Intent intent = new Intent(FarmerGrnDetailsList.this, AddGRNbyFarmer.class);
                intent.putExtra("FarmerName", farmerName);
                intent.putExtra("FarmerCode", farmerCode);
                startActivity(intent);
            }
        });

        // Initialize your RecyclerView and Adapter here
        recyclegrn = findViewById(R.id.recycler_grn);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclegrn.setLayoutManager(layoutManager);
        grnList = new ArrayList<>(); // Initialize the list


        imgCancel.setOnClickListener(v -> {
            // finish();
            onBackPressed();
        });


        SearchManager searchManager = (SearchManager) FarmerGrnDetailsList.this.getSystemService(Context.SEARCH_SERVICE);
        searchByName.setSearchableInfo(searchManager
                .getSearchableInfo(FarmerGrnDetailsList.this.getComponentName()));
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
                if (grnByFarmercodeAdapter != null && grnByFarmercodeAdapter.getFilter() != null) {
                    grnByFarmercodeAdapter.getFilter().filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (grnByFarmercodeAdapter != null && grnByFarmercodeAdapter.getFilter() != null) {
                    grnByFarmercodeAdapter.getFilter().filter(query);
                }
                return false;
            }
        });


    }


    private void configureDagger() {
        AndroidInjection.inject(FarmerGrnDetailsList.this);
    }

    public void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
    }


    // Method to fetch GRN data by Farmer code from the server
    private void getGrnDataByFarmerCode(String farmerCode) {

        AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        final ProgressDialog progressDialog = new ProgressDialog(FarmerGrnDetailsList.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading  data...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        String authToken = AUTHORIZATION_TOKEN_KEY;// Replace with your authentication token

        Call<JsonElement> callRetrofit = service.getGrnDataByFarmerCodeFromServer(farmerCode, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));

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
                            ArrayList<GrnByFarmercode> getGrnListTableArrayList = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                GrnByFarmercode GrnList = new GrnByFarmercode();
                                GrnList.setId(Integer.valueOf(jsonObjectFarmerPD.getString("Id")));
                                GrnList.setFarmerCode(jsonObjectFarmerPD.getString("FarmerCode"));
                                GrnList.setFarmerName(jsonObjectFarmerPD.getString("FarmerName"));
                                GrnList.setCurrentQty(jsonObjectFarmerPD.getInt("CurrentQty"));
                                GrnList.setQuantity(jsonObjectFarmerPD.getInt("Quantity"));
                                GrnList.setGRNdate(jsonObjectFarmerPD.getString("GRNdate"));
                                GrnList.setGRNnumber(jsonObjectFarmerPD.getString("GRNnumber"));
                                GrnList.setPrimaryContactNo(jsonObjectFarmerPD.getString("PrimaryContactNo"));
                                GrnList.setGRNDocument(jsonObjectFarmerPD.getString("GRNDocument"));
                                getGrnListTableArrayList.add(GrnList);
                            }

                            // Update the grnByFarmercodeAdapter with the new data
                            grnList.clear();
                            grnList.addAll(getGrnListTableArrayList);
                            grnByFarmercodeAdapter = new GrnByFarmercodeAdapter(appHelper,grnList);

                            if (grnByFarmercodeAdapter.getItemCount() == 0) {
                                imgempty.setVisibility(View.VISIBLE);
                                recyclegrn.setVisibility(View.GONE);
                            } else {
                                imgempty.setVisibility(View.GONE);
                                recyclegrn.setVisibility(View.VISIBLE);
                            }

                            recyclegrn.setAdapter(grnByFarmercodeAdapter);
                            grnByFarmercodeAdapter.notifyDataSetChanged();
                        } else {
                            String message = jsonObject.optString("message", ""); // Get the "message" field from the JSON response
                            if ("no GRN found".equals(message)) {
                                // The "message" field is "no GRN found," so display the toast
                                Toast.makeText(FarmerGrnDetailsList.this, "No GRN found", Toast.LENGTH_LONG).show();
                                Log.d("Chekki", "No GRN found");
                            } else {
                                // Handle other cases when "message" is not "no GRN found"
                                // You can log an error message or display an appropriate toast/message here.
                            }
                        }
                    } else {
                        // Handle the case when the response is not successful (e.g., HTTP error)
                        // You can log an error message or display an appropriate toast/message here.
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
        final ProgressDialog progressDialog = new ProgressDialog(FarmerGrnDetailsList.this, R.style.AppCompatAlertDialogStyle);
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


                                    if ("Add GRN".equals(jsonEngWord)) {
                                        addgrnbtn.setText(jsonEngWord);
                                    } else if ("GRN List".equals(jsonEngWord)) {
                                        txtGrnlist.setText(jsonEngWord);
                                    } else if ("FarmerCode".equals(jsonEngWord)) {
                                        txtfarmercode.setText(jsonEngWord);
                                    } else if ("Farmer Name".equals(jsonEngWord)) {
                                        txtfarmername.setText(jsonEngWord);
                                    } else if ("Village".equals(jsonEngWord)) {
                                        txtvillagename.setText(jsonEngWord);
                                    }


                                } else {
                                    if ("Add GRN".equals(jsonEngWord)) {
                                        addgrnbtn.setText(strConvertedWord);
                                    } else if ("GRN List".equals(jsonEngWord)) {
                                        txtGrnlist.setText(strConvertedWord);
                                    } else if ("FarmerCode".equals(jsonEngWord)) {
                                        txtfarmercode.setText(strConvertedWord);
                                    } else if ("Farmer Name".equals(jsonEngWord)) {
                                        txtfarmername.setText(strConvertedWord);
                                    } else if ("Village".equals(jsonEngWord)) {
                                        txtvillagename.setText(strConvertedWord);
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

                        Toast.makeText(FarmerGrnDetailsList.this, "no records found", Toast.LENGTH_LONG).show();
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
