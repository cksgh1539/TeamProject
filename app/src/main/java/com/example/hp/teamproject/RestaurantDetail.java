package com.example.hp.teamproject;
//메뉴 등록 액티비티-------------------------------------------------------

import android.accessibilityservice.GestureDescription;
import android.content.Intent;
import android.database.Cursor;
import android.media.browse.MediaBrowser;
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
    TextView rsNAME,menuName;
    TextView NUMBER,PRICE;
    TextView ADRRESS;
    ImageView RSIMAGE,MENUIMAGE;

    String TAG = "food";

    private RSdbHelper rsDbHelper;
   // private MENUdbHelper menuDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menulist); // restaurant 레이아웃의 틀을 기반으로한다.

        RSIMAGE = (ImageView) findViewById(R.id.rsimage);
        rsNAME = (TextView) findViewById(R.id.name);
        NUMBER = (TextView) findViewById(R.id.number);
        ADRRESS = (TextView) findViewById(R.id.adrress);

        MENUIMAGE = (ImageView) findViewById(R.id.MenuImage);
        menuName = (TextView) findViewById(R.id.MenuName);
        PRICE = (TextView) findViewById(R.id.MenuPrice);

        rsDbHelper = new RSdbHelper(this);
        Log.i(TAG, getLocalClassName() + " :printdb");
        viewRSToListView();
        //viewMENUToListView();

    }


    // ResisRS에서 받아온 정보 나타내기-------------------------------------------------------------
    private void viewRSToListView() {
        Uri RSUri = getIntent().getData();//https://stackoverflow.com/questions/8017374/how-to-pass-a-uri-to-an-intent참조
        //맛집 등록에서 찍은 사진 Uri받음
        RSIMAGE.setImageURI(RSUri);

        Cursor cursor = rsDbHelper.getRSByMethod();
        if (cursor.moveToLast()) {
            rsNAME.setText(cursor.getString(1));
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
                startActivity(new Intent(getApplicationContext(), ResistMenu.class));
                Log.i(TAG, getLocalClassName() + " :changeActivity");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //추가된 메뉴 list 나타내기---------------------------------------------------------------------
    private void viewMENUToListView() {
        Uri MENUUri = getIntent().getData();
        //메뉴 등록에서 찍은 사진 Uri받음
        MENUIMAGE.setImageURI(MENUUri);

        Cursor cursor = rsDbHelper.getMenuByMethod();
       /* SimpleCursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(),
                R.layout.menu, cursor, new String[]{
                MENUdb.Menu.KEY_menu,
                MENUdb.Menu.KEY_price},
                new int[]{ R.id.MenuName, R.id.MenuPrice}, 0);

        ListView listView = (ListView)findViewById(R.id.listview);
        listView.setAdapter(adapter);*/

        if (cursor.moveToLast()) {
            menuName.setText(cursor.getString(1));
            PRICE.setText(cursor.getString(2));
          //  ADRRESS.setText(cursor.getString(3));
        }
    }


}


