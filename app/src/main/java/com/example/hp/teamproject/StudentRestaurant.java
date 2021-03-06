package com.example.hp.teamproject;/*
package com.example.hp.teamproject;
 // 학생식당 메뉴의 큰 범주를 선택할 수 있는 뷰입니다.

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class StudentRestaurant extends AppCompatActivity {

    static final String TAG = "Team";

    private ListAdapter createAdapter() {

        String[] items = {"Roll & Noodles", "Rice", "Fry" };

        ArrayAdapter<String> adapt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        return adapt;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant); // restaurant 레이아웃의 틀을 기반으로한다.
        Log.i(TAG, getLocalClassName() + ".onCreate");

        ListAdapter adapt = createAdapter();

        ListView list = (ListView) findViewById(R.id.restaurantMenu); //menu의 범주 리스트 ID를 받아옴
        list.setAdapter(adapt);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View vClicked,
                                    int position, long id) {   // 각 메뉴 범주 클릭시 뷰 이동
                if(position == 0){
                    Intent intent = new Intent(getApplicationContext(), SR_noodle.class);
                    intent.putExtra("Menu", "Roll & Noodles");
                    startActivity(intent);
                }else if(position == 1){
                    Intent intent = new Intent(getApplicationContext(), SR_rice.class);
                    intent.putExtra("Menu", "Rice");
                    startActivity(intent);
                }else if(position == 2){
                    Intent intent = new Intent(getApplicationContext(), SR_fry.class);
                    intent.putExtra("Menu", "Fry");
                    startActivity(intent);
                }
            }

        });
    }

    public void calling(View v) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:027604499")); //전화 걸기 메소드
        startActivity(intent);
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

}

*/
