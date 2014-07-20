package com.nguyenmp.reader;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.nguyenmp.reader.data.Account;
import com.nguyenmp.reader.db.AccountsDatabase;
import com.nguyenmp.reader.util.DisplayHelper;
import com.nguyenmp.reddit.Reddit;
import com.nguyenmp.reddit.data.LoginData;

public class LoginDialogFragment extends DialogFragment {

    public static LoginDialogFragment newInstance() {
        return new LoginDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Initialize the dialog
        final FragmentActivity context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.add_account));

        // Set up the view
        LayoutInflater inflater = LayoutInflater.from(context);
        View contentView = inflater.inflate(R.layout.dialog_login, null);
        builder.setView(contentView);

        // Get the edit text fields
        final EditText username = (EditText) contentView.findViewById(R.id.username);
        final EditText password = (EditText) contentView.findViewById(R.id.password);

        // Create dialog
        final AlertDialog dialog = builder.create();

        // Set the login and cancel button
        LoginClickListener clickListener = null;
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.log_in), clickListener);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.cancel), clickListener);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {
                DisplayHelper.showKeyboard(username);
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new LoginTask(context, username.getText().toString(), password.getText().toString()).execute();
                    }
                });
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
        return dialog;
    }

    public void enableFields() {
        // TODO: Enable all fields when we need to
    }

    public void disableFields() {
        // TODO: Enable all fields when we need to
    }

    private static class LoginClickListener implements DialogInterface.OnClickListener {
        private final FragmentActivity context;
        private final EditText username, password;

        private LoginClickListener(FragmentActivity context, EditText username, EditText password) {
            this.context = context;
            this.username = username;
            this.password = password;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
            } else {
                dialog.dismiss();
            }
        }
    }

    private static class LoginTask extends AsyncTask<Void, Void, Account> {
        private final Refreshable refreshListener;
        private final Context context;
        private final String username, password;
        private String error = null;

        private LoginTask(FragmentActivity context, String username, String password) {
            this.refreshListener = (Refreshable) context;
            this.context = context;
            this.username = username;
            this.password = password;
        }

        @Override
        protected Account doInBackground(Void... params) {
            try {
                LoginData data = Reddit.login(username, password);
                if (isCancelled()) return null;
                return new Account(username, data);
            } catch (Exception e) {
                error = e.getMessage();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Account account) {
            if (error != null) Toast.makeText(context, error,Toast.LENGTH_LONG).show();
            if (account == null) return;
            AccountsDatabase.put(context, account);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            prefs.edit()
                    .putString("username", account.username)
                    .putString("cookie", account.data.cookie)
                    .putString("modhash", account.data.modhash)
                    .commit();
            refreshListener.refresh();
        }
    }
}
