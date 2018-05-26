package edu.uw.tacoma.group2.mobileappproject.order.OrderMenu;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OrderMasterTask extends AsyncTask<String, Void, String> {
    /*
    Ordering modes: 'O' -- you ordered, and you're updating your hangout
     */
    public char orderingMode;

    private final String TAG = "Order Master Task";

    @Override
    protected String doInBackground(String[] urls) {
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
                response = "Unable to run order stuff, Reason: " + e.getMessage();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.i(TAG, "onPostExecute");

        if (result.startsWith("Unable to")) {
            Log.e(TAG, result);
            return;
        }
        try {
            if (orderingMode == 'O') {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {
                    Log.i(TAG, "Successfully updated order.");
                } else {
                    Log.i(TAG, "Failed to add: "
                        + jsonObject.get("error"));
                }
            }

        }catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            return;
        }

    }
}
