package com.example.jiananlu.expressyouremotions;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Emotion;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.contract.FaceAttribute;
import com.microsoft.projectoxford.face.contract.FaceRectangle;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Task2_1 extends AppCompatActivity {

    private String cont;
    private Integer pic_id;
    private Bitmap global_bitmap;

    private ImageView emotion_image_display;
    private TextView emotion_text_display;

    private final int PICK_IMAGE = 1;

    private String cur_emotion;
    private Button back, takePic, next_button;
    private ImageView textureView;
    boolean cleared = false;

    public Uri imageUri;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_task2_1);

        Intent intent = getIntent();
        cont = intent.getStringExtra("content");
        pic_id = intent.getIntExtra("pic_id", 0);

        emotion_image_display = findViewById(R.id.imageView3);
        emotion_image_display.setImageResource(pic_id);

        emotion_text_display = findViewById(R.id.textView6);
        emotion_text_display.setText("Make a " +cont+ " face!");

        textureView = findViewById(R.id.textureView);

        next_button = findViewById(R.id.next_button123);

        clickOnButton();
    }

    private void clickOnButton(){
        back  = (Button) findViewById(R.id.back2_1);
        takePic = (Button) findViewById(R.id.takePic_2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Task2_1.this, Task2_1_Setup.class);
                startActivity(it);
            }
        });
        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cleared) {
                    Intent intent = new Intent(Task2_1.this, Task2_2.class);
                    intent.putExtra("content",cont);
                    intent.putExtra("pic_id",pic_id);
//                    intent.putExtra("img_bitmap",global_bitmap);
                    intent.putExtra("img_path",mCurrentPhotoPath);
                    startActivity(intent);

                } else {
                    Toast.makeText(Task2_1.this, "Please take a picture!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap imageBitmap = BitmapFactory.decodeFile(
                mCurrentPhotoPath);
        global_bitmap = imageBitmap;


        cleared = true;
        textureView.setImageBitmap(imageBitmap);
    }

    // Save image as a file, with a given name path based on time/date
    // From Android Docs
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

//    private void detectAndFrame() {
//
////        textureView = (ImageView) findViewById(R.id.textureView);
////        textureView.setImageBitmap(imageBitmap);
//
//        while (global_bitmap == null) {
//
//        }
//
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        global_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
//
//        final FaceServiceClient.FaceAttributeType[] requiredFaceAttributes = new FaceServiceClient.FaceAttributeType[1];
//        requiredFaceAttributes[0] = FaceServiceClient.FaceAttributeType.Emotion;
//        AsyncTask<InputStream, String, Face[]> detectTask = new AsyncTask<InputStream, String, Face[]>() {
//
//            String exceptionMessage = "";
//
//            @Override
//            protected Face[] doInBackground(InputStream... inputStreams) {
//                try {
//                    publishProgress("Evaluating...");
//                    Face[] result = faceServiceClient.detect(
//                            inputStreams[0],
//                            true,         // returnFaceId
//                            false,        // returnFaceLandmarks
//                            requiredFaceAttributes          // returnFaceAttributes:
//                                        /* new FaceServiceClient.FaceAttributeType[] {
//                                            FaceServiceClient.FaceAttributeType.Age,
//                                            FaceServiceClient.FaceAttributeType.Gender }
//                                        */
//                    );
//                    if (result == null) {
//                        publishProgress("Evaluation Finished, sadly nothing detected");
//                        return null;
//                    }
//                    publishProgress(String.format("Detection finished. %d face(s) detected", result.length));
//                    return result;
//                } catch (Exception e) {
//                    exceptionMessage = String.format(
//                            "Detection failed: %s", e.getMessage());
//                    return null;
//                }
//            }
//
//            @Override
//            protected void onPreExecute() {
//                //TODO: show progress dialog
//                detectionProgressDialog.show();
//            }
//
//            @Override
//            protected void onProgressUpdate(String... progress) {
//                //TODO: update progress
//                detectionProgressDialog.setMessage(progress[0]);
//            }
//
//            @Override
//            protected void onPostExecute(Face[] result) {
//                //TODO: update face frames
//                detectionProgressDialog.dismiss();
//
//                if (!exceptionMessage.equals("")) {
//                    showError(exceptionMessage);
//                }
//                if (result == null) return;
//
//                String emotion = getEmotion(result);
//                cur_emotion = emotion;
//                if (emotion.toLowerCase() == cont.toLowerCase()) {
//                    correct_face = true;
//                }
//                ImageView imageView = findViewById(R.id.textureView);
//                imageView.setImageBitmap(
//                        drawFaceRectanglesOnBitmap(global_bitmap, result, emotion));
//                global_bitmap.recycle();
//                Toast.makeText(Task2_1.this, emotion, Toast.LENGTH_SHORT).show();
//            }
//        };
//        detectTask.execute(inputStream);
//
//        Intent intent = new Intent(Task2_1.this, Task2_2.class);
////        intent.putExtra("emotion_passed",cur_emotion);
//        intent.putExtra("correct_face",correct_face);
//        intent.putExtra("pic_ID",pic_id);
//        intent.putExtra("img_bitmap",global_bitmap);
//        startActivity(intent);
//    }

//    private void showError(String message) {
//        new AlertDialog.Builder(this)
//                .setTitle("Error")
//                .setMessage(message)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                    }})
//                .create().show();
//    }
//
//    private static Bitmap drawFaceRectanglesOnBitmap(
//            Bitmap originalBitmap, Face[] faces,String emotion) {
//        Bitmap bitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
//        Canvas canvas = new Canvas(bitmap);
//        Paint paint = new Paint();
//        paint.setAntiAlias(true);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setColor(Color.RED);
//        paint.setStrokeWidth(10);
//        if (faces != null) {
//            for (Face face : faces) {
//                FaceRectangle faceRectangle = face.faceRectangle;
//                canvas.drawRect(
//                        faceRectangle.left,
//                        faceRectangle.top,
//                        faceRectangle.left + faceRectangle.width,
//                        faceRectangle.top + faceRectangle.height,
//                        paint);
//            }
//        }
//
//        return bitmap;
//    }
//
//    private static String getEmotion(Face[] v_res) {
//
//        FaceAttribute faceAttributes = v_res[0].faceAttributes;
//        Emotion cur_face_emotion = faceAttributes.emotion;
//
//        List<Double> emo_scores = new ArrayList<>();
//        emo_scores.add(cur_face_emotion.sadness);
//        emo_scores.add(cur_face_emotion.happiness);
//        emo_scores.add(cur_face_emotion.anger);
//        emo_scores.add(cur_face_emotion.surprise);
//        emo_scores.add(cur_face_emotion.fear);
//        emo_scores.add(cur_face_emotion.neutral);
//        emo_scores.add(cur_face_emotion.disgust);
//        emo_scores.add(cur_face_emotion.contempt);
//
//        Collections.sort(emo_scores);
//        double expressed_emotion = emo_scores.get(emo_scores.size() - 1);
//
//        if (expressed_emotion == cur_face_emotion.sadness) {
//            return "Sadness";
//        } else if (expressed_emotion == cur_face_emotion.happiness) {
//            return "Happy";
//        } else if (expressed_emotion == cur_face_emotion.anger) {
//            return "Angry";
//        } else if (expressed_emotion == cur_face_emotion.surprise) {
//            return "Surprised";
//        } else if (expressed_emotion == cur_face_emotion.fear) {
//            return "Scared";
//        } else if (expressed_emotion == cur_face_emotion.disgust) {
//            return "Disgusted";
//        } else if (expressed_emotion == cur_face_emotion.contempt) {
//            return "Upset";
//        } else if (expressed_emotion == cur_face_emotion.neutral) {
//            return "Bored";
//        }
//        return "Could not determine emotion";
//    }


}