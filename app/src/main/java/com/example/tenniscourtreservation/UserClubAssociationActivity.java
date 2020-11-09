package com.example.tenniscourtreservation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

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

        new HttpReqTask().execute();

    }

    private class HttpReqTask extends AsyncTask<Void, Void, Boolean> {
        @SuppressLint("SetTextI18n")
        @Override
        protected Boolean doInBackground(Void... voids) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                final String url = "http://10.0.2.2:8080/OurTennis/clubAssociation.json";
                HttpAuthentication authHeader = new HttpBasicAuthentication(LoginInActivity.username, LoginInActivity.password);
                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setAuthorization(authHeader);
                RestTemplate restTemplate = new RestTemplate();
                MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
                restTemplate.getMessageConverters().add(messageConverter);
                ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(requestHeaders), JsonNode.class);
                isClubMenResponse = mapper.convertValue(response.getBody().get("isClubMen"), Boolean.class);
                isActiveClubMenResponse = mapper.convertValue(response.getBody().get("isActiveClubMen"), Boolean.class);
                isRejectedApplication = mapper.convertValue(response.getBody().get("isRejectedApplication"), Boolean.class);
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
                if (isActiveClubMenResponse)
                    isNotActiveClubMen.setVisibility(View.GONE);
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

}
