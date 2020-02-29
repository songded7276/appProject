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

public class Search2 extends AppCompatActivity {
    User user = PrefManager.getInstance(this).getUser();

    EditText text_search;
    ListView listView;
    JSONObject jsonObj = new JSONObject();
    Button btn_save, btn_cancel, selLo, btn_input ,btn_Matlot;
    ImageView btnbackkk;
    final String[] stocks2 = new String[1];
    final String[] stocks3 = new String[1];
    final String[] stocks4 = new String[1];
    final String[] stocks5 = new String[1];
    final String[] stocks6 = new String[1];
    final String[] stocks7 = new String[1];
    final String[] stocks8 = new String[1];
    final String[] stocks9 = new String[1];
    final String[] FirstName = new String[1];
    final String[] LastName = new String[1];
    final String[] Locname = new String[50];
    final String[] LocID = new String[50];
    final String[] MasterTestID = new String[50];
    final String[] IssueDate = new String[50];


    TextView text1, text2, text3, text4, text5, text6, text7,MatLotNO;

    String setText1 = "";
    String setText2 = "";
    String setText3 = "";
    String setText4 = "";
    String setText5 = "";
    String setText6 = "";
    String setText7 = "";
    String Matlot = "";

    String data2 = "";

    String status = "";


    String MTID = "";

    String SampleQTY = "";
    String TotalQTY = "";
    String Loc = "";
    String MasID = "";
    String IssueDateValuue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiti_detail);
        ImageView back = findViewById(R.id.img_btn);
        Button search_btn = findViewById(R.id.btn_search);
        text_search = findViewById(R.id.editsearch);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        final String Loc = intent.getStringExtra("Loc");
        final String data = intent.getStringExtra("data");
        data2 = data;

        downloadJSON(URLS.URL_ALL + "/test/php_get_data.php?id=" + data + "&LocID=" + Loc);


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
                String test = status;
                listView = (ListView) findViewById(R.id.listView);
                try {
                    if (s.equals("[]") || s.equals("NULL")) {
                        setContentView(R.layout.activity_create_ith);
                        downloadJSON2(URLS.URL_ALL + "/flask_detect_number/mis?LotNO=" + data2);
                    } else {
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
                stocks9[0] = obj.getString("UserID");
                FirstName[0] = obj.getString("FirstName");
                LastName[0] = obj.getString("LastName");


                setText1 = stocks[i];
                setText2 = stocks2[i];
                setText3 = stocks3[i];
                setText4 = stocks4[i];
                setText5 = stocks5[i];
                setText7 = stocks7[i];

                status = stocks8[i];
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
        text6.setText(setText4);
        text7.setText(setText7);


        if (status.equals("confirm") || status.equals("verify") || status.equals("approve")) {
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
        }else  if(!stocks9[0].equals(String.valueOf(user.getId()))){
            AlertDialog.Builder builder = new AlertDialog.Builder(Search2.this);
            builder.setCancelable(false);
            builder.setMessage("This lot brlong to "+ FirstName[0]+" "+LastName[0]);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                   finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else {

        }

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sam = stocks2[0];
                String part = stocks3[0];
                String testid = stocks4[0];
                String LotNO = stocks5[0];
                String LocID = stocks6[0];
                Intent i = new Intent(Search2.this, Product.class);
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
        btn_Matlot = findViewById(R.id.btn_input_matlot);


        listView = (ListView) findViewById(R.id.listView);
        class DownloadJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    if (!TextUtils.isEmpty(s) && s.equals("[]")) {
                           AlertDialog.Builder builder = new AlertDialog.Builder(Search2.this);
                           builder.setCancelable(false);
                           builder.setMessage("LotNO not exist.");
                           builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int id) {
                                   finish();
                               }
                           });
                           AlertDialog dialog = builder.create();
                           dialog.show();

                    }else if(TextUtils.isEmpty(s)){
                        AlertDialog.Builder builder = new AlertDialog.Builder(Search2.this);
                        builder.setCancelable(false);
                        builder.setMessage("Connect MIS Fail.");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
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


                stocks[i] = obj.getString("CurrentQty");
                stocks3[0] = obj.getString("PartNo");
                stocks5[0] = obj.getString("LotNo");
                IssueDate[0] = obj.getString("IssueDate");

//                stocks2[0] = obj.getString("SampleQTY");
//                stocks4[0] = obj.getString("MasterTestID");
//                stocks6[0] = obj.getString("LocID");
//                stocks7[0] = obj.getString("Location");

                setText1 = stocks3[i];
                setText2 = stocks5[i];
                setText3 = stocks3[i];
//                setText4 =  stocks2[i];
                setText5 = stocks[i];
//                MasID = stocks4[0];

            }
        }


        text1 = findViewById(R.id.textView1);
        text1.setText(setText1);
        text2 = findViewById(R.id.textView2);
        text2.setText(setText2);

        text4 = findViewById(R.id.textView4);
        text5 = findViewById(R.id.textView5);
        text5.setText(setText5);
        MatLotNO = findViewById(R.id.textMatlot);


        Integer SampleTotal = 0;
        Integer Sampletest = 0;
        if (setText5.equals("null")) {
            Intent intent = new Intent(getApplicationContext(), test.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Toast.makeText(getBaseContext(), "LotNO not exist.", Toast.LENGTH_LONG).show();
            startActivity(intent);
        } else {
            Sampletest = Integer.valueOf(setText5);
            // Sampletest = 500;
        }
        if (Sampletest >= 16 && Sampletest <= 25) {
            SampleTotal = 2;
        } else if (Sampletest >= 26 && Sampletest <= 50) {
            SampleTotal = 3;
        } else if (Sampletest >= 51 && Sampletest <= 90) {
            SampleTotal = 3;
        } else if (Sampletest >= 91 && Sampletest <= 150) {
            SampleTotal = 3;
        } else if (Sampletest >= 151 && Sampletest <= 280) {
            SampleTotal = 5;
        } else if (Sampletest >= 281 && Sampletest <= 500) {
            SampleTotal = 5;
        } else if (Sampletest >= 501 && Sampletest <= 1200) {
            SampleTotal = 5;
        } else if (Sampletest >= 1201 && Sampletest <= 3200) {
            SampleTotal = 8;
        } else if (Sampletest >= 3201 && Sampletest <= 10000) {
            SampleTotal = 8;
        } else if (Sampletest >= 10001 && Sampletest <= 35000) {
            SampleTotal = 8;
        } else if (Sampletest >= 35001) {
            SampleTotal = 13;
        }
        text4.setText(String.valueOf(SampleTotal));

        Intent intent = getIntent();
        final String Loc2 = intent.getStringExtra("Loc");
        Loc = Loc2;
        downloadJSON3(URLS.URL_ALL + "/test/QueryLocAndMID.php?partNO=" + setText1 + "&LocID=" + Loc2);


        final String[] m_Text = {""};
        btn_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Search2.this);
                builder.setTitle("TotalQTY ");
                final EditText input = new EditText(Search2.this);
                input.setText(TotalQTY);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text[0] = input.getText().toString();
                        text5.setText(m_Text[0]);

                        Integer SampleTotal = 0;
                        Integer Sampletest = Integer.parseInt(m_Text[0]);
                        if (Sampletest >= 16 && Sampletest <= 25) {
                            SampleTotal = 2;
                        } else if (Sampletest >= 26 && Sampletest <= 50) {
                            SampleTotal = 3;
                        } else if (Sampletest >= 51 && Sampletest <= 90) {
                            SampleTotal = 3;
                        } else if (Sampletest >= 91 && Sampletest <= 150) {
                            SampleTotal = 3;
                        } else if (Sampletest >= 151 && Sampletest <= 280) {
                            SampleTotal = 5;
                        } else if (Sampletest >= 281 && Sampletest <= 500) {
                            SampleTotal = 5;
                        } else if (Sampletest >= 501 && Sampletest <= 1200) {
                            SampleTotal = 5;
                        } else if (Sampletest >= 1201 && Sampletest <= 3200) {
                            SampleTotal = 8;
                        } else if (Sampletest >= 3201 && Sampletest <= 10000) {
                            SampleTotal = 8;
                        } else if (Sampletest >= 10001 && Sampletest <= 35000) {
                            SampleTotal = 8;
                        } else if (Sampletest >= 35001) {
                            SampleTotal = 13;
                        }
                        text4.setText(String.valueOf(SampleTotal));
                        SampleQTY = text4.getText().toString();
                        TotalQTY = text5.getText().toString();

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


        SampleQTY = text4.getText().toString();
        TotalQTY = text5.getText().toString();

        final String[] MatlotValue = {""};
        btn_Matlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Search2.this);
                builder.setTitle("TotalQTY ");
                final EditText input = new EditText(Search2.this);
                input.setText(Matlot);
                input.setInputType(InputType.TYPE_CLASS_TEXT );
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MatlotValue[0] = input.getText().toString();
                        MatLotNO.setText(MatlotValue[0]);
                        Matlot = MatLotNO.getText().toString();

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
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] values = new String[1];
                if (Matlot.equals("") || TotalQTY.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Search2.this);
                    builder.setCancelable(false);
                    builder.setMessage("Plesae Enter MatLot & TotalQTY.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }else {

                    values[0] = "";
                    registerUser(values);
                }
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
                listView = (ListView) findViewById(R.id.listView);
                try {
                    if (s.equals("[]") || s.equals("NULL")) {
                        Toast.makeText(getBaseContext(), "Location not exist.", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
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


                Locname[i] = obj.getString("LocName");
                LocID[i] = obj.getString("LocID");
                MasterTestID[i] = obj.getString("MasterTestID");

            }
        }
        Intent intent = getIntent();
        final String Loc = intent.getStringExtra("Loc");
        MTID = MasterTestID[0];
        IssueDateValuue = IssueDate[0];
        for (int i = 0; i < LocID.length; i++) {
            if (LocID[i] == null) {

            } else {
                if (Loc.equals(LocID[i])) {
                    text7.setText(Locname[i]);
                }
            }
        }

    }

    private void registerUser(String[] values) {
        //String[] values = new String[buttonArray.size()];
        //if it passes all the validations
        //executing the async task
        Search2.RegisterUser ru = new Search2.RegisterUser(values);
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

            Intent i = new Intent(Search2.this, Product.class);
            i.putExtra("Sample", SampleQTY);
            i.putExtra("PartNO", setText1);
            i.putExtra("MasterTestID", MTID);
            i.putExtra("IssueDate", IssueDateValuue);
            i.putExtra("LotNO", setText2);
            i.putExtra("LocID", Loc);

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
                    Toast.makeText(getApplicationContext(), "Location & PartNo not exist.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            String Test = SampleQTY;
            //creating request handler object
            RequestHandler requestHandler = new RequestHandler();
            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            try {
                jsonObj.put("TotalQTY", TotalQTY);
                jsonObj.put("LocID", Loc);
                jsonObj.put("SampleQTY", SampleQTY);
                jsonObj.put("LotNO", setText2);
                jsonObj.put("UserID", user.getId());
                jsonObj.put("MasterTestID", MTID);
                jsonObj.put("MatLot", Matlot);
                jsonObj.put("IssueDate", IssueDateValuue);

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
