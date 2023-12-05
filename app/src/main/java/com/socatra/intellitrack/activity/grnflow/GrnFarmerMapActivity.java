package com.socatra.intellitrack.activity.grnflow;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserRole;
import static com.socatra.intellitrack.constants.AppConstant.LanguageId;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class GrnFarmerMapActivity extends FragmentActivity implements OnMapReadyCallback {


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
    List<GetLatLongListfromPlot> latLngList;
    String mToken;
    String deviceRoleName;
    String strFarmercode;
    Spinner spSelectPlotCoode;
    ArrayList<String> listOfPlotCodes = new ArrayList<>();
    ArrayList<String> listOfGeoboundaries = new ArrayList<>();
    ArrayList<String> listofArea = new ArrayList<>();
    String strPlotcode;
    String strPlotsCount;
    TextView txtWordMapView, txtWordFarmerCode, txtWordNumberOfPlots, txtWordPlotCode, txtWordGeoBoundaries, txtWordArea;
    String strLanguageId;
    private TextView txtFarmerCode, txtArea, txtGeoboundariesArea, txtPlotsCount;
    //ArrayList<String>
    private CoordinatesAdapter coordinatesAdapter;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grn_map_farmer);
        appHelper = new AppHelper(this);
        Intent intent = getIntent();
        strFarmercode = intent.getStringExtra("farmercode");
        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");
        initializeUI();
        initializeValues();
        txtFarmerCode.setText(strFarmercode);
        // Initialize the Google Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapview);
        mapFragment.getMapAsync(this);

        if (appHelper.isNetworkAvailable()) {
            getPlotdetailsByFarmercode();
        } else {
            Toast.makeText(GrnFarmerMapActivity.this, "please check your internet connection", Toast.LENGTH_SHORT).show();
        }

    }


    private void initializeUI() {
        ImgCancel = findViewById(R.id.img_cancel);
        txtFarmerCode = findViewById(R.id.txt_farmer_code);
        txtArea = findViewById(R.id.txtArea);
        txtGeoboundariesArea = findViewById(R.id.txtGeoboundariesArea);
        spSelectPlotCoode = findViewById(R.id.sp_select_plotcode);
        deviceRoleName = appHelper.getSharedPrefObj().getString(DeviceUserRole, "");
        txtPlotsCount = findViewById(R.id.txt_plots_count);
        recyclerView = findViewById(R.id.rec_plot_lat_lang);
        recyclerView.setLayoutManager(new LinearLayoutManager(GrnFarmerMapActivity.this));


        txtWordMapView = findViewById(R.id.txt_word_map_view);
        txtWordFarmerCode = findViewById(R.id.txt_word_farmer_cd);
        txtWordNumberOfPlots = findViewById(R.id.txt_word_number_plots);
        txtWordPlotCode = findViewById(R.id.txt_word_plot_code);
        txtWordGeoBoundaries = findViewById(R.id.txt_word_geo_boundaries);
        txtWordArea = findViewById(R.id.txt_word_area_hec);

    }

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
        final ProgressDialog progressDialog = new ProgressDialog(GrnFarmerMapActivity.this, R.style.AppCompatAlertDialogStyle);
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


                                    if (getResources().getString(R.string.map_view).equals(jsonEngWord)) {
                                        txtWordMapView.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.farmer_code).equals(jsonEngWord)) {
                                        txtWordFarmerCode.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.number_of_plots).equals(jsonEngWord)) {
                                        txtWordNumberOfPlots.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.plot_code).equals(jsonEngWord)) {
                                        txtWordPlotCode.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.geo_boundaries_area).equals(jsonEngWord)) {
                                        txtWordGeoBoundaries.setText(jsonEngWord1);
                                    } else if (getResources().getString(R.string.area_in_hec).equals(jsonEngWord)) {
                                        txtWordArea.setText(jsonEngWord1);
                                    }
                                } else {
                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
                                    if (getResources().getString(R.string.map_view).equals(jsonEngWord)) {
                                        txtWordMapView.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.farmer_code).equals(jsonEngWord)) {
                                        txtWordFarmerCode.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.number_of_plots).equals(jsonEngWord)) {
                                        txtWordNumberOfPlots.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.plot_code).equals(jsonEngWord)) {
                                        txtWordPlotCode.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.geo_boundaries_area).equals(jsonEngWord)) {
                                        txtWordGeoBoundaries.setText(strConvertedWord);
                                    } else if (getResources().getString(R.string.area_in_hec).equals(jsonEngWord)) {
                                        txtWordArea.setText(strConvertedWord);
                                    }


                                }
//
//                                else if (strLanguageId.equals("3")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//                                    if (getResources().getString(R.string.map_view).equals(jsonEngWord)) {
//                                        txtWordMapView.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.farmer_code).equals(jsonEngWord)) {
//                                        txtWordFarmerCode.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.number_of_plots).equals(jsonEngWord)) {
//                                        txtWordNumberOfPlots.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.plot_code).equals(jsonEngWord)) {
//                                        txtWordPlotCode.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.geo_boundaries_area).equals(jsonEngWord)) {
//                                        txtWordGeoBoundaries.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.area_in_hec).equals(jsonEngWord)) {
//                                        txtWordArea.setText(strConvertedWord);
//                                    }
//
//
//                                } else if (strLanguageId.equals("4")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//
//
//                                    if (getResources().getString(R.string.map_view).equals(jsonEngWord)) {
//                                        txtWordMapView.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.farmer_code).equals(jsonEngWord)) {
//                                        txtWordFarmerCode.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.number_of_plots).equals(jsonEngWord)) {
//                                        txtWordNumberOfPlots.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.plot_code).equals(jsonEngWord)) {
//                                        txtWordPlotCode.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.geo_boundaries_area).equals(jsonEngWord)) {
//                                        txtWordGeoBoundaries.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.area_in_hec).equals(jsonEngWord)) {
//                                        txtWordArea.setText(strConvertedWord);
//                                    }
//
//
//                                } else if (strLanguageId.equals("5")) {
//                                    String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");
//
//
//                                    if (getResources().getString(R.string.map_view).equals(jsonEngWord)) {
//                                        txtWordMapView.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.farmer_code).equals(jsonEngWord)) {
//                                        txtWordFarmerCode.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.number_of_plots).equals(jsonEngWord)) {
//                                        txtWordNumberOfPlots.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.plot_code).equals(jsonEngWord)) {
//                                        txtWordPlotCode.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.geo_boundaries_area).equals(jsonEngWord)) {
//                                        txtWordGeoBoundaries.setText(strConvertedWord);
//                                    } else if (getResources().getString(R.string.area_in_hec).equals(jsonEngWord)) {
//                                        txtWordArea.setText(strConvertedWord);
//                                    }
//
//
//                                } else {
//
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

                        Toast.makeText(GrnFarmerMapActivity.this, "no records found", Toast.LENGTH_LONG).show();
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initializeValues() {
//        ImgCancel.setOnClickListener(view -> onBackPressed());

        ImgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        spSelectPlotCoode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //  if (appHelper.isNetworkAvailable()) {
                strPlotcode = listOfPlotCodes.get(position);
                txtArea.setText(listofArea.get(position));
                txtGeoboundariesArea.setText(listOfGeoboundaries.get(position));
                if (appHelper.isNetworkAvailable()) {
                    getLatLongDetailsByPlotCode(listOfPlotCodes.get(position));
                } else {
                    Toast.makeText(GrnFarmerMapActivity.this, "please check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void getPlotdetailsByFarmercode() {
        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;
        // callRetrofit = service.getGetLogisticsDetailsDataByIDFromServer(strDealerID, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));
        callRetrofit = service.getPlantationDetailsByFarmerCode(strFarmercode, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));
        final ProgressDialog progressDialog = new ProgressDialog(GrnFarmerMapActivity.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading  data...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    String strResponse = String.valueOf(response.body());
                    Log.d(TAG, "onResponse: >>>" + strResponse);
                    JSONObject jsonObject = new JSONObject(strResponse);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.d(TAG, "onResponse: length" + jsonArray.length());
                    strPlotsCount = String.valueOf(jsonArray.length());
                    txtPlotsCount.setText(strPlotsCount);

                    listOfPlotCodes.clear();
                    if (jsonArray.length() > 0) {

                        try {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                listOfPlotCodes.add(jsonObjectFarmerPD.getString("PlotCode"));
                                listOfGeoboundaries.add(jsonObjectFarmerPD.getString("GeoboundariesArea"));
                                listofArea.add(jsonObjectFarmerPD.getString("AreaInHectors"));

                            }
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(GrnFarmerMapActivity.this,
                                    R.layout.spinner_dropdown_layout, listOfPlotCodes);
                            dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
                            spSelectPlotCoode.setAdapter(dataAdapter);
                            dataAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                            // Toast.makeText(AddGrnActivity.this, "Fetch All Data From Server SucessFully", Toast.LENGTH_LONG).show();

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Log.d("Error", ">>>>" + ex.toString());
                        }

                    } else {
                        Toast.makeText(GrnFarmerMapActivity.this, "no records found", Toast.LENGTH_LONG).show();
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

    private void getLatLongDetailsByPlotCode(String plotCode) {
        AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = service.getLatLongDetailsByPlotCodeFromServer(plotCode, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        JsonElement jsonElement = response.body();
                        if (jsonElement != null && jsonElement.isJsonObject()) {
                            latLngList = new ArrayList<>();
                            latLngList.clear();
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

                                latLngList.addAll(getLatLongListTableArrayList);
                                plotCoordinatesOnMap(getLatLongListTableArrayList);

                                coordinatesAdapter = new CoordinatesAdapter(GrnFarmerMapActivity.this, getLatLongListTableArrayList);
                                recyclerView.setAdapter(coordinatesAdapter);
                                coordinatesAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(GrnFarmerMapActivity.this, "No records found", Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        Toast.makeText(GrnFarmerMapActivity.this, "Response not successful", Toast.LENGTH_LONG).show();
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
}
