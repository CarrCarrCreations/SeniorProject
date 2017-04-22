package com.example.carrc.seniorproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import bolts.Task;

public class IngredientManagementActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    // Declare Variables
    ListView list;
    IngredientListViewAdapter adapter;
    SearchView editsearch;
    String text;
    ArrayList<IngredientObject> arraylist = new ArrayList<IngredientObject>();

    public void getIngredientNames(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Ingredients");
        query.orderByDescending("createdAt");
        Task<Integer> count = query.countInBackground();
        SystemClock.sleep(1000);
        query.setLimit(count.getResult());

        List<ParseObject> objects;

        try {
            objects = query.find();

            for(int i = 0; i < objects.size(); i++){
                String name = objects.get(i).get("Name").toString();
                IngredientObject recipe = new IngredientObject(name);
                arraylist.add(recipe);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void managerDash(View view){

        Intent intent = new Intent(this, ManagerDashboardActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_management);

        // Generate sample data

        getIngredientNames();

        // Locate the ListView in listview_main.xml
        list = (ListView) findViewById(R.id.nameListView);


        // Pass results to IngredientListViewAdapter Class
        adapter = new IngredientListViewAdapter(this, arraylist, "Ingredient");
        adapter.notifyDataSetChanged();

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = adapter.getItem(position).getIngredientNameName();

                if(name.equals("Create New Ingredient")){
                    Intent intent = new Intent(getApplicationContext(), CreateIngredientActivity.class);
                    intent.putExtra("Name", text);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), ModifyIngredientActivity.class);
                    intent.putExtra("Name", name);
                    startActivity(intent);
                }
            }
        });

        // Locate the EditText in listview_main.xml
        editsearch = (SearchView) findViewById(R.id.searchView);
        editsearch.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        text = newText;
        adapter.filter(text);

        return false;
    }
}