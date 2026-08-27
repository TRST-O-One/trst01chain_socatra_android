package com.socatra.excutivechain.activity;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.icu.text.DecimalFormat;
import android.icu.text.DecimalFormatSymbols;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
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
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.android.SphericalUtil;
import com.socatra.excutivechain.utils.AppConstant;
import com.socatra.excutivechain.utils.AppHelper;
import com.socatra.excutivechain.utils.CommonConstants;
import com.socatra.excutivechain.R;
import com.socatra.excutivechain.adapters.CoordinatesAdapter;
import com.socatra.excutivechain.database.entity.PlantationGeoBoundaries;
import com.socatra.excutivechain.databinding.ActivityMapsBinding;
import com.socatra.excutivechain.view_models.AppViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    TextView txtFrstLatLong;
    SupportMapFragment supportMapFragment;


    RecyclerView RVgpsRecords;
    TextView txtArea;

    Button btnRecord;
    CoordinatesAdapter coordinatesAdapter;
    CardView cvStart, cvSave, cvRetake, cvRecord, cvStop, cvUndo, cvPreview;
    Polyline polyline = null;
    Polygon polygon = null;
    List<LatLng> latLngList = new ArrayList<>();
    List<String> lat = new ArrayList<>();
    List<String> lon = new ArrayList<>();
    List<Marker> markerList = new ArrayList<>();
    int time = 0;
    String PlotId = "";
    String id = "";
    Handler handler;
    Runnable run;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    Double areaBefore = 0.0;
    int count = 0;
    PolylineOptions polylineOptions;
    PolygonOptions polygonOptions;
    String dist = "";
    double totalSize;// for size
    MarkerOptions markerOptionsLast = new MarkerOptions();
    Marker markerName;
    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    public AppViewModel viewModel;

    //ppo
    int counter = 0;
    Integer gpsCat = 0;

    String farmerCode;
    FloatingActionButton gps;
    FusedLocationProviderClient client;//Main
    LocationRequest locationRequest;//ash new

    LatLng overLatLng;

    int saveCount = 0;


    Marker myMark;
    int myMarkerCount = 0;


    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            supportMapFragment.getMapAsync(googleMap -> {
                Location l1 = locationResult.getLocations().get(0);
                //initialise latlang
                LatLng latLng = new LatLng(locationResult.getLocations().get(0).getLatitude(), locationResult.getLocations().get(0).getLongitude());
                overLatLng = new LatLng(locationResult.getLocations().get(0).getLatitude(), locationResult.getLocations().get(0).getLongitude());
                if (counter == 0) {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                    counter++;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            stopLocationUpdate();
                        }
                    }, 1 * 4000);//ch1
                }
                //Current location marker
                if (myMarkerCount > 0) {
                    if (myMark != null) {
                        myMark.remove();
                    }
                }

                myMark = googleMap.addMarker(new MarkerOptions()
                        .position(latLng).title("My Location")
                        .icon(BitmapFromVector(getApplicationContext(), R.drawable.baseline_my_location))
                        .visible(true));
                myMarkerCount++;

            });

        }
    };

    private BitmapDescriptor
    BitmapFromVector(Context context, int vectorResId) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);


        client = LocationServices.getFusedLocationProviderClient(this);
        //ash new
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(4000);//1hr3600000
        locationRequest.setFastestInterval(3000);//360000
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        //Todo Intent data

        PlotId = getIntent().getStringExtra("PlotId");
        farmerCode = getIntent().getStringExtra("FarmerCode");
        id = getIntent().getStringExtra("id");
        gpsCat = getIntent().getIntExtra("gpsCat", 0);
        totalSize = Double.parseDouble(getIntent().getStringExtra("ProvideSize"));

        initializeUI();
        initializeValues();
        configureDagger();
        configureViewModel();
        checkSettingsAndStartLocationUpdates();//ash new
    }

    //ash new
    private void checkSettingsAndStartLocationUpdates() {
        LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build();
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);

        Task<LocationSettingsResponse> locationSettingsResponseTask = settingsClient.checkLocationSettings(request);
        locationSettingsResponseTask.addOnSuccessListener(locationSettingsResponse -> startLocationUpdate());

        locationSettingsResponseTask.addOnFailureListener(e -> {
            if (e instanceof ResolvableApiException) {
                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                try {
                    resolvableApiException.startResolutionForResult(MapsActivity.this, 2000001);
                } catch (IntentSender.SendIntentException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    private void startLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        client.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdate() {
        client.removeLocationUpdates(locationCallback);

    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdate();//ch1
        Log.e("AshOnstopMap", "On stop called");
    }

    private void setRecyclerView() {
        coordinatesAdapter = new CoordinatesAdapter(MapsActivity.this, latLngList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MapsActivity.this, RecyclerView.VERTICAL, false);
        RVgpsRecords.setLayoutManager(linearLayoutManager);
        RVgpsRecords.setItemAnimator(new DefaultItemAnimator());
        RVgpsRecords.setNestedScrollingEnabled(false);
        RVgpsRecords.setAdapter(coordinatesAdapter);
    }

    private void initializeUI() {
        txtArea = findViewById(R.id.txtArea);
        cvRecord = findViewById(R.id.cvRecord);
        btnRecord = findViewById(R.id.btnRecord);
        cvRetake = findViewById(R.id.cvRetake);
        RVgpsRecords = findViewById(R.id.RVgpsRecords);
        cvSave = findViewById(R.id.cvSave);
        cvPreview = findViewById(R.id.cvPreview);//preview
        txtFrstLatLong = findViewById(R.id.txtFrstLatLong);
        cvStop = findViewById(R.id.cvStop);
        gps = findViewById(R.id.gps);
        cvStart = findViewById(R.id.cvStart);
        cvUndo = findViewById(R.id.cvUndo);


    }


    private void initializeValues() {
        setRecyclerView();

        txtArea.setText("00.00");
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRecyclerView();
            }
        });

        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
// Check permission
                if (ActivityCompat.checkSelfPermission(MapsActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    checkSettingsAndStartLocationUpdates();//ash new
                } else {
                    ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }

            }
        });

        cvRetake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (polyline != null) polyline.remove();

                for (Marker marker : markerList) marker.remove();
                latLngList.clear();
                markerList.clear();
                lat.clear();
                lon.clear();

                if (polyline != null) {
                    polyline.remove();
                    polylineOptions.visible(false);
                }
                if (polygon != null) {
                    polygon.remove();
                }
                //Txtdistance.setText("Distance:"+"");
                txtArea.setText("00.00");

                markerList.clear();

                mMap.clear();

                latLngList = new ArrayList<>();

                coordinatesAdapter = new CoordinatesAdapter(MapsActivity.this, latLngList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MapsActivity.this, RecyclerView.VERTICAL, false);
                RVgpsRecords.setLayoutManager(linearLayoutManager);
                RVgpsRecords.setItemAnimator(new DefaultItemAnimator());
                RVgpsRecords.setNestedScrollingEnabled(true);
                RVgpsRecords.setAdapter(coordinatesAdapter);

                checkSettingsAndStartLocationUpdates();//Current location marker

            }
        });

        cvUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (polygon != null) {//for polygon
                    polygon.remove();
                }

                if (latLngList.size() >= 2) {

                    List<LatLng> newList = new ArrayList<>();
                    newList = latLngList;
                    if (markerName != null) {
                        Log.e("markerName", String.valueOf(markerName.getPosition()));
                    } else {
                        Log.e("markerName", "null");
                    }

                    markerName = markerList.get(newList.size() - 1);
                    markerName.setVisible(false);
                    markerList.remove(markerList.size() - 1);
                    latLngList.remove(latLngList.size() - 1);
                    markerName = markerList.get(markerList.size() - 1);
                    if (markerList.size() >= 1) {
                        if (polyline != null) polyline.remove();
                        polylineOptions = new PolylineOptions()
                                .addAll(latLngList)
                                .clickable(true);
                        polyline = mMap.addPolyline(polylineOptions);
                        polyline.setColor(getApplicationContext().getResources().getColor(R.color.teal_200));
                        double Area = SphericalUtil.computeArea(latLngList);
                        areaBefore = Area;

                        count = count + 1;
                    }

                    coordinatesAdapter = new CoordinatesAdapter(MapsActivity.this, latLngList);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MapsActivity.this, RecyclerView.VERTICAL, false);
                    RVgpsRecords.setLayoutManager(linearLayoutManager);
                    RVgpsRecords.setItemAnimator(new DefaultItemAnimator());
                    RVgpsRecords.setNestedScrollingEnabled(true);
                    RVgpsRecords.setAdapter(coordinatesAdapter);
                } else {

                    if (polyline != null) polyline.remove();

                    for (Marker marker : markerList) marker.remove();
                    latLngList.clear();
                    markerList.clear();
                    lat.clear();
                    lon.clear();

                    if (polyline != null) {
                        polyline.remove();
                        polylineOptions.visible(false);
                    }
                    if (polygon != null) {
                        polygon.remove();
                    }
                    txtArea.setText("00.00");

                    markerList.clear();

                    mMap.clear();

                    checkSettingsAndStartLocationUpdates();//Current location marker


                    latLngList = new ArrayList<>();

                    coordinatesAdapter = new CoordinatesAdapter(MapsActivity.this, latLngList);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MapsActivity.this, RecyclerView.VERTICAL, false);
                    RVgpsRecords.setLayoutManager(linearLayoutManager);
                    RVgpsRecords.setItemAnimator(new DefaultItemAnimator());
                    RVgpsRecords.setNestedScrollingEnabled(true);
                    RVgpsRecords.setAdapter(coordinatesAdapter);
                }

            }
        });

        //Preview
        cvPreview.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                try {
                    if (!latLngList.isEmpty()) {
                        polygonOptions = new PolygonOptions()
                                .addAll(latLngList)
                                .clickable(true);
                        polygon = mMap.addPolygon(polygonOptions);
                        polygon.setFillColor(getApplicationContext().getResources().getColor(R.color.tab_bg_color));
                    }

                } catch (Exception ex) {
                    Toast.makeText(MapsActivity.this, "test bug", Toast.LENGTH_SHORT).show();
                }


                if (markerList.size() > 2) {
                    List<LatLng> latLngLists = new ArrayList<>();
                    latLngLists.add(latLngList.get(0));
                    latLngLists.add(latLngList.get(latLngList.size() - 1));
                    //create polylineoption
                    polylineOptions = new PolylineOptions()
                            .addAll(latLngLists)
                            .clickable(true);
                    polyline = mMap.addPolyline(polylineOptions);
                    polyline.setColor(getApplicationContext().getResources().getColor(R.color.teal_200));
                    double distance = SphericalUtil.computeArea(latLngList);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        DecimalFormatSymbols symbols = new DecimalFormatSymbols();//dec formatter
                        symbols.setDecimalSeparator('.');//dec formatter
                        DecimalFormat df_obj = new DecimalFormat("#.###", symbols);
                        df_obj.format(distance * 0.000247105);
                        dist = String.valueOf(df_obj.format(SphericalUtil.computeArea(latLngList) * 0.000247105 * 0.404686));
                        txtArea.setText(String.valueOf(df_obj.format(distance * 0.000247105 * 0.404686)));
                    }
                }
            }
        });

        //Save button
        cvSave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (saveCount == 0) {
                    try {
                        if (!latLngList.isEmpty()) {
                            polygonOptions = new PolygonOptions()
                                    .addAll(latLngList)
                                    .clickable(true);
                            polygon = mMap.addPolygon(polygonOptions);
                            polygon.setFillColor(getApplicationContext().getResources().getColor(R.color.tab_bg_color));
                        }

                    } catch (Exception ex) {
                        Toast.makeText(MapsActivity.this, "test bug", Toast.LENGTH_SHORT).show();
                    }


                    if (markerList.size() > 2) {
                        List<LatLng> latLngLists = new ArrayList<>();
                        latLngLists.add(latLngList.get(0));
                        latLngLists.add(latLngList.get(latLngList.size() - 1));
                        //create polylineoption
                        polylineOptions = new PolylineOptions()
                                .addAll(latLngLists)
                                .clickable(true);
                        polyline = mMap.addPolyline(polylineOptions);
                        polyline.setColor(getApplicationContext().getResources().getColor(R.color.teal_200));
                        double distance = SphericalUtil.computeArea(latLngList);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            DecimalFormatSymbols symbols = new DecimalFormatSymbols();//dec formatter
                            symbols.setDecimalSeparator('.');//dec formatter
                            DecimalFormat df_obj = new DecimalFormat("#.###", symbols);
                            df_obj.format(distance * 0.000247105);
                            dist = String.valueOf(df_obj.format(SphericalUtil.computeArea(latLngList) * 0.000247105 * 0.404686));
                            txtArea.setText(String.valueOf(df_obj.format(distance * 0.000247105 * 0.404686)));
                        }

                    }

                    //Insert
                    if (Double.parseDouble(dist) <= totalSize) {
                        if (txtArea.getText().toString().length() > 0 && markerList.size() > 2) {
                            saveCount++;
                            for (int i = 0; i < latLngList.size(); i++) {


                                String dateTime = getCurrentDateTime(AppConstant.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);


                                //Todo:Plantation Geo

                                PlantationGeoBoundaries geoBoundary = new PlantationGeoBoundaries();
                                geoBoundary.setPlotCode(PlotId);
                                geoBoundary.setFarmerCode(farmerCode);
                                geoBoundary.setLatitude(latLngList.get(i).latitude);
                                geoBoundary.setLongitude(latLngList.get(i).longitude);
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
                                viewModel.updatePlotDetailListTableSyncAndPlotArea1(false, "0", Double.parseDouble(dist), PlotId);
                                if (i == latLngList.size() - 1) {
                                    Toast.makeText(MapsActivity.this, "Geobounds details are saved successfully", Toast.LENGTH_SHORT).show();

                                    new Handler().postDelayed(new Runnable() {

                                        @Override
                                        public void run() {
                                            Intent intent = new Intent();
                                            intent.putExtra("areaGeo", dist);
                                            setResult(RESULT_OK, intent);
                                            finish();
                                        }

                                    }, 1 * 1000);
                                }
                            }
                        } else {
                            Toast.makeText(MapsActivity.this, "Please mark at-least 3 boundaries", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MapsActivity.this, "Area must be less than provided area!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("MapActSavebtn:", String.valueOf(saveCount));
                }
            }
        });

        cvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler = new Handler();
                run = new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        handler.postDelayed(this, 10000);
                    }
                };
                handler.post(run);
                cvStop.setVisibility(View.VISIBLE);
                cvStart.setVisibility(View.GONE);

            }

        });

        cvStop.setOnClickListener(new View.OnClickListener() {//issue
            @Override
            public void onClick(View v) {

                handler.removeCallbacks(run);
                cvStop.setVisibility(View.GONE);
                cvStart.setVisibility(View.VISIBLE);
            }
        });

    }

    public String getCurrentDateTime(String strDateFormat) {
        String strCurrDate = null;
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(strDateFormat);
            Calendar c1 = Calendar.getInstance(); // today
            strCurrDate = sdf.format(c1.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strCurrDate;

    }
    private void configureDagger() {
        AndroidInjection.inject(this);

    }

    private void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkSettingsAndStartLocationUpdates();//ash new
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setIndoorEnabled(true);


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                //creating markerOptions
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("pinnedlocation");
                markerOptions.snippet(latLng.latitude + ":" + latLng.longitude + ":");
                //to clear marker

                //creater Marker
                markerOptionsLast = markerOptions;
                Marker marker = mMap.addMarker(markerOptions);
                //add latlag and markers
                latLngList.add(latLng);
                lat.add(String.valueOf(latLng.latitude));
                lon.add(String.valueOf(latLng.longitude));
                markerList.add(marker);
                //for now hide it
                if (markerList.size() > 1) {
                    if (polyline != null) polyline.remove();
                    //create polylineoption
                    polylineOptions = new PolylineOptions()
                            .addAll(latLngList)
                            .clickable(true);
                    polyline = mMap.addPolyline(polylineOptions);
                    polyline.setColor(getApplicationContext().getResources().getColor(R.color.teal_200));
                    double Area = SphericalUtil.computeArea(latLngList);
                    areaBefore = Area;
                    count = count + 1;
                }


                coordinatesAdapter = new CoordinatesAdapter(MapsActivity.this, latLngList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MapsActivity.this, RecyclerView.VERTICAL, false);
                RVgpsRecords.setLayoutManager(linearLayoutManager);
                RVgpsRecords.setItemAnimator(new DefaultItemAnimator());
                RVgpsRecords.setNestedScrollingEnabled(true);
                RVgpsRecords.setAdapter(coordinatesAdapter);
            }
        });
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

}