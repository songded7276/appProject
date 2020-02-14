package com.appsnipp.loginsamples;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class point_select extends AppCompatActivity {
    ListView listView, listView2, listView3;
    TextView text;
    ImageView imgback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_select);

        imgback = findViewById(R.id.img_back);

        listView = (ListView) findViewById(R.id.listView);
        listView2 = (ListView) findViewById(R.id.listView2);
        listView3 = (ListView) findViewById(R.id.listView3);
        text = findViewById(R.id.tool_name);
        Intent intent = getIntent();
        final String name = intent.getStringExtra("ToolName");
        final String testid = intent.getStringExtra("MasterTestID");
        final String inspectid = intent.getStringExtra("InspectTestID");
        final String LOT = intent.getStringExtra("LotNO");
        final String part = intent.getStringExtra("PartNO");
        final String sam = intent.getStringExtra("Sample");

        text.setText(name+"\nLOT : "+ LOT+"\n");

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });


        int ID = Integer.valueOf(testid);
        downloadJSON(URLS.URL_ALL+"/test/php_get_data3.php?testid=" + ID + "&ToolName=" + name + "&InspectTestID=" + inspectid);
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
                    loadIntoListView(s);
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
        JSONArray jsonArray = new JSONArray(json);
        Intent intent = getIntent();
        final String testid = intent.getStringExtra("MasterTestID");
        final String sam = intent.getStringExtra("Sample");
        final String LOT = intent.getStringExtra("LotNO");
        final String ISTID = intent.getStringExtra("InspectTestID");
        final String part = intent.getStringExtra("PartNO");
        final String[] stocks = new String[jsonArray.length()];
        //final String[] stocks2 = new String[jsonArray.length()];
        final String[] stocks2 = new String[jsonArray.length()];
        final String[] stocks3 = new String[jsonArray.length()];
        final String[] stocks4 = new String[jsonArray.length()];
        final String[] stocks5 = new String[jsonArray.length()];
        final String[] stocks6 = new String[jsonArray.length()];
        final String[] stocks7 = new String[jsonArray.length()];
        final String[] stocks8 = new String[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            stocks[i] = obj.getString("TestNO");
            stocks2[i] = obj.getString("TestName");
            stocks3[i] = obj.getString("TestedQty") + " / " + obj.getString("SampleQTY");
            stocks4[i] = obj.getString("MasterTestDetailID");
            stocks5[i] = obj.getString("TestValueTypeID");
            stocks6[i] = obj.getString("MinimumValue");
            stocks7[i] = obj.getString("MaximumValue");
            stocks8[i] = obj.getString("Spec");
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stocks);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stocks2);
        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stocks3);
        listView.setAdapter(arrayAdapter);
        listView2.setAdapter(arrayAdapter2);
        listView3.setAdapter(arrayAdapter3);
        Product.Utility.setListViewHeightBasedOnChildren(listView);
        Product.Utility.setListViewHeightBasedOnChildren(listView2);
        Product.Utility.setListViewHeightBasedOnChildren(listView3);


        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                String text = stocks2[position];
                String text2 = stocks4[position];
                String TestType = stocks5[position];
                String Min = stocks6[position];
                String Max = stocks7[position];
                String TestNO = stocks[position];
                String Spec = stocks8[position];

                int TypeID = Integer.valueOf(TestType);

                if (TypeID == 0) {
                    Intent i = new Intent(point_select.this, inputdata.class);
                    i.putExtra("ToolName", text);
                    i.putExtra("Sample", sam);
                    i.putExtra("MasterTestID", testid);
                    i.putExtra("MasterTestDetailID", text2);
                    i.putExtra("InspectTestID", ISTID);
                    i.putExtra("MinimumValue", Min);
                    i.putExtra("MaximumValue", Max);
                    i.putExtra("TestNO", TestNO);
                    i.putExtra("LotNO",LOT);
                    i.putExtra("PartNO",part);
                    startActivityForResult(i,0);
                } else {
                    Intent i = new Intent(point_select.this, InputDataButton.class);
                    i.putExtra("ToolName", text);
                    i.putExtra("Sample", sam);
                    i.putExtra("MasterTestID", testid);
                    i.putExtra("MasterTestDetailID", text2);
                    i.putExtra("InspectTestID", ISTID);
                    i.putExtra("TestValueTypeID", TestType);
                    i.putExtra("TestNO", TestNO);
                    i.putExtra("LotNO",LOT);
                    i.putExtra("PartNO",part);
                    i.putExtra("Spec",Spec);
                    startActivityForResult(i,0);
                }
            }
        });
    }
}