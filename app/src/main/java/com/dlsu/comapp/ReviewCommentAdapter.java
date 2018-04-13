package com.dlsu.comapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Enrico Zabayle and Andrew Santiago on 20/03/2018.
 */

public class ReviewCommentAdapter extends RecyclerView.Adapter<ReviewCommentAdapter.MyViewHolder>{

    protected List<ReviewComment> reviewCommentList;
    protected HomeActivity homeContext;
    protected Context mainContext;
    private FirebaseDatabase maindb = FirebaseDatabase.getInstance();
    private DatabaseReference dbUsers = maindb.getReference("users");
    private FirebaseUser fbCurrUser = FirebaseAuth.getInstance().getCurrentUser();
    private final static int EDIT_COMMENT_CODE = 80;
    private ProgressDialog progressDialog;
    private FullReviewFragment fragContext;

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


    public ReviewCommentAdapter(List<ReviewComment> reviewCommentList, HomeActivity homeContext, FullReviewFragment fragContext) {
        this.reviewCommentList = reviewCommentList;
        this.homeContext = homeContext;
        this.fragContext = fragContext;
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


        holder.deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                progressDialog = new ProgressDialog(mainContext,R.style.MyAlertDialogStyle);
                                progressDialog.setMessage("Deleting your comment..."); // Setting Message
                                progressDialog.setTitle("Review Comment"); // Setting Title
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                                progressDialog.show(); // Display Progress Dialog
                                progressDialog.setCancelable(false);

                                DatabaseReference dbComment = maindb.getReference("review_comments/" + reviewComment.getId());
                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put("/active/", false);

                                dbComment.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();
                                        fragContext.setReviewCommentList();
                                    }
                                });
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(mainContext);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
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
