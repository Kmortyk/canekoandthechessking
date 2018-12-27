package com.kmortyk.canekoandthechessking.game.gameinterface;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.kmortyk.canekoandthechessking.game.math.Vector2;

/**
 * Used for elements that are constantly on the screen
 * in one place and with which you can interact.
 */
public abstract class InterfaceElement {

    /**
     * Position - center of bounds.
     */
    public RectF bounds;

    /**
     * Shows priority of clicking on the item and
     * the priority of its drawing.
     */
    private int zIndex = 0;

    InterfaceElement() { }

    InterfaceElement(float cx, float cy, float w, float h) {
        w /= 2.0f;
        h /= 2.0f;
        bounds = new RectF(cx - w, cy - h, cx + w, cy + h);
    }

    /**
     * Called to draw an element on canvas.
     * @param canvas of view
     */
    public abstract void draw(Canvas canvas);

    /**
     * Will be called before the element link is destroyed.
     */
    public abstract void dispose();

    /**
     * Called when directly clicked.
     */
    public abstract void onClick();

    public final boolean clickable() { return zIndex > 0; }

    public final void setZIndex(int zIndex) { this.zIndex = zIndex; }

    public final boolean contains(float x, float y) { return bounds.contains(x, y); }

    /**
     * For debug purposes.
     * @return bounds
     * @deprecated
     */
    public final RectF getDebugRect() { return bounds; }

}
