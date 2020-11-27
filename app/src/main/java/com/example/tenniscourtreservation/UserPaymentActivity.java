package com.example.tenniscourtreservation;

import android.annotation.SuppressLint;
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

import com.example.tenniscourtreservation.model.Payment;
import com.example.tenniscourtreservation.model.Reservation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.SneakyThrows;

public class UserPaymentActivity extends Activity {

    MenuUserAccountTools menuTools;

    LinearLayout content;
    LinearLayout bank;
    Button cancel;
    Button payInBank;
    Payment[] payments;
    Reservation[] reservations;
    TableLayout tableContext;
    TableLayout eventExample;
    int paymentRowStyle;
    int cellOfRowStyle;
    int paymentDetailsStyle;
    int paymentDetailsCellStyle;
    int payBillButtonStyle;
    LinearLayout.LayoutParams tableContextParam;
    LinearLayout.LayoutParams paymentRowParam;
    LinearLayout.LayoutParams idParam;
    LinearLayout.LayoutParams kindOfPaymentParam;
    LinearLayout.LayoutParams priceParam;
    LinearLayout.LayoutParams cellOfRowPaymentDetailsParam;
    LinearLayout.LayoutParams showDetailsParam;

    String objectOfPayment;
    Long objectOfPaymentId;
    Handler mHandler;
    String message;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_payment);
        getActionBar().hide();

        menuTools = new MenuUserAccountTools(this, (Button) findViewById(R.id.paymentMenu));
        menuTools.done();

        bank = (LinearLayout) findViewById(R.id.bank);
        content = (LinearLayout) findViewById(R.id.content);
        cancel = (Button) findViewById(R.id.cancel);
        payInBank = (Button) findViewById(R.id.payFee);

        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                bank.setVisibility(View.GONE);
                content.setVisibility(View.VISIBLE);
            }
        });

        payInBank.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (objectOfPayment.equals("Tournament"))
                    new HttpReqTaskPayEventFee(objectOfPaymentId).execute();
                if (objectOfPayment.equals("Reservation"))
                    new HttpReqTaskPayForReservation(objectOfPaymentId).execute();
            }
        });

        new HttpReqTask().execute();

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
    }

    private class HttpReqTask extends AsyncTask<Void, Void, Payment[]> {

        @Override
        protected void onPreExecute() {

            tableContext = (TableLayout) findViewById(R.id.table);
            eventExample = (TableLayout) findViewById(R.id.payment);
            eventExample.setVisibility(View.GONE);

            paymentRowStyle = R.style.eventsListTableRow;
            cellOfRowStyle = R.style.eventsListTableCell;
            paymentDetailsStyle = R.style.eventDetails;
            paymentDetailsCellStyle = R.style.eventTableCell;
            payBillButtonStyle = R.style.actionButton;

            tableContextParam = (LinearLayout.LayoutParams) eventExample.getLayoutParams();
            tableContextParam.setMargins(0, 0, 0, 20);
            paymentRowParam = (LinearLayout.LayoutParams) findViewById(R.id.paymentRow).getLayoutParams();
            idParam = (LinearLayout.LayoutParams) findViewById(R.id.id).getLayoutParams();
            kindOfPaymentParam = (LinearLayout.LayoutParams) findViewById(R.id.kindOfPayment).getLayoutParams();
            priceParam = (LinearLayout.LayoutParams) findViewById(R.id.price).getLayoutParams();
            cellOfRowPaymentDetailsParam = (LinearLayout.LayoutParams) findViewById(R.id.titlePayValue).getLayoutParams();
            showDetailsParam = (LinearLayout.LayoutParams) findViewById(R.id.showDetails).getLayoutParams();
        }

        @Override
        protected Payment[] doInBackground(Void... voids) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                final String url = MenuTools.startOfUrl + "OurTennis/payment.json";
                RestTemplate restTemplate = menuTools.getDefaultRestTemplate();
                ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(menuTools.requestHeaders), JsonNode.class);
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

            List<Payment> paymentList = new ArrayList<>();
            paymentList.addAll(Arrays.asList(payments));
            for (Reservation r : reservations) {
                paymentList.add(new Payment(r.getId(), "Reservation fee", r.getFinalPaymentDate(),
                        r.getFinalPrice(), r.getStatusPaying(), r.getTypeOfPaying()));
            }
            Collections.sort(paymentList);
            Collections.reverse(paymentList);
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

    private class HttpReqTaskPayForReservation extends AsyncTask<Void, Void, Void> {
        Long id;

        public HttpReqTaskPayForReservation(Long id) {
            this.id = id;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final String url = MenuTools.startOfUrl + "payForReservation/" + id;
                RestTemplate restTemplate = menuTools.getDefaultRestTemplate();
                ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(menuTools.requestHeaders), Object.class);
            } catch (RestClientException | IllegalArgumentException e) {
                e.printStackTrace();
            }
            message = "The payment has been made";
            Message message = mHandler.obtainMessage();
            message.sendToTarget();
            Intent intent = new Intent(getApplicationContext(), UserPaymentActivity.class);
            startActivity(intent);
            finish();
            return null;
        }
    }

    private class HttpReqTaskPayEventFee extends AsyncTask<Void, Void, Void> {
        Long paymentId;

        public HttpReqTaskPayEventFee(Long id) {
            this.paymentId = id;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                String url = MenuTools.startOfUrl + "OurTennis/payFeeOfParticipantEventFromAccount/" + paymentId + ".json";
                RestTemplate restTemplate = menuTools.getDefaultRestTemplate();
                ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET,
                        new HttpEntity<Object>(menuTools.requestHeaders), JsonNode.class);
                Long eventId = mapper.convertValue(response.getBody().get("ID"), Long.class);
                url = MenuTools.startOfUrl + "OurTennis/payEventApplicationFee/" + eventId + ".json";
                ResponseEntity<Object> response2 = restTemplate.exchange(url, HttpMethod.GET,
                        new HttpEntity<Object>(menuTools.requestHeaders), Object.class);
            } catch (RestClientException | IllegalArgumentException e) {
                e.printStackTrace();
            }
            message = "The payment has been made";
            Message message = mHandler.obtainMessage();
            message.sendToTarget();
            Intent intent = new Intent(getApplicationContext(), UserPaymentActivity.class);
            startActivity(intent);
            finish();
            return null;
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
        Button payBill = new Button(getApplicationContext(), null, 0, payBillButtonStyle);

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

        payBill.setText("Pay Bill");
        paymentAction.addView(payBill);
        paymentAction.setGravity(Gravity.CENTER);
        paymentAction.setPadding(0, 0, 0, 20);
        paymentAction.setVisibility(View.GONE);

        if (!p.getStatusPaying().equals("Paid")) {
            payBill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bank.setVisibility(View.VISIBLE);
                    content.setVisibility(View.GONE);
                    objectOfPaymentId = p.getId();
                    objectOfPayment = tempOfTitle[0];
                }
            });
        }

        showDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetails.setText((paymentDetails.getVisibility() == View.GONE) ? "<- ->" : "-> <-");
                paymentDetails.setVisibility((paymentDetails.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE);
                emptyRow.setVisibility((emptyRow.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE);
                if (!p.getStatusPaying().equals("Paid"))
                    paymentAction.setVisibility((paymentAction.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE);
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
