package com.socatra.intellitrack.activity.drc;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.DeviceProcessorId;
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
import com.socatra.intellitrack.activity.main_dash_board.MainDashBoardActivity;
import com.socatra.intellitrack.adapters.DrclistAdapter;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.models.get.GetDrcByProcessor;
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

public class Drclist extends BaseActivity {

    private static final String TAG = Drclist.class.getCanonicalName();
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    public AppViewModel viewModel;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    //    String ProcessorId = "1"; // Updated ProcessorId to 1
    String strProcessorId, strLanguageId;
    RecyclerView recyclegrn;
    ImageView imgCancel;

    LinearLayout imgempty;
    Button addDrcbtn;

    TextView txt_headerview;

    SearchView svFarmer, searchByName;


    ArrayList<GetDrcByProcessor> DrcList;
    DrclistAdapter adapter;
    // String strLanguageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_drclist);
        strProcessorId = appHelper.getSharedPrefObj().getString(DeviceProcessorId, "");
        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");


        initializeUI();
        configureDagger();
        configureViewModel();
        initializeValues();

        if (appHelper.isNetworkAvailable()) {
            getDrcDetailsByProcessorIdFromServer();
        } else {
            Toast.makeText(Drclist.this, "please check  your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeUI() {
        recyclegrn = findViewById(R.id.recycler_grn);
        imgCancel = findViewById(R.id.img_cancel);
        searchByName = findViewById(R.id.svDRC);
        imgempty = findViewById(R.id.emptyListImage);

        //language
        txt_headerview = findViewById(R.id.txtDRCview);
        addDrcbtn = findViewById(R.id.addDrcbtn);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Drclist.this, MainDashBoardActivity.class); // Replace MainActivity with your actual dashboard activity
        startActivity(intent);
        finish();
    }

    private void initializeValues() {
        DrcList = new ArrayList<>();

        //   getLanguageDataList();

        addDrcbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Drclist.this, ProcessorAddDrcDataActivity.class);
                startActivity(intent);
            }
        });

        // Initialize your RecyclerView and Adapter here
        recyclegrn = findViewById(R.id.recycler_grn);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclegrn.setLayoutManager(layoutManager);
        DrcList = new ArrayList<>(); // Initialize the list

        // Create the adapter and set it to the RecyclerView


        imgCancel.setOnClickListener(v -> {
            Intent intent = new Intent(Drclist.this, MainDashBoardActivity.class); // Replace MainActivity with your actual dashboard activity
            startActivity(intent);
            finish();
        });

        SearchManager searchManager = (SearchManager) Drclist.this.getSystemService(Context.SEARCH_SERVICE);
        searchByName.setSearchableInfo(searchManager
                .getSearchableInfo(Drclist.this.getComponentName()));
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
        final ProgressDialog progressDialog = new ProgressDialog(Drclist.this, R.style.AppCompatAlertDialogStyle);
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
                                    if (getResources().getString(R.string.add_drc).equals(jsonEngWord)) {
                                        addDrcbtn.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.drc_list).equals(jsonEngWord)) {
                                        txt_headerview.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.search_by_drc_no).equals(jsonEngWord)) {
                                        searchByName.setQueryHint(jsonEngWord);
                                    }


                                } else {

                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");

                                    if (getResources().getString(R.string.add_drc).equals(jsonEngWord)) {
                                        addDrcbtn.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.drc_list).equals(jsonEngWord)) {
                                        txt_headerview.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.search_by_drc_no).equals(jsonEngWord)) {
                                        searchByName.setQueryHint(strConvertedWord);
                                    }


                                }
//                                else if (strLanguageId.equals("3")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//                                    if ("ADD DRC".equals(jsonEngWord)) {
//                                        addDrcbtn.setText(strConvertedWord);
//                                    } else if ("GRN List".equals(jsonEngWord)) {
//                                        txt_headerview.setText(strConvertedWord);
//                                    } else if ("Search by DRC No".equals(jsonEngWord)) {
//                                        searchByName.setQueryHint(strConvertedWord);
//                                    }
//
//                                } else if (strLanguageId.equals("4")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//                                    if ("ADD DRC".equals(jsonEngWord)) {
//                                        addDrcbtn.setText(strConvertedWord);
//                                    } else if ("GRN List".equals(jsonEngWord)) {
//                                        txt_headerview.setText(strConvertedWord);
//                                    } else if ("Search by DRC No".equals(jsonEngWord)) {
//                                        searchByName.setQueryHint(strConvertedWord);
//                                    }
//
//
//                                } else if (strLanguageId.equals("5")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//                                    if ("ADD DRC".equals(jsonEngWord)) {
//                                        addDrcbtn.setText(strConvertedWord);
//                                    } else if ("GRN List".equals(jsonEngWord)) {
//                                        txt_headerview.setText(strConvertedWord);
//                                    } else if ("Search by DRC No".equals(jsonEngWord)) {
//                                        searchByName.setQueryHint(strConvertedWord);
//                                    }
//
//
//                                } else {


                            }


                        } catch (Exception ex) {
                            ex.printStackTrace();
                            progressDialog.dismiss();
                            Log.d("Error", ">>>>" + ex.toString());
                        }
                    } else {
                        progressDialog.dismiss();

                        Toast.makeText(Drclist.this, "no records found", Toast.LENGTH_LONG).show();
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


    private void configureDagger() {
        AndroidInjection.inject(Drclist.this);
    }

    public void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
    }


    private void getDrcDetailsByProcessorIdFromServer() {

        AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        final ProgressDialog progressDialog = new ProgressDialog(Drclist.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("generating pdf please wait ");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Call<JsonElement> callRetrofit = service.getDrcDetailsByProcessorIdFromServer(strProcessorId, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));

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
                            ArrayList<GetDrcByProcessor> getDRCListTableArrayList = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                GetDrcByProcessor DrcList = new GetDrcByProcessor();
                                DrcList.setId(Integer.valueOf(jsonObjectFarmerPD.getString("Id")));
                                DrcList.setDRCNo(jsonObjectFarmerPD.getString("DRCNo"));
                                DrcList.setCreatedDate(jsonObjectFarmerPD.getString("CreatedDate"));
                                DrcList.setDRCDocument(jsonObjectFarmerPD.getString("DRCDocument"));
                                getDRCListTableArrayList.add(DrcList);
                            }

                            // Update the adapter with the new data
                            DrcList.clear();
                            DrcList.addAll(getDRCListTableArrayList);
                            adapter = new DrclistAdapter(appHelper, DrcList);
                            recyclegrn.setAdapter(adapter);

                            if (adapter.getItemCount() == 0) {
                                imgempty.setVisibility(View.VISIBLE);
                                recyclegrn.setVisibility(View.GONE);
                            } else {
                                imgempty.setVisibility(View.GONE);
                                recyclegrn.setVisibility(View.VISIBLE);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            String message = jsonObject.optString("message", ""); // Get the "message" field from the JSON response
                            if ("no GRN found".equals(message)) {
                                // The "message" field is "no GRN found," so display the toast
                                Toast.makeText(Drclist.this, "No GRN found", Toast.LENGTH_LONG).show();
                                Log.d("Check", "No GRN found");
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
}