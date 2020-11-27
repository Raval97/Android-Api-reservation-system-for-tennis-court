package com.example.tenniscourtreservation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.tenniscourtreservation.model.Reservation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class UserReservationActivity extends Activity {

    MenuUserAccountTools menuTools;

    Reservation[] reservations;
    TableLayout tableContext;
    TableLayout reservationExample;
    int rowStyle;
    int cellOfRowStyle;
    LinearLayout.LayoutParams tableContextParam;
    LinearLayout.LayoutParams rowParam;
    LinearLayout.LayoutParams idParam;
    LinearLayout.LayoutParams dateParam;
    LinearLayout.LayoutParams priceParam;
    LinearLayout.LayoutParams showDetailsParam;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_reservation);
        getActionBar().hide();

        menuTools = new MenuUserAccountTools(this, (Button) findViewById(R.id.myReservationMenu));
        menuTools.done();

        new HttpReqTask().execute();
    }

    private class HttpReqTask extends AsyncTask<Void, Void, Reservation[]> {

        @Override
        protected void onPreExecute() {
            tableContext = (TableLayout) findViewById(R.id.table);
            reservationExample = (TableLayout) findViewById(R.id.reservation);
            reservationExample.setVisibility(View.GONE);
            rowStyle = R.style.eventsListTableRow;
            cellOfRowStyle = R.style.eventsListTableCell;
            tableContextParam = (LinearLayout.LayoutParams) reservationExample.getLayoutParams();
            tableContextParam.setMargins(0, 0, 0, 20);
            rowParam = (LinearLayout.LayoutParams) findViewById(R.id.reservationRow).getLayoutParams();
            idParam = (LinearLayout.LayoutParams) findViewById(R.id.id).getLayoutParams();
            dateParam = (LinearLayout.LayoutParams) findViewById(R.id.date).getLayoutParams();
            priceParam = (LinearLayout.LayoutParams) findViewById(R.id.price).getLayoutParams();
            showDetailsParam = (LinearLayout.LayoutParams) findViewById(R.id.showDetails).getLayoutParams();
        }

        @Override
        protected Reservation[] doInBackground(Void... voids) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                final String url = MenuTools.startOfUrl + "OurTennis/clientReservation.json";
                RestTemplate restTemplate = menuTools.getDefaultRestTemplate();
                ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET,
                        new HttpEntity<Object>(menuTools.requestHeaders), JsonNode.class);
                reservations = mapper.convertValue(response.getBody().get("reservationList"), Reservation[].class);
            } catch (RestClientException | IllegalArgumentException e) {
                e.printStackTrace();
            }
            return reservations;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(Reservation[] reservations) {
            super.onPostExecute(reservations);

            int iter = 0;
            for (Reservation r : reservations) {
                if (iter % 2 == 0)
                    tableContext.addView(createTableForReservation(r, "#776074"), tableContextParam);
                else
                    tableContext.addView(createTableForReservation(r, "#913860"), tableContextParam);
                iter++;
            }
            tableContext.removeView(reservationExample);
        }
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private TableLayout createTableForReservation(Reservation r, String cellColor) {
        TableLayout reservation = new TableLayout(getApplicationContext());
        TableRow reservationMainData = new TableRow(new ContextThemeWrapper(getApplicationContext(), rowStyle));
        TextView id = new TextView(new ContextThemeWrapper(getApplicationContext(), cellOfRowStyle));
        TextView date = new TextView(new ContextThemeWrapper(getApplicationContext(), cellOfRowStyle));
        TextView price = new TextView(new ContextThemeWrapper(getApplicationContext(), cellOfRowStyle));
        Button showDetails = new Button(this);
        TableRow emptyRow = new TableRow(getApplicationContext());

        reservationMainData.setBackgroundColor(Color.parseColor(cellColor));

        id.setText(r.getId().toString());
        date.setText(r.getDateOfReservation().toString());
        price.setText(String.valueOf(r.getFinalPrice()));
        showDetails.setText("-> <-");
        reservationMainData.addView(id, idParam);
        reservationMainData.addView(date, dateParam);
        reservationMainData.addView(price, priceParam);
        reservationMainData.addView(showDetails, showDetailsParam);

        showDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserReservationDetailsActivity.reservationId = r.getId();
                Intent intent = new Intent(getApplicationContext(), UserReservationDetailsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        reservation.addView(reservationMainData, rowParam);
        reservation.setPadding(0, 0, 0, 5);

        return reservation;
    }

}
