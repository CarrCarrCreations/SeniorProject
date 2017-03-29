package com.example.carrc.seniorproject;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

class CustomList extends ArrayAdapter<String>{

    private final Activity context;
    private final List<String> recipeName;

    public CustomList(Activity context,
                      List<String> recipeName) {
        super(context, R.layout.list_single, recipeName);
        this.context = context;
        this.recipeName = recipeName;
    }


    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single, null, true);

        TextView name = (TextView) rowView.findViewById(R.id.txt0);
        TextView price = (TextView) rowView.findViewById(R.id.txt1);

        name.setText(recipeName.get(position));
        price.setText("5.00");


        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Cart");
                query.whereEqualTo("Username", ParseUser.getCurrentUser().getUsername());
                query.whereEqualTo("RecipeName", recipeName.get(position));
                query.setLimit(1);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(e == null && objects.size() > 0){
                            objects.get(0).deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e == null){
                                        Toast.makeText(getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            recipeName.remove(position);
                            notifyDataSetChanged();
                        }
                    }
                });
            }
        });

        imageView.setImageResource(R.drawable.deleteicon);


        return rowView;
    }
}



public class CartActivity extends AppCompatActivity {

    ListView cartListView;
    CustomList adapter;

    public List<String> getRecipeNames() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Cart");
        query.whereEqualTo("Username", ParseUser.getCurrentUser().getUsername());

        List<String> names = new ArrayList<>();
        try {
            List<ParseObject> objects = query.find();

            if(objects.size() > 0){
                for(int i = 0; i < objects.size(); i++){
                    String name = objects.get(i).get("RecipeName").toString();
                    Log.i("StringName", name);
                    names.add(name);
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return names;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        setTitle("Cart");

        List<String> recipeNames = getRecipeNames();

        adapter = new CustomList(CartActivity.this, recipeNames);
        cartListView = (ListView) findViewById(R.id.cartListView);
        cartListView.setAdapter(adapter);
    }
}
