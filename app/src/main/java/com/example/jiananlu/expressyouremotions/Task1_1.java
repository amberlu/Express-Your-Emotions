package com.example.jiananlu.expressyouremotions;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Task1_1 extends AppCompatActivity {

    private ImageView angry, happy, sad, scared, bored, surprised;
    private Button home;

    Map<String, String> emotions = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_task1_1);
        loadEmotions();
        clickOnButton();
    }

    public void clickOnButton(){
        angry  = (ImageView) findViewById(R.id.angry);
        happy = (ImageView) findViewById(R.id.happy);
        sad  = (ImageView) findViewById(R.id.sad);
        scared = (ImageView) findViewById(R.id.scared);
        bored  = (ImageView) findViewById(R.id.bored);
        surprised = (ImageView) findViewById(R.id.surprised);
        home = (Button) findViewById(R.id.task1_home);
        angry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Task1_1.this, Task1_emotion.class);
                it.putExtra("content", emotions.get("angry"));
                System.out.println("angry:"+emotions.get("angry"));
                it.putExtra("pic_id", R.drawable.angry);
                System.out.println("this id:"+R.drawable.angry);
                startActivity(it);
            }
        });
        happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Task1_1.this, Task1_emotion.class);
                it.putExtra("content", emotions.get("happy"));
                it.putExtra("pic_id", R.drawable.happy);
                startActivity(it);
            }
        });
        sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Task1_1.this, Task1_emotion.class);
                it.putExtra("content", emotions.get("sad"));
                it.putExtra("pic_id", R.drawable.sad);
                startActivity(it);
            }
        });
        scared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Task1_1.this, Task1_emotion.class);
                it.putExtra("content", emotions.get("scared"));
                it.putExtra("pic_id", R.drawable.scared);
                startActivity(it);
            }
        });
        bored.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Task1_1.this, Task1_emotion.class);
                it.putExtra("content", emotions.get("bored"));
                it.putExtra("pic_id", R.drawable.bored);
                startActivity(it);
            }
        });
        surprised.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Task1_1.this, Task1_emotion.class);
                it.putExtra("content", emotions.get("surprised"));
                it.putExtra("pic_id", R.drawable.surprised);
                startActivity(it);
            }
        });
//        angry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent it = new Intent(Task1_1.this, Angry.class);
//                it.putExtra("content", emotions.get("angry"));
//                it.putExtra("pic_id", R.drawable.task1_angry);
//                startActivity(it);
//            }
//        });
//        happy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent it = new Intent(Task1_1.this, Happy.class);
//                it.putExtra("content", emotions.get("happy"));
//                it.putExtra("pic_id", R.drawable.happy);
//                startActivity(it);
//            }
//        });
//        sad.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent it = new Intent(Task1_1.this, Sad.class);
//                it.putExtra("content", emotions.get("sad"));
//                it.putExtra("pic_id", R.drawable.sad);
//                startActivity(it);
//            }
//        });
//        scared.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent it = new Intent(Task1_1.this, Scared.class);
//                it.putExtra("content", emotions.get("scared"));
//                it.putExtra("pic_id", R.drawable.scared);
//                startActivity(it);
//            }
//        });
//        bored.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent it = new Intent(Task1_1.this, Bored.class);
//                it.putExtra("content", emotions.get("bored"));
//                it.putExtra("pic_id", R.drawable.bored);
//                startActivity(it);
//            }
//        });
//        surprised.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent it = new Intent(Task1_1.this, Surprised.class);
//                it.putExtra("content", emotions.get("surprised"));
//                it.putExtra("pic_id", R.drawable.surprised);
//                startActivity(it);
//            }
//        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Task1_1.this, Homepage.class);
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
