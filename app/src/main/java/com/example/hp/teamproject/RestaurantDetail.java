package com.example.hp.teamproject;
//메뉴 등록 액티비티-------------------------------------------------------

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by hp on 2017-11-17.
 */

public class RestaurantDetail extends AppCompatActivity {
    TextView NAME;
    TextView NUMBER;
    TextView ADRRESS;

    private RSdbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menulist); // restaurant 레이아웃의 틀을 기반으로한다.

        NAME = (TextView)findViewById(R.id.name);
        NUMBER = (TextView)findViewById(R.id.number);
        ADRRESS = (TextView)findViewById(R.id.adrress);

        mDbHelper = new RSdbHelper(this);

        viewAllToTextView();
    }

    private void viewAllToTextView() {
        TextView result = (TextView)findViewById(R.id.result);

        Cursor cursor = mDbHelper.getAllUsersBySQL();

        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {
            buffer.append(cursor.getString(1)+" \t");
            buffer.append(cursor.getString(2)+" \t");
            buffer.append(cursor.getString(3)+"\n");
        }
        result.setText(buffer);
    }

    /*private void viewAllToListView() {
        Cursor cursor = mDbHelper.getAllUsersByMethod();

        SimpleCursorAdapter adapter
                = new SimpleCursorAdapter(getApplicationContext(),R.layout.rstaurantitem, cursor,
                new String[]{RSdbContract.Restaurant.KEY_name,
                             RSdbContract.Restaurant.KEY_num,
                             RSdbContract.Restaurant.KEY_adrress},
                new int[]{R.id.name, R.id.number, R.id.adrress}, 0);

        ListView lv = (ListView)findViewById(R.id.listview);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Adapter adapter = adapterView.getAdapter();
                NAME.setText(((Cursor)adapter.getItem(i)).getString(1));
                NUMBER.setText(((Cursor)adapter.getItem(i)).getString(2));
                ADRRESS.setText(((Cursor)adapter.getItem(i)).getString(3));
            }
        });
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }*/

}


