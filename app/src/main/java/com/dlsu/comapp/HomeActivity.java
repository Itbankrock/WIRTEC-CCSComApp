package com.dlsu.comapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.util.Freezable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager;
    private int navItem;

    private TextView navusername;
    private TextView navemail;
    private ImageView navpic;
    private User theuser;
    private Toolbar toolbar;
    private threadFragment thread;
    private FullReviewFragment fullReview;
    private ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser fbCurrUser;
    DatabaseReference dbUsers;
    private final static int NEW_THREAD_CODE = 69;
    private final static int NEW_REPLY_CODE = 70;
    private final static int EDIT_REPLY_CODE = 71;
    private final static int EDIT_COMMENT_CODE = 80;
    private final static int EDIT_THREAD_CODE = 68;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbUsers = FirebaseDatabase.getInstance().getReference("users");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    Intent i = new Intent(HomeActivity.this,Login.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }
            }
        };
        initialize();

        //INITIALIZE THE USER IN DB
        progressDialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Fetching data..."); // Setting Message
        progressDialog.setTitle("CCS ComApp"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);
        progressDialog.show();
        dbUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    //User already exists
                    if( dataSnapshot.hasChild(fbCurrUser.getUid()) ) {
                        theuser = dataSnapshot.child(fbCurrUser.getUid()).getValue(User.class);
                        dbUsers.removeEventListener(this);
                        updateUserSession();
                    }
                    //If user is new
                    else{
                        theuser = new User(fbCurrUser.getUid(),fbCurrUser.getDisplayName(),fbCurrUser.getEmail(),fbCurrUser.getPhotoUrl().toString(),"", "", "");
                        dbUsers.child(fbCurrUser.getUid()).setValue(theuser);
                        dbUsers.removeEventListener(this);
                        updateUserSession();
                    }

                Picasso.with(getApplicationContext()).load(theuser.getPhotourl()).into(navpic);
                navusername.setText(theuser.getName());
                navemail.setText(theuser.getEmail());

                Bundle bundle = new Bundle();
                bundle.putParcelable("theuser", theuser);
                HomeFragment home = new HomeFragment();
                home.setArguments(bundle);
                fragmentManager = getSupportFragmentManager();
                resetStack();
                fragmentManager.beginTransaction().replace(R.id.main_fragment, home, "A").commit();
                initializeDrawer();

                String notif = getIntent().getStringExtra("notifType");
                if (notif != null) {
                    if(notif.equals("comment")){
                        viewFullReview(getIntent().getStringExtra("assocID"),getIntent().getStringExtra("makerID"));
                        progressDialog.cancel();
                    }
                    else if(notif.equals("reply")){
                        DatabaseReference dbThread = FirebaseDatabase.getInstance().getReference("threads/" + getIntent().getStringExtra("assocID"));
                        dbThread.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                progressDialog.cancel();
                                viewThread(dataSnapshot.getValue(ForumThread.class));
                            }
                            @Override public void onCancelled(DatabaseError databaseError) {progressDialog.cancel();}});

                    }

                }
                else{
                    progressDialog.cancel();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.cancel();
                Toast.makeText(getApplicationContext(),"Cannot download data from the internet.",Toast.LENGTH_SHORT).show();
            }
        });//INITIALIZE THE USER IN DB

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String notifType = intent.getStringExtra("notifType");

            if(notifType.equals("comment")){
                String makerID = intent.getStringExtra("makerID");
                String assocID = intent.getStringExtra("assocID");
                viewFullReview(assocID,makerID);
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(mAuthListener);
        fbCurrUser = mAuth.getCurrentUser();

        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver),
                new IntentFilter("MyData")
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    public void initialize() {
        navItem = 0;

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        DatabaseReference dbAdmins = FirebaseDatabase.getInstance().getReference("admins");
        dbAdmins.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if( dataSnapshot.hasChild(fbCurrUser.getUid()) ){
                    Menu nav_Menu = navigationView.getMenu();
                    nav_Menu.findItem(R.id.nav_admin_add_prof).setVisible(true);
                    nav_Menu.findItem(R.id.nav_admin_add_course).setVisible(true);
                    nav_Menu.findItem(R.id.nav_admin_associate).setVisible(true);
                }
            }
            @Override public void onCancelled(DatabaseError databaseError) {}});

        View sidebarView = navigationView.getHeaderView(0);

        navusername = (TextView) sidebarView.findViewById(R.id.nav_username);
        navemail = (TextView) sidebarView.findViewById(R.id.nav_email);
        navpic = (ImageView) sidebarView.findViewById(R.id.nav_userpic);

    }

    public void initializeDrawer(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = getNavigationView();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.notif && navItem != 6) {
            navItem = 6;
            NotificationFragment notif = new NotificationFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, notif).addToBackStack(null).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        resetStack();

        if (id == R.id.nav_home && navItem != 0) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("theuser", theuser);
            HomeFragment home = new HomeFragment();
            home.setArguments(bundle);
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, home, "A").commit();
        }

        else if (id == R.id.nav_courses && navItem != 1) {
            navItem = 1;
            Bundle bundle = new Bundle();
            bundle.putParcelable("theuser", theuser);
            CourseProfFragment course = new CourseProfFragment();
            course.setArguments(bundle);
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, course).addToBackStack(null).commit();
        }

        else if (id == R.id.nav_profs && navItem != 2) {
            navItem = 2;
            Bundle bundle = new Bundle();
            bundle.putParcelable("theuser", theuser);
            CourseProfFragment prof = new CourseProfFragment();
            prof.setArguments(bundle);
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, prof).addToBackStack(null).commit();

        }

        else if (id == R.id.nav_forum && navItem != 3) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("theuser", theuser);
            ForumThreadListFragment forums = new ForumThreadListFragment();
            forums.setArguments(bundle);
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, forums).addToBackStack(null).commit();
            navItem = 3;
        }

        else if (id == R.id.nav_help && navItem != 4) {
            navItem = 4;
            HelpFragment help = new HelpFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, help).addToBackStack(null).commit();
        }

        else if (id == R.id.nav_admin_associate && navItem != 5) {
            navItem = 5;
            AdminAssociateCourseProfFragment adminassociate = new AdminAssociateCourseProfFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, adminassociate).addToBackStack(null).commit();
        }

        else if (id == R.id.nav_admin_add_course && navItem != 6) {
            navItem = 6;
            AdminAddCourseFragment addCourse = new AdminAddCourseFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, addCourse).addToBackStack(null).commit();
        }

        else if (id == R.id.nav_admin_add_prof && navItem != 7) {
            navItem = 7;
            AdminAddProfFragment addProf = new AdminAddProfFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, addProf).addToBackStack(null).commit();
        }

        else if (id == R.id.nav_account && navItem != 8) {
            navItem = 8;
            Bundle bundle = new Bundle();
            bundle.putParcelable("theuser", theuser);
            AccountFragment account = new AccountFragment();
            account.setArguments(bundle);
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, account).addToBackStack(null).commit();
        }

        else if (id == R.id.nav_feedback && navItem != 9) {
            navItem = 9;
            FeedbackFragment feedback = new FeedbackFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, feedback).addToBackStack(null).commit();
        }

        else if (id == R.id.nav_logout) {
            DatabaseReference dblogout = FirebaseDatabase.getInstance().getReference("users").child(theuser.getGoogleuid());
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/token_id/", "logged out");
            dblogout.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    mAuth.signOut();
                }
            });
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public NavigationView getNavigationView() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        return navigationView;
    }

    public int getNavItem() {
        return navItem;
    }

    public void setNavItem(int navItem) {
        this.navItem = navItem;
    }

    public void viewProfessor(Professor theprof) {
        //fragmentManager = getSupportFragmentManager();
        //CourseProfFragment fragment = (CourseProfFragment)fragmentManager.findFragmentById(R.id.main_fragment);
        //fragment.viewProfessor(position);
        Bundle bundle = new Bundle();
        bundle.putParcelable("prof", theprof);
        bundle.putParcelable("theUser", theuser );
        ProfessorFragment prof = new ProfessorFragment();
        prof.setArguments(bundle);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_fragment, prof).addToBackStack(null).commit();
    }

    public void viewCourse(Course thecourse) {
        fragmentManager = getSupportFragmentManager();
        CourseProfFragment fragment = (CourseProfFragment)fragmentManager.findFragmentById(R.id.main_fragment);
        fragment.viewCourse(thecourse);
    }

    public void viewThread(ForumThread object){
        //id, title
        Bundle bundle = new Bundle();
        bundle.putParcelable("theuser", theuser);
        bundle.putParcelable("targetObject",object);
        thread = new threadFragment();
        thread.setArguments(bundle);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_fragment, thread).addToBackStack(null).commit();
        //setTitle(object.getTitle());
    }



    public void viewNote(Note thenote) {
        fragmentManager = getSupportFragmentManager();
        CourseFragment fragment = (CourseFragment)fragmentManager.findFragmentById(R.id.main_fragment);
        fragment.viewNote(thenote);
    }

    public void viewFullReview(String reviewID, String reviewMakerID) {
        Bundle bundle = new Bundle();
        bundle.putString("reviewID", reviewID);
        bundle.putString("makerID",reviewMakerID);
        fullReview = new FullReviewFragment();
        fullReview.setArguments(bundle);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_fragment, fullReview).addToBackStack(null).commit();
    }

    public void updateUserSession(){
        dbUsers = FirebaseDatabase.getInstance().getReference("users").child(theuser.getGoogleuid());
        dbUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                theuser = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/token_id/", FirebaseInstanceId.getInstance().getToken());
        dbUsers.updateChildren(childUpdates);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_THREAD_CODE) {
            if(resultCode == Activity.RESULT_OK){
                progressDialog.setMessage("Creating thread..."); // Setting Message
                progressDialog.setTitle("New thread"); // Setting Title
                progressDialog.setCancelable(false);
                progressDialog.show();
                final String newtitle = data.getStringExtra("newthreadtitle");
                String newcontent = data.getStringExtra("newthreadcontent");

                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy h:mm a");
                String timestamp = dateFormat.format(new Date());


                DatabaseReference dbtest = FirebaseDatabase.getInstance().getReference("threads");
                DatabaseReference dbtest2 = FirebaseDatabase.getInstance().getReference("thread_replies");

                final String idthread = dbtest.push().getKey();
                final ForumThread newThread = new ForumThread(  idthread,newtitle,theuser.getGoogleuid(),timestamp,1,true, newcontent.substring(0, Math.min(newcontent.length(), 20)) + "..."  );
                dbtest.child(idthread).setValue(newThread);

                DatabaseReference dbUserActs = FirebaseDatabase.getInstance().getReference("users/" + fbCurrUser.getUid() + "/activities");
                String actKey = dbUserActs.push().getKey();
                dbUserActs.child(actKey).setValue(timestamp + " - " + fbCurrUser.getDisplayName().split(" ")[0] + " created a new thread in the forums.");

                String idfirstpost = dbtest.child(idthread).child("replies").push().getKey();
                ThreadPost post = new ThreadPost(idfirstpost,theuser.getGoogleuid(),idthread,newcontent,timestamp,timestamp,true);

                dbtest.child(idthread).child("replies").child(idfirstpost).setValue(true);
                dbtest2.child(idfirstpost).setValue(post, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        viewThread(newThread);
                        progressDialog.cancel();
                    }
                });

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }

        else if(requestCode == NEW_REPLY_CODE){
            if(resultCode == Activity.RESULT_OK){
                progressDialog.setMessage("Submitting reply..."); // Setting Message
                progressDialog.setTitle("New reply"); // Setting Title
                progressDialog.setCancelable(false);
                progressDialog.show();
                String newreply = data.getStringExtra("newreplycontent");
                String threadid = data.getStringExtra("threadrepid");
                ForumThread thethread = data.getParcelableExtra("threaditself");

                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy h:mm a");
                String timestamp = dateFormat.format(new Date());

                DatabaseReference dbtest = FirebaseDatabase.getInstance().getReference("threads/" + threadid + "/replies");
                DatabaseReference dbtest2 = FirebaseDatabase.getInstance().getReference("thread_replies");
				DatabaseReference dbtest3 = FirebaseDatabase.getInstance().getReference("users/" + thethread.getUserID());

                final String idreply = dbtest2.push().getKey();
                dbtest.child(idreply).setValue(true);
                ThreadPost post = new ThreadPost(idreply,theuser.getGoogleuid(),threadid,newreply,timestamp, timestamp, true);
                dbtest2.child(idreply).setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        thread.preparePosts();
                        progressDialog.cancel();
                        Toast.makeText(getApplicationContext(),"Your reply has been added.",Toast.LENGTH_SHORT).show();

                    }
                });
				
				if(!fbCurrUser.getUid().equals(thethread.getUserID())){
                String notifKey = dbtest3.child("notifications").push().getKey();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("from", fbCurrUser.getUid());
                childUpdates.put("message", fbCurrUser.getDisplayName().split(" ")[0] + " replied to your thread " + thethread.getTitle());
                childUpdates.put("messagelong", newreply);
                childUpdates.put("notificationType", "reply");
                childUpdates.put("associatedID", threadid);
                dbtest3.child("notifications").child(notifKey).setValue(childUpdates);

                DatabaseReference dbUserActs = FirebaseDatabase.getInstance().getReference("users/" + fbCurrUser.getUid() + "/activities");
                String actKey = dbUserActs.push().getKey();
                dbUserActs.child(actKey).setValue(timestamp + " - " + fbCurrUser.getDisplayName().split(" ")[0] + " replied to a thread: " + thethread.getTitle());
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }

        else if(requestCode == EDIT_REPLY_CODE){
            if(resultCode == Activity.RESULT_OK){
                progressDialog.setMessage("Saving changes..."); // Setting Message
                progressDialog.setTitle("Edit reply"); // Setting Title
                progressDialog.setCancelable(false);
                progressDialog.show();
                String newreply = data.getStringExtra("newreplycontent");
                String replyID = data.getStringExtra("replyID");

                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy h:mm a");
                String timestamp = dateFormat.format(new Date());

                DatabaseReference dbtest2 = FirebaseDatabase.getInstance().getReference("thread_replies/" + replyID);

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/content/", newreply);
                childUpdates.put("/lastupdated/", timestamp);
                dbtest2.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.cancel();
                        Toast.makeText(getApplicationContext(),"Changes saved.",Toast.LENGTH_SHORT).show();
                    }
                });
                thread.preparePosts();

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }

        else if(requestCode == EDIT_COMMENT_CODE){
            if(resultCode == Activity.RESULT_OK){
                progressDialog.setMessage("Saving changes..."); // Setting Message
                progressDialog.setTitle("Edit comment"); // Setting Title
                progressDialog.setCancelable(false);
                progressDialog.show();
                String newcomment = data.getStringExtra("newcommentcontent");
                String reviewID = data.getStringExtra("commentID");

                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy h:mm a");
                String timestamp = dateFormat.format(new Date());

                DatabaseReference dbtest2 = FirebaseDatabase.getInstance().getReference("review_comments/" + reviewID);

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/content/", newcomment);
                childUpdates.put("/lastupdated/", timestamp);

                dbtest2.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.cancel();
                        Toast.makeText(getApplicationContext(),"Changes saved.",Toast.LENGTH_SHORT).show();
                    }
                });
                fullReview.setReviewCommentList();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
        else if(requestCode == EDIT_THREAD_CODE){
            if(resultCode == Activity.RESULT_OK){
                thread.preparePosts();
                setTitle(data.getStringExtra("newTitleEy"));
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    public void resetStack() {
        int count = fragmentManager.getBackStackEntryCount();
        for(int i = 0; i < count; ++i) {
            fragmentManager.popBackStackImmediate();
        }
    }
}
