package edu.uw.tacoma.group2.mobileappproject.user;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Our App's User Content, currently retrieved via facebook log in.
 * Upon start of the app.
 **/
public class UserContent {
    private static final String TAG = "User Content";
    //defined JSON tags for parsing and storage:
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String PIC = "picture";
    private static final String PIC_DATA = "data";
    private static final String PIC_URL = "url";
    private static final String EMAIL = "email";
    private final static String USER_ADD_URL
            = "http://stephd27.000webhostapp.com/addUsers.php?";

    public static String userID;
    public static String userName;
    public static String userPic;
    public static String userEmail;

    //Called at start of the app to initialize the user, DO NOT MAKE MULTIPLE USERS.
    public UserContent(JSONObject userJSON) {
        try {
            userID = userJSON.getString(ID);
            userName = userJSON.getString(NAME);
            JSONObject picture = userJSON.getJSONObject(PIC).getJSONObject(PIC_DATA);
            userPic = picture.getString(PIC_URL);
            userEmail = userJSON.getString(EMAIL);

        } catch (JSONException ex) {
            Log.e(TAG, "Couldn't parse the JSON Request into User Content.");
        }
        AddUserTask task = new AddUserTask();
        task.execute(new String[]{buildUserURL()});

    }

    private String buildUserURL() {
        StringBuilder sb = new StringBuilder(USER_ADD_URL);

        try {
            sb.append("id=");
            sb.append(URLEncoder.encode(userID, "UTF-8"));

            sb.append("&email=");
            sb.append(URLEncoder.encode(userEmail, "UTF-8"));

            sb.append("&name=");
            sb.append(URLEncoder.encode(userName, "UTF-8"));

            sb.append("&picture=");
            sb.append(URLEncoder.encode(userPic, "UTF-8"));

            Log.d(TAG, "Adding user by url: " + sb.toString());

        }catch (Exception e) {
            Log.e(TAG, "Something went wrong saving user data: " + e.getMessage());
        }

        return sb.toString();
    }

    private class AddUserTask extends AsyncTask<String, Void, String> {
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
                    String s = "";

                     while ((s = buffer.readLine()) != null) {
                         response += s;
                     }
                } catch (Exception e) {
                    response = "Unable to add User, Reason: " + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                String status = (String) jsonObject.get("result");

                if (status.equals("success")) {
                    Log.d(TAG,"User successfully added!");
                } else {
                    Log.d(TAG, "Failed to add user: "
                            + jsonObject.get("error"));
                }
            } catch (JSONException e) {
                Log.e(TAG, "Something wrong with the data: " +
                        e.getMessage());
            }
        }
    }
}
