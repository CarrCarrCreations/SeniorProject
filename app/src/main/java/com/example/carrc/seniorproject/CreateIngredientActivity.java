package com.example.carrc.seniorproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

public class CreateIngredientActivity extends AppCompatActivity {

    EditText nameEditText;
    EditText unitEditText;
    EditText quantityEditText;

    public void recreate(){
        Intent intent = new Intent(this, IngredientManagementActivity.class);
        startActivity(intent);
        finish();
    }

    public void saveIngredient(View view){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Ingredients");
        query.orderByDescending("createdAt");
        query.setLimit(1);

        List<ParseObject> objects;

        try {
            objects = query.find();
            ParseObject temp = objects.get(0);
            int id = Integer.parseInt(temp.get("ID").toString()) + 1;
            String stringID = String.valueOf(id);

            ParseObject ingredient = new ParseObject("Ingredients");
            ingredient.put("Name", nameEditText.getText().toString());
            ingredient.put("Unit", unitEditText.getText().toString());
            ingredient.put("Quantity", quantityEditText.getText().toString());
            ingredient.put("ID", stringID);

            ingredient.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null){
                        Toast.makeText(getBaseContext(), " Ingredient Saved!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getBaseContext(), e.getMessage() , Toast.LENGTH_SHORT).show();
                    }
                }
            });

            recreate();

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ingredient);

        setTitle("Create Ingredient");

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        unitEditText = (EditText) findViewById(R.id.unitEditText);
        quantityEditText = (EditText) findViewById(R.id.quantityEditText);

    }
}
