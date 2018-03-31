package com.dlsu.comapp;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CourseProfFragment extends Fragment {

    private List<Course> courselist = new ArrayList<>();
    private List<Professor> proflist = new ArrayList<>();
    private RecyclerView recyclerView;
    private CourseAdapter cAdapter;
    private ProfessorAdapter pAdapter;
    private FragmentManager fragmentManager;
    private DatabaseReference dbTest;
    private LinearLayout progresselements;
    private SearchView searchView;

    public CourseProfFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_prof, container, false);
        final NavigationView navigationView = ((HomeActivity)getActivity()).getNavigationView();
        recyclerView = view.findViewById(R.id.frag_view);
        int navItem = ((HomeActivity)getActivity()).getNavItem();
        progresselements = view.findViewById(R.id.coursepro_progresselements);


        courselist.clear();
        proflist.clear();

        //prepareCourseData();

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) view.findViewById(R.id.courseprof_search);
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.colorWhite));
        searchEditText.setHintTextColor(getResources().getColor(R.color.colorWhite));
        ImageView searchIcon = searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        ImageView searchIcon2 = searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        searchIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);
        searchIcon2.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);

        if (navItem == 1) {
            getActivity().setTitle("Courses");
            ((HomeActivity)getActivity()).setNavItem(1);
            navigationView.getMenu().getItem(1).setChecked(true);
            cAdapter = new CourseAdapter(courselist, ((HomeActivity)getActivity()));
            RecyclerView.LayoutManager cLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(cLayoutManager);
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(cAdapter);

            prepareCourseData();
        }
        else if (navItem == 2) {
            getActivity().setTitle("Professors");
            ((HomeActivity)getActivity()).setNavItem(2);
            navigationView.getMenu().getItem(2).setChecked(true);
            ImageView frag_bg = view.findViewById(R.id.frag_background);
            frag_bg.setImageResource(R.drawable.profbg);

            TextView frag_title = view.findViewById(R.id.frag_title);
            frag_title.setText("CCS Professors");

            TextView frag_subtitle = view.findViewById(R.id.frag_subtitle);
            frag_subtitle.setText("Know more about your favorite professor");


            pAdapter = new ProfessorAdapter(proflist, ((HomeActivity)getActivity()));
            RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(pLayoutManager);
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(pAdapter);

            prepareProfData();

        }

        return view;
    }

    public void prepareCourseData() {
        progresselements.setVisibility(View.VISIBLE);
        dbTest = FirebaseDatabase.getInstance().getReference("courses");
        dbTest.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean checker = false;
                for(DataSnapshot object: dataSnapshot.getChildren()){
                    Course thiscourse= object.getValue(Course.class);
                    courselist.add( thiscourse );
                    checker = true;
                }
                if(checker){
                    cAdapter.notifyDataSetChanged();
                    progresselements.setVisibility(View.GONE);
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            // filter recycler view when query submitted
                            cAdapter.getFilter().filter(query);
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String query) {
                            // filter recycler view when text is changed
                            cAdapter.getFilter().filter(query);
                            return false;
                        }
                    });
                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });


        Collections.sort(courselist, new Comparator<Course>() {
            public int compare(Course a, Course b) {
                return a.getId().compareTo(b.getId());
            }
        });
    }

    public void prepareProfData() {
        progresselements.setVisibility(View.VISIBLE);
        dbTest = FirebaseDatabase.getInstance().getReference("professors");
        dbTest.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean checker = false;
                for(DataSnapshot object: dataSnapshot.getChildren()){
                    Professor thisprof= object.getValue(Professor.class);
                    proflist.add( thisprof );
                    checker = true;
                }
                if(checker){
                    pAdapter.notifyDataSetChanged();
                    progresselements.setVisibility(View.GONE);
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            // filter recycler view when query submitted
                            pAdapter.getFilter().filter(query);
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String query) {
                            // filter recycler view when text is changed
                            pAdapter.getFilter().filter(query);
                            return false;
                        }
                    });
                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        Collections.sort(proflist, new Comparator<Professor>() {
            public int compare(Professor a, Professor b) {
                return a.getName().compareTo(b.getName());
            }
        });
    }

    public ArrayList<String> getCourses(int position) {
        ArrayList<String> profCourseList = new ArrayList<>();
        return profCourseList;
    }

    public ArrayList<Professor> getProfs(int position) {
        ArrayList<Professor> courseProfList = new ArrayList<>();

        return courseProfList;
    }

    public void viewProfessor(int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("prof", proflist.get(position));
        bundle.putParcelable("theUser", getArguments().getParcelable("theuser") );
        ProfessorFragment prof = new ProfessorFragment();
        prof.setArguments(bundle);
        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_fragment, prof).addToBackStack(null).commit();
    }

    public void viewCourse(int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("course", courselist.get(position));
        bundle.putParcelableArrayList("profs", getProfs(position));

        CourseFragment course = new CourseFragment();
        course.setArguments(bundle);
        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_fragment, course).addToBackStack(null).commit();
    }
}
