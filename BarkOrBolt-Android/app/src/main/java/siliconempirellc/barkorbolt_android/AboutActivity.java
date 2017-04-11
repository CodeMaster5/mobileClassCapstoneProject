package siliconempirellc.barkorbolt_android;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by khuramchaudhry on 4/10/17.
  This activity displays my information
 */

public class AboutActivity extends AppCompatActivity {

    public static final String TAG = "AboutActivity";

    private FloatingActionButton fabCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        initialize();
    }

    public void initialize() {
        fabCancel = (FloatingActionButton) findViewById(R.id.fabCancel);
        fabCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
