package edu.uw.tacoma.group2.mobileappproject.friend;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.uw.tacoma.group2.mobileappproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class FriendDetails extends Fragment {
    //TODO:Once you add the image view place reference here
    private TextView mFriendName;
    private TextView mFriendEmail;

    public static final String FRIEND_SELECTED = "friend_selected";

    //private OnFragmentInteractionListener mListener;

    public FriendDetails() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friend_details, container, false);
        mFriendName = (TextView) view.findViewById(R.id.friend_name);
        mFriendEmail = (TextView) view.findViewById(R.id.friend_email);

        return view;
    }


    public void updateFriendView(FriendContent friend) {
        if (friend != null) {
            mFriendName.setText(friend.getFrenName());
            mFriendEmail.setText(friend.getFrenEmail());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle args = getArguments();
        if (args != null) {
            updateFriendView((FriendContent) args.getSerializable(FRIEND_SELECTED));
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
