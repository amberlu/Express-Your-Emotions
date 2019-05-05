package com.example.jiananlu.expressyouremotions;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Emotion;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.contract.FaceAttribute;
import com.microsoft.projectoxford.face.contract.FaceRectangle;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Task2_2 extends AppCompatActivity {

    ImageView emotion_goal,picture_taken,visual_support;
    Button back, try_again;

    boolean correct_face;
    int pic_id;
    Bitmap image_taken;
    String emotion_should_be;

    private ProgressDialog detectionProgressDialog;
    private final String apiKey = "2d1dbe92587345da9bf698b0d221beb0";
    private final String apiEndpoint = "https://westus.api.cognitive.microsoft.com/face/v1.0/";
    private final FaceServiceClient faceServiceClient = new FaceServiceRestClient(apiEndpoint,apiKey);


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

        emotion_should_be = intent.getStringExtra("content");
        pic_id = intent.getIntExtra("pic_ID",0);
        image_taken = (Bitmap) intent.getParcelableExtra("img_bitmap");

        picture_taken = findViewById(R.id.picture_taken);
        picture_taken.setImageBitmap(image_taken);

        emotion_goal = findViewById(R.id.imageView3);
        emotion_goal.setImageResource(pic_id);

//        visual_support = findViewById(R.id.visual_support);
//        if (correct_face) {
//            visual_support.setImageResource(R.drawable.mickey);
//        }
//        else {
//            visual_support.setImageResource(R.drawable.try_again_text);
//        }
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

    private void detectAndFrame(final Bitmap img_bitmap_new) {

//        textureView = (ImageView) findViewById(R.id.textureView);
//        textureView.setImageBitmap(imageBitmap);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        img_bitmap_new.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        final FaceServiceClient.FaceAttributeType[] requiredFaceAttributes = new FaceServiceClient.FaceAttributeType[1];
        requiredFaceAttributes[0] = FaceServiceClient.FaceAttributeType.Emotion;
        AsyncTask<InputStream, String, Face[]> detectTask = new AsyncTask<InputStream, String, Face[]>() {

            String exceptionMessage = "";

            @Override
            protected Face[] doInBackground(InputStream... inputStreams) {
                try {
                    publishProgress("Evaluating...");
                    Face[] result = faceServiceClient.detect(
                            inputStreams[0],
                            true,         // returnFaceId
                            false,        // returnFaceLandmarks
                            requiredFaceAttributes          // returnFaceAttributes:
                                        /* new FaceServiceClient.FaceAttributeType[] {
                                            FaceServiceClient.FaceAttributeType.Age,
                                            FaceServiceClient.FaceAttributeType.Gender }
                                        */
                    );
                    if (result == null) {
                        publishProgress("Evaluation Finished, sadly nothing detected");
                        return null;
                    }
                    publishProgress(String.format("Detection finished. %d face(s) detected", result.length));
                    return result;
                } catch (Exception e) {
                    exceptionMessage = String.format(
                            "Detection failed: %s", e.getMessage());
                    return null;
                }
            }

            @Override
            protected void onPreExecute() {
                //TODO: show progress dialog
                detectionProgressDialog.show();
            }

            @Override
            protected void onProgressUpdate(String... progress) {
                //TODO: update progress
                detectionProgressDialog.setMessage(progress[0]);
            }

            @Override
            protected void onPostExecute(Face[] result) {
                //TODO: update face frames
                detectionProgressDialog.dismiss();

                if (!exceptionMessage.equals("")) {
                    showError(exceptionMessage);
                }
                if (result == null) return;

                String emotion = getEmotion(result);
//                cur_emotion = emotion;
//                if (emotion.toLowerCase() == cont.toLowerCase()) {
//                    correct_face = true;
//                }
                ImageView imageView = findViewById(R.id.textureView);
                imageView.setImageBitmap(
                        drawFaceRectanglesOnBitmap(img_bitmap_new, result, emotion));
                img_bitmap_new.recycle();
                Toast.makeText(Task2_2.this, emotion, Toast.LENGTH_SHORT).show();
            }
        };
        detectTask.execute(inputStream);
    }


    private void showError(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }})
                .create().show();
    }

    private static Bitmap drawFaceRectanglesOnBitmap(
            Bitmap originalBitmap, Face[] faces, String emotion) {
        Bitmap bitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);
        if (faces != null) {
            for (Face face : faces) {
                FaceRectangle faceRectangle = face.faceRectangle;
                canvas.drawRect(
                        faceRectangle.left,
                        faceRectangle.top,
                        faceRectangle.left + faceRectangle.width,
                        faceRectangle.top + faceRectangle.height,
                        paint);
            }
        }

        return bitmap;
    }

    private static String getEmotion(Face[] v_res) {

        FaceAttribute faceAttributes = v_res[0].faceAttributes;
        Emotion cur_face_emotion = faceAttributes.emotion;

        List<Double> emo_scores = new ArrayList<>();
        emo_scores.add(cur_face_emotion.sadness);
        emo_scores.add(cur_face_emotion.happiness);
        emo_scores.add(cur_face_emotion.anger);
        emo_scores.add(cur_face_emotion.surprise);
        emo_scores.add(cur_face_emotion.fear);
        emo_scores.add(cur_face_emotion.neutral);
        emo_scores.add(cur_face_emotion.disgust);
        emo_scores.add(cur_face_emotion.contempt);

        Collections.sort(emo_scores);
        double expressed_emotion = emo_scores.get(emo_scores.size() - 1);

        if (expressed_emotion == cur_face_emotion.sadness) {
            return "Sadness";
        } else if (expressed_emotion == cur_face_emotion.happiness) {
            return "Happy";
        } else if (expressed_emotion == cur_face_emotion.anger) {
            return "Angry";
        } else if (expressed_emotion == cur_face_emotion.surprise) {
            return "Surprised";
        } else if (expressed_emotion == cur_face_emotion.fear) {
            return "Scared";
        } else if (expressed_emotion == cur_face_emotion.disgust) {
            return "Disgusted";
        } else if (expressed_emotion == cur_face_emotion.contempt) {
            return "Upset";
        } else if (expressed_emotion == cur_face_emotion.neutral) {
            return "Bored";
        }
        return "Could not determine emotion";
    }

}
