package com.dlsu.comapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.MyViewHolder> {

    protected List<Course> courseList;
    protected HomeActivity homeContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView code, desc, units, overview;

        public MyViewHolder(View view) {
            super(view);
            code = view.findViewById(R.id.rv_code);
            desc = view.findViewById(R.id.rv_course_desc);
            units = view.findViewById(R.id.rv_units);
            overview = view.findViewById(R.id.rv_course_overview);
        }
    }


    public CourseAdapter(List<Course> courseList, HomeActivity homeContext) {
        this.courseList = courseList;
        this.homeContext = homeContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Course course = courseList.get(position);
        holder.code.setText(course.getCode());
        holder.desc.setText(course.getDesc());
        holder.units.setText(course.getUnits());
        holder.overview.setText(course.getOverview());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeContext.viewCourse(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }
}
