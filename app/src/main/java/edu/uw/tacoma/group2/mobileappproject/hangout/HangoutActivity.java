package edu.uw.tacoma.group2.mobileappproject.hangout;

import android.support.v4.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

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
import java.util.concurrent.CountDownLatch;

import edu.uw.tacoma.group2.mobileappproject.R;
import edu.uw.tacoma.group2.mobileappproject.hangout.CreateHangoutTask;
import edu.uw.tacoma.group2.mobileappproject.order.OrdersContent;
import edu.uw.tacoma.group2.mobileappproject.order.OrdersFragment;
import edu.uw.tacoma.group2.mobileappproject.group.GroupContent;
import edu.uw.tacoma.group2.mobileappproject.group.GroupFragment;
import edu.uw.tacoma.group2.mobileappproject.user.UserContent;


public class HangoutActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GroupFragment.GroupTabListener,
        OrdersFragment.OrdersTabListener,
        HangoutFragment.HangoutInteractionListener{
    private static final String TAG_ONE = "GROUP ID URL === ";
    private static final String TAG_TWO = "HANGOUT INSERT === ";
    private static final String TAG_THREE = "MEMBER INSERT === ";
    private static final String TAG_FOUR = "MEMBER TASK === ";
    private static final String ADD_HANGOUT_URL =
            "http://hangryfoodiehangout.000webhostapp.com/hangout.php?cmd=hangout";
    private static final String ADD_HANGOUT_MEMBER_URL =
            "http://hangryfoodiehangout.000webhostapp.com/hangout.php?cmd=members";
    private static final String GET_MEMBERS_URL =
            "http://stephd27.000webhostapp.com/list.php?cmd=members&group=";
    private GroupFragment myGroupFrag;
    private HashMap<String, String> mMemberMap;
    private Date mDate;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hangout);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mMemberMap = new HashMap<>();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.content_frame, GroupFragment.newInstance(1))
                        .commit();
                fm.executePendingTransactions();
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fm = getSupportFragmentManager();

        if (id == R.id.nav_hangout) {
            fm.beginTransaction()
                    .replace(R.id.content_frame, HangoutFragment.newInstance(1))
                    .commit();
            fab.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_oldOrders) {
            fm.beginTransaction()
                    .replace(R.id.content_frame, new OrdersFragment())
                    .commit();
                fab.setVisibility(View.GONE);
        } else if (id == R.id.nav_share) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void orderTabListener(OrdersContent.OrderItem item) {

    }


    @Override
    public void groupTabListener(GroupContent item)  {
        /*Toast.makeText(getApplicationContext(), "CLICKED ON " + item.getGroupName(),
                Toast.LENGTH_LONG).show();*/
        /*String infoURL =
                "http://stephd27.000webhostapp.com/list.php?cmd=groupsuid="
                + item.getGroupID();*/
        //Log.i(TAG_ONE,ADD_HANGOUT_MEM_URL + item.getGroupID());


            getMembersFromDB(item);
            insertIntoHangoutTable(item);
            insertMembersIntoDB();





        //StringBuilder urlMem = new StringBuilder();
        //urlMem.append(buildHangoutURL(mGroupCount, mDate));
        /*if(!url.isEmpty()){
            CreateHangoutTask task = new CreateHangoutTask();
            task.execute(url);
           // Log.i(TAG_TWO,url);
        }
*/
        //Log.e(TAG_THREE, mMemberMap.toString());
        //Log.i(TAG_THREE, urlMem.toString());
        //taskMem.execute(new String[]{urlMem.toString()});

    }

    private void insertMembersIntoDB(){

        //List<String> groupMems = new ArrayList<>();
        for(String key : mMemberMap.keySet()){
            CreateHangoutMembersTask taskMembers = new CreateHangoutMembersTask();
            String friendName = mMemberMap.get(key) ;
            String friendID =key ;
            String urlMembers = buildHangoutMembers(mDate, friendName, friendID);
            //groupMems.add(urlMembers);
            taskMembers.execute(new String[]{urlMembers});
            //urlArr.add(buildHangoutMembers(mDate,friendName,friendID));

        }
        CreateHangoutMembersTask taskUser = new CreateHangoutMembersTask();
        String userInsert = buildHangoutMembers(mDate, UserContent.sUserID, UserContent.sUserName);
        //groupMems.add(userInsert);
        //Log.e(TAG_THREE,groupMems.toString());
        taskUser.execute(new String[]{userInsert});

    }

    private void insertIntoHangoutTable( GroupContent group){
        long sysTime = System.currentTimeMillis();
        mDate = new Date(sysTime);
        CreateHangoutTask taskMem = new CreateHangoutTask();
        String mGroupCount = group.getGroupCount();
        String urlHangout = buildHangoutURL(mGroupCount, mDate);
        taskMem.execute(new String[] {urlHangout});
    }

    private void getMembersFromDB( GroupContent group){
        GetMemberInfoTask memInfoTask = new GetMemberInfoTask();
        String membersURL = GET_MEMBERS_URL + group.getGroupID();
        memInfoTask.execute(new String[]{membersURL});
    }

    private String buildHangoutURL(String memCount, Date date){
        StringBuilder sb = new StringBuilder(ADD_HANGOUT_URL);
        sb.append("&hid=").append(date.toString());
        if(!(UserContent.sUserRestaurant == null)){
            sb.append("&rest_name=" + UserContent.sUserRestaurant);
        }else {
            sb.append("&rest_name=").append("DEFAULT_RESTAURANT");
        }
        sb.append("&num_members=").append(memCount);
        sb.append("&closed_open=").append("0");
        //Log.e(TAG_TWO, sb.toString());
        return  sb.toString();
    }

    private String buildHangoutMembers(Date date, String fName, String fID ){
        StringBuilder sb = new StringBuilder(ADD_HANGOUT_MEMBER_URL);
            sb.append("&hid=").append(date.toString());
            sb.append("&fid=").append(fName);
            sb.append("&ordered=").append("0");
            sb.append("&friend_name=").append(fID);
            sb.append("&price=").append("0");

        /*Iterator<Map.Entry<String,String>> it = mMemberMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, String> pair = (Map.Entry<String, String>) it.next();
            sb.append("&fid=").append(pair.getKey().toString());
            sb.append("&ordered=").append("0");
            sb.append("&friend_name=").append(pair.getValue().toString());
            sb.append("&price=").append("0");
        }*/
        Log.e(TAG_THREE, sb.toString());
        return sb.toString();
    }

    @Override
    public void hangoutListener(Hangout item) {

    }


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
            //Log.i(TAG_FOUR, "onPostExecute");

            if (result.startsWith("Unable to")) {
                Log.e(TAG_FOUR, result);
                return;
            }
            try {
                mMemberMap = GroupContent.parseGroupMembers(result);
                //Log.e(TAG_FOUR, result);


            }catch (JSONException e) {
                Log.e(TAG_FOUR, e.getMessage());
                return;
            }
        }

    }
}
