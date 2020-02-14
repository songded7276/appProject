package com.appsnipp.loginsamples;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
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

public class activity_sv_tool extends AppCompatActivity {
    ListView listView, listView2, listView3;
    Button verify, reject;

    ImageView btn_back;
    int state = 0;

    User user = PrefManager.getInstance(this).getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sv);

        listView = findViewById(R.id.listView);
        listView2 = findViewById(R.id.listView2);
        listView3 = findViewById(R.id.listView3);

        verify = findViewById(R.id.btn_verify);
        reject = findViewById(R.id.btn_reject);

        btn_back = findViewById(R.id.img_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        if(user.getMember_info().equals("admin")){
            verify.setText("APPROVE");
        }
        Intent intent = getIntent();
        final String InspectTestID = intent.getStringExtra("InspectTestID");

        downloadJSON(URLS.URL_ALL + "/test/Querysv_tool.php?InspectTestID="+InspectTestID);


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
        String[] stocks2 = new String[jsonArray.length()];
        final String[] stocks3 = new String[jsonArray.length()];
        final String[] stocks4 = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            stocks[i] = obj.getString("TestNO");
            stocks2[i] = obj.getString("TestName");
            stocks3[i] = obj.getString("PercentOK");
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stocks);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stocks2);
        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stocks3);
        listView.setAdapter(arrayAdapter);
        listView2.setAdapter(arrayAdapter2);
        listView3.setAdapter(arrayAdapter3);
        activity_sv_tool.Utility.setListViewHeightBasedOnChildren(listView);
        activity_sv_tool.Utility.setListViewHeightBasedOnChildren(listView2);
        activity_sv_tool.Utility.setListViewHeightBasedOnChildren(listView3);


        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(activity_sv_tool.this);
                if(user.getMember_info().equals("admin")){
                    builder1.setMessage("Do you want to approve?");
                }else {
                    builder1.setMessage("Do you want to verify?");
                }
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

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(activity_sv_tool.this);
                builder1.setMessage("Do you want to reject?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String[] val = new String[0];
                                state = 1;
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

    }
    private void registerUser(String[] values) {
        //String[] values = new String[buttonArray.size()];
        //if it passes all the validations
        //executing the async task
        activity_sv_tool.RegisterUser ru = new activity_sv_tool.RegisterUser(values);
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



            Intent i = new Intent(activity_sv_tool.this, activity_sv.class);


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
                if(state == 0) {
                    if (user.getMember_info().equals("staff")) {
                        jsonDetails.put("Inspect_status", "confirm");
                    } else if (user.getMember_info().equals("SV")) {
                        jsonDetails.put("Inspect_status", "verify");
                    } else {
                        jsonDetails.put("Inspect_status", "approve");
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
