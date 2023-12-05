package com.socatra.intellitrack.activity.invoice;


import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS;
import static com.socatra.intellitrack.constants.AppConstant.DeviceProcessorId;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserID;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserName;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserRole;
import static com.socatra.intellitrack.constants.AppConstant.LanguageId;
import static com.socatra.intellitrack.constants.AppConstant.TIME_FORMAT_HH_MM_SS;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.socatra.intellitrack.database.entity.AddAllInvoiceDetailsSubmitTable;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.database.entity.GetBatchDataFromServer;
import com.socatra.intellitrack.models.get.GetCustomersbyProcessorId;
import com.socatra.intellitrack.multiSelectAdapter.ProcessorBatchDropDownListAdapter;
import com.socatra.intellitrack.view_models.AppViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoiceManufactureActivity extends BaseActivity implements View.OnClickListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 1001;
    private static final String TAG = InvoiceManufactureActivity.class.getCanonicalName();
    public static boolean[] checkSelectedBatchProcessor;
    public static String strSelectProcessorBatchId;
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    public AppViewModel viewModel;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    String strProcessorId, strLanguageId, strManufatureName, strAddress, strSelectBatch, strSelectShipping, strSelectShippingID, strQuantity, strProduct, strVessel, strPortlaodingAddress, strPortdischargeAddress, strPortloadingtime, strPortdischargetime;
    AppCompatSpinner spBatch, spShipping;
    TextInputEditText txtProcessorName;
    TextInputEditText txtEtProduct, txtEtQuantity, txtEtShippingAddress, txtEtVessel, txtEtportloadingaddress, txtEtportdischargeaddress, txtEtportloadingtime, txtEtportdischargetime;
    TextView txtInvoiceDetails, txtProcessor, txtProduct, txtBatch, txtQuantity, txtShippingTo, txtShippingAddress, txtVessel, txtloadingAddress, txtDischargeAddress, txtLoadingTime, txtDischargeTime;
    ImageView imgCancel;
    ImageButton cdloadingtime, cddischargetime;
    TextView txtSave;
    List<String> BatchList = new ArrayList<>();
    List<String> BatchListID = new ArrayList<>();
    List<String> DealerID = new ArrayList<>();
    List<String> DealerList = new ArrayList<>();
    List<String> custmerIDs = new ArrayList<>();
    List<String> customerDataList = new ArrayList<>();
    String deviceRoleName;
    private PopupWindow batchPUW;
    private boolean expandedBatchList;//to  store information whether the selected values are displayed completely or in shortened representatn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_manufacture);
        strProcessorId = appHelper.getSharedPrefObj().getString(DeviceProcessorId, "");
        strManufatureName = appHelper.getSharedPrefObj().getString(DeviceUserName, "");
        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");

        deviceRoleName = appHelper.getSharedPrefObj().getString(DeviceUserRole, "");
        Log.d(TAG, "Processor id" + strProcessorId);
        initializeUI();
        initializeValues();
        configureDagger();
        configureViewModel();

        if (appHelper.isNetworkAvailable()) {
            getBatchData();
            getCustomerData();
        } else {
            Toast.makeText(InvoiceManufactureActivity.this, "please check your internet connection", Toast.LENGTH_LONG);

        }

    }


    private void initializeUI() {

        imgCancel = findViewById(R.id.image_back_sc);
        txtProcessorName = findViewById(R.id.et_processor_name);
        txtEtProduct = findViewById(R.id.et_product_processor);
        txtEtQuantity = findViewById(R.id.et_quantity_processor);
        spBatch = findViewById(R.id.sp_batch_processor);
        spShipping = findViewById(R.id.sp_shipping_processor);
        txtEtShippingAddress = findViewById(R.id.et_shipping_address_processor);
        txtEtVessel = findViewById(R.id.et_vessel_name_processor);
        txtEtportloadingaddress = findViewById(R.id.et_port_loading_processor);
        txtEtportdischargeaddress = findViewById(R.id.et_port_discharge_processor);
        txtEtportloadingtime = findViewById(R.id.et_port_loading_time_processor);
        txtEtportdischargetime = findViewById(R.id.et_port_discharge_time_processor);
        cdloadingtime = findViewById(R.id.cd_loadingtime_select);
        cddischargetime = findViewById(R.id.cd_dischargetime_select);
        imgCancel = findViewById(R.id.img_cancel);

        //Language
        txtInvoiceDetails = findViewById(R.id.txt_Invoice_details);
        txtProcessor = findViewById(R.id.txt_processor);
        txtProduct = findViewById(R.id.txt_product);
        txtBatch = findViewById(R.id.txt_batch);
        txtQuantity = findViewById(R.id.txt_quantity);
        txtShippingTo = findViewById(R.id.txt_shippingTo);
        txtShippingAddress = findViewById(R.id.txt_shipping_address);
        txtVessel = findViewById(R.id.txt_vessel);
        txtloadingAddress = findViewById(R.id.txt_port_loading);
        txtDischargeAddress = findViewById(R.id.txt_port_discharge);
        txtLoadingTime = findViewById(R.id.txt_port_loading_time);
        txtDischargeTime = findViewById(R.id.txt_port_discharge_time);
        txtSave = findViewById(R.id.txt_invoice_processor_save);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (appHelper.isNetworkAvailable()) {
            //getLanguageDataList();
        } else {
            Toast.makeText(InvoiceManufactureActivity.this, "please check your internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void initializeValues() {

        txtProcessorName.setText(strManufatureName);
        imgCancel.setOnClickListener(this);
        txtSave.setOnClickListener(this);
        txtEtportloadingtime.setOnClickListener(this);
        txtEtportdischargetime.setOnClickListener(this);


        //getLanguageDataList();


//        spBatch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                //  if (appHelper.isNetworkAvailable()) {
//                strSelectBatch = BatchList.get(position);
//                strSelectBatchId = BatchListID.get(position);
//
//                // ClusterNameTv.setText(ClusterName);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

//        spShipping.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                //  if (appHelper.isNetworkAvailable()) {
//                strSelectShippingID = DealerID.get(position);
//                strSelectShipping = DealerList.get(position);
//
//                // ClusterNameTv.setText(ClusterName);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//
//        });

        String dateTime = appHelper.getCurrentDateTime(TIME_FORMAT_HH_MM_SS);
        txtEtportloadingtime.setText(dateTime);
        txtEtportdischargetime.setText(dateTime);
//        strPortloadingtime = appHelper.getCurrentDateTime(TIME_FORMAT_HH_MM_SS);   DATE_FORMAT_DD_MM_YYYY2
//        strPortdischargetime = appHelper.getCurrentDateTime(TIME_FORMAT_HH_MM_SS);

        strPortloadingtime = appHelper.getCurrentDateTime(DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS);
        strPortdischargetime = appHelper.getCurrentDateTime(DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS);


        cdloadingtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "cdloadingtime clicked"); // Add a log statement to check if the click listener is triggered.

                final Calendar currentDate = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(InvoiceManufactureActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        currentDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        currentDate.set(Calendar.MINUTE, minute);
                        Log.v(TAG, "The chosen time: " + currentDate.getTime());

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS, Locale.US);
                        strPortloadingtime = simpleDateFormat.format(currentDate.getTime());


//                        SimpleDateFormat showDate = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS, Locale.US);
                        SimpleDateFormat showDate = new SimpleDateFormat(TIME_FORMAT_HH_MM_SS, Locale.US);
                        String showDate_value = showDate.format(currentDate.getTime());

                        txtEtportloadingtime.setText(showDate_value);
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false);

                // Check if the dialog is being created.
                if (timePickerDialog != null) {
                    timePickerDialog.show();
                } else {
                    Log.e(TAG, "TimePickerDialog is null");
                }
            }
        });


        cddischargetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "cdloadingtime clicked"); // Add a log statement to check if the click listener is triggered.

                final Calendar currentDate = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(InvoiceManufactureActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        currentDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        currentDate.set(Calendar.MINUTE, minute);
                        Log.v(TAG, "The chosen time: " + currentDate.getTime());

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS, Locale.US);
                        strPortdischargetime = simpleDateFormat.format(currentDate.getTime());
//                        SimpleDateFormat showDate = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS, Locale.US);
                        SimpleDateFormat showDate = new SimpleDateFormat(TIME_FORMAT_HH_MM_SS, Locale.US);
                        String showDate_value = showDate.format(currentDate.getTime());
                        txtEtportdischargetime.setText(showDate_value);
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false);

                // Check if the dialog is being created.
                if (timePickerDialog != null) {
                    timePickerDialog.show();
                } else {
                    Log.e(TAG, "TimePickerDialog is null");
                }
            }
        });


    }


    private void configureDagger() {
        AndroidInjection.inject(InvoiceManufactureActivity.this);
    }

    public void configureViewModel() {
        //  viewModel = ViewModelProviders.of(this, viewModelFactory).get(AppViewModel.class);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);


    }


    public void getBatchData() {
        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getBatchDetailsDataFromServerByProcessorID(strProcessorId, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));
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
                            ex.printStackTrace();
                            Log.d("Error", ">>>>" + ex.toString());
                        }

                    } else {
                        Toast.makeText(InvoiceManufactureActivity.this, "no records found", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    //  progressDialog.dismiss();
                    Toast.makeText(InvoiceManufactureActivity.this, "No Batch Data Found", Toast.LENGTH_LONG).show();
                    Log.d("Error", ">>>>" + ex.toString());
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                // progressDialog.dismiss();
                Toast.makeText(InvoiceManufactureActivity.this, "No Batch Data Found", Toast.LENGTH_LONG).show();
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());

            }
        });
    }

    public void getCustomerData() {
        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getCustomerDetailsbyIdFromServerByProcessorId(strProcessorId, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));
        final ProgressDialog progressDialog = new ProgressDialog(InvoiceManufactureActivity.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading  data...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.show();
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
                            ArrayList<GetCustomersbyProcessorId> getCustomersbyProcessorIdArrayList = new ArrayList<>();
                            custmerIDs.clear();
                            customerDataList.clear();

                            customerDataList.add("Select");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonCustomerID = jsonArray.getJSONObject(i);
                                GetCustomersbyProcessorId customersbyProcessorId = new GetCustomersbyProcessorId();
                                customersbyProcessorId.setId(Integer.valueOf(jsonCustomerID.getString("Id")));
                                customersbyProcessorId.setEntityName(jsonCustomerID.getString("EntityName"));

                                custmerIDs.add(jsonCustomerID.getString("Id"));
                                customerDataList.add(jsonCustomerID.getString("EntityName"));

                                if (!jsonCustomerID.isNull("Address")) {
                                    String Address = jsonCustomerID.getString("Address");
                                    Log.d("Address", "Address: " + Address);
                                    customersbyProcessorId.setAddress(Address);
                                } else {
                                    Log.d("Address", "Address is null or not found");
                                    // Handle the case where "TotalQuantity" is null or not present in the response
                                }
                                getCustomersbyProcessorIdArrayList.add(customersbyProcessorId);
                            }

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(InvoiceManufactureActivity.this,
                                    R.layout.spinner_dropdown_layout, customerDataList);
                            dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
                            spShipping.setAdapter(dataAdapter);
                            dataAdapter.notifyDataSetChanged();
//                            progressDialog.dismiss();


                            spShipping.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                                    if (position == 0) {
                                        // "--select Shipping--" is selected; clear the EditText
                                        txtEtShippingAddress.setText("");
                                    } else {
                                        // An actual shipping option is selected; update the EditText with the address
                                        String selectedDealerName = customerDataList.get(position - 1); // Adjust the index to match your data
                                        strSelectShippingID = custmerIDs.get(position - 1); // Adjust the index
                                        strSelectShipping = customerDataList.get(position - 1);

                                        GetCustomersbyProcessorId selectCustomerData = getCustomersbyProcessorIdArrayList.get(position - 1); // Adjust the index

                                        String Address = String.valueOf(selectCustomerData.getAddress());

                                        if (Address != null && !Address.equals("null")) {
                                            // Address is not null, so set it in the EditText
                                            Log.d("Address", "Setting - Address: " + Address);
                                            txtEtShippingAddress.setText(Address);
                                        } else {
                                            // Handle the case where "Address" is null or 'null'
                                            Log.d("Address", "Address is null");
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
                            ex.printStackTrace();
                            Log.d("Error", ">>>>" + ex.toString());
                        }

                    } else {
                        customerDataList.clear();
                        customerDataList.add("No customers found"); // Add a default item indicating no customers found
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(InvoiceManufactureActivity.this,
                                R.layout.spinner_dropdown_layout, customerDataList);
                        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
                        spShipping.setAdapter(dataAdapter);
                        dataAdapter.notifyDataSetChanged();
                        spShipping.setEnabled(false); // Disable the spinner as there are no items
                        txtEtShippingAddress.setText(""); // Clear the address field or handle it as needed
                        Toast.makeText(InvoiceManufactureActivity.this, "No customers found", Toast.LENGTH_LONG).show();
                        Toast.makeText(InvoiceManufactureActivity.this, "no records found", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
//                    progress_Dialog.dismiss();
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

        Intent intent = new Intent(InvoiceManufactureActivity.this, InvoiceListActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onClick(View v) {

        try {
            switch (v.getId()) {
                case R.id.img_cancel:
                    onBackPressed();
                    break;

                case R.id.txt_invoice_processor_save:

                    if (TextUtils.isEmpty(txtEtProduct.getText().toString().trim())) {
                        txtEtProduct.setError("Enter Product");
                    } else if (TextUtils.isEmpty(txtEtQuantity.getText().toString().trim())) {
                        txtEtQuantity.setError("Enter Quantity");
                    }
//                  } else  if (strSelectShipping.isEmpty()) {
//                Toast.makeText(InvoiceDealerActivity.this, "please select Customer", Toast.LENGTH_LONG).show();
//            }
                    else if (appHelper.isNetworkAvailable()) {
                        txtEtProduct.setText("Natural Rubber");
                        // String strFarmerCode = etFarmerCode.getText().toString().trim();
                        strQuantity = txtEtQuantity.getText().toString().trim();
                        strProduct = txtEtProduct.getText().toString().trim();
                        strAddress = txtEtShippingAddress.getText().toString().trim();
                        strVessel = txtEtVessel.getText().toString().trim();
                        strPortlaodingAddress = txtEtportloadingaddress.getText().toString().trim();
                        strPortdischargeAddress = txtEtportdischargeaddress.getText().toString().trim();


                        AddAllInvoiceDetailsSubmitTable addInvoiceetailsSubmitTable = new AddAllInvoiceDetailsSubmitTable();
                        addInvoiceetailsSubmitTable.setToProcessorId("");
                        addInvoiceetailsSubmitTable.setFromProcessorId(strProcessorId);
                        addInvoiceetailsSubmitTable.setFromDealerId("");
                        addInvoiceetailsSubmitTable.setToDealerId("");
                        addInvoiceetailsSubmitTable.setToCustomerId(strSelectShippingID);
                        addInvoiceetailsSubmitTable.setProduct(strProduct);
                        addInvoiceetailsSubmitTable.setQuantity(strQuantity);
                        addInvoiceetailsSubmitTable.setBatchId(strSelectProcessorBatchId);
                        Log.d(TAG, "onClick: Processor batchId " + strSelectProcessorBatchId);

                        addInvoiceetailsSubmitTable.setVessel(strVessel);
                        addInvoiceetailsSubmitTable.setPortofLoading(strPortlaodingAddress);
                        addInvoiceetailsSubmitTable.setPortofDischarge(strPortdischargeAddress);
                        addInvoiceetailsSubmitTable.setStrPOLTiming(strPortloadingtime);
                        addInvoiceetailsSubmitTable.setStrPODTiming(strPortdischargetime);
                        addInvoiceetailsSubmitTable.setShippingAddress(strAddress);


                        String dateTime = appHelper.getCurrentDateTime(DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS);
                        Log.d(TAG, "onClick: date" + dateTime);

                        addInvoiceetailsSubmitTable.setIsActive("1");
                        addInvoiceetailsSubmitTable.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, "1"));
                        addInvoiceetailsSubmitTable.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, "1"));
                        addInvoiceetailsSubmitTable.setCreatedDate(dateTime);
                        addInvoiceetailsSubmitTable.setUpdatedDate(dateTime);

                        insertInvoiceManufacturerDataToServer(addInvoiceetailsSubmitTable);
                        break;
                    } else {

                        Toast.makeText(InvoiceManufactureActivity.this, "please check your  internet connection", Toast.LENGTH_SHORT).show();

                    }
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }


    public void insertInvoiceManufacturerDataToServer(AddAllInvoiceDetailsSubmitTable addInvoiceetailsSubmitTable) {
        try {
            viewModel.syncAllAddInvoiceDetailsDataToServer(addInvoiceetailsSubmitTable, deviceRoleName);
            if (viewModel.getStringLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        viewModel.getStringLiveData().removeObserver(this);
                        String strMessage = (String) o;
                        Toast.makeText(InvoiceManufactureActivity.this, strMessage, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(InvoiceManufactureActivity.this, InvoiceListActivity.class);
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


    private void multipleSelection(ArrayList<GetBatchDataFromServer> getBatchDataFromServerTableArrayList) {
        final ArrayList<GetBatchDataFromServer> items = new ArrayList<GetBatchDataFromServer>();
        items.addAll(getBatchDataFromServerTableArrayList);
        checkSelectedBatchProcessor = new boolean[items.size()];
        //initialize all values of list to 'unselected' initially
        for (int i = 0; i < checkSelectedBatchProcessor.length; i++) {
            checkSelectedBatchProcessor[i] = false;
        }

        /*SelectBox is the TextView where the selected values will be displayed in the form of "Item 1 & 'n' more".
         * When this selectBox is clicked it will display all the selected values
         * and when clicked again it will display in shortened representation as before.
         * */
        TextView txtData = (TextView) findViewById(R.id.txt_batch_selection_prcsr);
        TextView finalTxtData = txtData;

        TextInputEditText txtQuantity = findViewById(R.id.et_quantity_processor);
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
                        if (checkSelectedBatchProcessor[i] == true) {
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
                    finalTxtData.setText(ProcessorBatchDropDownListAdapter.getSelected());
                    expandedBatchList = false;
                }
            }
        });

        //onClickListener to initiate the dropDown list
        Button createButton = (Button) findViewById(R.id.bt_batch_selection_prcsr);
        createButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                initiatePopUpBatch(items, finalTxtData, finaltxtQuantity);
            }
        });
    }

    private void initiatePopUpBatch(ArrayList<GetBatchDataFromServer> items, TextView tv, TextInputEditText finaltxtQuantity) {
        LayoutInflater inflater = (LayoutInflater) InvoiceManufactureActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //get the pop-up window i.e.  drop-down layout
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.pop_up_window, (ViewGroup) findViewById(R.id.PopUpView));

        //get the view to which drop-down layout is to be anchored
        RelativeLayout layout1 = (RelativeLayout) findViewById(R.id.rl_batch_selection_prcsr);
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
        ProcessorBatchDropDownListAdapter adapter = new ProcessorBatchDropDownListAdapter(this, items, tv, finaltxtQuantity, strSelectProcessorBatchId);
        list.setAdapter(adapter);
    }

    private void getLanguageDataList() {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        final ProgressDialog progressDialog = new ProgressDialog(InvoiceManufactureActivity.this, R.style.AppCompatAlertDialogStyle);
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
                                    if (getResources().getString(R.string.invoice_details).equals(jsonEngWord)) {
                                        txtInvoiceDetails.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.processor).equals(jsonEngWord)) {
                                        txtProcessor.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.net_quantity).equals(jsonEngWord)) {
                                        txtQuantity.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.product).equals(jsonEngWord)) {
                                        txtProduct.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.batch_no).equals(jsonEngWord)) {
                                        txtBatch.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.save).equals(jsonEngWord)) {
                                        txtSave.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.shipping_to).equals(jsonEngWord)) {
                                        txtShippingTo.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.shipping_address).equals(jsonEngWord)) {
                                        txtShippingAddress.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.vessel).equals(jsonEngWord)) {
                                        txtVessel.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.port_loading_address).equals(jsonEngWord)) {
                                        txtloadingAddress.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.port_discharge_address).equals(jsonEngWord)) {
                                        txtDischargeAddress.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.port_loading_time).equals(jsonEngWord)) {
                                        txtLoadingTime.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.port_discharge_time).equals(jsonEngWord)) {
                                        txtDischargeTime.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.save).equals(jsonEngWord)) {
                                        txtSave.setText(jsonEngWord1);
                                    }
                                } else {

                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");

                                    if (getResources().getString(R.string.invoice_details).equals(jsonEngWord)) {
                                        txtInvoiceDetails.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.processor).equals(jsonEngWord)) {
                                        txtProcessor.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.net_quantity).equals(jsonEngWord)) {
                                        txtQuantity.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.product).equals(jsonEngWord)) {
                                        txtProduct.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.batch_no).equals(jsonEngWord)) {
                                        txtBatch.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.save).equals(jsonEngWord)) {
                                        txtSave.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.shipping_to).equals(jsonEngWord)) {
                                        txtShippingTo.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.shipping_address).equals(jsonEngWord)) {
                                        txtShippingAddress.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.vessel).equals(jsonEngWord)) {
                                        txtVessel.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.port_loading_address).equals(jsonEngWord)) {
                                        txtloadingAddress.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.port_discharge_address).equals(jsonEngWord)) {
                                        txtDischargeAddress.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.port_loading_time).equals(jsonEngWord)) {
                                        txtLoadingTime.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.port_discharge_time).equals(jsonEngWord)) {
                                        txtDischargeTime.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.save).equals(jsonEngWord)) {
                                        txtSave.setText(strConvertedWord);
                                    }

                                }

//                                else if (strLanguageId.equals("3")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//                                    if ("Invoice Details".equals(jsonEngWord)) {
//                                        txtInvoiceDetails.setText(strConvertedWord);
//                                    } else if ("Processors".equals(jsonEngWord)) {
//                                        txtProcessor.setText(strConvertedWord);
//                                    }else if ("Net Quantity".equals(jsonEngWord)) {
//                                        txtQuantity.setText(strConvertedWord);
//                                    }else if ("Product".equals(jsonEngWord)) {
//                                        txtProduct.setText(strConvertedWord);
//                                    }else if ("Batch No".equals(jsonEngWord)) {
//                                        txtBatch.setText(strConvertedWord);
//                                    }else if ("Save".equals(jsonEngWord)) {
//                                        txtSave.setText(strConvertedWord);
//                                    }else if ("Shipping To".equals(jsonEngWord)) {
//                                        txtShippingTo.setText(strConvertedWord);
//                                    }else if ("Shipping Address".equals(jsonEngWord)) {
//                                        txtShippingAddress.setText(strConvertedWord);
//                                    }else if ("".equals(jsonEngWord)) {
//                                        txtVessel.setText(strConvertedWord);
//                                    }else if ("".equals(jsonEngWord)) {
//                                        txtloadingAddress.setText(strConvertedWord);
//                                    }else if ("".equals(jsonEngWord)) {
//                                        txtDischargeAddress.setText(strConvertedWord);
//                                    }else if ("".equals(jsonEngWord)) {
//                                        txtLoadingTime.setText(strConvertedWord);
//                                    }else if ("".equals(jsonEngWord)) {
//                                        txtDischargeTime.setText(strConvertedWord);
//                                    }
//
//                                } else if (strLanguageId.equals("4")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//                                    if ("Invoice Details".equals(jsonEngWord)) {
//                                        txtInvoiceDetails.setText(strConvertedWord);
//                                    } else if ("Processors".equals(jsonEngWord)) {
//                                        txtProcessor.setText(strConvertedWord);
//                                    }else if ("Net Quantity".equals(jsonEngWord)) {
//                                        txtQuantity.setText(strConvertedWord);
//                                    }else if ("Product".equals(jsonEngWord)) {
//                                        txtProduct.setText(strConvertedWord);
//                                    }else if ("Batch No".equals(jsonEngWord)) {
//                                        txtBatch.setText(strConvertedWord);
//                                    }else if ("Save".equals(jsonEngWord)) {
//                                        txtSave.setText(strConvertedWord);
//                                    }else if ("Shipping To".equals(jsonEngWord)) {
//                                        txtShippingTo.setText(strConvertedWord);
//                                    }else if ("Shipping Address".equals(jsonEngWord)) {
//                                        txtShippingAddress.setText(strConvertedWord);
//                                    }else if ("".equals(jsonEngWord)) {
//                                        txtVessel.setText(strConvertedWord);
//                                    }else if ("".equals(jsonEngWord)) {
//                                        txtloadingAddress.setText(strConvertedWord);
//                                    }else if ("".equals(jsonEngWord)) {
//                                        txtDischargeAddress.setText(strConvertedWord);
//                                    }else if ("".equals(jsonEngWord)) {
//                                        txtLoadingTime.setText(strConvertedWord);
//                                    }else if ("".equals(jsonEngWord)) {
//                                        txtDischargeTime.setText(strConvertedWord);
//                                    }
//
//
//
//                                }  else if (strLanguageId.equals("5")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//                                    if ("Invoice Details".equals(jsonEngWord)) {
//                                        txtInvoiceDetails.setText(strConvertedWord);
//                                    } else if ("Processors".equals(jsonEngWord)) {
//                                        txtProcessor.setText(strConvertedWord);
//                                    }else if ("Net Quantity".equals(jsonEngWord)) {
//                                        txtQuantity.setText(strConvertedWord);
//                                    }else if ("Product".equals(jsonEngWord)) {
//                                        txtProduct.setText(strConvertedWord);
//                                    }else if ("Batch No".equals(jsonEngWord)) {
//                                        txtBatch.setText(strConvertedWord);
//                                    }else if ("Save".equals(jsonEngWord)) {
//                                        txtSave.setText(strConvertedWord);
//                                    }else if ("Shipping To".equals(jsonEngWord)) {
//                                        txtShippingTo.setText(strConvertedWord);
//                                    }else if ("Shipping Address".equals(jsonEngWord)) {
//                                        txtShippingAddress.setText(strConvertedWord);
//                                    }else if ("".equals(jsonEngWord)) {
//                                        txtVessel.setText(strConvertedWord);
//                                    }else if ("".equals(jsonEngWord)) {
//                                        txtloadingAddress.setText(strConvertedWord);
//                                    }else if ("".equals(jsonEngWord)) {
//                                        txtDischargeAddress.setText(strConvertedWord);
//                                    }else if ("".equals(jsonEngWord)) {
//                                        txtLoadingTime.setText(strConvertedWord);
//                                    }else if ("".equals(jsonEngWord)) {
//                                        txtDischargeTime.setText(strConvertedWord);
//                                    }
//
//
//                                } else{


                            }


                        } catch (Exception ex) {
                            ex.printStackTrace();
                            progressDialog.dismiss();
                            Log.d("Error", ">>>>" + ex.toString());
                        }
                    } else {
                        progressDialog.dismiss();

                        Toast.makeText(InvoiceManufactureActivity.this, "no records found", Toast.LENGTH_LONG).show();
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
//    public void getBatchDataOld() {
//        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
//        Call<JsonElement> callRetrofit = null;
//        // callRetrofit = service.getGetLogisticsDetailsDataByIDFromServer(strDealerID, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));
//        callRetrofit = service.getBatchDetailsDataFromServerByProcessorID(strProcessorId,appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));
////        final ProgressDialog progressDialog = new ProgressDialog(InvoiceManufactureActivity.this, R.style.AppCompatAlertDialogStyle);
////        progressDialog.setCancelable(false);
////        progressDialog.setMessage("loading Batch data...");
////        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
////        progressDialog.show();
//        callRetrofit.enqueue(new Callback<JsonElement>() {
//            @Override
//            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//                try {
//                    String strResponse = String.valueOf(response.body());
//                    Log.d(TAG, "onResponse: >>>" + strResponse);
//                    JSONObject jsonObject = new JSONObject(strResponse);
//                    JSONArray jsonArray = jsonObject.getJSONArray("data");
//                    if (jsonArray.length() > 0) {
//
//                        try {
//                            ArrayList<GetBatchDataFromServer> getBatchDataFromServerTableArrayList = new ArrayList<>();
//                            BatchListID.clear();
//                            BatchList.clear();
//                            BatchList.add("Select Batch Number");
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
//                                GetBatchDataFromServer getBatchDataFromServerTable = new GetBatchDataFromServer();
//                                getBatchDataFromServerTable.setId(jsonObjectFarmerPD.getString("Id"));
//                                getBatchDataFromServerTable.setBatchNo(jsonObjectFarmerPD.getString("BatchNo"));
//
//                                if (!jsonObjectFarmerPD.isNull("TotalQuantity")) {
//                                    String totalQuantityString = jsonObjectFarmerPD.getString("TotalQuantity");
//                                    Log.d("TotalQuantity", "TotalQuantity: " + totalQuantityString);
//                                    getBatchDataFromServerTable.setTotalQuantity(Integer.valueOf(totalQuantityString));
//                                } else {
//                                    Log.d("TotalQuantity", "TotalQuantity is null or not found");
//                                    // Handle the case where "TotalQuantity" is null or not present in the response
//                                }
//
//                                getBatchDataFromServerTableArrayList.add(getBatchDataFromServerTable);
//                            }
//                            for (int i = 0; i < getBatchDataFromServerTableArrayList.size(); i++) {
//                                BatchListID.add(getBatchDataFromServerTableArrayList.get(i).getId());
//                                BatchList.add(getBatchDataFromServerTableArrayList.get(i).getBatchNo());
//                                //strStateID = stateListResponseDTOList.get(i).getStateId();
//                                Log.e("BatchNo", getBatchDataFromServerTableArrayList.get(i).getBatchNo());
//                            }
//                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(InvoiceManufactureActivity.this,
//                                    R.layout.spinner_dropdown_layout, BatchList);
//                            dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
//                            spBatch.setAdapter(dataAdapter);
//                            dataAdapter.notifyDataSetChanged();
//                            //  progressDialog.dismiss();
//
//
//                            spBatch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                @Override
//                                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//                                    if (position == 0) {
//                                        // "--select Batch--" is selected; clear the EditText
//                                        txtEtQuantity.setText("");
//                                    } else {
//                                        // An actual batch is selected; update the EditText with the total quantity
//                                        String selectedBatchNo = BatchList.get(position - 1); // Adjust the index to match your data
//                                        strSelectBatch = BatchList.get(position - 1);
//                                        strSelectBatchId = BatchListID.get(position - 1); // Adjust the index
//
//                                        GetBatchDataFromServer selectedBatchData = getBatchDataFromServerTableArrayList.get(position - 1); // Adjust the index
//
//                                        String totalQuantity = String.valueOf(selectedBatchData.getTotalQuantity());
//
//                                        if (totalQuantity != null && !totalQuantity.equals("null")) {
//                                            // Total quantity is not null, so set it in the EditText
//                                            Log.d("TotalQuantity", "Setting - Total Quantity: " + totalQuantity);
//                                            txtEtQuantity.setText(totalQuantity);
//                                        } else {
//                                            // Handle the case where "TotalQuantity" is null or 'null'
//                                            Log.d("TotalQuantity", "Total Quantity is null");
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
//
//
//                        } catch (Exception ex) {
//                            ex.printStackTrace();
//                            Log.d("Error", ">>>>" + ex.toString());
//                        }
//
//                    } else {
//                        Toast.makeText(InvoiceManufactureActivity.this, "no records found", Toast.LENGTH_LONG).show();
//                    }
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                    //  progressDialog.dismiss();
//                    Log.d("Error", ">>>>" + ex.toString());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonElement> call, Throwable t) {
//                // progressDialog.dismiss();
//                Log.d("Error Call", ">>>>" + call.toString());
//                Log.d("Error", ">>>>" + t.toString());
//
//            }
//        });
//    }