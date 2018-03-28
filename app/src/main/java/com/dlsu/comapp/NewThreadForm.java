package com.dlsu.comapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class NewThreadForm extends AppCompatActivity {

    private EditText newThreadTitle,newThreadContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_thread_form);

        newThreadTitle = (EditText) findViewById(R.id.newThread_Title);
        newThreadContent = (EditText) findViewById(R.id.newThread_content);

        setTitle("New Thread");

        Toolbar actionBarToolBar = (Toolbar) findViewById(R.id.newThread_toolbar);
        actionBarToolBar.inflateMenu(R.menu.newthread_items);
        actionBarToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                boolean toReturn = false;
                int id = item.getItemId();

                //noinspection SimplifiableIfStatement
                if (id == R.id.newThread_action_submit) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("newthreadtitle", newThreadTitle.getText().toString());
                    returnIntent.putExtra("newthreadcontent",newThreadContent.getText().toString());
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();

                    toReturn = true;
                }
                return toReturn;
            }
        });

    }
}
