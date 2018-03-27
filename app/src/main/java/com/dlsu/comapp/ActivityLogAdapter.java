package com.dlsu.comapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Enrico Zabayle on 08/03/2018.
 */

public class ActivityLogAdapter extends RecyclerView.Adapter<ActivityLogAdapter.MyViewHolder>{

    protected List<ActivityLog> activityList;
    protected HomeActivity homeContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView activityLog;

        public MyViewHolder(View view) {
            super(view);
            activityLog = view.findViewById(R.id.rv_activity_log);
        }
    }


    public ActivityLogAdapter(List<ActivityLog> activityList, HomeActivity homeContext) {
        this.activityList = activityList;
        this.homeContext = homeContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_log_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ActivityLog al = activityList.get(position);
        holder.activityLog.setText(al.getActivity());
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }
}
