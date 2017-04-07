package com.example.carrc.seniorproject;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import static com.example.carrc.seniorproject.R.id.soyCheckBox;

/**
 * Created by scottieb on 4/3/17.
 */

public class EditRecipeActivity extends AppCompatActivity{

    int prevTextViewId = 0;
    ArrayList<EditText> ingredients = new ArrayList<EditText>();
    String FoodID = "";
    boolean taken = false;

    public void update(View view) {

        System.out.println(ingredients.size());
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipes");
        query.whereContains("FoodID", FoodID);
        ParseObject recipe = null;
        final Context _this = this;
        try {
            recipe = query.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // query.getInBackground(FoodID, new GetCallback<ParseObject>() {  //retrieve serverID instead of object from parse
          //  public void done(ParseObject recipe, ParseException e) {
            //    if (e == null) {

                    int listCount = 0;

                    // Food name

                    EditText name = (EditText) findViewById(R.id.recipeText);

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

                    //ParseObject recipeNew = new ParseObject("Recipes");

                    int ingredientId;

                    ParseQuery<ParseObject> query2 = new ParseQuery<ParseObject>("Ingredients");
                    try {
                        if (query2.whereEqualTo("Name", ingredients.get(listCount).getText().toString()).count() == 0) {

                            //Not an ingredient that already exists
                            ParseObject ingredient = new ParseObject("Ingredients");
                            ingredient.put("Name", ingredients.get(listCount).getText().toString());
                            ingredient.put("Unit", ingredients.get(listCount + 2).getText().toString());

                            ingredientId = (int) (Math.random() * 1000001) + 1;
                            taken = false;
                            while (!taken) {
                                ParseQuery<ParseObject> query3 = new ParseQuery<ParseObject>("Ingredients");
                                if (query3.whereEqualTo("ID", Integer.toString(ingredientId)).count() == 0) {
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


                            ParseQuery<ParseObject> query4 = ParseQuery.getQuery("Ingredients");
                            query4.whereEqualTo("Name", ingredients.get(listCount).getText().toString());
                            ParseObject ingredient;
                            try {
                                ingredient = query4.getFirst();
                                //query.whereNotContainedIn("username", person.getList("friends"));
                                //query.whereNotContainedIn("email", n);
                                //query.setLimit(15);
                                recipe.put("IngredientID0", ingredient.get("ID").toString());
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }


                            recipe.put("IngredientName0", ingredients.get(listCount).getText().toString());
                            recipe.put("IngredientAmount0", ingredients.get(listCount + 1).getText().toString());
                            recipe.put("IngredientUnit0", ingredients.get(listCount + 2).getText().toString());

                            // get ingredient ID

                        }
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }


                    recipe.put("IngredientName0", ingredients.get(listCount).getText().toString());
                    recipe.put("IngredientAmount0", ingredients.get(listCount+1).getText().toString());
                    recipe.put("IngredientUnit0", ingredients.get(listCount+2).getText().toString());
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
                    recipe.put("weightWatcher", pricePerServing.getText().toString());

                    listCount +=3;


                    for(int i=1; i<prevTextViewId; i++){

                        ParseQuery<ParseObject> query5 = new ParseQuery<ParseObject>("Ingredients");

                        try {
                            if (query5.whereEqualTo("Name", ingredients.get(listCount).getText().toString()).count() == 0) {
                                //Not an ingredient that already exists
                                ParseObject ingredient = new ParseObject("Ingredients");
                                ingredient.put("Name", ingredients.get(listCount).getText().toString());
                                ingredient.put("Unit", ingredients.get(listCount + 2).getText().toString());

                                ingredientId = (int) (Math.random() * 1000001) + 1;
                                taken = false;
                                while (!taken) {
                                    ParseQuery<ParseObject> query6 = new ParseQuery<ParseObject>("Ingredients");
                                    if (query6.whereEqualTo("ID", Integer.toString(ingredientId)).count() == 0) {
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

                                ParseQuery<ParseObject> query7 = ParseQuery.getQuery("Ingredients");
                                query7.whereEqualTo("Name", ingredients.get(listCount).getText().toString());
                                ParseObject ingredient;
                                try {
                                    ingredient = query7.getFirst();
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
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                        listCount += 3;
                    }

                    recipe.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null) {
                                Toast.makeText(_this, "Recipe updated in the database!", Toast.LENGTH_SHORT).show();
                            } else {
                                System.out.println(e.getMessage());
                            }
                        }
                    });


         //       }
         //   }
        //});

    }

    public void fillRest(ParseObject recipe){
                    RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);

                    //LinearLayout myRoot = (LinearLayout) findViewById(R.id.my_root);
                    LinearLayout a = new LinearLayout(this);
                    // cant call this here.

                    //LinearLayout a = (LinearLayout) (findViewById(R.id.linearLayout));

                    a.setOrientation(LinearLayout.HORIZONTAL);

                    //get first ingredient

                        int curTextViewId = prevTextViewId + 1;
                         EditText editText = new EditText(this);
                        editText.setHint("Ingredient");
                        editText.setText(recipe.get("IngredientName0").toString());
                        //editText.setId(Integer.parseInt(curTextViewId + "" + 1));
                         EditText editText2 = new EditText(this);
                        editText2.setHint("Quantity");
                        editText2.setText(recipe.get("IngredientAmount0").toString());
                        //editText2.setId(Integer.parseInt(curTextViewId + "" + 1));
                         EditText editText3 = new EditText(this);
                        editText3.setHint("Unit");
                        editText3.setText(recipe.get("IngredientUnit0").toString());
                        //editText3.setId(Integer.parseInt(curTextViewId + "" + 1));
                        a.setId(curTextViewId);

                         RelativeLayout.LayoutParams params =
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



        // do the rest

                    String x = "IngredientName";
                    int ingCount = 1;
                    //System.out.println(x+ingCount);
                    //while recipe.get(IngredientNameX) is not null, if IngredientNameX is not null, it has a triple
                    // where Name Amount and Unit are all not null
                    while(recipe.get(x + ingCount)!= null){

                        a = new LinearLayout(this);
                        // cant call this here.

                        //LinearLayout a = (LinearLayout) (findViewById(R.id.linearLayout));

                        a.setOrientation(LinearLayout.HORIZONTAL);

                        curTextViewId = prevTextViewId + 1;
                        editText = new EditText(this);
                        editText.setHint("Ingredient");
                        editText.setText(recipe.get("IngredientName" + ingCount).toString());
                        //editText.setId(Integer.parseInt(curTextViewId + "" + 1));
                        editText2 = new EditText(this);
                        editText2.setHint("Quantity");
                        editText2.setText(recipe.get("IngredientAmount" + ingCount).toString());
                        //editText2.setId(Integer.parseInt(curTextViewId + "" + 1));
                        editText3 = new EditText(this);
                        editText3.setHint("Unit");
                        editText3.setText(recipe.get("IngredientUnit" + ingCount).toString());
                        //editText3.setId(Integer.parseInt(curTextViewId + "" + 1));
                        a.setId(curTextViewId);

                         params =
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





                        //get next ingredient
                        ingCount++;
                    }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        //TODO : get ObjectId for user chosen recipe (before everything loads)



    }

    public void changeLayout(View view) throws ParseException {

        EditText editRecipeText = (EditText) findViewById(R.id.editRecipeName);

        ParseQuery<ParseObject> query3 = new ParseQuery<ParseObject>("Recipes");
        if(query3.whereEqualTo("ItemTitle", editRecipeText.getText().toString()).count() == 1) {

            String editRecipeName = editRecipeText.getText().toString();

            ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Recipes");
            query2.whereEqualTo("ItemTitle",editRecipeName);
            ParseObject recipe;
            try {
                recipe = query2.getFirst();
                //query.whereNotContainedIn("username", person.getList("friends"));
                //query.whereNotContainedIn("email", n);
                //query.setLimit(15);
                //recipe.put("IngredientID0", ingredient.get("ID").toString());
                FoodID = recipe.get("FoodID").toString();
            } catch (ParseException e1) {
                e1.printStackTrace();
            }

            RelativeLayout editLayout = (RelativeLayout) findViewById(R.id.editLayout);
            editLayout.setVisibility(View.INVISIBLE);

            RelativeLayout recipeLayout = (RelativeLayout) findViewById(R.id.activity_create_recipe);
            recipeLayout.setVisibility(View.VISIBLE);

            getRecipe(FoodID);
        } else {
            Toast.makeText(this, "Recipe does not exist in the database!", Toast.LENGTH_SHORT).show();

        }

    }

    public void getRecipe(String FoodID){
        // get recipe using name
        // make view visible


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipes");
        query.whereEqualTo("FoodID", FoodID);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    if(objects.size()>0) {
                        ParseObject recipe = objects.get(0);
                        EditText name = (EditText) findViewById(R.id.recipeText);
                        name.setText(recipe.get("ItemTitle").toString());
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
                        imageUrl.setText(recipe.get("Image").toString());

                        // EditTexts

                        EditText pricePerServing = (EditText) findViewById(R.id.pricePerServing);
                        EditText course = (EditText) findViewById(R.id.course);
                        EditText mealType = (EditText) findViewById(R.id.mealType);
                        EditText weightWatcher = (EditText) findViewById(R.id.weightWatcher);

                        pricePerServing.setText(recipe.get("PricePerServing").toString());
                        course.setText(recipe.get("course").toString());
                        mealType.setText(recipe.get("mealType").toString());
                        weightWatcher.setText(recipe.get("weightWatcher").toString());

                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        System.out.println("Toggling values");
                        if (recipe.get("dairyFree") != null && recipe.get("dairyFree").toString().equals("true")) {
                            dairyCheckBox.toggle();
                        }
                        if (recipe.get("glutenFree") != null && recipe.get("glutenFree").toString().equals("true")) {
                            glutenCheckBox.toggle();
                        }
                        if (recipe.get("peanutFree") != null && recipe.get("peanutFree").toString().equals("true")) {
                            peanutCheckBox.toggle();
                        }
                        if (recipe.get("seafoodFree") != null && recipe.get("seafoodFree").toString().equals("true")) {
                            seafoodCheckBox.toggle();
                        }
                        if (recipe.get("vegan") != null && recipe.get("vegan").toString().equals("true")) {
                            veganCheckBox.toggle();
                        }
                        if (recipe.get("vegetarian") != null && recipe.get("vegetarian").toString().equals("true")) {
                            vegetarianCheckBox.toggle();
                        }
                        if (recipe.get("veryPopular") != null && recipe.get("veryPopular").toString().equals("true")) {
                            veryPopularCheckBox.toggle();
                        }
                        if (recipe.get("veryHealthy") != null && recipe.get("veryHealthy").toString().equals("true")) {
                            veryHealthyCheckBox.toggle();
                        }

                        if (recipe.get("soyFree") != null && recipe.get("soyFree").toString().equals("true")) {
                            soyCheckBox.toggle();
                        }
                        if (recipe.get("wheatFree") != null && recipe.get("wheatFree").toString().equals("true")) {
                            wheatCheckBox.toggle();
                        }
                        if (recipe.get("shellfishFree") != null && recipe.get("shellfishFree").toString().equals("true")) {
                            shellfishCheckBox.toggle();
                        }
                        if (recipe.get("sesameFree") != null && recipe.get("sesameFree").toString().equals("true")) {
                            sesameCheckBox.toggle();
                        }
                        if (recipe.get("eggFree") != null && recipe.get("eggFree").toString().equals("true")) {
                            eggCheckBox.toggle();
                        }
                        fillRest(recipe);

                    }
                } else {
                    // Something went wrong.
                }
            }
        });

        // populate fields

        // Food name

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
