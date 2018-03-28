package com.dlsu.comapp;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Andrew Santiago on 3/10/2018.
 */

public class ForumThreadListAdapter extends RecyclerView.Adapter<ForumThreadListAdapter.MyViewHolder> {

    private DatabaseReference dbUsers = FirebaseDatabase.getInstance().getReference("users");

    private List<ForumThread> threadList;
    private PrettyTime p = new PrettyTime();
    private SimpleDateFormat dateFormat;
    private NumberShortener ns = new NumberShortener();
    private ForumThreadListFragment mContext;
    private Context mainContext;
    private DatabaseReference dbTest;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, creatorandtime, content, views, replies;
        public ImageView userpic;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.rv_thread_title);
            creatorandtime = (TextView) view.findViewById(R.id.rv_thread_creator);
            content = (TextView) view.findViewById(R.id.rv_thread_text);
            views = (TextView) view.findViewById(R.id.rv_thread_tv_views);
            replies = (TextView) view.findViewById(R.id.rv_thread_tv_replies);
            userpic = (ImageView) view.findViewById(R.id.rv_thread_userpic);
        }
    }


    public ForumThreadListAdapter(ForumThreadListFragment mContext, List<ForumThread> threadList) {
        this.mContext = mContext;
        this.threadList = threadList;
    }
    public ForumThreadListAdapter(ForumThreadListFragment mContext){
        this.mContext = mContext;
        this.threadList = new ArrayList<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.forumthread_item_layout, parent, false);

        this.mainContext = parent.getContext();

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        dateFormat = new SimpleDateFormat("MMMM dd, yyyy h:mm a");
        final ForumThread thread = threadList.get(position);

        dbUsers.child(thread.getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String dateposted = "";
                try {
                    Date date = dateFormat.parse(thread.getTimestamp());
                    holder.creatorandtime.setText(name + " • " + p.format(date) );
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Picasso.with(mainContext).load( dataSnapshot.child("photourl").getValue().toString() ).into(holder.userpic);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.title.setText(thread.getTitle());
        holder.content.setText(thread.getContent());
        holder.views.setText(ns.format(thread.getViews()) + " ");


        dbTest = FirebaseDatabase.getInstance().getReference("threads/" + thread.getId()).child("replies");
        dbTest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                holder.replies.setText(ns.format( dataSnapshot.getChildrenCount() ) + " ");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity) mContext.getActivity()).viewThread(thread);
                dbTest = FirebaseDatabase.getInstance().getReference("threads/" + thread.getId());
                dbTest.child("views").setValue(thread.getViews() + 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return threadList.size();
    }

    public void addItem(ForumThread item){
        this.threadList.add(item);
        notifyDataSetChanged();
    }
    public void clearItems(){
        threadList.clear();
    }
}
