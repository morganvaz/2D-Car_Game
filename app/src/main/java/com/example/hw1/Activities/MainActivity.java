package com.example.hw1.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.example.hw1.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class MainActivity extends AppCompatActivity {

    private MaterialButton startGame;
    private MaterialButton highScores;
    private SwitchMaterial gameMode;
    public static final String GAME_MODE = "GAME_MODE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ask location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},101);
        }

        startGame = findViewById(R.id.main_BTN_startGame);
        gameMode = findViewById(R.id.main_TGL_gameMode);

        Intent gameIntent = new Intent(MainActivity.this, GameActivity.class);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameMode.isChecked())
                    gameIntent.putExtra(GAME_MODE,"Sensors");
                 else
                    gameIntent.putExtra(GAME_MODE,"Arrows");

                startActivity(gameIntent);
            }
        });
        highScores = findViewById(R.id.main_BTN_highScores);
        highScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HighScoreActivity.class));
            }
        });
    }
}