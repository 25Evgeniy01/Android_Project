package com.feezrook.Paracart.Pets;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.BoringLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.feezrook.Paracart.Pets.direct.HideBar;
import com.feezrook.Paracart.Pets.direct.MusicSoundPlay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class RecordActivity extends AppCompatActivity { //вывод рекордов с БД в активити

    private Typeface typeface;
    private ListView lvSimple;
    private static final String TAG = "LoGs";
    private Button prizeButton;
    private TextView notRecord;
    private BDRecordAndCard bdRecordAndCard;
    private Boolean soundsGame;


    @Override
    protected void onResume() {
        super.onResume();
        HideBar.hideSystemUI(getWindow().getDecorView());
       orientation();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent("com.feezrook.Paracart.Pets.MainActivity");
        setButtonSound(soundsGame);
        startActivity(intent);
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
        setContentView(R.layout.activity_record);
        soundsGame = getSharedPreferences(Bases.PREFERENCE_FOR_LEVEL, Context.MODE_PRIVATE).getBoolean(Bases.SOUND_PLAY, true);

        bdRecordAndCard = new BDRecordAndCard(getBaseContext());
        notRecord = findViewById(R.id.textView1);

        // массивы данных
        String[] texts = { "Очков: ", "Очков: ", "Очков: ",
                "Очков: ", "Очков: " , "Очков: ", "Очков: ", "Очков: ", "Очков: ", "Очков: "};
        int[] number = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        BDRecordAndCard BDRecordAndCard = new BDRecordAndCard(getBaseContext());
        ArrayList<Integer> recordArray = BDRecordAndCard.getAllGlobalRecord();

        ArrayList<String> cardArray = BDRecordAndCard.getSavedCards();

        typeface = Typeface.createFromAsset(getAssets(), getString(R.string.font_freakomix));

        // упаковываем данные в понятную для адаптера структуру
        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(
                texts.length);
        Map<String, Object> m;
        int recordArraySize = recordArray.size();



        if (recordArray.size() == 0) {

            notRecord.setText("Продолжайте играть =)");
            notRecord.setTextColor(getResources().getColor(R.color.button));

        } else {

            notRecord.setVisibility(View.GONE);

            if (recordArray.size() > 10) {
                recordArraySize = 10;
            }
            ArrayList<String> mySavedCards = bdRecordAndCard.getSavedCards();
            ArrayList<Integer> numberCards = new ArrayList<Integer>();
            ArrayList<String> nameArray = bdRecordAndCard.getAllNameCards();
            for (int i = 0; i < mySavedCards.size(); i++ ) {
                //Log.d(TAG, "card #" + bdRecordAndCard.getIdCard1(mySavedCards.get(i)));
                numberCards.add(searchNumberCard(mySavedCards.get(i)));
            }


            int[] arr = new int[recordArray.size()];
            String[] arrName = new String[nameArray.size()];
            int[] cardArr = new int[numberCards.size()];
            for(int t = 0; t < arr.length; t++){
                arr[t] = recordArray.get(t);
                cardArr[t] = numberCards.get(t);
                arrName[t] = nameArray.get(t);
            }

            int last = arr.length;

            for ( boolean sorted = last == 0; !sorted; --last )
            {
                sorted = true;
                for ( int i = 1; i < last; ++i )
                {
                    if ( arr[i-1] < arr[i] )
                    {
                        sorted = false;
                        String tmp2 = arrName[i-1];
                        int tmp = arr[i-1];
                        int tmp1 = cardArr[i-1];

                        arrName[i-1] = arrName[i];
                        arr[i-1] = arr[i];
                        cardArr[i-1] = cardArr[i];

                        arrName[i] = tmp2;
                        arr[i] = tmp;
                        cardArr[i] = tmp1;
                    }
                }
            }
            for (int i = 0; i < recordArraySize; i++) {
                m = new HashMap<String, Object>();
                m.put("text", texts[i]);

                if (i < cardArray.size()) {
                    m.put("number", cardArr[i]);
                }

                if (i < recordArray.size()) {
                    m.put("score", String.valueOf(arr[i]));
                    m.put("name" , arrName[i]);
                }

                data.add(m);
            }

            // массив имен атрибутов, из которых будут читаться данные
            String[] from = { "text", "number", "score", "name"};
            // массив ID View-компонентов, в которые будут вставлять данные
            int[] to = {R.id.text_score1, R.id.icon_card, R.id.text_score3, R.id.nameCard};
            // создаем адаптер
            SimpleAdapter sAdapter = new SimpleAdapter(this, data, R.layout.content_record_item,
                    from, to){
                @Override
                public View getView (int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);

                    TextView textView = v.findViewById(R.id.nameCard);

                    if (!textView.getText().equals("card")) {
                        textView.setVisibility(View.VISIBLE);
                    }

                    return v;
                }
            };

            // определяем список и присваиваем ему адаптер
            lvSimple = (ListView) findViewById(R.id.list_room);
            //Log.d(TAG, "room befor set adapter is ok");
            lvSimple.setAdapter(sAdapter);

        }

        homeWatcher();
        playAudio();
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
