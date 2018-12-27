package com.kmortyk.canekoandthechessking.game.gameinterface;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

import com.kmortyk.canekoandthechessking.resources.ResourceManager;

public class TextElement extends InterfaceElement {

    private static final Typeface defType = ResourceManager.getInstance().loadTypeface("sofiapro_light");
    private Paint paint;
    private String text;

    private final float cx, cy;

    public TextElement(float cx, float cy, String text) {
        paint         = new Paint();
        paint .setTypeface(defType);
        paint.setColor(Color.WHITE);
        paint.      setTextSize(40);
        paint.   setAntiAlias(true);

        this.cx = cx;
        this.cy = cy;

        this.text = text;
        this.bounds = new RectF();

        setBounds(cx, cy, text);
    }

    public void setFont(String name) { paint.setTypeface( ResourceManager.getInstance().loadTypeface(name)); }

    public void setText(String text) {
        setBounds(cx, cy, text);
        this.text = text;
    }

    public String getText() { return text; }

    @Override
    public void draw(Canvas canvas) { canvas.drawText(text, bounds.left, bounds.bottom, paint); }

    @Override
    public void dispose() { }

    @Override
    public void onClick() { }

    private void setBounds(float cx, float cy, String text) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        this.bounds.set(bounds);
        this.bounds.offset(cx - bounds.width()  / 2.0f,
                           cy - bounds.height() / 2.0f);
    }
}
