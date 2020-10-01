package com.example.tenniscourtreservation;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;

public class MainActivity extends Activity {

    LinearLayout menu;
    Button login;
    Button menuButton;
    ViewGroup tContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getActionBar().hide();

        // Login Clicked
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), LogInActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

        // Menu Clicked
        tContainer = (ViewGroup) findViewById(R.id.container);
        menu = findViewById(R.id.menu);
        menuButton = (Button) findViewById(R.id.menuButton);
        menuButton.setOnClickListener(new View.OnClickListener() {
            boolean visible;
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                AutoTransition autoTransition = new AutoTransition();
                autoTransition.setDuration(500);
                visible = !visible;
                TransitionManager.beginDelayedTransition(tContainer, autoTransition);
                menu.setVisibility(visible ? View.VISIBLE : View.GONE);
                menuButton.setBackgroundResource(visible ? R.drawable.button_menu_clicked_bg : R.drawable.button_menu_bg);
                menuButton.setText(visible ? "..." : "Menu");
            }
        });

    }

}