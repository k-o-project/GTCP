package com.zbqmal.gtcp_10.Student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.zbqmal.gtcp_10.Account.MyAccountActivity;
import com.zbqmal.gtcp_10.R;

public class StudentHomeActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView studentMapView;
    private GoogleMap googleMap;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        studentMapView = findViewById(R.id.studentHomeMapView);
        studentMapView.onCreate(mapViewBundle);
        studentMapView.getMapAsync(this);
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
        LatLng ny = new LatLng(40.7143528, -74.0059731);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ny));
    }
}
