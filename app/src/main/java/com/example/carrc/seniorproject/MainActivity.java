package com.example.carrc.seniorproject;

import android.content.Intent;
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

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    Boolean loginModeActive = true;
    TextView changeSignupModeTextView;
    EditText passwordEditText;


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

        EditText usernameEditText = (EditText) findViewById(R.id.usernameTextView);


        if(usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")){
            Toast.makeText(this, "A username and password are required.", Toast.LENGTH_SHORT).show();
        } else {
            if(loginModeActive){

                ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(user != null){
                            Log.i("Login", "Successful");
                        } else {
                            Toast.makeText(MainActivity.this, "Invalid username/password combination", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } else {

                ParseUser user = new ParseUser();
                user.setUsername(usernameEditText.getText().toString());
                user.setPassword(passwordEditText.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i("Sign Up", "Success");
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

        changeSignupModeTextView = (TextView) findViewById(R.id.changeSignupModeTextView);
        changeSignupModeTextView.setOnClickListener(this);

        RelativeLayout backgroundRelativeLayout = (RelativeLayout) findViewById(R.id.backgroundRelativeLayout);
        backgroundRelativeLayout.setOnClickListener(this);

        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        passwordEditText.setOnKeyListener(this);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}
