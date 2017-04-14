package com.example.carrc.seniorproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MenuItemActivity extends AppCompatActivity {

    ArrayAdapter arrayAdapter;
    ArrayList<String> ingredientsArrayList = new ArrayList<>();
    List<ingredientInfo> ingredients;
    String menuItems[] = {"Remove Ingredient"};

    Intent intent;

    EditText comments;

    int ingredientNum;
    int itemSelectedTag;

    String ingredientName;
    String ingredientID;
    String comment;
    // Recipe name and id
    String name;
    String id;
    String price;

    RelativeLayout activity_menu_item;
    ListView ingredientsListView;

    RatingBar ratingBar;


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

            ingredientsArrayList.remove(itemSelectedTag);
            ingredients.remove(itemSelectedTag);
            arrayAdapter.notifyDataSetChanged();
        }

        return super.onContextItemSelected(item);
    }


    public void addToCart(View view){
        ParseObject cartItem = new ParseObject("Cart");

        if(!comments.getText().toString().isEmpty()){
            comment = comments.getText().toString();
            cartItem.put("Comment", comment);
        }

        cartItem.put("RecipeName", name);
        cartItem.put("RecipeId", id);
        cartItem.put("Price", price);

        // put each ingredients name and id
        for(int i = 0; i < ingredients.size(); i++){
            cartItem.put("IngredientName" + i, ingredients.get(i).ingredName);
            cartItem.put("IngredientID" + i, ingredients.get(i).ingredID);
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

        comments.setText("");
    }

    public void displayIngredients(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipes");
        query.whereContains("FoodID", id);

        List<ParseObject> objects = null;
        try {
            objects = query.find();
            ParseObject recipe = objects.get(0);
            price = recipe.get("PricePerServing").toString();

            if(!ingredients.isEmpty()){
                ingredients.clear();
            }

            do {

                if(recipe.get("IngredientName" + ingredientNum) == null){
                    break;
                }

                ingredientName = recipe.get("IngredientName" + ingredientNum).toString();
                ingredientID = recipe.get("IngredientID" + ingredientNum).toString();

                ingredientInfo ingredientInfo = new ingredientInfo();
                ingredientInfo.ingredName = ingredientName;
                ingredientInfo.ingredID = ingredientID;
                ingredientInfo.ingredTag = ingredientNum;

                ingredients.add(ingredientInfo);

                ingredientsArrayList.add(ingredientName);
                arrayAdapter.notifyDataSetChanged();

                ingredientNum++;

            } while (ingredientName != null);


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void addToFavorites(View view){
        ParseObject favorite = new ParseObject("FavoriteMeals");
        favorite.put("Username", ParseUser.getCurrentUser().getUsername());
        favorite.put("MealName", name);
        favorite.put("MealID", id);
        favorite.put("Price", price);

        for(int i = 0; i < ingredients.size(); i++){
            favorite.put("IngredientName" + i, ingredients.get(i).ingredName);
            favorite.put("IngredientID" + i, ingredients.get(i).ingredID);
        }

        if(!comments.getText().toString().isEmpty()){
            comment = comments.getText().toString();
            favorite.put("Comments", comment);
        }


        favorite.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Toast.makeText(getBaseContext(), "Meal Saved To Favorites!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), e.getMessage() , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item);

        setTitle("Modify Order");

        intent = getIntent();
        name = intent.getStringExtra("name");
        id = intent.getStringExtra("id");

        activity_menu_item = (RelativeLayout) findViewById(R.id.activity_menu_item);
        comments = (EditText) findViewById(R.id.commentsEditText);

        ingredients = new ArrayList<>();

        TextView nameTextView = (TextView) findViewById(R.id.nameTextView);
        nameTextView.setText(name);

        ingredientsListView = (ListView) findViewById(R.id.ingredientsListView);
        registerForContextMenu(ingredientsListView);


        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ingredientsArrayList);
        ingredientsListView.setAdapter(arrayAdapter);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        displayIngredients();
    }
}
