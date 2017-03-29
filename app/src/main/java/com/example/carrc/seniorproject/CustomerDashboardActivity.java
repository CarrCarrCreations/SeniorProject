package com.example.carrc.seniorproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class CustomerDashboardActivity extends AppCompatActivity {

    // saves the current waiter's name who is working the table that is saved in the apps shared preferences.
    String waiterName;

    public void locateWaiter(){
        // locate the table number from shared preferences and create the string for the table name
        // as it is saved in the Tables database
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.carrc.seniorproject", Context.MODE_PRIVATE );
        String tableNumber = sharedPreferences.getString("tableNumber", "");
        String formatedName = "table" + tableNumber;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tables");
        query.whereEqualTo("name", formatedName);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null && objects.size() > 0){
                    waiterName = objects.get(0).getString("staff");
                }
            }
        });
    }

    public void messenger(View view){

        // creates the chat activity between the current waiter and the table number
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra("staffName", waiterName);
        startActivity(intent);

    }

    public void toCart(View view){
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
    }

    public void menu(View view){
        Intent intent = new Intent(this, MenuMainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.customerdashboard, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.logout){
            ParseUser.logOut();

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        setTitle("Customer Dashboard");

        // query the name of the waiter currently working the apps saved table number
        locateWaiter();
    }
}
