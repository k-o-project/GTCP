package com.zbqmal.gtcp_10.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zbqmal.gtcp_10.Account.CreateAccountActivity;
import com.zbqmal.gtcp_10.Account.ForgotPasswordActivity;
import com.zbqmal.gtcp_10.Police.PoliceHomeActivity;
import com.zbqmal.gtcp_10.R;
import com.zbqmal.gtcp_10.Student.StudentHomeActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut();
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
