package com.socatra.intellitrack.activity.customerflow;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.DeviceDealerID;
import static com.socatra.intellitrack.constants.AppConstant.DeviceProcessorId;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserName;
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
import com.socatra.intellitrack.adapters.FarmerInvoiceAdapter;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.models.get.GetFarmerDetailsByInvoiceId;
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

public class FarmerListbyInvoiceActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = FarmerListbyInvoiceActivity.class.getCanonicalName();
    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    public AppViewModel viewModel;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    SearchView svFarmer, searchByName;
    LinearLayout imgempty;
    String Token, strDealerID, strDealerName, strProcessorId;

    ImageView ImgCancel;


    RecyclerView recycleFarmer;
    String strLanguageId;
    ArrayList<GetFarmerDetailsByInvoiceId> Farmerslist;

    FarmerInvoiceAdapter adapter;
    TextView txtWordHeaderTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_listby_invoice_activity);

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
        recycleFarmer = findViewById(R.id.recycler_Invoice_Farmer);
        searchByName = findViewById(R.id.svInvoiceFarmer);
        imgempty = findViewById(R.id.emptyListImage);
        txtWordHeaderTitle = findViewById(R.id.txt_word_head_title);
    }

    private void initializeValues() {
        strDealerName = appHelper.getSharedPrefObj().getString(DeviceUserName, "");
        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");
        Token = appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, "");

        ImgCancel.setOnClickListener(view -> onBackPressed());


        if (searchByName != null) {

            SearchManager searchManager = (SearchManager) FarmerListbyInvoiceActivity.this.getSystemService(Context.SEARCH_SERVICE);
            searchByName.setSearchableInfo(searchManager
                    .getSearchableInfo(FarmerListbyInvoiceActivity.this.getComponentName()));
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


        Farmerslist = new ArrayList<>();
        recycleFarmer.setLayoutManager(new LinearLayoutManager(this));

//        adapter.setOnItemClickListener(new DealerListAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(GetDealerDetailsbyProcessorId item) {
//                Intent dealerProcurementIntent = new Intent(DealerlistActivity.this, DealerProcurementListActivity.class);
//                dealerProcurementIntent.putExtra("DealerId", item.getDealerId());
//                Log.d("DealerId", "DealerId: " + item.getDealerId());
//                startActivity(dealerProcurementIntent);
//            }
//        });


        String InvoiceId = getIntent().getStringExtra("INVNo");
        Log.d("INVNo", "INVNo" + InvoiceId);

        if (appHelper.isNetworkAvailable()) {
            getFarmerList(InvoiceId);
        } else {
            Toast.makeText(FarmerListbyInvoiceActivity.this, "please check your internet connection", Toast.LENGTH_LONG).show();
        }


    }

    private void configureDagger() {
        AndroidInjection.inject(FarmerListbyInvoiceActivity.this);
    }

    public void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
    }

    protected void onResume() {
        super.onResume();

        if (appHelper.isNetworkAvailable()) {
            //getLanguageDataList();
        } else {
            Toast.makeText(getApplicationContext(), "please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getLanguageDataList() {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
//        final ProgressDialog progressDialog = new ProgressDialog(FarmerListByCustomerViewFromBatchActivity.this, R.style.AppCompatAlertDialogStyle);
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
                            //  progressDialog.dismiss();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);

                                String jsonEngWord = jsonObjectFarmerPD.getString("SelectedWord");

                                if (strLanguageId.equals("1")) {
                                    String jsonEngWord1 = jsonObjectFarmerPD.getString("SelectedWord");
                                    if (getResources().getString(R.string.farmer_list).equals(jsonEngWord)) {
                                        txtWordHeaderTitle.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.search_here).equals(jsonEngWord)) {
                                        searchByName.setQueryHint(jsonEngWord1);
                                    }
                                } else {

                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");


                                    if (getResources().getString(R.string.farmer_list).equals(jsonEngWord)) {
                                        txtWordHeaderTitle.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.search_here).equals(jsonEngWord)) {
                                        searchByName.setQueryHint(strConvertedWord);
                                    }
                                }


//                                } else if (strLanguageId.equals("3")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//
//
//                                    if ("Farmers List".equals(jsonEngWord)) {
//                                        txtWordHeaderTitle.setText(strConvertedWord);
//                                    } else if ("Search here".equals(jsonEngWord)) {
//                                        searchByName.setQueryHint(strConvertedWord);
//                                    }
//
//                                } else if (strLanguageId.equals("4")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//
//
//                                    if ("Farmers List".equals(jsonEngWord)) {
//                                        txtWordHeaderTitle.setText(strConvertedWord);
//                                    } else if ("Search here".equals(jsonEngWord)) {
//                                        searchByName.setQueryHint(strConvertedWord);
//                                    }
//
//
//                                } else if (strLanguageId.equals("5")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//
//
//                                    if ("Farmers List".equals(jsonEngWord)) {
//                                        txtWordHeaderTitle.setText(strConvertedWord);
//                                    } else if ("Search here".equals(jsonEngWord)) {
//                                        searchByName.setQueryHint(strConvertedWord);
//                                    }
//
//                                } else {
//
//
//
//                                }


                                }
                            } catch(Exception ex){
                                    ex.printStackTrace();
                                    //  progressDialog.dismiss();
                                    Log.d("Error", ">>>>" + ex.toString());
                                }
                            } else{
                                // progressDialog.dismiss();

                                Toast.makeText(FarmerListbyInvoiceActivity.this, "no records found", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            // progressDialog.dismiss();

                            Log.d("Error", ">>>>" + ex.toString());
                        }
                    }

                    @Override
                    public void onFailure (Call < JsonElement > call, Throwable t){
                        // progressDialog.dismiss();

                        Log.d("Error Call", ">>>>" + call.toString());
                        Log.d("Error", ">>>>" + t.toString());
                    }
                });
            }

            private void getFarmerList(String InvoiceId) {

                AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);

                final ProgressDialog progressDialog = new ProgressDialog(FarmerListbyInvoiceActivity.this, R.style.AppCompatAlertDialogStyle);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("loading  data...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();


                Call<JsonElement> callRetrofit = service.getFarmerDetailsByInvoiceIdFromServer(InvoiceId, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));

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
                                    ArrayList<GetFarmerDetailsByInvoiceId> getFarmerListTableArrayList = new ArrayList<>();


                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                        GetFarmerDetailsByInvoiceId FarmerList = new GetFarmerDetailsByInvoiceId();
                                        FarmerList.setFarmerCode(jsonObjectFarmerPD.getString("FarmerCode"));
                                        FarmerList.setFarmerName(jsonObjectFarmerPD.getString("FarmerName"));


                                        getFarmerListTableArrayList.add(FarmerList);


                                    }

                                    // Update the adapter with the new data
                                    Farmerslist.addAll(getFarmerListTableArrayList);
                                    adapter = new FarmerInvoiceAdapter(Farmerslist);

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
                                } catch (Exception ex) {
                                    progressDialog.dismiss();

                                    ex.printStackTrace();
                                    Log.d("Error", ">>>>" + ex.toString());
                                }
                            } else {
                                progressDialog.dismiss();

                                Toast.makeText(FarmerListbyInvoiceActivity.this, "no records found", Toast.LENGTH_LONG).show();
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