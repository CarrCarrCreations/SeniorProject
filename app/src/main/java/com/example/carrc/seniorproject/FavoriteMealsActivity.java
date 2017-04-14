package com.example.carrc.seniorproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FavoriteMealsActivity extends AppCompatActivity {

    ListView listView;
    List<String> meals = new ArrayList<>();
    List<String> mealIDs = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    public void displayFavorites() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("FavoriteMeals");
        query.whereEqualTo("Username", ParseUser.getCurrentUser().getUsername());

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null && objects.size() > 0){
                    meals.clear();
                    mealIDs.clear();

                    for(int i = 0; i < objects.size(); i++){
                        meals.add(objects.get(i).get("MealName").toString());
                        mealIDs.add(objects.get(i).get("MealID").toString());
                    }

                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_meals);

        setTitle("Favorite Meals");

        listView = (ListView) findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, meals);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String mealName = meals.get(position);
                String mealID = mealIDs.get(position);

                Intent intent = new Intent(getApplicationContext(), EditFavoriteActivity.class);
                intent.putExtra("mealName", mealName);
                intent.putExtra("mealID", mealID);
                startActivity(intent);
            }
        });

        displayFavorites();
    }
}
