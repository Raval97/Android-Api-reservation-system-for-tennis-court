package com.example.tenniscourtreservation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class MenuTools extends Activity {

    Activity activity;
    Button actualSite;
    static HttpHeaders requestHeaders = null;
    static String startOfUrl = "http://10.0.2.2:8080/";

    public MenuTools(Activity activity, Button actualSite) {
        this.activity = activity;
        this.actualSite = actualSite;
        if(LoginInActivity.isLogged) {
            Button login = (Button) activity.findViewById(R.id.login);
            login.setText("Account");
        }
        setRequestHeaders();
    }

    public MenuTools() {
    }

    public void done(){
        login();
        menu();
        startPage();
        reservation();
        events();
        priceList();
        gallery();
        contact();
    }

    public void login() {
        Button login = (Button) activity.findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(LoginInActivity.isLogged) {
                    Intent myIntent = new Intent(activity, UserAccountActivity.class);
                    activity.startActivityForResult(myIntent, 0);
                } else {
                    Intent myIntent = new Intent(activity, LoginInActivity.class);
                    activity.startActivityForResult(myIntent, 0);
                }
            }
        });
    }

    public void  menu() {
        final LinearLayout menu = (LinearLayout) activity.findViewById(R.id.menu);
        final Button menuButton = (Button) activity.findViewById(R.id.menuButton);
        final ViewGroup container = (ViewGroup) activity.findViewById(R.id.menuContainer);

        menuButton.setOnClickListener(new View.OnClickListener() {
            boolean visible;
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                AutoTransition autoTransition = new AutoTransition();
                autoTransition.setDuration(300);
                visible = !visible;
                TransitionManager.beginDelayedTransition(container, autoTransition);
                menu.setVisibility(visible ? View.VISIBLE : View.GONE);
                if (!visible)
                    menuButton.setBackgroundResource(R.drawable.button_menu_bg);
                else
                    menuButton.setBackgroundColor(Color.parseColor("#3700B3"));
                menuButton.setText(visible ? "..." : "Menu");
                actualSite.setTextColor(Color.parseColor("#9480C6"));
            }
        });
    }

    public void startPage() {
        Button startPageMenu = (Button) activity.findViewById(R.id.startMenu);
        startPageMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(activity, StartPageActivity.class);
                activity.startActivityForResult(myIntent, 0);
            }
        });
    }

    public void reservation() {
        Button reservationMenu = (Button) activity.findViewById(R.id.reservationMenu);
        reservationMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(activity, ReservationActivity.class);
                activity.startActivityForResult(myIntent, 0);
            }
        });
    }

    public void events() {
        Button eventsMenu = (Button) activity.findViewById(R.id.eventsMenu);
        eventsMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(activity, EventActivity.class);
                activity.startActivityForResult(myIntent, 0);
            }
        });
    }

    public void priceList() {
        Button priceListMenu = (Button) activity.findViewById(R.id.priceListMenu);
        priceListMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(activity, PriceListActivity.class);
                activity.startActivityForResult(myIntent, 0);
            }
        });
    }

    public void gallery() {
        Button galleryMenu = (Button) activity.findViewById(R.id.galleryMenu);
        galleryMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(activity, GalleryActivity.class);
                activity.startActivityForResult(myIntent, 0);
            }
        });
    }

    public void contact() {
        Button contactMenu = (Button) activity.findViewById(R.id.contactMenu);
        contactMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(activity, ContactActivity.class);
                activity.startActivityForResult(myIntent, 0);
            }
        });
    }

    public RestTemplate getDefaultRestTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        restTemplate.getMessageConverters().add(messageConverter);
        return restTemplate;
    }

    public void setRequestHeaders(){
        HttpAuthentication authHeader = new HttpBasicAuthentication(LoginInActivity.username, LoginInActivity.password);
        requestHeaders = new HttpHeaders();
        requestHeaders.setAuthorization(authHeader);
    }

}
