package com.example.jiananlu.expressyouremotions;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task2_1 extends AppCompatActivity {

    private String cont;
    private Integer pic_id;
    private ImageView emotion_image_display;
    private TextView emotion_text_display;
    private Button back, takePic, next_button;
    private ImageView textureView;
    boolean cleared = false;
    public Uri imageUri;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public String mCurrentPhotoPath;


    // Sets up the page's buttons, image views etc. for taking a picture.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.temp2_1);

        Intent intent = getIntent();
        cont = intent.getStringExtra("content");
        pic_id = intent.getIntExtra("pic_id", 0);

        emotion_image_display = findViewById(R.id.imageView3);
        emotion_image_display.setImageResource(pic_id);

        emotion_text_display = findViewById(R.id.textView6);
        emotion_text_display.setText("Make a " +cont+ " face!");

        textureView = findViewById(R.id.textureView);
        next_button = findViewById(R.id.next_button_2);
        clickOnButton();
    }

    private void clickOnButton(){
        back  = (Button) findViewById(R.id.back2_1_1);
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
                    intent.putExtra("img_path",mCurrentPhotoPath);
                    startActivity(intent);

                } else {
                    // make the toast message with bigger font size and pink background
                    String msg = "Please take a picture!";
                    SpannableStringBuilder biggerText = new SpannableStringBuilder(msg);
                    biggerText.setSpan(new RelativeSizeSpan(2.0f), 0, msg.length(), 0);
                    Toast toast = Toast.makeText(getApplicationContext(), biggerText, Toast.LENGTH_SHORT);
                    View toast_view = toast.getView();
                    toast_view.getBackground().setColorFilter(Color.parseColor("#FFCAEA"), PorterDuff.Mode.SRC_IN);
                    toast.show();
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

    // On result of the picture we allow the user to move forward to the next activity.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap imageBitmap = BitmapFactory.decodeFile(
                mCurrentPhotoPath);
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

}