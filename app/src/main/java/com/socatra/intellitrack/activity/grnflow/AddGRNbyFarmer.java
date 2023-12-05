package com.socatra.intellitrack.activity.grnflow;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.DATE_FORMAT_DD_MM_YYYY2;
import static com.socatra.intellitrack.constants.AppConstant.DATE_FORMAT_YYYY_MM_DD;
import static com.socatra.intellitrack.constants.AppConstant.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS;
import static com.socatra.intellitrack.constants.AppConstant.DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS;
import static com.socatra.intellitrack.constants.AppConstant.DeviceDealerID;
import static com.socatra.intellitrack.constants.AppConstant.DeviceProcessorId;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserID;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserName;
import static com.socatra.intellitrack.constants.AppConstant.LanguageId;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.constants.CommonUtils;
import com.socatra.intellitrack.database.entity.AddGRNDetailsSubmitTable;
import com.socatra.intellitrack.database.entity.GetModeOfTransportDataFromServerTable;
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
import dagger.android.DispatchingAndroidInjector;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddGRNbyFarmer extends BaseActivity implements View.OnClickListener {


    public static final int REQUEST_CAM_PERMISSIONS = 1;//cam
    private static final String TAG = AddGRNbyFarmer.class.getCanonicalName();
    private static final int FILE_SELECT_CODE = 1;
    private static final int CAMERA_REQUEST = 1888;//cam
    static File f = null;//cam
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    public AppViewModel viewModel;
    AppCompatSpinner spSelectFarmer, spSelectModeOfTransport, spSelectTypeData, spSelectSubDealer;
    CardView cdDate;
    ImageButton imageDate;
    TextView txtSave;
    TextView selectButtonGrnPro;
    ImageView imgGrnProEv;
    Dialog imagePreviewDialog, dialog;
    ImageView imgCancel;
    List<String> modeOfTransportList = new ArrayList<>();
    List<String> modeOfTransportListIds = new ArrayList<>();
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    File selectedFile;
    String selectedFilePath = "";
    Integer testPictureCount = 0;
    String strFileExtension1 = null;//cam
    int pickHandle1 = 0;
    int imageTaken = 0;
    Bitmap bitmapDocument = null;//cam
    TextInputEditText txtEtQuantity, txtEtDRC, txtFarmer, txtDealerName, txtSelectDate;
    String strFarmerName, strFarmerCode, strDealerID, strDealerName, strProcessorId, strSelectTransportID, strSelectTransport, strSelectSubDealerID, strSelectSubDealerName, strSelectTransportIDs, strSelectDate, strGRNValue, strQuantity, strSelectFarmerId;
    String strLanguageId;
    TextView txtWordTitle, txtWordUser, txtWordFarmer,
            txtWordQuantity, txtWordModeOFTransport, txtWordGrnDate, txtWordGRNEvidence;
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
        setContentView(R.layout.activity_add_grnby_farmer);
        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");
        strProcessorId = appHelper.getSharedPrefObj().getString(DeviceProcessorId, "");

        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");

        if (strDealerID.isEmpty()) {
            strDealerName = appHelper.getSharedPrefObj().getString(DeviceUserName, "");
        } else {

            strDealerName = appHelper.getSharedPrefObj().getString(DeviceUserName, "");
        }
        strFarmerName = getIntent().getStringExtra("FarmerName");
        strFarmerCode = getIntent().getStringExtra("FarmerCode");


        initializeUI();
        initializeValues();
        configureDagger();
        configureViewModel();

    }

    private void initializeUI() {
        txtDealerName = findViewById(R.id.et_dealer_name);
        txtFarmer = findViewById(R.id.et_farmer_name);
        txtEtQuantity = findViewById(R.id.et_quantity);
        spSelectModeOfTransport = findViewById(R.id.sp_mode_of_transport1);
        txtSelectDate = findViewById(R.id.et_grn_date1);
        cdDate = findViewById(R.id.cd_date1);
        imageDate = findViewById(R.id.btn_open_date_picker1);
        txtSave = findViewById(R.id.txt_save_grn_farmer);
        imgCancel = findViewById(R.id.img_cancel1);


//        selectButtonGrnPro = findViewById(R.id.selectButtonGrn1);

        imgGrnProEv = findViewById(R.id.imgGrnEv1);


        txtWordTitle = findViewById(R.id.txt_word_grn_header);
        txtWordUser = findViewById(R.id.txt_word_user);
        txtWordFarmer = findViewById(R.id.txt_word_farmer);
        txtWordQuantity = findViewById(R.id.txt_word_quantity);
        txtWordModeOFTransport = findViewById(R.id.txt_word_mode_of_transport);
        txtWordGrnDate = findViewById(R.id.txt_word_grn_date);
        txtWordGRNEvidence = findViewById(R.id.txt_word_evidence_document);


    }

    private void initializeValues() {
        txtDealerName.setText(strDealerName);

        txtFarmer.setText(strFarmerName);

        imgCancel.setOnClickListener(v -> {
            finish();
        });
        txtSave.setOnClickListener(this);
        cdDate.setOnClickListener(this);
        imageDate.setOnClickListener(this);
        txtSelectDate.setOnClickListener(this);

        imgGrnProEv.setOnClickListener(view -> {
            if (imageTaken == 0) {
                pickImageDialog(0);
            } else if (imageTaken == 1) {
//                previewImageDialog(selectedFilePath);
//
            }
        });


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
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddGRNbyFarmer.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date.set(year, monthOfYear, dayOfMonth);
                        new TimePickerDialog(AddGRNbyFarmer.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                date.set(Calendar.MINUTE, minute);
                                Log.v(TAG, "The choosen one " + date.getTime());
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS, Locale.US);
                                strSelectDate = simpleDateFormat.format(date.getTime());

                                SimpleDateFormat showDate = new SimpleDateFormat(DATE_FORMAT_DD_MM_YYYY2, Locale.US);
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

    @Override
    protected void onResume() {
        super.onResume();

        if (appHelper.isNetworkAvailable()) {
          //  getLanguageDataList();
        } else {
            Toast.makeText(AddGRNbyFarmer.this, "please check your internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void getLanguageDataList() {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        final ProgressDialog progressDialog = new ProgressDialog(AddGRNbyFarmer.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading data...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        callRetrofit = service.getTransalteLanguageWordsByLanguageIdFromServer(appHelper.getSharedPrefObj().getString(LanguageId, ""), appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));

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
                                        txtWordTitle.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.user).equals(jsonEngWord)) {
                                        txtWordUser.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.farmer).equals(jsonEngWord)) {
                                        txtWordFarmer.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.quantity).equals(jsonEngWord)) {
                                        txtWordQuantity.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.mode_of_transport).equals(jsonEngWord)) {
                                        txtWordModeOFTransport.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.grn_date).equals(jsonEngWord)) {
                                        txtWordGrnDate.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.grn_evidence).equals(jsonEngWord)) {
                                        txtWordGRNEvidence.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.save).equals(jsonEngWord)) {
                                        txtSave.setText(jsonEngWord1);
                                    }
                                } else {


                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
                                    if (getResources().getString(R.string.grn_details).equals(jsonEngWord)) {
                                        txtWordTitle.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.user).equals(jsonEngWord)) {
                                        txtWordUser.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.farmer).equals(jsonEngWord)) {
                                        txtWordFarmer.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.quantity).equals(jsonEngWord)) {
                                        txtWordQuantity.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.mode_of_transport).equals(jsonEngWord)) {
                                        txtWordModeOFTransport.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.grn_date).equals(jsonEngWord)) {
                                        txtWordGrnDate.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.grn_evidence).equals(jsonEngWord)) {
                                        txtWordGRNEvidence.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.save).equals(jsonEngWord)) {
                                        txtSave.setText(strConvertedWord);
                                    }


                                }

//                                else if (strLanguageId.equals("3")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//
//                                    if (getResources().getString(R.string.grn_details).equals(jsonEngWord)) {
//                                        txtWordTitle.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.user).equals(jsonEngWord)) {
//                                        txtWordUser.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.farmer).equals(jsonEngWord)) {
//                                        txtWordFarmer.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.quantity).equals(jsonEngWord)) {
//                                        txtWordQuantity.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.mode_of_transport).equals(jsonEngWord)) {
//                                        txtWordModeOFTransport.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.grn_date).equals(jsonEngWord)) {
//                                        txtWordGrnDate.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.grn_evidence).equals(jsonEngWord)) {
//                                        txtWordGRNEvidence.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.save).equals(jsonEngWord)) {
//                                        txtSave.setText(strConvertedWord);
//                                    }
//
//
//                                } else if (strLanguageId.equals("4")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//
//
//                                    if (getResources().getString(R.string.grn_details).equals(jsonEngWord)) {
//                                        txtWordTitle.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.user).equals(jsonEngWord)) {
//                                        txtWordUser.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.farmer).equals(jsonEngWord)) {
//                                        txtWordFarmer.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.quantity).equals(jsonEngWord)) {
//                                        txtWordQuantity.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.mode_of_transport).equals(jsonEngWord)) {
//                                        txtWordModeOFTransport.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.grn_date).equals(jsonEngWord)) {
//                                        txtWordGrnDate.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.grn_evidence).equals(jsonEngWord)) {
//                                        txtWordGRNEvidence.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.save).equals(jsonEngWord)) {
//                                        txtSave.setText(strConvertedWord);
//                                    }
//
//
//                                } else if (strLanguageId.equals("5")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//
//
//                                    if (getResources().getString(R.string.grn_details).equals(jsonEngWord)) {
//                                        txtWordTitle.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.user).equals(jsonEngWord)) {
//                                        txtWordUser.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.farmer).equals(jsonEngWord)) {
//                                        txtWordFarmer.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.quantity).equals(jsonEngWord)) {
//                                        txtWordQuantity.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.mode_of_transport).equals(jsonEngWord)) {
//                                        txtWordModeOFTransport.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.grn_date).equals(jsonEngWord)) {
//                                        txtWordGrnDate.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.grn_evidence).equals(jsonEngWord)) {
//                                        txtWordGRNEvidence.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.save).equals(jsonEngWord)) {
//                                        txtSave.setText(strConvertedWord);
//                                    }
//
//
//                                } else {
//                                    String jsonEngWord1 = jsonObjectFarmerPD.getString("SelectedWord");
//
//
//                                    if (getResources().getString(R.string.grn_details).equals(jsonEngWord)) {
//                                        txtWordTitle.setText(jsonEngWord1);
//                                    } else if (getResources().getString(R.string.user).equals(jsonEngWord)) {
//                                        txtWordUser.setText(jsonEngWord1);
//                                    } else if (getResources().getString(R.string.farmer).equals(jsonEngWord)) {
//                                        txtWordFarmer.setText(jsonEngWord1);
//                                    } else if (getResources().getString(R.string.quantity).equals(jsonEngWord)) {
//                                        txtWordQuantity.setText(jsonEngWord1);
//                                    } else if (getResources().getString(R.string.mode_of_transport).equals(jsonEngWord)) {
//                                        txtWordModeOFTransport.setText(jsonEngWord1);
//                                    } else if (getResources().getString(R.string.grn_date).equals(jsonEngWord)) {
//                                        txtWordGrnDate.setText(jsonEngWord1);
//                                    } else if (getResources().getString(R.string.grn_evidence).equals(jsonEngWord)) {
//                                        txtWordGRNEvidence.setText(jsonEngWord1);
//                                    } else if (getResources().getString(R.string.save).equals(jsonEngWord)) {
//                                        txtSave.setText(jsonEngWord1);
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

                        Toast.makeText(AddGRNbyFarmer.this, "no records found", Toast.LENGTH_LONG).show();
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

    private void pickImageDialog(int i) {
        dialog = new Dialog(AddGRNbyFarmer.this, R.style.MyAlertDialogThemeNew);
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

    public void configureViewModel() {
        //  viewModel = ViewModelProviders.of(this, viewModelFactory).get(AppViewModel.class);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);

        if (appHelper.isNetworkAvailable()) {
            getModeofTransportDataFromServer();
        } else {
            Toast.makeText(AddGRNbyFarmer.this, "please check your  internet connection", Toast.LENGTH_SHORT).show();
        }


    }

    public void getModeofTransportDataFromServer() {
        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        // callRetrofit = service.getGetLogisticsDetailsDataByIDFromServer(strDealerID, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));

        callRetrofit = service.getGetLogisticsDetailsDataFromServer(appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));

        final ProgressDialog progressDialog = new ProgressDialog(AddGRNbyFarmer.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading logistics data...");
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
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddGRNbyFarmer.this,
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
                        Toast.makeText(AddGRNbyFarmer.this, "no records found", Toast.LENGTH_LONG).show();
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
        finish();
    }

    @Override
    public void onClick(View v) {

        try {
            switch (v.getId()) {
                case R.id.img_cancel1:
                    onBackPressed();
                    break;
                case R.id.cd_date1:
                case R.id.et_grn_date1:
                case R.id.btn_open_date_picker1:
                    final Calendar currentDate = Calendar.getInstance();
                    Calendar date = Calendar.getInstance();
                    DatePickerDialog datePickerDialog = new DatePickerDialog(AddGRNbyFarmer.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            date.set(year, monthOfYear, dayOfMonth);
                            new TimePickerDialog(AddGRNbyFarmer.this, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    date.set(Calendar.MINUTE, minute);
                                    Log.v(TAG, "The choosen one " + date.getTime());
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS, Locale.US);
                                    strSelectDate = simpleDateFormat.format(date.getTime());

                                    SimpleDateFormat showDate = new SimpleDateFormat(DATE_FORMAT_DD_MM_YYYY2, Locale.US);
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

                case R.id.txt_save_grn_farmer:
                    if (TextUtils.isEmpty(txtEtQuantity.getText().toString().trim())) {
                        txtEtQuantity.setError("Enter Quantity");
                    } else if (strSelectTransport.isEmpty()) {
                        Toast.makeText(AddGRNbyFarmer.this, "please select mode of trans port", Toast.LENGTH_LONG).show();
                    } else if (Objects.equals(selectedFilePath, "")) {
                        Toast.makeText(AddGRNbyFarmer.this, "please add image", Toast.LENGTH_LONG).show();
                    } else if (appHelper.isNetworkAvailable()) {
                        // String strFarmerCode = etFarmerCode.getText().toString().trim();
                        strQuantity = txtEtQuantity.getText().toString().trim();
                        AddGRNDetailsSubmitTable addGRNDetailsSubmitTable = new AddGRNDetailsSubmitTable();
//                        addGRNDetailsSubmitTable.setprocessorId("");
//                        addGRNDetailsSubmitTable.setDealerId(strDealerID);

                        if (strDealerID.equals("")) {
                            addGRNDetailsSubmitTable.setFromProcessorId(strProcessorId);
                            addGRNDetailsSubmitTable.setToDealerId("");
                            addGRNDetailsSubmitTable.setFromDealerId("");

                            Log.d("Processor Id", "Id" + strProcessorId);
                            addGRNDetailsSubmitTable.setFromDealerId("");
                        } else {
                            addGRNDetailsSubmitTable.setFromProcessorId("");
                            addGRNDetailsSubmitTable.setFromDealerId(strDealerID);
                            addGRNDetailsSubmitTable.setToDealerId("");
                            Log.d("Processor Id", "Id" + strDealerID);

                        }

                        addGRNDetailsSubmitTable.setQuantity(strQuantity);
                        addGRNDetailsSubmitTable.setFarmerCode(strFarmerCode);
                        addGRNDetailsSubmitTable.setGRNdate(strSelectDate);
                        addGRNDetailsSubmitTable.setModeofTransport(strSelectTransportIDs);
                        String dateTime = appHelper.getCurrentDateTime(DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS);
                        Log.d(TAG, "onClick: date" + dateTime);

                        addGRNDetailsSubmitTable.setIsActive(("1"));
                        addGRNDetailsSubmitTable.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                        addGRNDetailsSubmitTable.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                        addGRNDetailsSubmitTable.setCreatedDate(dateTime);
                        addGRNDetailsSubmitTable.setUpdatedDate(dateTime);
                        uploadImagesTOServerAPI(selectedFile, addGRNDetailsSubmitTable);


                    } else {

                        Toast.makeText(AddGRNbyFarmer.this, "please check your  internet connection", Toast.LENGTH_SHORT).show();

                    }
                    break;


            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

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
                        Toast.makeText(AddGRNbyFarmer.this, strMessage, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AddGRNbyFarmer.this, FarmerGrnDetailsList.class);

                        String farmerName = getIntent().getStringExtra("FarmerName");

                        String farmerCode = getIntent().getStringExtra("FarmerCode");

                        intent.putExtra("FarmerName", farmerName);

                        intent.putExtra("FarmerCode", farmerCode);

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

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, FILE_SELECT_CODE);
    }

    //For file path
    private void openFilePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (!CommonUtils.isPermissionAllowed(AddGRNbyFarmer.this, Manifest.permission.CAMERA))) {
            android.util.Log.v(TAG, "Location Permissions Not Granted");
            ActivityCompat.requestPermissions(
                    AddGRNbyFarmer.this,
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
                Log.e(TAG, "Selected File Path: " + selectedFilePath); // Log the selected file path


                try {
                    selectedFile = appHelper.convertUriToFile(AddGRNbyFarmer.this, uri);
                    imageTaken = 1;

                    Log.e(TAG, "selPath2:" + selectedFilePath);
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
                imgGrnProEv.setRotation(90.f);
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
                                    imgGrnProEv.setRotation(90.f);
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

    private void uploadImagesTOServerAPI(File uploadFile, AddGRNDetailsSubmitTable addGRNDetailsSubmitTable) {

        Log.d(TAG, "uploadDataBaseTOServerAPI: " + uploadFile.getAbsolutePath());
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
                    Log.d(TAG, "onResponse: AppData " + response);
                    JSONObject json_object = new JSONObject(strResponse);
                    String message = "";
                    int status = 0;
                    Log.e(TAG, "onResponse: data json" + json_object);
                    message = json_object.getString("message");
                    status = json_object.getInt("status");
                    String location = json_object.getString("location");
                    Log.e(TAG, "status of location in new method" + location);
                    //Toast.makeText(AddGRNbyFarmer.this, location, Toast.LENGTH_SHORT).show();
                    if (status == 1) {
                        addGRNDetailsSubmitTable.setGRNDocument(location);

                        Log.e(TAG, json_object.getString("location"));

                        //to server
                        insertGrnDataToServer(addGRNDetailsSubmitTable, "dealer");

                        Toast.makeText(AddGRNbyFarmer.this, message, Toast.LENGTH_SHORT).show();
                    } else if (status == 0) {
                        Log.e(TAG, "status of location in new method error message");
                        Toast.makeText(AddGRNbyFarmer.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "status of location in new method error message");
                        Toast.makeText(AddGRNbyFarmer.this, message, Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                    Log.e(TAG, "status of location in new method error");
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

    private void openCameraPermission(Boolean land, Integer pictureCount) {

        testPictureCount = pictureCount;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (!CommonUtils.isPermissionAllowed(AddGRNbyFarmer.this, Manifest.permission.CAMERA))) {
            android.util.Log.v(TAG, "Location Permissions Not Granted");
            ActivityCompat.requestPermissions(
                    AddGRNbyFarmer.this,
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


                        Uri photoURI = FileProvider.getUriForFile(AddGRNbyFarmer.this,
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
            mediaStorageDir = new File(AddGRNbyFarmer.this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "Chain_Documents");
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


}