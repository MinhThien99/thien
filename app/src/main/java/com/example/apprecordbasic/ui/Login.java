package com.example.apprecordbasic.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apprecordbasic.R;
import com.example.apprecordbasic.database.databaseLogin;
import com.example.apprecordbasic.ui.audio.recordAudio;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Login extends AppCompatActivity {

    EditText edtid , edtpassword;
    Button btnlogin , btnregister;
    com.example.apprecordbasic.database.databaseLogin databaseLogin;
    private CallbackManager callbackManager;
    LoginButton loginButton;
    public static final int CHECK_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();
        databaseLogin = new databaseLogin(this);
        addViews();
        loginButton.setPermissions("email");

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent intent = new Intent(Login.this, recordAudio.class);
                startActivity(intent);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Wrong email or password ", Toast.LENGTH_SHORT).show();
            }
        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, registerUser.class);
                startActivity(intent);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkRequestPermission()) {
                    String id = edtid.getText().toString();
                    String pass = edtpassword.getText().toString();

                    Boolean checkemailpass = databaseLogin.emailPass(id, pass);

                    if (checkemailpass == true) {
                        Toast.makeText(getApplicationContext(), "Successful Login", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, MainLaucher.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Wrong email or password ", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    requestPermission();
                }
            }
        });

    }

    private void addViews() {

        edtid = findViewById(R.id.edtid);
        edtpassword = findViewById(R.id.edtpassword);
        loginButton = findViewById(R.id.loginfacebook);
        btnlogin = findViewById(R.id.btnlogin);
        btnregister = findViewById(R.id.btnregister);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CHECK_PERMISSION:
                if (grantResults.length> 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] ==  PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(),"Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkRequestPermission(){
        int RqRecord = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        int RqWrite = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return RqRecord == PackageManager.PERMISSION_GRANTED && RqWrite == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission(){
        ActivityCompat.requestPermissions(Login.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE} , CHECK_PERMISSION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}