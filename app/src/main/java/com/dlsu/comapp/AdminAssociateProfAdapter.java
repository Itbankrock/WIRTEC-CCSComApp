package com.dlsu.comapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdminAssociateProfAdapter extends ArrayAdapter<Professor> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private ArrayList<Professor> proflist = new ArrayList<>();
    private LayoutInflater inflater;

    public AdminAssociateProfAdapter(Context context, int textViewResourceId,
                                     ArrayList<Professor> proflist) {
        super(context, textViewResourceId, proflist);
        this.context = context;
        this.proflist = proflist;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount(){
        return proflist.size();
    }

    @Override
    public Professor getItem(int position){
        return proflist.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.support_simple_spinner_dropdown_item, null);
        // get view
        TextView tvText1 = (TextView) convertView.findViewById(android.R.id.text1);
        // set content
        tvText1.setText(proflist.get(position).getName());
        tvText1.setTextColor(Color.BLACK);
        // return
        return convertView;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        convertView = inflater.inflate(R.layout.spinner_textview, null);
        TextView names = (TextView) convertView.findViewById(R.id.spinner_assoc_textview);
        names.setText(proflist.get(position).getName());
        return convertView;
    }
}