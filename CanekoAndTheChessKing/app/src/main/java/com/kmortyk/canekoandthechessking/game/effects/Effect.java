package com.kmortyk.canekoandthechessking.game.effects;

import android.graphics.Canvas;

import com.kmortyk.canekoandthechessking.util.Vector2;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public abstract class Effect {

    /**
     * Called after the effect ends.
     */
    private Runnable callback = null;

    /**
     * Determines whether the effect should be drawn in the next frame.
     */
    public abstract boolean extend(float delta);

    /**
     * Called to draw an object on canvas.
     * @param canvas of view
     * @param viewOffset camera offset
     */
    public abstract void draw(Canvas canvas, Vector2 viewOffset);

    /**
     * Will be called before the object link is destroyed.
     */
    public void dispose() { if(callback != null) { callback.run(); } }

    public final void setCallback(Runnable callback) { this.callback = callback; }

}
