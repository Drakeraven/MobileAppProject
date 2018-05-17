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
 * Streamlined adapter for displaying the members of a group
 * @author Stephanie Day
 * @version 1.0
 */
public class GroupMemberRecyclerViewAdapter extends RecyclerView.Adapter<GroupMemberRecyclerViewAdapter.ViewHolder> {

    private final List<String> mValues;

    /**
     * Constructs a new adapter with list of group members
     * @param mValues List of the group's members names
     */
    GroupMemberRecyclerViewAdapter(List<String> mValues) {
        this.mValues = mValues;
    }


    /**
     * creates holder for view that displays a single member of a group in the list
     * @param parent view that holds all of the view holders
     * @param viewType Type of view?
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_member_details_popup, parent, false);

        return new ViewHolder(view);
    }

    /**
     * Binds a view holder to the list to display one group member
     * @param holder The view holder
     * @param position where in the list that the member is displayed
     */
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mMemberView.setText(mValues.get(position));
    }


    /**
     * Returns number of members being displayed
     * @return number of members being displayed
     */
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /**
     * View holder that displays member of a group.
     */
    class ViewHolder extends RecyclerView.ViewHolder{

        private final View mView;
        private final TextView mMemberView;

        /**
         * Creates the view holder
         * @param itemView view that needs to be held, very gently
         */
        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mMemberView = itemView.findViewById(R.id.Member_Name);

        }
    }
}
