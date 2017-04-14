package com.example.carrc.seniorproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class EditFavoriteActivity extends AppCompatActivity {

    String name;
    String ingredName;
    String id;

    int counter;

    ArrayList<String> ingredients = new ArrayList<>();

    ArrayAdapter arrayAdapter;

    ListView ingredientsListView;
    EditText commentEditText;

    public void displayIngredients(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("FavoriteMeals");
        query.whereEqualTo("Username", ParseUser.getCurrentUser().getUsername());
        query.whereEqualTo("MealID", id);

        try {
            List<ParseObject> objects = query.find();
            ParseObject recipe = objects.get(0);

            counter = 0;
            ingredients.clear();

            if(recipe.get("Comments") != null){
                commentEditText.setText(recipe.get("Comments").toString());
            }

            do {

                ingredName = "IngredientName" + counter;

                if(recipe.get(ingredName) == null){
                    break;
                }

                String ingredientName = recipe.get(ingredName).toString();
                ingredients.add(ingredientName);

                counter++;

            } while(recipe.get(ingredName) != null);

            arrayAdapter.notifyDataSetChanged();

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_favorite);

        Intent intent = getIntent();
        name = intent.getStringExtra("mealName");
        id = intent.getStringExtra("mealID");

        setTitle("Edit " + name);

        commentEditText = (EditText) findViewById(R.id.commentEditText);

        ingredientsListView = (ListView) findViewById(R.id.ingredientsListView);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ingredients);
        ingredientsListView.setAdapter(arrayAdapter);

        displayIngredients();

    }
}
