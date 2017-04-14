package com.example.carrc.seniorproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class EditFavoriteActivity extends AppCompatActivity {

    String name;
    String ingredName;
    String ingredID;
    String id;
    String price;

    int counter;
    int itemSelectedTag;

    ArrayList<String> ingredients = new ArrayList<>();

    ArrayAdapter arrayAdapter;

    ListView ingredientsListView;
    EditText commentEditText;

    ParseObject oldRecipe;

    String menuItems[] = {"Remove Ingredient"};

    public class ingredientInfo {
        String ingredName;
        String ingredID;
        int ingredTag;
    }

    // create the menu for long pressing an item on the listView
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v.getId() == R.id.ingredientsListView){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            for(int i = 0; i < menuItems.length; i++){
                menu.add(Menu.NONE, i, i,menuItems[i]);
            }
            itemSelectedTag = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String menuItemName = menuItems[menuItemIndex];


        if(menuItemName.matches("Remove Ingredient")){

            ingredients.remove(itemSelectedTag);
            arrayAdapter.notifyDataSetChanged();
        }

        return super.onContextItemSelected(item);
    }



    public void displayIngredients(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("FavoriteMeals");
        query.whereEqualTo("Username", ParseUser.getCurrentUser().getUsername());
        query.whereEqualTo("MealID", id);

        try {
            List<ParseObject> objects = query.find();
            ParseObject recipe = objects.get(0);
            price = recipe.get("Price").toString();

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

    public void addToCartButton(View view){

        ParseObject cartItem = new ParseObject("Cart");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("FavoriteMeals");
        query.whereEqualTo("Username", ParseUser.getCurrentUser().getUsername());
        query.whereEqualTo("MealID", id);


        if(!commentEditText.getText().toString().isEmpty()){
            String comment = commentEditText.getText().toString();
            cartItem.put("Comment", comment);
        }

        cartItem.put("RecipeName", name);
        cartItem.put("RecipeId", id);
        cartItem.put("Price", price);


        try {
            List<ParseObject> objects = query.find();
            ParseObject recipe = objects.get(0);

            do {

                ingredName = "IngredientName" + counter;
                ingredID = "IngredientID" + counter;

                if(recipe.get(ingredName) == null){
                    break;
                }

                String ingredientName = recipe.get(ingredName).toString();
                String ingredientID = recipe.get(ingredID).toString();
                cartItem.put(ingredName, ingredientName);
                cartItem.put(ingredID, ingredientID);

                counter++;

            } while(recipe.get(ingredName) != null);


        } catch (ParseException e) {
            e.printStackTrace();
        }


        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.carrc.seniorproject", Context.MODE_PRIVATE );
        String currentTableNumber = sharedPreferences.getString("tableNumber", "");

        // save table number and current user with order
        cartItem.put("TableNumber", currentTableNumber);
        cartItem.put("Username", ParseUser.getCurrentUser().getUsername());

        cartItem.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Toast.makeText(getBaseContext(), "Added to Cart", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        commentEditText.setText("");

    }

    public void saveChanges(View view){

        ParseObject newFavorite = new ParseObject("FavoriteMeals");

        // query the original meal saved in favorites
        ParseQuery<ParseObject> query = ParseQuery.getQuery("FavoriteMeals");
        query.whereEqualTo("Username", ParseUser.getCurrentUser().getUsername());
        query.whereEqualTo("MealID", id);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.carrc.seniorproject", Context.MODE_PRIVATE );
        String currentTableNumber = sharedPreferences.getString("tableNumber", "");

        newFavorite.put("Username", ParseUser.getCurrentUser().getUsername());
        newFavorite.put("MealName", name);
        newFavorite.put("MealID", id);
        newFavorite.put("Price", price);

        // save table number and current user with order
        newFavorite.put("TableNumber", currentTableNumber);

        try {
            List<ParseObject> objects = query.find();
            oldRecipe = objects.get(0);

            for(int i = 0; i < ingredients.size(); i++){
                newFavorite.put("IngredientName" + i, ingredients.get(i));
                newFavorite.put("IngredientID" + i, oldRecipe.get("IngredientID" + i));
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }


        newFavorite.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Toast.makeText(getBaseContext(), "Edit Saved!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        oldRecipe.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Toast.makeText(getBaseContext(), "Old favorite deleted.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), e.getMessage() , Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void resetIngreds(View view){

        ParseObject newFavorite = new ParseObject("FavoriteMeals");

        ParseQuery<ParseObject> original = ParseQuery.getQuery("Recipes");
        original.whereEqualTo("MealID", id);

        ParseQuery<ParseObject> current = ParseQuery.getQuery("FavoriteMeals");
        current.whereEqualTo("Username", ParseUser.getCurrentUser().getUsername());
        current.whereEqualTo("MealID", id);

        try {
            // query the original recipe
            List<ParseObject> originalObjects = original.find();
            ParseObject originalRecipe = originalObjects.get(0);




            // query the current favorite
            List<ParseObject> currentObjects = current.find();
            ParseObject currentRecipe = currentObjects.get(0);

            // delete current favorite
            currentRecipe.deleteInBackground();

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
        registerForContextMenu(ingredientsListView);

        displayIngredients();

    }
}
