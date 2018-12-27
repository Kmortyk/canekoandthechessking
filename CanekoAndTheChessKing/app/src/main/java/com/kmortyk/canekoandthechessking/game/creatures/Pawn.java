package com.kmortyk.canekoandthechessking.game.creatures;

import android.graphics.Bitmap;
import com.kmortyk.canekoandthechessking.game.GameWorld;
import com.kmortyk.canekoandthechessking.game.effects.PopUpText;
import com.kmortyk.canekoandthechessking.game.object.PathNode;
import com.kmortyk.canekoandthechessking.game.steps.Step;

import java.util.LinkedList;

public class Pawn extends Enemy {

    public Pawn(GameWorld gameWorld, int i, int j, Bitmap texture) { super(gameWorld, i, j, texture); }

    @Override
    public LinkedList<PathNode> allSteps() {
        LinkedList<PathNode> nodes = new LinkedList<>();

        int i = node.i, j = node.j;
        int spaces = gameWorld.hasSpaces() ? 2 : 1;

        checkNode(i+spaces, j, nodes);
        checkNode(i-spaces, j, nodes);
        checkNode(i, j+spaces, nodes);
        checkNode(i, j-spaces, nodes);

        return nodes;
    }

}
