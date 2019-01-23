package com.kinitoapps.quicknotes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kinitoapps.quicknotes.R;


public class ChooseClass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_class);

                TextView ninth_class = findViewById(R.id.ninth_class);
        TextView tenth_class = findViewById(R.id.tenth_class);
        tenth_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseClass.this, MainDrawerActivity.class);
                intent.putExtra("class_name", "10th Class ");
                startActivity(intent);
            }
        });
        ninth_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseClass.this, MainDrawerActivity.class);
                intent.putExtra("class_name", "9th Class ");
                startActivity(intent);
            }
        });








       }

    }







