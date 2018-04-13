package com.dlsu.comapp;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragment extends Fragment {

    private ExpandableRelativeLayout expand;
    private List<Help> helpList = new ArrayList<>();
    private Button eButton1, eButton2;
    private ExpandableRelativeLayout eLayout1, eLayout2;

    public HelpFragment () {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("DLSU FAQ");
        ((HomeActivity)getActivity()).setNavItem(4);
        final NavigationView navigationView = ((HomeActivity)getActivity()).getNavigationView();
        navigationView.getMenu().getItem(4).setChecked(true);

        final View view = inflater.inflate(R.layout.fragment_help, container, false);

        final RecyclerView hRecyclerView = view.findViewById(R.id.help_view);
        HelpAdapter hAdapter = new HelpAdapter(helpList, ((HomeActivity)getActivity()));
        RecyclerView.LayoutManager hLayoutManager = new LinearLayoutManager(getActivity());
        hRecyclerView.setLayoutManager(hLayoutManager);
        hRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        hRecyclerView.setItemAnimator(new DefaultItemAnimator());
        hRecyclerView.setAdapter(hAdapter);

        setHelpList();
        hAdapter.notifyDataSetChanged();

        return view;
    }

    public void setHelpList() {
        helpList.add(new Help("At what year level do students start majoring in their respective degree programs?", "Students generally begin to major in their fifth term, or during the second term of their second year. Prior to entrance to a major program, some departments require a qualifying examination. Other departments peg a cut-off grade for entrance to their programs."));
        helpList.add(new Help("Is it true that one's chances of being admitted improves by enrolling in the more difficult and \"least-enrolled-in\" degree programs, then later shifting to the degree program that one really prefers?", "The school has strict policies on shifting. We do not encourage backdoor entries and changing degree programs. When admitted to a degree program, it is expected that the student has the aptitude for that particular degree program."));
        helpList.add(new Help("Where can I find a list of undergraduate majors?", "You may visit the Undergraduate Degree Catalog. The Graduate Degree Programs are grouped according to the college where they are offered."));
        helpList.add(new Help("What is the average size of an undergraduate class?", "For freshmen, there are 42 students in each class. For upper classes, there are around 40 students per class."));
        helpList.add(new Help("Does DLSU-Manila have English as a Second Language (ESL) Programs?", "The College of Education (CED) offers BSE-English, MAT-English Language, and MA in English Language Education. Although they are not marketed as ESL programs, they are tacitly standard ESL programs."));
        helpList.add(new Help("Does DLSU-Manila offer distance learning degree programs?", "At present, there are no distance-learning degree programs. However, the Educational Management Department of the College of Education (CED) is currently working on this and may offer it in the future.\n" +
                "CED, however, conducts extension classes in \"partner institutions\". The extension classes are held in the premises of the partner institution and the instructors come from DLSU-Manila."));
        helpList.add(new Help("Does DLSU-Manila have executive education or professional enrichment programs?", "These types of programs are incorporated in the Educational Management Program of the College of Education."));
        helpList.add(new Help("Does DLSU-Manila have courses for high school students?", "DLSU-Manila does NOT have a high school program."));
        helpList.add(new Help("Do you encourage students to take double majors?", "Taking a double major degree is not really a bad option. In these times when industries and fields of study become multidisciplinary, a person with training in more than just one specialization has a distinct advantage."));
        helpList.add(new Help("In addition to academic programs, what other activities do Lasallian students engage in?", "Lasallian students may choose from the different organizations which have been grouped into cultural, sports and publications, among others."));
    }
}
