package com.example.carrc.seniorproject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scottieb on 4/5/17.
 */

public class DeleteRecipeActivity extends AppCompatActivity {

    String FoodID = "";
    ArrayList<String> recipeNames = new ArrayList<String>();

    public void deleteRecipe(View view) throws ParseException {

        //EditText deleteRecipeText = (EditText) findViewById(R.id.editText);
        SearchView deleteRecipeSearchView = (SearchView) findViewById(R.id.searchView);

        ParseQuery<ParseObject> query3 = new ParseQuery<ParseObject>("Recipes");
        if(query3.whereEqualTo("ItemTitle", deleteRecipeSearchView.getQuery().toString()).count() == 1) {
            //get recipe in search view
            String deleteRecipeName = deleteRecipeSearchView.getQuery().toString();

            ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Recipes");
            query2.whereEqualTo("ItemTitle", deleteRecipeName);
            ParseObject recipe;
            try {
                recipe = query2.getFirst();
                //query.whereNotContainedIn("username", person.getList("friends"));
                //query.whereNotContainedIn("email", n);
                //query.setLimit(15);
                //recipe.put("IngredientID0", ingredient.get("ID").toString());
                FoodID = recipe.get("FoodID").toString();
            } catch (ParseException e1) {
                e1.printStackTrace();
            }

            //ask for confirmation

            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Deleting Recipe")
                    .setMessage("Are you sure you want to delete this recipe?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // delete from database
                            finishDelete();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();

            //delete from database
        } else {
            Toast.makeText(this, "Recipe does not exist in the database!", Toast.LENGTH_SHORT).show();
        }
    }

    public void finishDelete(){
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Recipes");
        query2.whereEqualTo("FoodID", FoodID);
        ParseObject recipe;
        try {
            recipe = query2.getFirst();
            //query.whereNotContainedIn("username", person.getList("friends"));
            //query.whereNotContainedIn("email", n);
            //query.setLimit(15);
            //recipe.put("IngredientID0", ingredient.get("ID").toString());
            recipe.delete();
            Toast.makeText(this, "Recipe deleted from the database!", Toast.LENGTH_SHORT).show();
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_recipe);


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipes");
        try {
            List<ParseObject> recipeList = query.find();

            for(ParseObject recipe: recipeList){
                recipeNames.add(recipe.get("ItemTitle").toString());
            }
        } catch (ParseException e){
            System.out.println(e.getMessage());
        }


        final ListView listIngred = (ListView) findViewById(R.id.listView);


        //int height = (int) (50 * scale + 0.5f);
        final SearchView searchViewIngred = (SearchView) findViewById(R.id.searchView);

        final ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recipeNames);
        listIngred.setAdapter(adapter);

        listIngred.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Object listItem = listIngred.getItemAtPosition(position);
                String str = (String) listItem; //As you are using Default String Adapter

                searchViewIngred.setQuery( (CharSequence) str, false);
                //searchViewIngred.setIconified(false);

                listIngred.setVisibility(View.GONE);
            }
        });


        searchViewIngred.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                // searchViewIngred.getId

                // I HAVE IDD
                // whatever ID i give it, use that as the i, the i will be same as curSearchViewID
                //ingredients.set(  (curSearchViewId  - 1)   , ingredients.get( (curSearchViewId -1)).setText(newText) );
                listIngred.setVisibility(View.VISIBLE);
                listIngred.setAlpha(1);
                adapter.getFilter().filter(newText);

                return false;
            }
        });
    }
}
