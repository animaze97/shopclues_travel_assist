package com.adhawk.team.travelassist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BasicInfo extends AppCompatActivity {
    TextView tv_name,tv_intro;
    Button nav_btn;
    String name,intro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_info);
        tv_name = (TextView) findViewById(R.id.basic_info_name);
        tv_intro = (TextView) findViewById(R.id.basic_info_intro);
        name = getIntent().getStringExtra("name").toString();
        intro = getIntent().getStringExtra("basic_info").toString();
        if(name!=null){
            tv_name.setText(name);
        }
        if(intro!=null){
            tv_intro.setText(intro);
        }
        nav_btn = (Button) findViewById(R.id.basic_info_navBtn);
        nav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Navigation.class);
                intent.putExtra("selected_poi",name);
                startActivity(intent);
            }
        });

    }
}
