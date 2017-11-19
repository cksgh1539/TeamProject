package com.example.hp.teamproject;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MENUdbHelper extends SQLiteOpenHelper {

    public MENUdbHelper(Context context) {
        super(context, MENUdb.DB_NAME, null, RSdb.DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MENUdb.Menu.CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(MENUdb.Menu.DELETE_TABLE);
        onCreate(db);
    }


    //메뉴 정보 db에 저장---------------------------------------------------------------------------
    public long insertMENUByMethod(String menu, String price, String comment) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MENUdb.Menu.KEY_menu, menu);
        values.put(MENUdb.Menu.KEY_price, price);
        values.put(MENUdb.Menu.KEY_comment, comment);
        return db.insert(MENUdb.Menu.TABLE_NAME, null, values);
    }

    public Cursor getMENUByMethod() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(MENUdb.Menu.TABLE_NAME, null, null, null, null, null, null);
    }
}
