package com.example.hp.teamproject;
//메뉴 등록 액티비티-------------------------------------------------------

import android.accessibilityservice.GestureDescription;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.net.URI;

public class RestaurantDetail extends AppCompatActivity {
    TextView NAME;
    TextView NUMBER;
    TextView ADRRESS;
    ImageView RSIMAGE;

    String TAG = "food";

    private RSdbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menulist); // restaurant 레이아웃의 틀을 기반으로한다.

        RSIMAGE = (ImageView) findViewById(R.id.rsimage);
        NAME = (TextView) findViewById(R.id.name);
        NUMBER = (TextView) findViewById(R.id.number);
        ADRRESS = (TextView) findViewById(R.id.adrress);

        mDbHelper = new RSdbHelper(this);
        Log.i(TAG, getLocalClassName() + " :printdb");
        viewAllToListView();

    }


    // ResisRS에서 받아온 정보 나타내기-------------------------------------------------------------
    private void viewAllToListView() {
        Uri imageUri = getIntent().getData();//https://stackoverflow.com/questions/8017374/how-to-pass-a-uri-to-an-intent참조
        //맛집 등록에서 찍은 사진 Uri받음
        RSIMAGE.setImageURI(imageUri);

        Cursor cursor = mDbHelper.getRSByMethod();
        if (cursor.moveToLast()) {
            RSIMAGE.setImageURI(imageUri);
            NAME.setText(cursor.getString(1));
            NUMBER.setText(cursor.getString(2));
            ADRRESS.setText(cursor.getString(3));
        }
    }

    // 메뉴 추가 액션바 구현 -----------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.quick_add:
                Log.i(TAG, getLocalClassName() + " :click");
                Intent ResistMenu = new Intent(getApplicationContext(), SR_fry.class);
                startActivity(ResistMenu);
                Log.i(TAG, getLocalClassName() + " :changeActivity");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


