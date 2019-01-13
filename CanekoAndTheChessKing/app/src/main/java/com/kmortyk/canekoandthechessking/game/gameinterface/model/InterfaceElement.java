package com.kmortyk.canekoandthechessking.game.gameinterface.model;
import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * Used for elements that are constantly on the screen
 * in one place and with which you can interact.
 */
public abstract class InterfaceElement {

    /**
     * Position - center of bounds.
     */
    public RectF bounds;

    InterfaceElement() { bounds = new RectF(); }

    InterfaceElement(float left, float top, float w, float h) { bounds = new RectF(left, top,left + w, top + h); }

    /* --- bounds ---------------------------------------------------------------------------------*/

    public final boolean contains(float x, float y) { return bounds.contains(x, y); }

    public final void offset(float dx, float dy) { bounds.offset(dx, dy); }

    public void offsetTo(float x, float y) { bounds.offsetTo(x, y); }

    public void centering() { bounds.offset(-bounds.width()*0.5f, -bounds.height()*0.5f); }

    public float width() { return bounds.width(); }

    public float height() { return bounds.height(); }

    /**
     * Called to draw an element on canvas.
     * @param canvas of view
     */
    public abstract void draw(Canvas canvas);

    /**
     * Called when directly clicked.
     */
    public abstract boolean onTouch(float x, float y);

    /**
     * Called when touched down...
     */
    public void onTouchDown(float x, float y) { }

    /**
     * Will be called before the element link is destroyed.
     */
    public abstract void dispose();


}
