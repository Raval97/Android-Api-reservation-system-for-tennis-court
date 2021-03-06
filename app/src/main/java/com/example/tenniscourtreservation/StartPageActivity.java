package com.example.tenniscourtreservation;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class StartPageActivity extends Activity {

    MenuTools menuTools;
    protected static final String TAG = StartPageActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_page);
        getActionBar().hide();

        menuTools = new MenuTools(this, (Button) findViewById(R.id.startMenu));
        menuTools.done();
    }

}
