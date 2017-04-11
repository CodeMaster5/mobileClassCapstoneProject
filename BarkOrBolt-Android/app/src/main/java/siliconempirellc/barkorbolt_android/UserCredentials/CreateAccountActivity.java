package siliconempirellc.barkorbolt_android.UserCredentials;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import siliconempirellc.barkorbolt_android.R;
import siliconempirellc.barkorbolt_android.Utils.backendServer;


/**
 * Created by khuramchaudhry on 4/9/17.
 * This activity creates the user account.
 */

public class CreateAccountActivity extends AppCompatActivity {
    private static final String TAG = "CreateAccountActivity";

    private EditText nameField;
    private EditText emailField;
    private EditText passwordField;
    private EditText reenterPasswordField;
    private Button createAccountButton;
    private TextView loginLabel;
    private String name, email, password, reenterPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        //Stop auto keyboard popup.
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initialize();
    }

    /**
     * Initializing values, listeners, and other stuff.
     * */
    public void initialize() {
        nameField = (EditText) findViewById(R.id.accountName);
        emailField = (EditText) findViewById(R.id.accountEmail);
        passwordField = (EditText) findViewById(R.id.accountPassword);
        reenterPasswordField = (EditText) findViewById(R.id.reenterAccountPassword);
        createAccountButton = (Button) findViewById(R.id.createAccountButton);
        loginLabel = (TextView) findViewById(R.id.loginLabel);

        //Setting up Listeners.
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        loginLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    /**
     * This function handles create account validations.
     * */
    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            Toast.makeText(getBaseContext(), "Failed to create account", Toast.LENGTH_LONG).show();
            return;
        }

        createAccountButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(CreateAccountActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                        backendServer server = new backendServer(CreateAccountActivity.this);
                        server.saveUserToServer(name, email, password);
                        onSignupSuccess();
                    }
                }, 3000);
    }

    /**
     * Creates Account in the database and goes back to LoginActivity with the new email.
     * */
    public void onSignupSuccess() {
        Toast.makeText(getBaseContext(), "Created Account", Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.putExtra("userEmail", email);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Checks the name, email, password, and re-entered fields are not empty and makes sure
     * the text entered in the email field is an email address. Also makes sure to passwords match.
     * Else, the fields show an error for invalidation.
     * */
    public boolean validate() {
        boolean valid = true;

        name = nameField.getText().toString();
        email = emailField.getText().toString();
        password = passwordField.getText().toString();
        reenterPassword = reenterPasswordField.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            nameField.setError("at least 3 characters");
            valid = false;
        } else {
            nameField.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("enter a valid email address");
            valid = false;
        } else {
            emailField.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordField.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        if (reenterPassword.isEmpty() || reenterPassword.length() < 4 ||
                reenterPassword.length() > 10 || !(reenterPassword.equals(password))) {
            reenterPasswordField.setError("Password Do not match");
            valid = false;
        } else {
            reenterPasswordField.setError(null);
        }

        return valid;
    }
}