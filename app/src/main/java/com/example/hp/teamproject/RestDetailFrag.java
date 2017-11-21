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

import java.util.ArrayList;

/**
 * Created by hp on 2017-11-21.
 */

public class RestDetailFrag extends Fragment {

    TextView rsNAME,menuName;
    TextView NUMBER,PRICE;
    TextView ADRRESS;
    ImageView RSIMAGE,MENUIMAGE;

    String TAG = "food";

    private RSdbHelper rsDbHelper;
   // private MenuInflater menuInflater;

    int mCurCheckPosition = -1;

    public interface OnTitleSelectedListener {
        public void onTitleSelected(int i);
    }

    public RestDetailFrag() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = (View)inflater.inflate(R.layout.menulist,container,false);
        setHasOptionsMenu(true);
        RSIMAGE = (ImageView) rootView.findViewById(R.id.rsimage);
        rsNAME = (TextView) rootView.findViewById(R.id.name);
        NUMBER = (TextView) rootView.findViewById(R.id.number);
        ADRRESS = (TextView) rootView.findViewById(R.id.adrress);

        MENUIMAGE = (ImageView) rootView.findViewById(R.id.MenuImage);
        menuName = (TextView) rootView.findViewById(R.id.MenuName);
        PRICE = (TextView) rootView.findViewById(R.id.MenuPrice);

        rsDbHelper = new RSdbHelper(getActivity());

       // viewRSToListView();
        //viewMENUToListView();

        Cursor RS = rsDbHelper.getRSByMethod();
        RS.moveToLast();

        String RSImg = RS.getString(1);
        Uri RSUri = Uri.parse(RSImg);
        RSIMAGE.setImageURI(RSUri);

        // Cursor cursor = rsDbHelper.getRSByMethod();
        if (RS.moveToLast()) {
            rsNAME.setText(RS.getString(2));
            NUMBER.setText(RS.getString(3));
            ADRRESS.setText(RS.getString(4));
        }

//메뉴 목록
        Cursor Menu = rsDbHelper.getMenuByMethod();

        ArrayList<FoodItem> info = new ArrayList<>();

        while (Menu.moveToNext()) {
            info.add(new FoodItem(Menu.getString(1),
                    Menu.getString(2),
                    Menu.getString(3) ));
        }
        final FoodAdapter adapter = new FoodAdapter(info, getActivity(), R.layout.menu);

        ListView listView = (ListView) rootView.findViewById(R.id.listview);
        listView.setAdapter(adapter);
        listView.setDividerHeight(5);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View vClicked, int position, long id) {
                Intent intent = new Intent(getActivity(), FoodDetail.class);

                intent.putExtra("Position",position);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.main_menu, menu);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.quick_add:
                startActivity(new Intent(getActivity(), ResistMenu.class));
                break;

        }
        return super.onOptionsItemSelected(item);
        }



   /* private void viewRSToListView() {

        Cursor RS = rsDbHelper.getRSByMethod();
        RS.moveToLast();

        String RSImg = RS.getString(1);
        Uri RSUri = Uri.parse(RSImg);
        RSIMAGE.setImageURI(RSUri);

       // Cursor cursor = rsDbHelper.getRSByMethod();
        if (RS.moveToLast()) {
            rsNAME.setText(RS.getString(2));
            NUMBER.setText(RS.getString(3));
            ADRRESS.setText(RS.getString(4));
        }
    }*/

   /* private void viewMENUToListView() {
        Cursor Menu = rsDbHelper.getMenuByMethod();

        ArrayList<FoodItem> info = new ArrayList<>();

        while (Menu.moveToNext()) {
            info.add(new FoodItem(Menu.getString(1),
                    Menu.getString(2),
                    Menu.getString(3),
                    Menu.getString(4)));
        }
        final FoodAdapter adapter = new FoodAdapter(info, getActivity(), R.layout.menu);

        ListView listView = (ListView) view.findViewById(R.id.listview);
        listView.setAdapter(adapter);
        listView.setDividerHeight(5);

       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View vClicked, int position, long id) {
                Intent intent = new Intent(getActivity(), FoodDetail.class);

                intent.putExtra("Position",position);
                startActivity(intent);
            }
        });
    }*/

    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mCurCheckPosition = savedInstanceState.getInt("curChoice", -1);
            if (mCurCheckPosition >= 0) {
                Activity activity = getActivity(); // activity associated with the current fragment
                ((OnTitleSelectedListener)activity).onTitleSelected(mCurCheckPosition);

                ListView lv = (ListView) getView().findViewById(R.id.listview);
                lv.setSelection(mCurCheckPosition);
                lv.smoothScrollToPosition(mCurCheckPosition);
            }
        }
    }

    //    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }


}
