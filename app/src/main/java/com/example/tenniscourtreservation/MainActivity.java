package com.example.tenniscourtreservation;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.annotation.RequiresApi;
import com.example.tenniscourtreservation.model.PriceList;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URL;

public class MainActivity extends Activity {

    LinearLayout menu;
    Button login;
    Button menuButton;
    ViewGroup tContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().hide();


        // Login Clicked
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), LoginInActivity.class);
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
                //#################################
                new HttpReqTask().execute();
                //#################################
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

    private class HttpReqTask extends AsyncTask<Void, Void, PriceList[]> {
        @Override
        protected PriceList[] doInBackground(Void... voids) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(new URL("http://10.0.2.2:8080/ourTennis/priceList.json"));
                PriceList[] priceList = mapper.convertValue(jsonNode.get("priceList"), PriceList[].class);
                Boolean b1 = mapper.convertValue(jsonNode.get("logged"), Boolean.class);
                Boolean b2 = mapper.convertValue(jsonNode.get("isAdmin"), Boolean.class);
                System.out.println(b1 + " "+ b2+"\n#################");
                return priceList;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(PriceList[] priceLists) {
            super.onPostExecute(priceLists);
            for (PriceList p : priceLists) {
                Log.i("PriceList: ", "#######################");
                Log.i("PriceList id: ", String.valueOf(p.getId()));
                Log.i("PriceList price: ", String.valueOf(p.getPrice()));
                Log.i("PriceList time: ", String.valueOf(p.getTime()));
                Log.i("PriceList type: ", String.valueOf(p.getType()));
            }
        }
    }

}
