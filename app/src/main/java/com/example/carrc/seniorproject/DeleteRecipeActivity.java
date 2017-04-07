package com.example.carrc.seniorproject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by scottieb on 4/5/17.
 */

public class DeleteRecipeActivity extends AppCompatActivity {

    String FoodID = "";
    public void deleteRecipe(View view) throws ParseException {

        EditText deleteRecipeText = (EditText) findViewById(R.id.editText);

        ParseQuery<ParseObject> query3 = new ParseQuery<ParseObject>("Recipes");
        if(query3.whereEqualTo("ItemTitle", deleteRecipeText.getText().toString()).count() == 1) {
            //get recipe in search view
            String deleteRecipeName = deleteRecipeText.getText().toString();

            ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Recipes");
            query2.whereEqualTo("ItemTitle", deleteRecipeName);
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

            //ask for confirmation

            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Deleting Recipe")
                    .setMessage("Are you sure you want to delete this recipe?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // delete from database
                            finishDelete();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();

            //delete from database
        } else {
            Toast.makeText(this, "Recipe does not exist in the database!", Toast.LENGTH_SHORT).show();
        }
    }

    public void finishDelete(){
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Recipes");
        query2.whereEqualTo("FoodID", FoodID);
        ParseObject recipe;
        try {
            recipe = query2.getFirst();
            //query.whereNotContainedIn("username", person.getList("friends"));
            //query.whereNotContainedIn("email", n);
            //query.setLimit(15);
            //recipe.put("IngredientID0", ingredient.get("ID").toString());
            recipe.delete();
            Toast.makeText(this, "Recipe deleted from the database!", Toast.LENGTH_SHORT).show();
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_recipe);
    }
}
