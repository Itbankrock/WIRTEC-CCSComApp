package com.dlsu.comapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Enrico Zabayle on 11/03/2018.
 */

public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.MyViewHolder> {

    protected List<Help> helpList;
    protected HomeActivity homeContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView helpHeader, helpContent;

        public MyViewHolder(View view) {
            super(view);
            helpHeader = view.findViewById(R.id.rv_help_header);
            helpContent = view.findViewById(R.id.rv_help_content);
        }
    }

    public HelpAdapter(List<Help> helpList, HomeActivity homeContext) {
        this.helpList = helpList;
        this.homeContext = homeContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.help_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Help help = helpList.get(position);
        holder.helpHeader.setText(help.getHelpHeader());
        holder.helpContent.setText(help.getHelpContent());
    }

    @Override
    public int getItemCount() {
        return helpList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
