package com.example.carrc.seniorproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class UpdateAccountInfoActivity extends AppCompatActivity {

    EditText currentNameEditText;
    EditText newUsernameEditText;
    EditText originalPWEditText;
    EditText passwordOneEditText;
    EditText passwordTwoEditText;

    SharedPreferences sharedPreferences;
    String username;
    String password;

    String currentUserName;

    public void recreate(){
        startActivity(getIntent());
        finish();
    }

    public void relog(){

        // re log in as the current user
        ParseUser.logInInBackground(username, password);

        SystemClock.sleep(2000);

    }

    public void newUsername(View view){

        currentUserName = ParseUser.getCurrentUser().getUsername();

        // if the current username and the
        if(!currentNameEditText.getText().toString().matches("") && !newUsernameEditText.getText().toString().matches("")){
            if(currentUserName.matches(currentNameEditText.getText().toString())){

                ParseQuery<ParseUser> query = ParseQuery.getQuery("Users");
                query.whereEqualTo("username", currentNameEditText.getText().toString());
                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        if(objects.size() > 0){
                            Toast.makeText(getBaseContext(), "Username already in use, choose a new name", Toast.LENGTH_SHORT).show();
                            recreate();
                        } else {
                            ParseUser.getCurrentUser().setUsername(newUsernameEditText.getText().toString());
                            ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e == null){
                                        Toast.makeText(getBaseContext(), "Username changed Successfully", Toast.LENGTH_SHORT).show();
                                        recreate();
                                    } else {
                                        relog();
                                        Toast.makeText(getBaseContext(), e.getMessage() , Toast.LENGTH_SHORT).show();
                                        recreate();
                                    }
                                }
                            });
                        }
                    }
                });
            } else {
                Toast.makeText(getBaseContext(), "Current and new username do not match." , Toast.LENGTH_SHORT).show();
                recreate();
            }
        } else {
            Toast.makeText(getBaseContext(), "Make sure to enter current and new username." , Toast.LENGTH_SHORT).show();
        }
    }

    public void changePassword(View view){

        if(!originalPWEditText.getText().toString().matches("") && !passwordOneEditText.getText().toString().matches("") && !passwordTwoEditText.getText().toString().matches("")){

            if(originalPWEditText.getText().toString().matches(password)){

                if(passwordOneEditText.getText().toString().matches(passwordTwoEditText.getText().toString())){
                    ParseUser.getCurrentUser().setPassword(passwordOneEditText.getText().toString());
                    ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){
                                Toast.makeText(getBaseContext(), "Password Changed Successfully" , Toast.LENGTH_SHORT).show();
                                recreate();
                            } else {
                                Toast.makeText(getBaseContext(), e.getMessage() , Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(getBaseContext(), "New passwords do not match." , Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getBaseContext(), "Please enter correct password" , Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getBaseContext(), "Fill out all password fields.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account_info);

        setTitle("Update Account");

        currentNameEditText = (EditText) findViewById(R.id.currentNameEditText);
        newUsernameEditText = (EditText) findViewById(R.id.newUsernameEditText);
        originalPWEditText  = (EditText) findViewById(R.id.originalPWEditText);
        passwordOneEditText = (EditText) findViewById(R.id.passwordOneEditText);
        passwordTwoEditText = (EditText) findViewById(R.id.passwordTwoEditText);

        sharedPreferences = this.getSharedPreferences("com.example.carrc.seniorproject", Context.MODE_PRIVATE );
        username = sharedPreferences.getString("username", "");
        password = sharedPreferences.getString("password", "");
    }
}
