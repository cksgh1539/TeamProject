package com.example.hp.teamproject;
//db에 맛집 정보 입력,출력 ---------------------------------------------------------
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    public long insertUserByMethod(String name, String num, String adrress) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RSdb.Restaurant.KEY_name, name);
        values.put(RSdb.Restaurant.KEY_num, num);
        values.put(RSdb.Restaurant.KEY_adrress, adrress);

        return db.insert(RSdb.Restaurant.TABLE_NAME, null, values);
    }

    public Cursor getAllUsersByMethod() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(RSdb.Restaurant.TABLE_NAME,null,null,null,null,null,null);
    }

    public Cursor getAllUsersBySQL() {
        String sql = "Select * FROM " + RSdb.Restaurant.TABLE_NAME;
        return getReadableDatabase().rawQuery(sql,null);

    }
}
