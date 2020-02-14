package com.appsnipp.loginsamples;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import static com.appsnipp.loginsamples.R.id.img_back;

public class ProfileActivity extends AppCompatActivity {

    TextView textViewId, textViewUsername, textViewName, textViewlevel;

    ImageView btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        init();


    }
    void init(){
        Button back = findViewById(R.id.btn_back);
        textViewId = findViewById(R.id.textViewId);
        textViewUsername = findViewById(R.id.textViewUsername);
        textViewName = findViewById(R.id.textViewName);
        textViewlevel = findViewById(R.id.textViewlevel);
        btn = (ImageView)findViewById(img_back);


        //getting the current user
        User user = PrefManager.getInstance(this).getUser();

        //setting the values to the textviews
        textViewId.setText(String.valueOf(user.getId()));
        textViewUsername.setText(user.getUsername());
        textViewName.setText(user.getEmail());
        textViewlevel.setText(user.getGroupID());


        //when the user presses logout button calling the logout method
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


}
