package com.appsnipp.loginsamples;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import butterknife.ButterKnife;

public class inputdata extends AppCompatActivity {
    User user = PrefManager.getInstance(this).getUser();
    EditText value ;
    int PERMISSION_ALL = 1;
    boolean flagPermissions = false;

    ArrayList<String> ValINPUT = new ArrayList<String>();
    ArrayList<String> SAMPLETEST = new ArrayList<String>();

    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };
    LinearLayout linearLayout;
    final Context context = this;


    ArrayList<EditText> buttonArray = new ArrayList<EditText>();




    EditText test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        ButterKnife.bind(this);
        checkPermissions();

        Intent intent = getIntent();
        final String testdetailid = intent.getStringExtra("MasterTestDetailID");
        final String ISTID = intent.getStringExtra("InspectTestID");

        int ID = Integer.valueOf(ISTID);
        int MTDID = Integer.valueOf(testdetailid);

        downloadJSON(URLS.URL_ALL+"/test/Queryvalue_inputdata.php?InspectTestID=" + ID + "&MasterTestDetailID=" + MTDID);






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
        final String MIN = intent.getStringExtra("MinimumValue");
        final String MAX = intent.getStringExtra("MaximumValue");
        final String sam = intent.getStringExtra("Sample");
        final String name = intent.getStringExtra("ToolName");
        final String testid = intent.getStringExtra("MasterTestID");
        final String TestNO = intent.getStringExtra("TestNO");
        final String LOT = intent.getStringExtra("LotNO");
        final String part = intent.getStringExtra("PartNO");
        final String ISTID = intent.getStringExtra("InspectTestID");

        int test = Integer.valueOf(sam);


        JSONArray jsonArray = new JSONArray(json);
        final String[] stocks = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            stocks[i] = obj.getString("Value");
            ValINPUT.add(jsonArray.getJSONObject(i).getString("Value"));
            SAMPLETEST.add(jsonArray.getJSONObject(i).getString("SampleNO"));
        }

        final float Min = Float.parseFloat(MIN);
        final float Max = Float.parseFloat(MAX);
        int b = Integer.valueOf(sam);

        linearLayout = findViewById(R.id.linear_layout);
        //RelativeLayout.setOrientation(LinearLayout.HORIZONTAL);
        //Adding 2 TextViews
        ScrollView scrollView = new ScrollView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity=Gravity.CENTER_HORIZONTAL;
        scrollView.setLayoutParams(layoutParams);

        //textview position
        RelativeLayout.LayoutParams textViewLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        textViewLayoutParams.setMargins(50,0,0,0);
        textViewLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);

        //button position
        RelativeLayout.LayoutParams buttonLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        buttonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        buttonLayoutParams.setMargins(0,0,50,0);
        buttonLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);


        //edittext position
        RelativeLayout.LayoutParams edittextLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        edittextLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        edittextLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);




        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(layout);
        //layout.setGravity(Gravity.CENTER_VERTICAL);
        int blackColorValue = Color.parseColor("#000000");
        TextView Text = new TextView(this);
        Text.setTextColor(blackColorValue);
        Text.setText(TestNO+"."+name +"\nMin - Max : "+MIN+" - "+MAX+"\n");
        Text.setTextSize(22);
        Text.setGravity(Gravity.CENTER);
        layout.addView(Text);

        final String[] inputValue = new String[ValINPUT.size()];
        final String[] sampleID = new String[SAMPLETEST.size()];
        int fix_index = 0;
        for (int t = 0; t < b; t++) {
            RelativeLayout row = new RelativeLayout(this);
            row.setLayoutParams(layoutParams);
            final EditText myEditText = new EditText(this);
            buttonArray.add(myEditText);
            TextView textView = new TextView(this);
            textView.setText("SAMPLE " + String.valueOf(t+1)+ ":");
            textView.setTextColor(blackColorValue);
            textView.setLayoutParams(textViewLayoutParams);
            //myEditText.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            myEditText.setHint("VALUE " + String.valueOf(t+1));
            myEditText.setLayoutParams(edittextLayoutParams);
            int id_text = t;
            myEditText.setId(id_text);
            try{
                sampleID[t-fix_index] = SAMPLETEST.get(t-fix_index);
                if(sampleID[t-fix_index].equals(String.valueOf((t+1)))) {
                    inputValue[t-fix_index] = ValINPUT.get(t-fix_index);
                    float valuein = Float.parseFloat(inputValue[t-fix_index]);
                    if(valuein>Max||valuein<Min){
                        myEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                        myEditText.setTextColor(Color.RED);
                        if(inputValue[t-fix_index].equals("-999")){
                            myEditText.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP);
                            myEditText.setTextColor(Color.BLACK);
                            myEditText.setText("");
                        }else {
                            myEditText.setText(inputValue[t-fix_index]);
                        }
                    }
                    else {
                        myEditText.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP);
                        myEditText.setTextColor(Color.BLACK);
                        myEditText.setText(inputValue[t-fix_index]);
                    }
                } else {
                    fix_index++;
                    myEditText.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP);
                    myEditText.setTextColor(Color.BLACK);
                    myEditText.setText("");
                }
            } catch (Exception e) {
                fix_index++;
                myEditText.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP);
                myEditText.setTextColor(Color.BLACK);
                myEditText.setText("");
            }



            LinearLayout row2 = new LinearLayout(this);
            final ImageButton button = new ImageButton(this);

            final int idbutton = t;
            button.setId(idbutton);
            button.setImageResource(R.drawable.ic_camera_alt_black_24dp);
            //button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            myEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            //button.setLayoutParams(buttonLayoutParams);


            final ImageButton button2 = new ImageButton(this);
            button2.setImageResource(R.drawable.ic_pencil_svgrepo_com);
            //button2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            //button2.setLayoutParams(buttonLayoutParams);
            row2.setLayoutParams(buttonLayoutParams);

            final int id2 = t;


            myEditText.setEnabled(false);
            //myEditText.setFocusableInTouchMode(true);


            button2.setId(id2);
            row.addView(textView);
            row.addView(myEditText);
            row2.addView(button);
            row2.addView(button2);

            row.addView(row2);
            layout.addView(row);


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myEditText.requestFocus();
                    myEditText.setEnabled(true);
                    button.requestFocus();
                    value=myEditText;

                    //test = myEditText;
                    Intent i = new Intent(inputdata.this, camera.class);
                    startActivityForResult(i,0);

                }
            });

            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myEditText.setEnabled(true);
                    myEditText.requestFocus();
                    button2.requestFocus();

                    //myEditText.setFocusableInTouchMode(true);


                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 1);

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
                    final String[] values = new String[buttonArray.size()];
                    String showValue = "";
                    int isNotPass = 0;
//                    for (int i = 0; i < buttonArray.size(); i++) {
//                        values[i] = buttonArray.get(i).getText().toString();
//                        if(values[i].equals("")){
//                            buttonArray.get(i).getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
//                            buttonArray.get(i).setTextColor(Color.RED);
//                            int k = Integer.valueOf(i+1);
//                            Toast.makeText(getBaseContext(), "Values "+k+" Empty", Toast.LENGTH_LONG).show();
//                            return;
//                        }else {
//                            buttonArray.get(i).getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP);
//                            buttonArray.get(i).setTextColor(Color.BLACK);
//                        }
//                    }
                    for (int i = 0; i < buttonArray.size(); i++) {
                        values[i] = buttonArray.get(i).getText().toString();
                        showValue += values[i] + ",";
                        try {
                            final float Val = Float.parseFloat(values[i]);

                            if (Val > Max || Val < Min) {
                                isNotPass = 1;
                                buttonArray.get(i).getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                                buttonArray.get(i).setTextColor(Color.RED);
                            } else {
                                buttonArray.get(i).getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP);
                                buttonArray.get(i).setTextColor(Color.BLACK);
                            }
                        } catch (NumberFormatException e) {
                            if (values[i].equals("")) {
                                isNotPass = 1;
                                buttonArray.get(i).getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                                buttonArray.get(i).setTextColor(Color.RED);
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

                        registerUser(values);
                    }
                }
            });

            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });


        }


        setContentView(scrollView);
        }

    String Val2 ="";
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (0) : {
                if (resultCode == Activity.RESULT_OK) {
                    Val2 = data.getStringExtra("test");
                    if(Val2.equals("None")||(Val2.equals("null"))){
                        new android.support.v7.app.AlertDialog.Builder(inputdata.this)
                                .setTitle("No detect picture.")
                                .setMessage("Please take picture again.")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(inputdata.this, camera.class);
                                        startActivityForResult(i,0);
                                    }
                                }).show();
                    }else {
                        value.setText(Val2);
                    }
                }
                break;
            }
        }
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



            Intent i = new Intent(inputdata.this, point_select.class);
            Intent intent = getIntent();
            final String name = intent.getStringExtra("ToolName");
            final String testid = intent.getStringExtra("MasterTestID");
            final String sam = intent.getStringExtra("Sample");
            final String ISTID = intent.getStringExtra("InspectTestID");
            final String LOT = intent.getStringExtra("LotNO");
            final String part = intent.getStringExtra("PartNO");

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
//                    i.putExtra("InspectTestID", ISTID);
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
            final String MIN = intent.getStringExtra("MinimumValue");
            final String MAX = intent.getStringExtra("MaximumValue");
            final String testdetailid = intent.getStringExtra("MasterTestDetailID");
            final String ISTID = intent.getStringExtra("InspectTestID");
            final String TestType = intent.getStringExtra("TestType");

            final float Min = Float.parseFloat(MIN);
            final float Max = Float.parseFloat(MAX);
            int detailid = Integer.valueOf(testdetailid);
            int dataist = Integer.valueOf(ISTID);

            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            JSONObject jsonObj = new JSONObject();
            JSONObject jsonDetails = new JSONObject();
            JSONObject jsonValues = new JSONObject();
            try {
                jsonDetails.put("IDuser",String.valueOf(user.getId()));
                jsonDetails.put("InspectTestID", dataist);
                jsonDetails.put("MasterTestDetailID", detailid);
                jsonDetails.put("TestTypeID", TestType);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < buttonArray.size(); i++) {
                try {
                    final float Val = Float.parseFloat(values[i]);
                    if (Val > Max || Val < Min) {
                        jsonValues.put("value" + i, values[i].toString() + ",N");
                    } else {
                        jsonValues.put("value" + i, values[i].toString() + ",Y");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (NumberFormatException e){
                    try {
                        jsonValues.put("value" + i, "-999".toString() + ",N");
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
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
            return requestHandler.sendPostRequest(URLS.URL_DATA, params);
        }


    }



    void checkPermissions() {
        if (!hasPermissions(this, PERMISSIONS)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(PERMISSIONS,
                        PERMISSION_ALL);
            }
            flagPermissions = false;
        }
        flagPermissions = true;

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}

