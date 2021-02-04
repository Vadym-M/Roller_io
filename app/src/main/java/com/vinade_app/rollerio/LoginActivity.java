package com.vinade_app.rollerio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    Button signUp, login;
    EditText email, password;
    private FirebaseAuth mAuth;
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
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogInMethod(email.getText().toString(), password.getText().toString());
            }
        });
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
        mAuth = mAuth.getInstance();
    }

    private void initFields(String mail, String pass)
    {


        if(!TextUtils.isEmpty(mail) && !TextUtils.isEmpty(pass))
        {
            email.setText(mail);
            password.setText(pass);
        }
    }
    private void LogInMethod(String mail , String pass)
    {
        mAuth.signInWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                           Intent intent = new Intent(LoginActivity.this, NavigationBarActivity.class);
                           startActivity(intent);
                           finish();

                        } else {
                            Toast.makeText(LoginActivity.this,"Error",Toast.LENGTH_SHORT).show();

                            // ...
                        }

                        // ...
                    }
                });
    }
}
