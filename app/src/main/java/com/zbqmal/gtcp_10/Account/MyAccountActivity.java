package com.zbqmal.gtcp_10.Account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zbqmal.gtcp_10.Main.MainActivity;
import com.zbqmal.gtcp_10.R;

public class MyAccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText userId;
    private EditText userEmail;
    private EditText userPhoneNum;
    private ImageButton backButton;

    DatabaseReference mRefUserChild;
    DatabaseReference mRefCurrUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        mAuth = FirebaseAuth.getInstance();
        userId = findViewById(R.id.userIdInput);
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

        final FirebaseUser currUser = mAuth.getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        if (currUser == null) {
            Toast.makeText(MyAccountActivity.this, "You have not logged in", Toast.LENGTH_SHORT).show();
            return;
        } else {

            //user logged in

            Bundle userType = getIntent().getExtras();

            switch (userType.getString("USERTYPE")) {

                case "student":

                    mRefUserChild = database.getReference("gtcp/user/student");

                    mRefUserChild.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot data: dataSnapshot.getChildren()) {
                                if (data.child("uid").getValue().equals(currUser.getUid())) {
                                    userId.setText(data.child("id").getValue().toString());
                                    userEmail.setText(data.child("emailAddress").getValue().toString());
                                    userPhoneNum.setText(data.child("phoneNumber").getValue().toString());
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    break;

                case "police":

                    mRefUserChild = database.getReference("gtcp/user/police");

                    mRefUserChild.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot data: dataSnapshot.getChildren()) {
                                if (data.child("uid").getValue().equals(currUser.getUid())) {
                                    userId.setText(data.child("id").getValue().toString());
                                    userEmail.setText(data.child("emailAddress").getValue().toString());
                                    userPhoneNum.setText(data.child("phoneNumber").getValue().toString());
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
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
