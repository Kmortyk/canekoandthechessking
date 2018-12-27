package com.kmortyk.canekoandthechessking.game.math;

public class Vector2 {

    public float x, y;

    public Vector2() {
        x = 0;
        y = 0;
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2 set(Vector2 o) {
        this.x = o.x;
        this.y = o.y;
        return this;
    }

    public static Vector2 toIsometric(Vector2 v) {
        return v.set(v.y-v.x, (v.x+v.y)/2 );
    }

    public static Vector2 fromIsometric(Vector2 v) {
        return v.set( (2*v.y + v.x)/2, (2*v.y-v.x)/2 );
    }

    public void add(float x, float y) {
        this.x += x;
        this.y += y;
    }

    public static float distance2(float x1, float y1, float x2, float y2) {
        return (x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1);
    }

}
