package com.example.cc17nu_rescue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
/*import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;*/

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private EditText Editname, Editemail, EditDoB, Editnumber, Editpass,
            Editcpass;
    private ProgressBar PBar;
    private RadioGroup RGgender;
    private RadioButton RBgender;
    private DatePickerDialog picker;
    private static final String TAG= "RegisterActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Registration");

        Toast.makeText(RegisterActivity.this, "You can register now", Toast.LENGTH_LONG).show();

        PBar = findViewById(R.id.pbar);
        Editname = findViewById(R.id.editname);
        Editemail = findViewById(R.id.editemail);
        EditDoB = findViewById(R.id.editbirth);
        Editnumber = findViewById(R.id.editnumber);
        Editpass = findViewById(R.id.editpass);
        Editcpass = findViewById(R.id.editcpass);
        //Radio gender
        RGgender = findViewById(R.id.radiogender);
        RGgender.clearCheck();
        //Date picker
        EditDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                //date dialog
                picker = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        EditDoB.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        Button BtnRegister = findViewById(R.id.register);
        BtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedGender = RGgender.getCheckedRadioButtonId();
                RBgender = findViewById(selectedGender);

                //obtain entered data
                String textFullname = Editname.getText().toString();
                String textEmail = Editemail.getText().toString();
                String textBday = EditDoB.getText().toString();
                String textNumber = Editnumber.getText().toString();
                String textPassword = Editpass.getText().toString();
                String textCpass = Editcpass.getText().toString();
                String textGender;

                //Validate mobile number
                String mobileRegex = "[8-9][0-9]{9}";
                Matcher mobileMatcher;
                Pattern mobilePattern = Pattern.compile(mobileRegex);
                mobileMatcher = mobilePattern.matcher(textNumber);

                if(TextUtils.isEmpty(textFullname)){
                    Toast.makeText(RegisterActivity.this, "Please enter your full name", Toast.LENGTH_LONG).show();
                    Editname.setError("Full name is required");
                    Editname.requestFocus();
                } else if(TextUtils.isEmpty(textEmail)){
                    Toast.makeText(RegisterActivity.this, "Please enter your Email", Toast.LENGTH_LONG).show();
                    Editemail.setError("Email is required");
                    Editemail.requestFocus();
                } else if(!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText(RegisterActivity.this, "Please re-enter your Email", Toast.LENGTH_LONG).show();
                    Editemail.setError("Valid Email is required");
                    Editemail.requestFocus();
                } else if(TextUtils.isEmpty(textBday)){
                    Toast.makeText(RegisterActivity.this, "Please enter your date of birth", Toast.LENGTH_LONG).show();
                    EditDoB.setError("Date of birth is required");
                    EditDoB.requestFocus();
                } else if(RGgender.getCheckedRadioButtonId() == -1){
                    Toast.makeText(RegisterActivity.this, "Please select your gender", Toast.LENGTH_LONG).show();
                    RBgender.setError("Gender is required");
                    RBgender.requestFocus();
                } else if(TextUtils.isEmpty(textNumber)){
                    Toast.makeText(RegisterActivity.this, "Please enter your contact number", Toast.LENGTH_LONG).show();
                    Editnumber.setError("Contact number is required");
                    Editnumber.requestFocus();
                } else if(Editnumber.length() != 10){
                    Toast.makeText(RegisterActivity.this, "Please re-enter your contact number", Toast.LENGTH_LONG).show();
                    Editnumber.setError("Contact number should be 10 digits");
                    Editnumber.requestFocus();
                } else if(!mobileMatcher.find()){
                    Toast.makeText(RegisterActivity.this, "Please re-enter your contact number", Toast.LENGTH_LONG).show();
                    Editnumber.setError("Contact number is invalid");
                    Editnumber.requestFocus();
                }else if(TextUtils.isEmpty(textPassword)){
                    Toast.makeText(RegisterActivity.this, "Please enter your Password", Toast.LENGTH_LONG).show();
                    Editpass.setError("Password is required");
                    Editpass.requestFocus();
                } else if(Editpass.length() < 6){
                    Toast.makeText(RegisterActivity.this, "Password should be at least 6 characters", Toast.LENGTH_LONG).show();
                    Editpass.setError("Password is to weak");
                    Editpass.requestFocus();
                } else if(TextUtils.isEmpty(textCpass)){
                    Toast.makeText(RegisterActivity.this, "Please confirm your Password", Toast.LENGTH_LONG).show();
                    Editcpass.setError("Password confirmation is required");
                    Editcpass.requestFocus();
                }else if(!textPassword.equals(textCpass)){
                    Toast.makeText(RegisterActivity.this, "Confirmation password didn't match", Toast.LENGTH_LONG).show();
                    Editcpass.setError("Password confirmation is required");
                    Editcpass.requestFocus();
                    Editpass.clearComposingText();
                    Editcpass.clearComposingText();
                } else {
                    textGender = RBgender.getText().toString();
                    PBar.setVisibility(View.VISIBLE);
                    registerUser(textFullname, textEmail, textBday, textNumber, textPassword, textGender);
                }
            }
        });
    }

      private void registerUser(String textFullname, String textEmail, String textBday, String textNumber, String textPassword, String textGender) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                   FirebaseUser firebaseUser = auth.getCurrentUser();

                   //Enter user Details
                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(textBday, textGender ,textNumber, textFullname);

                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
                    referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "User registered successfully.", Toast.LENGTH_LONG).show();
                                auth.signOut();
                                Intent intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
                                //clear stack to prevent user coming back to ProfileActivity on clicking back button after logging out
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else{
                                Toast.makeText(RegisterActivity.this, "User registered Failed. Please try again", Toast.LENGTH_LONG).show();
                            }
                            PBar.setVisibility(View.GONE);
                        }
                    });
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        Editpass.setError("Your password is to weak. Kindly use a mix of alphabets, number and special characters");
                        Editpass.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        Editemail.setError("Your email is invalid or already in use. Kindly re enter your email");
                        Editemail.requestFocus();
                    } catch (FirebaseAuthUserCollisionException e) {
                        Editpass.setError("User is already registered with this email. use another email");
                        Editpass.requestFocus();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    //hiding process bar
                    PBar.setVisibility(View.GONE);
                }
            }
        });
    }
}