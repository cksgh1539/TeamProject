package com.example.hp.teamproject;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by hp on 2017-11-21.
 */

public class MainRSfragment extends Fragment {
    TextView rsNAME, menuName;
    TextView NUMBER, PRICE;
    TextView ADRRESS;
    ImageView RSIMAGE, MENUIMAGE, ImagePhone;
    Cursor RS;
    int RS_id;
    String TAG = "food";

    private RSdbHelper rsDbHelper;

    int mCurCheckPosition = -1;

    public interface OnTitleSelectedListener {
        public void onTitleSelected(int i, String RS_id);
    }

    public MainRSfragment() {    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = (View) inflater.inflate(R.layout.frag_main_rs, container, false);
        setHasOptionsMenu(true);

        //맛집 정보 레이아웃
        RSIMAGE = (ImageView) rootView.findViewById(R.id.rsimage);
        rsNAME = (TextView) rootView.findViewById(R.id.name);
        NUMBER = (TextView) rootView.findViewById(R.id.number);
        ADRRESS = (TextView) rootView.findViewById(R.id.adrress);
        ImagePhone = (ImageView) rootView.findViewById(R.id.imagePhone);

        //메뉴 정보 레이아웃
        MENUIMAGE = (ImageView) rootView.findViewById(R.id.MenuImage);
        menuName = (TextView) rootView.findViewById(R.id.MenuName);
        PRICE = (TextView) rootView.findViewById(R.id.MenuPrice);

        rsDbHelper = new RSdbHelper(getActivity());
        ImagePhone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                calling();
            }
        });

        viewRSToListView();//맛집 정보 여주기
        viewMENUToListView(rootView);// 메뉴 리스트 보여주기

        return rootView;
    }

    //맛집 정보 출력---------------------------------------------------------------------------------
    private void viewRSToListView() {
        int i = getActivity().getIntent().getIntExtra("INT",0);
        if(i == 0) { //처음 등록하는 맛집일 경우
            RS = rsDbHelper.getRSByMethod();
            insertInfo(RS);

        }else if (i ==1) { // map에서 등록된 맛집 클릭한 경우
            String latitude = getActivity().getIntent().getStringExtra("markerlatitude");
            String longitude = getActivity().getIntent().getStringExtra("markerlongitude");
            RS = rsDbHelper.getRSbyLocation(latitude, longitude);
            insertInfo(RS);

        }else if( i ==2 ){ // 메뉴 추가하고 돌아온 맛집인 경우
            String RS_ID = getActivity().getIntent().getStringExtra("MENU_id");
            RS = rsDbHelper.getRSbyID(Integer.parseInt(RS_ID));
            insertInfo(RS);
        }
    }

    private void insertInfo(Cursor RS) { //맛집 정보들 화면에 띄움
        RS.moveToLast();
        RS_id = RS.getInt(0);

        String RSImg = RS.getString(1);
        Uri RSUri = Uri.parse(RSImg);
        RSIMAGE.setImageURI(RSUri);

        rsNAME.setText(RS.getString(2));
        NUMBER.setText(RS.getString(3));
        ADRRESS.setText(RS.getString(4));
    }

    //메뉴 리스트------------------------------------------------------------------------------------
    private void viewMENUToListView(View view) {
        Cursor Menu = rsDbHelper.getMenuByMethod();
        if (RS.getCount() != 0 && Menu.getCount() != 0) { //db테이블이 생성된 경우

            Cursor ID = rsDbHelper.getMenuByID(String.valueOf(RS_id)); //RS_id와 같은 id값을 가진 레코드 선택
                if (ID.getCount() != 0) { //등록된 메뉴가 있을 경우
                    ArrayList<FoodItem> info = new ArrayList<>();
                    while (ID.moveToNext()) {
                        info.add(new FoodItem(ID.getString(1),
                                ID.getString(2),
                                ID.getString(3)));
                    }

                    final FoodAdapter adapter = new FoodAdapter(info, getActivity(), R.layout.menu);

                    ListView listView = (ListView) view.findViewById(R.id.listview);
                    listView.setAdapter(adapter);
                    listView.setDividerHeight(5);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View vClicked, int position, long id) {
                            mCurCheckPosition = position;
                            Activity activity = getActivity();
                            ((OnTitleSelectedListener) activity).onTitleSelected(position,String.valueOf(RS_id));
                            Log.i(TAG, "position= "+position +"/"+ String.valueOf(RS_id));
                        }
                    });
                    listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                }
        }else
            return;
    }

    //메뉴 추가하는 메소드----------------------------------------------------------------------------
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.resist, menu);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.quick_add:
                Intent ResistMenu = new Intent(getActivity(), ResistMenu.class);
                ResistMenu.putExtra("RS_ID", RS_id);
                startActivity(ResistMenu);
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                break;
        }
        return super.onOptionsItemSelected(item);
    }


    //전화 거는 메소드-------------------------------------------------------------------------------
    public void calling() {
        Cursor Call = rsDbHelper.getRSByMethod();
        Call.moveToLast();
        Intent call = new Intent(Intent.ACTION_DIAL);
        call.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        call.setData(Uri.parse("tel:" + Call.getString(3)));
        startActivity(call);
    }

}
