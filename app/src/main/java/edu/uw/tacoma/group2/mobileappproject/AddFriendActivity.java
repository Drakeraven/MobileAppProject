package edu.uw.tacoma.group2.mobileappproject;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import edu.uw.tacoma.group2.mobileappproject.R;
import edu.uw.tacoma.group2.mobileappproject.friend.FriendContent;
import edu.uw.tacoma.group2.mobileappproject.user.AddFriendFragment;

public class AddFriendActivity extends AppCompatActivity implements AddFriendFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //TODO: Show list of users in the application
        AddFriendFragment addFriendFragment = new AddFriendFragment();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.addfriend_fragment_container, addFriendFragment)
                .addToBackStack(null);
        // Commit the transaction
        transaction.commit();

    }

    @Override
    public void onListFragmentInteraction(FriendContent item) {
        //TODO: When click on the user, show a pop up

        //TODO: On pop up interaction, add user to friends list
    }
}
