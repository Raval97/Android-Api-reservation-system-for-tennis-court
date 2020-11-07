package com.example.tenniscourtreservation;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.annotation.RequiresApi;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

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

        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                new FetchSecuredResourceTask().execute();
            }
        });
    }


    private class FetchSecuredResourceTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                final String url = "http://10.0.2.2:8080/OurTennis/reservation.json";
                HttpAuthentication authHeader = new HttpBasicAuthentication(LoginInActivity.username, LoginInActivity.password);
                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setAuthorization(authHeader);
                RestTemplate restTemplate = new RestTemplate();
                MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
                restTemplate.getMessageConverters().add(messageConverter);
                ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(requestHeaders), JsonNode.class);
                System.out.println(response.getBody().toString());
                System.out.println(response.getBody().get("isAdmin").toString());
            } catch (ResourceAccessException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

}
