package com.zbqmal.gtcp_10.Police;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.zbqmal.gtcp_10.Account.MyAccountActivity;
import com.zbqmal.gtcp_10.Profile.Police;
import com.zbqmal.gtcp_10.Profile.Student;
import com.zbqmal.gtcp_10.R;
//import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static com.zbqmal.gtcp_10.Util.Constants.ERROR_DIALOG_REQUEST;
import static com.zbqmal.gtcp_10.Util.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.zbqmal.gtcp_10.Util.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

public class PoliceHomeActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mMapView;
    private GoogleMap googleMap;

    private boolean mLocationPermissionGranted = false;

    private FusedLocationProviderClient mFusedLocationClient;

    private Set<String> mStudentIds = new HashSet<>();
    private RecyclerView mUserListRecyclerView;
    private ArrayList<Student> mUSerList = new ArrayList<>();

    //private ListenerRegistration mEventListener;

    private static String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_home);

        mUserListRecyclerView = findViewById(R.id.user_list_recycler_view);
        mMapView = (MapView) findViewById(R.id.user_list_map);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        initRecyclerView();
        initGoogleMap(savedInstanceState);

    }

    private void getLastKnownLocation() {

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {

                    Location location = task.getResult();


                    try {
                        Geocoder geocoder = new Geocoder(PoliceHomeActivity.this);
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        System.out.println("****** address" + addresses);
                    } catch (Exception e) {

                    }
                }
            }
        });
    }

    private void initGoogleMap(Bundle savedInstanceState) {

        //bundle contain only MapView SDK obj or sub bundles
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);

    }

    private boolean checkMapServices() {
        if (isServicesOk()) {
            if (isMapsEnabled()) {
                return true;
            }
        }
        return false;
    }

    /**
     * send alert message to the user when there is no GPS disabled
     */
    private void buildAlertMsgNoGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This app requires GPS, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(@SuppressWarnings("unsued") final DialogInterface dialog,
                                        @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS); // GPS setting scene
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * determine if GPS enabled on the user's device
     * @return
     */
    public boolean isMapsEnabled() {

        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMsgNoGPS();
            return false;
        }
        return true;
    }

    /**
     * Request location permission for getting location of the device.
     * This will handled by callback, onRequestPermissionResult.
     */
    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            getStudentLists(); //start intent (main screen)
            getLastKnownLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public boolean isServicesOk() {

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(PoliceHomeActivity.this);

        if (available == ConnectionResult.SUCCESS) { // working fine and user able to make map requests
            return true;

        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //error but can be fixed
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(PoliceHomeActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();

        } else {
            //cannot solve the error
            Toast.makeText(this, "Cannot make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    /**
     * check request result
     * @param code request code
     * @param permissions request permission
     * @param grantResults granted request result
     */
    @Override
    public void onRequestPermissionsResult(int code, @NonNull String permissions[], @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;

        switch (code) {

            //request cancelled and result is empty
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                if (mLocationPermissionGranted) { //gps is enabled, load student lists
                    getStudentLists();
                    getLastKnownLocation();
                } else {
                    getLocationPermission(); //get permission to access location
                }
            }
        }
    }

    private void initRecyclerView() {
        mUserListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getStudentLists() {


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();

        if (checkMapServices()) {
            if (mLocationPermissionGranted) {
                getStudentLists();
                getLastKnownLocation();
            } else {
                getLocationPermission(); //need to set booleans to true
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public void goToMyAccountActivity(View view) {

        Intent intent = new Intent(this, MyAccountActivity.class);
        Bundle userData = getIntent().getExtras();

        //passing user type to MyAccountActivity
        userData.putString("USERTYPE", "police");
        intent.putExtras(userData);
        startActivity(intent);
    }
}
