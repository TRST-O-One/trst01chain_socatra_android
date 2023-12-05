package com.socatra.intellitrack.activity.batchProcessing;


import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.DATE_FORMAT_YYYY_MM_DD;
import static com.socatra.intellitrack.constants.AppConstant.DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS;
import static com.socatra.intellitrack.constants.AppConstant.DeviceDealerID;
import static com.socatra.intellitrack.constants.AppConstant.DeviceProcessorId;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserID;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserName;
import static com.socatra.intellitrack.constants.AppConstant.LanguageId;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonElement;
import com.socatra.intellitrack.BaseActivity;
import com.socatra.intellitrack.R;
import com.socatra.intellitrack.activity.main_dash_board.MainDashBoardActivity;
import com.socatra.intellitrack.adapters.BatchGrnAdapter;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.constants.AppConstant;
import com.socatra.intellitrack.database.entity.AddBatchProcessingDtlDetailsSubmitTable;
import com.socatra.intellitrack.database.entity.AddBatchProcessingHdrDetailsSubmitTable;
import com.socatra.intellitrack.models.get.GetGrnByDealerId;
import com.socatra.intellitrack.models.get.GetGrnData;
import com.socatra.intellitrack.view_models.AppViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DealerBatchCreationAct extends BaseActivity implements BatchGrnAdapter.SyncCallbackInterface {

    private static final String TAG = DealerBatchCreationAct.class.getCanonicalName();
    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    public AppViewModel viewModel;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    TextView addGrnBtn, txtSave;
    TextInputEditText txtBatchNo, txtdate;
    String strDealerName, mToken;
    ImageView imgBack;
    String strBatchNo;
    RecyclerView recycleBatchGrn;
    Dialog dialog;

    BatchGrnAdapter batchGrnRecAdapter;
    int grnId = 0;
    //Header data
    AddBatchProcessingHdrDetailsSubmitTable addBatchProcessingHdrSubmitTable;
    String hdrId;
    GetGrnByDealerId getGrnByDealerIdObj;


    List<GetGrnByDealerId> filteredGrnList;
    List<GetGrnByDealerId> getGrnByDealerIdArrayList;


    String strDealerID;
    String strProcessorID;

    TextView txtWordTitle, txtWordBatchNum, txtWordBatchCreatedDate, txtWordTitleList, txtWordAddGrn, txtWordGrnNum, txtWordActualQuantity, txtSaveBatch;

    String strLanguageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_processing);

        String farmerCode = getIntent().getStringExtra("FarmerCodeExtra");
        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");
        initializeUI();
        configureDagger();
        configureViewModel();
        initializeValues();

    }


    private void initializeUI() {

        addGrnBtn = findViewById(R.id.add_batch_btn);
        txtBatchNo = findViewById(R.id.et_batch_no);
        txtdate = findViewById(R.id.et_date);
        imgBack = findViewById(R.id.img_cancel);
        recycleBatchGrn = findViewById(R.id.recycleBatchgrn);
        txtSave = findViewById(R.id.txtSave_Batch);


        // TODO: 11/27/2023 adding langauges for the text

        txtWordTitle = findViewById(R.id.txt_batch_grn_details);
        txtWordBatchNum = findViewById(R.id.txt_batch_no);
        txtWordBatchCreatedDate = findViewById(R.id.txt_batch_created_date);
        txtWordTitleList = findViewById(R.id.txt_list_of_grn);
        txtWordAddGrn = findViewById(R.id.add_batch_btn);
        txtWordGrnNum = findViewById(R.id.txt_child_grn_no);
        txtWordActualQuantity = findViewById(R.id.txt_child_actual_quantity);
        txtSaveBatch = findViewById(R.id.txtSave_Batch);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(DealerBatchCreationAct.this, MainDashBoardActivity.class); // Replace MainActivity with your actual dashboard activity
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (appHelper.isNetworkAvailable()) {
            //getLanguageDataList();
        } else {
            Toast.makeText(DealerBatchCreationAct.this, "please check your internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void getLanguageDataList() {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        final ProgressDialog progressDialog = new ProgressDialog(DealerBatchCreationAct.this, R.style.AppCompatAlertDialogStyle);
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
                                    if (getResources().getString(R.string.batch_grn_details).equals(jsonEngWord)) {
                                        txtWordTitle.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.batch_no).equals(jsonEngWord)) {
                                        txtWordBatchNum.setText(jsonEngWord1);
                                    } else if  (getResources().getString(R.string.date).equals(jsonEngWord)) {
                                        txtWordBatchCreatedDate.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.grn_list).equals(jsonEngWord)) {
                                        txtWordTitleList.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.add_grn).equals(jsonEngWord)) {
                                        addGrnBtn.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.grn_no).equals(jsonEngWord)) {
                                        txtWordGrnNum.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.actual_quantity).equals(jsonEngWord)) {
                                        txtWordActualQuantity.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.save).equals(jsonEngWord)) {
                                        txtSave.setText(jsonEngWord1);
                                    }
                                } else {

                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
                                    if (getResources().getString(R.string.batch_grn_details).equals(jsonEngWord)) {
                                        txtWordTitle.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.batch_no).equals(jsonEngWord)) {
                                        txtWordBatchNum.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.date).equals(jsonEngWord)) {
                                        txtWordBatchCreatedDate.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.grn_list).equals(jsonEngWord)) {
                                        txtWordTitleList.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.add_grn).equals(jsonEngWord)) {
                                        addGrnBtn.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.grn_no).equals(jsonEngWord)) {
                                        txtWordGrnNum.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.actual_quantity).equals(jsonEngWord)) {
                                        txtWordActualQuantity.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.save).equals(jsonEngWord)) {
                                        txtSave.setText(strConvertedWord);
                                    }

                                }


//                                else if (strLanguageId.equals("3")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//
//                                    if ("Batch GRN Details".equals(jsonEngWord)) {
//                                        txtWordTitle.setText(strConvertedWord);
//                                    } else if ("Batch No".equals(jsonEngWord)) {
//                                        txtWordBatchNum.setText(strConvertedWord);
//                                    } else if ("Date".equals(jsonEngWord)) {
//                                        txtWordBatchCreatedDate.setText(strConvertedWord);
//                                    } else if ("List Of GRN".equals(jsonEngWord)) {
//                                        txtWordTitleList.setText(strConvertedWord);
//                                    } else if ("Add GRN ".equals(jsonEngWord)) {
//                                        addGrnBtn.setText(strConvertedWord);
//                                    } else if ("GRN No".equals(jsonEngWord)) {
//                                        txtWordGrnNum.setText(strConvertedWord);
//                                    } else if ("Actual Quantity".equals(jsonEngWord)) {
//                                        txtWordActualQuantity.setText(strConvertedWord);
//                                    } else if ("Save".equals(jsonEngWord)) {
//                                        txtSave.setText(strConvertedWord);
//                                    }
//
//                                } else if (strLanguageId.equals("4")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//
//
//                                    if ("Batch GRN Details".equals(jsonEngWord)) {
//                                        txtWordTitle.setText(strConvertedWord);
//                                    } else if ("Batch No".equals(jsonEngWord)) {
//                                        txtWordBatchNum.setText(strConvertedWord);
//                                    } else if ("Date".equals(jsonEngWord)) {
//                                        txtWordBatchCreatedDate.setText(strConvertedWord);
//                                    } else if ("List Of GRN".equals(jsonEngWord)) {
//                                        txtWordTitleList.setText(strConvertedWord);
//                                    } else if ("Add GRN ".equals(jsonEngWord)) {
//                                        addGrnBtn.setText(strConvertedWord);
//                                    } else if ("GRN No".equals(jsonEngWord)) {
//                                        txtWordGrnNum.setText(strConvertedWord);
//                                    } else if ("Actual Quantity".equals(jsonEngWord)) {
//                                        txtWordActualQuantity.setText(strConvertedWord);
//                                    } else if ("Save".equals(jsonEngWord)) {
//                                        txtSave.setText(strConvertedWord);
//                                    }
//
//
//                                } else if (strLanguageId.equals("5")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//
//
//                                    if ("Batch GRN Details".equals(jsonEngWord)) {
//                                        txtWordTitle.setText(strConvertedWord);
//                                    } else if ("Batch No".equals(jsonEngWord)) {
//                                        txtWordBatchNum.setText(strConvertedWord);
//                                    } else if ("Date".equals(jsonEngWord)) {
//                                        txtWordBatchCreatedDate.setText(strConvertedWord);
//                                    } else if ("List Of GRN".equals(jsonEngWord)) {
//                                        txtWordTitleList.setText(strConvertedWord);
//                                    } else if ("Add GRN ".equals(jsonEngWord)) {
//                                        addGrnBtn.setText(strConvertedWord);
//                                    } else if ("GRN No".equals(jsonEngWord)) {
//                                        txtWordGrnNum.setText(strConvertedWord);
//                                    } else if ("Actual Quantity".equals(jsonEngWord)) {
//                                        txtWordActualQuantity.setText(strConvertedWord);
//                                    } else if ("Save".equals(jsonEngWord)) {
//                                        txtSave.setText(strConvertedWord);
//                                    }
//
//
//                                } else {
//                                    String jsonEngWord1 = jsonObjectFarmerPD.getString("SelectedWord");
//
//
//                                    if ("Batch GRN Details".equals(jsonEngWord)) {
//                                        txtWordTitle.setText(jsonEngWord1);
//                                    } else if ("Batch No".equals(jsonEngWord)) {
//                                        txtWordBatchNum.setText(jsonEngWord1);
//                                    } else if ("Date".equals(jsonEngWord)) {
//                                        txtWordBatchCreatedDate.setText(jsonEngWord1);
//                                    } else if ("List Of GRN".equals(jsonEngWord)) {
//                                        txtWordTitleList.setText(jsonEngWord1);
//                                    } else if ("Add GRN ".equals(jsonEngWord)) {
//                                        addGrnBtn.setText(jsonEngWord1);
//                                    } else if ("GRN No".equals(jsonEngWord)) {
//                                        txtWordGrnNum.setText(jsonEngWord1);
//                                    } else if ("Actual Quantity".equals(jsonEngWord)) {
//                                        txtWordActualQuantity.setText(jsonEngWord1);
//                                    } else if ("Save".equals(jsonEngWord)) {
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

                        Toast.makeText(DealerBatchCreationAct.this, "no records found", Toast.LENGTH_LONG).show();
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

    private void initializeValues() {
        //For dealer name
        strDealerName = appHelper.getSharedPrefObj().getString(DeviceUserName, "");
        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");
        strProcessorID = appHelper.getSharedPrefObj().getString(DeviceProcessorId, "");
        //Token
        mToken = appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, "");


        //for back button
        imgBack.setOnClickListener(view -> {
            Intent intent = new Intent(DealerBatchCreationAct.this, MainDashBoardActivity.class); // Replace MainActivity with your actual dashboard activity
            startActivity(intent);
            finish();
        });


        //Batch number
        long millis = 0;
        try {
            String myDate = appHelper.getCurrentDateTime(AppConstant.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
            android.icu.text.SimpleDateFormat sdf = new android.icu.text.SimpleDateFormat(AppConstant.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
            Date date = sdf.parse(myDate);
            millis = date.getTime();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        txtBatchNo.setText("BP" + millis);

        //Current Date
        String dateTime1 = appHelper.getCurrentDateTime(DATE_FORMAT_YYYY_MM_DD);
        txtdate.setText(dateTime1);


        //Add grn btn
        addGrnBtn.setOnClickListener(view -> {
            addGrnDialog(strDealerID, mToken);


//            if(getGrnByDealerIdArrayList.size() <= 0){
//                Toast.makeText(this, "GRN List is empty", Toast.LENGTH_SHORT).show();
//            }
//            else {
//                addGrnDialog(strDealerID, mToken);
//            }
        });


        //For save button
        txtSave.setOnClickListener(v -> {
            if (appHelper.isNetworkAvailable()) {


                if (getGrnByDealerIdArrayList.size() > 0) {
                    addBatchProcessingHdrSubmitTable = new AddBatchProcessingHdrDetailsSubmitTable();
                    strBatchNo = txtBatchNo.getText().toString().trim();
                    addBatchProcessingHdrSubmitTable.setBatchNo(strBatchNo);

                    String dateTime = appHelper.getCurrentDateTime(DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS);

                    addBatchProcessingHdrSubmitTable.setIsActive("1");
                    addBatchProcessingHdrSubmitTable.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, "1"));
                    addBatchProcessingHdrSubmitTable.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, "1"));
                    addBatchProcessingHdrSubmitTable.setCreatedDate(dateTime);
                    addBatchProcessingHdrSubmitTable.setUpdatedDate(dateTime);

                    if (strDealerID.isEmpty()) {
                        addBatchProcessingHdrSubmitTable.setProcessorId(strProcessorID);
                        addBatchProcessingHdrSubmitTable.setDealerId("");
                    } else {
                        addBatchProcessingHdrSubmitTable.setDealerId(strDealerID);
                        addBatchProcessingHdrSubmitTable.setProcessorId("");
                    }


                    inserBatchProcessingHdrToServer(addBatchProcessingHdrSubmitTable, "dealer");


                    Toast.makeText(this, "Batch Details Saved!!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Please add GRNs!!", Toast.LENGTH_SHORT).show();
                }


            } else {
                Toast.makeText(DealerBatchCreationAct.this, "please check your  internet connection", Toast.LENGTH_SHORT).show();

            }
        });


        //for dealer
        getGrnByDealerIdArrayList = new ArrayList<>();
        batchGrnRecAdapter = new BatchGrnAdapter(DealerBatchCreationAct.this, DealerBatchCreationAct.this, appHelper, viewModel, getGrnByDealerIdArrayList);
        recycleBatchGrn.setLayoutManager(new LinearLayoutManager(this));
        recycleBatchGrn.hasFixedSize();
        if (appHelper.isNetworkAvailable()) {
            callSpinnerData();
        } else {
            Toast.makeText(DealerBatchCreationAct.this, "please check  your internet connection", Toast.LENGTH_SHORT).show();
        }
    }


    private void configureDagger() {
        AndroidInjection.inject(DealerBatchCreationAct.this);
    }

    public void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
    }


    //Post Header
    private void inserBatchProcessingHdrToServer(AddBatchProcessingHdrDetailsSubmitTable addBatchProcessingHdrSubmitTable, String typeRequest) {
        try {
            viewModel.syncBatchProcessinglHdrListDataToServer(addBatchProcessingHdrSubmitTable, typeRequest);
            if (viewModel.getStringLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        viewModel.getStringLiveData().removeObserver(this);
                        String strMessage = (String) o;
                        hdrId = strMessage;
                        Log.e("BatchTagHdr", hdrId.toString());

                        makeArrayList(hdrId);
                        // finish();
                        Intent intent = new Intent(DealerBatchCreationAct.this, MainDashBoardActivity.class); // Replace MainActivity with your actual dashboard activity
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

    private void makeArrayList(String hdrId) {

        List<AddBatchProcessingDtlDetailsSubmitTable> batchProcessingDtlDetailsArrayList = new ArrayList<>();
        String dateTime = appHelper.getCurrentDateTime(DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS);

        if (getGrnByDealerIdArrayList.size() > 0) {
            for (int i = 0; i < getGrnByDealerIdArrayList.size(); i++) {
                AddBatchProcessingDtlDetailsSubmitTable data = new AddBatchProcessingDtlDetailsSubmitTable();
                data.setBatchHdrId(hdrId);
                data.setGRNId(String.valueOf(getGrnByDealerIdArrayList.get(i).getId()));
                //   data.setDealerId(strDealerID);
                // data.setProcessorId("");
                //data.setFarmerId(String.valueOf(getGrnByDealerIdArrayList.get(i).getFarmerCode()));
                data.setQuantity(String.valueOf(getGrnByDealerIdArrayList.get(i).getQuantity()));

                data.setIsActive("1");
                data.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, "1"));
                data.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, "1"));
                data.setCreatedDate(dateTime);
                data.setUpdatedDate(dateTime);
                batchProcessingDtlDetailsArrayList.add(data);
            }
        }


        Log.e("batchTag", "2ndTable" + batchProcessingDtlDetailsArrayList.toString());
        insertbatchProcessingDtlToServer(batchProcessingDtlDetailsArrayList);
    }

    //Post Grn info
    private void insertbatchProcessingDtlToServer(List<AddBatchProcessingDtlDetailsSubmitTable> batchProcessingDtlDetailsArrayList) {

        for (int i = 0; i < batchProcessingDtlDetailsArrayList.size(); i++) {
            //Post
            try {
                viewModel.syncBatchProcessingDtlListDataT0Server(batchProcessingDtlDetailsArrayList.get(i));
                if (viewModel.getStringLiveData() != null) {
                    Observer getLeadRawDataObserver = new Observer() {
                        @Override
                        public void onChanged(@Nullable Object o) {
                            viewModel.getStringLiveData().removeObserver(this);
                            String strMessage = (String) o;
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

    public void callSpinnerData() {
        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);

        //for dealer
        Call<GetGrnData> call = service.getGRNDataForBacthByDealerIdFromServer(appHelper.getSharedPrefObj().getString(DeviceDealerID, ""), mToken);
        call.enqueue(new Callback<GetGrnData>() {
            @Override
            public void onResponse(Call<GetGrnData> call, Response<GetGrnData> response) {
                if (response.isSuccessful() && response.body() != null) {
//                    Log.e("BatchTag","Grn Data:"+response.body().getData().toString());

                    List<GetGrnByDealerId> getGrnByDealerId = response.body().getData();
                    GetGrnByDealerId grn = new GetGrnByDealerId();
                    grn.setGRNnumber("--select grn--");
                    getGrnByDealerId.add(0, grn);

                    filteredGrnList = getGrnByDealerId;

                    addGrnBtn.setClickable(true);
                    addGrnBtn.setEnabled(true);
                } else {
                    Toast.makeText(DealerBatchCreationAct.this, "no GRN records found please add grn", Toast.LENGTH_LONG).show();
                    addGrnBtn.setClickable(false);
                    addGrnBtn.setEnabled(false);
                }
            }


            @Override
            public void onFailure(Call<GetGrnData> call, Throwable t) {
                Toast.makeText(DealerBatchCreationAct.this, "no GRN records found please add grn", Toast.LENGTH_LONG).show();
                addGrnBtn.setClickable(false);
                addGrnBtn.setEnabled(false);
            }
        });
    }


    //Add Grn info dialog
    private void addGrnDialog(String dealerId, String token) {

        dialog = new Dialog(this, R.style.MyAlertDialogThemeNew);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.batch_grn_dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.show();


        //Initialize ui
        AppCompatSpinner spGrnNo = dialog.findViewById(R.id.sp_grn_no);
        AppCompatSpinner spProceesSelect = dialog.findViewById(R.id.sp_process_no);

        TextInputEditText txtQuantity = dialog.findViewById(R.id.et_actual_quantity);
        TextView txtgrnsave = dialog.findViewById(R.id.txtgrnsave);
        TextView txtBackChild = dialog.findViewById(R.id.txtgrncancel);


        TextView txtgrndetails = dialog.findViewById(R.id.txt_dialog_grn_details);
        TextView txtgrnno = dialog.findViewById(R.id.txt_dialog_grn_no);
        TextView txtquantity = dialog.findViewById(R.id.txt_dialog_quantity);


        LanguageApi(txtgrndetails, txtgrnno, txtquantity, txtgrnsave, txtBackChild);
        //Back dialog
        txtBackChild.setOnClickListener(view -> {
            dialog.dismiss();
        });


        spGrnNo.setVisibility(View.VISIBLE);
        spProceesSelect.setVisibility(View.GONE);
        if (getGrnByDealerIdArrayList.size() > 0) {
            // Toast.makeText(BatchProcessingActivity.this, "update", Toast.LENGTH_SHORT).show();
            filteredGrnList.removeAll(getGrnByDealerIdArrayList);
            ArrayAdapter<GetGrnByDealerId> dataAdapterCont = new ArrayAdapter<>(DealerBatchCreationAct.this,
                    android.R.layout.simple_spinner_item, filteredGrnList);
            dataAdapterCont.setDropDownViewResource(android.R.layout.simple_spinner_item);
            dataAdapterCont.notifyDataSetChanged();
//                ArrayAdapter<GetGrnByDealerId> getGrnByDealerIdArrayAdapter = dataAdapterCont;
            spGrnNo.setAdapter(dataAdapterCont);
        } else {
            ArrayAdapter<GetGrnByDealerId> dataAdapterCont = new ArrayAdapter<>(DealerBatchCreationAct.this,
                    android.R.layout.simple_spinner_item, filteredGrnList);
            dataAdapterCont.setDropDownViewResource(android.R.layout.simple_spinner_item);
            dataAdapterCont.notifyDataSetChanged();
//                ArrayAdapter<GetGrnByDealerId> getGrnByDealerIdArrayAdapter = dataAdapterCont;
            spGrnNo.setAdapter(dataAdapterCont);

        }


        spGrnNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GetGrnByDealerId data = (GetGrnByDealerId) parent.getItemAtPosition(position);
                if (position != 0) {
                    Log.e("BatchTag", String.valueOf(data.getId()));
                    grnId = data.getId();
                    txtQuantity.setText(data.getQuantity().toString());
                    getGrnByDealerIdObj = data;
                } else {
                    txtQuantity.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        txtgrnsave.setOnClickListener(view -> {
            if (grnId == 0) {
                Toast.makeText(DealerBatchCreationAct.this, "Please select a GRN!!", Toast.LENGTH_SHORT).show();
            } else if (txtQuantity.getText().toString().isEmpty()) {
                Toast.makeText(DealerBatchCreationAct.this, "Quantity Cannot be empty!!", Toast.LENGTH_SHORT).show();
            } else {
                if (getGrnByDealerIdObj != null) {
                    getGrnByDealerIdArrayList.add(getGrnByDealerIdObj);
                    recycleBatchGrn.setAdapter(batchGrnRecAdapter);
                    batchGrnRecAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }

            }
        });


    }

    private void LanguageApi(TextView txtgrndetails, TextView txtgrnno, TextView txtquantity, TextView txtgrnsave, TextView txtBackChild) {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        final ProgressDialog progressDialog = new ProgressDialog(DealerBatchCreationAct.this, R.style.AppCompatAlertDialogStyle);
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
                                        txtgrndetails.setText(jsonEngWord);
                                    } else if (getResources().getString(R.string.grn_no).equals(jsonEngWord)) {
                                        txtgrnno.setText(jsonEngWord);
                                    } else if (getResources().getString(R.string.actual_quantity).equals(jsonEngWord)) {
                                        txtquantity.setText(jsonEngWord);
                                    } else if (getResources().getString(R.string.save).equals(jsonEngWord)) {
                                        txtgrnsave.setText(jsonEngWord);
                                    } else if (getResources().getString(R.string.back).equals(jsonEngWord)) {
                                        txtBackChild.setText(jsonEngWord);
                                    }


                                } else {
                                    if (getResources().getString(R.string.add_grn_details).equals(jsonEngWord)) {
                                        txtgrndetails.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.grn_no).equals(jsonEngWord)) {
                                        txtgrnno.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.actual_quantity).equals(jsonEngWord)) {
                                        txtquantity.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.save).equals(jsonEngWord)) {
                                        txtgrnsave.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.back).equals(jsonEngWord)) {
                                        txtBackChild.setText(strConvertedWord);
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

                        Toast.makeText(DealerBatchCreationAct.this, "no records found", Toast.LENGTH_LONG).show();
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
    public void removePosition(int position, GetGrnByDealerId getGrnByDealerId, List<GetGrnByDealerId> getGrnByDealerIdList) {
        //int clickedPosition = holder.getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            // Handle the delete button click here, for example, by removing the item from the list
            GetGrnByDealerId removedItem = getGrnByDealerIdList.remove(position);
//            batchGrnRecAdapter.notifyItemRemoved(position); // Refresh the RecyclerView
//
//            filteredGrnList.add(removedItem);
            // Update the Spinner's adapter to reflect the changes
            batchGrnRecAdapter.notifyItemRemoved(position);// Refresh the RecyclerView
            // Determine the original position of the removed item in filteredGrnList
            //    int originalPosition = findOriginalPosition(removedItem, getGrnByDealerIdList);
            int originalPosition = findOriginalPosition(removedItem, filteredGrnList);
            // Add the removed item back to filteredGrnList at its original position
            if (originalPosition >= 0 && originalPosition <= filteredGrnList.size()) {
                filteredGrnList.add(originalPosition, removedItem);
            } else {
                // If original position is out of bounds, add it to the end
                filteredGrnList.add(removedItem);
            }

            // Update the Spinner's adapter to reflect the changes

        }
    }

    private int findOriginalPosition(GetGrnByDealerId item, List<GetGrnByDealerId> getGrnByDealerIdListData) {
        for (int i = 0; i < getGrnByDealerIdListData.size(); i++) {
            if (item.equals(getGrnByDealerIdListData.get(i))) {
                return i;
            }
        }
        return -1; // Item not found in the list
    }


}



