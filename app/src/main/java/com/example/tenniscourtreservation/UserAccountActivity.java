package com.example.tenniscourtreservation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;

import com.example.tenniscourtreservation.model.Services;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import lombok.SneakyThrows;

public class UserAccountActivity extends Activity {

    MenuUserAccountTools menuTools;
    Handler mHandler;

    Button editDateOption;
    Button changePasswordOption;
    Button deleteAccountOption;

    LinearLayout editDataContainer;
    EditText name;
    EditText surname;
    EditText email;
    EditText phone;
    EditText username;
    Button editData;

    LinearLayout changePasswordContainer;
    EditText actualPassword;
    EditText newPassword;
    EditText repeatPassword;
    Button changePassword;

    LinearLayout deleteAccountContainer;
    Switch deleteSwitch;
    LinearLayout deleteAfterSwitch;
    EditText passwordCheck;
    Button delete;

    String userName;
    String userSurname;
    String userEmail;
    int userPhoneNumber;
    Long userID;
    String message;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_account);
        getActionBar().hide();

        menuTools = new MenuUserAccountTools(this, (Button) findViewById(R.id.personalDataMenu));
        menuTools.done();

        editDateOption = (Button) findViewById(R.id.editDateOption);
        changePasswordOption = (Button) findViewById(R.id.changePasswordOption);
        deleteAccountOption = (Button) findViewById(R.id.deleteAccountOption);
        editDataContainer = (LinearLayout) findViewById(R.id.editDataContainer);
        name = (EditText) findViewById(R.id.name);
        surname = (EditText) findViewById(R.id.surname);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone);
        username = (EditText) findViewById(R.id.username);
        editData = (Button) findViewById(R.id.editData);
        changePasswordContainer = (LinearLayout) findViewById(R.id.changePasswordContainer);
        actualPassword = (EditText) findViewById(R.id.actualPassword);
        newPassword = (EditText) findViewById(R.id.newPassword);
        repeatPassword = (EditText) findViewById(R.id.repeatPassword);
        changePassword = (Button) findViewById(R.id.changePassword);
        deleteAccountContainer = (LinearLayout) findViewById(R.id.deleteAccountContainer);
        deleteSwitch = (Switch) findViewById(R.id.deleteSwitch);
        deleteAfterSwitch = (LinearLayout) findViewById(R.id.deleteAfterSwitch);
        passwordCheck = (EditText) findViewById(R.id.passwordCheck);
        delete = (Button) findViewById(R.id.delete);

        editDateOption.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                editDataContainer.setVisibility(View.VISIBLE);
                changePasswordContainer.setVisibility(View.GONE);
                deleteAccountContainer.setVisibility(View.GONE);
            }
        });

        editData.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                HttpReqTaskEditData async = new HttpReqTaskEditData();
                async.execute();
            }
        });

        changePasswordOption.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                editDataContainer.setVisibility(View.GONE);
                changePasswordContainer.setVisibility(View.VISIBLE);
                deleteAccountContainer.setVisibility(View.GONE);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (LoginInActivity.password.equals(actualPassword.getText().toString())) {
                    if (newPassword.getText().toString().equals(repeatPassword.getText().toString()))
                        new HttpReqTaskChangePassword().execute();
                    else {
                        message = "Repeat Password in not same the new password";
                        Message message = mHandler.obtainMessage();
                        message.sendToTarget();
                    }
                } else {
                    message = "Incorrect Actual Password";
                    Message message = mHandler.obtainMessage();
                    message.sendToTarget();
                }

            }
        });

        deleteAccountOption.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                editDataContainer.setVisibility(View.GONE);
                changePasswordContainer.setVisibility(View.GONE);
                deleteAccountContainer.setVisibility(View.VISIBLE);
            }
        });

        deleteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                deleteAfterSwitch.setVisibility(deleteSwitch.isChecked() ? View.VISIBLE : View.GONE);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (LoginInActivity.password.equals(passwordCheck.getText().toString())) {
                    new HttpReqTaskDeleteAccount().execute();
                } else {
                    message = "Incorrect Password";
                    Message message = mHandler.obtainMessage();
                    message.sendToTarget();
                }
            }
        });

        new UserAccountActivity.HttpReqTaskGetData().execute();

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

    private class HttpReqTaskGetData extends AsyncTask<Void, Void, Boolean> {
        @SuppressLint("SetTextI18n")
        @Override
        protected Boolean doInBackground(Void... voids) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                final String url = MenuTools.startOfUrl + "OurTennis/account/1.json";
                RestTemplate restTemplate = menuTools.getDefaultRestTemplate();
                ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(menuTools.requestHeaders), JsonNode.class);
                userName = mapper.convertValue(response.getBody().get("client").get("name"), String.class);
                userSurname = mapper.convertValue(response.getBody().get("client").get("surname"), String.class);
                userEmail = mapper.convertValue(response.getBody().get("client").get("emailAddress"), String.class);
                userPhoneNumber = mapper.convertValue(response.getBody().get("client").get("phoneNumber"), int.class);
                userID = mapper.convertValue(response.getBody().get("client").get("id"), Long.class);
            } catch (RestClientException e) {
                e.printStackTrace();
            }
            return null;
        }

        @SuppressLint("SetTextI18n")
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(Boolean nothing) {
            name.setText(userName);
            surname.setText(userSurname);
            email.setText(userEmail);
            phone.setText("" + userPhoneNumber);
            username.setText(LoginInActivity.username);
        }
    }

    private class HttpReqTaskEditData extends AsyncTask<Void, Void, Void> {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", name.getText().toString());
                jsonObject.put("surname", surname.getText().toString());
                jsonObject.put("emailAddress", email.getText().toString());
                jsonObject.put("phoneNumber", phone.getText().toString());
                jsonObject.put("username", username.getText().toString());

                URL url = new URL(MenuTools.startOfUrl + "OurTennis/client/edit_data/" + userID);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                String userPassword = LoginInActivity.username + ":" + LoginInActivity.password;
                String encodedAuth = Base64.getEncoder().encodeToString(userPassword.getBytes());
                String authHeaderValue = "Basic " + new String(encodedAuth);
                httpURLConnection.setRequestProperty("Authorization", authHeaderValue);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.connect();

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(jsonObject.toString());
                wr.flush();
                wr.close();

                if (httpURLConnection.getResponseCode() == 200) {
                    message = "Successful update data";
                    Message message = mHandler.obtainMessage();
                    message.sendToTarget();
                    LoginInActivity.username=username.getText().toString();
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class HttpReqTaskChangePassword extends AsyncTask<Void, Void, Void> {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("newPassword", newPassword.getText().toString())
                        .appendQueryParameter("repeatNewPassword", repeatPassword.getText().toString())
                        .appendQueryParameter("oldPassword", actualPassword.getText().toString());
                String queryParams = builder.build().getEncodedQuery();
                System.out.println(queryParams);

                URL url = new URL(MenuTools.startOfUrl + "OurTennis/client/changePassword/" + userID);//+"newPassword=admin2&repeatNewPassword=admin2&oldPassword=admin");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                String userPassword = LoginInActivity.username + ":" + LoginInActivity.password;
                String encodedAuth = Base64.getEncoder().encodeToString(userPassword.getBytes());
                String authHeaderValue = "Basic " + encodedAuth;
                httpURLConnection.setRequestProperty("Authorization", authHeaderValue);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(queryParams);
                wr.flush();
                wr.close();

                if (httpURLConnection.getResponseCode() == 401) {
                    message = "Successful change password";
                    Message message = mHandler.obtainMessage();
                    message.sendToTarget();
                    LoginInActivity.password=newPassword.getText().toString();
                }

            } catch (IOException  e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class HttpReqTaskDeleteAccount extends AsyncTask<Void, Void, Void> {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String url = MenuTools.startOfUrl + "OurTennis/client/deleteAccount/" + userID + ".json";
                RestTemplate restTemplate = menuTools.getDefaultRestTemplate();
                ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(menuTools.requestHeaders), Object.class);
            } catch (RestClientException e) {
                e.printStackTrace();
            }
            LoginInActivity.isLogged = false;
            Intent intent = new Intent(getApplicationContext(), StartPageActivity.class);
            startActivity(intent);
            finish();
            return null;
        }
    }

}


