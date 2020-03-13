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
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class point_select extends AppCompatActivity {
    ListView listView;
    TextView text;
    ImageView imgback;
    String Sample="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_select);

        imgback = findViewById(R.id.img_back);

        listView = (ListView) findViewById(R.id.listView);
        text = findViewById(R.id.tool_name);
        Intent intent = getIntent();
        final String name = intent.getStringExtra("ToolName");
        final String testid = intent.getStringExtra("MasterTestID");
        final String inspectid = intent.getStringExtra("InspectTestID");
        final String LOT = intent.getStringExtra("LotNO");
        final String TotalQTY = intent.getStringExtra("TotalQTY");

        text.setText(name+"\nLOT : "+ LOT+"\n");

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        int ID = Integer.valueOf(testid);
        downloadJSON(URLS.URL_ALL+"/test/php_get_data3.php?testid=" + ID + "&ToolName=" + name + "&InspectTestID=" + inspectid + "&TotalQTY=" + TotalQTY);
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
        final String[] sample = new String[jsonArray.length()];
        ArrayList<HashMap<String, Object>> MyArrList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map;

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            map = new HashMap<String, Object>();
            stocks[i] = obj.getString("TestNO");
            stocks2[i] = obj.getString("TestName");
            stocks3[i] = obj.getString("TestedQty") + " / " + obj.getString("SampleQTY");
            stocks4[i] = obj.getString("MasterTestDetailID");
            stocks5[i] = obj.getString("TestValueTypeID");
            stocks6[i] = obj.getString("MinimumValue");
            stocks7[i] = obj.getString("MaximumValue");
            stocks8[i] = obj.getString("Spec");
            sample[i] = obj.getString("SampleQTY");
            map.put("TestNO",stocks[i]);
            map.put("TestName",stocks2[i]);
            map.put("TestedQty",stocks3[i]);
            MyArrList.add(map);
        }
        SimpleAdapter sAdap;
        sAdap = new SimpleAdapter(point_select.this, MyArrList, R.layout.layout_listview_point_select,
                new String[] {"TestName", "PointQty","TestedQty"}, new int[] {R.id.TestNO, R.id.TestName,R.id.TestedQty});
        listView.setAdapter(sAdap);

        Sample = sample[0];

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                    i.putExtra("Sample", Sample);
                    i.putExtra("MasterTestID", testid);
                    i.putExtra("MasterTestDetailID", text2);
                    i.putExtra("InspectTestID", ISTID);
                    i.putExtra("MinimumValue", Min);
                    i.putExtra("MaximumValue", Max);
                    i.putExtra("TestNO", TestNO);
                    i.putExtra("LotNO",LOT);
                    i.putExtra("PartNO",part);
                    i.putExtra("TestType",TestType);
                    startActivityForResult(i,0);
                }else if(TypeID == 2){
                    Intent i = new Intent(point_select.this, inputdata_CTM.class);
                    i.putExtra("ToolName", text);
                    i.putExtra("Sample", Sample);
                    i.putExtra("MasterTestID", testid);
                    i.putExtra("MasterTestDetailID", text2);
                    i.putExtra("InspectTestID", ISTID);
                    i.putExtra("MinimumValue", Min);
                    i.putExtra("MaximumValue", Max);
                    i.putExtra("TestNO", TestNO);
                    i.putExtra("LotNO",LOT);
                    i.putExtra("PartNO",part);
                    i.putExtra("TestType",TestType);
                    startActivityForResult(i,0);
                }
                else {
                    Intent i = new Intent(point_select.this, InputDataButton.class);
                    i.putExtra("ToolName", text);
                    i.putExtra("Sample", Sample);
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