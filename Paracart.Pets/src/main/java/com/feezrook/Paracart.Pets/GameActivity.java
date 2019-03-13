package com.feezrook.Paracart.Pets;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feezrook.Paracart.Pets.direct.HideBar;
import com.feezrook.Paracart.Pets.direct.MusicSoundPlay;
import com.feezrook.Paracart.Pets.direct.RewardedVideo;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

public class GameActivity extends Activity { //основа игры

    private InterstitialAd mInterstitialAd;

    private MediaPlayer buttonSound;
    private MediaPlayer startSound;
    private MediaPlayer finishSound;
    private MediaPlayer loseSound;
    private MediaPlayer bingoSound ;
    private Typeface typeface;

    private MainActivity mainActivity;

    private Boolean soundsGame;
    private GridView mGrid;
    private GridAdapter mAdapter;
    private int mCOLS;
    private int mROWS;
    private int level;
    private int recordBingo = 0;

    private TextView mStepScreen, mStep, mCountScreen, mCount;//
    private TextView mTimeScreen, mTime;
    private CountDownTimer countDownTimer;
    private BDRecordAndCard BDRecordAndCard;
    private View mDecorView;
    private LinearLayout myHendlerBar;

    private Integer stepCount = 0; // кол-во ходов
    private int[] changeStepCount;
    private int TIMEFORLEVEL;//for Lose
    private int MAXSTEPFORLAVEL;
    private int COUNTCELFORSHOW;
    private int sizePicture; //for GridAdapter

    private Boolean timerOn = false;//for start chronometer

    private Handler handlerCheck = new Handler(); //for paused close
    private Runnable runnableCheck;

    private int[] arrayOfRandomShow; //to show random cell
    private static final String TAG = "LoGs";

    private static MediaPlayer gameOverPlay, winPlay, bingoPlay, timePlay, buttonPlay, startPlay;
    private static int gameOver = R.raw.airballoon, win = R.raw.yeahchildren, bingo = R.raw.bingo, timeFinish = R.raw.littlebells, button = R.raw.generalclick, startGame = R.raw.start;


    protected void onResume() {
        super.onResume();
        HideBar.hideSystemUI(getWindow().getDecorView());
        orientation();
    }
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent("com.feezrook.Paracart.Pets.activity_game_hearts");
        countDownTimer.cancel();
        startActivity(intent);
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


    AdapterView.OnItemClickListener oclItem = new AdapterView.OnItemClickListener() { //Listener
        @Override
        public void onItemClick(AdapterView<?> parent, View v,int position, long id) { //event click

            try {
                handlerCheck.removeCallbacks(runnableCheck);
            } catch (Exception e) {
                Log.d(TAG, "EXCEPTION:handlerCheck.removeCallbacks(runnableCheck)");
            }

            //таймер
            if (!timerOn) { //check timer on
                countDownTimer.start();
                timerOn = true;
            }

            changeStepCount = mAdapter.checkOpenCells();
            stepCount += changeStepCount[0];
            mStepScreen.setText(String.valueOf(MAXSTEPFORLAVEL - stepCount)); //count Step

            checkLeftStep();

            if (mAdapter.openCell (position)) setButtonSound(soundsGame);

            runnableCheck = new Runnable() {
                public void run() {
                    changeStepCount = mAdapter.checkOpenCells();

                    //mAdapter.notifyDataSetChanged();//i add this in checkOpenCells

                    stepCount += changeStepCount[0];
                    if (changeStepCount[1] == 1) {

                        setBingoSound(soundsGame);

                        recordBingo = recordBingo + 20;
                        mCountScreen.setText(String.valueOf(recordBingo));
                    }
                    mStepScreen.setText(String.valueOf(MAXSTEPFORLAVEL - stepCount)); //count Step

                    //проверка выиграл ли игрок
                    if (mAdapter.checkGameOver())
                    {
                        ShowGameOver(true);
                        try { //with out this we get lose when win on last step (max level) when second treat finish because checkLeftStep(); do showGameOver(false)
                            handlerCheck.removeCallbacks(runnableCheck);
                        } catch (Exception e) {
                            Log.d(TAG, "EXCEPTION:handlerCheck.removeCallbacks(runnableCheck)");
                        }
                    } else checkLeftStep();
                }
            };

            handlerCheck.postDelayed(runnableCheck, 250); //Close cell with pause*/
        }
    };

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

    private AdListener adListener = new AdListener() {
        @Override
        public void onAdLoaded() {
            // Code to be executed when an ad finishes loading.
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            // Code to be executed when an ad request fails.
        }

        @Override
        public void onAdOpened() {
            // Code to be executed when the ad is displayed.
        }

        @Override
        public void onAdLeftApplication() {
            // Code to be executed when the user has left the app.
        }

        @Override
        public void onAdClosed() {
            Intent intent = new Intent("com.feezrook.Paracart.Pets.activity_game_hearts");
            startActivity(intent);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) { //MAIN
        super.onCreate(savedInstanceState);

        gameOverPlay = MediaPlayer.create(this, gameOver);
        winPlay = MediaPlayer.create(this, win);
        bingoPlay = MediaPlayer.create(this, bingo);
        timePlay = MediaPlayer.create(this, timeFinish);
        buttonPlay = MediaPlayer.create(this, button);
        startPlay = MediaPlayer.create(this, startGame);

        if (RewardedVideo.getMreward() == null || !RewardedVideo.getMreward().isLoaded()) {
            RewardedVideo rewardedVideo = new RewardedVideo(getBaseContext());
            rewardedVideo.onCreateRewarded();
        }

        BDRecordAndCard = new BDRecordAndCard(getBaseContext());
        mDecorView = getWindow().getDecorView();
        HideBar.hideSystemUI(getWindow().getDecorView());

        setLevelSetting(getLevel()); // setLevelSetting
        level = getLevel(); //setLevel get rows,cols,MAXTIME,MAXSTEP...
        soundsGame = getSharedPreferences(Bases.PREFERENCE_FOR_LEVEL, Context.MODE_PRIVATE).getBoolean(Bases.SOUND_PLAY, true);
        mainActivity = new MainActivity();
        changeOrientation();//orientation & //switch row and col if need


        if (level == 3 || level == 7) {
            MobileAds.initialize(this,
                    "ca-app-pub-8799270726987439~7438704571");
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdListener(adListener);
            mInterstitialAd.setAdUnitId("ca-app-pub-8799270726987439/1390059131");
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }

        afterStart();//Dialog window

        setContentView(R.layout.activity_game);



        mTimeScreen = (TextView) findViewById(R.id.timeview);
        mStepScreen = (TextView)findViewById(R.id.stepview);
        mCountScreen = (TextView) findViewById(R.id.countview);
        mStep = (TextView)findViewById(R.id.step);
        mTime = (TextView)findViewById(R.id.time);
        mCount = (TextView)findViewById(R.id.count);
        mGrid = (GridView)findViewById(R.id.field);
        if (level == 0 && getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mGrid.setPadding(0,0,0,0);
        }

        //mGrid.setBackgroundResource(BDRecordAndCard.getIdSubCard(level));
        //mGrid.setBackgroundResource(BDRecordAndCard.getIdCard());
        mGrid.setNumColumns(mCOLS);
        //mGrid.setEnabled(true);

        typeface = Typeface.createFromAsset(getAssets(), getString(R.string.font_freakomix));

        mStepScreen.setTypeface(typeface);
        mTimeScreen.setTypeface(typeface);
        mStep.setTypeface(typeface);
        mTime.setTypeface(typeface);
        mCountScreen.setTypeface(typeface);
        mCount.setTypeface(typeface);

        mCountScreen.setText(String.valueOf(recordBingo));
        mTimeScreen.setText(String.valueOf(TIMEFORLEVEL/1000));

        if (MAXSTEPFORLAVEL >= 999 && TIMEFORLEVEL/1000 >= 999) {
            mTimeScreen.setVisibility(View.GONE);
            mStepScreen.setVisibility(View.GONE);
            mTime.setVisibility(View.GONE);
            mStep.setVisibility(View.GONE);
        }
        if (MAXSTEPFORLAVEL >= 999 ) {
            mStepScreen.setVisibility(View.GONE);
            mStep.setVisibility(View.GONE);
        }
        if (TIMEFORLEVEL/1000 >= 999) {
            mTimeScreen.setVisibility(View.GONE);
            mTime.setVisibility(View.GONE);
        }


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

       playAudio();

        countDownTimer = new CountDownTimer(TIMEFORLEVEL, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeScreen.setText(String.valueOf(millisUntilFinished/1000));
                if (millisUntilFinished/1000 == 6) {
                    setTimeSound(soundsGame);
                }
                            }

            @Override
            public void onFinish() {
                mTimeScreen.setText("0");

                    ShowGameOver(false);


            }
        };

        mStepScreen.setText (String.valueOf(MAXSTEPFORLAVEL - stepCount));

        mAdapter = new GridAdapter(this, mCOLS, mROWS, sizePicture);
        mGrid.setAdapter(mAdapter);
        mAdapter.konfettiView = findViewById(R.id.konfeti);

    }

    private void alertAddMark () {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.PREFERENCE_FOR_LEVEL), Context.MODE_PRIVATE);
        Boolean addMark1 = sharedPreferences.getBoolean("addMark", false);
        if (!addMark1) {
            // Диалоговое окно
            AlertDialog.Builder alertbox = new AlertDialog.Builder(this);

            LayoutInflater inflater = this.getLayoutInflater();
            final View iview = (View) inflater.inflate(R.layout.addmark, null);

            typeface = Typeface.createFromAsset(getAssets(), getString(R.string.font_freakomix));

            Button yesButton = iview.findViewById(R.id.yesButton);
            yesButton.setTypeface(typeface);

            Button noButton = iview.findViewById(R.id.noButton);
            noButton.setTypeface(typeface);

            TextView text13 = iview.findViewById(R.id.textView13);
            text13.setTypeface(typeface);

            alertbox.setCancelable(false);

            alertbox.setView(iview);

            AlertDialog dialog = alertbox.create();
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);

            yesButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            yesButton.setShadowLayer(0,0,0, R.color.colorWhite);
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.feezrook.Paracart.Pets")));
                            SharedPreferences.Editor edit = sharedPreferences.edit();
                            edit.putBoolean("addMark", true);
                            edit.apply();
                        }
                    }
            );

            noButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            noButton.setShadowLayer(0,0,0, R.color.colorWhite);
                            Intent intent = new Intent("com.feezrook.Paracart.Pets.activity_game_hearts");
                            startActivity(intent);
                        }
                    }
            );
        }
    }

    private void ShowGameOver (boolean win) {

        // Диалоговое окно
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
        text171.setTypeface(typeface);

        TextView text181 = iview.findViewById(R.id.textView181);
        text181.setTypeface(typeface);

        TextView text191 = iview.findViewById(R.id.textView191);
        text191.setTypeface(typeface);

        TextView text172 = iview.findViewById(R.id.textView172);
        text172.setTypeface(typeface);

        TextView text182 = iview.findViewById(R.id.textView182);
        text182.setTypeface(typeface);

        TextView text192 = iview.findViewById(R.id.textView192);
        text192.setTypeface(typeface);

        String time = mTimeScreen.getText().toString();

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.PREFERENCE_FOR_LEVEL), Context.MODE_PRIVATE);
        int hearts = sharedPreferences.getInt(Bases.HEARTS_FOR_GAME, 1);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        int numberGame = sharedPreferences.getInt(Bases.NUMBER_GAME, 0);

        Boolean finalGame = false;
        Boolean winThisGame = false;
        // Header + text
        if (win) {
            int winScore = scoreCalc(Integer.parseInt(time),MAXSTEPFORLAVEL-stepCount, mAdapter.getCount());
            if ((winScore - recordBingo) < 10) {
                winScore = winScore + 10;
            }
            if (level == 11) {
                finalGame = true;
                winThisGame = true;
                text17.setText(String.valueOf(recordBingo));
                text181.setText(String.valueOf(winScore-recordBingo));
                text182.setText(String.valueOf(winScore));
                text19.setText(String.valueOf(getRecordSublevel()));
                changeRecordSublevel(winScore);
                BDRecordAndCard.saveNewCard();
                setLevel(-1);
                BDRecordAndCard.putGlobalRecord(getRecordSublevel(), "card");
                clearRecordSublevel();
                edit.putInt(Bases.NUMBER_GAME, 0);
                edit.apply();
                hearts = hearts + 1;
                edit.putInt(Bases.REFRESH_CARD, 0);
                edit.apply();
                edit.putInt(Bases.HEARTS_FOR_GAME, hearts);
                edit.apply();
            } else {
                winThisGame = true;
                changeRecordSublevel(winScore);
                text17.setText(String.valueOf(recordBingo));
                text181.setText(String.valueOf(winScore-recordBingo));
                text182.setText(String.valueOf(winScore));
                text19.setText(String.valueOf(getRecordSublevel()));
                setLevel(level+1);
                hearts = hearts + 1;
                edit.putInt(Bases.NUMBER_GAME, 0);
                edit.apply();
                edit.putInt(Bases.HEARTS_FOR_GAME, hearts);
                edit.apply();
            }
            setFinishSound(soundsGame);

        } else {
            text13.setText("Ой!");
            text14.setText("Вы проиграли");
            text17.setVisibility(View.GONE);
            text18.setVisibility(View.GONE);
            text19.setVisibility(View.GONE);
            text20.setVisibility(View.GONE);
            text171.setVisibility(View.GONE);
            text181.setVisibility(View.GONE);
            text191.setVisibility(View.GONE);
            text172.setVisibility(View.GONE);
            text182.setVisibility(View.GONE);
            text192.setVisibility(View.GONE);

            numberGame = numberGame + 1;
            edit.putInt(Bases.NUMBER_GAME, numberGame);
            edit.apply();


            setLoseSound(soundsGame);

            /*
            hearts = hearts - 1;
            edit.putInt(Bases.HEARTS_FOR_GAME, hearts);
            edit.apply();
            */
        }

        alertbox.setCancelable(false);


        Boolean finalGame1 = finalGame;

        alertbox.setView(iview);

        AlertDialog dialog = alertbox.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

        Boolean finalWinThisGame = winThisGame;
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button.setShadowLayer(0,0,0, R.color.colorWhite);
                        if (!finalGame1) {
                            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.PREFERENCE_FOR_LEVEL), Context.MODE_PRIVATE);
                            Boolean addMark1 = sharedPreferences.getBoolean("addMark", false);

                            if (mInterstitialAd != null && mInterstitialAd.isLoaded() && finalWinThisGame) {
                                if (level == 7 && !addMark1) alertAddMark(); else  mInterstitialAd.show();
                            } else {
                                    Intent intent = new Intent("com.feezrook.Paracart.Pets.activity_game_hearts");
                                    startActivity(intent);
                            }



                        } else {

                            if (mInterstitialAd != null && mInterstitialAd.isLoaded() && finalWinThisGame) {
                                mInterstitialAd.show();
                            } else {
                                Intent intent = new Intent("com.feezrook.Paracart.Pets.RoomActivity");
                                startActivity(intent);
                            }

                        }
                    }
                }
        );

        countDownTimer.cancel();
    }

    private void checkLeftStep() { ///for lose after last step

        if (stepCount >= MAXSTEPFORLAVEL) {
            ShowGameOver(false);
        }

    }

    private boolean portraitOrientation() {
        SharedPreferences sharedPreferences  = getSharedPreferences(Bases.PREFERENCE_FOR_LEVEL, Context.MODE_PRIVATE);
        Boolean orientation;

            orientation = true;
        try {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                orientation = true;
            }
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                orientation = false;
            }

        } catch (Exception e) {
            Log.d(TAG, "error " + e);
        }

        return orientation;
    }

    public void changeOrientation() {
        Display display1 = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display1.getSize(size);
        int scrWidth = size.x;
        Log.d(TAG, "width size.x " + scrWidth);
        int scrHeight = size.y;
        Log.d(TAG, "height size.y " + scrHeight);

        if (scrHeight < scrWidth) {
            scrWidth = scrHeight + scrWidth;
            scrHeight = scrWidth - scrHeight;
            scrWidth = scrWidth - scrHeight;
        }
        //scrHeight = scrHeight - (scrWidth * mROWS / mCOLS)-48;

        if (portraitOrientation()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            double sizePicture1 = (scrWidth-(scrWidth*0.035))/mCOLS;
            sizePicture = (int)sizePicture1;
            //myHendlerBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, scrHeight));
            Log.d(TAG, "sizePicture " + sizePicture + ":" + scrWidth +"/" + mCOLS + " //mROWS" + mROWS);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            mCOLS = mCOLS + mROWS;
            mROWS = mCOLS - mROWS;
            mCOLS = mCOLS - mROWS;
            sizePicture = (int) (scrWidth-(scrWidth*0.05))/mROWS;
            //myHendlerBar.setLayoutParams(new LinearLayout.LayoutParams(scrHeight, ViewGroup.LayoutParams.MATCH_PARENT));
            Log.d(TAG, "sizePicture " + sizePicture + ":" + (scrWidth) +"/" + mROWS + "    //mCOWS" + mCOLS);
        }
        //mGrid.setLayoutParams(new GridLayout.LayoutParams(sizePicture, sizePicture));
        /*LinearLayout linearLayout = (LinearLayout) findViewById(R.id.hendler);
        linearLayout.setMinimumWidth(3*scrWidth);*/
        //linearLayout.setLayoutParams(new LinearLayout.LayoutParams(3*scrWidth, LinearLayout.LayoutParams.MATCH_PARENT));
    }

    public void afterStart() {
        final AlertDialog.Builder alertbox = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        final View iview = (View) inflater.inflate(R.layout.dialog_signin, null);

        typeface = Typeface.createFromAsset(getAssets(), getString(R.string.font_freakomix));
        TextView text13 = iview.findViewById(R.id.textView13);
        text13.setTypeface(typeface);

        TextView text1 = iview.findViewById(R.id.textView1);
        text1.setTypeface(typeface);
        TextView text3 = iview.findViewById(R.id.textView3);
        text3.setTypeface(typeface);

        TextView text18 = iview.findViewById(R.id.textView18);
        text18.setTypeface(typeface);
        TextView text20 = iview.findViewById(R.id.textView20);
        text20.setTypeface(typeface);

        TextView text2 = iview.findViewById(R.id.textView2);
        text2.setTypeface(typeface);
        TextView text4 = iview.findViewById(R.id.textView4);
        text4.setTypeface(typeface);

        text2.setText(String.valueOf(level+1));
        text4.setText(String.valueOf(mCOLS*mROWS));

        TextView text17 = iview.findViewById(R.id.textView17);
        text17.setTypeface(typeface);
        TextView text19 = iview.findViewById(R.id.textView19);
        text19.setTypeface(typeface);

        Button button = iview.findViewById(R.id.startButton);
        button.setTypeface(typeface);

        if (MAXSTEPFORLAVEL <= 999 && TIMEFORLEVEL/1000 <= 999) {
            text17.setText(String.valueOf(MAXSTEPFORLAVEL));
            text19.setText(String.valueOf(TIMEFORLEVEL/1000));
        }

        if (MAXSTEPFORLAVEL <= 999 && TIMEFORLEVEL/1000 >= 999) {
            text17.setText(String.valueOf(MAXSTEPFORLAVEL));
            text19.setVisibility(View.GONE);
            text20.setVisibility(View.GONE);
        }

        if (MAXSTEPFORLAVEL >= 999 && TIMEFORLEVEL/1000 <= 999) {
            text17.setVisibility(View.GONE);
            text18.setVisibility(View.GONE);
            text19.setText(String.valueOf(TIMEFORLEVEL/1000));
        }

        if (MAXSTEPFORLAVEL >= 999 && TIMEFORLEVEL/1000 >= 999) {
            text17.setVisibility(View.GONE);
            text19.setVisibility(View.GONE);
            text13.setVisibility(View.GONE);
            text18.setVisibility(View.GONE);
            text20.setVisibility(View.GONE);
        }

        alertbox.setView(iview);

        AlertDialog dialog = alertbox.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button.setShadowLayer(0,0,0, R.color.colorWhite);
                        dialog.cancel();
                        startAfterAlert();
                    }
                }
        );

    }



    public void startAfterAlert() {
        arrayOfRandomShow = new int[mAdapter.getCount()/2]; //array have random cell cols*rows/2
        arrayOfRandomShow = mAdapter.getArrayOfRandomShow();
        mAdapter.changeStatus(arrayOfRandomShow, 0, COUNTCELFORSHOW); //Open cell

        //cards which show on the start game
        Handler handler = new Handler(); //for paused
        handler.postDelayed(new Runnable() {
            public void run() {
                mAdapter.changeStatus(arrayOfRandomShow, 1, COUNTCELFORSHOW); //Show cell
                mGrid.setOnItemClickListener(oclItem); //activate oclItem
            }
        }, 1000);

        setStartSound(soundsGame);


    }


    public void setLevelSetting(int levelforsetting) {
        /*if (levelforsetting < 0) {
            levelforsetting = 0;
            setLevel(0);
            clearRecordSublevel();
            BDRecordAndCard.refreshCard();
        }*/

        SettingLevel settingLevel = SettingLevel.getArrayLevels().get(levelforsetting);



        mCOLS = settingLevel.getCol();
        mROWS = settingLevel.getRow();
        COUNTCELFORSHOW = settingLevel.getShow();
        MAXSTEPFORLAVEL = settingLevel.getStep();
        TIMEFORLEVEL = settingLevel.getTime();
    }

    public int getLevel() {
        int levelforsetting;
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.PREFERENCE_FOR_LEVEL), Context.MODE_PRIVATE);
        try {
            levelforsetting = sharedPreferences.getInt(getString(R.string.PREFERENCE_LEVEL), -1);
            return levelforsetting;
        } catch (Exception e) {
            return -1;
        }
    }

    public void setLevel(int thislevel) {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.PREFERENCE_FOR_LEVEL), Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        if (thislevel > SettingLevel.getArrayLevels().size()-1) {
            Log.d(TAG, "Level > max level");
            thislevel %= SettingLevel.getArrayLevels().size(); //SettingLevel.getArrayLevels().size() -> max level
        }
        edit.putInt(getString(R.string.PREFERENCE_LEVEL), thislevel);
        edit.apply();
    }

    public void clearRecordSublevel() {
        SharedPreferences sharedPreferences = getSharedPreferences(Bases.PREFERENCE_FOR_LEVEL, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(getString(R.string.RECORD_FOR_LEVEL), 0);
        edit.apply();
        Log.d(TAG, "subrecords is clear");
    }
    private void changeRecordSublevel(int recordSublevel) {
        SharedPreferences sharedPreferences = getSharedPreferences(Bases.PREFERENCE_FOR_LEVEL, Context.MODE_PRIVATE);
        int oldRecord = sharedPreferences.getInt(Bases.RECORD_FOR_LEVEL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Bases.RECORD_FOR_LEVEL, oldRecord + recordSublevel);
        editor.apply();
        Log.d(TAG, "SubRecord: "+String.valueOf(recordSublevel+oldRecord));
    }

    private int getRecordSublevel() {
        SharedPreferences sharedPreferences = getSharedPreferences(Bases.PREFERENCE_FOR_LEVEL, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(Bases.RECORD_FOR_LEVEL, Context.MODE_PRIVATE);
    }

    int scoreCalc(int timeScore, int stepScore, int cardScore) {

        Log.d(TAG, String.valueOf(cardScore) + "score card");
        Log.d(TAG, String.valueOf(TIMEFORLEVEL/1000) + " time1");
        Log.d(TAG, String.valueOf(timeScore) + " time2");
        Log.d(TAG, String.valueOf(stepScore) + " step1");
        Log.d(TAG, String.valueOf(MAXSTEPFORLAVEL) + " step2");

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.PREFERENCE_FOR_LEVEL), Context.MODE_PRIVATE);
        int numberGame = sharedPreferences.getInt(Bases.NUMBER_GAME, 0);
        double coef = 1;
        switch (numberGame) {
            case 0:
                coef = 1;
                break;
            case 1:
                coef = 0.9;
                break;
            case 2:
                coef = 0.8;
                break;
            case 3:
                coef = 0.75;
                break;
        }

        if (numberGame >= 3 ){
            coef = 0.75;
        }
        int lessTime  = TIMEFORLEVEL/1000 - timeScore; //потратил времени
        int lessStep = MAXSTEPFORLAVEL - stepScore; // потратил ходов

        if (MAXSTEPFORLAVEL <= 999 && TIMEFORLEVEL/1000 <= 999) {
            return (int) (((cardScore*(timeScore) + cardScore*(stepScore))*coef) + recordBingo);
        }

        if ( MAXSTEPFORLAVEL <= 999) {
            return (int) ((cardScore*(stepScore)*coef) + recordBingo);
        }

        if (TIMEFORLEVEL/1000 <= 999) {
            return (int) ((cardScore*(timeScore)*coef) + recordBingo);
        }

        if ((cardScore == 6 && (lessTime > 10 || lessStep > 7)) || (cardScore == 24 && (lessTime > 60 || lessStep > 30)) || (cardScore == 40 && (lessTime > 120 || lessStep > 60))) {
            return (int) (recordBingo);
        }

        if (cardScore == 6 ) {
             return (int) (((cardScore*(10 - lessTime) + cardScore*(7 - lessStep)))*coef + recordBingo);
        }
        if (cardScore == 24) {
             return (int) (((cardScore*(60 - lessTime) + cardScore*(30 - lessStep))*coef) + recordBingo);
        }
        if (cardScore == 40 ) {
             return (int) (((cardScore*(120 - lessTime) + cardScore*(60 - lessStep))*coef) + recordBingo);
        }


        return (int) (((cardScore*(TIMEFORLEVEL/1000 - timeScore) + cardScore*(MAXSTEPFORLAVEL - stepScore))*coef) + recordBingo);
    }

    public void setButtonSound(Boolean soundsGame) {
        if (soundsGame) buttonPlay.start();
    }

    public void setBingoSound(Boolean soundsGame) {
        if (soundsGame) bingoPlay.start();
    }

    public void setTimeSound(Boolean soundsGame) {
        if (soundsGame) timePlay.start();
    }

    public void setFinishSound(Boolean soundsGame) {
        if (soundsGame) winPlay.start();
    }

    public void setLoseSound(Boolean soundsGame) {
        if (soundsGame) gameOverPlay.start();
    }

    public void setStartSound(Boolean soundsGame) {
        if (soundsGame) startPlay.start();
    }
}