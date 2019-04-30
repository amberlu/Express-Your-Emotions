package com.example.jiananlu.expressyouremotions;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Task2 extends AppCompatActivity {

    private Button back, letsGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_task2);
        clickOnButton();
    }

    private void clickOnButton(){
        back  = (Button) findViewById(R.id.back2_1);
        letsGo = (Button) findViewById(R.id.b2_2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Task2.this, Homepage.class);
                startActivity(it);
            }
        });
        letsGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Task2.this, Task2.class);
                startActivity(it);
            }
        });
    }
}

