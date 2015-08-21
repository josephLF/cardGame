package com.garmin.android.apps.cardgame;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by ChangJosephlf on 2015/8/13.
 */
public class GameThread extends Thread {
    private int FPS = 30;
    private double averageFPS;
    private SurfaceHolder mSurfaceHolder;
    private GameSurfaceView mGameSurfaceView;

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean aRunning) {
        running = aRunning;
    }

    private boolean running;
    public static Canvas canvas;

    public GameThread(SurfaceHolder aSurfaceHolder, GameSurfaceView aGameSurfaceView) {
        super();
        this.mSurfaceHolder=aSurfaceHolder;
        this.mGameSurfaceView=aGameSurfaceView;
    }
    @Override
    public void run(){
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime=0;
        int frameCount=0;
        long targetTime=1000/FPS;

        while(running){
            startTime=System.nanoTime();
            canvas=null;

            //try locking the canvas for pixel thing;
            try{
                canvas=this.mSurfaceHolder.lockCanvas();
                synchronized (mSurfaceHolder){
                    this.mGameSurfaceView.update();
                    this.mGameSurfaceView.drawSurface(canvas);
                }
            }catch(Exception ex){
            }finally {
                if(canvas!=null){
                    try{
                        mSurfaceHolder.unlockCanvasAndPost(canvas);
                    }catch(Exception e){
                        Log.d(this.getClass().getSimpleName(),e.toString());
                    }
                }
            }

            timeMillis =(System.nanoTime()-startTime) /1000000;
            waitTime=targetTime-timeMillis;
            try{
                this.sleep(waitTime);
            }catch (Exception ex){}

            totalTime+=System.nanoTime()-startTime;
            frameCount++;
            if(frameCount==FPS){
                averageFPS =1000/((totalTime/frameCount)/1000000);
                frameCount=0;
                totalTime=0;
                Log.d(this.getClass().getSimpleName(), "FPS:" + averageFPS);
            }

        }

    }
}
