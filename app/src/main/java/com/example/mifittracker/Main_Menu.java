package com.example.mifittracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Main_Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__menu);

        ImageView startTrainingButton = (ImageView)findViewById(R.id.imageView6);
        ImageView statTrainingButton = (ImageView)findViewById(R.id.imageView7);

        startTrainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToStartTraining = new Intent(getApplicationContext(), Training_Page.class);
                startActivity(intentToStartTraining);
            }
        });

        statTrainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToStatTraining = new Intent(getApplicationContext(), CompletedExercitesData.class);
                startActivity(intentToStatTraining);
            }
        });
    }
}
