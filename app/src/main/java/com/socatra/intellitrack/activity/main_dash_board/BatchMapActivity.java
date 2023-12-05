package com.socatra.intellitrack.activity.main_dash_board;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserRole;
import static com.socatra.intellitrack.constants.AppConstant.LanguageId;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.socatra.intellitrack.R;
import com.socatra.intellitrack.adapters.BatchCoordinatesAdapter;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.helper.AppHelper;
import com.socatra.intellitrack.models.get.GetLatLongListfromBatchId;

import com.socatra.intellitrack.view_models.AppViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BatchMapActivity extends FragmentActivity implements OnMapReadyCallback {


    private static final String TAG = "BatchMapActivity";
    private static String strBatchId,strLanguageId;
    public AppHelper appHelper;
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    public AppViewModel viewModel;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    ImageView ImgCancel;
    RecyclerView recyclerView;
    List<GetLatLongListfromBatchId> latLngList = new ArrayList<>();
    String mToken;
    String deviceRoleName;
    private TextView txtPlotCode, txtArea, txtGeoboundariesArea;
    private BatchCoordinatesAdapter adapter;
    private GoogleMap mMap;

    TextView txtl_mapview,txtl_PlotCode,txtl_Area,txtl_GeoboundariesArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        appHelper = new AppHelper(this);

        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");


        initializeUI();
        initializeValues();

        Intent intent = getIntent();


        strBatchId = intent.getStringExtra("BatchId");


//        txtPlotCode.setText(strPlotCode);

//        //   Log.d(TAG, "Plot Code: " + plotCode);
//        Log.d(TAG, "MToken: " + mToken);
//        txtArea.setText(area);
//        txtGeoboundariesArea.setText(geoBoundariesArea + " in Hec");

        recyclerView = findViewById(R.id.latlong);
        adapter = new BatchCoordinatesAdapter(appHelper,this, latLngList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Initialize the Google Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapview);
        mapFragment.getMapAsync(this);


        getLatLongDetailsByBatchId(strBatchId);



    }


    private void initializeUI() {

        ImgCancel = findViewById(R.id.img_cancel);
        txtPlotCode = findViewById(R.id.txtPlotCode);
        txtArea = findViewById(R.id.txtArea);
        txtGeoboundariesArea = findViewById(R.id.txtGeoboundariesArea);
        deviceRoleName = appHelper.getSharedPrefObj().getString(DeviceUserRole, "");

        //language
        txtl_mapview = findViewById(R.id.txt_word_map_view);
        txtl_PlotCode = findViewById(R.id.txt_plot_code_view);
        txtl_Area = findViewById(R.id.txt_area_view);
        txtl_GeoboundariesArea = findViewById(R.id.txt_geo_boundaries_arae_view);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    protected void onResume() {
        super.onResume();

        if (appHelper.isNetworkAvailable()) {
           // getLanguageDataList();
        } else {
            Toast.makeText(getApplicationContext(), "please check your internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void initializeValues() {
        ImgCancel.setOnClickListener(view -> onBackPressed());
    }

    private void getLatLongDetailsByBatchId(String strBatchId) {


        AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        String authToken = AUTHORIZATION_TOKEN_KEY;

        Call<JsonElement> callRetrofit = service.getLatLongDetailsByBatchIdFromServer(strBatchId, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));


        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        JsonElement jsonElement = response.body();
                        if (jsonElement != null && jsonElement.isJsonObject()) {
                            JsonObject jsonObject = jsonElement.getAsJsonObject();
                            if (jsonObject.has("data") && jsonObject.get("data").isJsonArray()) {
                                JsonArray jsonArray = jsonObject.getAsJsonArray("data");
                                ArrayList<GetLatLongListfromBatchId> getLatLongListTableArrayList = new ArrayList<>();

                                for (int i = 0; i < jsonArray.size(); i++) {
                                    JsonObject jsonObjectFarmerPD = jsonArray.get(i).getAsJsonObject();

                                    // Parse the LatLong field as a JSON string
                                    String latLongString = jsonObjectFarmerPD.get("LatLong").getAsString();

                                    String geoboundariesArea = jsonObjectFarmerPD.get("GeoboundariesArea").getAsString();
                                    String areaInHectors = jsonObjectFarmerPD.get("AreaInHectors").getAsString();
                                    String PlotCode = jsonObjectFarmerPD.get("PlotCode").getAsString();

                                    txtArea.setText(areaInHectors);

                                    txtPlotCode.setText(PlotCode);


                                    txtGeoboundariesArea.setText(geoboundariesArea + " in Hec");

                                    // Parse the JSON array from the string
                                    JsonArray latLongArray = new Gson().fromJson(latLongString, JsonArray.class);

                                    // Iterate through the latLongArray
                                    for (int j = 0; j < latLongArray.size(); j++) {
                                        JsonArray latLngPair = latLongArray.get(j).getAsJsonArray();
                                        double latitude = latLngPair.get(0).getAsDouble();
                                        double longitude = latLngPair.get(1).getAsDouble();

                                        // Create a GetLatLongListfromBatchId object and add it to the list
                                        GetLatLongListfromBatchId getLatLongDetails = new GetLatLongListfromBatchId();
                                        getLatLongDetails.setLatLong(latitude + "," + longitude); // Store as a string

                                        getLatLongListTableArrayList.add(getLatLongDetails);


                                        // Log latitude and longitude separately
                                        Log.d(TAG, "Latitude: " + latitude + ", Longitude: " + longitude);
                                    }

                                    // Other fields can be added here
                                }

                                latLngList.clear();
                                latLngList.addAll(getLatLongListTableArrayList);
                                plotCoordinatesOnMap(latLngList);
                                adapter.notifyDataSetChanged();


                                Log.d(TAG, "API Response: " + jsonObject.toString());
                            } else {
                                Toast.makeText(BatchMapActivity.this, "No records found", Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        Toast.makeText(BatchMapActivity.this, "Response not successful", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Log.d("Error", ">>>>" + ex.toString());
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setIndoorEnabled(true);
    }

//    private void plotCoordinatesOnMap(List<GetLatLongListfromBatchId> coordinatesList) {
//        mMap.clear(); // Clear any existing markers and polygons on the map
//
//        if (coordinatesList.isEmpty()) {
//            return; // Nothing to plot
//        }
//
//        // Create a LatLngBounds builder to encompass all coordinates
//        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
//
//        // Add markers for all coordinates
//        for (GetLatLongListfromBatchId coordinate : coordinatesList) {
//            // Split the latLng string into latitude and longitude
//            String[] latLngParts = coordinate.getLatLong().split(",");
//            if (latLngParts.length == 2) {
//                double latitude = Double.parseDouble(latLngParts[0]);
//                double longitude = Double.parseDouble(latLngParts[1]);
//
//                LatLng latLng = new LatLng(latitude, longitude);
//
//                // Add a marker on the map at the specified coordinates
//                mMap.addMarker(new MarkerOptions().position(latLng).title("Coordinates"));
//
//                // Extend the bounds to include this marker
//                boundsBuilder.include(latLng);
//            }
//        }
//
//        // Create a polygon by connecting the coordinates
//        PolygonOptions polygonOptions = new PolygonOptions();
//        for (GetLatLongListfromBatchId coordinate : coordinatesList) {
//            // Split the latLng string into latitude and longitude
//            String[] latLngParts = coordinate.getLatLong().split(",");
//            if (latLngParts.length == 2) {
//                double latitude = Double.parseDouble(latLngParts[0]);
//                double longitude = Double.parseDouble(latLngParts[1]);
//
//                // Add the coordinate to the polygon
//                polygonOptions.add(new LatLng(latitude, longitude));
//            }
//        }
//
//        // Set polygon style
//        polygonOptions.strokeWidth(2)
//                .strokeColor(Color.GREEN)
//                .fillColor(Color.argb(128, 0, 255, 0)); // Adjust stroke and fill colors as needed
//
//        // Add the polygon to the map
//        mMap.addPolygon(polygonOptions);
//
//        // Fit the camera to the bounds to ensure all markers and the polygon are visible
//        LatLngBounds bounds = boundsBuilder.build();
//        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100)); // Adjust padding as needed
//    }

    private void plotCoordinatesOnMap(List<GetLatLongListfromBatchId> coordinatesList) {
        mMap.clear(); // Clear any existing markers and polygons on the map

        if (coordinatesList.isEmpty()) {
            return; // Nothing to plot
        }

        // Create separate polygons for each PlotCode
        Map<String, List<LatLng>> plotCodePolygons = new HashMap<>();

        for (GetLatLongListfromBatchId coordinate : coordinatesList) {
            // Split the latLng string into latitude and longitude
            String[] latLngParts = coordinate.getLatLong().split(",");
            if (latLngParts.length == 2) {
                double latitude = Double.parseDouble(latLngParts[0]);
                double longitude = Double.parseDouble(latLngParts[1]);

                // Check which PlotCode this coordinate belongs to
                String plotCode = coordinate.getPlotCode();

                // Get or create the list of coordinates for this PlotCode
                List<LatLng> polygonCoordinates = plotCodePolygons.get(plotCode);
                if (polygonCoordinates == null) {
                    polygonCoordinates = new ArrayList<>();
                    plotCodePolygons.put(plotCode, polygonCoordinates);
                }

                // Add the coordinate to the list for this PlotCode
                polygonCoordinates.add(new LatLng(latitude, longitude));

                // Add a marker on the map at the specified coordinates
                mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Coordinates"));
            }
        }

        // Draw separate polygons for each PlotCode
        for (Map.Entry<String, List<LatLng>> entry : plotCodePolygons.entrySet()) {
            List<LatLng> coordinates = entry.getValue();
            PolygonOptions polygonOptions = new PolygonOptions()
                    .strokeWidth(2)
                    .strokeColor(Color.GREEN)
                    .fillColor(Color.argb(128, 0, 255, 0)); // Adjust stroke and fill colors as needed

            for (LatLng coordinate : coordinates) {
                polygonOptions.add(coordinate);
            }

            mMap.addPolygon(polygonOptions);
        }
    }


    private void getLanguageDataList() {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        final ProgressDialog progressDialog = new ProgressDialog(BatchMapActivity.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading Dealer data...");
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
                                String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");


                                if (strLanguageId.equals("1")) {


                                    if (getResources().getString(R.string.map_view).equals(jsonEngWord)) {
                                        txtl_mapview.setText(jsonEngWord);
                                    } else if (getResources().getString(R.string.plot_code).equals(jsonEngWord)) {
                                        txtl_PlotCode.setText(jsonEngWord);
                                    } else if (getResources().getString(R.string.geo_boundaries_area).equals(jsonEngWord)) {
                                        txtl_GeoboundariesArea.setText(jsonEngWord);
                                    } else if (getResources().getString(R.string.area_in_hec).equals(jsonEngWord)) {
                                        txtl_Area.setText(jsonEngWord);
                                    }

                                } else {



                                    if (getResources().getString(R.string.map_view).equals(jsonEngWord)) {
                                        txtl_mapview.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.plot_code).equals(jsonEngWord)) {
                                        txtl_PlotCode.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.geo_boundaries_area).equals(jsonEngWord)) {
                                        txtl_GeoboundariesArea.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.area_in_hec).equals(jsonEngWord)) {
                                        txtl_Area.setText(strConvertedWord);
                                    }


                                }

//
//                                else if (strLanguageId.equals("3")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//
//                                    if (getResources().getString(R.string.grn_details).equals(jsonEngWord)) {
//                                        txtWordTitle.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.dealer).equals(jsonEngWord)) {
//                                        txtWordDealer.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.farmer).equals(jsonEngWord)) {
//                                        txtWordFarmer.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.quantity).equals(jsonEngWord)) {
//                                        txtWordQuantity.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.mode_of_transport).equals(jsonEngWord)) {
//                                        txtWordModeOFTransport.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.grn_date).equals(jsonEngWord)) {
//                                        txtWordGrnDate.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.grn_evidence).equals(jsonEngWord)) {
//                                        txtWordGRNEvidence.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.save).equals(jsonEngWord)) {
//                                        txtSave.setText(strConvertedWord);
//                                    }
//
//
//                                } else if (strLanguageId.equals("4")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//
//
//                                    if (getResources().getString(R.string.grn_details).equals(jsonEngWord)) {
//                                        txtWordTitle.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.dealer).equals(jsonEngWord)) {
//                                        txtWordDealer.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.farmer).equals(jsonEngWord)) {
//                                        txtWordFarmer.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.quantity).equals(jsonEngWord)) {
//                                        txtWordQuantity.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.mode_of_transport).equals(jsonEngWord)) {
//                                        txtWordModeOFTransport.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.grn_date).equals(jsonEngWord)) {
//                                        txtWordGrnDate.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.grn_evidence).equals(jsonEngWord)) {
//                                        txtWordGRNEvidence.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.save).equals(jsonEngWord)) {
//                                        txtSave.setText(strConvertedWord);
//                                    }
//
//
//                                } else if (strLanguageId.equals("5")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//
//
//                                    if (getResources().getString(R.string.grn_details).equals(jsonEngWord)) {
//                                        txtWordTitle.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.dealer).equals(jsonEngWord)) {
//                                        txtWordDealer.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.farmer).equals(jsonEngWord)) {
//                                        txtWordFarmer.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.quantity).equals(jsonEngWord)) {
//                                        txtWordQuantity.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.mode_of_transport).equals(jsonEngWord)) {
//                                        txtWordModeOFTransport.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.grn_date).equals(jsonEngWord)) {
//                                        txtWordGrnDate.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.grn_evidence).equals(jsonEngWord)) {
//                                        txtWordGRNEvidence.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.save).equals(jsonEngWord)) {
//                                        txtSave.setText(strConvertedWord);
//                                    }
//
//
//                                } else {
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

                        Toast.makeText(BatchMapActivity.this, "no records found", Toast.LENGTH_LONG).show();
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
