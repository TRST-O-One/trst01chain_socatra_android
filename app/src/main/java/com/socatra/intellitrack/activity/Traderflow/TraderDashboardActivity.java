package com.socatra.intellitrack.activity.Traderflow;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.App_PackageName;
import static com.socatra.intellitrack.constants.AppConstant.DeviceCustomerId;
import static com.socatra.intellitrack.constants.AppConstant.DeviceTraderId;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserName;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserPwd;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserRole;

import android.app.Dialog;
import android.app.ProgressDialog;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.socatra.intellitrack.BaseActivity;
import com.socatra.intellitrack.R;
import com.socatra.intellitrack.activity.LoginActivity;
import com.socatra.intellitrack.activity.grnflow.DealerAddGrnActivity;
import com.socatra.intellitrack.activity.main_dash_board.DashBoardDealerListActivity;
import com.socatra.intellitrack.adapters.CustomerInvoiceListAdapter;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.models.get.GetDashboardDataByTraderId;
import com.socatra.intellitrack.models.get.GetInvoiceDetailsByCustomerId;
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

public class TraderDashboardActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = DealerAddGrnActivity.class.getCanonicalName();
    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    public AppViewModel viewModel;


    LinearLayout llAddGRN, llBatchProcessing, llInvoice, llDrc, llQualityCheck, llLogOut;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    String deviceRoleName;
    String strDealerName;
    String strDealerID;
    String strProcessorId;

    String strTraderId;

    Dialog dialog;
    TextView txtUserName, txtRoleName,CustomerName,CustomerRole;

    LinearLayout  Customerlayout;

    LinearLayout plotsNavLayout;
    SharedPreferences sharedPreferencesData;
    private DrawerLayout drawerLayout;
    private ImageButton CustomerButton;


     TextView TraderProcessor, TraderDealer,TraderInvoiceCount,TraderPlots,TraderArea , FarmerCount;

     LinearLayout processortraderlayout, DealertraderLayout,Invoicetraderlayout,Traderplotalayout,TraderCustomerLayout,TraderArealayout;

    //Customer Login
    RecyclerView recycleCustomerInvoice;

    ArrayList<GetInvoiceDetailsByCustomerId> NavInvoicelist;

    CustomerInvoiceListAdapter adapter;

    SearchView searchByName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trader_dashboard);
        deviceRoleName = appHelper.getSharedPrefObj().getString(DeviceUserRole, "");
        strDealerName = appHelper.getSharedPrefObj().getString(DeviceUserName, "");

        strTraderId =appHelper.getSharedPrefObj().getString(DeviceTraderId,"");

        sharedPreferencesData = getSharedPreferences(App_PackageName, MODE_PRIVATE);
        initializeUI();
        initializeValues();
        configureDagger();
        configureViewModel();

        if (appHelper.isNetworkAvailable())
        {
            getMainDashBoardData();
        }else {
            Toast.makeText(TraderDashboardActivity.this,"please check your internet connection",Toast.LENGTH_LONG).show();
        }


    }


    private void initializeUI() {

        CustomerButton = findViewById(R.id.C_Logout);
        txtUserName = findViewById(R.id.txtName);
        txtRoleName = findViewById(R.id.txtRole);

        //Customer Layout
        Customerlayout = findViewById(R.id.ll_Customer_layout);
        CustomerName = findViewById(R.id.txtCustomerName);
        CustomerRole = findViewById(R.id.txtCustomerRole);

        TraderProcessor = findViewById(R.id.txt_processorcount_trader);
        TraderDealer = findViewById(R.id.txt_dealers_trader);
        TraderInvoiceCount = findViewById(R.id.txt_invoice_trader);
        TraderPlots = findViewById(R.id.txt_plots_trader);
        TraderCustomerLayout = findViewById(R.id.ll_customers);
        TraderArea = findViewById(R.id.txt_totalarea_trader);
        FarmerCount = findViewById(R.id.txtFarmerCount);


        //Dashboard Layout
        processortraderlayout = findViewById(R.id.ll_Processor_trader);
        DealertraderLayout = findViewById(R.id.ll_Dealers_trader);
        Invoicetraderlayout = findViewById(R.id.ll_Invoice_trader);
        Traderplotalayout = findViewById(R.id.ll_plots_trader);
        TraderArealayout = findViewById(R.id.ll_Area_trader);









        CustomerName.setText(strDealerName);
        CustomerRole.setText(deviceRoleName);



    }

    private void initializeValues() {

        CustomerButton.setOnClickListener(this);
        processortraderlayout.setOnClickListener(this);
        DealertraderLayout.setOnClickListener(this);
        Invoicetraderlayout.setOnClickListener(this);
        Traderplotalayout.setOnClickListener(this);
        TraderCustomerLayout.setOnClickListener(this);
        TraderArealayout.setOnClickListener(this);

















    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    public void configureViewModel() {
        //  viewModel = ViewModelProviders.of(this, viewModelFactory).get(AppViewModel.class);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);


    }

    @Override
    public void onBackPressed() {

        showExitDialog();

    }

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to exit the app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked Yes button
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked No button
                        dialog.dismiss();
                    }
                });


        builder.create().show();
    }







    private void getMainDashBoardData() {
        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        final ProgressDialog progressDialog = new ProgressDialog(TraderDashboardActivity

                .this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading data...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
            Call<JsonElement> callRetrofit = null;
            callRetrofit = service.getDashboardDataByTraderIdFromServer(strTraderId, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));
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
                                ArrayList<GetDashboardDataByTraderId> DashboardList = new ArrayList<>();


                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                    GetDashboardDataByTraderId TraderDashboard = new GetDashboardDataByTraderId();
                                    TraderDashboard.setNoOfFarmers(Integer.valueOf(jsonObjectFarmerPD.getString("NoOfFarmers")));
                                    TraderDashboard.setTotalArea(Double.valueOf((jsonObjectFarmerPD.getString("TotalArea"))));
                                    TraderDashboard.setNoOfPlantations(Integer.valueOf(jsonObjectFarmerPD.getString("NoOfPlantations")));
                                    TraderDashboard.setNoOfDealers(Integer.valueOf((jsonObjectFarmerPD.optString("NoOfDealers"))));
                                    TraderDashboard.setNoOfProcessors(Integer.valueOf(jsonObjectFarmerPD.getString("NoOfProcessors")));


                                    DashboardList.add(TraderDashboard);
                                }

                                if (DashboardList.size() > 0) {
                                    int Farmers = DashboardList.get(0).getNoOfFarmers();
                                    int Plantations = DashboardList.get(0).getNoOfPlantations();
                                    double TotalArea = DashboardList.get(0).getTotalArea();
                                    int Processor = DashboardList.get(0).getNoOfProcessors();
                                    int Dealers = DashboardList.get(0).getNoOfDealers();



                                    TraderProcessor.setText(String.valueOf(Processor));
                                    TraderDealer.setText(String.valueOf(Dealers));
//                                    TraderInvoiceCount.setText(String.valueOf(TotalArea));
                                    TraderPlots.setText(String.valueOf(Plantations));
                                    TraderArea.setText(String.valueOf(TotalArea));
                                    FarmerCount.setText(String.valueOf(Farmers));



                                }

                            } catch (Exception ex) {
                                progressDialog.dismiss();
                                ex.printStackTrace();
                                Log.d("Error", ">>>>" + ex.toString());
                            }
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(TraderDashboardActivity.this, "no records found", Toast.LENGTH_LONG).show();
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
    public void onClick(View v) {


        try {
            switch (v.getId()) {



                case R.id.ll_Processor_trader:
                  processorTraderList();
                    break;


                case R.id.ll_Dealers_trader:
                    DealerTraderList();
                    break;


                case R.id.ll_Invoice_trader:
                   InvoiceTraderList();
                    break;

                case R.id.ll_customers:
                   CustomerTraderList();
                    break;

                case R.id.ll_plots_trader:
                    TraderPlotsList();
                    break;

                case R.id.ll_Area_trader:
                    TraderAreaList();
                    break;


                case R.id.C_Logout:

                    appHelper.getSharedPrefObj().edit().remove(DeviceCustomerId).apply();
                    appHelper.getSharedPrefObj().edit().remove(AUTHORIZATION_TOKEN_KEY).apply();
                    appHelper.getSharedPrefObj().edit().remove(DeviceUserName).apply();
                    appHelper.getSharedPrefObj().edit().remove(DeviceUserPwd).apply();
                    appHelper.getSharedPrefObj().edit().remove(DeviceUserRole).apply();
                    sharedPreferencesData.edit().putBoolean("firstrun", true).commit();
                    Intent Cintent = new Intent(TraderDashboardActivity.this, LoginActivity.class);
                    startActivity(Cintent);
                    finish();
                    Toast.makeText(TraderDashboardActivity.this, "log out succesfully", Toast.LENGTH_LONG).show();
                    break;




            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    private void processorTraderList() {
        Intent intent = new Intent(TraderDashboardActivity.this, TraderProcessorDetails.class);
        startActivity(intent);
        // overridePendingTransition(R.anim.rotate_out, R.anim.rotate_in);
    }

    private void DealerTraderList() {
        Intent intent = new Intent(TraderDashboardActivity.this, TraderDetailsListAct.class);
        startActivity(intent);
        // overridePendingTransition(R.anim.rotate_out, R.anim.rotate_in);
    }


    private void InvoiceTraderList() {
        Intent intent = new Intent(TraderDashboardActivity.this, DashBoardDealerListActivity.class);
        startActivity(intent);
        // overridePendingTransition(R.anim.rotate_out, R.anim.rotate_in);

    }


    private void TraderPlotsList() {
        Intent intent = new Intent(TraderDashboardActivity.this, PlotListbyTraderId.class);
        startActivity(intent);
        //   overridePendingTransition(R.anim.rotate_out, R.anim.rotate_in);

    }

    private void CustomerTraderList() {
        Intent intent = new Intent(TraderDashboardActivity.this, TraderCustListAct.class);
        startActivity(intent);
    }



    private void TraderAreaList() {
//        Intent intent = new Intent(TraderDashboardActivity.this, SubDealerListActivity.class);
//        startActivity(intent);
        Toast.makeText(TraderDashboardActivity.this,"In-progress will update soon",Toast.LENGTH_LONG).show();
        // overridePendingTransition(R.anim.rotate_out, R.anim.rotate_in);

    }
}