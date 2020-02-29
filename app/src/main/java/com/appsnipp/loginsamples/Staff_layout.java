package com.appsnipp.loginsamples;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.HashMap;

public class Staff_layout extends AppCompatActivity {


    EditText text_search;
    ListView listView;
    JSONObject jsonObj = new JSONObject();
    Button btn_save, btn_cancel, btn_edit, btn_input;
    ImageView btnbackkk;
    final String[] stocks2 = new String[1];
    final String[] stocks3 = new String[1];
    final String[] stocks4 = new String[1];
    final String[] stocks5 = new String[1];
    final String[] stocks6 = new String[1];
    final String[] stocks7 = new String[1];
    final String[] stocks8 = new String[1];
    final String[] Locname = new String[50];
    final String[] LocID = new String[50];


    TextView text1, text2, text3, text4, text5, text6, text7;

    User user = PrefManager.getInstance(this).getUser();

    int state = 0;

    String setText1 = "";
    String setText2 = "";
    String setText3 = "";
    String setText4 = "";
    String setText5 = "";
    String setText6 = "";
    String setText7 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_layout);

        Intent intent = getIntent();
        final String testid = intent.getStringExtra("InspectTestID");


        Integer data = Integer.valueOf(testid);

        downloadJSON(URLS.URL_ALL + "/test/QueryITH.php?id=" + data);
    }

    private void downloadJSON(final String urlWebService) {
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_can);
        btnbackkk = findViewById(R.id.img_back);
        // selLo = findViewById(R.id.btn_sel);
        text7 = findViewById(R.id.textView7);
        btn_edit = findViewById(R.id.btn_edit);

        Intent intent = getIntent();
        final String Value = intent.getStringExtra("Value");

        btn_save.setText("Confirm");
        if (!Value.equals("100.0")) {
            btn_save.setEnabled(false);
            btn_save.setVisibility(View.GONE);
        }


        listView = (ListView) findViewById(R.id.listView);
        class DownloadJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                listView = (ListView) findViewById(R.id.listView);
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
        final String Value = intent.getStringExtra("Value");
        JSONArray jsonArray = new JSONArray(json);
        String[] stocks = new String[1];
        User user = PrefManager.getInstance(this).getUser();

        if (jsonArray.length() == 0) {
            stocks[0] = "NULL";
        } else {
            stocks = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);


                stocks[i] = obj.getString("TotalQTY");
                stocks2[0] = obj.getString("SampleQTY");
                stocks3[0] = obj.getString("PartNO");
                stocks4[0] = obj.getString("MasterTestID");
                stocks5[0] = obj.getString("LotNO");
                stocks6[0] = obj.getString("LocID");
                stocks7[0] = obj.getString("Location");
                stocks8[0] = obj.getString("Inspect_status");


                setText1 = stocks[i];
                setText2 = stocks2[i];
                setText3 = stocks3[i];
                setText4 = stocks4[i];
                setText5 = stocks5[i];
                setText6 = stocks4[i];
                setText7 = stocks7[i];
            }
        }
        text1 = findViewById(R.id.textView1);
        text1.setText(setText3);
        text2 = findViewById(R.id.textView2);
        text2.setText(setText5);

        text4 = findViewById(R.id.textView4);
        text4.setText(setText2);
        text5 = findViewById(R.id.textView5);
        text5.setText(setText1);
        text6 = findViewById(R.id.textView6);
        text6.setText(setText6);
        text7.setText(setText7);


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder1 = new AlertDialog.Builder(Staff_layout.this);
                builder1.setMessage("Do you want to confirm?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String[] val = new String[0];
                                state = 0;
                                registerUser(val);
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sam = stocks2[0];
                String part = stocks3[0];
                String testid = stocks4[0];
                String LotNO = stocks5[0];
                String LocID = stocks6[0];
                Intent i = new Intent(Staff_layout.this, Product.class);
                i.putExtra("Sample", sam);
                i.putExtra("PartNO", part);
                i.putExtra("MasterTestID", testid);
                i.putExtra("LotNO", LotNO);
                i.putExtra("LocID", LocID);
                startActivity(i);
            }
        });
        btnbackkk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void registerUser(String[] values) {
        //String[] values = new String[buttonArray.size()];
        //if it passes all the validations
        //executing the async task
        RegisterUser ru = new RegisterUser(values);
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


            Intent i = new Intent(Staff_layout.this, activity_staff.class);


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
                jsonDetails.put("IDuser", String.valueOf(user.getId()));
                jsonDetails.put("InspectTestID", InspectTestID);
                jsonDetails.put("Inspect_status", "confirm");

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
