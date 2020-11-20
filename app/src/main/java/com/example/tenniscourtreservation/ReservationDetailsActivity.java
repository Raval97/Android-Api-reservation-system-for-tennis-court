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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.tenniscourtreservation.model.Reservation;
import com.example.tenniscourtreservation.model.Services;
import com.example.tenniscourtreservation.model.UserReservation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
import java.util.Base64;

import lombok.SneakyThrows;

public class ReservationDetailsActivity extends Activity {

    Handler mHandler;
    String message;
    MenuUserAccountTools menuTools;
    Button back;
    LinearLayout content;
    TableRow action;
    Button cancelReservation;
    Button confirmReservation;

    Reservation reservation;
    Services[] services;

    TextView price;
    TextView clubAssociation;
    TextView finalPrice;
    Spinner typeOfPaying;
    static String[] spinnerItems = {"online", "offline"};

    TableLayout tableContext;
    TableLayout servicesExample;
    LinearLayout.LayoutParams rowDataParam;
    LinearLayout.LayoutParams rowAdditionsParam;
    int cellOfRowStyle;
    int additionsCellOfRowStyle;
    LinearLayout.LayoutParams cel_015_param;
    LinearLayout.LayoutParams cel_025_param;
    LinearLayout.LayoutParams cel_033_param;
    LinearLayout.LayoutParams cel_01_param;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation_details);


        menuTools = new MenuUserAccountTools();

        back = (Button) findViewById(R.id.back);
        price = (TextView) findViewById(R.id.price);
        clubAssociation = (TextView) findViewById(R.id.clubAssociation);
        finalPrice = (TextView) findViewById(R.id.finalPrice);
        typeOfPaying = (Spinner) findViewById(R.id.typeOfPaying);
        content = (LinearLayout) findViewById(R.id.content);

        action = (TableRow) findViewById(R.id.action);
        cancelReservation = (Button) findViewById(R.id.cancelReservation);
        confirmReservation = (Button) findViewById(R.id.confirmReservation);

        cancelReservation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new HttpReqTaskCancelReservation().execute();
            }
        });

        confirmReservation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new HttpReqTaskConfirmReservation().execute();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReservationActivity.class);
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
            additionsCellOfRowStyle = R.style.switchStyle;
            cel_015_param = (LinearLayout.LayoutParams) findViewById(R.id.serviceId).getLayoutParams();
            cel_025_param = (LinearLayout.LayoutParams) findViewById(R.id.serviceData).getLayoutParams();
            cel_033_param = (LinearLayout.LayoutParams) findViewById(R.id.serviceAdditionBalls).getLayoutParams();
            cel_01_param = (LinearLayout.LayoutParams) findViewById(R.id.deleteService).getLayoutParams();
            cel_01_param.setMargins(0,0,50,0);
        }

        @Override
        protected Services[] doInBackground(Void... voids) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                final String url = MenuTools.startOfUrl + "OurTennis/makeReservation.json";
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

            price.setText(String.valueOf(reservation.getPrice()));
            clubAssociation.setText(reservation.isDiscount() ? "Yes" : "No");
            finalPrice.setText(String.valueOf(reservation.getFinalPrice()));
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_text, spinnerItems);
            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
            typeOfPaying.setAdapter(adapter);
            typeOfPaying.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
                    String selected_val = typeOfPaying.getSelectedItem().toString();
                    reservation.setTypeOfPaying(selected_val);
                    spinnerItems = (typeOfPaying.getSelectedItem().equals("online") ?
                            new String[]{"online", "offline"} : new String[]{"offline", "online"});
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    reservation.setTypeOfPaying(spinnerItems[0]);
                }
            });

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
        Switch balls = new Switch(new ContextThemeWrapper(getApplicationContext(), additionsCellOfRowStyle));
        Switch rocket = new Switch(new ContextThemeWrapper(getApplicationContext(), additionsCellOfRowStyle));
        Switch shoes = new Switch(new ContextThemeWrapper(getApplicationContext(), additionsCellOfRowStyle));
        Button delete = new Button(getApplicationContext(), null, 0, additionsCellOfRowStyle);

        balls.setText("Balls");
        balls.setPadding(0, 0, 20, 0);
        balls.setTextColor(Color.parseColor("#ffffff"));
        balls.setChecked(!s.getIfBalls());
        rocket.setText("Rocket");
        rocket.setPadding(0, 0, 20, 0);
        rocket.setTextColor(Color.parseColor("#ffffff"));
        rocket.setChecked(!s.getIfRocket());
        shoes.setText("Shoes");
        shoes.setTextColor(Color.parseColor("#ffffff"));
        shoes.setChecked(!s.getIfShoes());
        delete.setText("X");
        delete.setBackgroundColor(Color.parseColor(color));
        delete.setTextColor(Color.parseColor("#9DD813"));

        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (services.length == 1)
                    new HttpReqTaskCancelReservation().execute();
                else
                    new HttpReqTaskUpdateDeleteService(s.getId()).execute();
            }
        });

        dataRow.addView(balls, cel_033_param);
        dataRow.addView(rocket, cel_033_param);
        dataRow.addView(shoes, cel_033_param);
        dataRow.addView(delete, cel_01_param);


        dataRow.setPadding(5, 5, 5, 5);
        dataRow.setBackgroundColor(Color.parseColor(color));

        balls.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                new HttpReqTaskUpdateAddition(s.getId(), (isChecked ? "0" : "1"), "updateIfBalls").execute();
            }
        });

        rocket.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                new HttpReqTaskUpdateAddition(s.getId(), (isChecked ? "0" : "1"), "updateIfRocket").execute();
            }
        });

        shoes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                new HttpReqTaskUpdateAddition(s.getId(), (isChecked ? "0" : "1"), "updateIfShoes").execute();
            }
        });

        return dataRow;
    }

    private class HttpReqTaskCancelReservation extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String url = MenuTools.startOfUrl + "OurTennis/cancelReservation/" + reservation.getId();
                RestTemplate restTemplate = menuTools.getDefaultRestTemplate();
                ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(menuTools.requestHeaders), Object.class);
            } catch (RestClientException | IllegalArgumentException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(getApplicationContext(), ReservationActivity.class);
            startActivity(intent);
            finish();
            message = "Reservation canceled";
            Message message = mHandler.obtainMessage();
            message.sendToTarget();
            return null;
        }
    }

    private class HttpReqTaskConfirmReservation extends AsyncTask<Void, Void, Void> {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("typeOfPaying", reservation.getTypeOfPaying());

                URL url = new URL(MenuTools.startOfUrl + "OurTennis/confirmReservation");
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
                    Intent intent = new Intent(getApplicationContext(), UserReservationActivity.class);
                    startActivity(intent);
                    finish();
                    message = "Reservation successful created";
                    Message message = mHandler.obtainMessage();
                    message.sendToTarget();
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class HttpReqTaskUpdateAddition extends AsyncTask<Void, Void, Void> {
        Long servicesId;
        String decision;
        String additionUrl;

        public HttpReqTaskUpdateAddition(Long servicesId, String decision, String additionUrl) {
            this.servicesId = servicesId;
            this.decision = decision;
            this.additionUrl = additionUrl;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("addition", decision);

                URL url = new URL(MenuTools.startOfUrl + "" + additionUrl + "/" + servicesId);
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
                    Intent intent = new Intent(getApplicationContext(), ReservationDetailsActivity.class);
                    startActivity(intent);
                    finish();
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private class HttpReqTaskUpdateDeleteService extends AsyncTask<Void, Void, Void> {
        Long serviceId;

        public HttpReqTaskUpdateDeleteService(Long serviceId) {
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
            Intent intent = new Intent(getApplicationContext(), ReservationDetailsActivity.class);
            startActivity(intent);
            finish();
            return null;
        }
    }

}
