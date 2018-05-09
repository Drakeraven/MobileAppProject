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

public class AddGroupDialog extends DialogFragment {

    private static final String ADD_GROUP_URL =
            "http://stephd27.000webhostapp.com/gross.php?";


    CharSequence[] friendNames;
    EditText groupName;
    private static int GroupCount = 2;

        public static AddGroupDialog newGroup(CharSequence[] stuff) {
            AddGroupDialog f = new AddGroupDialog();
            Bundle args = new Bundle();
            args.putCharSequenceArray("names", stuff);
            f.setArguments(args);
            return f;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                friendNames = getArguments().getCharSequenceArray("names");
            }
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            LayoutInflater layoutInflater = getActivity().getLayoutInflater();
            View v = layoutInflater.inflate(R.layout.add_group_fields, null);
            groupName = v.findViewById(R.id.add_group_name);
            final List mSelectedItems = new ArrayList();

            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Select New Members: ")
                    .setView(v)
                    .setMultiChoiceItems(friendNames, null, new DialogInterface.OnMultiChoiceClickListener() {
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

                            List<String> selFriends = new ArrayList<>();
                            for (int i = 0; i < mSelectedItems.size(); i++) {
                                selFriends.add(friendNames[(int) mSelectedItems.get(i)].toString());
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


    private String buildAddingGroupsURL(List<String> selFriends) {
            //TODO: build the url for adding to general group list and for adding individual members
            StringBuilder sb = new StringBuilder(ADD_GROUP_URL);

        sb.append("uid=").append(UserContent.userID);

            if (!groupName.getText().toString().isEmpty()) {
                sb.append("&groupname=" + groupName.getText().toString());
            } else {
                sb.append("&groupname=Groupies");
            }

        sb.append("&memcount=").append(Integer.toString(friendNames.length));

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

    private static class addGroupTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

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
         * It checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. It tries to call the parse Method and checks to see if it was successful.
         * If not, it displays the exception.
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
