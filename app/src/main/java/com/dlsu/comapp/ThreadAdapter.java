package com.dlsu.comapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrew Santiago on 3/11/2018.
 */

public class ThreadAdapter extends RecyclerView.Adapter<ThreadAdapter.MyViewHolder> {

    private DatabaseReference dbUsers = FirebaseDatabase.getInstance().getReference("users");

    private List<ThreadPost> moviesList;
    private threadFragment tContext;
    private Context mainContext;
    private final static int EDIT_REPLY_CODE = 71;
    private final static int EDIT_THREAD_CODE = 68;
    private ProgressDialog progressDialog;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser fbCurrUser = mAuth.getCurrentUser();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView userfname,datecreated,content,numlikes,lastupdated;
        public ImageView userpic;
        public ImageButton likebutton,deletebutton,editbutton;
        public LinearLayout editorslayout;

        public MyViewHolder(View view) {
            super(view);
            userfname = (TextView) view.findViewById(R.id.rv_threadpost_userfullname);
            datecreated = (TextView) view.findViewById(R.id.rv_threadpost_dateposted);
            lastupdated = (TextView) view.findViewById(R.id.rv_threadpost_lastedited);
            content = (TextView) view.findViewById(R.id.rv_threadpost_maincontent);
            numlikes = (TextView) view.findViewById(R.id.rv_threadpost_numlikes);
            userpic = (ImageView) view.findViewById(R.id.rv_threadpost_userpic);
            //replybutton = (ImageButton) view.findViewById(R.id.rv_threadpost_replybutton);
            likebutton = (ImageButton) view.findViewById(R.id.rv_threadpost_likebutton);
            deletebutton = (ImageButton) view.findViewById(R.id.rv_threadpost_deletebutton);
            editbutton = (ImageButton) view.findViewById(R.id.rv_threadpost_editbutton);
            editorslayout = (LinearLayout) view.findViewById(R.id.rv_threadpost_editorslayout);

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
        this.mainContext = parent.getContext();

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ThreadPost post = moviesList.get(position);
        if(post.getUserID().equals( fbCurrUser.getUid() )){
            holder.editorslayout.setVisibility(View.VISIBLE);
            if(position == 0){
                holder.deletebutton.setVisibility(View.GONE);
                holder.editbutton.setImageResource(R.drawable.ic_master_edit);

                holder.editbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent( ((HomeActivity)(tContext.getActivity()) ) ,  NewThreadForm.class  );
                        i.putExtra("sendAction","editThread");
                        i.putExtra("threadID",post.getPostID());
                        i.putExtra("thepost",post);
                        tContext.getActivity().startActivityForResult(i,EDIT_THREAD_CODE);
                    }
                });
            }
            else{
                holder.editbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(  ((HomeActivity)tContext.getActivity()) ,  NewReplyThread.class  );
                        i.putExtra("leaction","editreply");
                        i.putExtra("replyID",post.getId());
                        i.putExtra("existingcontent",post.getContent());
                        ((HomeActivity) tContext.getActivity() ).startActivityForResult(i,EDIT_REPLY_CODE);
                    }
                });
            }
        }

        holder.datecreated.setText(post.getTimestamp());
        holder.content.setText(post.getContent());
        holder.lastupdated.setText("Last updated on " + post.getLastupdated());

        dbUsers.child(post.getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                holder.userfname.setText(name);
                Picasso.with(mainContext).load( dataSnapshot.child("photourl").getValue().toString() ).into(holder.userpic);

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
                            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy h:mm a");
                            String timestamp = dateFormat.format(new Date());

                            DatabaseReference dbUserActs = FirebaseDatabase.getInstance().getReference("users/" + fbCurrUser.getUid() + "/activities");
                            String actKey = dbUserActs.push().getKey();
                            dbUserActs.child(actKey).setValue(timestamp + " - " + fbCurrUser.getDisplayName().split(" ")[0] + " liked a thread post.");
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
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
                                progressDialog.setMessage("Deleting your post..."); // Setting Message
                                progressDialog.setTitle("Forums"); // Setting Title
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                                progressDialog.show(); // Display Progress Dialog
                                progressDialog.setCancelable(false);

                                DatabaseReference dbtest = FirebaseDatabase.getInstance().getReference("thread_replies/" + post.getId());
                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put("/active/", false);

                                dbtest.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();
                                        tContext.preparePosts();
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