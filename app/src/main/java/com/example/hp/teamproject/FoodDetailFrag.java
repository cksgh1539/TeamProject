package com.example.hp.teamproject;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by hp on 2017-11-21.
 */


public class FoodDetailFrag extends Fragment {

    ImageView MenuImg;
    TextView MenuName,MenuPrice,MenuComment;
    private RSdbHelper MenuDB;

    static int index=-1;

    public FoodDetailFrag() {
        // Required empty public constructor
    }

    public void setSelection(int i) { index = i; }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_fooddetail, container, false);
        MenuDB = new RSdbHelper(getActivity());

        MenuImg = (ImageView) view.findViewById(R.id.imageView);
        MenuName = (TextView) view.findViewById(R.id.Name);
        MenuPrice = (TextView) view.findViewById(R.id.Price);
        MenuComment = (TextView) view.findViewById(R.id.Comment);

        Cursor cursor= MenuDB.getMenuByMethod();
        if(index >= 0)
        cursor.moveToPosition(index);

        Uri Img = Uri.parse(cursor.getString(1));
        MenuImg.setImageURI(Img);
        MenuName.setText(cursor.getString(2));
        MenuPrice.setText(cursor.getString(3));
        MenuComment.setText(cursor.getString(4));

        return view;
    }

}