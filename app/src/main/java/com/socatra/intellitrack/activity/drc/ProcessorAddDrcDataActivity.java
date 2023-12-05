package com.socatra.intellitrack.activity.drc;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS;
import static com.socatra.intellitrack.constants.AppConstant.DeviceProcessorId;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserID;
import static com.socatra.intellitrack.constants.AppConstant.LanguageId;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;
import com.socatra.intellitrack.BaseActivity;
import com.socatra.intellitrack.R;
import com.socatra.intellitrack.adapters.DrcGrnAdapter;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.constants.AppConstant;
import com.socatra.intellitrack.constants.CommonUtils;
import com.socatra.intellitrack.models.get.GetDrcGrnByProcessorId;
import com.socatra.intellitrack.models.get.GetGrnDrcData;
import com.socatra.intellitrack.models.post.PostDRCDtlDetailsDTO;
import com.socatra.intellitrack.models.post.PostDRCHdrDetailsDTO;
import com.socatra.intellitrack.view_models.AppViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

public class ProcessorAddDrcDataActivity extends BaseActivity implements View.OnClickListener, DrcGrnAdapter.SyncCallbackProcessorInterface {

    public static final int REQUEST_CAM_PERMISSIONS = 1;//cam
    private static final int FILE_SELECT_CODE = 1;
    private static final int CAMERA_REQUEST = 1888;//cam
    static File f = null;//cam
    public String TAG = ProcessorAddDrcDataActivity.class.getCanonicalName();
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    public AppViewModel viewModel;
    String mTag = "DrcTag";
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    TextView txtAddDrc, txtDrcNo, txtDRCDetals, txtDRCEvidence, txtListOfDrc, txtChildGrnNo, txtChildQuantity, txtChildDrc;
    TextView addDrcBtn, fileDisplay, selectButton, saveDrc;
    TextInputEditText etDrcNo;
    String strDrcNo, strProcessorID, mToken;
    Dialog imagePreviewDialog, dialog;
    int grnId = 0;
    String strDrcValue;
    GetDrcGrnByProcessorId dataDropDown;
    GetDrcGrnByProcessorId getDrcGrnByProcessorIdObj;

    //ArrayList<String> strGrn


    List<GetDrcGrnByProcessorId> getDrcGrnByProcessorIdList;
    List<GetDrcGrnByProcessorId> filterdProcessorList;
    DrcGrnAdapter drcGrnAdapter;
    RecyclerView recyclerView;
    PostDRCHdrDetailsDTO postDRCHdrDetailsDTOS;
    String dateTime, strLanguageId;
    String hdrId = "0";
    ImageView imgBack, imgDrcEv;
    File selectedFile;
    String selectedFilePath = "";
    Integer testPictureCount = 0;
    String strFileExtension1 = null;//cam
    int pickHandle1 = 0;
    int imageTaken = 0;
    Bitmap bitmapDocument = null;//cam
    String strGRNDataExit;
    List<GetDrcGrnByProcessorId> getDrcGrnByProcessorIdListData;
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
        setContentView(R.layout.activity_drc_manufacture);

        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");


        initializeUI();
        initializeValues();
        configureDagger();
        configureViewModel();


    }

    private void initializeUI() {

        etDrcNo = findViewById(R.id.et_drc_no);
        addDrcBtn = findViewById(R.id.add_drc_btn);
        selectButton = findViewById(R.id.selectButton);
//        fileDisplay = findViewById(R.id.fileDisplay);
        recyclerView = findViewById(R.id.recycleDrc);
        saveDrc = findViewById(R.id.txtSaveDrc);
        imgBack = findViewById(R.id.img_cancel_Drc);
        imgDrcEv = findViewById(R.id.imgDrcEv);


        //Language
        txtDRCDetals = findViewById(R.id.txt_Drc_details);
        txtDrcNo = findViewById(R.id.txt_drc_no);
        txtDRCEvidence = findViewById(R.id.txt_evidence_document);
        txtListOfDrc = findViewById(R.id.txt_list_of_Drc);
        addDrcBtn = findViewById(R.id.add_drc_btn);
        txtChildGrnNo = findViewById(R.id.txt_child_grn_no);
        txtChildQuantity = findViewById(R.id.txt_child_quantity);
        txtChildDrc = findViewById(R.id.txt_child_drc);

    }

    private void initializeValues() {
        //Id
        strProcessorID = appHelper.getSharedPrefObj().getString(DeviceProcessorId, "");
        //Token
        mToken = appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, "");

        //date
        dateTime = appHelper.getCurrentDateTime(DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS);


        //   getLanguageDataList();


        //Drc no
        long millis = 0;
        try {
            String myDate = appHelper.getCurrentDateTime(AppConstant.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
            android.icu.text.SimpleDateFormat sdf = new android.icu.text.SimpleDateFormat(AppConstant.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
            Date date = sdf.parse(myDate);
            millis = date.getTime();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        strDrcNo = "DRC" + millis;
        etDrcNo.setText(strDrcNo);
        imgBack.setOnClickListener(this);
        addDrcBtn.setOnClickListener(this);
        saveDrc.setOnClickListener(this);
        imgDrcEv.setOnClickListener(this);

        //Back btn
//        imgBack.setOnClickListener(view -> {
//            finish();
//        });


        //  add btn
        if (appHelper.isNetworkAvailable()) {
            final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);

            Call<GetGrnDrcData> call = service.getDrcGRNDataByProcessorIdFromServer(strProcessorID, mToken);
            call.enqueue(new Callback<GetGrnDrcData>() {
                @Override
                public void onResponse(Call<GetGrnDrcData> call, Response<GetGrnDrcData> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        getDrcGrnByProcessorIdListData = response.body().getData();
                        Log.d(TAG, "onResponse: grndata" + getDrcGrnByProcessorIdListData);
                        //grn.setGRNnumber("--select grn--");
                        if (getDrcGrnByProcessorIdListData.size() > 0) {
                            strGRNDataExit = "1";
                        }

                    } else {
                        strGRNDataExit = "0";
                    }
//                    else {
//                        Toast.makeText(DrcManufactureActivity.this, "no data!!", Toast.LENGTH_SHORT).show();
//                    }
                }

                @Override
                public void onFailure(Call<GetGrnDrcData> call, Throwable t) {
                    strGRNDataExit = "0";
                }
            });
        } else {
            Toast.makeText(this, "Please check internet!!", Toast.LENGTH_SHORT).show();
        }
        addDrcBtn.setOnClickListener(view -> {

            addDrcDialog();
        });

        //Save btn
//        saveDrc.setOnClickListener(v -> {
//            if (!Objects.equals(selectedFilePath, "")) {
//                if (getDrcGrnByProcessorIdList.size() > 0) {
//
//                    postDRCHdrDetailsDTOS = new PostDRCHdrDetailsDTO();
//                    postDRCHdrDetailsDTOS.setDRCNo(strDrcNo);
//                    postDRCHdrDetailsDTOS.setDRCDocument("");
//
//                    postDRCHdrDetailsDTOS.setIsActive("1");
//                    postDRCHdrDetailsDTOS.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, "1"));
//                    postDRCHdrDetailsDTOS.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, "1"));
//                    postDRCHdrDetailsDTOS.setCreatedDate(dateTime);
//                    postDRCHdrDetailsDTOS.setUpdatedDate(dateTime);
//
//                    uploadImagesTOServerAPI(selectedFile, postDRCHdrDetailsDTOS);
//
//                } else {
//                    Toast.makeText(this, "Please select GRN!!", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Toast.makeText(this, "Add Image!!", Toast.LENGTH_SHORT).show();
//            }
//
//        });
//
//
//        imgDrcEv.setOnClickListener(view -> {
//
//            if (imageTaken == 0) {
//                pickImageDialog(0);
//            } else if (imageTaken == 1) {
////                previewImageDialog(selectedFilePath);
////
//            }
//
//        });
//


        if (appHelper.isNetworkAvailable()) {
            callSpinnerData();
        } else {
            Toast.makeText(ProcessorAddDrcDataActivity.this, "please check internet", Toast.LENGTH_SHORT).show();
        }

        //array init
        getDrcGrnByProcessorIdList = new ArrayList<>();

        //RecyclerView & Adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();
        drcGrnAdapter = new DrcGrnAdapter(appHelper, viewModel, getDrcGrnByProcessorIdList, ProcessorAddDrcDataActivity.this);
        recyclerView.setAdapter(drcGrnAdapter);
    }

    protected void onResume() {
        super.onResume();
        if (appHelper.isNetworkAvailable()) {
           // getLanguageDataList();
        } else {
            Toast.makeText(getApplicationContext(), "please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void callSpinnerData() {
        if (appHelper.isNetworkAvailable()) {
            final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
            Call<GetGrnDrcData> call = service.getDrcGRNDataForDrcByProcessorIdFromServer(strProcessorID, mToken);
            call.enqueue(new Callback<GetGrnDrcData>() {
                @Override
                public void onResponse(Call<GetGrnDrcData> call, Response<GetGrnDrcData> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<GetDrcGrnByProcessorId> getDrcGrnByProcessorId = response.body().getData();
                        Log.d(TAG, "onResponse: ");
                        GetDrcGrnByProcessorId grn = new GetDrcGrnByProcessorId();
                        grn.setGRNnumber("--select grn--");
                        getDrcGrnByProcessorId.add(0, grn);
                        filterdProcessorList = getDrcGrnByProcessorId;
//                        ArrayAdapter<GetDrcGrnByProcessorId> dataAdapterCont = new ArrayAdapter<>(DrcManufactureActivity.this,
//                                android.R.layout.simple_spinner_item, getDrcGrnByProcessorId);
//                        dataAdapterCont.setDropDownViewResource(android.R.layout.simple_spinner_item);
//                        dataAdapterCont.notifyDataSetChanged();
//                        spGrnNo.setAdapter(dataAdapterCont);

                        imgDrcEv.setClickable(true);
                        addDrcBtn.setEnabled(true);
                    } else {
                        Toast.makeText(ProcessorAddDrcDataActivity.this, "no GRN records found please add grn", Toast.LENGTH_LONG).show();
                        addDrcBtn.setClickable(false);
                        addDrcBtn.setEnabled(false);
                    }
                }


                @Override
                public void onFailure(Call<GetGrnDrcData> call, Throwable t) {
                    Toast.makeText(ProcessorAddDrcDataActivity.this, "no GRN records found please add grn", Toast.LENGTH_LONG).show();
                    addDrcBtn.setClickable(false);
                    addDrcBtn.setEnabled(false);

                }
            });
        } else {
            Toast.makeText(this, "Please check internet!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendDrcHdrToServer(PostDRCHdrDetailsDTO postDRCHdrDetailsDTO) {
        Log.e(mTag, "hdrArr:" + postDRCHdrDetailsDTO.toString());
        try {
            viewModel.syncDRClHdrListDataToServer(postDRCHdrDetailsDTO);
            if (viewModel.getStringLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        viewModel.getStringLiveData().removeObserver(this);
                        String strMessage = (String) o;
                        hdrId = strMessage;
                        Log.e(mTag, "hdId:" + hdrId.toString());
                        makeDrcDltArray(hdrId);
                    }
                };
                viewModel.getStringLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void makeDrcDltArray(String hdrId) {
        Log.e(mTag, "AdapterList:" + getDrcGrnByProcessorIdList.toString());
        //Todo : 2nd table creation
        List<PostDRCDtlDetailsDTO> postDRCDtlDetailsDTOList = new ArrayList<>();
        if (getDrcGrnByProcessorIdList.size() > 0) {
            for (int i = 0; i < getDrcGrnByProcessorIdList.size(); i++) {
                PostDRCDtlDetailsDTO data = new PostDRCDtlDetailsDTO();
                data.setDRCHdrId(Integer.parseInt(hdrId));
                data.setGRNId(getDrcGrnByProcessorIdList.get(i).getId());
                data.setDRCValue(getDrcGrnByProcessorIdList.get(i).getDRCValue());
                data.setIsActive("1");
                data.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, "1"));
                data.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, "1"));
                data.setCreatedDate(dateTime);
                data.setUpdatedDate(dateTime);
                postDRCDtlDetailsDTOList.add(data);
            }
        }
        if (appHelper.isNetworkAvailable()) {
            sendDrcDltToServer(postDRCDtlDetailsDTOList);
        } else {
            Toast.makeText(ProcessorAddDrcDataActivity.this, "please check  your internet connection", Toast.LENGTH_SHORT).show();

        }
    }

    private void sendDrcDltToServer(List<PostDRCDtlDetailsDTO> postDRCDtlDetailsDTOList) {
        Log.e(mTag, "PostAdapterList:" + postDRCDtlDetailsDTOList.toString());

        for (int i = 0; i < postDRCDtlDetailsDTOList.size(); i++) {
            //Post
            try {
                viewModel.syncDRCDtlListDataT0Server(postDRCDtlDetailsDTOList.get(i));
                if (viewModel.getStringLiveData() != null) {
                    Observer getLeadRawDataObserver = new Observer() {
                        @Override
                        public void onChanged(@Nullable Object o) {
                            viewModel.getStringLiveData().removeObserver(this);
                            String strMessage = (String) o;
                            Toast.makeText(ProcessorAddDrcDataActivity.this, strMessage, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ProcessorAddDrcDataActivity.this, Drclist.class);
                            startActivity(intent);
                            finish();
                        }
                    };
                    viewModel.getStringLiveData().observe(this, getLeadRawDataObserver);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }


    }


    //For file path

    private void pickImageDialog(int i) {

        dialog = new Dialog(ProcessorAddDrcDataActivity.this, R.style.MyAlertDialogThemeNew);
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

    //For file path
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, FILE_SELECT_CODE);
    }

    private void configureDagger() {
        AndroidInjection.inject(ProcessorAddDrcDataActivity.this);
    }

    public void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);


    }

    private void addDrcDialog() {
        Dialog dialog = new Dialog(this, R.style.MyAlertDialogThemeNew);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.drc_dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.show();

        Spinner spGrnNo = dialog.findViewById(R.id.sp_grn_no);
        TextInputEditText etQuantity = dialog.findViewById(R.id.et_actual_quantity_drc);
        TextInputEditText etDrcPercent = dialog.findViewById(R.id.et_drc1);
        TextView txtSaveDialog = dialog.findViewById(R.id.txtSaveCdial);
        TextView txtBackDialog = dialog.findViewById(R.id.txtBackCdial);

        //language

        TextView txtGRNDetails = dialog.findViewById(R.id.txt_grn_details);
        TextView txtGRNNo = dialog.findViewById(R.id.txt_grn_no);
        TextView txtQuantity = dialog.findViewById(R.id.txt_actual_quantity);
        TextView txtDRC = dialog.findViewById(R.id.txt_drc);


        languagAPI(txtGRNDetails, txtGRNNo, txtQuantity, txtDRC, txtSaveDialog, txtBackDialog);


//        etDrcPercent.addTextChangedListener(new TextWatcher(){
//            public void afterTextChanged(Editable s) {}
//
//            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
//
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                String strEnteredVal = etDrcPercent.getText().toString();
//
//                if (!strEnteredVal.equals("")) {
//                    int num = Integer.parseInt(strEnteredVal);
//                    if (num <= 100) {
//                        etDrcPercent.setText("" + num);
//                    } else {
//                        etDrcPercent.setText("");
//                    }
//                }
//            }});

        //back dialog
        txtBackDialog.setOnClickListener(v -> {
            dialog.dismiss();
        });


        if (getDrcGrnByProcessorIdList.size() > 0) {
            filterdProcessorList.removeAll(getDrcGrnByProcessorIdList);
            ArrayAdapter<GetDrcGrnByProcessorId> dataAdapterCont = new ArrayAdapter<>(ProcessorAddDrcDataActivity.this, android.R.layout.simple_spinner_item, filterdProcessorList);
            dataAdapterCont.setDropDownViewResource(android.R.layout.simple_spinner_item);
            dataAdapterCont.notifyDataSetChanged();
            spGrnNo.setAdapter(dataAdapterCont);
        } else {
            ArrayAdapter<GetDrcGrnByProcessorId> dataAdapterCont = new ArrayAdapter<>(ProcessorAddDrcDataActivity.this, android.R.layout.simple_spinner_item, filterdProcessorList);
            dataAdapterCont.setDropDownViewResource(android.R.layout.simple_spinner_item);
            dataAdapterCont.notifyDataSetChanged();
            spGrnNo.setAdapter(dataAdapterCont);
            //  spProceesSelect.setAdapter(getGrnByProcessorIdArrayAdapter);
        }
        //save dialog
        txtSaveDialog.setOnClickListener(v -> {
            if (grnId == 0) {
                Toast.makeText(ProcessorAddDrcDataActivity.this, "Please select a GRN!!", Toast.LENGTH_SHORT).show();
            } else if (!(Integer.parseInt(etDrcPercent.getText().toString()) <= 100)) {
                Toast.makeText(ProcessorAddDrcDataActivity.this, "Please enter drc % upto 100 only", Toast.LENGTH_SHORT).show();
            } else {
                //   dataDropDown.setDRCValue(Integer.parseInt(etDrcPercent.getText().toString()));
                getDrcGrnByProcessorIdObj.setDRCValue(Integer.parseInt(etDrcPercent.getText().toString()));


                if (getDrcGrnByProcessorIdObj != null) {
                    getDrcGrnByProcessorIdList.add(getDrcGrnByProcessorIdObj);
                    Log.e(mTag, "dialogsave" + getDrcGrnByProcessorIdList.toString());
                    drcGrnAdapter.notifyDataSetChanged();
                    Toast.makeText(ProcessorAddDrcDataActivity.this, "GRN Added!!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });

//            else if(etDrcPercent.getText().toString().trim().isEmpty() || Integer.parseInt(etDrcPercent.getText().toString()) > 100 )

        spGrnNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GetDrcGrnByProcessorId data_DropDown = (GetDrcGrnByProcessorId) parent.getItemAtPosition(position);
                dataDropDown = (GetDrcGrnByProcessorId) parent.getItemAtPosition(position);
                if (position != 0) {
                    Log.e(mTag, "Id:" + String.valueOf(data_DropDown.getId()));
                    grnId = data_DropDown.getId();
                    etQuantity.setText(String.valueOf(data_DropDown.getQuantity()));
                    strDrcValue = String.valueOf(data_DropDown.getDRCValue());
                    //  data_DropDown.setDRCValue();
                    getDrcGrnByProcessorIdObj = data_DropDown;
                } else {
                    etQuantity.setText("");
                    etDrcPercent.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //   etDrcPercent.setText(String.valueOf(dataDropDown.getDRCValue()));


        //data.setDRCValue(Integer.parseInt(etDrcPercent.getText().toString()));

        //Spinner
//        if (appHelper.isNetworkAvailable()) {
//            final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
//
//            Call<GetGrnDrcData> call = service.getDrcGRNDataByProcessorIdFromServer(strProcessorID, mToken);
//            call.enqueue(new Callback<GetGrnDrcData>() {
//                @Override
//                public void onResponse(Call<GetGrnDrcData> call, Response<GetGrnDrcData> response) {
//                    if (response.isSuccessful() && response.body() != null) {
//                        List<GetDrcGrnByProcessorId> getDrcGrnByProcessorId = response.body().getData();
//                        GetDrcGrnByProcessorId grn = new GetDrcGrnByProcessorId();
//                        grn.setGRNnumber("--select grn--");
//                        getDrcGrnByProcessorId.add(0, grn);
//
//                        ArrayAdapter<GetDrcGrnByProcessorId> dataAdapterCont = new ArrayAdapter<>(DrcManufactureActivity.this,
//                                android.R.layout.simple_spinner_item, getDrcGrnByProcessorId);
//                        dataAdapterCont.setDropDownViewResource(android.R.layout.simple_spinner_item);
//                        dataAdapterCont.notifyDataSetChanged();
//                        spGrnNo.setAdapter(dataAdapterCont);
//
//
//
//                    } else {
//                        Toast.makeText(DrcManufactureActivity.this, "no data!!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<GetGrnDrcData> call, Throwable t) {
//
//                }
//            });
//        } else {
//            Toast.makeText(this, "Please check internet!!", Toast.LENGTH_SHORT).show();
//        }

    }


    private void languagAPI(TextView txtGRNDetails, TextView txtGRNNo, TextView txtQuantity, TextView txtDRC, TextView txtSaveDialog, TextView txtBackDialog) {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        final ProgressDialog progressDialog = new ProgressDialog(ProcessorAddDrcDataActivity.this, R.style.AppCompatAlertDialogStyle);
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


                                    if (getResources().getString(R.string.add_grn_details).equals(jsonEngWord)) {
                                        txtGRNDetails.setText(jsonEngWord);
                                    } else if (getResources().getString(R.string.grn_no).equals(jsonEngWord)) {
                                        txtGRNNo.setText(jsonEngWord);
                                    } else if (getResources().getString(R.string.quantity).equals(jsonEngWord)) {
                                        txtQuantity.setText(jsonEngWord);
                                    } else if (getResources().getString(R.string.drcpercentage).equals(jsonEngWord)) {
                                        txtDRC.setText(jsonEngWord);
                                    } else if (getResources().getString(R.string.save).equals(jsonEngWord)) {
                                        txtSaveDialog.setText(jsonEngWord);
                                    } else if (getResources().getString(R.string.cancel).equals(jsonEngWord)) {
                                        txtBackDialog.setText(jsonEngWord);
                                    }


                                } else  {
                                    if (getResources().getString(R.string.add_grn_details).equals(jsonEngWord)) {
                                        txtGRNDetails.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.grn_no).equals(jsonEngWord)) {
                                        txtGRNNo.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.quantity).equals(jsonEngWord)) {
                                        txtQuantity.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.drcpercentage).equals(jsonEngWord)) {
                                        txtDRC.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.save).equals(jsonEngWord)) {
                                        txtSaveDialog.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.cancel).equals(jsonEngWord)) {
                                        txtBackDialog.setText(strConvertedWord);
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

                        Toast.makeText(ProcessorAddDrcDataActivity.this, "no records found", Toast.LENGTH_LONG).show();
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

    private void openFilePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (!CommonUtils.isPermissionAllowed(ProcessorAddDrcDataActivity.this, Manifest.permission.CAMERA))) {
            Log.v(mTag, "Location Permissions Not Granted");
            ActivityCompat.requestPermissions(
                    ProcessorAddDrcDataActivity.this,
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
                    selectedFile = appHelper.convertUriToFile(ProcessorAddDrcDataActivity.this, uri);
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
                        .into(imgDrcEv);
                imgDrcEv.setRotation(90.f);
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
                                    imgDrcEv.setRotation(90.f);
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

    private void uploadImagesTOServerAPI(File uploadFile, PostDRCHdrDetailsDTO
            postDRCHdrDetailsDTOS) {

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
                    //      Toast.makeText(DrcManufactureActivity.this, location, Toast.LENGTH_SHORT).show();
                    if (status == 1) {
                        postDRCHdrDetailsDTOS.setDRCDocument(location);

                        Log.e(mTag, json_object.getString("location"));
                        if (appHelper.isNetworkAvailable()) {


                            sendDrcHdrToServer(postDRCHdrDetailsDTOS);
                        } else {
                            Toast.makeText(ProcessorAddDrcDataActivity.this, "please check  your internet connection", Toast.LENGTH_SHORT).show();
                        }

                        // Toast.makeText(DrcManufactureActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else if (status == 0) {
                        Log.e(mTag, "status of location in new method error message");
                        Toast.makeText(ProcessorAddDrcDataActivity.this, "something error please contact admin", Toast.LENGTH_SHORT).show();
                        //     Toast.makeText(DrcManufactureActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(mTag, "status of location in new method error message");
                        //Toast.makeText(DrcManufactureActivity.this, message, Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(ProcessorAddDrcDataActivity.this, "something error please contact admin", Toast.LENGTH_SHORT).show();

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

    private void openCameraPermission(Boolean land, Integer pictureCount) {

        testPictureCount = pictureCount;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (!CommonUtils.isPermissionAllowed(ProcessorAddDrcDataActivity.this, Manifest.permission.CAMERA))) {
            Log.v(mTag, "Location Permissions Not Granted");
            ActivityCompat.requestPermissions(
                    ProcessorAddDrcDataActivity.this,
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


                        Uri photoURI = FileProvider.getUriForFile(ProcessorAddDrcDataActivity.this,
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
            mediaStorageDir = new File(ProcessorAddDrcDataActivity.this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "Chain_Documents");
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

    private void copyFile(InputStream inputStream, OutputStream outputStream) throws
            IOException {
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
            int targetW = imgDrcEv.getWidth();
            int targetH = imgDrcEv.getHeight();

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

            imgDrcEv.setImageBitmap(rotatedBitmap);
            imgDrcEv.invalidate();


        }
    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        bytes = stream.toByteArray();
        return stream.toByteArray();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(ProcessorAddDrcDataActivity.this, Drclist.class);
//        startActivity(intent);
//        finish();
        finish();
    }

    @Override
    public void onClick(View v) {

        try {
            switch (v.getId()) {
                case R.id.add_drc_btn:


                    break;

                case R.id.selectButton:

                    break;

                case R.id.img_cancel_Drc:
//                    Intent intent = new Intent(ProcessorAddDrcDataActivity.this, Drclist.class);
//                    startActivity(intent);
//                    finish();
                    finish();
                    break;

                case R.id.imgDrcEv:
                    if (imageTaken == 0) {
                        pickImageDialog(0);
                    } else if (imageTaken == 1) {
//                previewImageDialog(selectedFilePath);
//
                    }

                    break;

                case R.id.txtSaveDrc:
                    if (!Objects.equals(selectedFilePath, "")) {
                        if (getDrcGrnByProcessorIdList.size() > 0) {

                            postDRCHdrDetailsDTOS = new PostDRCHdrDetailsDTO();
                            postDRCHdrDetailsDTOS.setDRCNo(strDrcNo);
                            postDRCHdrDetailsDTOS.setDRCDocument("");
                            postDRCHdrDetailsDTOS.setProcessorId(strProcessorID);

                            postDRCHdrDetailsDTOS.setIsActive("1");
                            postDRCHdrDetailsDTOS.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, "1"));
                            postDRCHdrDetailsDTOS.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, "1"));
                            postDRCHdrDetailsDTOS.setCreatedDate(dateTime);
                            postDRCHdrDetailsDTOS.setUpdatedDate(dateTime);

                            uploadImagesTOServerAPI(selectedFile, postDRCHdrDetailsDTOS);

                        } else {
                            Toast.makeText(this, "Please select GRN!!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Add Image!!", Toast.LENGTH_SHORT).show();
                    }


                    break;


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeProcessorPosition(int position, GetDrcGrnByProcessorId
            getDrcGrnByProcessorId, List<GetDrcGrnByProcessorId> getGrnByProcessorIdList) {


        if (position != RecyclerView.NO_POSITION) {
            // Handle the delete button click here, for example, by removing the item from the list
            GetDrcGrnByProcessorId removedItem = getGrnByProcessorIdList.remove(position);
            drcGrnAdapter.notifyItemRemoved(position);// Refresh the RecyclerView
            //    int originalPosition = findOriginalPosition(removedItem, getGrnByDealerIdList);
            int originalPosition = findOriginalProcessorPosition(removedItem, filterdProcessorList);
            if (originalPosition >= 0 && originalPosition <= filterdProcessorList.size()) {
                filterdProcessorList.add(originalPosition, removedItem);
            } else {
                // If original position is out of bounds, add it to the end
                filterdProcessorList.add(removedItem);
            }

            // Update the Spinner's drcGrnAdapter to reflect the changes

        }
    }


    private int findOriginalProcessorPosition(GetDrcGrnByProcessorId
                                                      item, List<GetDrcGrnByProcessorId> getGrnByProcessorIdListData) {
        for (int i = 0; i < getGrnByProcessorIdListData.size(); i++) {
            if (item.equals(getGrnByProcessorIdListData.get(i))) {
                return i;
            }
        }
        return -1; // Item not found in the list
    }


    private void getLanguageDataList() {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        final ProgressDialog progressDialog = new ProgressDialog(ProcessorAddDrcDataActivity.this, R.style.AppCompatAlertDialogStyle);
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
                                    if (getResources().getString(R.string.drc_details).equals(jsonEngWord)) {
                                        txtDRCDetals.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.drc).equals(jsonEngWord)) {
                                        txtDrcNo.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.evidence_document).equals(jsonEngWord)) {
                                        txtDRCEvidence.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.list_of_drc).equals(jsonEngWord)) {
                                        txtListOfDrc.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.add_grn_plus).equals(jsonEngWord)) {
                                        addDrcBtn.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.grn_no).equals(jsonEngWord)) {
                                        txtChildGrnNo.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.quantity).equals(jsonEngWord)) {
                                        txtChildQuantity.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.drc).equals(jsonEngWord)) {
                                        txtChildDrc.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.save).equals(jsonEngWord)) {
                                        saveDrc.setText(jsonEngWord1);
                                    }

                                } else {

                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");

                                    if (getResources().getString(R.string.drc_details).equals(jsonEngWord)) {
                                        txtDRCDetals.setText(strConvertedWord);
                                    }else if (getResources().getString(R.string.drc).equals(jsonEngWord)) {
                                        txtDrcNo.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.evidence_document).equals(jsonEngWord)) {
                                        txtDRCEvidence.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.list_of_drc).equals(jsonEngWord)) {
                                        txtListOfDrc.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.add_grn_plus).equals(jsonEngWord)) {
                                        addDrcBtn.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.grn_no).equals(jsonEngWord)) {
                                        txtChildGrnNo.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.quantity).equals(jsonEngWord)) {
                                        txtChildQuantity.setText(strConvertedWord);
                                    } else if (getString(R.string.drc).equals(jsonEngWord)) {
                                        txtChildDrc.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.save).equals(jsonEngWord)) {
                                        saveDrc.setText(strConvertedWord);
                                    }


                                }


//                                else if (strLanguageId.equals("3")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//                                    if ("Drc Details".equals(jsonEngWord)) {
//                                        txtDRCDetals.setText(strConvertedWord);
//                                    } else if ("DRC".equals(jsonEngWord)) {
//                                        txtDrcNo.setText(strConvertedWord);
//                                    } else if ("Evidence Document".equals(jsonEngWord)) {
//                                        txtDRCEvidence.setText(strConvertedWord);
//                                    } else if ("List Of DRC".equals(jsonEngWord)) {
//                                        txtListOfDrc.setText(strConvertedWord);
//                                    } else if ("Add GRN ".equals(jsonEngWord)) {
//                                        addDrcBtn.setText(strConvertedWord);
//                                    } else if ("GRN No".equals(jsonEngWord)) {
//                                        txtChildGrnNo.setText(strConvertedWord);
//                                    } else if ("Quantity".equals(jsonEngWord)) {
//                                        txtChildQuantity.setText(strConvertedWord);
//                                    } else if ("DRC".equals(jsonEngWord)) {
//                                        txtChildDrc.setText(strConvertedWord);
//                                    } else if ("Save".equals(jsonEngWord)) {
//                                        saveDrc.setText(strConvertedWord);
//                                    }
//                                } else if (strLanguageId.equals("4")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//                                    if ("Drc Details".equals(jsonEngWord)) {
//                                        txtDRCDetals.setText(strConvertedWord);
//                                    } else if ("DRC".equals(jsonEngWord)) {
//                                        txtDrcNo.setText(strConvertedWord);
//                                    } else if ("Evidence Document".equals(jsonEngWord)) {
//                                        txtDRCEvidence.setText(strConvertedWord);
//                                    } else if ("List Of DRC".equals(jsonEngWord)) {
//                                        txtListOfDrc.setText(strConvertedWord);
//                                    } else if ("Add GRN ".equals(jsonEngWord)) {
//                                        addDrcBtn.setText(strConvertedWord);
//                                    } else if ("GRN No".equals(jsonEngWord)) {
//                                        txtChildGrnNo.setText(strConvertedWord);
//                                    } else if ("Quantity".equals(jsonEngWord)) {
//                                        txtChildQuantity.setText(strConvertedWord);
//                                    } else if ("DRC".equals(jsonEngWord)) {
//                                        txtChildDrc.setText(strConvertedWord);
//                                    } else if ("Save".equals(jsonEngWord)) {
//                                        saveDrc.setText(strConvertedWord);
//                                    }
//
//
//                                } else if (strLanguageId.equals("5")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//                                    if ("Drc Details".equals(jsonEngWord)) {
//                                        txtDRCDetals.setText(strConvertedWord);
//                                    } else if ("DRC".equals(jsonEngWord)) {
//                                        txtDrcNo.setText(strConvertedWord);
//                                    } else if ("Evidence Document".equals(jsonEngWord)) {
//                                        txtDRCEvidence.setText(strConvertedWord);
//                                    } else if ("List Of DRC".equals(jsonEngWord)) {
//                                        txtListOfDrc.setText(strConvertedWord);
//                                    } else if ("Add GRN ".equals(jsonEngWord)) {
//                                        addDrcBtn.setText(strConvertedWord);
//                                    } else if ("GRN No".equals(jsonEngWord)) {
//                                        txtChildGrnNo.setText(strConvertedWord);
//                                    } else if ("Quantity".equals(jsonEngWord)) {
//                                        txtChildQuantity.setText(strConvertedWord);
//                                    } else if ("DRC".equals(jsonEngWord)) {
//                                        txtChildDrc.setText(strConvertedWord);
//                                    } else if ("Save".equals(jsonEngWord)) {
//                                        saveDrc.setText(strConvertedWord);
//                                    }
//
//
//                                } else {
//                                    String jsonEngWord1 = jsonObjectFarmerPD.getString("SelectedWord");
//                                    if ("Drc Details".equals(jsonEngWord)) {
//                                        txtDRCDetals.setText(jsonEngWord1);
//                                    } else if ("DRC".equals(jsonEngWord)) {
//                                        txtDrcNo.setText(jsonEngWord1);
//                                    } else if ("Evidence Document ".equals(jsonEngWord)) {
//                                        txtDRCEvidence.setText(jsonEngWord1);
//                                    } else if ("List Of DRC".equals(jsonEngWord)) {
//                                        txtListOfDrc.setText(jsonEngWord1);
//                                    } else if ("Add GRN".equals(jsonEngWord)) {
//                                        addDrcBtn.setText(jsonEngWord1);
//                                    } else if ("GRN No".equals(jsonEngWord)) {
//                                        txtChildGrnNo.setText(jsonEngWord1);
//                                    } else if ("Quantity".equals(jsonEngWord)) {
//                                        txtChildQuantity.setText(jsonEngWord1);
//                                    } else if ("DRC".equals(jsonEngWord)) {
//                                        txtChildDrc.setText(jsonEngWord1);
//                                    } else if ("Save".equals(jsonEngWord)) {
//                                        saveDrc.setText(jsonEngWord1);
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

                        Toast.makeText(ProcessorAddDrcDataActivity.this, "no records found", Toast.LENGTH_LONG).show();
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