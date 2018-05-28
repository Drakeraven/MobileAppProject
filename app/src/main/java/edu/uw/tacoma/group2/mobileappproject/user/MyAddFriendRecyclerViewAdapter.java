package edu.uw.tacoma.group2.mobileappproject.user;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import java.util.List;

import edu.uw.tacoma.group2.mobileappproject.R;
import edu.uw.tacoma.group2.mobileappproject.friend.FriendContent;
import edu.uw.tacoma.group2.mobileappproject.friend.FriendFragment.FriendTabListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link FriendContent} and makes a call to the
 * specified {@link FriendTabListener}.
 * @author Stephanie Day
 * @version 1.0
 */
public class MyAddFriendRecyclerViewAdapter extends RecyclerView.Adapter<MyAddFriendRecyclerViewAdapter.ViewHolder> {

    private final List<FriendContent> mValues;
    private final AddFriendFragment.OnListFragmentInteractionListener mListener;

    /**
     * Used for creating a new adapter.
     * @param items User's friends
     * @param listener Listens for clicks on a particular user's friends
     */
    MyAddFriendRecyclerViewAdapter(List<FriendContent> items, AddFriendFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    /**
     * Called when creating individual item in the list.
     * @param parent Fragment holding the individual friend fragments
     * @param viewType Type of view?
     * @return Single view of a friend in the list
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_friend, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Called when binding a friend view to the list.
     * @param holder the view holding a particular friend
     * @param position Where in the list that friend view in positioned
     */
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getFrenName());
        holder.mFriendIcon.setProfileId(mValues.get(position).getFrenID());
        holder.mFriendIcon.setPresetSize(-2);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    /**
     * Returns count of friends listed.
     * @return Number of friends listed
     */
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /**
     * Wraps individual friend fragments for handling by the recycler view.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        final View mView;
        final TextView mIdView;
        public FriendContent mItem;
        public ProfilePictureView mFriendIcon;

        /**
         * Generates a view holder
         * @param view the view to be held, gently.
         */
        ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.item_number);
            mFriendIcon = view.findViewById(R.id.friend_profile_pic);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mIdView.getText() + "'";
        }
    }
}
