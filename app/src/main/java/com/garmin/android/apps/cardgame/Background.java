package com.garmin.android.apps.cardgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by ChangJosephlf on 2015/8/14.
 */
public class Background {
    private Bitmap mBackgroundImage;
    private int x;
    private int y;
    private int dx;
    private int dy;
    private int width;
    private int height;
    public Background(Bitmap aBackgroundImage,int aWidth,int aHeight) {
        mBackgroundImage = aBackgroundImage;
        width=aWidth;
        height=aHeight;

    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setVectorX(int aDx) {
        dx = aDx;
    }

    public void setVectorY(int aDy) {
        dy = aDy;
    }
    public void update(){
        x=0;
        y=0;
    }

    public void draw(Canvas aCanvas) {
        aCanvas.drawBitmap(mBackgroundImage, x, y, null);
    }
}