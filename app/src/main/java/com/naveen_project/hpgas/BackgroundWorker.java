package com.naveen_project.hpgas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

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

/**
 * Created by SANKAR on 3/12/2018.
 */
public class BackgroundWorker extends AsyncTask<String,Void,String> {

    AlertDialog alertDialog;
    String type,supplierId;
    String password;

    Context context;
    BackgroundWorker (Context ctx) {
        context = ctx;
    }
    @Override
    protected String doInBackground(String... params) {
        type = params[0];
        String login_url = SessionManager.IP+"HP-Gas/login.php";
        String register_url = SessionManager.IP+"HP-Gas/register.php";
        String sr_url = SessionManager.IP+"HP-Gas/emp_sr_no.php";

        if(type.equals("login")) {
            try {
                supplierId = params[1];
                password = params[2];



                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("uid","UTF-8")+"="+URLEncoder.encode(supplierId,"UTF-8")+"&"
                        +URLEncoder.encode("passwd","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();


                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else if(type.equals("Store In DB")) {
            try {
                String cusId = params[1];
                String gasId = params[2];
                String date = params[3];
                String time = params[4];
                supplierId = params[5];
                String NewSr = params[6];
                String OldSr = params[7];
                String Cust_Name = params[8];

                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("cusId", "UTF-8") + "=" + URLEncoder.encode(cusId, "UTF-8") + "&"
                        + URLEncoder.encode("gasId", "UTF-8") + "=" + URLEncoder.encode(gasId, "UTF-8") + "&"
                        + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8") + "&"
                        + URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(time, "UTF-8") + "&"
                        + URLEncoder.encode("supId", "UTF-8") + "=" + URLEncoder.encode(supplierId, "UTF-8")+"&"
                        + URLEncoder.encode("new_sr_no", "UTF-8") + "=" + URLEncoder.encode(NewSr, "UTF-8")+"&"
                        + URLEncoder.encode("old_sr_no_cusName", "UTF-8") + "=" + URLEncoder.encode(OldSr, "UTF-8")+"&"
                        + URLEncoder.encode("Cust_Name", "UTF-8") + "=" + URLEncoder.encode(Cust_Name, "UTF-8") ;
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (type.equals("emp_sr_no")){
            Log.w(type,type );
            try {
                String srno, date, time;
                supplierId = params[1];
                srno = params[2];
                date = params[3];
                time = params[4];

                URL url = new URL(sr_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("srno", "UTF-8") + "=" + URLEncoder.encode(srno, "UTF-8") + "&"
                        + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8") + "&"
                        + URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(time, "UTF-8") + "&"
                        + URLEncoder.encode("supId", "UTF-8") + "=" + URLEncoder.encode(supplierId, "UTF-8") ;
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(String result) {

        String f = result;



        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        if(type == "login"){

            builder.setTitle("Login Status");

            if(supplierId.trim().length() > 0 && password.trim().length() > 0){

                if(result.charAt(0)=='L')
                {
                    f ="Login Success! Welcome!!!";

                    SessionManager session;
                    session = new SessionManager(context);
                    session.createLoginSession(supplierId);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(context,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            Bundle extras = new Bundle();
                            extras.putString("supplierId",supplierId);
                            intent.putExtras(extras);
                            context.startActivity(intent);
                        }
                    });
                }
                else {
                    f = "User Id or Password is Incorrect";
                    builder.setNeutralButton("Try Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            android.support.v7.app.AlertDialog alert1 = builder.create();
                            alert1.cancel();
                        }
                    });
                }

            }
            else{
                // user didn't entered username or password
                // Show alert asking him to enter the details
                f="Please enter username and password";
                builder.setNeutralButton("Try Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        android.support.v7.app.AlertDialog alert1 = builder.create();
                        alert1.cancel();
                    }
                });
            }

        }
        else if (type == "Store In DB"){
            f = result;
            builder.setTitle("Submit Status");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(context,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    Bundle extras = new Bundle();
                    extras.putString("supplierId",supplierId);
                    intent.putExtras(extras);
                    context.startActivity(intent);

                }
            });

        }
        else if (type.equals("emp_sr_no")){
            Log.w(type,result);
            f = result;

            builder.setTitle("Submit Status");

            builder.setPositiveButton("Add More", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(context,Cylinder.class);
                    Bundle extras = new Bundle();
                    extras.putString("supplierId",supplierId);
                    intent.putExtras(extras);
                    context.startActivity(intent);

                }
            });
            builder.setNeutralButton("View Details", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(context,EmpCylinder.class);
                    Bundle extras = new Bundle();
                    extras.putString("supplierId",supplierId);
                    intent.putExtras(extras);
                    context.startActivity(intent);
                }
            });
        }
        builder.setMessage(f);
        android.support.v7.app.AlertDialog alert1 = builder.create();
        alert1.show();
        // To close the AlertDialog
        // alert1.cancel();

    }
}

