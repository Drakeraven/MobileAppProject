package edu.uw.tacoma.group2.mobileappproject.hangout;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import edu.uw.tacoma.group2.mobileappproject.order.OrderMenu.OrderMenuFragment;
import edu.uw.tacoma.group2.mobileappproject.user.UserContent;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HangoutInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HangoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HangoutFragment extends Fragment {
    private static final String HANGOUTS_URL =
            "https://hangryfoodiehangout.000webhostapp.com/getHangouts.php?&fid=" + UserContent.sUserID;
    private static final String TAG = "Hangout List";
    private static final String  ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private List<Hangout> mHangoutList;
    private HangoutInteractionListener mListener;
    private RecyclerView mRecyclerView;

    public HangoutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment HangoutFragment.
     */
    public static HangoutFragment newInstance(int columnCount) {
        HangoutFragment fragment = new HangoutFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_hangout_list, container, false);
        if(view instanceof RecyclerView){
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            registerForContextMenu(mRecyclerView);
            if(mColumnCount <= 1){
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            }else{
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            GetHangoutsTask task = new GetHangoutsTask();
            task.execute(HANGOUTS_URL);


        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        Toolbar tb = getActivity().findViewById(R.id.toolbar);
        tb.setTitle(R.string.current_hangouts);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HangoutInteractionListener) {
            mListener = (HangoutInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement HangoutInteractionListener");
        }
    }

    @Override
    public void onStart(){
        super.onStart();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = -1;
        try {
            position = ((HangoutAdapter)mRecyclerView.getAdapter()).getPosition();
        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        Toolbar tb = getActivity().findViewById(R.id.toolbar);
        tb.setTitle("Order Item: ");
        Hangout hangout = mHangoutList.get(position);
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.content_frame, OrderMenuFragment.newInstance(1, hangout))
                .addToBackStack(null)
                .commit();

        return super.onContextItemSelected(item);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface HangoutInteractionListener {
        void hangoutListener(Hangout item);
    }

    private class GetHangoutsTask extends AsyncTask<String, Void, String>{
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
                    response = "Unable to give hangouts, Reason: " + e.getMessage();
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
                mHangoutList = Hangout.parseHangout(result);
                Log.e(TAG, result);

            }catch (JSONException e) {
                Log.e(TAG, e.getMessage());
                return;
            }
            if (!mHangoutList.isEmpty()) {
                mRecyclerView.setAdapter(null);
                mRecyclerView.setAdapter(new HangoutAdapter(mHangoutList, mListener, getActivity()));
            }
        }
    }
}
