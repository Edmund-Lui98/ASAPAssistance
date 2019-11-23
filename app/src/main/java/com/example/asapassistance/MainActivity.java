package com.example.asapassistance;

import androidx.appcompat.app.AppCompatActivity;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MainActivity extends AppCompatActivity{
    static String URLstring = "https://api.zang.io/v2/Accounts/AC777c3e32b672b6782bfe4d798a4e6800/SMS/Messages.json";

    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = findViewById(R.id.result);

        new AsyncClassGet().execute();
    }

    public void onCLicksendSMS(View view) throws IOException {
        Log.i("ASAPAssistance", "Button pressed");

        new AsyncClassPost().execute();

    }
    /*
    public static String sendSMS(URL url, HttpURLConnection connection) throws IOException {
        String ret;

        Log.i("ASAPAssistance", "Send sms function");

        try {
            InputStream in = connection.getInputStream();
            Log.i("ASAPAssistance", "after input stream");

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                ret =  scanner.next();
            } else {
                ret =  null;
            }
            return ret;
        } catch (Exception e) {
            .setText(e.toString());
        } finally {
            connection.disconnect();
        }
        ret =  "hello";
        return ret;
    }

     */

    private class AsyncClassGet extends AsyncTask<Void, Void, String> {
        String ret;

        @Override
        protected String doInBackground(Void... voids) {
            String x = "AC777c3e32b672b6782bfe4d798a4e6800" + ":" + "5fce2bddc8744fac940b51d893c7a385";
            try {
                URL url = new URL(URLstring);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.addRequestProperty("Accept", "application/json");
                connection.addRequestProperty("Content-Type", "application/json");
                String encoded = Base64.getEncoder().encodeToString((x).getBytes());  //Java 8
                connection.setRequestProperty("Authorization", "Basic " + encoded);

                Log.i("ASAPAssistance", "get sms function");

                try {
                    InputStream in = connection.getInputStream();
                    Log.i("ASAPAssistance", "after input stream");

                    Scanner scanner = new Scanner(in);
                    scanner.useDelimiter("\\A");

                    boolean hasInput = scanner.hasNext();
                    if (hasInput) {
                        ret =  scanner.next();
                    } else {
                        ret = null;
                    }
                    return ret;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    connection.disconnect();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            result.setText(ret);
            Log.i("MainActivity","Is it finished???");
        }
    }

    private class AsyncClassPost extends AsyncTask<Void, Void, String> {
        String ret;

        @Override
        protected String doInBackground(Void... voids) {
            String x = "AC777c3e32b672b6782bfe4d798a4e6800" + ":" + "5fce2bddc8744fac940b51d893c7a385";
            try {
                URL url = new URL(URLstring);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setInstanceFollowRedirects( false );
                connection.setRequestMethod("POST");
                connection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty( "charset", "utf-8");
                connection.setRequestProperty( "Content-Length", "To=6477068738&From=9412004022&Body=This is an SMS sent from Edmund");
                connection.setUseCaches( false );

                Log.i("ASAPAssistance", "Send sms function");

                connection.disconnect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
