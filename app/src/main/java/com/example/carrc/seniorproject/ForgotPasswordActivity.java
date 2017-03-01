package com.example.carrc.seniorproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class ForgotPasswordActivity extends AppCompatActivity {


    public void passwordReset(View view){

        EditText pwResetEditText = (EditText) findViewById(R.id.pwResetEditText);
        String email = pwResetEditText.getText().toString();

        ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Log.i("Reset Password", "Email sent successfully");
                } else {
                    Log.i("Reset Password", "Error: " + e.toString());
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
    }
}
