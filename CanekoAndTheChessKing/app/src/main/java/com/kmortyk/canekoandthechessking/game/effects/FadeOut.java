package com.kmortyk.canekoandthechessking.game.effects;

import android.graphics.Canvas;
import android.support.annotation.Nullable;

import com.kmortyk.canekoandthechessking.resources.ResourceManager;
import com.kmortyk.canekoandthechessking.game.math.Vector2;
import com.kmortyk.canekoandthechessking.game.object.GameObject;

public class FadeOut extends Effect {

    private GameObject object;

    // fade alpha
    private final static float alphaSpeed = 0.04f;
    private final static float minAlpha = 200;
    private float alphaLevel = 255;

    // time before fade
    private final static float maxDelay = 1_000;
    private float deltaDelay = 0;

    public FadeOut(GameObject object, @Nullable Runnable callback) {
        this.object = object;
        setCallback(callback);
    }

    @Override
    public boolean extend(float delta) {

        if( (deltaDelay += delta) > maxDelay ) {
            if(alphaLevel > minAlpha) alphaLevel -= delta * alphaSpeed;
        }

        return alphaLevel > minAlpha;

    }

    @Override
    public void draw(Canvas canvas, Vector2 viewOffset) {
        // update object texture
        // fine for mutable bitmaps
        object.texture = ResourceManager.adjustOpacity(object.texture, (int) alphaLevel);
    }

}
