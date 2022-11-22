package com.example.cc17nu_rescue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<Pets> {

    public ListAdapter(Context context, ArrayList<Pets> petArrayList){
        super(context,R.layout.list_item,petArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Pets pets = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        ImageView imageView = convertView.findViewById(R.id.petProfile);
        TextView petname = convertView.findViewById(R.id.petName);
        TextView petbreed = convertView.findViewById(R.id.petBreed);
        TextView petage = convertView.findViewById(R.id.petAge);

        imageView.setImageResource(pets.imageId);
        petname.setText(pets.name);
        petbreed.setText(pets.breed);
        petage.setText(pets.age);

        return convertView;
    }
}
