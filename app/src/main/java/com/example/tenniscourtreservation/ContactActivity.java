package com.example.tenniscourtreservation;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class ContactActivity extends Activity {

    MenuTools menuTools;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);
        getActionBar().hide();

        menuTools = new MenuTools(this, (Button) findViewById(R.id.contactMenu));
        menuTools.done();
    }

}
