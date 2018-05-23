package edu.uw.tacoma.group2.mobileappproject.group;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import edu.uw.tacoma.group2.mobileappproject.R;
import edu.uw.tacoma.group2.mobileappproject.user.UserContent;


/**
 * A fragment representing a list of Groups
 * <p/>
 * Activities containing this fragment MUST implement the {@link GroupTabListener}
 * interface.
 * @author Stephanie Day
 * @version 1.0
 */
public class GroupFragment extends Fragment {

    private static final String TAG = "Group List";
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String GROUPS_URL =
            "http://stephd27.000webhostapp.com/list.php?cmd=groups&uid=" + UserContent.sUserID;

    private int mColumnCount = 1;
    private GroupTabListener mListener;
    private List<GroupContent> mGroupList;
    private RecyclerView mRecyclerView;



    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GroupFragment() {
    }

    /**
     * For generating a new Group Fragment
     * @param columnCount How many columns to view groups by
     * @return New Friend List populated with user's groups
     */
    @SuppressWarnings("unused")
    public static GroupFragment newInstance(int columnCount) {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called when fragment is created to set how many columns in list.
     * @param savedInstanceState Any saved information
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }
    /**
     * <p>Creates the layout for the groups in the list,</p>
     * and prompts loading of user's groups from server.
     * @param inflater inflates fragment inside view
     * @param container view to inflate fragment in
     * @param savedInstanceState any saved information
     * @return View associated with fragment
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        GetGroupInfoTask task = new GetGroupInfoTask();
        task.execute(GROUPS_URL);
    }


    /**
     * <p>Called when Fragment is attached to an activity</p>
     * Binds listener to fragment.
     * @param context Context of the app
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GroupTabListener) {
            mListener = (GroupTabListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement GroupTabListener");
        }
    }

    /**Called if/when fragment is removed from activity*/
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface GroupTabListener {
        void groupTabListener(GroupContent item);
    }

    /**
     * Web Service used to retrieve a User's Groups.
     */
    private class GetGroupInfoTask extends AsyncTask<String, Void, String> {

        /**
         * Kicks off getting groups
         * @param urls query url for retrieving groups
         * @return Response from web service
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
                    response = "Unable to give friends, Reason: " + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /**
         * Handles response from web service, populates the user's groups.
         * @param result Response from the web service
         */
        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, "onPostExecute");

            if (result.startsWith("Unable to")) {
                Log.e(TAG, result);
                return;
            }
            try {
                mGroupList = GroupContent.parseGroupList(result);

            }catch (JSONException e) {
                Log.e(TAG, e.getMessage());
                return;
            }
            if (!mGroupList.isEmpty()) {
                mRecyclerView.setAdapter(new MyGroupRecyclerViewAdapter(mGroupList, mListener));
            }
        }
    }
}
