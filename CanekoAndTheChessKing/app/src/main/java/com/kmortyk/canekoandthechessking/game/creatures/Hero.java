package com.kmortyk.canekoandthechessking.game.creatures;

import android.graphics.Bitmap;

import com.kmortyk.canekoandthechessking.game.tiles.Tile;
import com.kmortyk.canekoandthechessking.thread.GameThread;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user1 on 05.11.2018.
 */

public class Hero extends Creature{

    private GameThread gameThread;
    private List<Tile> activeTiles;

    public Hero(GameThread gameThread, int i, int j, Bitmap texture) {
        super(gameThread.getGameWorld(), i, j, texture);
        this.gameThread = gameThread;
        activeTiles = new ArrayList<>(5);
    }

    @Override
    protected void onArrived() {
        super.onArrived();
        path.clear();
        gameWorld.nextTurn();
        gameThread.viewToHero();
    }

    public Tile getSameTile() {
        return gameWorld.get(node.i, node.j);
    }

    public List<Tile> getActiveTiles() {
        activeTiles.clear();
        // up
        if(gameWorld.isValid             (node.i, node.j + 1))
            activeTiles.add(gameWorld.get(node.i, node.j + 1));
        // right
        if(gameWorld.isValid             (node.i + 1, node.j))
            activeTiles.add(gameWorld.get(node.i + 1, node.j));
        // down
        if(gameWorld.isValid             (node.i, node.j - 1))
            activeTiles.add(gameWorld.get(node.i, node.j - 1));
        // left
        if(gameWorld.isValid             (node.i - 1, node.j))
            activeTiles.add(gameWorld.get(node.i - 1, node.j));

        return activeTiles;
    }

}
