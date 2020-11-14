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
import com.example.tenniscourtreservation.model.Services;
import com.example.tenniscourtreservation.model.UserReservation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class UserReservationDetailsActivity extends Activity {

    static Long reservationId;
    MenuUserAccountTools menuTools;
    Button back;

    Reservation reservation;
    Services[] services;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_reservation_details);
        getActionBar().hide();

        menuTools = new MenuUserAccountTools();

        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserReservationActivity.class);
                startActivity(intent);
                finish();
            }
        });

        new HttpReqTask().execute();
    }

    private class HttpReqTask extends AsyncTask<Void, Void, Services[]> {

//        @Override
//        protected void onPreExecute() {
//            tableContext = (TableLayout) findViewById(R.id.table);
//            reservationExample = (TableLayout) findViewById(R.id.reservation);
//            rowStyle = R.style.eventsListTableRow;
//            cellOfRowStyle = R.style.eventsListTableCell;
//            tableContextParam = (LinearLayout.LayoutParams) reservationExample.getLayoutParams();
//            tableContextParam.setMargins(0, 0, 0, 20);
//            rowParam = (LinearLayout.LayoutParams) findViewById(R.id.reservationRow).getLayoutParams();
//            idParam = (LinearLayout.LayoutParams) findViewById(R.id.id).getLayoutParams();
//            dateParam = (LinearLayout.LayoutParams) findViewById(R.id.date).getLayoutParams();
//            priceParam = (LinearLayout.LayoutParams) findViewById(R.id.price).getLayoutParams();
//            showDetailsParam = (LinearLayout.LayoutParams) findViewById(R.id.showDetails).getLayoutParams();
//        }

        @Override
        protected Services[] doInBackground(Void... voids) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                final String url = "http://10.0.2.2:8080/OurTennis/clientReservation/"+reservationId+".json";
                RestTemplate restTemplate = menuTools.getDefaultRestTemplate();
                ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET,
                        new HttpEntity<Object>(menuTools.requestHeaders), JsonNode.class);
                reservation = mapper.convertValue(response.getBody().get("reservation"), Reservation.class);
                services = mapper.convertValue(response.getBody().get("servicesList"), Services[].class);
            } catch (RestClientException | IllegalArgumentException e) {
                e.printStackTrace();
            }
            return services;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(Services[] services) {
            super.onPostExecute(services);
            System.out.println(reservation.toString());
            int iter = 0;
            for (Services s : services) {
                System.out.println(s);
//                if (iter % 2 == 0)
//                    tableContext.addView(createTableForReservation(r, "#776074"), tableContextParam);
//                else
//                    tableContext.addView(createTableForReservation(r, "#913860"), tableContextParam);
                iter++;
            }
//            tableContext.removeView(reservationExample);
        }
    }

//    @SuppressLint("SetTextI18n")
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private TableLayout createTableForReservation(Reservation r, String cellColor) {
//        TableLayout reservation = new TableLayout(getApplicationContext());
//        TableRow reservationMainData = new TableRow(new ContextThemeWrapper(getApplicationContext(), rowStyle));
//        TextView id = new TextView(new ContextThemeWrapper(getApplicationContext(), cellOfRowStyle));
//        TextView date = new TextView(new ContextThemeWrapper(getApplicationContext(), cellOfRowStyle));
//        TextView price = new TextView(new ContextThemeWrapper(getApplicationContext(), cellOfRowStyle));
//        Button showDetails = new Button(this);
//        TableRow emptyRow = new TableRow(getApplicationContext());
//
//        reservationMainData.setBackgroundColor(Color.parseColor(cellColor));
//
//        id.setText(r.getId().toString());
//        date.setText(r.getDateOfReservation().toString());
//        price.setText(String.valueOf(r.getFinalPrice()));
//        showDetails.setText("-> <-");
//        reservationMainData.addView(id, idParam);
//        reservationMainData.addView(date, dateParam);
//        reservationMainData.addView(price, priceParam);
//        reservationMainData.addView(showDetails, showDetailsParam);
//
//        showDetails.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                UserReservationDetailsActivity.reservationId = r.getId();
//                Intent intent = new Intent(getApplicationContext(), UserReservationDetailsActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//
//        reservation.addView(reservationMainData, rowParam);
//        reservation.setPadding(0, 0, 0, 5);
//
//        return reservation;
//    }

}
