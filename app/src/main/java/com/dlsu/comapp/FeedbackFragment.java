package com.dlsu.comapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedbackFragment extends Fragment {

    private EditText newFeedbackSubject;
    private EditText newFeedbackContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);

        getActivity().setTitle("Feedback");
        ((HomeActivity)getActivity()).setNavItem(9);
        final NavigationView navigationView = ((HomeActivity)getActivity()).getNavigationView();
        navigationView.getMenu().getItem(9).setChecked(true);

        newFeedbackSubject = view.findViewById(R.id.add_feedback_subject);
        newFeedbackContent = view.findViewById(R.id.add_feedback_content);

        Toolbar actionBarToolBar = view.findViewById(R.id.feedback_toolbar);
        actionBarToolBar.inflateMenu(R.menu.newthread_items);
        actionBarToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                boolean toReturn = false;
                int id = item.getItemId();

                //noinspection SimplifiableIfStatement
                if (id == R.id.newThread_action_submit) {
                    /*Intent returnIntent = new Intent();
                    returnIntent.putExtra("newthreadtitle", .getText().toString());
                    returnIntent.putExtra("newthreadcontent",newThreadContent.getText().toString());
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();

                    toReturn = true;*/
                }
                return toReturn;
            }
        });

        return view;
    }
}
