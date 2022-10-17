package com.example.nu_rescue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.nio.channels.InterruptedByTimeoutException;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText Username, Password;
    private ImageView IBtn;
    private TextView Forgotpassword, Register;
    private ImageView Fb, Google;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        Username = (EditText) findViewById(R.id.username);
        Password = (EditText) findViewById(R.id.password);
        IBtn = (ImageView) findViewById(R.id.iBtn);
        Forgotpassword = (TextView) findViewById(R.id.forgotpassword);
        Register = (TextView) findViewById(R.id.register);
        Fb = (ImageView) findViewById(R.id.fb);
        Google = (ImageView) findViewById(R.id.google);

        IBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               login();
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

    }

   private void login() {

        String user = Username.getText().toString().trim();
        String pass = Password.getText().toString().trim();
        if(user.isEmpty()){
            Username.setError("Username cannot be empty");
        }
        if(pass.isEmpty()){
            Password.setError("Password cannot be empty");
        }
        else{
            mAuth.signInWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(MainActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Login Failed"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

}