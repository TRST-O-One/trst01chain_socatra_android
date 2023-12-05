package com.socatra.excutivechain.activity;

import static com.socatra.excutivechain.AppConstant.DeviceUserID;
import static com.socatra.excutivechain.AppConstant.accessToken;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.icu.text.DecimalFormat;
import android.icu.text.DecimalFormatSymbols;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.util.Xml;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.SphericalUtil;
import com.socatra.excutivechain.AppConstant;
import com.socatra.excutivechain.AppHelper;
import com.socatra.excutivechain.R;
import com.socatra.excutivechain.adapters.CoordinatesKmlAdapter;
import com.socatra.excutivechain.api.AppAPI;
import com.socatra.excutivechain.api.webservice.Retrofit_funtion_class;
import com.socatra.excutivechain.database.entity.PlantationGeoBoundaries;
import com.socatra.excutivechain.view_models.AppViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KMLMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    String mTag="KMLTag1";
    public AppHelper appHelper;
    private static final int PICK_FILE = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;

    private CardView chooseFileButton,saveButtonKml;

    TextView txtGPSArea;
    //    private ListView latLngListView;
    private RecyclerView latLngListView;

    CoordinatesKmlAdapter adapter;
    private List<String> latLngList;

    List<LatLng> GetlatLngList;
    String strDistanceArea ;
    Integer gpsCat = 0;
    double area=0.0;

    String PlotId = "",farmerCode="",id="";
    double totalSize;// for size

    ImageView imgBackKml;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    public AppViewModel viewModel;

    File selectedFile;

    String selectedFilePath="";

    String extraLatLon="";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kml_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_kml);
        mapFragment.getMapAsync(this);

        configureDagger();
        configureViewModel();

        PlotId = getIntent().getStringExtra("PlotId");
        farmerCode = getIntent().getStringExtra("FarmerCode");
        id = getIntent().getStringExtra("id");
        gpsCat = getIntent().getIntExtra("gpsCat", 0);
        totalSize=Double.parseDouble(getIntent().getStringExtra("ProvideSize"));
        appHelper=new AppHelper(this);
        chooseFileButton = findViewById(R.id.cvImport);
        saveButtonKml = findViewById(R.id.cvSave);
        imgBackKml = findViewById(R.id.img_back_kml);
        latLngListView = findViewById(R.id.rec_list_kml);
        txtGPSArea = findViewById(R.id.txt_kml_area);
        latLngListView.setLayoutManager(new LinearLayoutManager(this));


        latLngList = new ArrayList<>();
        GetlatLngList = new ArrayList<>();

        txtGPSArea.setText("Area :" + " 00.00 Hector");

        chooseFileButton.setOnClickListener(v -> {
            if (checkPermission()) {
                openFilePicker();
            } else {
                requestPermission();
            }
        });

        saveButtonKml.setOnClickListener(v->{
            if (GetlatLngList.size()>0) {
               saveLatLngToDb(GetlatLngList);
            }
        });

        imgBackKml.setOnClickListener(v -> finish());

    }

    private void saveLatLngToDb(List<LatLng> latLngLists) {
        for (int i=0;i<latLngLists.size()-1;i++){
            Log.e(mTag,"Save LatLng:"+String.valueOf(latLngLists.get(i).latitude)
                    +","+String.valueOf(latLngLists.get(i).longitude));


            String dateTime = getCurrentDateTime(AppConstant.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);


            //Todo:Plantation Geo

            PlantationGeoBoundaries geoBoundary=new PlantationGeoBoundaries();
            geoBoundary.setPlotCode(PlotId);
            geoBoundary.setFarmerCode(farmerCode);
            geoBoundary.setLatitude(latLngLists.get(i).latitude);
            geoBoundary.setLongitude(latLngLists.get(i).longitude);
            geoBoundary.setSeqNo(i+1);
            geoBoundary.setPlotCount(gpsCat + 1);
            geoBoundary.setIsActive("true");
            geoBoundary.setCreatedByUserId(id);
            geoBoundary.setUpdatedByUserId(id);
            geoBoundary.setSync(false);
            geoBoundary.setServerSync("0");
            geoBoundary.setCreatedDate(dateTime);
            geoBoundary.setUpdatedDate(dateTime);


            insertOrUpdateGeoBoundariesDataToServer(geoBoundary);
            viewModel.updatePlotDetailListTableSyncAndPlotArea1(false, "0", Double.parseDouble(strDistanceArea), PlotId);
            if (i == latLngLists.size() - 2) {
                Toast.makeText(KMLMapsActivity.this, "Geobounds details are saved successfully", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        Intent intent = new Intent();
                       // intent.putExtra("areaGeo",area);
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                }, 1 * 500);

            }
        }
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

    private void configureDagger() {
        AndroidInjection.inject(this);

    }

    private void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);
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
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap=googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setIndoorEnabled(true);
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openFilePicker();
            } else {
//                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                openFilePicker();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void openFilePicker() {
        GetlatLngList.clear();
        latLngList.clear();

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");  //Allow all file types
//        String[] mimeTypes = {"application/vnd.google-earth.kml+xml", "application/x-esri-shape", "application/geo+json", "application/json","application/shp"};
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);  // Specify allowed MIME types
        startActivityForResult(intent, PICK_FILE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //new
        if (requestCode == PICK_FILE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Log.e(mTag, uri.toString());
            if (uri != null) {
                String fileType = getContentResolver().getType(uri);
                if (fileType != null) {
                    if (fileType.equals("application/vnd.google-earth.kml+xml")) {
                        // Handle KML file
                        readKMLFile(uri);
                    } else if (fileType.equals("application/x-esri-shape")) {
                        // Handle SHP file
                        readSHPFile(uri);
                        Log.e(mTag,"Eshp"+uri);
                    } else if (fileType.equals("application/geo+json")) {
                        // Handle GeoJSON file
                        readGeoJSONFile(uri);
                        Log.e(mTag,"geojsonuri"+uri);
                    } else if (fileType.equals("application/json")) {
                        // Handle JSON file
                        readGeoJSONFile(uri);
                        Log.e(mTag,"jsonuri"+uri);
                    }else if (fileType.equals("application/shp")) {
                        // Handle JSON file
//                        readGeoJSONFile(uri);
                        Log.e(mTag,"shp"+uri);
                        shapeFileResult(uri);
                    } else if (fileType.equals("application/pdf")) {
                        // Handle PDF file
                        Toast.makeText(this, "Please select valid file!!", Toast.LENGTH_SHORT).show();
                    } else if (fileType.equals("application/msword") || fileType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
                        // Handle DOC or DOCX file
                        Toast.makeText(this, "Please select valid file!!", Toast.LENGTH_SHORT).show();
                    } else if (fileType.equals("application/vnd.ms-excel")) {
                        // Handle Excel file
                        Toast.makeText(this, "Please select valid file!!", Toast.LENGTH_SHORT).show();
                    } else if (fileType.equals("application/zip")) {
                        // Handle ZIP file
                        Toast.makeText(this, "Please select valid file!!", Toast.LENGTH_SHORT).show();
                    } else if (fileType.equals("application/x-rar-compressed")) {
                        // Handle RAR file
                        Toast.makeText(this, "Please select valid file!!", Toast.LENGTH_SHORT).show();
                    } else if (fileType.equals("image/gif")) {
                        // Handle GIF image
                        Toast.makeText(this, "Please select valid file!!", Toast.LENGTH_SHORT).show();
                    } else if (fileType.equals("image/png")) {
                        // Handle PNG image
                        Toast.makeText(this, "Please select valid file!!", Toast.LENGTH_SHORT).show();
                    } else if (fileType.equals("image/svg+xml")) {
                        // Handle SVG image
                        Toast.makeText(this, "Please select valid file!!", Toast.LENGTH_SHORT).show();
                    } else if (fileType.equals("image/jpeg") || fileType.equals("image/jpg") || fileType.equals("image/jfif") || fileType.equals("image/pjpeg") || fileType.equals("image/pjp")) {
                        // Handle JPEG image
                        Toast.makeText(this, "Please select valid file!!", Toast.LENGTH_SHORT).show();
                    } else if (fileType.equals("application/x-7z-compressed")) {
                        // Handle 7zip file
                        Toast.makeText(this, "Please select valid file!!", Toast.LENGTH_SHORT).show();
                    } else if (fileType.equals("application/vnd.android.package-archive")) {
                        // Handle APK file
                        Toast.makeText(this, "Please select valid file!!", Toast.LENGTH_SHORT).show();
                    } else if (fileType.equals("text/plain")) {
                        // Handle TXT file
                        Toast.makeText(this, "Please select valid file!!", Toast.LENGTH_SHORT).show();
                    } else if (fileType.equals("video/mp4")) {
                        // Handle MP4 video
                        Toast.makeText(this, "Please select valid file!!", Toast.LENGTH_SHORT).show();
                    } else if (fileType.equals("video/x-msvideo")) {
                        // Handle AVI video
                        Toast.makeText(this, "Please select valid file!!", Toast.LENGTH_SHORT).show();
                    } else if (fileType.equals("video/x-matroska")) {
                        // Handle MKV video
                        Toast.makeText(this, "Please select valid file!!", Toast.LENGTH_SHORT).show();
                    } else if (fileType.equals("video/webm")) {
                        // Handle WEBM video
                        Toast.makeText(this, "Please select valid file!!", Toast.LENGTH_SHORT).show();
                    } else if (fileType.equals("text/html")) {
                        // Handle HTML5 file
                        Toast.makeText(this, "Please select valid file!!", Toast.LENGTH_SHORT).show();
                    } else if (fileType.equals("video/quicktime")) {
                        // Handle MOV video
                        Toast.makeText(this, "Please select valid file!!", Toast.LENGTH_SHORT).show();
                    } else if (fileType.equals("video/x-flv")) {
                        // Handle FLV video
                        Toast.makeText(this, "Please select valid file!!", Toast.LENGTH_SHORT).show();
                    } else if (fileType.equals("application/x-sqlite3")) {
                        // Handle SQLite database file
                        Toast.makeText(this, "Please select valid file!!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // Handle other file types if needed
                        // You can add more file type checks here
                        shapeFileResult(uri);
//                        Toast.makeText(this, "Please select valid file!!", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    // Handle the case where the file type couldn't be determined
                    Toast.makeText(this, "Please select valid file!!", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private void shapeFileResult(Uri uri) {
        Log.e(mTag,"OtherUri here");
        Log.e(mTag,"shpOther"+uri);
        selectedFilePath=uri.getPath();

        try {
            selectedFile=appHelper.convertUriToFile(KMLMapsActivity.this,uri);
            Log.e(mTag,"shpOtherpath:"+selectedFilePath);
            uploadShapeFileToServer(selectedFile);
//                        selectedFile = new File(selectedFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void readGeoJSONFile(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                String geoJSONString = stringBuilder.toString();

                // Now, you have the GeoJSON data in geoJSONString. You can parse it as needed.
                parseGeoJSON(geoJSONString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseGeoJSON(String geoJSONString) {
        try {
            JSONObject geoJSON = new JSONObject(geoJSONString);

            // Ensure that the GeoJSON type is "FeatureCollection"
            if (geoJSON.has("type") && geoJSON.getString("type").equals("FeatureCollection")) {
                JSONArray features = geoJSON.getJSONArray("features");
                for (int i = 0; i < features.length(); i++) {
                    JSONObject feature = features.getJSONObject(i);

                    // Access properties
                    JSONObject properties = feature.getJSONObject("properties");
                    String plotCode = properties.optString("PlotCode", "");

                    // Access geometry
                    JSONObject geometry = feature.getJSONObject("geometry");
                    String geometryType = geometry.optString("type", "");

                    // Depending on the geometry type, you may need different processing
                    if (geometryType.equals("Polygon")) {
                        JSONArray coordinatesArray = geometry.getJSONArray("coordinates");

                        // Assuming there is only one polygon in this example
                        JSONArray polygonCoordinates = coordinatesArray.getJSONArray(0);

                        // Process polygon coordinates
                        for (int j = 0; j < polygonCoordinates.length(); j++) {
                            JSONArray point = polygonCoordinates.getJSONArray(j);
                            double longitude = point.getDouble(0);
                            double latitude = point.getDouble(1);

                            // Do something with latitude and longitude
                            // For example, you can add them to a list or display them
                            String coordinate = "Latitude: " + latitude + ", Longitude: " + longitude;
                            latLngList.add(latitude + "," +longitude);
                            LatLng latLng1=new LatLng(latitude,longitude);
                            GetlatLngList.add(latLng1);
                            Log.e(mTag, "geojsonlist:"+coordinate);
                        }
                    }
                }
                plotCoordinatesOnMap(latLngList);
                Set<String> uniqueSet = new HashSet<>(latLngList);
                // Convert the Set back to a List (removes duplicates)
                List<String> uniqueList = new ArrayList<>(uniqueSet);

                adapter=new CoordinatesKmlAdapter(this,uniqueList);
                latLngListView.setAdapter(adapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void readSHPFile(Uri uri) {
        //todo: need to add
    }



//    private File saveShapefileToLocal(Uri uri) {
//        try {
//            InputStream inputStream = getContentResolver().openInputStream(uri);
//            File tempFile = File.createTempFile("temp_shapefile", ".shp", getCacheDir());
//            FileOutputStream outputStream = new FileOutputStream(tempFile);
//            byte[] buffer = new byte[1024];
//            int bytesRead;
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, bytesRead);
//            }
//            inputStream.close();
//            outputStream.close();
//            return tempFile;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//    private void readSHPFile(Uri uri) {
//        File shapefile = saveShapefileToLocal(uri);
//        if (shapefile != null) {
//            try {
//                SimpleFeatureCollection featureCollection = readShapefile(shapefile.getAbsolutePath());
//                // Now you have the featureCollection to work with
//                drawPolygonOnMap(featureCollection);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public SimpleFeatureCollection readShapefile(String shapefilePath) throws Exception {
//        File shapefile = new File(shapefilePath);
//
//        ShapefileDataStore dataStore = new ShapefileDataStore(shapefile.toURI().toURL());
//        SimpleFeatureSource featureSource = dataStore.getFeatureSource();
//
//        return featureSource.getFeatures();
//    }
//
//    private void drawPolygonOnMap(SimpleFeatureCollection featureCollection) {
//        FeatureIterator<SimpleFeature> features = featureCollection.features();
//        while (features.hasNext()) {
//            SimpleFeature feature = features.next();
//            GeometryAttribute geomAttribute = feature.getDefaultGeometryProperty();
//
//            if (geomAttribute != null) {
//                Geometry geometry = (Geometry) geomAttribute.getValue();
//                if (geometry instanceof Polygon) {
//                    Polygon polygon = (Polygon) geometry;
//                    List<LatLng> coordinates = new ArrayList<>();
//
//                    Coordinate[] coords = polygon.getCoordinates();
//                    for (Coordinate coord : coords) {
//                        coordinates.add(new LatLng(coord.y, coord.x)); // Assuming y is latitude and x is longitude
//                    }
//
//                    // Draw the polygon on the map
//                    mMap.addPolygon(new PolygonOptions()
//                            .addAll(coordinates)
//                            .strokeColor(Color.RED)
//                            .fillColor(Color.BLUE));
//                }
//            }
//        }
//    }

    private void readKMLFile(Uri uri) {
        latLngList.clear();
        GetlatLngList.clear();
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(inputStream, null);

                int eventType = parser.getEventType();
                String currentTag = "";
                StringBuilder coordinates = new StringBuilder();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            currentTag = parser.getName();
                            break;

                        case XmlPullParser.TEXT:
                            if ("coordinates".equals(currentTag)) {
                                coordinates.append(parser.getText());
                            }
                            break;

                        case XmlPullParser.END_TAG:
                            if ("coordinates".equals(parser.getName())) {
                                // Split and parse the coordinates
                                String[] latLngArray = coordinates.toString().trim().split(" ");
                                for (String latLngStr : latLngArray) {
                                    String[] latLng = latLngStr.split(",");
                                    if (latLng.length == 3) {
                                        // The format is "longitude,latitude,altitude"
                                        double latitude = Double.parseDouble(latLng[1]);
                                        double longitude = Double.parseDouble(latLng[0]);
//                                        latLngList.add("Latitude: " + latitude + ", Longitude: " + longitude);
                                        latLngList.add(latitude + "," +longitude);
                                        LatLng latLng1=new LatLng(latitude,longitude);
                                        GetlatLngList.add(latLng1);
                                    }
                                }
                                coordinates.setLength(0); // Reset the StringBuilder
                            }
                            break;
                    }

                    eventType = parser.next();
                }
                plotCoordinatesOnMap(latLngList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_list_item_1, latLngList);
        Set<String> uniqueSet = new HashSet<>(latLngList);

// Convert the Set back to a List (removes duplicates)
        List<String> uniqueList = new ArrayList<>(uniqueSet);

        adapter=new CoordinatesKmlAdapter(this,uniqueList);
        latLngListView.setAdapter(adapter);


    }


    private void plotCoordinatesOnMap(List<String> coordinatesList) {
        mMap.clear(); // Clear any existing markers and polygons on the map

        if (coordinatesList.isEmpty()) {
            return; // Nothing to plot
        }

        // Create a LatLngBounds builder to encompass all coordinates
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

        // Parse the first coordinates to set the initial camera position
        String[] firstCoordinateParts = coordinatesList.get(0).split(",");
        if (firstCoordinateParts.length == 2) {
            double latitude = Double.parseDouble(firstCoordinateParts[0]);
            double longitude = Double.parseDouble(firstCoordinateParts[1]);

            LatLng initialLatLng = new LatLng(latitude, longitude);

            // Create a camera position with the initial coordinates and zoom level
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(initialLatLng)
                    .zoom(14) // Adjust the zoom level as needed
                    .build();

            // Move the camera to the initial position
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            // Add markers for all coordinates
            for (String coordinateStr : coordinatesList) {
                String[] parts = coordinateStr.split(",");
                if (parts.length == 2) {
                    latitude = Double.parseDouble(parts[0]);
                    longitude = Double.parseDouble(parts[1]);

                    LatLng latLng = new LatLng(latitude, longitude);

                    // Add a marker on the map at the specified coordinates
                    mMap.addMarker(new MarkerOptions().position(latLng).title(String.valueOf(latitude) + "," + String.valueOf(longitude)));

                    // Extend the bounds to include this marker
                    boundsBuilder.include(latLng);
                }
            }

            // Define the polygon's points using the coordinates list
            List<LatLng> polygonPoints = new ArrayList<>();
            for (String coordinateStr : coordinatesList) {
                String[] parts = coordinateStr.split(",");
                if (parts.length == 2) {
                    double lat = Double.parseDouble(parts[0]);
                    double lng = Double.parseDouble(parts[1]);
                    polygonPoints.add(new LatLng(lat, lng));
                }
            }

            // Add a polygon to the map
            mMap.addPolygon(new PolygonOptions()
                    .addAll(polygonPoints)
                    .strokeWidth(2)
                    .strokeColor(Color.GREEN)
                    .fillColor(Color.argb(128, 0, 255, 0))); // Adjust stroke and fill colors as needed


            double distance = SphericalUtil.computeArea(polygonPoints);
//                    dist = String.valueOf(SphericalUtil.computeArea(latLngList)*0.000247105);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                DecimalFormatSymbols symbols = new DecimalFormatSymbols();//dec formatter
                symbols.setDecimalSeparator('.');//dec formatter
                DecimalFormat df_obj = new DecimalFormat("#.###",symbols);
                df_obj.format(distance*0.000247105);
                strDistanceArea = String.valueOf(df_obj.format(SphericalUtil.computeArea(polygonPoints)*0.000247105*0.404686));
                txtGPSArea.setText("Area : " + String.valueOf(df_obj.format(distance*0.000247105*0.404686)) + " Hector");
            }

        }

        // Fit the camera to the bounds to ensure all markers and the polygon are visible
        LatLngBounds bounds = boundsBuilder.build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100)); // Adjust padding as needed
    }

    public static String getMimeType(String url) {

        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File file = new File(path, url);

        try {
            // Make sure the Pictures directory exists.
            path.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return String.valueOf(file);
    }


    private void uploadShapeFileToServer(File uploadFile) {
        ProgressDialog progressDialog = new ProgressDialog(KMLMapsActivity.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Log.d(mTag, "upload shpe file to server : " + uploadFile.getAbsolutePath());

        MultipartBody.Part file_pathDB = null;
        File file = new File(uploadFile.getAbsolutePath());

        // Parsing any Media type file
        RequestBody requestBody = RequestBody.create(MediaType.parse(getMimeType(String.valueOf(uploadFile))), uploadFile);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", uploadFile.getName(), requestBody);
        Log.d("Filepath", ">>>>>>>>>>" + fileToUpload);
        RequestBody r_acces_token = RequestBody.create(MediaType.parse("multipart/form-data"),
                appHelper.getSharedPrefObj().getString(accessToken, ""));

        RequestBody r_userID = RequestBody.create(MediaType.parse("multipart/form-data"),
                appHelper.getSharedPrefObj().getString(DeviceUserID, ""));

        RequestBody r_plotCode = RequestBody.create(MediaType.parse("multipart/form-data"),
                PlotId);
        Log.d(mTag, "plotCode: " + r_plotCode);
        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<ResponseBody> callRetrofit = null;
        callRetrofit = service.uploadShapeFileToserver(r_plotCode, fileToUpload);
        callRetrofit.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    progressDialog.dismiss();
                    String strResponse = response.body().string();
                    Log.d(mTag, "onResponse: SHAPE DATA : " + response);
                    JSONObject json_object = new JSONObject(strResponse);
//                    String message = "", status = "";
                    Log.e(mTag, "onResponse: data json" + json_object.getJSONArray("data").getJSONObject(0).getJSONArray("arraylist"));

                    JSONArray array=json_object.getJSONArray("data").getJSONObject(0).getJSONArray("arraylist");
                    Log.e(mTag, String.valueOf(array));
//                    for (int i=0;i<array.length();i++){
//                        JSONArray coordinates = array.getJSONArray(i);
//                        String lat = coordinates.getJSONObject(i).getString("lat");
//                        String lon = coordinates.getJSONObject(i).getString("long");
//
//
//                        latLngList.add(lat+","+lon);
//                    }
                    for (int i = 0; i < array.length(); i++) {
                        JSONArray coordinates = array.getJSONArray(i);

                        // Loop through coordinates in the array
                        for (int j = 0; j < coordinates.length(); j++) {
                            String lat = coordinates.getJSONObject(j).getString("lat");
                            String  lon = coordinates.getJSONObject(j).getString("long");
                            latLngList.add(lat+","+lon);
                            // Print each set of coordinates
//                            System.out.println("Latitude: " + lat);
//                            System.out.println("Longitude: " + lon);
                            LatLng latLng=new LatLng(Double.parseDouble(lat),Double.parseDouble(lon));
                            GetlatLngList.add(latLng);
                            extraLatLon=lat+","+lon;
                        }
                    }

                    latLngList.add(extraLatLon);
                    GetlatLngList.add(new LatLng(0.0,0.0));


                    Log.e(mTag,"onResponse: List:"+latLngList.toString());
                    Set<String> uniqueSet = new HashSet<>(latLngList);

                    // Convert the Set back to a List (removes duplicates)
                    List<String> uniqueList = new ArrayList<>(uniqueSet);

                    adapter=new CoordinatesKmlAdapter(KMLMapsActivity.this,uniqueList);
                    latLngListView.setAdapter(adapter);
                    plotCoordinatesOnMap(latLngList);

                    Log.e(mTag, String.valueOf(latLngList.size()));
                    Log.e(mTag, String.valueOf(GetlatLngList.size()));



//                    message = json_object.getString("message");
//                    status = json_object.getString("status");
//                    Log.d(TAG, "status " + status);
//                    if (status.equals("1")) {
//                        Toast.makeText(SyncActivity.this, message, Toast.LENGTH_SHORT).show();
//                    } else if (status.equals("0")) {
//                        Toast.makeText(SyncActivity.this, message, Toast.LENGTH_SHORT).show();
//                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                    progressDialog.dismiss();

                    Log.d("Error Call", ">>>>" + ex.toString());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                progressDialog.dismiss();
            }
        });
    }



}