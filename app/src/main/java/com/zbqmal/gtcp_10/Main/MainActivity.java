package com.zbqmal.gtcp_10.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zbqmal.gtcp_10.Account.CreateAccountActivity;
import com.zbqmal.gtcp_10.Account.ForgotPasswordActivity;
import com.zbqmal.gtcp_10.Police.PoliceHomeActivity;
import com.zbqmal.gtcp_10.R;
import com.zbqmal.gtcp_10.Student.StudentHomeActivity;

public class MainActivity extends AppCompatActivity {

    // Widgets & pre-factor
    private Button loginButton;
    private EditText usernameInput;
    private EditText passwordInput;
    private RadioGroup userTypeRadioGroup;
    private RadioButton userTypeRadioButton;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private boolean mLocationPermissionGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // LOGOUT automatically everytime a user gets MainActivity
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut();
        }

        // Assign widgets & pre-factor
        mAuth = FirebaseAuth.getInstance();
        loginButton = findViewById(R.id.loginButton);
        usernameInput = findViewById(R.id.loginUsername);
        passwordInput = findViewById(R.id.loginPassword);
        userTypeRadioGroup = findViewById(R.id.login_UserTypeSelect_RadioGroup);

        // When clicked LOGIN button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButtonClicked();
            }
        });
    }

    /**
     * Check if username and password are correct, and allows a user to log in.
     */
    private void loginButtonClicked() {

        final String username = usernameInput.getText().toString().trim();
        final String password = passwordInput.getText().toString().trim();
        int selectedUserTypeID = userTypeRadioGroup.getCheckedRadioButtonId();
        userTypeRadioButton = findViewById(selectedUserTypeID);
        String selectedUserType = userTypeRadioButton.getText().toString().toLowerCase();

        if (username.isEmpty()) {
            usernameInput.setError("Enter your username");
            return;
        }

        if (password.isEmpty()) {
            passwordInput.setError("Enter your password");
            return;
        }

        /*
        TODO: More conditions using regex
         */

        // Student Login
        if (selectedUserType.equals("student")) {
            myRef = FirebaseDatabase.getInstance().getReference("gtcp/user/student").child(username);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        // Retrieve the user's uid
                        String userUID = dataSnapshot.child("uid").getValue().toString();
                        String userEmail = dataSnapshot.child("emailAddress").getValue().toString();
                        System.out.println("userEmail is " + userEmail);
                        System.out.println("================ Current User is " + mAuth.getCurrentUser());

                        /*
                        TODO: Not able to sign in yet.
                         */
                        // Sign in
                        mAuth.signInWithEmailAndPassword(userEmail, password);
                        System.out.println("================ Current User is " + mAuth.getCurrentUser());

                        // Move Activity
                        Intent intent = new Intent(MainActivity.this, StudentHomeActivity.class);
                        Bundle extras = new Bundle();

                        extras.putString("userID", username);
                        extras.putString("userUID", userUID);
                        extras.putString("userType", "student");
                        intent.putExtras(extras);
                        startActivity(intent);

                    } else {
                        Toast.makeText(MainActivity.this,
                                "This ID doesn't exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        // Police Login
        if (selectedUserType.equals("gtpd")) {
            myRef = FirebaseDatabase.getInstance().getReference("gtcp/user/police");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        // Retrieve the user's uid
                        DataSnapshot userUIDDS = dataSnapshot.child("uid");
                        DataSnapshot userEmailDS = dataSnapshot.child("emailAddress");
                        String userUID = userUIDDS.getValue().toString();
                        String userEmail = userEmailDS.getValue().toString();


                        // Sign in
                        mAuth.signInWithEmailAndPassword(userEmail, password);

                        // Move Activity
                        Intent intent = new Intent(MainActivity.this, PoliceHomeActivity.class);
                        Bundle extras = new Bundle();

                        extras.putString("userID", username);
                        extras.putString("userUID", userUID);
                        extras.putString("userType", "police");
                        intent.putExtras(extras);
                        startActivity(intent);

                    } else {
                        Toast.makeText(MainActivity.this,
                                "This ID doesn't exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    /**
     * if user type is student, access to student home activity
     *
     * @param view student home activity
     */
    public void goToStudentHomeActivity(View view) {
        Intent intent = new Intent(this, StudentHomeActivity.class);
        startActivity(intent);
    }

    /**
     * if user type is police depart., access to GTPD home activity
     *
     * @param view police home activity
     */
    public void goToPoliceHomeActivity(View view) {
        Intent intent = new Intent(this, PoliceHomeActivity.class);
        startActivity(intent);
    }

    /**
     *
     * @param view view to pass to ForgotPasswordActivity
     * Sends user to ForgotPassword activity.
     */
    public void goToForgotPassword(View view) {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    /**
     *
     * @param view view to pass to CreateAccountActivity
     * Sends user to CreateAccount activity.
     */
    public void goToCreateAccount(View view) {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }

    /**
     * Let user sign out whenever be in MainActivity.
     */
    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut();
        }
    }
}
