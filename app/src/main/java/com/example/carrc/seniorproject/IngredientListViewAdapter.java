package com.example.carrc.seniorproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class IngredientListViewAdapter extends BaseAdapter {

    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private List<IngredientObject> recipelNamesList = null;
    private ArrayList<IngredientObject> arraylist;

    public IngredientListViewAdapter(Context context, List<IngredientObject> recipeNameList) {
        mContext = context;
        this.recipelNamesList = recipeNameList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<IngredientObject>();
        this.arraylist.addAll(recipeNameList);
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return recipelNamesList.size();
    }

    @Override
    public IngredientObject getItem(int position) {
        return recipelNamesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.ingredient_listview_item, null);
            // Locate the TextViews in ingredient_listview_item.xmlew_item.xml
            holder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(recipelNamesList.get(position).getIngredientNameName());
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        recipelNamesList.clear();
        if (charText.length() == 0) {
            recipelNamesList.addAll(arraylist);
        } else {
            for (IngredientObject wp : arraylist) {
                if (wp.getIngredientNameName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    recipelNamesList.add(wp);
                }
            }
            IngredientObject createNew = new IngredientObject("Create New Ingredient");
            recipelNamesList.add(createNew);
        }
        notifyDataSetChanged();
    }

}