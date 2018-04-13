package com.dlsu.comapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
    private EditText name,email;
    private Button submitbutton;
    private ImageView addpic;
    private static final int PICK_IMAGE = 90;
    private StorageReference mStorage;
    private DatabaseReference dbProfs = FirebaseDatabase.getInstance().getReference("professors");

    private Uri imageUri;
    private String imageurl = "";

    private ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_add_prof, container, false);

        imageUri = null;
        mStorage = FirebaseStorage.getInstance().getReference().child("profimages");

        progressDialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Uploading data..."); // Setting Message
        progressDialog.setTitle("Add Professor"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);

        getActivity().setTitle("Add Professor");
        addpic = view.findViewById(R.id.add_prof_imagepick);
        name = view.findViewById(R.id.add_prof_name);
        email = view.findViewById(R.id.add_prof_email);
        submitbutton = view.findViewById(R.id.add_prof_submitbutton);
        ((HomeActivity)getActivity()).setNavItem(7);
        final NavigationView navigationView = ((HomeActivity)getActivity()).getNavigationView();
        navigationView.getMenu().getItem(7).setChecked(true);

        String[] pos = {"Lecturer", "Assistant Prof. Lecturer", "Assistant Professor", "Full Professor", "Associate Professor", "Teaching Associate"};
        posSpinner = view.findViewById(R.id.add_prof_position_spinner);

        posSpinnerAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(),
                R.layout.general_spinner_layout, new ArrayList<>(Arrays.asList(pos)));
        posSpinner.setAdapter(posSpinnerAdapter);

        final String[] dept = {"IT", "ST", "CT"};
        deptSpinner = view.findViewById(R.id.add_prof_dept_spinner);

        deptSpinnerAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(),
                R.layout.general_spinner_layout, new ArrayList<>(Arrays.asList(dept)));
        deptSpinner.setAdapter(deptSpinnerAdapter);

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String key = dbProfs.push().getKey();

                if(imageUri != null){
                    progressDialog.show(); // Display Progress Dialog
                    StorageReference prof_pic = mStorage.child(key + ".jpg");

                    prof_pic.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                imageurl = task.getResult().getDownloadUrl().toString();

                                dbProfs.child(key).setValue( new Professor(key,name.getText().toString(),email.getText().toString(),"0.0",deptSpinner.getSelectedItem().toString(),posSpinner.getSelectedItem().toString(),imageurl) ).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            Toast.makeText(getActivity(),"Professor added!",Toast.LENGTH_SHORT).show();
                                            name.setText("");
                                            email.setText("");
                                            deptSpinner.setSelection(0);
                                            posSpinner.setSelection(0);
                                            addpic.setImageResource(R.drawable.ic_add_circle);
                                        }
                                        else{
                                            Toast.makeText(getActivity(),"There was an error.",Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }

                                    }
                                });

                            }
                            else{Toast.makeText(getActivity(),"Error uploading pic",Toast.LENGTH_SHORT).show();progressDialog.dismiss();}
                        }
                    });
                }
                        //Professor(String id, String name, String email, String rating, String department, String position, String photourl)
            }
        });

        addpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE){
            if(data !=null){
                imageUri = data.getData();
                addpic.setColorFilter(null);
                addpic.setImageURI(imageUri);
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            //Write your code if there's no result
        }
    }
}