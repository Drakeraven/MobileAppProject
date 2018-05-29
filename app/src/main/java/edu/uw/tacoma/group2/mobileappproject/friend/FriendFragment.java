package edu.uw.tacoma.group2.mobileappproject.friend;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import edu.uw.tacoma.group2.mobileappproject.R;
import edu.uw.tacoma.group2.mobileappproject.SplashActivity;
import edu.uw.tacoma.group2.mobileappproject.hangout.CreateHangoutTask;
import edu.uw.tacoma.group2.mobileappproject.user.UserContent;

/**
 * A fragment representing one person in a list of Friends.
 * <p/>
 * Activities containing this fragment MUST implement the {@link FriendTabListener}
 * interface.
 * @author Stephanie Day
 * @version 1.0
 */
public class FriendFragment extends Fragment {
    /**Constants for debugging, sending queries*/
    private static final String TAG = "Friend List";
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String FRIENDS_URL =
            "http://stephd27.000webhostapp.com/list.php?cmd=friends&uid=" + UserContent.sUserID;
    private static final String USERS_URL =
            "http://http://stephd27.000webhostapp.com/list.php?cmd=users";
    private int mColumnCount = 1;
    private FriendTabListener mListener;
    private List<FriendContent> mFriendList;
    private List<FriendContent> mUserList;
    private RecyclerView mRecyclerView;
    private View mLoadingView;
    private int mLongAnimDuration;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FriendFragment() {
    }


    /**
     * For generating a new Friend Fragment
     * @param columnCount How many columns to view friends by
     * @return New Friend List populated with user's friends
     */
    @SuppressWarnings("unused")
    public static FriendFragment newInstance(int columnCount) {
        FriendFragment fragment = new FriendFragment();
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
     * <p>Creates the layout for the friends in the list,</p>
     * and prompts loading of user's friends from server.
     * @param inflater inflates fragment inside view
     * @param container view to inflate fragment in
     * @param savedInstanceState any saved information
     * @return View associated with fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);
        mLoadingView = getActivity().findViewById(R.id.loading_spinner);
        mLongAnimDuration= getResources().getInteger(android.R.integer.config_longAnimTime);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            GetFriendsTask task = new GetFriendsTask();
            task.execute(new String[]{FRIENDS_URL});
        }
        return view;
    }


    /**
     * <p>Called when Fragment is attached to an activity</p>
     * Binds listener to fragment.
     * @param context Context of the app
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FriendTabListener) {
            mListener = (FriendTabListener) context;
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


    /**Gives our friendlist kick ass fading animations!     */
    private void crossfade() {
        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        mLoadingView.animate()
                .alpha(0f)
                .setDuration(mLongAnimDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLoadingView.setVisibility(View.GONE);
                    }
                });

        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        mRecyclerView.setAlpha(0f);
        mRecyclerView.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        mRecyclerView.animate()
                .alpha(1f)
                .setDuration(mLongAnimDuration)
                .setListener(null);
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
    public interface FriendTabListener {
        void friendTabListener(FriendContent item);
    }

    /**
     * Web Service used to retrieve a User's friends.
     */
    private class GetFriendsTask extends AsyncTask<String, Void, String> {

        /**
         * Kicks off getting friends
         * @param urls query url for retrieving friends
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
         * Handles response from web service, populates the user's friends.
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
                mFriendList = FriendContent.giveMeFriends(result);

            }catch (JSONException e) {
                Log.e(TAG, e.getMessage());
                return;
            }
            if (!mFriendList.isEmpty()) {
                crossfade();
                mRecyclerView.setAdapter(new MyFriendRecyclerViewAdapter(mFriendList, mListener));
            }
        }
    }
}
