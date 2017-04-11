package siliconempirellc.barkorbolt_android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import siliconempirellc.barkorbolt_android.Models.dogObject;
import siliconempirellc.barkorbolt_android.Utils.backendServer;

/**
 * Created by khuramchaudhry on 4/11/17.
 * This shows a list of randomly generated dogs.
 */

public class ViewDogsActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private backendServer server;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dogs);

        initialize();
    }

    public void initialize() {
        server = new backendServer(ViewDogsActivity.this);
        backButton = (ImageButton) findViewById(R.id.backButton);

        RecyclerView my_recycler_view = (RecyclerView) findViewById(R.id.my_recycler_view);
        my_recycler_view.setLayoutManager(new LinearLayoutManager(ViewDogsActivity.this,
                LinearLayoutManager.VERTICAL, false));
        my_recycler_view.setHasFixedSize(false);
        DoggieListAdapter adapter = new DoggieListAdapter(ViewDogsActivity.this,
                server.getDogsFromServer());
        my_recycler_view.setAdapter(adapter);

        //Setting up Listeners.
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
