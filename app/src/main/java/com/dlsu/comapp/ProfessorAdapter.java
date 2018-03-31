package com.dlsu.comapp;

/**
 * Created by Enrico Zabayle on 08/03/2018.
 */

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Filterable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProfessorAdapter extends RecyclerView.Adapter<ProfessorAdapter.MyViewHolder> implements Filterable {

    protected List<Professor> profList;
    protected List<Professor> FilteredProflist;
    protected HomeActivity homeContext;
    protected boolean defaultConstructor = true;
    protected Context mainContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView profName, profEmail, profRating;
        public RelativeLayout rlProf;
        public ImageView profpic;

        @RequiresApi(api = Build.VERSION_CODES.M)
        public MyViewHolder(View view) {
            super(view);
            profName = view.findViewById(R.id.rv_prof_name);
            profEmail = view.findViewById(R.id.rv_prof_email);
            profRating = view.findViewById(R.id.rv_prof_rating);
            rlProf = view.findViewById(R.id.rl_prof);
            profpic = view.findViewById(R.id.imageView7);
        }
    }


    public ProfessorAdapter(List<Professor> profList, HomeActivity homeContext) {
        this.profList = profList;
        this.homeContext = homeContext;
        this.FilteredProflist = profList;
    }

    public ProfessorAdapter(List<Professor> profList, HomeActivity homeContext, boolean defaultConstructor) {
        this.profList = profList;
        this.homeContext = homeContext;
        this.defaultConstructor = defaultConstructor;
        this.FilteredProflist = profList;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.prof_item_layout, parent, false);

        mainContext = parent.getContext();

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Professor prof = FilteredProflist.get(position);
        holder.profName.setText(prof.getName());
        holder.profEmail.setText(prof.getEmail());
        holder.profRating.setText(prof.getRating());

        Picasso.with( mainContext ).load( prof.getPhotourl() ).into(holder.profpic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    homeContext.viewProfessor( prof );
            }
        });
    }

    @Override
    public int getItemCount() {
        return FilteredProflist.size();
    }

    public void addItem(Professor object){
        profList.add(object);
        notifyDataSetChanged();
    }
    public void clearItems(){
        profList.clear();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    FilteredProflist = profList;
                } else {
                    List<Professor> filteredList = new ArrayList<>();
                    for (Professor row : profList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getPosition().toLowerCase().contains(charString.toLowerCase()) || row.getDepartment().toLowerCase().contains(charString.toLowerCase()) ) {
                            filteredList.add(row);
                        }
                    }

                    FilteredProflist = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = FilteredProflist;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                FilteredProflist = (ArrayList<Professor>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
