package com.adhawk.team.travelassist;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Created by del on 2/10/2017.
 */
public class Requester {

    public static String make_request(HttpURLConnection urlConnection, String params, Context context){

        try {
            //add request header
            urlConnection.setRequestMethod("POST");
            urlConnection.setConnectTimeout(10000);

            // Send post request
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.writeBytes(params);
            wr.flush();
            wr.close();

            InputStream in = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            buffer.append(reader.readLine());

            return buffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void setDisplayHeightWidth(final Dialog getInLineDialog, Context context, double _width, double _height){
        //code below to set custom height and width to the dialog
        //get height and width
        WindowManager manager = (WindowManager) context.getSystemService(Activity.WINDOW_SERVICE);
        int width, height;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
            width = manager.getDefaultDisplay().getWidth();
            height = manager.getDefaultDisplay().getHeight();
        } else {
            Point point = new Point();
            manager.getDefaultDisplay().getSize(point);
            width = point.x;
            height = point.y;
        }
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getInLineDialog.getWindow().getAttributes());
        lp.width = (int)(width*_width);
        lp.height = (int)(height*_height);
        getInLineDialog.getWindow().setAttributes(lp);

    }

}
