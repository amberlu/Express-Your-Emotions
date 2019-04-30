package com.example.jiananlu.expressyouremotions;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Emotion;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.contract.VerifyResult;
import com.microsoft.projectoxford.face.rest.ClientException;

import com.microsoft.projectoxford.face.*;
import com.microsoft.projectoxford.face.contract.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.io.*;
import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.graphics.*;
import android.widget.*;
import android.provider.*;


public class EmotionRecogTestClass extends AppCompatActivity {

    private Button process;
    private ImageView filler;
    private Bitmap bitmap;

    private final int PICK_IMAGE = 1;
    private ProgressDialog detectionProgressDialog;

    private final String apiKey = "2d1dbe92587345da9bf698b0d221beb0";
    private final String apiEndpoint = "https://westus.api.cognitive.microsoft.com/face/v1.0/";
//
    private final FaceServiceClient faceServiceClient = new FaceServiceRestClient(apiEndpoint,apiKey);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emotionrecog_test);
//        try
//        {
//            this.getSupportActionBar().hide();
//        }
//        catch (NullPointerException e){e.printStackTrace();}

        process = findViewById(R.id.process);
        filler = findViewById(R.id.emo_filler);

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.sad_baby);
        filler.setImageBitmap(bitmap);

        process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/*");
//                startActivityForResult(Intent.createChooser(
//                        intent, "Select Picture"), PICK_IMAGE);
                detectAndFrame(bitmap);
            }
        });


        detectionProgressDialog = new ProgressDialog(this);
    }


//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK &&
//                data != null && data.getData() != null) {
//            Uri uri = data.getData();
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(
//                        getContentResolver(), uri);
//                ImageView imageView = findViewById(R.id.emo_filler);
//                imageView.setImageBitmap(bitmap);
//
//                // Comment out for tutorial
//                detectAndFrame(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    private void detectAndFrame(final Bitmap imageBitmap) {

        filler = (ImageView) findViewById(R.id.emo_filler);
        filler.setImageBitmap(imageBitmap);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG,100, outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream((outputStream.toByteArray()));
        AsyncTask<InputStream,String, Face[]> detectTask =  new AsyncTask<InputStream, String, Face[]>() {

            String exceptionMessage = "";
            @Override
            protected Face[] doInBackground(InputStream... inputStreams) {

                try {
                    publishProgress("Evaluating...");
                    Face[] result = faceServiceClient.detect(
                            inputStreams[0],
                            true,         // returnFaceId
                            false,        // returnFaceLandmarks
                            null          // returnFaceAttributes:
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


                }
                catch (Exception e) {
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

                if(!exceptionMessage.equals("")){
                    showError(exceptionMessage);
                }
                if (result == null) return;

                ImageView imageView = findViewById(R.id.emo_filler);
                imageView.setImageBitmap(
                        drawFaceRectanglesOnBitmap(imageBitmap, result));
                imageBitmap.recycle();
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
            Bitmap originalBitmap, Face[] faces) {
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
}
