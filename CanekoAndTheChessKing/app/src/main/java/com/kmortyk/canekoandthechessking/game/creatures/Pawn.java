package com.kmortyk.canekoandthechessking.game.creatures;

import android.graphics.Bitmap;
import com.kmortyk.canekoandthechessking.game.GameWorld;
import com.kmortyk.canekoandthechessking.game.object.PathNode;

import java.util.LinkedList;

public class Pawn extends Enemy {

    public Pawn(GameWorld gameWorld, int i, int j, Bitmap texture) { super(gameWorld, i, j, texture); }

    @Override
    public LinkedList<PathNode> allSteps() {
        LinkedList<PathNode> nodes = new LinkedList<>();

        int i = node.i, j = node.j;

        checkNode(i+1, j, nodes);
        checkNode(i-1, j, nodes);
        checkNode(i, j+1, nodes);
        checkNode(i, j-1, nodes);

        return nodes;
    }

}
