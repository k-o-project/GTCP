package com.zbqmal.gtcp_10.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zbqmal.gtcp_10.Account.MyAccountActivity;
import com.zbqmal.gtcp_10.R;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class StudentHomeActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    private MapView studentMapView;
    private GoogleMap googleMap;
    private FusedLocationProviderClient client;
    private Button submitBtn;
    private Spinner destinationSpinner;
    private DatabaseReference mRootRef;
    private Bundle extras;
    private String userID;


    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        requestPermission();

        // Construct a FusedLocationProviderClient.
        client = LocationServices.getFusedLocationProviderClient(this);

        submitBtn = findViewById(R.id.studentDirectionSubmitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Set destination spinner
                destinationSpinner = findViewById(R.id.studentDestinationSpinner);
                String destination = destinationSpinner.getSelectedItem().toString();

                // When nothing selected
                if (destination.equals("N/A")) {
                    Toast errorToastMessage = Toast.makeText(getApplicationContext(),
                            "PLEASE SELECT YOUR DESTINATION", Toast.LENGTH_SHORT);
                    errorToastMessage.show();
                    return;
                }

                // Update current location and destination in Firebase
                mRootRef = FirebaseDatabase.getInstance().getReference();
                extras = getIntent().getExtras();
                userID = extras.getString("userID");
                mRootRef.child("gtcp/user/student").child(userID).child("destination").setValue(destination);

                fetchLastLocation();

                Intent intent = new Intent(StudentHomeActivity.this, TrackingActivity.class);
                startActivity(intent);
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

    public void goToMyAccountActivity(View view) {

        Intent intent = new Intent(this, MyAccountActivity.class);
        Bundle userData = getIntent().getExtras();

        //get user's id
        final String userID = userData.getString("ID");

        //passing user's id and type
        userData.putString("ID", userID);
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

        if (checkSelfPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        client.getLastLocation().addOnSuccessListener(StudentHomeActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {
                    Toast.makeText(StudentHomeActivity.this, "Success loading current location.", Toast.LENGTH_SHORT).show();
                    // TODO: Check This is really loading the current location
                    LatLng ny = new LatLng(location.getLatitude(), location.getLongitude());
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(ny));
                } else {
                    Toast.makeText(StudentHomeActivity.this, "Failed to load current location.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // TODO: This still makes it reloads the current activity. Solve this.
    private void fetchLastLocation() {

        if (ContextCompat.checkSelfPermission(StudentHomeActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(StudentHomeActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                new AlertDialog.Builder(this)
                        .setTitle("Required Location Permission")
                        .setMessage("You have to give this permission to access this feature")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(StudentHomeActivity.this,
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(StudentHomeActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            client.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                Double latittude = location.getLatitude();
                                Double longitude = location.getLongitude();
                                LatLng ny = new LatLng(location.getLatitude(), location.getLongitude());
                                googleMap.moveCamera(CameraUpdateFactory.newLatLng(ny));

                                mRootRef = FirebaseDatabase.getInstance().getReference();
                                extras = getIntent().getExtras();
                                userID = extras.getString("userID");
//                                mRootRef.child("gtcp/user/student").child(userID).child("currentLat").setValue(latittude);
//                                mRootRef.child("gtcp/user/student").child(userID).child("currentLng").setValue(longitude);
                            }
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {

            }
        }
    }
}
