/*
package com.example.hp.teamproject;
package com.example.hp.teamproject;

//SR_noodle 클래스와 동일합니다.

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class SR_fry extends AppCompatActivity {

    static final String TAG = "Chan";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menulist);

        Intent intent = getIntent();
        String Menu = intent.getStringExtra("Menu");
        TextView menu = (TextView)findViewById(R.id.menubar);
        menu.setText(Menu);

        ArrayList<FoodItem> data2 = new ArrayList<>();
        data2.add(new FoodItem(R.drawable.m6,"돈까스덮밥","5000원","평점: ★★"));
        data2.add(new FoodItem(R.drawable.m7,"돈까스 오므라이스","3000원","평점: ★★"));
        data2.add(new FoodItem(R.drawable.m8,"등심돈까스","4500원","평점: ★★"));

        final FoodAdapter adapter = new FoodAdapter(data2,this,R.layout.menu);


        ListView listView1 = (ListView)findViewById(R.id.listview);
        listView1.setAdapter(adapter);
        listView1.setDividerHeight(5);

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

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View vClicked, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), FoodDetail.class);

                int Image = ((FoodItem)adapter.getItem(position)).getFood();
                String name = ((FoodItem)adapter.getItem(position)).getName();
                String price = ((FoodItem)adapter.getItem(position)).getPrice();
                String score = ((FoodItem)adapter.getItem(position)).getScore();

                intent.putExtra("Image", Image);
                intent.putExtra("Name", name);
                intent.putExtra("Price", price);
                intent.putExtra("Score", score);

                startActivity(intent);
            }
        });
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

    public void calling(View v) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:027604499"));
        startActivity(intent);
    }


}


*/
