package com.dlsu.comapp;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Enrico Zabayle and Andrew Santiago on 14/03/2018 to 19/03/2018
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder>{

    protected List<Review> reviewList;
    protected HomeActivity homeContext;
    private DatabaseReference dbUsers = FirebaseDatabase.getInstance().getReference("users");
    private DatabaseReference dbCourses = FirebaseDatabase.getInstance().getReference("courses");
    private FirebaseUser fbCurrUser = FirebaseAuth.getInstance().getCurrentUser();
    private NumberShortener ns = new NumberShortener();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView reviewName, reviewDate, reviewCourse, reviewContent, numlikes;
        public RatingBar reviewRating;
        public ImageView userpic;
        public ImageView likebutton;

        public MyViewHolder(View view) {
            super(view);
            reviewName = view.findViewById(R.id.rv_review_name);
            reviewDate = view.findViewById(R.id.rv_review_date);
            reviewCourse = view.findViewById(R.id.rv_review_course);
            reviewContent = view.findViewById(R.id.rv_review_content);
            reviewRating = view.findViewById(R.id.rv_review_rating);
            userpic = view.findViewById(R.id.rv_review_icon);
            likebutton = view.findViewById(R.id.rv_review_like);
            numlikes = view.findViewById(R.id.rv_review_num_likes);
        }
    }


    public ReviewAdapter(List<Review> reviewList, HomeActivity homeContext) {
        this.reviewList = reviewList;
        this.homeContext = homeContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Review review = reviewList.get(position);

        dbUsers.child(review.getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                holder.reviewName.setText( dataSnapshot.child("name").getValue().toString() );
                Picasso.with( homeContext.getApplicationContext() ).load( dataSnapshot.child("photourl").getValue().toString() ).into(holder.userpic);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }});

        dbCourses.child(review.getReviewCourseID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                holder.reviewCourse.setText( dataSnapshot.child("code").getValue().toString() );
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }});

        holder.reviewDate.setText(review.getReviewDate());
        holder.reviewContent.setText(review.getReviewContent());
        holder.reviewRating.setRating(review.getReviewRating());

        final DatabaseReference dbPost = FirebaseDatabase.getInstance().getReference("prof_reviews/" + review.getReviewID());

        dbPost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final long i = dataSnapshot.child("likers").getChildrenCount();
                holder.numlikes.setText( ns.format( (long) dataSnapshot.child("likerscount").getValue() ) + "" );
                //Check if the user has already liked this post
                if(dataSnapshot.child("likers").hasChild(fbCurrUser.getUid())){
                    holder.likebutton.setColorFilter(Color.argb(255, 27, 87, 50));
                    holder.likebutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dbPost.child("likerscount").setValue(i - 1);
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
                            dbPost.child("likerscount").setValue(i + 1);
                            dbPost.child("likers").child(fbCurrUser.getUid()).setValue("liked");
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeContext.viewFullReview(review.getReviewID(),review.getUserID());
            }
        });

    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }
}
