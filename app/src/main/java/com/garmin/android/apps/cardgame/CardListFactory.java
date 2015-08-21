package com.garmin.android.apps.cardgame;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

/**
 * Created by ChangJosephlf on 2015/8/17.
 */
public class CardListFactory {
    //GENERATE CARD OBJECT
    private final static int minimumCardScore =10;
    private final static int maximumCardScore =40;
    private final static String default_vocabulary="x";
    private String[] cardInputArr;
    private Bitmap frontBitmap;
    private Bitmap backBitmap;

    public CardListFactory(String[] cardInput,Bitmap aFrontBitMap,Bitmap aBackBitMap){
        cardInputArr=cardInput;
        frontBitmap=aFrontBitMap;
        backBitmap=aBackBitMap;
    }
    public ArrayList<TextCard> generateMode(GameMode mode, Point startPoint , int boundWidth,int boundHeight,int itemNumber) throws Exception {
        if (itemNumber % 2 != 0) {
            throw new Exception("Sould Be Even number!!");
        }

        ArrayList<TextCard> list = new ArrayList<TextCard>();
        init_card(list, itemNumber);
        mode.applyBehavior(list);
        //shuffle list
        Collections.shuffle(list);
        //relocate card
        relocateCard(list, startPoint, boundWidth, boundHeight);
        Log.d(this.getClass().getSimpleName()," list size:"+list.size());
        return list;
    }

    private void init_card(ArrayList<TextCard> aList, int aItemNumber) {
        for(int index=0;index<aItemNumber/2;index++){
            int random_score=new Util().generateRandomNumber(minimumCardScore, maximumCardScore);

            TextCard textCard1 =new TextCard("",random_score,0,0,0,0,new GameAnimation());
            TextCard textCard2 =new TextCard("",random_score,0,0,0,0,new GameAnimation());
            textCard1.setTextSize(40);
            textCard2.setTextSize(40);
            if(cardInputArr.length!=0){
                textCard1.setText(cardInputArr[index%26]);
                textCard2.setText(cardInputArr[index%26]);
            }else{
                textCard1.setText(default_vocabulary);
                textCard2.setText(default_vocabulary);
            }
            String contain_id=UUID.randomUUID().toString();
            textCard1.setContain_id(contain_id);
            textCard1.setKey_id(textCard1.getContain_id() + "1");
            textCard2.setContain_id(contain_id);
            textCard2.setKey_id(textCard1.getContain_id() + "2");

            textCard1.setFrontSideImage(frontBitmap.copy(frontBitmap.getConfig(),true));
            textCard1.setBackSideImage(backBitmap.copy(backBitmap.getConfig(),true));

            textCard2.setFrontSideImage(frontBitmap.copy(frontBitmap.getConfig(),true));
            textCard2.setBackSideImage(backBitmap.copy(backBitmap.getConfig(),true));
            aList.add(textCard1);
            aList.add(textCard2);

        }
    }
    private void relocateCard(ArrayList<TextCard> aList, Point aStartPoint, int aBoundWidth, int aBoundHeight) {
        int grid_edge_number=((int)Math.sqrt(aList.size()));
        int grid_width= aBoundWidth /grid_edge_number;
        int grid_height= aBoundHeight /grid_edge_number;
        int gridx_index=0;
        int gridy_index=0;
        for(TextCard card:aList){
            int cardX, cardY, cardWidth, cardHeight;
            if(gridx_index>=grid_edge_number){
                gridx_index=0;
                gridy_index++;
            }
            cardWidth=grid_height/2;
            cardX=gridx_index*grid_width+aStartPoint.x+(grid_width/2-cardWidth/2);
            cardHeight=5*grid_height/6;
            cardY=gridy_index*grid_height+aStartPoint.y;
            card.setX(cardX);
            card.setY(cardY);
            card.setWidth(cardWidth);
            card.setHeight(cardHeight);
            gridx_index++;
            Log.d(this.getClass().getSimpleName(), "card fill:" + card.getX() + "," + card.getY() + "," + card.getWidth() + "," + card.getHeight());
        }
    }
}
