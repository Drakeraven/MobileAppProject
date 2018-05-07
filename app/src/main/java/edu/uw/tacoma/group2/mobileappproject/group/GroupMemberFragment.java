package edu.uw.tacoma.group2.mobileappproject.group;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class GroupMemberFragment extends DialogFragment {

    private final String TAG = "Group Member Fragment";
    private static final String MEMBERS_URL =
            "http://stephd27.000webhostapp.com/list.php?cmd=members&group=";
    private RecyclerView mRecyclerView;
    private List<String> mMembers;
    private String GroupQ;


    public static GroupMemberFragment newInstance(String param1) {
        GroupMemberFragment f = new GroupMemberFragment();

        Bundle args = new Bundle();
        args.putString("name", param1);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GroupQ = getArguments().getString("name");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mRecyclerView = new RecyclerView(getActivity());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getMembersTask task = new getMembersTask();
        task.execute(MEMBERS_URL + GroupQ);

        return new AlertDialog.Builder(getActivity())
                .setView(mRecyclerView).setTitle("Group Members: ").create();

    }

    private class getMembersTask extends AsyncTask<String, Void, String> {

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
                    response = "Unable to get Group Members, Reason: " + e.getMessage();
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
                mMembers = GroupContent.parseGroupMembers(result);

            }catch (JSONException e) {
                Log.e(TAG, e.getMessage());
                return;
            }
            if (!mMembers.isEmpty()) {
                mRecyclerView.setAdapter(new GroupMemberRecyclerViewAdapter(mMembers));
            }
        }
    }
}
