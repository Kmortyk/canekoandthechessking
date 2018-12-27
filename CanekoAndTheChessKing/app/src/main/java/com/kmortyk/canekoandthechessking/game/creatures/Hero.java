package com.kmortyk.canekoandthechessking.game.creatures;

import android.graphics.Bitmap;

import com.kmortyk.canekoandthechessking.game.GameWorld;

/**
 * Created by user1 on 05.11.2018.
 */

public class Hero extends Creature{

    public Hero(GameWorld gameWorld, int i, int j, Bitmap texture) { super(gameWorld, i, j, texture); }

    @Override
    protected void onArrived() {
        super.onArrived();
        path.clear();
        gameWorld.nextTurn();
    }

}
