package edu.uw.tacoma.group2.mobileappproject.group;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.uw.tacoma.group2.mobileappproject.R;

/**
 * {@link RecyclerView.Adapter} that can display a {@link GroupContent} and makes a call to the
 * specified {@link GroupFragment.GroupTabListener}.
 * @author Stephanie Day
 * @version 1.0
 */
public class MyGroupRecyclerViewAdapter extends RecyclerView.Adapter<MyGroupRecyclerViewAdapter.ViewHolder> {

    private final List<GroupContent> mValues;
    private final GroupFragment.GroupTabListener mListener;

    /**
     * Creates new adapter with list of groups to display
     * @param items list of user's groups
     * @param listener listens for clicks on particular group
     */
    MyGroupRecyclerViewAdapter(List<GroupContent> items, GroupFragment.GroupTabListener listener) {
        mValues = items;
        mListener = listener;
    }

    /**
     * Called when creating individual item in the list.
     * @param parent Fragment holding the individual group fragments
     * @param viewType Type of view?
     * @return Single view of a friend in the list
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_group, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Called when binding a group view to the list.
     * @param holder the view holding a particular group
     * @param position Where in the list that group view in positioned
     */
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getGroupName());
        holder.mContentView.setText(mValues.get(position).getGroupCount() + " Members");

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.groupTabListener(holder.mItem);
                }
            }
        });
    }

    /**
     * Returns count of groups listed.
     * @return Number of groups listed
     */
    @Override
    public int getItemCount() {
        return mValues.size();
    }
    /**
     * Wraps individual group fragments for handling by the recycler view.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mIdView;
        final TextView mContentView;
        public GroupContent mItem;

        /**
         * Generates a view holder
         * @param view the view to be held, gently.
         */
        ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.item_number);
            mContentView = view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
