package com.zbqmal.gtcp_10.Student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.zbqmal.gtcp_10.Account.MyAccountActivity;
import com.zbqmal.gtcp_10.R;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class StudentHomeActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView studentMapView;
    private GoogleMap googleMap;
//    private GeoDataClient mGeoDataClient;
//    private PlaceDetectionClient mPlaceDetectionClient;
    private FusedLocationProviderClient client;
    private Button searchBtn, submitBtn;
    private EditText departureTxt, destinationTxt;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        requestPermission();

//        // Construct a GeoDataClient.
//        mGeoDataClient = Places.getGeoDataClient(this, null);
//
//        // Construct a PlaceDetectionClient.
//        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
        client = LocationServices.getFusedLocationProviderClient(this);

        searchBtn = findViewById(R.id.submitDestinationBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        studentMapView = findViewById(R.id.studentHomeMapView);
        studentMapView.onCreate(mapViewBundle);
        studentMapView.getMapAsync(this);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    public void goToStartTrackingActivity(View view) {
        Intent intent = new Intent(this, TrackingActivity.class);
        startActivity(intent);
    }

    public void goToMyAccountActivity(View view) {

        Intent intent = new Intent(this, MyAccountActivity.class);
        Bundle userData = getIntent().getExtras();

        //get user's id
        final String userID = userData.getString("ID");

        //passing user's id and type
        userData.putString("ID",userID);
        userData.putString("USERTYPE", "student");

        intent.putExtras(userData);

        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        studentMapView.onSaveInstanceState(mapViewBundle);
    }
    @Override
    protected void onResume() {
        super.onResume();
        studentMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        studentMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        studentMapView.onStop();
    }
    @Override
    protected void onPause() {
        studentMapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        studentMapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        studentMapView.onLowMemory();
    }
    @Override
    public void onMapReady(GoogleMap gMap) {
        googleMap = gMap;
        googleMap.setMinZoomPreference(12);
//        LatLng ny = new LatLng(40.7143528, -74.0059731);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ny));
        if (checkSelfPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        client.getLastLocation().addOnSuccessListener(StudentHomeActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {
                    Toast.makeText(StudentHomeActivity.this, "Success loading current location.",Toast.LENGTH_SHORT).show();
                    LatLng ny = new LatLng(location.getLatitude(), location.getLongitude());
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(ny));
                } else {
                    Toast.makeText(StudentHomeActivity.this, "Failed to load current location.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
