package com.dlsu.comapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.MyViewHolder> implements Filterable {

    protected List<Course> courseList;
    protected HomeActivity homeContext;
    protected List<Course> FilteredCourseList;

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
        this.FilteredCourseList = courseList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Course course = FilteredCourseList.get(position);
        holder.code.setText(course.getId());
        holder.desc.setText(course.getDesc());
        holder.units.setText(course.getUnits());
        holder.overview.setText(course.getOverview());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeContext.viewCourse(course);
            }
        });
    }

    @Override
    public int getItemCount() {
        return FilteredCourseList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    FilteredCourseList = courseList;
                } else {
                    List<Course> filteredList = new ArrayList<>();
                    for (Course row : courseList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getId().toLowerCase().contains(charString.toLowerCase())  ) {
                            filteredList.add(row);
                        }
                    }

                    FilteredCourseList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = FilteredCourseList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                FilteredCourseList = (ArrayList<Course>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
