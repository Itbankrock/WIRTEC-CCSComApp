package com.dlsu.comapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class FullReviewFragment extends Fragment {

    private List<ReviewComment> reviewCommentList = new ArrayList<>();
    private ImageView sendButton;
    private EditText commentbox;
    private FirebaseUser fbCurrUser = FirebaseAuth.getInstance().getCurrentUser();
    private String reviewID;
    private String makerID;
    DatabaseReference dbrevcomments = FirebaseDatabase.getInstance().getReference("review_comments");
    private ReviewCommentAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_full_review, container, false);

        reviewID = getArguments().getString("reviewID");
        makerID = getArguments().getString("makerID");
        sendButton = view.findViewById(R.id.full_review_send);
        commentbox = view.findViewById(R.id.full_review_comment);

        getActivity().setTitle("Comments");
        final NavigationView navigationView = ((HomeActivity)getActivity()).getNavigationView();
        navigationView.getMenu().getItem(2).setChecked(true);

        recyclerView = view.findViewById(R.id.full_review_view);
        adapter = new ReviewCommentAdapter(reviewCommentList, ((HomeActivity)getActivity()));
        RecyclerView.LayoutManager nLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(nLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        setReviewCommentList();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty( commentbox.getText().toString() )){
                    final DatabaseReference dbtest = FirebaseDatabase.getInstance().getReference("prof_reviews/" + reviewID + "/comments");
                    final String key = dbrevcomments.push().getKey();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy h:mm a");
                    String timestamp = dateFormat.format(new Date());
                    ReviewComment thecomment = new ReviewComment(key,fbCurrUser.getUid(),reviewID,commentbox.getText().toString(),timestamp,timestamp,true);
                            //(String id, String userID, String reviewID, String content, String timestamp, boolean isActive)
                    dbrevcomments.child(key).setValue(thecomment).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dbtest.child(key).setValue(true);
                            Toast.makeText(getActivity(), "Your comment has been posted!",
                                    Toast.LENGTH_SHORT).show();
                            setReviewCommentList();
                            if(!fbCurrUser.getUid().equals(makerID)){
                                DatabaseReference dbtest3 = FirebaseDatabase.getInstance().getReference("users/" + makerID);
                                String notifKey = dbtest3.child("notifications").push().getKey();
                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put("from", fbCurrUser.getUid());
                                childUpdates.put("message", fbCurrUser.getDisplayName().split(" ")[0] + " commented on your review");
                                childUpdates.put("messagelong", commentbox.getText().toString());
                                childUpdates.put("notificationType", "comment");
                                childUpdates.put("associatedID", reviewID);
                                dbtest3.child("notifications").child(notifKey).setValue(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override public void onComplete(@NonNull Task<Void> task) {commentbox.setText("");}});
                            }
                        }
                    });
                }

            }
        });

        return view;
    }

    public void setReviewCommentList() {
        reviewCommentList.clear();
        //(String id, String userID, String reviewID, String content, String timestamp, boolean isActive)
        final DatabaseReference dbtest = FirebaseDatabase.getInstance().getReference("prof_reviews/" + reviewID + "/comments");
        dbtest.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot object: dataSnapshot.getChildren()){
                     dbrevcomments.child(object.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                         @Override
                         public void onDataChange(DataSnapshot dataSnapshot) {
                             adapter.addItem(dataSnapshot.getValue(ReviewComment.class));
                         }
                         @Override
                         public void onCancelled(DatabaseError databaseError) {}});
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
