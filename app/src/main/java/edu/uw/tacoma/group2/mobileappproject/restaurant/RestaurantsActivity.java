package edu.uw.tacoma.group2.mobileappproject.restaurant;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.uw.tacoma.group2.mobileappproject.R;

/**
 * This class represents the Restaurants activity. The purpose of the activity is
 * to provide the user with a list of restaurants near their location and allow them to select one
 * of them as the destination for their hangry foody hangout dinner with friends.
 * @author Harlan Stewart
 * @version 1.0
 */
public class RestaurantsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private String URL = "https://developers.zomato.com/api/v2.1/geocode?";
    private static final String DEFAULT_LAT = "47.244176";
    private static final String DEFAULT_LON ="-122.436984";
    private static final String API_KEY = "1b66bc58a591877d162b63cb4ebcb5de";
    private static final String TAG = "RestaurantsActivity";
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private LocationRequest mLocationRequest;
    private Button mSearchBtn;
    private Location mCurrentLocation;
    private String mLatitude;
    private String mLongitude;
    private static final int MY_PERMISSIONS_LOCATIONS = 0;
    private FusedLocationProviderClient mFusedClient;
    private LocationCallback mLocationCallback;
    private GoogleApiClient mGoogleApiClient;
    private RecyclerView mRecycler;
    private RestaurantAdapter mAdapter;
    private List<Restaurant> mRestaurantList;
    private SwipeRefreshLayout mSwipeRefreshLayout;



    /**
     * Defines the layout for the list of nearby restaurants to display to the user.
     * @param savedInstanceState saved instance state for last created activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);

        mRecycler = (RecyclerView) findViewById(R.id.recycler_restaurants);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new LinearLayoutManager(getParent()));
        mRestaurantList = new ArrayList<Restaurant>();
        mSearchBtn = findViewById(R.id.btn_search);
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLatitude == null || mLongitude == null){
                    mLatitude = DEFAULT_LAT;
                    mLongitude = DEFAULT_LON;
                    loadZomatoData(mLatitude, mLongitude);
                } else {
                    loadZomatoData(mLatitude,mLongitude);
                }

            }
        });
        mFusedClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult){
                List<Location> locationList = locationResult.getLocations();
                if(locationList.size() > 0){
                    Location location = locationList.get(locationList.size() - 1);
                    Log.i(TAG, "Location: " + location.getLatitude() + " " + location.getLongitude());
                    mLatitude = String.valueOf(location.getLatitude());
                    mLongitude = String.valueOf(location.getLongitude());
                }
            }
        };
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mFusedClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            } else {
                getLocation();
            }
        } else {
            mFusedClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }


    }



    /**
     * This method is used to request data from the Zomato api curl request service.
     * For this particular request we are asking for a list of the restaurants near the
     * users location. We use the information retrieved to display a restaurants name,
     * image, and aggregate user rating to the user.
     */
    private void loadZomatoData(String lat, String lon){
        /*final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();*/
        //RequestQueue queue = Volley.newRequestQueue(this);
        String url = URL + "lat="+  lat + "&lon=" + lon;
        //Log.i(TAG, "URL: " + url);
        ZomatoAsyncTask zomTask = new ZomatoAsyncTask();
        zomTask.execute(new String[]{url});
        /*final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try{
                            mRestaurantList = Restaurant.getRestaurants(response);
                            mAdapter = new RestaurantAdapter(mRestaurantList, getApplicationContext());
                            mRecycler.setAdapter(mAdapter);
                        }catch (JSONException e){
                            Log.e("JEXCEPTION", e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user-key", API_KEY);
                params.put("Accept", "application/json");
                //params.put("lat", String.valueOf(mLatitude));
                //params.put("lon", String.valueOf(mLongitude));
                return params;
            }
        };
        queue.add(request);*/


    }

    @Override
    public void onPause(){
        super.onPause();
        if(mFusedClient != null) {
            mFusedClient.removeLocationUpdates(mLocationCallback);
        }

    }

    /**
     * NOT YET IMPLEMENTED
     * intended to get the users current coordinates to use when requesting nearby
     * restaurants.
     */
    private void getLocation() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Required")
                        .setMessage("This app needs permission to use your location")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(RestaurantsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_LOCATIONS);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_LOCATIONS);
            }

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch (requestCode){
            case MY_PERMISSIONS_LOCATIONS: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED){
                        mFusedClient.requestLocationUpdates(mLocationRequest,mLocationCallback, Looper.myLooper());
                    }

                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }

    }

    @Override
    public void onRefresh() {
        if(mLatitude == null || mLongitude == null){
            mLatitude = DEFAULT_LAT;
            mLongitude = DEFAULT_LON;
            loadZomatoData(mLatitude, mLongitude);
        } else {
            loadZomatoData(mLatitude,mLongitude);
        }
    }

    private class ZomatoAsyncTask extends AsyncTask<String, Void, String>{


        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    java.net.URL urlObject = new URL(url);


                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    //urlConnection.setRequestMethod("HEAD");
                    urlConnection.addRequestProperty("user-key",API_KEY );

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s;
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                        //Log.e(TAG,response);
                    }

                } catch (Exception e) {
                    response = "Unable to add course, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            //Log.i(TAG,response);
            return response;
        }

        @Override
        protected void onPostExecute(String result){
            if (result.startsWith("Unable to")) {
                Log.e(TAG, result);
                return;
            }
            try {

                mRestaurantList = Restaurant.getRestaurants(result);
                mAdapter = new RestaurantAdapter(mRestaurantList, getApplicationContext(), mRecycler);
                mRecycler.setAdapter(mAdapter);


            }catch (JSONException e) {
                Log.e(TAG, e.getMessage());
                return;
            }
        }
    }
}