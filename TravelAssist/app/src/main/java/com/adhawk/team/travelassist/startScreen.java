package com.adhawk.team.travelassist;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

public class startScreen extends AppCompatActivity {
    ListView poiLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        String[] shopsAndCounters = { "Delicious Restaurant", "Gift Shop", "Boarding Counter", "Check-in Counter", "Utility Store", "Security Check"};
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.poi_layout, shopsAndCounters);
        poiLv = (ListView) findViewById(R.id.poiListView);
        poiLv.setAdapter(adapter);

        poiLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new makeQuery().execute(MainActivity.URI + "base/getBasic/",poiLv.getItemAtPosition(position).toString());
            }
        });

    }



    public class makeQuery extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("name", params[1]);
                //jsonParam.put("body", params[2]);
                return Requester.make_request(urlConnection, jsonParam.toString(), getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s == null) {
                Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                JSONObject reader = new JSONObject(s);
                if (reader.getInt("status") != 200) {
                    Toast.makeText(getApplicationContext(), reader.getString("message"), Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    Toast.makeText(getApplicationContext(), reader.getString("message"), Toast.LENGTH_LONG).show();
                    String name = reader.getString("name");
                    String basicInfo = reader.getString("intro");
                    Intent intent = new Intent(getApplicationContext(),BasicInfo.class);
                    intent.putExtra("name",name);
                    intent.putExtra("basic_info",basicInfo);
                    startActivity(intent);
                    //return;
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {

        }
    }


}
