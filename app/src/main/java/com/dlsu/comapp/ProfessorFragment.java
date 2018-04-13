package com.dlsu.comapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfessorFragment extends Fragment {

    private FragmentManager fragmentManager;
    public List<Review> reviewList = new ArrayList<>();
    ArrayList<String> courses = new ArrayList<>();
    private DatabaseReference dbProf = FirebaseDatabase.getInstance().getReference("professors");
    private DatabaseReference dbProfRev = FirebaseDatabase.getInstance().getReference("prof_reviews");
    private DatabaseReference dbUserActivity = FirebaseDatabase.getInstance().getReference("users");
    private DatabaseReference dbProfCourses = FirebaseDatabase.getInstance().getReference("courses");
    DatabaseReference dbProfMidRevs;
    private CourseSpinnerAdapter courseAdapter;
    private ArrayList<Course> courselist = new ArrayList<>();
    private Professor prof;
    private EditText reviewtv;
    private RatingBar rb;
    private Spinner coursesSpinner;
    private TextView submit;
    private ProgressBar progressbar;
    private ProgressBar revprogressbar;
    private RecyclerView recyclerView;
    private CardView readAll;
    private TextView profRating;
    private RatingBar curRating;
    private ImageView profpic;
    private String timestamp;

    private long totalrevcount;
    float total;
    //variables for checking if user has reviewed selected course for prof
    private String idpewds;
    private boolean isReviewed = false;

    private User theuser;

    private ReviewAdapter rRdapter;

    public ProfessorFragment () {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_professor, container, false);

        getActivity().setTitle("Professors");
        final NavigationView navigationView = ((HomeActivity)getActivity()).getNavigationView();
        navigationView.getMenu().getItem(2).setChecked(true);

        prof = getArguments().getParcelable("prof");
        theuser = getArguments().getParcelable("theUser");
        dbProfMidRevs = FirebaseDatabase.getInstance().getReference("professors/" + prof.getId() + "/prof_reviews");
        //Course(String id, String code, String desc, String units, String overview)
        courselist.add(new Course("Choose a course...","EYY","0.0","0.0"));

        //THIS VERY LONG CODE SEGMENT LOADS THE COURSES THAT THE PROF IS ASSOCIATED TO IN THE DB
        dbProf.child(prof.getId()).child("courses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                courselist.clear();
                courselist.add(new Course("Choose a course...","EYY","0.0","0.0"));
                courselist.add(new Course("General","For General reviews.","0.0","0.0"));
                for(DataSnapshot object: dataSnapshot.getChildren()){
                    dbProfCourses.child(object.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot2) {
                            //courses.add(dataSnapshot2.child("code").getValue().toString());
                            courselist.add(dataSnapshot2.getValue(Course.class));
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }});
                }
                //THIS ANOOTHER LONG SEGMENT ADDS A SELECTED LISTENER TO THE COURSES SPINNER SO THAT IT RETRIEVES FROM THE DB IF THE USER HAS REVIEWED THE PROF ALREADY
                //FOR THE CHOSEN COURSE
                coursesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        final String courseID = courseAdapter.getItem(coursesSpinner.getSelectedItemPosition()).getId();
                        dbProf = FirebaseDatabase.getInstance().getReference("prof_reviews");
                        dbUserActivity.child(theuser.getGoogleuid()).child("prof_reviews").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                revprogressbar.setVisibility(View.VISIBLE);
                                for( final DataSnapshot object: dataSnapshot.getChildren()){
                                    String coursecheck = object.child("course").getValue().toString();
                                    String profcheck = object.child("prof").getValue().toString();
                                    if( coursecheck.equals(courseID) && profcheck.equals(prof.getId()) ){
                                        dbProf.child(object.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                idpewds = object.getKey();
                                                isReviewed = true;
                                                String contentpewds = dataSnapshot.child("reviewContent").getValue().toString();
                                                float ratingpewds = dataSnapshot.child("reviewRating").getValue(Float.class);
                                                reviewtv.setText(contentpewds);
                                                rb.setRating(ratingpewds);
                                                submit.setText("UPDATE REVIEW");
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                            }});
                                        break;
                                    }
                                    else{
                                        submit.setText("SUBMIT");
                                        reviewtv.setText("");
                                        rb.setRating(0.0f);
                                        isReviewed = false;
                                    }
                                }
                                revprogressbar.setVisibility(View.GONE);
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }});
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        //END OF VERY LONG CODE SEGMENT
        profpic = view.findViewById(R.id.prof_userpic);
        readAll = view.findViewById(R.id.review_all_button);
        revprogressbar = view.findViewById(R.id.yourrev_progressbar);
        progressbar = view.findViewById(R.id.allreviews_progressbar);
        reviewtv = view.findViewById(R.id.your_prof_review);
        rb = view.findViewById(R.id.rating_bar);
        submit = view.findViewById(R.id.submit_rating_review);

        TextView profName = view.findViewById(R.id.prof_name);
        profName.setText(prof.getName());
        TextView profEmail = view.findViewById(R.id.prof_email);
        profEmail.setText(prof.getEmail());
        TextView profPos = view.findViewById(R.id.prof_position);
        profPos.setText(prof.getPosition());
        TextView profDept = view.findViewById(R.id.prof_dept);
        profDept.setText(prof.getDepartment());
        profRating = view.findViewById(R.id.prof_rating);
        profRating.setText(prof.getRating());

        Picasso.with( getActivity() ).load( prof.getPhotourl() ).into(profpic);

        coursesSpinner = view.findViewById(R.id.courses_spinner);
        courseAdapter = new CourseSpinnerAdapter(getActivity().getApplicationContext(),
                R.layout.spinner_textview, courselist);
        coursesSpinner.setAdapter(courseAdapter);

        curRating = view.findViewById(R.id.cur_rating);
        curRating.setRating(Float.parseFloat(prof.getRating()));

        reviewList.clear();
        recyclerView = view.findViewById(R.id.prof_top_reviews_view);
        rRdapter = new ReviewAdapter(reviewList, ((HomeActivity)getActivity()));
        RecyclerView.LayoutManager hLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(hLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(rRdapter);

        updateProfRating();
        setReviewList();
        courseAdapter.notifyDataSetChanged();

        readAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("thetargetprof", prof);
                ReviewsFragment reviews = new ReviewsFragment();
                reviews.setArguments(bundle);
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_fragment, reviews).addToBackStack(null).commit();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                if(coursesSpinner.getSelectedItemPosition() != 0 && TextUtils.isEmpty(reviewtv.getText().toString()) == false ){
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmm");
                    timestamp = dateFormat.format(new Date());
                    dbProf = FirebaseDatabase.getInstance().getReference("prof_reviews");
                    final DatabaseReference dbProfEy = FirebaseDatabase.getInstance().getReference("professors/" + prof.getId() + "/prof_reviews");

                    timestamp = dateFormat.format(new Date());
                    final String courseID = courseAdapter.getItem(coursesSpinner.getSelectedItemPosition()).getId();

                    if(isReviewed == false){
                        idpewds = timestamp + dbProf.push().getKey();
                        dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
                        timestamp = dateFormat.format(new Date());
                        dbProf.child(idpewds).setValue( new Review(idpewds,theuser.getGoogleuid(),prof.getId(),timestamp, courseID,reviewtv.getText().toString(),rb.getRating() ) ).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getActivity(), "Review submitted, thanks for your feedback!", Toast.LENGTH_SHORT).show();
                                reviewtv.setText("");
                                rb.setRating(0.0f);
                                coursesSpinner.setSelection(0);
                                dbUserActivity.child(theuser.getGoogleuid()).child("prof_reviews").child(idpewds).child("course").setValue(courseID);
                                dbUserActivity.child(theuser.getGoogleuid()).child("prof_reviews").child(idpewds).child("prof").setValue(prof.getId());
                                dbProfEy.child(idpewds).setValue(true);
                                dbProfEy.child(idpewds).child("likerscount").setValue(0);

                                DatabaseReference eyeyeeyey = FirebaseDatabase.getInstance().getReference("users/" + theuser.getGoogleuid() + "/activities");
                                String actKey = eyeyeeyey.push().getKey();
                                SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMMM dd, yyyy h:mm a");
                                timestamp = dateFormat2.format(new Date());
                                eyeyeeyey.child(actKey).setValue(timestamp + " - " + theuser.getName().split(" ")[0] + " reviewed a prof: " + prof.getName());
                            }
                        });
                    }
                    else{
                        dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
                        timestamp = dateFormat.format(new Date());
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/reviewContent/", reviewtv.getText().toString());
                        childUpdates.put("/reviewDate/", timestamp);
                        childUpdates.put("/reviewRating/", rb.getRating());

                        dbProf.child(idpewds).updateChildren(childUpdates);
                        Toast.makeText(getActivity(), "Review updated!", Toast.LENGTH_SHORT).show();
                        reviewtv.setText("");
                        rb.setRating(0.0f);
                        coursesSpinner.setSelection(0);

                    }
                    setReviewList();
                    updateProfRating();
                }
                else{
                    Toast.makeText(getActivity(), "Please complete the review form.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void setReviewList() {
        rRdapter.clearItems();
        readAll.setVisibility(View.INVISIBLE);
        progressbar.setVisibility(View.VISIBLE);
        final DatabaseReference dbRevs = FirebaseDatabase.getInstance().getReference("prof_reviews");

        dbProfMidRevs.orderByChild("likerscount").limitToLast(3).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for (final DataSnapshot object: dataSnapshot.getChildren()){
                    dbRevs.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot flak) {
                            rRdapter.addItem(flak.child(object.getKey()).getValue(Review.class));
                        }
                        @Override public void onCancelled(DatabaseError databaseError) { }});

                }
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    rRdapter.reverseItems();
                    progressbar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    readAll.setVisibility(View.VISIBLE);
                    }
                }, 500);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateProfRating(){
        totalrevcount = 0;
        total = 0.0f;
        DatabaseReference pota = FirebaseDatabase.getInstance().getReference("professors/" + prof.getId() + "/prof_reviews");
        pota.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot object: dataSnapshot.getChildren() ){
                    dbProfRev.child(object.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            total += dataSnapshot.child("reviewRating").getValue(Float.class);
                            totalrevcount++;
                        }
                        @Override public void onCancelled(DatabaseError databaseError) {}});
                }

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(total != 0.0f && totalrevcount != 0){
                            profRating.setText(String.format("%.1f", total / totalrevcount));
                            curRating.setRating(total / totalrevcount);
                        }
                    }
                }, 1000);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}