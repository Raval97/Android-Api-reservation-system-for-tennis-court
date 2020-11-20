package com.example.tenniscourtreservation;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tenniscourtreservation.model.Tournament;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URL;

import lombok.SneakyThrows;

public class LoginInActivity extends Activity {

    static String username = "admin";
    static String password = "admin";
    static Boolean isLogged = true;

    TextView loginText;
    Button back;
    Button loginButton;
    EditText usernameText;
    EditText passwordText;
    Handler mHandler;
    String message;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        getActionBar().hide();

        loginText = (TextView) findViewById(R.id.loginText);
        back = (Button) findViewById(R.id.back);
        loginButton = (Button) findViewById(R.id.login);
        usernameText = (EditText) findViewById(R.id.username);
        passwordText = (EditText) findViewById(R.id.password);

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), StartPageActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                System.out.println("ffdgdsfgfsgh");
                username = usernameText.getText().toString();
                password = passwordText.getText().toString();
                usernameText.setText("");
                passwordText.setText("");
                new LoginInActivity.HttpReqTask().execute();
            }
        });

        mHandler = new Handler(Looper.getMainLooper()) {
            @SneakyThrows
            @Override
            public void handleMessage(Message message2) {
                Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
                ((TextView) ((LinearLayout) toast.getView()).getChildAt(0)).setGravity(Gravity.CENTER_HORIZONTAL);
                toast.show();
            }
        };
    }

    private class HttpReqTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                final String url = MenuTools.startOfUrl + "OurTennis/login.json";
                MenuTools menuTools = new MenuTools();
                RestTemplate restTemplate = menuTools.getDefaultRestTemplate();
                ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(menuTools.requestHeaders), JsonNode.class);
                isLogged = mapper.convertValue(response.getBody().get("authorization"), Boolean.class);
                Intent myIntent = new Intent(getApplicationContext(), StartPageActivity.class);
                startActivityForResult(myIntent, 0);
                message = "Successful Authorization";
                Message message = mHandler.obtainMessage();
                message.sendToTarget();
                isLogged = true;
            } catch (RestClientException e) {
                e.printStackTrace();
                message = "Incorrect Authorization";
                Message message = mHandler.obtainMessage();
                message.sendToTarget();
            }
            return null;
        }
    }
}