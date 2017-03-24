package com.example.carrc.seniorproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MenuItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item);

        setTitle("Modify Order");

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

        TextView nameTextView = (TextView) findViewById(R.id.nameTextView);
        nameTextView.setText(name);
    }
}
