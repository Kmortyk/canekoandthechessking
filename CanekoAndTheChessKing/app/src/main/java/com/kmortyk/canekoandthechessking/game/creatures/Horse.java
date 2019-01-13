package com.kmortyk.canekoandthechessking.game.creatures;

import android.graphics.Bitmap;

import com.kmortyk.canekoandthechessking.game.GameWorld;
import com.kmortyk.canekoandthechessking.game.object.PathNode;

import java.util.LinkedList;

public class Horse extends Enemy {

    private int[] offset1 = {-2, 2};
    private int[] offset2 = {-4, 4};

    public Horse(GameWorld gameWorld, int i, int j, Bitmap texture) { super(gameWorld, i, j, texture); }

    @Override
    public LinkedList<PathNode> allSteps() {
        LinkedList<PathNode> nodes = new LinkedList<>();

        int i = node.i, j = node.j;
        int steps = 1 + gameWorld.getSpaces();

        checkNode(i + 2*steps, j + steps, nodes);
        checkNode(i + 2*steps, j - steps, nodes);
        checkNode(i - 2*steps, j + steps, nodes);
        checkNode(i - 2*steps, j - steps, nodes);

        checkNode(i + steps, j + 2*steps, nodes);
        checkNode(i - steps, j + 2*steps, nodes);
        checkNode(i + steps, j - 2*steps, nodes);
        checkNode(i - steps, j - 2*steps, nodes);

        return nodes;
    }

    @Override
    public void addPathNode(PathNode node) {
        if(!this.node.equals(node)) {
            path.add(node);
            path.add(new PathNode(gameWorld.get(node.i, this.node.j), node.i, this.node.j));
        }
    }

}
