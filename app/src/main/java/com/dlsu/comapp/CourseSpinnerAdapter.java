package com.dlsu.comapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CourseSpinnerAdapter extends ArrayAdapter<Course> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private ArrayList<Course> courselist = new ArrayList<>();
    private LayoutInflater inflater;

    public CourseSpinnerAdapter(Context context, int textViewResourceId,
                                ArrayList<Course> courselist) {
        super(context, textViewResourceId, courselist);
        this.context = context;
        this.courselist = courselist;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount(){
        return courselist.size();
    }

    @Override
    public Course getItem(int position){
        return courselist.get(position);
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
        tvText1.setText(courselist.get(position).getId());
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
        names.setText(courselist.get(position).getId());
        if(position == 0){
            names.setTextColor(Color.GRAY);
        }
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        if (position == 0 )
            return false;
        return true;
    }
}