package com.vinade_app.rollerio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class RegistrationActivity extends AppCompatActivity {

    EditText name, surname, email, mobile_phone, password;
    ProgressDialog loadingBar;
    Button reg;
    FirebaseUser user;
    String nameUser, surnameUser, phoneUser, passUser, emailUser;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initComponents();


        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creatAccount();
            }
        });
    }

    void initComponents()
    {
        name = findViewById(R.id.fieldName);
        surname = findViewById(R.id.fieldSurname);
        email = findViewById(R.id.fieldEmailReg);
        mobile_phone = findViewById(R.id.fieldPhone);
        password = findViewById(R.id.fieldPasswordReg);
        reg = findViewById(R.id.btn_registr);
        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);
    }

    void creatAccount()
    {
        if(TextUtils.isEmpty(name.getText().toString()))
        {
            Toasty.normal(this,"Please enter your Name").show();
        }
        else if(TextUtils.isEmpty(surname.getText().toString()))
        {
            Toasty.normal(this,"Please enter your Surname").show();
        }
        else if(TextUtils.isEmpty(email.getText().toString()))
        {
            Toasty.normal(this,"Please enter your Email").show();
        }
        else if(TextUtils.isEmpty(mobile_phone.getText().toString()))
        {
            Toasty.normal(this,"Please enter your Mobile Phone").show();
        }
        else if(TextUtils.isEmpty(password.getText().toString()))
        {
            Toasty.normal(this,"Please enter your Password").show();
        }
        else
            {
                loadingBar.setTitle("Creating your account");
                loadingBar.setMessage("Please wait...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                nameUser = name.getText().toString();
                surnameUser = surname.getText().toString();
                emailUser = email.getText().toString();
                phoneUser = mobile_phone.getText().toString();
                passUser = password.getText().toString();
                Auth(emailUser, passUser);
            }
    }
    private void validateUser(String name, String surname, String email, String phone, String password, String id)
    {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refDB= database.getReference("Users");
        refDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(phone).exists())
                {
                    loadingBar.dismiss();
                    Toasty.normal(RegistrationActivity.this,"Account with this Email already exists").show();
                }
                else
                    {
                        HashMap<String, Object> userData = new HashMap<>();
                        userData.put("name", name);
                        userData.put("surname", surname);
                        userData.put("email", email);
                        userData.put("mobile_number", phone);
                        userData.put("password", password);
                        userData.put("favorites", "0");

                        refDB.child(user.getUid()).updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                loadingBar.dismiss();
                                Toasty.normal(RegistrationActivity.this,"Account created").show();
                                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                intent.putExtra("email", email);
                                intent.putExtra("password", password);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void Auth(String email, String password)
    {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    user = mAuth.getCurrentUser();
                    assert user != null;
                    validateUser(nameUser,surnameUser,emailUser, phoneUser, passUser, user.getUid());
                } else {

                    loadingBar.dismiss();
                    Toasty.normal(RegistrationActivity.this,"Authentication failed.").show();

                }
            }
        });
    }
}