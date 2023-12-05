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
import com.socatra.intellitrack.adapters.BatchGrnProcessorAdapter;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.constants.AppConstant;
import com.socatra.intellitrack.database.entity.AddBatchProcessingDtlDetailsSubmitTable;
import com.socatra.intellitrack.database.entity.AddBatchProcessingHdrDetailsSubmitTable;
import com.socatra.intellitrack.models.get.GetGrnByDealerId;
import com.socatra.intellitrack.models.get.GetGrnByProcessorId;
import com.socatra.intellitrack.models.get.GetGrnProcessorData;
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

public class ProcessorBatchCreationAct extends BaseActivity implements BatchGrnProcessorAdapter.SyncCallbackProcessorInterface {

    private static final String TAG = ProcessorBatchCreationAct.class.getCanonicalName();

    public static ArrayList<GetGrnByDealerId> strGetListFinal;

    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    public AppViewModel viewModel;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    TextView addGrnBtn, txtSave;
    TextInputEditText txtBatchNo, txtdate;
    String strDealerName, mToken, strLanguageId;
    ImageView imgBack;

    String strBatchNo;

    RecyclerView recycleBatchGrn;

    Dialog dialog;

    BatchGrnProcessorAdapter batchGrnProcessorAdapter;
    int grnId = 0;
    //Header data
    AddBatchProcessingHdrDetailsSubmitTable addBatchProcessingHdrSubmitTable;
    String hdrId;

    GetGrnByProcessorId getGrnByProcessorIdObj;

    ArrayList<GetGrnByProcessorId> getGrnByProcessorIdArrayList;
    List<GetGrnByProcessorId> filterdProcessorList;

    String strDealerID;
    String strProcessorID;


    TextView txtBatchGrnDetails, txtbatchNo, txtDate, txtList, txtChildGrnNo, txtChildQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_processing);

        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");


        String farmerCode = getIntent().getStringExtra("FarmerCodeExtra");

        initializeUI();
        configureDagger();
        configureViewModel();
        initializeValues();

    }


    private void initializeUI() {

        txtBatchNo = findViewById(R.id.et_batch_no);
        txtdate = findViewById(R.id.et_date);
        imgBack = findViewById(R.id.img_cancel);
        recycleBatchGrn = findViewById(R.id.recycleBatchgrn);


        //language
        txtBatchGrnDetails = findViewById(R.id.txt_batch_grn_details);
        txtbatchNo = findViewById(R.id.txt_batch_no);
        txtDate = findViewById(R.id.txt_batch_created_date);
        txtList = findViewById(R.id.txt_list_of_grn);
        addGrnBtn = findViewById(R.id.add_batch_btn);
        txtChildGrnNo = findViewById(R.id.txt_child_grn_no);
        txtChildQuantity = findViewById(R.id.txt_child_actual_quantity);
        txtSave = findViewById(R.id.txtSave_Batch);


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProcessorBatchCreationAct.this, MainDashBoardActivity.class); // Replace MainActivity with your actual dashboard activity
        startActivity(intent);
        finish();
    }

    private void initializeValues() {
        //For dealer name
        strDealerName = appHelper.getSharedPrefObj().getString(DeviceUserName, "");
        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");
        strProcessorID = appHelper.getSharedPrefObj().getString(DeviceProcessorId, "");
        //Token
        mToken = appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, "");

        getLanguageDataList();
        //for back button
        imgBack.setOnClickListener(view -> {
            Intent intent = new Intent(ProcessorBatchCreationAct.this, MainDashBoardActivity.class); // Replace MainActivity with your actual dashboard activity
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
            // addGrnDialog(strDealerID, mToken);
//            if(getGrnByProcessorIdArrayList.size() <= 0){
//                Toast.makeText(this, "GRN List is empty", Toast.LENGTH_SHORT).show();
//            } else {
//                addGrnDialog(strDealerID, mToken);
//            }
        });


        //For save button
        txtSave.setOnClickListener(v -> {
            if (appHelper.isNetworkAvailable()) {

                //if (strDealerID.isEmpty()) {
                //for processor
                if (getGrnByProcessorIdArrayList.size() > 0) {
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
                    inserBatchProcessingHdrToServer(addBatchProcessingHdrSubmitTable, "processor");


                    Toast.makeText(this, "Batch Details Saved!!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Please add GRNs!!", Toast.LENGTH_SHORT).show();
                }


            } else {
                Toast.makeText(ProcessorBatchCreationAct.this, "please check your  internet connection", Toast.LENGTH_SHORT).show();

            }
        });


        if (appHelper.isNetworkAvailable()) {
            callSpinnerData();
        } else {
            Toast.makeText(ProcessorBatchCreationAct.this, "please check internet", Toast.LENGTH_SHORT).show();
        }


        //for processor
        getGrnByProcessorIdArrayList = new ArrayList<>();
        batchGrnProcessorAdapter = new BatchGrnProcessorAdapter(ProcessorBatchCreationAct.this, ProcessorBatchCreationAct.this, appHelper, viewModel, getGrnByProcessorIdArrayList);
        recycleBatchGrn.setLayoutManager(new LinearLayoutManager(this));
        recycleBatchGrn.hasFixedSize();

    }

    @Override
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
        final ProgressDialog progressDialog = new ProgressDialog(ProcessorBatchCreationAct.this, R.style.AppCompatAlertDialogStyle);
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
                                    if (getResources().getString(R.string.batch_grn_details).equals(jsonEngWord)) {
                                        txtBatchGrnDetails.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.batch_no).equals(jsonEngWord)) {
                                        txtbatchNo.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.date).equals(jsonEngWord)) {
                                        txtDate.setText(jsonEngWord1);
                                    } else if (getString(R.string.list_of_grn).equals(jsonEngWord)) {
                                        txtList.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.add_grn).equals(jsonEngWord)) {
                                        addGrnBtn.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.grn_no).equals(jsonEngWord)) {
                                        txtChildGrnNo.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.actual_quantity).equals(jsonEngWord)) {
                                        txtChildQuantity.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.save).equals(jsonEngWord)) {
                                        txtSave.setText(jsonEngWord1);
                                    }

                                } else {
                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");


                                    if (getResources().getString(R.string.batch_grn_details).equals(jsonEngWord)) {
                                        txtBatchGrnDetails.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.batch_no).equals(jsonEngWord)) {
                                        txtbatchNo.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.date).equals(jsonEngWord)) {
                                        txtDate.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.grn_list).equals(jsonEngWord)) {
                                        txtList.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.add_grn).equals(jsonEngWord)) {
                                        addGrnBtn.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.grn_no).equals(jsonEngWord)) {
                                        txtChildGrnNo.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.actual_quantity).equals(jsonEngWord)) {
                                        txtChildQuantity.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.save).equals(jsonEngWord)) {
                                        txtSave.setText(strConvertedWord);
                                    }
                                }


//                                else if (strLanguageId.equals("3")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//
//
//                                    if ("Batch GRN Details".equals(jsonEngWord)) {
//                                        txtBatchGrnDetails.setText(strConvertedWord);
//                                    } else if ("Batch No".equals(jsonEngWord)) {
//                                        txtbatchNo.setText(strConvertedWord);
//                                    }else if ("Date ".equals(jsonEngWord)) {
//                                        txtDate.setText(strConvertedWord);
//                                    }else if ("List Of GRN".equals(jsonEngWord)) {
//                                        txtList.setText(strConvertedWord);
//                                    }else if ("Add GRN ".equals(jsonEngWord)) {
//                                        addGrnBtn.setText(strConvertedWord);
//                                    }else if ("GRN No".equals(jsonEngWord)) {
//                                        txtChildGrnNo.setText(strConvertedWord);
//                                    }else if ("Actual Quantity".equals(jsonEngWord)) {
//                                        txtChildQuantity.setText(strConvertedWord);
//                                    }else if ("Save".equals(jsonEngWord)) {
//                                        txtSave.setText(strConvertedWord);
//                                    }
//                                } else if (strLanguageId.equals("4")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//
//
//                                    if ("Batch GRN Details".equals(jsonEngWord)) {
//                                        txtBatchGrnDetails.setText(strConvertedWord);
//                                    } else if ("Batch No".equals(jsonEngWord)) {
//                                        txtbatchNo.setText(strConvertedWord);
//                                    }else if ("Date ".equals(jsonEngWord)) {
//                                        txtDate.setText(strConvertedWord);
//                                    }else if ("List Of GRN".equals(jsonEngWord)) {
//                                        txtList.setText(strConvertedWord);
//                                    }else if ("Add GRN ".equals(jsonEngWord)) {
//                                        addGrnBtn.setText(strConvertedWord);
//                                    }else if ("GRN No".equals(jsonEngWord)) {
//                                        txtChildGrnNo.setText(strConvertedWord);
//                                    }else if ("Actual Quantity".equals(jsonEngWord)) {
//                                        txtChildQuantity.setText(strConvertedWord);
//                                    }else if ("Save".equals(jsonEngWord)) {
//                                        txtSave.setText(strConvertedWord);
//                                    }
//
//
//
//                                }else if (strLanguageId.equals("5")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//
//
//                                    if ("Batch GRN Details".equals(jsonEngWord)) {
//                                        txtBatchGrnDetails.setText(strConvertedWord);
//                                    } else if ("Batch No".equals(jsonEngWord)) {
//                                        txtbatchNo.setText(strConvertedWord);
//                                    }else if ("Date ".equals(jsonEngWord)) {
//                                        txtDate.setText(strConvertedWord);
//                                    }else if ("List Of GRN".equals(jsonEngWord)) {
//                                        txtList.setText(strConvertedWord);
//                                    }else if ("Add GRN ".equals(jsonEngWord)) {
//                                        addGrnBtn.setText(strConvertedWord);
//                                    }else if ("GRN No".equals(jsonEngWord)) {
//                                        txtChildGrnNo.setText(strConvertedWord);
//                                    }else if ("Actual Quantity".equals(jsonEngWord)) {
//                                        txtChildQuantity.setText(strConvertedWord);
//                                    }else if ("Save".equals(jsonEngWord)) {
//                                        txtSave.setText(strConvertedWord);
//                                    }
//


                                //     }  else{


                            }


                        } catch (Exception ex) {
                            ex.printStackTrace();
                            progressDialog.dismiss();
                            Log.d("Error", ">>>>" + ex.toString());
                        }
                    } else {
                        progressDialog.dismiss();

                        Toast.makeText(ProcessorBatchCreationAct.this, "no records found", Toast.LENGTH_LONG).show();
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


    private void configureDagger() {
        AndroidInjection.inject(ProcessorBatchCreationAct.this);
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
                        Log.e("BatchTagHdr", hdrId);

                        makeArrayList(hdrId);
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

        if (getGrnByProcessorIdArrayList.size() > 0) {
            for (int i = 0; i < getGrnByProcessorIdArrayList.size(); i++) {
                AddBatchProcessingDtlDetailsSubmitTable data = new AddBatchProcessingDtlDetailsSubmitTable();
                data.setBatchHdrId(hdrId);
                data.setGRNId(String.valueOf(getGrnByProcessorIdArrayList.get(i).getGRNId()));

                Log.d("GRNID", "Id" + String.valueOf(getGrnByProcessorIdArrayList.get(i).getGRNId()));


                // data.setDealerId("");
                //data.setProcessorId(strProcessorID);
                //data.setFarmerId(String.valueOf(getGrnByProcessorIdArrayList.get(i).getFarmerId()));
                data.setQuantity(String.valueOf(getGrnByProcessorIdArrayList.get(i).getQuantity()));

                data.setIsActive("1");
                data.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, "1"));
                data.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, "1"));
                data.setCreatedDate(dateTime);
                data.setUpdatedDate(dateTime);
                batchProcessingDtlDetailsArrayList.add(data);
            }
        }
        Log.e("batchTag", "2ndTable" + batchProcessingDtlDetailsArrayList);
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

        //for processor
        Call<GetGrnProcessorData> call = service.getGRNDataForBacthByProcessorIdFromServer(appHelper.getSharedPrefObj().getString(DeviceProcessorId, ""), mToken);
        call.enqueue(new Callback<GetGrnProcessorData>() {
            @Override
            public void onResponse(Call<GetGrnProcessorData> call, Response<GetGrnProcessorData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<GetGrnByProcessorId> getGrnByProcessorId = response.body().getData();
                    GetGrnByProcessorId grn = new GetGrnByProcessorId();
                    grn.setGRNnumber("--select grn--");
                    getGrnByProcessorId.add(0, grn);
                    filterdProcessorList = getGrnByProcessorId;

                    addGrnBtn.setClickable(true);
                    addGrnBtn.setEnabled(true);
                } else {
                    Toast.makeText(ProcessorBatchCreationAct.this, "no GRN records found", Toast.LENGTH_LONG).show();
                    addGrnBtn.setClickable(false);
                    addGrnBtn.setEnabled(false);
                }
            }

            @Override
            public void onFailure(Call<GetGrnProcessorData> call, Throwable t) {
                Toast.makeText(ProcessorBatchCreationAct.this, "no GRN records found", Toast.LENGTH_LONG).show();
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

        //   if (appHelper.getSharedPrefObj().getString(DeviceDealerID, "").isEmpty()) {
        spProceesSelect.setVisibility(View.VISIBLE);
        spGrnNo.setVisibility(View.GONE);

        if (getGrnByProcessorIdArrayList.size() > 0) {
            filterdProcessorList.removeAll(getGrnByProcessorIdArrayList);
            ArrayAdapter<GetGrnByProcessorId> dataAdapterCont = new ArrayAdapter<>(ProcessorBatchCreationAct.this, android.R.layout.simple_spinner_item, filterdProcessorList);
            dataAdapterCont.setDropDownViewResource(android.R.layout.simple_spinner_item);
            dataAdapterCont.notifyDataSetChanged();
            spProceesSelect.setAdapter(dataAdapterCont);
        } else {
            ArrayAdapter<GetGrnByProcessorId> dataAdapterCont = new ArrayAdapter<>(ProcessorBatchCreationAct.this, android.R.layout.simple_spinner_item, filterdProcessorList);
            dataAdapterCont.setDropDownViewResource(android.R.layout.simple_spinner_item);
            dataAdapterCont.notifyDataSetChanged();
            spProceesSelect.setAdapter(dataAdapterCont);
            //  spProceesSelect.setAdapter(getGrnByProcessorIdArrayAdapter);
        }


        spProceesSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GetGrnByProcessorId data = (GetGrnByProcessorId) parent.getItemAtPosition(position);
                if (position != 0) {
                    Log.e("BatchTag", String.valueOf(data.getId()));
                    grnId = data.getId();
                    txtQuantity.setText(String.valueOf(data.getQuantity()));
                    getGrnByProcessorIdObj = data;
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
                Toast.makeText(ProcessorBatchCreationAct.this, "Please select a GRN!!", Toast.LENGTH_SHORT).show();
            } else if (txtQuantity.getText().toString().isEmpty()) {
                Toast.makeText(ProcessorBatchCreationAct.this, "Quantity Cannot be empty!!", Toast.LENGTH_SHORT).show();
            } else {
                if (getGrnByProcessorIdObj != null) {
                    getGrnByProcessorIdArrayList.add(getGrnByProcessorIdObj);
                    recycleBatchGrn.setAdapter(batchGrnProcessorAdapter);
                    batchGrnProcessorAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });


    }

    private void LanguageApi(TextView txtgrndetails, TextView txtgrnno, TextView txtquantity, TextView txtgrnsave, TextView txtBackChild) {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        final ProgressDialog progressDialog = new ProgressDialog(ProcessorBatchCreationAct.this, R.style.AppCompatAlertDialogStyle);
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

                        Toast.makeText(ProcessorBatchCreationAct.this, "no records found", Toast.LENGTH_LONG).show();
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
    public void removeProcessorPosition(int position, GetGrnByProcessorId getGrnByProcessorId, List<GetGrnByProcessorId> getGrnByProcessorIdList) {


        if (position != RecyclerView.NO_POSITION) {
            // Handle the delete button click here, for example, by removing the item from the list
            GetGrnByProcessorId removedItem = getGrnByProcessorIdList.remove(position);
            batchGrnProcessorAdapter.notifyItemRemoved(position);// Refresh the RecyclerView
            //    int originalPosition = findOriginalPosition(removedItem, getGrnByDealerIdList);
            int originalPosition = findOriginalProcessorPosition(removedItem, filterdProcessorList);
            if (originalPosition >= 0 && originalPosition <= filterdProcessorList.size()) {
                filterdProcessorList.add(originalPosition, removedItem);
            } else {
                // If original position is out of bounds, add it to the end
                filterdProcessorList.add(removedItem);
            }

            // Update the Spinner's adapter to reflect the changes

        }
    }

    private int findOriginalProcessorPosition(GetGrnByProcessorId item, List<GetGrnByProcessorId> getGrnByProcessorIdListData) {
        for (int i = 0; i < getGrnByProcessorIdListData.size(); i++) {
            if (item.equals(getGrnByProcessorIdListData.get(i))) {
                return i;
            }
        }
        return -1; // Item not found in the list
    }

}



