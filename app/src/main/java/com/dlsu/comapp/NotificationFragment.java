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

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    private List<Notification> notifList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        getActivity().setTitle("Notifications");
        ((HomeActivity)getActivity()).setNavItem(6);
        final NavigationView navigationView = ((HomeActivity)getActivity()).getNavigationView();
        for (int ctr = 0; ctr < navigationView.getMenu().size(); ctr ++) {
            navigationView.getMenu().getItem(ctr).setChecked(false);
        }

        notifList.clear();
        RecyclerView recyclerView = view.findViewById(R.id.notif_view);
        NotificationAdapter adapter = new NotificationAdapter(notifList, ((HomeActivity)getActivity()));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        setNotifList();
        adapter.notifyDataSetChanged();

        return view;
    }

    public void setNotifList() {
        for(int ctr = 0; ctr < 10; ctr ++) {
            if (ctr % 2 == 0)
                notifList.add(new Notification("Enrico Zabayle likes your thread.", "like"));
            else
                notifList.add(new Notification("Enrico Zabayle replies to your thread.", "reply"));
        }
    }
}
