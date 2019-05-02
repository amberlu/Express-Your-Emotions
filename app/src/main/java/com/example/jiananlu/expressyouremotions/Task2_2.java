package com.example.jiananlu.expressyouremotions;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Task2_2 extends AppCompatActivity {

    ImageView emotion_goal,picture_taken,visual_support;
    Button back, try_again;

    boolean correct_face;
    int pic_id;
    byte[] image_taken;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_task2_2);

        Intent intent = getIntent();
        correct_face = intent.getBooleanExtra("correct_face",false);
        pic_id = intent.getIntExtra("pic_ID",0);
        image_taken = intent.getByteArrayExtra("img_bytes");

        picture_taken = findViewById(R.id.picture_taken);
        picture_taken.setImageBitmap(BitmapFactory.decodeByteArray(image_taken,0,image_taken.length));

        emotion_goal = findViewById(R.id.imageView3);
        emotion_goal.setImageResource(pic_id);

        visual_support = findViewById(R.id.visual_support);
        if (correct_face) {
            visual_support.setImageResource(R.drawable.mickey);
        }
        else {
            visual_support.setImageResource(R.drawable.try_again_text);
        }
        clickOnButton();
    }

    private void clickOnButton(){
        try_again = (Button) findViewById(R.id.next_face);
        back = (Button) findViewById(R.id.back2_1);

        try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Task2_2.this, Task2_1.class);
                startActivity(it);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Task2_2.this, Task2_1_Setup.class);
                startActivity(it);
            }
        });
    }
}
