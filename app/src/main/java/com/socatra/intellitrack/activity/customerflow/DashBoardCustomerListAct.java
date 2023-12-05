package com.socatra.intellitrack.activity.customerflow;

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
import com.socatra.intellitrack.adapters.CustomerListAdapter;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.models.get.GetCustomersbyProcessorId;
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

public class DashBoardCustomerListAct extends BaseActivity implements View.OnClickListener {

    private static final String TAG = DashBoardCustomerListAct.class.getCanonicalName();
    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    public AppViewModel viewModel;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    SearchView svFarmer, searchByName;
    LinearLayout imgempty;
    String Token, strDealerID, strDealerName, strProcessorId;

    ImageView ImgCancel;


    RecyclerView recycleCustomer;

    ArrayList<GetCustomersbyProcessorId> Customerlist;

    CustomerListAdapter adapter;

    TextView txtWordTitle;
    String strLanguageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

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
        recycleCustomer = findViewById(R.id.recycler_Customer);
        searchByName = findViewById(R.id.svCustomer);
        imgempty = findViewById(R.id.emptyListImage);


        //language
        txtWordTitle = findViewById(R.id.txt_word_cust_list);
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

    private void getLanguageDataList() {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
//        final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext(), R.style.AppCompatAlertDialogStyle);
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("loading data...");
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.show();

        callRetrofit = service.getTransalteLanguageWordsByLanguageIdFromServer(strLanguageId, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));

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
                            //    progressDialog.dismiss();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);

                                String jsonEngWord = jsonObjectFarmerPD.getString("SelectedWord");

                                if (strLanguageId.equals("1")) {
                                    String jsonEngWord1 = jsonObjectFarmerPD.getString("SelectedWord");

                                    if (getResources().getString(R.string.customer_list).equals(jsonEngWord)) {
                                        txtWordTitle.setText(jsonEngWord1);
                                    }


                                } else {

                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");


                                    if (getResources().getString(R.string.customer_list).equals(jsonEngWord)) {
                                        txtWordTitle.setText(strConvertedWord);
                                    }


                                }
//                                        else if (strLanguageId.equals("3")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//
//                                    if ("Customer List".equals(jsonEngWord)) {
//                                        txtWordTitle.setText(strConvertedWord);
//                                    }
//
//
//                                } else if (strLanguageId.equals("4")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//
//
//                                    if ("Customer List".equals(jsonEngWord)) {
//                                        txtWordTitle.setText(strConvertedWord);
//                                    }
//
//
//                                } else if (strLanguageId.equals("5")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//
//
//                                    if ("Customer List".equals(jsonEngWord)) {
//                                        txtWordTitle.setText(strConvertedWord);
//                                    }
//
//
//                                } else {


                            }


                        } catch (Exception ex) {
                            ex.printStackTrace();
                            // progressDialog.dismiss();
                            Log.d("Error", ">>>>" + ex.toString());
                        }
                    } else {
                        //progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(), "no records found", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    //progressDialog.dismiss();

                    Log.d("Error", ">>>>" + ex.toString());
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                // progressDialog.dismiss();

                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
            }
        });
    }

    private void initializeValues() {
        strDealerName = appHelper.getSharedPrefObj().getString(DeviceUserName, "");
        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");
        Token = appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, "");
        ImgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //    ImgCancel.setOnClickListener(view -> onBackPressed());


        if (searchByName != null) {
            SearchManager searchManager = (SearchManager) DashBoardCustomerListAct.this.getSystemService(Context.SEARCH_SERVICE);
            searchByName.setSearchableInfo(searchManager
                    .getSearchableInfo(DashBoardCustomerListAct.this.getComponentName()));
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


        Customerlist = new ArrayList<>();
        recycleCustomer.setLayoutManager(new LinearLayoutManager(this));

//        adapter.setOnItemClickListener(new DealerListAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(GetDealerDetailsbyProcessorId item) {
//                Intent dealerProcurementIntent = new Intent(DealerlistActivity.this, DealerProcurementListActivity.class);
//                dealerProcurementIntent.putExtra("DealerId", item.getDealerId());
//                Log.d("DealerId", "DealerId: " + item.getDealerId());
//                startActivity(dealerProcurementIntent);
//            }
//        });


        if (appHelper.isNetworkAvailable()) {
            getCustomerListbyProcessorId();
        } else {
            Toast.makeText(DashBoardCustomerListAct.this, "please check your internet connection", Toast.LENGTH_LONG).show();
        }


    }

    private void configureDagger() {
        AndroidInjection.inject(DashBoardCustomerListAct.this);
    }

    public void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
    }

    private void getCustomerListbyProcessorId() {
        AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        final ProgressDialog progressDialog = new ProgressDialog(DashBoardCustomerListAct.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading  data...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        Call<JsonElement> callRetrofit = service.getCustomerDetailsbyIdFromServerByProcessorId(strProcessorId, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));
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
                            ArrayList<GetCustomersbyProcessorId> getCustomerListTableArrayList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                GetCustomersbyProcessorId CustomerList = new GetCustomersbyProcessorId();
                                CustomerList.setId(Integer.valueOf(jsonObjectFarmerPD.getString("Id")));
                                CustomerList.setEntityName(jsonObjectFarmerPD.getString("EntityName"));
                                CustomerList.setAddress(jsonObjectFarmerPD.getString("Address"));
                                CustomerList.setEmail(jsonObjectFarmerPD.getString("Email"));
                                CustomerList.setPhone(String.valueOf(jsonObjectFarmerPD.getInt("Phone")));
                                CustomerList.setVillageName((jsonObjectFarmerPD.getString("VillageName")));
                                CustomerList.setSubDistrictname(jsonObjectFarmerPD.getString("SubDistrictname"));
                                CustomerList.setDistrictorRegencyName(jsonObjectFarmerPD.getString("DistrictorRegencyName"));
                                CustomerList.setStateorProvinceName((jsonObjectFarmerPD.getString("StateorProvinceName")));
                                CustomerList.setCountryName((jsonObjectFarmerPD.getString("CountryName")));
                                getCustomerListTableArrayList.add(CustomerList);

                            }

                            // Update the adapter with the new data
                            Customerlist.addAll(getCustomerListTableArrayList);
                            adapter = new CustomerListAdapter(appHelper, DashBoardCustomerListAct.this, Customerlist);
                            if (adapter.getItemCount() == 0) {
                                imgempty.setVisibility(View.VISIBLE);
                                recycleCustomer.setVisibility(View.GONE);
                            } else {
                                imgempty.setVisibility(View.GONE);
                                recycleCustomer.setVisibility(View.VISIBLE);
                            }
                            recycleCustomer.hasFixedSize();
                            recycleCustomer.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } catch (Exception ex) {

                            progressDialog.dismiss();
                            ex.printStackTrace();
                            Log.d("Error", ">>>>" + ex.toString());
                        }
                    } else {

                        progressDialog.dismiss();
                        Toast.makeText(DashBoardCustomerListAct.this, "no records found", Toast.LENGTH_LONG).show();
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