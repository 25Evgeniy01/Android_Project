package com.feezrook.Paracart.Pets;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.feezrook.Paracart.Pets.direct.RewardedVideo;

public class SplashActivity extends AppCompatActivity { //fizrook перед игрой
    private ImageView fizrukSplash, noneFlash;

    public void orientation() {
        SharedPreferences sPref = getSharedPreferences(Bases.PREFERENCE_FOR_LEVEL, Context.MODE_PRIVATE);
        Boolean savedText = sPref.getBoolean(getString(R.string.ORIENT_SETT_SAVED), true);
        //Toast.makeText(MainActivity.this, savedText, Toast.LENGTH_LONG).show();

        if (savedText == false) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        if (savedText == true) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        orientation();
        fizrukSplash = findViewById(R.id.feezrooksplash);
        noneFlash = findViewById(R.id.feezrooksplash);
        Display display1 = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display1.getSize(size);

        fizrukSplash.setLayoutParams(new ConstraintLayout.LayoutParams(size.x, size.x/2));

        //второй поток
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,
                        MainActivity.class));
                SplashActivity.this.finish();
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable, 3000);
    }
}
