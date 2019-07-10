package com.zbqmal.gtcp_10.Account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    //firebase database
    private DatabaseReference mFirebasDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        verifyBtn = findViewById(R.id.verifyForgotPasswordBtn);
        userIdInput = findViewById(R.id.UserIdNum);

        mAuth = FirebaseAuth.getInstance();
        mFirebasDatabase = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser user = mAuth.getCurrentUser();

        verifyBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //access to user data
                myRef = mFirebasDatabase.child("user/student").child(userIdInput.getText().toString());

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        System.out.println("part 3");
                        DataSnapshot userPhoneNumDS = dataSnapshot.child("phoneNumber");
                        String userPhoneNum = userPhoneNumDS.getValue().toString();
                        System.out.println("part 4");
                        System.out.println("--------------" + userPhoneNum);

                        Intent intent = new Intent(ForgotPasswordActivity.this, VerifyActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

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
