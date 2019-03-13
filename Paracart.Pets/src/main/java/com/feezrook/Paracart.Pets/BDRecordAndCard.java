package com.feezrook.Paracart.Pets;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;



import java.util.ArrayList;
import java.util.Collections;

import static com.feezrook.Paracart.Pets.Bases.TAG;

/**
 * Created by DIO on 30.11.2017.
 */

public class BDRecordAndCard {//функции для работы с БД

    private Context context;
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    BDRecordAndCard(Context context) {
        this.context = context;
        this.dbHelper = new DBHelper(context);
        this.db = dbHelper.getWritableDatabase();
    }

    public ArrayList<Integer> getAllGlobalRecord() { //получить ряды рекордов которые записаны в БД
        Cursor cursor = db.query(DBHelper.TABLE_RECORDS, null, null, null, null, null, null);
        ArrayList<Integer> records = new ArrayList<>();
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.COLUMN_NAME_RECORD);
            do {
                Log.d(TAG, "ID = " + cursor.getInt(idIndex) +
                        ", name recored = " + cursor.getInt(nameIndex));
                records.add(cursor.getInt(nameIndex));
            } while (cursor.moveToNext());
        } else
            Log.d(TAG,"0 rows in db when getAllGlobRecor");
        cursor.close();
        return records;
    }

    public ArrayList<String> getAllNameCards() {
        Cursor cursor = db.query(DBHelper.TABLE_RECORDS, null, null, null, null, null, null);
        ArrayList<String> names = new ArrayList<>();
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.COLUMN_NAMECARDS);
            do {
                Log.d(TAG, "ID = " + cursor.getInt(idIndex) +
                        ", name recored = " + cursor.getInt(nameIndex));
                names.add(cursor.getString(nameIndex));
            } while (cursor.moveToNext());
        } else
            Log.d(TAG,"0 rows in db when getAllGlobRecor");
        cursor.close();
        return names;
    }


    public void putNameCard(int record, String card) {
        ArrayList<Integer> records = getAllGlobalRecord();
        ContentValues contentValues = new ContentValues();
        String id = "0";
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i) == record) {
                i = i + 1;
                id = String.valueOf(i);
                contentValues.put(DBHelper.COLUMN_NAMECARDS, card);
                break;
            }
        }
        db.update(DBHelper.TABLE_RECORDS, contentValues, "_id="+id, null);
        //db.execSQL("update " + DBHelper.TABLE_RECORDS + " SET " + DBHelper.COLUMN_NAMECARDS + "='" + card  + "' WHERE _id " + id);
    }

    /*public ArrayList<String> putGlobalRecordCard(int record) {
        ArrayList<String> recordCard = new ArrayList<>();

        String card1 = getCard();//карточку которую мы только что открыли
        String record1 = String.valueOf(record); //рекорд который мы записываем для карточки

        return recordCard;
    }*/

    public void putGlobalRecord(int record, String card) { //положить после 12 уровня рекорды в БД

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN_NAME_RECORD, record);
        contentValues.put(DBHelper.COLUMN_NAMECARDS, card);
        db.insert(DBHelper.TABLE_RECORDS, null, contentValues);
    }




    public ArrayList<String> getSavedCards() {

        Cursor cursor = db.query(DBHelper.TABLE_CARDS, null, null, null, null, null, null);
        ArrayList<String> savedCards = new ArrayList<>();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.COLUMN_NAME_CARD);
            do {
                Log.d(TAG, "ID = " + cursor.getInt(idIndex) +
                        ", name card = " + cursor.getString(nameIndex));
                savedCards.add(cursor.getString(nameIndex));
            } while (cursor.moveToNext());
        } else
            Log.d(TAG,"0 rows in db when getAllSavedCard");
        cursor.close();
        return savedCards;
    }

    private ArrayList<String> makeCard() { //create array of name card and return him
        ArrayList<String> colectionOfCards = new ArrayList<>();
        for (int i = 0; i < Bases.COUNTALLCARD; i++) {
            colectionOfCards.add(Bases.colectionName + i);
        }
        return colectionOfCards;
    }

    public void saveNewCard() {
        String card = getCard();
        if (card.equals("base")) { //don*t save this card
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN_NAME_CARD, card);
        db.insert(DBHelper.TABLE_CARDS, null, contentValues);
        Log.d(TAG, "put in db " + card);
    }



    public void refreshCard() { //change card witch using
        ArrayList<String> allCards = makeCard();
        ArrayList<String> myCards = getSavedCards();
        if (myCards.size() > 0) {
            for (String card:myCards) {
                allCards.remove(card);
            }
        }
        if (allCards.size() > 0) {
            Collections.shuffle(allCards);
            setCard(allCards.get(0));
            setSubCards(allCards.get(0));
        } else {
            setCard("base");
            setSubCards("base");
            Log.d(TAG, "we got all card, cards is saved");
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.PREFERENCE_FOR_LEVEL), Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(Bases.HEARTS_FOR_GAME, 1);
        edit.apply();
    }

    public void setCard(String card) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.PREFERENCE_FOR_LEVEL), Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(context.getString(R.string.PREFERENCE_CARD), card);
        edit.apply();
    }

    public String getCard() {
        String card;
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.PREFERENCE_FOR_LEVEL), Context.MODE_PRIVATE);
        try {
            card = sharedPreferences.getString(context.getString(R.string.PREFERENCE_CARD), "base");
            return card;
        } catch (Exception e) {
            Log.d(TAG, "DBRecordCard getCard Trouble");
            return "base";
        }

    }

    public int getIdCard() {
        Resources mResourse = context.getResources();
        String card = getCard();
        return mResourse.getIdentifier(card, "drawable", context.getPackageName());
    }

    public int getIdCard1 (String card) {
        Resources mResourse = context.getResources();
        return mResourse.getIdentifier(card, "drawable", context.getPackageName());
    }

    public String getNameCard (int card) {
        Resources mResourse = context.getResources();
        return mResourse.getResourceEntryName(card);
    }

    public int getNameCardMain (int card) {
        Resources mResourse = context.getResources();
        String cardName = mResourse.getResourceEntryName(card);
        switch (cardName) {
            case "zoo0" :
                return R.drawable.main01;
            case "zoo1" :
                return R.drawable.main02;
            case "zoo2" :
                return R.drawable.main03;
            case "zoo3" :
                return R.drawable.main04;
            case "zoo4" :
                return R.drawable.main5;
            case "zoo5" :
                return R.drawable.main06;
            case "zoo6" :
                return R.drawable.main07;
            case "zoo7" :
                return R.drawable.main08;
            case "zoo8" :
                return R.drawable.main09;
            case "zoo9" :
                return R.drawable.main10;
            case "zoo10" :
                return R.drawable.main11;
            case "zoo11" :
                return R.drawable.main12;
            case "zoo12" :
                return R.drawable.main13;
            case "zoo13" :
                return R.drawable.main14;
            case "zoo14" :
                return R.drawable.main15;
            case "zoo15" :
                return R.drawable.main16;
            case "zoo16" :
                return R.drawable.main17;
            case "zoo17" :
                return R.drawable.main18;
            case "zoo18" :
                return R.drawable.main19;
            case "zoo19" :
                return R.drawable.main20;
        }
        return card;
    }

    private void setSubCards(String card) {
        dbHelper.clearTableSubCards(db);

        int[] arrRandom = {0, 1, 2, 3, 7, 11, 10, 9, 8, 4, 5, 6};
        for (Integer i:arrRandom) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.COLUMN_NAME_SUBCARD, card+"_"+i);
            db.insert(DBHelper.TABLE_SUBCARDS, null, contentValues);
            Log.d(TAG, "put in db (ConVal) "+card+"_"+i);
        }

    }


}
