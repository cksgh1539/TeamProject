package com.example.hp.teamproject;
//db에 맛집 정보 입력,출력 ---------------------------------------------------------
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kangeunjin on 2017-11-18.
 */

public class RSdbHelper extends SQLiteOpenHelper {

    public RSdbHelper(Context context) {
        super(context, RSdbContract.DB_NAME, null, RSdbContract.DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(RSdbContract.Restaurant.CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(RSdbContract.Restaurant.DELETE_TABLE);
        onCreate(db);
    }

    public long insertUserByMethod(String name, String num, String adrress) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RSdbContract.Restaurant.KEY_name, name);
        values.put(RSdbContract.Restaurant.KEY_num, num);
        values.put(RSdbContract.Restaurant.KEY_adrress, adrress);

        return db.insert(RSdbContract.Restaurant.TABLE_NAME, null, values);
    }

    public Cursor getAllUsersByMethod() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(RSdbContract.Restaurant.TABLE_NAME,null,null,null,null,null,null);
    }

    public Cursor getAllUsersBySQL() {
        String sql = "Select * FROM " + RSdbContract.Restaurant.TABLE_NAME;
        return getReadableDatabase().rawQuery(sql,null);

    }


}
