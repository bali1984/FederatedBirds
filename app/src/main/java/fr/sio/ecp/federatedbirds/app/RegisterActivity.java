package fr.sio.ecp.federatedbirds.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import fr.sio.ecp.federatedbirds.R;
import fr.sio.ecp.federatedbirds.utils.ValidationUtils;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViewById(R.id.validateRegistration).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateRegistration();
            }
        });
    }

    private void validateRegistration() {

        // Get form views
        EditText usernameText = (EditText) findViewById(R.id.usernameNew);
        EditText passwordText = (EditText) findViewById(R.id.passwordNew);
        EditText emailText = (EditText) findViewById(R.id.emailNew);

        String login = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        String email = emailText.getText().toString();



        if (!(ValidationUtils.validateLogin(login))){
            usernameText.setError(getString(R.string.invalid_format));
            usernameText.requestFocus();
            return;
        }

        if (!(ValidationUtils.validatePassword(password))) {
            passwordText.setError(getString(R.string.invalid_format));
            passwordText.requestFocus();
            return;
        }

        if (!(ValidationUtils.validateEmail(email))) {
            emailText.setError(getString(R.string.invalid_format));
            emailText.requestFocus();
            return;
        }

        RegisterTaskFragment taskFragment = new RegisterTaskFragment();
        taskFragment.setArguments(login, password, email);
        taskFragment.show(getSupportFragmentManager(), "register_task");

    }

}

