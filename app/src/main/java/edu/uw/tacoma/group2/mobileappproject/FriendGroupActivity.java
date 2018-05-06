package edu.uw.tacoma.group2.mobileappproject;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import edu.uw.tacoma.group2.mobileappproject.friend.FriendContent;
import edu.uw.tacoma.group2.mobileappproject.friend.FriendFragment;
import edu.uw.tacoma.group2.mobileappproject.group.AddGroup;
import edu.uw.tacoma.group2.mobileappproject.group.GroupContent;
import edu.uw.tacoma.group2.mobileappproject.group.GroupFragment;
import edu.uw.tacoma.group2.mobileappproject.group.GroupMemberFragment;

public class FriendGroupActivity extends AppCompatActivity implements
        FriendFragment.FriendTabListener,
        GroupFragment.GroupTabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private Dialog friendPopUp;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_group);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the 2
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tabLayout.getSelectedTabPosition() == 0) {
                    Snackbar.make(view, "Replace with Add Friend", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
//                    Snackbar.make(view, "Replace with Create Group", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
                }
            }
        });

        createFriendPopUp();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friend_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void friendTabListener(FriendContent item) {
        TextView popUpName = friendPopUp.findViewById(R.id.friend_name);
        TextView popUpEmail = friendPopUp.findViewById(R.id.friend_email);
        //ProfilePictureView popUpIcon = friendPopUp.findViewById(R.id.friend_profile_pic);
        popUpName.setText(item.getFrenName());
        popUpEmail.setText(item.getFrenEmail());
        //popUpIcon.setProfileId(item.getFrenID());
        friendPopUp.show();
    }

    @Override
    public void groupTabListener(GroupContent item) {
        DialogFragment memberDetail = GroupMemberFragment.newInstance(item.getGroupID());
        memberDetail.show(getFragmentManager(), "Mem detail");


    }

    private void createFriendPopUp() {
        friendPopUp = new Dialog(this);
        friendPopUp.setContentView(R.layout.friend_group_details_popup);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages. In this case, Friends or Groups.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return FriendFragment.newInstance(1);
                case 1:
                    return GroupFragment.newInstance(1);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }

}
