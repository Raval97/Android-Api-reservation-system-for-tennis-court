package com.example.tenniscourtreservation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.tenniscourtreservation.model.Services;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationActivity extends Activity {

    MenuTools menuTools;
    TextView noLogged;
    LinearLayout logged;

    HorizontalScrollView calendarContainer;
    LinearLayout calendarExample;
    Button dayExample;
    Button showLegend;
    TableLayout legend;

    TableLayout summaryOfReservation;
    TableRow exampleRowSummary;
    TextView priceText;

    TableLayout tableReservation;
    TableRow exampleRowOfTableReservation;

    int selectDay = 0;
    Services[] startedReservationServices;
    float price;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation);
        getActionBar().hide();

        menuTools = new MenuTools(this, (Button) findViewById(R.id.reservationMenu));
        menuTools.done();

        noLogged = (TextView) findViewById(R.id.noLogged);
        logged = (LinearLayout) findViewById(R.id.logged);
        noLogged.setVisibility(LoginInActivity.isLogged ? View.GONE : View.VISIBLE);
        logged.setVisibility(LoginInActivity.isLogged ? View.VISIBLE : View.GONE);

        calendarContainer = (HorizontalScrollView) findViewById(R.id.calendar);
        calendarExample = (LinearLayout) findViewById(R.id.calendarExample);
        dayExample = (Button) findViewById(R.id.dayExample);
        createSchedule();

        legend = (TableLayout) findViewById(R.id.legend);
        showLegend = (Button) findViewById(R.id.showLegend);
        showLegend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AutoTransition autoTransition = new AutoTransition();
                autoTransition.setDuration(300);
                TransitionManager.beginDelayedTransition(legend, autoTransition);
                showLegend.setText(legend.getVisibility() == View.GONE ? " Info   -> <-" : " Info   <-->");
                legend.setVisibility(legend.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });

        summaryOfReservation = (TableLayout) findViewById(R.id.summaryOfReservation);
        exampleRowSummary = (TableRow) findViewById(R.id.exampleRowOfSummary);
        priceText = (TextView) findViewById(R.id.price);
        new HttpReqTaskGetData().execute();

        tableReservation = (TableLayout) findViewById(R.id.tableReservation);
        exampleRowOfTableReservation = (TableRow) findViewById(R.id.exampleRowOfTableReservation);
        createTableOfReservations();

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createSchedule() {
        LinearLayout schedule = new LinearLayout(this);
        FrameLayout.LayoutParams scheduleExampleParam = (FrameLayout.LayoutParams) calendarExample.getLayoutParams();
        int dayExampleStyle = R.style.calendarDayButton;
        LinearLayout.LayoutParams dayExampleParam = (LinearLayout.LayoutParams) dayExample.getLayoutParams();
        LocalDate date = LocalDate.now();
        String[] weekday = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        for (int i = 0; i < 28; i++) {
            Button day = new Button(getApplicationContext(), null, 0, dayExampleStyle);
            day.setText(weekday[date.getDayOfWeek().getValue() - 1] + "\n" + date.toString());
            if (date.getDayOfWeek().getValue() > 5) {
                day.setBackgroundResource(R.drawable.border_style_2);
                day.setTextColor(Color.parseColor("#39C1DA"));
            }
            if (selectDay == i) {
                if (date.getDayOfWeek().getValue() > 5)
                    day.setBackgroundResource(R.drawable.border_style_4);
                else
                    day.setBackgroundResource(R.drawable.border_style_3);
                dayExample = day;
            }
            schedule.addView(day, dayExampleParam);
            date = date.plusDays(1L);

            int finalIter = i;
            day.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    selectDay = finalIter;
                    createSchedule();
                }
            });
        }
        calendarContainer.removeAllViews();
        calendarContainer.addView(schedule, scheduleExampleParam);
        calendarExample = schedule;
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createTableOfReservations() {
        int rowOfTable = R.style.rowOfTable;
        int timeAsCellOfTable = R.style.timeAsCellOfSchedule;
        int cellOfTable = R.style.cellOfSchedule;
        LinearLayout.LayoutParams rowParam = (LinearLayout.LayoutParams)
                exampleRowOfTableReservation.getLayoutParams();
        LinearLayout.LayoutParams cellParam = (LinearLayout.LayoutParams)
                findViewById(R.id.exampleCellOfRow).getLayoutParams();
        LocalTime time = LocalTime.of(6,0);

        tableReservation.removeView(exampleRowOfTableReservation);
        for (int i = 0; i < 48; i++) {
            TableRow row = new TableRow(new ContextThemeWrapper(getApplicationContext(), rowOfTable));
            Button timeCell = new Button(getApplicationContext(), null, 0, timeAsCellOfTable);
            timeCell.setText(time.toString());
            row.addView(timeCell, cellParam);
            for (int j = 0; j < 4; j++) {
                Button cell = new Button(getApplicationContext(), null, 0, cellOfTable);
                row.addView(cell, cellParam);
            }
            if (i == 15 || i == 33) {
                rowParam.setMargins(0, 0, 0, 100);
                tableReservation.addView(row, rowParam);
            }
            else
                tableReservation.addView(row);
            time = time.plusMinutes(30L);
        }

    }

    private class HttpReqTaskGetData extends AsyncTask<Void, Void, Services[]> {

        @Override
        protected Services[] doInBackground(Void... voids) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                final String url = "http://10.0.2.2:8080/OurTennis/reservation.json";
                RestTemplate restTemplate = menuTools.getDefaultRestTemplate();
                ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(menuTools.requestHeaders), JsonNode.class);
                startedReservationServices = mapper.convertValue(response.getBody().get("startedReservationServices"), Services[].class);
                price = mapper.convertValue(response.getBody().get("startedReservation").get("price"), float.class);
            } catch (RestClientException | IllegalArgumentException e) {
                e.printStackTrace();
            }
            return startedReservationServices;
        }

        @SuppressLint("SetTextI18n")
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(Services[] serviceList) {
            super.onPostExecute(serviceList);
            LinearLayout.LayoutParams rowParam = (LinearLayout.LayoutParams)
                    exampleRowSummary.getLayoutParams();
            rowParam.setMargins(0,0,0,10);
            int iter = 0;
            for (Services s : serviceList) {
                if (iter % 2 == 0)
                    summaryOfReservation.addView(createTableForSummary(s, "#1D8136", iter+1), rowParam);
                else
                    summaryOfReservation.addView(createTableForSummary(s, "#1B5129", iter+1), rowParam);
                iter++;
            }
            summaryOfReservation.removeView(exampleRowSummary);
            priceText.setText("Price:   " + String.valueOf(price));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TableRow createTableForSummary(Services s, String cellColor, int iter){
        int cellOfTable = R.style.celOfRowOfSummary;
        LinearLayout.LayoutParams cell_01_Param = (LinearLayout.LayoutParams) findViewById(R.id.exampleSummaryCell_01).getLayoutParams();
        LinearLayout.LayoutParams cell_012_Param = (LinearLayout.LayoutParams) findViewById(R.id.exampleSummaryCell_012).getLayoutParams();
        LinearLayout.LayoutParams cell_02_Param = (LinearLayout.LayoutParams) findViewById(R.id.exampleSummaryCell_02).getLayoutParams();
        LinearLayout.LayoutParams cell_005_Param = (LinearLayout.LayoutParams) findViewById(R.id.exampleSummaryCell_005).getLayoutParams();
        TableRow row = new TableRow(this);
        TextView nr = new TextView(new ContextThemeWrapper(getApplicationContext(), cellOfTable));
        TextView court = new TextView(new ContextThemeWrapper(getApplicationContext(), cellOfTable));
        TextView date = new TextView(new ContextThemeWrapper(getApplicationContext(), cellOfTable));
        TextView startTime = new TextView(new ContextThemeWrapper(getApplicationContext(), cellOfTable));
        TextView time = new TextView(new ContextThemeWrapper(getApplicationContext(), cellOfTable));
        TextView cost = new TextView(new ContextThemeWrapper(getApplicationContext(), cellOfTable));
        TextView price = new TextView(new ContextThemeWrapper(getApplicationContext(), cellOfTable));
        Button delete = new Button(getApplicationContext(), null, 0, cellOfTable);

        nr.setText(String.valueOf(iter));
        court.setText(String.valueOf(s.getCourt().getId()));
        date.setText(String.valueOf(s.getDate()));
        startTime.setText(String.valueOf(s.getTime()));
        time.setText(String.valueOf(s.getNumberOfHours()));
        cost.setText(String.valueOf(s.getUnitCost()));
        price.setText(String.valueOf(s.getPrice()));
        delete.setText("X");
        delete.setBackgroundColor(Color.parseColor("#BD0B0B"));
        delete.setTextColor(Color.parseColor("#9DD813"));

        row.addView(nr, cell_01_Param);
        row.addView(court, cell_012_Param);
        row.addView(date, cell_02_Param);
        row.addView(startTime, cell_02_Param);
        row.addView(time, cell_01_Param);
        row.addView(cost, cell_01_Param);
        row.addView(price, cell_012_Param);
        row.addView(delete, cell_005_Param);

        row.setBackgroundColor(Color.parseColor(cellColor));
        row.setPadding(0, 5, 0, 5);

        return row;
    }

}
