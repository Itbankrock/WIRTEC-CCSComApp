package com.dlsu.comapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
 * Created by Enrico Zabayle on 20/03/2018.
 */

public class ReviewCommentAdapter extends RecyclerView.Adapter<ReviewCommentAdapter.MyViewHolder>{

    protected List<ReviewComment> reviewCommentList;
    protected HomeActivity homeContext;
    protected Context mainContext;
    private DatabaseReference dbUsers = FirebaseDatabase.getInstance().getReference("users");
    private FirebaseUser fbCurrUser = FirebaseAuth.getInstance().getCurrentUser();
    private final static int EDIT_COMMENT_CODE = 80;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView userfname,datecreated,content,lastupdated;
        public ImageView userpic;
        public ImageButton editbutton,deletebutton;
        public LinearLayout editorslayout;

        public MyViewHolder(View view) {
            super(view);
            userfname = (TextView) view.findViewById(R.id.rv_comment_userfullname);
            datecreated = (TextView) view.findViewById(R.id.rv_comment_dateposted);
            content = (TextView) view.findViewById(R.id.rv_comment_maincontent);
            userpic = (ImageView) view.findViewById(R.id.rv_comment_userpic);
            editbutton = (ImageButton) view.findViewById(R.id.rv_comment_editbutton);
            deletebutton = (ImageButton) view.findViewById(R.id.rv_comment_deletebutton);
            editorslayout = (LinearLayout) view.findViewById(R.id.rv_comment_editorslayout);
            lastupdated = (TextView) view.findViewById(R.id.rv_comment_lastupdated);
        }
    }


    public ReviewCommentAdapter(List<ReviewComment> reviewCommentList, HomeActivity homeContext) {
        this.reviewCommentList = reviewCommentList;
        this.homeContext = homeContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_comment_item_layout, parent, false);

        mainContext = parent.getContext();

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ReviewComment reviewComment = reviewCommentList.get(position);
        if(reviewComment.getUserID().equals( fbCurrUser.getUid() )){
            holder.editorslayout.setVisibility(View.VISIBLE);
        }

        dbUsers.child(reviewComment.getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                holder.userfname.setText( dataSnapshot.child("name").getValue().toString() );
                Picasso.with( mainContext ).load( dataSnapshot.child("photourl").getValue().toString() ).into(holder.userpic);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }});


        holder.datecreated.setText(reviewComment.getTimestamp());
        holder.content.setText(reviewComment.getContent());
        holder.lastupdated.setText("Last updated on " + reviewComment.getLastupdated());

        holder.editbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(  homeContext ,  NewReplyThread.class  );
                i.putExtra("leaction","editrevcomment");
                i.putExtra("commentID",reviewComment.getId());
                i.putExtra("existingcontent",reviewComment.getContent());
                homeContext.startActivityForResult(i,EDIT_COMMENT_CODE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviewCommentList.size();
    }

    public void addItem(ReviewComment item){
        reviewCommentList.add(item);
        notifyDataSetChanged();
    }
}
