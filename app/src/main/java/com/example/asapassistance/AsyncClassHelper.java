package com.example.asapassistance;

import android.os.AsyncTask;

import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.util.Base64;

public class AsyncClassHelper extends AsyncTask<Void, String, String> {
    @Override
    protected String doInBackground(Void... voids) {
        /*
        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        connection.addRequestProperty("Accept", "application/json");
        connection.addRequestProperty("Content-Type", "application/json");
        String encoded = Base64.getEncoder().encodeToString(("AC777c3e32b672b6782bfe4d798a4e6800" + ":" + "5fce2bddc8744fac940b51d893c7a385").getBytes());  //Java 8
        connection.setRequestProperty("Authorization", "Basic " + encoded);

         */
        return null;
    }
}
