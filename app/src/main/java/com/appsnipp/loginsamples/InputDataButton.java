package com.appsnipp.loginsamples;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

public class InputDataButton extends AppCompatActivity {
    User user = PrefManager.getInstance(this).getUser();
    ArrayList<EditText> buttonArray = new ArrayList<EditText>();
    LinearLayout linearLayout;

    String[] arrOfStr1 = new String[10];
    final Context context = this;
    final String[] stocksType = new String[1];
    //final String[] stocks2 = new String[jsonArray.length()];
    final String[] stocks2Type = new String[1];

    ArrayList<String> ValINPUT = new ArrayList<String>();
    ArrayList<String> SAMPLETEST = new ArrayList<String>();

    TextView Text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data_button);

        Intent intent = getIntent();
        final String testdetailid = intent.getStringExtra("MasterTestDetailID");
        final String ISTID = intent.getStringExtra("InspectTestID");

        int ID = Integer.valueOf(ISTID);
        int MTDID = Integer.valueOf(testdetailid);

        //String TN = "Round Test";

        downloadJSON(URLS.URL_ALL+"/test/QuerytestType.php?InspectTestID=" + ID + "&MasterTestDetailID=" + MTDID);


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
        final String sam = intent.getStringExtra("Sample");
        final String name = intent.getStringExtra("ToolName");
        final String testid = intent.getStringExtra("MasterTestID");
        final String TestNO = intent.getStringExtra("TestNO");
        final String Spec = intent.getStringExtra("Spec");
        final String LOT = intent.getStringExtra("LotNO");
        final String ISTID = intent.getStringExtra("InspectTestID");
        final String[] stocks = new String[jsonArray.length()];
        //final String[] stocks2 = new String[jsonArray.length()];
        final String[] stocks2 = new String[jsonArray.length()];
        String showValue = "";

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            ValINPUT.add(jsonArray.getJSONObject(i).getString("TextValue"));
            SAMPLETEST.add(jsonArray.getJSONObject(i).getString("SampleNO"));
        }

        linearLayout = findViewById(R.id.linear_layout);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        //Adding 2 TextViews

        ScrollView scrollView = new ScrollView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity=Gravity.CENTER_HORIZONTAL;
        scrollView.setLayoutParams(layoutParams);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(layout);
        int b = Integer.valueOf(sam);

        Text = new TextView(this);
        Text.setText(TestNO+"."+name+"\n" + Spec);
        Text.setTextSize(22);
        Text.setGravity(Gravity.CENTER);
        layout.addView(Text);

        final String[] inputValue = new String[ValINPUT.size()];
        final String[] sampleID = new String[SAMPLETEST.size()];
        int fix_index = 0;
        for (int t = 0; t < b; t++) {
            LinearLayout row = new LinearLayout(this);
            row.setGravity(Gravity.CENTER);
            final EditText myEditText = new EditText(this);
            buttonArray.add(myEditText);
            TextView textView = new TextView(this);
            textView.setText("SAMPLE " + String.valueOf(t+1) +":");
            myEditText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            myEditText.setHint("VALUE " + String.valueOf(t+1));
            int id_text = t;
            myEditText.setId(id_text);


            try{
                sampleID[t-fix_index] = SAMPLETEST.get(t-fix_index);
                if(sampleID[t-fix_index].equals(String.valueOf((t+1)))) {
                    inputValue[t-fix_index] = ValINPUT.get(t-fix_index);
                    if(inputValue[t-fix_index].equals("NG")){
                        myEditText.setText(inputValue[t-fix_index]);
                        myEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                        myEditText.setTextColor(Color.RED);
                    }else {
                        myEditText.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP);
                        myEditText.setTextColor(Color.BLACK);
                        myEditText.setText(inputValue[t-fix_index]);
                    }
                } else {
                    fix_index++;
                    myEditText.setText("");
                }
            } catch (Exception e) {
                fix_index++;
                myEditText.setText("");
            }

            final ImageButton button = new ImageButton(this);
            button.setImageResource(R.drawable.ic_pencil_svgrepo_com);
            button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            final int id = t;
            button.setId(id);


            myEditText.setEnabled(false);
            //myEditText.setFocusableInTouchMode(true);


            row.addView(textView);
            row.addView(myEditText);
            row.addView(button);
            layout.addView(row);


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myEditText.requestFocus();
                    //myEditText.setEnabled(true);
                    button.requestFocus();

                    //int myId = v.getId();

                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(InputDataButton.this);
                    builderSingle.setTitle("Select One Value:-");

                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(InputDataButton.this, android.R.layout.select_dialog_singlechoice);
//                    String[] arrOfStr = stocksType[0].split(",", 10);
//                    for(int i = 0 ; i < arrOfStr.length ; i++){
//                        arrayAdapter.add(arrOfStr[i]);
//                    }
                    arrayAdapter.add("OK");
                    arrayAdapter.add("NG");

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
                            AlertDialog.Builder builderInner = new AlertDialog.Builder(InputDataButton.this);
                            builderInner.setMessage(strName);

                            myEditText.setText(strName);
                        }
                    });
                    builderSingle.show();
                }
            });

        }
        layout.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i < 1; i++) {
            LinearLayout row2 = new LinearLayout(this);


            Button button = new Button(this);
            button.setText("SAVE");
            final int id = 1;
            button.setId(id);


            Button button2 = new Button(this);
            button2.setText("CANCEL");
            final int id2 = 2;
            button2.setId(id2);

            row2.setGravity(Gravity.CENTER);
            row2.addView(button);
            row2.addView(button2);
            layout.addView(row2);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String[] values = new String[buttonArray.size()];
                    String showValue = "";
                    int isNotPass = 0;
                    for (int i = 0; i < buttonArray.size(); i++) {
                        values[i] = buttonArray.get(i).getText().toString();
                        // showValue += values[i] + ",";
                        // String[] arrOfStr2 = stocks2Type[0].split(",", 10);
                        if (values[i].equals("OK")) {
                            buttonArray.get(i).getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP);
                            buttonArray.get(i).setTextColor(Color.BLACK);
                        } else {
                            isNotPass = 1;
                            buttonArray.get(i).getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                            buttonArray.get(i).setTextColor(Color.RED);
                        }

                    }
                    if (isNotPass == 1) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                context);

                        alertDialogBuilder.setTitle("WARNING...!!!");
                        alertDialogBuilder
                                .setMessage("Confirm...!!!")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        final String[] values3 = new String[buttonArray.size()];
                                        for (int i = 0; i < buttonArray.size(); i++) {
                                            values3[i] = buttonArray.get(i).getText().toString();
                                        }
                                        //Toast.makeText(getBaseContext(), showValue  , Toast.LENGTH_LONG).show();
                                        registerUser(values3);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
//                                    for(int i=0;i<inputTextbox.size();i++){
//                                        inputTextbox.get(i).getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
//                                    }
                                        dialog.cancel();
                                        return;
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    } else {

                        //Toast.makeText(getBaseContext(), showValue  , Toast.LENGTH_LONG).show();
                        registerUser(values);
                    }
                }
            });

            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent i = new Intent(InputDataButton.this, point_select.class);
//                   i.putExtra("ToolName", name);
//                   i.putExtra("MasterTestID", testid);
//                    i.putExtra("InspectTestID", ISTID);
                         finish();
//                         startActivity(i);
                }
            });


        }
        setContentView(scrollView);
    }

    private void registerUser(String[] values) {
        //String[] values = new String[buttonArray.size()];
        //if it passes all the validations
        //executing the async task
        InputDataButton.RegisterUser ru = new InputDataButton.RegisterUser(values);
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

            Intent intent = getIntent();
            final String name = intent.getStringExtra("ToolName");
            final String testid = intent.getStringExtra("MasterTestID");
            final String inspectid = intent.getStringExtra("InspectTestID");
            final String sam = intent.getStringExtra("Sample");
            final String LOT = intent.getStringExtra("LotNO");
            final String part = intent.getStringExtra("PartNO");
            Intent i = new Intent(InputDataButton.this, point_select.class);


            //hiding the progressbar after completion
//            progressBar.setVisibility(View.GONE);
            try {
                //converting response to json object
                JSONObject obj = new JSONObject(s);
                //if no error in response
                if (!obj.getBoolean("error")) {

                    finish();
//                    i.putExtra("ToolName", name);
//                    i.putExtra("MasterTestID",testid);
//                    i.putExtra("Sample", sam);
//                    i.putExtra("InspectTestID", inspectid);
//                    i.putExtra("LotNO",LOT);
//                    i.putExtra("PartNO",part);
//                    startActivity(i);

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

            final String testdetailid = intent.getStringExtra("MasterTestDetailID");
            final String ISTID = intent.getStringExtra("InspectTestID");

            int detailid = Integer.valueOf(testdetailid);
            int dataist = Integer.valueOf(ISTID);

            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            JSONObject jsonObj = new JSONObject();
            JSONObject jsonDetails = new JSONObject();
            JSONObject jsonValues = new JSONObject();
            try {
                jsonDetails.put("IDuser",String.valueOf(user.getId()));
                jsonDetails.put("InspectTestID",dataist );
                jsonDetails.put("MasterTestDetailID", detailid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < buttonArray.size(); i++) {
                try {
                    if(values[i].equals("OK")) {
                        jsonValues.put("value" + i, values[i].toString() + ",Y");
                    }else {

                        jsonValues.put("value" + i, values[i].toString() + ",N");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            try {
                jsonObj.put("Values", jsonValues);
                jsonObj.put("Details", jsonDetails);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            params.put("data", jsonObj.toString());
            //params.put("data", "test");
            //returing the response
            return requestHandler.sendPostRequest(URLS.URL_INSERTDATAVALUETYPE, params);
        }


    }
}
