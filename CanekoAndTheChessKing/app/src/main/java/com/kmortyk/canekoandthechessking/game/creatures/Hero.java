package com.kmortyk.canekoandthechessking.game.creatures;

import android.graphics.Bitmap;

import com.kmortyk.canekoandthechessking.thread.GameThread;

/**
 * Created by user1 on 05.11.2018.
 */

public class Hero extends Creature{

    private GameThread gameThread;

    public Hero(GameThread gameThread, int i, int j, Bitmap texture) {
        super(gameThread.getGameWorld(), i, j, texture);
        this.gameThread = gameThread;
    }

    @Override
    protected void onArrived() {
        super.onArrived();
        path.clear();
        gameWorld.nextTurn();
        gameThread.viewToHero();
    }

}
