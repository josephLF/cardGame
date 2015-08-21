package com.garmin.android.apps.cardgame;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by ChangJosephlf on 2015/8/13.
 */
public abstract class GameObject {
    protected String key_id;
    protected int score;
    protected int x;
    protected int y;
    protected int dx;
    protected int dy;
    protected int dxAcceleration;
    protected int dyAcceleration;
    protected int width;
    protected int height;
    private GameAnimation animation;
    public GameObject(String aKey_id,int aScore,int aX,int aY,int aWidth,int aHeight,GameAnimation aAnimation){
        animation=aAnimation;
        key_id=aKey_id;
        score=aScore;
        dx=0;
        dy=0;
        dxAcceleration=0;
        dyAcceleration=0;
    }
    public String getKey_id() {
        return key_id;
    }

    public void setKey_id(String aKey_id) {
        key_id = aKey_id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int aScore) {
        score = aScore;
    }

    public int getX() {
        return x;
    }

    public void setX(int aX) {
        x = aX;
    }

    public int getY() {
        return y;
    }

    public void setY(int aY) {
        y = aY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int aWidth) {
        width = aWidth;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int aHeight) {
        height = aHeight;
    }

    public int getVectorX() {
        return dx;
    }

    public void setVectorX(int aDx) {
        dx = aDx;
    }

    public int getVectorY() {
        return dy;
    }

    public void setVectorY(int aDy) {
        dy = aDy;
    }
    public Rect getRectangle(){
        return new Rect(x,y,x+width,y+height);
    }
    public abstract void update();
    public abstract void draw(Canvas aCanvas);

}
