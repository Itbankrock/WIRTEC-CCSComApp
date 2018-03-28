package com.dlsu.comapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsFragment extends Fragment {

    public List<Review> reviewList = new ArrayList<>();
    private Professor prof;
    private ReviewAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reviews, container, false);

        getActivity().setTitle("Reviews");
        final NavigationView navigationView = ((HomeActivity)getActivity()).getNavigationView();
        navigationView.getMenu().getItem(2).setChecked(true);
        progressbar = view.findViewById(R.id.prof_all_reviews_progressbar);

        prof = getArguments().getParcelable("thetargetprof");

        reviewList.clear();
        recyclerView = view.findViewById(R.id.prof_all_reviews_view);
        adapter = new ReviewAdapter(reviewList, ((HomeActivity)getActivity()));
        RecyclerView.LayoutManager hLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(hLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        setReviewList();
        adapter.notifyDataSetChanged();

        return view;
    }

    public void setReviewList() {
        reviewList.clear();
        progressbar.setVisibility(View.VISIBLE);
        DatabaseReference dbRevs = FirebaseDatabase.getInstance().getReference("prof_reviews");

        dbRevs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot object: dataSnapshot.getChildren()){
                    if(object.child("reviewProfID").getValue().toString().equals(prof.getId())){
                        reviewList.add(object.getValue(Review.class));
                    }
                }
                Collections.reverse(reviewList);
                adapter.notifyDataSetChanged();
                progressbar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
