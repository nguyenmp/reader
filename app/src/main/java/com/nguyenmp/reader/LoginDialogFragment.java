package com.nguyenmp.reader;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nguyenmp.reader.data.Account;
import com.nguyenmp.reader.db.AccountsDatabase;
import com.nguyenmp.reader.util.DisplayHelper;
import com.nguyenmp.reddit.CookieSession;
import com.nguyenmp.reddit.Reddit;

public class LoginDialogFragment extends DialogFragment {

    public static LoginDialogFragment newInstance() {
        return new LoginDialogFragment();
    }

    @NonNull
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
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.log_in), (DialogInterface.OnClickListener) null);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.cancel), (DialogInterface.OnClickListener) null);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {
                DisplayHelper.showKeyboard(username);
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new LoginTask(context, username.getText().toString(), password.getText().toString(), LoginDialogFragment.this).execute();
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
        setEnabled(true);
    }

    public void disableFields() {
        setEnabled(false);
    }

    public void setEnabled(boolean enabled) {
        AlertDialog dialog = (AlertDialog) getDialog();
        EditText username = (EditText) dialog.findViewById(R.id.username);
        username.setEnabled(enabled);
        EditText password = (EditText) dialog.findViewById(R.id.password);
        password.setEnabled(enabled);

        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setEnabled(enabled);
        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setEnabled(enabled);
    }

    private static class LoginTask extends AsyncTask<Void, Void, Account> {
        private final Refreshable refreshListener;
        private final Context context;
        private final String username, password;
        private String error = null;
        private final LoginDialogFragment fragment;

        private LoginTask(FragmentActivity context, String username, String password, LoginDialogFragment fragment) {
            this.fragment = fragment;
            this.refreshListener = (Refreshable) context;
            this.context = context;
            this.username = username;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fragment.disableFields();
        }

        @Override
        protected Account doInBackground(Void... params) {
            try {
                CookieSession data = (CookieSession) Reddit.login(username, password);
                if (isCancelled()) return null;
                return new Account(username, data);
            } catch (Exception e) {
                error = e.getMessage();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Account account) {
            fragment.enableFields();
            if (error != null) Toast.makeText(context, error,Toast.LENGTH_LONG).show();
            if (account == null) return;
            AccountsDatabase.put(context, account);
            NavigationDrawerFragment.setCurrentAccount(context, account);
            fragment.dismiss();
            refreshListener.refresh();
        }
    }
}
