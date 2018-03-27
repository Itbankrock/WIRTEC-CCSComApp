package com.dlsu.comapp;

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

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragment extends Fragment {

    private ExpandableRelativeLayout expand;
    private List<Help> helpList = new ArrayList<>();

    public HelpFragment () {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("FAQ");
        ((HomeActivity)getActivity()).setNavItem(4);
        final NavigationView navigationView = ((HomeActivity)getActivity()).getNavigationView();
        navigationView.getMenu().getItem(4).setChecked(true);

        final View view = inflater.inflate(R.layout.fragment_help, container, false);

        final RecyclerView hRecyclerView = view.findViewById(R.id.help_view);
        HelpAdapter hAdapter = new HelpAdapter(helpList, ((HomeActivity)getActivity()));
        RecyclerView.LayoutManager hLayoutManager = new LinearLayoutManager(getActivity());
        hRecyclerView.setLayoutManager(hLayoutManager);
        hRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        hRecyclerView.setItemAnimator(new DefaultItemAnimator());
        hRecyclerView.setAdapter(hAdapter);

        setHelpList();
        hAdapter.notifyDataSetChanged();

        return view;
    }

    public void setHelpList() {
        for (int ctr = 0; ctr < 10; ctr ++) {
            helpList.add(new Help("DO U KNOW DA WAE?", "Possibly the January 2018 Meme of the Month, \"Do you know da wae\"; has taken the world by storm. Many brave, Ugandan Warriors formed by a group of Knuckles, which is a Sonic character, follow around people on VRChat, asking if they know the way. They find queens to follow, and if they find a false queen, they make spit sounds. If they find a real queen, they click their tongues to show that they are the queen's followers. This random craze is funny because of how random it is, and many people do not seem to laugh at this, usually people who are not true meme artists."));
        }
    }
}
