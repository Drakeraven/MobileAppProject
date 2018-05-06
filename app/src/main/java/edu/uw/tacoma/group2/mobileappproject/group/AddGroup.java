package edu.uw.tacoma.group2.mobileappproject.group;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class AddGroup {
    private static final String FRIENDS_URL =
            "http://stephd27.000webhostapp.com/list.php?cmd=friends&uid=" + UserContent.userID;
    private static final String TAG = "Adding Group";
    private List<FriendContent> currentFriends;
    CharSequence[] friendNames;
    private static int GroupCount = 2;

    public AddGroup() {
        GetFriendsTask task = new GetFriendsTask();
        task.execute();
    }
    private void setUpAddGroup() {
        List<String> listItems = new ArrayList<String>();

            for (FriendContent friend : currentFriends) {
                listItems.add(friend.getFrenName());
            }
        friendNames = listItems.toArray(new CharSequence[listItems.size()]);

    }


    private class GetFriendsTask extends AsyncTask<String, Void, String> {

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

        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, "onPostExecute");

            if (result.startsWith("Unable to")) {
                Log.e(TAG, result);
                return;
            }
            try {
                currentFriends = FriendContent.giveMeFriends(result);

            }catch (JSONException e) {
                Log.e(TAG, e.getMessage());
                return;
            }
            if (!currentFriends.isEmpty()) {
               setUpAddGroup();
            }
        }
    }

//    public class addGroupDialog extends DialogFragment {
//
//        public AddGroup newGroup() {
//            AddGroup f = new AddGroup();
//            return f;
//        }
//
//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//        }
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            final List mSelectedItems = new ArrayList();
//            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
//            builder.setTitle("Select New Members: ")
//                    .setMultiChoiceItems(friendNames, null, new DialogInterface.OnMultiChoiceClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
//
//                            if (isChecked) {
//                                mSelectedItems.add(which);
//                            }else if (mSelectedItems.contains(which)) {
//                                mSelectedItems.remove(Integer.valueOf(which));
//                            }
//                        }
//                    })
//                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            //TODO: handle choices here, basically send for the thing to be added.
//                        }
//                    });
//
//            return builder.create();
////        }
//    }
}
