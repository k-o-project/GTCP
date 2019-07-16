package com.zbqmal.gtcp_10.Account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zbqmal.gtcp_10.Main.MainActivity;
import com.zbqmal.gtcp_10.R;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText newPW;
    private EditText confirmNewPW;
    private Button verifyBtn;

    private DatabaseReference userRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        //user inputs
        newPW = findViewById(R.id.resetPassword);
        confirmNewPW = findViewById(R.id.resetConfirmPassword);
        verifyBtn = findViewById(R.id.confirmResetPasswordBtn);

        //accessing to user's info with id number
        userRef = FirebaseDatabase.getInstance().getReference();

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (newPW.getText().toString().isEmpty()) {

                    newPW.setError("Enter new password");

                } else if (confirmNewPW.getText().toString().isEmpty()) {

                    confirmNewPW.setError("Invalid Input");

                } else if (!newPW.getText().toString().equals(confirmNewPW.getText().toString())) {

                    confirmNewPW.setError("Invalid Input");

                } else {

                    Toast.makeText(ResetPasswordActivity.this,
                            "Your password has been updated", Toast.LENGTH_SHORT).show();

                    //user id
                    String userID = getIntent().getExtras().getString("ID");

                    //set new value for password
                    userRef.child("gtcp/user/student").child(userID).child("password").setValue(newPW.getText().toString());

                    Intent intent = new Intent(ResetPasswordActivity.this, MainActivity.class);
                    startActivity(intent);

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
