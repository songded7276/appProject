package com.appsnipp.loginsamples;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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

public class SearchLoc extends AppCompatActivity {
    Button btn_search, btn_cancel, selLo;

    ImageView btnbackkk;

    final String[] Locname = new String[50];
    final String[] LocID = new String[50];

    TextView TextLoc;

    String Loc = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_loc);

        btn_search = findViewById(R.id.btn_search);
        btn_cancel = findViewById(R.id.btn_cancel);
        btnbackkk = findViewById(R.id.img_btn);
        selLo = findViewById(R.id.btn_select);

        TextLoc = findViewById(R.id.textloc);

        downloadJSON3(URLS.URL_ALL + "/test/QueryLoc.php");


    }

    private void downloadJSON3(final String urlWebService) {

        class DownloadJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    if (s.equals("[]") || s.equals("NULL")) {

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


//                jsonObj.put("TotalQTY", obj.getString("TotalQTY"));
//                jsonObj.put("LocID", obj.getString("LocID"));
//                jsonObj.put("SampleQTY", obj.getString("SampleQTY"));


                Locname[i] = obj.getString("LocName");
                LocID[i] = obj.getString("LocID");

            }
        }

        selLo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builderSingle = new android.app.AlertDialog.Builder(SearchLoc.this);
                builderSingle.setTitle("Select Location:-");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SearchLoc.this, android.R.layout.select_dialog_singlechoice);
//                    String[] arrOfStr = stocksType[0].split(",", 10);
                for (int i = 0; i < Locname.length; i++) {
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
                        android.app.AlertDialog.Builder builderInner = new android.app.AlertDialog.Builder(SearchLoc.this);
                        builderInner.setMessage(strName);

                        TextLoc.setText(strName);
                        for (int i = 0; i < Locname.length; i++) {
                            if (Locname[i] == null) {

                            } else {
                                if (strName.equals(Locname[i])) {
                                    Loc = LocID[i];
                                }
                            }
                        }

                    }
                });
                builderSingle.show();
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Loc.equals("")) {
                    Toast.makeText(getApplicationContext(), "Plese select Location", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = getIntent();
                    final String data = intent.getStringExtra("data");
                    Intent i = new Intent(SearchLoc.this, Search2.class);
                    i.putExtra("Loc", Loc);
                    i.putExtra("data", data);
                    startActivity(i);
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), test.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
        btnbackkk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), test.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

}


