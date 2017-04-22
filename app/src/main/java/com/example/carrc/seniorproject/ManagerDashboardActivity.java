package com.example.carrc.seniorproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class ManagerDashboardActivity extends AppCompatActivity {

    public void recreate(){
        startActivity(getIntent());
        finish();
    }

    public void recipeSearch(View view){
        Intent intent = new Intent(this, RecipeManagementSearchActivity.class);
        startActivity(intent);
    }

    public void recipeManagement(View view) {
        Intent intent = new Intent(this, RecipeManagementActivity.class);
        startActivity(intent);
    }

    public void ingredientManagement(View view){
        Intent intent = new Intent(this, IngredientManagementActivity.class);
        startActivity(intent);
    }

    public void inventoryManagement(View view){
        Intent intent = new Intent(this, InventoryActivity.class);
        startActivity(intent);
    }

    public void openMenu(View view){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    public void deleteAccount(View view){
        Intent intent = new Intent(getApplicationContext(), DeleteEmployeeAccountActivity.class );
        startActivity(intent);
    }

    public void setNumTables(View view){
        EditText numOfTablesEditText = (EditText) findViewById(R.id.numOfTablesEditText);
        int num = Integer.parseInt(numOfTablesEditText.getText().toString());


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tables");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null && objects.size() > 0){
                    for(ParseObject object : objects){
                        try {
                            object.delete();
                            object.saveInBackground();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });

        SystemClock.sleep(1000);

        ParseObject numOfTables = new ParseObject("Tables");
        numOfTables.put("name", "Master");
        numOfTables.put("Total", num);

        for(int i = 0; i < num; i++){
            ParseObject newTable = new ParseObject("Tables");
            newTable.put("name", "table" + (i + 1));
            newTable.put("staff", "closed");
            newTable.saveInBackground();

        }

        numOfTables.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Toast.makeText(getBaseContext(), "Number of tables saved successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "Number of tables failed to save", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recreate();
    }

    // set the table number and save it into shared preferences
    public void setTableNumber(View view){

        EditText tableNumberEditText = (EditText) findViewById(R.id.tableNumberEditText);
        String tableNumber = tableNumberEditText.getText().toString();

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.carrc.seniorproject", Context.MODE_PRIVATE );
        sharedPreferences.edit().putString("tableNumber", tableNumber).apply();
        recreate();

    }

    public void createEmployee(View view){
        Intent intent = new Intent(getApplicationContext(), CreateEmployeeAccountActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.managerdashboard, menu);

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

        if(item.getItemId() == R.id.update){

            Intent intent = new Intent(getApplicationContext(), UpdateAccountInfoActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_dashboard);

        TextView tableNumberTextView = (TextView) findViewById(R.id.tableNumberTextView);
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.carrc.seniorproject", Context.MODE_PRIVATE );
        String currentTableNumber = sharedPreferences.getString("tableNumber", "");

        tableNumberTextView.setText("Current Table Number: " + currentTableNumber);

        setTitle("Manager Dashboard");

    }
}
