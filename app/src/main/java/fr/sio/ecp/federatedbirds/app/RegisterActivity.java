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
        EditText passwordTextCheck = (EditText) findViewById(R.id.passwordCheck);

        String login = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        String passwordCheck = passwordTextCheck.getText().toString();

        if (!ValidationUtils.validateLogin(login)) {
            usernameText.setError(getString(R.string.invalid_format));
            usernameText.requestFocus();
            return;
        }

        if (!ValidationUtils.validatePassword(password)) {
            passwordText.setError(getString(R.string.invalid_format));
            passwordText.requestFocus();
            return;
        }

        boolean correct = passwordTextCheck.equals(passwordText);
        if (!correct) {
            passwordText.setError(getString(R.string.invalid_password_check));
            passwordText.requestFocus();
            return;
        }


        LoginTaskFragment taskFragment = new LoginTaskFragment();
        taskFragment.setArguments(login, password);
        taskFragment.show(getSupportFragmentManager(), "login_task");

    }

}

