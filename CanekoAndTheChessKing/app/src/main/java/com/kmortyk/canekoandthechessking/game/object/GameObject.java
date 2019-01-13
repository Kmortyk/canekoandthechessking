package com.kmortyk.canekoandthechessking.game.object;

import android.graphics.Bitmap;
import android.graphics.RectF;

import com.kmortyk.canekoandthechessking.game.math.Vector2;
import com.kmortyk.canekoandthechessking.resources.GameResources;

public class GameObject {

    public  Vector2 pos;      // actual position of object
    public  Bitmap  texture;
    private RectF   bounds;   // rectangle that surrounds the object

    /**
     *  -------                  -------
     * |       | <- bounds      |       | <- bounds
     * |  pos  |            or  |       |
     * |       |                |pos    |
     *  -------                  -------
     */

    public GameObject(Bitmap texture) { this(new Vector2(), texture); }

    public GameObject(Vector2 pos, Bitmap texture) {
        this.pos = pos;
        this.texture = texture;

        bounds = new RectF(0, 0, texture.getWidth(), texture.getHeight());
    }

    public final boolean contains(float x, float y) {
        float px = x - pos.x;
        float py = y - pos.y;

        return px >= bounds.left && py >= bounds.top && px <= bounds.right && py <= bounds.bottom;
    }

    public final RectF getDebugRect(Vector2 viewOffsetVector) {
        return new RectF(
                   (int)(pos.x + viewOffsetVector.x) + bounds.left,
                   (int)(pos.y + viewOffsetVector.y) + bounds.top,
                  (int)(pos.x + viewOffsetVector.x) + bounds.right,
                (int)(pos.y + viewOffsetVector.y) + bounds.bottom
        );
    }

    public final float centerX() { return pos.x + bounds.centerX(); }

    public final float centerY() { return pos.y + bounds.centerY(); }

    public final float bottomY() { return pos.y + bounds.height(); }

    public final void centering() {
        pos.x -= texture.getWidth() / 2;
        pos.y += texture.getHeight() / 2;
    }

    public final void offset(float dx, float dy) {
        pos.x += dx;
        pos.y += dy;
    }

    public void moveTo(int i, int j) {
        float x = GameResources.getSectionWidth() * i;
        float y = GameResources.getSectionHeight() * j - texture.getHeight();
        pos.set(x, y);
        Vector2.toIsometric(pos);
    }

    public float getWidth() { return bounds.width(); }

    public float getHeight() { return bounds.height(); }

    protected void centerBounds() { bounds.offset(-bounds.width()/4, -bounds.height()/4); }

    protected void scaleBounds(float sx, float sy) { bounds.right *= sx; bounds.bottom *= sy; }

    protected void setZeroBounds() { bounds.set(0, 0, 0, 0); }

}
