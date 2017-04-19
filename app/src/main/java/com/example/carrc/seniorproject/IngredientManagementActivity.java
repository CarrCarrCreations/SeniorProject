package com.example.carrc.seniorproject;

import android.content.Intent;
import android.os.Bundle;
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

public class IngredientManagementActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    // Declare Variables
    ListView list;
    IngredientListViewAdapter adapter;
    SearchView editsearch;
    String[] animalNameList;
    ArrayList<IngredientObject> arraylist = new ArrayList<IngredientObject>();

    public void getIngredientNames(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Ingredients");
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_management);

        // Generate sample data

        getIngredientNames();

        // Locate the ListView in listview_main.xml
        list = (ListView) findViewById(R.id.nameListView);


        // Pass results to IngredientListViewAdapter Class
        adapter = new IngredientListViewAdapter(this, arraylist);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = adapter.getItem(position).getIngredientNameName();

                Intent intent = new Intent(getApplicationContext(), ModifyIngredientActivity.class);
                intent.putExtra("Name", name);
                startActivity(intent);
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
        String text = newText;
        adapter.filter(text);
        return false;
    }
}