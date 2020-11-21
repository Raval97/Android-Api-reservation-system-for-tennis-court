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

public class MenuUserAccountTools extends Activity {

    Activity activity;
    Button actualSite;
    static HttpHeaders requestHeaders;

    public MenuUserAccountTools(Activity activity, Button actualSite) {
        this.activity = activity;
        this.actualSite = actualSite;
        setRequestHeaders();
    }

    public MenuUserAccountTools(){
        setRequestHeaders();
    }

    public void done() {
        back();
        menu();
        personalData();
        myReservation();
        clubAssociation();
        payment();
        logOut();
    }

    public void back() {
        Button back = (Button) activity.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(activity, StartPageActivity.class);
                activity.startActivityForResult(myIntent, 0);
            }
        });
    }

    public void menu() {
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

    public void personalData() {
        Button personalDataMenu = (Button) activity.findViewById(R.id.personalDataMenu);
        personalDataMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(activity, UserAccountActivity.class);
                activity.startActivityForResult(myIntent, 0);
            }
        });
    }

    public void myReservation() {
        Button myReservationMenu = (Button) activity.findViewById(R.id.myReservationMenu);
        myReservationMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(activity, UserReservationActivity.class);
                activity.startActivityForResult(myIntent, 0);
            }
        });
    }

    public void clubAssociation() {
        Button clubAssociationMenu = (Button) activity.findViewById(R.id.clubAssociationMenu);
        clubAssociationMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(activity, UserClubAssociationActivity.class);
                activity.startActivityForResult(myIntent, 0);
            }
        });
    }

    public void payment() {
        Button paymentMenu = (Button) activity.findViewById(R.id.paymentMenu);
        paymentMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(activity, UserPaymentActivity.class);
                activity.startActivityForResult(myIntent, 0);
            }
        });
    }

    public void logOut() {
        Button logOutMenu = (Button) activity.findViewById(R.id.logOutMenu);
        logOutMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                LoginInActivity.isLogged = false;
                Intent myIntent = new Intent(activity, StartPageActivity.class);
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
