package com.example.tenniscourtreservation;

import android.annotation.SuppressLint;
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

import com.example.tenniscourtreservation.model.Payment;
import com.example.tenniscourtreservation.model.Reservation;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserPaymentActivity extends Activity {

    MenuUserAccountTools menuTools;

    Payment[] payments;
    Reservation[] reservations;
    TableLayout tableContext;
    TableLayout eventExample;
    int paymentRowStyle;
    int cellOfRowStyle;
    int paymentDetailsStyle;
    int paymentDetailsCellStyle;
    LinearLayout.LayoutParams tableContextParam;
    LinearLayout.LayoutParams paymentRowParam;
    LinearLayout.LayoutParams idParam;
    LinearLayout.LayoutParams kindOfPaymentParam;
    LinearLayout.LayoutParams priceParam;
    LinearLayout.LayoutParams cellOfRowPaymentDetailsParam;
    LinearLayout.LayoutParams showDetailsParam;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_payment);
        getActionBar().hide();

        menuTools = new MenuUserAccountTools(this, (Button) findViewById(R.id.paymentMenu));
        menuTools.done();

        new HttpReqTask().execute();
    }

    private class HttpReqTask extends AsyncTask<Void, Void, Payment[]> {

        @Override
        protected Payment[] doInBackground(Void... voids) {
            try {
                ObjectMapper mapper = new ObjectMapper();

                final String url = "http://10.0.2.2:8080/OurTennis/payment.json";
                HttpAuthentication authHeader = new HttpBasicAuthentication(LoginInActivity.username, LoginInActivity.password);
                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setAuthorization(authHeader);
                RestTemplate restTemplate = new RestTemplate();
                MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
                restTemplate.getMessageConverters().add(messageConverter);
                ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(requestHeaders), JsonNode.class);
                payments = mapper.convertValue(response.getBody().get("paymentList"), Payment[].class);
                reservations = mapper.convertValue(response.getBody().get("reservationList"), Reservation[].class);
            } catch (RestClientException | IllegalArgumentException e) {
                e.printStackTrace();
            }
            return payments;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(Payment[] payments) {
            super.onPostExecute(payments);

            tableContext = (TableLayout) findViewById(R.id.table);
            eventExample = (TableLayout) findViewById(R.id.payment);

            paymentRowStyle = R.style.eventsListTableRow;
            cellOfRowStyle = R.style.eventsListTableCell;
            paymentDetailsStyle = R.style.eventDetails;
            paymentDetailsCellStyle = R.style.eventTableCell;

            tableContextParam = (LinearLayout.LayoutParams) eventExample.getLayoutParams();
            tableContextParam.setMargins(0, 0, 0, 20);
            paymentRowParam = (LinearLayout.LayoutParams) findViewById(R.id.paymentRow).getLayoutParams();
            idParam = (LinearLayout.LayoutParams) findViewById(R.id.id).getLayoutParams();
            kindOfPaymentParam = (LinearLayout.LayoutParams) findViewById(R.id.kindOfPayment).getLayoutParams();
            priceParam = (LinearLayout.LayoutParams) findViewById(R.id.price).getLayoutParams();
            cellOfRowPaymentDetailsParam = (LinearLayout.LayoutParams) findViewById(R.id.titlePayValue).getLayoutParams();
            showDetailsParam = (LinearLayout.LayoutParams) findViewById(R.id.showDetails).getLayoutParams();


            List<Payment> paymentList = new ArrayList<>();
            paymentList.addAll(Arrays.asList(payments));
            for (Reservation r : reservations) {
                paymentList.add(new Payment(r.getId(), "Reservation fee", r.getFinalPaymentDate(),
                        r.getPrice(), r.getStatusPaying(), r.getTypeOfPaying()));
            }
            int iter = 0;
            for (Payment p : paymentList) {
                if (iter % 2 == 0)
                    tableContext.addView(createTableForPayment(p, "#776074"), tableContextParam);
                else
                    tableContext.addView(createTableForPayment(p, "#913860"), tableContextParam);
                iter++;
            }
            tableContext.removeView(eventExample);
        }
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private TableLayout createTableForPayment(Payment p, String cellColor) {
        TableLayout payment = new TableLayout(getApplicationContext());
        TableRow paymentRow = new TableRow(new ContextThemeWrapper(getApplicationContext(), paymentRowStyle));
        TextView id = new TextView(new ContextThemeWrapper(getApplicationContext(), cellOfRowStyle));
        TextView kindOfPayment = new TextView(new ContextThemeWrapper(getApplicationContext(), cellOfRowStyle));
        TextView price = new TextView(new ContextThemeWrapper(getApplicationContext(), cellOfRowStyle));
        Button showDetails = new Button(this);
        TableRow emptyRow = new TableRow(getApplicationContext());
        TableLayout paymentDetails = new TableLayout(new ContextThemeWrapper(getApplicationContext(), paymentDetailsStyle));
        TableRow paymentAction = new TableRow(getApplicationContext());
        Button payBill = new Button(this);

        paymentRow.setBackgroundColor(Color.parseColor(cellColor));
        paymentDetails.setBackgroundColor(Color.parseColor(cellColor));
        paymentAction.setBackgroundColor(Color.parseColor(cellColor));

        id.setText((p.getTitle().startsWith("Reservation fee")) ? p.getId() + "r" : p.getId() + "o");
        String[] tempOfTitle = p.getTitle().split(" ");
        kindOfPayment.setText(tempOfTitle[0] + " " + tempOfTitle[1]);
        price.setText(String.valueOf(p.getPrice()));
        showDetails.setText("-> <-");
        paymentRow.addView(id, idParam);
        paymentRow.addView(kindOfPayment, kindOfPaymentParam);
        paymentRow.addView(price, priceParam);
        paymentRow.addView(showDetails, showDetailsParam);

        emptyRow.setBackgroundColor(Color.parseColor("#21175E"));
        emptyRow.setPadding(0, 0, 0, 8);
        emptyRow.setVisibility(View.GONE);

        if (tempOfTitle[0].equals("Reservation")) {
            paymentDetails.addView(createRowOfTable("Final Payment date:", String.valueOf(p.getDateOfPaying())));
            paymentDetails.addView(createRowOfTable("Type of paying:", String.valueOf(p.getTypeOfPaying())));
        }
        if (tempOfTitle[0].equals("Tournament")) {
            paymentDetails.addView(createRowOfTable("Title of event:", p.getTitle().substring(16)));
            paymentDetails.addView(createRowOfTable("Final Payment date:", String.valueOf(p.getFinalPaymentDate())));
            paymentDetails.addView(createRowOfTable("Date of paying:", String.valueOf(p.getDateOfPaying())));
            paymentDetails.addView(createRowOfTable("Type of paying:", "Online"));
        }
        if (tempOfTitle[0].equals("Membership")) {
            paymentDetails.addView(createRowOfTable("Date of paying:", String.valueOf(p.getDateOfPaying())));
            paymentDetails.addView(createRowOfTable("Type of paying:", "Online"));
        }
        paymentDetails.addView(createRowOfTable("Status of paying:", String.valueOf(p.getStatusPaying())));
        paymentDetails.setVisibility(View.GONE);

        payBill.setText("Pyy Bill");
        paymentAction.addView(payBill);
        paymentAction.setGravity(Gravity.CENTER);
        paymentAction.setVisibility(View.GONE);

        showDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentDetails.setVisibility((paymentDetails.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE);
                emptyRow.setVisibility((emptyRow.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE);
                paymentAction.setVisibility((paymentAction.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE);
                showDetails.setText((paymentDetails.getVisibility() == View.GONE) ? "<- ->" : "-> <-");
            }
        });

        payment.addView(paymentRow, paymentRowParam);
        payment.addView(emptyRow);
        payment.addView(paymentDetails);
        payment.addView(paymentAction);
        payment.setPadding(0, 0, 0, 5);

        return payment;
    }

    private TableRow createRowOfTable(String text1, String text2) {
        TableRow row = new TableRow(getApplicationContext());
        TextView cell1 = new TextView(new ContextThemeWrapper(getApplicationContext(), paymentDetailsCellStyle));
        TextView cell2 = new TextView(new ContextThemeWrapper(getApplicationContext(), paymentDetailsCellStyle));
        cell1.setText(text1);
        cell2.setText(text2);
        row.addView(cell1, cellOfRowPaymentDetailsParam);
        row.addView(cell2, cellOfRowPaymentDetailsParam);
        row.setPadding(5, 5, 5, 5);
        return row;
    }

}
