package com.example.cc17nu_rescue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.nio.channels.InterruptedByTimeoutException;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth authProfile;
    private EditText Username, Password;
    private ImageView IBtn;
    private TextView Forgotpassword, Register;
    private ImageView Fb, Google;
    private ProgressBar PBar;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Login user");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0E86D4")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        authProfile = FirebaseAuth.getInstance();
        Username = findViewById(R.id.username);
        Password =  findViewById(R.id.password);
        IBtn =  findViewById(R.id.iBtn);
        Forgotpassword = findViewById(R.id.forgotpassword);
        Register =  findViewById(R.id.register);
        Fb =  findViewById(R.id.fb);
        Google =  findViewById(R.id.google);
        PBar = findViewById(R.id.pbar);

        //show hide password
        ImageView showPass = findViewById(R.id.eyeIcon);
        showPass.setImageResource(R.drawable.hide_pass);
        showPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Password.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    //if password is visible
                    Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    //change icon
                    showPass.setImageResource(R.drawable.hide_pass);
                }else {
                    Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPass.setImageResource(R.drawable.show_pass);
                }
            }
        });


        //login user
        IBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textEmail = Username.getText().toString();
                String textPass = Password.getText().toString();

                if(TextUtils.isEmpty(textEmail)){
                    Toast.makeText(MainActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    Username.setError("Email is required");
                    Username.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText(MainActivity.this, "Please re-enter your email", Toast.LENGTH_SHORT).show();
                    Username.setError("Valid email is required");
                    Username.requestFocus();
                } else if(TextUtils.isEmpty(textPass)){
                    Toast.makeText(MainActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    Password.setError("Password is required");
                    Password.requestFocus();
                }else {
                    PBar.setVisibility(View.VISIBLE);
                    loginUser(textEmail,textPass);
                }
            }
        });


        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

    }

    private void loginUser(String email, String pass) {
        authProfile.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                        Toast.makeText(MainActivity.this, "You are now Logged in", Toast.LENGTH_SHORT).show();
                        //open user profile
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                }else{
                    try{
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e){
                        Username.setError("User does not exist or no longer valid. please register again.");
                        Username.requestFocus();
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        Username.setError("Invalid email or password please check and re-enter");
                        Username.requestFocus();
                    } catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                PBar.setVisibility(View.GONE);
            }
        });
    }

   /* private void showAlertDialog() {

        //set up alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Email not Verified");
        builder.setMessage("Please verify your email. You cannot log in without email verification");

        //open email app
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // to email window
                startActivity(intent);
            }
        });
        //alert box
        AlertDialog alertDialog = builder.create();
        //show alert box
        alertDialog.show();
    }*/
   //check if the user is already logged in
    @Override
    protected void onStart() {
        super.onStart();
        if (authProfile.getCurrentUser()!=null){
            Toast.makeText(MainActivity.this, "Already logged in", Toast.LENGTH_LONG).show();
            //start the user Dashboard
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            finish();
        } else {
            Toast.makeText(MainActivity.this, "Fill up to log in", Toast.LENGTH_LONG).show();
        }
    }
}