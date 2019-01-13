package com.kmortyk.canekoandthechessking.game.item;

import android.graphics.Bitmap;

import com.kmortyk.canekoandthechessking.game.math.Vector2;
import com.kmortyk.canekoandthechessking.game.object.GameObject;

public class GameItem extends GameObject {

    private int i, j;

    public GameItem(Vector2 pos, Bitmap texture) { super(pos, texture); }

    public GameItem(Bitmap texture) { super(texture); }

    private GameItem(GameItem gameItem) {
        super(gameItem.texture);
        i = j = 0;
    }

    public GameItem toInterfaceItem(int i, int j) {
        GameItem interfaceItem = new GameItem(this);
        interfaceItem.setInterfacePos(i, j);
        return new GameItem(this);
    }

    private void setInterfacePos(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public int getI() { return i; }

    public int getJ() { return j; }

}
