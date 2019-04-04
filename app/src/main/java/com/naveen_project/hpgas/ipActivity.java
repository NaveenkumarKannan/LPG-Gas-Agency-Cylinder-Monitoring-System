package com.naveen_project.hpgas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;

public class ipActivity extends AppCompatActivity {
    SessionManager session;
    EditText etIp;
    String ip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip);

        etIp = findViewById(R.id.etIp);
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getIp();
        SessionManager.IP = user.get(SessionManager.KEY_IP);
        //Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        if(SessionManager.IP !=null){
            etIp.setText(SessionManager.IP);
        }
    }

    public void OnNext(View view) {
        ip = etIp.getText().toString();
        if(ip.trim().length()>0){
            session.createIpSession(ip);
            SessionManager.IP = ip;
            Intent intent = new Intent(this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else{
            etIp.setError("IP / Web Url is must");
        }
    }
}
