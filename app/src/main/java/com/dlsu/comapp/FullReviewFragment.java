package com.dlsu.comapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FullReviewFragment extends Fragment {

    private List<ReviewComment> reviewCommentList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_full_review, container, false);

        getActivity().setTitle("Comments");
        final NavigationView navigationView = ((HomeActivity)getActivity()).getNavigationView();
        navigationView.getMenu().getItem(2).setChecked(true);

        reviewCommentList.clear();
        RecyclerView recyclerView = view.findViewById(R.id.full_review_view);
        ReviewCommentAdapter adapter = new ReviewCommentAdapter(reviewCommentList, ((HomeActivity)getActivity()));
        RecyclerView.LayoutManager nLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(nLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        setReviewCommentList();
        adapter.notifyDataSetChanged();

        return view;
    }

    public void setReviewCommentList() {
        for(int ctr = 0; ctr < 10; ctr ++) {
            reviewCommentList.add(new ReviewComment("123", "Enrico Zabayle", "456", "Ugly duckling!", "March 20, 2018", true));
        }
    }
}
