package com.socatra.intellitrack.di.module;


import com.socatra.intellitrack.activity.RiskAssesmentActivity;
import com.socatra.intellitrack.activity.Traderflow.TraderCustListAct;
import com.socatra.intellitrack.activity.Traderflow.TraderDetailsListAct;
import com.socatra.intellitrack.activity.Traderflow.DealerDetailsinTrader;
import com.socatra.intellitrack.activity.Traderflow.FarmerDetailsbyDealerinTrader;
import com.socatra.intellitrack.activity.Traderflow.TraderProcessorDetails;
import com.socatra.intellitrack.activity.Traderflow.TraderProcessorFarmerList;
import com.socatra.intellitrack.activity.Traderflow.PlotListbyTraderId;
import com.socatra.intellitrack.activity.Traderflow.SubDealerDetailsinTrader;
import com.socatra.intellitrack.activity.Traderflow.TraderDashboardActivity;
import com.socatra.intellitrack.activity.batchProcessing.DealerBatchCreationAct;
import com.socatra.intellitrack.activity.batchProcessing.ProcessorBatchCreationAct;
import com.socatra.intellitrack.activity.customerflow.CustomerBatchFarmerListAct;
import com.socatra.intellitrack.activity.customerflow.CustomerViewBatchListAct;
import com.socatra.intellitrack.activity.customerflow.CustomerDashboard;
import com.socatra.intellitrack.activity.customerflow.DashBoardCustomerListAct;
import com.socatra.intellitrack.activity.customerflow.FarmerListbyInvoiceActivity;

import com.socatra.intellitrack.activity.customerflow.FarmerRiskAssesmentPrintViewAct;
import com.socatra.intellitrack.activity.customerflow.ViewFarmerRiskAssesMentData;
import com.socatra.intellitrack.activity.drc.ProcessorAddDrcDataActivity;
import com.socatra.intellitrack.activity.grnflow.GrnFarmerMapActivity;
import com.socatra.intellitrack.activity.grnflow.ShareDealerGRNReceipt;

import com.socatra.intellitrack.activity.main_dash_board.DashBoardProcessorListActivity;
import com.socatra.intellitrack.activity.main_dash_board.DealerProcumentData;
import com.socatra.intellitrack.activity.grnflow.AddGRNbyFarmer;
import com.socatra.intellitrack.activity.main_dash_board.DashBoardDealerListActivity;
import com.socatra.intellitrack.activity.qualitycheck.AddQualityCheck;
import com.socatra.intellitrack.activity.grnflow.DealerAddGrnActivity;
import com.socatra.intellitrack.activity.drc.Drclist;
import com.socatra.intellitrack.activity.main_dash_board.FarmerDetailsListActivity;
import com.socatra.intellitrack.activity.grnflow.FarmerGrnDetailsList;
import com.socatra.intellitrack.activity.invoice.InvoiceDealerActivity;
import com.socatra.intellitrack.activity.invoice.InvoiceListActivity;
import com.socatra.intellitrack.activity.invoice.InvoiceManufactureActivity;
import com.socatra.intellitrack.activity.LoginActivity;
import com.socatra.intellitrack.activity.main_dash_board.MainDashBoardActivity;
import com.socatra.intellitrack.activity.grnflow.ProcessorAddGRNActivity;
import com.socatra.intellitrack.activity.main_dash_board.MapActivity;
import com.socatra.intellitrack.activity.grnflow.NavGrnActivity;
import com.socatra.intellitrack.activity.main_dash_board.PlotsListActivity;
import com.socatra.intellitrack.activity.main_dash_board.PlotsMainActivity;
import com.socatra.intellitrack.activity.qualitycheck.QualityCheckList;
import com.socatra.intellitrack.activity.main_dash_board.SubDealerListActivity;
import com.socatra.intellitrack.activity.main_dash_board.TotalProcurementReportActivity;
import com.socatra.intellitrack.activity.main_dash_board.TotalSupplyReportActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {


    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract LoginActivity contributeLoginActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract DealerAddGrnActivity AddGrnActivity();


    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract ProcessorAddGRNActivity ManfactureGRNActivity();


    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract MainDashBoardActivity MainDashBoardActivity();


    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract DealerBatchCreationAct BatchProcessingActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract InvoiceDealerActivity InvoiceDealerActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract InvoiceManufactureActivity InvoiceManufactureActivity();

    //DrcManufactureActivity
    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract ProcessorAddDrcDataActivity ContrDrcManufactureActivity();

    //FarmerDetailsListActivity
    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract FarmerDetailsListActivity FarmerDetailsListActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract FarmerGrnDetailsList GrnDetailsList();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract AddGRNbyFarmer AddGRNbyFarmer();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract PlotsListActivity PlotsListActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract PlotsMainActivity PlotsMainActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract MapActivity ContrMapActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract DashBoardDealerListActivity DealerlistActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract DealerProcumentData DealerProcurementListActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract TotalProcurementReportActivity TotalProcurementReportActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract TotalSupplyReportActivity TotalSupplyReportActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract SubDealerListActivity SubDealerListActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract InvoiceListActivity NavInvoiceActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract NavGrnActivity NavGrnActivity();


    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract Drclist Drclist();


    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract QualityCheckList QualityCheckList();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract AddQualityCheck AddQualityCheck();


    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract ProcessorBatchCreationAct ProcessorBatchProcessingActivity();


    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract ShareDealerGRNReceipt ShareDealerGRNReceipt();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract DashBoardCustomerListAct CustomerList();


    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract CustomerDashboard CustomerDashboard();


    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract RiskAssesmentActivity RiskAssesmentActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract FarmerListbyInvoiceActivity FarmerListbyInvoiceActivity();


    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract GrnFarmerMapActivity GrnFarmerMapActivity();

//    @ContributesAndroidInjector(modules = FragmentModule.class)
//    abstract CustomerBatchFarmerListActivity CustomerBatchFarmerListActivity();


    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract DashBoardProcessorListActivity DashBoardProcessorListActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract TraderDashboardActivity TraderDashboardActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract TraderProcessorDetails ProcessorDetailsbyTraderActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract DealerDetailsinTrader DealerDetailsinTrader();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract TraderProcessorFarmerList FarmerDetailsbyProcessorinTrader();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract TraderDetailsListAct DealerDetailsbyTraderActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract SubDealerDetailsinTrader SubDealerDetailsinTrader();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract FarmerDetailsbyDealerinTrader FarmerDetailsbyDealerinTrader();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract PlotListbyTraderId PlotListbyTraderId();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract CustomerViewBatchListAct BatchListByCustomerViewActivity();





    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract ViewFarmerRiskAssesMentData ViewFarmerRiskAssesmentSurveyDataActivity();


    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract FarmerRiskAssesmentPrintViewAct PrintViewOfFarmerRiskAssesmentSurveyActivity();


    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract CustomerBatchFarmerListAct FarmerListByCustomerViewFromBatchActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract TraderCustListAct CustomerListbyTraderActivity();


}
