package com.example.carrc.seniorproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InterfaceAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class MenuMainActivity extends AppCompatActivity {

    String name;
    String id;

    List<RecipeInfo> recipeInfoArrayList;
    EditText searchEditText;
    CheckBox[] checkBoxes;
    GridLayout filterGridLayout;
    LinearLayout linearLayout;

    CheckBox dairyCheckBox;
    CheckBox eggCheckBox;
    CheckBox glutenCheckBox;
    CheckBox peanutCheckBox;
    CheckBox sesameCheckBox;
    CheckBox seafoodCheckBox;
    CheckBox shellfishCheckBox;
    CheckBox soyCheckBox;
    CheckBox wheatCheckBox;

    public class RecipeInfo {

        String recipeName;
        String recipeID;

    }

    public void showFilter(View view){

        if(filterGridLayout.getVisibility() == View.GONE){
            filterGridLayout.setVisibility(View.VISIBLE);
        } else {
            filterGridLayout.setVisibility(View.GONE);
        }
    }

    public List<String> intoleranceFilter(){

        List<String> intoleranceQueries = new ArrayList<>();

        // loop through all the checkboxes
        for(int i = 0; i < checkBoxes.length; i++){
            // if the current checkbox is selected
            if(checkBoxes[i].isChecked()){
                // get the intolerance name from the checkbox and lowercase it
                String checkboxText = checkBoxes[i].getText().toString().toLowerCase();
                // add the Free to the text so it matches the database entry name
                String formattedText = checkboxText + "Free";
                // add to intolerance list
                intoleranceQueries.add(formattedText);
            }
        }
        return intoleranceQueries;
    }

    public String searchString(){

        String queryContains = searchEditText.getText().toString();
        String output = queryContains.substring(0, 1).toUpperCase() + queryContains.substring(1);
        Log.i("SearchString", output);

        return output;
    }

    public void search(View view){

        // get the intolerances checked
        List<String> intoleranceFilters = intoleranceFilter();

        // query the database
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Recipes");
        ParseQuery<ParseObject> queryMealType = new ParseQuery<ParseObject>("Recipes");


        // if there is a string input into the search bar
        if(!searchEditText.getText().toString().matches("")){
            String queryContains = searchString();
            // query mealType and ItemTitle for the searched word
            // search it with the first letter Upper and lowercase
            query.whereContains("ItemTitle", queryContains);
            queryMealType.whereContains("mealType", queryContains);

            searchEditText.setText("");
        }


        // if there are intolerances
        if(intoleranceFilters.size() > 0) {
            for (int i = 0; i < intoleranceFilters.size(); i++) {
                Log.i("Intolerance",intoleranceFilters.get(i));
                query.whereEqualTo(intoleranceFilters.get(i), "true");
            }
        }

        // create an Array list with query and queryMealType
        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(query);
        queries.add(queryMealType);

        // create a query that contains both query and queryMealType restraints
        ParseQuery<ParseObject> queryMenu = ParseQuery.or(queries);

        linearLayout.removeAllViews();

        queryMenu.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    if(objects.size() > 0){

                        recipeInfoArrayList.clear();

                        for(int i = 0; i < objects.size(); i++){

                            // obtain the url for the recipe image
                            String url = (String) objects.get(i).get("Image");

                            // save the name and ID of the recipe
                            name = (String) objects.get(i).get("ItemTitle");
                            id = (String) objects.get(i).get("FoodID");

                            // create a new RecipeInfo object to save the name and id of the recipe
                            // this object will be used to match the tag of the clicked image in the activity
                            // and send the correct info for the selected image to the MenuItemActivity
                            RecipeInfo recipeInfo = new RecipeInfo();
                            recipeInfo.recipeID = id;
                            recipeInfo.recipeName = name;
                            recipeInfoArrayList.add(recipeInfo);



                            ImageDownloader task = new ImageDownloader();
                            Bitmap bitmap;

                            try {

                                bitmap = task.execute(url).get();

                                // create a text view and set it to the name of the recipe
                                TextView textView = new TextView(getApplicationContext());
                                textView.setTextColor(Color.parseColor("Black"));
                                textView.setPadding(0, 30, 0, 0);
                                textView.setText(name);

                                // create an imageview
                                ImageView imageView = new ImageView(getApplicationContext());

                                // set the parameters for the image view
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(1300,1000);
                                layoutParams.setMargins(0, 20, 0, 45);
                                imageView.setLayoutParams(layoutParams);

                                // set the imageview to the recipe picture and the tag of the image
                                imageView.setImageBitmap(bitmap);
                                imageView.setTag(i);

                                // set what happens when an image is clicked
                                imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        // get the tag number of the clicked image
                                        int tag = (int) v.getTag();

                                        // obtain the name and ID from the recipeObject saved at the selected tag index
                                        String recipeName = recipeInfoArrayList.get(tag).recipeName;
                                        String recipeID = recipeInfoArrayList.get(tag).recipeID;

                                        // create a new intent, save the name and ID and open the menuItentActivity
                                        Intent intent = new Intent(getApplicationContext(), MenuItemActivity.class);
                                        intent.putExtra("id", recipeID);
                                        intent.putExtra("name", recipeName);
                                        startActivity(intent);

                                    }
                                });

                                // add the name of the recipe and recipe picture to the layout
                                linearLayout.addView(textView);
                                linearLayout.addView(imageView);


                            } catch (Exception e1) {

                                e1.printStackTrace();

                            }
                        }
                    }
                }
            }
        });

    }


    public class ImageDownloader extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... urls) {

            try {

                URL url = new URL(urls[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.connect();

                InputStream inputStream = connection.getInputStream();

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                return bitmap;



            } catch (Exception e) {

                e.printStackTrace();
            }

            return null;
        }
    }

    public void displayMenu(){

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Recipes");
        query.setLimit(15);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    if(objects.size() > 0){

                        for(int i = 0; i < objects.size(); i++){

                            // obtain the url for the recipe image
                            String url = (String) objects.get(i).get("Image");

                            // save the name and ID of the recipe
                            name = (String) objects.get(i).get("ItemTitle");
                            id = (String) objects.get(i).get("FoodID");

                            // create a new RecipeInfo object to save the name and id of the recipe
                            // this object will be used to match the tag of the clicked image in the activity
                            // and send the correct info for the selected image to the MenuItemActivity
                            RecipeInfo recipeInfo = new RecipeInfo();
                            recipeInfo.recipeID = id;
                            recipeInfo.recipeName = name;
                            recipeInfoArrayList.add(recipeInfo);



                            ImageDownloader task = new ImageDownloader();
                            Bitmap bitmap;

                            try {

                                bitmap = task.execute(url).get();

                                // create a text view and set it to the name of the recipe
                                TextView textView = new TextView(getApplicationContext());
                                textView.setTextColor(Color.parseColor("Black"));
                                textView.setPadding(0, 30, 0, 0);
                                textView.setText(name);

                                // create an imageview
                                ImageView imageView = new ImageView(getApplicationContext());

                                // set the parameters for the image view
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(1300,1000);
                                layoutParams.setMargins(0, 20, 0, 45);
                                imageView.setLayoutParams(layoutParams);

                                // set the imageview to the recipe picture and the tag of the image
                                imageView.setImageBitmap(bitmap);
                                imageView.setTag(i);

                                // set what happens when an image is clicked
                                imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        // get the tag number of the clicked image
                                        int tag = (int) v.getTag();

                                        // obtain the name and ID from the recipeObject saved at the selected tag index
                                        String recipeName = recipeInfoArrayList.get(tag).recipeName;
                                        String recipeID = recipeInfoArrayList.get(tag).recipeID;

                                        // create a new intent, save the name and ID and open the menuItentActivity
                                        Intent intent = new Intent(getApplicationContext(), MenuItemActivity.class);
                                        intent.putExtra("id", recipeID);
                                        intent.putExtra("name", recipeName);
                                        startActivity(intent);

                                    }
                                });

                                // add the name of the recipe and recipe picture to the layout
                                linearLayout.addView(textView);
                                linearLayout.addView(imageView);


                            } catch (Exception e1) {

                                e1.printStackTrace();

                            }
                        }
                    }
                }
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main2);

        setTitle("Menu");

        dairyCheckBox = (CheckBox) findViewById(R.id.dairyCheckBox);
        eggCheckBox = (CheckBox) findViewById(R.id.eggCheckBox);
        glutenCheckBox = (CheckBox) findViewById(R.id.glutenCheckBox);
        peanutCheckBox = (CheckBox) findViewById(R.id.peanutCheckBox);
        sesameCheckBox = (CheckBox) findViewById(R.id.sesameCheckBox);
        seafoodCheckBox = (CheckBox) findViewById(R.id.seafoodCheckBox);
        shellfishCheckBox = (CheckBox) findViewById(R.id.shellfishCheckBox);
        soyCheckBox = (CheckBox) findViewById(R.id.soyCheckBox);
        wheatCheckBox = (CheckBox) findViewById(R.id.wheatCheckBox);

        searchEditText = (EditText) findViewById(R.id.searchEditText);

        checkBoxes = new CheckBox[]{dairyCheckBox, eggCheckBox, glutenCheckBox, peanutCheckBox,
                sesameCheckBox, seafoodCheckBox, shellfishCheckBox, soyCheckBox, wheatCheckBox};

        filterGridLayout = (GridLayout) findViewById(R.id.filterGridLayout);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        recipeInfoArrayList = new ArrayList<>();


        displayMenu();
    }
}
