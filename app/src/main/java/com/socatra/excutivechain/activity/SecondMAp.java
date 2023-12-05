package com.socatra.excutivechain.activity;

import androidx.fragment.app.FragmentActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.kml.KmlLayer;
import com.socatra.excutivechain.R;
import com.socatra.excutivechain.databinding.ActivitySecondMapBinding;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class SecondMAp extends FragmentActivity implements OnMapReadyCallback {
    String TAG="SecondMAp";
    private GoogleMap mMap;
    private ActivitySecondMapBinding binding;
    Uri kmlUri;
    KmlLayer kmlLayer = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySecondMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String uriString =  getIntent().getStringExtra("selected_uri");
        Log.e(TAG, "onCreate:value "+ uriString );
        if (uriString != null) {
            Uri urlKmlUri = Uri.parse(uriString);
            kmlUri = urlKmlUri;
            Log.d(TAG, "onCreate:data "+ kmlUri );
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        try {
       //     InputStream inputStream = getContentResolver().openInputStream(kmlUri);

            InputStream inputStream = getContentResolver().openInputStream(kmlUri);
            Log.d(TAG, "onMapReady: inputstream " + inputStream);
//            KmlLayer kmlLayer = new KmlLayer(mMap, inputStream, getApplicationContext());
//            kmlLayer.addLayerToMap();
//            List<LatLng> coordinates = parseKML(inputStream);
//            Log.d(TAG, "onMapReady: coordinates" + coordinates);
//            drawPolygon(coordinates);

            List<LatLng> coordinates = parseKML(inputStream);

            // Add a marker for each coordinate point (optional)
            for (LatLng latLng : coordinates) {
                mMap.addMarker(new MarkerOptions().position(latLng));
            }

            // Draw the polygon on the map
            if (!coordinates.isEmpty()) {
                PolygonOptions polygonOptions = new PolygonOptions().addAll(coordinates);
                mMap.addPolygon(polygonOptions);

                // Optionally, move the camera to center the map on the polygon
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates.get(0), 12));
            }
//            LatLngBounds.Builder builder = new LatLngBounds.Builder();
//            for (KmlPlacemark placemark : kmlLayer.getPlacemarks()) {
//                if (placemark.getGeometry() instanceof KmlPolygon) {
//                    KmlPolygon polygon = (KmlPolygon) placemark.getGeometry();
//                    List<LatLng> coordinates = polygon.getOuterBoundaryCoordinates();
//                    builder.include(coordinates);
//                }
//            }
//
//            // Set the map camera position to fit the KML data
//            LatLngBounds bounds = builder.build();
//            // Optionally, zoom to the KML layer's extent
//            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));


   //         mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(kmlLayer.getBoundingBox(), 50));
//            InputStream inputStream = getContentResolver().openInputStream(kmlUri);
//            kmlLayer = new KmlLayer(mMap, inputStream, SecondMAp.this);
//            kmlLayer.addLayerToMap();
//
//            PolylineOptions polylineOptions = new PolylineOptions();
//            for (LatLng latLng : coordinatesList) {
//                polylineOptions.add(latLng);
//            }
//            mMap.addPolyline(polylineOptions);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private List<LatLng> parseKML(InputStream inputStream) throws IOException, XmlPullParserException {
        List<LatLng> coordinates = new ArrayList<>();
        XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
        XmlPullParser parser = xmlFactoryObject.newPullParser();
        parser.setInput(inputStream, null);

        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && parser.getName().equals("coordinates")) {
                String coords = parser.nextText();
                String[] latLngs = coords.split("\\s+"); // Split by spaces
                for (String latLngStr : latLngs) {
                    String[] latLngArray = latLngStr.split(",");
                    if (latLngArray.length == 2) {
                        double lat = Double.parseDouble(latLngArray[1]);
                        double lng = Double.parseDouble(latLngArray[0]);
                        coordinates.add(new LatLng(lat, lng));
                    }
                }
            }
            eventType = parser.next();
        }
        return coordinates;
    }
    private void drawPolygon(List<LatLng> coordinates) {
        PolygonOptions polygonOptions = new PolygonOptions();
        for (LatLng latLng : coordinates) {
            polygonOptions.add(latLng);
        }
        Polygon polygon = mMap.addPolygon(polygonOptions);
        polygon.setStrokeWidth(2); // Set polygon line width
        polygon.setStrokeColor(0xFF00FF00); // Set polygon line color
        polygon.setFillColor(0x7F00FF00); // Set polygon fill color with alpha
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates.get(0), 15)); // Adjust map camera position and zoom level
    }
}