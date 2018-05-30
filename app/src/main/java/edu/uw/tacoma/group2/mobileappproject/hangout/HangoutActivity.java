package edu.uw.tacoma.group2.mobileappproject.hangout;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import edu.uw.tacoma.group2.mobileappproject.R;
import edu.uw.tacoma.group2.mobileappproject.group.GroupContent;
import edu.uw.tacoma.group2.mobileappproject.group.GroupFragment;
import edu.uw.tacoma.group2.mobileappproject.order.OrderMenu.FoodContent;
import edu.uw.tacoma.group2.mobileappproject.order.OrderMenu.OrderMenuFragment;
import edu.uw.tacoma.group2.mobileappproject.order.OrdersContent;
import edu.uw.tacoma.group2.mobileappproject.order.OrdersFragment;
import edu.uw.tacoma.group2.mobileappproject.user.UserContent;

/**
 * This class is used for the hangout activity portion of the Hangry Foodie Hangout application.
 * In this class the user will be able to create new hangouts, view their current hangout, and also
 * have access to the ordering activity where they can place an order.
 * @author Harlan Stewart
 * @version 1.5
 */
public class HangoutActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GroupFragment.GroupTabListener,
        OrdersFragment.OrdersTabListener,
        HangoutFragment.HangoutInteractionListener,
        OrderMenuFragment.onOrderMenuListener{
    private static final String TAG_THREE = "MEMBER INSERT === ";
    private static final String TAG_FOUR = "MEMBER TASK === ";
    private static final String ADD_HANGOUT_URL =
            "http://hangryfoodiehangout.000webhostapp.com/hangout.php?cmd=hangout";
    private static final String ADD_HANGOUT_MEMBER_URL =
            "http://hangryfoodiehangout.000webhostapp.com/hangout.php?cmd=members";
    private static final String GET_MEMBERS_URL =
            "http://stephd27.000webhostapp.com/list.php?cmd=members&group=";
    private HashMap<String, String> mMemberMap;
    private Date mDate;
    FloatingActionButton fab;


    /**
     * This method creates the layout for the hangout activity and will initially call the fragment
     * manager to display the list of current hangouts the user is a part of. Also this method sets
     * local variables for the activities associated views and implements the function of the floating
     * action buttons onClick listener which will display a list of groups to select for a new hangout
     * when it is pressed.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hangout);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.current_hangouts);
        setSupportActionBar(toolbar);
        mMemberMap = new HashMap<>();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.content_frame, new HangoutFragment()).commit();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.content_frame, new GroupFragment())
                        .commit();
                toolbar.setTitle("Choose a group");
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hangout, menu);
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

    /**
     * Handles what happens when the user clicks on a particular menu item from the
     * left side navigation menu.
     * @param item the item chosen.
     * @return indicates whether an item was selected.
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fm = getSupportFragmentManager();
        if (id == R.id.nav_hangout) {
            fm.beginTransaction()
                    .replace(R.id.content_frame, new HangoutFragment())
                    .commit();
            fab.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_oldOrders) {
            fm.beginTransaction()
                    .replace(R.id.content_frame, new OrdersFragment())
                    .commit();
                fab.setVisibility(View.GONE);
        } else if (id == R.id.nav_share) {

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void orderTabListener(OrdersContent item) {
    }

    /**
     * Once the user has chosen to create a new hangout by hitting the floating action button
     * a list of groups will be displayed and the user can choose one of them to start a new hangout.
     * This method calls other methods that will handle the async tasks for creating a hangout.
     * @param item the group item the user has chosen.
     */
    @Override
    public void groupTabListener(GroupContent item)  {
            getMembersFromDB(item);
            insertIntoHangoutTable(item);
        Toast.makeText(this, "Hangout added! Pull to Refresh Hangout.", Toast.LENGTH_SHORT).show();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.content_frame, new HangoutFragment()).commit();
    }

    /**
     * This method calls the methods that will build the string urls for Async tasks. Then it will
     * create these tasks and pass the created string urls when the created task is executed.
     * Inserts the people who are in the group the user chose when creating a hangout into the
     * hangout_mems table as new members of the created hangout.
     */
    private void insertMembersIntoDB(){
        List<String> groupMems = new ArrayList<>();
        for(String key : mMemberMap.keySet()){
            String friendName = mMemberMap.get(key) ;
            String memUrl = buildHangoutMembers(mDate, friendName, key);
            groupMems.add(memUrl);
        }
        CreateHangoutMembersTask taskUser = new CreateHangoutMembersTask();
        String userInsert = buildHangoutMembers(mDate, UserContent.sUserID, UserContent.sUserName);
        groupMems.add(userInsert);
        Log.e("INSERTING MEMBERS: ",groupMems.toString());
        taskUser.execute(groupMems.toArray(new String[0]));
    }

    /**
     * This method creates a string url and async task that will use the created url for
     * inserts to a new hangout into the data base table. The unique id used to identify
     * a created hangout is the current system time and the group count is set to the number of
     * people in the group the user has chosen when starting a hangout.
     * @param group the users group choice.
     */
    private void insertIntoHangoutTable(GroupContent group){
        long sysTime = System.currentTimeMillis();
        mDate = new Date(sysTime);
        CreateHangoutTask taskMem = new CreateHangoutTask();
        String mGroupCount = group.getGroupCount();
        String urlHangout = buildHangoutURL(mGroupCount, mDate);
        taskMem.execute(urlHangout);
    }

    /**
     * This method creates the string url that can be used for getting the information from the database
     * for members of the group the user has chosen for starting a new hangout.
     * @param group the users group choice.
     */
    private void getMembersFromDB( GroupContent group){
        GetMemberInfoTask memInfoTask = new GetMemberInfoTask();
        String membersURL = GET_MEMBERS_URL + group.getGroupID();
        memInfoTask.execute(membersURL);
    }

    /**
     * Method used for creating the string that will be used as a command to send to the database
     * when a user has created a new hangout.
     * @param memCount the number of members in the group the user chose.
     * @param date the current system date/time for primary key.
     * @return the string url for creating a new hangout.
     */
    private String buildHangoutURL(String memCount, Date date){
        StringBuilder sb = new StringBuilder(ADD_HANGOUT_URL);
        sb.append("&hid=").append(date.toString());
        if(!(UserContent.sUserRestaurant == null)){
            String cleanString = UserContent.sUserRestaurant.replace("'", "\\\'");
            cleanString = cleanString.replace("\"", "\\\\\"");
            sb.append("&rest_name=").append(cleanString);
        }else {
            sb.append("&rest_name=").append("DEFAULT_RESTAURANT");
        }
        sb.append("&num_members=").append(memCount);
        sb.append("&closed_open=").append("0");
        //Log.e(TAG_TWO, sb.toString());
        return  sb.toString();
    }

    /**
     * Method used for creating the string that will be used as a command to send to the database
     * to insert new values into the hangout_mems table.
     * @param date the current system date/time for primary key.
     * @param fName the name of a person in the hangout.
     * @param fID the friend id of a person in the hangout.
     * @return the string url that will insert members of the chosen group into the hangout_mems table.
     */
    private String buildHangoutMembers(Date date, String fName, String fID ){
        StringBuilder sb = new StringBuilder(ADD_HANGOUT_MEMBER_URL);
            sb.append("&hid=").append(date.toString());
            sb.append("&fid=").append(fName);
            sb.append("&ordered=").append("0");
            sb.append("&friend_name=").append(fID);
            sb.append("&price=").append("0");
        Log.e(TAG_THREE, sb.toString());
        return sb.toString();
    }

    @Override
    public void hangoutListener(Hangout item) {
    }

    @Override
    public void onOrderMenuInteraction(FoodContent.FoodItem item) {
    }

    /**
     * Private inner class that represents an AyncTask that is used for getting information based
     * on the users chosen group. After the background work has been completed and after execution of this
     * task if the task was successful the values of the members will be placed into the HangoutActivities HashMap.
     */
    private class GetMemberInfoTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s;
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                        //Log.e(TAG_FOUR, response);
                    }
                } catch (Exception e) {
                    response = "Unable to add course, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /**
         * Handles response from web service, populates the user's groups.
         * @param result Response from the web service
         */
        @Override
        protected void onPostExecute(String result) {
            if (result.startsWith("Unable to")) {
                Log.e(TAG_FOUR, result);
                return;
            }
            try {
                mMemberMap = GroupContent.parseGroupMembers(result);
            }catch (JSONException e) {
                Log.e(TAG_FOUR, e.getMessage());
                return;
            }
            if (!mMemberMap.isEmpty()) {
                insertMembersIntoDB();
            }
        }
    }
}