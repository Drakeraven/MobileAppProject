package edu.uw.tacoma.group2.mobileappproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.net.URL;
import java.net.URLEncoder;

import edu.uw.tacoma.group2.mobileappproject.R;
import edu.uw.tacoma.group2.mobileappproject.friend.FriendContent;
import edu.uw.tacoma.group2.mobileappproject.hangout.CreateHangoutTask;
import edu.uw.tacoma.group2.mobileappproject.user.AddFriendFragment;
import edu.uw.tacoma.group2.mobileappproject.user.UserContent;

public class AddFriendActivity extends AppCompatActivity implements AddFriendFragment.OnListFragmentInteractionListener {

    private final static String ADD_FRIEND
            = "http://stephd27.000webhostapp.com/addFriend.php?id=";
    private static final String TAG = "Add_Friend";

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
                .replace(R.id.addfriend_fragment_container, addFriendFragment);
        // Commit the transaction
        transaction.commit();

    }


    /**
     * Builds URL for php execution - add's a user to the current user's friend list
     * @param user
     */
    public void addFriend(FriendContent user) {

        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(ADD_FRIEND);
            stringBuilder.append(URLEncoder.encode(UserContent.sUserID, "UTF-8"));
            stringBuilder.append("&fid=");
            stringBuilder.append(URLEncoder.encode(user.getFrenID(), "UTF-8"));
            stringBuilder.append("&fname=");
            stringBuilder.append(URLEncoder.encode(user.getFrenName(), "UTF-8"));
            stringBuilder.append("&femail=");
            stringBuilder.append(URLEncoder.encode(user.getFrenEmail()));

            Log.i(TAG, "Url is " +stringBuilder.toString());
            CreateHangoutTask addFriendAsyncTask = new CreateHangoutTask();
            addFriendAsyncTask.execute(stringBuilder.toString());
            Toast toast = Toast.makeText(this, "Friend added!", Toast.LENGTH_SHORT);
            toast.show();

            Intent splashReturn = new Intent(this, SplashActivity.class);
            splashReturn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(splashReturn);
        }
        catch (Exception e) {
            Toast.makeText(this, "Couldn't register, Something wrong with the URL" + e.getMessage(),
                    Toast.LENGTH_SHORT). show();
        }
    }

    @Override
    public void onListFragmentInteraction(FriendContent item) {
        //TODO: When click on the user, show a pop up
        addFriend(item);
        //TODO: On pop up interaction, add user to friends list
    }
}
