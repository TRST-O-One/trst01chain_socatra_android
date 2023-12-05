package com.socatra.intellitrack.activity.main_dash_board;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.DeviceDealerID;
import static com.socatra.intellitrack.constants.AppConstant.DeviceProcessorId;
import static com.socatra.intellitrack.constants.AppConstant.LanguageId;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.socatra.intellitrack.BaseActivity;
import com.socatra.intellitrack.R;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.models.get.GetdashboardTotalProcurement;
import com.socatra.intellitrack.models.get.GetdashboardTotalSupply;
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

public class TotalSupplyReportActivity extends BaseActivity {

    private static final String TAG = TotalSupplyReportActivity.class.getCanonicalName();

    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    public AppViewModel viewModel;


    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    String deviceRoleName;

    String strDealerName;
    String strDealerID;
    String strProcessorId;

    TextView txtYearly, txtMontly, txtWeekly;

    TextView txtl_Yearly, txtl_Montly, txtl_Weekly, txtl_headerview;

    String strLanguageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_supply_report);
        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");
        strProcessorId = appHelper.getSharedPrefObj().getString(DeviceProcessorId, "");
        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");
        initializeUI();
        initializeValues();
        configureDagger();
        configureViewModel();

    }

    private void initializeUI() {
        txtYearly = findViewById(R.id.txt_yearly_supply);
        txtMontly = findViewById(R.id.txt_montly_supply);
        txtWeekly = findViewById(R.id.txt_weekly_supply);

        //language
        txtl_Yearly = findViewById(R.id.txt_word_yearly);
        txtl_Montly = findViewById(R.id.txt_word_monthly);
        txtl_Weekly = findViewById(R.id.txt_word_weekly);
        txtl_headerview = findViewById(R.id.txt_word_total_supply_view);


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (appHelper.isNetworkAvailable()) {
            //getLanguageDataList();
        } else {
            Toast.makeText(TotalSupplyReportActivity.this, "please check your internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void getLanguageDataList() {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        final ProgressDialog progressDialog = new ProgressDialog(TotalSupplyReportActivity.this, R.style.AppCompatAlertDialogStyle);
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
                                    if (getResources().getString(R.string.total_supply_report).equals(jsonEngWord)) {
                                        txtl_headerview.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.yearly).equals(jsonEngWord)) {
                                        txtl_Yearly.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.monthly).equals(jsonEngWord)) {
                                        txtl_Montly.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.weekly).equals(jsonEngWord)) {
                                        txtl_Weekly.setText(jsonEngWord1);
                                    }
                                } else {

                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");

                                    if (getResources().getString(R.string.total_supply_report).equals(jsonEngWord)) {
                                        txtl_headerview.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.yearly).equals(jsonEngWord)) {
                                        txtl_Yearly.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.monthly).equals(jsonEngWord)) {
                                        txtl_Montly.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.weekly).equals(jsonEngWord)) {
                                        txtl_Weekly.setText(strConvertedWord);
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

                        Toast.makeText(TotalSupplyReportActivity.this, "no records found", Toast.LENGTH_LONG).show();
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


    private void initializeValues() {

        if (appHelper.isNetworkAvailable()) {
            getdashboardtotalSupply();
        } else {
            Toast.makeText(TotalSupplyReportActivity.this, "please check your internet connection", Toast.LENGTH_LONG).show();
        }


    }


    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    public void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);


    }

    private void getdashboardtotalSupply() {

        if (appHelper.getSharedPrefObj().getString(DeviceDealerID, "").equals("")) {
            final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
            final ProgressDialog progressDialog = new ProgressDialog(TotalSupplyReportActivity.this, R.style.AppCompatAlertDialogStyle);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("loading  data...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            Call<JsonElement> callRetrofit = null;
            callRetrofit = service.getDashboardTotalSupplybyProcessor(strProcessorId, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));
            callRetrofit.enqueue(new Callback<JsonElement>() {

                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    try {
                        if (response.isSuccessful()) {
                            JsonElement jsonElement = response.body();
                            if (jsonElement != null) {
                                JsonObject jsonObject = jsonElement.getAsJsonObject();
                                JsonArray dataArray = jsonObject.getAsJsonArray("data");

                                if (dataArray.size() > 0) {
                                    progressDialog.dismiss();
                                    ArrayList<GetdashboardTotalProcurement> TotalSupplylist = new ArrayList<>();

                                    for (JsonElement innerArrayElement : dataArray) {
                                        if (innerArrayElement.isJsonArray()) {
                                            JsonArray innerArray = innerArrayElement.getAsJsonArray();
                                            if (innerArray.size() > 0) {
                                                JsonObject dataObject = innerArray.get(0).getAsJsonObject();


                                                if (dataObject.has("SumofTotalLastYearSupply")) {
                                                    int SumofTotalLastYearSupply = dataObject.get("SumofTotalLastYearSupply").getAsInt();
                                                    Log.d(TAG, "SumofTotalLastYearSupply: " + SumofTotalLastYearSupply);
                                                    txtYearly.setText(String.valueOf(SumofTotalLastYearSupply));

                                                } else if (dataObject.has("SumofTotalLastMonthSupply")) {
                                                    int SumofTotalLastMonthSupply = dataObject.get("SumofTotalLastMonthSupply").getAsInt();
                                                    Log.d(TAG, "SumofTotalLastMonthSupply: " + SumofTotalLastMonthSupply);
                                                    txtMontly.setText(String.valueOf(SumofTotalLastMonthSupply));
                                                } else if (dataObject.has("SumofTotalLastWeekSupply")) {
                                                    int SumofTotalLastWeekSupply = dataObject.get("SumofTotalLastWeekSupply").getAsInt();
                                                    Log.d(TAG, "SumofTotalLastWeekSupply: " + SumofTotalLastWeekSupply);
                                                    txtWeekly.setText(String.valueOf(SumofTotalLastWeekSupply));

                                                }

                                            }
                                        }
                                    }

                                    if (!TotalSupplylist.isEmpty()) {

                                    }
                                } else {
                                    progressDialog.dismiss();

                                    Toast.makeText(TotalSupplyReportActivity.this, "No records found", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                progressDialog.dismiss();

                                Toast.makeText(TotalSupplyReportActivity.this, "Invalid JSON response", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            progressDialog.dismiss();

                            Toast.makeText(TotalSupplyReportActivity.this, "Response not successful", Toast.LENGTH_LONG).show();
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
        } else {
            final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
            final ProgressDialog progressDialog = new ProgressDialog(TotalSupplyReportActivity.this, R.style.AppCompatAlertDialogStyle);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("loading  data...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            Call<JsonElement> callRetrofit = null;
            callRetrofit = service.getDashboardTotalSupplybyDealer(strDealerID, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));
            callRetrofit.enqueue(new Callback<JsonElement>() {

                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    try {
                        if (response.isSuccessful()) {
                            JsonElement jsonElement = response.body();

                            if (jsonElement != null) {
                                JsonObject jsonObject = jsonElement.getAsJsonObject();
                                JsonArray dataArray = jsonObject.getAsJsonArray("data");

                                if (dataArray.size() > 0) {
                                    progressDialog.dismiss();

                                    ArrayList<GetdashboardTotalSupply> TotalSupplylist = new ArrayList<>();

                                    for (JsonElement innerArrayElement : dataArray) {
                                        if (innerArrayElement.isJsonArray()) {
                                            JsonArray innerArray = innerArrayElement.getAsJsonArray();
                                            if (innerArray.size() > 0) {
                                                JsonObject dataObject = innerArray.get(0).getAsJsonObject();


                                                if (dataObject.has("SumofTotalLastYearSupply")) {
                                                    int SumofTotalLastYearSupply = dataObject.get("SumofTotalLastYearSupply").getAsInt();
                                                    Log.d(TAG, "SumofTotalLastYearSupply: " + SumofTotalLastYearSupply);
                                                    txtYearly.setText(String.valueOf(SumofTotalLastYearSupply));

                                                } else if (dataObject.has("SumofTotalLastMonthSupply")) {
                                                    int SumofTotalLastMonthSupply = dataObject.get("SumofTotalLastMonthSupply").getAsInt();
                                                    Log.d(TAG, "SumofTotalLastMonthSupply: " + SumofTotalLastMonthSupply);
                                                    txtMontly.setText(String.valueOf(SumofTotalLastMonthSupply));
                                                } else if (dataObject.has("SumofTotalLastWeekSupply")) {
                                                    int SumofTotalLastWeekSupply = dataObject.get("SumofTotalLastWeekSupply").getAsInt();
                                                    Log.d(TAG, "SumofTotalLastWeekSupply: " + SumofTotalLastWeekSupply);
                                                    txtWeekly.setText(String.valueOf(SumofTotalLastWeekSupply));

                                                }

                                            }
                                        }
                                    }

                                    if (!TotalSupplylist.isEmpty()) {

                                    }
                                } else {
                                    progressDialog.dismiss();

                                    Toast.makeText(TotalSupplyReportActivity.this, "No records found", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                progressDialog.dismiss();

                                Toast.makeText(TotalSupplyReportActivity.this, "Invalid JSON response", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            progressDialog.dismiss();

                            Toast.makeText(TotalSupplyReportActivity.this, "Response not successful", Toast.LENGTH_LONG).show();
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
}