package com.example.hw1;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    private final int LEFT = 0, CENTER_LEFT = 1, CENTER = 2, CENTER_RIGHT = 3, RIGHT = 4;
    private ImageView[][] game_IMG_dynamites;
    private ImageView[][] game_IMG_nitros;
    private ImageView[] game_IMG_explosions;
    private ImageView[] game_IMG_car;
    private ImageView[] game_IMG_lives;
    private ImageButton leftArrow, rightArrow;
    private int lifeCount = 2, carPos = CENTER, dynamiteLane, score = 0;
    private MediaPlayer explosionSound, gameOverSound, nitrosSound;
    private TextView game_LBL_score;

    private static final int DELAY = 900;
    private int clock = 0;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        hideSystemUI();
        initViews();
        explosionSound = MediaPlayer.create(this, R.raw.explosion_sound);
        gameOverSound = MediaPlayer.create(this, R.raw.game_over_sound);
        nitrosSound = MediaPlayer.create(this, R.raw.nitros_sound);

        rightArrow.setOnClickListener(v -> {
            if (carPos < RIGHT) {
                game_IMG_car[carPos].setVisibility(View.INVISIBLE);
                game_IMG_car[++carPos].setVisibility(View.VISIBLE);
                checkHit();
            }
        });
        leftArrow.setOnClickListener(v -> {
            if (carPos > LEFT) {
                game_IMG_car[carPos].setVisibility(View.INVISIBLE);
                game_IMG_car[--carPos].setVisibility(View.VISIBLE);
                checkHit();
            }
        });
    }
    private void updateUI() {
        startTicker();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
    }

    private void startTicker() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.d("timeTick", "Tick: " + clock + " On Thread: " + Thread.currentThread().getName());
                runOnUiThread(() -> {
                    Log.d("timeTick", "Tick: " + clock + " On Thread: " + Thread.currentThread().getName());
                    updateModels();
                });
            }
        }, 0, DELAY);
    }

    private void updateModels() {
        clock++;
        score++;
        game_LBL_score.setText(String.valueOf(score));
        hideExplosions();

        for (int i = 0; i < 5; i++) {
            if (game_IMG_dynamites[11][i].getVisibility() == View.VISIBLE) {
                game_IMG_dynamites[11][i].setVisibility(View.GONE);
            }
            if (game_IMG_nitros[11][i].getVisibility() == View.VISIBLE) {
                game_IMG_nitros[11][i].setVisibility(View.GONE);
            }
            for (int j = 11; j >= 0; j--) {
                if (game_IMG_dynamites[j][i].getVisibility() == View.VISIBLE) {
                    game_IMG_dynamites[j][i].setVisibility(View.GONE);
                    game_IMG_dynamites[j + 1][i].setVisibility(View.VISIBLE);
                }
            }
            for (int j = 11; j >= 0; j--) {
                if (game_IMG_nitros[j][i].getVisibility() == View.VISIBLE) {
                    game_IMG_nitros[j][i].setVisibility(View.GONE);
                    game_IMG_nitros[j + 1][i].setVisibility(View.VISIBLE);
                }
            }
        }
        if (clock % 2 == 0)
            placeDynamiteInLane();

        if (clock % 5 == 0)
            placeNitrosInLane();

        checkHit();

    }

    private void placeNitrosInLane() {
        int nitrosLane = getRandomLane();
        while (nitrosLane == dynamiteLane)
            nitrosLane = getRandomLane();

        switch (nitrosLane) {
            case LEFT:
                game_IMG_nitros[0][LEFT].setVisibility(View.VISIBLE);
                break;
            case CENTER_LEFT:
                game_IMG_nitros[0][CENTER_LEFT].setVisibility(View.VISIBLE);
                break;
            case CENTER:
                game_IMG_nitros[0][CENTER].setVisibility(View.VISIBLE);
                break;
            case CENTER_RIGHT:
                game_IMG_nitros[0][CENTER_RIGHT].setVisibility(View.VISIBLE);
                break;
            case RIGHT:
                game_IMG_nitros[0][RIGHT].setVisibility(View.VISIBLE);
                break;
        }
    }

    private void placeDynamiteInLane() {
        dynamiteLane = getRandomLane();
        switch (dynamiteLane) {
            case LEFT:
                game_IMG_dynamites[0][LEFT].setVisibility(View.VISIBLE);
                break;
            case CENTER_LEFT:
                game_IMG_dynamites[0][CENTER_LEFT].setVisibility(View.VISIBLE);
                break;
            case CENTER:
                game_IMG_dynamites[0][CENTER].setVisibility(View.VISIBLE);
                break;
            case CENTER_RIGHT:
                game_IMG_dynamites[0][CENTER_RIGHT].setVisibility(View.VISIBLE);
                break;
            case RIGHT:
                game_IMG_dynamites[0][RIGHT].setVisibility(View.VISIBLE);
                break;
        }
    }

    private void hideExplosions() {
        game_IMG_explosions[LEFT].setVisibility(View.GONE);
        game_IMG_explosions[CENTER_LEFT].setVisibility(View.GONE);
        game_IMG_explosions[CENTER].setVisibility(View.GONE);
        game_IMG_explosions[CENTER_RIGHT].setVisibility(View.GONE);
        game_IMG_explosions[RIGHT].setVisibility(View.GONE);
        game_IMG_car[carPos].setVisibility(View.VISIBLE);
    }

    private void checkHit() {
        if (game_IMG_dynamites[11][carPos].getVisibility() == View.VISIBLE
                && game_IMG_car[carPos].getVisibility() == View.VISIBLE) {
            game_IMG_dynamites[11][carPos].setVisibility(View.GONE);
            game_IMG_car[carPos].setVisibility(View.GONE);
            game_IMG_explosions[carPos].setVisibility(View.VISIBLE);
            game_IMG_lives[lifeCount--].setVisibility(View.INVISIBLE);
            toast(true);
            vibrate();
            explosionSound.start();
        }
        if (game_IMG_nitros[11][carPos].getVisibility() == View.VISIBLE
                && game_IMG_car[carPos].getVisibility() == View.VISIBLE) {
            game_IMG_nitros[11][carPos].setVisibility(View.GONE);
            //game_IMG_explosions[carPos].setVisibility(View.VISIBLE); ### CONSIDER ADDING ANOTHER ICON FOR NITROS HIT ###
            toast(false);
            vibrate();
            nitrosSound.start();
            score += 1000;
        }

    }

    private void toast(boolean dynamite) {
        if (dynamite){
            switch(lifeCount){
                case 1:
                    Toast.makeText(this, "2 more lives to go!", Toast.LENGTH_LONG).show();
                    break;
                case 0:
                    Toast.makeText(this, "LAST LIFE!", Toast.LENGTH_LONG).show();
                    break;
                case -1:
                    Toast.makeText(this, "##  Restarting Game  ## ", Toast.LENGTH_LONG).show();
                    restartGame();
                    break;
            }
        } else
            Toast.makeText(this, " !!! Nitros BOOST +1000 !!! ", Toast.LENGTH_LONG).show();
    }

    private void restartGame() {
        gameOverSound.start();
        lifeCount = 2;
        game_IMG_lives[0].setVisibility(View.VISIBLE);
        game_IMG_lives[1].setVisibility(View.VISIBLE);
        game_IMG_lives[2].setVisibility(View.VISIBLE);
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
    }

    private int getRandomLane() {
        return (int) (Math.random()*(RIGHT+1-LEFT)) + LEFT;
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopTicker();
    }

    private void stopTicker() {
        timer.cancel();
    }

    private void initViews() {
        game_IMG_dynamites = new ImageView[12][5];
        game_IMG_nitros = new ImageView[12][5];
        game_IMG_explosions = new ImageView[5];
        game_IMG_car = new ImageView[5];
        game_IMG_lives = new ImageView[3];
        game_LBL_score = findViewById(R.id.game_LBL_score);

        game_IMG_dynamites[0][LEFT] = findViewById(R.id.game_IMG_dynamite_left1);
        game_IMG_dynamites[1][LEFT] = findViewById(R.id.game_IMG_dynamite_left2);
        game_IMG_dynamites[2][LEFT] = findViewById(R.id.game_IMG_dynamite_left3);
        game_IMG_dynamites[3][LEFT] = findViewById(R.id.game_IMG_dynamite_left4);
        game_IMG_dynamites[4][LEFT] = findViewById(R.id.game_IMG_dynamite_left5);
        game_IMG_dynamites[5][LEFT] = findViewById(R.id.game_IMG_dynamite_left6);
        game_IMG_dynamites[6][LEFT] = findViewById(R.id.game_IMG_dynamite_left7);
        game_IMG_dynamites[7][LEFT] = findViewById(R.id.game_IMG_dynamite_left8);
        game_IMG_dynamites[8][LEFT] = findViewById(R.id.game_IMG_dynamite_left9);
        game_IMG_dynamites[9][LEFT] = findViewById(R.id.game_IMG_dynamite_left10);
        game_IMG_dynamites[10][LEFT] = findViewById(R.id.game_IMG_dynamite_left11);
        game_IMG_dynamites[11][LEFT] = findViewById(R.id.game_IMG_dynamite_left12);

        game_IMG_dynamites[0][CENTER_LEFT] = findViewById(R.id.game_IMG_dynamite_center_left1);
        game_IMG_dynamites[1][CENTER_LEFT] = findViewById(R.id.game_IMG_dynamite_center_left2);
        game_IMG_dynamites[2][CENTER_LEFT] = findViewById(R.id.game_IMG_dynamite_center_left3);
        game_IMG_dynamites[3][CENTER_LEFT] = findViewById(R.id.game_IMG_dynamite_center_left4);
        game_IMG_dynamites[4][CENTER_LEFT] = findViewById(R.id.game_IMG_dynamite_center_left5);
        game_IMG_dynamites[5][CENTER_LEFT] = findViewById(R.id.game_IMG_dynamite_center_left6);
        game_IMG_dynamites[6][CENTER_LEFT] = findViewById(R.id.game_IMG_dynamite_center_left7);
        game_IMG_dynamites[7][CENTER_LEFT] = findViewById(R.id.game_IMG_dynamite_center_left8);
        game_IMG_dynamites[8][CENTER_LEFT] = findViewById(R.id.game_IMG_dynamite_center_left9);
        game_IMG_dynamites[9][CENTER_LEFT] = findViewById(R.id.game_IMG_dynamite_center_left10);
        game_IMG_dynamites[10][CENTER_LEFT] = findViewById(R.id.game_IMG_dynamite_center_left11);
        game_IMG_dynamites[11][CENTER_LEFT] = findViewById(R.id.game_IMG_dynamite_center_left12);

        game_IMG_dynamites[0][CENTER] = findViewById(R.id.game_IMG_dynamite_center1);
        game_IMG_dynamites[1][CENTER] = findViewById(R.id.game_IMG_dynamite_center2);
        game_IMG_dynamites[2][CENTER] = findViewById(R.id.game_IMG_dynamite_center3);
        game_IMG_dynamites[3][CENTER] = findViewById(R.id.game_IMG_dynamite_center4);
        game_IMG_dynamites[4][CENTER] = findViewById(R.id.game_IMG_dynamite_center5);
        game_IMG_dynamites[5][CENTER] = findViewById(R.id.game_IMG_dynamite_center6);
        game_IMG_dynamites[6][CENTER] = findViewById(R.id.game_IMG_dynamite_center7);
        game_IMG_dynamites[7][CENTER] = findViewById(R.id.game_IMG_dynamite_center8);
        game_IMG_dynamites[8][CENTER] = findViewById(R.id.game_IMG_dynamite_center9);
        game_IMG_dynamites[9][CENTER] = findViewById(R.id.game_IMG_dynamite_center10);
        game_IMG_dynamites[10][CENTER] = findViewById(R.id.game_IMG_dynamite_center11);
        game_IMG_dynamites[11][CENTER] = findViewById(R.id.game_IMG_dynamite_center12);

        game_IMG_dynamites[0][CENTER_RIGHT] = findViewById(R.id.game_IMG_dynamite_center_right1);
        game_IMG_dynamites[1][CENTER_RIGHT] = findViewById(R.id.game_IMG_dynamite_center_right2);
        game_IMG_dynamites[2][CENTER_RIGHT] = findViewById(R.id.game_IMG_dynamite_center_right3);
        game_IMG_dynamites[3][CENTER_RIGHT] = findViewById(R.id.game_IMG_dynamite_center_right4);
        game_IMG_dynamites[4][CENTER_RIGHT] = findViewById(R.id.game_IMG_dynamite_center_right5);
        game_IMG_dynamites[5][CENTER_RIGHT] = findViewById(R.id.game_IMG_dynamite_center_right6);
        game_IMG_dynamites[6][CENTER_RIGHT] = findViewById(R.id.game_IMG_dynamite_center_right7);
        game_IMG_dynamites[7][CENTER_RIGHT] = findViewById(R.id.game_IMG_dynamite_center_right8);
        game_IMG_dynamites[8][CENTER_RIGHT] = findViewById(R.id.game_IMG_dynamite_center_right9);
        game_IMG_dynamites[9][CENTER_RIGHT] = findViewById(R.id.game_IMG_dynamite_center_right10);
        game_IMG_dynamites[10][CENTER_RIGHT] = findViewById(R.id.game_IMG_dynamite_center_right11);
        game_IMG_dynamites[11][CENTER_RIGHT] = findViewById(R.id.game_IMG_dynamite_center_right12);

        game_IMG_dynamites[0][RIGHT] = findViewById(R.id.game_IMG_dynamite_right1);
        game_IMG_dynamites[1][RIGHT] = findViewById(R.id.game_IMG_dynamite_right2);
        game_IMG_dynamites[2][RIGHT] = findViewById(R.id.game_IMG_dynamite_right3);
        game_IMG_dynamites[3][RIGHT] = findViewById(R.id.game_IMG_dynamite_right4);
        game_IMG_dynamites[4][RIGHT] = findViewById(R.id.game_IMG_dynamite_right5);
        game_IMG_dynamites[5][RIGHT] = findViewById(R.id.game_IMG_dynamite_right6);
        game_IMG_dynamites[6][RIGHT] = findViewById(R.id.game_IMG_dynamite_right7);
        game_IMG_dynamites[7][RIGHT] = findViewById(R.id.game_IMG_dynamite_right8);
        game_IMG_dynamites[8][RIGHT] = findViewById(R.id.game_IMG_dynamite_right9);
        game_IMG_dynamites[9][RIGHT] = findViewById(R.id.game_IMG_dynamite_right10);
        game_IMG_dynamites[10][RIGHT] = findViewById(R.id.game_IMG_dynamite_right11);
        game_IMG_dynamites[11][RIGHT] = findViewById(R.id.game_IMG_dynamite_right12);

        game_IMG_nitros[0][LEFT] = findViewById(R.id.game_IMG_nitros_left1);
        game_IMG_nitros[1][LEFT] = findViewById(R.id.game_IMG_nitros_left2);
        game_IMG_nitros[2][LEFT] = findViewById(R.id.game_IMG_nitros_left3);
        game_IMG_nitros[3][LEFT] = findViewById(R.id.game_IMG_nitros_left4);
        game_IMG_nitros[4][LEFT] = findViewById(R.id.game_IMG_nitros_left5);
        game_IMG_nitros[5][LEFT] = findViewById(R.id.game_IMG_nitros_left6);
        game_IMG_nitros[6][LEFT] = findViewById(R.id.game_IMG_nitros_left7);
        game_IMG_nitros[7][LEFT] = findViewById(R.id.game_IMG_nitros_left8);
        game_IMG_nitros[8][LEFT] = findViewById(R.id.game_IMG_nitros_left9);
        game_IMG_nitros[9][LEFT] = findViewById(R.id.game_IMG_nitros_left10);
        game_IMG_nitros[10][LEFT] = findViewById(R.id.game_IMG_nitros_left11);
        game_IMG_nitros[11][LEFT] = findViewById(R.id.game_IMG_nitros_left12);

        game_IMG_nitros[0][CENTER_LEFT] = findViewById(R.id.game_IMG_nitros_center_left1);
        game_IMG_nitros[1][CENTER_LEFT] = findViewById(R.id.game_IMG_nitros_center_left2);
        game_IMG_nitros[2][CENTER_LEFT] = findViewById(R.id.game_IMG_nitros_center_left3);
        game_IMG_nitros[3][CENTER_LEFT] = findViewById(R.id.game_IMG_nitros_center_left4);
        game_IMG_nitros[4][CENTER_LEFT] = findViewById(R.id.game_IMG_nitros_center_left5);
        game_IMG_nitros[5][CENTER_LEFT] = findViewById(R.id.game_IMG_nitros_center_left6);
        game_IMG_nitros[6][CENTER_LEFT] = findViewById(R.id.game_IMG_nitros_center_left7);
        game_IMG_nitros[7][CENTER_LEFT] = findViewById(R.id.game_IMG_nitros_center_left8);
        game_IMG_nitros[8][CENTER_LEFT] = findViewById(R.id.game_IMG_nitros_center_left9);
        game_IMG_nitros[9][CENTER_LEFT] = findViewById(R.id.game_IMG_nitros_center_left10);
        game_IMG_nitros[10][CENTER_LEFT] = findViewById(R.id.game_IMG_nitros_center_left11);
        game_IMG_nitros[11][CENTER_LEFT] = findViewById(R.id.game_IMG_nitros_center_left12);

        game_IMG_nitros[0][CENTER] = findViewById(R.id.game_IMG_nitros_center1);
        game_IMG_nitros[1][CENTER] = findViewById(R.id.game_IMG_nitros_center2);
        game_IMG_nitros[2][CENTER] = findViewById(R.id.game_IMG_nitros_center3);
        game_IMG_nitros[3][CENTER] = findViewById(R.id.game_IMG_nitros_center4);
        game_IMG_nitros[4][CENTER] = findViewById(R.id.game_IMG_nitros_center5);
        game_IMG_nitros[5][CENTER] = findViewById(R.id.game_IMG_nitros_center6);
        game_IMG_nitros[6][CENTER] = findViewById(R.id.game_IMG_nitros_center7);
        game_IMG_nitros[7][CENTER] = findViewById(R.id.game_IMG_nitros_center8);
        game_IMG_nitros[8][CENTER] = findViewById(R.id.game_IMG_nitros_center9);
        game_IMG_nitros[9][CENTER] = findViewById(R.id.game_IMG_nitros_center10);
        game_IMG_nitros[10][CENTER] = findViewById(R.id.game_IMG_nitros_center11);
        game_IMG_nitros[11][CENTER] = findViewById(R.id.game_IMG_nitros_center12);

        game_IMG_nitros[0][CENTER_RIGHT] = findViewById(R.id.game_IMG_nitros_center_right1);
        game_IMG_nitros[1][CENTER_RIGHT] = findViewById(R.id.game_IMG_nitros_center_right2);
        game_IMG_nitros[2][CENTER_RIGHT] = findViewById(R.id.game_IMG_nitros_center_right3);
        game_IMG_nitros[3][CENTER_RIGHT] = findViewById(R.id.game_IMG_nitros_center_right4);
        game_IMG_nitros[4][CENTER_RIGHT] = findViewById(R.id.game_IMG_nitros_center_right5);
        game_IMG_nitros[5][CENTER_RIGHT] = findViewById(R.id.game_IMG_nitros_center_right6);
        game_IMG_nitros[6][CENTER_RIGHT] = findViewById(R.id.game_IMG_nitros_center_right7);
        game_IMG_nitros[7][CENTER_RIGHT] = findViewById(R.id.game_IMG_nitros_center_right8);
        game_IMG_nitros[8][CENTER_RIGHT] = findViewById(R.id.game_IMG_nitros_center_right9);
        game_IMG_nitros[9][CENTER_RIGHT] = findViewById(R.id.game_IMG_nitros_center_right10);
        game_IMG_nitros[10][CENTER_RIGHT] = findViewById(R.id.game_IMG_nitros_center_right11);
        game_IMG_nitros[11][CENTER_RIGHT] = findViewById(R.id.game_IMG_nitros_center_right12);

        game_IMG_nitros[0][RIGHT] = findViewById(R.id.game_IMG_nitros_right1);
        game_IMG_nitros[1][RIGHT] = findViewById(R.id.game_IMG_nitros_right2);
        game_IMG_nitros[2][RIGHT] = findViewById(R.id.game_IMG_nitros_right3);
        game_IMG_nitros[3][RIGHT] = findViewById(R.id.game_IMG_nitros_right4);
        game_IMG_nitros[4][RIGHT] = findViewById(R.id.game_IMG_nitros_right5);
        game_IMG_nitros[5][RIGHT] = findViewById(R.id.game_IMG_nitros_right6);
        game_IMG_nitros[6][RIGHT] = findViewById(R.id.game_IMG_nitros_right7);
        game_IMG_nitros[7][RIGHT] = findViewById(R.id.game_IMG_nitros_right8);
        game_IMG_nitros[8][RIGHT] = findViewById(R.id.game_IMG_nitros_right9);
        game_IMG_nitros[9][RIGHT] = findViewById(R.id.game_IMG_nitros_right10);
        game_IMG_nitros[10][RIGHT] = findViewById(R.id.game_IMG_nitros_right11);
        game_IMG_nitros[11][RIGHT] = findViewById(R.id.game_IMG_nitros_right12);

        game_IMG_explosions[LEFT] = findViewById(R.id.game_IMG_explostion_left);
        game_IMG_explosions[CENTER_LEFT] = findViewById(R.id.game_IMG_explostion_center_left);
        game_IMG_explosions[CENTER] = findViewById(R.id.game_IMG_explostion_center);
        game_IMG_explosions[CENTER_RIGHT] = findViewById(R.id.game_IMG_explostion_center_right);
        game_IMG_explosions[RIGHT] = findViewById(R.id.game_IMG_explostion_right);

        game_IMG_car[LEFT] = findViewById(R.id.game_IMG_car_left);
        game_IMG_car[CENTER_LEFT] = findViewById(R.id.game_IMG_car_center_left);
        game_IMG_car[CENTER] = findViewById(R.id.game_IMG_car_center);
        game_IMG_car[CENTER_RIGHT] = findViewById(R.id.game_IMG_car_center_right);
        game_IMG_car[RIGHT] = findViewById(R.id.game_IMG_car_right);

        leftArrow = findViewById(R.id.game_BTN_leftArrow);
        rightArrow = findViewById(R.id.game_BTN_rightArrow);
        game_IMG_lives[0] = findViewById(R.id.game_IMG_heart1);
        game_IMG_lives[1] = findViewById(R.id.game_IMG_heart2);
        game_IMG_lives[2] = findViewById(R.id.game_IMG_heart3);
    }

    public void hideSystemUI() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        //| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}