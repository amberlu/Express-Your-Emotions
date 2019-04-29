package com.example.jiananlu.expressyouremotions;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Homepage extends AppCompatActivity {

    private Button go1, go2, go3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_homepage);
        clickOnButton();
    }

    private void clickOnButton(){
        go1 = (Button) findViewById(R.id.go1);
        go2 = (Button) findViewById(R.id.go2);
        go3 = (Button) findViewById(R.id.go3);
        go1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Homepage.this, IntroPage.class);
                it.putExtra("content", "Look at the pictures, and learn what they mean!");
                it.putExtra("task", "task1");
                it.putExtra("pic_id", R.drawable.task1_intro);
                startActivity(it);
            }
        });
        go2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Homepage.this, IntroPage.class);
                it.putExtra("content", "Make your face look like the picture of the character!");
                it.putExtra("task", "task2");
                it.putExtra("pic_id", R.drawable.task2_intro);
                startActivity(it);
            }
        });
        go3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Homepage.this, IntroPage.class);
                it.putExtra("content", "Lets see how well you know your emotions!");
                it.putExtra("task", "task3");
                it.putExtra("pic_id", R.drawable.task3_intro);
                startActivity(it);
            }
        });
    }
}

