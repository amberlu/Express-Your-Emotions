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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Task2_1 extends AppCompatActivity {

    private String cont;
    private Integer pic_id;

    private ImageView emotion_image_display;
    private TextView emotion_text_display;
    private Bitmap bitmap;

    private final int PICK_IMAGE = 1;
    private ProgressDialog detectionProgressDialog;
    private final String apiKey = "2d1dbe92587345da9bf698b0d221beb0";
    private final String apiEndpoint = "https://westus.api.cognitive.microsoft.com/face/v1.0/";
    private final FaceServiceClient faceServiceClient = new FaceServiceRestClient(apiEndpoint,apiKey);

    private String cur_emotion;
    private Button back, takePic;
    private TextureView textureView;
    private static final SparseIntArray orientations = new SparseIntArray();
    static {
        orientations.append(Surface.ROTATION_0,90);
        orientations.append(Surface.ROTATION_90,0);
        orientations.append(Surface.ROTATION_180,270);
        orientations.append(Surface.ROTATION_270,180);
    }

    private String camID;
    private CameraDevice cameraDevice;
    private CameraCaptureSession captureSessions;
    private CaptureRequest.Builder captureBuilder;
    private Size imageSize;
    private Image imageReader;

    private File file;
    private static final int camera_perm = 200;
    private Handler backgroundHanlder;
    private HandlerThread backgroundThread;
    private CameraDevice.StateCallback resultCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
            cameraPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {

            cameraDevice.close();
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };

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
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.sad_baby);
        detectionProgressDialog = new ProgressDialog(this);
        assert textureView !=null;
        textureView.setSurfaceTextureListener(textListener);
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
//                Intent it = new Intent(Task2_1.this, Task2.class);
//                startActivity(it);
                takePicture();

            }
        });
    }

    private void startCamera() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            camID = manager.getCameraIdList()[0];
            CameraCharacteristics chars = manager.getCameraCharacteristics(camID);
            StreamConfigurationMap mapper = chars.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert mapper != null;
            imageSize = mapper.getOutputSizes(SurfaceTexture.class)[0];

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, camera_perm);
                return;
            }
            manager.openCamera(camID, resultCallback, null);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }



    }

    private void takePicture() {

        if (cameraDevice == null) {
            return;
        }
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            CameraCharacteristics characters =
                    manager.getCameraCharacteristics(cameraDevice.getId());
            Size[] picSizes = null;
            if (characters !=null) {
                picSizes = characters.get
                        (CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }

            int width = 640;
            int height = 480;
            if (picSizes != null && picSizes.length > 0) {
                width = picSizes[0].getWidth();
                height = picSizes[0].getHeight();
            }

            final ImageReader imgReader = ImageReader.newInstance(width,height,ImageFormat.JPEG,1);
            List<Surface> imageOut = new ArrayList<>(2);
            imageOut.add(imgReader.getSurface());

            imageOut.add(new Surface(textureView.getSurfaceTexture()));
            final CaptureRequest.Builder builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            builder.addTarget(imgReader.getSurface());
            builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

            int orientation = getWindowManager().getDefaultDisplay().getRotation();
            builder.set(CaptureRequest.JPEG_ORIENTATION,orientations.get(orientation));
            file = new File(Environment.getExternalStorageDirectory() + "/"+UUID.randomUUID().toString() + ".jpg");
            ImageReader.OnImageAvailableListener readingListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {

                    Image image = null;
                    try {
                        image = imgReader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] imgBytes = new byte[buffer.capacity()];
                        buffer.get(imgBytes);
                        save(imgBytes);
                    }
                    catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    finally {
                        if (image != null) {
                            image.close();
                        }
                    }
                }

                private void save (byte[] bytes) throws IOException {
                    OutputStream outputStream = null;
                    try {
                        outputStream = new FileOutputStream(file);
                        outputStream.write(bytes);
                        detectAndFrame(bytes);
                    }finally {
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    }
                }
            };

            imgReader.setOnImageAvailableListener(readingListener,backgroundHanlder);
            final CameraCaptureSession.CaptureCallback callbackListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                    Toast.makeText(Task2_1.this, "Saved " + file, Toast.LENGTH_SHORT).show();
                    cameraPreview();
                }
            };
            cameraDevice.createCaptureSession(imageOut, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {

                    try {
                        session.capture(builder.build(), callbackListener,backgroundHanlder);
                    }
                    catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            }, backgroundHanlder);
        }
        catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void cameraPreview () {

        try {
            SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
            assert surfaceTexture != null;
            surfaceTexture.setDefaultBufferSize(imageSize.getWidth(), imageSize.getHeight());
            Surface surface = new Surface(surfaceTexture);
            captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {

                    if (cameraDevice == null) {
                        return;
                    }
                    captureSessions = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(Task2_1.this, "Changed", Toast.LENGTH_SHORT).show();
                }
            },null);

        }
        catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    private void updatePreview () {
        if (cameraDevice == null) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
        captureBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
        try {
            captureSessions.setRepeatingRequest(captureBuilder.build(),null,backgroundHanlder);

        }
        catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    TextureView.SurfaceTextureListener textListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            startCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == camera_perm) {

            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera needs permission to use",Toast.LENGTH_SHORT).show();
                finish();
            }
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        beginBackgroundThread();
        if (textureView.isAvailable()) {
            startCamera();
        }
        else {
            textureView.setSurfaceTextureListener(textListener);
        }
    }

    @Override
    public void onPause() {
        stopBackgroundThread();
        super.onPause();
    }

    private void stopBackgroundThread() {
        backgroundThread.quitSafely();
        try {
            backgroundThread.join();
            backgroundThread = null;
            backgroundHanlder = null;
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private void beginBackgroundThread() {

        backgroundThread = new HandlerThread("background for camera");
        backgroundThread.start();
        backgroundHanlder = new Handler(backgroundThread.getLooper());
    }

    private void detectAndFrame(final byte[] byte_array) {

//        filler = (ImageView) findViewById(R.id.emo_filler);
//        filler.setImageBitmap(imageBitmap);

//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        imageBitmap.compress(Bitmap.CompressFormat.JPEG,100, outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byte_array);

        final FaceServiceClient.FaceAttributeType[] requiredFaceAttributes = new FaceServiceClient.FaceAttributeType[1];
        requiredFaceAttributes[0] = FaceServiceClient.FaceAttributeType.Emotion;
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

                String emotion = getEmotion(result);
                cur_emotion = emotion;

//                ImageView imageView = findViewById(R.id.emo_filler);
//                imageView.setImageBitmap(
//                        drawFaceRectanglesOnBitmap(imageBitmap, result,emotion));
//                imageBitmap.recycle();
                Toast.makeText(Task2_1.this,emotion, Toast.LENGTH_SHORT).show();
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
            Bitmap originalBitmap, Face[] faces,String emotion) {
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
            return "No emotion, neutral!";
        }
        return "Could not determine emotion";
    }


}