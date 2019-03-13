package com.feezrook.Paracart.Pets.direct;

import android.content.Context;
import android.content.SharedPreferences;

import com.feezrook.Paracart.Pets.Bases;
import com.feezrook.Paracart.Pets.MainActivity;
import com.feezrook.Paracart.Pets.R;
import com.feezrook.Paracart.Pets.activity_game_hearts;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class RewardedVideo {

    private static RewardedVideoAd mRewardedVideoAd;
    private static Boolean onRewardedLoadedBool = false;

    private static Boolean onRewardedClosedBool = false;
    private static Context context;

    public RewardedVideo(Context context) {
        this.context = context;
    }

    public static void pausemRewardedVideoAd() {
        mRewardedVideoAd.pause(context);
    }

    public static RewardedVideoAd getMreward() {
        return mRewardedVideoAd;
    }

    public void onCreateRewarded() {
        MobileAds.initialize(context,
                "ca-app-pub-8799270726987439~7438704571");

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context);
        onRewardedLoadedBool = false;
        onRewardedClosedBool = false;
        mRewardedVideoAd.setRewardedVideoAdListener(rewardedVideoAdListener);

        loadRewardedVideoAd();
    }


    public static void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-8799270726987439/3802711892",
                new AdRequest.Builder()
                        .addTestDevice("03D92257CB0CAA26D8278FA26D0B9778")
                        .build());
    }

    public static Boolean getOnRewardedVideoAdLoaded() {
        return onRewardedLoadedBool;
    }

    public static Boolean getOnRewardedVideoAdClosed() {
        loadRewardedVideoAd();
        return onRewardedClosedBool;
    }


    private static RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener() {
        @Override
        public void onRewardedVideoAdLoaded() {
            onRewardedLoadedBool = true;
           /* bannerButton.setEnabled(true);
            if (!PreferencesHelper.isAdsDisabled()) bannerButton.setVisibility(View.VISIBLE);
            if (startButton.getVisibility() == View.INVISIBLE)
            startButton.setVisibility(View.VISIBLE);*/
        }

        @Override
        public void onRewardedVideoAdOpened() {
        }

        @Override
        public void onRewardedVideoStarted() {
        }

        @Override
        public void onRewardedVideoAdClosed() {
            onRewardedClosedBool = true;
            /*bannerButton.setEnabled(false);
            bannerButton.setVisibility(View.INVISIBLE);*/
            Boolean musicGame = context.getSharedPreferences(Bases.PREFERENCE_FOR_LEVEL, Context.MODE_PRIVATE).getBoolean(Bases.MUSIC_PLAY, true);
            if (!MusicSoundPlay.isplayingAudio && musicGame)  MusicSoundPlay.playMusic(context);
            loadRewardedVideoAd();
        }

        @Override
        public void onRewarded(RewardItem rewardItem) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.PREFERENCE_FOR_LEVEL), Context.MODE_PRIVATE);
            int hearts = sharedPreferences.getInt(Bases.HEARTS_FOR_GAME, 1);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            hearts = hearts + 1;
            final int[] advertNumber = {sharedPreferences.getInt(Bases.ADVERT_NUMBER, 0)};
            advertNumber[0] = advertNumber[0] + 1;
            edit.putInt(Bases.ADVERT_NUMBER, advertNumber[0]);
            edit.apply();
            edit.putInt(Bases.HEARTS_FOR_GAME, hearts);
            edit.apply();


            activity_game_hearts.getHeartsStart().setText(String.valueOf(sharedPreferences.getInt(Bases.HEARTS_FOR_GAME, 1)));
        }

        @Override
        public void onRewardedVideoAdLeftApplication() {
        }

        @Override
        public void onRewardedVideoAdFailedToLoad(int i) {
        }
    };


   }

