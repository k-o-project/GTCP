package com.zbqmal.gtcp_10.Account;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zbqmal.gtcp_10.Main.MainActivity;
import com.zbqmal.gtcp_10.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    //widgets
    private EditText userIdInput;
    private Button verifyBtn;
    private RadioGroup userTypeRadioGroup;
    private RadioButton userTypeRadioButton;

    //firebase database
    private FirebaseDatabase mFirebasDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        verifyBtn = findViewById(R.id.verifyForgotPasswordBtn);
        userIdInput = findViewById(R.id.UserIdNum);
        userTypeRadioGroup = findViewById(R.id.forgotUserType);

        mFirebasDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        verifyBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (userIdInput.getText().toString().isEmpty()) {

                    userIdInput.setError("Enter your ID number");

                } else if (userIdInput.getText().toString().length() != 9) {

                    userIdInput.setError("Invalid ID number");

                } else {
                    int selectedUserTypeID = userTypeRadioGroup.getCheckedRadioButtonId();
                    userTypeRadioButton = findViewById(selectedUserTypeID);
                    String selectedUserType = userTypeRadioButton.getText().toString().toLowerCase();


                    //student user
                    if (selectedUserType.equals("student")) {

                        myRef = FirebaseDatabase.getInstance().getReference("gtcp/user/student").child(userIdInput.getText().toString());


                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    DataSnapshot userEmailAddr = dataSnapshot.child("emailAddress");
                                    String userEmail = userEmailAddr.getValue().toString();

                                    mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(ForgotPasswordActivity.this, "Reset password link sent", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);


                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(ForgotPasswordActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                } else {
                                    Toast.makeText(ForgotPasswordActivity.this,
                                            "This ID doesn't exist", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    // police user
                    if (selectedUserType.equals("gtpd")) {
                        myRef = FirebaseDatabase.getInstance().getReference("gtcp/user/police").child(userIdInput.getText().toString());
                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    DataSnapshot userEmailAddr = dataSnapshot.child("emailAddress");
                                    String userEmail = userEmailAddr.getValue().toString();

                                    mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(ForgotPasswordActivity.this, "Reset password link sent", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);


                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(ForgotPasswordActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                } else {
                                    Toast.makeText(ForgotPasswordActivity.this,
                                            "This ID doesn't exist", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }
            }
        });
    }

    /**
     *
     * @param view view to pass to MainActivity
     * Sends user to Main activity.
     */
    public void goToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}