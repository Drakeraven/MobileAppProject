package edu.uw.tacoma.group2.mobileappproject.user;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

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

    public static String sUserID;
    public static String sUserName;
    public static String sUserPic;
    public static String sUserEmail;
    public static String sUserRestaurant;


    /**
     * Called at the start of the app to create the user profile.
     * Just one version!
     * @param userJSON Web service to retrieve this particular user using fb login
     */
    public UserContent(JSONObject userJSON) {
        try {
            sUserID = userJSON.getString(ID);
            sUserName = userJSON.getString(NAME);
            JSONObject picture = userJSON.getJSONObject(PIC).getJSONObject(PIC_DATA);
            sUserPic = picture.getString(PIC_URL);
            sUserEmail = userJSON.getString(EMAIL);

        } catch (JSONException ex) {
            Log.e(TAG, "Couldn't parse the JSON Request into User Content.");
        }
        AddUserTask task = new AddUserTask();
        task.execute(buildUserURL());

    }

    /**
     * Builds a query url for adding the user to our list of users
     * @return
     */
    private String buildUserURL() {
        StringBuilder sb = new StringBuilder(USER_ADD_URL);

        try {
            sb.append("id=");
            sb.append(URLEncoder.encode(sUserID, "UTF-8"));

            sb.append("&email=");
            sb.append(URLEncoder.encode(sUserEmail, "UTF-8"));

            sb.append("&name=");
            sb.append(URLEncoder.encode(sUserName, "UTF-8"));

            sb.append("&picture=");
            sb.append(URLEncoder.encode(sUserPic, "UTF-8"));

            Log.d(TAG, "Adding user by url: " + sb.toString());

        }catch (Exception e) {
            Log.e(TAG, "Something went wrong saving user data: " + e.getMessage());
        }

        return sb.toString();
    }

    /**
     * Web service for taking fb log in information and saving user data,
     * needed for other things
     */
    private class AddUserTask extends AsyncTask<String, Void, String> {
        /**
         * @param urls query url for adding a new user
         * @return webservice response
         */
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

        /**
         * debug logging for if it add the user successfully.
         * Not a big deal if it fails because we already know the user.
         * @param s Webservice response
         */
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
