package com.example.carrc.seniorproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RecipeManagementActivity extends AppCompatActivity {

    public void openRecipe(View view) {
        Intent intent = new Intent(this, CreateRecipeActivity.class);
        startActivity(intent);
    }

    public void editRecipe(View view) {
        Intent intent = new Intent(this, EditRecipeActivity.class);
        startActivity(intent);
    }

    public void deleteRecipe(View view) {
        Intent intent = new Intent(this, DeleteRecipeActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_management);

        setTitle("Recipe Management");
    }
}