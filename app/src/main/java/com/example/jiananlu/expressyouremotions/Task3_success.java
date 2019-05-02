package com.example.jiananlu.expressyouremotions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Task3_success extends AppCompatActivity {

    private Button home, next;
    private Integer first, second;
    private ImageView first_img, second_img;
    private TextView def_text;
    private String def;

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
        def = intent.getStringExtra("def");


        if (first != 0) {
            first_img = findViewById(R.id.first);
            first_img.setImageResource(first);
        }

        if (second != 0) {
            second_img = findViewById(R.id.second);
            second_img.setImageResource(second);
        }

        if (def != null) {
            def_text = findViewById(R.id.msg_def);
            def_text.setText(def);

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
//        letsGo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent it = new Intent(Task3_question.this, Task3_question.class);
//                startActivity(it);
//            }
//        });
    }
}