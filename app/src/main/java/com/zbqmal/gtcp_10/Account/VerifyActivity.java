package com.zbqmal.gtcp_10.Account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zbqmal.gtcp_10.Main.MainActivity;
import com.zbqmal.gtcp_10.R;

public class VerifyActivity extends AppCompatActivity {

    // Passed object
    private Intent previousIntent;
    private Bundle extras;

    // Widget
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        // Send verfication code first
        /*
        TODO: Uncomment after sendVerificationCode method has been completed.
         */
        //sendVerificationCode();

        // Assign widget
        confirmButton = findViewById(R.id.confirmCreateAccountBtn);

        // Split case from CreateAccountActivity and ForgetPasswordActivity

    }

    /**
     *
     * @param view view to pass to MainActivity
     * Sends user to Main activity. (Will be deleted when being able to distinguish if it's from
     *             CreateAccountActivity or ForgotPasswordActivity.)
     */
    public void goToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /*
    TODO: Complete this method.
     */
    /**
     *
     */
    public void sendVerificationCode() {

    }
}
