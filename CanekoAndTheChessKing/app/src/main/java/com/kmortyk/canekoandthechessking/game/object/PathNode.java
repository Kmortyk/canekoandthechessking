package com.kmortyk.canekoandthechessking.game.object;

import android.support.annotation.NonNull;

import com.kmortyk.canekoandthechessking.game.tiles.Tile;

public class PathNode {

    public int i, j;
    public float x, y;

    // weights for A* algorithm
    public int f,g,h;
    public int cost = 1;
    public PathNode parent = null;

    public PathNode(@NonNull Tile tile, int i, int j) {
        x = tile.centerX();
        y = tile.centerY();
        this.i = i;
        this.j = j;
    }

    public PathNode(float x, float y, int i, int j) {
        this.x = x;
        this.y = y;
        this.i = i;
        this.j = j;
    }

    public void clearWeights() {
        f = g = h = 0;
        parent = null;
    }

    /**
     * @return square of absolute distance between n1 and n2
     */
    public static int distance(PathNode n1, PathNode n2) { return (int) (Math.abs(n2.x - n1.x) + Math.abs(n2.y - n1.y)); }

    @Override
    public boolean equals(Object o) {

        if(o instanceof PathNode) {
            PathNode node = (PathNode) o;
            return this.i == node.i && this.j == node.j;
        }

        return false;
    }

    @Override @NonNull
    public String toString() {
        return "(" + i + ", " + j + ")";
    }

}
