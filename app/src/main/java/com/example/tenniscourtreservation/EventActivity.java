package com.example.tenniscourtreservation;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

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
import java.time.LocalDate;


public class EventActivity extends Activity {

    MenuTools menuTools;
    Tournament[] tournaments;
    String[] userStatusList;

    TableLayout contextTable;
    TableLayout eventExample;
    int eventDetailsStyle;
    int eventsCellStyle;
    int eventsRowStyle;
    int eventCellStyle;
    int eventInfoTextStyle;
    int eventActionButtonStyle;
    LinearLayout.LayoutParams cellOfRowEventDetailsParam;
    LinearLayout.LayoutParams eventRowParam;
    LinearLayout.LayoutParams eventCellRowParam;
    LinearLayout.LayoutParams showDetailsParam;
    LinearLayout.LayoutParams eventDetailsParam;
    LinearLayout.LayoutParams rowEventDetailsParam;
    LinearLayout.LayoutParams infoEventParam;
    LinearLayout.LayoutParams actionEventParam;
    String infoEventText = "";
    String actionEventText = "";
    String actionEventText2 = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events);
        getActionBar().hide();

        menuTools = new MenuTools(this, (Button) findViewById(R.id.eventsMenu));
        menuTools.done();

        new EventActivity.HttpReqTask().execute();

    }

    private class HttpReqTask extends AsyncTask<Void, Void, Tournament[]> {

        @Override
        protected Tournament[] doInBackground(Void... voids) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                if (LoginInActivity.isLogged) {
                    try {
                        final String url = "http://10.0.2.2:8080/ourTennis/events.json";
                        HttpAuthentication authHeader = new HttpBasicAuthentication(LoginInActivity.username, LoginInActivity.password);
                        HttpHeaders requestHeaders = new HttpHeaders();
                        requestHeaders.setAuthorization(authHeader);
                        RestTemplate restTemplate = new RestTemplate();
                        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
                        restTemplate.getMessageConverters().add(messageConverter);
                        ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(requestHeaders), JsonNode.class);
                        tournaments = mapper.convertValue(response.getBody().get("tournaments"), Tournament[].class);
                        userStatusList = mapper.convertValue(response.getBody().get("userStatusList"), String[].class);
                        return tournaments;
                    } catch (RestClientException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                } else {
                    JsonNode jsonNode = mapper.readTree(new URL("http://10.0.2.2:8080/ourTennis/events.json"));
                    tournaments = mapper.convertValue(jsonNode.get("tournaments"), Tournament[].class);
                    return tournaments;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(Tournament[] tournaments) {
            super.onPostExecute(tournaments);
            contextTable = (TableLayout) findViewById(R.id.table);
            eventExample = (TableLayout) findViewById(R.id.event);
            eventDetailsStyle = R.style.eventDetails;
            eventsCellStyle = R.style.eventsListTableCell;
            eventsRowStyle = R.style.eventsListTableRow;
            eventCellStyle = R.style.eventTableCell;
            eventInfoTextStyle = R.style.infoText;
            eventActionButtonStyle = R.style.actionButton;
            eventRowParam = (LinearLayout.LayoutParams) findViewById(R.id.eventRow).getLayoutParams();
            eventCellRowParam = (LinearLayout.LayoutParams) findViewById(R.id.eventTitle).getLayoutParams();
            showDetailsParam = (LinearLayout.LayoutParams) findViewById(R.id.showDetails).getLayoutParams();
            eventDetailsParam = (LinearLayout.LayoutParams) findViewById(R.id.eventDetails).getLayoutParams();
            rowEventDetailsParam = (LinearLayout.LayoutParams) findViewById(R.id.dateOfStartedRow).getLayoutParams();
            cellOfRowEventDetailsParam = (LinearLayout.LayoutParams) findViewById(R.id.firstCellDateOfStartedRow).getLayoutParams();
            infoEventParam = (LinearLayout.LayoutParams) findViewById(R.id.infoText).getLayoutParams();
            actionEventParam = (LinearLayout.LayoutParams) findViewById(R.id.actionButton).getLayoutParams();

            int iter = 0;
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) eventExample.getLayoutParams();
            params.setMargins(0, 0, 0, 20);
            for (Tournament t : tournaments) {
                if (t.getDateOfStarted().isAfter(LocalDate.now())) {
                    if (LoginInActivity.isLogged) {
                        if (userStatusList[iter].equals("Accepted")) {
                            infoEventText = "You are a participant of the event.\n" +
                                    "Here you can cancel your participation";
                            actionEventText = "Cancel from participation";
                        }
                        if (userStatusList[iter].equals("Delivered")) {
                            infoEventText = "You have sent your application for participation\n" +
                                    "Currently under review by the admin";
                            actionEventText = "Cancel from participation";
                        }
                        if (userStatusList[iter].equals("To Pay")) {
                            infoEventText = "Your application has been approved by the admin.\n" +
                                    "To fully confirm participation, pay the fee";
                            actionEventText = "Pay Fee Of Participant";
                            actionEventText2 = "Cancel your application";
                        }
                        if (userStatusList[iter].equals("without_application") ||
                                userStatusList[iter].equals("Rejected") ||
                                userStatusList[iter].equals("Canceled")) {
                            if (t.getMaxCountOFParticipant() == t.getCountOFRegisteredParticipant())
                                infoEventText = "Full set of participants,\n you cannot register";
                            else {
                                if (userStatusList[iter].equals("Rejected"))
                                    infoEventText = "Your application has been rejected.\n" +
                                            "There is free space left. You can re-apply";
                                if (userStatusList[iter].equals("without_application"))
                                    infoEventText = "There is free space left.\n" +
                                            "You can register for the event below";
                            }
                            actionEventText = "Take Part In This Event";
                        }
                    } else {
                        infoEventText = "Login required to join";
                        actionEventText = "Log In";
                    }
                } else
                    infoEventText = "The Event is terminated";

                if (iter % 2 == 0)
                    contextTable.addView(createTableForEvent(t, "#776074"), params);
                else
                    contextTable.addView(createTableForEvent(t, "#913860"), params);
                iter++;
                contextTable.removeView(eventExample);
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private TableLayout createTableForEvent(Tournament t, String cellColor) {
        TableLayout event = new TableLayout(getApplicationContext());
        TableRow eventRow = new TableRow(new ContextThemeWrapper(getApplicationContext(), eventsRowStyle));
        Button showDetails = new Button(this);
        TableRow emptyRow = new TableRow(getApplicationContext());
        TextView eventTitle = new TextView(new ContextThemeWrapper(getApplicationContext(), eventsCellStyle));
        TextView eventDate = new TextView(new ContextThemeWrapper(getApplicationContext(), eventsCellStyle));
        TableLayout eventDetails = new TableLayout(new ContextThemeWrapper(getApplicationContext(), eventDetailsStyle));
        TableRow eventInfo = new TableRow(getApplicationContext());
        TextView eventInfoText = new TextView(new ContextThemeWrapper(getApplicationContext(), eventInfoTextStyle));
        TableRow eventAction = new TableRow(getApplicationContext());
        Button eventActionButton = new Button(getApplicationContext(), null, 0, eventActionButtonStyle);

        eventRow.setBackgroundColor(Color.parseColor(cellColor));
        eventDetails.setBackgroundColor(Color.parseColor(cellColor));

        eventTitle.setText(String.valueOf(t.getTitle()));
        String eventDateText = t.getDateOfStarted().isBefore(LocalDate.now()) ? "Terminated" : t.getDateOfStarted().toString();
        eventDate.setText(eventDateText);
        showDetails.setText("-> <-");
        eventRow.addView(eventTitle, eventCellRowParam);
        eventRow.addView(eventDate, eventCellRowParam);
        eventRow.addView(showDetails, showDetailsParam);
        emptyRow.setBackgroundColor(Color.parseColor("#BC1319"));
        emptyRow.setPadding(0, 0, 0, 3);

        eventDetails.addView(createRowOfTable("Date of started:",
                String.valueOf(t.getDateOfStarted())), rowEventDetailsParam);
        eventDetails.addView(createRowOfTable("Date of ended:",
                String.valueOf(t.getDateOfEnded())), rowEventDetailsParam);
        eventDetails.addView(createRowOfTable("Max count of participants:",
                String.valueOf(t.getMaxCountOFParticipant())), rowEventDetailsParam);
        eventDetails.addView(createRowOfTable("Date of started:",
                String.valueOf(t.getDateOfStarted())), rowEventDetailsParam);
        eventDetails.addView(createRowOfTable("Actual registered participants:",
                String.valueOf(t.getCountOFRegisteredParticipant())), rowEventDetailsParam);
        eventDetails.addView(createRowOfTable("Entry fee:",
                String.valueOf(t.getEntryFee())), rowEventDetailsParam);

        eventInfoText.setText(infoEventText);
        eventInfoText.setGravity(Gravity.CENTER);
        eventInfo.addView(eventInfoText, infoEventParam);
        eventInfo.setGravity(Gravity.CENTER);
        eventInfo.setPadding(0, 50, 0, 30);
        eventActionButton.setText(actionEventText);
        eventAction.addView(eventActionButton);
        eventAction.setGravity(Gravity.CENTER);
        eventDetails.addView(eventInfo, rowEventDetailsParam);
        eventDetails.addView(eventAction, rowEventDetailsParam);
        eventDetails.setVisibility(View.GONE);

        showDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventDetails.setVisibility((eventDetails.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE);
                showDetails.setText((eventDetails.getVisibility() == View.GONE) ? "<- ->" : "-> <-");
            }
        });

        event.addView(eventRow, eventRowParam);
        event.addView(emptyRow);
        event.addView(eventDetails, eventDetailsParam);
        event.setPadding(0, 0, 0, 5);

        return event;
    }

    private TableRow createRowOfTable(String text1, String text2) {
        TableRow row = new TableRow(getApplicationContext());
        TextView cell1 = new TextView(new ContextThemeWrapper(getApplicationContext(), eventCellStyle));
        TextView cell2 = new TextView(new ContextThemeWrapper(getApplicationContext(), eventCellStyle));
        cell1.setText(text1);
        cell2.setText(text2);
        row.addView(cell1, cellOfRowEventDetailsParam);
        row.addView(cell2, cellOfRowEventDetailsParam);
        row.setPadding(5, 5, 5, 5);
        return row;
    }
}

//private HashMap<String, Integer> idMap= new HashMap<String, Integer>();
//idMap.put("showButton" + index, index*10);
//showDetails.setId(index);