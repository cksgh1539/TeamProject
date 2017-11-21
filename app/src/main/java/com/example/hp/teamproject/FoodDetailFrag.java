package com.example.hp.teamproject;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fooddetail, container, false);

        MenuDB = new RSdbHelper(getActivity());

        Intent intent = getActivity().getIntent();
        int Position = intent.getExtras().getInt("Position");

        MenuImg = (ImageView) view.findViewById(R.id.imageView);
        MenuName = (TextView) view.findViewById(R.id.Name);
        MenuPrice = (TextView) view.findViewById(R.id.Price);
        MenuComment = (TextView) view.findViewById(R.id.Comment);

        Cursor cursor= MenuDB.getMenuByMethod();
        cursor.moveToPosition(Position);

        Uri Img = Uri.parse(cursor.getString(1));
        MenuImg.setImageURI(Img);
        MenuName.setText(cursor.getString(2));
        MenuPrice.setText(cursor.getString(3));
        MenuComment.setText(cursor.getString(4));

     /*   ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            Drawable drawable = getDrawable(R.drawable.ic_arrow_back_black_24dp);
            if (drawable != null) {
                drawable.setTint(Color.WHITE);
                actionBar.setHomeAsUpIndicator(drawable);
            }
        }*/


        return view;
    }

}