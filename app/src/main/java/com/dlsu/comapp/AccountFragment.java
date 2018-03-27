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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private User theuser;
    private TextView acctname;
    private TextView acctemailnum;
    private ImageView acctpic;

    private EditText acctphone;
    private EditText acctcourse;
    private EditText acctaddress;
    private ImageView accteditbutton;
    private Button savebutton;
    DatabaseReference dbUsers;


    private ArrayList<ActivityLog> activityList = new ArrayList<>();

    public AccountFragment () {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Account");
        ((HomeActivity)getActivity()).setNavItem(5);
        final NavigationView navigationView = ((HomeActivity)getActivity()).getNavigationView();
        navigationView.getMenu().getItem(5).setChecked(true);

        View view =  inflater.inflate(R.layout.fragment_account, container, false);

        theuser = getArguments().getParcelable("theuser");
        initialize(view);

        setActivityLog(view);

        acctname.setText(theuser.getName());
        acctemailnum.setText(theuser.getEmail());
        Picasso.with(getActivity().getApplicationContext()).load(theuser.getPhotourl().toString()).into(acctpic);
        acctphone.setText(theuser.getPhoneNumber());
        acctcourse.setText(theuser.getCourse());
        acctaddress.setText(theuser.getAddress());


        return view;
    }

    private void initialize(View view){
        acctname = view.findViewById(R.id.account_name);
        acctemailnum = view.findViewById(R.id.account_email);
        acctpic = view.findViewById(R.id.account_userpic);

        acctphone = view.findViewById(R.id.account_phone);
        acctcourse = view.findViewById(R.id.account_course);
        acctaddress = view.findViewById(R.id.account_address);
        accteditbutton = view.findViewById(R.id.account_edit);
        savebutton = view.findViewById(R.id.account_savebutton);

        acctphone.setEnabled(false);
        acctcourse.setEnabled(false);
        acctaddress.setEnabled(false);

        accteditbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                accteditbutton.setVisibility(View.INVISIBLE);

                acctphone.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                acctcourse.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                acctaddress.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

                acctphone.setEnabled(true);
                acctphone.requestFocus();
                acctcourse.setEnabled(true);
                acctaddress.setEnabled(true);

                savebutton.setVisibility(View.VISIBLE);
            }
        });

        savebutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                accteditbutton.setVisibility(View.VISIBLE);

                acctphone.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                acctcourse.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                acctaddress.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

                acctphone.setEnabled(false);
                acctcourse.setEnabled(false);
                acctaddress.setEnabled(false);

                savebutton.setVisibility(View.INVISIBLE);

                dbUsers = FirebaseDatabase.getInstance().getReference("users");
                dbUsers.child(theuser.getGoogleuid()).child("course").setValue(acctcourse.getText().toString());
                dbUsers.child(theuser.getGoogleuid()).child("address").setValue(acctaddress.getText().toString());
                dbUsers.child(theuser.getGoogleuid()).child("phonenumber").setValue(acctphone.getText().toString());

                Toast.makeText(getActivity(), "Account information updated", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setActivityLog(View view) {
        activityList.clear();
        RecyclerView recyclerView = view.findViewById(R.id.activity_log_view);
        ActivityLogAdapter adapter = new ActivityLogAdapter(activityList, ((HomeActivity)getActivity()));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        setActivityList();
        adapter.notifyDataSetChanged();
    }

    public void setActivityList() {
        for (int ctr = 0; ctr < 10; ctr ++) {
            if (ctr % 2 == 0)
                activityList.add(new ActivityLog("Enrico Zabayle likes Carlos' thread in the forum."));
            else if (ctr % 3 == 0)
                activityList.add(new ActivityLog("Enrico Zabayle added a note in APP-DEV"));
            else
                activityList.add(new ActivityLog("Enrico Zabayle commented on Andrew's thread in the forum"));
        }
    }
}
