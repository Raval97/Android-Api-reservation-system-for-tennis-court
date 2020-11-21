package com.example.tenniscourtreservation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.tenniscourtreservation.model.Reservation;
import com.example.tenniscourtreservation.model.Services;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import lombok.SneakyThrows;

public class UserReservationDetailsActivity extends Activity {

    static Long reservationId;
    Handler mHandler;
    String message;
    MenuUserAccountTools menuTools;
    Button back;
    LinearLayout content;
    LinearLayout bank;
    Button cancel;
    Button payInBank;
    TableRow action;
    Button payReservation;
    Button cancelReservation;

    Reservation reservation;
    Services[] services;

    TextView id;
    TextView dateOfReservation;
    TextView statusOfReservation;
    TextView price;
    TextView clubAssociation;
    TextView finalPrice;
    TextView typeOfPaying;
    TextView statusOfPaying;

    TableLayout tableContext;
    TableLayout servicesExample;
    LinearLayout.LayoutParams rowDataParam;
    LinearLayout.LayoutParams rowAdditionsParam;
    int cellOfRowStyle;
    LinearLayout.LayoutParams cel_015_param;
    LinearLayout.LayoutParams cel_025_param;
    LinearLayout.LayoutParams cel_033_param;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_reservation_details);
        getActionBar().hide();

        menuTools = new MenuUserAccountTools();

        back = (Button) findViewById(R.id.back);
        id = (TextView) findViewById(R.id.id);
        dateOfReservation = (TextView) findViewById(R.id.dateOfReservation);
        statusOfReservation = (TextView) findViewById(R.id.statusOfReservation);
        price = (TextView) findViewById(R.id.price);
        clubAssociation = (TextView) findViewById(R.id.clubAssociation);
        finalPrice = (TextView) findViewById(R.id.finalPrice);
        typeOfPaying = (TextView) findViewById(R.id.typeOfPaying);
        statusOfPaying = (TextView) findViewById(R.id.statusOfPaying);
        bank = (LinearLayout) findViewById(R.id.bank);
        content = (LinearLayout) findViewById(R.id.content);
        cancel = (Button) findViewById(R.id.cancel);
        payInBank = (Button) findViewById(R.id.payFee);
        action = (TableRow) findViewById(R.id.action);
        payReservation = (Button) findViewById(R.id.payReservation);
        cancelReservation = (Button) findViewById(R.id.cancelReservation);

        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                bank.setVisibility(View.GONE);
                content.setVisibility(View.VISIBLE);
            }
        });

        payInBank.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new HttpReqTaskPayReservationFee().execute();
            }
        });

        payReservation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                bank.setVisibility(View.VISIBLE);
                content.setVisibility(View.GONE);
            }
        });

        cancelReservation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new HttpReqTaskCancelReservation().execute();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserReservationActivity.class);
                startActivity(intent);
                finish();
            }
        });

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

        new HttpReqTask().execute();
    }

    private class HttpReqTask extends AsyncTask<Void, Void, Services[]> {

        @Override
        protected void onPreExecute() {
            tableContext = (TableLayout) findViewById(R.id.servicesList);
            servicesExample = (TableLayout) findViewById(R.id.servicesExample);
            rowDataParam = (LinearLayout.LayoutParams) findViewById(R.id.dataServices).getLayoutParams();
            rowAdditionsParam = (LinearLayout.LayoutParams) findViewById(R.id.additionsServices).getLayoutParams();
            cellOfRowStyle = R.style.reservationDetailsTableCell;
            cel_015_param = (LinearLayout.LayoutParams) findViewById(R.id.serviceId).getLayoutParams();
            cel_025_param = (LinearLayout.LayoutParams) findViewById(R.id.serviceData).getLayoutParams();
            cel_033_param = (LinearLayout.LayoutParams) findViewById(R.id.serviceAdditionBalls).getLayoutParams();
        }

        @Override
        protected Services[] doInBackground(Void... voids) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                final String url = MenuTools.startOfUrl + "OurTennis/clientReservation/" + reservationId + ".json";
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

            id.setText(String.valueOf(reservation.getId()));
            dateOfReservation.setText(reservation.getDateOfReservation().toString());
            statusOfReservation.setText(reservation.getStatusOfReservation());
            price.setText(String.valueOf(reservation.getPrice()));
            clubAssociation.setText(reservation.isDiscount() ? "Yes" : "No");
            finalPrice.setText(String.valueOf(reservation.getFinalPrice()));
            typeOfPaying.setText(reservation.getTypeOfPaying());
            statusOfPaying.setText(reservation.getStatusPaying());
            if(reservation.getStatusPaying().equals("Paid"))
                action.setVisibility(View.GONE);
            if(reservation.getTypeOfPaying().equals("offline"))
                payReservation.setVisibility(View.GONE);

            int iter = 0;
            for (Services s : services) {
                System.out.println(s);
                if (iter % 2 == 0) {
                    tableContext.addView(createDataServiceRowTable(s, "#670A6A"), rowDataParam);
                    tableContext.addView(createAdditionsServiceRowTable(s, "#840888"), rowAdditionsParam);
                } else {
                    tableContext.addView(createDataServiceRowTable(s, "#24334A"), rowDataParam);
                    tableContext.addView(createAdditionsServiceRowTable(s, "#35445B"), rowAdditionsParam);
                }
                iter++;
            }
            tableContext.removeView(servicesExample);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private TableRow createDataServiceRowTable(Services s, String color) {
        TableRow dataRow = new TableRow(getApplicationContext());
        TextView court = new TextView(new ContextThemeWrapper(getApplicationContext(), cellOfRowStyle));
        TextView date = new TextView(new ContextThemeWrapper(getApplicationContext(), cellOfRowStyle));
        TextView startTime = new TextView(new ContextThemeWrapper(getApplicationContext(), cellOfRowStyle));
        TextView time = new TextView(new ContextThemeWrapper(getApplicationContext(), cellOfRowStyle));
        TextView cost = new TextView(new ContextThemeWrapper(getApplicationContext(), cellOfRowStyle));
        TextView price = new TextView(new ContextThemeWrapper(getApplicationContext(), cellOfRowStyle));

        court.setText(String.valueOf(s.getCourt().getId()));
        date.setText(s.getDate().toString());
        startTime.setText(s.getTime().toString());
        time.setText(String.valueOf(s.getNumberOfHours()));
        cost.setText(String.valueOf(s.getUnitCost()));
        price.setText(String.valueOf(s.getPrice()));

        dataRow.addView(court, cel_015_param);
        dataRow.addView(date, cel_025_param);
        dataRow.addView(startTime, cel_015_param);
        dataRow.addView(time, cel_015_param);
        dataRow.addView(cost, cel_015_param);
        dataRow.addView(price, cel_015_param);

        dataRow.setPadding(5, 5, 5, 5);
        dataRow.setBackgroundColor(Color.parseColor(color));

        return dataRow;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private TableRow createAdditionsServiceRowTable(Services s, String color) {
        TableRow dataRow = new TableRow(getApplicationContext());
        TextView balls = new TextView(new ContextThemeWrapper(getApplicationContext(), cellOfRowStyle));
        TextView rocket = new TextView(new ContextThemeWrapper(getApplicationContext(), cellOfRowStyle));
        TextView shoes = new TextView(new ContextThemeWrapper(getApplicationContext(), cellOfRowStyle));

        balls.setText(s.getIfBalls() ? "Balls: YES" : "Balls: NO");
        rocket.setText(s.getIfRocket() ? "Rocket: YES" : "Rocket: NO");
        shoes.setText(s.getIfShoes() ? "Shoes: YES" : "Shoes: NO");

        dataRow.addView(balls, cel_033_param);
        dataRow.addView(rocket, cel_033_param);
        dataRow.addView(shoes, cel_033_param);

        dataRow.setPadding(5, 5, 5, 5);
        dataRow.setBackgroundColor(Color.parseColor(color));

        return dataRow;
    }

    private class HttpReqTaskPayReservationFee extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String url = MenuTools.startOfUrl + "payForReservation/" + reservationId;
                RestTemplate restTemplate = menuTools.getDefaultRestTemplate();
                ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(menuTools.requestHeaders), Object.class);
            } catch (RestClientException | IllegalArgumentException e) {
                e.printStackTrace();
            }
            message = "The payment has been made";
            Message message = mHandler.obtainMessage();
            message.sendToTarget();
            Intent intent = new Intent(getApplicationContext(), UserReservationDetailsActivity.class);
            startActivity(intent);
            finish();
            return null;
        }
    }

    private class HttpReqTaskCancelReservation extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String url = MenuTools.startOfUrl + "OurTennis/cancelReservation/" + reservationId;
                RestTemplate restTemplate = menuTools.getDefaultRestTemplate();
                ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(menuTools.requestHeaders), Object.class);
            } catch (RestClientException | IllegalArgumentException e) {
                e.printStackTrace();
            }
            message = "The Reservation\n has been canceled";
            Message message = mHandler.obtainMessage();
            message.sendToTarget();
            Intent intent = new Intent(getApplicationContext(), UserReservationActivity.class);
            startActivity(intent);
            finish();
            return null;
        }
    }

}
