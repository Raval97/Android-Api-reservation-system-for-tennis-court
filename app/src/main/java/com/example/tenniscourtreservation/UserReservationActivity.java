package com.example.tenniscourtreservation;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class UserReservationActivity extends Activity {

    MenuUserAccountTools menuTools;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_reservation);
        getActionBar().hide();

        menuTools = new MenuUserAccountTools(this, (Button) findViewById(R.id.myReservationMenu));
        menuTools.done();
    }

}
