package com.naveen_project.hpgas;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
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

public class EmpCylinder extends AppCompatActivity {
    String supplierId;

    String json_string;
    JSONArray jsonArray;
    JSONObject jsonObject;

    String gasId, status;

    DataAdapter dataAdapter;
    ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_cylinder);

        listView = (ListView) findViewById(R.id.listview);
        dataAdapter = new DataAdapter(this, R.layout.row_layout);
        listView.setAdapter(dataAdapter);

        Bundle extras = getIntent().getExtras();
        supplierId = extras.getString("supplierId");

        new BackgroundWorker_JSON3().execute();
    }
    public class BackgroundWorker_JSON3 extends AsyncTask<String,Void,String> {

        String JSON_STRING;
        String json_url;
        @Override
        protected String doInBackground(String... params) {

            json_url = SessionManager.IP+"HP-Gas/emp_gas_json_data.php";
            try {

                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("supId","UTF-8")+"="+URLEncoder.encode(supplierId,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while ((JSON_STRING = bufferedReader.readLine()) != null){

                    stringBuilder.append(JSON_STRING);
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

            TextView textView = (TextView) findViewById(R.id.tvEmpGasJsonData);
            textView.setText(result);
         */

            json_string = result;

            if(json_string == null){

                Toast.makeText(getApplicationContext(),"First Get JSON data",Toast.LENGTH_LONG).show();

            }
            else {
                try {
                    jsonObject = new JSONObject(json_string);
                    jsonArray = jsonObject.getJSONArray("gas_details");

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jo = jsonArray.getJSONObject(i);

                        gasId = jo.getString("srno");
                        status = jo.getString("Status");

                        Data data = new Data(gasId, status);
                        dataAdapter.add(data);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
/*
                tx_cust_name = (TextView) findViewById(R.id.tvName);
                tx_addr = (TextView) findViewById(R.id.tvAddr);
                tx_pinc = (TextView) findViewById(R.id.tvPincd);
                tx_phno = (TextView) findViewById(R.id.tvPhoneNo);

                showCusId = (TextView) findViewById(R.id.tvShowCusId);
                showGasId = (TextView) findViewById(R.id.tvShowGasId);
                showSupId = (TextView) findViewById(R.id.tvShowSupId);

                showNSr = (TextView) findViewById(R.id.tvShowNSr);
                showOSr = (TextView) findViewById(R.id.tvShowOsr);

                showCusId.setText(strCusId);
                showGasId.setText(strGasId);
                showSupId.setText(strSupId);
                showNSr.setText(strNewSr);
                showOSr.setText(strOldSr);

                tx_cust_name.setText(cust_name);
                tx_addr.setText(addr);
                tx_pinc.setText(pinc);
                tx_phno.setText(phno);
*/
            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
