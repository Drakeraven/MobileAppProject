package edu.uw.tacoma.group2.mobileappproject.group;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.uw.tacoma.group2.mobileappproject.R;
import edu.uw.tacoma.group2.mobileappproject.user.UserContent;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Dialog that displays User's friends,
 * prompts for adding group,
 * then performs webservice to add group.
 * @author Stephanie Day
 * @version 1.0
 */
public class AddGroupDialog extends DialogFragment {

    private static final String ADD_GROUP_URL =
            "http://stephd27.000webhostapp.com/gross.php?";

    CharSequence[] mFriendNames;
    EditText mGroupName;

    /**
     * Retrieves list of friend's names for populating dialog.
     * @param stuff List of user's friend's names
     * @return New Dialog for adding group.
     */
        public static AddGroupDialog newGroup(CharSequence[] stuff) {
            AddGroupDialog f = new AddGroupDialog();
            Bundle args = new Bundle();
            args.putCharSequenceArray("names", stuff);
            f.setArguments(args);
            return f;
        }

    /**
     * Called when fragment is created, gets friend's names
     * @param savedInstanceState any saved information
     */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                mFriendNames = getArguments().getCharSequenceArray("names");
            }
        }

    /**
     * Creates checkbox list, listener logs user's choices for adding to group.
     * @param savedInstanceState Saved information
     * @return Checkbox dialog for adding groups.
     */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            LayoutInflater layoutInflater = getActivity().getLayoutInflater();
            View v = layoutInflater.inflate(R.layout.add_group_fields, null);
            mGroupName = v.findViewById(R.id.add_group_name);
            final List mSelectedItems = new ArrayList();

            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Select New Members: ")
                    .setView(v)
                    .setMultiChoiceItems(mFriendNames, null, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                            if (isChecked) {
                                mSelectedItems.add(which);
                            }else if (mSelectedItems.contains(which)) {
                                mSelectedItems.remove(Integer.valueOf(which));
                            }
                        }
                    })
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Retrieves user's selected friends, sends off web service to add group.
                            List<String> selFriends = new ArrayList<>();
                            for (int i = 0; i < mSelectedItems.size(); i++) {
                                selFriends.add(mFriendNames[(int) mSelectedItems.get(i)].toString());
                            }
                            String url = buildAddingGroupsURL(selFriends);
                            if (url != null) {
                                addGroupTask task = new addGroupTask();
                                task.execute(url);
                            }
                        }
                    });

            return builder.create();
        }


    /**
     * Builds the query url for adding a group and its members
     * @param selFriends User's selected for new group
     * @return URL for adding the group
     */
    private String buildAddingGroupsURL(List<String> selFriends) {
            StringBuilder sb = new StringBuilder(ADD_GROUP_URL);

        sb.append("uid=").append(UserContent.sUserID);

            if (!mGroupName.getText().toString().isEmpty()) {
                sb.append("&groupname=" + mGroupName.getText().toString());
            } else {
                sb.append("&groupname=Groupies");
            }

        sb.append("&memcount=").append(Integer.toString(mFriendNames.length));

        if (selFriends.size() < 2) {
            Toast.makeText(getApplicationContext(), "Must have at least two members",
                    Toast.LENGTH_LONG).show();
            return null;

        }
        sb.append("&members=").append(selFriends.get(0));
        for (int i = 1; i < selFriends.size(); i++) {
                sb.append(',');
            sb.append(selFriends.get(i));
            }
        return sb.toString();
        }

    /**
     * Web service for adding the group
     */
    private static class addGroupTask extends AsyncTask<String, Void, String> {

        /**
         * queries web service to add new group
         * @param urls
         * @return
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
         * Lets user know if add is taking some time.
         * @param values any values retrieved when in progress
         */
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Toast.makeText(getApplicationContext(), "Adding Group..", Toast.LENGTH_LONG).show();
        }

        /**
         * It checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. It parses the result, and notifies user of success.
         * If not successful, it displays the exception.
         *
         * @param result The JSON detailing resulsts of group add.
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {
                    Toast.makeText(getApplicationContext(), "Group added!"
                            , Toast.LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to add: "
                                    + jsonObject.get("error")
                            , Toast.LENGTH_LONG)
                            .show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the data " +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
