package com.appsnipp.loginsamples;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import it.sephiroth.android.library.tooltip.Tooltip;

public class Sample_Product extends AppCompatActivity {
    ListView listsample;
    ArrayList<EditText> ArrayInsert = new ArrayList<EditText>();
    ArrayList<String> TestName = new ArrayList<String>();
    ArrayList<String> Min = new ArrayList<String>();
    ArrayList<String> MAX = new ArrayList<String>();
    ArrayList<String> TestType = new ArrayList<String>();
    ArrayList<String> MasterTestDTID = new ArrayList<String>();
    ArrayList<String> ValINPUT = new ArrayList<String>();
    ArrayList<String> SAMPLETEST = new ArrayList<String>();
    ArrayList<String> Textinput = new ArrayList<String>();
    ArrayList<String> MasterTestDTID2 = new ArrayList<String>();
    ArrayList<String> TestNO = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;

    String maxx = "";
    String minn = "";
    String MSTDT = "";
    final Context context = this;
    EditText valueInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample__product);

        Intent intent = getIntent();
        final String Sampletest = intent.getStringExtra("SampleTest");
        final String testid = intent.getStringExtra("MasterTestID");



        int MSTID = Integer.valueOf(testid);
        int SAMPLE = Integer.valueOf(Sampletest);

        downloadJSON("http://158.108.112.125/test/sampleproduct.php?SampleNO=" + SAMPLE +"&MSTID=" + MSTID);


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
        final String[] stocks5 = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            TestName.add(jsonArray.getJSONObject(i).getString("TestName"));
            Min.add(jsonArray.getJSONObject(i).getString("MinimumValue"));
            MAX.add(jsonArray.getJSONObject(i).getString("MaximumValue"));
            TestType.add(jsonArray.getJSONObject(i).getString("TestValueTypeID"));
            MasterTestDTID.add(jsonArray.getJSONObject(i).getString("MasterTestDetailID"));
            ValINPUT.add(jsonArray.getJSONObject(i).getString("Value"));
            SAMPLETEST.add(jsonArray.getJSONObject(i).getString("SampleNO"));
            Textinput.add(jsonArray.getJSONObject(i).getString("TextValue"));
            MasterTestDTID2.add(jsonArray.getJSONObject(i).getString("MasterTestDetailID2"));
            TestNO.add(jsonArray.getJSONObject(i).getString("TestNO"));
        }
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stocks);

        Intent intent = getIntent();
        final String sam = intent.getStringExtra("Sample");
        final String Sampletest = intent.getStringExtra("SampleTest");
        final String testid = intent.getStringExtra("MasterTestID");
        final String ISTID = intent.getStringExtra("InspectTestID");
         int sampleid = Integer.valueOf(Sampletest);

        ScrollView scrollView = new ScrollView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        scrollView.setLayoutParams(layoutParams);


        LinearLayout layout = new LinearLayout(this);
        layout.setLayoutParams(layoutParams);
        layout.setOrientation(LinearLayout.VERTICAL);

        scrollView.addView(layout);


        TextView Text = new TextView(this);
        Text.setText("SAMPLE"+Sampletest);
        Text.setTextSize(22);
        Text.setGravity(Gravity.CENTER);
        layout.addView(Text);

        final String[] valname = new String[TestName.size()];
        final String[] TesttypeID = new String[TestType.size()];
        final String[] MINVALUE = new String[Min.size()];
        final String[] MAXVALUE = new String[MAX.size()];
        final String[] inputValue = new String[ValINPUT.size()];
        final String[] sampleID = new String[SAMPLETEST.size()];
        final String[] Textvalue = new String[Textinput.size()];
        final String[] MTDTID = new String[MasterTestDTID.size()];
        final String[] MTDTID2 = new String[MasterTestDTID2.size()];
        final String[] TestNOID = new String[TestNO.size()];
        final String IDType = "";
        int fix_index = 0;
        for (int t = 0; t < TestName.size(); t++) {
            MINVALUE[t] = Min.get(t);
            MAXVALUE[t] = MAX.get(t);
            valname[t] = TestName.get(t).toString();
            TesttypeID[t] = TestType.get(t).toString();
            TestNOID[t] = TestNO.get(t).toString();
            // TesttypeID[t] = IDType;
            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(layoutParams);
            row.setGravity(Gravity.RIGHT);
            final EditText myEditText = new EditText(this);
            ArrayInsert.add(myEditText);

            final TextView textView = new TextView(this);
            if (valname[t].length() > 9) {
                textView.setText(TestNOID[t] +"."+valname[t].substring(0, 8) + "..." + " (" + MINVALUE[t] + " - " + MAXVALUE[t] + " )");
            } else {
                textView.setText(TestNOID[t] +"."+ valname[t] + " (" + MINVALUE[t] + " - " + MAXVALUE[t] + " )");
            }
            final int finalT1 = t;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Tooltip.make(Sample_Product.this,
                            new Tooltip.Builder(101)
                                    .anchor(textView, Tooltip.Gravity.TOP)
                                    .closePolicy(new Tooltip.ClosePolicy()
                                            .insidePolicy(true, false)
                                            .outsidePolicy(true, false), 4000)
                                    .activateDelay(900)
                                    .showDelay(400)
                                    .text(valname[finalT1])
                                    .maxWidth(600)
                                    .withArrow(true)
                                    .withOverlay(true).build()
                    ).show();
                }
            });

            myEditText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            myEditText.setHint("   VALUE " + String.valueOf(t));
            int id_text = t;
            myEditText.setId(id_text);

            try{
                MTDTID[t] = MasterTestDTID.get(t);
                MTDTID2[t] = MasterTestDTID2.get(t);
                if(MTDTID[t].equals(String.valueOf((MTDTID2[t])))) {
                    inputValue[t] = ValINPUT.get(t);
                    Textvalue[t] = Textinput.get(t);

                    if(inputValue[t].equals("0")){
                        myEditText.setText(Textvalue[t]);
                    }else {
                        myEditText.setText(inputValue[t]);
                    }
                }
                else {
                    fix_index++;
                    myEditText.setText("");
                }
            } catch (Exception e) {
                fix_index++;
                myEditText.setText("");
            }



            //myEditText.setText(val);
            final ImageButton button = new ImageButton(this);
            final int id = t;
            // button.setText("PICTURE");
            button.setImageResource(R.drawable.ic_camera_alt_black_24dp);
            button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            myEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);


            //INPUT page 1.2.6
            final ImageButton button2 = new ImageButton(this);
            button2.setImageResource(R.drawable.ic_pencil_svgrepo_com);
            button2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));



            final int id2 = t;

            myEditText.setEnabled(false);
            //myEditText.setFocusableInTouchMode(true);


            button2.setId(id2);
            row.addView(textView);
            row.addView(myEditText);
            row.addView(button);
            row.addView(button2);
            layout.addView(row);



            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myEditText.requestFocus();
                    myEditText.setEnabled(true);
                    button.requestFocus();
                    valueInput = myEditText;

                    //test = myEditText;
                    Intent i = new Intent(Sample_Product.this, camera.class);
                    startActivityForResult(i, 0);

                }
            });

            final String finalIDType = IDType;
            final int finalT = t;
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    //myEditText.setFocusableInTouchMode(true);
                    if (TesttypeID[finalT].equals("0")) {
                        myEditText.setEnabled(true);
                        myEditText.requestFocus();
                        button2.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 1);
                    } else {

                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(Sample_Product.this);
                        builderSingle.setTitle("Select One Value:-");

                        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Sample_Product.this, android.R.layout.select_dialog_singlechoice);
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
                                AlertDialog.Builder builderInner = new AlertDialog.Builder(Sample_Product.this);
                                builderInner.setMessage(strName);

                                myEditText.setText(strName);
                            }
                        });
                        builderSingle.show();
                    }

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
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    final String[] values = new String[ArrayInsert.size()];
                    final String[] MINVALUE = new String[Min.size()];
                    final String[] MAXVALUE = new String[MAX.size()];
                    String showValue = "";
                    int isNotPass = 0;
                    for (int i = 0; i < ArrayInsert.size(); i++) {
                        values[i] = ArrayInsert.get(i).getText().toString();
                        MINVALUE[i] = Min.get(i);
                        MAXVALUE[i] = MAX.get(i);
                        try {
                            final float Val = Float.parseFloat(values[i]);
                            final float MIN = Float.parseFloat(MINVALUE[i]);
                            final float MAX = Float.parseFloat(MAXVALUE[i]);

                            if (Val > MAX || Val < MIN || values[i].equals("")) {
                                ArrayInsert.get(i).getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                                ArrayInsert.get(i).setTextColor(Color.RED);
                                isNotPass = 1;

                            } else {
                                ArrayInsert.get(i).getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP);
                                ArrayInsert.get(i).setTextColor(Color.BLACK);
                            }
                        } catch (NumberFormatException e) {
                            if (values[i].equals("OK")) {
                                ArrayInsert.get(i).getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP);
                                ArrayInsert.get(i).setTextColor(Color.BLACK);
                            } else {
                                ArrayInsert.get(i).getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                                ArrayInsert.get(i).setTextColor(Color.RED);
                                isNotPass = 1;
                            }
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

                                        final String[] values3 = new String[ArrayInsert.size()];
                                        for (int i = 0; i < ArrayInsert.size(); i++) {
                                            values3[i] = ArrayInsert.get(i).getText().toString();
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
                    Intent i = new Intent(Sample_Product.this, Sample.class);
                    // i.putExtra("ToolName", name);
                    i.putExtra("MasterTestID", testid);
                    i.putExtra("Sample", sam);
                    i.putExtra("InspectTestID", ISTID);
                    // i.putExtra("LotNO",LOT);
                    startActivity(i);
                }
            });


        }


        setContentView(scrollView);
        //setContentView(layout2);

    }

    String Val2 = "";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (0): {
                if (resultCode == Activity.RESULT_OK) {
                    Val2 = data.getStringExtra("test");
                    valueInput.setText(Val2);
                }
                break;
            }
        }
    }

    private void registerUser(String[] values) {
        //String[] values = new String[buttonArray.size()];
        //if it passes all the validations
        //executing the async task
        Sample_Product.RegisterUser ru = new Sample_Product.RegisterUser(values);
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


            Intent i = new Intent(Sample_Product.this, Sample.class);
            Intent intent = getIntent();
            final String testid = intent.getStringExtra("MasterTestID");
            final String ISTID = intent.getStringExtra("InspectTestID");
            final String Sampletest = intent.getStringExtra("SampleTest");
            //hiding the progressbar after completion
//            progressBar.setVisibility(View.GONE);
            try {
                //converting response to json object
                JSONObject obj = new JSONObject(s);
                //if no error in response
                if (!obj.getBoolean("error")) {

                    finish();
                    i.putExtra("MasterTestID", testid);
                    i.putExtra("InspectTestID", ISTID);
                    i.putExtra("SampleTest", Sampletest);
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
//            final float Minn22 = Float.parseFloat(minn);
//            final float Maxx22 = Float.parseFloat(maxx);
            final String[] MSTDTID = new String[MasterTestDTID.size()];

            Intent intent = getIntent();
            final String ISTID = intent.getStringExtra("InspectTestID");
            final String Sampletest = intent.getStringExtra("SampleTest");

            int Sample = Integer.valueOf(Sampletest);
            int IST = Integer.valueOf(ISTID);


            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            JSONObject jsonObj = new JSONObject();
            JSONObject jsonDetails = new JSONObject();
            JSONObject jsonValues = new JSONObject();
            JSONObject jsonDATA = new JSONObject();

            try {
                jsonDATA.put("InspectTestID", IST);
                jsonDATA.put("SampleTest", Sample);
            } catch (Exception e) {
                e.printStackTrace();
            }
            final String[] MINVALUE = new String[Min.size()];
            final String[] MAXVALUE = new String[MAX.size()];
            for (int i = 0; i < ArrayInsert.size(); i++) {
                MINVALUE[i] = Min.get(i);
                MAXVALUE[i] = MAX.get(i);
                try {
                        final float Val = Float.parseFloat(values[i]);
                        MSTDTID[i] = MasterTestDTID.get(i);
                        final float MIN = Float.parseFloat(MINVALUE[i]);
                        final float MAX = Float.parseFloat(MAXVALUE[i]);

                        if (Val > MAX || Val < MIN) {
                            jsonValues.put("value" + i, values[i].toString() + ",N" + "," + MSTDTID[i]);
                        } else {
                            jsonValues.put("value" + i, values[i].toString() + ",Y" + "," + MSTDTID[i]);
                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (NumberFormatException e){
                    try {
                        MSTDTID[i] = MasterTestDTID.get(i);
                        if(values[i].equals("OK")){
                            jsonValues.put("value" + i, values[i].toString() + ",Y" + "," + MSTDTID[i]);
                        }else if (values[i].equals("")){
                            jsonValues.put("value" + i, "999" + ",N" + "," + MSTDTID[i]);
                        }
                        else {
                            jsonValues.put("value" + i, values[i].toString() + ",N" + "," + MSTDTID[i]);
                        }
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }

            }
            try {
                jsonObj.put("Datadetail", jsonDATA);
                jsonObj.put("Values", jsonValues);
                //jsonObj.put("Details", jsonDetails);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            params.put("data", jsonObj.toString());
            //params.put("data", "test");
            //returing the response
            return requestHandler.sendPostRequest(URLS.URL_INSERTDATAVALUETYPE_SAMPLE, params);
        }


    }

}



