package com.example.jiananlu.expressyouremotions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Task3_success extends AppCompatActivity {

    //private Button back, letsGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_task3_success);
        //clickOnButton();
    }

//    private void clickOnButton(){
//        back  = (Button) findViewById(R.id.b3_1);
//        letsGo = (Button) findViewById(R.id.b3_2);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent it = new Intent(Task3_question.this, Homepage.class);
//                startActivity(it);
//            }
//        });
//        letsGo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent it = new Intent(Task3_question.this, Task3_question.class);
//                startActivity(it);
//            }
//        });
//    }
}