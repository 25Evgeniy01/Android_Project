package com.feezrook.Paracart.Pets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.BoringLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.feezrook.Paracart.Pets.direct.HideBar;
import com.feezrook.Paracart.Pets.direct.MusicSoundPlay;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class RoomActivity extends AppCompatActivity { //особистий кабинет

    private Typeface typeface;
    private GridView lvSimple;
    private static final String TAG = "LoGs";
    private Button prizeButton;
    private BDRecordAndCard bdRecordAndCard;
    private Boolean soundsGame;
    InputMethodManager imm;


    private ImageView imgMain, img3, img31, img32, img4, img41, img42,img5, img51, img52,img6, img61, img62, imgNumber;
    private ImageView[] a = new ImageView[12];
    private TextView levelStart, countStart, heartsStart, allCards;

    public int searchNumberCard (String card) {
        switch (card) {
            case "zoo0":
                return R.drawable.img0;
            case "zoo1":
                return R.drawable.img1;

            case "zoo2":
                return R.drawable.img2;

            case "zoo3":
                return R.drawable.img3;

            case "zoo4":
                return R.drawable.img4;

            case "zoo5":
                return R.drawable.img5;

            case "zoo6":
                return R.drawable.img6;

            case "zoo7":
                return R.drawable.img7;

            case "zoo8":
                return R.drawable.img8;

            case "zoo9":
                return R.drawable.img9;

            case "zoo10":
                return R.drawable.img10;

            case "zoo11":
                return R.drawable.img11;

            case "zoo12":
                return R.drawable.img12;

            case "zoo13":
                return R.drawable.img13;

            case "zoo14":
                return R.drawable.img14;

            case "zoo15":
                return R.drawable.img15;

            case "zoo16":
                return R.drawable.img16;

            case "zoo17":
                return R.drawable.img17;

            case "zoo18":
                return R.drawable.img18;

            case "zoo19":
                return R.drawable.img19;

        }
        return 0;
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
    protected void onResume() {
        super.onResume();
        HideBar.hideSystemUI(getWindow().getDecorView());
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

    public void functionRenameCard () {

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent("com.feezrook.Paracart.Pets.MainActivity");
        setButtonSound(soundsGame);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        soundsGame = getSharedPreferences(Bases.PREFERENCE_FOR_LEVEL, Context.MODE_PRIVATE).getBoolean(Bases.SOUND_PLAY, true);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        bdRecordAndCard = new BDRecordAndCard(getBaseContext());

        playAudio();

        imgMain = (ImageView) findViewById(R.id.imageView2);

        //if (bdRecordAndCard.getIdCard())
        Log.d(TAG, " !!!!!!!!!!! " + bdRecordAndCard.getIdCard());
        if (bdRecordAndCard.getIdCard() != 0)
        imgMain.setImageResource(bdRecordAndCard.getNameCardMain(bdRecordAndCard.getIdCard()));

        SharedPreferences sharedPreferences = getSharedPreferences(Bases.PREFERENCE_FOR_LEVEL, Context.MODE_PRIVATE);
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

        int level = (sharedPreferences.getInt(getString(R.string.PREFERENCE_LEVEL), -1) + 1);

        levelStart = (TextView) findViewById(R.id.textView51);
        countStart = (TextView) findViewById(R.id.textView61);
        heartsStart = (TextView) findViewById(R.id.textView8);
        allCards = findViewById(R.id.textView32);
        allCards.setText(bdRecordAndCard.getSavedCards().size() + "/20");
        levelStart.setText(String.valueOf(level));
        countStart.setText(String.valueOf(sharedPreferences.getInt(Bases.RECORD_FOR_LEVEL, Context.MODE_PRIVATE)));
        heartsStart.setText(String.valueOf(sharedPreferences.getInt(Bases.HEARTS_FOR_GAME, 1)));

        if (PreferencesHelper.isAdsDisabled())  {
            heartsStart.setText("∞");
        };

        int levelforsetting = sharedPreferences.getInt(getString(R.string.PREFERENCE_LEVEL), -1);



        for (int i = 0; i < level-1; i++) {
             a[i].setAlpha(0);
        }

        Log.d(TAG, "level " + (sharedPreferences.getInt(getString(R.string.PREFERENCE_LEVEL), -1) + 1));
        Log.d(TAG, "счет " + sharedPreferences.getInt(Bases.RECORD_FOR_LEVEL, Context.MODE_PRIVATE));

        ArrayList<String> mySavedCards = bdRecordAndCard.getSavedCards();
        ArrayList<Integer> numberCards = new ArrayList<Integer>();


        for (int i = 0; i < mySavedCards.size(); i++ ) {
            //Log.d(TAG, "card #" + bdRecordAndCard.getIdCard1(mySavedCards.get(i)));
            numberCards.add(searchNumberCard(mySavedCards.get(i)));
        }



        //получаем сохраненную картинку и говорим , что если она равна изначально примерно 1 , то это первая карточка и так далее через switchcase

        // массивы данных
        String[] texts = { "Очков: ", "Очков: ", "Очков: ",
                "Очков: ", "Очков: " , "Очков: ", "Очков: ", "Очков: ", "Очков: ", "Очков: ", "Очков: ", "Очков: ", "Очков: ",
                "Очков: ", "Очков: " , "Очков: ", "Очков: ", "Очков: ", "Очков: ", "Очков: "};

        typeface = Typeface.createFromAsset(getAssets(), getString(R.string.font_freakomix));

        // упаковываем данные в понятную для адаптера структуру
        ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>(
                texts.length);
        ArrayList<Integer> recordArray = bdRecordAndCard.getAllGlobalRecord();
        ArrayList<String> nameArray = bdRecordAndCard.getAllNameCards();

        HashMap<String, Object> m;

        for (int i = 0; i < numberCards.size(); i++) {
            m = new HashMap<String, Object>();
            m.put("text", texts[i]);
            m.put("img", numberCards.get(i));
            m.put("score", recordArray.get(i) );
            m.put("name",nameArray.get(i) );
            data.add(m);
        }



        // массив имен атрибутов, из которых будут читаться данные
        String[] from = { "text", "img", "score", "name"};
        // массив ID View-компонентов, в которые будут вставлять данные
        int[] to = {R.id.text_score2, R.id.icon_card, R.id.text_score4, R.id.nameCard};

        final int[] lastFocussedPosition = {-1};
        final Handler[] handler = {new Handler()};
        // создаем адаптер
        SimpleAdapter sAdapter = new SimpleAdapter(this, data, R.layout.my_room_item,
                from, to) {
            @Override
            public View getView (int position, View convertView, ViewGroup parent)
            {
                View v = super.getView(position, convertView, parent);

                ImageView b = (ImageView) v.findViewById(R.id.renameCard);
                TextView textView = v.findViewById(R.id.nameCard);

                TextView textView1 = v.findViewById(R.id.text_score4);



                if (!textView.getText().equals("card")) {
                    b.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                }

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        setButtonSound(soundsGame);
                        b.setImageResource(R.drawable.renamepress);
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                b.setImageResource(R.drawable.rename);
                            }
                        };
                        Handler handler = new Handler();
                        handler.postDelayed(runnable, 200);



                        //открывается SimpleAdapter - с помощью которого делаем запись в БД
                        AlertDialog.Builder alertbox = new AlertDialog.Builder(RoomActivity.this);
                        LayoutInflater inflater = RoomActivity.this.getLayoutInflater();
                        final View iview = (View) inflater.inflate(R.layout.dialog_rename_card, null);

                        typeface = Typeface.createFromAsset(getAssets(), getString(R.string.font_freakomix));

                        alertbox.setCancelable(false);
                        EditText editText = iview.findViewById(R.id.editName);
                        Button but = iview.findViewById(R.id.editNameButton);

                        alertbox.setView(iview);

                        AlertDialog dialog = alertbox.create();
                        dialog.show();

                        but.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        but.setBackgroundResource(R.drawable.enterpress);
                                        Runnable runnable = new Runnable() {
                                            @Override
                                            public void run() {
                                                but.setBackgroundResource(R.drawable.enter);
                                                dialog.cancel();
                                                if (editText.getText().length() != 0) {
                                                    b.setVisibility(View.GONE);

                                                    bdRecordAndCard.putNameCard(Integer.valueOf((String) textView1.getText()), editText.getText().toString());
                                                    textView.setText(editText.getText().toString());
                                                    textView.setVisibility(View.VISIBLE);
                                                } else {
                                                    b.setVisibility(View.VISIBLE);
                                                    textView.setVisibility(View.GONE);
                                                }
                                            }
                                        };
                                        Handler handler = new Handler();
                                        handler.postDelayed(runnable, 50);

                                    }
                                }
                        );

                    }
                });



                ImageView imageView = (ImageView) v.findViewById(R.id.button2);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        setButtonSound(soundsGame);
                        HashMap<String, Object> a = (HashMap<String,Object>) lvSimple.getItemAtPosition(position);
                        imageView.setImageResource(R.drawable.getprizepress);
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageResource(R.drawable.getprize);
                            }
                        };
                        Handler handler = new Handler();
                        handler.postDelayed(runnable, 200);

                        //imgMain.setImageResource(Integer.valueOf(String.valueOf(a.get("img"))));
                        Intent intent = new Intent(getApplicationContext(), winActivity.class);
                        intent.putExtra("card", bdRecordAndCard.getNameCard(Integer.valueOf(String.valueOf(a.get("img")))));
                        startActivity(intent);

                    }
                });

                return v;
            }
        };

        // определяем список и присваиваем ему адаптер
        lvSimple = (GridView) findViewById(R.id.list_room);
        //Log.d(TAG, "room befor set adapter is ok");
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            lvSimple.setNumColumns(1);
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            lvSimple.setNumColumns(2);
        }

        lvSimple.setAdapter(sAdapter);





        homeWatcher();

        HideBar.hideSystemUI(getWindow().getDecorView());

    }





    public void showKeyboard(View view) {
        imm.toggleSoftInput(0, 0);
    }

    public void hideKeyboard(View view) {
        imm.toggleSoftInput(0, 0);

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

    public void setButtonSound(Boolean soundsGame) {
        if (soundsGame) MusicSoundPlay.setButtonPlay(this);
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
}
