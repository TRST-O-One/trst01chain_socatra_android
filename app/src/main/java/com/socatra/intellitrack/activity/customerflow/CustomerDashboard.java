package com.socatra.intellitrack.activity.customerflow;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.App_PackageName;
import static com.socatra.intellitrack.constants.AppConstant.DeviceCustomerAddress;
import static com.socatra.intellitrack.constants.AppConstant.DeviceCustomerId;
import static com.socatra.intellitrack.constants.AppConstant.DeviceCustomerName;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserName;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserPwd;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserRole;
import static com.socatra.intellitrack.constants.AppConstant.LanguageId;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.socatra.intellitrack.BaseActivity;
import com.socatra.intellitrack.R;
import com.socatra.intellitrack.activity.LoginActivity;

import com.socatra.intellitrack.activity.grnflow.DealerAddGrnActivity;

import com.socatra.intellitrack.adapters.CustomerInvoiceListAdapter;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.models.get.GetInvoiceDetailsByCustomerId;
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

public class CustomerDashboard extends BaseActivity implements View.OnClickListener {
    private static final String TAG = DealerAddGrnActivity.class.getCanonicalName();
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    public AppViewModel viewModel;
    LinearLayout llAddGRN, llBatchProcessing, llInvoice, llDrc, llQualityCheck, llLogOut;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    String deviceRoleName;
    String strUserName;
    String strDealerID;
    String strProcessorId;

    String strCustomerId;

    Dialog dialog;
    TextView txtUserName, txtRoleName, CustomerName, CustomerRole;

    LinearLayout Customerlayout;

    LinearLayout plotsNavLayout;
    SharedPreferences sharedPreferencesData;
    //Customer Login
    RecyclerView recycleCustomerInvoice;
    ArrayList<GetInvoiceDetailsByCustomerId> NavInvoicelist;
    CustomerInvoiceListAdapter adapter;
    SearchView searchByName;
    TextView txtWordAppTitle, TxtWordCustomerTitle, txtWordCustomerRole, txtWordInvoiceList;
    String strLanguageId;
    private DrawerLayout drawerLayout;
    private ImageButton CustomerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);
        deviceRoleName = appHelper.getSharedPrefObj().getString(DeviceUserRole, "");
        strUserName = appHelper.getSharedPrefObj().getString(DeviceCustomerName, "");

        strCustomerId = appHelper.getSharedPrefObj().getString(DeviceCustomerId, "");
        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");
        sharedPreferencesData = getSharedPreferences(App_PackageName, MODE_PRIVATE);
        initializeUI();
        initializeValues();
        configureDagger();
        configureViewModel();

        if (appHelper.isNetworkAvailable()) {
            getInvoiceList();
        } else {
            Toast.makeText(CustomerDashboard.this, "please check your internet connection", Toast.LENGTH_LONG).show();
        }


    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finish();
//    }

    private void initializeUI() {

        CustomerButton = findViewById(R.id.C_Logout);
        txtUserName = findViewById(R.id.txtName);
        txtRoleName = findViewById(R.id.txtRole);

        //Customer Layout
        Customerlayout = findViewById(R.id.ll_Customer_layout);
        CustomerName = findViewById(R.id.txtCustomerName);
        CustomerRole = findViewById(R.id.txtCustomerRole);
        recycleCustomerInvoice = findViewById(R.id.recycler_Customer_invoice);
        searchByName = findViewById(R.id.svCustomerInvoice);


        CustomerName.setText(strUserName);
        CustomerRole.setText(deviceRoleName);


        txtWordAppTitle = findViewById(R.id.home_header_title_cus);
        TxtWordCustomerTitle = findViewById(R.id.txt_word_cust_name);
        txtWordCustomerRole = findViewById(R.id.txt_word_txt_role);
        txtWordInvoiceList = findViewById(R.id.txt_word_invoice);
        //  searchByName = findViewById(R.id.svCustomerInvoice);


    }

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        showExitConfirmationDialog();
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish();
//
//        System.exit(0);
    }

    //    @Override
//    public void onBackPressed() {
//
//        showExitConfirmationDialog();
//
//    }


    @Override
    protected void onResume() {
        super.onResume();

        if (appHelper.isNetworkAvailable()) {
           // getLanguageDataList();
        } else {
            Toast.makeText(getApplicationContext(), "please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getLanguageDataList() {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        final ProgressDialog progressDialog = new ProgressDialog(CustomerDashboard.this, R.style.AppCompatAlertDialogStyle);
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

                                    if (getResources().getString(R.string.app_name).equals(jsonEngWord)) {
                                        txtWordAppTitle.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.name).equals(jsonEngWord)) {
                                        TxtWordCustomerTitle.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.role).equals(jsonEngWord)) {
                                        txtWordCustomerRole.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.invoice_list).equals(jsonEngWord)) {
                                        txtWordInvoiceList.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.search_here).equals(jsonEngWord)) {
                                        searchByName.setQueryHint(jsonEngWord1);
                                    }
                                } else {

                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");

                                    if (getResources().getString(R.string.app_name).equals(jsonEngWord)) {
                                        txtWordAppTitle.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.name).equals(jsonEngWord)) {
                                        TxtWordCustomerTitle.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.role).equals(jsonEngWord)) {
                                        txtWordCustomerRole.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.invoice_list).equals(jsonEngWord)) {
                                        txtWordInvoiceList.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.search_here).equals(jsonEngWord)) {
                                        searchByName.setQueryHint(strConvertedWord);
                                    }


                                }


//                                else if (strLanguageId.equals("3")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//
//
//                                    if ("IntelliTrack".equals(jsonEngWord)) {
//                                        txtWordAppTitle.setText(strConvertedWord);
//                                    } else if ("Name".equals(jsonEngWord)) {
//                                        TxtWordCustomerTitle.setText(strConvertedWord);
//                                    } else if ("Role".equals(jsonEngWord)) {
//                                        txtWordCustomerRole.setText(strConvertedWord);
//                                    } else if ("Invoice List".equals(jsonEngWord)) {
//                                        txtWordInvoiceList.setText(strConvertedWord);
//                                    } else if ("Search here".equals(jsonEngWord)) {
//                                        searchByName.setQueryHint(strConvertedWord);
//                                    }
//
//                                } else if (strLanguageId.equals("4")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//
//
//                                    if ("IntelliTrack".equals(jsonEngWord)) {
//                                        txtWordAppTitle.setText(strConvertedWord);
//                                    } else if ("Name".equals(jsonEngWord)) {
//                                        TxtWordCustomerTitle.setText(strConvertedWord);
//                                    } else if ("Role".equals(jsonEngWord)) {
//                                        txtWordCustomerRole.setText(strConvertedWord);
//                                    } else if ("Invoice List".equals(jsonEngWord)) {
//                                        txtWordInvoiceList.setText(strConvertedWord);
//                                    } else if ("Search here".equals(jsonEngWord)) {
//                                        searchByName.setQueryHint(strConvertedWord);
//                                    }
//
//
//                                } else if (strLanguageId.equals("5")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//
//
//                                    if ("IntelliTrack".equals(jsonEngWord)) {
//                                        txtWordAppTitle.setText(strConvertedWord);
//                                    } else if ("Name".equals(jsonEngWord)) {
//                                        TxtWordCustomerTitle.setText(strConvertedWord);
//                                    } else if ("Role".equals(jsonEngWord)) {
//                                        txtWordCustomerRole.setText(strConvertedWord);
//                                    } else if ("Invoice List".equals(jsonEngWord)) {
//                                        txtWordInvoiceList.setText(strConvertedWord);
//                                    } else if ("Search here".equals(jsonEngWord)) {
//                                        searchByName.setQueryHint(strConvertedWord);
//                                    }
//
//
//                                } else {
//                                    String jsonEngWord1 = jsonObjectFarmerPD.getString("SelectedWord");
//
//                                    if ("IntelliTrack".equals(jsonEngWord)) {
//                                        txtWordAppTitle.setText(jsonEngWord1);
//                                    } else if ("Name".equals(jsonEngWord)) {
//                                        TxtWordCustomerTitle.setText(jsonEngWord1);
//                                    } else if ("Role".equals(jsonEngWord)) {
//                                        txtWordCustomerRole.setText(jsonEngWord1);
//                                    } else if ("Invoice List".equals(jsonEngWord)) {
//                                        txtWordInvoiceList.setText(jsonEngWord1);
//                                    } else if ("Search here".equals(jsonEngWord)) {
//                                        searchByName.setQueryHint(jsonEngWord1);
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

                        Toast.makeText(CustomerDashboard.this, "no records found", Toast.LENGTH_LONG).show();
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

    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to exit the app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        System.exit(0);
                        finish();
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog, do nothing
                    }
                });
        builder.create().show();
    }

    private void initializeValues() {

        CustomerButton.setOnClickListener(this);


        if (searchByName != null) {

            SearchManager searchManager = (SearchManager) CustomerDashboard.this.getSystemService(Context.SEARCH_SERVICE);
            searchByName.setSearchableInfo(searchManager
                    .getSearchableInfo(CustomerDashboard.this.getComponentName()));
            searchByName.setMaxWidth(Integer.MAX_VALUE);
// listening to search query text change

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


        NavInvoicelist = new ArrayList<>();
//        adapter=new DealerListAdapter(appHelper,viewModel,Dealerlist , );
        adapter = new CustomerInvoiceListAdapter(appHelper, viewModel, (List<GetInvoiceDetailsByCustomerId>) NavInvoicelist, strDealerID);

        recycleCustomerInvoice.setLayoutManager(new LinearLayoutManager(this));
        recycleCustomerInvoice.hasFixedSize();
        recycleCustomerInvoice.setAdapter(adapter);

    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    public void configureViewModel() {
        //  viewModel = ViewModelProviders.of(this, viewModelFactory).get(AppViewModel.class);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);


    }


    private void getInvoiceList() {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getInvoiceDetailsByCustomerIdFromServer(
                strCustomerId,
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
                            ArrayList<GetInvoiceDetailsByCustomerId> InvoiceArraylist = new ArrayList<>();
                            NavInvoicelist.clear();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                GetInvoiceDetailsByCustomerId Invoicelist = new GetInvoiceDetailsByCustomerId();
                                Invoicelist.setId(Integer.valueOf(jsonObjectFarmerPD.getString("Id")));
                                Invoicelist.setInvoiceNumber(jsonObjectFarmerPD.getString("InvoiceNumber"));
                                Invoicelist.setProduct(jsonObjectFarmerPD.getString("Product"));
//                                Invoicelist.setDealerName(jsonObjectFarmerPD.getString("DealerName"));
                                Invoicelist.setQuantity(Integer.valueOf(jsonObjectFarmerPD.getString("Quantity")));
                                Invoicelist.setShippingAddress(jsonObjectFarmerPD.getString("ShippingAddress"));
                                Invoicelist.setCustomerName(jsonObjectFarmerPD.getString("CustomerName"));
                                Invoicelist.setProcessorName(jsonObjectFarmerPD.getString("ProcessorName"));
                                Invoicelist.setProcessorAddress(jsonObjectFarmerPD.getString("ProcessorAddress"));
                                Invoicelist.setVessel(jsonObjectFarmerPD.getString("Vessel"));
                                Invoicelist.setPODTiming(jsonObjectFarmerPD.getString("PODTiming"));
                                Invoicelist.setPortofDischarge(jsonObjectFarmerPD.getString("PortofDischarge"));
                                Invoicelist.setPortofLoading(jsonObjectFarmerPD.getString("PortofLoading"));
                                Invoicelist.setPOLTiming(jsonObjectFarmerPD.getString("POLTiming"));
                                Invoicelist.setToCustomerId(Integer.valueOf(jsonObjectFarmerPD.getString("ToCustomerId")));


                                Invoicelist.setCreatedDate(jsonObjectFarmerPD.getString("CreatedDate"));
                                InvoiceArraylist.add(Invoicelist);
                            }

                            // Update the adapter with the new data
                            NavInvoicelist.addAll(InvoiceArraylist);
//                            if (adapter.getItemCount() == 0) {
////                                imgempty.setVisibility(View.VISIBLE);
//                                recycleNavInvoice.setVisibility(View.GONE);
//                            } else {
//                                imgempty.setVisibility(View.GONE);
//                                recycleNavInvoice.setVisibility(View.VISIBLE);
//                            }
                            adapter.notifyDataSetChanged();

//                            if (adapter.getItemCount() == 0) {
//                                Toast.makeText(FarmerDetailsListActivity.this, "Adapter is empty", Toast.LENGTH_LONG).show();
//                            }


                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Log.d("Error", ">>>>" + ex.toString());
                        }
                    } else {
                        Toast.makeText(CustomerDashboard.this, "no records found", Toast.LENGTH_LONG).show();
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
    public void onClick(View v) {


        try {
            switch (v.getId()) {


                case R.id.C_Logout:

                    appHelper.getSharedPrefObj().edit().remove(DeviceCustomerId).apply();
                    appHelper.getSharedPrefObj().edit().remove(AUTHORIZATION_TOKEN_KEY).apply();
                    appHelper.getSharedPrefObj().edit().remove(DeviceCustomerName).apply();
                    appHelper.getSharedPrefObj().edit().remove(DeviceCustomerAddress).apply();

                    appHelper.getSharedPrefObj().edit().remove(DeviceUserName).apply();
                    appHelper.getSharedPrefObj().edit().remove(DeviceUserPwd).apply();
                    appHelper.getSharedPrefObj().edit().remove(DeviceUserRole).apply();
                    sharedPreferencesData.edit().putBoolean("firstrun", true).commit();
                    Intent Cintent = new Intent(CustomerDashboard.this, LoginActivity.class);
                    startActivity(Cintent);
                    finish();
                    Toast.makeText(CustomerDashboard.this, "log out succesfully", Toast.LENGTH_LONG).show();
                    break;


            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}