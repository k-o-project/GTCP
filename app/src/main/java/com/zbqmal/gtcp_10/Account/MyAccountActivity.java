package com.zbqmal.gtcp_10.Account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zbqmal.gtcp_10.Main.MainActivity;
import com.zbqmal.gtcp_10.R;

public class MyAccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText userId;
    private EditText userPW;
    private EditText userEmail;
    private EditText userPhoneNum;
    private ImageButton backButton;

    DatabaseReference mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        mAuth = FirebaseAuth.getInstance();
        userId = findViewById(R.id.userIdInput);
        userPW = findViewById(R.id.userPWInput);
        userEmail = findViewById(R.id.userEmailInput);
        userPhoneNum = findViewById(R.id.userPhoneNumInput);
        backButton = findViewById(R.id.closeButton);

        getUserAccount();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void getUserAccount() {
        FirebaseUser currUser = mAuth.getCurrentUser();

        if (currUser == null) {
            Toast.makeText(MyAccountActivity.this, "You have not logged in", Toast.LENGTH_SHORT).show();
            return;
        } else {
            //user logged in
            Bundle userData = getIntent().getExtras();

            switch (userData.getString("USERTYPE")) {

                case "student" :
                    mRef = FirebaseDatabase.getInstance().getReference("gtcp/user/student").child(userData.getString("ID"));
                    userId.setText(mRef.child("id").toString());
                    userPW.setText(mRef.child("password").toString());
                    userEmail.setText(mRef.child("emailAddress").toString());
                    userPhoneNum.setText(mRef.child("phoneNumber").toString());
                    break;

                case "police" :
                    mRef = FirebaseDatabase.getInstance().getReference("gtcp/user/police");
                    userId.setText(mRef.child("id").toString());
                    userPW.setText(mRef.child("password").toString());
                    userEmail.setText(mRef.child("emailAddress").toString());
                    userPhoneNum.setText(mRef.child("phoneNumber").toString());
                    break;
            }

        }
    }

    public void goToMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}
