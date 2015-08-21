package com.garmin.android.apps.cardgame;

/**
 * Created by ChangJosephlf on 2015/8/14.
 */
public class PlayerSetting {
    private int score;
    private String name;
    private boolean isPlaying;
    private int error_counter;
    public PlayerSetting(){
        this("GUEST");
    }
    public PlayerSetting(String aName){
        name=aName;
        setPlaying(false);

        resetScore();
    }

    public void setPlaying(boolean aPlaying) {
        isPlaying = aPlaying;
    }

    public boolean isPlaying(){
        return isPlaying;
    }
    public void resetScore(){
        score=0;
        error_counter=0;
    }

    public int getError_counter() {
        return error_counter;
    }

    public void setScore(int aScore) {
        score = aScore;
    }

    public int getScore(){
        return score;
    }
}
