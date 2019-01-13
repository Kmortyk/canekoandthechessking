package com.kmortyk.canekoandthechessking.game.gameinterface.model;

import android.graphics.Canvas;

import com.kmortyk.canekoandthechessking.game.item.GameItem;
import com.kmortyk.canekoandthechessking.resources.ResourceManager;

public class InventoryCell extends Backing {

    public InventoryCell(float left, float top, float w, float h) {
        super(left, top, w, h);
        setFlags(true, true);
    }

    @Override
    public void draw(Canvas canvas) { super.draw(canvas); }

    @Override
    public void onTouchDown(float x, float y) { super.onTouchDown(x, y); }

    @Override
    public boolean onTouch(float x, float y) { return super.onTouch(x, y); }

    public void setItem(GameItem item) { setTexture(item.texture, 0, 0, 255); }

}
