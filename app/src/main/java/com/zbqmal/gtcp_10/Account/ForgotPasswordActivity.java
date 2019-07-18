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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        verifyBtn = findViewById(R.id.verifyForgotPasswordBtn);
        userIdInput = findViewById(R.id.UserIdNum);
        userTypeRadioGroup = findViewById(R.id.forgotUserType);

        mFirebasDatabase = FirebaseDatabase.getInstance();

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

                        myRef = mFirebasDatabase.getReference("gtcp/user/student").child(userIdInput.getText().toString());


                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    DataSnapshot userPhoneNumDS = dataSnapshot.child("phoneNumber");
                                    String userPhoneNum = userPhoneNumDS.getValue().toString();

                                    Intent intent = new Intent(ForgotPasswordActivity.this, VerifyActivity.class);
                                    Bundle extras = new Bundle();

                                    extras.putString("ID", userIdInput.getText().toString());
                                    extras.putString("PHONENUMBER", userPhoneNum);
                                    extras.putString("WHERE_IS_FROM", "ForgotPassword");
                                    extras.putString("USERTYPE", "student");
                                    intent.putExtras(extras);
                                    startActivity(intent);

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
                        ;
                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    DataSnapshot userPhoneNumDS = dataSnapshot.child("phoneNumber");
                                    String userPhoneNum = userPhoneNumDS.getValue().toString();

                                    Intent intent = new Intent(ForgotPasswordActivity.this, VerifyActivity.class);
                                    Bundle extras = new Bundle();

                                    extras.putString("ID", userIdInput.getText().toString());
                                    extras.putString("PHONENUMBER", userPhoneNum);
                                    extras.putString("WHERE_IS_FROM", "ForgotPassword");
                                    extras.putString("USERTYPE", "police");
                                    intent.putExtras(extras);
                                    startActivity(intent);

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