package com.socatra.excutivechain.activity;

import static com.socatra.excutivechain.AppConstant.DeviceUserID;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.media.metrics.EditingSession;
import android.nfc.Tag;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.socatra.excutivechain.AppConstant;
import com.socatra.excutivechain.BaseActivity;
import com.socatra.excutivechain.R;
import com.socatra.excutivechain.adapters.MyAdapter;
import com.socatra.excutivechain.database.entity.RiskAssessment;
import com.socatra.excutivechain.models.StateVO;
import com.socatra.excutivechain.multispineeradapters.DropDownListAdapter;
import com.socatra.excutivechain.multispineeradapters.DropDownListAdapterQ10;
import com.socatra.excutivechain.multispineeradapters.DropDownListAdapterQ20;
import com.socatra.excutivechain.multispineeradapters.DropDownListAdapterQ21;
import com.socatra.excutivechain.multispineeradapters.DropDownListAdapterQ24;
import com.socatra.excutivechain.multispineeradapters.DropDownListAdapterQ31;
import com.socatra.excutivechain.multispineeradapters.DropDownListAdapterQ35;
import com.socatra.excutivechain.multispineeradapters.DropDownListAdapterQ37;
import com.socatra.excutivechain.multispineeradapters.DropDownListAdapterQ40;
import com.socatra.excutivechain.view_models.AppViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class RiskAssessmentActivity extends BaseActivity implements HasSupportFragmentInjector {

    String TAG="RiskAssessmentActivityTAG";
    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    public AppViewModel viewModel;
    private SharedPreferences preferences;



    TextView question1_text,question2,question3_text,question4,question5_text,question6_text,question7_text,question8_text,question9,question10,question11_text,question12_text,question13,
            question14_text,question15_text,question16_text,question17_text,question18_text,question19_text,question20_text,question21_text,question22_text,question23_text,question24_text,
            question25_text,question26_text,question27_text,question28_text,question29_text,question30_text,question31_text,question32_text,question33_text,question34_text,question35_text,
            question36_text,question37text,question38_text,question39_text,question40_text,question41_text,question42_text,question43_text,question44_text,question45_text,question46_text,
            question47_text,question48_text,question49_text,question50_text,question51_text,question52_text,question53_text,question54_text,question55_text,question56_text,question57_text,
            question58_text,question59_text,question60_text,question61_text,question62_text,question63_text,question64_text,question65_text,question66_text,question67_text,question68_text,
            question69_text,question70_text,question71_text,question72_text,question73_text,question74_text,question75_text,question76_text,question77_text,question78_text,question79_text
            ,question80_text,txtSaveRisk;
    String farmerCode="";

    ImageView imgBack;
    TextView txtSave;

    ArrayList<RiskAssessment> riskAssessmentArrayList;


    //New
    String[] PlantationArr=new String[] {"Select","Less than 10 years","11-25 years","26-50 years","More than 50 years"};

    String[] YesArr=new String[] {"Select","Yes","No"};

    String[] WorkershiredArr=new String[] {"Select","1-5","6-30","More than 31"};

    String[] JobtypeArr=new String[] {"Select","Tapping","Collecting cuplumps, latex","Spraying chemical (Herbicide)","Delivering/ Rubber transportation","Plantation maintenance","Rubber processing (Rubber sheets)","Other"};

    String[] ActivitiesArr=new String[] {"Spraying chemical (Herbicide)","Plantation maintenance","Other","I dont use contractors"};

    String[] YesorNoArr=new String[] {"Select","Yes","No","I dont Know"};

    String[] MontlyincomeArr=new String[] {"Less than 60 US$","60 to 150 US$","151 to 300 US$","301 to 600 US$","more than 600 US$"};


    String[] wagecalculatorArr=new String[] {"Provide wage slips in country language","For foreign workers provide wage slips in workers","Verbal explanation","We don’t really do that"};

    String[] WorkinghoursArr=new String[] {"Select","Less than 10 hours","10 to12 hours","More than 12 hours"};

    String[] WorkerscomplaintsArr=new String[] {"Workers never complain","Face to face/ personal interview","Workers Representatives/ workers Workers consultating","Workers supporting group- Third party (NGO..)","Workers can complain through hotline/ SMS"};

    String[] VacationArr=new String[] {"Select","Yes, paid leave","Yes, no-pay leave","No"};

    String[] daysArr=new String[] {"Select","1-5 days","6-10 days","11-15 days","More than 15 days"};

    String[] childrendetailsArr=new String[] {"Select","Yes, we rely on/ need their help/there is no school","Yes, they help when activity levels are high at the farm","No, they go to school, but they will likely help after school","No, they are too young/ they only go to school","Not applicable (do not have children below 15)"};

    String[] InjuriesArr=new String[] {"Eye infection/ red eye","Skin rash","Cough","Cut/ Wound (get cut during tapping, plantation)","Animal bited (snakes…)","Road accident (communicating to site)","Falls, hits (Safety in movement inside the plantation)","Pain in the back, painful arm/hand… (ergonomics)","Never have injury","Other"};

    String[] nightInjuryyesornoArr=new String[] {"Select","Yes","No","Not applicable- We only start tapping when we have day"};

    String[] EquipmentArr=new String[] {"Friend/ Family","Buyer/cooperative","Chemical seller","Agency/administrator (e.g: Forestry department)","Third party (NGO, Consultant)","Self-study (Books, internet)","Never","Not applicable- I dont use chemicals"};

    String[] PersonalEquipmentArr=new String[] {"Select","Yes, all of us use them","Yes, we use them but subcontractors don't","Only subcontractors use them","No","Not applicable- I dont use chemicals"};

    String[] PersonalEquipmentlistArr=new String[] {"Mask","Gloves","Protective googles","Boots","Apron"};

    String[] chemicalsArr=new String[] {"Paraquat/Gramoxone","2-4D","Glyphosate/ Roundup","Other herbicide","sulfuric Acid","Formic Acid","Other coagulant","Ammonia","TMTD/ZnO (TZ)","Ethephon stimulant","Other chemicals/ local brand","I dont Know","I dont use any chemicals"};



    String[] cropsArr=new String[] {"Select","Forest","Degraded Forest (thick bush, old scrub, dense)","Fallow (light bush, low/light scrub, savanna)","Perennial crop (rubber, palm, coffee, cocoa)","Annual crop (rice, cassava, maize, vegetables)"};

    String[] ans62Arr=new String[] {"Select","Workers have access to adequate healthcare services","Healthcare services are limited","Workers have no access to healthcare services"};

    String[] ans63Arr=new String[] {"Select","Working conditions are fair and equitable, with reasonable hours and rest periods","Working conditions need improvement","Working conditions are inadequate"};


    String[] ans64Arr=new String[] {"Select","Workers have the right to organize and join unions","Workers rights in this regard are restricted","Workers do not have the right to organize or join unions"};

    String[] ans65Arr=new String[] {"Select","There is a well-established mechanism for resolving labor disputes","Labor disputes are not effectively addressed","There is no mechanism in place for dispute resolution"};

    String[] ans66Arr=new String[] {"Select","Yes, there is a policy to prevent child labor","There is no specific policy in place","Child labor is not a concern on the plantation"};

    String[] ans67Arr=new String[] {"Select","Yes, we have actively sought FPIC","We have sought FPIC in some instances","No, we have not sought FPIC"};

    String[] ans68Arr=new String[] {"Select","Yes, we have a comprehensive FPIC policy","We have a basic FPIC policy","We do not have an FPIC policy"};

    String[] ans69Arr=new String[] {"Select","We provide detailed information and hold consultations","We provide some information and engage in discussions","We do not provide comprehensive information"};

    String[] ans70Arr=new String[] {"Select","Yes, we have faced challenges and resolved them","Yes, we have faced challenges but have not yet resolved them","No, we have not encountered challenges related to FPIC"};



    String[] ans71Arr=new String[] {"Select","Yes, actively ensuring supplier compliance","Yes, but not yet fully compliant","No specific supplier accountability measures in place"};

    String[] ans72Arr=new String[] {"Select","Yes, a comprehensive verification and monitoring system","Yes, a basic verification and monitoring system","No specific system in place"};

    String[] ans73Arr=new String[] {"Select","Yes, actively seeking diverse and ethical suppliers","Yes, but not yet fully diverse and ethical","No specific efforts in place"};




    String[] ans74Arr=new String[] {"Select","Yes, and we have mitigation plans in place","Yes, but no mitigation plans","No, not yet assessed"};

    String[] ans75Arr=new String[] {"Select","Yes, actively ensuring supplier compliance","Yes, but not yet fully compliant","No specific supplier accountability measures in place"};

    String[] ans76Arr=new String[] {"Select","Yes, and we have anti-corruption policies","Yes, but no specific anti-corruption policies","No, not applicable"};

    String[] ans77Arr=new String[] {"Select","Yes, and we are compliant","Yes, but working on compliance","No measures in place"};

    String[] ans78Arr=new String[] {"Select","Yes, comprehensive documentation","Basic documentation","No documentation"};

    String[] ans79Arr=new String[] {"Select","Yes, regularly","Occasionally","No"};

    String[] ans80Arr=new String[] {"Select","Yes, actively engage with stakeholders and employees","Partially engage with them","No specific engagement measures in place"};


//    CheckBox option4_1, option4_2, option5_1, option5_2, option42_1,option42_2,option42_3 , option45_1,option45_2,option45_3,option47_1,option47_2,option47_3,option49_1,option49_2,option49_3,option50_1,option50_2,option50_3,option53_1,option53_2,option53_3,option56_1,option56_2,option56_3,option57_1,option57_2,option57_3,option59_1,option59_2,option59_3;

    EditText etAns4,etAns5,etAns42,etAns45,etAns47,etAns49,etAns50,etAns53,etAns56,etAns57,etAns59;

    EditText etAnswer10;

    EditText et_answer1,et_answer2, et_answer6, et_answer46,et_answer48,et_answer51,et_answer52,et_answer54, et_answer55,et_answer58,et_answer60,et_answer61;
    Spinner spplantationtime,spFamilyWorking,spEmployPeople,spWorkershired,spJobtype,spActivities,spcontract,spforeignworkers,spworkpermit,sppassport,spforeignworkerspayment,spownearnings,spworkersearnings,spmontlyincome,spworkersmontlyincome,spwagecalculator,spworkinghours,spworkerworkinghours,spWorkerComplaints,spWorkersUnion,spRestDay,
            spQuestion27 , spQuestion28 , spQuestion29 ,spQuestion30 , spQuestion31 ,spQuestion32, spQuestion33,        spQuestion34 , spQuestion35 , spQuestion36 , spQuestion37 ,spQuestion38 , spQuestion39,spQuestion40 ,spQuestion41 ,spQuestion43 ,  spQuestion44,
    spQuestion62,spQuestion63,spQuestion64,spQuestion65,spQuestion66,spQuestion67,spQuestion68,spQuestion69,spQuestion70,spQuestion71,spQuestion72,spQuestion73,spQuestion74,spQuestion75,spQuestion76,spQuestion77,spQuestion78,spQuestion79,spQuestion80;

    TextView question3_number;

    TextView txtriskassesment;

    String ans3=null,ans7=null,ans8=null,ans9=null,ans10=null,ans11=null,ans12=null,ans13=null,ans14=null,ans15=null,ans16=null,
            ans17=null,ans18=null,ans19=null,ans20=null,ans21=null,ans22=null,ans23=null,ans24=null,ans25=null,ans26=null,ans27=null,
            ans28=null,ans29=null,ans30=null,ans31=null,ans32=null,ans33=null,ans34=null,ans35=null,ans36=null,
            ans37=null,ans38=null,ans39=null,ans40=null,ans41=null,ans43=null,ans44=null,
            ans62=null,ans63=null,ans64=null,ans65=null,ans66=null,ans67=null,ans68=null,ans69=null,ans70=null,
            ans71=null,ans72=null,ans73=null,ans74=null,ans75=null,ans76=null,ans77=null,ans78=null,ans79=null,ans80=null;

    private boolean[] checkedQ11Items;
    MyAdapter myAdapter;

    RelativeLayout rlQuestion11Txt,relQuestion10Txt;

     TextView autoCompleteQuestion11Txt;
    private PopupWindow pw;
    private boolean expanded; 		//to  store information whether the selected values are displayed completely or in shortened representatn
    public static boolean[] checkSelected;	//


    // TODO: 9/21/2023 for question 10

    TextView txtQuestion10;
    private PopupWindow pw10;
    private boolean expanded10; 		//to  store information whether the selected values are displayed completely or in shortened representatn
    public static boolean[] checkSelected10;	//


    // TODO: 9/21/2023 for question 20

    TextView txtQuestion20;
    private PopupWindow pw20;
    private boolean expanded20; 		//to  store information whether the selected values are displayed completely or in shortened representatn
    public static boolean[] checkSelected20;

    // TODO: 9/21/2023 for question 21

    TextView txtQuestion21;
    private PopupWindow pw21;
    private boolean expanded21; 		//to  store information whether the selected values are displayed completely or in shortened representatn
    public static boolean[] checkSelected21;


    // TODO: 9/21/2023 for question 24

    TextView txtQuestion24;
    private PopupWindow pw24;
    private boolean expanded24; 		//to  store information whether the selected values are displayed completely or in shortened representatn
    public static boolean[] checkSelected24;


    // TODO: 9/21/2023 for question 31
    TextView txtQuestion31;
    private PopupWindow pw31;
    private boolean expanded31; 		//to  store information whether the selected values are displayed completely or in shortened representatn
    public static boolean[] checkSelected31;



    // TODO: 9/21/2023 for question 35
    TextView txtQuestion35;
    private PopupWindow pw35;
    private boolean expanded35; 		//to  store information whether the selected values are displayed completely or in shortened representatn
    public static boolean[] checkSelected35;


    // TODO: 9/21/2023 for question 37
    TextView txtQuestion37;
    private PopupWindow pw37;
    private boolean expanded37; 		//to  store information whether the selected values are displayed completely or in shortened representatn
    public static boolean[] checkSelected37;

    // TODO: 9/21/2023 for question 40
    TextView txtQuestion40;
    private PopupWindow pw40;
    private boolean expanded40; 		//to  store information whether the selected values are displayed completely or in shortened representatn
    public static boolean[] checkSelected40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risk_assesment);

        farmerCode=getIntent().getStringExtra("mFarmerCode");
//        getAreaValue();
        Log.e(TAG,farmerCode);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        initializeUI();
        configureDagger();
        configureViewModel();
        initializeLangForArray();
        initializeValues();
        saveDataFun();

        question1_text = findViewById(R.id.question1_text);
        question2 = findViewById(R.id.question2);
        question3_text = findViewById(R.id.question3_text);
        question4 = findViewById(R.id.question4);
        question5_text = findViewById(R.id.question5_text);
        question6_text = findViewById(R.id.question6_text);
        question7_text = findViewById(R.id.question7_text);
        question8_text = findViewById(R.id.question8_text);
        question9 = findViewById(R.id.question9);
        question10 = findViewById(R.id.question10);
        question11_text = findViewById(R.id.question11_text);
        question12_text = findViewById(R.id.question12_text);
        question13 = findViewById(R.id.question13);
        question14_text = findViewById(R.id.question14_text);
        question15_text = findViewById(R.id.question15_text);
        question16_text = findViewById(R.id.question16_text);
        question17_text = findViewById(R.id.question17_text);
        question18_text = findViewById(R.id.question18_text);
        question19_text = findViewById(R.id.question19_text);
        question20_text = findViewById(R.id.question20_text);
        question21_text = findViewById(R.id.question21_text);
        question22_text = findViewById(R.id.question22_text);
        question23_text = findViewById(R.id.question23_text);
        question24_text = findViewById(R.id.question24_text);
        question25_text = findViewById(R.id.question25_text);
        question26_text = findViewById(R.id.question26_text);
        question27_text = findViewById(R.id.question27_text);
        question28_text = findViewById(R.id.question28_text);
        question29_text = findViewById(R.id.question29_text);
        question30_text = findViewById(R.id.question30_text);
        question31_text = findViewById(R.id.question31_text);
        question32_text = findViewById(R.id.question32_text);
        question33_text = findViewById(R.id.question33_text);
        question34_text = findViewById(R.id.question34_text);
        question35_text = findViewById(R.id.question35_text);
        question36_text = findViewById(R.id.question36_text);
        question37text = findViewById(R.id.question37text);
        question38_text = findViewById(R.id.question38_text);
        question39_text = findViewById(R.id.question39_text);
        question40_text = findViewById(R.id.question40_text);
        question41_text = findViewById(R.id.question41_text);
        question42_text = findViewById(R.id.question42_text);
        question43_text = findViewById(R.id.question43_text);
        question44_text = findViewById(R.id.question44_text);
        question45_text = findViewById(R.id.question45_text);
        question46_text = findViewById(R.id.question46_text);
        question47_text = findViewById(R.id.question47_text);
        question48_text = findViewById(R.id.question48_text);
        question49_text = findViewById(R.id.question49_text);
        question50_text = findViewById(R.id.question50_text);
        question51_text = findViewById(R.id.question51_text);
        question52_text = findViewById(R.id.question52_text);
        question53_text = findViewById(R.id.question53_text);
        question54_text = findViewById(R.id.question54_text);
        question55_text = findViewById(R.id.question55_text);
        question56_text = findViewById(R.id.question56_text);
        question57_text = findViewById(R.id.question57_text);
        question58_text = findViewById(R.id.question58_text);
        question59_text = findViewById(R.id.question59_text);
        question60_text = findViewById(R.id.question60_text);
        question61_text = findViewById(R.id.question61_text);
        question62_text = findViewById(R.id.question62_text);
        question63_text = findViewById(R.id.question63_text);
        question64_text = findViewById(R.id.question64_text);
        question65_text = findViewById(R.id.question65_text);
        question66_text = findViewById(R.id.question66_text);
        question67_text = findViewById(R.id.question67_text);
        question68_text = findViewById(R.id.question68_text);
        question69_text = findViewById(R.id.question69_text);
        question70_text = findViewById(R.id.question70_text);
        question71_text = findViewById(R.id.question71_text);
        question72_text = findViewById(R.id.question72_text);
        question73_text = findViewById(R.id.question73_text);
        question74_text = findViewById(R.id.question74_text);
        question75_text = findViewById(R.id.question75_text);
        question76_text = findViewById(R.id.question76_text);
        question77_text = findViewById(R.id.question77_text);
        question78_text = findViewById(R.id.question78_text);
        question79_text = findViewById(R.id.question79_text);
        question80_text = findViewById(R.id.question80_text);
        txtSaveRisk = findViewById(R.id.txtSaveRisk);



        dropDownQuestion11(autoCompleteQuestion11Txt);
        dropDownQuestion10(txtQuestion10);

        dropDownQuestion20(txtQuestion20);
        dropDownQuestion21(txtQuestion21);
        dropDownQuestion24(txtQuestion24);

        dropDownQuestion31(txtQuestion31);
        dropDownQuestion37(txtQuestion37);
        dropDownQuestion35(txtQuestion35);

        dropDownQuestion40(txtQuestion40);


        txtriskassesment = findViewById(R.id.txt_riskassesment);







        updateButtonLabels();

    }




    private void initializeLangForArray() {
        String selectedLanguage = getSelectedLanguage();
        if (selectedLanguage.equals("English")) {
            //not required
        } else {
            //for normal spinner
            PlantationArr=getArrayData(PlantationArr,selectedLanguage);
            YesArr=getArrayData(YesArr,selectedLanguage);
            WorkershiredArr=getArrayData(WorkershiredArr,selectedLanguage);
            YesorNoArr=getArrayData(YesorNoArr,selectedLanguage);
            WorkinghoursArr = getArrayData(WorkinghoursArr,selectedLanguage);
            VacationArr = getArrayData(VacationArr,selectedLanguage);
            daysArr = getArrayData(daysArr,selectedLanguage);
            childrendetailsArr = getArrayData(childrendetailsArr,selectedLanguage);
            nightInjuryyesornoArr = getArrayData(nightInjuryyesornoArr,selectedLanguage);
            PersonalEquipmentArr = getArrayData(PersonalEquipmentArr,selectedLanguage);
            cropsArr = getArrayData(cropsArr,selectedLanguage);
            ans62Arr = getArrayData(ans62Arr,selectedLanguage);
            ans63Arr = getArrayData(ans63Arr,selectedLanguage);
            ans64Arr = getArrayData(ans64Arr,selectedLanguage);
            ans62Arr = getArrayData(ans62Arr,selectedLanguage);
            ans63Arr = getArrayData(ans63Arr,selectedLanguage);
            ans64Arr = getArrayData(ans64Arr,selectedLanguage);
            ans65Arr = getArrayData(ans65Arr,selectedLanguage);
            ans66Arr = getArrayData(ans66Arr,selectedLanguage);
            ans67Arr = getArrayData(ans67Arr,selectedLanguage);
            ans68Arr = getArrayData(ans68Arr,selectedLanguage);
            ans69Arr = getArrayData(ans69Arr,selectedLanguage);
            ans70Arr = getArrayData(ans70Arr,selectedLanguage);
            ans71Arr = getArrayData(ans71Arr,selectedLanguage);
            ans72Arr = getArrayData(ans72Arr,selectedLanguage);
            ans73Arr = getArrayData(ans73Arr,selectedLanguage);
            ans74Arr = getArrayData(ans74Arr,selectedLanguage);
            ans75Arr = getArrayData(ans75Arr,selectedLanguage);
            ans76Arr = getArrayData(ans76Arr,selectedLanguage);
            ans77Arr = getArrayData(ans78Arr,selectedLanguage);
            ans79Arr = getArrayData(ans79Arr,selectedLanguage);
            ans80Arr = getArrayData(ans80Arr,selectedLanguage);

            //for multi select spinner
            JobtypeArr=getArrayData(JobtypeArr,selectedLanguage);
            PlantationArr=getArrayData(PlantationArr,selectedLanguage);
            MontlyincomeArr = getArrayData(MontlyincomeArr,selectedLanguage);
            wagecalculatorArr = getArrayData(wagecalculatorArr,selectedLanguage);
            WorkerscomplaintsArr = getArrayData(WorkerscomplaintsArr,selectedLanguage);
            InjuriesArr = getArrayData(InjuriesArr,selectedLanguage);
            EquipmentArr = getArrayData(EquipmentArr,selectedLanguage);
            PersonalEquipmentlistArr = getArrayData(PersonalEquipmentlistArr,selectedLanguage);
            chemicalsArr = getArrayData(chemicalsArr,selectedLanguage);
            ActivitiesArr = getArrayData(ActivitiesArr,selectedLanguage);



        }
    }

    private String[] getArrayData(String[] arr,String selectedLanguage){
        for (int i=0;i<arr.length;i++){
            arr[i]=getLanguageFromLocalDb(selectedLanguage,arr[i].toString().trim());
        }
        return arr;
    }

    private void saveDataFun() {
        //Save
        txtSave.setOnClickListener(v->{
            String dateTime = appHelper.getCurrentDateTime(AppConstant.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);

            //ans1
            if (!et_answer1.getText().toString().isEmpty()){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(1);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(et_answer1.getText().toString());

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans2
            if (!et_answer2.getText().toString().isEmpty()){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(2);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(et_answer2.getText().toString());

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans3
            if (ans3!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(7);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans3);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans4
            if (!etAns4.getText().toString().isEmpty()){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(3);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(etAns4.getText().toString());

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans5
            if (!etAns5.getText().toString().isEmpty()){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(13);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(etAns5.getText().toString());

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans6
            if (!et_answer6.getText().toString().isEmpty()){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(14);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(et_answer6.getText().toString());

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans7
            if (ans7!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(15);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans7);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans8
            if (ans8!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(16);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans8);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans9
            if (ans9!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(17);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans9);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans10
            if (!txtQuestion10.getText().toString().isEmpty() && !txtQuestion10.getText().equals("select")){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(18);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(txtQuestion10.getText().toString().trim());

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans11
            //  if (ans11!=null){

            if(!autoCompleteQuestion11Txt.getText().toString().isEmpty() && !autoCompleteQuestion11Txt.getText().equals("select")){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(19);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(autoCompleteQuestion11Txt.getText().toString());
                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans12
            if (ans12!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(20);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans12);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans13
            if (ans13!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(24);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans13);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans14
            if (ans14!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(25);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans14);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans15
            if (ans15!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(26);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans15);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans16
            if (ans16!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(27);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans16);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans17
            if (ans17!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(29);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans17);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans18
            if (ans18!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(30);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans18);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans19
            if (ans19!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(31);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans19);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans20
          //  if (ans20!=null){
            if(!txtQuestion20.getText().toString().isEmpty() && !txtQuestion20.getText().equals("select")){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(32);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(txtQuestion20.getText().toString().trim());

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans21
            if(!txtQuestion21.getText().toString().isEmpty() && !txtQuestion21.getText().equals("select")){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(33);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(txtQuestion21.getText().toString().trim());

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans22
            if (ans22!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(37);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans22);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans23
            if (ans23!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(38);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans23);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans24
            if(!txtQuestion24.getText().toString().isEmpty() && !txtQuestion24.getText().equals("select")){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(39);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(txtQuestion24.getText().toString().trim() );
                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans25
            if (ans25!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(40);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans25);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans26
            if (ans26!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(41);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans26);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans27
            if (ans26!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(42);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans27);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans28
            if (ans28!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(43);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans28);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans29
            if (ans29!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(44);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans29);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans30
            if (ans30!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(45);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans30);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans31
            if(!txtQuestion31.getText().toString().isEmpty() && !txtQuestion31.getText().equals("select")){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(47);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(txtQuestion31.getText().toString().trim());

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans32
            if (ans32!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(48);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans32);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans33
            if (ans33!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(49);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans33);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans34
            if (ans34!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(50);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans34);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans35
            if(!txtQuestion35.getText().toString().isEmpty() && !txtQuestion35.getText().equals("select")){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(51);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(txtQuestion35.getText().toString().trim());

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans36
            if (ans36!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(52);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans36);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans37
            if(!txtQuestion37.getText().toString().isEmpty() && !txtQuestion37.getText().equals("select")){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(53);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(txtQuestion37.getText().toString().trim());

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans38
            if (ans38!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(54);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans38);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans39
            if (ans39!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(55);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans39);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans40
            if(!txtQuestion40.getText().toString().isEmpty() && !txtQuestion40.getText().equals("select")){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(8);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(txtQuestion40.getText().toString().trim());

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans41
            if (ans41!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(57);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans41);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans42
            if (!etAns42.getText().toString().isEmpty()){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(58);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(etAns42.getText().toString());

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans43
            if (ans43!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(9);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans43);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans44
            if (ans44!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(10);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans44);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans45
            if (!etAns45.getText().toString().isEmpty()){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(59);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(etAns45.getText().toString());

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans46
            if (!et_answer46.getText().toString().isEmpty()){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(60);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(et_answer46.getText().toString());

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans47
            if (!etAns47.getText().toString().isEmpty()){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(61);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(etAns47.getText().toString());

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans48
            if (!et_answer48.getText().toString().isEmpty()){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(4);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(et_answer48.getText().toString());

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans49
            if (!etAns49.getText().toString().isEmpty()){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(5);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(etAns49.getText().toString());

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans50
            if (!etAns50.getText().toString().isEmpty()){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(21);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(etAns50.getText().toString());

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans51
            if (!et_answer51.getText().toString().isEmpty()){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(22);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(et_answer51.getText().toString());

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans52
            if (!et_answer52.getText().toString().isEmpty()){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(23);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(et_answer52.getText().toString());

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans53
            if (!etAns53.getText().toString().isEmpty()){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(46);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(etAns53.getText().toString());

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans54
            if (!et_answer54.getText().toString().isEmpty()){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(28);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(et_answer54.getText().toString());

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans55
            if (!et_answer55.getText().toString().isEmpty()){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(34);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(et_answer55.getText().toString());

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans56
            if (!etAns56.getText().toString().isEmpty()){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(35);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(etAns56.getText().toString());

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans57
            if (!etAns57.getText().toString().isEmpty()){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(36);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(etAns57.getText().toString());

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans58
            if (!et_answer58.getText().toString().isEmpty()){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(11);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(et_answer58.getText().toString());

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans59
            if (!etAns59.getText().toString().isEmpty()){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(56);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(etAns59.getText().toString());

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans60
            if (!et_answer60.getText().toString().isEmpty()){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(12);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(et_answer60.getText().toString());

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans61
            if (!et_answer61.getText().toString().isEmpty()){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(6);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(et_answer61.getText().toString());

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans62
            if (ans62!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(63);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans62);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans63
            if (ans63!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(64);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans63);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }


            //ans64
            if (ans64!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(65);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans64);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans65
            if (ans65!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(66);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans65);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans66
            if (ans66!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(67);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans66);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans67
            if (ans67!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(72);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans67);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans68
            if (ans68!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(73);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans68);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans69
            if (ans69!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(74);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans69);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans70
            if (ans70!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(75);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans70);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans71
            if (ans71!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(76);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans71);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans72
            if (ans72!=null) {
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(77);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans72);
                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans73
            if (ans73!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(78);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans73);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans74
            if (ans74!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(79);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans74);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans75
            if (ans75!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(80);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans75);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans76
            if (ans76!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(81);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans76);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans77
            if (ans77!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(82);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans77);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans78
            if (ans78!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(83);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans78);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans79
            if (ans79!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(84);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans79);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }

            //ans80
            if (ans80!=null){
                RiskAssessment risk=new RiskAssessment();
                risk.setRiskAssesmentQuestionHdrId(85);
                risk.setFarmerCode(farmerCode);
                risk.setAnswers(ans80);

                risk.setIsActive("true");
                risk.setSync(false);
                risk.setServerSync("0");
                risk.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                risk.setCreatedDate(dateTime);
                risk.setUpdatedDate(dateTime);

                riskAssessmentArrayList.add(risk);
            }


            //Saving survey list
            if (riskAssessmentArrayList.size()>0){
                for (int i=0;i<riskAssessmentArrayList.size();i++){
                    viewModel.insertRiskAssessmentDataLocalDB(riskAssessmentArrayList.get(i));
                }
            }

            if (riskAssessmentArrayList.size()>0){
                Toast.makeText(this, "Saved!!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Fill survey before saving!!", Toast.LENGTH_SHORT).show();
//                finish();
            }

        });
    }

    private String getSelectedLanguage() {
        return preferences.getString("selected_language", "English");
    }


    private void updateButtonLabels() {
        String selectedLanguage = getSelectedLanguage();

        String hdQ1=getResources().getString(R.string.question1);
        String hdQ2=getResources().getString(R.string.question2);
        String hdQ3=getResources().getString(R.string.question3);
        String hdQ4=getResources().getString(R.string.question4);
        String hdQ5=getResources().getString(R.string.question5);
        String hdQ6=getResources().getString(R.string.question6);
        String hdQ7=getResources().getString(R.string.question7);
        String hdQ8=getResources().getString(R.string.question8);
        String hdQ9=getResources().getString(R.string.question9);
        String hdQ10=getResources().getString(R.string.question10);
        String hdQ11=getResources().getString(R.string.question11);
        String hdQ12=getResources().getString(R.string.question12);
        String hdQ13=getResources().getString(R.string.question13);
        String hdQ14=getResources().getString(R.string.question14);
        String hdQ15=getResources().getString(R.string.question15);
        String hdQ16=getResources().getString(R.string.question16);
        String hdQ17=getResources().getString(R.string.question17);
        String hdQ18=getResources().getString(R.string.question18);
        String hdQ19=getResources().getString(R.string.question19);
        String hdQ20=getResources().getString(R.string.question20);
        String hdQ21=getResources().getString(R.string.question21);
        String hdQ22=getResources().getString(R.string.question22);
        String hdQ23=getResources().getString(R.string.question23);
        String hdQ24=getResources().getString(R.string.question24);
        String hdQ25=getResources().getString(R.string.question25);
        String hdQ26=getResources().getString(R.string.question26);
        String hdQ27=getResources().getString(R.string.question27);
        String hdQ28=getResources().getString(R.string.question28);
        String hdQ29=getResources().getString(R.string.question29);
        String hdQ30=getResources().getString(R.string.question30);
        String hdQ31=getResources().getString(R.string.question31);
        String hdQ32=getResources().getString(R.string.question32);
        String hdQ33=getResources().getString(R.string.question33);
        String hdQ34=getResources().getString(R.string.question34);
        String hdQ35=getResources().getString(R.string.question35);
        String hdQ36=getResources().getString(R.string.question36);
        String hdQ37=getResources().getString(R.string.question37);
        String hdQ38=getResources().getString(R.string.question38);
        String hdQ39=getResources().getString(R.string.question39);
        String hdQ40=getResources().getString(R.string.question40);
        String hdQ41=getResources().getString(R.string.question41);
        String hdQ42=getResources().getString(R.string.question42);
        String hdQ43=getResources().getString(R.string.question43);
        String hdQ44=getResources().getString(R.string.question44);
        String hdQ45=getResources().getString(R.string.question45);
        String hdQ46=getResources().getString(R.string.question46);
        String hdQ47=getResources().getString(R.string.question47);
        String hdQ48=getResources().getString(R.string.question48);
        String hdQ49=getResources().getString(R.string.question49);
        String hdQ50=getResources().getString(R.string.question50);
        String hdQ51=getResources().getString(R.string.question51);
        String hdQ52=getResources().getString(R.string.question52);
        String hdQ53=getResources().getString(R.string.question53);
        String hdQ54=getResources().getString(R.string.question54);
        String hdQ55=getResources().getString(R.string.question55);
        String hdQ56=getResources().getString(R.string.question56);
        String hdQ57=getResources().getString(R.string.question57);
        String hdQ58=getResources().getString(R.string.question58);
        String hdQ59=getResources().getString(R.string.question59);
        String hdQ60=getResources().getString(R.string.question60);
        String hdQ61=getResources().getString(R.string.question61);

        String hdQ62=getResources().getString(R.string.question62);
        String hdQ63=getResources().getString(R.string.question63);
        String hdQ64=getResources().getString(R.string.question64);
        String hdQ65=getResources().getString(R.string.question65);
        String hdQ66=getResources().getString(R.string.question66);
        String hdQ67=getResources().getString(R.string.question67);
        String hdQ68=getResources().getString(R.string.question68);
        String hdQ69=getResources().getString(R.string.question69);
        String hdQ70=getResources().getString(R.string.question70);
        String hdQ71=getResources().getString(R.string.question71);
        String hdQ72=getResources().getString(R.string.question72);
        String hdQ73=getResources().getString(R.string.question73);
        String hdQ74=getResources().getString(R.string.question74);
        String hdQ75=getResources().getString(R.string.question75);
        String hdQ76=getResources().getString(R.string.question76);
        String hdQ77=getResources().getString(R.string.question77);
        String hdQ78=getResources().getString(R.string.question78);
        String hdQ79=getResources().getString(R.string.question79);
        String hdQ80=getResources().getString(R.string.question80);

        String hdSave=getResources().getString(R.string.save);

        String txtselect =getResources().getString(R.string.select);
        String hdriskassesment = getResources().getString(R.string.risk_assesment);


        String AQ1b = getResources().getString(R.string.enter_distance);
        String AQ2b = getResources().getString(R.string.enter_size);
        String AQEnteraswr = getResources().getString(R.string.enter_your_answer);



        if (selectedLanguage.equals("English")) {
            question1_text.setText(hdQ1);
            question2.setText(hdQ2);
            question3_text.setText(hdQ3);
            question4.setText(hdQ4);
            question5_text.setText(hdQ5);
            question6_text.setText(hdQ6);
            question7_text.setText(hdQ7);
            question8_text.setText(hdQ8);
            question9.setText(hdQ9);
            question10.setText(hdQ10);
            question11_text.setText(hdQ11);
            question12_text.setText(hdQ12);
            question13.setText(hdQ13);
            question14_text.setText(hdQ14);
            question15_text.setText(hdQ15);
            question16_text.setText(hdQ16);
            question17_text.setText(hdQ17);
            question18_text.setText(hdQ18);
            question19_text.setText(hdQ19);
            question20_text.setText(hdQ20);
            question21_text.setText(hdQ21);
            question22_text.setText(hdQ22);
            question23_text.setText(hdQ23);
            question24_text.setText(hdQ24);
            question25_text.setText(hdQ25);
            question26_text.setText(hdQ26);
            question27_text.setText(hdQ27);
            question28_text.setText(hdQ28);
            question29_text.setText(hdQ29);
            question30_text.setText(hdQ30);
            question31_text.setText(hdQ31);
            question32_text.setText(hdQ32);
            question33_text.setText(hdQ33);
            question34_text.setText(hdQ34);
            question35_text.setText(hdQ35);
            question36_text.setText(hdQ36);
            question37text.setText(hdQ37);
            question38_text.setText(hdQ38);
            question39_text.setText(hdQ39);
            question40_text.setText(hdQ40);
            question41_text.setText(hdQ41);
            question42_text.setText(hdQ42);
            question43_text.setText(hdQ43);
            question44_text.setText(hdQ44);
            question45_text.setText(hdQ45);
            question46_text.setText(hdQ46);
            question47_text.setText(hdQ47);
            question48_text.setText(hdQ48);
            question49_text.setText(hdQ49);
            question50_text.setText(hdQ50);
            question51_text.setText(hdQ51);
            question52_text.setText(hdQ52);
            question53_text.setText(hdQ53);
            question54_text.setText(hdQ54);
            question55_text.setText(hdQ55);
            question56_text.setText(hdQ56);
            question57_text.setText(hdQ57);
            question58_text.setText(hdQ58);
            question59_text.setText(hdQ59);
            question60_text.setText(hdQ60);
            question61_text.setText(hdQ61);

            question62_text.setText(hdQ62);
            question63_text.setText(hdQ63);
            question64_text.setText(hdQ64);
            question65_text.setText(hdQ65);
            question66_text.setText(hdQ66);
            question67_text.setText(hdQ67);
            question68_text.setText(hdQ68);
            question69_text.setText(hdQ69);
            question70_text.setText(hdQ70);
            question71_text.setText(hdQ71);
            question72_text.setText(hdQ72);
            question73_text.setText(hdQ73);
            question74_text.setText(hdQ74);
            question75_text.setText(hdQ75);
            question76_text.setText(hdQ76);
            question77_text.setText(hdQ77);
            question78_text.setText(hdQ78);
            question79_text.setText(hdQ79);
            question80_text.setText(hdQ80);

            txtSaveRisk.setText(hdSave);

            txtQuestion24.setText(txtselect);
            autoCompleteQuestion11Txt.setText(txtselect);
            txtQuestion10.setText(txtselect);
            txtQuestion20.setText(txtselect);
            txtQuestion21.setText(txtselect);
            txtQuestion24.setText(txtselect);
            txtQuestion31.setText(txtselect);
            txtQuestion35.setText(txtselect);
            txtQuestion37.setText(txtselect);
            txtQuestion40.setText(txtselect);


            et_answer1.setHint(AQ1b);
            et_answer2.setHint(AQ2b);
            etAns4.setHint(AQEnteraswr);
            etAns5.setHint(AQEnteraswr);
            et_answer6.setHint( AQEnteraswr);
            etAns42.setHint( AQEnteraswr);
            etAns45.setHint( AQEnteraswr);
            et_answer46.setHint( AQEnteraswr);
            etAns47.setHint( AQEnteraswr);
            et_answer48.setHint( AQEnteraswr);
            etAns49.setHint( AQEnteraswr);
            etAns50.setHint( AQEnteraswr);
            et_answer51.setHint( AQEnteraswr);
            et_answer52.setHint( AQEnteraswr);

            etAns53.setHint( AQEnteraswr);
            et_answer54.setHint( AQEnteraswr);
            et_answer55.setHint( AQEnteraswr);
            etAns56.setHint( AQEnteraswr);
            etAns57.setHint( AQEnteraswr);
            et_answer58.setHint( AQEnteraswr);
            etAns59.setHint( AQEnteraswr);
            et_answer60.setHint( AQEnteraswr);
            et_answer61.setHint(AQEnteraswr);

            txtriskassesment.setText(hdriskassesment);






        } else {
            //Todo : commented as not added in DB
            question1_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ1));
            question2.setText(getLanguageFromLocalDb(selectedLanguage,hdQ2));
            question3_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ3));
            question4.setText(getLanguageFromLocalDb(selectedLanguage,hdQ4));
            question5_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ5));
            question6_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ6));
            question7_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ7));
            question8_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ8));
            question9.setText(getLanguageFromLocalDb(selectedLanguage,hdQ9));
            question10.setText(getLanguageFromLocalDb(selectedLanguage,hdQ10));
            question11_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ11));
            question12_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ12));
            question13.setText(getLanguageFromLocalDb(selectedLanguage,hdQ13));
            question14_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ14));
            question15_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ15));
            question16_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ16));
            question17_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ17));
            question18_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ18));
            question19_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ19));
            question20_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ20));
            question21_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ21));
            question22_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ22));
            question23_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ23));
            question24_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ24));
            question25_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ25));
            question26_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ26));
            question27_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ27));
            question28_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ28));
            question29_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ29));
            question30_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ30));
            question31_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ31));
            question32_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ32));
            question33_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ33));
            question34_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ34));
            question35_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ35));
            question36_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ36));
            question37text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ37));
            question38_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ38));
            question39_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ39));
            question40_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ40));
            question41_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ41));
            question42_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ42));
            question43_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ43));
            question44_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ44));
            question45_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ45));
            question46_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ46));
            question47_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ47));
            question48_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ48));
            question49_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ49));
            question50_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ50));
            question51_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ51));
            question52_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ52));
            question53_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ53));
            question54_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ54));
            question55_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ55));
            question56_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ56));
            question57_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ57));
            question58_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ58));
            question59_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ59));
            question60_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ60));
            question61_text.setText(getLanguageFromLocalDb(selectedLanguage,hdQ61));

            //New added in DB
            question62_text.setText(getLanguageFromLocalDb(selectedLanguage, hdQ62));
            question63_text.setText(getLanguageFromLocalDb(selectedLanguage, hdQ63));
            question64_text.setText(getLanguageFromLocalDb(selectedLanguage, hdQ64));
            question65_text.setText(getLanguageFromLocalDb(selectedLanguage, hdQ65));
            question66_text.setText(getLanguageFromLocalDb(selectedLanguage, hdQ66));
            question67_text.setText(getLanguageFromLocalDb(selectedLanguage, hdQ67));
            question68_text.setText(getLanguageFromLocalDb(selectedLanguage, hdQ68));
            question69_text.setText(getLanguageFromLocalDb(selectedLanguage, hdQ69));
            question70_text.setText(getLanguageFromLocalDb(selectedLanguage, hdQ70));
            question71_text.setText(getLanguageFromLocalDb(selectedLanguage, hdQ71));
            question72_text.setText(getLanguageFromLocalDb(selectedLanguage, hdQ72));
            question73_text.setText(getLanguageFromLocalDb(selectedLanguage, hdQ73));
            question74_text.setText(getLanguageFromLocalDb(selectedLanguage, hdQ74));
            question75_text.setText(getLanguageFromLocalDb(selectedLanguage, hdQ75));
            question76_text.setText(getLanguageFromLocalDb(selectedLanguage, hdQ76));
            question77_text.setText(getLanguageFromLocalDb(selectedLanguage, hdQ77));
            question78_text.setText(getLanguageFromLocalDb(selectedLanguage, hdQ78));
            question79_text.setText(getLanguageFromLocalDb(selectedLanguage, hdQ79));
            question80_text.setText(getLanguageFromLocalDb(selectedLanguage, hdQ80));

            txtSaveRisk.setText(getLanguageFromLocalDb(selectedLanguage, hdSave));



            txtQuestion10.setText(getLanguageFromLocalDb(selectedLanguage,txtselect));
            autoCompleteQuestion11Txt.setText(getLanguageFromLocalDb(selectedLanguage,txtselect));

            txtQuestion20.setText(getLanguageFromLocalDb(selectedLanguage,txtselect));
            txtQuestion21.setText(getLanguageFromLocalDb(selectedLanguage,txtselect));
            txtQuestion24.setText(getLanguageFromLocalDb(selectedLanguage,txtselect));
            txtQuestion31.setText(getLanguageFromLocalDb(selectedLanguage,txtselect));
            txtQuestion35.setText(getLanguageFromLocalDb(selectedLanguage,txtselect));
            txtQuestion37.setText(getLanguageFromLocalDb(selectedLanguage,txtselect));
            txtQuestion40.setText(getLanguageFromLocalDb(selectedLanguage,txtselect));



            et_answer1.setHint(getLanguageFromLocalDb(selectedLanguage,AQ1b));
//            et_answer1.setHint(getLanguageFromLocalDb(selectedLanguage, AQ1b));
            et_answer2.setHint(getLanguageFromLocalDb(selectedLanguage, AQ2b));
            etAns4.setHint(getLanguageFromLocalDb(selectedLanguage, AQEnteraswr));
            etAns5.setHint(getLanguageFromLocalDb(selectedLanguage, AQEnteraswr));
            et_answer6.setHint(getLanguageFromLocalDb(selectedLanguage, AQEnteraswr));
            etAns42.setHint(getLanguageFromLocalDb(selectedLanguage, AQEnteraswr));
            etAns45.setHint(getLanguageFromLocalDb(selectedLanguage, AQEnteraswr));
            et_answer46.setHint(getLanguageFromLocalDb(selectedLanguage, AQEnteraswr));
            etAns47.setHint(getLanguageFromLocalDb(selectedLanguage, AQEnteraswr));
            et_answer48.setHint(getLanguageFromLocalDb(selectedLanguage, AQEnteraswr));
            etAns49.setHint(getLanguageFromLocalDb(selectedLanguage, AQEnteraswr));
            etAns50.setHint(getLanguageFromLocalDb(selectedLanguage, AQEnteraswr));
            et_answer51.setHint(getLanguageFromLocalDb(selectedLanguage, AQEnteraswr));
            et_answer52.setHint(getLanguageFromLocalDb(selectedLanguage, AQEnteraswr));

            etAns53.setHint(getLanguageFromLocalDb(selectedLanguage, AQEnteraswr));
            et_answer54.setHint(getLanguageFromLocalDb(selectedLanguage, AQEnteraswr));
            et_answer55.setHint(getLanguageFromLocalDb(selectedLanguage, AQEnteraswr));
            etAns56.setHint(getLanguageFromLocalDb(selectedLanguage, AQEnteraswr));
            etAns57.setHint(getLanguageFromLocalDb(selectedLanguage, AQEnteraswr));
            et_answer58.setHint(getLanguageFromLocalDb(selectedLanguage, AQEnteraswr));
            etAns59.setHint(getLanguageFromLocalDb(selectedLanguage, AQEnteraswr));
            et_answer60.setHint(getLanguageFromLocalDb(selectedLanguage, AQEnteraswr));
            et_answer61.setHint(getLanguageFromLocalDb(selectedLanguage, AQEnteraswr));


            txtriskassesment.setText(getLanguageFromLocalDb(selectedLanguage, hdriskassesment) + "/" + hdriskassesment);











        }

//        switch (selectedLanguage) {
//            case "English":
//                //Default english added
////                question1_text.setText(getString(R.string.question1));
////                question2.setText(getString(R.string.question2));
////                question3_text.setText(getString(R.string.question3));
////                question4.setText(getString(R.string.question4));
////                question5_text.setText(getString(R.string.question5));
////                question6_text.setText(getString(R.string.question6));
////                question7_text.setText(getString(R.string.question7));
////                question8_text.setText(getString(R.string.question8));
////                question9.setText(getString(R.string.question9));
////                question10.setText(getString(R.string.question10));
////                question11_text.setText(getString(R.string.question11));
////                question12_text.setText(getString(R.string.question12));
////                question13.setText(getString(R.string.question13));
////                question14_text.setText(getString(R.string.question14));
////                question15_text.setText(getString(R.string.question15));
////                question16_text.setText(getString(R.string.question16));
////                question17_text.setText(getString(R.string.question17));
////                question18_text.setText(getString(R.string.question18));
////                question19_text.setText(getString(R.string.question19));
////                question20_text.setText(getString(R.string.question20));
////                question21_text.setText(getString(R.string.question21));
////                question22_text.setText(getString(R.string.question22));
////                question23_text.setText(getString(R.string.question23));
////                question24_text.setText(getString(R.string.question24));
////                question25_text.setText(getString(R.string.question25));
////                question26_text.setText(getString(R.string.question26));
////                question27_text.setText(getString(R.string.question27));
////                question28_text.setText(getString(R.string.question28));
////                question29_text.setText(getString(R.string.question29));
////                question30_text.setText(getString(R.string.question30));
////                question31_text.setText(getString(R.string.question31));
////                question32_text.setText(getString(R.string.question32));
////                question33_text.setText(getString(R.string.question33));
////                question34_text.setText(getString(R.string.question34));
////                question35_text.setText(getString(R.string.question35));
////                question36_text.setText(getString(R.string.question36));
////                question37text.setText(getString(R.string.question37));
////                question38_text.setText(getString(R.string.question38));
////                question39_text.setText(getString(R.string.question39));
////                question40_text.setText(getString(R.string.question40));
////                question41_text.setText(getString(R.string.question41));
////                question42_text.setText(getString(R.string.question42));
////                question43_text.setText(getString(R.string.question43));
////                question44_text.setText(getString(R.string.question44));
////                question45_text.setText(getString(R.string.question45));
////                question46_text.setText(getString(R.string.question46));
////                question47_text.setText(getString(R.string.question47));
////                question48_text.setText(getString(R.string.question48));
////                question49_text.setText(getString(R.string.question49));
////                question50_text.setText(getString(R.string.question50));
////                question51_text.setText(getString(R.string.question51));
////                question52_text.setText(getString(R.string.question52));
////                question53_text.setText(getString(R.string.question53));
////                question54_text.setText(getString(R.string.question54));
////                question55_text.setText(getString(R.string.question55));
////                question56_text.setText(getString(R.string.question56));
////                question57_text.setText(getString(R.string.question57));
////                question58_text.setText(getString(R.string.question58));
////                question59_text.setText(getString(R.string.question59));
////                question60_text.setText(getString(R.string.question60));
////                question61_text.setText(getString(R.string.question61));
////
////                txtSaveRisk.setText(getString(R.string.save));
//                break;
//            case "Hindi":
//                question1_text.setText(getString(R.string.question1_H));
//                question2.setText(getString(R.string.question2_H));
//                question3_text.setText(getString(R.string.question3_H));
//                question4.setText(getString(R.string.question4_H));
//                question5_text.setText(getString(R.string.question5_H));
//                question6_text.setText(getString(R.string.question6_H));
//                question7_text.setText(getString(R.string.question7_H));
//                question8_text.setText(getString(R.string.question8_H));
//                question9.setText(getString(R.string.question9_H));
//                question10.setText(getString(R.string.question10_H));
//                question11_text.setText(getString(R.string.question11_H));
//                question12_text.setText(getString(R.string.question12_H));
//                question13.setText(getString(R.string.question13_H));
//                question14_text.setText(getString(R.string.question14_H));
//                question15_text.setText(getString(R.string.question15_H));
//                question16_text.setText(getString(R.string.question16_H));
//                question17_text.setText(getString(R.string.question17_H));
//                question18_text.setText(getString(R.string.question18_H));
//                question19_text.setText(getString(R.string.question19_H));
//                question20_text.setText(getString(R.string.question20_H));
//                question21_text.setText(getString(R.string.question21_H));
//                question22_text.setText(getString(R.string.question22_H));
//                question23_text.setText(getString(R.string.question23_H));
//                question24_text.setText(getString(R.string.question24_H));
//                question25_text.setText(getString(R.string.question25_H));
//                question26_text.setText(getString(R.string.question26_H));
//                question27_text.setText(getString(R.string.question27_H));
//                question28_text.setText(getString(R.string.question28_H));
//                question29_text.setText(getString(R.string.question29_H));
//                question30_text.setText(getString(R.string.question30_H));
//                question31_text.setText(getString(R.string.question31_H));
//                question32_text.setText(getString(R.string.question32_H));
//                question33_text.setText(getString(R.string.question33_H));
//                question34_text.setText(getString(R.string.question34_H));
//                question35_text.setText(getString(R.string.question35_H));
//                question36_text.setText(getString(R.string.question36_H));
//                question37text.setText(getString(R.string.question37_H));
//                question38_text.setText(getString(R.string.question38_H));
//                question39_text.setText(getString(R.string.question39_H));
//                question40_text.setText(getString(R.string.question40_H));
//                question41_text.setText(getString(R.string.question41_H));
//                question42_text.setText(getString(R.string.question42_H));
//                question43_text.setText(getString(R.string.question43_H));
//                question44_text.setText(getString(R.string.question44_H));
//                question45_text.setText(getString(R.string.question45_H));
//                question46_text.setText(getString(R.string.question46_H));
//                question47_text.setText(getString(R.string.question47_H));
//                question48_text.setText(getString(R.string.question48_H));
//                question49_text.setText(getString(R.string.question49_H));
//                question50_text.setText(getString(R.string.question50_H));
//                question51_text.setText(getString(R.string.question51_H));
//                question52_text.setText(getString(R.string.question52_H));
//                question53_text.setText(getString(R.string.question53_H));
//                question54_text.setText(getString(R.string.question54_H));
//                question55_text.setText(getString(R.string.question55_H));
//                question56_text.setText(getString(R.string.question56_H));
//                question57_text.setText(getString(R.string.question57_H));
//                question58_text.setText(getString(R.string.question58_H));
//                question59_text.setText(getString(R.string.question59_H));
//                question60_text.setText(getString(R.string.question60_H));
//                question61_text.setText(getString(R.string.question61_H));
//                txtSaveRisk.setText(getString(R.string.save_H));
//                break;
//            case "Vietnamese":
//                question1_text.setText(getString(R.string.question1_V));
//                question2.setText(getString(R.string.question2_V));
//                question3_text.setText(getString(R.string.question3_V));
//                question4.setText(getString(R.string.question4_V));
//                question5_text.setText(getString(R.string.question5_V));
//                question6_text.setText(getString(R.string.question6_V));
//                question7_text.setText(getString(R.string.question7_V));
//                question8_text.setText(getString(R.string.question8_V));
//                question9.setText(getString(R.string.question9_V));
//                question10.setText(getString(R.string.question10_V));
//                question11_text.setText(getString(R.string.question11_V));
//                question12_text.setText(getString(R.string.question12_V));
//                question13.setText(getString(R.string.question13_V));
//                question14_text.setText(getString(R.string.question14_V));
//                question15_text.setText(getString(R.string.question15_V));
//                question16_text.setText(getString(R.string.question16_V));
//                question17_text.setText(getString(R.string.question17_V));
//                question18_text.setText(getString(R.string.question18_V));
//                question19_text.setText(getString(R.string.question19_V));
//                question20_text.setText(getString(R.string.question20_V));
//                question21_text.setText(getString(R.string.question21_V));
//                question22_text.setText(getString(R.string.question22_V));
//                question23_text.setText(getString(R.string.question23_V));
//                question24_text.setText(getString(R.string.question24_V));
//                question25_text.setText(getString(R.string.question25_V));
//                question26_text.setText(getString(R.string.question26_V));
//                question27_text.setText(getString(R.string.question27_V));
//                question28_text.setText(getString(R.string.question28_V));
//                question29_text.setText(getString(R.string.question29_V));
//                question30_text.setText(getString(R.string.question30_V));
//                question31_text.setText(getString(R.string.question31_V));
//                question32_text.setText(getString(R.string.question32_V));
//                question33_text.setText(getString(R.string.question33_V));
//                question34_text.setText(getString(R.string.question34_V));
//                question35_text.setText(getString(R.string.question35_V));
//                question36_text.setText(getString(R.string.question36_V));
//                question37text.setText(getString(R.string.question37_V));
//                question38_text.setText(getString(R.string.question38_V));
//                question39_text.setText(getString(R.string.question39_V));
//                question40_text.setText(getString(R.string.question40_V));
//                question41_text.setText(getString(R.string.question41_V));
//                question42_text.setText(getString(R.string.question42_V));
//                question43_text.setText(getString(R.string.question43_V));
//                question44_text.setText(getString(R.string.question44_V));
//                question45_text.setText(getString(R.string.question45_V));
//                question46_text.setText(getString(R.string.question46_V));
//                question47_text.setText(getString(R.string.question47_V));
//                question48_text.setText(getString(R.string.question48_V));
//                question49_text.setText(getString(R.string.question49_V));
//                question50_text.setText(getString(R.string.question50_V));
//                question51_text.setText(getString(R.string.question51_V));
//                question52_text.setText(getString(R.string.question52_V));
//                question53_text.setText(getString(R.string.question53_V));
//                question54_text.setText(getString(R.string.question54_V));
//                question55_text.setText(getString(R.string.question55_V));
//                question56_text.setText(getString(R.string.question56_V));
//                question57_text.setText(getString(R.string.question57_V));
//                question58_text.setText(getString(R.string.question58_V));
//                question59_text.setText(getString(R.string.question59_V));
//                question60_text.setText(getString(R.string.question60_V));
//                question61_text.setText(getString(R.string.question61_V));
//                txtSaveRisk.setText(getString(R.string.save_V));
//                break;
//            case "Thai":
//
//                question1_text.setText(getString(R.string.question1_T));
//                question2.setText(getString(R.string.question2_T));
//                question3_text.setText(getString(R.string.question3_T));
//                question4.setText(getString(R.string.question4_T));
//                question5_text.setText(getString(R.string.question5_T));
//                question6_text.setText(getString(R.string.question6_T));
//                question7_text.setText(getString(R.string.question7_T));
//                question8_text.setText(getString(R.string.question8_T));
//                question9.setText(getString(R.string.question9_T));
//                question10.setText(getString(R.string.question10_T));
//                question11_text.setText(getString(R.string.question11_T));
//                question12_text.setText(getString(R.string.question12_T));
//                question13.setText(getString(R.string.question13_T));
//                question14_text.setText(getString(R.string.question14_T));
//                question15_text.setText(getString(R.string.question15_T));
//                question16_text.setText(getString(R.string.question16_T));
//                question17_text.setText(getString(R.string.question17_T));
//                question18_text.setText(getString(R.string.question18_T));
//                question19_text.setText(getString(R.string.question19_T));
//                question20_text.setText(getString(R.string.question20_T));
//                question21_text.setText(getString(R.string.question21_T));
//                question22_text.setText(getString(R.string.question22_T));
//                question23_text.setText(getString(R.string.question23_T));
//                question24_text.setText(getString(R.string.question24_T));
//                question25_text.setText(getString(R.string.question25_T));
//                question26_text.setText(getString(R.string.question26_T));
//                question27_text.setText(getString(R.string.question27_T));
//                question28_text.setText(getString(R.string.question28_T));
//                question29_text.setText(getString(R.string.question29_T));
//                question30_text.setText(getString(R.string.question30_T));
//                question31_text.setText(getString(R.string.question31_T));
//                question32_text.setText(getString(R.string.question32_T));
//                question33_text.setText(getString(R.string.question33_T));
//                question34_text.setText(getString(R.string.question34_T));
//                question35_text.setText(getString(R.string.question35_T));
//                question36_text.setText(getString(R.string.question36_T));
//                question37text.setText(getString(R.string.question37_T));
//                question38_text.setText(getString(R.string.question38_T));
//                question39_text.setText(getString(R.string.question39_T));
//                question40_text.setText(getString(R.string.question40_T));
//                question41_text.setText(getString(R.string.question41_T));
//                question42_text.setText(getString(R.string.question42_T));
//                question43_text.setText(getString(R.string.question43_T));
//                question44_text.setText(getString(R.string.question44_T));
//                question45_text.setText(getString(R.string.question45_T));
//                question46_text.setText(getString(R.string.question46_T));
//                question47_text.setText(getString(R.string.question47_T));
//                question48_text.setText(getString(R.string.question48_T));
//                question49_text.setText(getString(R.string.question49_T));
//                question50_text.setText(getString(R.string.question50_T));
//                question51_text.setText(getString(R.string.question51_T));
//                question52_text.setText(getString(R.string.question52_T));
//                question53_text.setText(getString(R.string.question53_T));
//                question54_text.setText(getString(R.string.question54_T));
//                question55_text.setText(getString(R.string.question55_T));
//                question56_text.setText(getString(R.string.question56_T));
//                question57_text.setText(getString(R.string.question57_T));
//                question58_text.setText(getString(R.string.question58_T));
//                question59_text.setText(getString(R.string.question59_T));
//                question60_text.setText(getString(R.string.question60_T));
//                question61_text.setText(getString(R.string.question61_T));
//                txtSaveRisk.setText(getString(R.string.save_T));
//                break;
//            case "Malay":
//
//                question1_text.setText(getString(R.string.question1_M));
//                question2.setText(getString(R.string.question2_M));
//                question3_text.setText(getString(R.string.question3_M));
//                question4.setText(getString(R.string.question4_M));
//                question5_text.setText(getString(R.string.question5_M));
//                question6_text.setText(getString(R.string.question6_M));
//                question7_text.setText(getString(R.string.question7_M));
//                question8_text.setText(getString(R.string.question8_M));
//                question9.setText(getString(R.string.question9_M));
//                question10.setText(getString(R.string.question10_M));
//                question11_text.setText(getString(R.string.question11_M));
//                question12_text.setText(getString(R.string.question12_M));
//                question13.setText(getString(R.string.question13_M));
//                question14_text.setText(getString(R.string.question14_M));
//                question15_text.setText(getString(R.string.question15_M));
//                question16_text.setText(getString(R.string.question16_M));
//                question17_text.setText(getString(R.string.question17_M));
//                question18_text.setText(getString(R.string.question18_M));
//                question19_text.setText(getString(R.string.question19_M));
//                question20_text.setText(getString(R.string.question20_M));
//                question21_text.setText(getString(R.string.question21_M));
//                question22_text.setText(getString(R.string.question22_M));
//                question23_text.setText(getString(R.string.question23_M));
//                question24_text.setText(getString(R.string.question24_M));
//                question25_text.setText(getString(R.string.question25_M));
//                question26_text.setText(getString(R.string.question26_M));
//                question27_text.setText(getString(R.string.question27_M));
//                question28_text.setText(getString(R.string.question28_M));
//                question29_text.setText(getString(R.string.question29_M));
//                question30_text.setText(getString(R.string.question30_M));
//                question31_text.setText(getString(R.string.question31_M));
//                question32_text.setText(getString(R.string.question32_M));
//                question33_text.setText(getString(R.string.question33_M));
//                question34_text.setText(getString(R.string.question34_M));
//                question35_text.setText(getString(R.string.question35_M));
//                question36_text.setText(getString(R.string.question36_M));
//                question37text.setText(getString(R.string.question37_M));
//                question38_text.setText(getString(R.string.question38_M));
//                question39_text.setText(getString(R.string.question39_M));
//                question40_text.setText(getString(R.string.question40_M));
//                question41_text.setText(getString(R.string.question41_M));
//                question42_text.setText(getString(R.string.question42_M));
//                question43_text.setText(getString(R.string.question43_M));
//                question44_text.setText(getString(R.string.question44_M));
//                question45_text.setText(getString(R.string.question45_M));
//                question46_text.setText(getString(R.string.question46_M));
//                question47_text.setText(getString(R.string.question47_M));
//                question48_text.setText(getString(R.string.question48_M));
//                question49_text.setText(getString(R.string.question49_M));
//                question50_text.setText(getString(R.string.question50_M));
//                question51_text.setText(getString(R.string.question51_M));
//                question52_text.setText(getString(R.string.question52_M));
//                question53_text.setText(getString(R.string.question53_M));
//                question54_text.setText(getString(R.string.question54_M));
//                question55_text.setText(getString(R.string.question55_M));
//                question56_text.setText(getString(R.string.question56_M));
//                question57_text.setText(getString(R.string.question57_M));
//                question58_text.setText(getString(R.string.question58_M));
//                question59_text.setText(getString(R.string.question59_M));
//                question60_text.setText(getString(R.string.question60_M));
//                question61_text.setText(getString(R.string.question61_M));
//                txtSaveRisk.setText(getString(R.string.save_M));
//                break;
//            case "Indonesian":
//                question1_text.setText(getString(R.string.question1_I));
//                question2.setText(getString(R.string.question2_I));
//                question3_text.setText(getString(R.string.question3_I));
//                question4.setText(getString(R.string.question4_I));
//                question5_text.setText(getString(R.string.question5_I));
//                question6_text.setText(getString(R.string.question6_I));
//                question7_text.setText(getString(R.string.question7_I));
//                question8_text.setText(getString(R.string.question8_I));
//                question9.setText(getString(R.string.question9_I));
//                question10.setText(getString(R.string.question10_I));
//                question11_text.setText(getString(R.string.question11_I));
//                question12_text.setText(getString(R.string.question12_I));
//                question13.setText(getString(R.string.question13_I));
//                question14_text.setText(getString(R.string.question14_I));
//                question15_text.setText(getString(R.string.question15_I));
//                question16_text.setText(getString(R.string.question16_I));
//                question17_text.setText(getString(R.string.question17_I));
//                question18_text.setText(getString(R.string.question18_I));
//                question19_text.setText(getString(R.string.question19_I));
//                question20_text.setText(getString(R.string.question20_I));
//                question21_text.setText(getString(R.string.question21_I));
//                question22_text.setText(getString(R.string.question22_I));
//                question23_text.setText(getString(R.string.question23_I));
//                question24_text.setText(getString(R.string.question24_I));
//                question25_text.setText(getString(R.string.question25_I));
//                question26_text.setText(getString(R.string.question26_I));
//                question27_text.setText(getString(R.string.question27_I));
//                question28_text.setText(getString(R.string.question28_I));
//                question29_text.setText(getString(R.string.question29_I));
//                question30_text.setText(getString(R.string.question30_I));
//                question31_text.setText(getString(R.string.question31_I));
//                question32_text.setText(getString(R.string.question32_I));
//                question33_text.setText(getString(R.string.question33_I));
//                question34_text.setText(getString(R.string.question34_I));
//                question35_text.setText(getString(R.string.question35_I));
//                question36_text.setText(getString(R.string.question36_I));
//                question37text.setText(getString(R.string.question37_I));
//                question38_text.setText(getString(R.string.question38_I));
//                question39_text.setText(getString(R.string.question39_I));
//                question40_text.setText(getString(R.string.question40_I));
//                question41_text.setText(getString(R.string.question41_I));
//                question42_text.setText(getString(R.string.question42_I));
//                question43_text.setText(getString(R.string.question43_I));
//                question44_text.setText(getString(R.string.question44_I));
//                question45_text.setText(getString(R.string.question45_I));
//                question46_text.setText(getString(R.string.question46_I));
//                question47_text.setText(getString(R.string.question47_I));
//                question48_text.setText(getString(R.string.question48_I));
//                question49_text.setText(getString(R.string.question49_I));
//                question50_text.setText(getString(R.string.question50_I));
//                question51_text.setText(getString(R.string.question51_I));
//                question52_text.setText(getString(R.string.question52_I));
//                question53_text.setText(getString(R.string.question53_I));
//                question54_text.setText(getString(R.string.question54_I));
//                question55_text.setText(getString(R.string.question55_I));
//                question56_text.setText(getString(R.string.question56_I));
//                question57_text.setText(getString(R.string.question57_I));
//                question58_text.setText(getString(R.string.question58_I));
//                question59_text.setText(getString(R.string.question59_I));
//                question60_text.setText(getString(R.string.question60_I));
//                question61_text.setText(getString(R.string.question61_I));
//                txtSaveRisk.setText(getString(R.string.save_I));
//
//                break;
//        }
    }

    private void initializeUI() {
        imgBack=findViewById(R.id.imgBackRisk);
        txtSave=findViewById(R.id.txtSaveRisk);

        rlQuestion11Txt = findViewById(R.id.rl_question_11);
        //New
        //Edittext
        et_answer1=findViewById(R.id.et_answer1);
        et_answer2 =findViewById(R.id.et_answer2);
        et_answer6=findViewById(R.id.et_answer6);


        et_answer46 = findViewById(R.id.et_answer46);
        et_answer48 = findViewById(R.id.et_answer48);
        et_answer51 = findViewById(R.id.et_answer51);
        et_answer52 = findViewById(R.id.et_answer52);
        et_answer54 = findViewById(R.id.et_answer54);
        et_answer55 = findViewById(R.id.et_answer55);
        et_answer58 = findViewById(R.id.et_answer58);
        et_answer60 = findViewById(R.id.et_answer60);
        et_answer61 = findViewById(R.id.et_answer61);



        //et
        etAns4 = findViewById(R.id.et_answer4);
        etAns5 = findViewById(R.id.et_answer5);
        etAns42 = findViewById(R.id.et_answer42);
        etAns45 = findViewById(R.id.et_answer45);
        etAns47 = findViewById(R.id.et_answer47);
        etAns49 = findViewById(R.id.et_answer49);
        etAns50 = findViewById(R.id.et_answer50);
        etAns53 = findViewById(R.id.et_answer53);
        etAns56 = findViewById(R.id.et_answer56);
        etAns57 = findViewById(R.id.et_answer57);
        etAns59 = findViewById(R.id.et_answer59);
        etAnswer10 = findViewById(R.id.edit_answer);



//        Spinner
        spplantationtime=findViewById(R.id.sp_plantationtime);
        spFamilyWorking=findViewById(R.id.sp_familyworking);
        spEmployPeople =findViewById(R.id.sp_employpeople);
        spWorkershired=findViewById(R.id.sp_workershired);
        spJobtype=findViewById(R.id.sp_jobtype);
        spActivities=findViewById(R.id.spinner_activities);
        spcontract=findViewById(R.id.spinner_contract);
        spforeignworkers=findViewById(R.id.spinner_foreign_workers);
        spworkpermit=findViewById(R.id.spinner_work_permit);
        sppassport=findViewById(R.id.spinner_passports);
        spforeignworkerspayment=findViewById(R.id.spinner_foreign_workers_payment);
        spownearnings =findViewById(R.id.spinner_own_earnings);
        spworkersearnings =findViewById(R.id.spinner_workers_earnings);
        spmontlyincome=findViewById(R.id.spinner_monthly_income);
        spworkersmontlyincome=findViewById(R.id.spinner_workers_monthly_income);
        spwagecalculator=findViewById(R.id.spinner_wage_calculation);
        spworkinghours=findViewById(R.id.spinner_working_hours);
        spworkerworkinghours=findViewById(R.id.spinner_workers_working_hours);
        spWorkerComplaints = findViewById(R.id.spinner_worker_complaints);
        spWorkersUnion = findViewById(R.id.spinner_workers_union);
        spRestDay = findViewById(R.id.spinner_rest_day);
        spQuestion27 = findViewById(R.id.spinner_question27);
        spQuestion28 = findViewById(R.id.spinner_question28);
        spQuestion29 = findViewById(R.id.spinner_question29);
        spQuestion30 = findViewById(R.id.spinner_question30);
        spQuestion31 = findViewById(R.id.spinner_question31);
        spQuestion32 = findViewById(R.id.spinner_question32);
        spQuestion33 = findViewById(R.id.spinner_question33);
        spQuestion34 = findViewById(R.id.spinner_question34);
        spQuestion35 = findViewById(R.id.spinner_question35);
        spQuestion36 = findViewById(R.id.spinner_question36);
        spQuestion37 = findViewById(R.id.spinner_question37);
        spQuestion38 = findViewById(R.id.spinner_question38);
        spQuestion39 = findViewById(R.id.spinner_question39);
        spQuestion40 = findViewById(R.id.spinner_question40);
        spQuestion41 = findViewById(R.id.spinner_question41);
        spQuestion43 = findViewById(R.id.spinner_question43);
        spQuestion44 = findViewById(R.id.spinner_question44);
        question3_number=findViewById(R.id.question3_number);


        autoCompleteQuestion11Txt = findViewById(R.id.txt_data);
        txtQuestion10 = findViewById(R.id.txt_question_10);

        txtQuestion20 = findViewById(R.id.txt_question_20);
        txtQuestion21 = findViewById(R.id.txt_question_21);
        txtQuestion24 = findViewById(R.id.txt_question_24);

        txtQuestion31 = findViewById(R.id.txt_question_31);
        txtQuestion35 = findViewById(R.id.txt_question_35);
        txtQuestion37 = findViewById(R.id.txt_question_37);

        txtQuestion40 = findViewById(R.id.txt_question_40);

        spQuestion62=findViewById(R.id.spinner_question62);
        spQuestion63=findViewById(R.id.spinner_question63);
        spQuestion64=findViewById(R.id.spinner_question64);
        spQuestion65=findViewById(R.id.spinner_question65);
        spQuestion66=findViewById(R.id.spinner_question66);
        spQuestion67=findViewById(R.id.spinner_question67);
        spQuestion68=findViewById(R.id.spinner_question68);
        spQuestion69=findViewById(R.id.spinner_question69);
        spQuestion70=findViewById(R.id.spinner_question70);
        spQuestion71=findViewById(R.id.spinner_question71);
        spQuestion72=findViewById(R.id.spinner_question72);
        spQuestion73=findViewById(R.id.spinner_question73);
        spQuestion74=findViewById(R.id.spinner_question74);
        spQuestion75=findViewById(R.id.spinner_question75);
        spQuestion76=findViewById(R.id.spinner_question76);
        spQuestion77=findViewById(R.id.spinner_question77);
        spQuestion78=findViewById(R.id.spinner_question78);
        spQuestion79=findViewById(R.id.spinner_question79);
        spQuestion80=findViewById(R.id.spinner_question80);




//        dropDownQuestion11(autoCompleteQuestion11Txt,ActivitiesArr);





    }

    private void initializeValues() {
        //
        riskAssessmentArrayList=new ArrayList<>();

        imgBack.setOnClickListener(view->{
            finish();
        });


        //Spinners
        //question3
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, PlantationArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spplantationtime.setAdapter(dataAdapter);
        spplantationtime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans3=null;
                } else {
                    ans3=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //question7
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, YesArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spFamilyWorking.setAdapter(dataAdapter1);
        spFamilyWorking.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans7=null;
                } else {
                    ans7=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question8
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, YesArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spEmployPeople.setAdapter(dataAdapter2);
        spEmployPeople.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans8=null;
                } else {
                    ans8=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question9
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, WorkershiredArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spWorkershired.setAdapter(dataAdapter3);
        spWorkershired.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans9=null;
                } else {
                    ans9=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question10
        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, JobtypeArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spJobtype.setAdapter(dataAdapter4);
        spJobtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans10=null;
                } else {
                    ans10=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });






        //question11
//        ArrayList<StateVO> listVOs = new ArrayList<>();
//        for (int i = 0; i < ActivitiesArr.length; i++) {
//            StateVO stateVO = new StateVO();
//            stateVO.setTitle(ActivitiesArr[i]);
//            stateVO.setSelected(false);
//            listVOs.add(stateVO);
//        }
//       MyAdapter  myAdapter = new MyAdapter(RiskAssessmentActivity.this, 0,
//                listVOs,autoCompleteQuestion11Txt);
//        spActivities.setAdapter(myAdapter);
        //autoCompleteQuestion11Txt.setAdapter(myAdapter);
//         rlQuestion11Txt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                autoCompleteQuestion11Txt.showDropDown();
//            }
//        });
//        autoCompleteQuestion11Txt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                autoCompleteQuestion11Txt.showDropDown();
//            }
//        } );
//
//        autoCompleteQuestion11Txt.setOnItemClickListener((parent, view, position, id) -> {
//         //   String selectedText = (String) parent.getItemAtPosition(position);
//            List<StateVO> selectedItems = myAdapter.getSelectedItems();
//            StringBuilder selectedText = new StringBuilder();
//
//            for (StateVO stateVO : selectedItems) {
//                selectedText.append(stateVO.getTitle()).append(", ");
//            }
//
//            // Remove trailing comma and space
//            if (selectedText.length() > 2) {
//                selectedText.setLength(selectedText.length() - 2);
//            }
//
//            // Display the selected items
//
//            autoCompleteQuestion11Txt.setText(selectedText.toString());
//        });

        spActivities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              //  showCustomDropdownDialog();

                Toast.makeText(RiskAssessmentActivity.this,"data is" + parent.getSelectedItem().toString(),Toast.LENGTH_LONG).show();

                List<StateVO> selectedItems = myAdapter.getSelectedItems();
                StringBuilder selectedText = new StringBuilder();

                for (StateVO stateVO : selectedItems) {
                    selectedText.append(stateVO.getTitle()).append(", ");
                }
                Log.d(TAG, "onItemSelected: selectItems" + selectedText);
                // Remove trailing comma and space
                if (selectedText.length() > 2) {
                    selectedText.setLength(selectedText.length() - 2);
                    Log.d(TAG, "onItemSelected: select" + selectedText);
                }
           //     spActivities.set
               // (  (TextView) findViewById(R.id.txt_data)).setText(selectedText.toString());


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle nothing selected
            }
        });

//        spActivities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if(position==0){
//                    ans11=null;
//                } else {
//                    ans11=parent.getItemAtPosition(position).toString();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });



        //question12
        ArrayAdapter<String> dataAdapter6 = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, YesArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spcontract.setAdapter(dataAdapter6);
        spcontract.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans12=null;
                } else {
                    ans12=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question13
        ArrayAdapter<String> dataAdapter7 = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, YesArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spforeignworkers.setAdapter(dataAdapter7);
        spforeignworkers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans13=null;
                } else {
                    ans13=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question14
        ArrayAdapter<String> dataAdapter8 = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, YesArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spworkpermit.setAdapter(dataAdapter8);
        spworkpermit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans14=null;
                } else {
                    ans14=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question15
        ArrayAdapter<String> dataAdapter9 = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, YesArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        sppassport.setAdapter(dataAdapter9);
        sppassport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans15=null;
                } else {
                    ans15=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question16
        ArrayAdapter<String> dataAdapter10 = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, YesArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spforeignworkerspayment.setAdapter(dataAdapter10);
        spforeignworkerspayment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans16=null;
                } else {
                    ans16=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question17
        ArrayAdapter<String> dataAdapter11 = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, YesorNoArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spownearnings.setAdapter(dataAdapter11);
        spownearnings.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans17=null;
                } else {
                    ans17=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //question18
        ArrayAdapter<String> dataAdapter12 = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, YesorNoArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spworkersearnings.setAdapter(dataAdapter12);
        spworkersearnings.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans18=null;
                } else {
                    ans18=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question19
        ArrayAdapter<String> dataAdapter13 = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, MontlyincomeArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spmontlyincome.setAdapter(dataAdapter13);
        spmontlyincome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans19=null;
                } else {
                    ans19=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question20
        ArrayAdapter<String> dataAdapter14 = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, MontlyincomeArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spworkersmontlyincome.setAdapter(dataAdapter14);
        spworkersmontlyincome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans20=null;
                } else {
                    ans20=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //question21
        ArrayAdapter<String> dataAdapter15 = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, wagecalculatorArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spwagecalculator.setAdapter(dataAdapter15);
        spwagecalculator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans21=null;
                } else {
                    ans21=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question22
        ArrayAdapter<String> dataAdapter16 = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, WorkinghoursArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spworkinghours.setAdapter(dataAdapter16);
        spworkinghours.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans22=null;
                } else {
                    ans22=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question23
        ArrayAdapter<String> dataAdapter17 = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, WorkinghoursArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spworkerworkinghours.setAdapter(dataAdapter17);
        spworkerworkinghours.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans23=null;
                } else {
                    ans23=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question24
        ArrayAdapter<String> dataAdapter18 = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, WorkerscomplaintsArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spWorkerComplaints.setAdapter(dataAdapter18);
        spWorkerComplaints.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans24=null;
                } else {
                    ans24=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question25
        ArrayAdapter<String> dataAdapter19 = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, YesArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spWorkersUnion.setAdapter(dataAdapter19);
        spWorkersUnion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans25=null;
                } else {
                    ans25=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question26
        ArrayAdapter<String> dataAdapter20 = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, YesArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spRestDay.setAdapter(dataAdapter20);
        spRestDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans26=null;
                } else {
                    ans26=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question27
        ArrayAdapter<String> dataAdapter21 = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, YesArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion27.setAdapter(dataAdapter21);
        spQuestion27.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans27=null;
                } else {
                    ans27=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question28
        ArrayAdapter<String> dataAdapter22 = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, VacationArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion28.setAdapter(dataAdapter22);
        spQuestion28.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans28=null;
                } else {
                    ans28=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //question29
        ArrayAdapter<String> dataAdapter23 = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, daysArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion29.setAdapter(dataAdapter23);
        spQuestion29.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans29=null;
                } else {
                    ans29=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //question30
        ArrayAdapter<String> dataAdapter24 = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, childrendetailsArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion30.setAdapter(dataAdapter24);
        spQuestion30.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans30=null;
                } else {
                    ans30=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question31
        ArrayAdapter<String> dataAdapter25 = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, InjuriesArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion31.setAdapter(dataAdapter25);
        spQuestion31.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans31=null;
                } else {
                    ans31=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question32
        ArrayAdapter<String> dataAdapter26 = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, YesArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion32.setAdapter(dataAdapter26);
        spQuestion32.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans32=null;
                } else {
                    ans32=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //question33
        ArrayAdapter<String> dataAdapter27 = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, YesArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion33.setAdapter(dataAdapter27);
        spQuestion33.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans33=null;
                } else {
                    ans33=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //question34
        ArrayAdapter<String> dataAdapter28 = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, nightInjuryyesornoArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion34.setAdapter(dataAdapter28);
        spQuestion34.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans34=null;
                } else {
                    ans34=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //question35
        ArrayAdapter<String> dataAdapter29 = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, EquipmentArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion35.setAdapter(dataAdapter29);
        spQuestion35.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans35=null;
                } else {
                    ans35=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question36
        ArrayAdapter<String> dataAdapter30 = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, PersonalEquipmentArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion36.setAdapter(dataAdapter30);
        spQuestion36.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans36=null;
                } else {
                    ans36=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question37
        ArrayAdapter<String> dataAdapter31 = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, PersonalEquipmentlistArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion37.setAdapter(dataAdapter31);
        spQuestion37.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans37=null;
                } else {
                    ans37=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question38
        ArrayAdapter<String> dataAdapter32 = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, YesArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion38.setAdapter(dataAdapter32);
        spQuestion38.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans38=null;
                } else {
                    ans38=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //question39
        ArrayAdapter<String> dataAdapter33 = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, YesArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion39.setAdapter(dataAdapter33);
        spQuestion39.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans39=null;
                } else {
                    ans39=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //question40
        ArrayAdapter<String> dataAdapter34 = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, chemicalsArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion40.setAdapter(dataAdapter34);
        spQuestion40.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans40=null;
                } else {
                    ans40=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question41
        ArrayAdapter<String> dataAdapter35 = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, YesArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion41.setAdapter(dataAdapter35);
        spQuestion41.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans41=null;
                } else {
                    ans41=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //question43
        ArrayAdapter<String> dataAdapter36 = new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, YesArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion43.setAdapter(dataAdapter36);
        spQuestion43.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans43=null;
                } else {
                    ans43=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question44
        ArrayAdapter<String> dataAdapter37= new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, cropsArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion44.setAdapter(dataAdapter37);
        spQuestion44.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans44=null;
                } else {
                    ans44=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question62
        ArrayAdapter<String> dataAdapter62= new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, ans62Arr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion62.setAdapter(dataAdapter62);
        spQuestion62.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans62=null;
                } else {
                    ans62=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //question63
        ArrayAdapter<String> dataAdapter63= new ArrayAdapter<String>(RiskAssessmentActivity.this,
                android.R.layout.simple_spinner_item, ans63Arr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion63.setAdapter(dataAdapter63);
        spQuestion63.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans63=null;
                } else {
                    ans63=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question64
        ArrayAdapter<String> dataAdapter64= new ArrayAdapter<String>(RiskAssessmentActivity.this,//todo ch
                android.R.layout.simple_spinner_item, ans64Arr);//todo ch
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion64.setAdapter(dataAdapter64);//todo ch 2
        spQuestion64.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//todo ch
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans64=null;//todo ch
                } else {
                    ans64=parent.getItemAtPosition(position).toString();//todo ch
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question65
        ArrayAdapter<String> dataAdapter65= new ArrayAdapter<String>(RiskAssessmentActivity.this,//todo ch
                android.R.layout.simple_spinner_item, ans65Arr);//todo ch
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion65.setAdapter(dataAdapter65);//todo ch 2
        spQuestion65.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//todo ch
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans65=null;//todo ch
                } else {
                    ans65=parent.getItemAtPosition(position).toString();//todo ch
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question66
        ArrayAdapter<String> dataAdapter66= new ArrayAdapter<String>(RiskAssessmentActivity.this,//todo ch
                android.R.layout.simple_spinner_item, ans66Arr);//todo ch
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion66.setAdapter(dataAdapter66);//todo ch 2
        spQuestion66.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//todo ch
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans66=null;//todo ch
                } else {
                    ans66=parent.getItemAtPosition(position).toString();//todo ch
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question67
        ArrayAdapter<String> dataAdapter67= new ArrayAdapter<String>(RiskAssessmentActivity.this,//todo ch
                android.R.layout.simple_spinner_item, ans67Arr);//todo ch
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion67.setAdapter(dataAdapter67);//todo ch 2
        spQuestion67.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//todo ch
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans67=null;//todo ch
                } else {
                    ans67=parent.getItemAtPosition(position).toString();//todo ch
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question68
        ArrayAdapter<String> dataAdapter68= new ArrayAdapter<String>(RiskAssessmentActivity.this,//todo ch
                android.R.layout.simple_spinner_item, ans68Arr);//todo ch
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion68.setAdapter(dataAdapter68);//todo ch 2
        spQuestion68.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//todo ch
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans68=null;//todo ch
                } else {
                    ans68=parent.getItemAtPosition(position).toString();//todo ch
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question69
        ArrayAdapter<String> dataAdapter69= new ArrayAdapter<String>(RiskAssessmentActivity.this,//todo ch
                android.R.layout.simple_spinner_item, ans69Arr);//todo ch
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion69.setAdapter(dataAdapter69);//todo ch 2
        spQuestion69.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//todo ch
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans69=null;//todo ch
                } else {
                    ans69=parent.getItemAtPosition(position).toString();//todo ch
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question70
        ArrayAdapter<String> dataAdapter70= new ArrayAdapter<String>(RiskAssessmentActivity.this,//todo ch
                android.R.layout.simple_spinner_item, ans70Arr);//todo ch
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion70.setAdapter(dataAdapter70);//todo ch 2
        spQuestion70.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//todo ch
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans70=null;//todo ch
                } else {
                    ans70=parent.getItemAtPosition(position).toString();//todo ch
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question71
        ArrayAdapter<String> dataAdapter71= new ArrayAdapter<String>(RiskAssessmentActivity.this,//todo ch
                android.R.layout.simple_spinner_item, ans71Arr);//todo ch
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion71.setAdapter(dataAdapter71);//todo ch 2
        spQuestion71.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//todo ch
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans71=null;//todo ch
                } else {
                    ans71=parent.getItemAtPosition(position).toString();//todo ch
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question72
        ArrayAdapter<String> dataAdapter72= new ArrayAdapter<String>(RiskAssessmentActivity.this,//todo ch
                android.R.layout.simple_spinner_item, ans72Arr);//todo ch
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion72.setAdapter(dataAdapter72);//todo ch 2
        spQuestion72.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//todo ch
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans72=null;//todo ch
                } else {
                    ans72=parent.getItemAtPosition(position).toString();//todo ch
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question73
        ArrayAdapter<String> dataAdapter73= new ArrayAdapter<String>(RiskAssessmentActivity.this,//todo ch
                android.R.layout.simple_spinner_item, ans73Arr);//todo ch
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion73.setAdapter(dataAdapter73);//todo ch 2
        spQuestion73.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//todo ch
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans73=null;//todo ch
                } else {
                    ans73=parent.getItemAtPosition(position).toString();//todo ch
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //question74
        ArrayAdapter<String> dataAdapter74= new ArrayAdapter<String>(RiskAssessmentActivity.this,//todo ch
                android.R.layout.simple_spinner_item, ans74Arr);//todo ch
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion74.setAdapter(dataAdapter74);//todo ch 2
        spQuestion74.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//todo ch
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans74=null;//todo ch
                } else {
                    ans74=parent.getItemAtPosition(position).toString();//todo ch
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question75
        ArrayAdapter<String> dataAdapter75= new ArrayAdapter<String>(RiskAssessmentActivity.this,//todo ch
                android.R.layout.simple_spinner_item, ans75Arr);//todo ch
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion75.setAdapter(dataAdapter75);//todo ch 2
        spQuestion75.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//todo ch
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans75=null;//todo ch
                } else {
                    ans75=parent.getItemAtPosition(position).toString();//todo ch
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question76
        ArrayAdapter<String> dataAdapter76= new ArrayAdapter<String>(RiskAssessmentActivity.this,//todo ch
                android.R.layout.simple_spinner_item, ans76Arr);//todo ch
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion76.setAdapter(dataAdapter76);//todo ch 2
        spQuestion76.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//todo ch
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans76=null;//todo ch
                } else {
                    ans76=parent.getItemAtPosition(position).toString();//todo ch
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question77
        ArrayAdapter<String> dataAdapter77= new ArrayAdapter<String>(RiskAssessmentActivity.this,//todo ch
                android.R.layout.simple_spinner_item, ans77Arr);//todo ch
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion77.setAdapter(dataAdapter77);//todo ch 2
        spQuestion77.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//todo ch
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans77=null;//todo ch
                } else {
                    ans77=parent.getItemAtPosition(position).toString();//todo ch
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question78
        ArrayAdapter<String> dataAdapter78= new ArrayAdapter<String>(RiskAssessmentActivity.this,//todo ch
                android.R.layout.simple_spinner_item, ans78Arr);//todo ch
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion78.setAdapter(dataAdapter78);//todo ch 2
        spQuestion78.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//todo ch
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans78=null;//todo ch
                } else {
                    ans78=parent.getItemAtPosition(position).toString();//todo ch
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question79
        ArrayAdapter<String> dataAdapter79= new ArrayAdapter<String>(RiskAssessmentActivity.this,//todo ch
                android.R.layout.simple_spinner_item, ans79Arr);//todo ch
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion79.setAdapter(dataAdapter79);//todo ch 2
        spQuestion79.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//todo ch
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans79=null;//todo ch
                } else {
                    ans79=parent.getItemAtPosition(position).toString();//todo ch
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //question80
        ArrayAdapter<String> dataAdapter80= new ArrayAdapter<String>(RiskAssessmentActivity.this,//todo ch
                android.R.layout.simple_spinner_item, ans80Arr);//todo ch
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spQuestion80.setAdapter(dataAdapter80);//todo ch 2
        spQuestion80.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//todo ch
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ans80=null;//todo ch
                } else {
                    ans80=parent.getItemAtPosition(position).toString();//todo ch
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    // TODO: 9/21/2023 for question 11
    private void dropDownQuestion11(TextView autoCompleteQuestionTxt){
        //data source for drop-down list
    //   ArrayList<String> ActivitiesArr=new String[] {"Select","Spraying chemical (Herbicide)","Plantation maintenance","Other","I don't use contractors"};
        String[] ActivitiesArrData=new String[] {"Spraying chemical (Herbicide)","Plantation maintenance","Other","I don't use contractors"};

        final ArrayList<String> items = new ArrayList<String>();
        items.addAll(Arrays.asList(ActivitiesArr));
        checkSelected = new boolean[items.size()];
        //initialize all values of list to 'unselected' initially
        for (int i = 0; i < checkSelected.length; i++) {
            checkSelected[i] = false;
        }

        /*SelectBox is the TextView where the selected values will be displayed in the form of "Item 1 & 'n' more".
         * When this selectBox is clicked it will display all the selected values
         * and when clicked again it will display in shortened representation as before.
         * */
        autoCompleteQuestionTxt = (TextView) findViewById(R.id.txt_data);

        TextView txtData = autoCompleteQuestionTxt;
        autoCompleteQuestionTxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(!expanded){
                    //display all selected values
                    String selected = "";
                    int flag = 0;
                    for (int i = 0; i < items.size(); i++) {
                        if (checkSelected[i] == true) {
                            selected += items.get(i);
                            selected += ", ";
                            flag = 1;
                        }
                    }

                    if(flag==1)
                        txtData.setText(selected);
                    expanded =true;
                }
                else{
                    //display shortened representation of selected values
                    txtData.setText(DropDownListAdapter.getSelected());
                    expanded = false;
                }
            }
        });

        //onClickListener to initiate the dropDown list
        Button createButton = (Button)findViewById(R.id.bt_drop_down);
        createButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                initiatePopUp(items,txtData);
            }
        });
    }

    /*
     * Function to set up the pop-up window which acts as drop-down list
     * */
    private void initiatePopUp(ArrayList<String> items, TextView tv){
        LayoutInflater inflater = (LayoutInflater)RiskAssessmentActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //get the pop-up window i.e.  drop-down layout
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.pop_up_window, (ViewGroup)findViewById(R.id.PopUpView));

        //get the view to which drop-down layout is to be anchored
        RelativeLayout layout1 = (RelativeLayout)findViewById(R.id.rl_question_11);
        pw = new PopupWindow(layout, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        //Pop-up window background cannot be null if we want the pop-up to listen touch events outside its window
        pw.setBackgroundDrawable(new BitmapDrawable());
        pw.setTouchable(true);

        //let pop-up be informed about touch events outside its window. This  should be done before setting the content of pop-up
        pw.setOutsideTouchable(true);
        pw.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        //dismiss the pop-up i.e. drop-down when touched anywhere outside the pop-up
        pw.setTouchInterceptor(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pw.dismiss();
                    return true;
                }
                return false;
            }
        });

        //provide the source layout for drop-down
        pw.setContentView(layout);

        //anchor the drop-down to bottom-left corner of 'layout1'
        pw.showAsDropDown(layout1);

        //populate the drop-down list
        final ListView list = (ListView) layout.findViewById(R.id.list_view);
        DropDownListAdapter adapter = new DropDownListAdapter(this, items, tv);
        list.setAdapter(adapter);
    }


    // TODO: 9/21/2023 for question 10
    private void dropDownQuestion10(TextView txtData){
        //data source for drop-down list
        //   ArrayList<String> ActivitiesArr=new String[] {"Select","Spraying chemical (Herbicide)","Plantation maintenance","Other","I don't use contractors"};
//        String[] JobtypeArr=new String[] {"Tapping","Collecting cuplumps, latex","Spraying chemical (Herbicide)","Delivering/ Rubber transportation","Plantation maintenance","Rubber processing (Rubber sheets)","Other"};

        final ArrayList<String> items = new ArrayList<String>();
        String[] JobtypeArr1 = new String[JobtypeArr.length - 1];
        System.arraycopy(JobtypeArr, 1, JobtypeArr1, 0, JobtypeArr.length - 1);
        items.addAll(Arrays.asList(JobtypeArr1));
        checkSelected10 = new boolean[items.size()];
        //initialize all values of list to 'unselected' initially
        for (int i = 0; i < checkSelected10.length; i++) {
            checkSelected10[i] = false;
        }

        /*SelectBox is the TextView where the selected values will be displayed in the form of "Item 1 & 'n' more".
         * When this selectBox is clicked it will display all the selected values
         * and when clicked again it will display in shortened representation as before.
         * */
        txtData = (TextView) findViewById(R.id.txt_question_10);
        TextView finalTxtData = txtData;
        txtData.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(!expanded10){
                    //display all selected values
                    String selected = "";
                    int flag = 0;
                    for (int i = 0; i < items.size(); i++) {
                        if (checkSelected10[i] == true) {
                            selected += items.get(i);
                            selected += ", ";
                            flag = 1;
                        }
                    }

                    if(flag==1)
                        finalTxtData.setText(selected);
                    expanded10 =true;
                }
                else{
                    //display shortened representation of selected values
                    finalTxtData.setText(DropDownListAdapter.getSelected());
                    expanded10 = false;
                }
            }
        });

        //onClickListener to initiate the dropDown list
        Button createButton = (Button)findViewById(R.id.bt_drop_down_10);
        createButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                initiatePopUp10(items,finalTxtData);
            }
        });
    }
    private void initiatePopUp10(ArrayList<String> items, TextView tv){
        LayoutInflater inflater = (LayoutInflater)RiskAssessmentActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //get the pop-up window i.e.  drop-down layout
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.pop_up_window, (ViewGroup)findViewById(R.id.PopUpView));

        //get the view to which drop-down layout is to be anchored
        RelativeLayout layout1 = (RelativeLayout)findViewById(R.id.rl_question_10);
        pw10 = new PopupWindow(layout, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        //Pop-up window background cannot be null if we want the pop-up to listen touch events outside its window
        pw10.setBackgroundDrawable(new BitmapDrawable());
        pw10.setTouchable(true);

        //let pop-up be informed about touch events outside its window. This  should be done before setting the content of pop-up
        pw10.setOutsideTouchable(true);
        pw10.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        //dismiss the pop-up i.e. drop-down when touched anywhere outside the pop-up
        pw10.setTouchInterceptor(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pw10.dismiss();
                    return true;
                }
                return false;
            }
        });

        //provide the source layout for drop-down
        pw10.setContentView(layout);

        //anchor the drop-down to bottom-left corner of 'layout1'
        pw10.showAsDropDown(layout1);

        //populate the drop-down list
        final ListView list = (ListView) layout.findViewById(R.id.list_view);
        DropDownListAdapterQ10 adapter = new DropDownListAdapterQ10(this, items, tv);
        list.setAdapter(adapter);
    }


    // TODO: 9/21/2023 for question 20 
    private void dropDownQuestion20(TextView txtData){
        //data source for drop-down list
//        String[] MontlyincomeArr=new String[] {"Less than 60 US$","60 to 150 US$","151 to 300 US$","301 to 600 US$","301 to 600 US$","more than 600 US$"};

        final ArrayList<String> items = new ArrayList<String>();
        items.addAll(Arrays.asList(MontlyincomeArr));
        checkSelected20 = new boolean[items.size()];
        //initialize all values of list to 'unselected' initially
        for (int i = 0; i < checkSelected20.length; i++) {
            checkSelected20[i] = false;
        }

        /*SelectBox is the TextView where the selected values will be displayed in the form of "Item 1 & 'n' more".
         * When this selectBox is clicked it will display all the selected values
         * and when clicked again it will display in shortened representation as before.
         * */
        txtData = (TextView) findViewById(R.id.txt_question_20);
        TextView finalTxtData = txtData;
        txtData.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(!expanded20){
                    //display all selected values
                    String selected = "";
                    int flag = 0;
                    for (int i = 0; i < items.size(); i++) {
                        if (checkSelected10[i] == true) {
                            selected += items.get(i);
                            selected += ", ";
                            flag = 1;
                        }
                    }

                    if(flag==1)
                        finalTxtData.setText(selected);
                    expanded20 =true;
                }
                else{
                    //display shortened representation of selected values
                    finalTxtData.setText(DropDownListAdapter.getSelected());
                    expanded20 = false;
                }
            }
        });

        //onClickListener to initiate the dropDown list
        Button createButton = (Button)findViewById(R.id.bt_drop_down_20);
        createButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                initiatePopUp20(items,finalTxtData);
            }
        });
    }
    private void initiatePopUp20(ArrayList<String> items, TextView tv){
        LayoutInflater inflater = (LayoutInflater)RiskAssessmentActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //get the pop-up window i.e.  drop-down layout
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.pop_up_window, (ViewGroup)findViewById(R.id.PopUpView));

        //get the view to which drop-down layout is to be anchored
        RelativeLayout layout1 = (RelativeLayout)findViewById(R.id.rl_question_20);
        pw20 = new PopupWindow(layout, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        //Pop-up window background cannot be null if we want the pop-up to listen touch events outside its window
        pw20.setBackgroundDrawable(new BitmapDrawable());
        pw20.setTouchable(true);

        //let pop-up be informed about touch events outside its window. This  should be done before setting the content of pop-up
        pw20.setOutsideTouchable(true);
        pw20.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        //dismiss the pop-up i.e. drop-down when touched anywhere outside the pop-up
        pw20.setTouchInterceptor(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pw20.dismiss();
                    return true;
                }
                return false;
            }
        });

        //provide the source layout for drop-down
        pw20.setContentView(layout);

        //anchor the drop-down to bottom-left corner of 'layout1'
        pw20.showAsDropDown(layout1);

        //populate the drop-down list
        final ListView list = (ListView) layout.findViewById(R.id.list_view);
        DropDownListAdapterQ20 adapter = new DropDownListAdapterQ20(this, items, tv);
        list.setAdapter(adapter);
    }

    // TODO: 9/21/2023 for question 21
    private void dropDownQuestion21(TextView txtData){
        //data source for drop-down list
//        String[] wagecalculatorArr=new String[] {"Provide wage slips in country language","For foreign workers provide wage slips in workers","Verbal explanation","We don’t really do that"};

        final ArrayList<String> items = new ArrayList<String>();
        items.addAll(Arrays.asList(wagecalculatorArr));
        checkSelected21 = new boolean[items.size()];
        //initialize all values of list to 'unselected' initially
        for (int i = 0; i < checkSelected21.length; i++) {
            checkSelected21[i] = false;
        }

        /*SelectBox is the TextView where the selected values will be displayed in the form of "Item 1 & 'n' more".
         * When this selectBox is clicked it will display all the selected values
         * and when clicked again it will display in shortened representation as before.
         * */
        txtData = (TextView) findViewById(R.id.txt_question_21);
        TextView finalTxtData = txtData;
        txtData.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(!expanded21){
                    //display all selected values
                    String selected = "";
                    int flag = 0;
                    for (int i = 0; i < items.size(); i++) {
                        if (checkSelected21[i] == true) {
                            selected += items.get(i);
                            selected += ", ";
                            flag = 1;
                        }
                    }

                    if(flag==1)
                        finalTxtData.setText(selected);
                    expanded21 =true;
                }
                else{
                    //display shortened representation of selected values
                    finalTxtData.setText(DropDownListAdapter.getSelected());
                    expanded21 = false;
                }
            }
        });

        //onClickListener to initiate the dropDown list
        Button createButton = (Button)findViewById(R.id.bt_drop_down_21);
        createButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                initiatePopUp21(items,finalTxtData);
            }
        });
    }
    private void initiatePopUp21(ArrayList<String> items, TextView tv){
        LayoutInflater inflater = (LayoutInflater)RiskAssessmentActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //get the pop-up window i.e.  drop-down layout
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.pop_up_window, (ViewGroup)findViewById(R.id.PopUpView));

        //get the view to which drop-down layout is to be anchored
        RelativeLayout layout1 = (RelativeLayout)findViewById(R.id.rl_question_21);
        pw21 = new PopupWindow(layout, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        //Pop-up window background cannot be null if we want the pop-up to listen touch events outside its window
        pw21.setBackgroundDrawable(new BitmapDrawable());
        pw21.setTouchable(true);

        //let pop-up be informed about touch events outside its window. This  should be done before setting the content of pop-up
        pw21.setOutsideTouchable(true);
        pw21.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        //dismiss the pop-up i.e. drop-down when touched anywhere outside the pop-up
        pw21.setTouchInterceptor(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pw21.dismiss();
                    return true;
                }
                return false;
            }
        });

        //provide the source layout for drop-down
        pw21.setContentView(layout);

        //anchor the drop-down to bottom-left corner of 'layout1'
        pw21.showAsDropDown(layout1);

        //populate the drop-down list
        final ListView list = (ListView) layout.findViewById(R.id.list_view);
        DropDownListAdapterQ21 adapter = new DropDownListAdapterQ21(this, items, tv);
        list.setAdapter(adapter);
    }

    // TODO: 9/21/2023 for question 24
    private void dropDownQuestion24(TextView txtData){
        //data source for drop-down list
        //   ArrayList<String> ActivitiesArr=new String[] {"Select","Spraying chemical (Herbicide)","Plantation maintenance","Other","I don't use contractors"};
//        String[] WorkerscomplaintsArr=new String[] {"Select","Workers never complain","Face to face/ personal interview","Workers Representatives/ workers Workers consultating","Workers' supporting group- Third party (NGO..)","Workers can complain through hotline/ SMS"};

        final ArrayList<String> items = new ArrayList<String>();
        items.addAll(Arrays.asList(WorkerscomplaintsArr));
        checkSelected24 = new boolean[items.size()];
        //initialize all values of list to 'unselected' initially
        for (int i = 0; i < checkSelected24.length; i++) {
            checkSelected24[i] = false;
        }

        /*SelectBox is the TextView where the selected values will be displayed in the form of "Item 1 & 'n' more".
         * When this selectBox is clicked it will display all the selected values
         * and when clicked again it will display in shortened representation as before.
         * */
        txtData = (TextView) findViewById(R.id.txt_question_24);
        TextView finalTxtData = txtData;
        txtData.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(!expanded24){
                    //display all selected values
                    String selected = "";
                    int flag = 0;
                    for (int i = 0; i < items.size(); i++) {
                        if (checkSelected24[i] == true) {
                            selected += items.get(i);
                            selected += ", ";
                            flag = 1;
                        }
                    }

                    if(flag==1)
                        finalTxtData.setText(selected);
                    expanded24 =true;
                }
                else{
                    //display shortened representation of selected values
                    finalTxtData.setText(DropDownListAdapter.getSelected());
                    expanded24 = false;
                }
            }
        });

        //onClickListener to initiate the dropDown list
        Button createButton = (Button)findViewById(R.id.bt_drop_down_24);
        createButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                initiatePopUp24(items,finalTxtData);
            }
        });
    }
    private void initiatePopUp24(ArrayList<String> items, TextView tv){
        LayoutInflater inflater = (LayoutInflater)RiskAssessmentActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //get the pop-up window i.e.  drop-down layout
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.pop_up_window, (ViewGroup)findViewById(R.id.PopUpView));

        //get the view to which drop-down layout is to be anchored
        RelativeLayout layout1 = (RelativeLayout)findViewById(R.id.rl_question_24);
        pw24 = new PopupWindow(layout, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        //Pop-up window background cannot be null if we want the pop-up to listen touch events outside its window
        pw24.setBackgroundDrawable(new BitmapDrawable());
        pw24.setTouchable(true);

        //let pop-up be informed about touch events outside its window. This  should be done before setting the content of pop-up
        pw24.setOutsideTouchable(true);
        pw24.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        //dismiss the pop-up i.e. drop-down when touched anywhere outside the pop-up
        pw24.setTouchInterceptor(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pw24.dismiss();
                    return true;
                }
                return false;
            }
        });

        //provide the source layout for drop-down
        pw24.setContentView(layout);

        //anchor the drop-down to bottom-left corner of 'layout1'
        pw24.showAsDropDown(layout1);

        //populate the drop-down list
        final ListView list = (ListView) layout.findViewById(R.id.list_view);
        DropDownListAdapterQ24 adapter = new DropDownListAdapterQ24(this, items, tv);
        list.setAdapter(adapter);
    }

    // TODO: 9/21/2023 for question 31
    private void dropDownQuestion31(TextView txtData){
        //data source for drop-down list
//        String[] InjuriesArr=new String[] {"Eye infection/ red eye","Skin rash","Cough","Cut/ Wound (get cut during tapping, plantation)","Animal bited (snakes…)","Road accident (communicating to site)","Falls, hits (Safety in movement inside the plantation)","Pain in the back, painful arm/hand… (ergonomics)","Other"};

        final ArrayList<String> items = new ArrayList<String>();
        items.addAll(Arrays.asList(InjuriesArr));
        checkSelected31 = new boolean[items.size()];
        //initialize all values of list to 'unselected' initially
        for (int i = 0; i < checkSelected31.length; i++) {
            checkSelected31[i] = false;
        }

        /*SelectBox is the TextView where the selected values will be displayed in the form of "Item 1 & 'n' more".
         * When this selectBox is clicked it will display all the selected values
         * and when clicked again it will display in shortened representation as before.
         * */
        txtData = (TextView) findViewById(R.id.txt_question_31);
        TextView finalTxtData = txtData;
        txtData.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(!expanded31){
                    //display all selected values
                    String selected = "";
                    int flag = 0;
                    for (int i = 0; i < items.size(); i++) {
                        if (checkSelected31[i] == true) {
                            selected += items.get(i);
                            selected += ", ";
                            flag = 1;
                        }
                    }

                    if(flag==1)
                        finalTxtData.setText(selected);
                    expanded31 =true;
                }
                else{
                    //display shortened representation of selected values
                    finalTxtData.setText(DropDownListAdapter.getSelected());
                    expanded31 = false;
                }
            }
        });

        //onClickListener to initiate the dropDown list
        Button createButton = (Button)findViewById(R.id.bt_drop_down_31);
        createButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                initiatePopUp31(items,finalTxtData);
            }
        });
    }
    private void initiatePopUp31(ArrayList<String> items, TextView tv){
        LayoutInflater inflater = (LayoutInflater)RiskAssessmentActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //get the pop-up window i.e.  drop-down layout
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.pop_up_window, (ViewGroup)findViewById(R.id.PopUpView));

        //get the view to which drop-down layout is to be anchored
        RelativeLayout layout1 = (RelativeLayout)findViewById(R.id.rl_question_31);
        pw31 = new PopupWindow(layout, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        //Pop-up window background cannot be null if we want the pop-up to listen touch events outside its window
        pw31.setBackgroundDrawable(new BitmapDrawable());
        pw31.setTouchable(true);

        //let pop-up be informed about touch events outside its window. This  should be done before setting the content of pop-up
        pw31.setOutsideTouchable(true);
        pw31.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        //dismiss the pop-up i.e. drop-down when touched anywhere outside the pop-up
        pw31.setTouchInterceptor(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pw31.dismiss();
                    return true;
                }
                return false;
            }
        });

        //provide the source layout for drop-down
        pw31.setContentView(layout);

        //anchor the drop-down to bottom-left corner of 'layout1'
        pw31.showAsDropDown(layout1);

        //populate the drop-down list
        final ListView list = (ListView) layout.findViewById(R.id.list_view);
        DropDownListAdapterQ31 adapter = new DropDownListAdapterQ31(this, items, tv);
        list.setAdapter(adapter);
    }

    // TODO: 9/21/2023 for question 35
    private void dropDownQuestion35(TextView txtData){
        //data source for drop-down list
//        String[] EquipmentArr=new String[] {"Friend/ Family","Buyer/cooperative","Chemical seller","Agency/administrator (e.g: Forestry department)","Third party (NGO, Consultant)","Self-study (Books, internet)","Never","Not applicable- I don't use chemicals"};

        final ArrayList<String> items = new ArrayList<String>();
        items.addAll(Arrays.asList(EquipmentArr));
        checkSelected35 = new boolean[items.size()];
        //initialize all values of list to 'unselected' initially
        for (int i = 0; i < checkSelected35.length; i++) {
            checkSelected35[i] = false;
        }

        /*SelectBox is the TextView where the selected values will be displayed in the form of "Item 1 & 'n' more".
         * When this selectBox is clicked it will display all the selected values
         * and when clicked again it will display in shortened representation as before.
         * */
        txtData = (TextView) findViewById(R.id.txt_question_35);
        TextView finalTxtData = txtData;
        txtData.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(!expanded35){
                    //display all selected values
                    String selected = "";
                    int flag = 0;
                    for (int i = 0; i < items.size(); i++) {
                        if (checkSelected35[i] == true) {
                            selected += items.get(i);
                            selected += ", ";
                            flag = 1;
                        }
                    }

                    if(flag==1)
                        finalTxtData.setText(selected);
                    expanded35 =true;
                }
                else{
                    //display shortened representation of selected values
                    finalTxtData.setText(DropDownListAdapter.getSelected());
                    expanded35 = false;
                }
            }
        });

        //onClickListener to initiate the dropDown list
        Button createButton = (Button)findViewById(R.id.bt_drop_down_35);
        createButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                initiatePopUp35(items,finalTxtData);
            }
        });
    }
    private void initiatePopUp35(ArrayList<String> items, TextView tv){
        LayoutInflater inflater = (LayoutInflater)RiskAssessmentActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //get the pop-up window i.e.  drop-down layout
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.pop_up_window, (ViewGroup)findViewById(R.id.PopUpView));

        //get the view to which drop-down layout is to be anchored
        RelativeLayout layout1 = (RelativeLayout)findViewById(R.id.rl_question_35);
        pw35 = new PopupWindow(layout, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        //Pop-up window background cannot be null if we want the pop-up to listen touch events outside its window
        pw35.setBackgroundDrawable(new BitmapDrawable());
        pw35.setTouchable(true);

        //let pop-up be informed about touch events outside its window. This  should be done before setting the content of pop-up
        pw35.setOutsideTouchable(true);
        pw35.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        //dismiss the pop-up i.e. drop-down when touched anywhere outside the pop-up
        pw35.setTouchInterceptor(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pw35.dismiss();
                    return true;
                }
                return false;
            }
        });

        //provide the source layout for drop-down
        pw35.setContentView(layout);

        //anchor the drop-down to bottom-left corner of 'layout1'
        pw35.showAsDropDown(layout1);

        //populate the drop-down list
        final ListView list = (ListView) layout.findViewById(R.id.list_view);
        DropDownListAdapterQ35 adapter = new DropDownListAdapterQ35(this, items, tv);
        list.setAdapter(adapter);
    }

    // TODO: 9/21/2023 for question 37
    private void dropDownQuestion37(TextView txtData){
//        String[] PersonalEquipmentlistArr=new String[] {"Mask","Gloves","Protective googles","Boots","Apron"};

        final ArrayList<String> items = new ArrayList<String>();
        items.addAll(Arrays.asList(PersonalEquipmentlistArr));
        checkSelected37 = new boolean[items.size()];
        //initialize all values of list to 'unselected' initially
        for (int i = 0; i < checkSelected37.length; i++) {
            checkSelected37[i] = false;
        }

        /*SelectBox is the TextView where the selected values will be displayed in the form of "Item 1 & 'n' more".
         * When this selectBox is clicked it will display all the selected values
         * and when clicked again it will display in shortened representation as before.
         * */
        txtData = (TextView) findViewById(R.id.txt_question_37);
        TextView finalTxtData = txtData;
        txtData.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(!expanded37){
                    //display all selected values
                    String selected = "";
                    int flag = 0;
                    for (int i = 0; i < items.size(); i++) {
                        if (checkSelected37[i] == true) {
                            selected += items.get(i);
                            selected += ", ";
                            flag = 1;
                        }
                    }

                    if(flag==1)
                        finalTxtData.setText(selected);
                    expanded37 =true;
                }
                else{
                    //display shortened representation of selected values
                    finalTxtData.setText(DropDownListAdapter.getSelected());
                    expanded37 = false;
                }
            }
        });

        //onClickListener to initiate the dropDown list
        Button createButton = (Button)findViewById(R.id.bt_drop_down_37);
        createButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                initiatePopUp37(items,finalTxtData);
            }


        });
    }
    private void initiatePopUp37(ArrayList<String> items, TextView tv){
        LayoutInflater inflater = (LayoutInflater)RiskAssessmentActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //get the pop-up window i.e.  drop-down layout
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.pop_up_window, (ViewGroup)findViewById(R.id.PopUpView));

        //get the view to which drop-down layout is to be anchored
        RelativeLayout layout1 = (RelativeLayout)findViewById(R.id.rl_question_37);
        pw37 = new PopupWindow(layout, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        //Pop-up window background cannot be null if we want the pop-up to listen touch events outside its window
        pw37.setBackgroundDrawable(new BitmapDrawable());
        pw37.setTouchable(true);

        //let pop-up be informed about touch events outside its window. This  should be done before setting the content of pop-up
        pw37.setOutsideTouchable(true);
        pw37.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        //dismiss the pop-up i.e. drop-down when touched anywhere outside the pop-up
        pw37.setTouchInterceptor(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pw37.dismiss();
                    return true;
                }
                return false;
            }
        });

        //provide the source layout for drop-down
        pw37.setContentView(layout);

        //anchor the drop-down to bottom-left corner of 'layout1'
        pw37.showAsDropDown(layout1);

        //populate the drop-down list
        final ListView list = (ListView) layout.findViewById(R.id.list_view);
        DropDownListAdapterQ37 adapter = new DropDownListAdapterQ37(this, items, tv);
        list.setAdapter(adapter);
    }

    // TODO: 9/21/2023 for question 40
    private void dropDownQuestion40(TextView txtData){
//        String[] chemicalsArr=new String[] {"Paraquat/Gramoxone","2-4D","Glyphosate/ Roundup","Other herbicide","sulfuric Acid","Formic Acid","Other coagulant","Ammonia","TMTD/ZnO (TZ)","Ethephon stimulant","Other chemicals/ local brand","I don't know","I don't use any chemicals"};

        final ArrayList<String> items = new ArrayList<String>();
        items.addAll(Arrays.asList(chemicalsArr));
        checkSelected40 = new boolean[items.size()];
        //initialize all values of list to 'unselected' initially
        for (int i = 0; i < checkSelected40.length; i++) {
            checkSelected40[i] = false;
        }

        /*SelectBox is the TextView where the selected values will be displayed in the form of "Item 1 & 'n' more".
         * When this selectBox is clicked it will display all the selected values
         * and when clicked again it will display in shortened representation as before.
         * */
        txtData = (TextView) findViewById(R.id.txt_question_40);
        TextView finalTxtData = txtData;
        txtData.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(!expanded40){
                    //display all selected values
                    String selected = "";
                    int flag = 0;
                    for (int i = 0; i < items.size(); i++) {
                        if (checkSelected40[i] == true) {
                            selected += items.get(i);
                            selected += ", ";
                            flag = 1;
                        }
                    }

                    if(flag==1)
                        finalTxtData.setText(selected);
                    expanded40 =true;
                }
                else{
                    //display shortened representation of selected values
                    finalTxtData.setText(DropDownListAdapter.getSelected());
                    expanded40 = false;
                }
            }
        });

        //onClickListener to initiate the dropDown list
        Button createButton = (Button)findViewById(R.id.bt_drop_down_40);
        createButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                initiatePopUp40(items,finalTxtData);
            }


        });
    }
    private void initiatePopUp40(ArrayList<String> items, TextView tv){
        LayoutInflater inflater = (LayoutInflater)RiskAssessmentActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //get the pop-up window i.e.  drop-down layout
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.pop_up_window, (ViewGroup)findViewById(R.id.PopUpView));

        //get the view to which drop-down layout is to be anchored
        RelativeLayout layout1 = (RelativeLayout)findViewById(R.id.rl_question_40);
        pw40 = new PopupWindow(layout, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        //Pop-up window background cannot be null if we want the pop-up to listen touch events outside its window
        pw40.setBackgroundDrawable(new BitmapDrawable());
        pw40.setTouchable(true);


        //let pop-up be informed about touch events outside its window. This  should be done before setting the content of pop-up
        pw40.setOutsideTouchable(true);
        pw40.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        //dismiss the pop-up i.e. drop-down when touched anywhere outside the pop-up
        pw40.setTouchInterceptor(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pw40.dismiss();
                    return true;
                }
                return false;
            }
        });

        //provide the source layout for drop-down
        pw40.setContentView(layout);

        //anchor the drop-down to bottom-left corner of 'layout1'
        pw40.showAsDropDown(layout1);

        //populate the drop-down list
        final ListView list = (ListView) layout.findViewById(R.id.list_view);
        DropDownListAdapterQ40 adapter = new DropDownListAdapterQ40(this, items, tv);
        list.setAdapter(adapter);
    }



    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return null;
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    private void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
    }

    public String getLanguageFromLocalDb(String stLanguage, String stWord) {

        try {
            if (viewModel.getLanguageDataVM(stLanguage, stWord) != null) {
                return viewModel.getLanguageDataVM(stLanguage, stWord);
            } else {
                return stWord;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return stWord;
        }

    }


}