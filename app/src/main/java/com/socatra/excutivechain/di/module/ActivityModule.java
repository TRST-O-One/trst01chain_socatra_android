package com.socatra.excutivechain.di.module;


import com.socatra.excutivechain.activity.DashBoardFarmerListActivity;
import com.socatra.excutivechain.activity.DealerActivity;
import com.socatra.excutivechain.activity.DocumentDetailsActivity;
import com.socatra.excutivechain.activity.DocumentHomeActivity;
import com.socatra.excutivechain.activity.EditPersonalDetailsActivity;
import com.socatra.excutivechain.activity.FarmerMappingActivity;
import com.socatra.excutivechain.activity.FarmerSurveyActivity;
import com.socatra.excutivechain.activity.FieldCalculatorActivity;
import com.socatra.excutivechain.activity.KMLMapsActivity;
import com.socatra.excutivechain.activity.LabourSurveyActivity;
import com.socatra.excutivechain.activity.LabourSurveyHomeActivity;
import com.socatra.excutivechain.activity.LoginActivity;
import com.socatra.excutivechain.activity.ManufacturerActivity;
import com.socatra.excutivechain.activity.MapsActivity;
import com.socatra.excutivechain.activity.PersonalRegistrationActivity;
import com.socatra.excutivechain.activity.PlantationActivity;
import com.socatra.excutivechain.activity.PlantationHomeActivity;
import com.socatra.excutivechain.activity.RiskAssessmentActivity;
import com.socatra.excutivechain.activity.SurveyActivity;
import com.socatra.excutivechain.activity.SyncActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract LoginActivity LoginActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract DashBoardFarmerListActivity DashBoardFarmerListActivity();

    //Personal Registration Activity
    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract PersonalRegistrationActivity ContributePersonalRegistrationActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract SyncActivity SyncActivity();

    //PlantationAct
    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract PlantationActivity ContributePlantationActivity();

    //EditPersonalDetailsActivity
    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract EditPersonalDetailsActivity ContributeEditPersonalDetailsActivity();

    //DocumentDetailsActivity
    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract DocumentDetailsActivity ContributeDocumentDetailsActivity();

    //SurveyActivity
    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract SurveyActivity ContributeSurveyActivity();

    //PlantationHomeActivity
    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract PlantationHomeActivity ContributePlantationHomeActivity();

    //MapsActivity
    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract MapsActivity ContributeMapsActivity();

    //FieldCalculatorActivity
    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract FieldCalculatorActivity ContributeFieldCalculatorActivity();

    //DocumentHomeActivity
    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract DocumentHomeActivity ContributeDocumentHomeActivity();

    //LabourSurveyActivity
    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract LabourSurveyActivity ContributeLabourSurveyActivity();

    //FarmerSurveyActivity
    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract FarmerSurveyActivity ContributeFarmerSurveyActivity();

    //RiskAssessmentActivity
    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract RiskAssessmentActivity ContributeRiskAssessmentActivity();

    //LabourSurveyHomeActivity
    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract LabourSurveyHomeActivity ContributeLabourSurveyHomeActivity();

    //FarmerMappingActivity
    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract FarmerMappingActivity ContributeFarmerMappingActivity();

    //ManufacturerActivity
    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract ManufacturerActivity ContributeManufacturerActivity();

    //DealerActivity
    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract DealerActivity ContributeDealerActivity();


    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract KMLMapsActivity ContributeKMLMapsActivity();

    //For Water cycle Nav
//    @ContributesAndroidInjector(modules = FragmentModule.class)
//    abstract FarmerConsentActivity ContributesFarmerConsentActivity();
//
//    //WaterCycle1
//    @ContributesAndroidInjector(modules = FragmentModule.class)
//    abstract WaterCycleActivity1 WaterCycleActivity1();
//
//    //WaterCycle2
//    @ContributesAndroidInjector(modules = FragmentModule.class)
//    abstract WaterCycleActivity2 WaterCycleActivity2();
//
//    //WaterCycle3
//    @ContributesAndroidInjector(modules = FragmentModule.class)
//    abstract WaterCycleActivity3 WaterCycleActivity3();


}
