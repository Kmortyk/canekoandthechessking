package com.kmortyk.canekoandthechessking.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MovingPath implements Iterator {

    private static final int AVERAGE_MAX = 100;

    private int curIndex = 0;

    private List<Vector2> positions;
    private int size = 0;
    private int prc; // precision

    public MovingPath(int prc) {
        positions = new ArrayList<>(AVERAGE_MAX);
        for (int i = 0; i < AVERAGE_MAX; i++) { positions.add(new Vector2()); }

        this.prc = prc;
    }

    private void addPoint(float x, float y) {
        size++;
        if(size < positions.size()) { positions.get(size - 1).set(x, y); }
        else{ positions.add(new Vector2(x, y)); }
    }

    public boolean hasPath() { return size > 0; }

    @Override
    public boolean hasNext() { return curIndex < size; }

    @Override
    public Vector2 next() {
        Vector2 pos = positions.get(curIndex);
        curIndex += prc;
        return pos;
    }

    public void clear() { curIndex = 0; size = 0; }

    public Vector2 getLast() { return positions.get(size - 1); }

    /**
     *  Bresenham's algorithm.
     *  Fill list with points from A(x1,y1) to B(x2,y2)
     *  per-pixel precision
     */
    public void createPath(float x1, float y1, float x2, float y2) {

        float dx = Math.abs(x2 - x1);
        float dy = Math.abs(y2 - y1);
        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;

        float err = dx - dy;

        while( Math.abs(x2 - x1) >= 1f || Math.abs(y2 - y1) >= 1f) {

            addPoint(x1, y1);
            float err2 = err * 2;

            if(err2 > -dy) { err -= dy; x1 += sx; }
            if(err2 <  dx) { err += dx; y1 += sy; }

        }

    }

}
