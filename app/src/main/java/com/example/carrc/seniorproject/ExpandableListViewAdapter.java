package com.example.carrc.seniorproject;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by carrc on 3/28/2017.
 */


public class ExpandableListViewAdapter extends BaseExpandableListAdapter {


    class recipe {
        String name;
        List<String> ingredients;
    }

    List<recipe> groupNames;

    String ingredientName;

    int ingredientNum;

    Context context;

    public ExpandableListViewAdapter(Context context){

        this.context = context;
        getRecipes();
    }

    public void getRecipes(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Cart");
        query.whereEqualTo("Username", ParseUser.getCurrentUser().getUsername());

        try {

            List<ParseObject> objects = query.find();
            int objectSize = objects.size();

            groupNames = new ArrayList<>();

            for(int i = 0; i < objectSize; i++){

                recipe recipeInfo = new recipe();
                recipeInfo.name = objects.get(i).get("RecipeName").toString();
                recipeInfo.ingredients = new ArrayList<>();

                do{
                    ingredientName = objects.get(i).get("IngredientName" + i).toString();
                    // objects.get(i).get("IngredientName" + i)
                    if(ingredientName == "null"){
                        break;
                    }

                    recipeInfo.ingredients.add(objects.get(i).get("IngredientName" + i).toString());
                    ingredientNum++;


                } while(ingredientName != "null");

                groupNames.add(recipeInfo);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }



    @Override
    public int getGroupCount() {
        return groupNames.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groupNames.get(groupPosition).ingredients.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupNames.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groupNames.get(groupPosition).ingredients.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        TextView textView = new TextView(context);
        textView.setText(groupNames.get(groupPosition).name);
        textView.setPadding(100, 0, 0, 0);
        textView.setTextSize(25);

        return textView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final TextView textView = new TextView(context);
        textView.setText(groupNames.get(groupPosition).ingredients.get(childPosition));
        textView.setPadding(100, 0, 0, 0);
        textView.setTextSize(15);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, textView.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        return  textView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
