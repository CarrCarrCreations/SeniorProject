package com.example.carrc.seniorproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.ArrayList;

/**
 * Created by scottieb on 4/3/17.
 */

public class EditRecipeActivity extends AppCompatActivity{

    int prevTextViewId = 0;
    ArrayList<EditText> ingredients = new ArrayList<EditText>();

    public void update() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);


        // populate fields

        // Food name

        EditText name = (EditText) findViewById(R.id.recipeText);
        name.setText(variable.get("ItemTitle"));
        // Intolerance Checkboxes

        CheckBox dairyCheckBox = (CheckBox) findViewById(R.id.dairyCheckBox);
        CheckBox glutenCheckBox = (CheckBox) findViewById(R.id.glutenCheckBox);
        CheckBox peanutCheckBox = (CheckBox) findViewById(R.id.peanutCheckBox);
        CheckBox seafoodCheckBox = (CheckBox) findViewById(R.id.seafoodCheckBox);
        CheckBox veganCheckBox = (CheckBox) findViewById(R.id.veganCheckBox);
        CheckBox vegetarianCheckBox = (CheckBox) findViewById(R.id.vegetarianCheckBox);
        CheckBox veryPopularCheckBox = (CheckBox) findViewById(R.id.veryPopularCheckBox);
        CheckBox veryHealthyCheckBox = (CheckBox) findViewById(R.id.healthyCheckBox);

        // image
        EditText imageUrl = (EditText) findViewById(R.id.imageUrl);
        imageUrl.setText(variable.get("Image"));

        // EditTexts

        EditText pricePerServing = (EditText) findViewById(R.id.pricePerServing);
        EditText course = (EditText) findViewById(R.id.course);
        EditText mealType = (EditText) findViewById(R.id.mealType);
        EditText weightWatcher = (EditText) findViewById(R.id.weightWatcher);

        pricePerServing.setText();
        course.setText();
        mealType.setText();
        weightWatcher.setText();


        if(variable.get("dairyFree") == "true"){
            dairyCheckBox.toggle();
        }
        if(variable.get("glutenFree") == "true"){
            glutenCheckBox.toggle();
        }
        if(variable.get("peanutFree") == "true"){
            peanutCheckBox.toggle();
        }
        if(variable.get("seafoodFree") == "true"){
            seafoodCheckBox.toggle();
        }
        if(variable.get("vegan") == "true"){
            veganCheckBox.toggle();
        }
        if(variable.get("vegetarian") == "true"){
            vegetarianCheckBox.toggle();
        }
        if(variable.get("veryPopular") == "true"){
            veryPopularCheckBox.toggle();
        }
        if(variable.get("veryHealthy") == "true"){
            veryHealthyCheckBox.toggle();
        }


        RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);

        //LinearLayout myRoot = (LinearLayout) findViewById(R.id.my_root);
        LinearLayout a = new LinearLayout(this);
        a.setOrientation(LinearLayout.HORIZONTAL);

        //get first ingredient

        while(ingredient value != null){


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





            //get next ingredient

        }

    }

    public void display(View view) throws ParseException {

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
    /*
        CheckBox eggCheckBox = (CheckBox) findViewById(R.id.eggCheckBox);
        CheckBox sesameCheckBox = (CheckBox) findViewById(R.id.sesameCheckBox);
        CheckBox shellfishCheckBox = (CheckBox) findViewById(R.id.shellfishCheckBox);
        CheckBox soyCheckBox = (CheckBox) findViewById(R.id.soyCheckBox);
        CheckBox wheatCheckBox = (CheckBox) findViewById(R.id.wheatCheckBox);
    */

        // image
        EditText imageUrl = (EditText) findViewById(R.id.imageUrl);

        // EditTexts

        EditText pricePerServing = (EditText) findViewById(R.id.pricePerServing);
        EditText course = (EditText) findViewById(R.id.course);
        EditText mealType = (EditText) findViewById(R.id.mealType);
        EditText weightWatcher = (EditText) findViewById(R.id.weightWatcher);


        // SQL PUSH all ingredients,

        ParseObject recipe = new ParseObject("Recipes");

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
        // Need to change?
        recipe.put("cheap", "false");
//
        recipe.put("PricePerServing", pricePerServing.getText().toString());
        recipe.put("course", course.getText().toString());
        recipe.put("mealType", mealType.getText().toString());
        recipe.put("weightWatcher", pricePerServing.getText().toString());

        listCount +=3;


        for(int i=1; i<prevTextViewId; i++){

            recipe.put( "IngredientName" + ( (listCount/3) ) ,ingredients.get(listCount).getText().toString() );
            recipe.put( "IngredientAmount" + ( (listCount/3) ) ,ingredients.get(listCount+1).getText().toString() );
            recipe.put( "IngredientUnit" + ( (listCount/3) ) ,ingredients.get(listCount+2).getText().toString() );

            listCount += 3;
        }

        recipe.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {

                } else {
                    System.out.println(e.getMessage());
                }
            }
        });

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
