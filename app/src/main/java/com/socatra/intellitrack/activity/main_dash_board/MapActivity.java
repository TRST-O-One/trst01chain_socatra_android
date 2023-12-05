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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.socatra.intellitrack.R;
import com.socatra.intellitrack.adapters.CoordinatesAdapter;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.helper.AppHelper;
import com.socatra.intellitrack.models.get.GetLatLongListfromPlot;
import com.socatra.intellitrack.view_models.AppViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {


    private static final String TAG = "MapActivity";
    private static String strPlotCode;
    public AppHelper appHelper;
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    public AppViewModel viewModel;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    ImageView ImgCancel;
    RecyclerView recyclerView;
    List<GetLatLongListfromPlot> latLngList = new ArrayList<>();
    String mToken,strLanguageId;
    String deviceRoleName;
    private TextView txtPlotCode, txtArea, txtGeoboundariesArea;
    private CoordinatesAdapter adapter;
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
        // String plotCode = "P_91519945591_1";
        String geoBoundariesArea = intent.getStringExtra("geoBoundariesArea");
        String area = intent.getStringExtra("Area");
        strPlotCode = intent.getStringExtra("plotCode");
        mToken = intent.getStringExtra("mToken");
        txtPlotCode.setText(strPlotCode);
        //   Log.d(TAG, "Plot Code: " + plotCode);
        Log.d(TAG, "MToken: " + mToken);
        txtArea.setText(area);
        txtGeoboundariesArea.setText(geoBoundariesArea + " in Hec");

        recyclerView = findViewById(R.id.latlong);
        adapter = new CoordinatesAdapter(this, latLngList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Initialize the Google Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapview);
        mapFragment.getMapAsync(this);


        if (deviceRoleName.equalsIgnoreCase("Dealer") || deviceRoleName.equalsIgnoreCase("Processor") ||deviceRoleName.equalsIgnoreCase("Trader")) {
            getLatLongDetailsByPlotCode(strPlotCode);
        }
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

    private void initializeValues() {
        ImgCancel.setOnClickListener(view -> onBackPressed());
    }

    private void getLatLongDetailsByPlotCode(String plotCode) {


        AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        String authToken = AUTHORIZATION_TOKEN_KEY;

        Call<JsonElement> callRetrofit = service.getLatLongDetailsByPlotCodeFromServer(plotCode, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));


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
                                ArrayList<GetLatLongListfromPlot> getLatLongListTableArrayList = new ArrayList<>();

                                for (int i = 0; i < jsonArray.size(); i++) {
                                    JsonObject jsonObjectFarmerPD = jsonArray.get(i).getAsJsonObject();

                                    // Parse the LatLong field as a JSON string
                                    String latLongString = jsonObjectFarmerPD.get("LatLong").getAsString();

                                    // Parse the JSON array from the string
                                    JsonArray latLongArray = new Gson().fromJson(latLongString, JsonArray.class);

                                    // Iterate through the latLongArray
                                    for (int j = 0; j < latLongArray.size(); j++) {
                                        JsonArray latLngPair = latLongArray.get(j).getAsJsonArray();
                                        double latitude = latLngPair.get(0).getAsDouble();
                                        double longitude = latLngPair.get(1).getAsDouble();

                                        // Create a GetLatLongListfromPlot object and add it to the list
                                        GetLatLongListfromPlot getLatLongDetails = new GetLatLongListfromPlot();
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
                                Toast.makeText(MapActivity.this, "No records found", Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        Toast.makeText(MapActivity.this, "Response not successful", Toast.LENGTH_LONG).show();
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

    protected void onResume() {
        super.onResume();

        if (appHelper.isNetworkAvailable()) {
          //  getLanguageDataList();
        } else {
            Toast.makeText(getApplicationContext(), "please check your internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setIndoorEnabled(true);
    }

    private void plotCoordinatesOnMap(List<GetLatLongListfromPlot> coordinatesList) {
        mMap.clear(); // Clear any existing markers and polygons on the map

        if (coordinatesList.isEmpty()) {
            return; // Nothing to plot
        }

        // Create a LatLngBounds builder to encompass all coordinates
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

        // Add markers for all coordinates
        for (GetLatLongListfromPlot coordinate : coordinatesList) {
            // Split the latLng string into latitude and longitude
            String[] latLngParts = coordinate.getLatLong().split(",");
            if (latLngParts.length == 2) {
                double latitude = Double.parseDouble(latLngParts[0]);
                double longitude = Double.parseDouble(latLngParts[1]);

                LatLng latLng = new LatLng(latitude, longitude);

                // Add a marker on the map at the specified coordinates
                mMap.addMarker(new MarkerOptions().position(latLng).title("Coordinates"));

                // Extend the bounds to include this marker
                boundsBuilder.include(latLng);
            }
        }

        // Create a polygon by connecting the coordinates
        PolygonOptions polygonOptions = new PolygonOptions();
        for (GetLatLongListfromPlot coordinate : coordinatesList) {
            // Split the latLng string into latitude and longitude
            String[] latLngParts = coordinate.getLatLong().split(",");
            if (latLngParts.length == 2) {
                double latitude = Double.parseDouble(latLngParts[0]);
                double longitude = Double.parseDouble(latLngParts[1]);

                // Add the coordinate to the polygon
                polygonOptions.add(new LatLng(latitude, longitude));
            }
        }

        // Set polygon style
        polygonOptions.strokeWidth(2)
                .strokeColor(Color.GREEN)
                .fillColor(Color.argb(128, 0, 255, 0)); // Adjust stroke and fill colors as needed

        // Add the polygon to the map
        mMap.addPolygon(polygonOptions);

        // Fit the camera to the bounds to ensure all markers and the polygon are visible
        LatLngBounds bounds = boundsBuilder.build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100)); // Adjust padding as needed
    }

    private void getLanguageDataList() {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        final ProgressDialog progressDialog = new ProgressDialog(MapActivity.this, R.style.AppCompatAlertDialogStyle);
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

                        Toast.makeText(MapActivity.this, "no records found", Toast.LENGTH_LONG).show();
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
