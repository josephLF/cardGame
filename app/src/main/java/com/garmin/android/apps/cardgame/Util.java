package com.garmin.android.apps.cardgame;

/**
 * Created by ChangJosephlf on 2015/8/19.
 */
public class Util {
    public final static String SCORE="SCORE :";
    public final static String MODE="MODE :";

    int generateRandomNumber(int lowerBound ,int upperBound){
        //random number between  lowerBound  <= x <= upperBound
       return (int)(Math.random() * (upperBound-lowerBound+1)) + lowerBound;
    }

}
