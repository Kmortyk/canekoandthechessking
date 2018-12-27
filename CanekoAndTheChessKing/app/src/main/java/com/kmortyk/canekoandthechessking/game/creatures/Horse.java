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
        int spaces = gameWorld.hasSpaces() ? 2 : 1;

        checkNode(i + 2*spaces, j + spaces, nodes);
        checkNode(i + 2*spaces, j - spaces, nodes);
        checkNode(i - 2*spaces, j + spaces, nodes);
        checkNode(i - 2*spaces, j - spaces, nodes);

        checkNode(i + spaces, j + 2*spaces, nodes);
        checkNode(i - spaces, j + 2*spaces, nodes);
        checkNode(i + spaces, j - 2*spaces, nodes);
        checkNode(i - spaces, j - 2*spaces, nodes);

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
