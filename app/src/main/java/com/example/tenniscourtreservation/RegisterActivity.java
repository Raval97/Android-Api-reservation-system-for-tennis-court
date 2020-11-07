package com.example.tenniscourtreservation;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import lombok.SneakyThrows;

public class RegisterActivity extends Activity {

    TextView logInText;
    Button back;
    EditText name;
    EditText surname;
    EditText email;
    EditText phoneNumber;
    EditText username;
    EditText password;
    Button register;
    Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        getActionBar().hide();

        logInText = (TextView) findViewById(R.id.logInText);
        back = (Button) findViewById(R.id.back);
        name = (EditText) findViewById(R.id.name);
        surname = (EditText) findViewById(R.id.surname);
        email = (EditText) findViewById(R.id.email);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        register = (Button) findViewById(R.id.register);

        logInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginInActivity.class);
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

        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AsyncT asyncT = new AsyncT();
                asyncT.execute();
            }
        });

        mHandler = new Handler(Looper.getMainLooper()) {
            @SneakyThrows
            @Override
            public void handleMessage(Message message) {
                Intent intent = new Intent(getApplicationContext(), LoginInActivity.class);
                startActivity(intent);
                finish();
                Toast toast = Toast.makeText(getApplicationContext(),
                        "User create successful! \n Now you can log in on your account",
                        Toast.LENGTH_LONG);
                ((TextView) ((LinearLayout) toast.getView()).getChildAt(0))
                        .setGravity(Gravity.CENTER_HORIZONTAL);
                toast.show();
            }
        };
    }

    private class AsyncT extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", name.getText().toString());
                jsonObject.put("surname", surname.getText().toString());
                jsonObject.put("emailAddress", email.getText().toString());
                jsonObject.put("phoneNumber", phoneNumber.getText().toString());
                jsonObject.put("username", username.getText().toString());
                jsonObject.put("password", password.getText().toString());

                URL url = new URL("http://10.0.2.2:8080/add-user");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.connect();

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(jsonObject.toString());
                wr.flush();
                wr.close();

                if (httpURLConnection.getResponseCode() == 200) {
                    Message message = mHandler.obtainMessage();
                    message.sendToTarget();
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}