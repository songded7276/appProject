package com.appsnipp.loginsamples;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Product extends AppCompatActivity {
    ImageView imgback;
    String CONTRACER;
    ListView listView;
    //String QRLot="MQ8979";
    TextView textpoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        imgback = findViewById(R.id.img_back);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        final String testid = intent.getStringExtra("MasterTestID");
        final String LOT = intent.getStringExtra("LotNO");
        final String LocID = intent.getStringExtra("LocID");


        int ID = Integer.valueOf(testid);
        int LocIDtest = Integer.valueOf(LocID);
        textpoint = findViewById(R.id.selpoint);
        listView = (ListView) findViewById(R.id.listView);

        downloadJSON(URLS.URL_ALL + "/test/php_get_data2.php?testid=" + ID + "&LotNO=" + LOT + "&LocID=" + LocIDtest);
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Intent newActivity = new Intent(Basket.this, Basket.class);
        recreate();
    }


    private void downloadJSON(final String urlWebService) {

        class DownloadJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {

                    if(s.equals("[]")||s.equals("NULL")){
                        Toast.makeText(getBaseContext(), "PartNO not exist.", Toast.LENGTH_LONG).show();
                        finish();

                    }
                    else {
                        loadIntoListView(s);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        DownloadJSON getJSON = new DownloadJSON();
        getJSON.execute();
    }

    private void loadIntoListView(String json) throws JSONException {

        Intent intent = getIntent();
        final String sam = intent.getStringExtra("Sample");
        final String part = intent.getStringExtra("PartNO");
        final String testid = intent.getStringExtra("MasterTestID");
        final String LOT = intent.getStringExtra("LotNO");
        final String TotalQty = intent.getStringExtra("TotalQTY");

        textpoint.setText(part + "\nLOT : " + LOT + "\n");
        JSONArray jsonArray = new JSONArray(json);
        final String[] stocks = new String[jsonArray.length()];
        String[] stocks2 = new String[jsonArray.length()];
        final String[] stocks3 = new String[jsonArray.length()];
        ArrayList<HashMap<String, Object>> MyArrList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            map = new HashMap<String, Object>();
            stocks[i] = obj.getString("TestName");
            stocks2[i] = obj.getString("PointQty");
            stocks3[i] = obj.getString("InspectTestID");
            map.put("TestName",stocks[i]);
            map.put("PointQty",stocks2[i]);
            MyArrList.add(map);
        }
        SimpleAdapter sAdap;
        sAdap = new SimpleAdapter(Product.this, MyArrList, R.layout.layout_listview_product,
                new String[] {"TestName", "PointQty"}, new int[] {R.id.TestName, R.id.PointQty});
        listView.setAdapter(sAdap);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                String ISTID = stocks3[position];
                String text = stocks[position];
                Intent i = new Intent(Product.this, point_select.class);
                i.putExtra("ToolName", text);
                i.putExtra("MasterTestID", testid);
                i.putExtra("Sample", sam);
                i.putExtra("PartNO", part);
                i.putExtra("InspectTestID", ISTID);
                i.putExtra("LotNO", LOT);
                i.putExtra("TotalQTY", TotalQty);

                startActivityForResult(i, 0);
                //Toast.makeText(getBaseContext(), text, Toast.LENGTH_LONG).show();

            }
        });

    }

    public static class Utility {

        public static void setListViewHeightBasedOnChildren(ListView listView) {
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null) {
                // pre-condition
                return;
            }

            int totalHeight = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            listView.setLayoutParams(params);
            listView.requestLayout();
        }
    }
}
