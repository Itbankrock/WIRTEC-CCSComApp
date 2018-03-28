package com.dlsu.comapp;


import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminAssociateCourseProfFragment extends Fragment {
    private Spinner spinProf;
    private Spinner spinCourse;
    private DatabaseReference dbTest;
    private ArrayList<Professor> proflist = new ArrayList<>();
    private ArrayList<Course> courselist = new ArrayList<>();
    private AdminAssociateProfAdapter profAdapter;
    private CourseSpinnerAdapter courseAdapter;
    private Button submitButton;


    public AdminAssociateCourseProfFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_associate_course_prof, container, false);

        getActivity().setTitle("Course-Prof Association");
        ((HomeActivity)getActivity()).setNavItem(5);
        final NavigationView navigationView = ((HomeActivity)getActivity()).getNavigationView();
        navigationView.getMenu().getItem(5).setChecked(true);

        spinProf = view.findViewById(R.id.admin_assoc_profspinner);
        spinCourse = view.findViewById(R.id.admin_assoc_coursespinner);
        submitButton = view.findViewById(R.id.admin_assoc_submitbutton);


        dbTest = FirebaseDatabase.getInstance().getReference("professors");

        profAdapter = new AdminAssociateProfAdapter(getActivity().getApplicationContext(),
                R.layout.spinner_textview,
                proflist);

        profAdapter.setDropDownViewResource(R.layout.spinner_textview);
        spinProf.setAdapter(profAdapter);

        courseAdapter = new CourseSpinnerAdapter(getActivity().getApplicationContext(),
                R.layout.spinner_textview, courselist);

        courseAdapter.setDropDownViewResource(R.layout.spinner_textview);
        spinCourse.setAdapter(courseAdapter);

        dbTest.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot object: dataSnapshot.getChildren()){
                    proflist.add(object.getValue(Professor.class));
                }
                profAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        dbTest = FirebaseDatabase.getInstance().getReference("courses");
        dbTest.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                courselist.add(new Course("Choose a course...","Eyy","0.0","EYYYY"));
                for(DataSnapshot object: dataSnapshot.getChildren()){
                    courselist.add(object.getValue(Course.class));
                }
                courseAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idcourse = courseAdapter.getItem( spinCourse.getSelectedItemPosition() ).getId();
                String idprof = profAdapter.getItem( spinProf.getSelectedItemPosition() ).getId();
                dbTest = FirebaseDatabase.getInstance().getReference("courses/" + idcourse);
                dbTest.child("professors").child(idprof).setValue(true);

                dbTest = FirebaseDatabase.getInstance().getReference("professors/" + idprof);
                dbTest.child("courses").child(idcourse).setValue(true);

                Toast.makeText(getActivity(), "Course Prof Associated!", Toast.LENGTH_SHORT).show();
            }
        });





        return view;
    }

}
