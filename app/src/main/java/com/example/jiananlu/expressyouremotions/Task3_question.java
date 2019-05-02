package com.example.jiananlu.expressyouremotions;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Task3_question extends AppCompatActivity {

    //private Button back, letsGo;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_task3_question);
        clickOnButton();
    }

    private void clickOnButton(){
//        back  = (Button) findViewById(R.id.b3_1);
//        letsGo = (Button) findViewById(R.id.b3_2);
        submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Task3_question.this, Task3_success.class);
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

