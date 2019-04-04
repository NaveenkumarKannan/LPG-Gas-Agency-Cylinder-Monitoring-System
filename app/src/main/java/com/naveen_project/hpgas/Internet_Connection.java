package com.naveen_project.hpgas;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Internet_Connection extends AppCompatActivity {

    Button btnTryAgain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet__connection);

        btnTryAgain = (Button) findViewById(R.id.btnTryAgain);
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Internet_Connection.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
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
}
