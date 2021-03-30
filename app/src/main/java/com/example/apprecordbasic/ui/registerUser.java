package com.example.apprecordbasic.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apprecordbasic.R;
import com.example.apprecordbasic.database.databaseLogin;

public class registerUser extends AppCompatActivity {

    EditText edtemail , edtpass , edtagainpass;
    Button btnsignin;

    com.example.apprecordbasic.database.databaseLogin databaseLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeruser);

        databaseLogin = new databaseLogin(this);
        addView();
        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtemail.getText().toString();
                String pass = edtpass.getText().toString();
                String againpass = edtagainpass.getText().toString();

                if(email.equals("")||pass.equals("")||againpass.equals("")){
                    Toast.makeText(getApplicationContext(),"fields are empty" , Toast.LENGTH_LONG).show();
                }
                else{
                    if(pass.equals(againpass)){
                        Boolean checkEmail = databaseLogin.checkEmail(email);
                        if(checkEmail == true){
                            Boolean insert = databaseLogin.insert(email, pass);
                            if(insert == true){
                                Toast.makeText(getApplicationContext(), "Register Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(registerUser.this, Login.class);
                                startActivity(intent);
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Email already exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Password do not match" , Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }



    private void addView() {

        edtemail = findViewById(R.id.edtemail);
        edtpass = findViewById(R.id.edtpass);
        edtagainpass = findViewById(R.id.edtagainpass);

        btnsignin = findViewById(R.id.btnsignin);

    }
}