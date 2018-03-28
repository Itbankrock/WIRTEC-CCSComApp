package com.dlsu.comapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminAddProfFragment extends Fragment {

    private Spinner posSpinner;
    private Spinner deptSpinner;
    private ArrayAdapter<String> posSpinnerAdapter;
    private ArrayAdapter<String> deptSpinnerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_add_prof, container, false);

        getActivity().setTitle("Add Professor");
        ((HomeActivity)getActivity()).setNavItem(7);
        final NavigationView navigationView = ((HomeActivity)getActivity()).getNavigationView();
        navigationView.getMenu().getItem(7).setChecked(true);

        String[] pos = {"Lecturer", "Assistant Professor", "Full Professor"};
        posSpinner = view.findViewById(R.id.add_prof_position_spinner);

        posSpinnerAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(),
                R.layout.general_spinner_layout, new ArrayList<>(Arrays.asList(pos)));
        posSpinner.setAdapter(posSpinnerAdapter);

        String[] dept = {"IT", "ST", "CT"};
        deptSpinner = view.findViewById(R.id.add_prof_dept_spinner);

        deptSpinnerAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(),
                R.layout.general_spinner_layout, new ArrayList<>(Arrays.asList(dept)));
        deptSpinner.setAdapter(deptSpinnerAdapter);

        return view;
    }
}
