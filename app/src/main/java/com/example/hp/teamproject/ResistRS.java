package com.example.hp.teamproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by hp on 2017-11-17.
 */

public class ResistRS extends AppCompatActivity{

    private Intent Resister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resist);
        Button ResistRS = (Button) findViewById(R.id.Resist_RS);
        ImageButton RSimage = (ImageButton)findViewById(R.id.RS_Image);

        RSimage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Resister = new Intent(getApplicationContext(), ResistMenu.class);
                startActivity(Resister);
            }

        });

        ResistRS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Resister = new Intent(getApplicationContext(), ResistMenu.class);
                startActivity(Resister);
            }

        });

    }



  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.quick_add:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
}
