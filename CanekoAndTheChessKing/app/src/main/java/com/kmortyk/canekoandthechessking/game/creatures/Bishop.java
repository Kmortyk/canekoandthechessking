package com.kmortyk.canekoandthechessking.game.creatures;

import android.graphics.Bitmap;

import com.kmortyk.canekoandthechessking.game.GameWorld;
import com.kmortyk.canekoandthechessking.game.object.PathNode;

import java.util.LinkedList;

public class Bishop extends Enemy  {

    public Bishop(GameWorld gameWorld, int i, int j, Bitmap texture) { super(gameWorld, i, j, texture); }

    @Override
    public LinkedList<PathNode> allSteps() { return null; } // FIXME return null

    /*@Override
    public void nextStep() {
        int i = node.i, j = node.j;
        int o;

        if(rando.nextDouble() < 0.5) { o = gameWorld.getGameMap().width  - i; }
        else                         { o = gameWorld.getGameMap().height - j; }

        i += o;
        j += o;

        if(gameWorld.isValid(i, j)) { addPathNode(i, j); }
        //else nextStep();
    }*/


}
