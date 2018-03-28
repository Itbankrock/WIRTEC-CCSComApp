package com.dlsu.comapp;


import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class threadFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private ThreadAdapter pAdapter;
    private DatabaseReference dbPosts;
    private DatabaseReference dbPostFinder;
    private FloatingActionButton fabNewReply;
    private List<ThreadPost> postsList = new ArrayList<>();

    private String id;
    private final static int NEW_REPLY_CODE = 70;
    SwipeRefreshLayout mSwipeRefreshLayout;


    public threadFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_thread, container, false);

        final ForumThread thethread = getArguments().getParcelable("targetObject");

        id = thethread.getId();
        dbPosts = FirebaseDatabase.getInstance().getReference("threads/" + id + "/replies");
        dbPostFinder = FirebaseDatabase.getInstance().getReference("thread_replies");

        fabNewReply = (FloatingActionButton) view.findViewById(R.id.fab_newReplyPost);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_threadpostlist);

        pAdapter = new ThreadAdapter(this,postsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pAdapter);


        //preparePosts();

        fabNewReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(  ((HomeActivity)getActivity()) ,  NewReplyThread.class  );
                i.putExtra("thethread",thethread);
                i.putExtra("leaction","newreply");
                ((HomeActivity)getActivity()).startActivityForResult(i,NEW_REPLY_CODE);
            }
        });

        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.rv_threadpostswipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);

                // Fetching data from server
                preparePosts();
            }
        });

        return view;
    }

    public void preparePosts() {
        dbPosts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postsList.clear();
                mSwipeRefreshLayout.setRefreshing(true);
                for(final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    dbPostFinder.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child(snapshot.getKey()).child("active").getValue(Boolean.class) == true){
                                pAdapter.addPost(dataSnapshot.child(snapshot.getKey()).getValue(ThreadPost.class));
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onRefresh() {
        preparePosts();
    }
}
