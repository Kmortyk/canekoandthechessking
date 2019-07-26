package com.kmortyk.canekoandthechessking.game.effects;

import android.graphics.Canvas;
import android.support.annotation.Nullable;

import com.kmortyk.canekoandthechessking.R;
import com.kmortyk.canekoandthechessking.resources.ResourceManager;
import com.kmortyk.canekoandthechessking.util.Vector2;
import com.kmortyk.canekoandthechessking.game.object.GameObject;

public class FadeIn extends Effect {

    private GameObject object;

    // fade alpha
    private final static float alphaSpeed = 0.04f;
    private final static float maxAlpha = 255;
    private float alphaLevel = 200;

    // time before fade
    private final static float maxDelay = 1_000;
    private float deltaDelay = 0;

    public FadeIn(GameObject object, @Nullable Runnable callback) {
        this.object = object;
        setCallback(callback);
    }

    @Override
    public boolean extend(float delta) {

        if( (deltaDelay += delta) > maxDelay ) {
            if(alphaLevel < maxAlpha) alphaLevel += delta * alphaSpeed;
        }

        return alphaLevel < maxAlpha;

    }

    @Override
    public void draw(Canvas canvas, Vector2 viewOffset) {
        // update object texture
        // fine for mutable bitmaps
        //Fixme bad fade in
        object.texture = ResourceManager.getInstance().loadDrawable(R.drawable.tile, 0.3f);
    }

}
