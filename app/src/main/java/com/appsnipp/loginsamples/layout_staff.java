package com.appsnipp.loginsamples;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class layout_staff extends AppCompatActivity {
    ImageView imgback;
    ListView listView ;
    User user = PrefManager.getInstance(this).getUser();

    int state = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_staff);
        imgback = findViewById(R.id.img_btn);
        listView = findViewById(R.id.listView);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button btn_conf = findViewById(R.id.btn_save);
        Button btn_rej = findViewById(R.id.btn_can);

        Intent intent = getIntent();
        final String InspectTestID = intent.getStringExtra("InspectTestID");
        downloadJSON(URLS.URL_ALL+"/test/QueryIsID.php?InspectTestID="+InspectTestID);


        btn_conf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] val = new String[0];
                state = 0;
                registerUser(val);

            }
        });
        btn_rej.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] val = new String[0];
                state = 1;
                registerUser(val);
            }
        });
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
        final String[] stocks = new String[jsonArray.length()];
        final String[] stocks2 = new String[jsonArray.length()];
        final String[] stocks3 = new String[jsonArray.length()];
        final String[] stocks4 = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            stocks[i] = "LotNO: " +obj.getString("LotNO") +"\nLocID: " +
                        obj.getString("LocID")+"\nTotalQTY: "+obj.getString("TotalQTY")+
                        "\nSampleQTY: "+obj.getString("SampleQTY")+"\nMatLotNO: "+obj.getString("MatLotNO");
            stocks2[i] = obj.getString("LotNO");
            stocks3[i] = obj.getString("MasterTestID");
            stocks4[i] = obj.getString("LocID");
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stocks);
        listView.setAdapter(arrayAdapter);
        activity_staff.Utility.setListViewHeightBasedOnChildren(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                String LotNO = stocks2[position];
                String MasterTestID = stocks3[position];
                String LocID = stocks4[position];
                Intent i = new Intent(layout_staff.this, Product.class);
                i.putExtra("LotNO", LotNO);
                i.putExtra("MasterTestID", MasterTestID);
                i.putExtra("LocID", LocID);
                //startActivity(i);
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
    private void registerUser(String[] values) {
        //String[] values = new String[buttonArray.size()];
        //if it passes all the validations
        //executing the async task
        layout_staff.RegisterUser ru = new layout_staff.RegisterUser(values);
        ru.execute();
    }



    private class RegisterUser extends AsyncTask<Void, Void, String> {
        private String[] values;

        RegisterUser(String[] value) {
            this.values = value;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);



            Intent i = new Intent(layout_staff.this, activity_staff.class);


            //hiding the progressbar after completion
//            progressBar.setVisibility(View.GONE);
            try {
                //converting response to json object
                JSONObject obj = new JSONObject(s);
                //if no error in response
                if (!obj.getBoolean("error")) {

                    finish();


                } else {
                    Toast.makeText(getApplicationContext(), "กรอกยังบ่ครบครับ", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            //creating request handler object
            RequestHandler requestHandler = new RequestHandler();


            Intent intent = getIntent();
            final String InspectTestID = intent.getStringExtra("InspectTestID");


            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            JSONObject jsonObj = new JSONObject();
            JSONObject jsonDetails = new JSONObject();
            try {
                jsonDetails.put("IDuser",String.valueOf(user.getId()));
                jsonDetails.put("InspectTestID",InspectTestID);
                if(String.valueOf(state).equals("0")) {
                    if (user.getMember_info().equals("staff")) {
                        jsonDetails.put("Inspect_status", "verify");
                    } else if (user.getMember_info().equals("SV")) {
                        jsonDetails.put("Inspect_status", "approve");
                    } else {
                        jsonDetails.put("Inspect_status", "success");
                    }
                } else {
                    jsonDetails.put("Inspect_status", "reject");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                jsonObj.put("Details", jsonDetails);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            params.put("data", jsonObj.toString());
            //params.put("data", "test");
            //returing the response
            return requestHandler.sendPostRequest(URLS.URL_STATUS, params);
        }


    }
}
