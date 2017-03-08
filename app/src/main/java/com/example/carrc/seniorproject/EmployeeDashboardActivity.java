package com.example.carrc.seniorproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDashboardActivity extends AppCompatActivity {

    int numOfTables;
    ArrayList<String> tables = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    String menuItems[] = {"Remove Table", "Messenger", "Review Order"};

    public void recreate(){
        startActivity(getIntent());
        finish();
    }


    public void currentTables(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tables");
        query.whereEqualTo("staff", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null && objects.size() > 0){
                    for(ParseObject object : objects){
                        tables.add(object.getString("name"));
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }

    public void removeAllTables(View view){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tables");
        query.whereEqualTo("staff", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null && objects.size() > 0){
                    for(ParseObject object : objects){
                        object.put("staff", "closed");
                        object.saveInBackground();
                        tables.remove(object.getString("name"));
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }

    public int totalNumberOfTables() {

        // Query the Tables class
        ParseQuery<ParseObject> totalTables = ParseQuery.getQuery("Tables");
        // Only look for the object with the name of Master
        totalTables.whereEqualTo("name", "Master");
        totalTables.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null && objects.size() > 0){
                    numOfTables = objects.get(0).getInt("Total");
                }
            }
        });
        return numOfTables;
    }


    public void servingTable(View view){
        final EditText tableWorkingEditText = (EditText) findViewById(R.id.tableWorkingEditText);
        final String editTextNum = tableWorkingEditText.getText().toString();
        final String tableName = "table" + editTextNum;

        Log.i("NumberOfTables", Integer.toString(numOfTables));
        if(Integer.parseInt(editTextNum) <= numOfTables){
            // Query the tables class
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Tables");
            // and only select the entry with the correct table name
            query.whereEqualTo("name", tableName);

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if(e == null && objects.size() > 0){
                        // if staff is equal to closed, add the current user to staff
                        if(objects.get(0).getString("staff").matches("closed")){
                            // add the current user to the table
                            objects.get(0).put("staff", ParseUser.getCurrentUser().getUsername());
                            tables.add(tableName);
                            objects.get(0).saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e == null){
                                        arrayAdapter.notifyDataSetChanged();
                                        tableWorkingEditText.setText("");
                                    } else {
                                        Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        recreate();
                                    }
                                }
                            });
                        } else {
                            // otherwise the table is already assigned to another staff member
                            Toast.makeText(getBaseContext(), "Table already assigned", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getBaseContext(), "Enter correct table number", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.employeedashboard, menu);

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

    // create the menu for long pressing an item on the listView
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v.getId() == R.id.tablesWaitedListView){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            for(int i = 0; i < menuItems.length; i++){
                menu.add(Menu.NONE, i, i,menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String menuItemName = menuItems[menuItemIndex];
        String tableName = ((TextView) info.targetView).getText().toString();


        if(menuItemName.matches("Messenger")){

            Intent intent = new Intent(getApplicationContext(), EmployeeChatActivity.class);
            intent.putExtra("staffName", tableName);
            startActivity(intent);

        }

        if(menuItemName.matches("Remove Table")){
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Tables");
            query.whereEqualTo("name", tableName);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if(e == null && objects.size() > 0){
                        for(ParseObject object : objects){
                            object.put("staff", "closed");
                            tables.remove(object.getString("name"));
                            arrayAdapter.notifyDataSetChanged();
                            object.saveInBackground();
                        }
                    }
                }
            });
        }

        return super.onContextItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dashboard);

        setTitle("Employee Dashboard");

        numOfTables = totalNumberOfTables();

        ListView tablesWaitedListView = (ListView) findViewById(R.id.tablesWaitedListView);
        registerForContextMenu(tablesWaitedListView);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, tables);
        tablesWaitedListView.setAdapter(arrayAdapter);

        currentTables();


    }
}
