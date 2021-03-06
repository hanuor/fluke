package com.hanuor.fluke.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Shantanu Johri on 06-07-2016.
 */
public class IdDatabase extends SQLiteOpenHelper {

    public static final String DB_NAME = "UserIds.db";
    private static final String TABLE_NAME = "flukemobility";
    private static final String TABLE_SONG = "currentsong";

    private static final int DB_VERSION = 1;

    private static final String ID_F = "id";
    private static final String SONG_F = "song";
    public IdDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public IdDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public IdDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String TABLE_ID = "CREATE TABLE " + TABLE_NAME + "("+
                 ID_F + " STRING"
                + ")";
        String TABLE_S = "CREATE TABLE " + TABLE_SONG + "("+
                SONG_F + " STRING"
                + ")";
        db.execSQL(TABLE_S);
        db.execSQL(TABLE_ID);

    }
    public void insert_song(String song){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SONG_F, song);
        db.insert(TABLE_SONG, null, contentValues);
        db.close();

    }
    public void insert(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_F, id);
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }
    public void clear_song(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SONG, 1 + "=" + 1, null);
        db.close();
    }
    public void clear() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, 1 + "=" + 1, null);
        db.close();
    }
    public int get_song_length(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query_params = "SELECT " + SONG_F + " FROM " + TABLE_SONG;
        Cursor cSor = db.rawQuery(query_params, null);
        Log.d("Mlearning", "" + cSor.getCount());
        return cSor.getCount();
    }
    public int getLength(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query_params = "SELECT " + ID_F + " FROM " + TABLE_NAME;
        Cursor cSor = db.rawQuery(query_params, null);
        Log.d("Mlearning", "" + cSor.getCount());
        return cSor.getCount();
    }

    public String query_song(){
        String daemon = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String query_params = "SELECT " + SONG_F + " FROM " + TABLE_SONG;
        Cursor cSor = db.rawQuery(query_params, null);
        if(cSor.moveToFirst()){
            do{
                daemon =  cSor.getString(cSor.getColumnIndex(IdDatabase.SONG_F));

            }while(cSor.moveToNext());

        }
        return daemon;

    }
    public String query(){
        String daemon = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String query_params = "SELECT " + ID_F + " FROM " + TABLE_NAME;
        Cursor cSor = db.rawQuery(query_params, null);
        if(cSor.moveToFirst()){
            do{
                daemon =  cSor.getString(cSor.getColumnIndex(IdDatabase.ID_F));

            }while(cSor.moveToNext());

        }
        return daemon;

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
