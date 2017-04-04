package com.example.carrc.seniorproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class IngredientManagementActivity extends AppCompatActivity {

    public void addIngred(View view){
        Intent intent = new Intent(this, AddIngredActivity.class);
        startActivity(intent);
    }

    public void editIngred(View view){
        Intent intent = new Intent(this, EditIngredActivity.class);
        startActivity(intent);
    }

    public void deleteIngred(View view){
        Intent intent = new Intent(this, DeleteIngredActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_management);

        setTitle("Ingredient Management");
    }
}
