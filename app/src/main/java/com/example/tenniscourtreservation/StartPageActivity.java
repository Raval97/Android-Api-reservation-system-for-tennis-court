package com.example.tenniscourtreservation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class StartPageActivity extends Activity {
    Button login;
    MenuTools menuTools;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_page);
        getActionBar().hide();

        menuTools = new MenuTools((LinearLayout) findViewById(R.id.menu),
                (Button) findViewById(R.id.menuButton), (ViewGroup) findViewById(R.id.container));

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), LoginInActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

    }

}
