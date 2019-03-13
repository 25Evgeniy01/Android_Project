package com.feezrook.Paracart.Pets.direct;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.feezrook.Paracart.Pets.Bases;
import com.feezrook.Paracart.Pets.R;

public class MusicSoundPlay {

    public static MediaPlayer mediaPlayer, gameOverPlay, winPlay, bingoPlay, timePlay, buttonPlay, startPlay;
    private static SoundPool soundPool;
    private static int music = R.raw.soundgame, gameOver = R.raw.airballoon, win = R.raw.yeahchildren, bingo = R.raw.bingo, timeFinish = R.raw.littlebells, button = R.raw.generalclick, startGame = R.raw.start;
    public static boolean isplayingAudio = false;

    //SOUND GAME
    public static void setGameOver(Context c) {
        gameOverPlay = MediaPlayer.create(c, gameOver);
        gameOverPlay.start();
    }
    public static void setWin(Context c) {
        winPlay = MediaPlayer.create(c, win);
        winPlay.start();
    }
    public static void setBingo(Context c) {
        bingoPlay = MediaPlayer.create(c, bingo);
        bingoPlay.start();
    }
    public static void setTimePlay(Context c) {
        timePlay = MediaPlayer.create(c, timeFinish);
        timePlay.start();
    }
    public static void setButtonPlay(Context c) {
        buttonPlay = MediaPlayer.create(c, button);
        buttonPlay.start();
    }
    public static void setStartPlay(Context c) {
        startPlay = MediaPlayer.create(c, startGame);
        startPlay.start();
    }
    //END SOUND GAME

    //MUSIC GAME
    public static void playMusic(Context c){
        mediaPlayer = MediaPlayer.create(c, music);
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        if(!mediaPlayer.isPlaying())
        {
            isplayingAudio=true;
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        }
    }

    public static void pauseMusic() {
        if(isplayingAudio){
            isplayingAudio=false;
            mediaPlayer.pause();
        }
    }

    public static void stopMusic(){
        if(isplayingAudio){
            isplayingAudio=false;
            mediaPlayer.stop();
        }

    }
}