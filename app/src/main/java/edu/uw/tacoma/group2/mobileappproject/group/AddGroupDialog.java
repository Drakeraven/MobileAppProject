package edu.uw.tacoma.group2.mobileappproject.group;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class AddGroupDialog extends DialogFragment {
    CharSequence[] friendNames;

        public AddGroupDialog newGroup() {
            AddGroupDialog f = new AddGroupDialog();
            return f;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final List mSelectedItems = new ArrayList();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Select New Members: ")
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
                            //TODO: handle choices here, basically send for the thing to be added.
                        }
                    });

            return builder.create();
        }

}
