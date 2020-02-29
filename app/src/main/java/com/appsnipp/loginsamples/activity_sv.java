package com.appsnipp.loginsamples;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class activity_sv extends AppCompatActivity {

    ImageView imgback;
    ListView listView;
    User user = PrefManager.getInstance(this).getUser();
    String I_status = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_staff);
        imgback = findViewById(R.id.img_back);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView = findViewById(R.id.listView);
        TextView TextHead = findViewById(R.id.setText);

        Intent intent = getIntent();
        final String TextName = intent.getStringExtra("TextName");
            TextHead.setText(TextName);

            if(TextName.equals("SUPERVISOR")){
                I_status = "confirm";
            }else {
                I_status = "verify";
            }


        downloadJSON(URLS.URL_ALL+"/test/Querysv.php?Inspect_status=" + I_status);


    }
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Intent newActivity = new Intent(Basket.this, Basket.class);
        recreate();
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
        ArrayList<HashMap<String, Object>> MyArrList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            map = new HashMap<String, Object>();
            stocks[i] = obj.getString("LotNO");
            stocks2[i] = obj.getString("NumOK");
            stocks3[i] = obj.getString("NumNG");
            stocks4[i] = obj.getString("InspectTestID");

            map.put("Lotno",stocks[i]);
            map.put("NumOK",stocks2[i]);
            map.put("NumNG",stocks3[i]);
            MyArrList.add(map);
        }

        SimpleAdapter sAdap;
        sAdap = new SimpleAdapter(activity_sv.this, MyArrList, R.layout.layout_listview_sv,
                new String[] {"Lotno", "NumOK","NumNG"}, new int[] {R.id.Lotno, R.id.textOK,R.id.textNG});
        listView.setAdapter(sAdap);


        Intent intent = getIntent();
        final String TextName = intent.getStringExtra("TextName");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                String ISTID =stocks4[position];
                Intent i = new Intent(activity_sv.this, activity_sv_tool.class);
                i.putExtra("InspectTestID", ISTID);
                i.putExtra("TextName", TextName);
                startActivityForResult(i,0);

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
}
