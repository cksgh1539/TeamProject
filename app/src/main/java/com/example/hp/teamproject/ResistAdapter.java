/*
package com.example.hp.teamproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

*/
/**
 * Created by hp on 2017-11-21.
 *//*


public class ResistAdapter  extends BaseAdapter

    {
        private ArrayList<FoodItem> fData;
        private Context fContext;
        private int fResource;

    public ResistAdapter(ArrayList<FoodItem> data, Context context, int resource) {
        fData = data;
        fContext = context;
        fResource = resource;
    }

        @Override
        public View getView(int j, View view, ViewGroup viewGroup) { //i번째 항목에 대한 view를 만듦

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) fContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(fResource, viewGroup,false);
        }

        ImageView Image = view.findViewById(R.id.MenuImage);
        TextView name = view.findViewById(R.id.MenuName);
        TextView price = view.findViewById(R.id.MenuPrice);

        Image.setImageResource(fData.get(j).getFood());
        name.setText(fData.get(j).getName());
        price.setText(fData.get(j).getPrice());


        return view;
    }

        @Override
        public int getCount() {
        return fData.size(); //MyAdapter가 관리하는 항목의 개수
    }

        @Override
        public Object getItem(int j) {
        return fData.get(j); //MyItem타입의 데이터가 가지고 있는 데이터를 get
    }

        @Override
        public long getItemId(int j) {
        return j;
    }

    }*/
