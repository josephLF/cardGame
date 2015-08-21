package com.garmin.android.apps.cardgame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by ChangJosephlf on 2015/8/15.
 */
public class TextCard extends GameObject implements GestureDetector.OnGestureListener {

    private Bitmap backSideImage;
    private Bitmap frontSideImage;
    private boolean isBackSide;
    private String text;
    private String contain_id;
    private int textSize;
    private Paint backPaint;
    private Paint frontPaint;
    public TextCard(String aKey_id, int aScore, int aX, int aY, int aWidth, int aHeight, GameAnimation aAnimation) {
        super(aKey_id, aScore, aX, aY, aWidth, aHeight, aAnimation);
        isBackSide=false;
        textSize=80;
        backPaint=new Paint();
        frontPaint=new Paint();

    }

    public String getContain_id() {
        return contain_id;
    }

    public void setContain_id(String aContain_id) {
        contain_id = aContain_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String aText) {
        text = aText;
    }

    public Bitmap getBackSideImage() {
        return backSideImage;
    }

    public void setBackSideImage(Bitmap aBackSideImage) {
        backSideImage = aBackSideImage;
    }

    public Bitmap getFrontSideImage() {
        return frontSideImage;
    }

    public void setFrontSideImage(Bitmap aFrontSideImage) {
        frontSideImage = aFrontSideImage;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setFrontPaint(Paint aFrontPaint) {
        frontPaint = aFrontPaint;
    }

    public void setBackPaint(Paint aBackPaint) {
        backPaint = aBackPaint;
    }

    public void setTextSize(int aTextSize) {
        textSize = aTextSize;
    }

    public boolean isBackSide() {
        return isBackSide;
    }

    public void setBackSide(boolean aIsBackSide) {
        isBackSide = aIsBackSide;
    }
    public boolean match(TextCard card){
        return this.contain_id.equals(card.contain_id);
    }
    @Override
    public void update() {
        x+=dx;
        y+=dy;
    }

    @Override
    public void draw(Canvas aCanvas) {
        if(isBackSide) {
            Log.d(this.getClass().getSimpleName(),"isBackSide x,y,w,h:"+getX()+","+getY()+","+getWidth()+","+getHeight());
            Bitmap scale_bitmap= Bitmap.createScaledBitmap(backSideImage,getWidth(),getHeight(),true);
            aCanvas.drawBitmap(scale_bitmap, getX(), getY(), backPaint);
        }else {
            Log.d(this.getClass().getSimpleName(), "isFrontSide x,y,w,h:" + getX() + "," + getY() + "," + getWidth() + "," + getHeight());
            Bitmap scale_bitmap = Bitmap.createScaledBitmap(frontSideImage, getWidth(), getHeight(), true);

            aCanvas.drawBitmap(scale_bitmap, getX(), getY(), frontPaint);
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(textSize);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            // measure text width
            int textleft = (int)paint.measureText(text, 0, text.length());
            aCanvas.drawText(text, getX() + (getWidth() / 2)-(textleft/2), getY() + (getHeight() / 2), paint);

        }

    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d(this.getClass().getSimpleName(),"onDown");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.d(this.getClass().getSimpleName(),"onShowPress");

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d(this.getClass().getSimpleName(),"onSingleTapUp");

        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d(this.getClass().getSimpleName(),"onLongPress");

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d(this.getClass().getSimpleName(),"onFling");

        return false;
    }
}

