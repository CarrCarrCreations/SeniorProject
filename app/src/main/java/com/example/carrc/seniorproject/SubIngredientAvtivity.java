package com.example.carrc.seniorproject;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.Future;

public class SubIngredientAvtivity extends AppCompatActivity {

    Future<HttpResponse<JsonNode>> response;

    public void test(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Ingredients");
        query.setLimit(1);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null && objects.size() > 0){

                    for(int i = 0; i < objects.size(); i++){

                        String id = "1033"; //objects.get(i).get("ID").toString();

                        response = Unirest.get("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/food/ingredients/" + id + "/substitutes")
                                .header("X-Mashape-Key", "cfCeth6V86mshu6OGAO9QCgv8vy7p1MHJYZjsnhCMiRIAdAEmm")
                                .header("Accept", "application/json")
                                .asJsonAsync(new Callback<JsonNode>() {
                                    @Override
                                    public void completed(HttpResponse<JsonNode> response) {

                                        String responseString = response.getBody().toString();
                                        Log.i("StringResponse", responseString);

                                        /*
                                        try {
                                            JSONObject jObj = new JSONObject(responseString);
                                            JSONArray ingredientArray = jObj.getJSONArray("substitutes");
                                            int len = ingredientArray.length();

                                            for(int i = 0; i < len; i++){
                                                JSONObject json = ingredientArray.getJSONObject(i);
                                                Log.i("Ingredient", json.toString());
                                            }


                                        } catch (JSONException e1) {
                                            e1.printStackTrace();
                                        }
                                        */

                                    }

                                    @Override
                                    public void failed(UnirestException e) {
                                        Log.i("Failed", e.getMessage());
                                    }

                                    @Override
                                    public void cancelled() {

                                    }
                                });
                    }
                }
            }
        });

    }

    public class downloadIngredients extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {

            test();

            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_ingredient_avtivity);

        setTitle("Sub Ingredient");

        downloadIngredients downloadIngredients = new downloadIngredients();
        downloadIngredients.execute();

    }
}
