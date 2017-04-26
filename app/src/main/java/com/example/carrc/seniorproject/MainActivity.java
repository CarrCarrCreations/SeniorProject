package com.example.carrc.seniorproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRole;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    // True means the button says login, false means the button says sign up
    Boolean loginModeActive = true;
    // the TextView that says either, "Or, Sign up", or "Or, Login"
    TextView changeSignupModeTextView;

    EditText usernameEditText;
    EditText passwordEditText;

    SharedPreferences sharedPreferences;

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
                            // save changes
                            objects.get(i).saveInBackground();
                        }
                    }
                }
            }
        });
    }

    // This method returns which role the current user is apart of. (Customer, Employee, Manager)
    public String getRole(){

        // create a ParseRole query
        ParseQuery<ParseRole> roleQuery = ParseRole.getQuery();
        // create the list that the roles will be saved in
        List<ParseRole> allRoles = null;

        try {
            // perform the role query
            allRoles = roleQuery.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ArrayList<ParseRole> userRoles = new ArrayList<ParseRole>();
        ArrayList<String> userRolesNames = new ArrayList<String>();
        for(ParseRole role : allRoles) {
            // create a query that retrieves the users relations from the current role
            ParseQuery usersQuery = role.getRelation("users").getQuery();
            // only find the role if the current users objectId is listed in current role selected
            usersQuery.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
            try {
                // if the user is apart of the role
                if(usersQuery.count() > 0) {
                    // save the role
                    userRoles.add(role);
                    // save the roles name
                    userRolesNames.add(role.getName().toString());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        // return the name of the role
        return userRolesNames.get(0);
    }


    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        // closes out the keyboard when password is entered, and uses the login/signup button automatically
        if(i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
            login(view);
        }
        return false;
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.changeSignupModeTextView){
            Button signupButton = (Button) findViewById(R.id.loginButton);

            if(loginModeActive){
                loginModeActive = false;
                signupButton.setText("Sign Up");
                changeSignupModeTextView.setText("Or, Login");
            } else {
                loginModeActive = true;
                signupButton.setText("Login");
                changeSignupModeTextView.setText("Or, Sign Up");
            }
        }

        if(view.getId() == R.id.backgroundRelativeLayout){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

    }

    public void login(View view){

        usernameEditText = (EditText) findViewById(R.id.usernameTextView);


        if(usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")){
            Toast.makeText(this, "A username and password are required.", Toast.LENGTH_SHORT).show();
        } else {
            if(loginModeActive){

                ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(user != null){
                            Log.i("Login", "Successful");

                            sharedPreferences.edit().putString("username", usernameEditText.getText().toString()).apply();
                            sharedPreferences.edit().putString("password", passwordEditText.getText().toString()).apply();

                            String userRole = getRole();

                            // direct the user to the proper Activity depending on their role
                            if(userRole.matches("Customer")){
                                Intent intent = new Intent(MainActivity.this, CustomerDashboardActivity.class);
                                startActivity(intent);
                                finish();
                            } else if(userRole.matches("Manager")){
                                Intent intent = new Intent(MainActivity.this, ManagerDashboardActivity.class);
                                startActivity(intent);
                                finish();
                            } else if(userRole.matches("Employee")){
                                Intent intent = new Intent(MainActivity.this, EmployeeDashboardActivity.class);
                                startActivity(intent);
                                finish();
                            }



                        } else {
                            Toast.makeText(MainActivity.this, "Invalid username/password combination", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } else {

                final ParseUser user = new ParseUser();
                user.setUsername(usernameEditText.getText().toString());
                user.setPassword(passwordEditText.getText().toString());

                sharedPreferences.edit().putString("username", usernameEditText.getText().toString()).apply();
                sharedPreferences.edit().putString("password", passwordEditText.getText().toString()).apply();

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i("Sign Up", "Success");
                            addToRole(user, "Customer");

                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Welcome");

        sharedPreferences = this.getSharedPreferences("com.example.carrc.seniorproject", Context.MODE_PRIVATE );

        changeSignupModeTextView = (TextView) findViewById(R.id.changeSignupModeTextView);
        changeSignupModeTextView.setOnClickListener(this);

        RelativeLayout backgroundRelativeLayout = (RelativeLayout) findViewById(R.id.backgroundRelativeLayout);
        backgroundRelativeLayout.setOnClickListener(this);


        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        passwordEditText.setOnKeyListener(this);


        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}
