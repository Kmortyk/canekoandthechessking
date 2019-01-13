package com.kmortyk.canekoandthechessking.game.gameinterface.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.kmortyk.canekoandthechessking.resources.ResourceManager;

public class TextElement extends InterfaceElement {

    private final Paint paint;

    private final float x, y; // left and top
    private float pX, pY;     // padding
    private String text;      // ...

    private boolean alignCenter;

    public TextElement(String text, float left, float top) {
        paint = new Paint();
        paint.setTypeface(ResourceManager.standardTypeface);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextSize(27*ResourceManager.getInstance().density());

        x = left;
        y = top;

        this.bounds = new RectF();

        setText(text);
    }

    @Override
    public void draw(Canvas canvas) { canvas.drawText(text, bounds.left, bounds.bottom, paint); }

    @Override
    public void dispose() { /* none */ }

    @Override
    public boolean onTouch(float x, float y) { return false; }

    /* --- get ------------------------------------------------------------------------------------*/

    public String getText() { return text; }

    /* --- set ------------------------------------------------------------------------------------*/

    public void setAlignCenter(boolean alignCenter) {
        this.alignCenter = alignCenter;
        updateBounds();
    }

    public void setPadding(float pX, float pY) {
        this.pX = pX;
        this.pY = pY;
        updateBounds();
    }

    public void setFont(String name) { paint.setTypeface( ResourceManager.getInstance().loadTypeface(name)); }

    public void setColor(int c) { paint.setColor(c); }

    public void setAlpha(int a) { paint.setAlpha(a); }

    public void setFontSize(float size) {
        paint.setTextSize(size);
        updateBounds();
    }

    public void setText(String text) { setText(text, true); }

    public void setText(String text, boolean updateBounds) {
        this.text = text;
        if(updateBounds) { updateBounds(); }
    }

    /* --------------------------------------------------------------------------------------------*/

    private void updateBounds() {
        Rect textBounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBounds);
        bounds.set(textBounds);
        bounds.offset(x + pX, y + pY);
        if(alignCenter) { centering(); }
    }

}
