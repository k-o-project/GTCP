package com.zbqmal.gtcp_10.Student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.zbqmal.gtcp_10.Account.MyAccountActivity;
import com.zbqmal.gtcp_10.R;

public class StudentHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        System.out.println("================ Current User is " + FirebaseAuth.getInstance().getCurrentUser().toString());
    }

    public void goToStartTrackingActivity(View view) {
        Intent intent = new Intent(this, TrackingActivity.class);
        startActivity(intent);
    }

    public void goToMyAccountActivity(View view) {

        Intent intent = new Intent(this, MyAccountActivity.class);
        Bundle userData = getIntent().getExtras();

        //get user's id and type
        final String userID = userData.getString("ID");
        final String userType = userData.getString("USERTYPE");

        //passing user's id and type
        userData.putString("ID",userID);
        userData.putString("USERTYPE", userType);

        intent.putExtras(userData);

        startActivity(intent);
    }
}
