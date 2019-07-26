package com.kmortyk.canekoandthechessking.gameinterface;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.kmortyk.canekoandthechessking.gameinterface.model.Backing;

public class TextureBacking extends Backing {

    private Bitmap texture;

    public TextureBacking(Bitmap texture, float cx, float cy, float w, float h) {
        super(cx, cy, w, h);
        this.texture = texture;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(texture, bounds.left, bounds.top, paint);
    }
}
