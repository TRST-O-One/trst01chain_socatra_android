package com.socatra.excutivechain.activity;

import static com.socatra.excutivechain.AppConstant.DeviceUserID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.socatra.excutivechain.AppConstant;
import com.socatra.excutivechain.BaseActivity;
import com.socatra.excutivechain.R;
import com.socatra.excutivechain.database.entity.FarmersTable;
import com.socatra.excutivechain.database.entity.Plantation;
import com.socatra.excutivechain.database.entity.VillageTable;
import com.socatra.excutivechain.location.BoundLocationManager;
import com.socatra.excutivechain.view_models.AppViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class PlantationActivity extends BaseActivity implements HasSupportFragmentInjector {

    String TAG = "PlantationActivity";
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    public AppViewModel viewModel;
    private SharedPreferences preferences;
    TextView txt_plot_add1;

    TextView txt_plot_num;
    TextView txt_farmer_code;
    TextView txt_plot_owner;
    TextView txt_plot_size;
    TextView txt_plot_address;
    TextView txt_plot_village;
    TextView txt_gps;
    TextView txtSaveAddPlant;

    String strFarmerCode = "", strFarmerSubDistricId = "", strFarmerVillageID = "";

    EditText etPlantNo, etFarmerCode, etSize, etAddress;


    String strPlantNo, strVillage, strVillageName = "select", crLat = "", crLong = "", strPlotOwner;

    double strSize = 0.0;

    Spinner spVillage, etPlotOwner;

    ImageView imgBack;

    TextView txtGpsPlant, txtGeoBoundPlant;

    String[] ownerArr = new String[]{"select", "Owned", "Leased"};

    Integer gpsCat = 0;
    String mArea, areaGeo;


    List<String> villageNamesList = new ArrayList<>();
    List<String> villageListIDs = new ArrayList<>();
    List<String> villageListCode = new ArrayList<>();

    FusedLocationProviderClient client;
    LocationRequest locationRequest;

    ProgressDialog progressDialog;

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);

            if (locationResult != null) {

                //initialise latlang
                LatLng latLng = new LatLng(locationResult.getLocations().get(0).getLatitude(), locationResult.getLocations().get(0).getLongitude());

                crLat = String.valueOf(locationResult.getLocations().get(0).getLatitude());
                crLong = String.valueOf(locationResult.getLocations().get(0).getLongitude());
                Log.e("AccLatLong", crLat + "," + crLong);
                txtGpsPlant.setText(crLat + "," + crLong);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stopLocationUpdate();
                        Log.e("AccLatLongStop", "stop for loc");

                    }
                }, 1 * 200);//ch1


            } else {
                Log.e("AccLatLong", "Loc Null");
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantation);

        strFarmerCode = getIntent().getStringExtra("mFarmerCode");
//        getAreaValue();
        Log.e(TAG, strFarmerCode);

        initializeUI();
        configureDagger();
        configureViewModel();
        initializeValues();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        client = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(200);//1hr3600000
        locationRequest.setFastestInterval(100);//360000
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        progressDialog = new ProgressDialog(PlantationActivity.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Getting location please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        updateTextLabels();

    }

    @SuppressLint("SetTextI18n")
    private void updateTextLabels() {
        String selectedLanguage = getSelectedLanguage();

        String hdAddPlot = getResources().getString(R.string.plot_add);
        String hdPlotNo = getResources().getString(R.string.plot_number);
        String hdFarmerCode = getResources().getString(R.string.farmer_code);
        String hdPlotOwner = getResources().getString(R.string.plot_ownership);
        String hdSize = getResources().getString(R.string.size);
        String hdAddress = getResources().getString(R.string.address1);
        String hdVillage = getResources().getString(R.string.pvillage);
        String hdGps = getResources().getString(R.string.gps);
        String hdSave = getResources().getString(R.string.save);

        if (selectedLanguage.equals("English")) {
            txt_plot_add1.setText(hdAddPlot);
            txt_plot_num.setText(hdPlotNo);
            txt_farmer_code.setText(hdFarmerCode);
            txt_plot_owner.setText(hdPlotOwner);
            txt_plot_size.setText(hdSize);
            txt_plot_address.setText(hdAddress);
            txt_plot_village.setText(hdVillage);
            txt_gps.setText(hdGps);
            txtSaveAddPlant.setText(hdSave);
        } else {
            txt_plot_add1.setText(getLanguageFromLocalDb(selectedLanguage,hdAddPlot)+ "/" + hdAddPlot);
            txt_plot_num.setText(getLanguageFromLocalDb(selectedLanguage,hdPlotNo)+ "/" + hdPlotNo);
            txt_farmer_code.setText(getLanguageFromLocalDb(selectedLanguage,hdFarmerCode)+ "/" + hdFarmerCode);
            txt_plot_owner.setText(getLanguageFromLocalDb(selectedLanguage,hdPlotOwner)+ "/" + hdPlotOwner);
            txt_plot_size.setText(getLanguageFromLocalDb(selectedLanguage,hdSize)+ "/" + hdSize);
            txt_plot_address.setText(getLanguageFromLocalDb(selectedLanguage,hdAddress)+ "/" + hdAddress);
            txt_plot_village.setText(getLanguageFromLocalDb(selectedLanguage,hdVillage)+ "/" + hdVillage);
            txt_gps.setText(getLanguageFromLocalDb(selectedLanguage,hdGps)+ "/" + hdGps);
            txtSaveAddPlant.setText(getLanguageFromLocalDb(selectedLanguage,hdSave)+ "/" + hdSave);
        }

    }

    private String getSelectedLanguage() {
        return preferences.getString("selected_language", "English");
    }
//    private void getAreaValue() {
//        if (areaGeo!=null) {
//            areaGeo = getIntent().getStringExtra("areaGeo");
//            txtGeoBoundPlant.setText(areaGeo);
//        }
//        else {
//            areaGeo="0";
//        }
//    }

    private void initializeUI() {
        etPlantNo = findViewById(R.id.et_plot_no_add);
        etFarmerCode = findViewById(R.id.etFarmerCodeAddPlant);
        imgBack = findViewById(R.id.imgBackAddPlant);
        spVillage = findViewById(R.id.spVillageAddPlant);
        txtGpsPlant = findViewById(R.id.txtGpsPlant);
        txtGeoBoundPlant = findViewById(R.id.txtGeoBoundPlant);
        txtSaveAddPlant = findViewById(R.id.txtSaveAddPlant);
        etPlotOwner = findViewById(R.id.et_plot_owner_ship);
        etSize = findViewById(R.id.et_plot_size_add);
        etAddress = findViewById(R.id.et_plot_address_add);

        //:Labels
        txt_plot_add1 = findViewById(R.id.txt_plot_add1);
        txt_plot_num = findViewById(R.id.txt_plot_num);
        txt_farmer_code = findViewById(R.id.txt_farmer_code);
        txt_plot_owner = findViewById(R.id.txt_plot_owner);
        txt_plot_size = findViewById(R.id.txt_plot_size);
        txt_plot_address = findViewById(R.id.txt_plot_address);
        txt_plot_village = findViewById(R.id.txt_plot_village);
        txt_gps = findViewById(R.id.txt_gps);
//        txtSaveAddPlant = findViewById(R.id.txtSaveAddPlant);
    }

    private void initializeValues() {

        //Plantation Code
        long millis = 0;
        try {
            String myDate = appHelper.getCurrentDateTime(AppConstant.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
            android.icu.text.SimpleDateFormat sdf = new android.icu.text.SimpleDateFormat(AppConstant.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
            Date date = sdf.parse(myDate);
            millis = date.getTime();
//            Toast.makeText(AddFarmerActivity.this, millis+"", Toast.LENGTH_SHORT).show();
        } catch (Exception exception) {
//            Toast.makeText(AddFarmerActivity.this, exception.getMessage()+"", Toast.LENGTH_SHORT).show();
            exception.printStackTrace();
        }
        strPlantNo = "P_" + millis + "_" + appHelper.getSharedPrefObj().getString(DeviceUserID, "");
        etPlantNo.setText(strPlantNo);

        //Farmer Code
        etFarmerCode.setText(strFarmerCode);

        //Gps
        txtGpsPlant.setOnClickListener(view -> {
            getGpsCoordinates();
        });

        //For title
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(PlantationActivity.this,
                android.R.layout.simple_spinner_item, ownerArr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        etPlotOwner.setAdapter(dataAdapter);
        etPlotOwner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strPlotOwner = parent.getItemAtPosition(position).toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spVillage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                VillageTable data = (VillageTable) parent.getItemAtPosition(position);
//                strVillage = String.valueOf(data.getId());
                strVillageName = villageNamesList.get(position);
                strVillage = villageListIDs.get(position);
                Log.d(TAG, "onItemSelected: strvillage" + strVillage + "village name" + strVillageName);
                //   Log.d(TAG, "onItemSelected: villageName" + villageNamesList);
                //     strVillageName
                // etPincode.setText(data.getPinCode());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Geo boundaries
//        txtGeoBoundPlant.setText(areaGeo);
//        txtGeoBoundPlant.setOnClickListener(view->{
//            getGeoBoundariesArea();
//        });


        txtSaveAddPlant.setOnClickListener(view -> {

            if (strPlotOwner.equals("select")) {
                Toast.makeText(this, "Please select plot owner ship", Toast.LENGTH_SHORT).show();
            } else if (etSize.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please add Area!!", Toast.LENGTH_SHORT).show();
            } else if (etAddress.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please add Address!!", Toast.LENGTH_SHORT).show();
            } else if (strVillageName.equalsIgnoreCase("select")) {
                Toast.makeText(this, "Please select the village", Toast.LENGTH_SHORT).show();
            } else if (strVillage.equalsIgnoreCase("No data Exist")) {
                Toast.makeText(this, "There is no village data exist ", Toast.LENGTH_SHORT).show();
            } else if (txtGpsPlant.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please take Gps!!", Toast.LENGTH_SHORT).show();
            } else {

                String strAddress = etAddress.getText().toString();
                strSize = Double.parseDouble(etSize.getText().toString());

                Plantation plantation = new Plantation();
                plantation.setPlotCode(strPlantNo);
                plantation.setFarmerCode(strFarmerCode);
                plantation.setTypeOfOwnership(strPlotOwner);
                plantation.setAreaInHectors(strSize);
                plantation.setGeoboundariesArea(0.0);//need to add
                plantation.setLatitude(Double.valueOf(crLat));
                plantation.setLongitude(Double.valueOf(crLong));
                plantation.setAddress(strAddress);
                plantation.setVillageId(strVillage);
                Log.d(TAG, "initializeValues: strvillage" + strVillage);
                plantation.setLabourStatus("false");

                plantation.setIsActive("true");
                plantation.setSync(false);
                plantation.setServerSync("0");
                plantation.setCreatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                plantation.setUpdatedByUserId(appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
                String dateTime = appHelper.getCurrentDateTime(AppConstant.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
                plantation.setCreatedDate(dateTime);
                plantation.setUpdatedDate(dateTime);


                viewModel.insertPlantationDetailListTableLocal(plantation);
                successfulSaved();
            }
        });

        //Back img
        imgBack.setOnClickListener(view -> {
            finish();
        });

    }

    private void successfulSaved() {
        Toast.makeText(this, "Successful!!", Toast.LENGTH_SHORT).show();
//        Intent intent= new Intent(PlantationActivity.this,PlantationHomeActivity.class);
//        intent.putExtra("mFarmerCode",strFarmerCode);
//        startActivity(intent);
        finish();
    }

    private void getGeoBoundariesArea() {
        mArea = etSize.getText().toString();
        if (!mArea.isEmpty()) {
//            Intent intent = new Intent(PlantationActivity.this, MapsActivity.class);
//            intent.putExtra("PlotId", strPlantNo);
//            intent.putExtra("gpsCat", gpsCat);
//            intent.putExtra("FarmerCode", strFarmerCode);
//            intent.putExtra("ProvideSize",mArea);
//            intent.putExtra("id", appHelper.getSharedPrefObj().getString(DeviceUserID, ""));
//            startActivityForResult(intent,RESULT_OK);
        } else {
            Toast.makeText(this, "Please enter Area!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void getGpsCoordinates() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(PlantationActivity.this);
        builder1.setMessage("Take current location??");
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        getLocationDetails();
                        checkSettingsAndStartLocationUpdates();
                        progressDialog.show();
                        Toast.makeText(PlantationActivity.this, "Coordinates collected!!", Toast.LENGTH_SHORT).show();
                        dialog.cancel();


//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                            }
//                        },2000);
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void getLocationDetails() {
        try {
            // appHelper.getDialogHelper().getLoadingDialog().showGIFLoading();
            BoundLocationManager.getInstance(PlantationActivity.this).observe(this, new Observer<Location>() {
                @Override
                public void onChanged(@Nullable Location location) {
                    if (location != null) {
                        //  appHelper.getDialogHelper().getLoadingDialog().closeDialog();
                        crLat = String.valueOf(location.getLatitude());
                        crLong = String.valueOf(location.getLongitude());
//                        Log.e("Ashraf ", location.getLatitude() + "longitude" + location.getLongitude());
                        txtGpsPlant.setText(crLat + "," + crLong);

                    } else {
                        Toast.makeText(PlantationActivity.this, "Location is null", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            //appHelper.getDialogHelper().getLoadingDialog().closeDialog();
        }
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    private void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);

        getFramerDetailsByFarmerCodeFromLocalDb();
        //getVillageData();
    }


    public void getFramerDetailsByFarmerCodeFromLocalDb() {
        try {
            viewModel.getFarmerDetailsByFarmerCode(strFarmerCode);
            if (viewModel.getFarmersTableByFarmerCodeLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        FarmersTable farmersTables = (FarmersTable) o;
                        viewModel.getFarmersTableByFarmerCodeLiveData().removeObserver(this);
                        Log.e(TAG, "onChanged: data" + farmersTables);
                        if (farmersTables != null) {
                            String strFarmerVillageID = farmersTables.getVillageId();
                            Log.d(TAG, "onChanged: village" + strFarmerVillageID);
                            getDisIDFromVillageIdFromLocalDb(strFarmerVillageID);
                            //getVillageListFromLocalDbById(strFarmerVillageID);

                        } else {
                            Toast.makeText(PlantationActivity.this, "data is empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                viewModel.getFarmersTableByFarmerCodeLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getDisIDFromVillageIdFromLocalDb(String strvillageId) {
        try {
            viewModel.getDistricIDFromVillageTableDetailsByVillageId(strvillageId);
            if (viewModel.getDissIdFromVillageTableLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        VillageTable villageTable = (VillageTable) o;
                        viewModel.getDissIdFromVillageTableLiveData().removeObserver(this);
                        Log.e(TAG, "onChanged: data" + villageTable);
                        if (villageTable != null) {
                            String strSubDisId = villageTable.getSubDistrictId();
                            Log.d(TAG, "onChanged: strSubDisId" + strSubDisId);
                            getVillageListFromLocalDbById(strSubDisId);

                        } else {
                            Toast.makeText(PlantationActivity.this, "data is empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                viewModel.getDissIdFromVillageTableLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getVillageListFromLocalDbById(String subDistricId) {

        try {
            viewModel.getVillageDetailsListFromLocalDB(subDistricId);
            if (viewModel.getvillageDetailsByPincodeLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<VillageTable> villageTableList = (List<VillageTable>) o;
                        viewModel.getvillageDetailsByPincodeLiveData().removeObserver(this);
                        villageNamesList.clear();
                        villageListIDs.clear();
                        villageListCode.clear();
                        villageNamesList.add("select");
                        villageListIDs.add("select");
                        if (villageTableList != null && villageTableList.size() > 0) {
                            for (int i = 0; i < villageTableList.size(); i++) {
                                villageNamesList.add(villageTableList.get(i).getName());
                                villageListIDs.add(villageTableList.get(i).getId());
                                villageListCode.add(villageTableList.get(i).getCode());
                                //strStateID = stateListResponseDTOList.get(i).getStateId();
                                Log.e(":dsvbjl_id_viilage", villageTableList.get(i).getId());
                                Log.e(":dsvbjl_id_viilageName", villageTableList.get(i).getName());
                                Log.e(":dsvbjl_id_viilageCode", villageTableList.get(i).getCode());
                                Log.e(":dsvbjl_id_Mandal", villageTableList.get(i).getCode());
                            }
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(PlantationActivity.this,
                                    android.R.layout.simple_spinner_item, villageNamesList);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            dataAdapter.notifyDataSetChanged();
                            spVillage.setAdapter(dataAdapter);

                        } else {
                            List<String> emptyList = new ArrayList<>();
                            emptyList.add("No data Exist");
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(PlantationActivity.this,
                                    android.R.layout.simple_spinner_item, emptyList);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            spVillage.setAdapter(dataAdapter);
                            dataAdapter.notifyDataSetChanged();
                            strVillage = "No data Exist";
                            spVillage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    strVillage = "No data Exist";
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        }
                    }
                };
                viewModel.getvillageDetailsByPincodeLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
//            Log.e("Villagelist","Null catch");
        }
    }


    private void getVillageData() {
        try {
            viewModel.getAllVillageDetailsListFromLocalDB();
            if (viewModel.getvillageDetailsByPincodeLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<VillageTable> villageTableList = (List<VillageTable>) o;
                        viewModel.getvillageDetailsByPincodeLiveData().removeObserver(this);
                        if (villageTableList != null && villageTableList.size() > 0) {


                            ArrayAdapter<VillageTable> dataAdapter = new ArrayAdapter<>(PlantationActivity.this,
                                    android.R.layout.simple_spinner_item, villageTableList);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            dataAdapter.notifyDataSetChanged();
                            spVillage.setAdapter(dataAdapter);

                            spVillage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    VillageTable data = (VillageTable) parent.getItemAtPosition(position);
                                    strVillage = String.valueOf(data.getVillageId());

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        } else {
                            List<String> emptyList = new ArrayList<>();
                            emptyList.add("No data Exist");
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(PlantationActivity.this,
                                    android.R.layout.simple_spinner_item, emptyList);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            spVillage.setAdapter(dataAdapter);
                            dataAdapter.notifyDataSetChanged();
                            strVillage = "No data Exist";
                            spVillage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    strVillage = "No data Exist";
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        }
                    }
                };
                viewModel.getvillageDetailsByPincodeLiveData().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
//            Log.e("Villagelist","Null catch");
        }
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getAreaValue();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_OK && resultCode == RESULT_OK) {
//            getAreaValue();
            areaGeo = getIntent().getStringExtra("areaGeo");

            Log.e("Res", areaGeo);
        } else {
            Log.e("Res", "Failed");
        }
    }


    //Current loc
    private void checkSettingsAndStartLocationUpdates() {
        LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build();
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);

        Task<LocationSettingsResponse> locationSettingsResponseTask = settingsClient.checkLocationSettings(request);
        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startLocationUpdate();
            }
        });

        locationSettingsResponseTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                    try {
                        resolvableApiException.startResolutionForResult(PlantationActivity.this, 2000001);
                    } catch (IntentSender.SendIntentException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }

    private void startLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        client.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdate() {
        client.removeLocationUpdates(locationCallback);
        progressDialog.dismiss();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopLocationUpdate();
    }

    public String getLanguageFromLocalDb(String stLanguage, String stWord) {

        try {
            if (viewModel.getLanguageDataVM(stLanguage, stWord)!=null){
                return viewModel.getLanguageDataVM(stLanguage, stWord);
            } else{
                return stWord;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return stWord;
        }

    }
}