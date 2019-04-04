package com.naveen_project.hpgas;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Details extends AppCompatActivity {
    TextView showCusId,showGasId,showSupId, showNSr, showOSr;
    String strCusId,strGasId,strSupId;
    String strNewSr, strOldSr;
    String json_string;
    JSONArray jsonArray;
    JSONObject jsonObject;
    String cust_name,addr,pinc,phno;
    TextView tx_cust_name,tx_addr,tx_pinc,tx_phno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        Bundle extras = getIntent().getExtras();
        strSupId = extras.getString("sendSupId-to-Details.class");
        strCusId = extras.getString("showCusId");
        strGasId = extras.getString("showGasId");
        strNewSr = extras.getString("NewSr");
        strOldSr = extras.getString("OldSr");

        tx_cust_name = (TextView) findViewById(R.id.tvName);
        tx_addr = (TextView) findViewById(R.id.tvAddr);
        tx_pinc = (TextView) findViewById(R.id.tvPincd);
        tx_phno = (TextView) findViewById(R.id.tvPhoneNo);

        showCusId = (TextView) findViewById(R.id.tvShowCusId);
        showGasId = (TextView) findViewById(R.id.tvShowGasId);
        showSupId = (TextView) findViewById(R.id.tvShowSupId);

        showNSr = (TextView) findViewById(R.id.tvShowNSr);
        showOSr = (TextView) findViewById(R.id.tvShowOsr);

        new BackgroundWorker_JSON2().execute();
    }

    public void onRegister(View view) {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);

        String f = "Are you sure want to submit to the DataBase?";

        final Context context = this;
        builder.setTitle("Submit Status");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Calendar c = Calendar.getInstance();
                System.out.println("Current time => "+c.getTime());
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
                String date = dateFormat.format(c.getTime());
                String time = timeFormat.format(c.getTime());
                String type = "Store In DB";
                BackgroundWorker backgroundWorker = new BackgroundWorker(context);
                backgroundWorker.execute(type, strCusId, strGasId, date, time, strSupId, strNewSr, strOldSr, cust_name);
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                android.support.v7.app.AlertDialog alert1 = builder.create();
                alert1.cancel();
            }
        });
        builder.setMessage(f);
        android.support.v7.app.AlertDialog alert1 = builder.create();
        alert1.show();


    }

    public class BackgroundWorker_JSON2 extends AsyncTask<String,Void,String> {

        String JSON_STRING;
        @Override
        protected String doInBackground(String... params) {

            try {
                String post_data = null;
                String websiteUrl = null;
                websiteUrl = SessionManager.IP+"HP-Gas/json_get_data.php";

                post_data = URLEncoder.encode("cusId", "UTF-8") + "=" + URLEncoder.encode(strCusId, "UTF-8")
                 //       +"&"+URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8")
                   //     +"&"+ URLEncoder.encode("profileImgString", "UTF-8") + "=" + URLEncoder.encode(profileImgString, "UTF-8")
                ;

                URL url = new URL(websiteUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
         /*     Textview to view the json data is also set to comment line format

            TextView textView = (TextView) findViewById(R.id.tvJsonData);
            textView.setText(result);
         */

            json_string = result;

            if(json_string == null){

                Toast.makeText(getApplicationContext(),"First Get JSON data",Toast.LENGTH_LONG).show();

            }
            else {
                try {
                    jsonObject = new JSONObject(json_string);
                    jsonArray = jsonObject.getJSONArray("customer_details");

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jo = jsonArray.getJSONObject(i);

                        String ID = jo.getString("cust_id");
                        if(ID.equals(strCusId))
                        {
                            //   id = jo.getString("id");
                            cust_name = jo.getString("cust_name");
                            addr = jo.getString("addres");
                            pinc = jo.getString("pinc");
                            phno = jo.getString("phone_no");
                            //  password = jo.getString("password");
                        }else {
                            Toast.makeText(Details.this, "Customer id not found in the server", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                showCusId.setText(strCusId);
                showGasId.setText(strGasId);
                showSupId.setText(strSupId);
                showNSr.setText(strNewSr);
                showOSr.setText(strOldSr);

                tx_cust_name.setText(cust_name);
                tx_addr.setText(addr);
                tx_pinc.setText(pinc);
                tx_phno.setText(phno);
            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
