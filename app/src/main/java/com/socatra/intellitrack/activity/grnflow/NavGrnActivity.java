package com.socatra.intellitrack.activity.grnflow;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.DeviceDealerID;
import static com.socatra.intellitrack.constants.AppConstant.DeviceProcessorId;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserName;
import static com.socatra.intellitrack.constants.AppConstant.LanguageId;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;


import com.google.gson.JsonElement;
import com.socatra.intellitrack.BaseActivity;
import com.socatra.intellitrack.R;
import com.socatra.intellitrack.activity.main_dash_board.MainDashBoardActivity;
import com.socatra.intellitrack.adapters.NavGrnAdapter;

import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.models.get.GetGrndetails;
import com.socatra.intellitrack.view_models.AppViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavGrnActivity extends BaseActivity {

    private static final String TAG = NavGrnActivity.class.getCanonicalName();
    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    public AppViewModel viewModel;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    SearchView searchByName;
    String Token, strDealerID, strDealerName, strProcessorId, strLanguageId;

    Button addgrnbtn;

    ImageView ImgCancel;


    LinearLayout imgempty;

    RecyclerView recycleNavGrn;

    ArrayList<GetGrndetails> NavGrnlist;

    NavGrnAdapter navGrnAdapter;

    TextView txt_headerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_grn);
        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");
        strProcessorId = appHelper.getSharedPrefObj().getString(DeviceProcessorId, "");
        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");

        initializeUI();
        initializeValues();
        configureDagger();
        configureViewModel();

    }

    private void initializeUI() {
        ImgCancel = findViewById(R.id.img_cancel);
        recycleNavGrn = findViewById(R.id.recycler_nav_Grn);
        searchByName = findViewById(R.id.svNavGrn);
        addgrnbtn = findViewById(R.id.add_grn_btn);
        imgempty = findViewById(R.id.emptyListImage);


        //language
        txt_headerview = findViewById(R.id.txtGrnListView);


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(NavGrnActivity.this, MainDashBoardActivity.class); // Replace MainActivity with your actual dashboard activity
        // intent.addCategory(Intent.CATEGORY_HOME);
        // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        // finishAffinity();
    }

    private void initializeValues() {
        strDealerName = appHelper.getSharedPrefObj().getString(DeviceUserName, "");
        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");
        Token = appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, "");

//        getLanguageDataList();


        ImgCancel.setOnClickListener(view -> {
            Intent intent = new Intent(NavGrnActivity.this, MainDashBoardActivity.class); // Replace MainActivity with your actual dashboard activity
            //   intent.addCategory(Intent.CATEGORY_HOME);
            //  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });


        if (strDealerID.isEmpty()) {
            addgrnbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(NavGrnActivity.this, ProcessorAddGRNActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            addgrnbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(NavGrnActivity.this, DealerAddGrnActivity.class);
                    startActivity(intent);
                }
            });
        }

        if (searchByName != null) {
            SearchManager searchManager = (SearchManager) NavGrnActivity.this.getSystemService(Context.SEARCH_SERVICE);
            searchByName.setSearchableInfo(searchManager
                    .getSearchableInfo(NavGrnActivity.this.getComponentName()));
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
                    if (navGrnAdapter != null && navGrnAdapter.getFilter() != null) {
                        navGrnAdapter.getFilter().filter(query);
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    if (navGrnAdapter != null && navGrnAdapter.getFilter() != null) {
                        navGrnAdapter.getFilter().filter(query);
                    }
                    return false;
                }
            });

        }


        recycleNavGrn.setLayoutManager(new LinearLayoutManager(this));
        recycleNavGrn.hasFixedSize();

        if (appHelper.isNetworkAvailable()) {
            getGrnList();
        } else {
            Toast.makeText(NavGrnActivity.this, "please check internet connection ", Toast.LENGTH_SHORT).show();
        }


    }

    private void showItemDetailsDialog(GetGrndetails item) {
        // Create and show the dialog here. Customize the dialog content as needed.
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.grn_share_dialog);
        dialog.show();

        TextView Share = dialog.findViewById(R.id.txtShare);


        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareContent();
                Share.setVisibility(View.GONE);
            }
        });


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

    private void shareContent() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Share as");
        builder.setItems(new CharSequence[]{"Image", "PDF"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    // User chose to share as an image
                    Bitmap bitmap = loadBitmapFromView(findViewById(R.id.share_card));
                    // Find the "Share" button
                    TextView shareButton = findViewById(R.id.txtShare);
                    shareButton.setVisibility(View.GONE);

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


//    private void shareAsImage(Bitmap bitmap) {
//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        shareIntent.setType("image/jpeg");
//
//        // Save the bitmap to a file
//        File imageFile = new File(getExternalCacheDir(), "shared_image.jpg");
//        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        startActivity(Intent.createChooser(shareIntent, "Share via"));
//    }


    private void createAndSharePDF() {
        Document document = new Document();
        String pdfFilePath = Environment.getExternalStorageDirectory() + "/my_layout.pdf";

        try {
            PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));
            document.open();

            // Create a sample PDF content (you can customize this)
            document.add(new Paragraph("This is a sample PDF created from your layout."));

            document.close();

            // Share the PDF
//            File file = new File(pdfFilePath);
//            Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
//            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("application/pdf");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(Intent.createChooser(shareIntent, "Share Via"));
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to create PDF.", Toast.LENGTH_SHORT).show();
        }
    }


    private void configureDagger() {
        AndroidInjection.inject(NavGrnActivity.this);
    }

    public void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
    }

    private void getGrnList() {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        final ProgressDialog progressDialog = new ProgressDialog(NavGrnActivity.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading  data...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        if (strDealerID.isEmpty()) {
            callRetrofit = service.getGrnDetailsByProcessorIdFromServer(strProcessorId, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));

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
                                ArrayList<GetGrndetails> GrnArraylist = new ArrayList<>();
                                NavGrnlist = new ArrayList<>();
                                NavGrnlist.clear();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                    GetGrndetails Grnlist = new GetGrndetails();
                                    Grnlist.setId(Integer.valueOf(jsonObjectFarmerPD.getString("Id")));
                                    Grnlist.setGRNnumber(jsonObjectFarmerPD.getString("GRNnumber"));
                                    Grnlist.setFarmerCode(jsonObjectFarmerPD.getString("FarmerCode"));
                                    Grnlist.setProcessor(jsonObjectFarmerPD.getString("Processor"));
                                    Grnlist.setFarmerName(jsonObjectFarmerPD.getString("FarmerName"));
                                    Grnlist.setPrimaryContactNo(jsonObjectFarmerPD.getString("PrimaryContactNo"));
                                    Grnlist.setToDealerName(jsonObjectFarmerPD.getString("ToDealerName"));
                                    Grnlist.setFarmeradress(jsonObjectFarmerPD.getString("farmeradress"));
                                    Grnlist.setQuantity(Integer.valueOf(jsonObjectFarmerPD.getString("Quantity")));
                                    Grnlist.setCurrentQty(Integer.valueOf(jsonObjectFarmerPD.getString("CurrentQty")));
                                    Grnlist.setGRNdate(jsonObjectFarmerPD.getString("GRNdate"));
                                    Grnlist.setGRNDocument(jsonObjectFarmerPD.getString("GRNDocument"));

                                    GrnArraylist.add(Grnlist);
                                }

                                // Update the adapter with the new data
                                NavGrnlist.addAll(GrnArraylist);
                                navGrnAdapter = new NavGrnAdapter(appHelper, viewModel, NavGrnlist);
                                recycleNavGrn.setAdapter(navGrnAdapter);

                                if (navGrnAdapter.getItemCount() == 0) {
                                    imgempty.setVisibility(View.VISIBLE);
                                    recycleNavGrn.setVisibility(View.GONE);
                                } else {
                                    imgempty.setVisibility(View.GONE);
                                    recycleNavGrn.setVisibility(View.VISIBLE);
                                }

                                int count = navGrnAdapter.getItemCount();
                                Log.d("count", "AdapterCount:  " + count);
                                navGrnAdapter.notifyDataSetChanged();

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
                            Toast.makeText(NavGrnActivity.this, "no records found", Toast.LENGTH_LONG).show();
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
            callRetrofit = service.getGrnDetailsByDealerIdFromServer(strDealerID, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));
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
                                ArrayList<GetGrndetails> GrnArraylist = new ArrayList<>();
                                NavGrnlist = new ArrayList<>();
                                NavGrnlist.clear();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                    GetGrndetails Grnlist = new GetGrndetails();
                                    Grnlist.setId(Integer.valueOf(jsonObjectFarmerPD.getString("Id")));
                                    Grnlist.setGRNnumber(jsonObjectFarmerPD.getString("GRNnumber"));
                                    Grnlist.setFarmerCode(jsonObjectFarmerPD.getString("FarmerCode"));
                                    // Grnlist.setDealer(jsonObjectFarmerPD.getString("Dealer"));
                                    Grnlist.setFarmerName(jsonObjectFarmerPD.getString("FarmerName"));
                                    //Grnlist.setToDealerName(jsonObjectFarmerPD.getString("ToDealerName"));
                                    Grnlist.setPrimaryContactNo(jsonObjectFarmerPD.getString("PrimaryContactNo"));
                                    Grnlist.setFarmeradress(jsonObjectFarmerPD.getString("farmeradress"));
                                    Grnlist.setQuantity(Integer.valueOf(jsonObjectFarmerPD.getString("Quantity")));
                                    Grnlist.setCurrentQty(Integer.valueOf(jsonObjectFarmerPD.getString("CurrentQty")));
                                    Grnlist.setGRNdate(jsonObjectFarmerPD.getString("GRNdate"));
                                    Grnlist.setGRNDocument(jsonObjectFarmerPD.getString("GRNDocument"));


                                    GrnArraylist.add(Grnlist);
                                }

                                // Update the adapter with the new data
                                NavGrnlist.addAll(GrnArraylist);
                                navGrnAdapter = new NavGrnAdapter(appHelper, viewModel, NavGrnlist);

                                if (navGrnAdapter.getItemCount() == 0) {
                                    imgempty.setVisibility(View.VISIBLE);
                                    recycleNavGrn.setVisibility(View.GONE);
                                } else {
                                    imgempty.setVisibility(View.GONE);
                                    recycleNavGrn.setVisibility(View.VISIBLE);
                                }
                                recycleNavGrn.setAdapter(navGrnAdapter);
                                navGrnAdapter.notifyDataSetChanged();

//                            if (adapter.getItemCount() == 0) {
//                                Toast.makeText(FarmerDetailsListActivity.this, "Adapter is empty", Toast.LENGTH_LONG).show();
//                            }


                            } catch (Exception ex) {
                                ex.printStackTrace();
                                progressDialog.dismiss();
                                Log.d("Error", ">>>>" + ex.toString());
                            }
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(NavGrnActivity.this, "no records found", Toast.LENGTH_LONG).show();
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

    private void getLanguageDataList() {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        final ProgressDialog progressDialog = new ProgressDialog(NavGrnActivity.this, R.style.AppCompatAlertDialogStyle);
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
                                    if (getResources().getString(R.string.add_grn).equals(jsonEngWord)) {
                                        addgrnbtn.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.grn_list).equals(jsonEngWord)) {
                                        txt_headerview.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.search_by_grn_no).equals(jsonEngWord)) {
                                        searchByName.setQueryHint(jsonEngWord1);
                                    }
                                } else {

                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
                                    if (getResources().getString(R.string.add_grn).equals(jsonEngWord)) {
                                        addgrnbtn.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.grn_list).equals(jsonEngWord)) {
                                        txt_headerview.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.search_by_grn_no).equals(jsonEngWord)) {
                                        searchByName.setQueryHint(strConvertedWord);
                                    }

                                }

//                                else if (strLanguageId.equals("3")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//                                    if (getResources().getString(R.string.add_grn).equals(jsonEngWord)) {
//                                        addgrnbtn.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.grn_list).equals(jsonEngWord)) {
//                                        txt_headerview.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.search_by_grn_no).equals(jsonEngWord)) {
//                                        searchByName.setQueryHint(strConvertedWord);
//                                    }
//
//                                } else if (strLanguageId.equals("4")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//                                    if (getResources().getString(R.string.add_grn).equals(jsonEngWord)) {
//                                        addgrnbtn.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.grn_list).equals(jsonEngWord)) {
//                                        txt_headerview.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.search_by_grn_no).equals(jsonEngWord)) {
//                                        searchByName.setQueryHint(strConvertedWord);
//                                    }
//
//
//                                } else if (strLanguageId.equals("5")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//                                    if (getResources().getString(R.string.add_grn).equals(jsonEngWord)) {
//                                        addgrnbtn.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.grn_list).equals(jsonEngWord)) {
//                                        txt_headerview.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.search_by_grn_no).equals(jsonEngWord)) {
//                                        searchByName.setQueryHint(strConvertedWord);
//                                    }
//
//
//                                } else {
//                                    String jsonEngWord1 = jsonObjectFarmerPD.getString("SelectedWord");
//                                    if (getResources().getString(R.string.add_grn).equals(jsonEngWord)) {
//                                        addgrnbtn.setText(jsonEngWord1);
//                                    } else if (getResources().getString(R.string.grn_list).equals(jsonEngWord)) {
//                                        txt_headerview.setText(jsonEngWord1);
//                                    } else if (getResources().getString(R.string.search_by_grn_no).equals(jsonEngWord)) {
//                                        searchByName.setQueryHint(jsonEngWord1);
//                                    }
//
//
//                                }


                            }


                        } catch (Exception ex) {
                            ex.printStackTrace();
                            progressDialog.dismiss();
                            Log.d("Error", ">>>>" + ex.toString());
                        }
                    } else {
                        progressDialog.dismiss();

                        Toast.makeText(NavGrnActivity.this, "no records found", Toast.LENGTH_LONG).show();
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