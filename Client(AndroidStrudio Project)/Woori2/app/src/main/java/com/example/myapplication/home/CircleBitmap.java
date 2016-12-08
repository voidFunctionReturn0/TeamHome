package com.example.myapplication.home;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2016-07-08.
 */


public class CircleBitmap {
    public Bitmap createFramedPhoto( Bitmap scaleBitmapImage, int size) {

        Drawable imageDrawable = (scaleBitmapImage != null) ? new BitmapDrawable(scaleBitmapImage) : null;

        Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        RectF outerRect = new RectF(0, 0, size, size);
        float cornerRadius = size / 2f;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        canvas.drawRoundRect(outerRect, cornerRadius, cornerRadius, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        imageDrawable.setBounds(0, 0, size, size);

        // Save the layer to apply the paint
        canvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG);
        imageDrawable.draw(canvas);
        canvas.restore();

        // FRAMING THE PHOTO
        float border = size / 24f;

        // 1. Create offscreen bitmap link:
        // http://www.youtube.com/watch?feature=player_detailpage&v=jF6Ad4GYjRU#t=1035s
        Bitmap framedOutput = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas framedCanvas = new Canvas(framedOutput);
        // End of Step 1

        // Start - TODO IMPORTANT - this section shouldn't be included in the final code
        // It's needed here to differentiate step 2 (red) with the background color of the activity
        // It's should be commented out after the codes includes step 3 onwards
        // Paint squaredPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // squaredPaint.setColor(Color.BLUE);
        // framedCanvas.drawRoundRect(outerRect, 0f, 0f, squaredPaint);
        // End

        // 2. Draw an opaque rounded rectangle link:
        // http://www.youtube.com/watch?feature=player_detailpage&v=jF6Ad4GYjRU#t=1044s
        RectF innerRect = new RectF(border, border, size - border, size - border);

        Paint innerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        innerPaint.setColor(Color.RED);
        framedCanvas.drawRoundRect(innerRect, cornerRadius, cornerRadius, innerPaint);

        // 3. Set the Power Duff mode
        // http://www.youtube.com/watch?feature=player_detailpage&v=jF6Ad4GYjRU#t=1056s
        Paint outerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outerPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));

        // 4. Draw a translucent rounded rectangle link:
        // http://www.youtube.com/watch?feature=player_detailpage&v=jF6Ad4GYjRU
        outerPaint.setColor(Color.argb(100, 0, 51, 102));
        framedCanvas.drawRoundRect(outerRect, cornerRadius, cornerRadius, outerPaint);

        // Draw the frame on top of original bitmap
        canvas.drawBitmap(framedOutput, 0f, 0f, null);

        return output;
    }

    public Bitmap getCircleBitmap(Bitmap scaleBitmapImage, int radiusDp) {
        int width = 0;
        int height = 0;
        int radius = (int)(radiusDp );
        if(scaleBitmapImage.getWidth() / scaleBitmapImage.getHeight() < 1.0) {
            width = radius * 2;
            height =radius * 2 * scaleBitmapImage.getHeight()/scaleBitmapImage.getWidth();
        } else {
            width =radius * 2 * scaleBitmapImage.getWidth()/scaleBitmapImage.getHeight();
            height = radius * 2;
        }
        Bitmap targetBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(width/2, height/2, radius, Path.Direction.CCW);
        canvas.clipPath(path);

        canvas.drawBitmap(
                scaleBitmapImage,
                new Rect(0, 0, scaleBitmapImage.getWidth(), scaleBitmapImage.getHeight()),
                new Rect(0, 0, width, height),
                null
        );

        return targetBitmap;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = 150;
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
