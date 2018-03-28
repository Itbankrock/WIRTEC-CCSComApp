package com.dlsu.comapp;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminAddCourseFragment extends Fragment {

    private EditText courseCode;
    private Spinner unitsSpinner;
    private ArrayAdapter<String> unitsSpinnerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_add_course, container, false);

        getActivity().setTitle("Add Course");
        ((HomeActivity)getActivity()).setNavItem(6);
        final NavigationView navigationView = ((HomeActivity)getActivity()).getNavigationView();
        navigationView.getMenu().getItem(6).setChecked(true);

        courseCode = view.findViewById(R.id.add_course_code);
        courseCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(courseCode.getText().toString().length() == 7) {
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

        return view;
    }
}
