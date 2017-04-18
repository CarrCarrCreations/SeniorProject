package com.example.carrc.seniorproject;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scottieb on 4/8/17.
 */

public class InventoryActivity extends AppCompatActivity {

    int prevTextViewId = 0;
    ArrayList<TextView> ingredientsName = new ArrayList<TextView>();
    ArrayList<TextView> ingredientsUnit = new ArrayList<TextView>();
    ArrayList<EditText> ingredientsQuantity = new ArrayList<EditText>();
    ArrayList<String> ingredientNameList = new ArrayList<String>();
    ArrayList<String> ingredientUnitList = new ArrayList<String>();
    ArrayList<String> ingredientQuantityList = new ArrayList<String>();

    public void updateInventory(View view) {
        // get all quantities currently

        // go through original list,
        //ingredientQuantityList.size()
        for(int i=0; i<ingredientQuantityList.size(); i++) {

            if(!ingredientQuantityList.get(i).equals(ingredientsQuantity.get(i).getText().toString())){
                // if different, update
                    // grab i-th item in Name list, and update according.

                String name = ingredientNameList.get(i);
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Ingredients");
                query.whereEqualTo("Name", name);
                try{
                    ParseObject ingredient = query.getFirst();
                    ingredient.put("Quantity", ingredientsQuantity.get(i).getText().toString());


                    ingredient.saveInBackground();
                } catch (ParseException e) {

                }
            } else {

            }
        }






    }

    public void displayInventory(){
        //System.out.println(ingredientNameList.size());\

        // put initial one
//        LinearLayout a = new LinearLayout(this);
//        a.setOrientation(LinearLayout.HORIZONTAL);
//
//        int curTextViewId = prevTextViewId + 1;
//        final EditText editTextFirst = new EditText(this);
//        final EditText editTextSecond = new EditText(this);
//        editTextFirst.setText("first");
//        editTextSecond.setText("firstUnit");
//
        for(int i = 0; i<ingredientNameList.size(); i++)
        {
                //Log.i("Info", "Here");

            RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);

            TableLayout tl = (TableLayout) findViewById(R.id.tableLayout);
            TableRow row= new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);

            int curTextViewId = prevTextViewId + 1;
            final TextView textView = new TextView(this);
            textView.setText(ingredientNameList.get(i));
            textView.setGravity(Gravity.CENTER | Gravity.BOTTOM);
            final TextView textView2 = new TextView(this);
            textView2.setText(ingredientUnitList.get(i));
            textView2.setGravity(Gravity.CENTER | Gravity.BOTTOM);

            final EditText editText3 = new EditText(this);
            editText3.setText(ingredientQuantityList.get(i));
            editText3.setGravity(Gravity.CENTER | Gravity.BOTTOM);




            prevTextViewId = curTextViewId;

            //TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT,Gravity.LEFT | Gravity.CENTER_VERTICAL);

            row.addView(textView);
            ingredientsName.add(textView);
            row.addView(textView2);
            row.addView(editText3);
            ingredientsUnit.add(textView2);
            ingredientsQuantity.add(editText3);



            tl.addView(row);


        }
    }

    public void addItem(View view){

    }

    public void createInventory()  {
        //grab Ingredients table and display them.
        final Context _this = this;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Ingredients");
        try {
            query.setLimit(20);
            List<ParseObject> ingredientsList = query.find();

            System.out.println("Size of query find" + ingredientsList.size());
            for(ParseObject ob: ingredientsList){

                if(ob.get("Name")!= null)
                  ingredientNameList.add(ob.get("Name").toString());
                else
                    ingredientNameList.add("");
                if(ob.get("Unit")!= null)
                    ingredientUnitList.add(ob.get("Unit").toString());
                else
                    ingredientUnitList.add("");
                if(ob.get("Quantity")!= null)
                 ingredientQuantityList.add(ob.get("Quantity").toString());
                else
                    ingredientQuantityList.add("");
            }
        } catch (ParseException e){
            System.out.println(e.getMessage());
        }


    }

//    @Override
//    protected void onStart()
//    {
//        super.onStart();
//        displayInventory();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        createInventory();
        displayInventory();
    }
}
