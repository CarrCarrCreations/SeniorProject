package com.example.carrc.seniorproject;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import bolts.Task;

public class CreateNewRecipeActivity extends AppCompatActivity {

    String recipeName;

    // popup alertbox variables
    IngredientListViewAdapter adapter;
    SearchView editsearch;
    ArrayList<IngredientObject> arraylist = new ArrayList<IngredientObject>();
    ListView subListView;
    AlertDialog.Builder mBuilder;
    AlertDialog dialog;
    View mView;

    ScrollView ingredientScrollView;
    TableLayout tableLayout;

    ArrayList<newIngredient> newIngred = new ArrayList<>();
    CheckBox[] checkBoxes;

    String text;
    int tag;

    EditText recipeNameEditText;
    EditText urlEditText;
    EditText priceEditText;
    EditText courseEditText;
    EditText mealEditText;

    CheckBox dairyCheckBox;
    CheckBox eggCheckBox;
    CheckBox glutenCheckBox;
    CheckBox peanutCheckBox;
    CheckBox sesameCheckBox;
    CheckBox seafoodCheckBox;
    CheckBox shellfishCheckBox;
    CheckBox soyCheckBox;
    CheckBox wheatCheckBox;
    CheckBox veganCheckBox;
    CheckBox vegetarianCheckBox;

    int id;
    String stringID;


    public class newIngredient{
        String name;
        String id;
        int tag;
        EditText unit;
        EditText quantity;
        ImageView imageView;
        TableRow tableRow;
    }

    public void recreate(){
        Intent intent = new Intent(this, RecipeManagementSearchActivity.class);
        startActivity(intent);
        finish();
    }

    public String getID(String name){

        ParseQuery query = ParseQuery.getQuery("Ingredients");
        query.whereEqualTo("Name", name);

        try {
            List<ParseObject> objects = query.find();

            ParseObject ingred = objects.get(0);

            return ingred.get("ID").toString();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }



    public void addIngredient(View view){
        if(mView.getParent() == null){
            mBuilder.setView(mView);
            dialog = mBuilder.create();
            dialog.show();
        } else {
            dialog.show();
        }
    }


    public void getIngredientNames(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Ingredients");
        query.orderByDescending("createdAt");
        Task<Integer> count = query.countInBackground();
        SystemClock.sleep(2000);
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


    public void saveChanges(View view){

            ParseObject recipe = new ParseObject("Recipes");

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipes");
            query.orderByAscending("createdAt");
            query.setLimit(1);

            try {
                List<ParseObject> objects = query.find();

                if(objects.isEmpty()){
                    id = 1;
                    stringID = String.valueOf(id);
                } else {
                    ParseObject temp = objects.get(0);
                    id = Integer.parseInt(temp.get("FoodID").toString()) + 1;
                    stringID = String.valueOf(id);
                }

                recipe.put("FoodID", stringID);

            } catch (ParseException e) {
                e.printStackTrace();
            }


            recipe.put("ItemTitle", recipeNameEditText.getText().toString());
            recipe.put("Image", urlEditText.getText().toString());
            recipe.put("PricePerServing", priceEditText.getText().toString());
            recipe.put("course", courseEditText.getText().toString());
            recipe.put("mealType", mealEditText.getText().toString());


            for(int i = 0; i < newIngred.size(); i++){
                String ingredNameFormat = "IngredientName" + i;
                String ingredUnitFormat = "IngredientUnit" + i;
                String ingredQuantityFormat = "IngredientAmount" + i;
                String ingredIDFormat = "IngredientID" + i;

                recipe.put(ingredNameFormat, newIngred.get(i).name);
                recipe.put(ingredIDFormat, newIngred.get(i).id);
                recipe.put(ingredUnitFormat, newIngred.get(i).unit.getText().toString());
                recipe.put(ingredQuantityFormat, newIngred.get(i).quantity.getText().toString());
            }

            for(int i = 0; i < checkBoxes.length; i++){
                String formattedText = "";
                String checkboxText = checkBoxes[i].getText().toString().toLowerCase();

                if(checkboxText.matches("vegan") || checkboxText.matches("vegetarian")){
                    formattedText = checkboxText;
                } else {
                    formattedText = checkboxText + "Free";
                }

                if(checkBoxes[i].isChecked()){
                    recipe.put(formattedText, "true");
                } else {
                    recipe.put(formattedText, "false");
                }
            }

            recipe.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null){
                        Toast.makeText(getApplicationContext(), "Recipe Saved Successfully", Toast.LENGTH_SHORT).show();
                        recreate();
                    } else {
                        Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_recipe);

        Intent intent = getIntent();
        recipeName = intent.getStringExtra("Name");

        setTitle(recipeName);

        getIngredientNames();

        newIngred.clear();

        tag = 0;

        ingredientScrollView = (ScrollView) findViewById(R.id.ingredientScrollView);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        tableLayout.setColumnStretchable(0,true);
        tableLayout.setColumnStretchable(1,true);
        tableLayout.setColumnStretchable(2,true);


        recipeNameEditText = (EditText) findViewById(R.id.recipeNameEditText);
        recipeNameEditText.setText(recipeName);

        urlEditText = (EditText) findViewById(R.id.urlEditText);
        priceEditText = (EditText) findViewById(R.id.priceEditText);
        courseEditText = (EditText) findViewById(R.id.courseEditText);
        mealEditText = (EditText) findViewById(R.id.mealEditText);

        dairyCheckBox = (CheckBox) findViewById(R.id.dairyCheckBox);
        eggCheckBox = (CheckBox) findViewById(R.id.eggCheckBox);
        glutenCheckBox = (CheckBox) findViewById(R.id.glutenCheckBox);
        peanutCheckBox = (CheckBox) findViewById(R.id.peanutCheckBox);
        sesameCheckBox = (CheckBox) findViewById(R.id.sesameCheckBox);
        seafoodCheckBox = (CheckBox) findViewById(R.id.seafoodCheckBox);
        shellfishCheckBox = (CheckBox) findViewById(R.id.shellfishCheckBox);
        soyCheckBox = (CheckBox) findViewById(R.id.soyCheckBox);
        wheatCheckBox = (CheckBox) findViewById(R.id.wheatCheckBox);
        veganCheckBox = (CheckBox) findViewById(R.id.veganCheckBox);
        vegetarianCheckBox = (CheckBox) findViewById(R.id.vegetarianCheckBox);

        checkBoxes = new CheckBox[]{dairyCheckBox, eggCheckBox, glutenCheckBox, peanutCheckBox,
                sesameCheckBox, seafoodCheckBox, shellfishCheckBox, soyCheckBox, wheatCheckBox,
                veganCheckBox, vegetarianCheckBox};


        // create pop up window and link the search view
        mBuilder = new AlertDialog.Builder(CreateNewRecipeActivity.this);
        mView = getLayoutInflater().inflate(R.layout.recipe_search_view, null);

        subListView = (ListView) mView.findViewById(R.id.subListView);
        adapter = new IngredientListViewAdapter(this, arraylist, "Ingredient");
        subListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        subListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(arraylist.get(position).getIngredientNameName().equals("Create New Ingredient")){
                    Toast.makeText(getApplicationContext(), "Disabled. Create Ingredient in Ingredient Management" , Toast.LENGTH_SHORT).show();
                } else {

                    final newIngredient Ingredient = new newIngredient();
                    final TableRow tableRow = new TableRow(getBaseContext());

                    Ingredient.tag = tag;

                    String ingredName = arraylist.get(position).getIngredientNameName();
                    Ingredient.name = ingredName;

                    Ingredient.id = getID(Ingredient.name);


                    final TextView nameTextView = new TextView(getBaseContext());
                    nameTextView.setText(ingredName);
                    if (ingredName.length() > 15) {
                        nameTextView.setTextSize(14);
                    } else {
                        nameTextView.setTextSize(20);
                    }

                    EditText unitEditText = new EditText(getBaseContext());
                    unitEditText.setHint("Units");
                    Ingredient.unit = unitEditText;

                    EditText quantityEditText = new EditText(getBaseContext());
                    quantityEditText.setHint("Quantity");
                    Ingredient.quantity = quantityEditText;

                    // create the delete image and set its parameters
                    TableRow.LayoutParams parms = new TableRow.LayoutParams(75,75);
                    parms.topMargin = 30;
                    final ImageView imageView = new ImageView(getBaseContext());
                    imageView.setBackgroundResource(R.drawable.deleteicon);
                    imageView.setTag(tag);
                    imageView.setLayoutParams(parms);
                    Ingredient.imageView = imageView;

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            for(int i = 0; i < newIngred.size(); i++){

                                if(newIngred.get(i).tag == (Integer) v.getTag()){
                                    tableLayout.removeView(newIngred.get(i).tableRow);
                                    newIngred.remove(i);
                                }
                            }

                        }
                    });

                    newIngred.add(Ingredient);
                    tableRow.addView(nameTextView);
                    tableRow.addView(unitEditText);
                    tableRow.addView(quantityEditText);
                    tableRow.addView(imageView);
                    tableRow.requestFocus();

                    Ingredient.tableRow = tableRow;

                    tableLayout.addView(tableRow);

                    tag++;
                    text = "";
                    dialog.hide();
                }

            }
        });

        // Locate the EditText in listview_main.xml
        editsearch = (SearchView) mView.findViewById(R.id.searchView);
        editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        });

    }
}
