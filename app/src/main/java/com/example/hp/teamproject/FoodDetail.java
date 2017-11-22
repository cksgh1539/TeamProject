package com.example.hp.teamproject;

import android.content.Intent;
import android.content.res.Configuration;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        FoodDetailFrag details = new FoodDetailFrag();
        details.setSelection(getIntent().getIntExtra("index",-1));
        getSupportFragmentManager().beginTransaction().replace(R.id.Detail, details).commit();
    }



}