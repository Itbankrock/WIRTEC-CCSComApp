package com.dlsu.comapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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
public class ForumThreadListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private List<ForumThread> forumList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ForumThreadListAdapter mAdapter;
    private User theuser;

    private FloatingActionButton fabNew;

    private DatabaseReference dbThreads = FirebaseDatabase.getInstance().getReference("threads");
    private final static int NEW_THREAD_CODE = 69;
    SwipeRefreshLayout mSwipeRefreshLayout;


    public ForumThreadListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forum_thread_list, container, false);

        theuser = getArguments().getParcelable("theuser");
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_forumthreadlist);
        recyclerView.setVisibility(View.INVISIBLE);
        fabNew = (FloatingActionButton) view.findViewById(R.id.fab_newThread);

        getActivity().setTitle("Forums");
        ((HomeActivity)getActivity()).setNavItem(3);
        final NavigationView navigationView = ((HomeActivity)getActivity()).getNavigationView();
        navigationView.getMenu().getItem(3).setChecked(true);

        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.rv_forumthreadlist_swiper);
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
                prepareforumList();
            }
        });


        //Opens a the new thread Activity Form
        fabNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(  ((HomeActivity)getActivity()) ,  NewThreadForm.class  );
                ((HomeActivity)getActivity()).startActivityForResult(i,NEW_THREAD_CODE);
            }
        });
        //

        //Initialize the RecyclerView and its Adapter
        mAdapter = new ForumThreadListAdapter(this,forumList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareforumList();

        return view;
    }

    private void prepareforumList() {
        dbThreads.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                forumList.clear();
                mSwipeRefreshLayout.setRefreshing(true);
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    forumList.add(snapshot.getValue(ForumThread.class));
                }
                mSwipeRefreshLayout.setRefreshing(false);
                mAdapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onRefresh() {
        prepareforumList();
    }
}
