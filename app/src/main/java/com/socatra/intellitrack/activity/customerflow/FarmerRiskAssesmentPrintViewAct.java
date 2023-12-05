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

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
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
import com.socatra.intellitrack.adapters.PrintRiskAssesmentAdapter;
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

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FarmerRiskAssesmentPrintViewAct extends BaseActivity {

    private static final String TAG = FarmerRiskAssesmentPrintViewAct.class.getCanonicalName();
    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    public AppViewModel viewModel;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    SearchView svFarmer, searchByName;
    String Token, strDealerID, strDealerName, strProcessorId;

    ImageView ImgCancel, print;


    RecyclerView recyclePrint;

    LinearLayout imgempty;

    ArrayList<GetRiskAssessmentForViewByFarmerCode> Surveylist;

    PrintRiskAssesmentAdapter adapter;
    TextView txtPrintData;

    String strFarmerCode;
    ProgressDialog progressDialog;

    String strLanguageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_risk_assesment);

        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");
        strProcessorId = appHelper.getSharedPrefObj().getString(DeviceProcessorId, "");
        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");
        strFarmerCode = getIntent().getStringExtra("FarmerCode");
        initializeUI();
        initializeValues();
        configureDagger();
        configureViewModel();
    }


    private void initializeUI() {

        ImgCancel = findViewById(R.id.img_back);
        recyclePrint = findViewById(R.id.recycler_print_risk_assesment);
        print = findViewById(R.id.print);
        txtPrintData = findViewById(R.id.txt_heading_print);
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
                                    if (getResources().getString(R.string.print).equals(jsonEngWord)) {
                                        txtPrintData.setText(jsonEngWord1);
                                    }
                                } else {

                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
                                    if (getResources().getString(R.string.print).equals(jsonEngWord)) {
                                        txtPrintData.setText(strConvertedWord);
                                    }


                                }


//                                else if (strLanguageId.equals("3")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//
//                                    if ("Print".equals(jsonEngWord)) {
//                                        txtPrintData.setText(strConvertedWord);
//                                    }
//
//                                } else if (strLanguageId.equals("4")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//
//
//                                    if ("Print".equals(jsonEngWord)) {
//                                        txtPrintData.setText(strConvertedWord);
//                                    }
//
//
//                                } else if (strLanguageId.equals("5")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//
//
//                                    if ("Print".equals(jsonEngWord)) {
//                                        txtPrintData.setText(strConvertedWord);
//                                    }
//
//                                } else {
//
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

                        Toast.makeText(FarmerRiskAssesmentPrintViewAct.this, "no records found", Toast.LENGTH_LONG).show();
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


        ImgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtPrintData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(FarmerRiskAssesmentPrintViewAct.this, R.style.AppCompatAlertDialogStyle);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("generating pdf please wait ");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        generatePDF(recyclePrint, adapter);
                    }

                }, 1 * 30);


            }
        });


        Surveylist = new ArrayList<>();
        recyclePrint.setLayoutManager(new LinearLayoutManager(this));

        getPrintRiskAssesmentlist(strFarmerCode);


    }


//    private void createAndSharePDF() {
//        // Create a new PDF document
//        PdfDocument pdfDocument = new PdfDocument();
//        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 400, 1).create();
//        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
//
//        Canvas canvas = page.getCanvas();
//
//// Create a Paint object for formatting text
//        Paint paint = new Paint();
//        paint.setColor(Color.BLACK);
//        paint.setTextSize(12f);
//
//        float x = 10; // X-coordinate for
//        float y = 25; // Y-coordinate for text
//
//        for (GetRiskAssessmentForViewByFarmerCode item : Surveylist) {
//            canvas.drawText("Serial No: " + item.getSerialNo(), x, y, paint);
//            y += 20;
//            canvas.drawText("Question: " + item.getQuestions(), x, y, paint);
//            y += 20;
//            canvas.drawText("Answer: " + item.getAnswers(), x, y, paint);
//            y += 40; // Adjust the vertical spacing as needed
//        }
//
//        pdfDocument.finishPage(page);
//
//// Save or share the PDF
//        File pdfFile = new File(context.getExternalFilesDir(null), "MyPDF.pdf");
//        try {
//            // Write the PDF document to the file
//            FileOutputStream fos = new FileOutputStream(pdfFile);
//            pdfDocument.writeTo(fos);
//            pdfDocument.close();
//            fos.close();
//
//            // Share the PDF file
//            Uri pdfUri = FileProvider.getUriForFile(this, "com.trst01.intellitarck.provider", pdfFile);
//            Intent shareIntent = new Intent(Intent.ACTION_SEND);
//            shareIntent.setType("application/pdf");
//            shareIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
//            startActivity(Intent.createChooser(shareIntent, "Share PDF"));
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

    public void generatePDF(RecyclerView view, PrintRiskAssesmentAdapter adapter) {


//        RecyclerView.Adapter adapter = view.getAdapter();
        int sie2 = adapter.getItemCount();
        if (sie2 == 0) {
            progressDialog.dismiss();
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
                    adapter.onBindViewHolder((PrintRiskAssesmentAdapter.ViewHolder) holder, i);
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
                progressDialog.dismiss();
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(FarmerRiskAssesmentPrintViewAct.this);
                builder.setTitle("Success")
                        .setMessage("PDF File Generated Successfully.")
                        .setIcon(R.drawable.success_icon)
                        .setPositiveButton("Open", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //    dialog.dismiss();
                                Intent target = new Intent(Intent.ACTION_VIEW);
//                                        Uri fileUri = FileProvider.getUriForFile(PrintRiskAssesment.this, getPackageName() + ".com.trst01.intellitarck.provider", file);
                                Uri pdfUri = FileProvider.getUriForFile(FarmerRiskAssesmentPrintViewAct.this, "com.trst01.intellitarck.provider", file);

                                target.setDataAndType(pdfUri, "application/pdf");
                                target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                Intent intent56 = Intent.createChooser(target, "Open File");
                                try {
                                    startActivity(intent56);
                                } catch (ActivityNotFoundException e) {
                                    Toast.makeText(FarmerRiskAssesmentPrintViewAct.this, "No PDF Viewer Installed.", Toast.LENGTH_LONG).show();
                                }


                            }

                        }).show();
                // Set on UI Thread
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//
//                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PrintRiskAssesment.this);
//                        builder.setTitle("Success")
//                                .setMessage("PDF File Generated Successfully.")
//                                .setIcon(android.R.drawable.ic_dialog_alert)
//                                .setPositiveButton("Open", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int whichButton) {
//                                        //    dialog.dismiss();
//                                        Intent target = new Intent(Intent.ACTION_VIEW);
////                                        Uri fileUri = FileProvider.getUriForFile(PrintRiskAssesment.this, getPackageName() + ".com.trst01.intellitarck.provider", file);
//                                        Uri pdfUri = FileProvider.getUriForFile(PrintRiskAssesment.this, "com.trst01.intellitarck.provider", file);
//
//                                        target.setDataAndType(pdfUri, "application/pdf");
//                                        target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//                                        Intent intent56 = Intent.createChooser(target, "Open File");
//                                        try {
//                                            startActivity(intent56);
//                                        } catch (ActivityNotFoundException e) {
//                                            Toast.makeText(PrintRiskAssesment.this, "No PDF Viewer Installed.", Toast.LENGTH_LONG).show();
//                                        }
//
//
//                                    }
//
//                                }).show();
//                    }
//                });

            }
        }

    }


    private void configureDagger() {
        AndroidInjection.inject(FarmerRiskAssesmentPrintViewAct.this);
    }

    public void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
    }

    private void getPrintRiskAssesmentlist(String farmercode) {

        AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        final ProgressDialog progressDialog = new ProgressDialog(FarmerRiskAssesmentPrintViewAct.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("generating pdf please wait ");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Call<JsonElement> callRetrofit = service.getRiskAssessmentForViewByFarmerCodeFromServer(farmercode, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override

            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    String strResponse = String.valueOf(response.body());
                    Log.d(TAG, "onResponse: >>>" + strResponse);
                    JSONObject jsonObject = new JSONObject(strResponse);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    progressDialog.dismiss();
                    if (jsonArray.length() > 0) {
                        try {
                            ArrayList<GetRiskAssessmentForViewByFarmerCode> getprintListTableArrayList = new ArrayList<>();


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                GetRiskAssessmentForViewByFarmerCode printList = new GetRiskAssessmentForViewByFarmerCode();
                                printList.setSerialNo(Integer.valueOf(jsonObjectFarmerPD.getString("SerialNo")));
                                printList.setAnswers(jsonObjectFarmerPD.getString("Answers"));
                                printList.setQuestions(jsonObjectFarmerPD.getString("Questions"));

                                getprintListTableArrayList.add(printList);


                            }

                            // Update the adapter with the new data
                            Surveylist.addAll(getprintListTableArrayList);

                            adapter = new PrintRiskAssesmentAdapter(farmercode, Surveylist);
                            recyclePrint.hasFixedSize();
                            recyclePrint.setAdapter(adapter);

                            adapter.notifyDataSetChanged();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Log.d("Error", ">>>>" + ex.toString());
                        }
                    } else {
                        Toast.makeText(FarmerRiskAssesmentPrintViewAct.this, "no records found", Toast.LENGTH_LONG).show();
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