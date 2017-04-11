package siliconempirellc.barkorbolt_android;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import siliconempirellc.barkorbolt_android.Models.dogObject;
import siliconempirellc.barkorbolt_android.UserCredentials.LoginActivity;
import siliconempirellc.barkorbolt_android.Utils.OnSwipeTouchListener;
import siliconempirellc.barkorbolt_android.Utils.backendServer;

/**
 * Created by khuramchaudhry on 4/10/17.
 * This activity is the main screen.
 */

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private Toolbar toolbar;
    private backendServer server;
    private TextView dogImage, dogName, dogEmail;
    private Button barkButton;
    private ArrayList<dogObject> dogsList;
    private int dogIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initialize();
    }

    public void initialize() {
        // Find the toolbar view inside the activity layout
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        dogImage = (TextView) findViewById(R.id.dogImage);
        dogName = (TextView) findViewById(R.id.dogName);
        dogEmail = (TextView) findViewById(R.id.dogEmail);
        barkButton = (Button) findViewById(R.id.barkButton);

        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        server = new backendServer(HomeActivity.this);
        dogsList = server.getDogsFromServer();

        displayDog();

        dogImage.setOnTouchListener(new OnSwipeTouchListener(HomeActivity.this) {
            @Override
            public void onSwipeLeft() {
                displayDog();
            }
        });

        // Set the listeners.
        barkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dogsList != null && dogIndex < dogsList.size()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    builder.setTitle("BARK!")
                            .setMessage("Your request has been sent.")
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();
                }
            }
        });

    }

    /**
     * Populates the toolbar with a menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    /**
     * Handles menu item clicks.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button.
        int id = item.getItemId();

        if (id == R.id.action_profile) {
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            return true;
        }

        if (id == R.id.action_view_dogs) {
            Intent intent = new Intent(getApplicationContext(), ViewDogsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            return true;
        }

        if (id == R.id.action_about) {
            Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            return true;
        }

        if (id == R.id.action_signout) {
            server.logUserOff();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This function displays a new dog as long there is one and it increases the dogIndex by 1.
     */
    public void displayDog() {
        if(dogsList != null && dogIndex < dogsList.size()) {
            dogImage.setBackground(dogsList.get(dogIndex).getDogImage());
            dogName.setText(dogsList.get(dogIndex).getName());
            dogEmail.setText(dogsList.get(dogIndex).getEmail());
            dogIndex++;
        } else {
            dogImage.setBackground(null);
            dogImage.setBackgroundColor(Color.WHITE);
            dogName.setText("No Dogs Available.");
            dogEmail.setText("");
        }
    }
}