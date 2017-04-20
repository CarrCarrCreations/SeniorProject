package com.example.carrc.seniorproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
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
import android.widget.PopupWindow;
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
import java.util.Objects;

public class MenuItemActivity extends AppCompatActivity {

    ArrayAdapter arrayAdapter;
    ArrayList<String> ingredientsArrayList = new ArrayList<>();
    List<ingredientInfo> ingredients;
    ArrayList<String> menuItem = new ArrayList<>();

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
    String ingredType;

    RelativeLayout activity_menu_item;
    ListView ingredientsListView;

    RatingBar ratingBar;

    // popup alertbox variables
    ArrayList<String> subIngredArray = new ArrayList<>();
    ArrayAdapter SubArrayAdapter;
    ListView subListView;
    AlertDialog.Builder mBuilder;
    AlertDialog dialog;
    View mView;




    public class ingredientInfo {
        String ingredName;
        String ingredID;
        int ingredTag;
    }

    public void recreate(){
        startActivity(getIntent());
        finish();
    }

    // create the menu for long pressing an item on the listView
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        // do a query to find the selected ingredient and it's type
        itemSelectedTag = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
        ParseQuery<ParseObject> ingred = ParseQuery.getQuery("Ingredients");
        ingred.whereEqualTo("Name", ingredientsArrayList.get(itemSelectedTag));
        try {
            List<ParseObject> objects = ingred.find();
            ParseObject selectedIngred = objects.get(0);
            if(selectedIngred.get("Type") != null){
                ingredType = selectedIngred.get("Type").toString();

                // do a query to find all ingredients with the ingredType
                ParseQuery<ParseObject> typeQuery = ParseQuery.getQuery("Ingredients");
                typeQuery.whereEqualTo("Type", ingredType);
                objects = typeQuery.find();
                if(objects.size() > 0){
                    menuItem.clear();
                    subIngredArray.clear();
                    menuItem.add("Remove Ingredient");
                    menuItem.add("Substitute Ingredient");

                    for(int i = 0; i < objects.size(); i++){
                        subIngredArray.add(objects.get(i).get("Name").toString());
                    }

                    SubArrayAdapter.notifyDataSetChanged();

                } else {
                    menuItem.add("Remove Ingredient");
                }
            } else {
                menuItem.clear();
                menuItem.add("Remove Ingredient");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(v.getId() == R.id.ingredientsListView){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            for(int i = 0; i < menuItem.size(); i++){
                menu.add(Menu.NONE, i, i,menuItem.get(i));
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String menuItemName = menuItem.get(menuItemIndex);


        if(menuItemName.matches("Remove Ingredient")){

            ingredientsArrayList.remove(itemSelectedTag);
            ingredients.remove(itemSelectedTag);
            arrayAdapter.notifyDataSetChanged();
        }

        if(menuItemName.matches("Substitute Ingredient")){

            // create and show the dialog box
            if(mView.getParent() == null){
                mBuilder.setView(mView);
                dialog = mBuilder.create();
                dialog.show();
            } else {
                dialog.show();
            }
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


        // create the alert dialog box with the substitute_popup layout
        mBuilder = new AlertDialog.Builder(MenuItemActivity.this);
        mView = getLayoutInflater().inflate(R.layout.substitute_popup, null);

        subListView = (ListView) mView.findViewById(R.id.subListView);
        SubArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, subIngredArray);
        subListView.setAdapter(SubArrayAdapter);

        subListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = subIngredArray.get(position);

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Ingredients");
                query.whereEqualTo("Name", name);

                List<ParseObject> objects;
                try {
                    objects = query.find();
                    ParseObject tempIngred = objects.get(0);

                    ingredientInfo ingredient = new ingredientInfo();
                    ingredient.ingredName = tempIngred.get("Name").toString();
                    ingredient.ingredID = tempIngred.get("ID").toString();

                    // remove the old ingredient
                    ingredientsArrayList.remove(itemSelectedTag);
                    ingredients.remove(itemSelectedTag);
                    arrayAdapter.notifyDataSetChanged();

                    // add the new ingredient
                    ingredients.add(ingredient);
                    ingredientsArrayList.add(ingredient.ingredName);
                    arrayAdapter.notifyDataSetChanged();

                    dialog.hide();

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
