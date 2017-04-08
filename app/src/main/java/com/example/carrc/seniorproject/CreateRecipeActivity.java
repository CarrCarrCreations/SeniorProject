package com.example.carrc.seniorproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    ArrayList<EditText> ingredients = new ArrayList<EditText>();
    ParseObject first;
    List<ParseObject> x;
    String constant ="";
    String objectId = "before";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

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
                    if (query2.whereEqualTo("ID", Integer.toString(ingredientId)).count() == 0) {
                        taken = true;

                    } else {
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
        //Log.i("Info", "Here");
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);

        //LinearLayout myRoot = (LinearLayout) findViewById(R.id.my_root);
        LinearLayout a = new LinearLayout(this);
        a.setOrientation(LinearLayout.HORIZONTAL);

            int curTextViewId = prevTextViewId + 1;
            final EditText editText = new EditText(this);
            editText.setHint("Ingredient");
            //editText.setId(Integer.parseInt(curTextViewId + "" + 1));
            final EditText editText2 = new EditText(this);
            editText2.setHint("Quantity");
            //editText2.setId(Integer.parseInt(curTextViewId + "" + 1));
            final EditText editText3 = new EditText(this);
            editText3.setHint("Unit");
            //editText3.setId(Integer.parseInt(curTextViewId + "" + 1));
            a.setId(curTextViewId);

            final RelativeLayout.LayoutParams params =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);

            params.addRule(RelativeLayout.BELOW, prevTextViewId);
            //editText.setLayoutParams(params);
            a.setLayoutParams(params);
            prevTextViewId = curTextViewId;

            a.addView(editText);
            ingredients.add(editText);
            a.addView(editText2);
            ingredients.add(editText2);
            a.addView(editText3);
            ingredients.add(editText3);
            //myRoot.addView(a);
            //layout.addView(a, params);
              layout.addView(a);
    }
}

