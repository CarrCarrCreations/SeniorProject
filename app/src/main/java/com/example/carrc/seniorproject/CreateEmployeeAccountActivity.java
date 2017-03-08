package com.example.carrc.seniorproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRole;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;

public class CreateEmployeeAccountActivity extends AppCompatActivity {

    public void recreate(){
        startActivity(getIntent());
        finish();
    }

    // This method performs a ParseRole Query to find the role that matched the role String provided
    // Once the role it found, the user is added to the role specified
    public void addToRole(final ParseUser user, final String Role){

        // Sleep time of 5 seconds to give the database time to save the user before proceeding
        SystemClock.sleep(5000);

        final ParseQuery<ParseRole> parseRoleParseQuery = ParseRole.getQuery();
        parseRoleParseQuery.findInBackground(new FindCallback<ParseRole>() {
            @Override
            public void done(List<ParseRole> objects, ParseException e) {
                // Make sure there are roles in the database
                if(objects.size() > 0){
                    // iterate through the roles
                    for(int i = 0; i < objects.size(); i++){
                        // if the role name matches the given role String
                        if (objects.get(i).getName().matches(Role)) {
                            Log.i("Role Name", Role + " Role Selected");
                            // add the user to the given role
                            objects.get(i).getUsers().add(user);
                            // log out the new user
                            ParseUser.logOut();
                            // save changes
                            objects.get(i).saveInBackground();
                        }
                    }
                }
            }
        });

        // log the manager back into the app
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.carrc.seniorproject", Context.MODE_PRIVATE );
        ParseUser.logInInBackground(sharedPreferences.getString("username", ""), sharedPreferences.getString("password", ""));
    }

    public void createEmployee(View view){

        final EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        final EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        CheckBox employeeCheckBox = (CheckBox) findViewById(R.id.employeeCheckBox);
        CheckBox managerCheckBox = (CheckBox) findViewById(R.id.managerCheckBox);


        final ParseUser user = new ParseUser();
        user.setUsername(usernameEditText.getText().toString());
        user.setPassword(passwordEditText.getText().toString());
        // Stores a string depending on employee or manager being checked
        String checkBoxValue = "";

        if(usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")
                || employeeCheckBox.isChecked() && managerCheckBox.isChecked()) {
            Toast.makeText(getBaseContext(), "Make sure username and password are entered. Only One checkbox can be checked", Toast.LENGTH_LONG).show();
            recreate();

        }else if(!employeeCheckBox.isChecked() && !managerCheckBox.isChecked()){
            Toast.makeText(getBaseContext(), "You must check employee or manager", Toast.LENGTH_LONG).show();

        } else if(employeeCheckBox.isChecked()){
            checkBoxValue = "Employee";
            /*
            int tableNumbers = 20;
            for(int i = 0; i < tableNumbers; i++){
                user.put("table" + (i + 1), "no");
            }
            */

            final String checkBoxValueForSignUp = checkBoxValue;

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {

                    if(e == null){
                        addToRole(user, checkBoxValueForSignUp);
                        Toast.makeText(getBaseContext(), checkBoxValueForSignUp + " Account Created Successfully", Toast.LENGTH_SHORT).show();
                        recreate();
                    } else {
                        Log.i("Sign up", "Failed: " + e.toString());
                        Toast.makeText(getBaseContext(), checkBoxValueForSignUp + " Account Creation Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if(managerCheckBox.isChecked()){
            checkBoxValue = "Manager";

            final String checkBoxValueForSignUp = checkBoxValue;

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {

                    if(e == null){
                        addToRole(user, checkBoxValueForSignUp);
                        Toast.makeText(getBaseContext(), checkBoxValueForSignUp + " Account Created Successfully", Toast.LENGTH_SHORT).show();
                        recreate();
                    } else {
                        Log.i("Sign up", "Failed: " + e.toString());
                        Toast.makeText(getBaseContext(), checkBoxValueForSignUp + " Account Creation Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee_account);

        setTitle("Create Employee Account");
    }
}
