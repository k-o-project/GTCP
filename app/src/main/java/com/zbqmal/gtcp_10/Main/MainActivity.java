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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
    private FirebaseUser mUser;

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
        TODO: More conditions (using regex and so on)
         */

        // Student Login
        if (selectedUserType.equals("student")) {
            myRef = FirebaseDatabase.getInstance().getReference("gtcp/user/student").child(username);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        // Sign in
                        String userEmail = dataSnapshot.child("emailAddress").getValue().toString();
                        signInStudentUser(userEmail, password);
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
            myRef = FirebaseDatabase.getInstance().getReference("gtcp/user/police").child(username);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        // Sign in
                        String userEmail = dataSnapshot.child("emailAddress").getValue().toString();
                        signInPoliceUser(userEmail, password);

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

    /**
     * A student user Sign in with inputs of email address and password by user.
     *
     * @param email user's email address
     * @param password user's password
     */
    private void signInStudentUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this,
                                    "Login Successful", Toast.LENGTH_SHORT).show();

                            // Move to userHomeActivity
                            Intent intent = new Intent(MainActivity.this, StudentHomeActivity.class);
                            Bundle extras = new Bundle();
                            extras.putString("userUID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            EditText loginUsername = findViewById(R.id.loginUsername);
                            extras.putString("userID", loginUsername.getText().toString().trim());
                            intent.putExtras(extras);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this,
                                    "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * A police user Sign in with inputs of email address and password by user.
     *
     * @param email user's email address
     * @param password user's password
     */
    private void signInPoliceUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this,
                                    "Login Successful", Toast.LENGTH_SHORT).show();

                            // Move to userHomeActivity
                            Intent intent = new Intent(MainActivity.this, PoliceHomeActivity.class);
                            Bundle extras = new Bundle();
                            extras.putString("userUID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            intent.putExtras(extras);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this,
                                    "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
