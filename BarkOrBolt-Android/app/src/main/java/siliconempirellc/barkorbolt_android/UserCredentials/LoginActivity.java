package siliconempirellc.barkorbolt_android.UserCredentials;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import siliconempirellc.barkorbolt_android.HomeActivity;
import siliconempirellc.barkorbolt_android.R;
import siliconempirellc.barkorbolt_android.Utils.backendServer;

/**
 * Created by khuramchaudhry on 4/9/17.
 * This activity logs in the user.
 */

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_NEW_ACCOUNT = 0;

    private EditText emailField;
    private EditText passwordField;
    private Button loginButton;
    private TextView createAccountLabel;
    private String userEmail, userPassword;
    private backendServer server;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Stop auto keyboard popup.
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initialize();
    }

    /**
     * Initializing values, listeners, and other stuff.
     * */
    public void initialize() {
        emailField = (EditText) findViewById(R.id.loginEmail);
        passwordField = (EditText) findViewById(R.id.loginPassword);
        loginButton = (Button) findViewById(R.id.loginButton);
        createAccountLabel = (TextView) findViewById(R.id.createAccountLabel);
        userEmail = "";
        userPassword = "";
        server = new backendServer(LoginActivity.this);

        //Setting up clickListeners
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        createAccountLabel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the createAccount activity
                Intent intent = new Intent(getApplicationContext(), CreateAccountActivity.class);
                startActivityForResult(intent, REQUEST_NEW_ACCOUNT);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        //Checks if the user has logged in before and if that is the case then the user is
        // automatically sent to the home screen.
        if(server.isUserLoggedInBefore()) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            finish();
        }
    }

    /**
     * This function handles login validations.
     * */
    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
            return;
        }

        loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        loginButton.setEnabled(true);
                        progressDialog.dismiss();
                        if(onLoginSuccess()) {
                            Toast.makeText(getBaseContext(), "Login Succeed", Toast.LENGTH_LONG)
                                    .show();
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }, 3000);
    }


    /**
     * Sets up the email field after the user successfully sets up a new account.
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_NEW_ACCOUNT) {
            if (resultCode == RESULT_OK) {
                emailField.setText(data.getStringExtra("userEmail"));
                passwordField.requestFocus();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    /**
     * Checks the password and email are in the database.
     * */
    public boolean onLoginSuccess() {
        return server.isUserOnServer(userEmail, userPassword);
    }

    /**
     * Checks the email and password fields are not empty and makes sure the text entered in the
     * email field is an email address. Else, the fields show an error for invalidation.
     * */
    public boolean validate() {
        boolean valid = true;

        userEmail = emailField.getText().toString();
        userPassword = passwordField.getText().toString();

        if (userEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail)
                .matches()) {
            emailField.setError("Enter a valid email address");
            valid = false;
        } else {
            emailField.setError(null);
        }

        if (userPassword.isEmpty()) {
            passwordField.setError("Please enter your password");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        return valid;
    }
}
