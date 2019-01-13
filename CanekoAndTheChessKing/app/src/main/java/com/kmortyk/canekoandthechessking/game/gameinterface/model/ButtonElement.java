package com.kmortyk.canekoandthechessking.game.gameinterface.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class ButtonElement extends InterfaceElement {

    private Runnable onTouch;
    private Bitmap   texture;

    private final Paint paint;

    public ButtonElement(float left, float top, Bitmap texture, Runnable onTouch) {
        super(left, top, texture.getWidth(), texture.getHeight());
        this.texture = texture;
        this.onTouch = onTouch;

        paint = new Paint();
    }

    public void setOnTouch(Runnable onTouch) { this.onTouch = onTouch; }

    public void setTexture(Bitmap texture) { this.texture = texture; }

    @Override
    public void draw(Canvas canvas) { canvas.drawBitmap(texture, bounds.left, bounds.top, paint); }

    @Override
    public void dispose() { texture.recycle(); }

    @Override
    public boolean onTouch(float x, float y) {
        if(onTouch == null) { return false; }
        onTouch.run();
        return true;
    }

}
