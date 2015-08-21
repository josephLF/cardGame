package com.garmin.android.apps.cardgame;

import android.graphics.Bitmap;

/**
 * Created by ChangJosephlf on 2015/8/13.
 */
public class GameAnimation {
    //TODO FOR ANIMATION BUT USELESS HERE
    private Bitmap[] frames;
    private int currentFrame;
    private long startTime;
    private long delayTime;
    private boolean playedOnce;

    public GameAnimation(){
        this(null,false);
    }
    public GameAnimation(Bitmap[] aFrames, boolean aPlayedOnce) {
        frames = aFrames;
        currentFrame=0;
        delayTime=0;
        startTime=System.nanoTime();
        playedOnce = aPlayedOnce;
    }
    public boolean isPlayedOnce() {
        return playedOnce;
    }
    public void setFrames(Bitmap[] aFrames){
        this.frames=aFrames;
        currentFrame=0;
        startTime=System.nanoTime();

    }
    public void update(){
        //millisecond
        long elapsed =(System.nanoTime()-startTime)/1000000;
        if(elapsed>delayTime){
            currentFrame++;
            startTime=System.nanoTime();
        }
        if(currentFrame ==frames.length){
            currentFrame=0;
            playedOnce=true;
        }
    }
    public Bitmap getCurrentFrameImage(){
        return frames[currentFrame];
    }
    public int getCurrentFrameIndex(){
        return currentFrame;
    }
}
