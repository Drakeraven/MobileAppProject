package edu.uw.tacoma.group2.mobileappproject.friend;

import android.app.Dialog;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import java.util.List;

import edu.uw.tacoma.group2.mobileappproject.R;
import edu.uw.tacoma.group2.mobileappproject.friend.FriendFragment.FriendTabListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link FriendContent} and makes a call to the
 * specified {@link FriendTabListener}.
 */
public class MyFriendRecyclerViewAdapter extends RecyclerView.Adapter<MyFriendRecyclerViewAdapter.ViewHolder> {

    private final List<FriendContent> mValues;
    private final FriendTabListener mListener;
    private Dialog friendPopUp;

    MyFriendRecyclerViewAdapter(List<FriendContent> items, FriendTabListener listener) {
        mValues = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getFrenName());
        holder.mFriendIcon.setProfileId(mValues.get(position).getFrenID());
        holder.mFriendIcon.setPresetSize(-2);
       // holder.mContentView.setText(mValues.get(position).getFrenEmail());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.friendTabListener(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final View mView;
        final TextView mIdView;
        //public final TextView mContentView;
        public FriendContent mItem;
        public ProfilePictureView mFriendIcon;
        LinearLayout friend_clicked;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.item_number);
            mFriendIcon = view.findViewById(R.id.friend_profile_pic);


            //mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mIdView.getText() + "'";
        }
    }
}
