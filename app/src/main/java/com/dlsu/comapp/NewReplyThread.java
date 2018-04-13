package com.dlsu.comapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

public class NewReplyThread extends AppCompatActivity {
    private EditText newReply;
    private ForumThread thethread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reply_thread);


        Intent i = getIntent();
        String receiveaction = i.getStringExtra("leaction");

        Toolbar actionBarToolBar = (Toolbar) findViewById(R.id.newReply_toolbar);
        actionBarToolBar.inflateMenu(R.menu.newthread_items);

        if(receiveaction.equals("newreply")){
            setTitle("Reply");
            thethread = i.getParcelableExtra("thethread");

            newReply = (EditText)findViewById(R.id.newReply_content);

            actionBarToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    boolean toReturn = false;
                    int id = item.getItemId();

                    //noinspection SimplifiableIfStatement
                    if (id == R.id.newThread_action_submit) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("newreplycontent",newReply.getText().toString());
                        returnIntent.putExtra("threadrepid",thethread.getId());
                        returnIntent.putExtra("threaditself",thethread);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                        toReturn = true;
                    }
                    return toReturn;
                }
            });
        }
        else if(receiveaction.equals("editreply")){
            setTitle("Edit Reply");
            final String replyID = i.getStringExtra("replyID");
            String existingContent = i.getStringExtra("existingcontent");

            newReply = (EditText)findViewById(R.id.newReply_content);
            newReply.setText(existingContent);

            actionBarToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    boolean toReturn = false;
                    int id = item.getItemId();

                    //noinspection SimplifiableIfStatement
                    if (id == R.id.newThread_action_submit) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("newreplycontent",newReply.getText().toString());
                        returnIntent.putExtra("replyID",replyID);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                        toReturn = true;
                    }
                    return toReturn;
                }
            });
        }
        else if(receiveaction.equals("editrevcomment")){
            setTitle("Edit Comment");
            final String commentID = i.getStringExtra("commentID");
            String existingContent = i.getStringExtra("existingcontent");

            newReply = (EditText)findViewById(R.id.newReply_content);
            newReply.setText(existingContent);

            actionBarToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    boolean toReturn = false;
                    int id = item.getItemId();

                    //noinspection SimplifiableIfStatement
                    if (id == R.id.newThread_action_submit) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("newcommentcontent",newReply.getText().toString());
                        returnIntent.putExtra("commentID",commentID);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                        toReturn = true;
                    }
                    return toReturn;
                }
            });
        }

    }
}
