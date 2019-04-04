package com.naveen_project.hpgas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Cylinder extends AppCompatActivity {
    TextView supId, clindrId;
    String supplierId, cylinderId;

    private static final int REQUEST_CAMERA = 1;
    private Class<?>  clss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cylinder);

        Bundle extras = getIntent().getExtras();
        supplierId = extras.getString("supplierId");

        supId = (TextView) findViewById(R.id.tvSupID);
        supId.setText(supplierId);

        clindrId = (TextView) findViewById(R.id.tvCylinderId);
        cylinderId = extras.getString("cylinderId");
        clindrId.setText(cylinderId);

        if(cylinderId != null){
            Button btnAdd = (Button) findViewById(R.id.btnAdd);
            btnAdd.setVisibility(View.VISIBLE);
        }
    }

    public void ScanCylinder(View view) {

        clss = Scan_Cylinder.class;

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

            startActivity(intent);
        }
    }

    public void Add(View view) {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
        String date = dateFormat.format(c.getTime());
        String time = timeFormat.format(c.getTime());


        String type = "emp_sr_no";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, supplierId, cylinderId, date, time);
    }

    public void showResult(View view) {
        Intent intent = new Intent(this,EmpCylinder.class);

        Bundle extras = new Bundle();
        extras.putString("supplierId",supplierId);
        intent.putExtras(extras);

        startActivity(intent);

    }


}
