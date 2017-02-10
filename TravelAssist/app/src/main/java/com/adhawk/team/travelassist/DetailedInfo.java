package com.adhawk.team.travelassist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailedInfo extends AppCompatActivity {
    TextView shop_name,shop_intro,discountORusage,RatingORcontact,sub1,sub2;
    String name,intro,type,disORusage,ratingORcontact;
    Button homeBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_info);
        shop_name= (TextView) findViewById(R.id.NameShop);
        shop_intro = (TextView) findViewById(R.id.Intro_detail);
        discountORusage = (TextView) findViewById(R.id.Dis_or_use);
        RatingORcontact = (TextView) findViewById(R.id.rating_contct);
        sub1= (TextView) findViewById(R.id.sheading1);
        sub2 = (TextView) findViewById(R.id.sheading2);
        homeBtn = (Button) findViewById(R.id.btn_home);

        name = getIntent().getStringExtra("Shop_name").toString();
        intro = getIntent().getStringExtra("shop_intro").toString();
        type = getIntent().getStringExtra("type").toString();
        disORusage = getIntent().getStringExtra("discount_usage").toString();
        ratingORcontact = getIntent().getStringExtra("rating_contact").toString();

        if(name!=null){
            shop_name.setText(name);
        }
        if(intro!=null){
            shop_intro.setText(intro);
        }
        if (disORusage!=null){
            discountORusage.setText(disORusage);
        }
        if(ratingORcontact!=null){
            RatingORcontact.setText(ratingORcontact);
        }

        if(type.equalsIgnoreCase("shop")){
            sub1.setText("Discount");
            sub2.setText("Rating");
        }
        else{
            sub1.setText("Uses");
            sub2.setText("Contact");
        }
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),startScreen.class));
            }
        });

    }
}
