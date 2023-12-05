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
import com.socatra.intellitrack.adapters.DealerListAdapter;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.models.get.GetDealerDetailsbyProcessorId;
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

public class DashBoardDealerListActivity extends BaseActivity implements View.OnClickListener {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    public AppViewModel viewModel;

    SearchView svFarmer,searchByName;

    private static final String TAG = DashBoardDealerListActivity.class.getCanonicalName();

    String Token, strDealerID, strLanguageId,strDealerName, strProcessorId;

    ImageView ImgCancel;

    RecyclerView recycleDealer;

    LinearLayout imgempty;

    ArrayList<GetDealerDetailsbyProcessorId> Dealerlist;

    DealerListAdapter adapter;


    TextView txt_l_dealerlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealerlist);

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
        recycleDealer = findViewById(R.id.recycler_dealer);
        searchByName = findViewById(R.id.svDealer);
        imgempty = findViewById(R.id.emptyListImage);
        //language
        txt_l_dealerlist = findViewById(R.id.txt_dealer_title);
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
            //getLanguageDataList();
        } else {
            Toast.makeText(getApplicationContext(), "please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeValues() {
        strDealerName = appHelper.getSharedPrefObj().getString(DeviceUserName, "");
        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");
        Token = appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, "");

        ImgCancel.setOnClickListener(view -> onBackPressed());








        if (searchByName != null) {

            SearchManager searchManager = (SearchManager) DashBoardDealerListActivity.this.getSystemService(Context.SEARCH_SERVICE);
            searchByName.setSearchableInfo(searchManager
                    .getSearchableInfo(DashBoardDealerListActivity.this.getComponentName()));
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



        Dealerlist = new ArrayList<>();
        recycleDealer.setLayoutManager(new LinearLayoutManager(this));

        if (appHelper.isNetworkAvailable())
        {

            getDealerList();
        }else {
            Toast.makeText(this,"please check your internet connection",Toast.LENGTH_SHORT).show();
        }


    }

    private void configureDagger() {
        AndroidInjection.inject(DashBoardDealerListActivity.this);
    }

    public void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
    }

    private void getDealerList() {

        AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        final ProgressDialog progressDialog = new ProgressDialog(DashBoardDealerListActivity.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading  data...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Call<JsonElement> callRetrofit = service.getDealerDetailsByProcessorIdFromServer(strProcessorId,  appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    String strResponse = String.valueOf(response.body());
                    Log.d(TAG, "onResponse: >>>" + strResponse);
                    JSONObject jsonObject = new JSONObject(strResponse);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        progressDialog.dismiss();
                        try {
                            ArrayList<GetDealerDetailsbyProcessorId> getDealerListTableArrayList = new ArrayList<>();


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                GetDealerDetailsbyProcessorId DealerList = new GetDealerDetailsbyProcessorId();
                                DealerList.setId(Integer.valueOf(jsonObjectFarmerPD.getString("Id")));
                                DealerList.setDealerName(jsonObjectFarmerPD.getString("DealerName"));
                                DealerList.setAddress(jsonObjectFarmerPD.getString("Address"));
                                DealerList.setPrimaryContactNo(jsonObjectFarmerPD.getInt("PrimaryContactNo"));

                                getDealerListTableArrayList.add(DealerList);


                            }

                            // Update the adapter with the new data
                            Dealerlist.addAll(getDealerListTableArrayList);

                            adapter = new DealerListAdapter(DashBoardDealerListActivity.this,appHelper,viewModel, (List<GetDealerDetailsbyProcessorId>) Dealerlist);
                            recycleDealer.hasFixedSize();
                            recycleDealer.setAdapter(adapter);
                            if (adapter.getItemCount() == 0) {
                                imgempty.setVisibility(View.VISIBLE);
                                recycleDealer.setVisibility(View.GONE);
                            } else {
                                imgempty.setVisibility(View.GONE);
                                recycleDealer.setVisibility(View.VISIBLE);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Log.d("Error", ">>>>" + ex.toString());
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(DashBoardDealerListActivity.this, "no records found", Toast.LENGTH_LONG).show();
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


    private void getLanguageDataList() {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        final ProgressDialog progressDialog = new ProgressDialog(DashBoardDealerListActivity.this, R.style.AppCompatAlertDialogStyle);
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
                    Log.d(TAG, "onResponse: >>> dealer" + strResponse);
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
                                    if (getResources().getString(R.string.dealers_list).equals(jsonEngWord)) {
                                        txt_l_dealerlist.setText(jsonEngWord);
                                    }


                                } else {
                                    if (getResources().getString(R.string.dealers_list).equals(jsonEngWord)) {
                                        txt_l_dealerlist.setText(strConvertedWord);
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

                        Toast.makeText(DashBoardDealerListActivity.this, "no records found", Toast.LENGTH_LONG).show();
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