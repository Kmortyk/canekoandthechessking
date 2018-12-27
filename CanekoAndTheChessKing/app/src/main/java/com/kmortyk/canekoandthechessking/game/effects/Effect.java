package com.kmortyk.canekoandthechessking.game.effects;

import android.graphics.Canvas;

import com.kmortyk.canekoandthechessking.game.GameWorld;
import com.kmortyk.canekoandthechessking.game.math.Vector2;

import java.util.List;

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
    public void dispose() { if(callback != null) callback.run(); }

    public final void setCallback(Runnable callback) { this.callback = callback; }

    public static void addTo(GameWorld gameWorld, float x, float y, String text)        { throw new RuntimeException("Stub!"); }

    public static void uniqueAddTo(GameWorld gameWorld, float x, float y, String text)  { throw new RuntimeException("Stub!"); }

    public static void uniqueAddTo(List<Effect> effects, float x, float y, String text) { throw new RuntimeException("Stub!"); }
}
