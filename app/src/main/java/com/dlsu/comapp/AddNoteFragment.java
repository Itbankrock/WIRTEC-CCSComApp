package com.dlsu.comapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddNoteFragment extends Fragment {
    private EditText title,addContent;
    private FloatingActionButton saveNote;
    private Course thecourse;
    private FirebaseUser fbCurrUser = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Add Note");
        final NavigationView navigationView = ((HomeActivity)getActivity()).getNavigationView();
        navigationView.getMenu().getItem(1).setChecked(true);

        thecourse = getArguments().getParcelable("thecourse");

        View view = inflater.inflate(R.layout.fragment_add_note, container, false);

        title = view.findViewById(R.id.add_notes_title);
        title.requestFocus();
        addContent = view.findViewById(R.id.add_notes_content);

        saveNote = view.findViewById(R.id.add_notes_save_fab);
        saveNote.show();
        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //include backend
                DatabaseReference dbnotes = FirebaseDatabase.getInstance().getReference("notes");
                final DatabaseReference dbcoursenotes = FirebaseDatabase.getInstance().getReference("courses/" + thecourse.getId() + "/notes");
                final String key = dbnotes.push().getKey();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy h:mm a");
                String timestamp = dateFormat.format(new Date());
                dbnotes.child(key).setValue( new Note(key,thecourse.getId(),title.getText().toString(),fbCurrUser.getUid(),addContent.getText().toString(),timestamp,timestamp) ).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dbcoursenotes.child(key).setValue(true);
                        Toast.makeText(getActivity(),"Note saved! Thank you for your contribution!", Toast.LENGTH_SHORT).show();
                        InputMethodManager imm = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        getActivity().onBackPressed();
                    }
                });


            }
        });

        FloatingActionButton cancelNote = view.findViewById(R.id.add_notes_cancel_fab);
        cancelNote.show();
        cancelNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                getActivity().onBackPressed();
            }
        });

        return view;
    }

}
