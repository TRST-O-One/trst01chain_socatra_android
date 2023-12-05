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
import com.socatra.intellitrack.adapters.SubDealerListAdapter;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.models.get.GetSubDealerByDealerId;
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

public class SubDealerListActivity extends BaseActivity {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    public AppViewModel viewModel;

    SearchView svFarmer,searchByName;

    private static final String TAG = SubDealerListActivity.class.getCanonicalName();

    String Token, strDealerID, strDealerName, strProcessorId,strLanguageId;

    ImageView ImgCancel;

    LinearLayout imgempty;




    RecyclerView recycleSubDealer;

    ArrayList<GetSubDealerByDealerId> SubDealerlist;

    SubDealerListAdapter adapter;

    TextView txt_header_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_dealer_list);

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
        recycleSubDealer = findViewById(R.id.recycler_sub_dealer);
        searchByName = findViewById(R.id.svSubDealer);
        imgempty = findViewById(R.id.emptyListImage);


        txt_header_view = findViewById(R.id.txt_sub_dealer_list_view);

    }

    private void initializeValues() {
        strDealerName = appHelper.getSharedPrefObj().getString(DeviceUserName, "");
        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");
        Token = appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, "");

        ImgCancel.setOnClickListener(view -> onBackPressed());





        if (searchByName != null) {

            SearchManager searchManager = (SearchManager) SubDealerListActivity.this.getSystemService(Context.SEARCH_SERVICE);
            searchByName.setSearchableInfo(searchManager
                    .getSearchableInfo(SubDealerListActivity.this.getComponentName()));
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



        SubDealerlist = new ArrayList<>();
        recycleSubDealer.setLayoutManager(new LinearLayoutManager(this));



        if (appHelper.isNetworkAvailable())
        {
            getSubDealerList();
        }else {
            Toast.makeText(SubDealerListActivity.this,"please check your internet connection",Toast.LENGTH_LONG).show();
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

    private void configureDagger() {
        AndroidInjection.inject(SubDealerListActivity.this);
    }

    public void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
    }


    private void getSubDealerList() {

        AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        final ProgressDialog progressDialog = new ProgressDialog(SubDealerListActivity.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading  data...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Call<JsonElement> callRetrofit = service.getSubDealerDataFromServer(strDealerID,  appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));

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
                            ArrayList<GetSubDealerByDealerId> getSubDealerList = new ArrayList<>();


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                GetSubDealerByDealerId SubDealerList = new GetSubDealerByDealerId();
                                SubDealerList.setId(Integer.valueOf(jsonObjectFarmerPD.getString("Id")));
                                SubDealerList.setSubDealerName(jsonObjectFarmerPD.getString("SubDealerName"));
                                SubDealerList.setSubDealerAddress(jsonObjectFarmerPD.getString("SubDealerAddress"));
                                SubDealerList.setSubDealerContact(jsonObjectFarmerPD.getInt("SubDealerContact"));

                                getSubDealerList.add(SubDealerList);


                            }

                            // Update the adapter with the new data
                            SubDealerlist.addAll(getSubDealerList);
                            adapter = new SubDealerListAdapter(appHelper,viewModel, (List<GetSubDealerByDealerId>) SubDealerlist);
                            if (adapter.getItemCount() == 0) {
                                imgempty.setVisibility(View.VISIBLE);
                                recycleSubDealer.setVisibility(View.GONE);
                            } else {
                                imgempty.setVisibility(View.GONE);
                                recycleSubDealer.setVisibility(View.VISIBLE);
                            }
                            recycleSubDealer.hasFixedSize();
                            recycleSubDealer.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } catch (Exception ex) {
                            progressDialog.dismiss();
                            ex.printStackTrace();
                            Log.d("Error", ">>>>" + ex.toString());
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(SubDealerListActivity.this, "no records found", Toast.LENGTH_LONG).show();
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
        final ProgressDialog progressDialog = new ProgressDialog(SubDealerListActivity.this, R.style.AppCompatAlertDialogStyle);
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
                                    if (getResources().getString(R.string.sub_dealer_list).equals(jsonEngWord)) {
                                        txt_header_view.setText(jsonEngWord);
                                    }else if (getResources().getString(R.string.search_by_sub_dealer_name).equals(jsonEngWord)) {
                                        searchByName.setQueryHint(jsonEngWord);
                                    }


                                } else {
                                    if (getResources().getString(R.string.sub_dealer_list).equals(jsonEngWord)) {
                                        txt_header_view.setText(strConvertedWord);
                                    }else if (getResources().getString(R.string.search_by_sub_dealer_name).equals(jsonEngWord)) {
                                        searchByName.setQueryHint(jsonEngWord);
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

                        Toast.makeText(SubDealerListActivity.this, "no records found", Toast.LENGTH_LONG).show();
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