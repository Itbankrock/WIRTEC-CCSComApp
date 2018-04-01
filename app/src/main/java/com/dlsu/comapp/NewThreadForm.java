package com.dlsu.comapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NewThreadForm extends AppCompatActivity {

    private EditText newThreadTitle,newThreadContent;
    private long threadnumber;
    private String receiveAction;
    private Intent i;
    private ForumThread thethread;
    private ThreadPost thepost;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_thread_form);

        newThreadTitle = (EditText) findViewById(R.id.newThread_Title);
        newThreadContent = (EditText) findViewById(R.id.newThread_content);

        i = getIntent();
        receiveAction = i.getStringExtra("sendAction");

        Toolbar actionBarToolBar = (Toolbar) findViewById(R.id.newThread_toolbar);
        actionBarToolBar.inflateMenu(R.menu.newthread_items);

        if(receiveAction.equals("newThread")){
            setTitle("New Thread");
        }
        else if(receiveAction.equals("editThread")){
            setTitle("Edit Thread");
            String threadID = i.getStringExtra("threadID");
            thepost = i.getParcelableExtra("thepost");

            progressDialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
            progressDialog.setMessage("Fetching data..."); // Setting Message
            progressDialog.setTitle("Edit thread"); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.setCancelable(false);
            progressDialog.show();

            DatabaseReference dbThreads = FirebaseDatabase.getInstance().getReference("threads/" + thepost.getPostID());
            dbThreads.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    thethread = dataSnapshot.getValue(ForumThread.class);
                    progressDialog.cancel();
                    newThreadContent.setText(thepost.getContent());
                    newThreadTitle.setText(thethread.getTitle());
                }
                @Override public void onCancelled(DatabaseError databaseError) {
                    progressDialog.cancel();
                    Toast.makeText(getApplicationContext(),"There was an error.",Toast.LENGTH_SHORT).show();}});
        }

        actionBarToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                boolean toReturn = false;
                int id = item.getItemId();

                //noinspection SimplifiableIfStatement
                if (id == R.id.newThread_action_submit) {
                    if(receiveAction.equals("newThread")){
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("newthreadtitle", newThreadTitle.getText().toString());
                        returnIntent.putExtra("newthreadcontent",newThreadContent.getText().toString());
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();

                        toReturn = true;
                    }
                    else if(receiveAction.equals("editThread")){

                        //SET IN DB BEFORE FINISHING ACTIVITY PUT PROGRESS BAR
                        progressDialog.setMessage("Saving data..."); // Setting Message
                        progressDialog.setTitle("Edit thread"); // Setting Title
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        final DatabaseReference dbThread = FirebaseDatabase.getInstance().getReference("threads/" + thethread.getId());
                        DatabaseReference dbThreadReplies = FirebaseDatabase.getInstance().getReference("thread_replies/" + thepost.getId());

                        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy h:mm a");
                        String timestamp = dateFormat.format(new Date());

                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/content/", newThreadContent.getText().toString());
                        childUpdates.put("/lastupdated/", timestamp);
                        dbThreadReplies.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Map<String, Object> childUpdates = new HashMap<>();
                                    childUpdates.put("/content/", newThreadContent.getText().toString().substring(0, Math.min(newThreadContent.getText().toString().length(), 20)) + "..."  );
                                    childUpdates.put("/title/", newThreadTitle.getText().toString());
                                    dbThread.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                progressDialog.cancel();
                                            }
                                            else{
                                                Toast.makeText(getApplicationContext(),"There was an error.",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"There was an error.",Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("newTitleEy",newThreadTitle.getText().toString());
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                        toReturn = true;
                    }

                }
                return toReturn;
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }
}
