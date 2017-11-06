package com.example.hp.teamproject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fooddetail); // fooddetail 레이아웃의 틀을 기반으로 합니다.

        Intent intent = getIntent(); //데이터들을 받아옵니다.

        int Image = intent.getExtras().getInt("Image");
        String name = intent.getExtras().getString("Name");
        String price = intent.getExtras().getString("Price");
        String score = intent.getExtras().getString("Score");

        TextView txt = (TextView)findViewById(R.id.Name);  // fooddetail 레이웃의 정의된 ID들을 사용
        TextView txt2 = (TextView)findViewById(R.id.Price);
        TextView txt3 = (TextView)findViewById(R.id.Score);
        ImageView img = (ImageView)findViewById(R.id.imageView);

        img.setImageResource(Image);
        txt.setText(name);
        txt2.setText(price);
        txt3.setText(score);

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