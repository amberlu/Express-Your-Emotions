package com.example.jiananlu.expressyouremotions;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Task2_1_Setup extends AppCompatActivity{


    private Button angry, happy, sad, scared, bored, surprised, home;

    Map<String, String> emotions = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_task2_1_setup);
        loadEmotions();
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
                Intent it = new Intent(Task2_1_Setup.this, Task2_1.class);
                it.putExtra("content", emotions.get("angry"));
                System.out.println("angry:"+emotions.get("angry"));
                it.putExtra("pic_id", R.drawable.task1_angry);
                System.out.println("this id:"+R.drawable.task1_angry);
                startActivity(it);
            }
        });
        happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Task2_1_Setup.this, Task2_1.class);
                it.putExtra("content", "happy");
                it.putExtra("pic_id", R.drawable.happy);
                startActivity(it);
            }
        });
        sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Task2_1_Setup.this, Task2_1.class);
                it.putExtra("content", "sad");
                it.putExtra("pic_id", R.drawable.sad);
                startActivity(it);
            }
        });
        scared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Task2_1_Setup.this, Task2_1.class);
                it.putExtra("content", "scared");
                it.putExtra("pic_id", R.drawable.scared);
                startActivity(it);
            }
        });
        bored.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Task2_1_Setup.this, Task2_1.class);
                it.putExtra("content","bored");
                it.putExtra("pic_id", R.drawable.bored);
                startActivity(it);
            }
        });
        surprised.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Task2_1_Setup.this, Task2_1.class);
                it.putExtra("content", "surprised");
                it.putExtra("pic_id", R.drawable.surprised);
                startActivity(it);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Task2_1_Setup.this, Homepage.class);
                startActivity(it);
            }
        });
    }


    void loadEmotions(){
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray emos=obj.getJSONArray("emotions");
            for (int i=0;i<emos.length();i++) {
                JSONObject jsonObject = emos.getJSONObject(i);
                String picture = jsonObject.getString("picture");
                String content = jsonObject.getString("content");
                emotions.put(picture, content);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("emotions.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}







