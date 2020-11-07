package com.example.tenniscourtreservation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tenniscourtreservation.model.User;

public class ReservationActivity extends Activity {

    MenuTools menuTools;
    TextView noLogged;
    LinearLayout logged;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation);
        getActionBar().hide();

        menuTools = new MenuTools(this, (Button) findViewById(R.id.reservationMenu));
        menuTools.done();

        noLogged = (TextView) findViewById(R.id.noLogged);
        logged = (LinearLayout) findViewById(R.id.logged);

        noLogged.setVisibility(LoginInActivity.isLogged ? View.GONE : View.VISIBLE);
        logged.setVisibility(LoginInActivity.isLogged ? View.VISIBLE : View.GONE);
    }

}
