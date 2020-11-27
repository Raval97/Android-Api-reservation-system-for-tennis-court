package com.example.tenniscourtreservation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import lombok.Data;


@RequiresApi(api = Build.VERSION_CODES.O)
public class ReservationActivity extends Activity {

    static LocalDate dateParam = LocalDate.now();
    static int selectDay = 0;
    MenuTools menuTools;
    TextView noLogged;
    LinearLayout logged;

    HorizontalScrollView calendarContainer;
    LinearLayout calendarExample;
    Button dayExample;
    Button showLegend;
    TableLayout legend;

    TableLayout startedReserve;
    TableLayout noStartedReserve;
    TableLayout summaryOfReservationBody;
    TableRow exampleRowSummary;
    TextView priceText;

    TableLayout tableReservationBody;
    TableRow exampleRowOfTableReservation;
    Button refresh;
    Button reserve;

    Services[] startedReservationServices;
    float price = 0;
    Float[] startedNumberOfHoursList;
    Float[] reservedNumberOfHoursList;
    Float[] reservedCourtIdList;
    Float[] startedCourtIdList;
    String[] startedTimeList;
    String[] reservedTimeList;
    List<SingleCellOfSchedule> startedCells = new ArrayList<>();
    List<SingleCellOfSchedule> reservedCells = new ArrayList<>();
    int countOfChanges = 0;
    ArrayList<String> selectNodeArray = new ArrayList<>();
    ArrayList<String> removedNodeArray = new ArrayList<>();


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

        startedReserve = (TableLayout) findViewById(R.id.startedReserve);
        noStartedReserve = (TableLayout) findViewById(R.id.noStartedReserve);
        summaryOfReservationBody = (TableLayout) findViewById(R.id.summaryOfReservationBody);
        exampleRowSummary = (TableRow) findViewById(R.id.exampleRowOfSummary);
        exampleRowSummary.setVisibility(View.GONE);
        priceText = (TextView) findViewById(R.id.price);
        new HttpReqTaskGetData().execute();

        tableReservationBody = (TableLayout) findViewById(R.id.tableReservationBody);
        exampleRowOfTableReservation = (TableRow) findViewById(R.id.exampleRowOfTableReservation);
        exampleRowOfTableReservation.setVisibility(View.GONE);

        refresh = (Button) findViewById(R.id.refresh);
        reserve = (Button) findViewById(R.id.reserve);

        refresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new HttpReqTaskUpdateRemovedCells().execute();
            }
        });

        reserve.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReservationDetailsActivity.class);
                startActivity(intent);
                finish();
            }
        });


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

            int finalIter = i;
            LocalDate finalDate = date;
            day.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    selectDay = finalIter;
                    dateParam = finalDate;
                    schedule.removeAllViews();
                    createSchedule();
                    summaryOfReservationBody.removeAllViews();
                    tableReservationBody.removeAllViews();
                    new HttpReqTaskGetData().execute();
                }
            });
            date = date.plusDays(1L);
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
        LocalTime time = LocalTime.of(6, 0);

        exampleRowOfTableReservation.setVisibility(View.GONE);
        Float[] startedNumbersHoursTemp = {0F, 0F, 0F, 0F};
        Float[] reservedNumbersHoursTemp = {0F, 0F, 0F, 0F};
        final ColorDrawable[] buttonColor = new ColorDrawable[1];
        final int[] colorId = new int[1];
        Boolean ifAllDay = (!reservedCells.isEmpty() && reservedCells.get(0).numberOfHours == 24);
        for (int i = 0; i < 48; i++) {
            TableRow row = new TableRow(new ContextThemeWrapper(getApplicationContext(), rowOfTable));
            Button timeCell = new Button(getApplicationContext(), null, 0, timeAsCellOfTable);
            timeCell.setText(time.toString());
            row.addView(timeCell, cellParam);
            while (!startedCells.isEmpty() && startedCells.get(0).time.equals(time)) {
                startedNumbersHoursTemp[(int) (startedCells.get(0).courtId - 1)] =
                        startedCells.get(0).numberOfHours * 2;
                startedCells.remove(0);
            }
            while (!reservedCells.isEmpty() && reservedCells.get(0).time.equals(time)) {
                reservedNumbersHoursTemp[(int) (reservedCells.get(0).courtId - 1)] =
                        reservedCells.get(0).numberOfHours * 2;
                reservedCells.remove(0);
            }
            for (int j = 0; j < 4; j++) {
                Button cell = new Button(getApplicationContext(), null, 0, cellOfTable);
                cell.setTag("d" + dateParam.getDayOfMonth() + "m" + dateParam.getMonthValue() +
                        "r" + dateParam.getYear() + "_c" + (j + 1) +
                        "_h" + (time.getHour() > 9 ? time.getHour() : "0" + time.getHour()) +
                        "m" + (time.getMinute() > 9 ? time.getMinute() : "0" + time.getMinute()));
                if (ifAllDay || reservedNumbersHoursTemp[j] > 0) {
                    cell.setBackgroundColor(Color.parseColor("#F3EDED"));
                    reservedNumbersHoursTemp[j]--;
                } else {
                    if (startedNumbersHoursTemp[j] > 0) {
                        cell.setBackgroundColor(Color.parseColor("#0C6518"));
                        startedNumbersHoursTemp[j]--;
                    }
                }
                cell.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        buttonColor[0] = (ColorDrawable) cell.getBackground();
                        colorId[0] = buttonColor[0].getColor();
                        if (colorId[0] == Color.parseColor("#A724BD")) {
                            cell.setBackgroundColor(Color.parseColor("#C7E80E"));
                            countOfChanges++;
                            selectNodeArray.add(cell.getTag().toString());
                        }
                        if (colorId[0] == Color.parseColor("#C7E80E")) {
                            cell.setBackgroundColor(Color.parseColor("#A724BD"));
                            countOfChanges--;
                            selectNodeArray.remove(cell.getTag().toString());
                        }
                        if (colorId[0] == Color.parseColor("#0C6518")) {
                            cell.setBackgroundColor(Color.parseColor("#4E0E59"));
                            countOfChanges++;
                            removedNodeArray.add(cell.getTag().toString());
                        }
                        if (colorId[0] == Color.parseColor("#4E0E59")) {
                            cell.setBackgroundColor(Color.parseColor("#0C6518"));
                            countOfChanges--;
                            removedNodeArray.remove(cell.getTag().toString());
                        }
                        reserve.setVisibility((countOfChanges == 0) ? View.VISIBLE : View.GONE);
                        refresh.setVisibility((countOfChanges == 0) ? View.GONE : View.VISIBLE);
                        if (startedReservationServices.length == 0 && countOfChanges == 0) {
                            startedReserve.setVisibility(View.GONE);
                            noStartedReserve.setVisibility(View.VISIBLE);
                        } else {
                            startedReserve.setVisibility(View.VISIBLE);
                            noStartedReserve.setVisibility(View.GONE);
                            priceText.setVisibility(startedReservationServices.length == 0 ? View.GONE : View.VISIBLE);
                        }

                    }
                });
                row.addView(cell, cellParam);
            }
            if (i == 15 || i == 33) {
                rowParam.setMargins(0, 0, 0, 100);
                tableReservationBody.addView(row, rowParam);
            } else
                tableReservationBody.addView(row);
            time = time.plusMinutes(30L);
        }

    }

    private class HttpReqTaskGetData extends AsyncTask<Void, Void, Services[]> {

        @Override
        protected Services[] doInBackground(Void... voids) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                final String url = MenuTools.startOfUrl + "OurTennis/reservation.json?date=" + dateParam.toString();
                RestTemplate restTemplate = menuTools.getDefaultRestTemplate();
                ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(menuTools.requestHeaders), JsonNode.class);
                startedReservationServices = mapper.convertValue(response.getBody().get("startedReservationServices"), Services[].class);
                reservedNumberOfHoursList = mapper.convertValue(response.getBody().get("reservedNumberOfHoursList"), Float[].class);
                startedNumberOfHoursList = mapper.convertValue(response.getBody().get("startedNumberOfHoursList"), Float[].class);
                reservedCourtIdList = mapper.convertValue(response.getBody().get("reservedCourtIdList"), Float[].class);
                startedCourtIdList = mapper.convertValue(response.getBody().get("startedCourtIdList"), Float[].class);
                reservedTimeList = mapper.convertValue(response.getBody().get("reservedTimeList"), String[].class);
                startedTimeList = mapper.convertValue(response.getBody().get("startedTimeList"), String[].class);
                price = mapper.convertValue(response.getBody().get("startedReservation").get("price"), float.class);
            } catch (RestClientException | IllegalArgumentException | NullPointerException e) {
                e.printStackTrace();
            }
            return startedReservationServices;
        }

        @SuppressLint("SetTextI18n")
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(Services[] serviceList) {
            super.onPostExecute(serviceList);

            if (serviceList.length == 0) {
                startedReserve.setVisibility(View.GONE);
                noStartedReserve.setVisibility(View.VISIBLE);
            }


            if (startedCourtIdList != null) {
                for (int i = 0; i < startedCourtIdList.length; i++)
                    startedCells.add(new SingleCellOfSchedule(startedCourtIdList[i],
                            LocalTime.parse(startedTimeList[i]), startedNumberOfHoursList[i]));
                Collections.sort(startedCells);
            }
            if (reservedCourtIdList != null) {
                for (int i = 0; i < reservedCourtIdList.length; i++)
                    reservedCells.add(new SingleCellOfSchedule(reservedCourtIdList[i],
                            LocalTime.parse(reservedTimeList[i]), reservedNumberOfHoursList[i]));
                Collections.sort(reservedCells);
            }
            createTableOfReservations();

            LinearLayout.LayoutParams rowParam = (LinearLayout.LayoutParams) exampleRowSummary.getLayoutParams();
            rowParam.setMargins(0, 0, 0, 10);
            int iter = 0;
            for (Services s : serviceList) {
                if (iter % 2 == 0)
                    summaryOfReservationBody.addView(createTableForSummary(s, "#1D8136", iter + 1), rowParam);
                else
                    summaryOfReservationBody.addView(createTableForSummary(s, "#1B5129", iter + 1), rowParam);
                iter++;
            }
            exampleRowSummary.setVisibility(View.GONE);
            priceText.setText("Price:   " + String.valueOf(price) + " PLN");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TableRow createTableForSummary(Services s, String cellColor, int iter) {
        int cellOfTable = R.style.celOfRowOfSummary;
        LinearLayout.LayoutParams cell_008_Param = (LinearLayout.LayoutParams) findViewById(R.id.exampleSummaryCell_008).getLayoutParams();
        LinearLayout.LayoutParams cell_01_Param = (LinearLayout.LayoutParams) findViewById(R.id.exampleSummaryCell_01).getLayoutParams();
        LinearLayout.LayoutParams cell_015_Param = (LinearLayout.LayoutParams) findViewById(R.id.exampleSummaryCell_015).getLayoutParams();
        LinearLayout.LayoutParams cell_005_Param = (LinearLayout.LayoutParams) findViewById(R.id.deleteServiceButton).getLayoutParams();
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
        date.setText(String.valueOf(s.getDate()).substring(2, String.valueOf(s.getDate()).length()));
        startTime.setText(String.valueOf(s.getTime()));
        time.setText(String.valueOf(s.getNumberOfHours()));
        cost.setText(String.valueOf(s.getUnitCost()));
        price.setText(String.valueOf(s.getPrice()));
        delete.setText("X");
        delete.setBackgroundColor(Color.parseColor("#2E2323"));
        delete.setTextColor(Color.parseColor("#9DD813"));

        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new HttpReqTaskDeleteService(s.getId()).execute();
            }
        });

        row.addView(nr, cell_008_Param);
        row.addView(court, cell_01_Param);
        row.addView(date, cell_015_Param);
        row.addView(startTime, cell_015_Param);
        row.addView(time, cell_01_Param);
        row.addView(cost, cell_01_Param);
        row.addView(price, cell_01_Param);
        row.addView(delete, cell_005_Param);

        row.setBackgroundColor(Color.parseColor(cellColor));
        row.setPadding(0, 5, 0, 5);

        return row;
    }

    private class HttpReqTaskUpdateRemovedCells extends AsyncTask<Void, Void, Void> {
        @SuppressLint("WrongThread")
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                JSONObject jsonObject = new JSONObject();
                JSONArray array = new JSONArray();
                removedNodeArray.forEach(ele -> array.put(ele));
                jsonObject.put("removedNodeArray", array);
                System.out.println(jsonObject.toString());

                URL url = new URL(MenuTools.startOfUrl + "saveRemovedDay");
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

                if (httpURLConnection.getResponseCode() == 204) {
                    new HttpReqTaskUpdateSelectedCells().execute();
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class HttpReqTaskUpdateSelectedCells extends AsyncTask<Void, Void, Void> {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                JSONObject jsonObject = new JSONObject();
                JSONArray array = new JSONArray();
                selectNodeArray.forEach(ele -> array.put(ele));
                jsonObject.put("selectNodeArray", array);
                System.out.println(jsonObject.toString());

                URL url = new URL(MenuTools.startOfUrl + "saveSelectedDay");
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

                if (httpURLConnection.getResponseCode() == 204) {
                    Intent intent = new Intent(getApplicationContext(), ReservationActivity.class);
                    startActivity(intent);
                    finish();
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class HttpReqTaskDeleteService extends AsyncTask<Void, Void, Void> {
        Long serviceId;

        public HttpReqTaskDeleteService(Long serviceId) {
            this.serviceId = serviceId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String url = MenuTools.startOfUrl + "OurTennis/cancelReservationService/" + serviceId;
                RestTemplate restTemplate = menuTools.getDefaultRestTemplate();
                ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(menuTools.requestHeaders), Object.class);
            } catch (RestClientException | IllegalArgumentException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(getApplicationContext(), ReservationActivity.class);
            startActivity(intent);
            finish();
            return null;
        }
    }

    @Data
    private class SingleCellOfSchedule implements Comparable<SingleCellOfSchedule> {
        Float courtId;
        LocalTime time;
        Float numberOfHours;

        public SingleCellOfSchedule(Float courtId, LocalTime time, Float numberOfHours) {
            this.courtId = courtId;
            this.time = time;
            this.numberOfHours = numberOfHours;
        }

        @Override
        public int compareTo(SingleCellOfSchedule ele) {
            return this.time.compareTo(ele.time);
        }

    }

}
