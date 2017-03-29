package com.example.carrc.seniorproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class DeleteEmployeeAccountActivity extends AppCompatActivity {

    EditText usernameEditText;

    public void recreate(){
        startActivity(getIntent());
        finish();
    }

    public void deleteUser(View view){

        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        final String usernameString = usernameEditText.getText().toString();

        // try using ParseUser.become() with session key, to become the user you want to delete.
        // then log out when done.

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", usernameString);

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {

                if(e == null && users.size() > 0){

                    users.get(0).deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(getBaseContext(), "User deleted successfully", Toast.LENGTH_SHORT).show();
                                recreate();
                            } else {
                                Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                recreate();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getBaseContext(), "Username does not exist", Toast.LENGTH_SHORT).show();
                    recreate();
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_employee_account);

        setTitle("delete Employee Account");
    }
}
