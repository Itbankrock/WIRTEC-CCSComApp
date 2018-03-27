package com.dlsu.comapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddNoteFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Add Note");
        final NavigationView navigationView = ((HomeActivity)getActivity()).getNavigationView();
        navigationView.getMenu().getItem(1).setChecked(true);

        View view = inflater.inflate(R.layout.fragment_add_note, container, false);

        EditText addContent = view.findViewById(R.id.add_notes_content);
        addContent.requestFocus();

        FloatingActionButton saveNote = view.findViewById(R.id.save_note);
        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //include backend
                Toast.makeText(getActivity(),"Include backend for this :')", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        });

        FloatingActionButton cancelNote = view.findViewById(R.id.cancel_note);
        cancelNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

}
