package com.example.carrc.seniorproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

public class ModifyIngredientActivity extends AppCompatActivity {

    String name;

    EditText nameEditText;
    EditText unitEditText;
    EditText quantityEditText;

    class Ingredient {
        String name;
        String unit;
        String quantity;
    }

    public Ingredient getIngredientInfo(){

        Ingredient ingredientObject = new Ingredient();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Ingredients");
        query.whereEqualTo("Name", name);

        try {
            List<ParseObject> objects = query.find();
            ParseObject ingredient = objects.get(0);

            ingredientObject.name = ingredient.get("Name").toString();
            ingredientObject.unit = ingredient.get("Unit").toString();
            ingredientObject.quantity = ingredient.get("Quantity").toString();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ingredientObject;
    }

    public void saveChanges(View view){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Ingredients");
        query.whereEqualTo("Name", name);

        try {
            List<ParseObject> objects = query.find();
            ParseObject ingredient = objects.get(0);

            ingredient.put("Name", nameEditText.getText().toString());
            ingredient.put("Unit",unitEditText.getText().toString());
            ingredient.put("Quantity",quantityEditText.getText().toString());

            ingredient.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null){
                        Toast.makeText(getBaseContext(), "Ingredient Saved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getBaseContext(), e.getMessage() , Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void deleteIngredient(View view){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Ingredients");
        query.whereEqualTo("Name", name);

        try {
            List<ParseObject> objects = query.find();
            ParseObject ingredient = objects.get(0);

            ingredient.deleteInBackground(new DeleteCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null){
                        Toast.makeText(getBaseContext(), "Ingredient Deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getBaseContext(), e.getMessage() , Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_ingredient);

        Intent intent = getIntent();
        name = intent.getStringExtra("Name");

        setTitle(name);

        Ingredient ingredient = getIngredientInfo();

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        nameEditText.setText(ingredient.name);

        unitEditText = (EditText) findViewById(R.id.unitEditText);
        unitEditText.setText(ingredient.unit);

        quantityEditText = (EditText) findViewById(R.id.quantityEditText);
        quantityEditText.setText(ingredient.quantity);

    }
}
