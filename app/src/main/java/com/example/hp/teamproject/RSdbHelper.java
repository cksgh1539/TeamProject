package com.example.hp.teamproject;
//db에 맛집,메뉴 정보 입출력 --------------------------------------------------------------------
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.w3c.dom.Text;

import java.net.URI;

public class RSdbHelper extends SQLiteOpenHelper {

    public RSdbHelper(Context context) {
        super(context, RSdb.DB_NAME, null, RSdb.DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(RSdb.Restaurant.CREATE_TABLE);
        db.execSQL(RSdb.Menu.CREATE_TABLE2);
    }

    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(RSdb.Restaurant.DELETE_TABLE);
        db.execSQL(RSdb.Menu.DELETE_TABLE2);
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

    public long insertMENUByMethod(String menu, String price, String comment) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RSdb.Menu.KEY_menu, menu);
        values.put(RSdb.Menu.KEY_price, price);
        values.put(RSdb.Menu.KEY_comment, comment);
        return db.insert(RSdb.Menu.TABLE_NAME2,null, values);
    }

    public Cursor getRSByMethod() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(RSdb.Restaurant.TABLE_NAME,null,null,null,null,null,null);
    }
    public Cursor getMenuByMethod() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(RSdb.Menu.TABLE_NAME2,null,null,null,null,null,null);
    }

    /*//메뉴 정보 db에 저장---------------------------------------------------------------------------
    public long insertMENUByMethod(String menu, String price, String comment) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values2 = new ContentValues();
        values2.put(RSdb.Menu.KEY_menu, menu);
        values2.put(RSdb.Menu.KEY_price, price);
        values2.put(RSdb.Menu.KEY_comment, comment);
        return db.insert(RSdb.Menu.TABLE_NAME, null, values2);
    }

    public Cursor getMENUByMethod() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(RSdb.Menu.TABLE_NAME,null,null,null,null,null,null);
    }*/
}
