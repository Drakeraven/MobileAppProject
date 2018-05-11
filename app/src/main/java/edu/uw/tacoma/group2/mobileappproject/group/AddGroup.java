package edu.uw.tacoma.group2.mobileappproject.group;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.uw.tacoma.group2.mobileappproject.friend.FriendContent;
import edu.uw.tacoma.group2.mobileappproject.user.UserContent;

/**
 * <p>populates a display of a user's friends</p>
 * So a user can then create a new group.
 * @author Stephanie Day
 * @version 1.0
 */
public class AddGroup {

    private static final String FRIENDS_URL =
            "http://stephd27.000webhostapp.com/list.php?cmd=friends&uid=" + UserContent.sUserID;
    private static final String TAG = "Adding Group";

    private List<FriendContent> mCurrentFriends;
    CharSequence[] mFriendNames;
    Activity mContext;

    /**
     * Create an add group object to retrieve friends list
     * @param context Context of the app, for the later dialog.
     */
    public AddGroup(Activity context) {
        mContext = context;
        GetFriendsTask task = new GetFriendsTask();
        task.execute(FRIENDS_URL);
    }

    /**
     * Takes in the friend objects to retrieve
     * Just a list of names for display
     */
    private void setUpAddGroup() {
        List<String> listItems = new ArrayList<String>();

            for (FriendContent friend : mCurrentFriends) {
                listItems.add(friend.getFrenName());
            }
        mFriendNames = listItems.toArray(new CharSequence[listItems.size()]);

    }

    /**
     * Web Service for retrieving the group of friends,
     * then kicks off the adding dialog on completion.
     */
    private class GetFriendsTask extends AsyncTask<String, Void, String> {

        /**
         * Retrieves User's friends from web service
         * @param urls query for user's friends
         * @return Web Service Response
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
                    response = "Unable to give friends, Reason: " + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /**
         * Parses returned query, generating friends list and
         * starting creation of add group dialog.
         * @param result Web service's response.
         */
        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, "onPostExecute");

            if (result.startsWith("Unable to")) {
                Log.e(TAG, result);
                return;
            }
            try {
                mCurrentFriends = FriendContent.giveMeFriends(result);

            }catch (JSONException e) {
                Log.e(TAG, "Not parsing JSON " + e.getMessage());
                return;
            }
            if (!mCurrentFriends.isEmpty()) {
               setUpAddGroup();
               DialogFragment agd = AddGroupDialog.newGroup(mFriendNames);
               agd.show(mContext.getFragmentManager(), "add group");
            }
        }
    }

}
