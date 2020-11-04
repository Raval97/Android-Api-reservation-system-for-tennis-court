package com.example.tenniscourtreservation;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.tenniscourtreservation.model.Tournament;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;

public class EventActivity extends Activity {

    MenuTools menuTools;
    Boolean logged;
    Boolean isAdmin;
    Tournament[] tournaments;
    String[] userStatusList;

    TableLayout contextTable;
    TableLayout eventExample;
    TableRow eventRowExample;
    TextView eventTitleExample;
    TextView eventDateExample;
    Button showDetailsExample;
    TableLayout eventDetailsExample;
    TableRow eventDetailsRowExample;
    TextView cellOfEventDetailsRowExample;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events);
        getActionBar().hide();

        menuTools = new MenuTools(this, (Button) findViewById(R.id.eventsMenu));
        menuTools.done();

        new EventActivity.HttpReqTask(this).execute();

    }


    private class HttpReqTask extends AsyncTask<Void, Void, Tournament[]> {
        Activity activity;

        public HttpReqTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected Tournament[] doInBackground(Void... voids) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(new URL("http://10.0.2.2:8080/ourTennis/events.json"));
                tournaments = mapper.convertValue(jsonNode.get("tournaments"), Tournament[].class);
                userStatusList = mapper.convertValue(jsonNode.get("userStatusList"), String[].class);
                logged = mapper.convertValue(jsonNode.get("logged"), Boolean.class);
                isAdmin = mapper.convertValue(jsonNode.get("isAdmin"), Boolean.class);
                return tournaments;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Tournament[] tournaments) {
            super.onPostExecute(tournaments);
            contextTable = (TableLayout) findViewById(R.id.table);
            eventExample = (TableLayout) findViewById(R.id.event);
            eventRowExample = (TableRow) findViewById(R.id.eventRow);
            eventTitleExample = (TextView) findViewById(R.id.eventTitle);
            eventDateExample = (TextView) findViewById(R.id.eventDate);
            showDetailsExample = (Button) findViewById(R.id.showDetails);
            eventDetailsExample = (TableLayout) findViewById(R.id.eventDetails);
            eventDetailsRowExample = (TableRow) findViewById(R.id.dateOfStartedRow);
            cellOfEventDetailsRowExample = (TextView) findViewById(R.id.firstCellDateOfStartedRow);
            int iter = 0;
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) eventExample.getLayoutParams();
            for (Tournament t : tournaments) {
                if (iter % 2 == 0)
                    contextTable.addView(createTableForEvent(activity, t, "#776074", R.style.eventDetails,
                            R.style.eventsListTableCell, R.style.eventsListTableRow, R.style.eventTableCell), params);
                else
                    contextTable.addView(createTableForEvent(activity, t, "#913860", R.style.eventDetails,
                            R.style.eventsListTableCell, R.style.eventsListTableRow, R.style.eventTableCell), params);
                iter++;
                contextTable.removeView(eventExample);
            }
        }
    }

    private TableLayout createTableForEvent(Activity activity, Tournament t, String cellColor, int  eventDetailsStyle,
                                            int eventsCellStyle, int EventsRowStyle, int EventCellStyle) {
        TableLayout event = new TableLayout(activity);
        TableRow eventRow = new TableRow(new ContextThemeWrapper(activity, EventsRowStyle));
        TextView eventTitle = new TextView(new ContextThemeWrapper(activity, eventsCellStyle));
        TextView eventDate = new TextView(new ContextThemeWrapper(activity, eventsCellStyle));
        Button showDetails = new Button(activity);
        TableLayout eventDetails = new TableLayout(new ContextThemeWrapper(activity, eventDetailsStyle));

        eventRow.setBackgroundColor(Color.parseColor(cellColor));
        showDetails.setBackgroundColor(Color.parseColor(cellColor));

        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) eventRowExample.getLayoutParams();
        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) eventTitleExample.getLayoutParams();
        LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams) showDetailsExample.getLayoutParams();
        LinearLayout.LayoutParams params4 = (LinearLayout.LayoutParams) eventDetailsExample.getLayoutParams();
        LinearLayout.LayoutParams params5 = (LinearLayout.LayoutParams) eventDetailsRowExample.getLayoutParams();

        eventTitle.setText(String.valueOf(t.getTitle()));
        eventDate.setText(String.valueOf(t.getDateOfStarted()));
        showDetails.setText("V");
        eventRow.addView(eventTitle, params2);
        eventRow.addView(eventDate, params3);
        eventRow.addView(showDetails, params3);

        eventDetails.addView(createRowOfTable(activity, "Date of started:",
                String.valueOf(t.getDateOfStarted()), EventCellStyle), params5);
        eventDetails.addView(createRowOfTable(activity, "Date of ended:",
                String.valueOf(t.getDateOfEnded()), EventCellStyle), params5);
        eventDetails.addView(createRowOfTable(activity, "Max count of participants:",
                String.valueOf(t.getMaxCountOFParticipant()), EventCellStyle), params5);
        eventDetails.addView(createRowOfTable(activity, "Date of started:",
                String.valueOf(t.getDateOfStarted()), EventCellStyle), params5);
        eventDetails.addView(createRowOfTable(activity, "Actual registered participants:",
                String.valueOf(t.getCountOFRegisteredParticipant()), EventCellStyle), params5);
        eventDetails.addView(createRowOfTable(activity, "Entry fee:",
                String.valueOf(t.getEntryFee()), EventCellStyle), params5);

        event.addView(eventRow, params1);
        event.addView(eventDetails, params4);
        event.setPadding(0, 0 ,0, 5);

        return event;
    }

    private TableRow createRowOfTable(Activity activity, String text1, String text2, int cellStyle) {
        TableRow row = new TableRow(activity);
        TextView cell1 = new TextView(new ContextThemeWrapper(activity, cellStyle));
        TextView cell2 = new TextView(new ContextThemeWrapper(activity, cellStyle));
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) cellOfEventDetailsRowExample.getLayoutParams();
        cell1.setText(text1);
        cell2.setText(text2);
        row.addView(cell1, params);
        row.addView(cell2, params);
        return row;
    }
}
