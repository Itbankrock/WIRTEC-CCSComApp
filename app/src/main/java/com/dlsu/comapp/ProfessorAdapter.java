package com.dlsu.comapp;

/**
 * Created by Enrico Zabayle on 08/03/2018.
 */

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class ProfessorAdapter extends RecyclerView.Adapter<ProfessorAdapter.MyViewHolder> {

    protected List<Professor> profList;
    protected HomeActivity homeContext;
    protected boolean defaultConstructor = true;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView profName, profEmail, profRating;
        public RelativeLayout rlProf;

        @RequiresApi(api = Build.VERSION_CODES.M)
        public MyViewHolder(View view) {
            super(view);
            profName = view.findViewById(R.id.rv_prof_name);
            profEmail = view.findViewById(R.id.rv_prof_email);
            profRating = view.findViewById(R.id.rv_prof_rating);
            rlProf = view.findViewById(R.id.rl_prof);
        }
    }


    public ProfessorAdapter(List<Professor> profList, HomeActivity homeContext) {
        this.profList = profList;
        this.homeContext = homeContext;
    }

    public ProfessorAdapter(List<Professor> profList, HomeActivity homeContext, boolean defaultConstructor) {
        this.profList = profList;
        this.homeContext = homeContext;
        this.defaultConstructor = defaultConstructor;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.prof_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Professor prof = profList.get(position);
        holder.profName.setText(prof.getName());
        holder.profEmail.setText(prof.getEmail());
        holder.profRating.setText(prof.getRating());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (defaultConstructor)
                    homeContext.viewProfessor( position,prof.getId() );
            }
        });
    }

    @Override
    public int getItemCount() {
        return profList.size();
    }
}
