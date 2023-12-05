package com.socatra.intellitrack.activity.grnflow;


import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.DATE_FORMAT_YYYY_MM_DD;
import static com.socatra.intellitrack.constants.AppConstant.DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS;
import static com.socatra.intellitrack.constants.AppConstant.DeviceProcessorId;

import static com.socatra.intellitrack.constants.AppConstant.DeviceUserID;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserName;
import static com.socatra.intellitrack.constants.AppConstant.LanguageId;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;
import com.socatra.intellitrack.BaseActivity;
import com.socatra.intellitrack.R;
import com.socatra.intellitrack.adapters.ProcessorFarmerListForDialogAdapter;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.constants.CommonUtils;
import com.socatra.intellitrack.database.entity.AddGRNDetailsSubmitTable;
import com.socatra.intellitrack.database.entity.GetDealerDataFromServer;
import com.socatra.intellitrack.database.entity.GetFarmerListFromServerTable;
import com.socatra.intellitrack.database.entity.GetModeOfTransportDataFromServerTable;
import com.socatra.intellitrack.models.get.GetInvoiceDetailsForGrnByProcessorIdandDealerId;
import com.socatra.intellitrack.view_models.AppViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProcessorAddGRNActivity extends BaseActivity implements View.OnClickListener, HasSupportFragmentInjector, ProcessorFarmerListForDialogAdapter.SyncCallbackInterface {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 1001;
    public static final int REQUEST_CAM_PERMISSIONS = 1;//cam
    private static final String TAG = ProcessorAddGRNActivity.class.getCanonicalName();
    private static final int FILE_SELECT_CODE = 1;
    private static final int CAMERA_REQUEST = 1888;//cam
    static File f = null;//cam
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    public AppViewModel viewModel;


    TextView txtGrnDetails, txtProcessor, txtSelectType, txtFarmer, txtDealer, txtInvoice, txtQuantity, txtModeOfTransport, txtGrnDate, txtGRNEvidence;
    ImageView imgCancel;
    TextView txtSave;
    Dialog farmerDialog, dialog;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    String strSelectFarmer, strSelectTransportID, strSelectTransport, strSelectDealerID, strSelectDealerName, strSelectTransportIDs, strManufatureName, strLanguageId, strDealerID, strSelectDate, strProcessorId, strGRNValue, strQuantity, strSelectFarmerId, strSelectInvoiceName, strSelectInvoiceID = "";
    AppCompatSpinner spSelectFarmer, spSelectModeOfTransport, spSelectTypeData, spSelectDealer, spInvoice;

    TextView txtNoDataFound;
    LinearLayout llSelectFarmer, llSelectDealer, llInvoice;
    List<String> farmerList = new ArrayList<>();
    List<String> farmerListIds = new ArrayList<>();
    String mTag = "GrnPro";
    List<String> DealerID = new ArrayList<>();
    List<String> DelaerList = new ArrayList<>();
    List<String> modeOfTransportList = new ArrayList<>();
    List<String> modeOfTransportListIds = new ArrayList<>();
    List<String> InvoiceList = new ArrayList<>();
    List<String> InvoiceId = new ArrayList<>();
    TextInputEditText txtManufactureName;
    TextInputEditText txtEtQuantity, txtEtDRC, txtSelectDate;
    CardView cdDate;
    ImageButton imageDate;
    String[] selecttypearr = new String[]{"Select *", "Farmer", "Dealer"};
    TextView txtSelectFarmer;
    ImageView imgGrnProEv;
    File selectedFile;
    String selectedFilePath = "";
    Integer testPictureCount = 0;
    String strFileExtension1 = null;//cam
    int pickHandle1 = 0;
    int imageTaken = 0;
    Bitmap bitmapDocument = null;//cam
    ArrayList<GetInvoiceDetailsForGrnByProcessorIdandDealerId> getInvoicedetails;

    ArrayAdapter<String> invoiceListDataAdapter;
    String spselecttype;
    RecyclerView recFarmerList;
    ArrayList<GetFarmerListFromServerTable> farmersSearchList;
    ProcessorFarmerListForDialogAdapter farmerByProcessorIDAdapter;
    ImageView imgCross;
    SearchView searchByName;
    CardView cdSelectFarmer;
    String spTxtWordSelect = "Select", spTxtWordDealer = "Dealer", spTxtWordFarmer = "Farmer";
    private String[] PERMISSIONS_STORAGE = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private String strDocumentImageLocalImagePath = "";
    private byte[] bytes = null;//cam

    public static String getMimeType(String url) {

        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File file = new File(path, url);

        try {
            // Make sure the Pictures directory exists.
            path.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return String.valueOf(file);
    }

    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manfacture_grnactivity);
        strProcessorId = appHelper.getSharedPrefObj().getString(DeviceProcessorId, "");
        strManufatureName = appHelper.getSharedPrefObj().getString(DeviceUserName, "");
        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");
        Log.d(TAG, "Language id" + strLanguageId);
        initializeUI();
        initializeValues();
        configureDagger();
        configureViewModel();
    }

    private void initializeUI() {
        farmersSearchList = new ArrayList<>();
        imgCancel = findViewById(R.id.img_cancel);
        txtManufactureName = findViewById(R.id.et_manufacturer_name);
        cdDate = findViewById(R.id.cd_date_select);
        imageDate = findViewById(R.id.btn_open_date_picker_m);

        spSelectTypeData = findViewById(R.id.sp_selecttype);

        llSelectFarmer = findViewById(R.id.ll_farmer);
        llSelectDealer = findViewById(R.id.ll_dealer);
        llInvoice = findViewById(R.id.ll_Invoice_processor);

        spSelectFarmer = findViewById(R.id.sp_farmer_name);
        spSelectDealer = findViewById(R.id.sp_dealer_name);
        spSelectModeOfTransport = findViewById(R.id.sp_mode_of_transport);
        spInvoice = findViewById(R.id.sp_select_Invoice_prc);


        txtEtQuantity = findViewById(R.id.et_quantity);
        txtEtDRC = findViewById(R.id.et_drc);
        txtSelectDate = findViewById(R.id.et_grn_date);


//        selectButtonGrnPro = findViewById(R.id.selectButtonGrnPro);
        imgGrnProEv = findViewById(R.id.imgGrnproEv);

        txtNoDataFound = findViewById(R.id.txt_no_data);


        txtSelectFarmer = findViewById(R.id.txt_select_farmer);
        cdSelectFarmer = findViewById(R.id.cd_select_farmer);


        //language words

        txtGrnDetails = findViewById(R.id.txt_GRN_details);
        txtProcessor = findViewById(R.id.txt_processor);
        txtSelectType = findViewById(R.id.txt_selecttype);
        txtFarmer = findViewById(R.id.txt_farmer);
        txtDealer = findViewById(R.id.txt_dealer);
        txtInvoice = findViewById(R.id.txt_Invoice);
        txtQuantity = findViewById(R.id.txt_l_quantity);
        txtModeOfTransport = findViewById(R.id.txt_mode_of_transport);
        txtGrnDate = findViewById(R.id.txt_grn_date);
        txtGRNEvidence = findViewById(R.id.txt_evidence_document);
        txtSave = findViewById(R.id.txt_Save);


    }

    private void initializeValues() {

        txtSelectFarmer.setOnClickListener(this);
        cdSelectFarmer.setOnClickListener(this);

        txtManufactureName.setText(strManufatureName);

        imgCancel.setOnClickListener(this);
        txtSave.setOnClickListener(this);
        cdDate.setOnClickListener(this);
        imageDate.setOnClickListener(this);

        txtSelectDate.setOnClickListener(this);

        //getLanguageDataList();

        imgGrnProEv.setOnClickListener(view -> {
            if (imageTaken == 0) {
                pickImageDialog(0);
            } else if (imageTaken == 1) {
//                previewImageDialog(selectedFilePath);
//
            }
        });


        //For Select Type
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ProcessorAddGRNActivity.this,
                android.R.layout.simple_spinner_item, selecttypearr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spSelectTypeData.setAdapter(dataAdapter);
        spSelectTypeData.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spselecttype = parent.getItemAtPosition(position).toString();


                if (spselecttype.equals(spTxtWordSelect)) {
                    llSelectFarmer.setVisibility(View.GONE);
                    llSelectDealer.setVisibility(View.GONE);
                    llInvoice.setVisibility(View.GONE);

                    DealerID.clear();
                    DelaerList.clear();
                    farmerListIds.clear();
                    farmerList.clear();
                    InvoiceList.clear();
                    InvoiceId.clear();
                } else if (spTxtWordFarmer.equals(spselecttype)) {
                    llSelectFarmer.setVisibility(View.VISIBLE);
                    llSelectDealer.setVisibility(View.GONE);
                    llInvoice.setVisibility(View.GONE);
                    DealerID.clear();
                    DelaerList.clear();
                    InvoiceList.clear();
                    InvoiceId.clear();
                    txtEtQuantity.setText("");


                    if (appHelper.isNetworkAvailable()) {

                        getFarmerList();


                        //getSyncFarmerAllDataFromServer();
                    } else {
                        Toast.makeText(ProcessorAddGRNActivity.this, "please check your  internet connection", Toast.LENGTH_SHORT).show();
                    }

                } else if (spTxtWordDealer.equals(spselecttype)) {
                    farmerListIds.clear();
                    farmerList.clear();
                    InvoiceList.clear();
                    InvoiceId.clear();
                    txtSelectFarmer.setText("");
                    strSelectFarmer = "";
                    llSelectDealer.setVisibility(View.VISIBLE);
                    llSelectFarmer.setVisibility(View.GONE);
                    llInvoice.setVisibility(View.VISIBLE);

                    if (appHelper.isNetworkAvailable()) {
                        getManufactureDealerData();
                    } else {


                        Toast.makeText(ProcessorAddGRNActivity.this, "please check your  internet connection", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spSelectFarmer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //  if (appHelper.isNetworkAvailable()) {
                strSelectFarmer = farmerList.get(position);
                strSelectFarmerId = farmerListIds.get(position);
                // ClusterNameTv.setText(ClusterName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spSelectDealer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Log.d("OnItemSelected", "Item selected");
                //   String DealerId = DealerID.get(position);
                strSelectDealerName = DelaerList.get(position);
                strSelectDealerID = DealerID.get(position);
                if (appHelper.isNetworkAvailable()) {
                    getInvoice(strSelectDealerID);
//                            invoiceListDataAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ProcessorAddGRNActivity.this, "please check your internet connection", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Handle nothing selected if needed
            }
        });
        spInvoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                if (position == 0) {
                    // "--select Invoice--" is selected; clear the EditText
                    txtEtQuantity.setText("");
                } else {
                    // An actual invoice is selected; update the EditText with the quantity
                    String selectedInvoice = InvoiceList.get(position - 1); // Adjust the index to match your data
                    strSelectInvoiceName = InvoiceList.get(position - 1);
                    strSelectInvoiceID = InvoiceId.get(position - 1); // Adjust the index

                    GetInvoiceDetailsForGrnByProcessorIdandDealerId selectedInvoiceData = getInvoicedetails.get(position - 1); // Adjust the index
                    String Quantity = String.valueOf(selectedInvoiceData.getQuantity());

                    if (Quantity != null && !Quantity.equals("null")) {
                        // Quantity is not null, so set it in the EditText
                        txtEtQuantity.setText(Quantity);
                    } else {
                        // Handle the case where "Quantityv  " is null or 'null'
                        txtEtQuantity.setText("");
                        // Clear the text field or handle it as needed
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Handle nothing selected if needed
            }
        });
//        spInvoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                //  if (appHelper.isNetworkAvailable()) {
//                strSelectInvoiceName = InvoiceList.get(position);
//                strSelectInvoiceID = InvoiceId.get(position);
//                // ClusterNameTv.setText(ClusterName);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


        spSelectModeOfTransport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //  if (appHelper.isNetworkAvailable()) {
                strSelectTransport = modeOfTransportList.get(position);
                strSelectTransportIDs = modeOfTransportListIds.get(position);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        String dateTime = appHelper.getCurrentDateTime(DATE_FORMAT_YYYY_MM_DD);
        txtSelectDate.setText(dateTime);
        strSelectDate = appHelper.getCurrentDateTime(DATE_FORMAT_YYYY_MM_DD);
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        cdDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar currentDate = Calendar.getInstance();
                Calendar date = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(ProcessorAddGRNActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date.set(year, monthOfYear, dayOfMonth);
                        new TimePickerDialog(ProcessorAddGRNActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                date.set(Calendar.MINUTE, minute);
                                Log.v(TAG, "The choosen one " + date.getTime());
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS, Locale.US);
                                strSelectDate = simpleDateFormat.format(date.getTime());

                                SimpleDateFormat showDate = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD, Locale.US);
                                String showDate_value = showDate.format(date.getTime());

                                txtSelectDate.setText(showDate_value);
                            }
                        }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
                    }
                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
                DatePicker datePicker = datePickerDialog.getDatePicker();
//                Calendar c = Calendar.getInstance();
//                c.add(Calendar.DATE, -1);
//                datePicker.setMinDate(c.getTimeInMillis());
//                datePicker.setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

    }


    private void pickImageDialog(int i) {
        dialog = new Dialog(ProcessorAddGRNActivity.this, R.style.MyAlertDialogThemeNew);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.image_dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);


        LinearLayout fromGallery = dialog.findViewById(R.id.ll_gallery);
        LinearLayout fromCamera = dialog.findViewById(R.id.ll_camera);

        fromGallery.setOnClickListener(v -> {
            dialog.dismiss();
            openFilePermissions();
        });

        fromCamera.setOnClickListener(v -> {
            dialog.dismiss();
            openCameraPermission(true, i);

        });

        dialog.show();
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    private void getFarmerList() {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getFarmerListByManufactureFromServer(strProcessorId, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));
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
                            ArrayList<GetFarmerListFromServerTable> getFarmerListTableArrayList = new ArrayList<>();
                            farmersSearchList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                GetFarmerListFromServerTable farmerTable = new GetFarmerListFromServerTable();
                                farmerTable.setFarmerCode(jsonObjectFarmerPD.getString("FarmerCode"));
                                farmerTable.setFarmerName(jsonObjectFarmerPD.getString("FarmerName"));
                                getFarmerListTableArrayList.add(farmerTable);
                            }

                            // Update the adapter with the new data
                            farmersSearchList.addAll(getFarmerListTableArrayList);

                            // farmerByProcessorIDAdapter.notifyDataSetChanged();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Log.d("Error", ">>>>" + ex.toString());
                        }
                    } else {
                        Toast.makeText(ProcessorAddGRNActivity.this, "no records found", Toast.LENGTH_LONG).show();
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

    private void farmerDialogue() {
        farmerDialog = new Dialog(this, R.style.MyAlertDialogThemeNew);
        farmerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        farmerDialog.setContentView(R.layout.dialog_farmer_details_list);
        farmerDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        farmerDialog.setCanceledOnTouchOutside(true);
        farmerDialog.setCancelable(true);
        farmerDialog.show();
        recFarmerList = farmerDialog.findViewById(R.id.rec_dialog_farmer);
        searchByName = farmerDialog.findViewById(R.id.svFarmer_dialog);
        LinearLayout imgempty = farmerDialog.findViewById(R.id.emptyListImage);
        imgCross = farmerDialog.findViewById(R.id.img_cross_dig);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ProcessorAddGRNActivity.this, LinearLayoutManager.VERTICAL, false);
        recFarmerList.setLayoutManager(layoutManager);
        recFarmerList.hasFixedSize();

        if (farmersSearchList.size() > 0) {
            farmerByProcessorIDAdapter = new ProcessorFarmerListForDialogAdapter(ProcessorAddGRNActivity.this, ProcessorAddGRNActivity.this, appHelper, viewModel, (List<GetFarmerListFromServerTable>) farmersSearchList, "farmer");
            recFarmerList.setAdapter(farmerByProcessorIDAdapter);
            farmerByProcessorIDAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(ProcessorAddGRNActivity.this, "no data found", Toast.LENGTH_SHORT).show();
        }


//        farmerByDealerIdAdapter = new FarmerListForDialogAdapter(appHelper, viewModel, (List<GetFarmerbyDealerid>) farmersSearchList, "farmer");
//        recycleFarmer.setAdapter(farmerByProcessorIDAdapter);
//        farmerByProcessorIDAdapter.notifyDataSetChanged();

        imgCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                farmerDialog.dismiss();
                //farmerDialog.se
            }
        });
        if (searchByName != null) {
//            SearchManager searchManager = (SearchManager) getApplicationContext().getSystemService(Context.SEARCH_SERVICE);
//            searchByName.setSearchableInfo(searchManager
//                    .getSearchableInfo(getComponentName()));
//            searchByName.setMaxWidth(Integer.MAX_VALUE);
            // listening to search query text change
            SearchManager searchManager = (SearchManager) ProcessorAddGRNActivity.this.getSystemService(Context.SEARCH_SERVICE);
            searchByName.setSearchableInfo(searchManager
                    .getSearchableInfo(ProcessorAddGRNActivity.this.getComponentName()));
            searchByName.setMaxWidth(Integer.MIN_VALUE);
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
                    if (farmerByProcessorIDAdapter != null && farmerByProcessorIDAdapter.getFilter() != null) {
                        farmerByProcessorIDAdapter.getFilter().filter(query);
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    if (farmerByProcessorIDAdapter != null && farmerByProcessorIDAdapter.getFilter() != null) {
                        farmerByProcessorIDAdapter.getFilter().filter(query);
                    }
                    return false;
                }
            });

        }


    }

    public void configureViewModel() {
        //  viewModel = ViewModelProviders.of(this, viewModelFactory).get(AppViewModel.class);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);

        if (appHelper.isNetworkAvailable()) {
            getModeofTransportDataFromServer();
        } else {
            Toast.makeText(ProcessorAddGRNActivity.this, "please check your  internet connection", Toast.LENGTH_SHORT).show();
        }


    }

    // TODO: 8/24/2023 spinner drop down selection data
    public void getFarmerListFromserverByDelaerID(String dealerID) {
        try {
            viewModel.getFarmerDetailsListDataFromserver(dealerID);
            if (viewModel.getFarmerDetailListTableLivedata() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<GetFarmerListFromServerTable> getFarmerListFromServerTableList = (List<GetFarmerListFromServerTable>) o;
                        viewModel.getFarmerDetailListTableLivedata().removeObserver(this);
                        // List<GetFarmerListFromServerTable> getFarmerListFromServerTableList = (List<GetFarmerListFromServerTable>) o;
                        farmerListIds.clear();
                        farmerList.clear();

                        if (getFarmerListFromServerTableList != null && getFarmerListFromServerTableList.size() > 0) {
                            // Collections.reverse(dataResponseDTO);
                            Toast.makeText(ProcessorAddGRNActivity.this, "ok", Toast.LENGTH_SHORT).show();

                            for (int i = 0; i < getFarmerListFromServerTableList.size(); i++) {
                                farmerListIds.add(getFarmerListFromServerTableList.get(i).getFarmerCode());
                                farmerList.add(getFarmerListFromServerTableList.get(i).getFarmerName());
                                //strStateID = stateListResponseDTOList.get(i).getStateId();
                                Log.e(":dsvbjl_id_farmer_code", getFarmerListFromServerTableList.get(i).getFarmerCode());
                                Log.e(":dsvbjl_farmer", getFarmerListFromServerTableList.get(i).getFarmerId());
                                Log.e(":dsvbjl__farmer", getFarmerListFromServerTableList.get(i).getFarmerName());
                            }
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ProcessorAddGRNActivity.this,
                                    R.layout.spinner_dropdown_layout, farmerList);
                            dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
                            spSelectFarmer.setAdapter(dataAdapter);
                            dataAdapter.notifyDataSetChanged();


                        } else {

                            Toast.makeText(ProcessorAddGRNActivity.this, "Unable to fetch Details", Toast.LENGTH_SHORT).show();
                        }

                    }
                };
                viewModel.getloginResponseDTOFromServerLiveData().observe(this, getLeadRawDataObserver);
            }

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
    }

    public void getSyncFarmerAllDataFromServer() {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getFarmerListByManufactureFromServer(strProcessorId, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));
        final ProgressDialog progressDialog = new ProgressDialog(ProcessorAddGRNActivity.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading farmer data...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
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
                            ArrayList<GetFarmerListFromServerTable> getFarmerListFromServerTables = new ArrayList<>();
                            farmerListIds.clear();
                            farmerList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                GetFarmerListFromServerTable farmerTable = new GetFarmerListFromServerTable();
                                farmerTable.setFarmerCode(jsonObjectFarmerPD.getString("FarmerCode"));
                                farmerTable.setFarmerName(jsonObjectFarmerPD.getString("FarmerName"));
                                getFarmerListFromServerTables.add(farmerTable);
                            }
                            for (int i = 0; i < getFarmerListFromServerTables.size(); i++) {
                                farmerListIds.add(getFarmerListFromServerTables.get(i).getFarmerCode());
                                farmerList.add(getFarmerListFromServerTables.get(i).getFarmerName());
                                //strStateID = stateListResponseDTOList.get(i).getStateId();
                                Log.e(":dsvbjl_id_farmer_code", getFarmerListFromServerTables.get(i).getFarmerCode());
                                Log.e(":dsvbjl__farmer", getFarmerListFromServerTables.get(i).getFarmerName());
                            }
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ProcessorAddGRNActivity.this,
                                    R.layout.spinner_dropdown_layout, farmerList);
                            dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
                            spSelectFarmer.setAdapter(dataAdapter);
                            dataAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                            // Toast.makeText(AddGrnActivity.this, "Fetch All Data From Server SucessFully", Toast.LENGTH_LONG).show();

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Log.d("Error", ">>>>" + ex.toString());
                        }

                    } else {
                        Toast.makeText(ProcessorAddGRNActivity.this, "no records found", Toast.LENGTH_LONG).show();
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

    public void getManufactureDealerData() {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getDealerDataByManufactureFromServer(strProcessorId, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));
        final ProgressDialog progressDialog = new ProgressDialog(ProcessorAddGRNActivity.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading Dealer data...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
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
                            ArrayList<GetDealerDataFromServer> getSubDealerDataFromServerArrayList = new ArrayList<>();
                            DealerID.clear();
                            DelaerList.clear();
                            // DelaerList.add("select");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonDealerID = jsonArray.getJSONObject(i);
                                GetDealerDataFromServer subDealerDataFromServer = new GetDealerDataFromServer();
                                subDealerDataFromServer.setDealerId(jsonDealerID.getString("DealerId"));
                                subDealerDataFromServer.setDealerName(jsonDealerID.getString("DealerName"));
                                DealerID.add(jsonDealerID.getString("DealerId"));
                                DelaerList.add(jsonDealerID.getString("DealerName"));
                                getSubDealerDataFromServerArrayList.add(subDealerDataFromServer);
                            }

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ProcessorAddGRNActivity.this,
                                    R.layout.spinner_dropdown_layout, DelaerList);
                            dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
                            spSelectDealer.setAdapter(dataAdapter);
                            dataAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();


                            // Toast.makeText(AddGrnActivity.this, "Fetch All Data From Server SucessFully", Toast.LENGTH_LONG).show();

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Log.d("Error", ">>>>" + ex.toString());
                        }

                    } else {
                        Toast.makeText(ProcessorAddGRNActivity.this, "no records found", Toast.LENGTH_LONG).show();
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

    private void getInvoice(String DealerId) {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getInvoiceDetailsForGrnByProcessorIdandDealerIdFromServer(strProcessorId, DealerId, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));

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
                            getInvoicedetails = new ArrayList<>();
                            txtNoDataFound.setVisibility(View.GONE);
                            spInvoice.setVisibility(View.VISIBLE);
                            InvoiceId.clear();
                            InvoiceList.clear();
                            InvoiceList.add("Select Invoice");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonSubDealerID = jsonArray.getJSONObject(i);
                                GetInvoiceDetailsForGrnByProcessorIdandDealerId InvoiceDataFromServer = new GetInvoiceDetailsForGrnByProcessorIdandDealerId();
                                InvoiceDataFromServer.setInvoiceNumber(jsonSubDealerID.getString("InvoiceNumber"));
                                InvoiceDataFromServer.setId(jsonSubDealerID.getString("Id"));
                                InvoiceId.add(jsonSubDealerID.getString("Id"));
                                InvoiceList.add(jsonSubDealerID.getString("InvoiceNumber"));

                                if (!jsonSubDealerID.isNull("Quantity")) {
                                    String totalQuantityString = jsonSubDealerID.getString("Quantity");
                                    Log.d("Quantity", "Quantity: " + totalQuantityString);
                                    InvoiceDataFromServer.setQuantity((totalQuantityString));
                                } else {
                                    Log.d("TotalQuantity", "TotalQuantity is null or not found");
                                    // Handle the case where "TotalQuantity" is null or not present in the response
                                }

                                getInvoicedetails.add(InvoiceDataFromServer);
                            }
//                            for (int i = 0; i < getInvoicedetails.size(); i++) {
//
//                                InvoiceId.add(getInvoicedetails.get(i).getId());
//                                InvoiceList.add(getInvoicedetails.get(i).getInvoiceNumber());
//                                Log.d(TAG, "InvoiceList contents: " + InvoiceList.toString());
//
//
//
//
//                            }
                            ArrayAdapter<String> invoiceListDataAdapter = new ArrayAdapter<String>(ProcessorAddGRNActivity.this,
                                    R.layout.spinner_dropdown_layout, InvoiceList);
                            invoiceListDataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
                            spInvoice.setAdapter(invoiceListDataAdapter);
                            invoiceListDataAdapter.notifyDataSetChanged();

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Log.d("Error", ">>>>" + ex.toString());
                        }

                    } else {
                        txtEtQuantity.setText("");
                        txtNoDataFound.setVisibility(View.VISIBLE);
                        spInvoice.setVisibility(View.GONE);
                        strSelectInvoiceID = "";
                        Toast.makeText(ProcessorAddGRNActivity.this, "no records found", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();

                    txtNoDataFound.setVisibility(View.VISIBLE);
                    spInvoice.setVisibility(View.GONE);
                    txtEtQuantity.setText("");
                    strSelectInvoiceID = "";
                    Log.d("Error", ">>>>" + ex.toString());
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

                txtNoDataFound.setVisibility(View.VISIBLE);
                spInvoice.setVisibility(View.GONE);
                txtEtQuantity.setText("");
                strSelectInvoiceID = "";
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());

            }
        });


    }

    public void getModeofTransportDataFromServer() {
        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        // callRetrofit = service.getGetLogisticsDetailsDataByIDFromServer(strManufactureId, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));

        callRetrofit = service.getGetLogisticsDetailsDataFromServer(appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));

        final ProgressDialog progressDialog = new ProgressDialog(ProcessorAddGRNActivity.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
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
                            ArrayList<GetModeOfTransportDataFromServerTable> getModeOfTransportDataFromServerTableArrayList = new ArrayList<>();
                            farmerListIds.clear();
                            farmerList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                GetModeOfTransportDataFromServerTable getModeOfTransportDataFromServerTable = new GetModeOfTransportDataFromServerTable();
                                getModeOfTransportDataFromServerTable.setId(jsonObjectFarmerPD.getString("Id"));
                                getModeOfTransportDataFromServerTable.setVehicleType(jsonObjectFarmerPD.getString("VehicleType"));
                                getModeOfTransportDataFromServerTableArrayList.add(getModeOfTransportDataFromServerTable);
                            }
                            for (int i = 0; i < getModeOfTransportDataFromServerTableArrayList.size(); i++) {
                                modeOfTransportListIds.add(getModeOfTransportDataFromServerTableArrayList.get(i).getId());
                                modeOfTransportList.add(getModeOfTransportDataFromServerTableArrayList.get(i).getVehicleType());
                                //strStateID = stateListResponseDTOList.get(i).getStateId();
                                Log.e(":dsvbjl__farmer", getModeOfTransportDataFromServerTableArrayList.get(i).getVehicleType());
                            }
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ProcessorAddGRNActivity.this,
                                    R.layout.spinner_dropdown_layout, modeOfTransportList);
                            dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
                            spSelectModeOfTransport.setAdapter(dataAdapter);
                            dataAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                            // Toast.makeText(AddGrnActivity.this, "Fetch All Data From Server SucessFully", Toast.LENGTH_LONG).show();

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Log.d("Error", ">>>>" + ex.toString());
                        }

                    } else {
                        Toast.makeText(ProcessorAddGRNActivity.this, "no records found", Toast.LENGTH_LONG).show();
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProcessorAddGRNActivity.this, NavGrnActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {

        try {
            switch (v.getId()) {
                case R.id.txt_select_farmer:
                case R.id.cd_select_farmer:
                    farmerDialogue();
                    break;
                case R.id.img_cancel:
                    onBackPressed();
                    break;
                case R.id.cd_date_select:
                case R.id.et_grn_date:
                case R.id.btn_open_date_picker_m:
                    final Calendar currentDate = Calendar.getInstance();
                    Calendar date = Calendar.getInstance();
                    DatePickerDialog datePickerDialog = new DatePickerDialog(ProcessorAddGRNActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            date.set(year, monthOfYear, dayOfMonth);
                            new TimePickerDialog(ProcessorAddGRNActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    date.set(Calendar.MINUTE, minute);
                                    Log.v(TAG, "The choosen one " + date.getTime());
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS, Locale.US);
                                    strSelectDate = simpleDateFormat.format(date.getTime());

                                    SimpleDateFormat showDate = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD, Locale.US);
                                    String showDate_value = showDate.format(date.getTime());

                                    txtSelectDate.setText(showDate_value);
                                }
                            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
                        }
                    }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
                    DatePicker datePicker = datePickerDialog.getDatePicker();
//                Calendar c = Calendar.getInstance();
//                c.add(Calendar.DATE, -1);
//                datePicker.setMinDate(c.getTimeInMillis());
//                datePicker.setMaxDate(System.currentTimeMillis());
                    datePickerDialog.show();

                    break;
                case R.id.txt_Save:
                    if (spselecttype.equals("Dealer") && strSelectInvoiceID.isEmpty()) {
                        Toast.makeText(ProcessorAddGRNActivity.this, "please select dealer invoice", Toast.LENGTH_LONG).show();
                    } else if (TextUtils.isEmpty(txtEtQuantity.getText().toString().trim())) {
                        txtEtQuantity.setError("Enter Quantity");
                    } else if (strSelectTransport.isEmpty()) {
                        Toast.makeText(ProcessorAddGRNActivity.this, "please select mode of transport", Toast.LENGTH_LONG).show();
                    } else if (Objects.equals(selectedFilePath, "")) {
                        Toast.makeText(ProcessorAddGRNActivity.this, "please add image", Toast.LENGTH_LONG).show();
                    } else if (appHelper.isNetworkAvailable()) {
                        // String strFarmerCode = etFarmerCode.getText().toString().trim();
                        strQuantity = txtEtQuantity.getText().toString().trim();
                        AddGRNDetailsSubmitTable addGRNDetailsSubmitTable = new AddGRNDetailsSubmitTable();
                        addGRNDetailsSubmitTable.setFromProcessorId(strProcessorId);
                        addGRNDetailsSubmitTable.setQuantity(strQuantity);
                        addGRNDetailsSubmitTable.setGRNdate(strSelectDate);
                        addGRNDetailsSubmitTable.setModeofTransport(strSelectTransportIDs);
                        addGRNDetailsSubmitTable.setGRNDocument("");
                        addGRNDetailsSubmitTable.setFromDealerId("");
                        if (spselecttype.equals(getResources().getString(R.string.dealer))) {
                            addGRNDetailsSubmitTable.setFarmerCode("");
                            addGRNDetailsSubmitTable.setToDealerId(strSelectDealerID);
                            addGRNDetailsSubmitTable.setInvoiceId(strSelectInvoiceID);
                        } else {
                            addGRNDetailsSubmitTable.setFarmerCode(strSelectFarmerId);
                            addGRNDetailsSubmitTable.setToDealerId("");
                            addGRNDetailsSubmitTable.setInvoiceId("");
                        }


                        String dateTime = appHelper.getCurrentDateTime(DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS);
                        Log.d(TAG, "onClick: date" + dateTime);

                        addGRNDetailsSubmitTable.setIsActive("1");
                        addGRNDetailsSubmitTable.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                        addGRNDetailsSubmitTable.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                        addGRNDetailsSubmitTable.setCreatedDate(dateTime);
                        addGRNDetailsSubmitTable.setUpdatedDate(dateTime);
                        uploadImagesTOServerAPI(selectedFile, addGRNDetailsSubmitTable);


                    } else {

                        Toast.makeText(ProcessorAddGRNActivity.this, "please check your  internet connection", Toast.LENGTH_SHORT).show();

                    }
                    break;
//                case R.id.selectButtonGrnPro:
//                    showFileChooser();
//                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    //For file path
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, FILE_SELECT_CODE);
    }

    //For file path
    private void openFilePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (!CommonUtils.isPermissionAllowed(ProcessorAddGRNActivity.this, Manifest.permission.CAMERA))) {
            android.util.Log.v(TAG, "Location Permissions Not Granted");
            ActivityCompat.requestPermissions(
                    ProcessorAddGRNActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_CAM_PERMISSIONS
            );
        } else {
            firstDispatchTakePictureIntent(FILE_SELECT_CODE);
        }
    }

    private void firstDispatchTakePictureIntent(int actionCode) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*"); // Allow all file types
        String[] mimeTypes = {"image/*", "application/pdf", "text/*"}; // Specify MIME types if needed
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, actionCode);
    }

    //For file path
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_SELECT_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();


            if (uri != null) {
//                selectedFilePath = getRealPathFromUri(uri);
//                if (selectedFilePath!=null){
//                    Log.e(mTag,"selPath2:"+selectedFilePath);
//                    selectedFile = new File(selectedFilePath);
//                } else {
                selectedFilePath = uri.getPath();
                Log.e(mTag, "Selected File Path: " + selectedFilePath); // Log the selected file path


                try {
                    selectedFile = appHelper.convertUriToFile(ProcessorAddGRNActivity.this, uri);
                    imageTaken = 1;

                    Log.e(mTag, "selPath2:" + selectedFilePath);
//                        selectedFile = new File(selectedFilePath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
//                }

//                fileDisplay.setText("Selected File: " + selectedFilePath);

//                selectedFile = new File(selectedFilePath);

                Picasso.get()
                        .load(uri)
                        .error(R.drawable.baseline_broken_image_24)
                        .into(imgGrnProEv);
                imgGrnProEv.setRotation(90.0f);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode) {
                case CAMERA_REQUEST:
                    if (resultCode == RESULT_OK) {
                        try {
//                        imgDocUpload.setClickable(false);
                            pickHandle1 = 1;
                            imageTaken = 1;
                            handleBigCameraPhoto();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
//                    strImageOnePath = null;
//                    strImageOnePath = null;
                    }
                    break;

                case FILE_SELECT_CODE:
                    if (resultCode == RESULT_OK) {
                        try {
                            if (testPictureCount == 0) {//for strWaterCycleLocalPath
//                            imgFarmer.setClickable(false);
                                imageTaken = 1;
                                Uri uri = data.getData();
//                            String realUriSt = getRealPathFromUri(uri);
//                            strFarmerImageLocalImagePath = realUriSt;//String.valueOf(uri);
//                            strFarmerRUri=realUriSt;
//                            handleBigCameraPhoto();
                                File IMAGE_COPY_PATH = createImageFileFirst();
                                try {
                                    InputStream inputStream = getContentResolver().openInputStream(uri);//Uri.parse(realUriSt)
                                    OutputStream outputStream = new FileOutputStream(IMAGE_COPY_PATH);
                                    copyFile(inputStream, outputStream);
                                    selectedFilePath = IMAGE_COPY_PATH.getAbsolutePath();
                                    inputStream.close();
                                    outputStream.close();
                                    handleBigCameraPhoto();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;

            }
        }
    }

    public String getRealPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }
        return uri.getPath(); // If cursor is null, fall back to the original Uri path
    }

    private void uploadImagesTOServerAPI(File uploadFile, AddGRNDetailsSubmitTable addGRNDetailsSubmitTable) {

        Log.d(mTag, "uploadDataBaseTOServerAPI: " + uploadFile.getAbsolutePath());
        MultipartBody.Part file_pathDB = null;

//        File file = new File(uploadFile.getAbsolutePath());
        // Parsing any Media type file
        RequestBody requestBody = RequestBody.create(MediaType.parse(getMimeType(String.valueOf(uploadFile))), uploadFile);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", uploadFile.getName(), requestBody);
//        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        Log.d("Filepath", ">>>>>>>>>>" + fileToUpload);
        RequestBody r_acces_token = RequestBody.create(MediaType.parse("multipart/form-data"),
                appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));
        RequestBody r_userID = RequestBody.create(MediaType.parse("multipart/form-data"),
                appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<ResponseBody> callRetrofit = null;
        callRetrofit = service.uploadFileDataToServer(r_userID, fileToUpload);
        callRetrofit.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String strResponse = response.body().string();
                    Log.d(mTag, "onResponse: AppData " + response);
                    JSONObject json_object = new JSONObject(strResponse);
                    String message = "";
                    int status = 0;
                    Log.e(mTag, "onResponse: data json" + json_object);
                    message = json_object.getString("message");
                    status = json_object.getInt("status");
                    String location = json_object.getString("location");
                    Log.e(mTag, "status of location in new method" + location);
//                    Toast.makeText(ManfactureGRNActivity.this, location, Toast.LENGTH_SHORT).show();
                    if (status == 1) {
                        addGRNDetailsSubmitTable.setGRNDocument(location);

                        Log.e(mTag, json_object.getString("location"));
                        //to server
                        insertGrnDataToServer(addGRNDetailsSubmitTable, "processor");

                        // Toast.makeText(ProcessorAddGRNActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else if (status == 0) {
                        Log.e(mTag, "status of location in new method error message");
                        //  Toast.makeText(ProcessorAddGRNActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(mTag, "status of location in new method error message");
                        //  Toast.makeText(ProcessorAddGRNActivity.this, message, Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                    Log.e(mTag, "status of location in new method error");
                    Log.d("Error Call", ">>>>" + ex.toString());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
            }
        });
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return null;
    }

    public void insertGrnDataToServer(AddGRNDetailsSubmitTable addGRNDetailsSubmitTable, String typeRequest) {
        try {
            viewModel.syncGRNlListDataToServer(addGRNDetailsSubmitTable, typeRequest);
            if (viewModel.getStringLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        viewModel.getStringLiveData().removeObserver(this);
                        String strMessage = (String) o;
                        Toast.makeText(ProcessorAddGRNActivity.this, strMessage, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ProcessorAddGRNActivity.this, NavGrnActivity.class);
                        startActivity(intent);
                        finish();
                    }
                };
                viewModel.getStringLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            //  appHelper.getDialogHelper().getLoadingDialog().closeDialog();
            // INSERT_LOG("syncMonitoringModuleDetailsDataToServer", "Exception : " + ex.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (appHelper.isNetworkAvailable()) {
        //    getLanguageDataList();
        } else {
            Toast.makeText(getApplicationContext(), "please check your internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void openCameraPermission(Boolean land, Integer pictureCount) {

        testPictureCount = pictureCount;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (!CommonUtils.isPermissionAllowed(ProcessorAddGRNActivity.this, Manifest.permission.CAMERA))) {
            android.util.Log.v(TAG, "Location Permissions Not Granted");
            ActivityCompat.requestPermissions(
                    ProcessorAddGRNActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_CAM_PERMISSIONS
            );
        } else {

            firstDispatchTakePictureIntent(CAMERA_REQUEST, land);


        }
    }

    private void firstDispatchTakePictureIntent(int actionCode, Boolean land) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        switch (actionCode) {
            case CAMERA_REQUEST:
                if (testPictureCount == 0) {
                    selectedFile = null;
                    selectedFilePath = null;
                    try {
                        selectedFile = setUpPhotoFile();

                        selectedFilePath = selectedFile.getAbsolutePath();

                        strFileExtension1 = selectedFile.getAbsolutePath().substring(selectedFile.getAbsolutePath().lastIndexOf("."));


                        Uri photoURI = FileProvider.getUriForFile(ProcessorAddGRNActivity.this,
                                "com.trst01.intellitarck.provider",
                                selectedFile);


                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    } catch (IOException e) {
                        e.printStackTrace();
                        selectedFile = null;
                        selectedFilePath = null;
                    }
                } else {

                }

                break;

            default:
                break;
        } // switch
        startActivityForResult(takePictureIntent, actionCode);

    }

    private File setUpPhotoFile() throws IOException {

        File selectedFile;
        selectedFile = createImageFileFirst();

        if (testPictureCount == 0) {
            selectedFilePath = selectedFile.getAbsolutePath();
        }
        return selectedFile;
    }

    private File createImageFileFirst() {
        File image = null;
        File mediaStorageDir = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mediaStorageDir = new File(ProcessorAddGRNActivity.this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "Chain_Documents");
        } else
            mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "Chain_Documents");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("App", "failed to create directory");
            }
        }
//            File image = null;
        try {
            image = File.createTempFile("imageFiles", ".jpg", mediaStorageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
//
        return image;
    }

    private void copyFile(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
    }

    private void handleBigCameraPhoto() throws Exception {

        if (testPictureCount == 0) {
            if (selectedFilePath != null) {
                setPic();
//                galleryAddPic();
            }
        }
    }

    private void setPic() throws Exception {

        /* There isn't enough memory to open up more than a couple camera photos */
        /* So pre-scale the target bitmap into which the file is decoded */

        /* Get the size of the ImageView */

        if (testPictureCount == 0) {
            int targetW = imgGrnProEv.getWidth();
            int targetH = imgGrnProEv.getHeight();

            /* Get the size of the image */
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(selectedFilePath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            /* Figure out which way needs to be reduced less */
            int scaleFactor = 1;
            if ((targetW > 0) || (targetH > 0)) {
                scaleFactor = Math.min(photoW / targetW, photoH / targetH);
            }

            /* Set bitmap options to scale the image decode target */
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            bitmapDocument = BitmapFactory.decodeFile(selectedFilePath, bmOptions);

            getBytesFromBitmap(bitmapDocument);

            ExifInterface ei = new ExifInterface(selectedFilePath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            /* Decode the JPEG file into a Bitmap */

            Bitmap rotatedBitmap = null;
            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmapDocument, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmapDocument, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmapDocument, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmapDocument;
            }

            imgGrnProEv.setImageBitmap(rotatedBitmap);
            imgGrnProEv.invalidate();


        }
    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        bytes = stream.toByteArray();
        return stream.toByteArray();
    }


    private void getLanguageDataList() {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        final ProgressDialog progressDialog = new ProgressDialog(ProcessorAddGRNActivity.this, R.style.AppCompatAlertDialogStyle);
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
                                    if (getResources().getString(R.string.grn_details).equals(jsonEngWord)) {
                                        txtGrnDetails.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.processor).equals(jsonEngWord)) {
                                        txtProcessor.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.quantity).equals(jsonEngWord)) {
                                        txtQuantity.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.mode_of_transport).equals(jsonEngWord)) {
                                        txtModeOfTransport.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.grn_date).equals(jsonEngWord)) {
                                        txtGrnDate.setText(jsonEngWord1);
                                    }else if (getResources().getString(R.string.evidence_document).equals(jsonEngWord)) {
                                        txtGRNEvidence.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.save).equals(jsonEngWord)) {
                                        txtSave.setText(jsonEngWord1);
                                    } else if (spTxtWordSelect.equals(jsonEngWord)) {
                                        spTxtWordSelect = spTxtWordSelect;
                                    } else if (spTxtWordDealer.equals(jsonEngWord)) {
                                        spTxtWordDealer = spTxtWordDealer;
                                    } else if (spTxtWordFarmer.equals(jsonEngWord)) {
                                        spTxtWordFarmer = spTxtWordFarmer;
                                    }


                                } else {
                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");

                                    if (getResources().getString(R.string.grn_details).equals(jsonEngWord)) {
                                        txtGrnDetails.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.processor).equals(jsonEngWord)) {
                                        txtProcessor.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.quantity).equals(jsonEngWord)) {
                                        txtQuantity.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.mode_of_transport).equals(jsonEngWord)) {
                                        txtModeOfTransport.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.grn_date).equals(jsonEngWord)) {
                                        txtGrnDate.setText(strConvertedWord);
                                    }else if (getResources().getString(R.string.evidence_document).equals(jsonEngWord)) {
                                        txtGRNEvidence.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.save).equals(jsonEngWord)) {
                                        txtSave.setText(strConvertedWord);
                                    } else if (spTxtWordSelect.equals(jsonEngWord)) {
                                        spTxtWordSelect = strConvertedWord;
                                    } else if (spTxtWordDealer.equals(jsonEngWord)) {
                                        spTxtWordDealer = strConvertedWord;
                                    } else if (spTxtWordFarmer.equals(jsonEngWord)) {
                                        spTxtWordFarmer = strConvertedWord;
                                    }


                                }

//                                else if (strLanguageId.equals("3")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//                                    if ("GRN Details".equals(jsonEngWord)) {
//                                        txtGrnDetails.setText(strConvertedWord);
//                                    } else if ("Processors".equals(jsonEngWord)) {
//                                        txtProcessor.setText(strConvertedWord);
//                                    } else if ("Quantity ".equals(jsonEngWord)) {
//                                        txtQuantity.setText(strConvertedWord);
//                                    } else if ("Mode of Transport".equals(jsonEngWord)) {
//                                        txtModeOfTransport.setText(strConvertedWord);
//                                    } else if ("GRN Date".equals(jsonEngWord)) {
//                                        txtGrnDate.setText(strConvertedWord);
//                                    } else if ("Save".equals(jsonEngWord)) {
//                                        txtSave.setText(strConvertedWord);
//                                    }
//
//                                } else if (strLanguageId.equals("4")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//                                    if ("GRN Details".equals(jsonEngWord)) {
//                                        txtGrnDetails.setText(strConvertedWord);
//                                    } else if ("Processors".equals(jsonEngWord)) {
//                                        txtProcessor.setText(strConvertedWord);
//                                    } else if ("Quantity ".equals(jsonEngWord)) {
//                                        txtQuantity.setText(strConvertedWord);
//                                    } else if ("Mode of Transport ".equals(jsonEngWord)) {
//                                        txtModeOfTransport.setText(strConvertedWord);
//                                    } else if ("GRN Date".equals(jsonEngWord)) {
//                                        txtGrnDate.setText(strConvertedWord);
//                                    } else if ("Save".equals(jsonEngWord)) {
//                                        txtSave.setText(strConvertedWord);
//                                    }
//
//
//                                } else if (strLanguageId.equals("5")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//                                    if ("GRN Details".equals(jsonEngWord)) {
//                                        txtGrnDetails.setText(strConvertedWord);
//                                    } else if ("Processors".equals(jsonEngWord)) {
//                                        txtProcessor.setText(strConvertedWord);
//                                    } else if ("Quantity ".equals(jsonEngWord)) {
//                                        txtQuantity.setText(strConvertedWord);
//                                    } else if ("Mode of Transport ".equals(jsonEngWord)) {
//                                        txtModeOfTransport.setText(strConvertedWord);
//                                    } else if ("GRN Date".equals(jsonEngWord)) {
//                                        txtGrnDate.setText(strConvertedWord);
//                                    } else if ("Save".equals(jsonEngWord)) {
//                                        txtSave.setText(strConvertedWord);
//                                    }
//
//
//                                } else {
//
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

                        Toast.makeText(ProcessorAddGRNActivity.this, "no records found", Toast.LENGTH_LONG).show();
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


    @Override
    public void selectPosition(int position, GetFarmerListFromServerTable getFarmerListFromServerTable, List<GetFarmerListFromServerTable> getFarmerListFromServerTableList) {
        txtSelectFarmer.setText(getFarmerListFromServerTable.getFarmerName());
        strSelectFarmerId = getFarmerListFromServerTable.getFarmerCode();
        farmerDialog.dismiss();
    }
}

