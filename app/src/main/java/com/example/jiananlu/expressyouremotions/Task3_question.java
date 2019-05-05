package com.example.jiananlu.expressyouremotions;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
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
    List<Integer> images;
    List<Boolean> correct_answers;
    private String correct_emotion;
    private final Integer num_correct_answers = 3;
    private ImageView upperleft, upperright, lowerleft, lowerright;
    private Integer first, second, third, fourth;
    Map<String, Integer[]> drawableMap = new HashMap< String, Integer[]>();
    // image source: https://www.pinclipart.com/
    private Integer[] happy_faces = {
            R.drawable.happy_1,
            R.drawable.happy_2,
            R.drawable.happy_3,
            R.drawable.happy_4,
            R.drawable.happy_5,
            R.drawable.happy_6,
            R.drawable.happy_7,
            R.drawable.happy_8,
            R.drawable.happy_9,
            R.drawable.happy_10,
            R.drawable.happy_11,
            R.drawable.happy_12,
            R.drawable.happy_13,
            R.drawable.happy_14,
            R.drawable.happy_15,
            R.drawable.happy_16,
            R.drawable.happy_17,
            R.drawable.happy_18,
            R.drawable.happy_19,
            R.drawable.happy_20
    };
    private Integer[] angry_faces = {
            R.drawable.angry_1,
            R.drawable.angry_2,
            R.drawable.angry_3,
            R.drawable.angry_4,
            R.drawable.angry_5,
            R.drawable.angry_6,
            R.drawable.angry_7,
            R.drawable.angry_8,
            R.drawable.angry_9,
            R.drawable.angry_10,
            R.drawable.angry_11,
            R.drawable.angry_12,
            R.drawable.angry_13,
            R.drawable.angry_14,
            R.drawable.angry_15
    };
    private Integer[] sad_faces = {
            R.drawable.sad_1,
            R.drawable.sad_2,
            R.drawable.sad_3,
            R.drawable.sad_4,
            R.drawable.sad_5,
            R.drawable.sad_6,
            R.drawable.sad_7,
            R.drawable.sad_8,
            R.drawable.sad_9,
            R.drawable.sad_10,
            R.drawable.sad_11,
            R.drawable.sad_12,
            R.drawable.sad_13,
            R.drawable.sad_14,
            R.drawable.sad_15
    };
    private Integer[] bored_faces = {
            R.drawable.bored_1,
            R.drawable.bored_2,
            R.drawable.bored_3,
            R.drawable.bored_4,
            R.drawable.bored_5,
            R.drawable.bored_6,
            R.drawable.bored_7
    };
    private Integer[] surprised_faces = {
            R.drawable.surprised_1,
            R.drawable.surprised_2,
            R.drawable.surprised_3,
            R.drawable.surprised_4,
            R.drawable.surprised_5,
            R.drawable.surprised_6,
            R.drawable.surprised_7,
            R.drawable.surprised_8,
            R.drawable.surprised_9,
            R.drawable.surprised_11
    };
    private Integer[] scared_faces = {
            R.drawable.scared_1,
            R.drawable.scared_2,
            R.drawable.scared_3,
            R.drawable.scared_4,
            R.drawable.scared_5,
            R.drawable.scared_6,
            R.drawable.scared_7,
            R.drawable.scared_8,
            R.drawable.scared_9
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

        // initialize the emotion map
        drawableMap.put("happy", happy_faces);
        drawableMap.put("angry", angry_faces);
        drawableMap.put("sad", sad_faces);
        drawableMap.put("bored", bored_faces);
        drawableMap.put("surprised", surprised_faces);
        drawableMap.put("scared", scared_faces);

        // randomly pick the tested emotion from emotions
        List<String> emotion_str = new ArrayList<String>(drawableMap.keySet());
        String correct_str = randomItem(emotion_str);
        emotion_str.remove(correct_str);
        String wrong_str = randomItem(emotion_str);

        images = new ArrayList<>(); // images is a list of three selected drawable

        correct_emotion = correct_str;
        Integer[] correct = drawableMap.get(correct_emotion);
        Integer[] wrong = drawableMap.get(wrong_str);

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
        //hint.setBackgroundColor(Color.parseColor("#FFCAEA"));

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
                    // make the toast message with bigger font size and pink background
                    SpannableStringBuilder biggerText = new SpannableStringBuilder(msg);
                    biggerText.setSpan(new RelativeSizeSpan(2.0f), 0, msg.length(), 0);
                    Toast toast = Toast.makeText(getApplicationContext(), biggerText, Toast.LENGTH_SHORT);
                    View toast_view = toast.getView();
                    toast_view.getBackground().setColorFilter(Color.parseColor("#FFCAEA"), PorterDuff.Mode.SRC_IN);
                    toast.show();
                    //Toast.makeText(getApplicationContext(), msg,Toast.LENGTH_SHORT).show();
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

    private String randomItem(List<String> list) {
        int index = (int) ((Math.random() * list.size()));
        return list.get(index);
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

