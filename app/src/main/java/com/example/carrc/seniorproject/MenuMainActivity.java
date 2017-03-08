package com.example.carrc.seniorproject;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuMainActivity extends AppCompatActivity {

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            TextView outputTextView = (TextView) findViewById(R.id.outputTetVIew);

            try {
                HttpResponse<JsonNode> resp = Unirest.get("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/food/ingredients/autocomplete?metaInformation=false&number=10&query=appl")
                        .header("X-Mashape-Key", "cfCeth6V86mshu6OGAO9QCgv8vy7p1MHJYZjsnhCMiRIAdAEmm")
                        .header("Accept", "application/json")
                        .asJson();

                String respnseString = resp.getBody().toString();

                JSONArray jsonArray = new JSONArray(respnseString);

                for(int i = 0; i < jsonArray.length(); i++){

                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    Log.i("name", jsonObject.getString("name"));
                }





            } catch (UnirestException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;

        }

    }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_menu_main);

            setTitle("Menu");

            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute();


        }
    }
