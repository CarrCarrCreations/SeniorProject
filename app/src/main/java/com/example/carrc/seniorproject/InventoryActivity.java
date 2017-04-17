package com.example.carrc.seniorproject;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

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
    ArrayList<EditText> ingredients = new ArrayList<EditText>();
    ArrayList<String> ingredientNameList = new ArrayList<String>();
    ArrayList<String> ingredientUnitList = new ArrayList<String>();
    ArrayList<String> ingredientQuantityList = new ArrayList<String>();

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
            final EditText editText = new EditText(this);
            editText.setText(ingredientNameList.get(i));
            final EditText editText2 = new EditText(this);
            editText2.setText(ingredientUnitList.get(i));
            final EditText editText3 = new EditText(this);
            editText3.setText(ingredientQuantityList.get(i));



            prevTextViewId = curTextViewId;

            row.addView(editText);
            ingredients.add(editText);
            row.addView(editText2);
            row.addView(editText3);
            ingredients.add(editText2);
            ingredients.add(editText3);



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
            query.setLimit(300);
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
