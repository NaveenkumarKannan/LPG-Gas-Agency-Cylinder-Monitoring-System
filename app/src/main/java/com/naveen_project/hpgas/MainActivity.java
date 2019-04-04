package com.naveen_project.hpgas;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
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
import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends FragmentActivity implements RatingDialogListener {
    TextView supId;
    String supplierId;
    SessionManager session;
    String type,rating,comment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;

        if(connected == false)
        {
            startActivity(new Intent(MainActivity.this,Internet_Connection.class));
            finish();
        }
        session = new SessionManager(getApplicationContext());
        //Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();
        if (session.isLoggedIn()==false){
            session.checkLogin();
            finish();
        }
        HashMap<String, String> user = session.getUserDetails();
        supplierId = user.get(SessionManager.KEY_ID);
        //Bundle extras = getIntent().getExtras();
        //supplierId = extras.getString("supplierId");

        supId = (TextView) findViewById(R.id.tvSubId);
        supId.setText(supplierId);
    }

    public void openOptionsMenu(View view) {
        PopupMenu popup = new PopupMenu(getApplicationContext(), view);
        popup.getMenuInflater().inflate(R.menu.logout_menu,
                popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_logout:
                        session.logoutUser();
                        break;

                    default:
                        break;
                }

                return true;
            }
        });
    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        showDialogOK1("Are you sure that you want to exit?",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                finish();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                // proceed with logic by disabling the related features or quit the app.
                                break;
                        }
                    }
                });
        /*
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
        */
    }
    private void showDialogOK1(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setTitle("HPGas")
                .setMessage(message)
                .setPositiveButton("YES", okListener)
                .setNegativeButton("NO", okListener)
                .create()
                .show();
    }
    public void getCylinder(View view) {
        Intent intent = new Intent(this,Cylinder.class);
        Bundle extras = new Bundle();
        extras.putString("supplierId",supplierId);
        intent.putExtras(extras);
        startActivity(intent);

    }

    public void DeliverCylinder(View view) {
        Intent intent = new Intent(this,Scan.class);
        Bundle extras = new Bundle();
        extras.putString("supplierId",supplierId);
        intent.putExtras(extras);
        startActivity(intent);

    }
    String TAG = "RATING";


    public void showDialog(View view) {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNeutralButtonText("Later")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!"))
                .setDefaultRating(5)
                .setTitle("Give us a Feedback")
                .setDescription("Rate us and give your feedback")
                .setCommentInputEnabled(true)
                //.setDefaultComment("This app is pretty cool !")
                .setStarColor(R.color.starColor)
                .setNoteDescriptionTextColor(R.color.noteDescriptionTextColor)
                .setTitleTextColor(R.color.titleTextColor)
                .setDescriptionTextColor(R.color.descriptionTextColor)
                .setHint("Please write your comment here ...")
                .setHintTextColor(R.color.hintTextColor)
                .setCommentTextColor(R.color.commentTextColor)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.MyDialogFadeAnimation)
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .create(MainActivity.this)
                //.setTargetFragment(, TAG) // only if listener is implemented by fragment
                .show();
    }

    @Override
    public void onPositiveButtonClicked(int rate,@NotNull String comment) {
        Log.w(TAG, "Rating : "+rate+"\nComment : "+comment);
        //Toast.makeText(this, "Rating : "+rate+"\nComment : "+comment, Toast.LENGTH_LONG).show();

        rating= String.valueOf(rate);
        this.comment = comment;
        type = "feedback";
        BackgroundWorkerJson backgroundWorker = new BackgroundWorkerJson();
        backgroundWorker.execute();
    }
    @Override
    public void onNegativeButtonClicked() {
        Log.w(TAG, "Positive button clicked");
        //Toast.makeText(this, "Positive button clicked", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNeutralButtonClicked() {
        Log.w(TAG, "Neutral button clicked");
        Toast.makeText(this, "Don't forget to give a feedback...", Toast.LENGTH_LONG).show();
    }

    public class BackgroundWorkerJson extends AsyncTask<String,Void,String> {
        String json_string;
        JSONArray jsonArray;
        JSONObject jsonObject;

        @Override
        protected String doInBackground(String... params) {

            try {
                String post_data = null;
                String webUrl = null;
                if(type.equals("feedback")){
                    webUrl = SessionManager.IP+"HP-Gas/feedback.php";

                    post_data = URLEncoder.encode("rating", "UTF-8") + "=" + URLEncoder.encode(rating, "UTF-8")
                            +"&"+URLEncoder.encode("comment", "UTF-8") + "=" + URLEncoder.encode(comment, "UTF-8")
                    ;
                }

                URL url = new URL(webUrl);
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

            if(type.equals("feedback")){
                Log.w(type,result );
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
            }
        }
    }


}