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
import java.util.ArrayList;

public class RestaurantDetail extends AppCompatActivity {
    TextView rsNAME,menuName;
    TextView NUMBER,PRICE;
    TextView ADRRESS;
    ImageView RSIMAGE,MENUIMAGE;

    String TAG = "food";

    private RSdbHelper rsDbHelper;

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
        viewMENUToListView();

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

    // ResisRS에서 받아온 정보 나타내기-------------------------------------------------------------
    private void viewRSToListView() {
        Cursor RS = rsDbHelper.getRSByMethod();
        RS.moveToLast();

        String RSImg = RS.getString(1);
        Uri RSUri = Uri.parse(RSImg);
        RSIMAGE.setImageURI(RSUri);

        Cursor cursor = rsDbHelper.getRSByMethod();
        if (cursor.moveToLast()) {
            rsNAME.setText(cursor.getString(2));
            NUMBER.setText(cursor.getString(3));
            ADRRESS.setText(cursor.getString(4));
        }
    }

    //추가된 메뉴 list 나타내기---------------------------------------------------------------------
    private void viewMENUToListView() {
        Cursor Menu = rsDbHelper.getMenuByMethod();

        ArrayList<FoodItem> Menuinfo = new ArrayList<>();

        while(Menu.moveToNext()) {
          Menuinfo.add(new FoodItem(Menu.getString(1),
            Menu.getString(2),
            Menu.getString(3),
            Menu.getString(4)));
}
final FoodAdapter adapter = new FoodAdapter(Menuinfo, this, R.layout.menu);

    ListView listView = (ListView) findViewById(R.id.listview);
    listView.setAdapter(adapter);
    listView.setDividerHeight(5);
        }

    }






