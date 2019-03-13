package com.feezrook.Paracart.Pets;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.BoringLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.feezrook.Paracart.Pets.direct.HideBar;
import com.feezrook.Paracart.Pets.direct.MusicSoundPlay;
import com.feezrook.Paracart.Pets.direct.RewardedVideo;
import com.feezrook.Paracart.Pets.util.IabHelper;
import com.feezrook.Paracart.Pets.util.IabResult;
import com.feezrook.Paracart.Pets.util.Inventory;
import com.feezrook.Paracart.Pets.util.Purchase;

public class MainActivity extends AppCompatActivity {//класс для обработки кнопок на главном меню

    private Button startButton, settingButton, recordButton, myRoomButton;
    private Typeface typeface;
    private MediaPlayer buttonSound, soundGame, albumMusic;
    private Context context;
    private static final String TAG = "LoGs";
    private Boolean soundsGame;

    IabHelper mHelper;
    static final String ITEM_SKU = "com.feezrook.livesunlimited";
    private String base64EncodedPublicKey =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAijdU5k8MHqTgdoiNnRrj+Kf3hcKDasF82fwkY8WIBBcwK7IPsnV/sv9iTqgrA3+EPCzfXJAlKO9Eq6x0jp7boCzwGelPiYt1OXh4rdvk/Vo8nxQiSlfhLKx/ILSiwb1A8zpEivJYRiigZJ6aSGlVV+UC38VJJnXSzem0YkOe8yQtTlIBJcw2hBRjDmgPmILhi97zcfS977trCcm5uhqPZoeB79mUAE/DW55fDhupXrADvH0wRJGgbM3yVt/It4T+d8pe/uf83ICeUpKszrjY83u3fGAEgbW1j6WxJz/AXAYiWQfWNhZeyzjOvdspLt8yYclFQ2R4eOX7dZzX1OoDNQIDAQAB";


    private void billingInit() {
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        // включаем дебагинг (в релизной версии ОБЯЗАТЕЛЬНО выставьте в false)
        mHelper.enableDebugLogging(true);

        // инициализируем; запрос асинхронен
        // будет вызван, когда инициализация завершится
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result)  {
                if (!result.isSuccess()) {
                    return;
                }
                // чекаем уже купленное
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });
    }

    // Слушатель для востановителя покупок.
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {

        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");
            if (result.isFailure()) {
                Log.d(TAG, "Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

			/*
			 * Проверяются покупки.
			 * Обратите внимание, что надо проверить каждую покупку, чтобы убедиться, что всё норм!
			 * см. verifyDeveloperPayload().
			 */

            Purchase purchase = inventory.getPurchase(ITEM_SKU);
            PreferencesHelper.savePurchase(
                    context,
                    PreferencesHelper.Purchase.DISABLE_ADS,
                    purchase != null && verifyDeveloperPayload(purchase));

            //ads.show(!PreferencesHelper.isAdsDisabled());

        }
    };
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();
		/*
		 * TODO: здесь необходимо свою верификацию реализовать
		 * Хорошо бы ещё с использованием собственного стороннего сервера.
		 */

        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (RewardedVideo.getMreward() == null || !RewardedVideo.getMreward().isLoaded()) {
            RewardedVideo rewardedVideo = new RewardedVideo(getBaseContext());
            rewardedVideo.onCreateRewarded();
        }


        //Create variables for game
        soundsGame = getSharedPreferences(Bases.PREFERENCE_FOR_LEVEL, Context.MODE_PRIVATE).getBoolean(Bases.SOUND_PLAY, true);

        Log.d(TAG, "CREATE");
        setContentView(R.layout.activity_main);
        context = this;
        HideBar.hideSystemUI(getWindow().getDecorView());
        //скрывает бар вверху окна


        billingInit();

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.PREFERENCE_FOR_LEVEL), Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        if (PreferencesHelper.isAdsDisabled())  {
            int hearts = sharedPreferences.getInt(Bases.HEARTS_FOR_GAME, 1);
            hearts = 999999999;
            edit.putBoolean(Bases.HEARTS_UNLIM, true);
            edit.putInt(Bases.HEARTS_FOR_GAME, hearts);
            edit.apply();
        };


        addListenerOnButton();
        orientation();


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
    public boolean onKeyDown(int keycode, KeyEvent e) {
        switch(keycode) {
            case KeyEvent.KEYCODE_MENU:
                MusicSoundPlay.stopMusic();
                break;
            case KeyEvent.KEYCODE_BACK:
                Intent intent = new Intent("com.feezrook.Paracart.Pets.MainActivity");
                startActivity(intent);
                break;


        }
        if (e.getKeyCode() == KeyEvent.KEYCODE_POWER) {
            MusicSoundPlay.stopMusic();
            e.startTracking(); // Needed to track long presses
            return true;
        }
        return super.onKeyDown(keycode, e);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_POWER) {
            MusicSoundPlay.stopMusic();
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        orientation();
        HideBar.hideSystemUI(getWindow().getDecorView());
    }

    @Override
    protected void onStart() {
        super.onStart();

        //RewardedVideo.onCreateRewarded(this);

        startButton.setShadowLayer(10,5,10, R.color.colorBlack);
        myRoomButton.setShadowLayer(10,5,10, R.color.colorBlack);
        recordButton.setShadowLayer(10,5,10, R.color.colorBlack);
        settingButton.setShadowLayer(10,5,10, R.color.colorBlack);

        myRoomButton.setTextSize(38);
        settingButton.setTextSize(38);
        recordButton.setTextSize(38);

        playAudio();
        orientation();
    }




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


    public void addListenerOnButton() {

        startButton = findViewById(R.id.startButton);
        myRoomButton = findViewById(R.id.myRoomButton);
        recordButton = findViewById(R.id.recordButton);
        settingButton = findViewById(R.id.settingButton);

        //sound in Game
        buttonSound = MediaPlayer.create(this, R.raw.generalclick);
        soundGame = MediaPlayer.create(this, R.raw.soundgame);

        typeface = Typeface.createFromAsset(getAssets(), getString(R.string.font_freakomix));

        startButton.setTypeface(typeface);
        myRoomButton.setTypeface(typeface);
        settingButton.setTypeface(typeface);
        recordButton.setTypeface(typeface);

        playAudio();

        startButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        startButton.setShadowLayer(0,0,0, R.color.colorWhite);

                        Intent intent = new Intent("com.feezrook.Paracart.Pets.activity_game_hearts");
                        setButtonSound(soundsGame);

                        startActivity(intent);

                    }
                }
        );

        myRoomButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        myRoomButton.setShadowLayer(0,0,0, R.color.colorWhite);
                        myRoomButton.setTextSize(36);
                        Intent intent = new Intent("com.feezrook.Paracart.Pets.RoomActivity");
                        setButtonSound(soundsGame);
                        startActivity(intent);

                    }
                }
        );

        recordButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        recordButton.setShadowLayer(0,0,0, R.color.colorWhite);
                        recordButton.setTextSize(36);

                        /*
                        Locale locale = new Locale("en");
                        Locale.setDefault(locale);
                        Configuration configuration = new Configuration();
                        configuration.locale = locale;
                        getBaseContext().getResources().updateConfiguration(configuration, null);
                        recreate();
                        */

                        Intent intent = new Intent("com.feezrook.Paracart.Pets.RecordActivity");
                        setButtonSound(soundsGame);
                        startActivity(intent);

                    }
                }
        );

        settingButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        settingButton.setShadowLayer(0,0,0, R.color.colorWhite);
                        settingButton.setTextSize(36);
                        Intent intent = new Intent("com.feezrook.Paracart.Pets.SettingActivity");

                        setButtonSound(soundsGame);
                        startActivity(intent);

                    }
                }
        );



    }

    public void playAudio() {
        Boolean musicGame = getSharedPreferences(Bases.PREFERENCE_FOR_LEVEL, Context.MODE_PRIVATE).getBoolean(Bases.MUSIC_PLAY, true);
        if (!MusicSoundPlay.isplayingAudio && musicGame)  MusicSoundPlay.playMusic(this);
    }
    public void setButtonSound(Boolean soundsGame) {
        if (soundsGame) MusicSoundPlay.setButtonPlay(this);
    }

}
