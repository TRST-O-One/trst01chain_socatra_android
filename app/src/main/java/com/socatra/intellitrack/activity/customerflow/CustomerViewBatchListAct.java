package com.socatra.intellitrack.activity.customerflow;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.DeviceCustomerId;
import static com.socatra.intellitrack.constants.AppConstant.DeviceDealerID;
import static com.socatra.intellitrack.constants.AppConstant.DeviceProcessorId;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserName;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserRole;
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
import com.socatra.intellitrack.adapters.BatchInvoiceListAdapter;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.models.get.GetBatchDetailsByInvoiceNumber;
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

public class CustomerViewBatchListAct extends BaseActivity {

    private static final String TAG = CustomerViewBatchListAct.class.getCanonicalName();
    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    public AppViewModel viewModel;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    SearchView searchByName;
    LinearLayout imgempty;
    String deviceRoleName;
    String Token, strDealerID, strDealerName, strProcessorId, strCustomerId;

    Button addgrnbtn;

    ImageView ImgCancel;


    RecyclerView recycleNavInvoice;


    ArrayList<GetBatchDetailsByInvoiceNumber> Batchlist;

    BatchInvoiceListAdapter adapter;
    String strLanguageId;
    TextView txtBatchTitleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_invoice_list);

        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");
        strProcessorId = appHelper.getSharedPrefObj().getString(DeviceProcessorId, "");
        strCustomerId = appHelper.getSharedPrefObj().getString(DeviceCustomerId, "");
        Log.d(TAG, "onCreate: csss" + strCustomerId);
        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");
        Log.d("strCustomerId", "CustomerId" + strCustomerId);

        deviceRoleName = appHelper.getSharedPrefObj().getString(DeviceUserRole, "");


        initializeUI();
        initializeValues();
        configureDagger();
        configureViewModel();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CustomerViewBatchListAct.this, CustomerDashboard.class); // Replace MainActivity with your actual dashboard activity
        startActivity(intent);
        finish();
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

    private void getLanguageDataList() {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        final ProgressDialog progressDialog = new ProgressDialog(CustomerViewBatchListAct.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading data...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

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
                            progressDialog.dismiss();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);

                                String jsonEngWord = jsonObjectFarmerPD.getString("SelectedWord");

                                if (strLanguageId.equals("1")) {
                                    String jsonEngWord1 = jsonObjectFarmerPD.getString("SelectedWord");

                                    if (getResources().getString(R.string.batch_list).equals(jsonEngWord)) {
                                        txtBatchTitleList.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.search_here).equals(jsonEngWord)) {
                                        searchByName.setQueryHint(jsonEngWord1);
                                    }
                                } else {

                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");

                                    //Batch List int the place of Batches
                                    if (getResources().getString(R.string.batch_list).equals(jsonEngWord)) {
                                        txtBatchTitleList.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.search_here).equals(jsonEngWord)) {
                                        searchByName.setQueryHint(strConvertedWord);
                                    }

//                                } else if (strLanguageId.equals("3")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//
//
//                                    if ("Batches".equals(jsonEngWord)) {
//                                        txtBatchTitleList.setText(strConvertedWord);
//                                    } else if ("Search here".equals(jsonEngWord)) {
//                                        searchByName.setQueryHint(strConvertedWord);
//                                    }
//                                } else if (strLanguageId.equals("4")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//
//
//                                    if ("Batches".equals(jsonEngWord)) {
//                                        txtBatchTitleList.setText(strConvertedWord);
//                                    } else if ("Search here".equals(jsonEngWord)) {
//                                        searchByName.setQueryHint(strConvertedWord);
//                                    }
//
//
//                                } else if (strLanguageId.equals("5")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//
//
//                                    if ("Batches".equals(jsonEngWord)) {
//                                        txtBatchTitleList.setText(strConvertedWord);
//                                    } else if ("Search here".equals(jsonEngWord)) {
//                                        searchByName.setQueryHint(strConvertedWord);
//                                    }
//
//
//                                } else {
//                                    String jsonEngWord1 = jsonObjectFarmerPD.getString("SelectedWord");
//
//
//                                    if ("Batches".equals(jsonEngWord)) {
//                                        txtBatchTitleList.setText(jsonEngWord1);
//                                    } else if ("Search here".equals(jsonEngWord)) {
//                                        searchByName.setQueryHint(jsonEngWord1);
//                                    }
//
//                                }


                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            progressDialog.dismiss();
                            Log.d("Error", ">>>>" + ex.toString());
                        }
                    } else {
                        progressDialog.dismiss();

                        Toast.makeText(CustomerViewBatchListAct.this, "no records found", Toast.LENGTH_LONG).show();
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


    private void initializeUI() {

        ImgCancel = findViewById(R.id.img_cancel);
        recycleNavInvoice = findViewById(R.id.recycler_Batch_Invoice);
        searchByName = findViewById(R.id.svBatchInvoice);

        imgempty = findViewById(R.id.emptyListImage);
        txtBatchTitleList = findViewById(R.id.txt_title_batch_list);

        //  searchByName.setQueryHint();
    }

    private void initializeValues() {

        strDealerName = appHelper.getSharedPrefObj().getString(DeviceUserName, "");
        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");

        Token = appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, "");

        ImgCancel.setOnClickListener(view -> {
            Intent intent = new Intent(CustomerViewBatchListAct.this, CustomerDashboard.class); // Replace MainActivity with your actual dashboard activity
            startActivity(intent);
            finish();
        });


        if (searchByName != null) {

            SearchManager searchManager = (SearchManager) CustomerViewBatchListAct.this.getSystemService(Context.SEARCH_SERVICE);
            searchByName.setSearchableInfo(searchManager
                    .getSearchableInfo(CustomerViewBatchListAct.this.getComponentName()));
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


        Batchlist = new ArrayList<>();
        recycleNavInvoice.setLayoutManager(new LinearLayoutManager(this));


        String InvoiceId = getIntent().getStringExtra("INVNo");
        Log.d("INVNo", "INVNo" + InvoiceId);
        if (appHelper.isNetworkAvailable()) {
            getBatchListByInvoiceId(InvoiceId);
        } else {
            Toast.makeText(CustomerViewBatchListAct.this, "please check your internet connection", Toast.LENGTH_LONG).show();
        }
    }

    private void configureDagger() {
        AndroidInjection.inject(CustomerViewBatchListAct.this);
    }

    public void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
    }

    private void getBatchListByInvoiceId(String invoiceId) {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        final ProgressDialog progressDialog = new ProgressDialog(CustomerViewBatchListAct.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading  data...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getBatchDetailsByInvoiceIdFromServer(
                invoiceId,
                appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, "")
        );
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
                            ArrayList<GetBatchDetailsByInvoiceNumber> BatchInvoiceArraylist = new ArrayList<>();
                            Batchlist.clear();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                GetBatchDetailsByInvoiceNumber BatchInvoicelist = new GetBatchDetailsByInvoiceNumber();
                                BatchInvoicelist.setId(Integer.valueOf(jsonObjectFarmerPD.getString("Id")));
                                BatchInvoicelist.setBatchId(Integer.valueOf(jsonObjectFarmerPD.getString("BatchId")));

                                BatchInvoicelist.setBatchNo(jsonObjectFarmerPD.getString("BatchNo"));
                                BatchInvoicelist.setQuantity(Integer.valueOf(jsonObjectFarmerPD.getString("Quantity")));
                                BatchInvoicelist.setCreatedDate(jsonObjectFarmerPD.getString("CreatedDate"));


                                BatchInvoiceArraylist.add(BatchInvoicelist);
                            }

                            Batchlist.addAll(BatchInvoiceArraylist);

                            adapter = new BatchInvoiceListAdapter(appHelper, viewModel, (List<GetBatchDetailsByInvoiceNumber>) Batchlist, strDealerID);
                            recycleNavInvoice.hasFixedSize();
                            recycleNavInvoice.setAdapter(adapter);

                            if (adapter.getItemCount() == 0) {
                                imgempty.setVisibility(View.VISIBLE);
                                recycleNavInvoice.setVisibility(View.GONE);
                            } else {
                                imgempty.setVisibility(View.GONE);
                                recycleNavInvoice.setVisibility(View.VISIBLE);
                            }

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
                        Toast.makeText(CustomerViewBatchListAct.this, "no records found", Toast.LENGTH_LONG).show();
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


}
