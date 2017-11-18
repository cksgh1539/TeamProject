package com.example.hp.teamproject;
//메뉴 등록 액티비티-------------------------------------------------------

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class RestaurantDetail extends AppCompatActivity {
    TextView NAME;
    TextView NUMBER;
    TextView ADRRESS;
    ImageView RSIMAGE;

    private RSdbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menulist); // restaurant 레이아웃의 틀을 기반으로한다.

        NAME = (TextView)findViewById(R.id.name);
        NUMBER = (TextView)findViewById(R.id.number);
        ADRRESS = (TextView)findViewById(R.id.adrress);
        RSIMAGE = (ImageView)findViewById(R.id.rsimage);

        mDbHelper = new RSdbHelper(this);
        viewAllToListView();
    }

    private void viewAllToListView() {
        Cursor cursor = mDbHelper.getAllUsersByMethod();

        //RSIMAGE.setImageURI(ResistRS.ImageURI);
        if(cursor.moveToLast()) {
            NAME.setText(cursor.getString(1));
            NUMBER.setText(cursor.getString(2));
            ADRRESS.setText(cursor.getString(3));
        }
    }

}


