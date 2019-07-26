package com.kmortyk.canekoandthechessking.util;

import android.support.annotation.NonNull;

public class Vector2 {

    public float x, y;

    public Vector2() { }

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
        return v.set( v.y-v.x, (v.x+v.y)/2 );
    }

    // public static Vector2 fromIsometric(Vector2 v) { return v.set( (2*v.y + v.x)/2, (2*v.y-v.x)/2 ); }

    public Vector2 add(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public static float dst(Vector2 from, Vector2 to) { return dst(from.x, from.y, to.x, to.y); }

    public static float dst(float x1, float y1, float x2, float y2) { return (float) Math.sqrt(dst2(x1, y1, x2, y2)); }

    /**
     * Used for comparisons of distance between vectors.
     * For accurate value use dst.
     * @return square of the actual distance.
     */
    public static float dst2(Vector2 from, Vector2 to) { return dst2(from.x, from.y, to.x, to.y); }

    public static float dst2(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return dx*dx + dy*dy;
    }

    @Override
    public boolean equals(Object obj) {

        if(obj instanceof Vector2) {
            Vector2 o = (Vector2) obj;
            return x == o.x && y == o.y;
        }

        return false;

    }

    @Override @NonNull
    public String toString() { return "(" + x + ", " + y + ")"; }

}
