package com.kmortyk.canekoandthechessking.game.steps;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import com.kmortyk.canekoandthechessking.resources.GameResources;
import com.kmortyk.canekoandthechessking.game.creatures.Creature;
import com.kmortyk.canekoandthechessking.game.math.Vector2;
import com.kmortyk.canekoandthechessking.game.object.GameObject;

public class Step extends GameObject {

    private final static int IS_STEPABLE = 2;
    private final static int IS_TOUCHABLE = 4;

    /**
     * Shows special properties of the steps
     */
    protected int flags;

    /**
     * Hero or enemy todo npc?
     */
    private Creature creature = null;

    /**
     * For derived classes
     */
    protected Step() { }

    public Step(int flags, @NonNull Bitmap texture, int i, int j) {
        super(new Vector2(), texture);
        this.flags = flags;

        moveTo(i, j);
        if(!isTouchable()) {
            this.bounds.set(0, 0, 0, 0);
        }
    }

    /**
     * Data constructor
     */
    public Step(int flags, Bitmap texture) {
        this.texture = texture;
        this.flags = flags;
    }

    public static int getFlags(boolean isStepable, boolean isTouchable) {
        int flags = 0;

        if(isStepable)  flags |= IS_STEPABLE;
        if(isTouchable) flags |= IS_TOUCHABLE;

        return flags;
    }

    public void onStep(Creature creature) { /* Override */ }

    public Step clone(int i, int j) { return new Step(flags, texture, i, j); }

    /* --- creature -------------------------------------------------------------------------------*/

    public final Creature getCreature() { return creature; }

    public final boolean isEmpty() { return creature == null; }

    public final void setCreature(Creature creature) { this.creature = creature; }

    public final void unsetCreature() { this.creature = null; }

    /* --- flags ----------------------------------------------------------------------------------*/

    public final boolean isStepable()  { return (flags & IS_STEPABLE)  != 0; }

    public final boolean isTouchable() { return (flags & IS_TOUCHABLE) != 0; }

}
