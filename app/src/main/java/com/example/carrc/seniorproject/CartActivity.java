package com.example.carrc.seniorproject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.LoginFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import com.parse.SaveCallback;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

class CustomList extends ArrayAdapter<String>{

    private final Activity context;
    private final List<String> recipeName;
    private final List<String> recipePrice;

    public CustomList(Activity context,
                      List<String> recipeName,
                      List<String> recipePrice) {
        super(context, R.layout.list_single, recipeName);
        this.context = context;
        this.recipeName = recipeName;
        this.recipePrice = recipePrice;
    }


    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single, null, true);

        TextView name = (TextView) rowView.findViewById(R.id.txt0);
        TextView price = (TextView) rowView.findViewById(R.id.txt1);

        name.setText(recipeName.get(position));
        price.setText("$" + recipePrice.get(position));


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
                            context.recreate();
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

    TextView subValueTextView;
    TextView taxValueTextView;
    TextView totalValueTextView;

    List<String> recipeNames;
    List<String> recipePrices;

    String ingredientUnit = "";
    int ingredientNum;

    NumberFormat nf;

    class Inventory{
        String recipeName;
        List<Double> ingredientQuantities = new ArrayList<>();
        List<String> ingredientNames = new ArrayList<>();
        ParseQuery<ParseObject> query;
    }

    List<Inventory> ingredients;

    public void buildInventory(){

        for(int i = 0; i < ingredients.size(); i++){
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipes");
            query.whereEqualTo("ItemTitle", ingredients.get(i).recipeName);

            try {
                List<ParseObject> objects = query.find();
                if(objects.size() > 0){
                    ParseObject recipe = objects.get(0);

                    ingredientNum = 0;

                    do{
                        if(recipe.get("IngredientAmount" + ingredientNum) == null){
                            break;
                        }

                        String ingredientName = recipe.get("IngredientName" + ingredientNum).toString();
                        ingredientUnit = recipe.get("IngredientAmount" + ingredientNum).toString();
                        double amount = Double.parseDouble(ingredientUnit);

                        ingredients.get(i).ingredientQuantities.add(amount);
                        ingredients.get(i).ingredientNames.add(ingredientName);

                        ingredientNum++;

                    } while (ingredientUnit != null);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        for(int i = 0; i < ingredients.size(); i++){
            Log.i("Amount", ingredients.get(i).ingredientQuantities.toString());
            Log.i("Amount", ingredients.get(i).ingredientNames.toString());
        }
    }

    public void inventoryMath(){

        for(int i = 0; i < ingredients.size(); i++){

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Ingredients");
            query.whereEqualTo("Name", ingredients.get(i).ingredientNames.get(i));

            try {
                List<ParseObject> objects = query.find();
                if(objects.size() > 0){

                    ParseObject ingredient = objects.get(0);

                    double quantity = Double.parseDouble(ingredient.get("Quantity").toString());
                    String newQuantity = String.valueOf(quantity - ingredients.get(i).ingredientQuantities.get(i));

                    ingredient.put("Quantity", newQuantity);
                    Log.i("NewQauntity", String.valueOf(newQuantity));
                    ingredient.save();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


    public class math extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            for(int i = 0; i < ingredients.size(); i++){
                for(int j = 0; j < ingredients.get(i).ingredientNames.size(); j++){

                    String name = ingredients.get(i).ingredientNames.get(j);

                    if(params[0].matches(name)){
                        ingredients.get(i).query = ParseQuery.getQuery("Ingredients");
                        ingredients.get(i).query.whereEqualTo("Name", ingredients.get(i).ingredientNames.get(j));

                        try {
                            List<ParseObject> objects =  ingredients.get(i).query.find();
                            if(objects.size() > 0){

                                ParseObject ingredient = objects.get(0);

                                double quantity = Double.parseDouble(ingredient.get("Quantity").toString());
                                String newQuantity = String.valueOf(quantity - ingredients.get(i).ingredientQuantities.get(i));

                                ingredient.put("Quantity", newQuantity);
                                Log.i("NewQauntity", String.valueOf(newQuantity));
                                ingredient.save();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }

                }
            }

            return null;
        }
    }


    public void checkOut(View view){

        buildInventory();

        for(int i = 0; i < ingredients.size(); i++){
            for(int j = 0; j < ingredients.get(i).ingredientNames.size(); j++){

                math task = new math();
                task.execute(ingredients.get(i).ingredientNames.get(j));
            }
        }
    }

    public List<String> getRecipeNames() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Cart");
        query.whereEqualTo("Username", ParseUser.getCurrentUser().getUsername());

        List<String> names = new ArrayList<>();
        try {
            List<ParseObject> objects = query.find();

            if(objects.size() > 0){

                ingredients.clear();

                for(int i = 0; i < objects.size(); i++){
                    String name = objects.get(i).get("RecipeName").toString();
                    names.add(name);

                    Inventory ingredientName = new Inventory();
                    ingredientName.recipeName = name;
                    ingredients.add(ingredientName);

                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return names;
    }

    public List<String> getRecipePrice(){

        List<String> prices = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Cart");
        query.whereEqualTo("Username", ParseUser.getCurrentUser().getUsername());

        try {
            List<ParseObject> objects = query.find();

            if(objects.size() > 0){
                for(int i = 0; i < objects.size(); i++){
                    String name = objects.get(i).get("Price").toString();
                    prices.add(name);
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return prices;
    }

    public String getSubTotal(){

        String originalFormat;
        String total;
        float price = 0;

        for(int i = 0; i < recipePrices.size(); i++){
            originalFormat = (recipePrices.get(i));
            price += Float.parseFloat(originalFormat);
        }

        total = nf.format(price);
        subValueTextView.setText("$" + total);

        return total;
    }

    public String getTax(double percentDecimal){
        float taxCalc;
        String tax;

        taxCalc = Float.parseFloat(getSubTotal());
        taxCalc *= percentDecimal;

        tax = nf.format(taxCalc);

        taxValueTextView.setText("$" + tax);

        return tax;
    }

    public String getTotal(double percentDecimal){
        double subTotal = Double.parseDouble(getSubTotal());
        double tax = Double.parseDouble(getTax(percentDecimal));

        double total = subTotal + tax;

        String finalTotal = nf.format(total);

        totalValueTextView.setText("$" + finalTotal);

        return finalTotal;
    }

    public void recreate(){
        startActivity(getIntent());
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        setTitle("Cart");

        subValueTextView = (TextView) findViewById(R.id.subValueTextView);
        taxValueTextView = (TextView) findViewById(R.id.taxValueTextView);
        totalValueTextView = (TextView) findViewById(R.id.totalValueTextView);

        ingredients = new ArrayList<>();

        nf = new DecimalFormat("##.##");

        recipeNames = getRecipeNames();
        recipePrices = getRecipePrice();
        getTotal(.07);

        adapter = new CustomList(CartActivity.this, recipeNames, recipePrices);
        cartListView = (ListView) findViewById(R.id.cartListView);
        cartListView.setAdapter(adapter);
    }
}
