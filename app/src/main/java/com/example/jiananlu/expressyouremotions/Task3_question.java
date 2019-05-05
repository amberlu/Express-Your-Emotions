package com.example.jiananlu.expressyouremotions;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
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
    private GridLayout mainGrid;
    private Button submit;
    private Random index;
    private List<Integer[]> emotions;
    List<Integer> images;
    List<Boolean> correct_answers;
    private String correct_emotion;
    private final Integer num_correct_answers = 3;
    private ImageView upperleft, upperright, lowerleft, lowerright;
    private Integer first, second, third, fourth;
    Map<String, Integer[]> drawableMap = new HashMap< String, Integer[]>();
    private Integer[] happy_faces = {
            R.drawable.happy_1,
            R.drawable.happy_2,
            R.drawable.happy_3,
    };
    private Integer[] angry_faces = {
            R.drawable.angry_1
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_task3_question);

        upperleft = findViewById(R.id.img_1);
        upperright = findViewById(R.id.img_2);
        lowerleft = findViewById(R.id.img_3);
        lowerright = findViewById(R.id.img_4);

        // initialize the emotions list
        drawableMap.put("happy", happy_faces);
        drawableMap.put("angry", angry_faces);

        emotions = new ArrayList<Integer[]>(drawableMap.values());
        // randomly pick the tested emotion from emotions
//        String correct = randomItem(emotions);
//        emotions.remove(correct);
//        String wrong = randomItem(emotions);

        images = new ArrayList<>(); // images is a list of three selected drawable

        // TODO: remove static selection after having more emotional images
        correct_emotion = "happy";
        Integer[] correct = happy_faces;
        Integer[] wrong = angry_faces;

        // select 3 images randomly from the correct emotional category
        index = new Random();
        first = correct[index.nextInt(correct.length)];
        images.add(first);

        second = correct[index.nextInt(correct.length)];
        while (images.contains(second)) {
            second = correct[index.nextInt(correct.length)];
        }
        images.add(second);

        third = correct[index.nextInt(correct.length)];
        while (images.contains(third)) {
            third = correct[index.nextInt(correct.length)];
        }
        images.add(third);

        // select 1 images randomly from the wrong emotional category
        index = new Random();
        fourth = wrong[index.nextInt(wrong.length)];
        images.add(fourth);

        // correct_answers is list of three boolean variables indicating
        // whether the upper left, upper right, lower left, lower right image is a correct answer or not
        correct_answers = new ArrayList<>();

        // shuffle images list
        Collections.shuffle(images);
        for (int image: images) {
            // fill in correct_answer
            if (image == first || image == second || image == third) {
                correct_answers.add(true);
            }
            else {
                correct_answers.add(false);
            }
        }

        // render the corresponding text and images in the layout
        upperleft.setImageResource(images.get(0));
        upperright.setImageResource(images.get(1));
        lowerleft.setImageResource(images.get(2));
        lowerright.setImageResource(images.get(3));

        TextView hint = findViewById(R.id.hint);
        hint.setText("Pick all of the " + correct_emotion + " faces. Hint: There are " + num_correct_answers +"!");

        // detect image selection
        mainGrid = findViewById(R.id.mainGrid);
        gridEvent(mainGrid);

        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String msg = check_answers();

                // if correct, proceed to the success page
                if (msg == "correct") {
                    Intent it = new Intent(Task3_question.this, Task3_success.class);
                    it.putExtra("first", first);
                    it.putExtra("second", second);
                    it.putExtra("third", third);
                    it.putExtra("emotion", correct_emotion);
                    startActivity(it);
                }
                // else, stay in the current page and display helpful messages
                else {
                    Toast.makeText(getApplicationContext(), msg,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void gridEvent(GridLayout mainGrid) {
        // iterate through all images contained in the mainGrid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            final CardView child = (CardView) mainGrid.getChildAt(i);
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (child.getCardBackgroundColor().getDefaultColor() == -1) {
                        child.setCardBackgroundColor(Color.parseColor("#FFCAEA"));
                    }
                    else {
                        child.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    }
                    //Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private Integer[] randomItem(List<Integer[]> list) {
        int index = (int) ((Math.random() * list.size()));
        Integer[] item = list.get(index);
        return item;
    }

    private String check_answers(){
        int count = 0;
        List<Boolean> checked = new ArrayList<>();
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            final CardView child = (CardView) mainGrid.getChildAt(i);
            if (child.getCardBackgroundColor().getDefaultColor() != -1) {
                count += 1;
                checked.add(true);
            } else {
                checked.add(false);
            }
        }

        if (count != num_correct_answers) {
            return "Select " + num_correct_answers + " Faces!";
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

