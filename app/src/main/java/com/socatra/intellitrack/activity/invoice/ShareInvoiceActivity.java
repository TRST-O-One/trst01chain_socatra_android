package com.socatra.intellitrack.activity.invoice;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.DATE_FORMAT_DD_MM_YYYY2;
import static com.socatra.intellitrack.constants.AppConstant.DeviceDealerAddress;
import static com.socatra.intellitrack.constants.AppConstant.DeviceDealerID;
import static com.socatra.intellitrack.constants.AppConstant.DeviceDealerName;
import static com.socatra.intellitrack.constants.AppConstant.DeviceProcessorAddress;
import static com.socatra.intellitrack.constants.AppConstant.DeviceProcessorId;
import static com.socatra.intellitrack.constants.AppConstant.DeviceProcessorName;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserName;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserRole;
import static com.socatra.intellitrack.constants.AppConstant.LanguageId;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.JsonElement;
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

public class ShareInvoiceActivity extends BaseActivity {

    private static final String TAG = ShareInvoiceActivity.class.getCanonicalName();
    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    public AppViewModel viewModel;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    SearchView searchByName;
    String Token, strDealerID, strDealerName, strProcessorId, strLanguageId;


    ImageView ImgCancel;

    Dialog dialog, showAddressDialog;

    TextView Sharebtn, Backbtn, Reportbtn, Mapbtn, CustomerInvoiceBack, txtInvoiceNumber, txtShippingTO, txtShippingAddress, txtSentby, txtShippingByAddress, txtProductName, txtQty, txtInvoiceDate, txtVessel, txtPortOfLoad, txtPortOfDis;

    RelativeLayout rlVessel, rlCustomer, rlSave;

    String deviceRoleName, deviceRole;

    TextView txtl_InvoiceReceipt, txtl_Sentby, txtl_ShippedTo, txtl_InvoiceNumber, txtl_Createddate, txtl_InvoiceDetails, txtl_Product, txtl_vesseldetails, txtl_Quantity, txtl_Vessel, txtl_Portofloading, txtl_portofDischarge;

    String strKey;
    TextView txtWordCopyRight, txtWordCmpnName, txtWordTimeAndDate, txtWordTimeZone, txtWordSystemON;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_share_dialog);

        strKey = getIntent().getStringExtra("key");
        deviceRole = appHelper.getSharedPrefObj().getString(DeviceUserRole, "");
        deviceRoleName = appHelper.getSharedPrefObj().getString(DeviceUserName, "");
        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");
        strProcessorId = appHelper.getSharedPrefObj().getString(DeviceProcessorId, "");
        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");



        initializeUI();
        initializeValues();
//        configureDagger();
//        configureViewModel();
    }

    private void initializeUI() {


        txtInvoiceNumber = findViewById(R.id.txt_share_inv_num);
        txtSentby = findViewById(R.id.txt_share_shipping_from);
        txtShippingByAddress = findViewById(R.id.txt_share_inv_from_address);
        txtShippingTO = findViewById(R.id.txt_share_shipping_to);
        txtShippingAddress = findViewById(R.id.txt_share_inv_address);


        txtProductName = findViewById(R.id.txt_product_name);
        txtQty = findViewById(R.id.txt_share_qty);

        txtInvoiceDate = findViewById(R.id.txt_inv_share_date);
        rlVessel = findViewById(R.id.rl_vessel);
        txtVessel = findViewById(R.id.txt_vessel_name);
        txtPortOfLoad = findViewById(R.id.txt_share_portofloading);
        txtPortOfDis = findViewById(R.id.txt_share_portofdischarge);


        //Language
        //language
        txtl_InvoiceReceipt = findViewById(R.id.share_InvoiceReceipt);
        txtl_Sentby = findViewById(R.id.share_Sentby);
        txtl_ShippedTo = findViewById(R.id.share_ShippedTo);
        txtl_InvoiceNumber = findViewById(R.id.share_Invoice);
        txtl_Createddate = findViewById(R.id.share_Createddate);
        txtl_InvoiceDetails = findViewById(R.id.share_InvoiceDetails);
        txtl_Product = findViewById(R.id.share_Product);
        txtl_Quantity = findViewById(R.id.share_Quantity);
        txtl_vesseldetails = findViewById(R.id.share_VesselDetails);
        txtl_Vessel = findViewById(R.id.share_Vessel);
        txtl_Portofloading = findViewById(R.id.share_Port_of_loading);
        txtl_portofDischarge = findViewById(R.id.share_Port_of_discharge);
        Sharebtn = findViewById(R.id.txtShare);
        Backbtn = findViewById(R.id.txtBack);


//        rlCustomer=findViewById(R.id.Customer_report_layout);
//        rlSave = findViewById(R.id.save_layout);

        if (strKey.equals("cust")) {
            txtSentby.setText(getIntent().getStringExtra("processorName"));
            txtShippingByAddress.setText(getIntent().getStringExtra("processorAdd"));
        } else {
            if (strDealerID.isEmpty()) {
                txtSentby.setText(appHelper.getSharedPrefObj().getString(DeviceProcessorName, ""));
                txtShippingByAddress.setText(appHelper.getSharedPrefObj().getString(DeviceProcessorAddress, ""));
            } else {
                txtSentby.setText(appHelper.getSharedPrefObj().getString(DeviceDealerName, ""));
                txtShippingByAddress.setText(appHelper.getSharedPrefObj().getString(DeviceDealerAddress, ""));
            }
        }


//        txtShippingByAddress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addressDialog();
//            }
//        });


        txtWordCmpnName = findViewById(R.id.txt_cmp_name);
        txtWordCopyRight = findViewById(R.id.txt_word_copy_right);
        txtWordTimeZone = findViewById(R.id.txt_word_time_zone);
        txtWordTimeAndDate = findViewById(R.id.txt_date_and_time);
        txtWordSystemON = findViewById(R.id.txt_system_on);


        String dateTime = appHelper.getCurrentDateTime(DATE_FORMAT_DD_MM_YYYY2);
        txtWordTimeAndDate.setText(dateTime);

        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        Log.d("Time zone", "=" + tz.getDisplayName());


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
    protected void onResume() {
        super.onResume();
        if (appHelper.isNetworkAvailable()) {
          // getLanguageDataList();
        } else {
            Toast.makeText(getApplicationContext(), "please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void addressDialog() {

        showAddressDialog = new Dialog(this, R.style.MyAlertDialogThemeNew);
        showAddressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showAddressDialog.setContentView(R.layout.processor_details_dialog);
        showAddressDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        showAddressDialog.setCanceledOnTouchOutside(true);
        showAddressDialog.setCancelable(true);
        showAddressDialog.show();

        TextView txtTitle = showAddressDialog.findViewById(R.id.txt_title);
        LinearLayout llData = showAddressDialog.findViewById(R.id.ll_data_prc);
        TextView txtAdd = showAddressDialog.findViewById(R.id.txt_add_inv);
        LinearLayout llSubData = showAddressDialog.findViewById(R.id.ll_inv_add);
        llData.setVisibility(View.GONE);
        llSubData.setVisibility(View.VISIBLE);


        txtTitle.setText("Address");

        if (strDealerID.isEmpty()) {
            txtAdd.setText(appHelper.getSharedPrefObj().getString(DeviceProcessorAddress, ""));
        } else {
            txtAdd.setText(appHelper.getSharedPrefObj().getString(DeviceDealerAddress, ""));
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initializeValues() {


        if (deviceRole.equalsIgnoreCase("Dealer")) {
            rlVessel.setVisibility(View.GONE);

        } else if (deviceRole.equalsIgnoreCase("Processor")) {
            rlVessel.setVisibility(View.VISIBLE);

        } else {
            rlVessel.setVisibility(View.VISIBLE);

        }

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


//        CustomerInvoiceBack.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });


//        ImgCancel.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                Reportdialog();
//            }
//        });


//        Reportbtn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                Reportdialog();
//            }
//        });
//
//        Mapbtn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(ShareInvoiceActivity.this, MapActivity.class);
//                startActivity(intent);
//            }
//        });


        String InvoiceNumber = getIntent().getStringExtra("INVNumber");

        String strShippingTo = getIntent().getStringExtra("shippingTO");


        String strShippingAdd = getIntent().getStringExtra("shippingAdd");

        String strProductName = getIntent().getStringExtra("product");

        String strProductQty = getIntent().getStringExtra("qty");

        String strInvDate = getIntent().getStringExtra("INvDate");


//        Log.d("GRNDate","GRNDate" + GRNDate);


        txtSentby.setText(deviceRoleName);
        txtInvoiceNumber.setText(InvoiceNumber);
        txtInvoiceDate.setText(strInvDate);
        txtShippingTO.setText(strShippingTo);

        txtShippingAddress.setText(strShippingAdd);
        txtProductName.setText(strProductName);
        txtQty.setText(strProductQty);

        String vessel = getIntent().getStringExtra("vessel");
        if (vessel == null || vessel.isEmpty()) {
            txtVessel.setText("Not available");
        } else {
            txtVessel.setText(vessel);
        }
        String PortofDis = getIntent().getStringExtra("PortofDis");
        if (PortofDis == null || PortofDis.isEmpty()) {
            txtPortOfDis.setText("Not available");
        } else {
            txtPortOfDis.setText(PortofDis);
        }

        String PortofLoad = getIntent().getStringExtra("PortofLoad");
        if (PortofLoad == null || PortofLoad.isEmpty()) {
            txtPortOfLoad.setText("Not available");
        } else {
            txtPortOfLoad.setText(PortofLoad);
        }

    }

    private void Reportdialog() {

        dialog = new Dialog(this, R.style.MyAlertDialogThemeNew);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.invoice_report_dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.show();

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

    // Function to share the captured image
    private void shareAsImage(Bitmap bitmap) {
        if (bitmap == null) {
            // Handle the case where the bitmap is null
            return;
        }

        try {
            // Save the bitmap to a file
            File cachePath = new File(getExternalCacheDir(), "share_image.png");
            try (FileOutputStream out = new FileOutputStream(cachePath)) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            }

            // Share the image using an Intent
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(
                    Intent.EXTRA_STREAM,
                    FileProvider.getUriForFile(this, "com.trst01.intellitarck.provider", cachePath)
            );
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

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(600, 1300, 1).create(); // A4 page size
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        // Create a bitmap of your CardView's content
        Bitmap bitmap = loadBitmapFromView(findViewById(R.id.share_card));

        // Draw the bitmap to the canvas
        canvas.drawBitmap(bitmap, 0, 0, null);

        // Finish the page
        document.finishPage(page);

        // Create a file to save the PDF
        File pdfFile = new File(getExternalFilesDir(null), "sample.pdf");

        try {
            // Write the PDF document to the file
            FileOutputStream fos = new FileOutputStream(pdfFile);
            document.writeTo(fos);
            document.close();
            fos.close();

            // Share the PDF file
            Uri pdfUri = FileProvider.getUriForFile(this, "com.trst01.intellitarck.provider", pdfFile);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("application/pdf");
            shareIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
            startActivity(Intent.createChooser(shareIntent, "Share PDF"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getLanguageDataList() {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        final ProgressDialog progressDialog = new ProgressDialog(ShareInvoiceActivity.this, R.style.AppCompatAlertDialogStyle);
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

                                    if (getResources().getString(R.string.invoice_receipt).equals(jsonEngWord)) {
                                        txtl_InvoiceReceipt.setText(jsonEngWord);
                                    }else if (getResources().getString(R.string.sent_by).equals(jsonEngWord)) {
                                        txtl_Sentby.setText(jsonEngWord);
                                    }else if(getResources().getString(R.string.shipped_to).equals(jsonEngWord)) {
                                        txtl_ShippedTo.setText(jsonEngWord);
                                    }else if(getResources().getString(R.string.inovice_number).equals(jsonEngWord)) {
                                        txtl_InvoiceNumber.setText(jsonEngWord);
                                    }else if(getResources().getString(R.string.created_date).equals(jsonEngWord)){
                                        txtl_Createddate.setText(jsonEngWord);
                                    }  else if(getResources().getString(R.string.part_a_invoice_details).equals(jsonEngWord)) {
                                        txtl_InvoiceDetails.setText(jsonEngWord);
                                    } else if(getResources().getString(R.string.product).equals(jsonEngWord)) {
                                        txtl_Product.setText(jsonEngWord);
                                    } else if (getResources().getString(R.string.qty).equals(jsonEngWord)) {
                                        txtl_Quantity.setText(jsonEngWord);
                                    }else if(getResources().getString(R.string.part_b_vessel_details).equals(jsonEngWord)) {
                                        txtl_vesseldetails.setText(jsonEngWord);
                                    }else if(getResources().getString(R.string.vessel).equals(jsonEngWord)) {
                                        txtl_Vessel.setText(jsonEngWord);
                                    }else if(getResources().getString(R.string.port_of_loading).equals(jsonEngWord)) {
                                        txtl_Portofloading.setText(jsonEngWord);
                                    }else if(getResources().getString(R.string.port_of_discharge).equals(jsonEngWord)) {
                                        txtl_portofDischarge.setText(jsonEngWord);
                                    } else if(getResources().getString(R.string.back).equals(jsonEngWord)){
                                        Backbtn.setText(jsonEngWord);
                                    } else if(getResources().getString(R.string.share).equals(jsonEngWord)) {
                                        Sharebtn.setText(jsonEngWord);
                                    } else if (getResources().getString(R.string.this_Invoice_is_auto_generated_by_trst01chain_system_on).equals(jsonEngWord)) {
                                        txtWordCopyRight.setText(jsonEngWord);
                                    } else if (getResources().getString(R.string.system_on).equals(jsonEngWord)) {
                                        txtWordSystemON.setText(jsonEngWord);
                                    }


                                } else {
                                    if (getResources().getString(R.string.invoice_receipt).equals(jsonEngWord)) {
                                        txtl_InvoiceReceipt.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.sent_by).equals(jsonEngWord)) {
                                        txtl_Sentby.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.shipped_to).equals(jsonEngWord)) {
                                        txtl_ShippedTo.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.inovice_number).equals(jsonEngWord)) {
                                        txtl_InvoiceNumber.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.created_date).equals(jsonEngWord)) {
                                        txtl_Createddate.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.part_a_invoice_details).equals(jsonEngWord)) {
                                        txtl_InvoiceDetails.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.product).equals(jsonEngWord)) {
                                        txtl_Product.setText(strConvertedWord);
                                    } else if(getResources().getString(R.string.quantity).equals(jsonEngWord)) {
                                        txtl_Quantity.setText(strConvertedWord);
                                    }else if(getResources().getString(R.string.part_b_vessel_details).equals(jsonEngWord)) {
                                        txtl_vesseldetails.setText(strConvertedWord);
                                    }else if(getResources().getString(R.string.vessel).equals(jsonEngWord)) {
                                        txtl_Vessel.setText(strConvertedWord);
                                    }else if(getResources().getString(R.string.port_of_loading).equals(jsonEngWord)) {
                                        txtl_Portofloading.setText(strConvertedWord);
                                    }else if(getResources().getString(R.string.port_of_discharge).equals(jsonEngWord)) {
                                        txtl_portofDischarge.setText(strConvertedWord);
                                    } else if(getResources().getString(R.string.back).equals(jsonEngWord)){
                                        Backbtn.setText(strConvertedWord);
                                    } else if(getResources().getString(R.string.share).equals(jsonEngWord)) {
                                        Sharebtn.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.this_Invoice_is_auto_generated_by_trst01chain_system_on).equals(jsonEngWord)) {
                                        txtWordCopyRight.setText(jsonEngWord);
                                    } else if (getResources().getString(R.string.system_on).equals(jsonEngWord)) {
                                        txtWordSystemON.setText(jsonEngWord);
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

                        Toast.makeText(ShareInvoiceActivity.this, "no records found", Toast.LENGTH_LONG).show();
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

