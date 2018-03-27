package com.dlsu.comapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */

public class HomeFragment extends Fragment {

    private FragmentManager fragmentManager;
    private CardView cardViewCourses;
    private CardView cardViewProfs;
    private CardView cardViewForum;
    private CardView cardViewHelp;
    private User theuser;
    private TextView welcomemsg;
    private FloatingActionButton fabtutorial;

    public HomeFragment () {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        getActivity().setTitle("CCS ComApp");
        ((HomeActivity)getActivity()).setNavItem(0);
        final NavigationView navigationView = ((HomeActivity)getActivity()).getNavigationView();
        navigationView.getMenu().getItem(0).setChecked(true);

        theuser = getArguments().getParcelable("theuser");

        initialize(view);

        cardViewCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity)getActivity()).setNavItem(1);
                CourseProfFragment course = new CourseProfFragment();
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_fragment, course).addToBackStack(null).commit();
            }
        });

        cardViewProfs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity)getActivity()).setNavItem(2);
                Bundle bundle = new Bundle();
                bundle.putParcelable("theuser", theuser);
                CourseProfFragment course = new CourseProfFragment();
                course.setArguments(bundle);
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_fragment, course).addToBackStack(null).commit();
            }
        });

        cardViewForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("theuser", theuser);
                ForumThreadListFragment forums = new ForumThreadListFragment();
                forums.setArguments(bundle);
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_fragment, forums).addToBackStack(null).commit();
            }
        });

        cardViewHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HelpFragment help = new HelpFragment();
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_fragment, help).addToBackStack(null).commit();
            }
        });

        fabtutorial.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                TutorialFragment tut = new TutorialFragment();
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_fragment, tut).addToBackStack(null).commit();
            }
        });

        return view;
    }

    public void initialize(View view) {
        cardViewCourses = view.findViewById(R.id.card_view_courses);
        cardViewProfs = view.findViewById(R.id.card_view_profs);
        cardViewForum = view.findViewById(R.id.card_view_forum);
        cardViewHelp = view.findViewById(R.id.card_view_help);
        welcomemsg = view.findViewById(R.id.textView2);
        fabtutorial = view.findViewById(R.id.fab_home_tutorial);



        welcomemsg.setText("Archer " + theuser.getName().split(" ")[0]);
    }
}
