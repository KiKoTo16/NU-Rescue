package com.example.cc17nu_rescue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.cc17nu_rescue.databinding.ActivityAdoptBinding;

import java.util.ArrayList;

public class AdoptActivity extends AppCompatActivity {

    ActivityAdoptBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdoptBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Adoption");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0E86D4")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int[] imageId = {R.drawable.a,R.drawable.b,R.drawable.c};
        String[] name = {"SNOWY","BLUE","GNOCHI"};
        String[] breed = {"PERSIAN","SHITZU","GOLDEN RETRIEVER"};
        String[] age = {"4 MONTHS","1 YEAR","8 MONTHS"};
        String[] gender = {"FEMALE","MALE","MALE"};
        String[] color = {"WHITE","BROWN","BROWN"};
        String[] weight = {"20 LBS","20 LBS","30 LBS"};
        String[] social = {"SWEET AND CARING","SLIGHTLY ATTITUDE","WISE AND FRIENDLY"};

        ArrayList<Pets> petsArrayList = new ArrayList<>();

        for(int i = 0; i < imageId.length; i++){

            Pets pets = new Pets(name[i],breed[i],age[i],gender[i],color[i],weight[i],social[i],imageId[i]);
            petsArrayList.add(pets);

        }

        ListAdapter listAdapter = new ListAdapter(AdoptActivity.this,petsArrayList);

        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);
        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent i = new Intent(AdoptActivity.this, PetProfileActivity.class);
                i.putExtra("name",name[position]);
                i.putExtra("breed",breed[position]);
                i.putExtra("age",age[position]);
                i.putExtra("gender",gender[position]);
                i.putExtra("color",color[position]);
                i.putExtra("weight",weight[position]);
                i.putExtra("social",social[position]);
                i.putExtra("imageId",imageId[position]);
                startActivity(i);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(AdoptActivity.this);
        }
        else {
            Toast.makeText(AdoptActivity.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}