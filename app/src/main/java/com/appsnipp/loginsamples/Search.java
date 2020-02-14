package com.appsnipp.loginsamples;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Search extends AppCompatActivity {
    User user = PrefManager.getInstance(this).getUser();

    EditText text_search;
    ListView listView;
    JSONObject jsonObj = new JSONObject();
    Button btn_save, btn_cancel,selLo , btn_input;
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


    TextView text1,text2,text3,text4,text5,text6,text7;

    String setText1="";
    String setText2="";
    String setText3="";
    String setText4="";
    String setText5="";
    String setText6="";
    String setText7="";

    String data = "";

    String status = "";

    String SampleQTY="";
    String TotalQTY="";
    String Loc="";
    String MasID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ImageView back = findViewById(R.id.img_btn);
        Button search_btn = findViewById(R.id.btn_search);
        text_search = findViewById(R.id.editsearch);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String searchdata = text_search.getText().toString();
                data = searchdata;
                if (TextUtils.isEmpty(searchdata)) {
                    text_search.setError("Please enter QR Code");
                    text_search.requestFocus();
                    return;
                } else {
                    Intent i = new Intent(Search.this, SearchLoc.class);
                    i.putExtra("data", data);
                    startActivity(i);
                }
            }
        });





    }

    public void Searchdata (String data){
        setContentView(R.layout.activiti_detail);

            downloadJSON(URLS.URL_ALL + "/test/php_get_data.php?id=" + data);


    }
    private void downloadJSON(final String urlWebService) {
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_can);
        btnbackkk = findViewById(R.id.img_btn);
       // selLo = findViewById(R.id.btn_sel);
        text7 = findViewById(R.id.textView7);


        listView = (ListView) findViewById(R.id.listView);
        class DownloadJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                String test = status;
                listView = (ListView) findViewById(R.id.listView);
                try {
                    if(s.equals("[]")||s.equals("NULL")){
                        setContentView(R.layout.activity_create_ith);
                        downloadJSON2(URLS.URL_ALL + "/flask_detect_number/mis?LotNO=" + data);

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



                setText1 =  stocks[i];
                setText2 =  stocks2[i];
                setText3 =  stocks3[i];
                setText4 =  stocks4[i];
                setText5 =  stocks5[i];
                setText7 =  stocks7[i];

                status = stocks8[i];
            }
        }
        text1 =findViewById(R.id.textView1);
        text1.setText(setText3);
        text2 =findViewById(R.id.textView2);
        text2.setText(setText5);

        text4 =findViewById(R.id.textView4);
        text4.setText(setText2);
        text5 =findViewById(R.id.textView5);
        text5.setText(setText1);
        text6 =findViewById(R.id.textView6);
        text6.setText(setText4);
        text7.setText(setText7);



         if(status.equals("confirm") ||status.equals("verify")|| status.equals("approve")) {
             setContentView(R.layout.activity_not_staff2);
             btn_cancel = findViewById(R.id.btn_can);
             btnbackkk = findViewById(R.id.img_btn);
             text1 = findViewById(R.id.textView1);
             text1.setText(setText3);
             text2 = findViewById(R.id.textView2);
             text2.setText(setText2);

             text4 = findViewById(R.id.textView4);
             text4.setText(setText4);
             text5 = findViewById(R.id.textView5);
             text5.setText(setText5);

             text7.setText(setText7);


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
        }else {

         }

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sam = stocks2[0];
                String part = stocks3[0];
                String testid = stocks4[0];
                String LotNO = stocks5[0];
                String LocID = stocks6[0];
                Intent i = new Intent(Search.this, Product.class);
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


    /*--------------------------------------------------------------*/
    private void downloadJSON2(final String urlWebService) {
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_can);
        btnbackkk = findViewById(R.id.img_btn);
        text7 = findViewById(R.id.textView7);
        btn_input = findViewById(R.id.btn_input);



        listView = (ListView) findViewById(R.id.listView);
        class DownloadJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                listView = (ListView) findViewById(R.id.listView);
                try {
                    if(s.equals("[]")||s.equals("NULL")){

                    }
                    else {
                        loadIntoListView2(s);
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


    private void loadIntoListView2(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] stocks = new String[1];
        User user = PrefManager.getInstance(this).getUser();

        if (jsonArray.length() == 0) {
            stocks[0] = "NULL";
        } else {
            stocks = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);


//                jsonObj.put("TotalQTY", obj.getString("TotalQTY"));
//                jsonObj.put("LocID", obj.getString("LocID"));
//                jsonObj.put("SampleQTY", obj.getString("SampleQTY"));


                stocks[i] = obj.getString("StartQty");
                stocks3[0] = obj.getString("PartNO");
                stocks5[0] = obj.getString("LotNO");

//                stocks2[0] = obj.getString("SampleQTY");
//                stocks4[0] = obj.getString("MasterTestID");
//                stocks6[0] = obj.getString("LocID");
//                stocks7[0] = obj.getString("Location");

                setText1 =  stocks3[i];
                setText2 =  stocks5[i];
                setText3 =  stocks3[i];
//                setText4 =  stocks2[i];
                setText5 =  stocks[i];
//                MasID = stocks4[0];

            }
        }
        text1 =findViewById(R.id.textView1);
        text1.setText(setText1);
        text2 =findViewById(R.id.textView2);
        text2.setText(setText2);

        text4 =findViewById(R.id.textView4);
        text4.setText(setText4);
        text5 =findViewById(R.id.textView5);
        text5.setText(setText5);

        downloadJSON3(URLS.URL_ALL + "/test/QueryLoc.php");
        SampleQTY = text4.getText().toString();




        final String[] m_Text = {""};
            btn_input.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Search.this);
                    builder.setTitle("TotalQTY ");

// Set up the input
                    final EditText input = new EditText(Search.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    input.setInputType(InputType.TYPE_CLASS_TEXT );
                    builder.setView(input);

// Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            m_Text[0] = input.getText().toString();
                            text5.setText(m_Text[0]);




                            Integer SampleTotal = 0;
                            Integer Sampletest = Integer.parseInt(m_Text[0]);
                            if(Sampletest>=16 && Sampletest<=25){
                                SampleTotal = 2;
                            }else if(Sampletest>=26 && Sampletest<=50){
                                SampleTotal = 3;
                            }else if(Sampletest>=51 && Sampletest<=90){
                                SampleTotal = 3;
                            }else if(Sampletest>=91 && Sampletest<=150){
                                SampleTotal = 3;
                            }else if(Sampletest>=151 && Sampletest<=280){
                                SampleTotal = 5;
                            }else if(Sampletest>=281 && Sampletest<=500){
                                SampleTotal = 5;
                            }else if(Sampletest>=501 && Sampletest<=1200){
                                SampleTotal = 5;
                            }else if(Sampletest>=1201 && Sampletest<=3200){
                                SampleTotal = 8;
                            }else if(Sampletest>=3201 && Sampletest<=10000){
                                SampleTotal = 8;
                            }else if(Sampletest>=10001 && Sampletest<=35000){
                                SampleTotal = 8;
                            }else if(Sampletest>=35001 && Sampletest<=150000){
                                SampleTotal = 13;
                            }
                            text4.setText(String.valueOf(SampleTotal));

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
            });

        TotalQTY =  text5.getText().toString();
        selLo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builderSingle = new android.app.AlertDialog.Builder(Search.this);
                builderSingle.setTitle("Select Location:-");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Search.this, android.R.layout.select_dialog_singlechoice);
//                    String[] arrOfStr = stocksType[0].split(",", 10);
                    for(int i = 0 ; i < Locname.length ; i++) {
                        if (Locname[i] == null) {

                        } else {
                            arrayAdapter.add(Locname[i]);
                        }
                    }
//                arrayAdapter.add("IQC");
//                arrayAdapter.add("OQC");

                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        android.app.AlertDialog.Builder builderInner = new android.app.AlertDialog.Builder(Search.this);
                        builderInner.setMessage(strName);

                        text7.setText(strName);
                        for(int i = 0 ; i < Locname.length ; i++) {
                            if (Locname[i] == null) {

                            } else {
                               if(strName.equals(Locname[i])){
                                   Loc = LocID[i];
                               }
                            }
                        }

                    }
                });
                builderSingle.show();
            }
        });



        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] values = new String[1];
                values[0] = "";
                registerUser(values);
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

    /*--------------------------------------------------------------*/
    private void downloadJSON3(final String urlWebService) {
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_can);
        btnbackkk = findViewById(R.id.img_btn);
        text7 = findViewById(R.id.textView7);
        btn_input = findViewById(R.id.btn_input);



        listView = (ListView) findViewById(R.id.listView);
        class DownloadJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                listView = (ListView) findViewById(R.id.listView);
                try {
                    if(s.equals("[]")||s.equals("NULL")){

                    }
                    else {
                        loadIntoListView3(s);
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


    private void loadIntoListView3(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] stocks = new String[1];

        if (jsonArray.length() == 0) {
            stocks[0] = "NULL";
        } else {
            stocks = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);


//                jsonObj.put("TotalQTY", obj.getString("TotalQTY"));
//                jsonObj.put("LocID", obj.getString("LocID"));
//                jsonObj.put("SampleQTY", obj.getString("SampleQTY"));


                Locname[i] = obj.getString("LocName");
                LocID[i] = obj.getString("LocID");

            }
        }


    }

    private void registerUser(String[] values) {
        //String[] values = new String[buttonArray.size()];
        //if it passes all the validations
        //executing the async task
        Search.RegisterUser ru = new Search.RegisterUser(values);
        ru.execute();
    }

    /*--------------------------------------------------------------*/

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


            // final String name = intent.getStringExtra("ToolName");
            //final String lot = intent.getStringExtra("QRLot");


            String sam = stocks2[0];
            String part = stocks3[0];
            String testid = stocks4[0];
            String LotNO = stocks5[0];
            String LocID = stocks6[0];
            Intent i = new Intent(Search.this, Product.class);
            i.putExtra("Sample", sam);
            i.putExtra("PartNO", part);
            i.putExtra("MasterTestID", testid);
            i.putExtra("LotNO", LotNO);
            i.putExtra("LocID", LocID);

            //hiding the progressbar after completion
//            progressBar.setVisibility(View.GONE);
            try {
                //converting response to json object
                JSONObject obj = new JSONObject(s);
                //if no error in response
                if (!obj.getBoolean("error")) {

                    finish();

                    startActivity(i);

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
            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            try {
                jsonObj.put("TotalQTY",  TotalQTY);
                jsonObj.put("LocID", Loc);
                jsonObj.put("SampleQTY", SampleQTY);
                jsonObj.put("LotNO", setText2);
                jsonObj.put("UserID", user.getId());
                jsonObj.put("MasterTestID", MasID);

            } catch (Exception e) {
                e.printStackTrace();
            }

            params.put("data", jsonObj.toString());
            //params.put("data", "test");
            //returing the response
            return requestHandler.sendPostRequest(URLS.URL_INSERTPRODUCT, params);
        }


    }


}
