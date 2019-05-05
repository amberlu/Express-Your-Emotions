package com.example.jiananlu.expressyouremotions;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Task3_success extends AppCompatActivity {

    private Button home, next;
    private Integer first, second, third;
    private ImageView first_img, second_img, third_img;
    private TextView def_text, hint;
    private String emotion;
    private String def_json_path = "emotions_def.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_task3_success);

        Intent intent = getIntent();
        first = intent.getIntExtra("first", 0);
        second = intent.getIntExtra("second", 0);
        third = intent.getIntExtra("third", 0);
        emotion = intent.getStringExtra("emotion");

        String def = readFromJSON(def_json_path);

        if (first != 0) {
            first_img = findViewById(R.id.img_1);
            first_img.setImageResource(first);
        }

        if (second != 0) {
            second_img = findViewById(R.id.img_2);
            second_img.setImageResource(second);
        }

        if (third != 0) {
            third_img = findViewById(R.id.img_3);
            third_img.setImageResource(third);
        }

        if (def != null) {
            def_text = findViewById(R.id.msg_def);
            def_text.setText(def);

            hint = findViewById(R.id.hint);
            hint.setText("The " + emotion + " faces are:");
        }

        clickOnButton();
    }


    private void clickOnButton(){
        home  = (Button) findViewById(R.id.home);
        next = (Button) findViewById(R.id.next);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Task3_success.this, Homepage.class);
                startActivity(it);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Task3_success.this, Task3_question.class);
                startActivity(it);
            }
        });
    }
    
    private String readFromJSON(String filepath) {
        try {
            InputStream is = this.getAssets().open(filepath);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            try {
                JSONArray list = new JSONArray(new String(buffer, "UTF-8"));
                for (int i = 0; i < list.length(); i++) {
                    JSONObject item = list.getJSONObject(i);

                    String name = item.getString("picture");
                    String def = item.getString("content");

                    if (name.equals(emotion)) {
                        return def;
                    }
                }
            } catch (JSONException e) {
                Log.e("json", "error while reading emotions.json " + e.toString());
            }
        } catch (IOException e) {
            Log.e("json", "error reading json file " + e);
        }
        return null;
    }
}