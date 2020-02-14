package com.appsnipp.loginsamples;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class select_tool extends AppCompatActivity {
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tool);

        text = findViewById(R.id.tool_name);
        Button point = findViewById(R.id.point);
        Button sample = findViewById(R.id.sample);
        Intent intent = getIntent();
        final String name = intent.getStringExtra("ToolName");
        final String sam = intent.getStringExtra("Sample");
        final String testid = intent.getStringExtra("MasterTestID");
        final String ISTID = intent.getStringExtra("InspectTestID");
        final String LOT = intent.getStringExtra("LotNO");
        final String part = intent.getStringExtra("PartNO");

        text.setText(name+"\nLOT : "+ LOT);

        point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(select_tool.this, point_select.class);
                i.putExtra("ToolName", name);
                i.putExtra("Sample",sam);
                i.putExtra("MasterTestID",testid);
                i.putExtra("InspectTestID",ISTID);
                i.putExtra("LotNO",LOT);
                startActivity(i);
            }
        });

        sample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(select_tool.this, Sample.class);
                i.putExtra("Sample",sam);
                i.putExtra("MasterTestID",testid);
                i.putExtra("InspectTestID",ISTID);
                i.putExtra("LotNO",LOT);
                i.putExtra("PartNO",part);
                startActivity(i);
            }
        });
    }
}
