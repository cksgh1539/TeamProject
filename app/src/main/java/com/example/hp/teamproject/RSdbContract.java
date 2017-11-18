package com.example.hp.teamproject;
//맛집 정보 저장하는 db테이블 생성------------------------------------------
import android.provider.BaseColumns;

public final class RSdbContract {
    public static final String DB_NAME="restaurant.db";
    public static final int DATABASE_VERSION = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private RSdbContract() {}

    /* Inner class that defines the table contents */
    public static class Restaurant implements BaseColumns {
        public static final String TABLE_NAME="ResisRestaurant";
        public static final String KEY_name = "RSname";
        public static final String KEY_num = "RSnum";
        public static final String KEY_adrress = "RSadrress";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                KEY_name + TEXT_TYPE + COMMA_SEP +
                KEY_num + TEXT_TYPE + COMMA_SEP +
                KEY_adrress + TEXT_TYPE +  " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    /*public static class Menues implements BaseColumns {
        public static final String TABLE_NAME="Users";
        public static final String KEY_NAME = "Name";
        public static final String KEY_PHONE = "Phone";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                KEY_NAME + TEXT_TYPE + COMMA_SEP +
                KEY_PHONE + TEXT_TYPE +  " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }*/
}