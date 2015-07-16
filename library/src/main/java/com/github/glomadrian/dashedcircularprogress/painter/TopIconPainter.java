package com.github.glomadrian.dashedcircularprogress.painter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * @author Adrián García Lomas
 */
public class TopIconPainter implements Painter {

    private Bitmap image;
    private int centreX;
    private int centreY;
    private int width;
    private int height;
    private int color;

    public TopIconPainter(Bitmap image) {
        this.image = image;
    }

    private void initBitmap() {
        image = Bitmap.createScaledBitmap(image, 64, 64, false);
    }

    @Override
    public void draw(Canvas canvas) {
        initBitmap();
       // canvas.drawBitmap(image, centreX, 0, new Paint()); // top
      //  canvas.drawBitmap(image,0 , centreX, new Paint()); // left

        canvas.drawBitmap(image, centreX,50,new Paint()); // top

    }

    @Override
    public void setColor(int color) {
        //Empty
        this.color = color;


    }

    @Override
    public int getColor() {
        return 0;
    }



    @Override
    public void onSizeChanged(int height, int width) {
        this.width = width;
        this.height = height;
        this.centreX = (width - 64) / 2;
        this.centreY = (height - 64) / 2;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
