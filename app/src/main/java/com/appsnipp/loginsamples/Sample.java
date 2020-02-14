package com.appsnipp.loginsamples;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class Sample extends AppCompatActivity {
    ListView listSample,listSampleQTY;
    TextView Text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        Text = findViewById(R.id.textview);
        listSample = findViewById(R.id.listView);
        listSampleQTY = findViewById(R.id.listView2);




        Intent intent = getIntent();
        final String sam = intent.getStringExtra("Sample");
        final String testid = intent.getStringExtra("MasterTestID");
        final String ISTID = intent.getStringExtra("InspectTestID");
        final String LOT = intent.getStringExtra("LotNO");
        final String part = intent.getStringExtra("PartNO");

        int Sample = Integer.valueOf(sam);


       // downloadJSON("http://158.108.112.125/test/SampleQTY.php?testid="+ID+"&LotNO="+LOT);


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

        Intent intent = getIntent();
        final String sam = intent.getStringExtra("Sample");
        final String part = intent.getStringExtra("PartNO");
        final String testid = intent.getStringExtra("MasterTestID");
        final String LOT = intent.getStringExtra("LotNO");
        final String ISTID = intent.getStringExtra("InspectTestID");

        int Sample = Integer.valueOf(sam);


        JSONArray jsonArray = new JSONArray(json);
        final String[] stocks = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            stocks[i] = obj.getString("TestName");
        }


        Text.setText(part +"\nLOT : "+ LOT+"\n");

        final ArrayList<String> list = new ArrayList<String>();
        for(int i=1 ; i<=Sample ; i++){

            list.add("SAMPLE "+i);

        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stocks);
        listSampleQTY.setAdapter(arrayAdapter2);

        listSample.setAdapter(arrayAdapter);

        listSample.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long l) {
                int pos = listSample.getPositionForView(v);

                String Sample = Integer.toString(pos+1);
                //String value = listSample.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), Sample, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(com.appsnipp.loginsamples.Sample.this, Sample_Product.class);
                i.putExtra("Sample",sam);
                i.putExtra("MasterTestID",testid);
                i.putExtra("InspectTestID",ISTID);
                i.putExtra("SampleTest",Sample);
                startActivity(i);

            }
        });

    }
}
