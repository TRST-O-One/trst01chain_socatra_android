package com.socatra.intellitrack.activity.grnflow;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.DATE_FORMAT_DD_MM_YYYY2;
import static com.socatra.intellitrack.constants.AppConstant.DeviceDealerAddress;
import static com.socatra.intellitrack.constants.AppConstant.DeviceDealerID;
import static com.socatra.intellitrack.constants.AppConstant.DeviceDealerName;
import static com.socatra.intellitrack.constants.AppConstant.DeviceProcessorAddress;
import static com.socatra.intellitrack.constants.AppConstant.DeviceProcessorId;
import static com.socatra.intellitrack.constants.AppConstant.DeviceProcessorName;
import static com.socatra.intellitrack.constants.AppConstant.LanguageId;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;
import com.socatra.intellitrack.BaseActivity;
import com.socatra.intellitrack.R;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.view_models.AppViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShareGRNActivity extends BaseActivity {

    private static final String TAG = ShareGRNActivity.class.getCanonicalName();
    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    public AppViewModel viewModel;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    SearchView searchByName;
    String Token, strDealerID, strDealerName, strProcessorId, strLanguageId;


    ImageView ImgCancel;

    TextView Sharebtn, previewbtn, Backbtn, GRNNumber, GRNDate, Address, FarmerName, ContactNo, FaxNo, procurementGRNNumber, Quantity, NetWeight;


    TextView txtSenderName, txtSenderAdd;
    String strKey;
    LinearLayout llSentData;

    TextView txtl_GRNReceipt, txtl_Sentby, txtl_GRNNumber, txtl_Address, txtl_FarmerDetails, txtl_GRNDate, txtl_DealerDetails, txtl_FarmerName, txtl_telephoneNo, txtl_FaxNo, txtl_procurementDetails, txtl_grn1, txtl_NetWeight;
    TextView txtWordCopyRight, txtWordCmpnName, txtWordTimeAndDate, txtWordTimeZone, txtWordSystemON;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grn_share_dialog);
        strKey = getIntent().getStringExtra("key");
        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");
        strProcessorId = appHelper.getSharedPrefObj().getString(DeviceProcessorId, "");
        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");
        initializeUI();
        initializeValues();
//        configureDagger();
//        configureViewModel();
    }

    private void initializeUI() {

        llSentData = findViewById(R.id.ll_sent);
        Sharebtn = findViewById(R.id.txtShare);
        GRNNumber = findViewById(R.id.txt_share_grn_no);
        GRNDate = findViewById(R.id.txt_share_date);
        Address = findViewById(R.id.txt_share_address);
        FarmerName = findViewById(R.id.txt_share_farmer_name);
        ContactNo = findViewById(R.id.txt_share_Contact_no);
        FaxNo = findViewById(R.id.txt_share_Fax_no);
        procurementGRNNumber = findViewById(R.id.txt_share_procurement_grn_no);
        Quantity = findViewById(R.id.txt_share_quantity);
        NetWeight = findViewById(R.id.txt_net_weight);
        previewbtn = findViewById(R.id.grn_preview);
        Backbtn = findViewById(R.id.txtBack);
        txtSenderAdd = findViewById(R.id.txt_share_inv_from_address_grn);
        txtSenderName = findViewById(R.id.txt_share_shipping_from_grn);

        //language
        txtl_GRNReceipt = findViewById(R.id.txt_GRNReceipt);
        txtl_Sentby = findViewById(R.id.txt_Sentby);
        txtl_GRNNumber = findViewById(R.id.share_grn);
        txtl_GRNDate = findViewById(R.id.share_date);
        txtl_DealerDetails = findViewById(R.id.share_farmer_details);
        txtl_Address = findViewById(R.id.share_address);
        txtl_FarmerDetails = findViewById(R.id.share_farmer_details);
        txtl_FarmerName = findViewById(R.id.share_farmer_name);
        txtl_telephoneNo = findViewById(R.id.share_telephoneno);
        txtl_FaxNo = findViewById(R.id.share_faxno);
        txtl_procurementDetails = findViewById(R.id.share_procurement_details);
        txtl_grn1 = findViewById(R.id.share_grn_1);
        txtl_NetWeight = findViewById(R.id.share_net_weight);
        Sharebtn = findViewById(R.id.txtShare);
        previewbtn = findViewById(R.id.grn_preview);
        Backbtn = findViewById(R.id.txtBack);

        if (strKey.equals("farmer")) {
            //txtSenderName.setVisibility(View.GONE);
            llSentData.setVisibility(View.GONE);

        } else {
            llSentData.setVisibility(View.VISIBLE);
            //  txtSenderAdd.setVisibility(View.VISIBLE);
        }

        //data set
        if (strDealerID.isEmpty()) {
            txtSenderName.setText("Name  " + " : " + " " + appHelper.getSharedPrefObj().getString(DeviceProcessorName, ""));
            txtSenderAdd.setText("Address  " + " : " + " " + appHelper.getSharedPrefObj().getString(DeviceProcessorAddress, ""));
        } else {
            txtSenderName.setText("Name  " + " : " + " " + appHelper.getSharedPrefObj().getString(DeviceDealerName, ""));
            txtSenderAdd.setText("Address  " + " : " + " " + appHelper.getSharedPrefObj().getString(DeviceDealerAddress, ""));
        }


        txtWordCmpnName = findViewById(R.id.txt_cmp_name);
        txtWordCopyRight = findViewById(R.id.txt_word_copy_right);
        txtWordTimeZone = findViewById(R.id.txt_word_time_zone);
        txtWordTimeAndDate = findViewById(R.id.txt_date_and_time);
        txtWordSystemON = findViewById(R.id.txt_system_on);



        String dateTime = appHelper.getCurrentDateTime(DATE_FORMAT_DD_MM_YYYY2);
        txtWordTimeAndDate.setText(dateTime);


        txtWordTimeZone.setText(getCurrentTimezoneOffset());
    }
    public String getCurrentTimezoneOffset() {

        TimeZone tz = TimeZone.getDefault();
        Calendar cal = GregorianCalendar.getInstance(tz);
        int offsetInMillis = tz.getOffset(cal.getTimeInMillis());

        String offset = String.format("%02d:%02d", Math.abs(offsetInMillis / 3600000), Math.abs((offsetInMillis / 60000) % 60));
        offset = "GMT"+(offsetInMillis >= 0 ? "+" : "-") + offset;

        return offset;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (appHelper.isNetworkAvailable()) {
           // getLanguageDataList();
        } else {
            Toast.makeText(getApplicationContext(), "please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeValues() {

        Sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareContent();
            }
        });

        Backbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });


        previewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.grn_evidence_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ImageView txtgrnPreview = dialog.findViewById(R.id.grndocument);
                String GRNdocument = getIntent().getStringExtra("GRNDocument");
                Log.d("GRNDocument", "GRNDocument" + GRNdocument);

                Picasso.get().load(GRNdocument).error(R.drawable.logotrack)
                        .placeholder(R.drawable.animation_new).into(txtgrnPreview);
                txtgrnPreview.setRotation(90.0f);

                dialog.setTitle("Preview Dialog");
                dialog.show();
            }
        });


        String GRNnumber = getIntent().getStringExtra("GRNNumber");
        Log.d("GRNNumber", "GRNNumber" + GRNNumber);

        String farmerName = getIntent().getStringExtra("FarmerName");
        Log.d("FarmerName", "FarmerName" + FarmerName);

        String address = getIntent().getStringExtra("Farmeradress");
        Log.d("Address", "Address" + Address);

        String quantity = getIntent().getStringExtra("Quantity");
        Log.d("Quantity", "Quantity" + Quantity);

        String contactNo = getIntent().getStringExtra("ContactNo");
        Log.d("ContactNo", "ContactNo" + ContactNo);

        String GRNdate = getIntent().getStringExtra("GRNDate");
        Log.d("GRNDate", "GRNDate" + GRNDate);


        GRNNumber.setText(GRNnumber);
        GRNDate.setText(GRNdate);
        Address.setText(address);
        FarmerName.setText(farmerName);
        ContactNo.setText(contactNo);
        Address.setText(address);
        NetWeight.setText(quantity);
        procurementGRNNumber.setText(GRNnumber);


    }


    private void shareContent() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Share as");
        builder.setItems(new CharSequence[]{"Image", "PDF"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    // User chose to share as an image
                    Bitmap bitmap = loadBitmapFromView(findViewById(R.id.share_card));
                    Log.d("image", "img" + bitmap);
                    if (bitmap != null) {
                        // Share the image
                        shareAsImage(bitmap);
                    }
                } else if (which == 1) {
                    // User chose to share as a PDF
                    createAndSharePDF();
                }
            }
        });
        builder.show();
    }

    // Function to capture a screenshot of a view
    private Bitmap loadBitmapFromView(View view) {
        if (view == null) {
            return null;
        }

        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();

        // Capture the screenshot
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());

        // Clean up
        view.setDrawingCacheEnabled(false);

        return bitmap;
    }

    private void shareAsImage(Bitmap bitmap) {
        if (bitmap == null) {
            // Handle the case where the bitmap is null
            return;
        }

        try {
            // Save the bitmap to a file
            String GRNnumber = getIntent().getStringExtra("GRNNumber");

            File cachePath = new File(getExternalCacheDir(), GRNnumber + ".png");
            try (FileOutputStream out = new FileOutputStream(cachePath)) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            }

            String textMessage = "Find your GRN number: " + GRNnumber;

            // Share the image using an Intent
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(
                    Intent.EXTRA_STREAM,

                    FileProvider.getUriForFile(this, "com.trst01.intellitarck.provider", cachePath)
            );
            // shareIntent.putExtra(Intent.EXTRA_TEXT, textMessage);
            //shareIntent.putExtra(Intent.EXTRA_SUBJECT, textMessage);
            startActivity(Intent.createChooser(shareIntent, "Share Image"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void createAndSharePDF() {
        // Create a PDF document
        PdfDocument document = new PdfDocument();
//        int pageWidth = (int) (8.5 * 72);  // 8.5 inches in points
//        int pageHeight = (int) (100 * 72);   // 11 inches in points
//
//        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(800, 1300, 1).create(); // A4 page size
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        // Create a bitmap of your CardView's content
        Bitmap bitmap = loadBitmapFromView(findViewById(R.id.share_card));

        // Draw the bitmap to the canvas
        canvas.drawBitmap(bitmap, 0, 0, null);

        // Finish the page
        document.finishPage(page);

        // Create a file to save the PDF
        String GRNnumber = getIntent().getStringExtra("GRNNumber");

        File pdfFile = new File(getExternalFilesDir(null), GRNnumber + ".pdf");

        try {
            // Write the PDF document to the file
            FileOutputStream fos = new FileOutputStream(pdfFile);
            document.writeTo(fos);
            document.close();
            fos.close();

            String textMessage = "Find your GRN number: " + GRNnumber;

            // Share the PDF file
            Uri pdfUri = FileProvider.getUriForFile(this, "com.trst01.intellitarck.provider", pdfFile);


            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("application/pdf");
            shareIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
            // shareIntent.putExtra(Intent.EXTRA_TEXT, textMessage);
            // shareIntent.putExtra(Intent.EXTRA_SUBJECT, textMessage);
            startActivity(Intent.createChooser(shareIntent, "Share PDF"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getLanguageDataList() {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        final ProgressDialog progressDialog = new ProgressDialog(ShareGRNActivity.this, R.style.AppCompatAlertDialogStyle);
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
                                String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");


                                if (strLanguageId.equals("1")) {

                                    if (getResources().getString(R.string.goods_receipt_note_grn).equals(jsonEngWord)) {
                                        txtl_GRNReceipt.setText(jsonEngWord);
                                    } else if (getResources().getString(R.string.sent_by).equals(jsonEngWord)) {
                                        txtl_Sentby.setText(jsonEngWord);
                                    } else if (getResources().getString(R.string.grn_no).equals(jsonEngWord)) {
                                        txtl_GRNNumber.setText(jsonEngWord);
                                    } else if (getResources().getString(R.string.date).equals(jsonEngWord)) {
                                        txtl_GRNDate.setText(jsonEngWord);
                                    } else if (getResources().getString(R.string.address).equals(jsonEngWord)) {
                                        txtl_Address.setText(jsonEngWord);
                                    } else if (getResources().getString(R.string.part_a_farmer_agent_details).equals(jsonEngWord)) {
                                        txtl_FarmerDetails.setText(jsonEngWord);
                                    } else if (getResources().getString(R.string.farmer_name).equals(jsonEngWord)) {
                                        txtl_FarmerName.setText(jsonEngWord);
                                    } else if (getResources().getString(R.string.telephone_no).equals(jsonEngWord)) {
                                        txtl_telephoneNo.setText(jsonEngWord);
                                    } else if (getResources().getString(R.string.fax_no).equals(jsonEngWord)) {
                                        txtl_FaxNo.setText(jsonEngWord);
                                    } else if (getResources().getString(R.string.part_b_procurement_details).equals(jsonEngWord)) {
                                        txtl_procurementDetails.setText(jsonEngWord);
                                    } else if (getResources().getString(R.string.grn_no1).equals(jsonEngWord)) {
                                        txtl_grn1.setText(jsonEngWord);
                                    } else if (getResources().getString(R.string.net_weight).equals(jsonEngWord)) {
                                        txtl_NetWeight.setText(jsonEngWord);
                                    } else if (getResources().getString(R.string.share).equals(jsonEngWord)) {
                                        Sharebtn.setText(jsonEngWord);
                                    } else if (getResources().getString(R.string.evidence).equals(jsonEngWord)) {
                                        previewbtn.setText(jsonEngWord);
                                    } else if (getResources().getString(R.string.back).equals(jsonEngWord)) {
                                        Backbtn.setText(jsonEngWord);
                                    } else if (getResources().getString(R.string.this_certificate_is_auto_generated_by_trst01chain_system_on).equals(jsonEngWord)) {
                                        txtWordCopyRight.setText(jsonEngWord);
                                    } else if (getResources().getString(R.string.system_on).equals(jsonEngWord)) {
                                        txtWordSystemON.setText(jsonEngWord);
                                    }

                                } else {
                                    if (getResources().getString(R.string.goods_receipt_note_grn).equals(jsonEngWord)) {
                                        txtl_GRNReceipt.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.sent_by).equals(jsonEngWord)) {
                                        txtl_Sentby.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.grn_no).equals(jsonEngWord)) {
                                        txtl_GRNNumber.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.date).equals(jsonEngWord)) {
                                        txtl_GRNDate.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.address).equals(jsonEngWord)) {
                                        txtl_Address.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.part_a_farmer_agent_details).equals(jsonEngWord)) {
                                        txtl_FarmerDetails.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.farmer_name).equals(jsonEngWord)) {
                                        txtl_FarmerName.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.telephone_no).equals(jsonEngWord)) {
                                        txtl_telephoneNo.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.fax_no).equals(jsonEngWord)) {
                                        txtl_FaxNo.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.part_b_procurement_details).equals(jsonEngWord)) {
                                        txtl_procurementDetails.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.grn_no1).equals(jsonEngWord)) {
                                        txtl_grn1.setText(strConvertedWord);

                                    } else if (getResources().getString(R.string.net_weight).equals(jsonEngWord)) {
                                        txtl_NetWeight.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.share).equals(jsonEngWord)) {
                                        Sharebtn.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.evidence).equals(jsonEngWord)) {
                                        previewbtn.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.back).equals(jsonEngWord)) {
                                        Backbtn.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.this_certificate_is_auto_generated_by_trst01chain_system_on).equals(jsonEngWord)) {
                                        txtWordCopyRight.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.system_on).equals(jsonEngWord)) {
                                        txtWordSystemON.setText(strConvertedWord);
                                    }

                                }


                            }


                        } catch (Exception ex) {
                            ex.printStackTrace();
                            progressDialog.dismiss();
                            Log.d("Error", ">>>>" + ex.toString());
                        }
                    } else {
                        progressDialog.dismiss();

                        Toast.makeText(ShareGRNActivity.this, "no records found", Toast.LENGTH_LONG).show();
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














//
//
//    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault());
//    Date currentLocalTime = calendar.getTime();
//    DateFormat date = new SimpleDateFormat("Z");
//    String localTime = date.format(currentLocalTime);
//
//
//    Calendar cal = Calendar.getInstance();
//    TimeZone tz = cal.getTimeZone();
//        Log.d("Time zone", "=" + tz.getDisplayName());
//
//                //  new SimpleDateFormat(pattern, Locale.getDefault()).format(System.currentTimeMillis());
//
//                String date_N = new SimpleDateFormat("z",Locale.getDefault()).format(System.currentTimeMillis());;
//                //    String localTimeN = date_N.format(currentLocalTime);
//                Log.d(TAG, "initializeUI: date" + date_N);