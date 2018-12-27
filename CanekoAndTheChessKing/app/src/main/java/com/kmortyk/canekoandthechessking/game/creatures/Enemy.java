package com.kmortyk.canekoandthechessking.game.creatures;

import android.graphics.Bitmap;
import com.kmortyk.canekoandthechessking.game.GameWorld;
import com.kmortyk.canekoandthechessking.game.object.PathNode;

import java.util.LinkedList;
import java.util.Random;

public abstract class Enemy extends Creature {

    private Random rando;

    Enemy(GameWorld gameWorld, int i, int j, Bitmap texture) {
        super(gameWorld, i, j, texture);

        rando      = new Random();
        rando.setSeed(31 * i + j);
    }

    /**
     * @return all possibly steps from cur position
     */
    public abstract LinkedList<PathNode> allSteps();

    private void nextStep() {
        LinkedList<PathNode> nodes = allSteps();
        if(nodes != null && nodes.size() > 0) { // FIXME random step
            addPathNode(nodes.get(  (int) (rando.nextDouble() * nodes.size())  ));
        }
    }

    protected void checkNode(int nextI, int nextJ, LinkedList<PathNode> nodes) {
        if(gameWorld.isValid(nextI, nextJ) &&
                (gameWorld.isEmpty(nextI, nextJ) || gameWorld.hasHero(nextI, nextJ)) ) {
            nodes.add(new PathNode(gameWorld.get(nextI, nextJ), nextI, nextJ));
        }
    }

    @Override
    protected void onArrived() {
        super.onArrived();

        if(path.size() > 0)
            return;

        nextStep();
        gameWorld.nextEnemy();
        gameWorld.nextTurn();
    }

}
