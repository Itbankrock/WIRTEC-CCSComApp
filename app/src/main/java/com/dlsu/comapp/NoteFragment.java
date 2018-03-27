package com.dlsu.comapp;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoteFragment extends Fragment {

    public NoteFragment () {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        getActivity().setTitle("Note");
        final NavigationView navigationView = ((HomeActivity)getActivity()).getNavigationView();
        navigationView.getMenu().getItem(1).setChecked(true);

        Note note = getArguments().getParcelable("note");

        TextView title = view.findViewById(R.id.notes_title);
        title.setText(note.getTitle());
        TextView author = view.findViewById(R.id.notes_author);
        author.setText(note.getAuthor());
        EditText content = view.findViewById(R.id.notes_content);
        content.setText(note.getContent());
        content.requestFocus();

        note.setTitle(title.getText().toString());
        note.setAuthor(author.getText().toString());
        note.setContent(content.getText().toString());

        return view;
    }
}
