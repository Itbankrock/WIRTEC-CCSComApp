package com.dlsu.comapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Enrico Zabayle on 13/03/2018.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    protected List<Notification> notifList;
    protected HomeActivity homeContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView notifTitle;
        public ImageView notifIcon;

        public MyViewHolder(View view) {
            super(view);
            notifTitle = view.findViewById(R.id.rv_notif_title);
            notifIcon = view.findViewById(R.id.rv_notif_icon);
        }
    }

    public NotificationAdapter(List<Notification> notifList, HomeActivity homeContext) {
        this.notifList = notifList;
        this.homeContext = homeContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notif_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Notification notif = notifList.get(position);
        holder.notifTitle.setText(notif.getNotifTitle());

        if (notif.getNotifType().equals("like")){
            holder.notifIcon.setImageResource(R.drawable.ic_thumbsup);
        }
        else if (notif.getNotifType().equals("reply")) {
            holder.notifIcon.setImageResource(R.drawable.ic_comment);
        }
    }

    @Override
    public int getItemCount() {
        return notifList.size();
    }
}
