package com.dlsu.comapp;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoteFragment extends Fragment {
    private EditText title;
    private EditText content;
    private FirebaseUser fbCurrUser = FirebaseAuth.getInstance().getCurrentUser();
    private FloatingActionButton editfab,savefab,cancelfab;
    private TextView author, lastupdated;
    private Note note;
    private String historyTitle,historyContent;

    public NoteFragment () {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        editfab = view.findViewById(R.id.notes_edit_edit_note);
        savefab = view.findViewById(R.id.notes_edit_save_note);
        cancelfab = view.findViewById(R.id.notes_edit_cancel_note);
        lastupdated = view.findViewById(R.id.notes_lastupdated);

        getActivity().setTitle("Note");
        final NavigationView navigationView = ((HomeActivity)getActivity()).getNavigationView();
        navigationView.getMenu().getItem(1).setChecked(true);

        note = getArguments().getParcelable("note");

        title = view.findViewById(R.id.notes_title);
        title.setText(note.getTitle());
        author = view.findViewById(R.id.notes_author);
        content = view.findViewById(R.id.notes_content);
        content.setText(note.getContent());

        DatabaseReference dbusers = FirebaseDatabase.getInstance().getReference("users/" + note.getAuthor());
        dbusers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                author.setText(dataSnapshot.child("name").getValue().toString() + "\nCreated on " + note.getDate());
            }
            @Override public void onCancelled(DatabaseError databaseError) {}});

        lastupdated.setText("Last updated on " + note.getLastupdated());
        title.setTag(title.getKeyListener());
        title.setKeyListener(null);
        content.setTag(content.getKeyListener());
        content.setKeyListener(null);

        if(fbCurrUser.getUid().equals(note.getAuthor())){
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    editfab.show();
                }
            }, 200);

        }

        editfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                historyContent = content.getText().toString();
                historyTitle = title.getText().toString();
                title.setKeyListener((KeyListener) title.getTag());
                content.setKeyListener((KeyListener) content.getTag());

                editfab.hide();
                savefab.show();
                cancelfab.show();
            }
        });

        cancelfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelfab.hide();
                savefab.hide();
                editfab.show();

                title.setTag(title.getKeyListener());
                title.setKeyListener(null);
                content.setTag(content.getKeyListener());
                content.setKeyListener(null);
                content.setText(historyContent);
                title.setText(historyTitle);
                InputMethodManager imm = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        savefab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                DatabaseReference dbNotes = FirebaseDatabase.getInstance().getReference("notes/" + note.getId());

                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy h:mm a");
                final String timestamp = dateFormat.format(new Date());

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/title/", title.getText().toString());
                childUpdates.put("/content/", content.getText().toString());
                childUpdates.put("/lastupdated/", timestamp);

                dbNotes.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity(),"Changes saved", Toast.LENGTH_SHORT).show();
                        cancelfab.hide();
                        savefab.hide();
                        editfab.show();
                        title.setTag(title.getKeyListener());
                        title.setKeyListener(null);
                        content.setTag(content.getKeyListener());
                        content.setKeyListener(null);
                        lastupdated.setText("Last updated on " + timestamp);
                        InputMethodManager imm = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                });
            }
        });

        return view;
    }
}
