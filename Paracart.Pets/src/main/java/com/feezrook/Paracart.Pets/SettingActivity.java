package com.feezrook.Paracart.Pets;

/**
 * Created by DIO on 01.11.2017.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.feezrook.Paracart.Pets.direct.HideBar;
import com.feezrook.Paracart.Pets.direct.MusicSoundPlay;

public class SettingActivity extends AppCompatActivity //PreferenceActivity
{
    private TextView buferTextView;
    private ImageButton landscapeButtonPort, landscapeButtonLand, soundOffView, soundOnView, musicOffView, musicOnView;

    private Boolean soundsGame;



    @Override
    public void onBackPressed() {
        Intent intent = new Intent("com.feezrook.Paracart.Pets.MainActivity");
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        soundsGame = getSharedPreferences(Bases.PREFERENCE_FOR_LEVEL, Context.MODE_PRIVATE).getBoolean(Bases.SOUND_PLAY, true);

        HideBar.hideSystemUI(getWindow().getDecorView());
        addListenerOnButton();
        homeWatcher();
    }

    public void homeWatcher() {
        HomeWatcher mHomeWatcher = new HomeWatcher(this) {
            @Override
            public void onHomePressed() {
                MusicSoundPlay.stopMusic();
            }

            @Override
            public void onHomeLongPressed() {
                MusicSoundPlay.stopMusic();
            }
        };
        mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                MusicSoundPlay.stopMusic();
            }
            @Override
            public void onHomeLongPressed() {
                MusicSoundPlay.stopMusic();
            }
        });
        mHomeWatcher.startWatch();
    }

    @Override
    protected void onResume() {
        super.onResume();
        HideBar.hideSystemUI(getWindow().getDecorView());
        orient();
    }

    @Override
    protected void onStart() {
        super.onStart();
        playAudio();
    }

    public void playAudio() {
        Boolean musicGame = getSharedPreferences(Bases.PREFERENCE_FOR_LEVEL, Context.MODE_PRIVATE).getBoolean(Bases.MUSIC_PLAY, true);
        if (!MusicSoundPlay.isplayingAudio && musicGame)  MusicSoundPlay.playMusic(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void orient() {
        SharedPreferences sPref = getSharedPreferences(Bases.PREFERENCE_FOR_LEVEL, Context.MODE_PRIVATE);
        Boolean savedText = sPref.getBoolean(getString(R.string.ORIENT_SETT_SAVED), true);

        Boolean musicGame = getSharedPreferences(Bases.PREFERENCE_FOR_LEVEL, Context.MODE_PRIVATE).getBoolean(Bases.MUSIC_PLAY, true);

        if (musicGame) {
            musicOnView.setImageResource(R.drawable.musicontrue);
            musicOffView.setImageResource(R.drawable.musicofffalse);
        }   else {
            soundOnView.setImageResource(R.drawable.musiconfalse);
            soundOffView.setImageResource(R.drawable.musicofftrue);
        }

        if (!savedText) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            landscapeButtonPort.setImageResource(R.drawable.portfalse);
            landscapeButtonLand.setImageResource(R.drawable.landtrue);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            landscapeButtonPort.setImageResource(R.drawable.porttrue);
            landscapeButtonLand.setImageResource(R.drawable.landfalse);
        }

        if (soundsGame) {
            soundOnView.setImageResource(R.drawable.soundontrue);
            soundOffView.setImageResource(R.drawable.soundofffalse);
        }   else {
            soundOnView.setImageResource(R.drawable.soundonfalse);
            soundOffView.setImageResource(R.drawable.soundofftrue);
        }


    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent e) {
        switch(keycode) {

            case KeyEvent.KEYCODE_MENU:
                MusicSoundPlay.stopMusic();
                break;

            case KeyEvent.KEYCODE_POWER:
                MusicSoundPlay.stopMusic();
                break;
        }

        return super.onKeyDown(keycode, e);
    }


    public void addListenerOnButton() {
        buferTextView = findViewById(R.id.languageText);

        Typeface tf = Typeface.createFromAsset(getAssets(), getString(R.string.font_freakomix));
        buferTextView.setTypeface(tf);
        buferTextView = findViewById(R.id.soundMusicText);
        buferTextView.setTypeface(tf);
        buferTextView = findViewById(R.id.sound);
        buferTextView.setTypeface(tf);
        buferTextView = findViewById(R.id.musicText);
        buferTextView.setTypeface(tf);
        buferTextView = findViewById(R.id.landscapeText);
        buferTextView.setTypeface(tf);

        landscapeButtonPort = findViewById(R.id.portonButton);
        landscapeButtonLand = findViewById(R.id.landoffButt);

        SharedPreferences sPref = getSharedPreferences(Bases.PREFERENCE_FOR_LEVEL, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();

        Boolean soundsGame = getSharedPreferences(Bases.PREFERENCE_FOR_LEVEL, Context.MODE_PRIVATE).getBoolean(Bases.SOUND_PLAY, true);
        MainActivity mainActivity = new MainActivity();

        landscapeButtonPort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setLandscape(true);
                ed.putBoolean(getString(R.string.ORIENT_SETT_SAVED), true);
                ed.apply();
                setButtonSound(soundsGame);
                landscapeButtonPort.setImageResource(R.drawable.porttrue);
                landscapeButtonLand.setImageResource(R.drawable.landfalse);
            }
        });

        landscapeButtonLand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setLandscape(false);
                setButtonSound(soundsGame);
                ed.putBoolean(getString(R.string.ORIENT_SETT_SAVED), false);
                ed.apply();
                landscapeButtonPort.setImageResource(R.drawable.portfalse);
                landscapeButtonLand.setImageResource(R.drawable.landtrue);
            }
        });

        soundOffView = findViewById(R.id.soundoffButt);
        soundOnView = findViewById(R.id.soundonButton);

        musicOffView = findViewById(R.id.musicoffButt);
        musicOnView = findViewById(R.id.musiconButton);

        musicOnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicOnView.setImageResource(R.drawable.musicontrue);
                musicOffView.setImageResource(R.drawable.musicofffalse);
                playAudio();
                setButtonSound(soundsGame);
                ed.putBoolean(Bases.MUSIC_PLAY, true);
                ed.apply();
            }
        });

        musicOffView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicOnView.setImageResource(R.drawable.musiconfalse);
                musicOffView.setImageResource(R.drawable.musicofftrue);
                MusicSoundPlay.stopMusic();
                setButtonSound(soundsGame);
                ed.putBoolean(Bases.MUSIC_PLAY, false);
                ed.apply();
            }
        });

        soundOnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundOnView.setImageResource(R.drawable.soundontrue);
                soundOffView.setImageResource(R.drawable.soundofffalse);
                setButtonSound(soundsGame);
                ed.putBoolean(Bases.SOUND_PLAY, true);
                ed.apply();
            }
        });

        soundOffView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundOnView.setImageResource(R.drawable.soundonfalse);
                soundOffView.setImageResource(R.drawable.soundofftrue);
                setButtonSound(soundsGame);
                ed.putBoolean(Bases.SOUND_PLAY, false);
                ed.apply();

            }
        });

    }

    public void setButtonSound(Boolean soundsGame) {
        if (soundsGame) MusicSoundPlay.setButtonPlay(this);
    }
}