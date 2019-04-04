package com.naveen_project.hpgas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GasId extends AppCompatActivity {

    TextView  gasId, showCusId,showSupId;
    String  strGasId, strCusId,strSupId;
    int intGasId,intCusId;
    private static final int REQUEST_CAMERA = 1;
    private Class<?>  clss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_id);

        showCusId = (TextView) findViewById(R.id.tvShowCusId);
        showSupId = (TextView) findViewById(R.id.tvShowSupId);

        Bundle extras = getIntent().getExtras();
        strCusId = extras.getString("cusId");
        strSupId = extras.getString("supplierId");
        showCusId.setText(strCusId);
        showSupId.setText(strSupId);

        gasId =(TextView) findViewById(R.id.tvGasId);
        strGasId = extras.getString("gasId");
        gasId.setText(strGasId);

        if(strGasId != null){
            LinearLayout linearLayout;
            linearLayout = (LinearLayout) findViewById(R.id.forVisble);
            linearLayout.setVisibility(View.VISIBLE);
        }

    }
    public void ScanGas(View view) {
        clss = ScanGasId.class;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            //   mClss = clss;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        } else {
            Intent intent = new Intent(this, clss);

            Bundle extras = new Bundle();
            extras.putString("cusId",strCusId);
            extras.putString("supplierId",strSupId);
            intent.putExtras(extras);

            startActivityForResult(intent,1);
        }
    }
    /*
    public void showResult(View view) {




      intCusId = Integer.parseInt(strCusId);
      Intent intent = getIntent();
      strGasId = intent.getExtras().getString("gasId");
      intGasId = Integer.parseInt(strGasId);
      */
/*
        showCusId = (TextView) findViewById(R.id.tvShowCusId);
        Intent intent2 = getIntent();
        Bundle extras2 = intent2.getExtras();
        strCusId = extras2.getString("showCusId");

        showCusId.setText(strCusId);
       }
*/


    public void onSubmitGasID(View view) {
        EditText NewSr, OldSr;
        String strnsr, strosr;

        NewSr = (EditText) findViewById(R.id.etNewSr);
        OldSr = (EditText) findViewById(R.id.etOldSr);

        strnsr = NewSr.getText().toString();
        strosr = OldSr.getText().toString();


        Intent intent = new Intent(GasId.this,Details.class);

        Bundle extras = new Bundle();
        extras.putString("showGasId",strGasId);
        extras.putString("showCusId",strCusId);
        extras.putString("sendSupId-to-Details.class",strSupId);
        extras.putString("NewSr",strnsr);
        extras.putString("OldSr",strosr);
        intent.putExtras(extras);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        gasId = (TextView) findViewById(R.id.tvGasId);

        data = getIntent();
        Bundle extras = data.getExtras();
        strGasId = extras.getString("gasId");
        gasId.setText(strGasId);
/*
        // Create the TextView so I can put the users name on it
        TextView usersNameMessage = (TextView) findViewById(R.id.tvGasId);

        // Get the users name from the previous Activity
        String nameSentBack = data.getStringExtra("gasId");

        // Add the users name to the end of the textView
        usersNameMessage.setText(nameSentBack);
  */  }


}
