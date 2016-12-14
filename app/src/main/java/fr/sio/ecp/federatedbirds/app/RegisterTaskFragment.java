package fr.sio.ecp.federatedbirds.app;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.os.AsyncTaskCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import fr.sio.ecp.federatedbirds.ApiClient;
import fr.sio.ecp.federatedbirds.R;
import fr.sio.ecp.federatedbirds.auth.TokenManager;

/**

 */

public class RegisterTaskFragment extends DialogFragment {

      private static final String ARG_LOGIN = "login";
      private static final String ARG_PASSWORD = "password";
      private static final String ARG_EMAIL = "email";

        public void setArguments(String login, String password, String email) {
            Bundle args = new Bundle();
            args.putString(RegisterTaskFragment.ARG_LOGIN, login);
            args.putString(RegisterTaskFragment.ARG_PASSWORD, password);
            args.putString(RegisterTaskFragment.ARG_EMAIL, email);
            setArguments(args);

        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
            AsyncTaskCompat.executeParallel(
                    new RegisterTask()
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
                    String login = getArguments().getString("login");
                    String password = getArguments().getString("password");
                    String email = getArguments().getString("email");
                    return ApiClient.getInstance(getContext()).addUser(login, password, email);
                } catch (IOException e) {
                    Log.e(RegisterActivity.class.getSimpleName(), "Registration failed", e);
                    return null;
                }
            }
/*
            @Override
            protected void onPostExecute(String user) {
                if (user != null) {
                    Toast.makeText(getContext(), R.string.registration_success, Toast.LENGTH_SHORT).show();
                }  else {
                    Toast.makeText(getContext(), R.string.login_failed, Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }

*/
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