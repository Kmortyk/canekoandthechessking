package com.kmortyk.canekoandthechessking.game.gameinterface;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.kmortyk.canekoandthechessking.game.gameinterface.model.Backing;

public class TextureBacking extends Backing {

    private Bitmap texture;

    public TextureBacking(Bitmap texture, float cx, float cy, float w, float h) {
        super(cx, cy, w, h);
        this.texture = texture;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(texture, bounds.left, bounds.top, paint);
    }
}
