package com.example.jiananlu.expressyouremotions;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Task1_1 extends AppCompatActivity {

    private Button angry, happy, sad, scared, bored, surprised, home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_task1_1);
        clickOnButton();
    }

    public void clickOnButton(){
        angry  = (Button) findViewById(R.id.angry);
        happy = (Button) findViewById(R.id.happy);
        sad  = (Button) findViewById(R.id.sad);
        scared = (Button) findViewById(R.id.scared);
        bored  = (Button) findViewById(R.id.bored);
        surprised = (Button) findViewById(R.id.surprised);
        home = (Button) findViewById(R.id.task1_home);
        angry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Task1_1.this, Angry.class);
                startActivity(it);
            }
        });
        happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Task1_1.this, Happy.class);
                startActivity(it);
            }
        });
        sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Task1_1.this, Sad.class);
                startActivity(it);
            }
        });
        scared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Task1_1.this, Scared.class);
                startActivity(it);
            }
        });
        bored.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Task1_1.this, Bored.class);
                startActivity(it);
            }
        });
        surprised.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Task1_1.this, Surprised.class);
                startActivity(it);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Task1_1.this, Homepage.class);
                startActivity(it);
            }
        });
    }
}
