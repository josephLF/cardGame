package com.garmin.android.apps.cardgame;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.util.ArrayList;

/**
 * Created by ChangJosephlf on 2015/8/13.
 */
public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    //m stand for class member
    private static int screenWidth;
    private static int screenHeight;
    private GameThread mGameThread;
    private Background mBackground;
    private PlayerSetting mPlayerSetting;
    private ArrayList<TextCard> cardList;
    private boolean newGameCreated;
    private int itemCounter;
    private int selectLevelItemIndex;
    private String[] vocabularyArray;
    private long gameStartTime;
    private long memoryStartTime;
    private long matchWaitingTime;
    private TextCard selectCard;
    private int score;
    private boolean isMemory=false;
    private GameMode currentMode;

    public GameSurfaceView(Context context) {
        super(context);

        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);

        //get device screen size
        initScreenSize();
        //make gamePanel focusable so it can handle events
        setFocusable(true);

    }

    @Override
    public void surfaceCreated(final SurfaceHolder holder) {
        //default size
        itemCounter=4;
        gameStartTime=0;
        selectCard =null;
        selectLevelItemIndex=0;
        vocabularyArray =getResources().getStringArray(R.array.text_string_array);
        BitmapFactory.Options dimensions = new BitmapFactory.Options();
        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background, dimensions);
        Log.d(this.getClass().getSimpleName(),"bitmap w,h:"+dimensions.outWidth+","+dimensions.outHeight);
        mBackground=new Background(mBitmap, dimensions.outWidth,dimensions.outHeight);
        Log.d(this.getClass().getSimpleName(),"after background new");

        //playSetting
        mPlayerSetting=new PlayerSetting();
        Log.d(this.getClass().getSimpleName(),"after playSetting new");

        cardList=new ArrayList<TextCard>();
        //assign game thread refresh gameSurfaceView
        mGameThread=new GameThread(holder,this);
        mGameThread.setRunning(true);
        AlertDialog dialog=new AlertDialog.Builder(this.getContext())
            .setTitle(R.string.select_level_title)
            .setSingleChoiceItems(R.array.level_string_array, 0, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(this.getClass().getSimpleName(),"choose dialog:"+which);
                    selectLevelItemIndex=which;
                }
            })
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            }).setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    Log.d(this.getClass().getSimpleName(), "after dialog show");
                    String[] arr=getResources().getStringArray(R.array.level_string_array);
                    itemCounter=Integer.valueOf(arr[selectLevelItemIndex]);
                    Log.d(this.getClass().getSimpleName(), "  itemCounter: " + itemCounter);
                    //start game thread
                    mGameThread.start();

                    Log.d(this.getClass().getSimpleName(), "after thread start");
                }
            }).create();
        dialog.show();

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
                Log.e(this.getClass().getSimpleName(),ex.toString());
            }

        }
    }
    public void initScreenSize(){
        WindowManager wm = (WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth =size.x;
        screenHeight = size.y;

        Log.d(this.getClass().getSimpleName(), "size :" + screenWidth + "," + screenHeight);

    }
    public void newGame(){
        //reset player setting
        mPlayerSetting.resetScore();
        mPlayerSetting.setPlaying(true);

        //control difficult by yourself
        shouldBeMoreDifficult(score,true);

        //reset card
        cardList.clear();
        Bitmap backBitMap=new BitmapFactory().decodeResource(getResources(), R.drawable.backside);
        Bitmap frontBitMap=new BitmapFactory().decodeResource(getResources(), R.drawable.foreside);
        currentMode=GameMode.randomMode();
        ArrayList<TextCard> tempCardList=null;
        try{
            tempCardList=new CardListFactory(vocabularyArray,frontBitMap,backBitMap).generateMode(currentMode,new Point(10,10), screenWidth -20, screenHeight -20,itemCounter);
        }catch (Exception ex){
            Log.e(this.getClass().getSimpleName(), "CardListFactory Generate Fail!");
        }
        if(tempCardList!=null){
            Log.d(this.getClass().getSimpleName(), "tempCardList!=null size:"+ tempCardList.size());
            cardList.addAll(tempCardList);
        }
        gameStartTime=System.nanoTime();
        memoryStartTime=0;
        isMemory=false;
    }

    public void savePreference(){
        //save user preference. use Editor object to make changes.
        SharedPreferences settings = this.getContext().getSharedPreferences("CGAME_USER_PREF", 0);
        //settings.edit().commit();
    }
    public void restorePreference(){
        //SharedPreferences settings =getSharedPreferences();
        //settings.
    }
    public void drawSurface(Canvas canvas){
         if(canvas!=null){
             final float scaleFactorX= screenWidth /(mBackground.getWidth()*1.f);
             final float scaleFactorY= screenHeight /(mBackground.getHeight()*1.f);
             Log.d(this.getClass().getSimpleName(),"screen size:"+ screenWidth +","+ screenHeight);
             Log.d(this.getClass().getSimpleName(),"get w,h:"+mBackground.getWidth()+","+mBackground.getHeight());
             Log.d(this.getClass().getSimpleName(), "factor x,y:" + scaleFactorX + "," + scaleFactorY);

             int savedState=canvas.save();
             canvas.scale(scaleFactorX, scaleFactorY);
             mBackground.draw(canvas);
             canvas.restoreToCount(savedState);
             drawText(canvas);
             for(TextCard card:cardList){
                 card.draw(canvas);
             }

        }
    }
    public void drawText(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(40);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        // measure text width
        int textleft = (int)paint.measureText(Util.SCORE, 0, Util.SCORE.length());
        // measure text height
        int textbottom = (int)(paint.descent() - paint.ascent());
        canvas.drawText(Util.SCORE + mPlayerSetting.getScore(), textleft/2+10, screenHeight, paint);
        canvas.drawText(Util.MODE + currentMode.toString(), screenWidth / 2, screenHeight, paint);
        Log.d(this.getClass().getSimpleName(), "isPlaying: " + mPlayerSetting.isPlaying() + " newGameCreated: " + newGameCreated);

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!isMemory){
            return super.onTouchEvent(event);
        }
        Log.d(this.getClass().getSimpleName(), "OnTouchEvent");
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.d(this.getClass().getSimpleName(), "ACTION_DOWN");
            int x=(int)event.getX();
            int y=(int)event.getY();

            Log.d(this.getClass().getSimpleName(),"touch x, y: :"+x+ ","+y);
            for (TextCard card : cardList) {
                boolean intersect=card.getRectangle().contains(x, y);
                Log.d(this.getClass().getSimpleName(),"intersect:"+intersect);
                if(intersect){
                    if(selectCard==null){
                        selectCard=card;
                    }
                    card.setBackSide(false);
                    return true;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    public void update(){
        if(mPlayerSetting.isPlaying()){
            mBackground.update();
            long currentTime=System.nanoTime();
            if(!isMemory){
                if(memoryStartTime==0){
                    memoryStartTime=currentTime;
                }else{
                    long mem_milli=(currentTime-memoryStartTime)/1000000;
                    if(mem_milli>4000){
                        isMemory=true;
                        memoryStartTime=0;
                    }
                    Log.d(this.getClass().getSimpleName(),"memoryTime:"+mem_milli);
                }
               return;
            }

            ArrayList<TextCard> matchList=new ArrayList<TextCard>();
            for(TextCard card:cardList){
                //10s memory time
                if(!card.isBackSide()){
                    if(selectCard!=null){
                        if(card.match(selectCard)){
                            matchList.add(card);
                        }else{
                            if(matchWaitingTime==0){
                                matchWaitingTime=System.nanoTime();
                            }else {
                                long matchTime=((currentTime-matchWaitingTime)/1000000);
                                if (matchTime > 1000) {
                                    card.setBackSide(true);
                                    selectCard.setBackSide(true);
                                    selectCard=null;
                                    matchWaitingTime = 0;
                                }
                            }
                        }
                    }else{
                        card.setBackSide(true);
                    }
                }
                checkReflection(card);

                card.update();
            }

            if(matchList.size()>=2){
                if(matchWaitingTime==0){
                    matchWaitingTime=System.nanoTime();
                }else{
                    if((System.nanoTime()-matchWaitingTime)>800){
                        int score=mPlayerSetting.getScore();
                        for(TextCard card:matchList){
                            score=score+card.getScore();
                        }
                        mPlayerSetting.setScore(score);
                        cardList.removeAll(matchList);
                        matchWaitingTime=0;
                        selectCard=null;
                    }
                }
            }
            if(cardList.size()==0){
                score=mPlayerSetting.getScore();
                newGame();
                mPlayerSetting.setScore(score);
            }
        }else{
            newGameCreated=false;
            if(!newGameCreated) {
                newGame();
                newGameCreated=true;
            }
        }

    }
    private void shouldBeMoreDifficult(int aScore,boolean random) {
        if(gameStartTime!=0) {
            if(random){
                int randomNumber=new Util().generateRandomNumber(1,3);
                if(randomNumber==1){
                    itemCounter=4;
                }else if(randomNumber==2){
                    itemCounter=16;
                }else{
                    itemCounter=36;
                }
            }else{
                if (score > 2000) {
                    itemCounter = 36;
                } else if (score > 500) {
                    itemCounter = 16;
                } else {
                    itemCounter = 4;
                }
            }

        }
    }
    private void checkReflection(TextCard aCard) {
        int nextX=aCard.getX()+aCard.getVectorX();
        int nextY=aCard.getY()+aCard.getVectorY();
        if(nextX<0){
            aCard.setVectorX(-nextX);
        }
        if (nextY<0){
            aCard.setVectorY(-nextY);
        }
        int boundX=nextX+aCard.getWidth();
        int boundY=nextY+aCard.getHeight();
        if (boundX > screenWidth){
            aCard.setVectorX(-(boundX- screenWidth));
        }

        if (boundY > screenHeight){
            aCard.setVectorY(-(boundY- screenHeight));
        }
    }
}
