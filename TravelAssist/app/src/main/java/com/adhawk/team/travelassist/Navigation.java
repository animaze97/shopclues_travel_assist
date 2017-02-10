package com.adhawk.team.travelassist;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

public class Navigation extends AppCompatActivity implements BeaconConsumer {
    String sample= "";
    Pair<Integer,Integer> userLocation;
    Pair<Integer,Integer> destinationLocation;
    String iTAG = "abc";
    private BeaconManager beaconManager;
    private static String TAG = "testBeacon";
    TextView tv_currentInstruction;
    TextView tv_currentClass;
    ListView lv_previousInstruction;
    //Button bt_stopNavigation;
    ArrayList<String> instructions;
    ArrayAdapter<String> adapter;
    String poiSelected = "Python";
    int cnt = 0;
    MyMap myMap;
    double distanceForPopup = 0.5;
    String nearbyPoi = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);


        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);


        tv_currentInstruction = (TextView) findViewById(R.id.current_instruction);
        tv_currentClass = (TextView) findViewById(R.id.current_class);
        //bt_stopNavigation = (Button) findViewById(R.id.stop_button);
        lv_previousInstruction = (ListView) findViewById(R.id.previous_instructions);
        instructions = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this,R.layout.previous_instructions_layout,instructions);
        myMap = (MyMap) findViewById(R.id.my_map);
        //myMap.setDrawingCacheEnabled(true);

 /*       bt_stopNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(),MainActivity.class));

                //changeInstruction(String.valueOf(cnt++));
                ArrayList<Pair<Integer,Integer>> points = new ArrayList<Pair<Integer, Integer>>();
                Display mdisp = getWindowManager().getDefaultDisplay();
                Point mdispSize = new Point();
                mdisp.getSize(mdispSize);
                int width = mdispSize.x;
                int height = mdispSize.y;
                height -= 1500;
                width -= 135;
                Log.d(iTAG,"height"+String.valueOf(height));
                Log.d(iTAG,"width"+String.valueOf(width));
                int x=12,y=12;
                points.add(new Pair<Integer, Integer>((width/24)*(x)+20,(height/24)*(y)+20));
                //points.add(new Pair<Integer, Integer>((width/24)*x+40,(height/24)*x+40));
                //points.add(new Pair<Integer, Integer>((width/24)*x+40,(height/24)*x+40));


                myMap.invalidate();

            }
        });*/


        lv_previousInstruction.setAdapter(adapter);
        lv_previousInstruction.setBackgroundColor(Color.CYAN);
        tv_currentClass.setText(poiSelected);
    }


    public void changeInstruction(String newInstruction){
        sample = newInstruction;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                instructions.add(0,tv_currentInstruction.getText().toString());
                adapter.notifyDataSetChanged();
                tv_currentInstruction.setText(sample);
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        LocateUser lUser = new LocateUser();
        clearCanvas();
        plotPoints(LocateUser.beacon_pos);

        if(getIntent().getStringExtra("selected_poi")!=null){
            poiSelected = getIntent().getStringExtra("selected_poi");
            if(poiSelected.equalsIgnoreCase("Boarding Counter")){
                ArrayList<Pair<Integer,Integer>> points = new ArrayList<>();
                destinationLocation = new Pair<Integer, Integer>(2,0);
                points.add(destinationLocation);
                plotPoints(points);
            }
            else if(poiSelected.equalsIgnoreCase("Security Check")){
                ArrayList<Pair<Integer,Integer>> points = new ArrayList<>();
                destinationLocation = new Pair<Integer, Integer>(0,4);
                points.add(destinationLocation);
                plotPoints(points);
            }
            else if(poiSelected.equalsIgnoreCase("Check-in Counter")){
                ArrayList<Pair<Integer,Integer>> points = new ArrayList<>();
                destinationLocation = new Pair<Integer, Integer>(3,3);
                points.add(destinationLocation);
                plotPoints(points);
            }
            else if(poiSelected.equalsIgnoreCase("Utility Store")){
                ArrayList<Pair<Integer,Integer>> points = new ArrayList<>();
                destinationLocation = new Pair<Integer, Integer>(6,3);
                points.add(destinationLocation);
                plotPoints(points);
            }
            else if(poiSelected.equalsIgnoreCase("Gift Shop")){
                ArrayList<Pair<Integer,Integer>> points = new ArrayList<>();
                destinationLocation = new Pair<Integer, Integer>(5,2);
                points.add(destinationLocation);
                plotPoints(points);
            }
            else if(poiSelected.equalsIgnoreCase("Delicious Restaurant")){
                ArrayList<Pair<Integer,Integer>> points = new ArrayList<>();
                destinationLocation = new Pair<Integer, Integer>(4,4);
                points.add(destinationLocation);
                plotPoints(points);
            }
            else {
                ArrayList<Pair<Integer,Integer>> points = new ArrayList<>();
                destinationLocation = new Pair<Integer, Integer>(3,3);
                points.add(destinationLocation);
                plotPoints(points);
            }
            tv_currentClass.setText(poiSelected);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        clearCanvas();
    }

    @Override
    public void onBeaconServiceConnect() {
        final Region region = new Region("myBeacons", Identifier.parse("b9407f30-f5f8-466e-aff9-25556b57fe6d"),null,null);

        beaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                try {
                    beaconManager.startRangingBeaconsInRegion(region);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void didExitRegion(Region region) {
                try {
                    beaconManager.stopRangingBeaconsInRegion(region);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {

            }
        });

        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {

                double minDistance = 100;
                Beacon popupBeacon = null;
                for(Beacon beacon : beacons){
                    Log.d(TAG,beacon.getId1()+" "+beacon.getId2()+" "+beacon.getId3()+" distance : "+beacon.getDistance());
                    if(minDistance > beacon.getDistance()){
                        minDistance = beacon.getDistance();
                        popupBeacon = beacon;
                    }
                }
                if(minDistance < distanceForPopup && popupBeacon != null){
                    nearbyPoi = ""/*getNearbyPoi(popupBeacon.getId2())*/;
                    final Dialog dialog = new Dialog(getApplicationContext(), R.style.CustomDialogTheme);
                    dialog.setContentView(R.layout.poi_nearby_popup);
                    Requester.setDisplayHeightWidth(dialog, getApplicationContext(),0.80,0.70);
                    dialog.setCancelable(true);

                    final Button continueNavBtn = (Button) dialog.findViewById(R.id.btn_continue_nav);
                    final Button getDetailsBtn = (Button) dialog.findViewById(R.id.btn_get_details);
                    final TextView poiName = (TextView) dialog.findViewById(R.id.nearby_poi_name);
                    poiName.setText(nearbyPoi);
                    final String poiNameNearby = nearbyPoi;
                    getDetailsBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new makeQuery().execute(MainActivity.URI + "base/getDetail/",poiNameNearby);
                            dialog.dismiss();
                        }
                    });
                    continueNavBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(nearbyPoi.equalsIgnoreCase(poiSelected)){
                                startActivity(new Intent(getApplicationContext(),startScreen.class));
                            }
                            else{
                            }
                            dialog.dismiss();
                        }
                    });
                    dialog.show();


                }
                if(beacons.size() == 3){
                    //
                    //changeInstruction(beacon.getId1()+" "+beacon.getId2()+" "+beacon.getId3()+" distance : "+beacon.getDistance());

                    ArrayList<Pair<Integer,Integer>> points = new ArrayList<>();
                    /*try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                    ArrayList<Double> distances = new ArrayList<Double>();
                    ArrayList<String> major = new ArrayList<String>();
                    for(Beacon beacon : beacons){
                        Log.d(TAG,beacon.getId1()+" "+beacon.getId2()+" "+beacon.getId3()+" distance : "+beacon.getDistance());
                        distances.add(beacon.getDistance());
                        major.add(beacon.getId2().toString());
                    }
                    userLocation = LocateUser.getUserLocation(distances,major);
                    Log.d(TAG,userLocation.getFirst() + "  "+ userLocation.getSecond());
                    if(userLocation.getFirst()!=-1000){
                        points.add(userLocation);
                        plotPoints(points);
                        Pair<Double, ArrayList<String>> directions= new Pair<Double, ArrayList<String>>(0.0,new ArrayList<String>());
                        LocateUser lUser = new LocateUser();
                        directions = lUser.getDestinationInfo(userLocation,destinationLocation);
                        ArrayList<String> printDirections = directions.getSecond();
                        for(String nextDir : printDirections){
                            changeInstruction(nextDir);
                        }
                    }
                }

            }
        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    public void  plotPoints(ArrayList<Pair<Integer,Integer>> temp){

        ArrayList<Pair<Integer,Integer>> points = new ArrayList<Pair<Integer, Integer>>();
        //LocateUser lUser = new LocateUser();
        Display mdisp = getWindowManager().getDefaultDisplay();
        Point mdispSize = new Point();
        mdisp.getSize(mdispSize);
        int width = mdispSize.x;
        int height = mdispSize.y;
        height -= 1500;
        width -= 135;
        int x=12,y=12;
        Log.d(iTAG,"height"+String.valueOf(height));
        Log.d(iTAG,"width"+String.valueOf(width));
        for(Pair<Integer,Integer> pt : temp){
            x = pt.getFirst();
            y = pt.getSecond();
            points.add(new Pair<Integer, Integer>((width/24)*(x)+20,(height/24)*(y)+20));
        }
        myMap.setPoints(points);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myMap.invalidate();
            }
        });
        //myMap.invalidate();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myMap.invalidate();
            }
        });
    }

    public void clearCanvas(){
        MyMap.clearCanvas = true;
        MyMap.points = new ArrayList<>();
        //myMap.invalidate();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myMap.invalidate();
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
                    String type = reader.getString("type");
                    Intent intent = new Intent(getApplicationContext(),DetailedInfo.class);
                    intent.putExtra("Shop_name",reader.getString("name"));
                    intent.putExtra("shop_intro",reader.getString("intro"));
                    intent.putExtra("type",type);
                    if(type.equalsIgnoreCase("shop")){
                        intent.putExtra("discount_usage",reader.getString("usage"));
                        intent.putExtra("rating_contact",reader.getString("contact_details"));
                    }
                    else{
                        intent.putExtra("discount_usage",reader.getString("pids"));
                        intent.putExtra("rating_contact",reader.getString("rating"));
                    }
                    startActivity(intent);
                    return;
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
