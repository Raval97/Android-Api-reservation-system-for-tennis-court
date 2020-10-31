package com.example.tenniscourtreservation;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.tenniscourtreservation.model.PriceList;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;

public class PriceListActivity extends Activity {

    MenuTools menuTools;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.price_list);
        getActionBar().hide();

        menuTools = new MenuTools(this, (Button) findViewById(R.id.priceListMenu));
        menuTools.done();

        new PriceListActivity.HttpReqTask().execute();
    }

    private class HttpReqTask extends AsyncTask<Void, Void, PriceList[]> {
        @Override
        protected PriceList[] doInBackground(Void... voids) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(new URL("http://10.0.2.2:8080/ourTennis/priceList.json"));
                PriceList[] priceList = mapper.convertValue(jsonNode.get("priceList"), PriceList[].class);
                Boolean b1 = mapper.convertValue(jsonNode.get("logged"), Boolean.class);
                Boolean b2 = mapper.convertValue(jsonNode.get("isAdmin"), Boolean.class);
                System.out.println(b1 + " " + b2 + "\n#################");
                return priceList;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(PriceList[] priceLists) {
            super.onPostExecute(priceLists);
            for (PriceList p : priceLists) {
                Log.i("PriceList: ", "#######################");
                Log.i("PriceList id: ", String.valueOf(p.getId()));
                Log.i("PriceList price: ", String.valueOf(p.getPrice()));
                Log.i("PriceList time: ", String.valueOf(p.getTime()));
                Log.i("PriceList type: ", String.valueOf(p.getType()));
            }
        }
    }

}
