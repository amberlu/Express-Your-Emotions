package com.example.jiananlu.expressyouremotions;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.FaceRectangle;

public class ImageProcessor {


//    public static Bitmap drawRectFace (Bitmap bitmap, FaceRectangle faceRectangle, String status) {
//
//        Bitmap bitmap1 = bitmap.copy(Bitmap.Config.ARGB_8888,true);
//        Canvas canvas = new Canvas(bitmap1);
//        Paint paint = new Paint();
//        paint.setAntiAlias(true);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setColor(Color.WHITE);
//        paint.setStrokeWidth(12);
//
//
//        canvas.drawRect(faceRectangle.left,
//                faceRectangle.top,
//                faceRectangle.left + faceRectangle.width,
//                faceRectangle.top + faceRectangle.height,
//                paint);
//
//        int deltaX = faceRectangle.left + faceRectangle.width;
//        int deltaY = faceRectangle.top + faceRectangle.height;
//
//        drawTextOnBitMap(canvas,100,deltaX/2 + deltaX/5, deltaY + 100, Color.WHITE, status);
//
//
//
//    }
//
//
//
//    private static void drawTextOnBitMap(Canvas canvas,int i,int j, int k, int color , String status) {
//
//        Paint tempTextPlain
//
//
//    }


    String apiKey = "2d1dbe92587345da9bf698b0d221beb0";
    public FaceServiceRestClient faceServiceRestClient = new FaceServiceRestClient(apiKey);







}
