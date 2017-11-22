package com.example.hp.teamproject;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by hp on 2017-11-21.
 */

public class Test extends AppCompatActivity implements RestDetailFrag.OnTitleSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testfrag);
    }

    public void onTitleSelected(int i) {
        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            FoodDetailFrag details = new FoodDetailFrag();
            details.setSelection(getIntent().getIntExtra("index",-1));
            getSupportFragmentManager().beginTransaction().replace(R.id.details, details).commit();
       /*   yyyy tails = new yyyy();
            tails.setSelection(getIntent().getIntExtra("index",-1));
            getSupportFragmentManager().beginTransaction().replace(R.id.details, tails).commit();*/

        } else {
            Intent intent = new Intent(this, FoodDetail.class);
            intent.putExtra("index", i);
            startActivity(intent);

        }
    }
}
