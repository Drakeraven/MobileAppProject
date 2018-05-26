package edu.uw.tacoma.group2.mobileappproject.hangout;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

import edu.uw.tacoma.group2.mobileappproject.group.GroupContent;

public class CreateHangoutMembersTask extends AsyncTask<String, Void, String> {

    private static final String TAG = "HANGOUT MEMBER TASK  ";

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
                    Log.e(TAG,response);
                }

            } catch (Exception e) {
                response = "Unable to add members, Reason: "
                        + e.getMessage();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
        }
        //Log.i(TAG,response);

        return response;
    }




}
