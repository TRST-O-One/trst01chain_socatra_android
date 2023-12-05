package com.socatra.intellitrack.activity.main_dash_board;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.App_PackageName;
import static com.socatra.intellitrack.constants.AppConstant.DeviceCustomerId;
import static com.socatra.intellitrack.constants.AppConstant.DeviceDealerID;
import static com.socatra.intellitrack.constants.AppConstant.DeviceDealerName;
import static com.socatra.intellitrack.constants.AppConstant.DeviceProcessorId;
import static com.socatra.intellitrack.constants.AppConstant.DeviceProcessorName;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserName;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserPwd;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserRole;
import static com.socatra.intellitrack.constants.AppConstant.LanguageId;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonElement;
import com.socatra.intellitrack.BaseActivity;
import com.socatra.intellitrack.R;
import com.socatra.intellitrack.activity.customerflow.DashBoardCustomerListAct;
import com.socatra.intellitrack.activity.LoginActivity;

import com.socatra.intellitrack.activity.batchProcessing.DealerBatchCreationAct;
import com.socatra.intellitrack.activity.batchProcessing.ProcessorBatchCreationAct;
import com.socatra.intellitrack.activity.drc.Drclist;
import com.socatra.intellitrack.activity.grnflow.DealerAddGrnActivity;
import com.socatra.intellitrack.activity.grnflow.NavGrnActivity;

import com.socatra.intellitrack.activity.invoice.InvoiceListActivity;
import com.socatra.intellitrack.activity.qualitycheck.QualityCheckList;
import com.socatra.intellitrack.adapters.HeaderLanguageAdapter;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.database.entity.GetProcessorDataFromServer;
import com.socatra.intellitrack.helper.AppHelper;
import com.socatra.intellitrack.helper.PreferenceUtils;
import com.socatra.intellitrack.models.get.GetFarmerAnalysisbyDealerId;
import com.socatra.intellitrack.models.get.GetFarmerAnalysisbyProcessorId;
import com.socatra.intellitrack.models.get.GetLanguageHeaderList;
import com.socatra.intellitrack.view_models.AppViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainDashBoardActivity extends BaseActivity implements View.OnClickListener, HeaderLanguageAdapter.SyncCallbackInterface {
    private static final String TAG = DealerAddGrnActivity.class.getCanonicalName();
    public static LinearLayout llAddGRN, llBatchProcessing, llInvoice, llDrc, llQualityCheck, llLogOut;
    public static String deviceRoleName, strDealerName, strProcessorName, strDealerID, strProcessorId;
    public static TextView txtUserName, txtRoleName, txtActivities, txtGRN, txtDRC, txtBatchProcessing, txtQualityCheck, txtInvoice, txtChangelanguagr, txtLogout, txtAppversion, CustomerName, CustomerRole, txtFarmerCount, txtPlots, txtDealerCount, txtArea, txtSupply, txtAppVersion;
    public static View ViewDrc, ViewQualityCheck, ViewGRN, ViewBatchProcessing;
    public static LinearLayout farmerDealerLayout, plotsDealerLayout, plotsProcessorLayout, farmerProcessorLayout, DealerLayout, ProcessorLayout, Dealerllayout, SubDealerlayout, Customerlayout, TotalSupplyDealerLayout, TotalSupplyProcessorLayout, TotalProcurementDealerLayout, TotalProcurementProcessorLayout, Customerprocessorlayout, DealerProcessorlayout;
    // public static String strWordTitleName, strWordRoleName, strWordAppVersion;
    public static String strLanguageId;
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    public AppViewModel viewModel;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    Dialog dialog;
    TextView txtFarmerDealer, txtSubdealer, txtPlotsDealer, txtAreaDealer, txtProcurementDealer, txtMainDealer, txtProcessorDealer, txtSupplyDealer;
    TextView txtDealer, txtFarmerProcessor, txtPlotsProcessor, txtAreaProcessor, txtProcurementProcessor, txtCustomers, txtSupplyProcessor;
    LinearLayout plotsNavLayout;
    SharedPreferences sharedPreferencesData;
    LinearLayout llNavBtn;
    LinearLayout llLangChange;
    Dialog changeLanguageDialogue;
    ArrayList<GetLanguageHeaderList> getLanguageHeaderLists;
    HeaderLanguageAdapter headerLanguageAdapter;
    TextView txtWordFarmerProcessor, txtWordDealerProcessor, txtWordPlotsProcessor, txtWordtotalprocurementProcessor, txtWordtotalareaProcessor, txtWordCustomersProcessor, txtWordtotalsupplyProcessor;
    String strConvertedWord;
    TextView txtWordHeaderPRCProcument, txtWordHeadrSupplyChainDTLPrc, txtWordTonsPrc, txtWordHeactersPRc, txtWordTonsPrcSupply;
    TextView txtWordHeaderDealerDashProcument,
            txtWordHeadrSupplyChainDTLDealer, txtWordTonsDealer,
            txtWordHeactersDealer, txtWordTonsDealerSupply;
    TextView txtWordFarmerDealerDash,
            txtWordSubDealerDashDealer, txtWordDealerDashPlots,
            txtWordDealerDashTotalProcurement, txtWordTotalAreaDashDealer, txtMainDealerDashWord,
            txtWordProceesorDealerDash, txtWordSupplyDealerDash, txtWordUName, txtWordURole, txtWordAppVersion;
    String languageToLoad = "";
    BottomSheetDialog mBottomSheetDialog;
    String strSelectLanguage;
    private DrawerLayout drawerLayout;
    private ImageButton SnavButton;

    public static void setBindData(AppHelper appHelper) {
        deviceRoleName = appHelper.getSharedPrefObj().getString(DeviceUserRole, "");
        strDealerName = appHelper.getSharedPrefObj().getString(DeviceDealerName, "");
        strProcessorName = appHelper.getSharedPrefObj().getString(DeviceProcessorName, "");


        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");
        strProcessorId = appHelper.getSharedPrefObj().getString(DeviceProcessorId, "");
        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");

        if (deviceRoleName.equals("Dealer")) {
            llDrc.setVisibility(View.GONE);
            ViewDrc.setVisibility(View.GONE);
            llQualityCheck.setVisibility(View.GONE);
            ViewQualityCheck.setVisibility(View.GONE);
            DealerLayout.setVisibility(View.VISIBLE);
            ProcessorLayout.setVisibility(View.GONE);
//            txtUserName.setText(strWordTitleName + " : " + strDealerName);
//            txtRoleName.setText(strWordRoleName + " : " + deviceRoleName);
            txtUserName.setText(strDealerName);
            txtRoleName.setText(deviceRoleName);
        } else {
            llDrc.setVisibility(View.VISIBLE);
            ViewDrc.setVisibility(View.VISIBLE);
            llQualityCheck.setVisibility(View.VISIBLE);
            ViewQualityCheck.setVisibility(View.VISIBLE);
            DealerLayout.setVisibility(View.GONE);
            ProcessorLayout.setVisibility(View.VISIBLE);
//            txtUserName.setText(strWordTitleName + " : " + strProcessorName);
//            txtRoleName.setText(strWordRoleName + " : " + deviceRoleName);
            txtUserName.setText(strProcessorName);
            txtRoleName.setText(deviceRoleName);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_dashboard_home);
        sharedPreferencesData = getSharedPreferences(App_PackageName, MODE_PRIVATE);

        sharedPreferencesData = PreferenceManager.getDefaultSharedPreferences(this);


        deviceRoleName = appHelper.getSharedPrefObj().getString(DeviceUserRole, "");
        strDealerName = appHelper.getSharedPrefObj().getString(DeviceDealerName, "");
        strProcessorName = appHelper.getSharedPrefObj().getString(DeviceProcessorName, "");


        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");
        strProcessorId = appHelper.getSharedPrefObj().getString(DeviceProcessorId, "");
        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");

//        strWordTitleName = getResources().getString(R.string.name);
//        strWordRoleName = getResources().getString(R.string.role);
        initializeUI();
        initializeValues();
        configureDagger();
        configureViewModel();
        setBindData(appHelper);
        if (preferenceUtils.getStringFromPreference(PreferenceUtils.Language, "").equalsIgnoreCase("in")) {
            txtChangelanguagr.setText(getResources().getString(R.string.indonesia));
        }else if (preferenceUtils.getStringFromPreference(PreferenceUtils.Language, "").equalsIgnoreCase("fr")) {
            txtChangelanguagr.setText(getResources().getString(R.string.french));
        } else if (preferenceUtils.getStringFromPreference(PreferenceUtils.Language, "").equalsIgnoreCase("zh")) {
            txtChangelanguagr.setText(getResources().getString(R.string.chines));
        }  else {
            txtChangelanguagr.setText(getResources().getString(R.string.english));
        }

//        if (deviceRoleName.equals("Dealer")) {
//            llDrc.setVisibility(View.GONE);
//            ViewDrc.setVisibility(View.GONE);
//            llQualityCheck.setVisibility(View.GONE);
//            ViewQualityCheck.setVisibility(View.GONE);
//            DealerLayout.setVisibility(View.VISIBLE);
//            ProcessorLayout.setVisibility(View.GONE);
//            txtUserName.setText(strWordTitleName + " : " + strDealerName);
//            txtRoleName.setText(strWordRoleName + " : " + deviceRoleName);
//        } else {
//            llDrc.setVisibility(View.VISIBLE);
//            ViewDrc.setVisibility(View.VISIBLE);
//            llQualityCheck.setVisibility(View.VISIBLE);
//            ViewQualityCheck.setVisibility(View.VISIBLE);
//            DealerLayout.setVisibility(View.GONE);
//            ProcessorLayout.setVisibility(View.VISIBLE);
//            txtUserName.setText(strWordTitleName + " : " + strProcessorName);
//            txtRoleName.setText(strWordRoleName + " : " + deviceRoleName);
//        }

    }

    private String getSelectedLanguage() {
        return sharedPreferencesData.getString("selected_language", "English");
    }


    private void showLAngPopUp() {
        mBottomSheetDialog = new BottomSheetDialog(MainDashBoardActivity.this);
        View dialogView = LayoutInflater.from(MainDashBoardActivity.this).inflate(R.layout.language_dialog, null);
        mBottomSheetDialog.setContentView(dialogView);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.show();
        TextView txt_english = dialogView.findViewById(R.id.txt_english);
        TextView txt_indonesia = dialogView.findViewById(R.id.txt_indonesia_word);
        TextView txt_french = dialogView.findViewById(R.id.txt_french_word);
        TextView txt_chines = dialogView.findViewById(R.id.txt_chines_word);

        txt_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageToLoad = "en"; // your language
                strSelectLanguage = languageToLoad;
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
//                getBaseContext().getResources().updateConfiguration(config,
//                        getBaseContext().getResources().getDisplayMetrics());
                getApplicationContext().getResources().updateConfiguration(config, getResources().getDisplayMetrics());

                preferenceUtils.setLanguage("en");
                preferenceUtils.saveString(PreferenceUtils.Language, "en");
                mBottomSheetDialog.dismiss();
                txtChangelanguagr.setText(getResources().getString(R.string.english));
                final ProgressDialog progressDialog = new ProgressDialog(MainDashBoardActivity.this, R.style.AppCompatAlertDialogStyle);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("please wait...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                Log.d("LanguageChange", "Locale: " + Locale.getDefault().toString());
                Log.d("LanguageChange", "Configuration: " + getResources().getConfiguration().locale.toString());

                //  onConfigurationChanged(config);
                //restartActivity();
//                Intent intent = new Intent(MainDashBoardActivity.this, MainDashBoardActivity.class);
//               // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
                recreate();
                changLang(progressDialog);
                // change_lang("en");
            }
        });

        txt_indonesia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageToLoad = "in"; // your language
                strSelectLanguage = languageToLoad;
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getApplicationContext().getResources().updateConfiguration(config, getResources().getDisplayMetrics());

//                getBaseContext().getResources().updateConfiguration(config,
//                        getBaseContext().getResources().getDisplayMetrics());
                preferenceUtils.setLanguage("in");
                preferenceUtils.saveString(PreferenceUtils.Language, "in");
                mBottomSheetDialog.dismiss();
                strSelectLanguage = "in";
                txtChangelanguagr.setText(getResources().getString(R.string.indonesia));
                final ProgressDialog progressDialog = new ProgressDialog(MainDashBoardActivity.this, R.style.AppCompatAlertDialogStyle);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("please wait...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                changLang(progressDialog);
                //  onConfigurationChanged(config);
//                Intent intent = new Intent(MainDashBoardActivity.this, MainDashBoardActivity.class);
//                // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
                //recreate();
            }
        });
        txt_french.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageToLoad = "fr"; // your language
                strSelectLanguage = languageToLoad;
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getApplicationContext().getResources().updateConfiguration(config, getResources().getDisplayMetrics());

//                getBaseContext().getResources().updateConfiguration(config,
//                        getBaseContext().getResources().getDisplayMetrics());
                preferenceUtils.setLanguage("fr");
                preferenceUtils.saveString(PreferenceUtils.Language, "fr");
                mBottomSheetDialog.dismiss();
                strSelectLanguage = "fr";
                txtChangelanguagr.setText(getResources().getString(R.string.indonesia));
                final ProgressDialog progressDialog = new ProgressDialog(MainDashBoardActivity.this, R.style.AppCompatAlertDialogStyle);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("please wait...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                changLang(progressDialog);
                //  onConfigurationChanged(config);
//                Intent intent = new Intent(MainDashBoardActivity.this, MainDashBoardActivity.class);
//                // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
                //recreate();
            }
        });
        txt_chines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageToLoad = "zh"; // your language
                strSelectLanguage = languageToLoad;
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getApplicationContext().getResources().updateConfiguration(config, getResources().getDisplayMetrics());

//                getBaseContext().getResources().updateConfiguration(config,
//                        getBaseContext().getResources().getDisplayMetrics());
                preferenceUtils.setLanguage("zh");
                preferenceUtils.saveString(PreferenceUtils.Language, "zh");
                mBottomSheetDialog.dismiss();
                strSelectLanguage = "zh";
                txtChangelanguagr.setText(getResources().getString(R.string.indonesia));
                final ProgressDialog progressDialog = new ProgressDialog(MainDashBoardActivity.this, R.style.AppCompatAlertDialogStyle);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("please wait...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                changLang(progressDialog);
                //  onConfigurationChanged(config);
//                Intent intent = new Intent(MainDashBoardActivity.this, MainDashBoardActivity.class);
//                // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
                //recreate();
            }
        });
    }


    public void changLang(ProgressDialog progressDialog) {
        if (strSelectLanguage.equalsIgnoreCase("en")) {
            String languageToLoad = "en"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            preferenceUtils.setLanguage("en");
            progressDialog.dismiss();
            Intent intent = new Intent(MainDashBoardActivity.this, MainDashBoardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else if (strSelectLanguage.equalsIgnoreCase("in")) {
            String languageToLoad = "in"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            preferenceUtils.setLanguage("in");
            progressDialog.dismiss();
            Intent intent = new Intent(MainDashBoardActivity.this, MainDashBoardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP );
            startActivity(intent);
            finish();
        } else if (strSelectLanguage.equalsIgnoreCase("fr")) {
            String languageToLoad = "fr"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            preferenceUtils.setLanguage("fr");
            progressDialog.dismiss();
            Intent intent = new Intent(MainDashBoardActivity.this, MainDashBoardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP );
            startActivity(intent);
            finish();
        } else if (strSelectLanguage.equalsIgnoreCase("zh")) {
            String languageToLoad = "zh"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            preferenceUtils.setLanguage("zh");
            progressDialog.dismiss();
            Intent intent = new Intent(MainDashBoardActivity.this, MainDashBoardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP );
            startActivity(intent);
            finish();
        } else {
            String languageToLoad = "en"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            preferenceUtils.setLanguage("en");
            progressDialog.dismiss();
            Intent intent = new Intent(MainDashBoardActivity.this, MainDashBoardActivity.class);
           // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP );
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // refresh your views here
        super.onConfigurationChanged(newConfig);
    }


    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void initializeUI() {
        getLanguageHeaderLists = new ArrayList<>();

        drawerLayout = findViewById(R.id.drawerLayout);
        SnavButton = findViewById(R.id.SnavButton);
        llAddGRN = findViewById(R.id.ll_grn);
        llLogOut = findViewById(R.id.ll_log_out);
        llBatchProcessing = findViewById(R.id.ll_batch_processing);
        llInvoice = findViewById(R.id.ll_invoice_main_nav);
        llDrc = findViewById(R.id.ll_drc);
        llQualityCheck = findViewById(R.id.ll_Quality_check);
        ViewDrc = findViewById(R.id.View_Drc);
        ViewQualityCheck = findViewById(R.id.View_QualityCheck);
        ViewGRN = findViewById(R.id.View_GRN);
        ViewBatchProcessing = findViewById(R.id.View_BatchProcessing);

        //Language
        txtUserName = findViewById(R.id.txtName);
        txtRoleName = findViewById(R.id.txtRole);
        txtActivities = findViewById(R.id.txtactivities);
        txtGRN = findViewById(R.id.txtGrn);
        txtDRC = findViewById(R.id.txtDrc);
        txtBatchProcessing = findViewById(R.id.txtbatchprocess);
        txtQualityCheck = findViewById(R.id.txtqualitycheck);
        txtInvoice = findViewById(R.id.txtinvoice);
        txtChangelanguagr = findViewById(R.id.txtchangelanguage);
        txtLogout = findViewById(R.id.txtlogout);
        txtAppversion = findViewById(R.id.txtAppVersion);

        llLangChange = findViewById(R.id.ll_change_lang);
        farmerDealerLayout = findViewById(R.id.ll_farmer_dealer);
        plotsDealerLayout = findViewById(R.id.ll_plots_dealer);
        TotalSupplyDealerLayout = findViewById(R.id.ll_total_supply_dealer);
        TotalProcurementDealerLayout = findViewById(R.id.ll_total_procurement_dealer);

        farmerProcessorLayout = findViewById(R.id.ll_farmer_processor);
        plotsProcessorLayout = findViewById(R.id.ll_plots_processor);
        TotalSupplyProcessorLayout = findViewById(R.id.ll_total_supply_processor);
        TotalProcurementProcessorLayout = findViewById(R.id.ll_total_procurement_processor);

        txtWordHeaderPRCProcument = findViewById(R.id.txt_word_prc_procument_header);
        txtWordHeadrSupplyChainDTLPrc = findViewById(R.id.txt_word_dash_prc_supply);

        //Dealer Layout
        DealerLayout = findViewById(R.id.ll_Dealer_layout);
        SubDealerlayout = findViewById(R.id.ll_Sub_Dealers);
        DealerProcessorlayout = findViewById(R.id.ll_processor_dealer);
        txtFarmerDealer = findViewById(R.id.txt_farmercount_dealer);
        txtSubdealer = findViewById(R.id.txt_sub_dealers_dealer);
        txtPlotsDealer = findViewById(R.id.txt_plots_dealer);
        txtAreaDealer = findViewById(R.id.txt_area_dealer);
        txtProcurementDealer = findViewById(R.id.txt_procurement_count_dealer);
        txtMainDealer = findViewById(R.id.txt_maindealercount_dealer);
        txtProcessorDealer = findViewById(R.id.txt_processor_dealer);
        txtSupplyDealer = findViewById(R.id.txt_supply_count_dealer);


        //Processor Layout
        ProcessorLayout = findViewById(R.id.ll_Processor_layout);
        txtFarmerProcessor = findViewById(R.id.txt_farmer_processor);
        txtDealer = findViewById(R.id.txt_dealer_count_processor);
        txtPlotsProcessor = findViewById(R.id.txt_plots_processor);
        txtAreaProcessor = findViewById(R.id.txt_area_processor);
        txtProcurementProcessor = findViewById(R.id.txt_procurementcount_processor);
        txtCustomers = findViewById(R.id.txt_customers_processor);
        txtSupplyProcessor = findViewById(R.id.txt_totalSupply_processor);
        Dealerllayout = findViewById(R.id.ll_dealer);
        Customerprocessorlayout = findViewById(R.id.ll_Customers);

        //Language words
        txtWordFarmerProcessor = findViewById(R.id.txt_farmer_word_processor);
        txtWordDealerProcessor = findViewById(R.id.txt_dealer_word_processor);
        txtWordPlotsProcessor = findViewById(R.id.txt_plots_word_processor);
        txtWordtotalprocurementProcessor = findViewById(R.id.txt_total_procurement_word_processor);
        txtWordtotalareaProcessor = findViewById(R.id.txt_total_area_word_processor);
        txtWordCustomersProcessor = findViewById(R.id.txt_customers_word_processor);
        txtWordtotalsupplyProcessor = findViewById(R.id.txt_total_supply_word_processor);


        txtAppVersion = findViewById(R.id.txt_app_version);
        llNavBtn = findViewById(R.id.ll_nav_bar);


        txtWordTonsPrc = findViewById(R.id.txt_wrd_tons_dash_prc);
        txtWordHeactersPRc = findViewById(R.id.txt_wrd_heactares_prc_dash);
        txtWordTonsPrcSupply = findViewById(R.id.txt_supply_tons_word_prc);


        // TODO: 11/27/2023 dealer language

        txtWordHeaderDealerDashProcument = findViewById(R.id.txt_procument_dealer_dash);
        txtWordHeadrSupplyChainDTLDealer = findViewById(R.id.txt_word_supply_dtl_dealer_dash);
        txtWordTonsDealer = findViewById(R.id.txt_word_tons_dash_dealer);
        txtWordHeactersDealer = findViewById(R.id.txt_word_hect_dash_dealer);
        txtWordTonsDealerSupply = findViewById(R.id.txt_wrd_supply_tons_dealer);

        txtWordFarmerDealerDash = findViewById(R.id.txt_word_farmer_dash_dealer);
        txtWordSubDealerDashDealer = findViewById(R.id.txt_word_sub_dealer_dash);
        txtWordDealerDashPlots = findViewById(R.id.txt_word_plots_dash_dealer);
        txtWordDealerDashTotalProcurement = findViewById(R.id.txt_word_tproc_dealer_dash);
        txtWordTotalAreaDashDealer = findViewById(R.id.txt_word_total_area_dealer_dash);
        txtMainDealerDashWord = findViewById(R.id.txt_wrd_main_dealer_dash);
        txtWordProceesorDealerDash = findViewById(R.id.txt_wrd_prc_count_dealer_dash);
        txtWordSupplyDealerDash = findViewById(R.id.txt_supply_count_dealer_dash);

        txtWordUName = findViewById(R.id.txt_word_name);
        txtWordURole = findViewById(R.id.txt_word_role);
        txtWordAppVersion = findViewById(R.id.txt_word_app_version);


    }

    private void initializeValues() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String appVersion = packageInfo.versionName;
            if (!TextUtils.isEmpty(appVersion)) {
                txtAppVersion.setText(appVersion);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        // Set click listener for the navigation button
        SnavButton.setOnClickListener(this);
        llAddGRN.setOnClickListener(this);
        llBatchProcessing.setOnClickListener(this);
        llInvoice.setOnClickListener(this);
        llDrc.setOnClickListener(this);
        llQualityCheck.setOnClickListener(this);
        llLogOut.setOnClickListener(this);
        llNavBtn.setOnClickListener(this);
        farmerProcessorLayout.setOnClickListener(this);
        farmerDealerLayout.setOnClickListener(this);
        TotalSupplyDealerLayout.setOnClickListener(this);
        TotalProcurementDealerLayout.setOnClickListener(this);
        plotsDealerLayout.setOnClickListener(this);
        plotsProcessorLayout.setOnClickListener(this);
        TotalSupplyProcessorLayout.setOnClickListener(this);
        TotalProcurementProcessorLayout.setOnClickListener(this);
        Dealerllayout.setOnClickListener(this);
        Customerprocessorlayout.setOnClickListener(this);
        SubDealerlayout.setOnClickListener(this);
        DealerProcessorlayout.setOnClickListener(this);
        llLangChange.setOnClickListener(this);
    }


    @Override
    public void onBackPressed() {
        //   super.onBackPressed();
        showExitConfirmationDialog();
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish();
//        System.exit(0);
    }

    //    @Override
//    public void onBackPressed() {
//
//        showExitConfirmationDialog();
//
//    }

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
                        // Exit the app
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog, do nothing
                    }
                });
        builder.create().show();
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    public void configureViewModel() {
        //  viewModel = ViewModelProviders.of(this, viewModelFactory).get(AppViewModel.class);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
        if (appHelper.isNetworkAvailable()) {
            getMainDashBoardData();
        } else {
            Toast.makeText(MainDashBoardActivity.this, "please check your connection", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        try {
            switch (v.getId()) {

                case R.id.ll_change_lang:
                    //  Toast.makeText(MainDashBoardActivity.this, "working in progress will update soon", Toast.LENGTH_SHORT).show();
                    showLAngPopUp();
                    //changeLanguage();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.ll_processor_dealer:
                    intent = new Intent(MainDashBoardActivity.this, DashBoardProcessorListActivity.class);
                    startActivity(intent);
                    // Processordialog();
                    break;


                case R.id.ll_nav_bar:
                case R.id.SnavButton:
                    // Open the navigation drawer when the button is clicked
                    drawerLayout.openDrawer(GravityCompat.START);
                    break;

                case R.id.ll_log_out:

                    appHelper.getSharedPrefObj().edit().remove(DeviceDealerID).apply();
                    appHelper.getSharedPrefObj().edit().remove(DeviceProcessorId).apply();
                    appHelper.getSharedPrefObj().edit().remove(DeviceCustomerId).apply();
                    appHelper.getSharedPrefObj().edit().remove(AUTHORIZATION_TOKEN_KEY).apply();
                    appHelper.getSharedPrefObj().edit().remove(DeviceUserName).apply();
                    appHelper.getSharedPrefObj().edit().remove(DeviceUserPwd).apply();
                    appHelper.getSharedPrefObj().edit().remove(DeviceUserRole).apply();
                    sharedPreferencesData.edit().putBoolean("firstrun", true).commit();
                    intent = new Intent(MainDashBoardActivity.this, LoginActivity.class);
                    startActivity(intent);

                    //  overridePendingTransition(R.anim.rotate_out, R.anim.rotate_in);
                    finish();
                    Toast.makeText(MainDashBoardActivity.this, "log out succesfully", Toast.LENGTH_LONG).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;


                case R.id.ll_grn:
                    if (deviceRoleName.equalsIgnoreCase("Dealer")) {
                     /*   appHelper.getSharedPrefObj().edit().putString(DeviceDealerID, loginResponseDTOList.getData().get(0).getDealerid()).apply();
                        appHelper.getSharedPrefObj().edit().remove(DeviceManufactureId).apply();*/
                        proceedToDashboard();
                    } else {
                        /*appHelper.getSharedPrefObj().edit().putString(DeviceManufactureId, loginResponseDTOList.getData().get(0).getManufacturerid()).apply();
                        appHelper.getSharedPrefObj().edit().remove(DeviceDealerID).apply();*/
                        proceedToManufacture();
                    }

                    break;


                case R.id.ll_batch_processing:
                    proceedToBatchProcessing();
                    break;

                case R.id.ll_invoice_main_nav:
                    if (appHelper.getSharedPrefObj().getString(DeviceUserRole, "").equals("Dealer")) {
                        proceedToDealerInvoice();
                    } else if (appHelper.getSharedPrefObj().getString(DeviceUserRole, "").equalsIgnoreCase("Processor")) {
                        proceedToManufactureInvoice();
                    }
                    break;

                case R.id.ll_drc:
                    proceedToManufactureDRC();
                    break;


                case R.id.ll_Quality_check:
                    proceedToQualityCheck();
                    break;


                case R.id.ll_plots_dealer:
                    proceedToPlotDealerList();
                    break;

                case R.id.ll_plots_processor:
                    proceedToPlotProcessorList();
                    break;

                case R.id.ll_farmer_dealer:
                    proceedToFarmerDealerList();
                    break;

                case R.id.ll_farmer_processor:
                    proceedToFarmerProcessorList();
                    break;

                case R.id.ll_dealer:
                    proceedToDealerList();
                    break;


                case R.id.ll_Customers:
                    proceedToCustomerList();
                    break;

                case R.id.ll_total_supply_dealer:
                    proceedToDealerTotalSupply();
                    break;

                case R.id.ll_Sub_Dealers:
                    proceedToSubDealerTotalSupply();
                    break;

                case R.id.ll_total_supply_processor:
                    proceedToProcessorTotalSupply();
                    break;

                case R.id.ll_total_procurement_dealer:
                    proceedToDealerTotalProcurement();
                    break;

                case R.id.ll_total_procurement_processor:
                    proceedToProcessorTotalProcurement();
                    break;

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void changeLanguage() {
        changeLanguageDialogue = new Dialog(this, R.style.MyAlertDialogThemeNew);
        changeLanguageDialogue.requestWindowFeature(Window.FEATURE_NO_TITLE);
        changeLanguageDialogue.setContentView(R.layout.change_language_header);
        changeLanguageDialogue.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        changeLanguageDialogue.setCanceledOnTouchOutside(true);
        changeLanguageDialogue.setCancelable(true);
        changeLanguageDialogue.show();
        RecyclerView recLanguageList = changeLanguageDialogue.findViewById(R.id.rec_dialog_language);
        ImageView imgCross = changeLanguageDialogue.findViewById(R.id.img_cross_dig_lang);

        LinearLayout imgempty = changeLanguageDialogue.findViewById(R.id.emptyListImage);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainDashBoardActivity.this, LinearLayoutManager.VERTICAL, false);
        recLanguageList.setLayoutManager(layoutManager);
        recLanguageList.hasFixedSize();
        getLanguagesList(recLanguageList);

//
//        farmerByDealerIdAdapter = new FarmerListForDialogAdapter(appHelper, viewModel, (List<GetFarmerbyDealerid>) farmersSearchList, "farmer");
//        recycleFarmer.setAdapter(farmerByDealerIdAdapter);
//        farmerByDealerIdAdapter.notifyDataSetChanged();

        imgCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLanguageDialogue.dismiss();

            }
        });


    }

    //farmer list
    private void proceedToPlotDealerList() {
        Intent intent = new Intent(MainDashBoardActivity.this, PlotsMainActivity.class);
        startActivity(intent);
        //     overridePendingTransition(R.anim.rotate_out, R.anim.rotate_in);
    }

    private void proceedToPlotProcessorList() {
        Intent intent = new Intent(MainDashBoardActivity.this, PlotsMainActivity.class);
        startActivity(intent);
        // overridePendingTransition(R.anim.rotate_out, R.anim.rotate_in);
    }

    private void proceedToFarmerDealerList() {
        Intent intent = new Intent(MainDashBoardActivity.this, FarmerDetailsListActivity.class);
        startActivity(intent);
        // overridePendingTransition(R.anim.rotate_out, R.anim.rotate_in);
    }

    private void proceedToFarmerProcessorList() {
        Intent intent = new Intent(MainDashBoardActivity.this, FarmerDetailsListActivity.class);
        startActivity(intent);
        // overridePendingTransition(R.anim.rotate_out, R.anim.rotate_in);
    }


    private void proceedToDealerList() {
        Intent intent = new Intent(MainDashBoardActivity.this, DashBoardDealerListActivity.class);
        startActivity(intent);
        // overridePendingTransition(R.anim.rotate_out, R.anim.rotate_in);

    }


    private void proceedToCustomerList() {
        Intent intent = new Intent(MainDashBoardActivity.this, DashBoardCustomerListAct.class);
        startActivity(intent);
        //   overridePendingTransition(R.anim.rotate_out, R.anim.rotate_in);

    }


    private void proceedToSubDealerTotalSupply() {
        Intent intent = new Intent(MainDashBoardActivity.this, SubDealerListActivity.class);
        startActivity(intent);
        // overridePendingTransition(R.anim.rotate_out, R.anim.rotate_in);
    }

    private void proceedToDealerTotalProcurement() {
        Intent intent = new Intent(MainDashBoardActivity.this, TotalProcurementReportActivity.class);
        startActivity(intent);
        // overridePendingTransition(R.anim.rotate_out, R.anim.rotate_in);

    }


    private void proceedToProcessorTotalProcurement() {
        Intent intent = new Intent(MainDashBoardActivity.this, TotalProcurementReportActivity.class);
        startActivity(intent);
        //  overridePendingTransition(R.anim.rotate_out, R.anim.rotate_in);

    }

    private void proceedToDealerTotalSupply() {
        Intent intent = new Intent(MainDashBoardActivity.this, TotalSupplyReportActivity.class);
        startActivity(intent);
        //    overridePendingTransition(R.anim.rotate_out, R.anim.rotate_in);

    }


    private void proceedToProcessorTotalSupply() {
        Intent intent = new Intent(MainDashBoardActivity.this, TotalSupplyReportActivity.class);
        startActivity(intent);
        //overridePendingTransition(R.anim.rotate_out, R.anim.rotate_in);

    }

    private void proceedToDashboard() {
        Intent intent = new Intent(MainDashBoardActivity.this, NavGrnActivity.class);
        startActivity(intent);
        //overridePendingTransition(R.anim.rotate_out, R.anim.rotate_in);
        drawerLayout.closeDrawer(GravityCompat.START);
        //finish();
    }

    private void proceedToManufacture() {
        Intent intent = new Intent(MainDashBoardActivity.this, NavGrnActivity.class);
        startActivity(intent);
        //  overridePendingTransition(R.anim.rotate_out, R.anim.rotate_in);
        drawerLayout.closeDrawer(GravityCompat.START);
        //finish();
    }

    private void proceedToDealerInvoice() {
        Intent intent = new Intent(MainDashBoardActivity.this, InvoiceListActivity.class);
        startActivity(intent);
        //  overridePendingTransition(R.anim.rotate_out, R.anim.rotate_in);
        drawerLayout.closeDrawer(GravityCompat.START);
        //finish();

    }

    private void proceedToManufactureInvoice() {
        Intent intent = new Intent(MainDashBoardActivity.this, InvoiceListActivity.class);
        startActivity(intent);
        // overridePendingTransition(R.anim.rotate_out, R.anim.rotate_in);
        drawerLayout.closeDrawer(GravityCompat.START);
        //finish();
    }

//    private void proceedToCustomerInvoice() {
//        Intent intent = new Intent(MainDashBoardActivity.this, CustomerInvoiceList.class);
//        startActivity(intent);
//        drawerLayout.closeDrawer(GravityCompat.START);
//        //finish();
//    }

    private void proceedToManufactureDRC() {
        Intent intent = new Intent(MainDashBoardActivity.this, Drclist.class);
        startActivity(intent);
        //   overridePendingTransition(R.anim.rotate_out, R.anim.rotate_in);
        drawerLayout.closeDrawer(GravityCompat.START);
        //finish();

    }

    private void proceedToQualityCheck() {
        Intent intent = new Intent(MainDashBoardActivity.this, QualityCheckList.class);
        startActivity(intent);
        //  overridePendingTransition(R.anim.rotate_out, R.anim.rotate_in);
        drawerLayout.closeDrawer(GravityCompat.START);
        //finish();

    }


    private void proceedToBatchProcessing() {

        if (appHelper.getSharedPrefObj().getString(DeviceDealerID, "").isEmpty()) {
            Intent intent = new Intent(MainDashBoardActivity.this, ProcessorBatchCreationAct.class);
            startActivity(intent);
            //  overridePendingTransition(R.anim.rotate_out, R.anim.rotate_in);
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(MainDashBoardActivity.this, DealerBatchCreationAct.class);
            startActivity(intent);
            //   overridePendingTransition(R.anim.rotate_out, R.anim.rotate_in);
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        //finish();
    }

    private void getLanguagesList(RecyclerView recLanguageList) {
        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        final ProgressDialog progressDialog = new ProgressDialog(MainDashBoardActivity.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading data...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        // callRetrofit = service.getFarmerListbyDealerIdFromserver(strDealerID, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));
        callRetrofit = service.getDataLanguages(appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    String strResponse = String.valueOf(response.body());
                    Log.d(TAG, "onResponse: >>> language" + strResponse);
                    JSONObject jsonObject = new JSONObject(strResponse);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        try {
                            ArrayList<GetLanguageHeaderList> getLanguageHeaderListArrayList = new ArrayList<>();
                            getLanguageHeaderLists.clear();
                            progressDialog.dismiss();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                GetLanguageHeaderList getLanguageHeaderList = new GetLanguageHeaderList();
                                getLanguageHeaderList.setId(Integer.valueOf(jsonObjectFarmerPD.getString("Id")));
                                getLanguageHeaderList.setLanguageName(jsonObjectFarmerPD.getString("LanguageName"));
                                getLanguageHeaderListArrayList.add(getLanguageHeaderList);
                            }

                            getLanguageHeaderLists.addAll(getLanguageHeaderListArrayList);
                            headerLanguageAdapter = new HeaderLanguageAdapter(MainDashBoardActivity.this, MainDashBoardActivity.this, appHelper, viewModel, (List<GetLanguageHeaderList>) getLanguageHeaderLists, "farmer");
                            recLanguageList.setAdapter(headerLanguageAdapter);
                            headerLanguageAdapter.notifyDataSetChanged();

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            progressDialog.dismiss();
                            Log.d("Error", ">>>>" + ex.toString());
                        }
                    } else {
                        progressDialog.dismiss();

                        Toast.makeText(MainDashBoardActivity.this, "no records found", Toast.LENGTH_LONG).show();
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


    private void getMainDashBoardData() {

        if (appHelper.getSharedPrefObj().getString(DeviceDealerID, "").isEmpty()) {
            final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
            Call<JsonElement> callRetrofit = null;
            callRetrofit = service.getFarmerAnalysisbyProcessorIdFromserver(strProcessorId, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));
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
                                ArrayList<GetFarmerAnalysisbyProcessorId> DashboardList = new ArrayList<>();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                    GetFarmerAnalysisbyProcessorId FarmerListDashboard = new GetFarmerAnalysisbyProcessorId();
                                    FarmerListDashboard.setNoOfFarmers(Integer.valueOf(jsonObjectFarmerPD.getString("NoOfFarmers")));
                                    FarmerListDashboard.setTotalArea(Double.valueOf(jsonObjectFarmerPD.getString("TotalArea")));
                                    FarmerListDashboard.setNoOfPlantations(Integer.valueOf(jsonObjectFarmerPD.getString("NoOfPlantations")));
                                    FarmerListDashboard.setTotalSupplyInTons(Double.valueOf(jsonObjectFarmerPD.optString("TotalSupplyInTons")));
                                    FarmerListDashboard.setTotalProcurement(Integer.valueOf(jsonObjectFarmerPD.getString("TotalProcurement")));
                                    FarmerListDashboard.setCustomerCount(Integer.valueOf(jsonObjectFarmerPD.getString("CustomerCount")));
                                    FarmerListDashboard.setNoofDealers(jsonObjectFarmerPD.optInt("NoofDealers"));

                                    DashboardList.add(FarmerListDashboard);
                                }

                                if (DashboardList.size() > 0) {
                                    int Farmers = DashboardList.get(0).getNoOfFarmers();
                                    int Plantations = DashboardList.get(0).getNoOfPlantations();
                                    double TotalArea = DashboardList.get(0).getTotalArea();
                                    double TotalSupply = DashboardList.get(0).getTotalSupplyInTons();

                                    int TotalProcurement = DashboardList.get(0).getTotalProcurement();
                                    int CustomerCount = DashboardList.get(0).getCustomerCount();
                                    int Dealers = DashboardList.get(0).getNoofDealers();


                                    Log.d(TAG, "Number of farmers: " + Farmers); // Debugging log
                                    txtFarmerProcessor.setText(String.valueOf(Farmers));
                                    txtPlotsProcessor.setText(String.valueOf(Plantations));
                                    txtAreaProcessor.setText(String.valueOf(TotalArea));
                                    txtSupplyProcessor.setText(String.valueOf(TotalSupply));
                                    txtProcurementProcessor.setText(String.valueOf(TotalProcurement));
                                    txtCustomers.setText(String.valueOf(CustomerCount));
                                    txtDealer.setText(String.valueOf(Dealers));


                                }

                            } catch (Exception ex) {
                                ex.printStackTrace();
                                Log.d("Error", ">>>>" + ex.toString());
                            }
                        } else {
                            Toast.makeText(MainDashBoardActivity.this, "no records found", Toast.LENGTH_LONG).show();
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
        } else {
            final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
            Call<JsonElement> callRetrofit = null;
            callRetrofit = service.getFarmerAnalysisbyDealerIdFromserver(strDealerID, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));
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
                                ArrayList<GetFarmerAnalysisbyDealerId> DashboardList = new ArrayList<>();


                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                    GetFarmerAnalysisbyDealerId FarmerListDashboard = new GetFarmerAnalysisbyDealerId();
                                    FarmerListDashboard.setNoOfFarmers(Integer.valueOf(jsonObjectFarmerPD.getString("NoOfFarmers")));
                                    FarmerListDashboard.setTotalArea(Double.valueOf(jsonObjectFarmerPD.optString("TotalArea")));
                                    FarmerListDashboard.setNoOfPlantations(Integer.valueOf(jsonObjectFarmerPD.getString("NoOfPlantations")));
//                                    FarmerListDashboard.setTotalSupplyInTons(Double.valueOf(jsonObjectFarmerPD.optString("TotalSupplyInTons")));
                                    FarmerListDashboard.setNoofSubDealers(Integer.valueOf(jsonObjectFarmerPD.getString("NoofSubDealers")));
                                    FarmerListDashboard.setTotalProcurement(Double.valueOf(jsonObjectFarmerPD.optString("TotalProcurement")));
                                    FarmerListDashboard.setMainDealer(Integer.valueOf(jsonObjectFarmerPD.getString("MainDealer")));
                                    FarmerListDashboard.setTotalSupplyInTons(Double.valueOf(jsonObjectFarmerPD.optString("TotalSupplyInTons")));
                                    FarmerListDashboard.setProcessorCount(Integer.valueOf(jsonObjectFarmerPD.getString("ProcessorCount")));
                                    DashboardList.add(FarmerListDashboard);
                                }

                                if (DashboardList.size() > 0) {
                                    int Farmers = DashboardList.get(0).getNoOfFarmers();
                                    int Plantations = DashboardList.get(0).getNoOfPlantations();
                                    double TotalArea = DashboardList.get(0).getTotalArea();
                                    double TotalInvoiceSupply = DashboardList.get(0).getTotalSupplyInTons();
                                    int SubDealers = DashboardList.get(0).getNoofSubDealers();
                                    double TotalProcurement = DashboardList.get(0).getTotalProcurement();
                                    int MainDealers = DashboardList.get(0).getMainDealer();
                                    int ProcessorCount = DashboardList.get(0).getProcessorCount();


                                    Log.d(TAG, "Number of farmers: " + Farmers); // Debugging log
                                    txtFarmerDealer.setText(String.valueOf(Farmers));
                                    txtPlotsDealer.setText(String.valueOf(Plantations));
                                    txtAreaDealer.setText(String.valueOf(TotalArea));
                                    txtSupplyDealer.setText(String.valueOf(TotalInvoiceSupply));
                                    txtSubdealer.setText(String.valueOf(SubDealers));
                                    txtProcurementDealer.setText(String.valueOf(TotalProcurement));
                                    txtMainDealer.setText(String.valueOf(MainDealers));
                                    txtProcessorDealer.setText(String.valueOf(ProcessorCount));


                                }

                            } catch (Exception ex) {
                                ex.printStackTrace();
                                Log.d("Error", ">>>>" + ex.toString());
                            }
                        } else {
                            Toast.makeText(MainDashBoardActivity.this, "no records found", Toast.LENGTH_LONG).show();
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


    }


    private void Processordialog() {

        dialog = new Dialog(this, R.style.MyAlertDialogThemeNew);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.processor_details_dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();

        TextView txtPName = dialog.findViewById(R.id.txtPName);
        TextView txtPAddress = dialog.findViewById(R.id.txtPAddress);
        TextView txtPContact = dialog.findViewById(R.id.txtContactnumber);

        AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = service.getProcessorListByDealerId(strDealerID, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        String strResponse = String.valueOf(response.body());
                        Log.d(TAG, "onResponse: >>>" + strResponse);
                        JSONObject jsonObject = new JSONObject(strResponse);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray.length() > 0) {
                            ArrayList<GetProcessorDataFromServer> ProcessorData = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                GetProcessorDataFromServer ProcessorList = new GetProcessorDataFromServer();

                                ProcessorList.setProcessor(jsonObjectFarmerPD.getString("Processor"));
                                ProcessorList.setAddress(jsonObjectFarmerPD.getString("Address"));
                                ProcessorList.setPrimaryContactNo(jsonObjectFarmerPD.getString("PrimaryContactNo"));
                                ProcessorData.add(ProcessorList);


                                txtPName.setText(ProcessorList.getProcessor());
                                txtPAddress.setText(ProcessorList.getAddress());
                                txtPContact.setText(ProcessorList.getPrimaryContactNo());

                            }

                        }

                    } else {

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
    public void selectPosition(int position, GetLanguageHeaderList getLanguageHeaderData, List<GetLanguageHeaderList> GetLanguageHeaderList) {
        String str_GetLangID = String.valueOf(getLanguageHeaderData.getId());
        appHelper.getSharedPrefObj().edit().putString(LanguageId, str_GetLangID).apply();
        Log.d(TAG, "selectPosition: data" + str_GetLangID);
        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");
        changeLanguageDialogue.dismiss();
        getLanguageDataList(str_GetLangID);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (appHelper.isNetworkAvailable()) {
//            getLanguageDataList(strLanguageId);
//
//        } else {
//            Toast.makeText(MainDashBoardActivity.this, "please check your internet", Toast.LENGTH_SHORT).show();
//        }

    }

    private void getLanguageDataList(String str_GetLangID) {
        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        final ProgressDialog progressDialog = new ProgressDialog(MainDashBoardActivity.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading data...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        callRetrofit = service.getTransalteLanguageWordsByLanguageIdFromServer(str_GetLangID, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));

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
                                    if (getResources().getString(R.string.farmers).equals(jsonEngWord)) {
                                        txtWordFarmerProcessor.setText(jsonEngWord1);
                                        txtWordFarmerDealerDash.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.dealers).equals(jsonEngWord)) {
                                        txtWordDealerProcessor.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.plots).equals(jsonEngWord)) {
                                        txtWordPlotsProcessor.setText(jsonEngWord1);
                                        txtWordDealerDashPlots.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.total_procurement).equals(jsonEngWord)) {
                                        txtWordtotalprocurementProcessor.setText(jsonEngWord1);
                                        txtWordDealerDashTotalProcurement.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.total_area).equals(jsonEngWord)) {
                                        txtWordtotalareaProcessor.setText(jsonEngWord1);
                                        txtWordTotalAreaDashDealer.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.customers).equals(jsonEngWord)) {
                                        txtWordCustomersProcessor.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.total_supply).equals(jsonEngWord)) {
                                        txtWordtotalsupplyProcessor.setText(jsonEngWord1);
                                        txtWordSupplyDealerDash.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.name).equals(jsonEngWord)) {
                                        //   strWordTitleName = jsonEngWord1;
                                        txtWordUName.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.role).equals(jsonEngWord)) {

                                        // strWordRoleName = jsonEngWord1;
                                        txtWordUName.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.activities).equals(jsonEngWord)) {
                                        txtActivities.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.grn).equals(jsonEngWord)) {
                                        txtGRN.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.drc).equals(jsonEngWord)) {
                                        txtDRC.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.batch_processing).equals(jsonEngWord)) {
                                        txtBatchProcessing.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.quality_check).equals(jsonEngWord)) {
                                        txtQualityCheck.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.invoice).equals(jsonEngWord)) {
                                        txtInvoice.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.change_language).equals(jsonEngWord)) {
                                        txtChangelanguagr.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.logout).equals(jsonEngWord)) {
                                        txtLogout.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.app_version).equals(jsonEngWord)) {
                                        //    txtAppVersion.setText(jsonEngWord1);


                                        txtWordAppVersion.setText(jsonEngWord1);

                                    } else if (getResources().getString(R.string.procurement_details).equals(jsonEngWord)) {
                                        txtWordHeaderPRCProcument.setText(jsonEngWord1);
                                        txtWordHeaderDealerDashProcument.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.supply_details).equals(jsonEngWord)) {
                                        txtWordHeadrSupplyChainDTLPrc.setText(jsonEngWord1);
                                        txtWordHeadrSupplyChainDTLDealer.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.tons).equals(jsonEngWord)) {
                                        txtWordTonsPrc.setText(jsonEngWord1);
                                        txtWordTonsPrcSupply.setText(jsonEngWord1);

                                        txtWordTonsDealer.setText(jsonEngWord1);
                                        txtWordTonsDealerSupply.setText(jsonEngWord1);

                                    } else if (getResources().getString(R.string.hectares).equals(jsonEngWord)) {
                                        txtWordHeactersPRc.setText(jsonEngWord1);
                                        txtWordHeactersDealer.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.sub_dealers).equals(jsonEngWord)) {
                                        txtWordSubDealerDashDealer.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.main_dealers).equals(jsonEngWord)) {
                                        txtMainDealerDashWord.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.processor).equals(jsonEngWord)) {
                                        txtWordProceesorDealerDash.setText(jsonEngWord1);
                                    }


                                } else {

                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");

//                                    if (getResources().getString(R.string.farmers).equals(jsonEngWord)) {
//                                        txtWordFarmerProcessor.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.dealers).equals(jsonEngWord)) {
//                                        txtWordDealerProcessor.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.plots).equals(jsonEngWord)) {
//                                        txtWordPlotsProcessor.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.total_procurement).equals(jsonEngWord)) {
//                                        txtWordtotalprocurementProcessor.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.total_area).equals(jsonEngWord)) {
//                                        txtWordtotalareaProcessor.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.customers).equals(jsonEngWord)) {
//                                        txtWordCustomersProcessor.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.total_supply).equals(jsonEngWord)) {
//                                        txtWordtotalsupplyProcessor.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.name).equals(jsonEngWord)) {
//                                        txtUserName.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.role).equals(jsonEngWord)) {
//                                        txtRoleName.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.activities).equals(jsonEngWord)) {
//                                        txtActivities.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.grn).equals(jsonEngWord)) {
//                                        txtGRN.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.drc).equals(jsonEngWord)) {
//                                        txtDRC.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.batch_processing).equals(jsonEngWord)) {
//                                        txtBatchProcessing.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.quality_check).equals(jsonEngWord)) {
//                                        txtQualityCheck.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.invoice).equals(jsonEngWord)) {
//                                        txtInvoice.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.change_language).equals(jsonEngWord)) {
//                                        txtChangelanguagr.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.logout).equals(jsonEngWord)) {
//                                        txtLogout.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.app_version).equals(jsonEngWord)) {
//                                        //     txtAppVersion.setText(strConvertedWord);
//
//                                        strWordAppVersion = strConvertedWord;
//                                    } else if (getResources().getString(R.string.procurement_details).equals(jsonEngWord)) {
//                                        txtWordHeaderPRCProcument.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.supply_details).equals(jsonEngWord)) {
//                                        txtWordHeadrSupplyChainDTLPrc.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.tons).equals(jsonEngWord)) {
//                                        txtWordTonsPrc.setText(strConvertedWord);
//                                        txtWordTonsPrcSupply.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.hectares).equals(jsonEngWord)) {
//                                        txtWordHeactersPRc.setText(strConvertedWord);
//                                    }

                                    if (getResources().getString(R.string.farmers).equals(jsonEngWord)) {
                                        txtWordFarmerProcessor.setText(strConvertedWord);
                                        txtWordFarmerDealerDash.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.dealers).equals(jsonEngWord)) {
                                        txtWordDealerProcessor.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.plots).equals(jsonEngWord)) {
                                        txtWordPlotsProcessor.setText(strConvertedWord);
                                        txtWordDealerDashPlots.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.total_procurement).equals(jsonEngWord)) {
                                        txtWordtotalprocurementProcessor.setText(strConvertedWord);
                                        txtWordDealerDashTotalProcurement.setText(strConvertedWord);

                                    } else if (getResources().getString(R.string.total_area).equals(jsonEngWord)) {
                                        txtWordtotalareaProcessor.setText(strConvertedWord);
                                        txtWordTotalAreaDashDealer.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.customers).equals(jsonEngWord)) {
                                        txtWordCustomersProcessor.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.total_supply).equals(jsonEngWord)) {
                                        txtWordtotalsupplyProcessor.setText(strConvertedWord);
                                        txtWordSupplyDealerDash.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.name).equals(jsonEngWord)) {
                                        //strWordTitleName = jsonEngWord;
                                        txtWordUName.setText(strConvertedWord);
                                        //  txtUserName.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.role).equals(jsonEngWord)) {
                                        // strWordRoleName = jsonEngWord;
                                        txtWordURole.setText(strConvertedWord);
                                        // txtRoleName.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.app_version).equals(jsonEngWord)) {
                                        txtWordAppVersion.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.activities).equals(jsonEngWord)) {
                                        txtActivities.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.grn).equals(jsonEngWord)) {
                                        txtGRN.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.drc).equals(jsonEngWord)) {
                                        txtDRC.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.batch_processing).equals(jsonEngWord)) {
                                        txtBatchProcessing.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.quality_check).equals(jsonEngWord)) {
                                        txtQualityCheck.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.invoice).equals(jsonEngWord)) {
                                        txtInvoice.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.change_language).equals(jsonEngWord)) {
                                        txtChangelanguagr.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.logout).equals(jsonEngWord)) {
                                        txtLogout.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.app_version).equals(jsonEngWord)) {
                                        //    txtAppVersion.setText(strConvertedWord);

                                        txtWordAppVersion.setText(strConvertedWord);

                                    } else if (getResources().getString(R.string.procurement_details).equals(jsonEngWord)) {
                                        txtWordHeaderPRCProcument.setText(strConvertedWord);
                                        txtWordHeaderDealerDashProcument.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.supply_details).equals(jsonEngWord)) {
                                        txtWordHeadrSupplyChainDTLPrc.setText(strConvertedWord);
                                        txtWordHeadrSupplyChainDTLDealer.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.tons).equals(jsonEngWord)) {
                                        txtWordTonsPrc.setText(strConvertedWord);
                                        txtWordTonsPrcSupply.setText(strConvertedWord);

                                        txtWordTonsDealer.setText(strConvertedWord);
                                        txtWordTonsDealerSupply.setText(strConvertedWord);

                                    } else if (getResources().getString(R.string.hectares).equals(jsonEngWord)) {
                                        txtWordHeactersPRc.setText(strConvertedWord);
                                        txtWordHeactersDealer.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.sub_dealers).equals(jsonEngWord)) {
                                        txtWordSubDealerDashDealer.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.main_dealers).equals(jsonEngWord)) {
                                        txtMainDealerDashWord.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.processor).equals(jsonEngWord)) {
                                        txtWordProceesorDealerDash.setText(strConvertedWord);
                                    }

                                }

//                                else if (Languageid.equals("3")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//                                    if ("Farmers".equals(jsonEngWord)) {
//                                        txtWordFarmerProcessor.setText(strConvertedWord);
//                                    }
//
//                                    else if ("Dealer".equals(jsonEngWord)) {
//                                        txtWordDealerProcessor.setText(strConvertedWord);
//                                    }
//
//                                    else if ("Plots".equals(jsonEngWord)) {
//                                        txtWordPlotsProcessor.setText(strConvertedWord);
//                                    }
//                                    else if ("Total Procurement".equals(jsonEngWord)) {
//                                        txtWordtotalprocurementProcessor.setText(strConvertedWord);
//                                    }
//
//                                    else if ("Total Area".equals(jsonEngWord)) {
//                                        txtWordtotalareaProcessor.setText(strConvertedWord);
//                                    }
//                                    else if ("Customers".equals(jsonEngWord)) {
//                                        txtWordCustomersProcessor.setText(strConvertedWord);
//                                    }
//                                    else if ("Total Supply".equals(jsonEngWord)) {
//                                        txtWordtotalsupplyProcessor.setText(strConvertedWord);
//
//                                    }  else if ("Name".equals(jsonEngWord)) {
//                                        txtUserName.setText(strConvertedWord);
//                                    }
//
//                                    else if ("Role ".equals(jsonEngWord)) {
//                                        txtRoleName.setText(strConvertedWord);
//                                    }
//                                    else if ("".equals(jsonEngWord)) {
//                                        txtActivities.setText(strConvertedWord);
//                                    }
//
//                                    else if ("GRN ".equals(jsonEngWord)) {
//                                        txtGRN.setText(strConvertedWord);
//                                    }
//                                    else if ("DRC ".equals(jsonEngWord)) {
//                                        txtDRC.setText(strConvertedWord);
//                                    }
//                                    else if ("Batch Processing ".equals(jsonEngWord)) {
//                                        txtBatchProcessing.setText(strConvertedWord);
//                                    }
//                                    else if ("Quality Check ".equals(jsonEngWord)) {
//                                        txtQualityCheck.setText(strConvertedWord);
//                                    }
//                                    else if ("Invoice ".equals(jsonEngWord)) {
//                                        txtInvoice.setText(strConvertedWord);
//                                    }
//
//                                    else if ("Change Language".equals(jsonEngWord)) {
//                                        txtChangelanguagr.setText(strConvertedWord);
//                                    }
//                                    else if ("Logout ".equals(jsonEngWord)) {
//                                        txtLogout.setText(strConvertedWord);
//                                    }
//                                    else if ("App Version : ".equals(jsonEngWord)) {
//                                        txtAppversion.setText(strConvertedWord);
//                                    }
//                                } else if (Languageid.equals("4")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//                                    if ("Farmers".equals(jsonEngWord)) {
//                                        txtWordFarmerProcessor.setText(strConvertedWord);
//                                    }
//
//                                    else if ("Dealer".equals(jsonEngWord)) {
//                                        txtWordDealerProcessor.setText(strConvertedWord);
//                                    }
//
//                                    else if ("Plots".equals(jsonEngWord)) {
//                                        txtWordPlotsProcessor.setText(strConvertedWord);
//                                    }
//                                    else if ("Total Procurement".equals(jsonEngWord)) {
//                                        txtWordtotalprocurementProcessor.setText(strConvertedWord);
//                                    }
//
//                                    else if ("Total Area".equals(jsonEngWord)) {
//                                        txtWordtotalareaProcessor.setText(strConvertedWord);
//                                    }
//                                    else if ("Customers".equals(jsonEngWord)) {
//                                        txtWordCustomersProcessor.setText(strConvertedWord);
//                                    }
//                                    else if ("Total Supply".equals(jsonEngWord)) {
//                                        txtWordtotalsupplyProcessor.setText(strConvertedWord);
//
//                                    }  else if ("Name".equals(jsonEngWord)) {
//                                        txtUserName.setText(strConvertedWord);
//                                    }
//
//                                    else if ("Role ".equals(jsonEngWord)) {
//                                        txtRoleName.setText(strConvertedWord);
//                                    }
//                                    else if ("".equals(jsonEngWord)) {
//                                        txtActivities.setText(strConvertedWord);
//                                    }
//
//                                    else if ("GRN ".equals(jsonEngWord)) {
//                                        txtGRN.setText(strConvertedWord);
//                                    }
//                                    else if ("DRC ".equals(jsonEngWord)) {
//                                        txtDRC.setText(strConvertedWord);
//                                    }
//                                    else if ("Batch Processing ".equals(jsonEngWord)) {
//                                        txtBatchProcessing.setText(strConvertedWord);
//                                    }
//                                    else if ("Quality Check ".equals(jsonEngWord)) {
//                                        txtQualityCheck.setText(strConvertedWord);
//                                    }
//                                    else if ("Invoice ".equals(jsonEngWord)) {
//                                        txtInvoice.setText(strConvertedWord);
//                                    }
//
//                                    else if ("Change Language".equals(jsonEngWord)) {
//                                        txtChangelanguagr.setText(strConvertedWord);
//                                    }
//                                    else if ("Logout ".equals(jsonEngWord)) {
//                                        txtLogout.setText(strConvertedWord);
//                                    }
//                                    else if ("App Version : ".equals(jsonEngWord)) {
//                                        txtAppversion.setText(strConvertedWord);
//                                    }
//                                }else if (Languageid.equals("5")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//                                    if ("Farmers".equals(jsonEngWord)) {
//                                        txtWordFarmerProcessor.setText(strConvertedWord);
//                                    }
//
//                                    else if ("Dealer".equals(jsonEngWord)) {
//                                        txtWordDealerProcessor.setText(strConvertedWord);
//                                    }
//
//                                    else if ("Plots".equals(jsonEngWord)) {
//                                        txtWordPlotsProcessor.setText(strConvertedWord);
//                                    }
//                                    else if ("Total Procurement".equals(jsonEngWord)) {
//                                        txtWordtotalprocurementProcessor.setText(strConvertedWord);
//                                    }
//
//                                    else if ("Total Area".equals(jsonEngWord)) {
//                                        txtWordtotalareaProcessor.setText(strConvertedWord);
//                                    }
//                                    else if ("Customers".equals(jsonEngWord)) {
//                                        txtWordCustomersProcessor.setText(strConvertedWord);
//                                    }
//                                    else if ("Total Supply".equals(jsonEngWord)) {
//                                        txtWordtotalsupplyProcessor.setText(strConvertedWord);
//
//                                    }  else if ("Name".equals(jsonEngWord)) {
//                                        txtUserName.setText(strConvertedWord);
//                                    }
//
//                                    else if ("Role ".equals(jsonEngWord)) {
//                                        txtRoleName.setText(strConvertedWord);
//                                    }
//                                    else if ("".equals(jsonEngWord)) {
//                                        txtActivities.setText(strConvertedWord);
//                                    }
//
//                                    else if ("GRN ".equals(jsonEngWord)) {
//                                        txtGRN.setText(strConvertedWord);
//                                    }
//                                    else if ("DRC ".equals(jsonEngWord)) {
//                                        txtDRC.setText(strConvertedWord);
//                                    }
//                                    else if ("Batch Processing ".equals(jsonEngWord)) {
//                                        txtBatchProcessing.setText(strConvertedWord);
//                                    }
//                                    else if ("Quality Check ".equals(jsonEngWord)) {
//                                        txtQualityCheck.setText(strConvertedWord);
//                                    }
//                                    else if ("Invoice ".equals(jsonEngWord)) {
//                                        txtInvoice.setText(strConvertedWord);
//                                    }
//
//                                    else if ("Change Language".equals(jsonEngWord)) {
//                                        txtChangelanguagr.setText(strConvertedWord);
//                                    }
//                                    else if ("Logout ".equals(jsonEngWord)) {
//                                        txtLogout.setText(strConvertedWord);
//                                    }
//                                    else if ("App Version : ".equals(jsonEngWord)) {
//                                        txtAppVersion.setText(strConvertedWord);
//                                    }
//                                }  else{


                            }


                        } catch (Exception ex) {
                            ex.printStackTrace();
                            progressDialog.dismiss();
                            Log.d("Error", ">>>>" + ex.toString());
                        }
                    } else {
                        progressDialog.dismiss();

                        Toast.makeText(MainDashBoardActivity.this, "no records found", Toast.LENGTH_LONG).show();
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