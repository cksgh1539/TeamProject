package com.example.hp.teamproject;
//db에 맛집,메뉴 정보 입출력 ------------------------------------------------------------------------
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

        db.execSQL("CREATE TABLE favorite_table ( id INTEGER primary key autoincrement, phoneNumber TEXT, title TEXT, thumbnail TEXT UNIQUE, method TEXT, videourl TEXT, intensity TEXT, frequency TEXT, time TEXT);");
    }

    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(RSdb.Restaurant.DELETE_TABLE);
        db.execSQL(RSdb.Menu.DELETE_TABLE2);
        onCreate(db);
    }

    //맛집 정보 db에 저장---------------------------------------------------------------------------
    public long insertRSByMethod(String ImageRS,String name, String num, String adrress, String latitude, String longitude) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RSdb.Restaurant.KEY_ImageRS,ImageRS);
        values.put(RSdb.Restaurant.KEY_name, name);
        values.put(RSdb.Restaurant.KEY_num, num);
        values.put(RSdb.Restaurant.KEY_adrress, adrress);
        values.put(RSdb.Restaurant.KEY_latitude, latitude);
        values.put(RSdb.Restaurant.KEY_longitde, longitude);

        return db.insert(RSdb.Restaurant.TABLE_NAME, null, values);
    }

    public long insertMENUByMethod(String ImageMenu,String menu, String price, String comment, String id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RSdb.Menu.KEY_ImageMenu,ImageMenu);
        values.put(RSdb.Menu.KEY_menu, menu);
        values.put(RSdb.Menu.KEY_price, price);
        values.put(RSdb.Menu.KEY_comment, comment);
        values.put(RSdb.Menu.KEY_ID, id);
        return db.insert(RSdb.Menu.TABLE_NAME2,null, values);
    }

    //db에 저장된 모든 restaurant_____________________________________________________________________
    public Cursor getRSByMethod() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(RSdb.Restaurant.TABLE_NAME,null,null,null,null,null,null);
    }

    //받아온 위치 값과 일치하는 restaurant select
    public Cursor getRSbyLocation(String latitude, String longitude) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "Select * FROM " + RSdb.Restaurant.TABLE_NAME
                + " Where " + RSdb.Restaurant.KEY_latitude + " = '" + latitude + "'"
                +" AND " + RSdb.Restaurant.KEY_longitde + " = '" + longitude + "'";
        return db.rawQuery(sql, null);
    }

    //받아온 이름과 일치하는 restaurant select
    public Cursor getRSbyName (String name) {
        SQLiteDatabase db = getReadableDatabase();
        String sql1 = "Select * FROM " + RSdb.Restaurant.TABLE_NAME
                + " Where " + RSdb.Restaurant.KEY_name + " = '" + name + "'";
        return db.rawQuery(sql1, null);
    }

    //db에 저장된 모든 menu__________________________________________________________________________
    public Cursor getMenuByMethod() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(RSdb.Menu.TABLE_NAME2,null,null,null,null,null,null);
    }

    //받아온 아이디와 일치하는 restaurant select
    public Cursor getRSbyID (int id) {
        SQLiteDatabase db = getReadableDatabase();
        String sql1 = "Select * FROM " + RSdb.Restaurant.TABLE_NAME
                + " Where " + RSdb.Restaurant._ID + " = '" + id + "'";
        return db.rawQuery(sql1, null);
    }
    //받아온 id 값과 일치하는 menu select
    public Cursor getMenuByID(String id) {
        SQLiteDatabase db = getReadableDatabase();
        String sql2 = "Select * FROM " + RSdb.Menu.TABLE_NAME2
                + " Where " + RSdb.Menu.KEY_ID + " = '" + id + "'";
        return db.rawQuery(sql2, null);
    }

}
