package com.garmin.android.apps.cardgame;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by ChangJosephlf on 2015/8/13.
 */
public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread mGameThread;
    public GameSurfaceView(Context context) {
        super(context);

        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);
        mGameThread=new GameThread(getHolder(),this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        int item_number;
        new AlertDialog.Builder(this.getContext())
            .setTitle(R.string.select_level_title)
            .setSingleChoiceItems(R.array.level_string_array, 0, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //TODO R.ar ray.level_string_array
                }
            })
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // doSomething with clicked item
                    Resources res =getResources();
                    String[] arr=res.getStringArray(R.array.level_string_array);
                    //start game thread
                    mGameThread.setRunning(true);
                    mGameThread.start();

                }
            }).show();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry=true;
        int counter=0;
        while(retry && counter<1000){
            counter++;
            try{
                mGameThread.setRunning(false);
                mGameThread.join();
                retry=false;
            }catch (InterruptedException ex){
                ex.printStackTrace();
            }

        }
    }
    public void update(){

    }
}
