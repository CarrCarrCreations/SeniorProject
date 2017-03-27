package com.example.carrc.seniorproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.w3c.dom.Text;

import java.util.List;

public class MenuItemActivity extends AppCompatActivity {

    int ingredientNum;
    String ingredientName;
    boolean whileState;
    LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item);

        setTitle("Modify Order");

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String id = intent.getStringExtra("id");

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        ingredientNum = 0;
        whileState = true;

        TextView nameTextView = (TextView) findViewById(R.id.nameTextView);
        nameTextView.setText(name);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipes");
        query.whereContains("FoodID", id);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null && objects.size() > 0){

                    ParseObject recipe = objects.get(0);

                    do {

                        if(recipe.get("IngredientName" + ingredientNum) == null){
                            break;
                        }

                        ingredientName = recipe.get("IngredientName" + ingredientNum).toString();

                        TextView textView = new TextView(getApplicationContext());
                        textView.setTextColor(Color.parseColor("Black"));
                        textView.setPadding(0, 30, 0, 30);
                        textView.setText(ingredientName);

                        linearLayout.addView(textView);

                        ingredientNum++;

                    } while (ingredientName != null);

                    EditText comments = new EditText(getApplicationContext());
                    comments.setHint("Enter comments here");
                    linearLayout.addView(comments);
                }
            }
        });
    }
}
