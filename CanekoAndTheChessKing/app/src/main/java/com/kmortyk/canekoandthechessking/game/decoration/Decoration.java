package com.kmortyk.canekoandthechessking.game.decoration;

import android.graphics.Bitmap;

import com.kmortyk.canekoandthechessking.util.Vector2;
import com.kmortyk.canekoandthechessking.game.object.GameObject;
import com.kmortyk.canekoandthechessking.resources.GameResources;

public class Decoration extends GameObject {

    private float height;

    public Decoration(Bitmap texture) { super(texture); }

    public Decoration(Bitmap texture, int i, int j) {
        super(new Vector2(), texture);
        moveTo(i, j);
    }

    public Decoration clone(int i, int j) { return new Decoration(texture, i, j); }

    @Override
    public void moveTo(int i, int j) {
        float x = GameResources.getSectionWidth() * i;
        float y = GameResources.getSectionHeight() * j - GameResources.getStepHeight();

        this.pos = Vector2.toIsometric(new Vector2(x, y));
    }

    public void setHeight(float height) {
        pos.y += this.height;
        pos.y -= height;
        this.height = height;
    }

    /* --- active -------------------------------------------------------------------------------- */

    /**
     * Determines whether the decoration can be activated by tap
     */
    private boolean isActive = false;

    public boolean isActive() { return isActive; }

    public void setActive(boolean isActive) { this.isActive = isActive; }

}
