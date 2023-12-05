package com.socatra.excutivechain.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.socatra.excutivechain.AppConstant;
import com.socatra.excutivechain.BaseActivity;
import com.socatra.excutivechain.CommonUtils;
import com.socatra.excutivechain.R;
import com.socatra.excutivechain.database.entity.PlantationGeoBoundaries;
import com.socatra.excutivechain.view_models.AppViewModel;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import letsrock.areaviewlib.AreaView;
import letsrock.areaviewlib.GPSCoordinate;

public class FieldCalculatorActivity extends BaseActivity implements HasSupportFragmentInjector {

    private static final String LOG_TAG = FieldCalculatorActivity.class.getName();
    private static final int PERMISSION_REQUEST_CODE = 1;
    public static List<GPSCoordinate> firstFourCoordinates = new ArrayList<>();
    public static List<GPSCoordinate> recordedBoundries = new ArrayList<>();
    public static List<GPSCoordinate> totalBoundries = new ArrayList<>();
    private AreaView measureView;
    private Button startStopButton, saveBtn, resetBtn;
    private Context c;
    private LocationManager locationManager;

    private LinkedHashMap<String, String> latLongMap = new LinkedHashMap<>();
    private Button recordBtn;
    private RecyclerView recordsList;

//    List<LatLng> latLngs;//Todo test

    //Todo test New
    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    public AppViewModel viewModel;
    String plotId = "";
    String id = "";
    Integer gpsCat = 0;
    String farmerCode;

    double totalSize=0.0;



    private BroadcastReceiver locationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null && intent.getExtras() != null) {
                latLongMap.put(String.valueOf(intent.getExtras().getDouble("latitude")), String.valueOf(intent.getExtras().getDouble("longitude")));
                if (null != firstFourCoordinates && firstFourCoordinates.size() <= 4) {
                    firstFourCoordinates.add(new GPSCoordinate(intent.getExtras().getDouble("latitude"), intent.getExtras().getDouble("longitude"), 0));
                }
                totalBoundries.add(new GPSCoordinate(intent.getExtras().getDouble("latitude"), intent.getExtras().getDouble("longitude"), 0));
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        c = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !isLocationPermissionGranted()) {
            Log.v(LOG_TAG, "Location Permissions Not Granted");
            requestLocationPermissions();
        } else {

            totalBoundries.clear();
            recordedBoundries.clear();
            firstFourCoordinates.clear();
            initViews();
            configureDagger();
            configureViewModel();

            plotId = getIntent().getStringExtra("PlotId");
            farmerCode = getIntent().getStringExtra("FarmerCode");
            id = getIntent().getStringExtra("id");
            totalSize=Double.parseDouble(getIntent().getStringExtra("ProvideSize"));
            gpsCat = getIntent().getIntExtra("gpsCat", 0);

            Log.e("fieldLog",plotId+","+farmerCode+","+id+","+gpsCat);
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(locationReceiver, new IntentFilter("location_receiver"));

    }


    public void initViews() {

        setContentView(R.layout.activity_field_calculator);
        // Typeface myFont = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold.ttf");
        totalBoundries.clear();
        recordedBoundries.clear();
        firstFourCoordinates.clear();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        recordBtn =findViewById(R.id.recordBtn);
        // recordBtn.setTypeface(myFont);
        saveBtn = findViewById(R.id.saveBtn);
        resetBtn = findViewById(R.id.reset);
        recordsList = findViewById(R.id.gpsRecords);

        recordsList.setLayoutManager(new LinearLayoutManager(FieldCalculatorActivity.this, LinearLayoutManager.VERTICAL, false));

        recordBtn.setOnClickListener(view -> {

            if (measureView.isRunning()) {

                GPSCoordinate pointsToRecord;
//                LatLng latLng;//Todo test
                if (null != recordedBoundries && recordedBoundries.size() > 0) {
                    double distance = CommonUtils.distance(recordedBoundries.get(recordedBoundries.size() - 1).latitude,
                            recordedBoundries.get(recordedBoundries.size() - 1).longitude, AreaView.latitude, AreaView.longitude, 'M');
                    pointsToRecord = new GPSCoordinate(AreaView.latitude, AreaView.longitude, distance);
//                    latLng=new LatLng(pointsToRecord.latitude,pointsToRecord.longitude);//Todo test
                } else {
                    pointsToRecord = new GPSCoordinate(AreaView.latitude, AreaView.longitude, 0.0);
//                    latLng=new LatLng(pointsToRecord.latitude,pointsToRecord.longitude);//Todo test
                }
//                latLngs.add(latLng);//Todo test
                recordedBoundries.add(pointsToRecord);
                recordsList.setAdapter(new RecordedCoordinatesAdapter(FieldCalculatorActivity.this, recordedBoundries));
            }

        });
        resetBtn.setOnClickListener(view -> {
//            latLngs.clear();//Todo test
            measureView.reset();
            measureView.invalidate();
            recordedBoundries.clear();
            totalBoundries.clear();
            firstFourCoordinates.clear();
            recordsList.setAdapter(new RecordedCoordinatesAdapter(FieldCalculatorActivity.this, recordedBoundries));

        });

        saveBtn.setOnClickListener(view -> {
            double measuredArea = Math.round(100 * measureView.getArea()) / (double) 100;
            if (measureView.isRunning()) {
                Toast.makeText(FieldCalculatorActivity.this, "Please stop area measuring and save", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!measureView.isReadyToStart()) {
                Toast.makeText(FieldCalculatorActivity.this, "Gps signal not received", Toast.LENGTH_SHORT).show();
                return;
            }
            if (measuredArea > 0) {
                displayAreaAreaDialog();
            } else {
                Toast.makeText(FieldCalculatorActivity.this, "Area is not measured", Toast.LENGTH_SHORT).show();
            }


            //    saveLatLongData();
        });

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            displayGpsDialog();
        }

        measureView = (AreaView) findViewById(R.id.measureView);
        measureView.setLengthUnits(AreaView.LENGTH_UNITS_KILOMETER);
        measureView.setAreaUnits(AreaView.AREA_UNITS_HECTARE);
        //  measureView.setLoggingMode(AreaView.LOGGING_MODE_MANUAL);
        //  measureView.setPolygon();
        startStopButton = (Button) findViewById(R.id.startBtn);
        startStopButton.setOnClickListener(view -> {

            if (measureView.isReadyToStart()) {
                measureView.start();
                startStopButton.setText("Stop");
                startStopButton.postInvalidate();
            } else if (measureView.isRunning()) {
                measureView.stop();
                startStopButton.setText("Start");
                startStopButton.postInvalidate();
            } else {
                Toast.makeText(FieldCalculatorActivity.this, "Waiting for gps signal", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public boolean isLocationPermissionGranted() {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        return hasFineLocationPermission == PackageManager.PERMISSION_GRANTED
                && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED;
    }

    public void requestLocationPermissions() {
        if (!isLocationPermissionGranted()) {
            String[] perms = new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this, perms, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    initViews();

                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        displayGpsDialog();
                    }

                } else {

                }
                break;
        }
    }

    private void displayGpsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("GPS is turned off ,Please Enable GPS").setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(settingsIntent);
                    }
                });
        builder.show();

    }

    private void displayAreaDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        double measuredArea = Math.round(100 * measureView.getArea()) / (double) 100;
        builder.setMessage("Total field area is : " + measuredArea + " " + measureView.getAreaUnit()).setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                saveLatLongData();
            }
        });
        builder.show();

    }


    private void displayAreaAreaDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        double measuredArea = Math.round(100 * measureView.getArea()) / (double) 100;
//        double diffPercentage = CommonUtils.getPercentage(measuredArea, ConversionLandDetailsFragment.plotEnteredArea);
        double diffPercentage = 0;
        double roundedValue = 0.0;

        try {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();//dec formatter
            symbols.setDecimalSeparator('.');//dec formatter
            DecimalFormat f = new DecimalFormat("##.000000",symbols);
            String formattedValue = f.format(diffPercentage);
            if (!TextUtils.isEmpty(formattedValue)) {
                roundedValue = Double.parseDouble(formattedValue);
            }
        } catch (Exception e) {
            roundedValue = 0;
        }


        String message = "Total field area is : " + measuredArea + " " + measureView.getAreaUnit();

        if (diffPercentage >= 60 && roundedValue != Double.POSITIVE_INFINITY && diffPercentage != Double.NEGATIVE_INFINITY) {
            message = message + "\n Variation between Plot area and Gps area is " + roundedValue + " %";
        }
        builder.setMessage(message).setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                saveLatLongData();
            }
        });
        builder.show();

    }

    public void saveLatLongData() {
//        ProgressBar.showProgressBar(FieldCalculatorActivity.this, "Saving Gps data");
        try {

            if (recordedBoundries.isEmpty()) {
                recordedBoundries.addAll(totalBoundries);
            }

            double measuredArea = Math.round(100 * measureView.getArea()) / (double) 100;

            if(measuredArea<=totalSize){
                //if(Double.parseDouble(dist)<=remArea){
                if (recordedBoundries.size()>2) {
//                    txtFrstLatLong.setText(latLngList.get(0).toString());
                    for (int i = 0; i < recordedBoundries.size(); i++) {


                        String dateTime = appHelper.getCurrentDateTime(AppConstant.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);


                        //Todo:Plantation Geo

                        PlantationGeoBoundaries geoBoundary=new PlantationGeoBoundaries();
                        geoBoundary.setPlotCode(plotId);
                        geoBoundary.setFarmerCode(farmerCode);
                        geoBoundary.setLatitude(recordedBoundries.get(i).latitude);
                        geoBoundary.setLongitude(recordedBoundries.get(i).longitude);
                        geoBoundary.setSeqNo(i);
                        geoBoundary.setPlotCount(gpsCat + 1);
                        geoBoundary.setIsActive("true");
                        geoBoundary.setCreatedByUserId(id);
                        geoBoundary.setUpdatedByUserId(id);
                        geoBoundary.setSync(false);
                        geoBoundary.setServerSync("0");
                        geoBoundary.setCreatedDate(dateTime);
                        geoBoundary.setUpdatedDate(dateTime);


                        insertOrUpdateGeoBoundariesDataToServer(geoBoundary);
                        viewModel.updatePlotDetailListTableSyncAndPlotArea1(false, "0", measuredArea, plotId);
                        if (i == recordedBoundries.size() - 1) {
                            Toast.makeText(FieldCalculatorActivity.this, "Geobounds details are saved successfully", Toast.LENGTH_SHORT).show();
//                                Toast.makeText(MapsActivity.this, "Area " + dist, Toast.LENGTH_SHORT).show();


                            new Handler().postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    Intent intent = new Intent();
                                    intent.putExtra("areaField", measuredArea);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }

                            }, 1 * 500);


//                            finish();
                        }

                    }
                }else {
                    Toast.makeText(FieldCalculatorActivity.this, "Please mark at-least 3 boundaries", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(FieldCalculatorActivity.this, "Area must be less than provided area!!", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.v(LOG_TAG, "@@@@ Error while saving lat longs");
        }

    }

    @Override
    public void onBackPressed() {
        totalBoundries.clear();
        recordedBoundries.clear();
        firstFourCoordinates.clear();
//        Intent intent = new Intent();
//        intent.putExtra("area", 0.0);
//        setResult(RESULT_OK, intent);
//        finish();
        Intent intent = new Intent();
        intent.putExtra("geo_value", "error");
        setResult(2, intent);
        finish();
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return null;
    }

    public void insertOrUpdateGeoBoundariesDataToServer(PlantationGeoBoundaries geoBoundariesTable) {
        try {
            viewModel.insertGeoBoundariesvaluesIntolocalDB(geoBoundariesTable);
            if (viewModel.getGeoBoundariesTableLocalDB() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        PlantationGeoBoundaries customerSurveyTable1 = (PlantationGeoBoundaries) o;
                        viewModel.getGeoBoundariesTableLocalDB().removeObserver(this);
                        if (customerSurveyTable1 != null) {

                        }
                    }
                };
                viewModel.getGeoBoundariesTableLocalDB().observe(this, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public class RecordedCoordinatesAdapter extends RecyclerView.Adapter<RecordedCoordinatesAdapter.MyHolder> {
        private Context mContext;
        private List<GPSCoordinate> gpsCoordinates;


        public RecordedCoordinatesAdapter(Context mContext, List<GPSCoordinate> gpsCoordinates) {
            this.mContext = mContext;
            this.gpsCoordinates = gpsCoordinates;
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View bookingView = inflater.inflate(R.layout.records_list_item, null);
            MyHolder myHolder = new MyHolder(bookingView);
            return myHolder;
        }


        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            holder.latitude.setText("" + gpsCoordinates.get(position).latitude);
            holder.longitude.setText("" + gpsCoordinates.get(position).longitude);
            if (gpsCoordinates != null && gpsCoordinates.size() > 1) {
                holder.distance.setText("" + gpsCoordinates.get(position).altitude + " Meters");

            } else {
                holder.distance.setText("0 " + "Meters");
            }
        }

        @Override
        public int getItemCount() {
            return gpsCoordinates.size();
        }

        public class MyHolder extends RecyclerView.ViewHolder {
            TextView latitude, longitude, distance;

            public MyHolder(View itemView) {
                super(itemView);
                latitude = itemView.findViewById(R.id.latitude);
                longitude = itemView.findViewById(R.id.longitude);
                distance = itemView.findViewById(R.id.distance);
            }
        }
    }

    private void configureDagger() {
        AndroidInjection.inject(this);

    }

    private void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
    }
}