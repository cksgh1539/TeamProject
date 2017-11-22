package com.example.hp.teamproject;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by hp on 2017-11-21.
 */

public class MainRestaurant extends AppCompatActivity implements MainRSfragment.OnTitleSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_rs);
    }

    public void onTitleSelected(int i) {
        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            FoodDetailFrag details = new FoodDetailFrag();
            details.setSelection(i);
            getSupportFragmentManager().beginTransaction().replace(R.id.details, details).commit();


        } else {
            Intent intent = new Intent(this, FoodDetail.class);
            intent.putExtra("index", i);
            startActivity(intent);

        }
    }
}
