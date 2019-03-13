package com.feezrook.Paracart.Pets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.feezrook.Paracart.Pets.direct.HideBar;
import com.feezrook.Paracart.Pets.direct.MusicSoundPlay;
import com.feezrook.Paracart.Pets.direct.RewardedVideo;

import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class activity_game_hearts extends AppCompatActivity {

    private Button startButton, refreshButton, bannerButton;
    private Typeface typeface;
    private ImageView imgMain, img3, img31, img32, img4, img41, img42,img5, img51, img52,img6, img61, img62;
    private ImageView[] a = new ImageView[12];
    private BDRecordAndCard bdRecordAndCard;
    private TextView countStart;
    private static TextView heartsStart;
    private static final String TAG = "LoGs";
    private int countRefreshCard = 0;

    private Boolean soundsGame;



    public static TextView getHeartsStart() {
        return heartsStart;
    }

    @Override
    protected void onStart() {
        super.onStart();
        playAudio();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if ( RewardedVideo.getMreward() != null) {
            RewardedVideo.getMreward().pause(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Boolean internetConnection = hasConnection(this);

        if (internetConnection && RewardedVideo.getMreward() != null && RewardedVideo.getMreward().isLoaded()) {
            if (!PreferencesHelper.isAdsDisabled()) bannerButton.setVisibility(View.VISIBLE);
            if (startButton.getVisibility() == View.INVISIBLE)
                startButton.setVisibility(View.VISIBLE);
        }



        HideBar.hideSystemUI(getWindow().getDecorView());
        orientation();
        if ( RewardedVideo.getMreward() != null) {
            RewardedVideo.getMreward().resume(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if ( RewardedVideo.getMreward() != null) {
            RewardedVideo.getMreward().destroy(this);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent("com.feezrook.Paracart.Pets.MainActivity");
        setButtonSound(soundsGame);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent e) {
        switch(keycode) {
            case KeyEvent.KEYCODE_MENU:
                MusicSoundPlay.stopMusic();
                setButtonSound(soundsGame);
                break;

            case KeyEvent.KEYCODE_POWER:
                MusicSoundPlay.stopMusic();
                setButtonSound(soundsGame);
                break;
        }

        return super.onKeyDown(keycode, e);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_game_hearts);


        soundsGame = getSharedPreferences(Bases.PREFERENCE_FOR_LEVEL, Context.MODE_PRIVATE).getBoolean(Bases.SOUND_PLAY, true);

        bdRecordAndCard = new BDRecordAndCard(getBaseContext());

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.PREFERENCE_FOR_LEVEL), Context.MODE_PRIVATE);
        int levelforsetting = sharedPreferences.getInt(getString(R.string.PREFERENCE_LEVEL), -1);
        if (levelforsetting < 0) {
            refreshCard();
        }

        bdRecordAndCard = new BDRecordAndCard(getBaseContext());
        imgMain = (ImageView) findViewById(R.id.imageView2);
        imgMain.setImageResource(bdRecordAndCard.getNameCardMain(bdRecordAndCard.getIdCard()));

        countStart = (TextView) findViewById(R.id.textView21);
        heartsStart = (TextView) findViewById(R.id.textView24);
        countStart.setText(String.valueOf(sharedPreferences.getInt(Bases.RECORD_FOR_LEVEL, Context.MODE_PRIVATE)));
        heartsStart.setText(String.valueOf(sharedPreferences.getInt(Bases.HEARTS_FOR_GAME, 1)));
        refreshButton = findViewById(R.id.button);

        if (sharedPreferences.getInt(Bases.REFRESH_CARD, 0) == 1) {
            refreshButton.setVisibility(View.GONE);
        }

        int level = (sharedPreferences.getInt(getString(R.string.PREFERENCE_LEVEL), -1) + 1);
        setAlphaImage(level);


        addListenerOnButton();

        homeWatcher();

        playAudio();

        HideBar.hideSystemUI(getWindow().getDecorView());

        if (PreferencesHelper.isAdsDisabled())  {
            bannerButton.setVisibility(View.GONE);
            heartsStart.setText("∞");
        }


    }






    public void setAlphaImage(int level){
        img3 = (ImageView) findViewById(R.id.imageView3); a[0] = img3;
        img31 = (ImageView) findViewById(R.id.imageView31); a[1] = img31;
        img32 = (ImageView) findViewById(R.id.imageView32); a[2] = img32;
        img4 = (ImageView) findViewById(R.id.imageView4); a[3] = img4;
        img41 = (ImageView) findViewById(R.id.imageView41); a[4] = img41;
        img42 = (ImageView) findViewById(R.id.imageView42); a[5] = img42;
        img5 = (ImageView) findViewById(R.id.imageView5); a[6] = img5;
        img51 = (ImageView) findViewById(R.id.imageView51); a[7] = img51;
        img52 = (ImageView) findViewById(R.id.imageView52); a[8] = img52;
        img6 = (ImageView) findViewById(R.id.imageView6); a[9] = img6;
        img61 = (ImageView) findViewById(R.id.imageView7); a[10] = img61;
        img62 = (ImageView) findViewById(R.id.imageView35); a[11] = img62;
        for (int i = 0; i < level-1; i++) {
            a[i].setAlpha(0);
        }
    }


    public void playAudio() {
        Boolean musicGame = getSharedPreferences(Bases.PREFERENCE_FOR_LEVEL, Context.MODE_PRIVATE).getBoolean(Bases.MUSIC_PLAY, true);
        if (!MusicSoundPlay.isplayingAudio && musicGame)  MusicSoundPlay.playMusic(this);
    }



    public static boolean hasConnection(final Context context)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        return false;
    }

    public void advertDownload() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.PREFERENCE_FOR_LEVEL), Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        final int[] advertNumber = {sharedPreferences.getInt(Bases.ADVERT_NUMBER, 0)};

        Log.d(TAG,"1231231241235413241412341234 " + advertNumber[0]);

        Boolean internetConnection = hasConnection(this);
        Boolean advertTrue = false;
        if (RewardedVideo.getMreward().isLoaded() && advertNumber[0] < 2 && internetConnection) {
            RewardedVideo.getMreward().show();
            advertTrue = true;
            MusicSoundPlay.pauseMusic();
            if (!RewardedVideo.getMreward().isLoaded()) bannerButton.setVisibility(View.INVISIBLE);
            if (bannerButton.getVisibility() == View.INVISIBLE) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        bannerButton.setVisibility(View.VISIBLE);
                        if (startButton.getVisibility() == View.INVISIBLE) {
                            startButton.setVisibility(View.VISIBLE);
                        }
                    }

                };
                Handler handler = new Handler();
                handler.postDelayed(runnable, 4000);
            }
        } else {
            if ((advertNumber[0] >= 2) || !internetConnection || !RewardedVideo.getMreward().isLoaded()) {
                Boolean advertOwnNumber = sharedPreferences.getBoolean(Bases.ADVERT_OWN_NUMBER, false);
                Intent intent = new Intent("com.feezrook.Paracart.Pets.activity_advert");
                if (!advertOwnNumber) {
                    intent = new Intent("com.feezrook.Paracart.Pets.activity_advert");
                    edit.putBoolean(Bases.ADVERT_OWN_NUMBER, true);
                } else {
                    intent = new Intent("com.feezrook.Paracart.Pets.OwnAdvertActivity");
                    edit.putBoolean(Bases.ADVERT_OWN_NUMBER, false);
                }

                startActivity(intent);
                MusicSoundPlay.pauseMusic();
                int hearts = sharedPreferences.getInt(Bases.HEARTS_FOR_GAME, 1);
                hearts = hearts + 1;
                edit.putInt(Bases.ADVERT_NUMBER, 0);
                edit.putInt(Bases.HEARTS_FOR_GAME, hearts);
                edit.apply();
                heartsStart.setText(String.valueOf(sharedPreferences.getInt(Bases.HEARTS_FOR_GAME, 1)));
                if (!RewardedVideo.getMreward().isLoaded()) bannerButton.setVisibility(View.INVISIBLE);
                if (bannerButton.getVisibility() == View.INVISIBLE) {
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            bannerButton.setVisibility(View.VISIBLE);
                            if (startButton.getVisibility() == View.INVISIBLE) {
                                startButton.setVisibility(View.VISIBLE);
                            }
                        }

                    };
                    Handler handler = new Handler();
                    handler.postDelayed(runnable, 4000);
                }
            }
        }

    }

    public void addListenerOnButton() {

        startButton = findViewById(R.id.button5);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.PREFERENCE_FOR_LEVEL), Context.MODE_PRIVATE);
        int hearts = sharedPreferences.getInt(Bases.HEARTS_FOR_GAME, 1);

        bannerButton = findViewById(R.id.button3);

        Boolean internetConnection = hasConnection(this);
        if (internetConnection && !RewardedVideo.getMreward().isLoaded()) bannerButton.setVisibility(View.INVISIBLE);
        if (internetConnection && hearts == 0 && !RewardedVideo.getMreward().isLoaded()) startButton.setVisibility(View.INVISIBLE);


        if (bannerButton.getVisibility() == View.INVISIBLE) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    bannerButton.setVisibility(View.VISIBLE);
                    if (startButton.getVisibility() == View.INVISIBLE) {
                        startButton.setVisibility(View.VISIBLE);
                    }
                }

            };
            Handler handler = new Handler();
            handler.postDelayed(runnable, 4000);
        }


        typeface = Typeface.createFromAsset(getAssets(), getString(R.string.font_freakomix));

        startButton.setTypeface(typeface);
        refreshButton.setTypeface(typeface);

        MainActivity mainActivity = new MainActivity();
        bannerButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                            bannerButton.setBackgroundResource(R.drawable.pluspress);
                            //Toast.makeText(activity_game_hearts.this, "" + internetConnection, Toast.LENGTH_LONG).show();
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    bannerButton.setBackgroundResource(R.drawable.plus);

                                    setButtonSound(soundsGame);

                                    advertDownload();
                                }

                            };
                            Handler handler = new Handler();
                            handler.postDelayed(runnable, 50);
                    }
                }
        );


        //Toast.makeText(activity_game_hearts.this, "" + PreferencesHelper.isAdsDisabled(), Toast.LENGTH_LONG).show();

        startButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        startButton.setBackgroundResource(R.drawable.playpress);
                        int hearts = sharedPreferences.getInt(Bases.HEARTS_FOR_GAME, 1);

                        setButtonSound(soundsGame);
                        if (hearts == 0) {
                            addHearts();
                        } else {
                            Intent intent = new Intent("com.feezrook.Paracart.Pets.GameActivity");
                            SharedPreferences.Editor edit = sharedPreferences.edit();
                            if (!PreferencesHelper.isAdsDisabled())  {
                                hearts = hearts - 1;
                            }
                            edit.putInt(Bases.HEARTS_FOR_GAME, hearts);
                            edit.apply();
                            startActivity(intent);
                        }
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                startButton.setBackgroundResource(R.drawable.play);
                            }
                        };
                        Handler handler = new Handler();
                        handler.postDelayed(runnable, 50);

                    }
                }
        );

        refreshButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {

                        refreshButton.setBackgroundResource(R.drawable.changecardpress);
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                refreshButton.setBackgroundResource(R.drawable.changecard);
                                refreshCard();
                                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.PREFERENCE_FOR_LEVEL), Context.MODE_PRIVATE);
                                countRefreshCard = 1;
                                SharedPreferences.Editor edit = sharedPreferences.edit();
                                edit.putInt(Bases.REFRESH_CARD, countRefreshCard);
                                edit.apply();
                                imgMain.setImageResource(bdRecordAndCard.getIdCard());
                                countStart.setText(String.valueOf(sharedPreferences.getInt(Bases.RECORD_FOR_LEVEL, Context.MODE_PRIVATE)));
                                if (PreferencesHelper.isAdsDisabled())  {
                                    heartsStart.setText("∞");
                                } else heartsStart.setText(String.valueOf(sharedPreferences.getInt(Bases.HEARTS_FOR_GAME, 1)));

                                setButtonSound(soundsGame);
                                refreshButton.setVisibility(View.GONE);
                                setAlphaImage(0);
                            }
                        };
                        Handler handler = new Handler();
                        handler.postDelayed(runnable, 50);
                    }
                }
        );

    }



    public void addHearts() {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View iview = (View) inflater.inflate(R.layout.dialog_win, null);

        typeface = Typeface.createFromAsset(getAssets(), getString(R.string.font_freakomix));

        Button button = iview.findViewById(R.id.startButton);
        button.setTypeface(typeface);

        TextView text13 = iview.findViewById(R.id.textView13);
        text13.setTypeface(typeface);

        TextView text14 = iview.findViewById(R.id.textView14);
        text14.setTypeface(typeface);

        TextView text18 = iview.findViewById(R.id.textView18);
        text18.setTypeface(typeface);

        TextView text20 = iview.findViewById(R.id.textView20);
        text20.setTypeface(typeface);

        TextView text17 = iview.findViewById(R.id.textView17);
        text17.setTypeface(typeface);

        TextView text19 = iview.findViewById(R.id.textView19);
        text19.setTypeface(typeface);

        TextView text171 = iview.findViewById(R.id.textView171);

        TextView text181 = iview.findViewById(R.id.textView181);

        TextView text191 = iview.findViewById(R.id.textView191);

        TextView text172 = iview.findViewById(R.id.textView172);

        TextView text182 = iview.findViewById(R.id.textView182);

        TextView text192 = iview.findViewById(R.id.textView192);

        text14.setVisibility(View.GONE);
        text18.setVisibility(View.GONE);
        text20.setVisibility(View.GONE);
        text17.setVisibility(View.GONE);
        text19.setVisibility(View.GONE);
        text171.setVisibility(View.GONE);
        text181.setVisibility(View.GONE);
        text191.setVisibility(View.GONE);
        text172.setVisibility(View.GONE);
        text182.setVisibility(View.GONE);
        text192.setVisibility(View.GONE);
        button.setText("Ок");
        text13.setText("Запас жизней пополнится после рекламы");
        alertbox.setCancelable(false);


        alertbox.setView(iview);

        AlertDialog dialog = alertbox.create();
        dialog.show();
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //closing this Activity

                        advertDownload();
                        dialog.cancel();
                    }
                }
        );
    }

    public void orientation() {
        SharedPreferences sPref = getSharedPreferences(Bases.PREFERENCE_FOR_LEVEL, Context.MODE_PRIVATE);
        Boolean savedText = sPref.getBoolean(getString(R.string.ORIENT_SETT_SAVED), true);
        //Toast.makeText(MainActivity.this, savedText, Toast.LENGTH_LONG).show();

        if (!savedText) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        if (savedText) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

    }

    public void refreshCard() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.PREFERENCE_FOR_LEVEL), Context.MODE_PRIVATE);
        int levelforsetting = sharedPreferences.getInt(getString(R.string.PREFERENCE_LEVEL), -1);
        bdRecordAndCard.refreshCard();
        levelforsetting = 0;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        if (levelforsetting > SettingLevel.getArrayLevels().size()-1) {
            Log.d(TAG, "Level > max level");
            levelforsetting %= SettingLevel.getArrayLevels().size(); //SettingLevel.getArrayLevels().size() -> max level
        }
        edit.putInt(getString(R.string.PREFERENCE_LEVEL), levelforsetting);
        edit.apply();
        edit.putInt(getString(R.string.RECORD_FOR_LEVEL), 0);
        edit.apply();
        if (!PreferencesHelper.isAdsDisabled())  {
            edit.putInt(Bases.HEARTS_FOR_GAME, 1);
            edit.apply();
        }
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

    public void setButtonSound(Boolean soundsGame) {
        if (soundsGame) MusicSoundPlay.setButtonPlay(this);
    }
}
