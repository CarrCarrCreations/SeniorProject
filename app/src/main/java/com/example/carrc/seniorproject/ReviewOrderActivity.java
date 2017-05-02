package com.example.carrc.seniorproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class ReviewOrderActivity extends AppCompatActivity {

    ArrayList<String> orders = new ArrayList<>();
    ListView orderListView;
    ArrayAdapter arrayAdapter;

    String tableNumber;

    public void getOrders(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Orders");
        query.whereEqualTo("TableNumber", tableNumber);

        try {
            List<ParseObject> objects = query.find();
            if(objects.size() > 0){
                orders.clear();
                for(int i = 0; i < objects.size(); i++){
                    String orderName = objects.get(i).get("ItemTitle").toString();
                    orders.add(orderName);
                }
                arrayAdapter.notifyDataSetChanged();

            } else {
                orders.clear();
                orders.add("No Current Orders for this Table");
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_order);

        Intent intent = getIntent();
        tableNumber = intent.getStringExtra("tableNumber").trim();

        setTitle("Review Order: Table " + tableNumber);

        orderListView = (ListView) findViewById(R.id.orderListView);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, orders);
        orderListView.setAdapter(arrayAdapter);

        getOrders();

        orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(!orders.get(position).matches("No Current Orders for this Table")){
                    Intent intent = new Intent(getApplicationContext(), ReviewSelectedOrderActivity.class);
                    intent.putExtra("tableNumber", tableNumber);
                    intent.putExtra("order", orders.get(position));
                    startActivity(intent);
                }

            }
        });

    }
}
