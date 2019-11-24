package com.example.asapassistance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class MainActivity extends AppCompatActivity implements AIListener{

    AIService aiService;
    TextView t;

    TextView result;
    static String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t = (TextView) findViewById(R.id.textView);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO);
        result = findViewById(R.id.result);

        if (permission != PackageManager.PERMISSION_GRANTED) {

            makeRequest();
        }
        final AIConfiguration config = new AIConfiguration("da9de0aeb4db401d9d82e748bbe86d72  ",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, config);
        aiService.setListener(this);

        //new AsyncClassGet().execute();


    }
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 3*1000; //Delay for 15 seconds.  One second = 1000 milliseconds.


    @Override
    protected void onResume() {
        //start handler as activity become visible

        handler.postDelayed( runnable = new Runnable() {
            public void run() {
                if (name.equals("")) {
                    //trigger
                }

                handler.postDelayed(runnable, delay);
            }
        }, delay);

        super.onResume();
    }

    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                101);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 101: {

                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {


                } else {

                }
                return;
            }
        }
    }

    public void buttonClicked(View view){
        aiService.startListening();
    }

    @Override
    public void onResult(AIResponse result) {

        Log.d("anu",result.toString());
        Result result1=result.getResult();
        t.setText("Query "+result1.getResolvedQuery()+" action: "+result1.getAction());


    }

    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }

    public void onClicksendSMS(View view) throws IOException {
        Log.i("ASAPAssistance", "Button pressed");

        new AsyncClassPost().execute();

    }
    public void onClickCall(View view) throws IOException {
        Log.i("ASAPAssistance", "called");

        new AsyncClassCall().execute();
    }

    public void dataExtract() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        (FirebaseDatabase.getInstance().getReference()).child("UserName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot == null || snapshot.getValue() == null) {
                    return;
                }
                int i = snapshot.getValue().toString().length()-1;
                String x = snapshot.getValue().toString().substring(i);

                while (!x.equals("=")) {
                    Log.i("MainActivity",x);
                    name = x + name;
                    i -= 1;
                    x = snapshot.getValue().toString().substring(i,i+1);

                }
                result.setText(name.substring(0,name.length()-2));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private class AsyncClassGet extends AsyncTask<Void, Void, String> {
        String ret;
        String URLstring = "https://api.zang.io/v2/Accounts/AC777c3e32b672b6782bfe4d798a4e6800/SMS/Messages.json";


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


            Log.i("MainActivity","Message recieved");
        }
    }

    private class AsyncClassPost extends AsyncTask<Void, Void, String> {
        String URLstring = "https://api.zang.io/v2/Accounts/AC777c3e32b672b6782bfe4d798a4e6800/SMS/Messages.json";

        String ret;
        int res;
        String urlParameters  = "To=+16477068738&From=+19412004022&Body=jkjk! from UWO :) (dont reply to this) ";
        byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
        int    postDataLength = postData.length;
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
                connection.setRequestProperty( "Content-Length", Integer.toString(postDataLength));
                String encoded = Base64.getEncoder().encodeToString((x).getBytes());  //Java 8
                connection.setRequestProperty("Authorization", "Basic " + encoded);
                connection.setUseCaches( false );
                try( DataOutputStream wr = new DataOutputStream( connection.getOutputStream())) {
                    wr.write( postData );
                }
                res = connection.getResponseCode();


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
            dataExtract();

            //result.setText(Integer.toString(res));
            Log.i("MainActivity","Message sent!");
        }
    }
    private class AsyncClassCall extends AsyncTask<Void, Void, String> {

        String URLstring = "https://api.zang.io/v2/Accounts/AC777c3e32b672b6782bfe4d798a4e6800/Calls.json";
        String ret;
        int res;
        String urlParameters  = "From=+19412004022&To=+16477068738&Url=http://zang.io/ivr/welcome/call";
        byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
        int    postDataLength = postData.length;

        public void dataExtract() {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String UID = user.getUid();
            (FirebaseDatabase.getInstance().getReference()).child("users").child(UID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot == null || snapshot.getValue() == null) {
                        return;
                    }
                    String name = snapshot.getValue().toString();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
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
                connection.setRequestProperty( "Content-Length", Integer.toString(postDataLength));
                String encoded = Base64.getEncoder().encodeToString((x).getBytes());  //Java 8
                connection.setRequestProperty("Authorization", "Basic " + encoded);
                connection.setUseCaches( false );
                try( DataOutputStream wr = new DataOutputStream( connection.getOutputStream())) {
                    wr.write( postData );
                }
                res = connection.getResponseCode();


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

            //result.setText(Integer.toString(res));
            Log.i("MainActivity","Called!");
        }
    }
}
