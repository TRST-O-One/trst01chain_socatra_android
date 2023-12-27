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
import com.socatra.excutivechain.AppConstant;
import com.socatra.excutivechain.AppHelper;
import com.socatra.excutivechain.CommonConstants;
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
    private ActivityMapsBinding binding;

    TextView txtFrstLatLong;
    SupportMapFragment supportMapFragment;


    RecyclerView RVgpsRecords;
    TextView Txtdistance, txtArea;

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
    double totalArea;// for area
    double totalSize;// for size
    MarkerOptions markerOptionsLast = new MarkerOptions();
    Marker markerName;
    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    public AppViewModel viewModel;

    private LinkedHashMap<String, String> latLongMap = new LinkedHashMap<>();

    AppHelper appHelper;
    //ppo
    int counter = 0;
    Integer gpsCat = 0;

    String farmerCode;
    FloatingActionButton gps;
    FusedLocationProviderClient client;//Main
    LocationRequest locationRequest;//ash new

    LatLng overLatLng;

    int saveCount=0;


    Marker myMark;
    int myMarkerCount=0;


    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
//            if (locationResult==null){
//                return;
//            }
//            Location l1=locationResult.getLastLocation();
//            Log.e("AshOneRes",l1.toString());
//            for (Location location:locationResult.getLocations()){
//                Log.e("AshContRes",location.toString());
//            }

            if (locationResult != null) {
                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        Location l1 = locationResult.getLocations().get(0);
                        Log.e("AshOneRes", String.valueOf(l1.getLatitude()));
//                        try {
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }

//                        for (Location location:locationResult.getLocations()){//ch1
//                            Log.e("AshContRes", String.valueOf(location.getLatitude()));
//                        }
                        //initialise latlang
                        LatLng latLng = new LatLng(locationResult.getLocations().get(0).getLatitude(), locationResult.getLocations().get(0).getLongitude());
//                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                        overLatLng=new LatLng(locationResult.getLocations().get(0).getLatitude(), locationResult.getLocations().get(0).getLongitude());
                        if (counter == 0) {
                            //test overlay
//                            googleMap.addGroundOverlay(new GroundOverlayOptions()
//                                    .position(overLatLng,1000f,1000f)
//                                    .image(BitmapDescriptorFactory.fromResource(R.drawable.test_over1)));

                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
//                            enableMyLocation(googleMap);
                            counter = counter++;

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    stopLocationUpdate();
                                    Log.e("Ashstopforloc", "stop for loc");
                                }
                            }, 1 * 4000);//ch1
                        }
                        Log.e("testMapOnMapReady", "its here task listener");
//                        MarkerOptions myMarkerOptions = new MarkerOptions();
//                        myMarkerOptions.position(latLng);
//                        myMarkerOptions.title("pinnedlocations");
//                        myMarkerOptions.icon(BitmapFromVector(getApplicationContext(),R.drawable.baseline_my_location));
//                        myMarkerOptions.snippet(latLng.latitude + ":" + latLng.longitude + ":");
//                        myMark =googleMap.addMarker(myMarkerOptions);
//                        googleMap.addMarker(myMarkerOptions);

                        //Current location marker
                        if (myMarkerCount>0) {
                            if (myMark != null) {
                                myMark.remove();
                            }
                        }

                        myMark = googleMap.addMarker(new MarkerOptions()
                                .position(latLng).title("My Location")
                                        .icon(BitmapFromVector(getApplicationContext(), R.drawable.baseline_my_location))
                                .visible(true));
                        myMarkerCount++;
//                        Log.e("AshMarker",myMark.toString());

                    }
                });
//                stopLocationUpdate();//ch1
            }

        }
    };
//    PlotDetailsListTable plotInfo;

    private BitmapDescriptor
    BitmapFromVector(Context context, int vectorResId)
    {
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

    //    SupportMapFragment supportMapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
//        binding = ActivityMapsBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());

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

//        PlotId= getIntent().getStringExtra("plot_code");

        //Todo Intent data

        PlotId = getIntent().getStringExtra("PlotId");
        farmerCode = getIntent().getStringExtra("FarmerCode");
        id = getIntent().getStringExtra("id");
        gpsCat = getIntent().getIntExtra("gpsCat", 0);
        totalSize=Double.parseDouble(getIntent().getStringExtra("ProvideSize"));


        Log.e("mapActSize",String.valueOf(totalSize));
//        Toast.makeText(this, PlotId, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "PlotId3" + CommonConstants.PLOT_CODE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            DecimalFormat df = new DecimalFormat("0.00");
        }
        initializeUI();
        initializeValues();
        configureDagger();
        configureViewModel();
//        getCurrentLocation();
        checkSettingsAndStartLocationUpdates();//ash new
    }

    private void enableMyLocation(GoogleMap googleMap) {
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
        googleMap.setMyLocationEnabled(true);
    }

    //ash new
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
                        resolvableApiException.startResolutionForResult(MapsActivity.this, 2000001);
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

    private void stopLocationUpdate(){
        client.removeLocationUpdates(locationCallback);

    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdate();//ch1
        Log.e("AshOnstopMap","On stop called");
    }

    private void setRecyclerView() {
        coordinatesAdapter = new CoordinatesAdapter(MapsActivity.this,latLngList);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(MapsActivity.this,RecyclerView.VERTICAL,false);
        RVgpsRecords.setLayoutManager(linearLayoutManager);
        RVgpsRecords.setItemAnimator(new DefaultItemAnimator());
        RVgpsRecords.setNestedScrollingEnabled(false);
        RVgpsRecords.setAdapter(coordinatesAdapter);
    }

    private void initializeUI() {
//        ImgBack=findViewById(R.id.ImgBack);
        txtArea=findViewById(R.id.txtArea);
        cvRecord=findViewById(R.id.cvRecord);
        btnRecord=findViewById(R.id.btnRecord);
        cvRetake=findViewById(R.id.cvRetake);
        RVgpsRecords=findViewById(R.id.RVgpsRecords);
        cvSave=findViewById(R.id.cvSave);
        cvPreview=findViewById(R.id.cvPreview);//preview
        txtFrstLatLong=findViewById(R.id.txtFrstLatLong);
        cvStop=findViewById(R.id.cvStop);
        gps=findViewById(R.id.gps);
        cvStart=findViewById(R.id.cvStart);
        cvUndo=findViewById(R.id.cvUndo);


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
//                    getCurrentLocation();//Todo : need to comment 1
//                    stopLocationUpdate();//ch1
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

                if(polyline!=null) {
                    polyline.remove();
                    polylineOptions.visible(false);
                }
                if(polygon!=null) {
                    polygon.remove();
                }
                //Txtdistance.setText("Distance:"+"");
                txtArea.setText("00.00");

                markerList.clear();

                mMap.clear();

                latLngList = new ArrayList<>();

                coordinatesAdapter = new CoordinatesAdapter(MapsActivity.this,latLngList);
                LinearLayoutManager linearLayoutManager= new LinearLayoutManager(MapsActivity.this,RecyclerView.VERTICAL,false);
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

                if(polygon!=null) {//for polygon
                    polygon.remove();
                }

                if(latLngList.size()>=2){

                    List<LatLng> newList= new ArrayList<>();
                    newList= latLngList;
                    if(markerName!=null){
                        Log.e("markerName", String.valueOf(markerName.getPosition()));
                    } else {
                        Log.e("markerName", "null");
                    }

                    markerName = markerList.get(newList.size()-1);
                    markerName.setVisible(false);
                    markerList.remove(markerList.size()-1);
                    latLngList.remove(latLngList.size()-1);
                    markerName = markerList.get(markerList.size()-1);
                    Log.e("markerName", String.valueOf(markerName.getPosition()));
                    Log.e("markerName", String.valueOf(markerList.size()));
                    Log.e("markerName", String.valueOf(latLngList.size()));

                    if (markerList.size()>=1){
                        if (polyline!=null) polyline.remove();
                        //create polylineoption
//                    PolylineOptions polylineOptions= new PolylineOptions()
                        polylineOptions= new PolylineOptions()
                                .addAll(latLngList)
                                .clickable(true);
                        polyline=mMap.addPolyline(polylineOptions);
                        polyline.setColor(getApplicationContext().getResources().getColor(R.color.teal_200));
                        double distance = SphericalUtil.computeLength(latLngList);
                        double Area = SphericalUtil.computeArea(latLngList);
                        //           Txtdistance.setText("Distance:" + String.valueOf(distance) + "meters");
//                    if(areaBefore<Area){
                        areaBefore = Area;

                        count = count+1;
//                    coordinatesAdapter.notifyDataSetChanged();

                        //

                    }

                    coordinatesAdapter = new CoordinatesAdapter(MapsActivity.this,latLngList);
                    LinearLayoutManager linearLayoutManager= new LinearLayoutManager(MapsActivity.this,RecyclerView.VERTICAL,false);
                    RVgpsRecords.setLayoutManager(linearLayoutManager);
                    RVgpsRecords.setItemAnimator(new DefaultItemAnimator());
                    RVgpsRecords.setNestedScrollingEnabled(true);
                    RVgpsRecords.setAdapter(coordinatesAdapter);



                    Log.e("testMapOnMapReady","its here");



                } else {

                    if (polyline != null) polyline.remove();

                    for (Marker marker : markerList) marker.remove();
                    latLngList.clear();
                    markerList.clear();
                    lat.clear();
                    lon.clear();

                    if(polyline!=null) {
                        polyline.remove();
                        polylineOptions.visible(false);
                    }
                    if(polygon!=null) {
                        polygon.remove();
                    }
                    //Txtdistance.setText("Distance:"+"");
                    txtArea.setText("00.00");

                    markerList.clear();

                    mMap.clear();

                    checkSettingsAndStartLocationUpdates();//Current location marker


                    latLngList= new ArrayList<>();

                    coordinatesAdapter = new CoordinatesAdapter(MapsActivity.this,latLngList);
                    LinearLayoutManager linearLayoutManager= new LinearLayoutManager(MapsActivity.this,RecyclerView.VERTICAL,false);
                    RVgpsRecords.setLayoutManager(linearLayoutManager);
                    RVgpsRecords.setItemAnimator(new DefaultItemAnimator());
                    RVgpsRecords.setNestedScrollingEnabled(true);
                    RVgpsRecords.setAdapter(coordinatesAdapter);
                }



            }
        });




        Log.d(TAG, "PlotId31" +PlotId);

        //Preview
        cvPreview.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                try {
                    if(!latLngList.isEmpty()){
//                        PolygonOptions polygonOptions= new PolygonOptions()
                        polygonOptions= new PolygonOptions()
                                .addAll(latLngList)
                                .clickable(true);
                        polygon=mMap.addPolygon(polygonOptions);
                        polygon.setFillColor(getApplicationContext().getResources().getColor(R.color.tab_bg_color));
                    }

                } catch (Exception ex){
                    Toast.makeText(MapsActivity.this, "test bug", Toast.LENGTH_SHORT).show();
                }


                if (markerList.size() > 2) {
                    List<LatLng> latLngLists = new ArrayList<>();
                    latLngLists.add(latLngList.get(0));
                    latLngLists.add(latLngList.get(latLngList.size() - 1));
                    //create polylineoption
//                    PolylineOptions polylineOptions = new PolylineOptions()
                    polylineOptions = new PolylineOptions()
                            .addAll(latLngLists)
                            .clickable(true);
                    polyline = mMap.addPolyline(polylineOptions);
                    polyline.setColor(getApplicationContext().getResources().getColor(R.color.teal_200));
                    double distance = SphericalUtil.computeArea(latLngList);
//                    dist = String.valueOf(SphericalUtil.computeArea(latLngList)*0.000247105);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        DecimalFormatSymbols symbols = new DecimalFormatSymbols();//dec formatter
                        symbols.setDecimalSeparator('.');//dec formatter
                        DecimalFormat df_obj = new DecimalFormat("#.###",symbols);
                        df_obj.format(distance*0.000247105);
                        dist = String.valueOf(df_obj.format(SphericalUtil.computeArea(latLngList)*0.000247105*0.404686));
                        txtArea.setText(String.valueOf(df_obj.format(distance*0.000247105*0.404686)));
                    }



                }
            }
        });

        //Save button
        cvSave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (saveCount==0) {
                    try {
                        if (!latLngList.isEmpty()) {
//                        PolygonOptions polygonOptions= new PolygonOptions()
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
//                    PolylineOptions polylineOptions = new PolylineOptions()
                        polylineOptions = new PolylineOptions()
                                .addAll(latLngLists)
                                .clickable(true);
                        polyline = mMap.addPolyline(polylineOptions);
                        polyline.setColor(getApplicationContext().getResources().getColor(R.color.teal_200));
                        double distance = SphericalUtil.computeArea(latLngList);
//                    dist = String.valueOf(SphericalUtil.computeArea(latLngList)*0.000247105);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            DecimalFormatSymbols symbols = new DecimalFormatSymbols();//dec formatter
                            symbols.setDecimalSeparator('.');//dec formatter
                            DecimalFormat df_obj = new DecimalFormat("#.###", symbols);
                            df_obj.format(distance * 0.000247105);
                            dist = String.valueOf(df_obj.format(SphericalUtil.computeArea(latLngList) * 0.000247105 * 0.404686));
                            txtArea.setText(String.valueOf(df_obj.format(distance * 0.000247105 * 0.404686)));
                        }

                    }

//                double remArea=totalSize-totalArea;
                    //Insert
                    if (Double.parseDouble(dist) <= totalSize) {
                        //if(Double.parseDouble(dist)<=remArea){
                        if (txtArea.getText().toString().length() > 0 && markerList.size() > 2) {
//                    txtFrstLatLong.setText(latLngList.get(0).toString());
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
//                                Toast.makeText(MapsActivity.this, "Area " + dist, Toast.LENGTH_SHORT).show();


                                    new Handler().postDelayed(new Runnable() {

                                        @Override
                                        public void run() {

//                                    intent.putExtra("area", dist);
//                                    viewModel.updatePlotDetailListTableGps(dist,PlotId);
//                                        if (gpsCat == 0) {
//                                            totalArea = Double.parseDouble(dist);
//                                            String mtotArea = String.valueOf(totalArea);
////                                            viewModel.updatePlotDetailListTableSyncAndPlotArea1("0", "0", dist, mtotArea, PlotId);
//                                        } else if (gpsCat == 1) {
//                                            double temp2 = Double.parseDouble(dist);
//                                            String mtotArea = String.valueOf(totalArea + temp2);
////                                            viewModel.updatePlotDetailListTableSyncAndPlotArea2("0", "0", dist, mtotArea, PlotId);
//                                        } else if (gpsCat == 2) {
//                                            double temp2 = Double.parseDouble(dist);
//                                            String mtotArea = String.valueOf(totalArea + temp2);
////                                            viewModel.updatePlotDetailListTableSyncAndPlotArea3("0", "0", dist, mtotArea, PlotId);
//                                        } else if (gpsCat == 3) {
//                                            double temp2 = Double.parseDouble(dist);
//                                            String mtotArea = String.valueOf(totalArea + temp2);
////                                            viewModel.updatePlotDetailListTableSyncAndPlotArea4("0", "0", dist, mtotArea, PlotId);
//                                        } else if (gpsCat == 4) {
//                                            double temp2 = Double.parseDouble(dist);
//                                            String mtotArea = String.valueOf(totalArea + temp2);
////                                            viewModel.updatePlotDetailListTableSyncAndPlotArea5("0", "0", dist, mtotArea, PlotId);
//                                        }
//                                        Log.e("MapGPSCat", gpsCat.toString());
//                                        Log.e("MapDist", dist);
//                            intent.putExtra("area", txtArea.getText().toString());
//                            intent.putExtra("geo_value", txtArea.getText().toString());
                                            Intent intent = new Intent();
                                            intent.putExtra("areaGeo", dist);
                                            setResult(RESULT_OK, intent);
                                            finish();
                                        }

                                    }, 1 * 1000);


//                            finish();
                                }

                            }
                        } else {
                            Toast.makeText(MapsActivity.this, "Please mark at-least 3 boundaries", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MapsActivity.this, "Area must be less than provided area!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("MapActSavebtn:",String.valueOf(saveCount));
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
                        time = time++;
//                        getCurrentLocation();
                        //      Toast.makeText(MainActivity.this, "counnter"+String.valueOf(time), Toast.LENGTH_SHORT).show();
                        handler.postDelayed(this, 10000);
                        //    finish();
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

//    private void insertPlotGeoBoundsToLocalDB(AddGeoBoundriesTable addGeoBoundriesTable) {
//        try {
//           //  appHelper.getDialogHelper().getLoadingDialog().showGIFLoading();
//            viewModel.insertPlotGeoBoundsTableLocal(addGeoBoundriesTable);
//            if (viewModel.PlotGeoBoundsTableLiveDataInsert() != null) {
//                Observer getLeadRawDataObserver = new Observer() {
//                    @Override
//                    public void onChanged(@Nullable Object o) {
//                        AddGeoBoundriesTable customerSurveyTable1 = (AddGeoBoundriesTable) o;
//                        viewModel.PlotGeoBoundsTableLiveDataInsert().removeObserver(this);
//                        if (customerSurveyTable1 != null) {
//                           // appHelper.getDialogHelper().getLoadingDialog().closeDialog();
//                            Toast.makeText(GepBoundariesMap.this, "Geobounds details are saved successfully", Toast.LENGTH_LONG).show();
//
////                            Intent intent= new Intent(GepBoundariesMap.this, PlotDetailsActivity.class);
////                            intent.putExtra("Area",txtArea.getText().toString());
////                            startActivity(intent);
////                            finish();
//                        }
//                    }
//                };
//                viewModel.PlotGeoBoundsTableLiveDataInsert().observe(this, getLeadRawDataObserver);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            appHelper.getDialogHelper().getLoadingDialog().closeDialog();
//        }
//
//    }

//    private void  insertLogBookGeoBoundsToLocalDB(AddLogBookGeoBoundariesTable addGeoBoundriesTable) {
//        try {
//            //  appHelper.getDialogHelper().getLoadingDialog().showGIFLoading();
//            viewModel.insertLogBookGeoBoundsTableLocal(addGeoBoundriesTable);
//            if (viewModel.logBookGeoBoundsTableLiveDataInsert() != null) {
//                Observer getLeadRawDataObserver = new Observer() {
//                    @Override
//                    public void onChanged(@Nullable Object o) {
//                        AddLogBookGeoBoundariesTable customerSurveyTable1 = (AddLogBookGeoBoundariesTable) o;
//                        viewModel.logBookGeoBoundsTableLiveDataInsert().removeObserver(this);
//                        if (customerSurveyTable1 != null) {
//                            // appHelper.getDialogHelper().getLoadingDialog().closeDialog();
////                            Toast.makeText(GepBoundariesMap.this, "Geobounds details are saved successfully", Toast.LENGTH_LONG).show();
//
////                            Intent intent= new Intent(GepBoundariesMap.this, PlotDetailsActivity.class);
////                            intent.putExtra("Area",txtArea.getText().toString());
////                            startActivity(intent);
////                            finish();
//                        }
//                    }
//                };
//                viewModel.logBookGeoBoundsTableLiveDataInsert().observe(this, getLeadRawDataObserver);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            appHelper.getDialogHelper().getLoadingDialog().closeDialog();
//        }
//
//    }



    private void getCurrentLocation() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

//        Task<Location> task = client.getLastLocation();
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location!= null){
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            //initialise latlang
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                            if(counter==0){
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));//test here
                                counter=counter++;
                            }
                            Log.e("testMapOnMapReady","its here task listener");
//                            MarkerOptions markerOptions = new MarkerOptions();
//                            markerOptions.position(latLng);
//                            markerOptions.title("pinnedlocations");
//                            markerOptions.snippet(latLng.latitude + ":" + latLng.longitude + ":");

                            //Current location marker
                            if (myMarkerCount>0) {
                                if (myMark != null) {
                                    myMark.remove();
                                }
                            }

                            myMark = googleMap.addMarker(new MarkerOptions()
                                    .position(latLng).title("My Location")
                                    .icon(BitmapFromVector(getApplicationContext(),R.drawable.baseline_my_location))
                                    .visible(true));
                            myMarkerCount++;
                            new Handler().postDelayed(new Runnable() {//delay check
                                @Override
                                public void run() {
                                    checkSettingsAndStartLocationUpdates();
                                }
                            }, 1 * 5000);



                            //to clear marker
                            //gmap.clear();

                            //creater Marker
//                            Marker marker = gmap.addMarker(markerOptions);
//
//
//                            //zoom camera to marker
////                            gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
//                            gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
//
//
//                            //add latlag and markers
//                            latLngList.add(latLng);
//                            lat.add(String.valueOf(location.getLatitude()));
//                            lon.add(String.valueOf(location.getLongitude()));
//                            markerList.add(marker);
//                            if (markerList.size() > 1) {
//                                if (polyline != null) polyline.remove();
//                                //create polylineoption
////                                PolylineOptions polylineOptions = new PolylineOptions()
//                                polylineOptions = new PolylineOptions()
//                                        .addAll(latLngList)
//                                        .clickable(true);
//                                polyline = gmap.addPolyline(polylineOptions);
//                                polyline.setColor(getColor(R.color.yellowish_color));
//
//
//                                if (polygon!=null) polygon.remove();
//                                //create polygoneoptions
////                                PolygonOptions polygonOptions= new PolygonOptions()
//                                 polygonOptions= new PolygonOptions()
//                                        .addAll(latLngList)
//                                        .clickable(true);
//                                polygon=gmap.addPolygon(polygonOptions);
//                                polygon.setFillColor(getColor(R.color.greenCardBg));
//
//                                //                   if (distance==120){
////                                    Toast.makeText(MainActivity.this, "moving too fast", Toast.LENGTH_SHORT).show();
////                                    if (polyline != null) polyline.remove();
////                                    latLngList.clear();
////                                    markerList.clear();
////
////                                    if(polyline!=null) {
////                                        polyline.remove();
////                                    }
////                                    if(polygon!=null) {
////                                        polygon.remove();
////                                    }
////                                    //Txtdistance.setText("Distance:"+"");
////                                    txtArea.setText("Area:"+"");
////                                    handler.removeCallbacks(run);
////                                    btnStop.setVisibility(View.GONE);
////                                    b10sec.setVisibility(View.VISIBLE);
////
////                                }else {
//                                double distance = SphericalUtil.computeLength(latLngList);
//                                double Area = SphericalUtil.computeArea(latLngList);
////                                Txtdistance.setText("Distance:" + String.valueOf(distance) + "meters");
////                                if(areaBefore<Area){
//                                    areaBefore= Area;
//                                    txtArea.setText( String.valueOf(Area));
////                                } else {
////                                    txtArea.setText("");
////                                    //test
////                                    if (polyline != null) polyline.remove();
////
////                                    for (Marker markers : markerList) markers.remove();
////                                    latLngList.clear();
////                                    markerList.clear();
////                                    lat.clear();
////                                    lon.clear();
////
////                                    if(polyline!=null) {
////                                        polyline.remove();
////                                    }
////                                    if(polygon!=null) {
////                                        polygon.remove();
////                                    }
////                                    //Txtdistance.setText("Distance:"+"");
////                                    txtArea.setText("");
////                                }
//
//                            }

                        }
                    });
                }
                Log.e("testMapOnMapReady","its here on task ready at the end ");
            }
        });


    }


    public static void main(String[] args) {

        double input = 1205.6358;

        System.out.println("salary : " + input);

        // DecimalFormat, default is RoundingMode.HALF_EVEN
//        System.out.println("salary : " + df.format(input));      //1205.64
//
//        df.setRoundingMode(RoundingMode.DOWN);
//        System.out.println("salary : " + df.format(input));      //1205.63
//
//        df.setRoundingMode(RoundingMode.UP);
//        System.out.println("salary : " + df.format(input));      //1205.64

    }

    private void configureDagger() {
        AndroidInjection.inject(this);

    }

    private void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
//        getPlotListFromLocalDb(farmerCode);Todo : Plantation
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==44){
            if(grantResults.length>0&& grantResults[0]== PackageManager.PERMISSION_GRANTED) {
//                getCurrentLocation();//TODO : need to comment
                checkSettingsAndStartLocationUpdates();//ash new
            }
        }
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
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap=googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setIndoorEnabled(true);


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                //creating markerOptions
                MarkerOptions markerOptions= new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("pinnedlocation");
                markerOptions.snippet(latLng.latitude+":"+latLng.longitude+":");
                //to clear marker
                //gmap.clear();

                //creater Marker
                markerOptionsLast=markerOptions;
                Marker marker= mMap.addMarker(markerOptions);
//                markerName= mMap.addMarker(markerOptions);
//                Marker marker= mMap.addMarker(markerOptions);
//mMap.re

                //zoom camera to marker
//                if(counter==0){
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,20));
//                    counter=counter++;
//                }

//                gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                Log.e("testMapOnMapReady","its here");

                //add latlag and markers
                latLngList.add(latLng);
                lat.add(String.valueOf(latLng.latitude));
                lon.add(String.valueOf(latLng.longitude));
                markerList.add(marker);
//                markerList.remove(marker);
                //for now hide it
                if (markerList.size()>1){
                    if (polyline!=null) polyline.remove();
                    //create polylineoption
//                    PolylineOptions polylineOptions= new PolylineOptions()
                    polylineOptions= new PolylineOptions()
                            .addAll(latLngList)
                            .clickable(true);
                    polyline=mMap.addPolyline(polylineOptions);
                    polyline.setColor(getApplicationContext().getResources().getColor(R.color.teal_200));
                    double distance = SphericalUtil.computeLength(latLngList);
                    double Area = SphericalUtil.computeArea(latLngList);
                    //           Txtdistance.setText("Distance:" + String.valueOf(distance) + "meters");
//                    if(areaBefore<Area){
                    areaBefore = Area;
//                        txtArea.setText( String.valueOf(Area));
                    //to remove area till last marker
//                    } else {
//                        if(!latLngList.isEmpty()){
//                            if(latLngList.size()>2){
//                                areaBefore=0.0;
//                                if (polyline != null) polyline.remove();
//
//                                for (Marker markers : markerList) markers.remove();
//                                latLngList.clear();
//                                markerList.clear();
//                                lat.clear();
//                                lon.clear();
//
//                                if(polyline!=null) {
//                                    polyline.remove();
//                                }
//                                if(polygon!=null) {
//                                    polygon.remove();
//                                }
//                                //Txtdistance.setText("Distance:"+"");
//                                txtArea.setText("");
//                            }
//                        }
//
//                    }


                    count = count+1;
//                    coordinatesAdapter.notifyDataSetChanged();

                    //

                }


                coordinatesAdapter = new CoordinatesAdapter(MapsActivity.this,latLngList);
                LinearLayoutManager linearLayoutManager= new LinearLayoutManager(MapsActivity.this,RecyclerView.VERTICAL,false);
                RVgpsRecords.setLayoutManager(linearLayoutManager);
                RVgpsRecords.setItemAnimator(new DefaultItemAnimator());
                RVgpsRecords.setNestedScrollingEnabled(true);
                RVgpsRecords.setAdapter(coordinatesAdapter);

                //end here
//                if (polygon!=null) polygon.remove();
                //create polygoneoptions

//important
//                try {
//                    if(!latLngList.isEmpty()){
//                        PolygonOptions polygonOptions= new PolygonOptions()
//                                .addAll(latLngList)
//                                .clickable(true);
//                        polygon=gmap.addPolygon(polygonOptions);
//                        polygon.setFillColor(getColor(R.color.primary));
//                    }
//
//                } catch (Exception ex){
//                    Toast.makeText(GepBoundariesMap.this, "test bug", Toast.LENGTH_SHORT).show();
//                }
//
//                polygon.setFillColor(getColor(R.color.teal_200));

            }
        });
        Log.e("testMapOnMapReady","its here on map ready at the end ");
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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//    }


    //Todo:Need to make for Plantation
//    public void getPlotListFromLocalDb(String farmer_code) {
//        try {
//            viewModel.getLandDetailsListFromLocalDb(farmer_code);
//            if (viewModel.getLandDetailsListTableLocalDBLiveData() != null) {
//                Observer getLeadRawDataObserver = new Observer() {
//                    @Override
//                    public void onChanged(@Nullable Object o) {
//                        List<PlotDetailsListTable> odVisitSurveyTableList = (List<PlotDetailsListTable>) o;
//                        viewModel.getLandDetailsListTableLocalDBLiveData().removeObserver(this);
//
//                        if (odVisitSurveyTableList != null && odVisitSurveyTableList.size() > 0) {
//
//
//
////                            double temp1= Double.parseDouble(plotArea1);
////                            double temp2= Double.parseDouble(plotArea2);
////                            double temp3= Double.parseDouble(plotArea3);
////                            double temp4=temp1+temp2+temp3;
////                            Log.e("PlotSum", Double.toString(temp4));//Integer.parseInt(plotArea1+plotArea2+plotArea3)
//
//                            if (odVisitSurveyTableList.get(0).getGPSPlotArea()=="0" || odVisitSurveyTableList.get(0).getGPSPlotArea()==null){
//                                totalArea=0.0;
//                            } else {
//                                totalArea=Double.parseDouble(odVisitSurveyTableList.get(0).getGPSPlotArea());
//                                totalSize=Double.parseDouble(odVisitSurveyTableList.get(0).getSize());
//                            }
//
//
//
//                        } else {
//
//                        }
//                    }
//                };
//                viewModel.getLandDetailsListTableLocalDBLiveData().observe(this, getLeadRawDataObserver);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//
//        }
//    }



}