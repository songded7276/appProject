package com.appsnipp.loginsamples;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

public class activity_staff extends AppCompatActivity {

    ImageView imgback;
    ListView listView,listView2;
    User user = PrefManager.getInstance(this).getUser();
    final String[] stocks_2 = new String[1];
    final String[] stocks_3 = new String[1];
    final String[] stocks_4 = new String[1];
    final String[] stocks_5 = new String[1];
    final String[] stocks_6 = new String[1];

    Integer[] arrImg ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);
        imgback = findViewById(R.id.img_back);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        downloadJSON(URLS.URL_ALL+"/test/Querystaff.php?Inspect_status=perform" +"&UserID="+user.getId());

        listView = findViewById(R.id.listView);
        //listView2 = findViewById(R.id.listView2);
        TextView TextHead = findViewById(R.id.setText);


        TextHead.setText("STAFF");


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
        final String[] stocks33 = new String[jsonArray.length()];
        final String[] value = new String[jsonArray.length()];
        final String[] listValue = new String[jsonArray.length()];
        ArrayList<HashMap<String, Object>> MyArrList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map;
        arrImg = new Integer[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            map = new HashMap<String, Object>();
            JSONObject obj = jsonArray.getJSONObject(i);
            stocks[i] = obj.getString("InspectTestID");
            stocks2[i] = obj.getString("LotNO");
            stocks3[i] = obj.getString("CountPerform");
            stocks33[i] =  obj.getString("CountTotal");

            Float valueINT = (Float.parseFloat(stocks3[i]) / Float.parseFloat( stocks33[i]))*100 ;

            String format = String.format("%.01f" , valueINT);

            value[i] = String.valueOf(format);
            if(value[i].equals("100.0")){
//                    arrImg[i] = R.drawable.full;
                map.put("Lotno",stocks2[i]);
                map.put("IMG",R.drawable.full);
            }else {
                map.put("Lotno",stocks2[i]);
                map.put("IMG",R.drawable.half);
            }
            MyArrList.add(map);
//
//            stocks_2[0] = obj.getString("SampleQTY");
//            stocks_3[0] = obj.getString("PartNO");
//            stocks_4[0] = obj.getString("MasterTestID");
//            stocks_5[0] = obj.getString("LotNO");
//            stocks_6[0] = obj.getString("LocID");
        }
        SimpleAdapter sAdap;
        sAdap = new SimpleAdapter(activity_staff.this, MyArrList, R.layout.listview_staff,
        new String[] {"Lotno", "IMG"}, new int[] {R.id.Lotno, R.id.imageView});
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stocks2);
        //ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, value);
        listView.setAdapter(sAdap);
        //listView.setAdapter(arrayAdapter);
//        listView2.setAdapter(new ImageAdapter(this));

        Utility.setListViewHeightBasedOnChildren(listView);
        //Utility.setListViewHeightBasedOnChildren(listView2);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                String InspectTestID = stocks[position];
                String Value = value[position];
//                String sam = stocks_2[0];
//                String part = stocks_3[0];
//                String testid = stocks_4[0];
//                String LotNO = stocks_5[0];
//                String LocID = stocks_6[0];
                Intent i = new Intent(activity_staff.this, Staff_layout.class);
                i.putExtra("InspectTestID", InspectTestID);
                i.putExtra("Value", Value);
//                i.putExtra("PartNO", part);
//                i.putExtra("MasterTestID", testid);
//                i.putExtra("LotNO", LotNO);
//                i.putExtra("LocID", LocID);
                startActivityForResult(i,0);
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Intent newActivity = new Intent(Basket.this, Basket.class);
        recreate();
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

