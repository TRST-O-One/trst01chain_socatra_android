package com.socatra.intellitrack.activity.Traderflow;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.DeviceTraderId;
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
import com.socatra.intellitrack.adapters.TraderDealerListAdapter;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.models.get.GetDealersDetailsByTraderId;
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

public class TraderDetailsListAct extends BaseActivity implements View.OnClickListener {

    private static final String TAG = TraderDetailsListAct.class.getCanonicalName();
    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    public AppViewModel viewModel;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    SearchView svFarmer, searchByName;
    LinearLayout imgempty;
    String strTraderId;

    ImageView ImgCancel;


    RecyclerView recycleDealer;

    ArrayList<GetDealersDetailsByTraderId> Dealerlist;

    TraderDealerListAdapter traderDealerListAdapter;

    TextView txtWordHeaderTrd;

    String strLanguageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_detailsby_trader);

        strTraderId = appHelper.getSharedPrefObj().getString(DeviceTraderId, "");
        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");
        initializeUI();
        initializeValues();
        configureDagger();
        configureViewModel();
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (appHelper.isNetworkAvailable()) {
            //getLanguageDataList();
        } else {
            Toast.makeText(TraderDetailsListAct.this, "please check your internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void getLanguageDataList() {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        final ProgressDialog progressDialog = new ProgressDialog(TraderDetailsListAct.this, R.style.AppCompatAlertDialogStyle);
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
                                    if (getResources().getString(R.string.dealer_list).equals(jsonEngWord)) {
                                        txtWordHeaderTrd.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.search_by_dealer_name).equals(jsonEngWord)) {
                                        searchByName.setQueryHint(jsonEngWord1);
                                    }
                                } else {

                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");

                                    if (getResources().getString(R.string.dealer_list).equals(jsonEngWord)) {
                                        txtWordHeaderTrd.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.search_by_dealer_name).equals(jsonEngWord)) {
                                        searchByName.setQueryHint(strConvertedWord);
                                    }


                                }


//                                else if (strLanguageId.equals("3")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//                                    if ("Add".equals(jsonEngWord)) {
//                                        addquality.setText(strConvertedWord);
//                                    } else if ("Quality Check ".equals(jsonEngWord)) {
//                                        txtheaderView.setText(strConvertedWord);
//                                    }
//
//                                } else if (strLanguageId.equals("4")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//                                    if ("Add".equals(jsonEngWord)) {
//                                        addquality.setText(strConvertedWord);
//                                    } else if ("Quality Check ".equals(jsonEngWord)) {
//                                        txtheaderView.setText(strConvertedWord);
//                                    }
//
//
//
//                                }else if (strLanguageId.equals("5")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//                                    if ("Add".equals(jsonEngWord)) {
//                                        addquality.setText(strConvertedWord);
//                                    } else if ("Quality Check ".equals(jsonEngWord)) {
//                                        txtheaderView.setText(strConvertedWord);
//                                    }
//
//
//
//                                }  else{
//                                    String jsonEngWord1 = jsonObjectFarmerPD.getString("SelectedWord");
//                                    if ("Add".equals(jsonEngWord)) {
//                                        addquality.setText(jsonEngWord1);
//                                    } else if ("Quality Check ".equals(jsonEngWord)) {
//                                        txtheaderView.setText(jsonEngWord1);
//                                    }
//
//
//                                }


                            }


                        } catch (Exception ex) {
                            ex.printStackTrace();
                            progressDialog.dismiss();
                            Log.d("Error", ">>>>" + ex.toString());
                        }
                    } else {
                        progressDialog.dismiss();

                        Toast.makeText(TraderDetailsListAct.this, "no records found", Toast.LENGTH_LONG).show();
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
        recycleDealer = findViewById(R.id.recycler_Dealer_trader);
        searchByName = findViewById(R.id.svTraderDealer);
        imgempty = findViewById(R.id.emptyListImage);
        txtWordHeaderTrd = findViewById(R.id.txt_word_title_trd_dealer);
    }

    private void initializeValues() {


        ImgCancel.setOnClickListener(view -> onBackPressed());


        if (searchByName != null) {

            SearchManager searchManager = (SearchManager) TraderDetailsListAct.this.getSystemService(Context.SEARCH_SERVICE);
            searchByName.setSearchableInfo(searchManager
                    .getSearchableInfo(TraderDetailsListAct.this.getComponentName()));
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
                    if (traderDealerListAdapter != null && traderDealerListAdapter.getFilter() != null) {
                        traderDealerListAdapter.getFilter().filter(query);
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    if (traderDealerListAdapter != null && traderDealerListAdapter.getFilter() != null) {
                        traderDealerListAdapter.getFilter().filter(query);
                    }
                    return false;
                }
            });

        }


        Dealerlist = new ArrayList<>();
        recycleDealer.setLayoutManager(new LinearLayoutManager(this));

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
            getDealerList();
        } else {
            Toast.makeText(TraderDetailsListAct.this, "please check your internet connection", Toast.LENGTH_LONG).show();
        }

    }

    private void configureDagger() {
        AndroidInjection.inject(TraderDetailsListAct.this);
    }

    public void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
    }

    private void getDealerList() {

        AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);

        final ProgressDialog progressDialog = new ProgressDialog(TraderDetailsListAct.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading data...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Call<JsonElement> callRetrofit = service.getDealersDetailsByTraderIdFromServer(strTraderId, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));

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
                            ArrayList<GetDealersDetailsByTraderId> getDealerListTableArrayList = new ArrayList<>();


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                GetDealersDetailsByTraderId DealeerList = new GetDealersDetailsByTraderId();
                                DealeerList.setDealerName(jsonObjectFarmerPD.getString("DealerName"));
                                DealeerList.setDealerId((jsonObjectFarmerPD.getString("DealerId")));

                                DealeerList.setSubDealerCount(Integer.valueOf(jsonObjectFarmerPD.getString("SubDealerCount")));
                                DealeerList.setFarmerCount(Integer.valueOf(jsonObjectFarmerPD.getString("FarmerCount")));


                                getDealerListTableArrayList.add(DealeerList);


                            }

                            Dealerlist.addAll(getDealerListTableArrayList);
                            traderDealerListAdapter = new TraderDealerListAdapter(Dealerlist);

                            if (traderDealerListAdapter.getItemCount() == 0) {
                                imgempty.setVisibility(View.VISIBLE);
                                recycleDealer.setVisibility(View.GONE);
                            } else {
                                imgempty.setVisibility(View.GONE);
                                recycleDealer.setVisibility(View.VISIBLE);
                            }
                            recycleDealer.hasFixedSize();
                            recycleDealer.setAdapter(traderDealerListAdapter);
                            traderDealerListAdapter.notifyDataSetChanged();
                        } catch (Exception ex) {
                            progressDialog.dismiss();
                            ex.printStackTrace();
                            Log.d("Error", ">>>>" + ex.toString());
                        }
                    } else {
                        Toast.makeText(TraderDetailsListAct.this, "no records found", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Log.d("Error", ">>>>" + ex.toString());
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
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