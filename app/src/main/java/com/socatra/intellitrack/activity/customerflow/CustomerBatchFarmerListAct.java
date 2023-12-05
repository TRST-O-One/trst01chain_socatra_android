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
import dagger.android.DispatchingAndroidInjector;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerBatchFarmerListAct extends BaseActivity implements FarmerByDealerIdAdapter.OnItemClickListener {

    private static final String TAG = CustomerBatchFarmerListAct.class.getCanonicalName();
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    public AppViewModel viewModel;
    String mTag = "FarmerActivityTag";
    @Inject
    DispatchingAndroidInjector<androidx.fragment.app.Fragment> dispatchingAndroidInjector;
    SearchView svFarmer, searchByName;
    String Token, strDealerID, strDealerName, strProcessorId;

    ImageView ImgCancel;
    LinearLayout imgempty;


    RecyclerView recycleFarmer;

    ArrayList<GetFarmerbyDealerid> farmerList;

    FarmerByDealerIdAdapter adapter;
    String strBatchID, strInvId;

    String strLanguageId;
    TextView txtWordHeaderTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_farmer_list);
        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");
        strProcessorId = appHelper.getSharedPrefObj().getString(DeviceProcessorId, "");
        strBatchID = getIntent().getStringExtra("BatchId");
        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");
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
        txtWordHeaderTitle = findViewById(R.id.txt_word_head_title);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(FarmerListByCustomerViewFromBatchActivity.this, BatchListByCustomerViewActivity.class); // Replace MainActivity with your actual dashboard activity
//        startActivity(intent);
        finish();
    }

    protected void onResume() {
        super.onResume();

        if (appHelper.isNetworkAvailable()) {
          //  getLanguageDataList();
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
                                } else  {

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


                        } catch (Exception ex) {
                            ex.printStackTrace();
                            //  progressDialog.dismiss();
                            Log.d("Error", ">>>>" + ex.toString());
                        }
                    } else {
                        // progressDialog.dismiss();

                        Toast.makeText(CustomerBatchFarmerListAct.this, "no records found", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    // progressDialog.dismiss();

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
                onBackPressed();
//                Intent intent = new Intent(FarmerListByCustomerViewFromBatchActivity.this, BatchListByCustomerViewActivity.class); // Replace MainActivity with your actual dashboard activity
//                intent.putExtra("INVNo",)
//                startActivity(intent);
                finish();
            }
        });
        //   ImgCancel.setOnClickListener(view -> onBackPressed());


        if (searchByName != null) {

//            SearchManager searchManager = (SearchManager) getApplicationContext().getSystemService(Context.SEARCH_SERVICE);
//            searchByName.setSearchableInfo(searchManager
//                    .getSearchableInfo(getComponentName()));
//            searchByName.setMaxWidth(Integer.MAX_VALUE);
            // listening to search query text change

            SearchManager searchManager = (SearchManager) CustomerBatchFarmerListAct.this.getSystemService(Context.SEARCH_SERVICE);
            searchByName.setSearchableInfo(searchManager
                    .getSearchableInfo(CustomerBatchFarmerListAct.this.getComponentName()));
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


        if (appHelper.isNetworkAvailable()) {
            getFarmerList();
        } else {
            Toast.makeText(CustomerBatchFarmerListAct.this, "please check your internet connection", Toast.LENGTH_LONG).show();
        }

    }

    private void configureDagger() {
        AndroidInjection.inject(CustomerBatchFarmerListAct.this);
    }

    public void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
    }

    private void getFarmerList() {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        final ProgressDialog progressDialog = new ProgressDialog(CustomerBatchFarmerListAct.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading  data...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        Call<JsonElement> callRetrofit = null;

        if (strDealerID.equals("")) {
            callRetrofit = service.getFarmerListbyProcessorIdFromserverFromBatch(
                    strBatchID,
                    appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, "")
            );
        } else {
            callRetrofit = service.getFarmerListbyDealerIdFromserver(
                    strDealerID,
                    appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, "")
            );
        }

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    progressDialog.dismiss();


                    String strResponse = String.valueOf(response.body());
                    Log.d(TAG, "onResponse: >>>" + strResponse);
                    JSONObject jsonObject = new JSONObject(strResponse);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        try {
                            ArrayList<GetFarmerbyDealerid> getFarmerListTableArrayList = new ArrayList<>();
                            farmerList.clear();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                GetFarmerbyDealerid farmerlist = new GetFarmerbyDealerid();
                                farmerlist.setId(Integer.valueOf(jsonObjectFarmerPD.getString("FarmerId")));
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
                            adapter = new FarmerByDealerIdAdapter(appHelper, viewModel, (List<GetFarmerbyDealerid>) farmerList, "map");
                            if (adapter.getItemCount() == 0) {
                                imgempty.setVisibility(View.VISIBLE);
                                recycleFarmer.setVisibility(View.GONE);
                                Toast.makeText(CustomerBatchFarmerListAct.this, "no records found", Toast.LENGTH_LONG).show();

                            } else {
                                imgempty.setVisibility(View.GONE);
                                recycleFarmer.setVisibility(View.VISIBLE);
                            }
//
//                            if (farmerList.size() >0 )
//                            {
//                                imgempty.setVisibility(View.GONE);
//                                recycleFarmer.setVisibility(View.VISIBLE);
//
//                            }else {
//                                imgempty.setVisibility(View.VISIBLE);
//                                recycleFarmer.setVisibility(View.GONE);
//                            }

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

                        Toast.makeText(CustomerBatchFarmerListAct.this, "no records found", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    progressDialog.dismiss();

                    ex.printStackTrace();
                    Toast.makeText(CustomerBatchFarmerListAct.this, "no records found", Toast.LENGTH_LONG).show();

                    Log.d("Error", ">>>>" + ex.toString());
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();

                Toast.makeText(CustomerBatchFarmerListAct.this, "no records found", Toast.LENGTH_LONG).show();

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