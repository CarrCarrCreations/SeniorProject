package com.example.carrc.seniorproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class ReviewSelectedOrderActivity extends AppCompatActivity {

    String tableNumber;
    String ingredientName;
    String order;

    int ingredientNum;

    ListView ingredientListView;
    ArrayAdapter arrayAdapter;
    ArrayList<String> ingredients = new ArrayList<>();


    public void recreate(){
        Intent intent = new Intent(this, EmployeeDashboardActivity.class);
        startActivity(intent);
        finish();
    }


    public void getIngredients(){

        ParseQuery query = ParseQuery.getQuery("Orders");
        query.whereEqualTo("TableNumber", tableNumber);
        query.whereEqualTo("ItemTitle", order);

        ingredientNum = 0;

        try {
            List<ParseObject> objects = query.find();
            ParseObject recipe = objects.get(0);
            Log.i("ObjectSize", String.valueOf(objects.size()));

            ingredients.clear();

            do {

                if(recipe.get("IngredientName" + ingredientNum) == null){
                    break;
                }
                ingredientName = recipe.get("IngredientName" + ingredientNum).toString();

                ingredients.add(ingredientName);
                arrayAdapter.notifyDataSetChanged();

                ingredientNum++;

            } while (ingredientName != null);


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void alertUser(View view){

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Delete Ingredient")
                .setMessage("Are you sure you want to delete this ingredient?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // delete from database
                        deleteOrder();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    public void deleteOrder(){

        ParseQuery query = ParseQuery.getQuery("Cart");
        query.whereEqualTo("TableNumber", tableNumber);
        query.whereEqualTo("RecipeName", order);

        try {
            List<ParseObject> objects = query.find();
            ParseObject order = objects.get(0);

            order.deleteInBackground(new DeleteCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null){
                        Toast.makeText(getBaseContext(), "Order Deleted", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_review_selected_order);

        Intent intent = getIntent();
        tableNumber = intent.getStringExtra("tableNumber");
        order = intent.getStringExtra("order");


        ingredientListView = (ListView) findViewById(R.id.ingredientListView);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ingredients);
        ingredientListView.setAdapter(arrayAdapter);

        getIngredients();

    }
}
