package com.kmortyk.canekoandthechessking.game.tiles;

import android.graphics.Bitmap;

import com.kmortyk.canekoandthechessking.thread.GameThread;
import com.kmortyk.canekoandthechessking.game.creatures.Creature;
import com.kmortyk.canekoandthechessking.game.creatures.Enemy;

public class ExitTile extends Tile {

    /**
     * Used to go to the next map when a player stands on the step.
     */
    private GameThread gameThread;

    public ExitTile(GameThread gameThread, int flags, Bitmap texture, Bitmap backTexture, int i, int j) {
        super(flags, texture, backTexture, i, j);
        this.gameThread = gameThread;
    }

    @Override
    public void onStep(Creature creature) {
        if(creature instanceof Enemy) return; // <- do not touch
        gameThread.nextMap();
    }

    @Override
    public Tile clone(int i, int j) { return new ExitTile(gameThread, flags, texture, backTexture, i, j); }

}
