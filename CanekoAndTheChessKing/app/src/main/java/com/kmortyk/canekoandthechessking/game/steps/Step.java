package com.kmortyk.canekoandthechessking.game.steps;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.kmortyk.canekoandthechessking.game.creatures.Creature;
import com.kmortyk.canekoandthechessking.game.item.GameItem;
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
     * Item or creature
     */
    private GameObject curObject = null;

    public Step(int flags, @NonNull Bitmap texture, int i, int j) {
        super(new Vector2(), texture);
        this.flags = flags;

        moveTo(i, j);
        scaleBounds(1, 2); // for game control convenience

        if(!isTouchable()) { setZeroBounds(); }
    }

    /**
     * Data constructor
     */
    public Step(int flags, Bitmap texture) {
        super(texture);
        this.flags = flags;
    }

    public void onStep(Creature creature) { /* Override */ }

    public Step clone(int i, int j, float offsetFactorX, float offsetFactorY) {
        Step step = clone(i, j);
        step.offset(texture.getWidth()*offsetFactorX, -texture.getHeight()*offsetFactorY);
        return step;
    }

    public Step clone(int i, int j) { return new Step(flags, texture, i, j); }

    /* --- current object -------------------------------------------------------------------------*/

    public final boolean isEmpty() { return curObject == null; }

    // set

    public final void setCurrentObject(GameObject curObject) { this.curObject = curObject; }

    public final void setEmpty() { curObject = null; }


    // has smth

    public final boolean  hasCreature() { return curObject instanceof Creature; }

    public final boolean  hasItem()     { return curObject instanceof GameItem; }

    // get

    public final GameObject getObject()   { return            curObject; }

    public final Creature   getCreature() { return (Creature) curObject; }

    public final GameItem   getItem()     { return (GameItem) curObject; }

    /* --- flags ----------------------------------------------------------------------------------*/

    public final boolean isStepable()  { return (flags & IS_STEPABLE)  != 0; }

    public final boolean isTouchable() { return (flags & IS_TOUCHABLE) != 0; }

    public static int getFlags(boolean isStepable, boolean isTouchable) {
        int flags = 0;

        if(isStepable)  flags |= IS_STEPABLE;
        if(isTouchable) flags |= IS_TOUCHABLE;

        return flags;
    }

}
