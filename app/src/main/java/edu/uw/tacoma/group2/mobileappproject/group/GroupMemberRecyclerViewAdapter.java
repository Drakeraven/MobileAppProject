package edu.uw.tacoma.group2.mobileappproject.group;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.uw.tacoma.group2.mobileappproject.R;

public class GroupMemberRecyclerViewAdapter extends RecyclerView.Adapter<GroupMemberRecyclerViewAdapter.ViewHolder> {

    private final List<String> mValues;

    GroupMemberRecyclerViewAdapter(List<String> mValues) {
        this.mValues = mValues;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_member_details_popup, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mMemberView.setText(mValues.get(position));
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private final View mView;
        private final TextView mMemberView;

        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mMemberView = itemView.findViewById(R.id.Member_Name);

        }
    }
}
