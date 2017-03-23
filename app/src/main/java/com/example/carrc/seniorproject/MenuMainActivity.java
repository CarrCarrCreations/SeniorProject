package com.example.carrc.seniorproject;

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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MenuMainActivity extends AppCompatActivity {

    public void search(View view){
        
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

        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Recipes");
        query.setLimit(10);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){

                    Log.i("ObjectsSize", String.valueOf(objects.size()));
                    if(objects.size() > 0){

                        for(ParseObject object : objects){

                            String url = (String) object.get("Image");
                            String name = (String) object.get("ItemTitle");

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

                                // set the imageview to the recipe picture
                                imageView.setImageBitmap(bitmap);

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
