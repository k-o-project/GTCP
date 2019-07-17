package com.zbqmal.gtcp_10.Account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zbqmal.gtcp_10.Main.MainActivity;
import com.zbqmal.gtcp_10.Profile.Police;
import com.zbqmal.gtcp_10.Profile.Student;
import com.zbqmal.gtcp_10.Profile.UserType;
import com.zbqmal.gtcp_10.R;

import java.util.concurrent.TimeUnit;

public class VerifyActivity extends AppCompatActivity {

    // Passed object
    private Intent previousIntent;
    private Bundle extras;

    // Widget
    private Button confirmButton;
    private EditText editText;

    // Authentication
    private String verificationId;
    private FirebaseAuth mAuth;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        System.out.println("----------------------------- VerifyActivity ------------------------------");
        System.out.println("<<<<<<<<<<<<<Current User : " + FirebaseAuth.getInstance().getCurrentUser() + " >>>>>>>>>>>>>>>>");

        // Initialize authentication
        mAuth = FirebaseAuth.getInstance();
        editText = findViewById(R.id.verifyConfirmation);

        // Send verfication code first
        String phoneNumber = getIntent().getExtras().getString("PHONENUMBER");
        sendVerificationCode(phoneNumber);

        // Verify code sent
        findViewById(R.id.confirmVerificationBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = editText.getText().toString().trim();
                if (code.isEmpty() || code.length() != 6) {
                    editText.setError("Enter 6 digits code.");
                    editText.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });
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

    /**
     * Verify code and let it sign in with the credential made.
     *
     * @param code typed verification code
     */
    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }


    private void signInWithCredential(PhoneAuthCredential credential) {

        //Get Extras from the previous activity
        Bundle passedObject = getIntent().getExtras();

        switch (passedObject.getString("WHERE_IS_FROM")) {

            case "CreateAccount":

                // Setting intent
                Intent intent = new Intent(VerifyActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                // User information
                final String userID = passedObject.getString("ID");
                final String userPassword = passedObject.getString("PASSWORD");
                final String userEmail = passedObject.getString("EMAILADDRESS");
                final String userPhoneNumber = passedObject.getString("PHONENUMBER");
                final String userUserType = passedObject.getString("USERTYPE");

                // Create User in Firebase with email and password
//                mAuth.createUserWithEmailAndPassword(userEmail, userPassword);
                mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                        .addOnCompleteListener(VerifyActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(VerifyActivity.this, "Inside the firebaseAuth method", Toast.LENGTH_SHORT).show();
                                System.out.println("================= task.isSuccessful is " + task.isSuccessful());
                                if (task.isSuccessful()) {
                                    // Get user's uid
                                    String userUID = mAuth.getCurrentUser().getUid();
                                    System.out.println("================== Current User is " + mAuth.getCurrentUser());

                                    // Store user's information in Firebase Database
                                    UserType newAccount;
                                    System.out.println("================= userUserType is " + userUserType);
                                    switch (userUserType) {
                                        case "student":
                                            System.out.println("================= I'm in student right now!!");
                                            newAccount = new Student(userID, userUID, userPassword, userEmail, userPhoneNumber);
                                            mRootRef.child("gtcp/user/student").child(userID).setValue(newAccount);
                                            break;
                                        case "gtpd":
                                            System.out.println("================= I'm in police right now!!");
                                            newAccount = new Police(userID, userUID, userPassword, userEmail, userPhoneNumber);
                                            mRootRef.child("gtcp/user/police").child(userID).setValue(newAccount);
                                            break;
                                        default:
                                            // Exception
                                            System.out.println("================= I'm in Exception right now!!");
                                            Toast.makeText(getApplicationContext(), "UserType case Exception occurred.",Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                    Toast.makeText(VerifyActivity.this, "Account has been created.",Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(VerifyActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                startActivity(intent);
                break;

            case "ForgotPassword":

                //new intent
                Intent intent1 = new Intent(VerifyActivity.this, ResetPasswordActivity.class);

                //passing in user id
                String userId = passedObject.getString("ID");
                passedObject.putString("ID", userId);
                intent1.putExtras(passedObject);

                startActivity(intent1);

                break;

            default:
                break;
        }
    }
//    /**
//     * Let user create user and sign in.
//     *
//     * @param credential
//     */
//    private void signInWithCredential(PhoneAuthCredential credential) {
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//
//                            // Get Extras from the previous activity
//                            Bundle passedObject = getIntent().getExtras();
//
//                            switch (passedObject.getString("WHERE_IS_FROM")) {
//
//                                case "CreateAccount":
//
//                                    // Setting intent
//                                    Intent intent = new Intent(VerifyActivity.this, MainActivity.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//                                    // User information
//                                    String userID = passedObject.getString("ID");
//                                    String userPassword = passedObject.getString("PASSWORD");
//                                    String userEmail = passedObject.getString("EMAILADDRESS");
//                                    String userPhoneNumber = passedObject.getString("PHONENUMBER");
//                                    String userUserType = passedObject.getString("USERTYPE");
//
//                                    // Create User in Firebase with email and password
//                                    mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
//                                            .addOnCompleteListener(VerifyActivity.this, new OnCompleteListener<AuthResult>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                                    if (task.isSuccessful()) {
//                                                        FirebaseUser user = mAuth.getCurrentUser();
//                                                        Toast.makeText(VerifyActivity.this, "Authentication success.",
//                                                                Toast.LENGTH_SHORT).show();
//                                                    } else {
//                                                        Toast.makeText(VerifyActivity.this, "Authentication failed.",
//                                                                Toast.LENGTH_SHORT).show();
//                                                    }
//                                                }
//                                            });
//
//                                    // Get user's uid
//                                    String userUID = mAuth.getCurrentUser().getUid();
//
//                                    // Store user's information in Firebase Database
//                                    UserType newAccount;
//                                    switch (userUserType) {
//                                        case "student":
//                                            newAccount = new Student(userID, userUID, userPassword, userEmail, userPhoneNumber);
//                                            mRootRef.child("gtcp/user/student").child(userID).setValue(newAccount);
//                                            break;
//                                        case "gtpd":
//                                            newAccount = new Police(userID, userUID, userPassword, userEmail, userPhoneNumber);
//                                            mRootRef.child("gtcp/user/police").child(userID).setValue(newAccount);
//                                            break;
//                                        default:
//                                            // Exception
//                                            newAccount = null;
//                                            Toast.makeText(getApplicationContext(), "UserType case Exception occurred.",Toast.LENGTH_SHORT).show();
//                                            break;
//                                    }
//
//                                    startActivity(intent);
//                                    break;
//
//                                case "ForgotPassword":
//
//                                    //new intent
//                                    Intent intent1 = new Intent(VerifyActivity.this, ResetPasswordActivity.class);
//
//                                    //passing in user id
//                                    String userId = passedObject.getString("ID");
//                                    passedObject.putString("ID", userId);
//                                    intent1.putExtras(passedObject);
//
//                                    startActivity(intent1);
//
//                                    break;
//
//                                default:
//                                    break;
//                            }
//
//
//                        } else {
//                            Toast.makeText(VerifyActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//    }

    /**
     * Send verification code to user.
     *
     * @param phoneNumber user's phone number
     */
    public void sendVerificationCode(String phoneNumber) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                editText.setText(code);
                signInWithCredential(phoneAuthCredential);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
}
