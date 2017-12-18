package com.example.hp.teamproject;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

//음식의 디테일한 내용을 나타내 주는 뷰입니다.

public class FoodDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fooddetail);

        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        FoodDetailFrag details = new FoodDetailFrag();
        details.setSelection(getIntent().getIntExtra("index",-1),getIntent().getStringExtra("RSid"));
        getSupportFragmentManager().beginTransaction().replace(R.id.Detail, details).commit();
    }



}