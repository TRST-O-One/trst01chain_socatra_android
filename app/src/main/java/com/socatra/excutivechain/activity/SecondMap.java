package com.socatra.excutivechain.activity;

import static com.socatra.excutivechain.App.appHelper;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.SphericalUtil;
import com.socatra.excutivechain.AppConstant;
import com.socatra.excutivechain.R;
import com.socatra.excutivechain.adapters.CoordinatesAdapter;
import com.socatra.excutivechain.database.entity.PlantationGeoBoundaries;
import com.socatra.excutivechain.view_models.AppViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class SecondMap extends FragmentActivity implements OnMapReadyCallback {

    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    public AppViewModel viewModel;

    String TAG = "SecondMapTag";
    private GoogleMap mMap;

    SupportMapFragment supportMapFragment;

//    private ActivitySecondMapBinding binding;

    Button startStopButton, saveBtnSmap, resetSmap, recordBtnSmap;

    TextView areaSmap;

    double totalArea=0.0;

    RecyclerView recordsRecyclerViewSmap;

    List<LatLng> recordedBoundries = new ArrayList<>();

    List<LatLng> totalBoundries = new ArrayList<>();
    CoordinatesAdapter coordinatesAdapter;


    //Location
    FusedLocationProviderClient client;
    LocationRequest locationRequest;

    LatLng myLocLatLng;
    int myFirstLatLngCount = 0;

    Marker myMarker;

    int myMarkerCount = 0;

    int startStopStatus = 0;

    Marker walkPathMarker;

    LatLng recordLatLang;

    String farmerCode,plotId,id;

    int gpsCat=0;

    double totalSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_map);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        plotId = getIntent().getStringExtra("PlotId");
        farmerCode = getIntent().getStringExtra("FarmerCode");
        id = getIntent().getStringExtra("id");
        totalSize=Double.parseDouble(getIntent().getStringExtra("ProvideSize"));
        gpsCat = getIntent().getIntExtra("gpsCat", 0);

        initializeUI();
        initializeValues();
        configureDagger();
        configureViewModel();

        client = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000);//1hr3600000/2
        locationRequest.setFastestInterval(700);//360000/1
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        checkSettingsAndStartLocationUpdates();

    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);

            if (locationResult != null) {
                if (myFirstLatLngCount == 0) {
                    //initial latlng for my loc
                    myLocLatLng = new LatLng(locationResult.getLocations().get(0).getLatitude(), locationResult.getLocations().get(0).getLongitude());

                    Log.e(TAG, "firstloc:" + String.valueOf(myLocLatLng.latitude) + "," + String.valueOf(myLocLatLng.longitude));

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            stopLocationUpdate();
                            Log.e(TAG, "first stop for loc");
                            myFirstLatLngCount++;
                        }
                    }, 1 * 1000);//2sec/1sec

                    if (myMarkerCount > 0) {
                        if (myMarker != null) {
                            myMarker.remove();
                        }
                    }

                    myMarker = mMap.addMarker(new MarkerOptions()
                            .position(myLocLatLng).title("My Location")
                            .icon(BitmapFromVector(getApplicationContext(), R.drawable.baseline_my_location))
                            .visible(true));
                    myMarkerCount++;

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocLatLng, 19));

                } else {
                    if (locationResult == null) {
                        return;
                    } else {
                        for (Location location : locationResult.getLocations()) {
                            LatLng crLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            recordLatLang = crLatLng;//curr lat record
                            totalBoundries.add(crLatLng);//new LatLng(location.getLatitude(),location.getLongitude())
                            Log.e(TAG, "Cont loc:" + String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()));
                            walkPathMarker = mMap.addMarker(new MarkerOptions()
                                    .position(crLatLng)//new LatLng(location.getLatitude(),location.getLongitude())
                                    .icon(BitmapFromVector(getApplicationContext(), R.drawable.baseline_circle_path))
                                    .title(String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()))
                                    .visible(true));

                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(crLatLng, 20));
                        }
                    }

                }
            } else {
                Log.e(TAG, "Loc Null");
            }

        }
    };

    private void initializeUI() {
        startStopButton = findViewById(R.id.startBtnSmap);
        saveBtnSmap = findViewById(R.id.saveBtnSmap);
        resetSmap = findViewById(R.id.resetSmap);
        recordBtnSmap = findViewById(R.id.recordBtnSmap);
        areaSmap = findViewById(R.id.areaSmap);
        recordsRecyclerViewSmap = findViewById(R.id.gpsRecordsSmap);
    }

    private void initializeValues() {
        //Todo : Area text init
        areaSmap.setText(String.valueOf(totalArea));

        //Todo : RecyclerView and Adapter init
        coordinatesAdapter = new CoordinatesAdapter(SecondMap.this, recordedBoundries);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SecondMap.this, RecyclerView.VERTICAL, false);
        recordsRecyclerViewSmap.setLayoutManager(linearLayoutManager);
        recordsRecyclerViewSmap.setItemAnimator(new DefaultItemAnimator());
        recordsRecyclerViewSmap.setNestedScrollingEnabled(true);
        recordsRecyclerViewSmap.hasFixedSize();
        recordsRecyclerViewSmap.setAdapter(coordinatesAdapter);


        //Todo : Start/Stop btn
        startStopButton.setOnClickListener(v -> {
            if (startStopStatus == 0) {
                startStopButton.setText("Stop");
                checkSettingsAndStartLocationUpdates();
                startStopStatus = 1;
                startStopButton.setBackgroundColor(Color.RED);
            } else {
                startStopButton.setText("Start");
                stopLocationUpdate();
                calculateAreaGeo();
                startStopStatus = 0;
                startStopButton.setBackgroundResource(R.color.lyt_green);
            }
        });

        //Todo : Save btn
        saveBtnSmap.setOnClickListener(v -> {
            if (totalBoundries.size() > 0) {//recordedBoundries
                if (startStopStatus == 0) {
                    Log.e(TAG, "totalList:" + totalBoundries.toString());
                    if (recordedBoundries.size()>0){
                        Log.e(TAG, "recordedList:" + recordedBoundries.toString());
                        saveGeoboundariesToDB();
                    } else {
                        recordedBoundries=totalBoundries;
                        Log.e(TAG, "recordedList:" + recordedBoundries.toString());
                        saveGeoboundariesToDB();

                    }
                } else {
                    Toast.makeText(this, "Please stop the progress first!!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please record the coordinates first!!", Toast.LENGTH_SHORT).show();
            }
        });

        //Todo : Reset btn
        resetSmap.setOnClickListener(v -> {
            if (startStopStatus == 0) {
                recordedBoundries.clear();
                totalBoundries.clear();
                if (walkPathMarker != null) {
                    walkPathMarker.remove();
                }
                totalArea=0.0;
                areaSmap.setText(String.valueOf(totalArea));
                mMap.clear();
                myFirstLatLngCount = 0;
                checkSettingsAndStartLocationUpdates();
                coordinatesAdapter = new CoordinatesAdapter(SecondMap.this, recordedBoundries);
                recordsRecyclerViewSmap.setAdapter(coordinatesAdapter);
            } else {
                Toast.makeText(this, "Please stop the progress first!!", Toast.LENGTH_SHORT).show();
            }

//            latLngList.clear();
        });

        //Todo : Record btn
        recordBtnSmap.setOnClickListener(v -> {
            if (startStopStatus == 1) {
                if (recordLatLang != null) {
                    Log.e(TAG, "recorded crrLatLng:" + recordLatLang.toString());

                    MarkerOptions markerOptionsRecorded = new MarkerOptions();
                    markerOptionsRecorded.position(recordLatLang);
                    markerOptionsRecorded.title(String.valueOf(recordLatLang.latitude) + "," + String.valueOf(recordLatLang.longitude));
                    mMap.addMarker(markerOptionsRecorded);

                    recordedBoundries.add(recordLatLang);//Add in arrayList
                    coordinatesAdapter = new CoordinatesAdapter(SecondMap.this, recordedBoundries);
                    Log.e(TAG, "recorded boundaries:" + recordedBoundries.toString());
                    recordsRecyclerViewSmap.setAdapter(coordinatesAdapter);
//                    coordinatesAdapter.notifyDataSetChanged();
                }
            } else {
                Toast.makeText(this, "Please start progress!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void calculateAreaGeo() {
        //Todo :calculate area
        if (totalBoundries.size()>1){
            totalBoundries.add(totalBoundries.get(0));
            totalArea = SphericalUtil.computeArea(totalBoundries) / 10000;// for hectares, for Acres( / 4046.86)
            String decimalForm = String.format("%.15f", totalArea);

            areaSmap.setText(decimalForm);
        }
    }

    private void saveGeoboundariesToDB() {
        //Todo : Save to DB
        if(Double.valueOf(areaSmap.getText().toString())<=totalSize+0.1) {
            if (recordedBoundries.size() > 2) {
                for (int i = 0; i < recordedBoundries.size(); i++) {


                    String dateTime = appHelper.getCurrentDateTime(AppConstant.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);


                    //Todo:Plantation Geo

                    PlantationGeoBoundaries geoBoundary = new PlantationGeoBoundaries();
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
                    viewModel.updatePlotDetailListTableSyncAndPlotArea1(false, "0", Double.valueOf(areaSmap.getText().toString()), plotId);
                    if (i == recordedBoundries.size() - 1) {
                        Toast.makeText(SecondMap.this, "Geo-boundaries details are saved successfully",
                                Toast.LENGTH_SHORT).show();


                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                Intent intent = new Intent();
                                intent.putExtra("areaField", totalArea);
                                setResult(RESULT_OK, intent);
                                finish();
                            }

                        }, 1 * 500);

                    }

                }
            } else {
                Toast.makeText(SecondMap.this, "Please mark at-least 3 boundaries", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(SecondMap.this, "Area must be less than provided area!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopLocationUpdate();
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    private void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setIndoorEnabled(true);

        // Add a marker in Sydney and move the camera
       /* LatLng hydOff = new LatLng(17.457776989405062, 78.36810104548931);
        mMap.addMarker(new MarkerOptions().position(hydOff).title("Marker here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(hydOff));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hydOff, 21));*/


    }


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
                        resolvableApiException.startResolutionForResult(SecondMap.this, 2000001);
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
    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(
                context, vectorResId);

        // below line is use to set bounds to our vector
        // drawable.
        vectorDrawable.setBounds(
                0, 0, vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(
                vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our
        // bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    //Todo : insert to DB
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

}