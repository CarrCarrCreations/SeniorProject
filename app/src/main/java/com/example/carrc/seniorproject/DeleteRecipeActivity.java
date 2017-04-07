package com.example.carrc.seniorproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by scottieb on 4/5/17.
 */

public class DeleteRecipeActivity extends AppCompatActivity {

    String FoodID = "";
    public void deleteRecipe(View view){
        //get recipe in search view
        EditText deleteRecipeText = (EditText) findViewById(R.id.editText);
        String deleteRecipeName = deleteRecipeText.getText().toString();

        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Recipes");
        query2.whereEqualTo("ItemTitle",deleteRecipeName);
        ParseObject recipe;
        try {
            recipe = query2.getFirst();
            //query.whereNotContainedIn("username", person.getList("friends"));
            //query.whereNotContainedIn("email", n);
            //query.setLimit(15);
            //recipe.put("IngredientID0", ingredient.get("ID").toString());
            FoodID = recipe.get("FoodID").toString();
            System.out.println(FoodID);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        //ask for confirmation

        //delete from database

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_recipe);
    }
}
