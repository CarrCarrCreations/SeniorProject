package com.example.carrc.seniorproject;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseRole;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

/**
 * Created by scottieb on 3/26/17.
 */

public class CreateRecipeActivity extends AppCompatActivity {

    int prevTextViewId = 0;
    int prevSearchViewId = 0;
    ArrayList<EditText> ingredients = new ArrayList<EditText>();
    ArrayList<String> ingredientNames = new ArrayList<String>();
    ArrayList<String> ingredientNameList = new ArrayList<String>();
    ArrayList<String> ingredientUnitList = new ArrayList<String>();
    ArrayList<String> ingredientQuantityList = new ArrayList<String>();
    ParseObject first;
    List<ParseObject> x;
    String constant ="";
    String objectId = "before";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);
        populateInventoryList();

    }

    public void populateInventoryList() {
            //grab Ingredients table and display them.
            final Context _this = this;
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Ingredients");
            try {
                query.setLimit(300);
                List<ParseObject> ingredientsList = query.find();

                System.out.println("Size of query find" + ingredientsList.size());
                for(ParseObject ob: ingredientsList){

                    if(ob.get("Name")!= null){
                        ingredientNameList.add(ob.get("Name").toString());
                        ingredientNames.add(ob.get("Name").toString());

                    }
                    else
                        ingredientNameList.add("");
                    if(ob.get("Unit")!= null)
                        ingredientUnitList.add(ob.get("Unit").toString());
                    else
                        ingredientUnitList.add("");
                    if(ob.get("Quantity")!= null)
                        ingredientQuantityList.add(ob.get("Quantity").toString());
                    else
                        ingredientQuantityList.add("");
                }
            } catch (ParseException e){
                System.out.println(e.getMessage());
            }


    }

    public void finish(List<ParseObject> objects){

    }

    public void getPreviousRecipe(){

    }

    public void submit(View view) throws ParseException {

        EditText name = (EditText) findViewById(R.id.recipeText);

        ParseQuery<ParseObject> query3 = new ParseQuery<ParseObject>("Recipes");
        if(query3.whereEqualTo("ItemTitle", name.getText().toString()).count() == 0) {

            // give recipe new Id
            int recipeId = (int) (Math.random() * 70001) + 1;
            boolean taken = false;
            while (!taken) {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Recipes");
                if (query.whereEqualTo("FoodID", Integer.toString(recipeId)).count() == 0) {
                    taken = true;

                } else {
                    recipeId = (int) (Math.random() * 70001) + 1;

                }

            }

            //System.out.println(objectId);
            // System.out.println(objectId);

            //System.out.println(objectId);

            //        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Recipes");
            //        // query.orderByDescending("createdAt");
            //        query.addDescendingOrder("createdAt");
            //        // query.toString();
            //        System.out.println("::::::::::::::::::QUERY:::::::::::::::" + query);
            //        try {
            //            x = query.find();
            //        } catch (ParseException e) {
            //            Log.e("Error", e.getMessage());
            //            e.printStackTrace();
            //        }
            //        if (x.size() > 0) {
            //            for(ParseObject ob: x){
            //
            //                System.out.println(.get(0).get("createdAt").toString());
            //
            //
            //            }
            //        }


            //System.out.println(first.get("objectId").toString());
            //String oldRecipeId = query.get("objectId").toString();
            //int oldFoodID = Integer.parseInt(query.get("FoodID").toString());
            // System.out.println(oldFoodID);
            int listCount = 0;

            // Food name


            // Intolerance Checkboxes

            CheckBox dairyCheckBox = (CheckBox) findViewById(R.id.dairyCheckBox);
            CheckBox glutenCheckBox = (CheckBox) findViewById(R.id.glutenCheckBox);
            CheckBox peanutCheckBox = (CheckBox) findViewById(R.id.peanutCheckBox);
            CheckBox seafoodCheckBox = (CheckBox) findViewById(R.id.seafoodCheckBox);
            CheckBox veganCheckBox = (CheckBox) findViewById(R.id.veganCheckBox);
            CheckBox vegetarianCheckBox = (CheckBox) findViewById(R.id.vegetarianCheckBox);
            CheckBox veryPopularCheckBox = (CheckBox) findViewById(R.id.veryPopularCheckBox);
            CheckBox veryHealthyCheckBox = (CheckBox) findViewById(R.id.healthyCheckBox);

            CheckBox eggCheckBox = (CheckBox) findViewById(R.id.eggCheckBox);
            CheckBox sesameCheckBox = (CheckBox) findViewById(R.id.sesameCheckBox);
            CheckBox shellfishCheckBox = (CheckBox) findViewById(R.id.shellfishCheckBox);
            CheckBox soyCheckBox = (CheckBox) findViewById(R.id.soyCheckBox);
            CheckBox wheatCheckBox = (CheckBox) findViewById(R.id.wheatCheckBox);


            // image
            EditText imageUrl = (EditText) findViewById(R.id.imageUrl);

            // EditTexts

            EditText pricePerServing = (EditText) findViewById(R.id.pricePerServing);
            EditText course = (EditText) findViewById(R.id.course);
            EditText mealType = (EditText) findViewById(R.id.mealType);
            EditText weightWatcher = (EditText) findViewById(R.id.weightWatcher);


            // SQL PUSH all ingredients,

            ParseObject recipe = new ParseObject("Recipes");
            recipe.put("FoodID", Integer.toString(recipeId));
            int ingredientId;
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Ingredients");
            if (query.whereEqualTo("Name", ingredients.get(listCount).getText().toString()).count() == 0) {
                //Not an ingredient that already exists
                ParseObject ingredient = new ParseObject("Ingredients");
                ingredient.put("Name", ingredients.get(listCount).getText().toString());
                ingredient.put("Unit", ingredients.get(listCount + 2).getText().toString());

                ingredientId = (int) (Math.random() * 1000001) + 1;
                taken = false;
                while (!taken) {
                    ParseQuery<ParseObject> query2 = new ParseQuery<ParseObject>("Ingredients");
                    //if this ID
                    if (query2.whereEqualTo("ID", Integer.toString(ingredientId)).count() == 0) {
                        taken = true;

                    } else {
                        ingredientId = (int) (Math.random() * 1000001) + 1;

                    }

                }

                ingredient.put("ID", Integer.toString(ingredientId));

                ingredient.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                        } else {
                            System.out.println(e.getMessage());
                        }
                    }
                });

                recipe.put("IngredientName0", ingredients.get(listCount).getText().toString());
                recipe.put("IngredientAmount0", ingredients.get(listCount + 1).getText().toString());
                recipe.put("IngredientUnit0", ingredients.get(listCount + 2).getText().toString());
                recipe.put("IngredientID0", Integer.toString(ingredientId));

            } else {
                // ingredient exists


                ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Ingredients");
                query2.whereEqualTo("Name", ingredients.get(listCount).getText().toString());
                ParseObject ingredient;
                try {
                    ingredient = query2.getFirst();
                    //query.whereNotContainedIn("username", person.getList("friends"));
                    //query.whereNotContainedIn("email", n);
                    //query.setLimit(15);
                    // get ingredient ID

                    recipe.put("IngredientID0", ingredient.get("ID").toString());
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }


                recipe.put("IngredientName0", ingredients.get(listCount).getText().toString());
                recipe.put("IngredientAmount0", ingredients.get(listCount + 1).getText().toString());
                recipe.put("IngredientUnit0", ingredients.get(listCount + 2).getText().toString());


            }

            recipe.put("Image", imageUrl.getText().toString());
            recipe.put("ItemTitle", name.getText().toString());
            recipe.put("dairyFree", (Boolean.toString(dairyCheckBox.isChecked())));
            recipe.put("glutenFree", (Boolean.toString(glutenCheckBox.isChecked())));
            recipe.put("peanutFree", Boolean.toString(peanutCheckBox.isChecked()));
            recipe.put("seafoodFree", Boolean.toString(seafoodCheckBox.isChecked()));
            recipe.put("vegan", Boolean.toString(veganCheckBox.isChecked()));
            recipe.put("vegetarian", Boolean.toString(vegetarianCheckBox.isChecked()));
            recipe.put("veryHealthy", Boolean.toString(veryHealthyCheckBox.isChecked()));
            recipe.put("veryPopular", Boolean.toString(veryPopularCheckBox.isChecked()));
            recipe.put("eggFree", Boolean.toString(eggCheckBox.isChecked()));
            recipe.put("soyFree", Boolean.toString(soyCheckBox.isChecked()));
            recipe.put("sesameFree", Boolean.toString(sesameCheckBox.isChecked()));
            recipe.put("shellfishFree", Boolean.toString(shellfishCheckBox.isChecked()));
            recipe.put("wheatFree", Boolean.toString(wheatCheckBox.isChecked()));
            // Need to change?
            recipe.put("cheap", "false");
            //
            recipe.put("PricePerServing", pricePerServing.getText().toString());
            recipe.put("course", course.getText().toString());
            recipe.put("mealType", mealType.getText().toString());
            recipe.put("weightWatcher", weightWatcher.getText().toString());

            listCount += 3;


            for (int i = 1; i < prevTextViewId; i++) {


                if (query.whereEqualTo("Name", ingredients.get(listCount).getText().toString()).count() == 0) {
                    //Not an ingredient that already exists
                    ParseObject ingredient = new ParseObject("Ingredients");
                    ingredient.put("Name", ingredients.get(listCount).getText().toString());
                    ingredient.put("Unit", ingredients.get(listCount + 2).getText().toString());

                    ingredientId = (int) (Math.random() * 1000001) + 1;
                    taken = false;
                    while (!taken) {
                        ParseQuery<ParseObject> query2 = new ParseQuery<ParseObject>("Ingredients");
                        if (query2.whereEqualTo("ID", Integer.toString(ingredientId)).count() == 0) {
                            taken = true;

                        } else {
                            ingredientId = (int) (Math.random() * 1000001) + 1;
                        }

                    }

                    ingredient.put("ID", Integer.toString(ingredientId));

                    ingredient.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {

                            } else {
                                System.out.println(e.getMessage());
                            }
                        }
                    });

                    recipe.put("IngredientName" + ((listCount / 3)), ingredients.get(listCount).getText().toString());
                    recipe.put("IngredientAmount" + ((listCount / 3)), ingredients.get(listCount + 1).getText().toString());
                    recipe.put("IngredientUnit" + ((listCount / 3)), ingredients.get(listCount + 2).getText().toString());
                    recipe.put("IngredientID" + ((listCount / 3)), Integer.toString(ingredientId));

                } else {
                    // ingredient exists

                    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Ingredients");
                    query2.whereEqualTo("Name", ingredients.get(listCount).getText().toString());
                    ParseObject ingredient;
                    try {
                        ingredient = query2.getFirst();
                        //query.whereNotContainedIn("username", person.getList("friends"));
                        //query.whereNotContainedIn("email", n);
                        //query.setLimit(15);
                        // get ingredient ID

                        recipe.put("IngredientID" + ((listCount / 3)), ingredient.get("ID").toString());
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }

                    recipe.put("IngredientName" + ((listCount / 3)), ingredients.get(listCount).getText().toString());
                    recipe.put("IngredientAmount" + ((listCount / 3)), ingredients.get(listCount + 1).getText().toString());
                    recipe.put("IngredientUnit" + ((listCount / 3)), ingredients.get(listCount + 2).getText().toString());

                }

                listCount += 3;
            }

            recipe.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {

                    } else {
                        System.out.println(e.getMessage());
                    }
                }
            });

            Toast.makeText(this, "Recipe created in the database!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Recipe already exists in the database!", Toast.LENGTH_SHORT).show();

        }
    }


    public void addItem(View view){
        //keeping track of rows essentially
        int curTextViewId = prevTextViewId + 1;
        prevTextViewId = curTextViewId;
        //Log.i("Info", "Here");
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        TableRow tableRow = new TableRow(this);

        //LinearLayout myRoot = (LinearLayout) findViewById(R.id.my_root);
        LinearLayout searchViewLayout = new LinearLayout(this);
        searchViewLayout.setOrientation(LinearLayout.VERTICAL);

        //test 1 listView for all

        final ListView listIngred = new ListView(this);



        final float scale = getResources().getDisplayMetrics().density;
        //int height = (int) (50 * scale + 0.5f);
        final SearchView searchViewIngred = new SearchView(this);
        int curSearchViewId = prevSearchViewId + 1;
        searchViewIngred.setId(curSearchViewId);
        final ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ingredientNames);
        listIngred.setAdapter(adapter);
        // old Ingredient EditText, keeping it for algorithm to stay consistent
        final EditText editText = new EditText(this);
        searchViewIngred.setQueryHint("Ingredient");
        //editText.setId(Integer.parseInt(curTextViewId + "" + 1));
        final EditText editText2 = new EditText(this);
        editText2.setHint("Quantity");
        //editText2.setId(Integer.parseInt(curTextViewId + "" + 1));
        final EditText editText3 = new EditText(this);
        editText3.setHint("Unit");
        //editText3.setId(Integer.parseInt(curTextViewId + "" + 1));

        listIngred.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Object listItem = listIngred.getItemAtPosition(position);
                String str = (String) listItem; //As you are using Default String Adapter
                editText.setText(str);
                if(ingredientNames.contains(str)){
                    //search databse to get file
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Ingredients");
                    query.whereEqualTo("Name", str);
                    try {
                        ParseObject ingredient = query.getFirst();
                        editText3.setText(ingredient.get("Unit").toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                searchViewIngred.setQuery( (CharSequence) str, false);
                searchViewIngred.setIconified(false);

                listIngred.setVisibility(View.GONE);
            }
        });

        listIngred.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View __v, MotionEvent __event) {
                if (__event.getAction() == MotionEvent.ACTION_DOWN) {
                    //  Disallow the touch request for parent scroll on touch of child view
                    requestDisallowParentInterceptTouchEvent(__v, true);
                } else if (__event.getAction() == MotionEvent.ACTION_UP || __event.getAction() == MotionEvent.ACTION_CANCEL) {
                    // Re-allows parent events
                    requestDisallowParentInterceptTouchEvent(__v, false);
                }
                return false;
            }
        });

        searchViewIngred.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                editText.setText(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                // searchViewIngred.getId

                // I HAVE IDD
                // whatever ID i give it, use that as the i, the i will be same as curSearchViewID
                //ingredients.set(  (curSearchViewId  - 1)   , ingredients.get( (curSearchViewId -1)).setText(newText) );
                listIngred.setVisibility(View.VISIBLE);
                listIngred.setAlpha(1);
                adapter.getFilter().filter(newText);
                editText.setText(newText);

                if(ingredientNames.contains(newText)){
                    //search databse to get file
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Ingredients");
                    query.whereEqualTo("Name", newText);
                    try {
                        ParseObject ingredient = query.getFirst();
                        editText3.setText(ingredient.get("Unit").toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                return false;
            }
        });







        //editText.setLayoutParams(params);
        ingredients.add(editText);
        ingredients.add(editText2);
        ingredients.add(editText3);

        searchViewLayout.addView(searchViewIngred);
        searchViewLayout.addView(listIngred);
        tableRow.addView(searchViewLayout);
        tableRow.addView(editText2);
        tableRow.addView(editText3);

        tableLayout.addView(tableRow);
    }

    private void requestDisallowParentInterceptTouchEvent(View __v, Boolean __disallowIntercept) {
        while (__v.getParent() != null && __v.getParent() instanceof View) {
            if (__v.getParent() instanceof ScrollView) {
                __v.getParent().requestDisallowInterceptTouchEvent(__disallowIntercept);
            }
            __v = (View) __v.getParent();
        }
    }
}

