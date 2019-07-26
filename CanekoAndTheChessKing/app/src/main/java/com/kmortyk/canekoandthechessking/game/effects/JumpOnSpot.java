package com.kmortyk.canekoandthechessking.game.effects;

import android.graphics.Canvas;
import com.kmortyk.canekoandthechessking.util.Vector2;
import com.kmortyk.canekoandthechessking.game.object.GameObject;

public class JumpOnSpot extends Effect {

    private static final float jumpSpeed = 400f;

    private GameObject gameObject;

    private boolean up = true;

    private float startHeight;
    private float minHeight;

    public JumpOnSpot(GameObject gameObject, float height) {
        this.gameObject = gameObject;

        startHeight = gameObject.pos.y;
        minHeight = gameObject.pos.y - height;
    }

    @Override
    public boolean extend(float delta) {

        if(up) {
            if(gameObject.pos.y > minHeight) { gameObject.pos.y -= delta * jumpSpeed; }
            else { up = false; }
        }else {
            if(gameObject.pos.y < startHeight) { gameObject.pos.y += delta * jumpSpeed; }
            else gameObject.pos.y = startHeight;
        }

        return up || gameObject.pos.y != startHeight; // continue...

    }

    @Override
    public void draw(Canvas canvas, Vector2 viewOffset) { /* empty */ }

    @Override
    public void dispose() {
        super.dispose();
        gameObject.pos.y = startHeight; /* if cancel */
    }

}
