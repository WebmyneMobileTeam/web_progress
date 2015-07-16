package com.github.glomadrian.dashedcircularprogress.painter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * @author Adrián García Lomas
 */
public class BottomIconPainter implements Painter {

    private Bitmap image;
    private int centreX;
    private int centreY;
    private int width;
    private int height;

    public BottomIconPainter(Bitmap image) {
        this.image = image;
    }

    private void initBitmap() {
        image = Bitmap.createScaledBitmap(image, 64, 64, false);
    }

    @Override
    public void draw(Canvas canvas) {
        initBitmap();

        canvas.drawBitmap(image,centreY-10, height-64, new Paint()); // left
      //  canvas.drawBitmap(image,0 , centreX, new Paint()); // left
     //   canvas.drawBitmap(image,centreY, height-32, new Paint()); // left

    }

    @Override
    public void setColor(int color) {
        //Empty
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
