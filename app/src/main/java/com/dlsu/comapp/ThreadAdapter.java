package com.dlsu.comapp;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Andrew Santiago on 3/11/2018.
 */

public class ThreadAdapter extends RecyclerView.Adapter<ThreadAdapter.MyViewHolder> {

    private DatabaseReference dbUsers = FirebaseDatabase.getInstance().getReference("users");

    private List<ThreadPost> moviesList;
    private threadFragment tContext;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser fbCurrUser = mAuth.getCurrentUser();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView userfname,datecreated,content,numlikes;
        public ImageView userpic;
        public ImageButton replybutton,likebutton;

        public MyViewHolder(View view) {
            super(view);
            userfname = (TextView) view.findViewById(R.id.rv_threadpost_userfullname);
            datecreated = (TextView) view.findViewById(R.id.rv_threadpost_dateposted);
            content = (TextView) view.findViewById(R.id.rv_threadpost_maincontent);
            numlikes = (TextView) view.findViewById(R.id.rv_threadpost_numlikes);
            userpic = (ImageView) view.findViewById(R.id.rv_threadpost_userpic);
            replybutton = (ImageButton) view.findViewById(R.id.rv_threadpost_replybutton);
            likebutton = (ImageButton) view.findViewById(R.id.rv_threadpost_likebutton);
        }
    }


    public ThreadAdapter(threadFragment theContext, List<ThreadPost> moviesList) {
        this.moviesList = moviesList;
        this.tContext = theContext;
    }

    public ThreadAdapter(threadFragment theContext){
        moviesList = new ArrayList<>();
        this.tContext = theContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.threadpost_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ThreadPost post = moviesList.get(position);

        holder.datecreated.setText(post.getTimestamp());
        holder.content.setText(post.getContent());

        dbUsers.child(post.getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                holder.userfname.setText(name);
                Picasso.with(tContext.getActivity().getApplicationContext()).load( dataSnapshot.child("photourl").getValue().toString() ).into(holder.userpic);
            }
            @Override
            public void onCancelled(final DatabaseError databaseError) {
            }
        });

        final DatabaseReference dbPost = FirebaseDatabase.getInstance().getReference("thread_replies/" + post.getId());

        dbPost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final long i = dataSnapshot.child("likers").getChildrenCount();
                holder.numlikes.setText(i + "");
                //Check if the user has already liked this post
                if(dataSnapshot.child("likers").hasChild(fbCurrUser.getUid())){
                    holder.likebutton.setColorFilter(Color.argb(255, 27, 87, 50));
                    holder.likebutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dbPost.child("likers").child(fbCurrUser.getUid()).removeValue();
                        }
                    });
                }
                //Executes if the user hasn't liked the post yet
                else{
                    holder.likebutton.setColorFilter(Color.argb(255, 165, 165, 165));
                    holder.likebutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dbPost.child("likers").child(fbCurrUser.getUid()).setValue("liked");
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public void addPost(ThreadPost thepost){
        moviesList.add(thepost);
        notifyDataSetChanged();
    }
    public void clearPosts(){
        moviesList.clear();
    }

}