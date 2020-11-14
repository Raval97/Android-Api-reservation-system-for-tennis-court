package com.example.tenniscourtreservation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import lombok.SneakyThrows;

public class UserClubAssociationActivity extends Activity {

    MenuUserAccountTools menuTools;
    LinearLayout isClubMen;
    LinearLayout isActiveClubMen;
    LinearLayout isNotActiveClubMen;
    LinearLayout isNotClubMen;
    LinearLayout hasActiveApplication;
    LinearLayout notHasActiveApplication;
    LinearLayout hasRejectedApplication;
    Button extendMembershipValidity;
    Button payMembershipFee;
    Button applyForMembership;
    Button applyForMembership2;
    Boolean isClubMenResponse;
    Boolean isActiveClubMenResponse;
    Boolean isRejectedApplication;
    Boolean hasActiveApplicationResponse;
    int daysOfActiveMembership;
    LinearLayout content;
    LinearLayout bank;
    Button cancel;
    Button payInBank;
    TextView counterOfDays;

    Handler mHandler;
    String message;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_club_association);
        getActionBar().hide();

        menuTools = new MenuUserAccountTools(this, (Button) findViewById(R.id.clubAssociationMenu));
        menuTools.done();

        isClubMen = (LinearLayout) findViewById(R.id.isClubMen);
        isActiveClubMen = (LinearLayout) findViewById(R.id.isActiveClubMen);
        isNotActiveClubMen = (LinearLayout) findViewById(R.id.isNotActiveClubMen);
        isNotClubMen = (LinearLayout) findViewById(R.id.isNotClubMen);
        hasActiveApplication = (LinearLayout) findViewById(R.id.hasActiveApplication);
        notHasActiveApplication = (LinearLayout) findViewById(R.id.notHasActiveApplication);
        hasRejectedApplication = (LinearLayout) findViewById(R.id.hasRejectedApplication);
        extendMembershipValidity = (Button) findViewById(R.id.extendMembershipValidity);
        payMembershipFee = (Button) findViewById(R.id.payMembershipFee);
        applyForMembership = (Button) findViewById(R.id.applyForMembership);
        applyForMembership2 = (Button) findViewById(R.id.applyForMembership2);
        TextView description = (TextView) findViewById(R.id.description);
        description.setText(Html.fromHtml(getString(R.string.membership_description)));
        TextView decision = (TextView) findViewById(R.id.decision);
        decision.setText(Html.fromHtml(getString(R.string.decisionClubAssociation)));
        bank = (LinearLayout) findViewById(R.id.bank);
        content = (LinearLayout) findViewById(R.id.content);
        cancel = (Button) findViewById(R.id.cancel);
        payInBank = (Button) findViewById(R.id.payFee);
        counterOfDays = (TextView) findViewById(R.id.counterOfDays);

        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                bank.setVisibility(View.GONE);
                content.setVisibility(View.VISIBLE);
            }
        });

        extendMembershipValidity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                bank.setVisibility(View.VISIBLE);
                content.setVisibility(View.GONE);
            }
        });

        payMembershipFee.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                bank.setVisibility(View.VISIBLE);
                content.setVisibility(View.GONE);
            }
        });

        payInBank.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new HttpReqTaskPayFee().execute();
            }
        });

        applyForMembership.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new HttpReqTaskApply().execute();
            }
        });

        applyForMembership2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new HttpReqTaskApply().execute();
            }
        });

        new HttpReqTaskGetData().execute();

        mHandler = new Handler(Looper.getMainLooper()) {
            @SneakyThrows
            @Override
            public void handleMessage(Message message2) {
                Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
                ((TextView) ((LinearLayout) toast.getView()).getChildAt(0)).setGravity(Gravity.CENTER_HORIZONTAL);
                toast.show();
                bank.setVisibility(View.GONE);
                content.setVisibility(View.VISIBLE);
            }
        };

    }

    private class HttpReqTaskGetData extends AsyncTask<Void, Void, Boolean> {
        @SuppressLint("SetTextI18n")
        @Override
        protected Boolean doInBackground(Void... voids) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                final String url = "http://10.0.2.2:8080/OurTennis/clubAssociation.json";
                RestTemplate restTemplate = menuTools.getDefaultRestTemplate();
                ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(menuTools.requestHeaders), JsonNode.class);
                isClubMenResponse = mapper.convertValue(response.getBody().get("isClubMen"), Boolean.class);
                isActiveClubMenResponse = mapper.convertValue(response.getBody().get("isActiveClubMen"), Boolean.class);
                isRejectedApplication = mapper.convertValue(response.getBody().get("hasRejectedApplication"), Boolean.class);
                hasActiveApplicationResponse = mapper.convertValue(response.getBody().get("hasActiveApplication"), Boolean.class);
                daysOfActiveMembership = mapper.convertValue(response.getBody().get("daysOfActiveMembership"), int.class);
            } catch (RestClientException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean arg) {

            if (isClubMenResponse) {
                isNotClubMen.setVisibility(View.GONE);
                if (isActiveClubMenResponse) {
                    isNotActiveClubMen.setVisibility(View.GONE);
                    counterOfDays.setText("Your club activity will expire in: \n"+ daysOfActiveMembership + " days");
                }
                else
                    isActiveClubMen.setVisibility(View.GONE);
            } else {
                isClubMen.setVisibility(View.GONE);
                if (hasActiveApplicationResponse) {
                    notHasActiveApplication.setVisibility(View.GONE);
                    hasRejectedApplication.setVisibility(View.GONE);
                } else {
                    if (isRejectedApplication)
                        notHasActiveApplication.setVisibility(View.GONE);
                    else
                        hasRejectedApplication.setVisibility(View.GONE);
                    hasActiveApplication.setVisibility(View.GONE);
                }
            }

        }
    }

    private class HttpReqTaskPayFee extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                String url = "http://10.0.2.2:8080/bankSimulatorForMembershipFee.json";
                RestTemplate restTemplate = menuTools.getDefaultRestTemplate();
                ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET,
                        new HttpEntity<Object>(menuTools.requestHeaders), JsonNode.class);
                int id = mapper.convertValue(response.getBody().get("ID"), int.class);
                System.out.println("id = " + id);
                url = "http://10.0.2.2:8080/OurTennis/payMembershipFee/" + id + ".json";
                ResponseEntity<Object> response2 = restTemplate.exchange(url, HttpMethod.GET,
                        new HttpEntity<Object>(menuTools.requestHeaders), Object.class);
            } catch (RestClientException e) {
                e.printStackTrace();
            }
            message = "The payment has been made";
            Message message = mHandler.obtainMessage();
            message.sendToTarget();
            return null;
        }
    }

    private class HttpReqTaskApply extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                final String url = "http://10.0.2.2:8080/OurTennis/applyForMembership.json";
                RestTemplate restTemplate = menuTools.getDefaultRestTemplate();
                ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(menuTools.requestHeaders), Object.class);
            } catch (RestClientException e) {
                e.printStackTrace();
            }
            message = "The application has been sent";
            Message message = mHandler.obtainMessage();
            message.sendToTarget();
            return null;
        }
    }
}

