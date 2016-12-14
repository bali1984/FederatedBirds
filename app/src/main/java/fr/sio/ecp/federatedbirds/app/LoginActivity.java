package fr.sio.ecp.federatedbirds.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import fr.sio.ecp.federatedbirds.R;
import fr.sio.ecp.federatedbirds.utils.ValidationUtils;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        findViewById(R.id.registerNewAcc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    private void login() {

        // Get form views
        EditText usernameText = (EditText) findViewById(R.id.username);
        EditText passwordText = (EditText) findViewById(R.id.password);

        String login = usernameText.getText().toString();
        String password = passwordText.getText().toString();

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

        LoginTaskFragment taskFragment = new LoginTaskFragment();
        taskFragment.setArguments(login, password);
        taskFragment.show(getSupportFragmentManager(), "login_task");

    }

    private void register() {

        RegisterTaskFragment taskFragment = new RegisterTaskFragment();
        taskFragment.show(getSupportFragmentManager(), "register_task");

    }

}
