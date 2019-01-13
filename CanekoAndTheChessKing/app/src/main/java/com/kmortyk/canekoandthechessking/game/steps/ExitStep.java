package com.kmortyk.canekoandthechessking.game.steps;

import android.graphics.Bitmap;

import com.kmortyk.canekoandthechessking.thread.GameThread;
import com.kmortyk.canekoandthechessking.game.creatures.Creature;
import com.kmortyk.canekoandthechessking.game.creatures.Enemy;

public class ExitStep extends Step {

    /**
     * Used to go to the next map when a player stands on the step.
     */
    private GameThread gameThread;

    public ExitStep(GameThread gameThread, int flags, Bitmap texture, int i, int j) {
        super(flags, texture, i, j);
        this.gameThread = gameThread;
    }

    /**
     * Data constructor
     */
    public ExitStep(GameThread gameThread, int flags, Bitmap texture) {
        super(flags, texture);
        this.gameThread = gameThread;
    }

    @Override
    public void onStep(Creature creature) {
        if(creature instanceof Enemy) return; // <- do not touch
        gameThread.nextMap();
    }

    @Override
    public Step clone(int i, int j) { return new ExitStep(gameThread, flags, texture, i, j); }

}
