package com.example.hp.teamproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MenuAdapter extends BaseAdapter {
    private ArrayList<MenuItem> nData;
    private Context nContext;
    private int nResource;

    public MenuAdapter(ArrayList<MenuItem> data1, Context context1, int resource1) {
        nData = data1;
        nContext = context1;
        nResource = resource1;
    }

    @Override
    public int getCount() {
        return nData.size(); //MyAdapter가 관리하는 항목의 개수
    }

    @Override
    public Object getItem(int j) {
        return nData.get(j); //MyItem타입의 데이터가 가지고 있는 데이터를 get
    }

    @Override
    public long getItemId(int j) {
        return j;
    }

    @Override
    //i번째항목, ListView, Parent group
    public View getView(int j, View view, ViewGroup viewGroup) { //i번째 항목에 대한 view를 만듦

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) nContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(nResource, viewGroup,false);
        }

        ImageView Image = view.findViewById(R.id.iconItem);
        TextView text4 = view.findViewById(R.id.textItem4);
        TextView text5 = view.findViewById(R.id.textItem5);

        Image.setImageResource(nData.get(j).getFood());
        text4.setText(nData.get(j).getName());
        text5.setText(nData.get(j).getPrice());

        return view;
    }
}