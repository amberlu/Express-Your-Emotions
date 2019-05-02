package com.example.jiananlu.expressyouremotions;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Task3_question extends AppCompatActivity {

    //private Button back, letsGo;
    private Button submit;
    private List<Integer[]> emotions;
    private ImageView upperleft;
    private ImageView upperright;
    private ImageView lower;
    private Integer[] happy_faces = {
            R.drawable.happy_1,
            R.drawable.happy_2
    };

    private Integer[] angry_faces = {
            R.drawable.angry_1
    };

    private Random index;
    private Context mContext;
    List<Integer> images;
    List<Boolean> correct_answers;
    private String correct_emotion;
    private Integer first;
    private Integer second;
    private Integer third;
    Map<String, String> defMap = new HashMap< String,String>();
    Map<String, Integer[]> drawableMap = new HashMap< String, Integer[]>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_task3_question);

        upperleft = findViewById(R.id.upper_left_img);
        upperright = findViewById(R.id.upper_right_img);
        lower = findViewById(R.id.lower_mid_img);

        // initialize the emotions list
        drawableMap.put("happy", happy_faces);
        drawableMap.put("angry", angry_faces);

        defMap.put("happy", "When you get what you want, you feel happy!");
        defMap.put("angry", "When something you don't like happens, you feel angry!");

        emotions = new ArrayList<Integer[]>(drawableMap.values());

//        emotions = new ArrayList<>();
//        emotions.add(happy_faces);
//        emotions.add(angry_faces);
//        emotions.add("angry");
//        emotions.add("bored");
//        emotions.add("happy");
//        emotions.add("sad");
//        emotions.add("scared");
//        emotions.add("surprised");



        // randomly pick the tested emotion from emotions list
//        String correct = randomItem(emotions);
//        emotions.remove(correct);
//        String wrong = randomItem(emotions);

        images = new ArrayList<>();
        correct_answers = new ArrayList<>();
        correct_emotion = "happy";
        Integer[] correct = happy_faces;
        Integer[] wrong = angry_faces;

        TextView hint = findViewById(R.id.hint);
        hint.setText("Pick all of the " + correct_emotion + " faces. Hint: There are 2!");

        // select 2 images randomly from the correct emotional category
        index = new Random();
        first = correct[index.nextInt(correct.length)];
        images.add(first);

        second = correct[index.nextInt(correct.length)];
        while (second == first) {
            second = correct[index.nextInt(correct.length)];
        }
        images.add(second);

        // select 1 images randomly from the wrong emotional category
        index = new Random();
        third = wrong[index.nextInt(wrong.length)];
        images.add(third);


        // shuffle images list
        Collections.shuffle(images);
        for (int image: images) {
            if (image == first || image == second) {
                correct_answers.add(true);
            }
            else {
                correct_answers.add(false);
            }
        }



        // render the corresponding text and images in the layout

        upperleft.setImageResource(images.get(0));
        upperright.setImageResource(images.get(1));
        lower.setImageResource(images.get(2));




        submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check user selection
                String msg = check_answers();

                if (msg == "correct") {
                    Intent it = new Intent(Task3_question.this, Task3_success.class);
                    it.putExtra("first", first);
                    it.putExtra("second", second);
                    it.putExtra("emotion", correct_emotion);
                    startActivity(it);
                }
                else {
                    Toast.makeText(getApplicationContext(), msg,Toast.LENGTH_SHORT).show();
                    //Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
        //clickOnButton();
    }

    private Integer[] randomItem(List<Integer[]> list) {
        int index = (int) ((Math.random() * list.size()));
        Integer[] item = list.get(index);
        return item;
    }

//    private void clickOnButton(){
////        back  = (Button) findViewById(R.id.b3_1);
////        letsGo = (Button) findViewById(R.id.b3_2);
//        submit = (Button) findViewById(R.id.submit);
//
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // check user selection
//                String msg = check_answers();
//
//                if (msg == "correct") {
//                    Intent it = new Intent(Task3_question.this, Task3_success.class);
//                    it.putExtra("first", first);
//                    it.putExtra("second", second);
//                    startActivity(it);
//                }
////                else {
////                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
////                }
//            }
//        });
////        letsGo.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Intent it = new Intent(Task3_question.this, Task3_question.class);
////                startActivity(it);
////            }
////        });
//    }

    private String check_answers(){
        int count = 0;
        List<Boolean> checked = new ArrayList<>();

        CheckBox upperleft_check = findViewById(R.id.upper_left_check);
        CheckBox upperright_check = findViewById(R.id.upper_right_check);
        CheckBox lower_check = findViewById(R.id.lower_mid_check);

        if (upperleft_check.isChecked()) {
            count += 1;
            checked.add(true);
        } else {
            checked.add(false);
        }

        if (upperright_check.isChecked()) {
            count += 1;
            checked.add(true);
        } else {
            checked.add(false);
        }

        if (lower_check.isChecked()) {
            count += 1;
            checked.add(true);
        } else {
            checked.add(false);
        }

        if (count != 2) {

            return "Select 2 faces!";
        }
        else {
            if (checked.equals(correct_answers)){
                return "correct";
            }
            else {
                return "Try Again";
            }
        }


    }
}

