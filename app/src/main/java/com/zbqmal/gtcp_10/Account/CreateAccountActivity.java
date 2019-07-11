package com.zbqmal.gtcp_10.Account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.zbqmal.gtcp_10.Main.MainActivity;
import com.zbqmal.gtcp_10.R;

public class CreateAccountActivity extends AppCompatActivity {

    // Widgets
    private RadioGroup userTypeRadioGroup;
    private RadioButton userTypeRadioButton;
    private EditText idText;
    private EditText emailAddressText;
    private EditText passwordText;
    private EditText confirmPasswordText;
    private EditText phoneNumberText;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // Assign widgets
        userTypeRadioGroup = findViewById(R.id.userTypeSelect_RadioGroup);
        idText = findViewById(R.id.createUsername);
        emailAddressText = findViewById(R.id.createEmailAddress);
        passwordText = findViewById(R.id.createPassword);
        confirmPasswordText = findViewById(R.id.createConfirmPassword);
        phoneNumberText = findViewById(R.id.createPhoneNumber);
        submitButton = findViewById(R.id.createAccountSubmitBtn);

        // When clicked submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitButtonClicked();
            }
        });
    }

    /**
     * Store user information in Firebase and move to the verifyActivity.
     */
    private void submitButtonClicked() {

        // Get selected user type
        int selectedUserTypeID = userTypeRadioGroup.getCheckedRadioButtonId();
        userTypeRadioButton = findViewById(selectedUserTypeID);
        String selectedUserType = userTypeRadioButton.getText().toString().toLowerCase();

        // Get Information from EditText
        String currentID = idText.getText().toString();
        String currentEmailAddress = emailAddressText.getText().toString();
        String currentPassword = passwordText.getText().toString();
        String currentConfirmPassword = confirmPasswordText.getText().toString();
        String currentPhoneNumber = "+1" + phoneNumberText.getText().toString();

        // Check if the information is possible to be used.
        boolean isAllNotEmpty = !currentID.isEmpty() && !currentEmailAddress.isEmpty()
                && !currentPassword.isEmpty() && !currentConfirmPassword.isEmpty()
                && !currentPhoneNumber.isEmpty();
        /*
        TODO: Add more conditions on each section
        * id must be 9 digits and be distinct
        * email must have right format, like something@gtcp.com, and be distinct
        * password must contain a-z, A-Z, 0-9. (special characters maybe later)
        * phonenumber must be 10 digits and be distinct
        * */

        /* If everything is okay, pass all information to VerifyActivity, sending verification
        code to the user.*/
        if (isAllNotEmpty) {

            boolean isPasswordSame = currentPassword.equals(currentConfirmPassword);
            if (isPasswordSame) {

                /*
                TODO: Don't use PhoneNumberUtils.isGlobalPhoneNumber method.
                 */
                boolean isPhoneNumberCorrect = PhoneNumberUtils.isGlobalPhoneNumber(currentPhoneNumber);
                if (isPhoneNumberCorrect) {

                    boolean isGoodToMove = false;
                    String userType;

                    if (selectedUserType.equals("student") || selectedUserType.equals("gtpd")) {
                        // Set isGoodToMove when selectedUserType is either one
                        isGoodToMove = true;
                    } else {
                        // Exception
                        Toast errorToastMessage = Toast.makeText(getApplicationContext(),
                                "selectedUserType is student nor gtpd.",Toast.LENGTH_SHORT);
                        errorToastMessage.show();
                    }

                    if (isGoodToMove) {

                        // Move to VerifyActivity, passing the values typed
                        Intent intent = new Intent(this, VerifyActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("ID", currentID);
                        extras.putString("PASSWORD", currentPassword);
                        extras.putString("EMAILADDRESS", currentEmailAddress);
                        extras.putString("PHONENUMBER", currentPhoneNumber);
                        extras.putString("USERTYPE", selectedUserType);
                        extras.putString("WHERE_IS_FROM", "CreateAccount");
                        intent.putExtras(extras);
                        startActivity(intent);
                    }

                } else {

                    // Toast message for the case of non-existing phone number
                    phoneNumberText.setError("This phone number seems not existing.");
                }

            } else {

                // Toast message for the case of wrong confirmPassword
                confirmPasswordText.setError("This password is not equal to what you entered.");
            }

        } else {

            if (currentID.isEmpty()) {
                idText.setError("You need to enter an ID.");
            } else if (currentEmailAddress.isEmpty()) {
                emailAddressText.setError("You need to enter an email address.");
            } else if (currentPassword.isEmpty()) {
                passwordText.setError("You need to enter a password.");
            } else if (currentConfirmPassword.isEmpty()) {
                confirmPasswordText.setError("You need to enter a password.");
            } else if (currentPhoneNumber.isEmpty()) {
                phoneNumberText.setError("You need to enter a phone number.");
            } else {
                // Exception
                Toast errorToastMessage = Toast.makeText(getApplicationContext(),
                        "Error occurred!",Toast.LENGTH_SHORT);
                errorToastMessage.show();
            }
        }
    }

    /**
     * Sends user to MainActivity.
     *
     * @param view view to pass to MainActivity
     */
    public void goToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
