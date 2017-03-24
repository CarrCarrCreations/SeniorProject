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
import java.util.concurrent.ExecutionException;

public class MenuMainActivity extends AppCompatActivity {

    String name;
    String id;

    List<RecipeInfo> recipeInfoArrayList;

    public void search(View view){

    }

    public class RecipeInfo {

        String recipeName;
        String recipeID;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main2);

        setTitle("Menu");

        recipeInfoArrayList = new ArrayList<>();

        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Recipes");
        query.setLimit(10);

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
}
