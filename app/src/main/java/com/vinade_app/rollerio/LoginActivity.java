package com.vinade_app.rollerio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    Button signUp, login;
    EditText email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Init();

        Bundle intent = getIntent().getExtras();
        if(intent != null) {
            String mail = intent.getString("email");
            String pass = intent.getString("password");
            initFields(mail,pass);
        }
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    void Init()
    {
        email = findViewById(R.id.fieldEmail);
        password = findViewById(R.id.fieldPassword);
        signUp = findViewById(R.id.btn_signup);
        login = findViewById(R.id.btn_login);
    }

    private void initFields(String mail, String pass)
    {


        if(!TextUtils.isEmpty(mail) && !TextUtils.isEmpty(pass))
        {
            email.setText(mail);
            password.setText(pass);
        }
    }
}
