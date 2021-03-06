package edu.uw.tacoma.group2.mobileappproject.hangout;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class is used for sending a command to the online database. The command that is
 * being sent is an insert into the hangout table. When the user chooses a group and
 * decides to create a hangout the information from their choice will be inserted into the
 * database.
 * @author Harlan Stewart
 * @version 1.3
 */
public class CreateHangoutTask extends AsyncTask<String, Void, String> {
    private static final String TAG = "HANGOUT TASK  ";

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
}