package com.feezrook.Paracart.Pets;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.feezrook.Paracart.Pets.direct.HideBar;
import com.feezrook.Paracart.Pets.direct.MusicSoundPlay;

public class winActivity extends AppCompatActivity {

    private static final String TAG = "LoGs";
    private String card;
    private Button imgButDownload;

    private Typeface typeface;
    private static final int PERMS_REQUEST_CODE = 123;
    private ImageView imageView;
    private Bitmap bitmap;
    private Bitmap finalBitmap;

    public void downloadImage() {
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

        text13.setVisibility(View.GONE);
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

        text14.setText("Файл успешно загружен в галерею. Распечатайте изображение на принтере и наслаждайтесь раскрашиванием.");

        alertbox.setCancelable(false);



        alertbox.setView(iview);

        AlertDialog dialog = alertbox.create();
        dialog.show();

        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                }
        );
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);
        Intent intent = getIntent();

        Log.d(TAG, String.valueOf(getIntent().getExtras().getString("card")));
        String a = String.valueOf(getIntent().getExtras().getString("card"));
        imageView = (ImageView) findViewById(R.id.imageView2);

        imgButDownload = (Button) findViewById(R.id.button0);

        bitmap =  BitmapFactory.decodeResource(getResources(), R.drawable.color01);

        switch (a) {
            case "img0":
                imageView.setImageResource(R.drawable.main01);
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.color01);
                break;
            case "img1":
                imageView.setImageResource(R.drawable.main02);
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.color02);
                break;
            case "img2":
                imageView.setImageResource(R.drawable.main03);
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.color03);
                break;
            case "img3":
                imageView.setImageResource(R.drawable.main04);
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.color04);
                break;
            case "img4":
                imageView.setImageResource(R.drawable.main5);
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.color05);
                break;
            case "img5":
                imageView.setImageResource(R.drawable.main06);
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.color06);
                break;
            case "img6":
                imageView.setImageResource(R.drawable.main07);
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.color07);
                break;
            case "img7":
                imageView.setImageResource(R.drawable.main08);
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.color08);
                break;
            case "img8":
                imageView.setImageResource(R.drawable.main09);
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.color09);
                break;
            case "img9":
                imageView.setImageResource(R.drawable.main10);
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.color10);
                break;
            case "img10":
                imageView.setImageResource(R.drawable.main11);
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.color11);
                break;
            case "img11":
                imageView.setImageResource(R.drawable.main12);
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.color12);
                break;
            case "img12":
                imageView.setImageResource(R.drawable.main13);
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.color13);
                break;
            case "img13":
                imageView.setImageResource(R.drawable.main14);
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.color14);
                break;
            case "img14":
                imageView.setImageResource(R.drawable.main15);
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.color15);
                break;
            case "img15":
                imageView.setImageResource(R.drawable.main16);
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.color16);
                break;
            case "img16":
                imageView.setImageResource(R.drawable.main17);
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.color17);
                break;
            case "img17":
                imageView.setImageResource(R.drawable.main18);
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.color18);
                break;
            case "img18":
                imageView.setImageResource(R.drawable.main19);
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.color19);
                break;
            case "img19":
                imageView.setImageResource(R.drawable.main20);
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.color20);
                break;
        }

        finalBitmap = bitmap; //bitmap

        imgButDownload.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        imgButDownload.setBackgroundResource(R.drawable.downloadpress);

                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                imgButDownload.setBackgroundResource(R.drawable.download);
                                downloadImage();
                            }
                        };


                        if (hasPermissions()){
                            MediaStore.Images.Media.insertImage(getContentResolver(), finalBitmap,"card","feezrook");
                            Handler handler = new Handler();
                            handler.postDelayed(runnable, 50);
                        }
                        else {
                            requestPerms();
                            MediaStore.Images.Media.insertImage(getContentResolver(), finalBitmap,"card","feezrook");
                            Handler handler = new Handler();
                            handler.postDelayed(runnable, 50);
                        }




                    }
                }
        );
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

    @SuppressLint("WrongConstant")
    private boolean hasPermissions(){
        int res = 0;
        //string array of permissions,
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        for (String perms : permissions){
            res = checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)){
                return false;
            }
        }
        return true;
    }

    private void requestPerms(){
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions,PERMS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allowed = true;

        switch (requestCode){
            case PERMS_REQUEST_CODE:

                for (int res : grantResults){
                    // if user granted all permissions.
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }

                break;
            default:
                // if user not granted permissions.
                allowed = false;
                break;
        }

        if (allowed){
            //user granted all permissions we can perform our task.
            MediaStore.Images.Media.insertImage(getContentResolver(), finalBitmap,"card","feezrook");
        }
        else {
            // we will give warning to user that they haven't granted permissions.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    Toast.makeText(this, "Storage Permissions denied.", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        HideBar.hideSystemUI(getWindow().getDecorView());
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

}
