package com.feezrook.Paracart.Pets;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.feezrook.Paracart.Pets.Bases.TAG;

public class DBHelper  extends SQLiteOpenHelper{//створення/ upgrade БД

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "gameDB";
    public static final String TABLE_RECORDS = "records";
    public static final String TABLE_CARDS = "cards";
    public static final String TABLE_SUBCARDS = "subcards";

    public static final String TABLE_NAMECARDS = "namecards";

    /*public static final String TABLE_CARDS_RECORD = "cardsRecord";
    public static final String TABLE_CARDSSAVE = "cardsSave";
    public static final String TABLE_RECORDSSAVE = "recordSave";*/

    public static final String KEY_ID = "_id";
    public static final String COLUMN_NAME_RECORD = "record";
    public static final String COLUMN_NAME_CARD = "card";
    public static final String COLUMN_NAME_SUBCARD = "subcard";
    public static final String COLUMN_NAMECARDS = "namecards";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTableRecords(db);
        createTableCards(db);
        createTableSubCards(db);

        Log.d(TAG, "DB id created at first");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_RECORDS);

        onCreate(db);

    }

    public void clearTableRecords(SQLiteDatabase db) {
        db.execSQL("drop table if exists " + TABLE_RECORDS);
        createTableRecords(db);
    }

    /*public void createTableRecordsAndCards(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_CARDS_RECORD + "(" + TABLE_CARDSSAVE
                + " text1" + TABLE_RECORDSSAVE + " text" + ")");
    }*/

    private void createTableRecords(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_RECORDS + "(" + KEY_ID
                + " integer primary key," + COLUMN_NAME_RECORD + " TEXT," + COLUMN_NAMECARDS + " TEXT" + ")");

        /*ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN_NAME_RECORD, 0);
        for (int i = 0; i<10; i++) {
            db.insert(DBHelper.TABLE_RECORDS, null ,contentValues);
        }*/

        Log.d(TAG, "DB record is created/update");
    }

    private void createTableCards(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_CARDS + "(" + KEY_ID
                + " integer primary key," + COLUMN_NAME_CARD + " text" + ")");
    }

    private void createTableSubCards(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_SUBCARDS + "(" + KEY_ID
                + " integer primary key," + COLUMN_NAME_SUBCARD + " text" + ")");
    }

    public void clearTableSubCards(SQLiteDatabase db) {
        db.execSQL("drop table if exists " + TABLE_SUBCARDS);
        createTableSubCards(db);
        Log.d(TAG, "clearTableSubCards finished");
    }

}