package com.dlsu.comapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminAddCourseFragment extends Fragment {

    private EditText courseCode, description, overview;
    private Spinner unitsSpinner;
    private ArrayAdapter<String> unitsSpinnerAdapter;
    private Button submitbutton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_add_course, container, false);

        getActivity().setTitle("Add Course");
        ((HomeActivity)getActivity()).setNavItem(6);
        final NavigationView navigationView = ((HomeActivity)getActivity()).getNavigationView();
        navigationView.getMenu().getItem(6).setChecked(true);

        submitbutton = view.findViewById(R.id.add_course_submit);
        courseCode = view.findViewById(R.id.add_course_code);
        description = view.findViewById(R.id.add_course_desc);
        overview = view.findViewById(R.id.add_course_overview);
        courseCode.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        courseCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(courseCode.getText().toString().length() == 8) {
                    courseCode.setText(courseCode.getText().toString().substring(0,7));
                    Toast.makeText(getActivity(), "Course code has 7 characters only", Toast.LENGTH_SHORT).show();
                    InputMethodManager imm = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(courseCode.getWindowToken(), 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        String[] units = {"0.0", "1.0", "2.0", "3.0"};
        unitsSpinner = view.findViewById(R.id.add_course_units);

        unitsSpinnerAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(),
                R.layout.general_spinner_layout, new ArrayList<>(Arrays.asList(units)));
        unitsSpinner.setAdapter(unitsSpinnerAdapter);

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //public Course(String code, String desc, String units, String overview)
                final String code = courseCode.getText().toString();
                final String desc = description.getText().toString();
                final String units = unitsSpinner.getSelectedItem().toString();
                final String ovv = overview.getText().toString();

                final DatabaseReference dbcourses = FirebaseDatabase.getInstance().getReference("courses");
                dbcourses.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(code)){
                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){
                                        case DialogInterface.BUTTON_POSITIVE:
                                            //Yes button clicked
                                            Map<String, Object> childUpdates = new HashMap<>();
                                            childUpdates.put("/desc/", desc);
                                            childUpdates.put("/overview/", ovv);
                                            childUpdates.put("/units/", units);

                                            dbcourses.child(code).updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(getActivity(),"Course updated!",Toast.LENGTH_SHORT).show();
                                                    courseCode.setText("");
                                                    description.setText("");
                                                    overview.setText("");
                                                    unitsSpinner.setSelection(0);
                                                }
                                            });
                                            break;

                                        case DialogInterface.BUTTON_NEGATIVE:

                                            break;
                                    }
                                }
                            };

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("This course exists already in the database, would you like to update the existing one?").setPositiveButton("Yes", dialogClickListener)
                                    .setNegativeButton("No", dialogClickListener).show();
                        }
                        else{
                            dbcourses.child(code).setValue( new Course(code,desc,units,ovv) ).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getActivity(),"Course added!",Toast.LENGTH_SHORT).show();
                                    courseCode.setText("");
                                    description.setText("");
                                    overview.setText("");
                                    unitsSpinner.setSelection(1);
                                }
                            });
                        }

                        }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }
        });

        return view;
    }
}
