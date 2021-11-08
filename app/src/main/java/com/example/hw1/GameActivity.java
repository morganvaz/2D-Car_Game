package com.example.hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    private final int LEFT = 0, CENTER = 1, RIGHT = 2;
    private ImageView[][] game_IMG_dynamites;
    private ImageView[] game_IMG_explosions;
    private ImageView[] game_IMG_car;
    private ImageView[] game_IMG_lives;
    private int lifeCount = 2;
    private ImageButton leftArrow, rightArrow;
    private int carPos = CENTER;

    private static final int DELAY = 1000;
    private int clock = 0;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        hideSystemUI();
        initViews();

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
                    updateDynamite();
                });
            }
        }, 0, DELAY);
    }

    private void updateDynamite() {
        clock++;
        hideExplosions();
        for (int i = 0; i < 3; i++) {
            if (game_IMG_dynamites[5][i].getVisibility() == View.VISIBLE) {
                game_IMG_dynamites[5][i].setVisibility(View.GONE);
            }
            for (int j = 4; j >= 0; j--) {
                if (game_IMG_dynamites[j][i].getVisibility() == View.VISIBLE) {
                    game_IMG_dynamites[j][i].setVisibility(View.INVISIBLE);
                    game_IMG_dynamites[j + 1][i].setVisibility(View.VISIBLE);
                }
            }
        }
        if (clock % 2 == 0) {
            int lane = getDynamiteRandomLane();
            switch (lane) {
                case LEFT:
                    game_IMG_dynamites[0][LEFT].setVisibility(View.VISIBLE);
                    break;
                case CENTER:
                    game_IMG_dynamites[0][CENTER].setVisibility(View.VISIBLE);
                    break;
                case RIGHT:
                    game_IMG_dynamites[0][RIGHT].setVisibility(View.VISIBLE);
                    break;
            }
        }
       checkHit();
    }

    private void hideExplosions() {
        game_IMG_explosions[LEFT].setVisibility(View.GONE);
        game_IMG_explosions[CENTER].setVisibility(View.GONE);
        game_IMG_explosions[RIGHT].setVisibility(View.GONE);
        game_IMG_car[carPos].setVisibility(View.VISIBLE);
    }

    private void checkHit() {
        if (game_IMG_dynamites[5][LEFT].getVisibility() == View.VISIBLE
                && game_IMG_car[LEFT].getVisibility() == View.VISIBLE) {
            game_IMG_dynamites[5][LEFT].setVisibility(View.GONE);
            game_IMG_car[LEFT].setVisibility(View.GONE);
            game_IMG_explosions[LEFT].setVisibility(View.VISIBLE);
            toast();
            vibrate();
            game_IMG_lives[lifeCount--].setVisibility(View.INVISIBLE);
        } else if (game_IMG_dynamites[5][CENTER].getVisibility() == View.VISIBLE
                && game_IMG_car[CENTER].getVisibility() == View.VISIBLE) {
            game_IMG_dynamites[5][CENTER].setVisibility(View.GONE);
            game_IMG_car[CENTER].setVisibility(View.GONE);
            game_IMG_explosions[CENTER].setVisibility(View.VISIBLE);
            toast();
            vibrate();
            game_IMG_lives[lifeCount--].setVisibility(View.INVISIBLE);
        } else if (game_IMG_dynamites[5][RIGHT].getVisibility() == View.VISIBLE
                && game_IMG_car[RIGHT].getVisibility() == View.VISIBLE) {
            game_IMG_dynamites[5][RIGHT].setVisibility(View.GONE);
            game_IMG_car[RIGHT].setVisibility(View.GONE);
            game_IMG_explosions[RIGHT].setVisibility(View.VISIBLE);
            toast();
            vibrate();
            game_IMG_lives[lifeCount--].setVisibility(View.INVISIBLE);
        }
        // TODO: What happens after getting hit 3 times.
    }

    private void toast() {
        switch(lifeCount){
            case 2:
                Toast.makeText(this, "2 more lives to go!", Toast.LENGTH_LONG).show();
                break;
            case 1:
                Toast.makeText(this, "LAST LIFE!", Toast.LENGTH_LONG).show();
                break;
            case 0:
                Toast.makeText(this, "##  Restarting Game  ## ", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void restartGame() {
        lifeCount = 2;
        game_IMG_lives[0].setVisibility(View.VISIBLE);
        game_IMG_lives[1].setVisibility(View.VISIBLE);
        game_IMG_lives[2].setVisibility(View.VISIBLE);
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
    }

    private int getDynamiteRandomLane() {
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
        game_IMG_dynamites = new ImageView[6][3];
        game_IMG_explosions = new ImageView[3];
        game_IMG_car = new ImageView[3];
        game_IMG_lives = new ImageView[3];
        game_IMG_dynamites[0][LEFT] = findViewById(R.id.game_IMG_dynamite_left1);
        game_IMG_dynamites[1][LEFT] = findViewById(R.id.game_IMG_dynamite_left2);
        game_IMG_dynamites[2][LEFT] = findViewById(R.id.game_IMG_dynamite_left3);
        game_IMG_dynamites[3][LEFT] = findViewById(R.id.game_IMG_dynamite_left4);
        game_IMG_dynamites[4][LEFT] = findViewById(R.id.game_IMG_dynamite_left5);
        game_IMG_dynamites[5][LEFT] = findViewById(R.id.game_IMG_dynamite_left6);
        game_IMG_dynamites[0][CENTER] = findViewById(R.id.game_IMG_dynamite_center1);
        game_IMG_dynamites[1][CENTER] = findViewById(R.id.game_IMG_dynamite_center2);
        game_IMG_dynamites[2][CENTER] = findViewById(R.id.game_IMG_dynamite_center3);
        game_IMG_dynamites[3][CENTER] = findViewById(R.id.game_IMG_dynamite_center4);
        game_IMG_dynamites[4][CENTER] = findViewById(R.id.game_IMG_dynamite_center5);
        game_IMG_dynamites[5][CENTER] = findViewById(R.id.game_IMG_dynamite_center6);
        game_IMG_dynamites[0][RIGHT] = findViewById(R.id.game_IMG_dynamite_right1);
        game_IMG_dynamites[1][RIGHT] = findViewById(R.id.game_IMG_dynamite_right2);
        game_IMG_dynamites[2][RIGHT] = findViewById(R.id.game_IMG_dynamite_right3);
        game_IMG_dynamites[3][RIGHT] = findViewById(R.id.game_IMG_dynamite_right4);
        game_IMG_dynamites[4][RIGHT] = findViewById(R.id.game_IMG_dynamite_right5);
        game_IMG_dynamites[5][RIGHT] = findViewById(R.id.game_IMG_dynamite_right6);
        game_IMG_explosions[LEFT] = findViewById(R.id.game_IMG_explostion_left);
        game_IMG_explosions[CENTER] = findViewById(R.id.game_IMG_explostion_center);
        game_IMG_explosions[RIGHT] = findViewById(R.id.game_IMG_explostion_right);
        game_IMG_car[LEFT] = findViewById(R.id.game_IMG_car_left);
        game_IMG_car[CENTER] = findViewById(R.id.game_IMG_car_center);
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