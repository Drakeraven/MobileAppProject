package edu.uw.tacoma.group2.mobileappproject.group;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tacoma.group2.mobileappproject.R;
import edu.uw.tacoma.group2.mobileappproject.user.UserContent;

public class AddGroupDialog extends DialogFragment {

    private static final String ADD_GROUP_URL =
            "http://stephd27.000webhostapp.com/gross.php?";

    CharSequence[] friendNames;
    EditText groupName;
    private static int GroupCount = 2;

        public static AddGroupDialog newGroup(CharSequence[] stuff) {
            AddGroupDialog f = new AddGroupDialog();
            Bundle args = new Bundle();
            args.putCharSequenceArray("names", stuff);
            f.setArguments(args);
            return f;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                friendNames = getArguments().getCharSequenceArray("names");
            }
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            LayoutInflater layoutInflater = getActivity().getLayoutInflater();
            View v = layoutInflater.inflate(R.layout.add_group_fields, null);
            groupName = v.findViewById(R.id.add_group_name);
            final List mSelectedItems = new ArrayList();

            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Select New Members: ")
                    .setView(v)
                    .setMultiChoiceItems(friendNames, null, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                            if (isChecked) {
                                mSelectedItems.add(which);
                            }else if (mSelectedItems.contains(which)) {
                                mSelectedItems.remove(Integer.valueOf(which));
                            }
                        }
                    })
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            buildAddingGroupsURL();
                        }
                    });

            return builder.create();
        }


        private void buildAddingGroupsURL() {
            //TODO: build the url for adding to general group list and for adding individual members
            StringBuilder sb = new StringBuilder(ADD_GROUP_URL);

            sb.append("uid=" + UserContent.userName);

            if (!groupName.getText().toString().isEmpty()) {
                sb.append("&groupname=" + groupName.getText().toString());
            } else {
                sb.append("&groupname=Groupies");
            }

            sb.append("&memcount=" + Integer.toString(friendNames.length));


            sb.append("&members=" + friendNames[0]);
            for (int i = 1; i < friendNames.length; i++) {
                sb.append(',');
                sb.append(friendNames[i]);
            }
        }

        //TODO: add in async task for adding group members. Repop list?
}
