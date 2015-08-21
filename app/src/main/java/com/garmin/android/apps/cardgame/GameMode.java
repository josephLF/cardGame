package com.garmin.android.apps.cardgame;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ChangJosephlf on 2015/8/17.
 */
public enum GameMode {

    NORMAL_MODE("NORMAL_MODE",0){
        @Override
        public void applyBehavior(ArrayList<TextCard> list) {
            for(TextCard card:list){
                card.setTextSize(40);
            }
        }
    },
    MIX_MODE("MIX_MODE",1){
        @Override
        public void applyBehavior(ArrayList<TextCard> list) {
            GOOD_EYE_MODE.applyBehavior(list);
            MOVING_MODE.applyBehavior(list);
        }
    },
    GOOD_EYE_MODE("GOOD_EYE_MODE",2){
        @Override
        public void applyBehavior(ArrayList<TextCard> list) {
            for(TextCard card:list){
                card.setTextSize(5);
            }
        }
    },
    MOVING_MODE("MOVING_MODE",3){
        @Override
        public void applyBehavior(ArrayList<TextCard> list) {

            for(TextCard card:list){
                int randomSpeedX=new Util().generateRandomNumber(-80,80);
                int randomSpeedY=new Util().generateRandomNumber(-80,80);
                card.setVectorX(randomSpeedX);
                card.setVectorY(randomSpeedY);
            }
        }
    },
    DARK_MODE("DARK_MODE",4){
        @Override
        public void applyBehavior(ArrayList<TextCard> list) {
            Paint paint=new Paint();
            paint.setColor(Color.BLACK);

            for(TextCard card:list){
                card.setFrontPaint(paint);
                //card.setBackSideImage(null);
            }
        }
    },
    TRANSPARENT_MODE("TRANSPARENT_MODE",5){
        @Override
        public void applyBehavior(ArrayList<TextCard> list) {
            Paint paint=new Paint();
            paint.setAlpha(100);

            for(TextCard card:list){
                card.setBackPaint(paint);
                card.setFrontPaint(paint);
                //card.setBackSideImage(null);
            }
        }
    };
    private String mStringValue;
    private int mModeIndex;
    GameMode(String aStringValue,int aModeIndex){
        mStringValue=aStringValue;
        mModeIndex=aModeIndex;
    }
    public int getModeIndex(){
        return mModeIndex;
    }
    public static GameMode randomMode() {
        int random_number = new Util().generateRandomNumber(0, 6);

        if (NORMAL_MODE.getModeIndex() == random_number) {
            return NORMAL_MODE;
        } else if (MIX_MODE.getModeIndex() == random_number) {
            return MIX_MODE;
        }else if(TRANSPARENT_MODE.getModeIndex() == random_number){
            return TRANSPARENT_MODE;
        }else if(MOVING_MODE.getModeIndex() == random_number){
            return MOVING_MODE;
        }else if(DARK_MODE.getModeIndex() == random_number){
            return DARK_MODE;
        }else if(GOOD_EYE_MODE.getModeIndex() == random_number){
            return GOOD_EYE_MODE;
        }else{
            return NORMAL_MODE;
        }
    }
    public abstract void applyBehavior(ArrayList<TextCard> list);
    @Override
    public String toString(){
        return mStringValue;
    }

}
