package com.kmortyk.canekoandthechessking.gameinterface.effect;

import android.graphics.Canvas;
import android.util.Log;

import com.kmortyk.canekoandthechessking.game.effects.Effect;
import com.kmortyk.canekoandthechessking.gameinterface.model.InterfaceElement;
import com.kmortyk.canekoandthechessking.resources.ResourceManager;
import com.kmortyk.canekoandthechessking.util.Vector2;

public class Move extends Effect {

    private static final float movingSpeed = ResourceManager.pxFromDp(100);

    private Vector2 curPos = new Vector2();
    private Vector2 toPos = new Vector2();

    private InterfaceElement e;

    public Move(InterfaceElement e, float x, float y) {
        this.e = e;
        curPos.set(e.bounds.left, e.bounds.top);
        toPos.set(x, y);
    }

    @Override
    public boolean extend(float delta) {

        float speed = movingSpeed * delta;

        float cx = curPos.x;
        float cy = curPos.y;

        if(cx < toPos.x) cx += speed;
        if(cx > toPos.x) cx -= speed;

        if(cy < toPos.y) cy += speed;
        if(cy > toPos.y) cy -= speed;

        curPos.set(cx, cy);
        e.offsetTo(cx, cy);

        return Vector2.dst(curPos, toPos) > 1;

    }

    @Override
    public void draw(Canvas canvas, Vector2 viewOffset) { /* none */ }


}
