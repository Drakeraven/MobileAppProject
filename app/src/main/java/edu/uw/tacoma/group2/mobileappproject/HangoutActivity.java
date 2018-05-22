package edu.uw.tacoma.group2.mobileappproject;

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
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.uw.tacoma.group2.mobileappproject.group.MyGroupRecyclerViewAdapter;
import edu.uw.tacoma.group2.mobileappproject.order.OrdersContent;
import edu.uw.tacoma.group2.mobileappproject.order.OrdersFragment;
import edu.uw.tacoma.group2.mobileappproject.group.GroupContent;
import edu.uw.tacoma.group2.mobileappproject.group.GroupFragment;
import edu.uw.tacoma.group2.mobileappproject.user.UserContent;


public class HangoutActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GroupFragment.GroupTabListener,
        OrdersFragment.OrdersTabListener{
    private static final String TAG_ONE = "GROUP ID URL === ";
    private static final String TAG_TWO = "HANGOUT INSERT === ";
    private static final String TAG_THREE = "MEMBER INSERT === ";
    private static final String TAG_FOUR = "MEMBER TASK === ";
    private static final String ADD_HANGOUT_URL =
            "http://hangryfoodiehangout.000webhostapp.com/hangout.php?cmd=hangout";
    private static final String ADD_HANGOUT_MEM_URL =
            "http://hangryfoodiehangout.000webhostapp.com/hangout.php?cmd=members";
    private GroupFragment myGroupFrag;
    private HashMap<String, String> mMemberMap;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hangout);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

    public void groupTabListener(GroupContent item) {
        Toast.makeText(getApplicationContext(), "CLICKED ON " + item.getGroupName(),
                Toast.LENGTH_LONG).show();
        String infoURL =
                "http://stephd27.000webhostapp.com/list.php?cmd=groups&uid="
                + item.getGroupID();
        GetMemberInfoTask memInfoTask = new GetMemberInfoTask();
        memInfoTask.execute(infoURL);
        Log.i(TAG_ONE,infoURL);
        String mGroupCount = item.getGroupCount();
        String url = buildHangoutURL(mGroupCount);
        if(!url.isEmpty()){
            CreateHangoutTask task = new CreateHangoutTask();
            task.execute(url);
            Log.i(TAG_TWO,url);
        }
        /*String urlMem = buildHangoutMembers();
        if(!urlMem.isEmpty()){
            CreateHangoutTask taskMem = new CreateHangoutTask();
            Log.i(TAG_THREE,urlMem);
            taskMem.execute(urlMem);
        }*/



    }

    private String buildHangoutURL(String memCount){
        StringBuilder sb = new StringBuilder(ADD_HANGOUT_URL);
        /*if(!UserContent.sUserRestaurant.isEmpty()){
            sb.append("&rest_name=" + UserContent.sUserRestaurant);
        }else {*/
            sb.append("&rest_name=").append("DEFAULT_RESTAURANT");
        //}
        sb.append("&num_members=").append(memCount);
        sb.append("&closed_open=").append("0");
        return  sb.toString();
    }

    private String buildHangoutMembers(){
        StringBuilder sb = new StringBuilder(ADD_HANGOUT_MEM_URL);
        Iterator<Map.Entry<String,String>> it = mMemberMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, String> pair = (Map.Entry<String, String>) it.next();
            sb.append("&fid=").append(pair.getKey().toString());
            sb.append("&ordered=").append("0");
            sb.append("&friend_name=").append(pair.getValue().toString());
            sb.append("&price=").append("0");
        }
        return sb.toString();
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

            }catch (JSONException e) {
                Log.e(TAG_FOUR, e.getMessage());
                return;
            }
        }

    }
}
