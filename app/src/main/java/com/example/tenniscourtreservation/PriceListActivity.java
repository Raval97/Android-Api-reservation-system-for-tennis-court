package com.example.tenniscourtreservation;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.tenniscourtreservation.model.PriceList;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;

public class PriceListActivity extends Activity {

    MenuTools menuTools;
    Boolean logged;
    Boolean isAdmin;
    PriceList[] priceList;
    LinearLayout context;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.price_list);
        getActionBar().hide();

        menuTools = new MenuTools(this, (Button) findViewById(R.id.priceListMenu));
        menuTools.done();

        new PriceListActivity.HttpReqTask(this).execute();

        LinearLayout context2 = (LinearLayout) findViewById(R.id.container);
        TextView price2 = new TextView(this);
        price2.setText("sfds");
        price2.setBackgroundColor(Color.parseColor("#aaa333"));
        context2.addView(price2);
    }



    private class HttpReqTask extends AsyncTask<Void, Void, PriceList[]> {
        Activity activity;

        public HttpReqTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected PriceList[] doInBackground(Void... voids) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(new URL("http://10.0.2.2:8080/ourTennis/priceList.json"));
                priceList = mapper.convertValue(jsonNode.get("priceList"), PriceList[].class);
                logged = mapper.convertValue(jsonNode.get("logged"), Boolean.class);
                isAdmin = mapper.convertValue(jsonNode.get("isAdmin"), Boolean.class);
                return priceList;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(PriceList[] priceList) {
            super.onPostExecute(priceList);
            context = (LinearLayout) findViewById(R.id.container);
            context.addView(createTable(activity, priceList));
        }
    }

    private TableRow createTableRow(Activity activity, PriceList p){
        TableRow tableRow = new TableRow(activity);
        TextView title = new TextView(activity);
        TextView time = new TextView(activity);
        TextView price = new TextView(activity);
        title.setText(String.valueOf(p.getType()));
        time.setText(String.valueOf(p.getTime()));
        price.setText(String.valueOf(p.getPrice()));
        tableRow.addView(title);
        tableRow.addView(time);
        tableRow.addView(price);
        return tableRow;
    }

    private TableLayout createTable(Activity activity, PriceList[] priceList){
        TableLayout table = new TableLayout(activity);
        for (PriceList p : priceList) {
            table.addView(createTableRow(activity, p));
        }
        return table;
    }

}
