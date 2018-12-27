package com.kmortyk.canekoandthechessking.game.graphics;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;

import com.kmortyk.canekoandthechessking.game.math.Vector2;

public class Shaders {

    public static Shader getBackgroundGradient(float width, float height, Vector2 offset) {

        int offsetX = (int) (offset.x * 0.2);
        int offsetY = (int) (offset.y * 0.2);

        return new LinearGradient(offsetX, offsetY, width + offsetX, height + offsetY,
                Color.parseColor("#393939"),
                Color.parseColor("#1F1F1F"),
                Shader.TileMode.CLAMP);
    }

}
