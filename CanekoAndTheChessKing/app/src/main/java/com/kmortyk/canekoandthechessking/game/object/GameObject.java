package com.kmortyk.canekoandthechessking.game.object;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.kmortyk.canekoandthechessking.game.math.Vector2;
import com.kmortyk.canekoandthechessking.resources.GameResources;

import java.util.jar.Attributes;

public class GameObject {

    public Vector2 pos;    // FIXME bad design: (pos, bounds) -> (bounds)
    public Rect    bounds;
    public Bitmap  texture;

    /**
     * For derived classes
     */
    protected GameObject() { }

    public GameObject(Vector2 pos, Bitmap texture) {
        this.pos = pos;
        this.texture = texture;

        bounds = new Rect();
        bounds.set(0, 0, texture.getWidth(), texture.getHeight() * 2);
    }

    public final boolean contains(float x, float y) {
        float px = x - pos.x;
        float py = y - pos.y;

        return px >= bounds.left && py >= bounds.top && px <= bounds.right && py <= bounds.bottom;
    }

    public final Rect getDebugRect(Vector2 viewOffsetVector) {
        return new Rect(
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

    public void moveTo(int i, int j) {
        float x = GameResources.getSectionWidth() * i;
        float y = GameResources.getSectionHeight() * j - texture.getHeight();
        pos.set(x, y);
        Vector2.toIsometric(pos);
    }

}
