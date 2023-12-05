package com.socatra.intellitrack.activity.Traderflow;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.DeviceDealerID;
import static com.socatra.intellitrack.constants.AppConstant.DeviceTraderId;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserName;
import static com.socatra.intellitrack.constants.AppConstant.LanguageId;

import androidx.appcompat.widget.AppCompatSpinner;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.socatra.intellitrack.BaseActivity;
import com.socatra.intellitrack.R;
import com.socatra.intellitrack.adapters.CustomerListbyTraderAdapter;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.models.get.GetCustomerInvoicesByTraderId;
import com.socatra.intellitrack.models.get.Month;
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

public class TraderCustListAct extends BaseActivity implements View.OnClickListener {

    private static final String TAG = TraderCustListAct.class.getCanonicalName();
    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    public AppViewModel viewModel;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    SearchView svFarmer, searchByName;
    LinearLayout imgempty;
    String Token, strDealerID, strDealerName, strTraderID;

    ImageView ImgCancel;

    AppCompatSpinner SpMonth, SpYear;

    Month[] monthItems = new Month[]{
            new Month("Select *", "0"),
            new Month("January", "1"),
            new Month("February", "2"),
            new Month("March", "3"),
            new Month("April", "4"),
            new Month("May", "5"),
            new Month("June", "6"),
            new Month("July", "7"),
            new Month("August", "8"),
            new Month("September", "9"),
            new Month("October", "10"),
            new Month("November", "11"),
            new Month("December", "12"),

    };


    String[] selectMontharr = new String[]{"Select *", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    String[] selectMontharr1 = new String[]{"Select *", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};


    String[] selectYeararr = new String[]{"Select *", "2023", "2022", "2021"};


    RecyclerView recycleCustomer;

    ArrayList<GetCustomerInvoicesByTraderId> Customerlist;

    CustomerListbyTraderAdapter customerListBYTraderAdapter;

    String strLanguageId;


    TextView txtWordTitle, txtWordBatchNum, txtWordBatchCreatedDate, txtWordTitleList, txtWordAddGrn, txtWordGrnNum, txtWordActualQuantity, txtSaveBatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_listby_trader);

        strTraderID = appHelper.getSharedPrefObj().getString(DeviceTraderId, "");
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

        SpMonth = findViewById(R.id.sp_selectmonth);
        SpYear = findViewById(R.id.sp_selectyear);
        txtWordTitle = findViewById(R.id.txt_word_title_cust_trd);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
            SearchManager searchManager = (SearchManager) TraderCustListAct.this.getSystemService(Context.SEARCH_SERVICE);
            searchByName.setSearchableInfo(searchManager
                    .getSearchableInfo(TraderCustListAct.this.getComponentName()));
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
                    if (customerListBYTraderAdapter != null && customerListBYTraderAdapter.getFilter() != null) {
                        customerListBYTraderAdapter.getFilter().filter(query);
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    if (customerListBYTraderAdapter != null && customerListBYTraderAdapter.getFilter() != null) {
                        customerListBYTraderAdapter.getFilter().filter(query);
                    }
                    return false;
                }
            });

        }


        Customerlist = new ArrayList<>();
        recycleCustomer.setLayoutManager(new LinearLayoutManager(this));


        String[] monthNames = new String[monthItems.length];
        for (int i = 0; i < monthItems.length; i++) {
            monthNames[i] = monthItems[i].getMonthName();
        }


        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(TraderCustListAct.this,
                android.R.layout.simple_spinner_item, selectYeararr);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        SpYear.setAdapter(yearAdapter);
        yearAdapter.notifyDataSetChanged();


        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(TraderCustListAct.this,
                android.R.layout.simple_spinner_item, monthNames);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        monthAdapter.notifyDataSetChanged();


        SpMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Month selectedMonthItem = monthItems[position];

                if (!selectedMonthItem.getMonthName().equals("Select *")) {


                    yearAdapter.notifyDataSetChanged();
                    SpYear.setSelection(0); // Set to the default value or adjust as needed

                    String selectedYear = (String) SpYear.getSelectedItem();
                    String selectedMonthId = selectedMonthItem.getMonthId();

                    Log.d("selectedMonthId", "Month : " + selectedMonthId);

                    // Pass the selected year and month ID to the method
                    getCustomerListbyTraderId(selectedYear, selectedMonthId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        SpYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedYear = (String) parent.getItemAtPosition(position);

                if (!selectedYear.equals("Select *")) {

                }
                Month selectedMonthItem = monthItems[position];
                String selectedMonth = selectedMonthItem.getMonthId();
                getCustomerListbyTraderId(selectedYear, selectedMonth);


                monthAdapter.notifyDataSetChanged();
                SpMonth.setSelection(0);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    private void configureDagger() {
        AndroidInjection.inject(TraderCustListAct.this);
    }

    public void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (appHelper.isNetworkAvailable()) {
            //getLanguageDataList();
        } else {
            Toast.makeText(TraderCustListAct.this, "please check your internet connection", Toast.LENGTH_SHORT).show();
        }

    }


    private void getLanguageDataList() {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        final ProgressDialog progressDialog = new ProgressDialog(TraderCustListAct.this, R.style.AppCompatAlertDialogStyle);
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
                                    if (getResources().getString(R.string.customer_list).equals(jsonEngWord)) {
                                        txtWordTitle.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.search_by_name_email).equals(jsonEngWord)) {
                                        searchByName.setQueryHint(jsonEngWord1);
                                    }
                                } else {

                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");

                                    if (getResources().getString(R.string.customer_list).equals(jsonEngWord)) {
                                        txtWordTitle.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.search_by_name_email).equals(jsonEngWord)) {
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

                        Toast.makeText(TraderCustListAct.this, "no records found", Toast.LENGTH_LONG).show();
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


    private void getCustomerListbyTraderId(String selectedYear, String selectedMonth) {
        AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        final ProgressDialog progressDialog = new ProgressDialog(TraderCustListAct.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading  data...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        Call<JsonElement> callRetrofit = service.getCustomerInvoicesByTraderIdFromServer(strTraderID, selectedYear, selectedMonth, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));

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
                            ArrayList<GetCustomerInvoicesByTraderId> getCustomerListTableArrayList = new ArrayList<>();


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                GetCustomerInvoicesByTraderId CustomerList = new GetCustomerInvoicesByTraderId();
                                CustomerList.setCustomer((jsonObjectFarmerPD.getString("Customer")));
                                CustomerList.setToCustomerId(Integer.valueOf(jsonObjectFarmerPD.getString("ToCustomerId")));
                                CustomerList.setMonth(jsonObjectFarmerPD.getString("Month"));
                                CustomerList.setMonthlyVolume(Integer.valueOf(jsonObjectFarmerPD.getString("MonthlyVolume")));
                                CustomerList.setTotalInvoices(Integer.valueOf(String.valueOf(jsonObjectFarmerPD.getInt("TotalInvoices"))));

                                getCustomerListTableArrayList.add(CustomerList);

                            }

                            // Update the adapter with the new data
                            Customerlist.addAll(getCustomerListTableArrayList);
                            customerListBYTraderAdapter = new CustomerListbyTraderAdapter(Customerlist);

                            if (customerListBYTraderAdapter.getItemCount() == 0) {
                                imgempty.setVisibility(View.VISIBLE);
                                recycleCustomer.setVisibility(View.GONE);
                            } else {
                                imgempty.setVisibility(View.GONE);
                                recycleCustomer.setVisibility(View.VISIBLE);
                            }
                            recycleCustomer.hasFixedSize();
                            recycleCustomer.setAdapter(customerListBYTraderAdapter);
                            customerListBYTraderAdapter.notifyDataSetChanged();
                        } catch (Exception ex) {
                            progressDialog.dismiss();
                            ex.printStackTrace();
                            Log.d("Error", ">>>>" + ex.toString());
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(TraderCustListAct.this, "no records found", Toast.LENGTH_LONG).show();
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