package com.example.cc17nu_rescue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.cc17nu_rescue.databinding.ActivityPetProfileBinding;

public class PetProfileActivity extends AppCompatActivity {
    private Button adopt, cancel;

    ActivityPetProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPetProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Pet Information");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0E86D4")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adopt = findViewById(R.id.btnpetadopt);
        cancel = findViewById(R.id.btnpetcancel);

        adopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowAlertDialog1();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PetProfileActivity.this,AdoptActivity.class));
            }
        });

        Intent intent = this.getIntent();

        if(intent != null){
            String name = intent.getStringExtra("name");
            String breed = intent.getStringExtra("breed");
            String age = intent.getStringExtra("age");
            String gender = intent.getStringExtra("gender");
            String color = intent.getStringExtra("color");
            String weight = intent.getStringExtra("weight");
            String social = intent.getStringExtra("social");
            int imageId = intent.getIntExtra("imageId",R.drawable.no_profile_pic);

            binding.petname.setText(name);
            binding.petbreed.setText(breed);
            binding.petage.setText(age);
            binding.petgender.setText(gender);
            binding.petcolor.setText(color);
            binding.petweight.setText(weight);
            binding.petsocial.setText(social);
            binding.petprofile.setImageResource(imageId);

        }
    }

    private void ShowAlertDialog1() {
        //set up alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(PetProfileActivity.this);
        builder.setTitle("Are you ready to adopt this pet? ");
        builder.setMessage("We will be needing your full responsibility to take care of this pet");

        //open email app
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ShowAlertDialog();
            }
        });
        builder.setNegativeButton("No",null);
        //alert box
        AlertDialog alertDialog = builder.create();
        //show alert box
        alertDialog.show();
    }

    private void ShowAlertDialog() {
        //set up alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(PetProfileActivity.this);
        builder.setTitle("Thank you! ^_^ ");
        builder.setMessage("Your request for adoption is waiting for confirmation, thank you for giving home to this lovely pets.");

        //open email app
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(PetProfileActivity.this,AdoptActivity.class));
            }
        });
        //alert box
        AlertDialog alertDialog = builder.create();
        //show alert box
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(PetProfileActivity.this);
        }
        else {
            Toast.makeText(PetProfileActivity.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}