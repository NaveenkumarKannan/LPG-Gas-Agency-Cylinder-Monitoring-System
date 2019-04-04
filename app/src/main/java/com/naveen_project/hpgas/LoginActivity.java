package com.naveen_project.hpgas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText SupplierId, PasswordEt;
    String supplierId, password, type;

    // Session Manager Class
    //SessionManager session;

    private static CheckBox show_hide_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Session Manager
        //session = new SessionManager(getApplicationContext());
        //Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        SupplierId = findViewById(R.id.etUserId);
        PasswordEt = findViewById(R.id.etPwd);
        show_hide_password = (CheckBox) findViewById(R.id.show_hide_password);
        show_hide_password
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton button,
                                                 boolean isChecked) {

                        // If it is checkec then show password else hide
                        // password
                        if (isChecked) {

                            show_hide_password.setText(R.string.hide_pwd);// change
                            // checkbox
                            // text

                            PasswordEt.setInputType(InputType.TYPE_CLASS_TEXT);
                            PasswordEt.setTransformationMethod(HideReturnsTransformationMethod
                                    .getInstance());// show password
                        } else {
                            show_hide_password.setText(R.string.show_pwd);// change
                            // checkbox
                            // text

                            PasswordEt.setInputType(InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            PasswordEt.setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());// hide password

                        }

                    }
                });


    }
    public void OnLogin(View view) {
        type = "login";
        //    Intent intent = new Intent(this,Scan.class);
        //    intent.putExtra("supid",username);


        supplierId = SupplierId.getText().toString();
        password = PasswordEt.getText().toString();

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, supplierId, password);

    }
}