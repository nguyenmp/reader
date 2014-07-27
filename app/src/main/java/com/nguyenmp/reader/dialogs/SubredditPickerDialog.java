package com.nguyenmp.reader.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class SubredditPickerDialog extends DialogFragment {

    public static SubredditPickerDialog newInstance() {
        return new SubredditPickerDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select a subreddit:");

        // Set up content view edit text
        final EditText subredditInput = new EditText(context);
        subredditInput.setHint("/r/...");
        builder.setView(subredditInput);

        builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Callback callback = (Callback) getActivity();
                if (callback != null) {
                    String result = subredditInput.getText().toString();
                    callback.onSubredditPicked(TextUtils.isEmpty(result) ? null : result);
                }
            }
        });
        return builder.create();
    }

    public static interface Callback {
        public void onSubredditPicked(String subreddit);
    }
    protected static class SubredditsAdapter extends ArrayAdapter<String> {

        public SubredditsAdapter(Context context, List<String> objects) {
            super(context, android.R.layout.simple_spinner_dropdown_item, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = super.getDropDownView(position, convertView, parent);

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setTextColor(view.getContext().getResources().getColor(android.R.color.white));

            return view;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setTextColor(view.getContext().getResources().getColor(android.R.color.white));

            return view;
        }
    }
}