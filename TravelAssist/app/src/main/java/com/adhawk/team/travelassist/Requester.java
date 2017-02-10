package com.adhawk.team.travelassist;

import android.content.Context;

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


}
