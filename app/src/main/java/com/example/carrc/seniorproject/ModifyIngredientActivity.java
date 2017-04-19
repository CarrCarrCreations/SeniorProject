package com.example.carrc.seniorproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ModifyIngredientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_ingredient);

        Intent intent = getIntent();
        String title = intent.getStringExtra("Name");

        setTitle(title);

    }
}
