package com.dlsu.comapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForAllFragment extends Fragment {

    private List<Course> courselist = new ArrayList<>();
    private List<Professor> proflist = new ArrayList<>();
    private RecyclerView recyclerView;
    private CourseAdapter cAdapter;
    private ProfessorAdapter pAdapter;
    private FragmentManager fragmentManager;

    public ForAllFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_for_all, container, false);
        recyclerView = view.findViewById(R.id.frag_view);
        int navItem = ((HomeActivity)getActivity()).getNavItem();

        prepareCourseData();

        if (navItem == 1) {
            cAdapter = new CourseAdapter(courselist, ((HomeActivity)getActivity()));
            RecyclerView.LayoutManager cLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(cLayoutManager);
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(cAdapter);

            cAdapter.notifyDataSetChanged();
        }
        else if (navItem == 2) {
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

            pAdapter.notifyDataSetChanged();
        }

        return view;
    }

    public void prepareCourseData() {
        prepareProfData();


        Collections.sort(courselist, new Comparator<Course>() {
            public int compare(Course a, Course b) {
                return a.getId().compareTo(b.getId());
            }
        });
    }

    public void prepareProfData() {

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
        bundle.putStringArrayList("courses", (ArrayList<String>) getCourses(position));
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
