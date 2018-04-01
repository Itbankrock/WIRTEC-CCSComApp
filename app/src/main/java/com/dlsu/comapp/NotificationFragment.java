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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
public class NotificationFragment extends Fragment {

    private List<Notification> notifList = new ArrayList<>();
    private FirebaseUser fbCurrUser = FirebaseAuth.getInstance().getCurrentUser();
    private NotificationAdapter adapter;
    private RecyclerView recyclerView;

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
        recyclerView = view.findViewById(R.id.notif_view);
        adapter = new NotificationAdapter(notifList, ((HomeActivity)getActivity()));
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
        DatabaseReference dbUserNotifs = FirebaseDatabase.getInstance().getReference("users/" + fbCurrUser.getUid() + "/notifications");
        dbUserNotifs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot object: dataSnapshot.getChildren()){
                    if(object.child("notificationType").getValue().toString().equals("like")){
                        notifList.add(new Notification(object.child("message").getValue().toString(), "like"));
                    }
                    else if(object.child("notificationType").getValue().toString().equals("reply") || object.child("notificationType").getValue().toString().equals("comment")){
                        notifList.add(new Notification(object.child("message").getValue().toString(), "reply"));
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
