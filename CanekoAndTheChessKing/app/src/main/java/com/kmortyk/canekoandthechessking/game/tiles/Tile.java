package com.kmortyk.canekoandthechessking.game.tiles;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.kmortyk.canekoandthechessking.game.creatures.Creature;
import com.kmortyk.canekoandthechessking.game.decoration.Decoration;
import com.kmortyk.canekoandthechessking.game.item.GameItem;
import com.kmortyk.canekoandthechessking.resources.ResourceManager;
import com.kmortyk.canekoandthechessking.util.GameMath;
import com.kmortyk.canekoandthechessking.util.Vector2;
import com.kmortyk.canekoandthechessking.game.object.GameObject;
import com.kmortyk.canekoandthechessking.resources.GameResources;

public class Tile extends GameObject {

    private final static int IS_STEPABLE = 2;
    private final static int IS_TOUCHABLE = 4;

    public Bitmap backTexture;

    /**
     * Shows special properties of the steps
     */
    protected int flags;

    /**
     * Item or decoration
     */
    private GameObject curObject = null;
    private Creature curCreature = null;

    public Tile(int flags, @NonNull Bitmap texture, @NonNull Bitmap backTexture, int i, int j) {
        super(new Vector2(), texture);
        this.backTexture = backTexture;
        this.flags = flags;

        moveTo(i, j);

        if(!isTouchable()) { setZeroBounds(); }
    }

    /**
     * Data constructor
     */
    public Tile(int flags, Bitmap texture) {
        super(texture);
        this.flags = flags;
    }

    public void onStep(Creature creature) { /* Override */ }

    @Override
    public final boolean contains(float x, float y) {
        // TODO simplify algorithm
        final float w = GameResources.getStepWidth() * 0.5f;
        final float h = GameResources.getStepHeight() * 0.5f;

        final Vector2 point = new Vector2(x, y);
        final Vector2 A = new Vector2(   pos.x,       pos.y + h);
        final Vector2 B = new Vector2(pos.x + w,      pos.y    );
        final Vector2 C = new Vector2(pos.x + w*2, pos.y + h);
        final Vector2 D = new Vector2(pos.x + w,   pos.y + h*2);

        return GameMath.pointInTriangle(point, A, B, D) || GameMath.pointInTriangle(point, B, C, D);

    }

    public Tile clone(int i, int j) { return new Tile(flags, texture, backTexture, i, j); }

    /* --- current object -------------------------------------------------------------------------*/

    public final boolean isEmpty() { return curCreature == null; }

    // set

    public final void setCreature(Creature curCreature) { this.curCreature = curCreature; }

    public final void setObject(GameObject curObject) { this.curObject = curObject; }

    public final void setEmpty() { curCreature = null; }

    // has smth

    public final boolean  hasCreature() { return curCreature != null; }

    public final boolean  hasItem()     { return curObject instanceof GameItem; }

    public final boolean hasDecoration() { return curObject instanceof Decoration; }

    public final boolean hasActiveDecoration() { return hasDecoration() && getDecoration().isActive(); }

    // get

    public final GameObject getObject()     { return            curObject; }

    public final Creature   getCreature()   { return          curCreature; }

    public final GameItem   getItem()       { return (GameItem) curObject; }

    public final Decoration getDecoration() { return (Decoration) curObject; }

    /* --- flags ----------------------------------------------------------------------------------*/

    public final boolean isStepable()  { return (flags & IS_STEPABLE)  != 0; }

    public final boolean isTouchable() { return (flags & IS_TOUCHABLE) != 0; }

    public final boolean hasBackTexture() { return backTexture != ResourceManager.emptyBitmap; }

    public static int getFlags(boolean isStepable, boolean isTouchable) {
        int flags = 0;

        if(isStepable)  flags |= IS_STEPABLE;
        if(isTouchable) flags |= IS_TOUCHABLE;

        return flags;
    }

}
