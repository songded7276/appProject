package com.appsnipp.loginsamples;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class test extends AppCompatActivity {
    TextView textuser, textstatus;
    String myweb = URLS.URL_ALL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        init();
    }

    void init() {
        Button profile = findViewById(R.id.btn_profile);
        Button qrcode = findViewById(R.id.btn_qrcode);
        Button search = findViewById(R.id.btn_search);
        Button web = findViewById(R.id.btn_web);
        final Button staff = findViewById(R.id.btn_staff);
        Button verify = findViewById(R.id.btn_verify);
        Button approve = findViewById(R.id.btn_approve);

        textstatus = findViewById(R.id.text_status);
        textuser = findViewById(R.id.text_user);

        final User user = PrefManager.getInstance(this).getUser();
        textuser.setText("Username : " + user.getUsername());
        textstatus.setText("GroupID : " + user.getGroupID());


        PrefManager prefManager = PrefManager.getInstance(test.this);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(test.this, ProfileActivity.class));
            }
        });
        qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(test.this, MainActivity.class));
            }
        });
        /*------------------------------------------------------------*/
        findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrefManager.getInstance(getApplicationContext()).logout();
                startActivity(new Intent(test.this, LoginActivity.class));
            }
        });
        /*------------------------------------------------------------------*/

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(test.this, Search.class));
            }
        });
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebURL(myweb);
            }
        });

        if (user.getPrepare().equals(0)) {
            staff.setEnabled(false);
        } else {
            staff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(test.this, activity_staff.class));
                }
            });
        }
        if (user.getVerify().equals(0)) {
            verify.setEnabled(false);
        } else {
            verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(test.this, activity_sv.class);
                    i.putExtra("TextName", "SUPERVISOR");
                    startActivity(i);
                }
            });
        }
        if (user.getApprove().equals(0)) {
            approve.setEnabled(false);
        } else {
            approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(test.this, activity_sv.class);
                    i.putExtra("TextName", "ADMIN");
                    startActivity(i);
                }
            });
        }


    }

    public void openWebURL(String inURL) {
        Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(inURL));

        startActivity(browse);
    }

}
