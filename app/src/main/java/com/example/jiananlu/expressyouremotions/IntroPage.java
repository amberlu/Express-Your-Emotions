package com.example.jiananlu.expressyouremotions;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class IntroPage extends AppCompatActivity {

    String cont;
    Integer pic_id;
    Button letsgo, back;
    String task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_intro_page);
        Intent intent = getIntent();
        cont = intent.getStringExtra("content");
        pic_id = intent.getIntExtra("pic_id", 0);
        task = intent.getStringExtra("task");
        ImageView image = findViewById(R.id.imageView);
        TextView text = findViewById(R.id.text0);
        image.setImageResource(pic_id);

        text.setText(cont);
        clickOnButton();
    }

    private void clickOnButton(){
        letsgo = (Button) findViewById(R.id.letsgo);
        back = (Button) findViewById(R.id.back);
        letsgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(task.equals("task1")){
                    Intent it = new Intent(IntroPage.this, Task1_1.class);
                    startActivity(it);
                }
                else if(task.equals("task2")){
                    Intent it = new Intent(IntroPage.this, Task2_1_Setup.class);
                    startActivity(it);
                }
                else{
                    Intent it = new Intent(IntroPage.this, Task1_1.class);
                    startActivity(it);
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(IntroPage.this, Homepage.class);
                startActivity(it);
            }
        });
    }
}
