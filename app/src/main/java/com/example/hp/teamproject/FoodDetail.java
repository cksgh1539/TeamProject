package com.example.hp.teamproject;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

//음식의 디테일한 내용을 나타내 주는 뷰입니다.

public class FoodDetail extends AppCompatActivity {


    static final String TAG = "Chan";

    ImageView MenuImg;
    TextView MenuName,MenuPrice,MenuComment;
    private RSdbHelper MenuDB;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fooddetail); // fooddetail 레이아웃의 틀을 기반으로 합니다.
        MenuDB = new RSdbHelper(this);

        Intent intent = getIntent();
        int Position = intent.getExtras().getInt("Position");

        MenuImg = (ImageView) findViewById(R.id.imageView);
        MenuName = (TextView) findViewById(R.id.Name);
        MenuPrice = (TextView) findViewById(R.id.Price);
        MenuComment = (TextView) findViewById(R.id.Comment);

             Cursor  cursor= MenuDB.getMenuByMethod();
            cursor.moveToPosition(Position);

                 Uri Img = Uri.parse(cursor.getString(1));
                MenuImg.setImageURI(Img);
                MenuName.setText(cursor.getString(2));
               MenuPrice.setText(cursor.getString(3));
              MenuComment.setText(cursor.getString(4));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            Drawable drawable = getDrawable(R.drawable.ic_arrow_back_black_24dp);
            if (drawable != null) {
                drawable.setTint(Color.WHITE);
                actionBar.setHomeAsUpIndicator(drawable);
            }
        }
        Log.i(TAG, getLocalClassName() + ".onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, getLocalClassName() + ".onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, getLocalClassName() + ".onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, getLocalClassName() + ".onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, getLocalClassName() + ".onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, getLocalClassName() + ".onDestroy");
    }


}