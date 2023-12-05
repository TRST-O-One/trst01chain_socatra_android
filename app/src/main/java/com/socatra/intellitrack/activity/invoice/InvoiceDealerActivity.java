package com.socatra.intellitrack.activity.invoice;


import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.DATE_FORMAT_DD_MM_YYYY2;
import static com.socatra.intellitrack.constants.AppConstant.DeviceDealerID;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserID;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserName;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserRole;
import static com.socatra.intellitrack.constants.AppConstant.LanguageId;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonElement;
import com.socatra.intellitrack.BaseActivity;
import com.socatra.intellitrack.R;
import com.socatra.intellitrack.activity.grnflow.DealerAddGrnActivity;
import com.socatra.intellitrack.database.entity.AddAllInvoiceDetailsSubmitTable;
import com.socatra.intellitrack.multiSelectAdapter.DealerBatchDropDownListAdapter;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.database.entity.GetBatchDataFromServer;
import com.socatra.intellitrack.database.entity.GetProcessorDataFromServer;
import com.socatra.intellitrack.view_models.AppViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoiceDealerActivity extends BaseActivity implements View.OnClickListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 1001;
    private static final String TAG = DealerAddGrnActivity.class.getCanonicalName();
    public static String strDealerSelectBatchId;
    public static boolean[] checkSelectedBatch;
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    public AppViewModel viewModel;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    String strDealerName, strDealerID, strAddress, strSelectBatch, strSelectShipping, strSelectShippingID, strQuantity, strProduct;
    AppCompatSpinner spBatch, spShipping;
    TextInputEditText txtDealerName;
    TextInputEditText txtEtProduct, txtEtQuantity, txtEtShippingAddress;
    ImageView imgCancel;
    TextView txtSave;
    List<String> processorID = new ArrayList<>();
    List<String> processorList = new ArrayList<>();
    String deviceRoleName;
    TextView txtNoDataBatch, txtNoDataProcessor;
    TextView txtWordInoviceDTLHeader, txtWordDealerName, txtWordProduct, txtWordBatch, txtWordNetQTY, txtWordShippingTO, txtWordShippingADD;
    private PopupWindow batchPUW;
    private boolean expandedBatchList;//to  store information whether the selected values are displayed completely or in shortened representatn

    String strLanguageId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_dealer);
        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");
        strDealerName = appHelper.getSharedPrefObj().getString(DeviceUserName, "");
        deviceRoleName = appHelper.getSharedPrefObj().getString(DeviceUserRole, "");
        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");
        Log.d(TAG, "dealred id" + strDealerID);
        initializeUI();
        initializeValues();
        configureDagger();
        configureViewModel();

        if (appHelper.isNetworkAvailable()) {
            getBatchData();
            getProcessorData();
        } else {
            Toast.makeText(InvoiceDealerActivity.this, "please check your internet connection", Toast.LENGTH_LONG).show();
        }


    }


    private void initializeUI() {

        imgCancel = findViewById(R.id.img_cancel_dealer);
        txtDealerName = findViewById(R.id.et_dealer_name);
        txtEtProduct = findViewById(R.id.et_product);
        spBatch = findViewById(R.id.sp_batch);
        spShipping = findViewById(R.id.sp_shipping);
        txtEtQuantity = findViewById(R.id.et_quantity_batch);
        txtEtShippingAddress = findViewById(R.id.et_shipping_address);


        txtSave = findViewById(R.id.txt_invoice_dealer_save);


        txtWordInoviceDTLHeader = findViewById(R.id.txt_word_invoice_details_dl);
        txtWordDealerName = findViewById(R.id.txt_word_dealer_name);
        txtWordProduct = findViewById(R.id.txt_word_product_dlr);
        txtWordBatch = findViewById(R.id.txt_word_batch);
        txtWordNetQTY = findViewById(R.id.txt_word_net_qty);
        txtWordShippingTO = findViewById(R.id.txt_word_shipping_to);
        txtWordShippingADD = findViewById(R.id.txt_word_shipping_address);


    }

    private void initializeValues() {

        txtDealerName.setText(strDealerName);
        imgCancel.setOnClickListener(this);
        txtSave.setOnClickListener(this);


    }


    private void configureDagger() {
        AndroidInjection.inject(InvoiceDealerActivity.this);
    }

    public void configureViewModel() {
        //  viewModel = ViewModelProviders.of(this, viewModelFactory).get(AppViewModel.class);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (appHelper.isNetworkAvailable()) {
            //getLanguageDataList();
        } else {
            Toast.makeText(InvoiceDealerActivity.this, "please check your internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void getLanguageDataList() {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        final ProgressDialog progressDialog = new ProgressDialog(InvoiceDealerActivity.this, R.style.AppCompatAlertDialogStyle);
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
                                    if (getResources().getString(R.string.invoice_details).equals(jsonEngWord)) {
                                        txtWordInoviceDTLHeader.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.dealer).equals(jsonEngWord)) {
                                        txtWordDealerName.setText(jsonEngWord1);
                                    } else if  (getResources().getString(R.string.product).equals(jsonEngWord)) {
                                        txtWordProduct.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.batch).equals(jsonEngWord)) {
                                        txtWordBatch.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.net_quantity).equals(jsonEngWord)) {
                                        txtWordNetQTY.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.shipping_to).equals(jsonEngWord)) {
                                        txtWordShippingTO.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.shipping_address).equals(jsonEngWord)) {
                                        txtWordShippingADD.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.save).equals(jsonEngWord)) {
                                        txtSave.setText(jsonEngWord1);
                                    }
                                } else {

                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
                                    if (getResources().getString(R.string.invoice_details).equals(jsonEngWord)) {
                                        txtWordInoviceDTLHeader.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.dealer).equals(jsonEngWord)) {
                                        txtWordDealerName.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.product).equals(jsonEngWord)) {
                                        txtWordProduct.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.batch).equals(jsonEngWord)) {
                                        txtWordBatch.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.net_quantity).equals(jsonEngWord)) {
                                        txtWordNetQTY.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.shipping_to).equals(jsonEngWord)) {
                                        txtWordShippingTO.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.shipping_address).equals(jsonEngWord)) {
                                        txtWordShippingADD.setText(strConvertedWord);
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

                        Toast.makeText(InvoiceDealerActivity.this, "no records found", Toast.LENGTH_LONG).show();
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


    public void getBatchData() {
        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        final ProgressDialog progressDialog = new ProgressDialog(InvoiceDealerActivity.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading data...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getBatchDetailsDataFromServerByDealerID(strDealerID, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    String strResponse = String.valueOf(response.body());
                    Log.d(TAG, "onResponse: batch >>>" + strResponse);
                    JSONObject jsonObject = new JSONObject(strResponse);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        try {
                            progressDialog.dismiss();

                            ArrayList<GetBatchDataFromServer> getBatchDataFromServerTableArrayList = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                GetBatchDataFromServer getBatchDataFromServerTable = new GetBatchDataFromServer();
                                getBatchDataFromServerTable.setId(jsonObjectFarmerPD.getString("Id"));
                                getBatchDataFromServerTable.setBatchNo(jsonObjectFarmerPD.getString("BatchNo"));
                                if (!jsonObjectFarmerPD.isNull("TotalQuantity")) {
                                    String totalQuantityString = jsonObjectFarmerPD.getString("TotalQuantity");
                                    Log.d("TotalQuantity", "TotalQuantity: " + totalQuantityString);
                                    getBatchDataFromServerTable.setTotalQuantity(Integer.valueOf(totalQuantityString));
                                } else {
                                    Log.d("TotalQuantity", "TotalQuantity is null or not found");
                                }
                                getBatchDataFromServerTableArrayList.add(getBatchDataFromServerTable);
                            }
                            multipleSelection(getBatchDataFromServerTableArrayList);

                        } catch (Exception ex) {
                            progressDialog.dismiss();

                            ex.printStackTrace();
                            Log.d("Error", ">>>>" + ex.toString());
                        }

                    } else {
                        progressDialog.dismiss();

                        Toast.makeText(InvoiceDealerActivity.this, "no records found", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    progressDialog.dismiss();

                    ex.printStackTrace();
                    //  progressDialog.dismiss();
                    Toast.makeText(InvoiceDealerActivity.this, "No Batch Data Found", Toast.LENGTH_LONG).show();
                    Log.d("Error", ">>>>" + ex.toString());
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();

                // progressDialog.dismiss();
                Toast.makeText(InvoiceDealerActivity.this, "No Batch Data Found", Toast.LENGTH_LONG).show();
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());

            }
        });
    }

    public void getProcessorData() {


        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        final ProgressDialog progressDialog = new ProgressDialog(InvoiceDealerActivity.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading data...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getProcessorListByDealerId(strDealerID, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));

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

                            ArrayList<GetProcessorDataFromServer> getProcessorDataFromServerArrayList = new ArrayList<>();
                            processorID.clear();
                            processorList.clear();
                            processorList.add("Select");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonProcessorID = jsonArray.getJSONObject(i);
                                GetProcessorDataFromServer getProcessorDataFromServer = new GetProcessorDataFromServer();
                                getProcessorDataFromServer.setProcessorId(jsonProcessorID.getString("ProcessorId"));
                                getProcessorDataFromServer.setProcessor(jsonProcessorID.getString("Processor"));
                                processorID.add(jsonProcessorID.getString("ProcessorId"));
                                processorList.add(jsonProcessorID.getString("Processor"));
                                if (!jsonProcessorID.isNull("Address")) {
                                    String strProcessorAdd = jsonProcessorID.getString("Address");
                                    Log.d("Address", "strProcessorAdd: " + strProcessorAdd);
                                    getProcessorDataFromServer.setAddress(String.valueOf(strProcessorAdd));
                                } else {
                                    Log.d("SubDealerAddress", "SubDealerAddress is null or not found");
                                }
                                getProcessorDataFromServerArrayList.add(getProcessorDataFromServer);
                            }
//                            for (int i = 0; i < getSubDealerDataFromServerArrayList.size(); i++) {
//                                subDealerID.add(getSubDealerDataFromServerArrayList.get(i).getprocessorID());
//                                processorList.add(getSubDealerDataFromServerArrayList.get(i).getSubDealerName());
//                                //strStateID = stateListResponseDTOList.get(i).getStateId();
//
//                            }
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(InvoiceDealerActivity.this,
                                    R.layout.spinner_dropdown_layout, processorList);
                            dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
                            spShipping.setAdapter(dataAdapter);
                            dataAdapter.notifyDataSetChanged();


                            spShipping.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                                    if (position == 0) {
                                        txtEtShippingAddress.setText("");
                                    } else {
                                        // An actual shipping option is selected; update the EditText with the address
                                        String selectedShipping = processorList.get(position - 1); // Adjust the index to match your data
                                        strSelectShippingID = processorID.get(position - 1); // Adjust the index
                                        strSelectShipping = processorList.get(position - 1);

                                        GetProcessorDataFromServer selectedProcessorData = getProcessorDataFromServerArrayList.get(position - 1); // Adjust the index

                                        String strProcessorAdd = String.valueOf(selectedProcessorData.getAddress());

                                        if (strProcessorAdd != null && !strProcessorAdd.equals("null")) {
                                            // Address is not null, so set it in the EditText
                                            Log.d("subDealerAddress", "Setting - subDealerAddress: " + strProcessorAdd);
                                            txtEtShippingAddress.setText(strProcessorAdd);
                                        } else {
                                            // Handle the case where "subDealerAddress" is null or 'null'
                                            Log.d("subDealerAddress", "subDealerAddress is null");
                                            txtEtShippingAddress.setText(""); // Clear the text field or handle it as needed
                                        }
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                    // Handle nothing selected if needed
                                }
                            });


                        } catch (Exception ex) {
                            progressDialog.dismiss();

                            ex.printStackTrace();
                            Log.d("Error", ">>>>" + ex.toString());
                        }

                    } else {

                        progressDialog.dismiss();

                        Toast.makeText(InvoiceDealerActivity.this, "no records found", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {

                    progressDialog.dismiss();

                    ex.printStackTrace();
//                    progress_Dialog.dismiss();
                    Log.d("Error", ">>>>" + ex.toString());
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

                progressDialog.dismiss();

//                progress_Dialog.dismiss();
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());

            }
        });
    }


    private void multipleSelection(ArrayList<GetBatchDataFromServer> getBatchDataFromServerTableArrayList) {
        final ArrayList<GetBatchDataFromServer> items = new ArrayList<GetBatchDataFromServer>();
        items.addAll(getBatchDataFromServerTableArrayList);
        checkSelectedBatch = new boolean[items.size()];
        //initialize all values of list to 'unselected' initially
        for (int i = 0; i < checkSelectedBatch.length; i++) {
            checkSelectedBatch[i] = false;
        }

        /*SelectBox is the TextView where the selected values will be displayed in the form of "Item 1 & 'n' more".
         * When this selectBox is clicked it will display all the selected values
         * and when clicked again it will display in shortened representation as before.
         * */
        TextView txtData = (TextView) findViewById(R.id.txt_batch_selection);
        TextView finalTxtData = txtData;

        TextInputEditText txtQuantity = findViewById(R.id.et_quantity_batch);
        TextInputEditText finaltxtQuantity = txtQuantity;
        txtData.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!expandedBatchList) {
                    //display all selected values
                    String selected = "";
                    int flag = 0;

                    for (int i = 0; i < items.size(); i++) {
                        if (checkSelectedBatch[i] == true) {
                            selected += items.get(i).getBatchNo();
                            selected += ", ";
                            flag = 1;
                        }
                    }

                    if (flag == 1)
                        finalTxtData.setText(selected);
                    expandedBatchList = true;
                } else {
                    //display shortened representation of selected values
                    finalTxtData.setText(DealerBatchDropDownListAdapter.getSelected());
                    expandedBatchList = false;
                }
            }
        });

        //onClickListener to initiate the dropDown list
        Button createButton = (Button) findViewById(R.id.bt_batch_selection);
        createButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                initiatePopUpBatch(items, finalTxtData, finaltxtQuantity);
            }
        });
    }

    private void initiatePopUpBatch(ArrayList<GetBatchDataFromServer> items, TextView tv, TextInputEditText finaltxtQuantity) {
        LayoutInflater inflater = (LayoutInflater) InvoiceDealerActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //get the pop-up window i.e.  drop-down layout
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.pop_up_window, (ViewGroup) findViewById(R.id.PopUpView));

        //get the view to which drop-down layout is to be anchored
        RelativeLayout layout1 = (RelativeLayout) findViewById(R.id.rl_batch_selection);
        batchPUW = new PopupWindow(layout, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        //Pop-up window background cannot be null if we want the pop-up to listen touch events outside its window
        batchPUW.setBackgroundDrawable(new BitmapDrawable());
        batchPUW.setTouchable(true);

        //let pop-up be informed about touch events outside its window. This  should be done before setting the content of pop-up
        batchPUW.setOutsideTouchable(true);
        batchPUW.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        //dismiss the pop-up i.e. drop-down when touched anywhere outside the pop-up
        batchPUW.setTouchInterceptor(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    batchPUW.dismiss();
                    return true;
                }
                return false;
            }
        });

        //provide the source layout for drop-down
        batchPUW.setContentView(layout);

        //anchor the drop-down to bottom-left corner of 'layout1'
        batchPUW.showAsDropDown(layout1);

        //populate the drop-down list
        final ListView list = (ListView) layout.findViewById(R.id.list_view);
        DealerBatchDropDownListAdapter adapter = new DealerBatchDropDownListAdapter(this, items, tv, finaltxtQuantity, strDealerSelectBatchId);
        list.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(InvoiceDealerActivity.this, InvoiceListActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.img_cancel_dealer:
                    onBackPressed();
                    break;

                case R.id.txt_invoice_dealer_save:

                    if (TextUtils.isEmpty(txtEtProduct.getText().toString().trim())) {
                        txtEtProduct.setError("Enter Product");
                    } else if (TextUtils.isEmpty(txtEtQuantity.getText().toString().trim())) {
                        txtEtQuantity.setError("Enter Quantity");
                    } else if (appHelper.isNetworkAvailable()) {
                        txtEtProduct.setText("Natural Rubber");
                        // String strFarmerCode = etFarmerCode.getText().toString().trim();
                        strQuantity = txtEtQuantity.getText().toString().trim();
                        strProduct = txtEtProduct.getText().toString().trim();
                        strAddress = txtEtShippingAddress.getText().toString().trim();

                        Log.d(TAG, "onClick: data" + strSelectShippingID);
                        AddAllInvoiceDetailsSubmitTable addInvoiceetailsSubmitTable = new AddAllInvoiceDetailsSubmitTable();
                        addInvoiceetailsSubmitTable.setToProcessorId(strSelectShippingID);
                        addInvoiceetailsSubmitTable.setFromProcessorId("");
                        addInvoiceetailsSubmitTable.setFromDealerId(strDealerID);
                        addInvoiceetailsSubmitTable.setToDealerId("");
                        addInvoiceetailsSubmitTable.setToCustomerId("");
                        addInvoiceetailsSubmitTable.setProduct(strProduct);
                        addInvoiceetailsSubmitTable.setQuantity(strQuantity);
                        addInvoiceetailsSubmitTable.setBatchId(strDealerSelectBatchId);
                        Log.d(TAG, "onClick: batchId " + strDealerSelectBatchId);

                        addInvoiceetailsSubmitTable.setVessel("");
                        addInvoiceetailsSubmitTable.setPortofLoading("");
                        addInvoiceetailsSubmitTable.setPortofDischarge("");
                        addInvoiceetailsSubmitTable.setPOLTiming(null);
                        addInvoiceetailsSubmitTable.setPODTiming(null);
                        addInvoiceetailsSubmitTable.setShippingAddress(strAddress);


                        String dateTime = appHelper.getCurrentDateTime(DATE_FORMAT_DD_MM_YYYY2);
                        Log.d(TAG, "onClick: date" + dateTime);

                        addInvoiceetailsSubmitTable.setIsActive("1");
                        addInvoiceetailsSubmitTable.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, "1"));
                        addInvoiceetailsSubmitTable.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, "1"));
                        addInvoiceetailsSubmitTable.setCreatedDate(dateTime);
                        addInvoiceetailsSubmitTable.setUpdatedDate(dateTime);

                        if (appHelper.isNetworkAvailable()) {
                            insertInvoiceDealerDataToServer(addInvoiceetailsSubmitTable);
                        } else {
                            Toast.makeText(InvoiceDealerActivity.this, "please check your internet", Toast.LENGTH_LONG).show();
                        }
                        break;
                    } else {
                        Toast.makeText(InvoiceDealerActivity.this, "please check your  internet connection", Toast.LENGTH_SHORT).show();

                    }
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void insertInvoiceDealerDataToServer(AddAllInvoiceDetailsSubmitTable addInvoiceetailsSubmitTable) {
        try {
            viewModel.syncAllAddInvoiceDetailsDataToServer(addInvoiceetailsSubmitTable, deviceRoleName);
            if (viewModel.getStringLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        viewModel.getStringLiveData().removeObserver(this);
                        String strMessage = (String) o;
                        Toast.makeText(InvoiceDealerActivity.this, strMessage, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(InvoiceDealerActivity.this, InvoiceListActivity.class);
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


}


//                            for (int i = 0; i < getBatchDataFromServerTableArrayList.size(); i++) {
//                                Log.d(TAG, "onResponse: bacthid" + getBatchDataFromServerTableArrayList.get(i).getId() );
//                                BatchListID.add(getBatchDataFromServerTableArrayList.get(i).getId());
//                                BatchList.add(getBatchDataFromServerTableArrayList.get(i).getBatchNo());
//                                //strStateID = stateListResponseDTOList.get(i).getStateId();
//                                Log.e("BatchNo", getBatchDataFromServerTableArrayList.get(i).getBatchNo());
//                            }
//                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(InvoiceDealerActivity.this,
//                                    R.layout.spinner_dropdown_layout, BatchList);
//                            dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
//                            spBatch.setAdapter(dataAdapter);
//                            dataAdapter.notifyDataSetChanged();
//                            progressDialog.dismiss();


//                            spBatch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                @Override
//                                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//                                    if (position == 0) {
//                                        // "--select Batch--" is selected; clear the EditText
//                                        txtEtQuantity.setText("");
//                                    } else {
//                                        // An actual batch is selected; update the EditText with the quantity
//                                        String selectedBatchNo = BatchList.get(position - 1); // Adjust the index to match your data
//                                        strSelectBatch = BatchList.get(position - 1);
//                                        strSelectBatchId = BatchListID.get(position - 1); // Adjust the index
//
//                                        GetBatchDataFromServer selectedBatchData = getBatchDataFromServerTableArrayList.get(position - 1); // Adjust the index
//
//                                        String totalQuantity = String.valueOf(selectedBatchData.getTotalQuantity());
//
//                                        if (totalQuantity != null && !totalQuantity.equals("null")) {
//                                            // TotalQuantity is not null, so set it in the EditText
//                                            Log.d("totalQuantity", "Setting - totalQuantity: " + totalQuantity);
//                                            txtEtQuantity.setText(totalQuantity);
//                                        } else {
//                                            // Handle the case where "TotalQuantity" is null or 'null'
//                                            Log.d("totalQuantity", "TotalQuantity is null or 'null'");
//                                            txtEtQuantity.setText(""); // Clear the text field or handle it as needed
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onNothingSelected(AdapterView<?> adapterView) {
//                                    // Handle nothing selected if needed
//                                }
//                            });