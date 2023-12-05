package com.socatra.intellitrack.activity.invoice;

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

import com.socatra.intellitrack.activity.main_dash_board.MainDashBoardActivity;
import com.socatra.intellitrack.adapters.NavInvoiceAdapter;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.models.get.GetInvoiceDetailsByCustomerId;
import com.socatra.intellitrack.models.get.GetInvoicedetails;
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

public class InvoiceListActivity extends BaseActivity {

    private static final String TAG = InvoiceListActivity.class.getCanonicalName();
    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    public AppViewModel viewModel;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    SearchView searchByName;
    String deviceRoleName;
    String Token, strDealerID, strDealerName, strProcessorId, strCustomerId, strLanguageId;

    Button addInvoicebtn;

    ImageView ImgCancel;

    LinearLayout imgempty;


    TextView Invoicetext, Invoicetext1;

    RecyclerView recycleNavInvoice;

    ArrayList<GetInvoicedetails> NavInvoicelist;

    ArrayList<GetInvoiceDetailsByCustomerId> NavInvoicelist1;

    NavInvoiceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_invoice);

        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");
        strProcessorId = appHelper.getSharedPrefObj().getString(DeviceProcessorId, "");
        strCustomerId = appHelper.getSharedPrefObj().getString(DeviceCustomerId, "");
        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");


        deviceRoleName = appHelper.getSharedPrefObj().getString(DeviceUserRole, "");


        initializeUI();
        initializeValues();
        configureDagger();
        configureViewModel();

        if (appHelper.isNetworkAvailable()) {
            getInvoiceList();
        } else {
            Toast.makeText(InvoiceListActivity.this, "please check your internet connection", Toast.LENGTH_LONG).show();
        }
    }

    private void initializeUI() {

        ImgCancel = findViewById(R.id.img_cancel);
        recycleNavInvoice = findViewById(R.id.recycler_nav_invoice);
        searchByName = findViewById(R.id.svNavInvoice);
        Invoicetext = findViewById(R.id.invoice_text);
        Invoicetext1 = findViewById(R.id.invoice_text1);
        imgempty = findViewById(R.id.emptyListImage);

        //language
        Invoicetext1 = findViewById(R.id.invoice_text1);
        addInvoicebtn = findViewById(R.id.add_invoice_btn);


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (appHelper.isNetworkAvailable()) {
           // getLanguageDataList();
        } else {
            Toast.makeText(InvoiceListActivity.this, "please check your internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(InvoiceListActivity.this, MainDashBoardActivity.class); // Replace MainActivity with your actual dashboard activity
        startActivity(intent);
        finish();
    }

    private void initializeValues() {
        strDealerName = appHelper.getSharedPrefObj().getString(DeviceUserName, "");
        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");

        Token = appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, "");

        ImgCancel.setOnClickListener(view -> {
            Intent intent = new Intent(InvoiceListActivity.this, MainDashBoardActivity.class); // Replace MainActivity with your actual dashboard activity
            startActivity(intent);
            finish();
        });


        //getLanguageDataList();


        if (deviceRoleName.equalsIgnoreCase("Dealer")) {
            Invoicetext.setVisibility(View.GONE);
            addInvoicebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(InvoiceListActivity.this, InvoiceDealerActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        } else if (deviceRoleName.equalsIgnoreCase("Processor")) {
            Invoicetext.setVisibility(View.GONE);
            addInvoicebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(InvoiceListActivity.this, InvoiceManufactureActivity.class);
                    startActivity(intent);
                    finish();
                    //finish();
                }
            });

        } else {
            addInvoicebtn.setVisibility(View.GONE);
            Invoicetext.setVisibility(View.VISIBLE);
            Invoicetext1.setVisibility(View.GONE);
        }


        if (searchByName != null) {

            SearchManager searchManager = (SearchManager) InvoiceListActivity.this.getSystemService(Context.SEARCH_SERVICE);
            searchByName.setSearchableInfo(searchManager
                    .getSearchableInfo(InvoiceListActivity.this.getComponentName()));
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


        NavInvoicelist = new ArrayList<>();
        recycleNavInvoice.setLayoutManager(new LinearLayoutManager(this));





    }

    private void configureDagger() {
        AndroidInjection.inject(InvoiceListActivity.this);
    }

    public void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
    }

    private void getInvoiceList() {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);

        final ProgressDialog progressDialog = new ProgressDialog(InvoiceListActivity.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading  data...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        Call<JsonElement> callRetrofit = null;

        if (deviceRoleName.equalsIgnoreCase("Processor")) {
            callRetrofit = service.getInvoiceDetailsByProcessorIdFromServer(
                    strProcessorId,
                    appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, "")
            );
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
                                ArrayList<GetInvoicedetails> InvoiceArraylist = new ArrayList<>();
                                NavInvoicelist.clear();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                    GetInvoicedetails Invoicelist = new GetInvoicedetails();
                                    Invoicelist.setId(jsonObjectFarmerPD.getString("Id"));
                                    Invoicelist.setInvoiceNumber(jsonObjectFarmerPD.getString("InvoiceNumber"));
                                    Invoicelist.setProduct(jsonObjectFarmerPD.getString("Product"));
//                                Invoicelist.setDealerName(jsonObjectFarmerPD.getString("DealerName"));
                                    Invoicelist.setQuantity(Integer.valueOf(jsonObjectFarmerPD.getString("Quantity")));
                                    Invoicelist.setManufacturerName(jsonObjectFarmerPD.getString("ManufacturerName"));
                                    Invoicelist.setShippingAddress(jsonObjectFarmerPD.getString("ShippingAddress"));
                                    Invoicelist.setShippingToName(jsonObjectFarmerPD.getString("ShippingToName"));

                                    Invoicelist.setVessel(jsonObjectFarmerPD.getString("Vessel"));
                                    Invoicelist.setPODTiming(jsonObjectFarmerPD.getString("PODTiming"));
                                    Invoicelist.setPortofDischarge(jsonObjectFarmerPD.getString("PortofDischarge"));
                                    Invoicelist.setPortofLoading(jsonObjectFarmerPD.getString("PortofLoading"));
                                    Invoicelist.setPOLTiming(jsonObjectFarmerPD.getString("POLTiming"));
                                    Invoicelist.setFromProcessorId(jsonObjectFarmerPD.getString("FromProcessorId"));
                                    Invoicelist.setToCustomerId(jsonObjectFarmerPD.getString("ToCustomerId"));


                                    Invoicelist.setCreatedDate(jsonObjectFarmerPD.getString("CreatedDate"));
                                    InvoiceArraylist.add(Invoicelist);
                                }

                                NavInvoicelist.addAll(InvoiceArraylist);
                                adapter = new NavInvoiceAdapter(appHelper, viewModel, (List<GetInvoicedetails>) NavInvoicelist, strDealerID);
                                if (adapter.getItemCount() == 0) {
                                    imgempty.setVisibility(View.VISIBLE);
                                    recycleNavInvoice.setVisibility(View.GONE);
                                } else {
                                    imgempty.setVisibility(View.GONE);
                                    recycleNavInvoice.setVisibility(View.VISIBLE);
                                }
                                recycleNavInvoice.hasFixedSize();
                                recycleNavInvoice.setAdapter(adapter);
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

                            Toast.makeText(InvoiceListActivity.this, "no records found", Toast.LENGTH_LONG).show();
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


            callRetrofit = service.getInvoiceDetailsByDealerIdFromServer(
                    strDealerID,
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
                                ArrayList<GetInvoicedetails> InvoiceArraylist = new ArrayList<>();
                                NavInvoicelist.clear();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                    GetInvoicedetails Invoicelist = new GetInvoicedetails();
                                    Invoicelist.setId(jsonObjectFarmerPD.getString("Id"));
                                    Invoicelist.setInvoiceNumber(jsonObjectFarmerPD.getString("InvoiceNumber"));
                                    Invoicelist.setProduct(jsonObjectFarmerPD.getString("Product"));

                                    Invoicelist.setQuantity(Integer.valueOf(jsonObjectFarmerPD.getString("Quantity")));
//                                    Invoicelist.setManufacturerName(jsonObjectFarmerPD.getString("ManufacturerName"));
                                    Invoicelist.setShippingAddress(jsonObjectFarmerPD.getString("ShippingAddress"));
                                    Invoicelist.setShippingToName(jsonObjectFarmerPD.getString("ShippingToName"));
                                    Invoicelist.setCreatedDate(jsonObjectFarmerPD.getString("CreatedDate"));
                                    InvoiceArraylist.add(Invoicelist);
                                }

                                // Update the adapter with the new data
                                NavInvoicelist.addAll(InvoiceArraylist);
                                adapter = new NavInvoiceAdapter(appHelper, viewModel, (List<GetInvoicedetails>) NavInvoicelist, strDealerID);
                                if (adapter.getItemCount() == 0) {
                                    imgempty.setVisibility(View.VISIBLE);
                                    recycleNavInvoice.setVisibility(View.GONE);
                                } else {
                                    imgempty.setVisibility(View.GONE);
                                    recycleNavInvoice.setVisibility(View.VISIBLE);
                                }
                                recycleNavInvoice.hasFixedSize();
                                recycleNavInvoice.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

//                            if (adapter.getItemCount() == 0) {
//                                Toast.makeText(FarmerDetailsListActivity.this, "Adapter is empty", Toast.LENGTH_LONG).show();
//                            }


                            } catch (Exception ex) {
                                progressDialog.dismiss();
                                Toast.makeText(InvoiceListActivity.this, "no records found", Toast.LENGTH_LONG).show();

                                ex.printStackTrace();
                                Log.d("Error", ">>>>" + ex.toString());
                            }
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(InvoiceListActivity.this, "no records found", Toast.LENGTH_LONG).show();
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

    private void getLanguageDataList() {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        final ProgressDialog progressDialog = new ProgressDialog(InvoiceListActivity.this, R.style.AppCompatAlertDialogStyle);
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
                                    if (getString(R.string.add_invoice).equals(jsonEngWord)) {
                                        addInvoicebtn.setText(jsonEngWord1);
                                    } else if (getString(R.string.invoice_details).equals(jsonEngWord)) {
                                        Invoicetext1.setText(jsonEngWord1);
                                    }
                                } else {

                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");

                                    if (getString(R.string.add_invoice).equals(jsonEngWord)) {
                                        addInvoicebtn.setText(strConvertedWord);
                                    } else if (getString(R.string.invoice_details).equals(jsonEngWord)) {
                                        Invoicetext1.setText(strConvertedWord);
                                    }


                                }
                                /*else if (strLanguageId.equals("3")) {
                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
                                    if ("ADD INVOICE".equals(jsonEngWord)) {
                                        addInvoicebtn.setText(strConvertedWord);
                                    } else if ("Invoice Details".equals(jsonEngWord)) {
                                        Invoicetext1.setText(strConvertedWord);
                                    }

                                } else if (strLanguageId.equals("4")) {
                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
                                    if ("ADD INVOICE".equals(jsonEngWord)) {
                                        addInvoicebtn.setText(strConvertedWord);
                                    } else if ("Invoice Details".equals(jsonEngWord)) {
                                        Invoicetext1.setText(strConvertedWord);
                                    }



                                }else if (strLanguageId.equals("5")) {
                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
                                    if ("ADD INVOICE".equals(jsonEngWord)) {
                                        addInvoicebtn.setText(strConvertedWord);
                                    } else if ("Invoice Details".equals(jsonEngWord)) {
                                        Invoicetext1.setText(strConvertedWord);
                                    }



                                }  else{



                                }
*/

                            }


                        } catch (Exception ex) {
                            ex.printStackTrace();
                            progressDialog.dismiss();
                            Log.d("Error", ">>>>" + ex.toString());
                        }
                    } else {
                        progressDialog.dismiss();

                        Toast.makeText(InvoiceListActivity.this, "no records found", Toast.LENGTH_LONG).show();
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