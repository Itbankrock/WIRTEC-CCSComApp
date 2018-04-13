package com.dlsu.comapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedbackFragment extends Fragment {

    private EditText newFeedbackSubject;
    private EditText newFeedbackContent;
    private FirebaseUser fbCurrUser = FirebaseAuth.getInstance().getCurrentUser();
    private ProgressDialog progressDialog;

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
                    progressDialog = new ProgressDialog(getContext(),R.style.MyAlertDialogStyle);
                    progressDialog.setMessage("Sending feedback..."); // Setting Message
                    progressDialog.setTitle("CCS ComApp"); // Setting Title
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    DatabaseReference dbFeedback = FirebaseDatabase.getInstance().getReference("feedbacks");
                    String key = dbFeedback.push().getKey();
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("title", newFeedbackSubject.getText().toString());
                    childUpdates.put("feedback", newFeedbackContent.getText().toString());
                    childUpdates.put("userID", fbCurrUser.getUid());
                    childUpdates.put("user_name",fbCurrUser.getDisplayName());
                    dbFeedback.child(key).setValue(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(getContext(),"Done! Thank you for your feedback!",Toast.LENGTH_SHORT).show();
                                newFeedbackContent.setText("");
                                newFeedbackSubject.setText("");
                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(getContext(),"There seems to be a problem submitting your feedback. Please try again.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                return toReturn;
            }
        });

        return view;
    }
}
