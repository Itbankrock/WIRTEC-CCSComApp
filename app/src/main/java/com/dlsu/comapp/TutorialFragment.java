package com.dlsu.comapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class TutorialFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);

        getActivity().setTitle("Tutorial");
        ((HomeActivity)getActivity()).setNavItem(7);
        final NavigationView navigationView = ((HomeActivity)getActivity()).getNavigationView();
        for (int ctr = 0; ctr < navigationView.getMenu().size(); ctr ++) {
            navigationView.getMenu().getItem(ctr).setChecked(false);
        }

        return view;
    }
}
