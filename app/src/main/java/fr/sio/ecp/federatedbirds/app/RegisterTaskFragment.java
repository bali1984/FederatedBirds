package fr.sio.ecp.federatedbirds.app;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.os.AsyncTaskCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import fr.sio.ecp.federatedbirds.ApiClient;
import fr.sio.ecp.federatedbirds.R;
import fr.sio.ecp.federatedbirds.auth.TokenManager;

/**

 */

public class RegisterTaskFragment extends DialogFragment {

        private static final String ARG_LOGIN_NEW = "usernameNew";
        private static final String ARG_PASSWORD_NEW = "passwordNew";
        private static final String ARG_PASSWORD_CHECK = "passwordCheck";

        public void setArguments(String login, String password, String passwordCheck) {
            Bundle args = new Bundle();
            args.putString(fr.sio.ecp.federatedbirds.app.RegisterTaskFragment.ARG_LOGIN_NEW, login);
            args.putString(fr.sio.ecp.federatedbirds.app.RegisterTaskFragment.ARG_PASSWORD_NEW, password);
            args.putString(fr.sio.ecp.federatedbirds.app.RegisterTaskFragment.ARG_PASSWORD_CHECK, passwordCheck);
            setArguments(args);
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
            AsyncTaskCompat.executeParallel(
                    new fr.sio.ecp.federatedbirds.app.RegisterTaskFragment.RegisterTask()
            );
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            ProgressDialog dialog = new ProgressDialog(getContext());
            dialog.setIndeterminate(true);
            dialog.setMessage(getString(R.string.register_progress));
            return dialog;
        }

        private class RegisterTask extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... params) {
                try {
                    String login = getArguments().getString("usernameNew");
                    String password = getArguments().getString("passwordNew");
                    String passwordCheck = getArguments().getString("passwordCheck");
                    return ApiClient.getInstance(getContext()).registerNewAcc(login, password, passwordCheck);
                } catch (IOException e) {
                    Log.e(RegisterActivity.class.getSimpleName(), "Registration failed", e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String token) {
                if (token != null) {
                    TokenManager.setUserToken(getContext(), token);
                    getActivity().finish();
                    startActivity(MainActivity.newIntent(getContext()));
                } else {
                    Toast.makeText(getContext(), R.string.login_failed, Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        }
    }
