package com.socatra.intellitrack.activity.qualitycheck;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.DeviceProcessorId;
import static com.socatra.intellitrack.constants.AppConstant.LanguageId;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
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
import com.socatra.intellitrack.adapters.BatchQualityCheckAdapter;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.models.get.GetQualityCheckDetails;
import com.socatra.intellitrack.view_models.AppViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QualityCheckList extends BaseActivity {

    private static final String TAG = QualityCheckList.class.getCanonicalName();
    private static final int FILE_SELECT_CODE = 1;
    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    public AppViewModel viewModel;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    //    String ProcessorId = "1"; // Updated ProcessorId to 1
    String strProcessorId, strLanguageId;
    RecyclerView recycleQualitycheckbatch;
    ImageView imgCancel;
    LinearLayout imgempty;
    SearchView svFarmer, searchByName;
    Button addquality;
    TextView txtheaderView;
    ArrayList<GetQualityCheckDetails> Qualitylist;
    BatchQualityCheckAdapter adapter;
    String selectedFilePath = "";
    File selectedFile;
    String mTag = "DealerTag";
    private ImageView imgQualityCheckEvidence;
    private String[] PERMISSIONS_STORAGE = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_quality_check);
        strProcessorId = appHelper.getSharedPrefObj().getString(DeviceProcessorId, "");
        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");


        initializeUI();
        configureDagger();
        configureViewModel();
        initializeValues();


        if (appHelper.isNetworkAvailable()) {
            getQualityCheckDetailsByProcessorIdFromServer();
        } else {
            Toast.makeText(QualityCheckList.this, "please check your internet connection", Toast.LENGTH_LONG).show();
        }

    }

    private void initializeUI() {
        recycleQualitycheckbatch = findViewById(R.id.recycler_Quality_Check);
        imgCancel = findViewById(R.id.img_cancel);
        searchByName = findViewById(R.id.svQualityCheck);
        imgempty = findViewById(R.id.emptyListImage);

        //language
        addquality = findViewById(R.id.add_quality_btn);
        txtheaderView = findViewById(R.id.txt_headerview);


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (appHelper.isNetworkAvailable()) {
            //getLanguageDataList();
        } else {
            Toast.makeText(QualityCheckList.this, "please check your internet connection", Toast.LENGTH_SHORT).show();
        }

    }


    private void initializeValues() {


        Qualitylist = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycleQualitycheckbatch.setLayoutManager(layoutManager);
        Qualitylist = new ArrayList<>(); // Initialize the list


        getLanguageDataList();

        addquality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QualityCheckList.this, AddQualityCheck.class);
                startActivity(intent);
            }
        });

        imgCancel.setOnClickListener(v -> {
            Intent intent = new Intent(QualityCheckList.this, MainDashBoardActivity.class); // Replace MainActivity with your actual dashboard activity
            startActivity(intent);
            finish();
        });

        SearchManager searchManager = (SearchManager) QualityCheckList.this.getSystemService(Context.SEARCH_SERVICE);
        searchByName.setSearchableInfo(searchManager
                .getSearchableInfo(QualityCheckList.this.getComponentName()));
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


    private void configureDagger() {
        AndroidInjection.inject(QualityCheckList.this);
    }

    public void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
    }


    private void getQualityCheckDetailsByProcessorIdFromServer() {

        AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);

        final ProgressDialog progressDialog = new ProgressDialog(QualityCheckList.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading data...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();


        Call<JsonElement> callRetrofit = service.getQualityCheckDeatilsByProcessorIdFromServer(strProcessorId, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));

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
                            progressDialog.dismiss();
                            ArrayList<GetQualityCheckDetails> getQualityListTableArrayList = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                GetQualityCheckDetails QualityList = new GetQualityCheckDetails();
                                QualityList.setId(Integer.valueOf(jsonObjectFarmerPD.getString("Id")));
                                QualityList.setBatchCode(jsonObjectFarmerPD.getString("BatchCode"));
                                QualityList.setQualityCheckDate(jsonObjectFarmerPD.getString("QualityCheckDate"));
                                QualityList.setQuantityRate(Double.valueOf(jsonObjectFarmerPD.getString("QuantityRate")));

                                getQualityListTableArrayList.add(QualityList);
                            }


                            Qualitylist.clear();
                            Qualitylist.addAll(getQualityListTableArrayList);
                            adapter = new BatchQualityCheckAdapter(appHelper, Qualitylist);
                            recycleQualitycheckbatch.hasFixedSize();
                            if (adapter.getItemCount() == 0) {
                                imgempty.setVisibility(View.VISIBLE);
                                recycleQualitycheckbatch.setVisibility(View.GONE);
                            } else {
                                imgempty.setVisibility(View.GONE);
                                recycleQualitycheckbatch.setVisibility(View.VISIBLE);
                            }
                            recycleQualitycheckbatch.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } else {
                            String message = jsonObject.optString("message", ""); // Get the "message" field from the JSON response
                            if ("no Batch found".equals(message)) {
                                // The "message" field is "no GRN found," so display the toast
                                Toast.makeText(QualityCheckList.this, "No Batch found", Toast.LENGTH_LONG).show();
                                Log.d("Check", "No Batch found");
                            } else {
                                // Handle other cases when "message" is not "no GRN found"
                                // You can log an error message or display an appropriate toast/message here.
                            }
                        }
                    } else {

                        progressDialog.dismiss();
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
        final ProgressDialog progressDialog = new ProgressDialog(QualityCheckList.this, R.style.AppCompatAlertDialogStyle);
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
                                    if (getResources().getString(R.string.add_plus).equals(jsonEngWord)) {
                                        addquality.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.quality_check_list).equals(jsonEngWord)) {
                                        txtheaderView.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.search_by_batch_no).equals(jsonEngWord)) {
                                        searchByName.setQueryHint(jsonEngWord1);
                                    }
                                } else {

                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");

                                    if (getResources().getString(R.string.add_plus).equals(jsonEngWord)) {
                                        addquality.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.quality_check_list).equals(jsonEngWord)) {
                                        txtheaderView.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.search_by_batch_no).equals(jsonEngWord)) {
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

                        Toast.makeText(QualityCheckList.this, "no records found", Toast.LENGTH_LONG).show();
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
