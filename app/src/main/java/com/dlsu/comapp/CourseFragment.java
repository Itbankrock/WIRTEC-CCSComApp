package com.dlsu.comapp;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CourseFragment extends Fragment {

    private ArrayList<Note> noteList = new ArrayList<>();
    private FragmentManager fragmentManager;

    public CourseFragment () {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_course, container, false);

        getActivity().setTitle("Courses");
        final NavigationView navigationView = ((HomeActivity)getActivity()).getNavigationView();
        navigationView.getMenu().getItem(1).setChecked(true);

        Course course = getArguments().getParcelable("course");
        ArrayList<Professor> profList = getArguments().getParcelableArrayList("profs");

        TextView courseCode = view.findViewById(R.id.course_code);
        courseCode.setText(course.getCode());
        TextView courseDesc = view.findViewById(R.id.course_desc);
        courseDesc.setText(course.getDesc());
        TextView courseUnits = view.findViewById(R.id.course_units);
        courseUnits.setText(course.getUnits() + " units");
        TextView courseOver = view.findViewById(R.id.course_overview);
        courseOver.setText(course.getOverview());

        RecyclerView pRecyclerView = view.findViewById(R.id.possicle_profs_view);
        ProfessorAdapter pAdapter = new ProfessorAdapter(profList, ((HomeActivity)getActivity()), false);
        RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(getActivity());
        pRecyclerView.setLayoutManager(pLayoutManager);
        pRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        pRecyclerView.setItemAnimator(new DefaultItemAnimator());
        pRecyclerView.setAdapter(pAdapter);

        pAdapter.notifyDataSetChanged();

        noteList.clear();
        RecyclerView nRecyclerView = view.findViewById(R.id.available_notes_view);
        NoteAdapter nAdapter = new NoteAdapter(noteList, ((HomeActivity)getActivity()));
        RecyclerView.LayoutManager nLayoutManager = new GridLayoutManager(getActivity(), 2);
        nRecyclerView.setLayoutManager(nLayoutManager);
        nRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        nRecyclerView.setItemAnimator(new DefaultItemAnimator());
        nRecyclerView.setAdapter(nAdapter);

        setNoteList();
        nAdapter.notifyDataSetChanged();

        CardView addNote = view.findViewById(R.id.add_note);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNoteFragment addNoteFrag = new AddNoteFragment();
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_fragment, addNoteFrag).addToBackStack(null).commit();
            }
        });

        return view;
    }

    public void setNoteList() {
        noteList.add(new Note("Setters & Getters", "by Enrico Zabayle", "Write your notes here.", "2018-03-09"));
        noteList.add(new Note("Setters & Getters", "by Enrico Zabayle", "Write your notes here.", "2018-03-09"));
        noteList.add(new Note("Setters & Getters", "by Enrico Zabayle", "Write your notes here.", "2018-03-09"));
        noteList.add(new Note("Setters & Getters", "by Enrico Zabayle", "Write your notes here.", "2018-03-09"));
        noteList.add(new Note("Setters & Getters", "by Enrico Zabayle", "Write your notes here.", "2018-03-09"));
        noteList.add(new Note("Setters & Getters", "by Enrico Zabayle", "Write your notes here.", "2018-03-09"));
        noteList.add(new Note("Setters & Getters", "by Enrico Zabayle", "Write your notes here.", "2018-03-09"));
        noteList.add(new Note("Setters & Getters", "by Enrico Zabayle", "Write your notes here.", "2018-03-09"));
        noteList.add(new Note("Setters & Getters", "by Enrico Zabayle", "Write your notes here.", "2018-03-09"));
        noteList.add(new Note("Setters & Getters", "by Enrico Zabayle", "Write your notes here.", "2018-03-09"));
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public void viewNote(int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("note", noteList.get(position));
        bundle.putInt("position", position);
        NoteFragment note = new NoteFragment();
        note.setArguments(bundle);
        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_fragment, note).addToBackStack(null).commit();
    }
}