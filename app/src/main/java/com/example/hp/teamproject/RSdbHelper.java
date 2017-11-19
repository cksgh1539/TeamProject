package com.example.hp.teamproject;
//db에 맛집,메뉴 정보 입출력 --------------------------------------------------------------------
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.w3c.dom.Text;

import java.net.URI;

public class RSdbHelper extends SQLiteOpenHelper {

    public RSdbHelper(Context context) {
        super(context, RSdb.DB_NAME, null, RSdb.DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(RSdb.Restaurant.CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(RSdb.Restaurant.DELETE_TABLE);
        onCreate(db);
    }

    //맛집 정보 db에 저장---------------------------------------------------------------------------
    public long insertRSByMethod(String name, String num, String adrress) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RSdb.Restaurant.KEY_name, name);
        values.put(RSdb.Restaurant.KEY_num, num);
        values.put(RSdb.Restaurant.KEY_adrress, adrress);

        return db.insert(RSdb.Restaurant.TABLE_NAME, null, values);
    }

    public Cursor getRSByMethod() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(RSdb.Restaurant.TABLE_NAME,null,null,null,null,null,null);
    }

    //메뉴 정보 db에 저장---------------------------------------------------------------------------
    public long insertMENUByMethod(String uri, String menu, String price, String coment) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RSdb.Menues.KEY_uri,uri);
        values.put(RSdb.Menues.KEY_menu, menu);
        values.put(RSdb.Menues.KEY_price, price);
        values.put(RSdb.Menues.KEY_coment, coment);

        return db.insert(RSdb.Menues.TABLE_NAME, null, values);
    }

    public Cursor getMENUByMethod() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(RSdb.Menues.TABLE_NAME,null,null,null,null,null,null);
    }
}
