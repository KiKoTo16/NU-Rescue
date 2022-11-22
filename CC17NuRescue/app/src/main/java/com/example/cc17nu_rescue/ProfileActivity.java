package com.example.cc17nu_rescue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class ProfileActivity extends AppCompatActivity {

    private TextView textFullname, textEmail, textGender, textBday, textNumber, textWelcome;
    private ProgressBar PBar;
    private String fullname, email, gender, bday, number;
    private ImageView imageView;
    private FirebaseAuth authProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0E86D4")));

        textFullname = findViewById(R.id.fullname);
        textEmail = findViewById(R.id.email);
        textBday = findViewById(R.id.bday);
        textGender = findViewById(R.id.gender);
        textNumber = findViewById(R.id.phone);
        textWelcome = findViewById(R.id.welcome);
        PBar = findViewById(R.id.pbar);

        //set Onclick Listener on Imageview to open upload profile pic activity
        imageView = findViewById(R.id.profileDp);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, UploadDPActivity.class);
                startActivity(intent);
            }
        });

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if(firebaseUser == null){
            Toast.makeText(ProfileActivity.this, "something went wrong, user details are not available at the moment",
                    Toast.LENGTH_LONG).show();
        }else{
            //checkIfEmailVerified(firebaseUser);
            PBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }


        //Initialization
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        //Home
        bottomNavigationView.setSelectedItemId(R.id.profile);
        //Item Selected Listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
                        return true;
                    case R.id.message:
                        startActivity(new Intent(getApplicationContext(), MessageActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.donate:
                        startActivity(new Intent(getApplicationContext(), DonationActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }


    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        //extracting user reference from database
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if(readUserDetails != null){
                    fullname = readUserDetails.name;
                    email = firebaseUser.getEmail();
                    bday = readUserDetails.dob;
                    gender = readUserDetails.gender;
                    number = readUserDetails.number;

                    textWelcome.setText("Welcome, " + fullname + "!");
                    textFullname.setText(fullname);
                    textEmail.setText(email);
                    textBday.setText(bday);
                    textGender.setText(gender);
                    textNumber.setText(number);

                    //setUser Dp (after user has uploaded)
                    Uri uri = firebaseUser.getPhotoUrl();

                    //Imageviewer set ImageURI() should not be used with regular URIs. So we using Picasso
                    Picasso.with(ProfileActivity.this).load(uri).into(imageView);
                }else{
                    Toast.makeText(ProfileActivity.this, "something went wrong!", Toast.LENGTH_LONG).show();
                }
                PBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "something went wrong!", Toast.LENGTH_LONG).show();
                PBar.setVisibility(View.GONE);
            }
        });
    }

    //creating action menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu item
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //when any menu is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(ProfileActivity.this);
        }
        else if(id == R.id.menu_refresh){
            //refresh page
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0 );
        } else if(id == R.id.logout) {
            authProfile.signOut();
            Toast.makeText(ProfileActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileActivity.this, WelcomeActivity.class);
            //clear stack to prevent user coming back to ProfileActivity on clicking back button after logging out
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(ProfileActivity.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
