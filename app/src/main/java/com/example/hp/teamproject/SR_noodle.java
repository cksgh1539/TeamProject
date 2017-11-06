package com.example.hp.teamproject;

//메뉴 세분화 리스트들이 나오는 뷰입니다.

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

public class SR_noodle extends AppCompatActivity {

    static final String TAG = "Chan";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menulist); // 음식들 리스트 레이아웃을 기반으로함

        Intent intent = getIntent();
        String Menu = intent.getStringExtra("Menu");
        TextView menu = (TextView)findViewById(R.id.menubar); // 소제목 레이아웃 ID
        menu.setText(Menu);

        ArrayList<MenuItem> data1 = new ArrayList<>();       // 이미지,이름,가격들의 데이터들
        data1.add(new MenuItem(R.drawable.m1,"냉모밀","4000원"));
        data1.add(new MenuItem(R.drawable.m2,"물냉면","4000원"));
        data1.add(new MenuItem(R.drawable.m3,"김밥","2000원"));

        ArrayList<FoodItem> data2 = new ArrayList<>(); // 평점이 추가된 데이터들
        data2.add(new FoodItem(R.drawable.m1,"냉모밀","4000원","평점: ★★"));
        data2.add(new FoodItem(R.drawable.m2,"물냉면","4000원","평점: ★★"));
        data2.add(new FoodItem(R.drawable.m3,"김밥","2000원","평점: ★★"));



        final MenuAdapter adapter1 = new MenuAdapter(data1,this,R.layout.menu); // menu의 이미지와 이름,내용을 나타내는 레이아웃을 받아옴,평점을 제외한 데이터
        final FoodAdapter adapter2 = new FoodAdapter(data2,this,R.layout.menu); // FoodDetail로 넘어갈 데이터들을 넣어줍니다.

        ListView listView1 = (ListView)findViewById(R.id.ListView4); // menulist.xml에 있는 listView ID입니다.
        listView1.setAdapter(adapter1);
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
            public void onItemClick(AdapterView<?> parent, View vClicked, int position, long id) { //메뉴를 클릭시 FoodDetail 뷰로 넘어갑니다.

                Intent intent = new Intent(getApplicationContext(), FoodDetail.class);

                int Image = ((FoodItem)adapter2.getItem(position)).getFood();     //평점까지 받은 데이터들을 받아서 넘겨줍니다.
                String name = ((FoodItem)adapter2.getItem(position)).getName();
                String price = ((FoodItem)adapter2.getItem(position)).getPrice();
                String score = ((FoodItem)adapter2.getItem(position)).getScore();

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

