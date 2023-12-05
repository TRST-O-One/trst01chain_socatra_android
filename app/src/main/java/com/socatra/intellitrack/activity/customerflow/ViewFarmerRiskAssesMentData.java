package com.socatra.intellitrack.activity.customerflow;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.DeviceDealerID;
import static com.socatra.intellitrack.constants.AppConstant.DeviceProcessorId;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserName;
import static com.socatra.intellitrack.constants.AppConstant.LanguageId;

import androidx.appcompat.widget.SearchView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.socatra.intellitrack.BaseActivity;
import com.socatra.intellitrack.R;

import com.socatra.intellitrack.adapters.ExpandableListViewAdapter;
import com.socatra.intellitrack.adapters.QuestionsAnswersAdapter;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.models.get.GetRiskAssessmentForViewByFarmerCode;
import com.socatra.intellitrack.view_models.AppViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewFarmerRiskAssesMentData extends BaseActivity implements View.OnClickListener {

    private static final String TAG = DashBoardCustomerListAct.class.getCanonicalName();
    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    public AppViewModel viewModel;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    SearchView svFarmer, searchByName;
    LinearLayout imgempty;
    String Token, strDealerID, strDealerName, strProcessorId;

    ImageView ImgCancel;

    ExpandableListView expandableListView;
    ExpandableListViewAdapter expandableListViewAdapter;
    ArrayList<GetRiskAssessmentForViewByFarmerCode> Surveylist_child = new ArrayList<>();
    ArrayList<GetRiskAssessmentForViewByFarmerCode> getSurveyListTableArrayList;
    String strGetFarmerCode;
    RecyclerView recSurveyList;
    QuestionsAnswersAdapter questionsAnswersAdapter;
    Button btPrintData;
    TextView txtWordHeaderTileSurvey;
    String strLanguageId;
    private List<GetRiskAssessmentForViewByFarmerCode> data;
    private Context activityContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_risk);

        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");
        strProcessorId = appHelper.getSharedPrefObj().getString(DeviceProcessorId, "");
        strGetFarmerCode = getIntent().getStringExtra("FarmerCode");
        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");
        Log.d("FarmerCode", "FarmerCode" + strGetFarmerCode);
        initializeUI();
        initializeValues();
        configureDagger();
        configureViewModel();
    }

    private void initializeUI() {

        ImgCancel = findViewById(R.id.img_cancel);
        expandableListView = findViewById(R.id.expandableListView);
        recSurveyList = findViewById(R.id.recycler_survey_list);
        recSurveyList.setLayoutManager(new LinearLayoutManager(this));
        recSurveyList.hasFixedSize();
        btPrintData = findViewById(R.id.bt_print_data);
        txtWordHeaderTileSurvey = findViewById(R.id.txt_word_survey_title);
        //recSurveyList.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    protected void onResume() {
        super.onResume();
        if (appHelper.isNetworkAvailable()) {
            //getLanguageDataList();
        } else {
            Toast.makeText(getApplicationContext(), "please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getLanguageDataList() {
        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
//        final ProgressDialog progressDialog = new ProgressDialog(FarmerListByCustomerViewFromBatchActivity.this, R.style.AppCompatAlertDialogStyle);
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("loading data...");
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.show();

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
                            //  progressDialog.dismiss();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);

                                String jsonEngWord = jsonObjectFarmerPD.getString("SelectedWord");


                                if (strLanguageId.equals("1")) {
                                    String jsonEngWord1 = jsonObjectFarmerPD.getString("SelectedWord");
                                    if (getResources().getString(R.string.risk_assessment_survey).equals(jsonEngWord)) {
                                        txtWordHeaderTileSurvey.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.preview).equals(jsonEngWord)) {
                                        btPrintData.setText(jsonEngWord1);
                                    }

                                } else {

                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");

                                    if (getResources().getString(R.string.risk_assessment_survey).equals(jsonEngWord)) {
                                        txtWordHeaderTileSurvey.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.preview).equals(jsonEngWord)) {
                                        btPrintData.setText(strConvertedWord);
                                    }

                                }

//                                        else if (strLanguageId.equals("3")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//
//                                    if ("Risk Assessment Survey".equals(jsonEngWord)) {
//                                        txtWordHeaderTileSurvey.setText(strConvertedWord);
//                                    } else if ("Preview".equals(jsonEngWord)) {
//                                        btPrintData.setText(strConvertedWord);
//                                    }
//
//                                } else if (strLanguageId.equals("4")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//
//
//                                    if ("Risk Assessment Survey".equals(jsonEngWord)) {
//                                        txtWordHeaderTileSurvey.setText(strConvertedWord);
//                                    } else if ("Preview".equals(jsonEngWord)) {
//                                        btPrintData.setText(strConvertedWord);
//                                    }
//
//
//                                } else if (strLanguageId.equals("5")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//
//
//                                    if ("Risk Assessment Survey".equals(jsonEngWord)) {
//                                        txtWordHeaderTileSurvey.setText(strConvertedWord);
//                                    } else if ("Preview".equals(jsonEngWord)) {
//                                        btPrintData.setText(strConvertedWord);
//                                    }
//
//                                } else {
//
//
//                                }


                            }


                        } catch (Exception ex) {
                            ex.printStackTrace();
                            //  progressDialog.dismiss();
                            Log.d("Error", ">>>>" + ex.toString());
                        }
                    } else {
                        // progressDialog.dismiss();

                        Toast.makeText(ViewFarmerRiskAssesMentData.this, "no records found", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    // progressDialog.dismiss();

                    Log.d("Error", ">>>>" + ex.toString());
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                // progressDialog.dismiss();

                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
            }
        });
    }

    private void initializeValues() {

        strDealerName = appHelper.getSharedPrefObj().getString(DeviceUserName, "");
        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");
        Token = appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, "");

        ImgCancel.setOnClickListener(view -> onBackPressed());


        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                GetRiskAssessmentForViewByFarmerCode headerInfo = getSurveyListTableArrayList.get(groupPosition);
                //get the child info
                //   CafeSubCatModel detailInfo = headerInfo.getCafeSubCatModels().get(childPosition);
                //display it or do something with it
//                Toast.makeText(getActivity(), "" + headerInfo.getCategorie()
//                        + "/" + detailInfo.getSub_categorie(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // ExpandableListView Group expanded listener
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getActivity(),
//                        cafeSubCatModels.get(groupPosition).getSub_categorie(),
//                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                parent.smoothScrollToPosition(groupPosition);

                if (parent.isGroupExpanded(groupPosition)) {
                    ImageView imageView = v.findViewById(R.id.imgViewGroup);
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.n_plusl));
                    parent.collapseGroup(groupPosition);
                    String strQuestionCat = getSurveyListTableArrayList.get(groupPosition).getQuestionCategory();
                    getSubQuestionsAndAnswerList(strQuestionCat);
                    //  expandableListViewAdapter.notifyDataSetChanged();
                } else {
                    boolean animateExpansion = false;
                    parent.expandGroup(groupPosition, animateExpansion);
                    ImageView imageView = v.findViewById(R.id.imgViewGroup);
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.n_minus));
                }

                return true;
            }
        });

        // ExpandableListView Group collapsed listener
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getActivity(), "" + cafeSubCatModels.get(groupPosition), Toast.LENGTH_SHORT).show();
            }
        });
        btPrintData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewFarmerRiskAssesMentData.this, FarmerRiskAssesmentPrintViewAct.class);
                intent.putExtra("FarmerCode", strGetFarmerCode);
                startActivity(intent);

                // generatePDF(recSurveyList,questionsAnswersAdapter);
            }
        });

    }

    private void configureDagger() {
        AndroidInjection.inject(ViewFarmerRiskAssesMentData.this);
    }

    public void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
        if (appHelper.isNetworkAvailable()) {
            getMainQuestionCatList();
        } else {
            Toast.makeText(ViewFarmerRiskAssesMentData.this, "please check your internet connection", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onClick(View view) {

    }

    private void getMainQuestionCatList() {

        AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);

        Call<JsonElement> callRetrofit = service.getRiskAssessmentForViewByFarmerCodeFromServer(strGetFarmerCode, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));

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

                            getSurveyListTableArrayList = new ArrayList<>();
                            getSurveyListTableArrayList.clear();
                            ArrayList<GetRiskAssessmentForViewByFarmerCode> getSurveyListTableArrayListData = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                GetRiskAssessmentForViewByFarmerCode CustomerList = new GetRiskAssessmentForViewByFarmerCode();
                                if (jsonObjectFarmerPD.getString("Rn").equals("1")) {
                                    CustomerList.setQuestionCategory((jsonObjectFarmerPD.getString("QuestionCategory")));
                                    CustomerList.setQuestions((jsonObjectFarmerPD.getString("Questions")));
                                    getSurveyListTableArrayList.add(CustomerList);
                                }
                                //   getSurveyListTableArrayListData.add(CustomerList);

                            }

                            //setUpRecyclerView();
                            questionsAnswersAdapter = new QuestionsAnswersAdapter(ViewFarmerRiskAssesMentData.this, strGetFarmerCode, appHelper, viewModel, getSurveyListTableArrayList);
                            recSurveyList.setAdapter(questionsAnswersAdapter);


                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Log.d("Error", ">>>>" + ex.toString());
                        }
                    } else {
                        Toast.makeText(ViewFarmerRiskAssesMentData.this, "no records found", Toast.LENGTH_LONG).show();
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

    private void setUpRecyclerView() {
        if (getSurveyListTableArrayList == null && getSurveyListTableArrayList.size() == 0) {
//            Toast.makeText(RequestDetailsActivity.this, "Empty", Toast.LENGTH_SHORT).show();
        } else {
            expandableListViewAdapter = new ExpandableListViewAdapter(ViewFarmerRiskAssesMentData.this, getSurveyListTableArrayList, Surveylist_child);
            expandableListView.setAdapter(expandableListViewAdapter);

        }
    }


    public void getSubQuestionsAndAnswerList(String strQuestionCat) {
        AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        //  String FarmerCode = getIntent().getStringExtra("FarmerCode");
        Call<JsonElement> callRetrofit = service.getRiskAssessmentForViewByFarmerCodeFromServer(strGetFarmerCode, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    String strResponse = String.valueOf(response.body());
                    Log.d(TAG, "onResponse: >>>" + strResponse);
                    JSONObject jsonObject = new JSONObject(strResponse);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    //Surveylist_child = new ArrayList<>();
                    Surveylist_child.clear();
                    if (jsonArray.length() > 0) {
                        try {
                            ArrayList<GetRiskAssessmentForViewByFarmerCode> getSurveyListTableArrayList = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                GetRiskAssessmentForViewByFarmerCode CustomerList = new GetRiskAssessmentForViewByFarmerCode();
                                if (jsonObjectFarmerPD.getString("QuestionCategory").equals(strQuestionCat)) {
                                    CustomerList.setQuestions((jsonObjectFarmerPD.getString("Questions")));
                                    CustomerList.setAnswers((jsonObjectFarmerPD.getString("Answers")));
                                    CustomerList.setQuestionNo(jsonObjectFarmerPD.getString("QuestionNo"));
                                    CustomerList.setSerialNo(Integer.valueOf((jsonObjectFarmerPD.getString("SerialNo"))));
                                    getSurveyListTableArrayList.add(CustomerList);
                                }
                            }
                            Surveylist_child.addAll(getSurveyListTableArrayList);
                            setUpRecyclerView();

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Log.d("Error", ">>>>" + ex.toString());
                        }
                    } else {
                        Toast.makeText(ViewFarmerRiskAssesMentData.this, "no records found", Toast.LENGTH_LONG).show();
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

    public void generatePDF(RecyclerView view, QuestionsAnswersAdapter adapter) {


//        RecyclerView.Adapter adapter = view.getAdapter();
        int sie2 = adapter.getItemCount();
        if (sie2 == 0) {
            Toast.makeText(this, "No Transactions", Toast.LENGTH_LONG).show();
        } else {
            Bitmap bigBitmap = null;
            if (adapter != null) {

                int size = adapter.getItemCount();
                int height = 0;
                Paint paint = new Paint();
                int iHeight = 0;
                final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

                // Use 1/8th of the available memory for this memory cache.
                final int cacheSize = maxMemory / 8;
                LruCache<String, Bitmap> bitmaCache = new LruCache<>(cacheSize);
                for (int i = 0; i < size; i++) {
                    RecyclerView.ViewHolder holder = adapter.createViewHolder(view, adapter.getItemViewType(i));
                    adapter.onBindViewHolder((QuestionsAnswersAdapter.ViewHolder) holder, i);
                    holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());
                    holder.itemView.setDrawingCacheEnabled(true);
                    holder.itemView.buildDrawingCache();
                    Bitmap drawingCache = holder.itemView.getDrawingCache();
                    if (drawingCache != null) {

                        bitmaCache.put(String.valueOf(i), drawingCache);
                    }

                    height += holder.itemView.getMeasuredHeight();
                }

                bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), height, Bitmap.Config.ARGB_8888);
                Canvas bigCanvas = new Canvas(bigBitmap);
                bigCanvas.drawColor(Color.WHITE);

                Document document = new Document();

                final File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "MAT" + System.currentTimeMillis() + ".pdf");
                try {
                    PdfWriter.getInstance(document, new FileOutputStream(file));
                } catch (DocumentException | FileNotFoundException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < size; i++) {

                    try {
                        //Adding the content to the document
                        Bitmap bmp = bitmaCache.get(String.valueOf(i));
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(stream.toByteArray());
                        //Image image = Image.getInstance(stream.toByteArray());
                        float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                                - document.rightMargin() - 50) / image.getWidth()) * 100; // 0 means you have no indentation. If you have any, change it.
                        image.scalePercent(scaler);
                        image.setAlignment(com.itextpdf.text.Image.ALIGN_CENTER | com.itextpdf.text.Image.ALIGN_TOP);
                        if (!document.isOpen()) {
                            document.open();
                        }
                        document.add(image);

                    } catch (Exception ex) {
                        Log.e("TAG-ORDER PRINT ERROR", ex.getMessage());
                    }
                }

                if (document.isOpen()) {
                    document.close();
                }
                // Set on UI Thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ViewFarmerRiskAssesMentData.this);
                        builder.setTitle("Success")
                                .setMessage("PDF File Generated Successfully.")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("Open", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        //    dialog.dismiss();
                                        Intent target = new Intent(Intent.ACTION_VIEW);
//                                        Uri fileUri = FileProvider.getUriForFile(PrintRiskAssesment.this, getPackageName() + ".com.trst01.intellitarck.provider", file);
                                        Uri pdfUri = FileProvider.getUriForFile(ViewFarmerRiskAssesMentData.this, "com.trst01.intellitarck.provider", file);

                                        target.setDataAndType(pdfUri, "application/pdf");
                                        target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                        Intent intent56 = Intent.createChooser(target, "Open File");
                                        try {
                                            startActivity(intent56);
                                        } catch (ActivityNotFoundException e) {
                                            Toast.makeText(ViewFarmerRiskAssesMentData.this, "No PDF Viewer Installed.", Toast.LENGTH_LONG).show();
                                        }


                                    }

                                }).show();
                    }
                });

            }
        }

    }

}


//
//
//    public void clickedcategory(int position, List<GetRiskAssessmentForViewByFarmerCode> getRiskAssessmentForViewByFarmerCode, String questionCategory) {
//
//        AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
//        String FarmerCode = getIntent().getStringExtra("FarmerCode");
//        Call<JsonElement> callRetrofit = service.getRiskAssessmentForViewByFarmerCodeFromServer(FarmerCode, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));
//
//        callRetrofit.enqueue(new Callback<JsonElement>() {
//            @Override
//            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//                try {
//                    String strResponse = String.valueOf(response.body());
//                    Log.d(TAG, "onResponse: >>>" + strResponse);
//                    JSONObject jsonObject = new JSONObject(strResponse);
//                    JSONArray jsonArray = jsonObject.getJSONArray("data");
//                    Surveylist_child = new ArrayList<>();
//                    Surveylist_child.clear();
//                    if (jsonArray.length() > 0) {
//                        try {
//                            ArrayList<GetRiskAssessmentForViewByFarmerCode> getSurveyListTableArrayList = new ArrayList<>();
//
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
//                                GetRiskAssessmentForViewByFarmerCode CustomerList = new GetRiskAssessmentForViewByFarmerCode();
//                                if (jsonObjectFarmerPD.getString("QuestionCategory").equals(questionCategory)) {
//                                    CustomerList.setQuestions((jsonObjectFarmerPD.getString("Questions")));
//                                    CustomerList.setAnswers((jsonObjectFarmerPD.getString("Answers")));
//                                    CustomerList.setSerialNo(Integer.valueOf((jsonObjectFarmerPD.getString("SerialNo"))));
//                                    getSurveyListTableArrayList.add(CustomerList);
//                                }
//
//
//                            }
//
//
//                            Surveylist_child.addAll(getSurveyListTableArrayList);
//
//
////                            Questionadapter = new RiskAssesmentAdapter(Surveylist_child);
////                            recycleQuestion.hasFixedSize();
////                            recycleQuestion.setAdapter(Questionadapter);
////                            Questioncard.setVisibility(View.VISIBLE);
////
////
////
////
////                            Questionadapter.notifyDataSetChanged();
//
//
//                        } catch (Exception ex) {
//                            ex.printStackTrace();
//                            Log.d("Error", ">>>>" + ex.toString());
//                        }
//                    } else {
//                        Toast.makeText(NewRisk.this, "no records found", Toast.LENGTH_LONG).show();
//                    }
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                    Log.d("Error", ">>>>" + ex.toString());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonElement> call, Throwable t) {
//                Log.d("Error Call", ">>>>" + call.toString());
//                Log.d("Error", ">>>>" + t.toString());
//            }
//        });
//
//
//    }