package com.naveen_project.hpgas;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
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

public class Scan extends AppCompatActivity {

    TextView cusId, gasId,supId;
    String supplierId;
    String strCusId, strGasId;
    String json_string;
    JSONArray jsonArray;
    JSONObject jsonObject;
    //int intCusId;
    String cust_name,addr,pinc,phno;
    TextView tx_cust_name,tx_addr,tx_pinc,tx_phno;
    TableLayout tableLayout;
    LinearLayout linearr;
    Button btnShowResult, btnNext;


    private static final int REQUEST_CAMERA = 1;
    private Class<?>  clss;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        Bundle cusExtras = getIntent().getExtras();
        supplierId= cusExtras.getString("supplierId");
        supId = (TextView) findViewById(R.id.supId);
        supId.setText(supplierId);

        cusId = (TextView) findViewById(R.id.tvCusId);
        btnShowResult = (Button) findViewById(R.id.btnShowResul);
        cusId.setText(strCusId);

        if (strCusId != null) {
            btnShowResult.setVisibility(View.VISIBLE);
        }
    }
    public void ScanCustomer(View view) {

        clss = ScanCusId.class;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
          //  mClss = clss;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},REQUEST_CAMERA);
        } else {
            Intent intent = new Intent(this, clss);

            Bundle extras = new Bundle();
            extras.putString("supplierId",supplierId);
            intent.putExtras(extras);

            startActivityForResult(intent,1);
        }

    }


    public void showResult(View view) {
        btnNext = (Button) findViewById(R.id.btnSubmit);
        btnNext.setVisibility(View.VISIBLE);

        //intCusId = Integer.parseInt(strCusId);
        new BackgroundWorker_JSON().execute();


    }
    public void onSubmit(View view){
        Intent intent = new Intent(Scan.this,GasId.class);

        Bundle extras = new Bundle();
        extras.putString("cusId",strCusId);
        extras.putString("supplierId",supplierId);
        intent.putExtras(extras);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                strCusId=data.getStringExtra("cusId");
                if (strCusId != null) {
                    cusId.setText(strCusId);
                    btnShowResult.setVisibility(View.VISIBLE);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    public class BackgroundWorker_JSON extends AsyncTask<String,Void,String> {

        String JSON_STRING;
        String json_url;
        @Override
        protected String doInBackground(String... params) {

            json_url = SessionManager.IP+"HP-Gas/json_get_data.php";
            try {

                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("cusId","UTF-8")+"="+URLEncoder.encode(strCusId,"UTF-8");
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

                        //int ID = jo.getInt("cust_id");
                        //String ID = jo.getString("cust_id");
                        //if(ID == strCusId)
                        //{
                         //   id = jo.getString("id");
                            cust_name = jo.getString("cust_name");
                            addr = jo.getString("addres");
                            pinc = jo.getString("pinc");
                            phno = jo.getString("phone_no");
                          //  password = jo.getString("password");
                        //}
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
             //   tx_id = (TextView) findViewById(R.id.tx_id);
                tx_cust_name = (TextView) findViewById(R.id.tvName);
                tx_addr = (TextView) findViewById(R.id.tvAddr);
                tx_pinc = (TextView) findViewById(R.id.tvPincd);
                tx_phno = (TextView) findViewById(R.id.tvPhoneNo);
             //   tx_password = (TextView) findViewById(R.id.tx_password);

             //   tx_id.setText(id);
                //tableLayout = findViewById(R.id.table);
                //tableLayout.setVisibility(View.VISIBLE);
                linearr = findViewById(R.id.linear);
                linearr.setVisibility(View.VISIBLE);
                tx_cust_name.setText(cust_name);
                tx_addr.setText(addr);
                tx_pinc.setText(pinc);
                tx_phno.setText(phno);
             //   tx_password.setText(password);
            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }



}
