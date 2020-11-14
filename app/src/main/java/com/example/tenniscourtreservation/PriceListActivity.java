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
    TextView cellTitle;
    TextView cellTime;
    TextView cellPrice;
    TableRow tableRow;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.price_list);
        getActionBar().hide();

        menuTools = new MenuTools(this, (Button) findViewById(R.id.priceListMenu));
        menuTools.done();

        new PriceListActivity.HttpReqTask(this).execute();
    }


    private class HttpReqTask extends AsyncTask<Void, Void, PriceList[]> {
        Activity activity;

        @Override
        protected void onPreExecute() {
            context = (TableLayout) findViewById(R.id.table);
            cellTitle = (TextView) findViewById(R.id.one);
            cellTime = (TextView) findViewById(R.id.two);
            cellPrice = (TextView) findViewById(R.id.three);
            tableRow = (TableRow) findViewById(R.id.example);
        }

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

            int iter = 0;
            for (PriceList p : priceList) {
                if(iter %2 ==0)
                    context.addView(createTableRow(activity, p, R.style.priceListTableCell, R.style.priceListTableRow, "#776074"));
                else
                    context.addView(createTableRow(activity, p, R.style.priceListTableCell, R.style.priceListTableRow, "#913860"));
                iter++;
                context.removeView(tableRow);
            }
        }
    }

    private TableRow createTableRow(Activity activity, PriceList p, int cellStyle, int rowStyle, String cellColor) {
        TableRow tableRow = new TableRow(new ContextThemeWrapper(activity, rowStyle));
        tableRow.setBackgroundColor(Color.parseColor(cellColor));
        TextView title = new TextView(new ContextThemeWrapper(activity, cellStyle));
        TextView time = new TextView(new ContextThemeWrapper(activity, cellStyle));
        TextView price = new TextView(new ContextThemeWrapper(activity, cellStyle));

        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) cellTitle.getLayoutParams();
        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) cellTime.getLayoutParams();
        LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams) cellPrice.getLayoutParams();

        title.setText(String.valueOf(p.getType()));
        time.setText(String.valueOf(p.getTime()));
        price.setText(String.valueOf(p.getPrice()));
        tableRow.addView(title, params1);
        tableRow.addView(time, params2);
        tableRow.addView(price, params3);
        return tableRow;
    }

}
