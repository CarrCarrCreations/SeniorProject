package com.example.carrc.seniorproject;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class MenuActivity extends AppCompatActivity {

    String[] recipeIDs;
    ArrayList<ParseObject> recipesList;

    String mealType;
    String courseType;
    String dietType;
    String intolerances;

    Future<HttpResponse<JsonNode>> response;


    public void getRecipes(){

        // type of food you are looking for
        mealType = "breakfast";

        // main+course, side dish, dessert, appetizer, salad, bread, breakfast, soup, beverage, sauce, or drink.
        courseType = "breakfast";
        //  pescetarian, lacto vegetarian, ovo vegetarian, vegan, and vegetarian.
        dietType = "diet=vegetarian&";

        // example intolerances=diary%2Cegg%2Cwheat& seperate each intolerance with %2C and end with &
        // dairy, egg, gluten, peanut, sesame, seafood, shellfish, soy, sulfite, tree nut, and wheat.
        intolerances = "intolerances=dairy&";



        try {
            HttpResponse<JsonNode> response = Unirest.get("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/search?" + dietType + "instructionsRequired=false&" + intolerances + "limitLicense=false&number=10&offset=0&query=" + mealType + "&type=" + courseType)
                    .header("X-Mashape-Key", "cfCeth6V86mshu6OGAO9QCgv8vy7p1MHJYZjsnhCMiRIAdAEmm")
                    .header("Accept", "application/json")
                    .asJson();


            String responseString = response.getBody().toString();
            JSONObject jObj = new JSONObject(responseString);
            JSONArray jsonArray = jObj.getJSONArray("results");
            recipeIDs = new String[jsonArray.length()];
            recipesList = new ArrayList<>();
            Log.i("RecipeLength", String.valueOf(recipeIDs.length));

            for(int i = 0; i < jsonArray.length(); i++){

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                recipeIDs[i] = jsonObject.getString("id");

            }

        } catch (UnirestException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void test() {

        getRecipes();

        for (int i = 0; i < recipeIDs.length; i++) {

            System.out.println("recipeID: " + recipeIDs[i]);
            System.out.println("waiting .5 second...");
            SystemClock.sleep(500);

            response = Unirest.get("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/" + recipeIDs[i] + "/information?includeNutrition=false")
                    .header("X-Mashape-Key", "cfCeth6V86mshu6OGAO9QCgv8vy7p1MHJYZjsnhCMiRIAdAEmm")
                    .header("Accept", "application/json")
                    .asJsonAsync(new Callback<JsonNode>() {
                        @Override
                        public void completed(HttpResponse<JsonNode> response) {

                            ParseObject recipe = new ParseObject("Recipes");

                            String responseString = response.getBody().toString();
                            JSONObject jObj = null;


                            try {
                                jObj = new JSONObject(responseString);

                                String formatCourseType = courseType.replace("+", "");

                                recipe.put("mealType", mealType);
                                recipe.put("course", formatCourseType);
                                recipe.put("ItemTitle", jObj.getString("title"));
                                recipe.put("FoodID", jObj.getString("id"));
                                recipe.put("Image", jObj.getString("image"));
                                recipe.put("PricePerServing", jObj.getString("pricePerServing"));
                                recipe.put("vegetarian", jObj.getString("vegetarian"));
                                recipe.put("vegan", jObj.getString("vegan"));
                                recipe.put("glutenFree", jObj.getString("glutenFree"));
                                recipe.put("dairyFree", jObj.getString("dairyFree"));
                                recipe.put("veryHealthy", jObj.getString("veryHealthy"));
                                recipe.put("cheap", jObj.getString("cheap"));
                                recipe.put("veryPopular", jObj.getString("veryPopular"));
                                recipe.put("weightWatcher", jObj.getString("weightWatcherSmartPoints"));

                                JSONArray ingredientArray = jObj.getJSONArray("extendedIngredients");
                                int len = ingredientArray.length();

                                for(int j=0; j<len; j++)
                                {
                                    JSONObject json = ingredientArray.getJSONObject(j);
                                    recipe.put("IngredientName" + j, json.getString("name"));
                                    recipe.put("IngredientID" + j, json.getString("id"));
                                    recipe.put("IngredientAmount" + j, json.getString("amount"));
                                    recipe.put("IngredientUnit" + j, json.getString("unit"));

                                }

                                recipesList.add(recipe);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void failed(UnirestException e) {

                            System.out.println("API call failed... " + e.getMessage());

                        }

                        @Override
                        public void cancelled() {

                        }
                    });


        }

        for(int i = 0; i < recipesList.size(); i++){

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipes");

            System.out.println("Saving...");
            downloadTask2 task = new downloadTask2();
            task.execute(recipesList.get(i));

        }
    }


    public class downloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {

            test();

            return null;
        }
    }

    public class downloadTask2 extends AsyncTask<ParseObject, Void, ParseObject>{

        @Override
        protected ParseObject doInBackground(ParseObject... params) {

            params[0].saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null){
                        System.out.println("Saved Recipe " + "Success");
                    } else {
                        System.out.println("Saved Recipe Error: " + e.getMessage());
                    }
                }
            });



            return null;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        downloadTask task = new downloadTask();
        task.execute();

    }
}
