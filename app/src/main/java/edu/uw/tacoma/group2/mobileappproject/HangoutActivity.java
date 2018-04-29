package edu.uw.tacoma.group2.mobileappproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import edu.uw.tacoma.group2.mobileappproject.group.GroupContent;

public class HangoutActivity extends AppCompatActivity implements
        GroupFragment.GroupTabListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hangout);
        //setting up the toolbar for the frame
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarHangout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Starts the hangout by having user select a group.
        getSupportActionBar().setTitle(R.string.sel_group_hangout);

        if (findViewById(R.id.hangout_container) != null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.hangout_container, GroupFragment.newInstance(1))
                    .commit();
        }
    }

    @Override
    public void groupTabListener(GroupContent.GroupItem item) {
        /*Activated when the user selects a group to use for their
        * Hangry hangout.
        *
        * You'll want to save the group information to be used
        * in the rest of the steps later on.*/
    }
}
