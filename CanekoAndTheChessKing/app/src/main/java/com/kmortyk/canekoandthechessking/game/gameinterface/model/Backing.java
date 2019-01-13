package com.kmortyk.canekoandthechessking.game.gameinterface.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Transparent rectangle with rounded edges.
 */

public class Backing extends InterfaceElement {

    private static final float round = 4;
    private static final int   grey  = 0;
    private static final int   alpha = 240;

    protected final Paint paint;
    private Paint borderPaint;

    // flags

    private boolean bottomRound = true;
    private boolean border = false;

    // backing texture

    private Bitmap texture;
    private Rect   texturePartRect;
    private Rect   textureDistRect;
    private int    textureAlpha;

    public Backing(RectF bounds) { this(bounds.left, bounds.top, bounds.width(), bounds.height()); }

    public Backing(float left, float top, float w, float h) {
        super(left, top, w, h);

        paint = new Paint();
        paint.setARGB(alpha, grey, grey, grey);
    }

    @Override
    public void draw(Canvas canvas) {

        if(!bottomRound)
            // draw small rect to hide rounded edges
            canvas.drawRect(bounds.left, bounds.bottom - round, bounds.right, bounds.bottom, paint);

            // draw transparent rounded rect
            canvas.drawRoundRect(bounds, round, round, paint);

        if(border)
            // draw unfilled rounded rectangle
            canvas.drawRoundRect(bounds, round, round, borderPaint);

        // draw texture
        if(texture != null) {
            paint.setAlpha(textureAlpha);
            canvas.drawBitmap(texture, texturePartRect, textureDistRect, paint);
            paint.setAlpha(alpha);
        }

    }

    @Override
    public void dispose() { /* empty */ }

    @Override
    public boolean onTouch(float x, float y) { return false; }

    /* --- bounds ---------------------------------------------------------------------------------*/

    public void scale(float sx, float sy) {
        float dh = bounds.width() * (sx - 1);
        float dv = bounds.height() * (sy - 1);

        bounds.left  -= dh*0.5f;
        bounds.right += dh*0.5f;

        bounds.top    -= dv*0.5f;
        bounds.bottom += dv*0.5f;
    }

    /* --- set ------------------------------------------------------------------------------------*/

    public void setColor(int c)     { paint.setColor(c); }

    public void setAlpha(int alpha) { paint.setAlpha(alpha); }

    public void setFlags(boolean bottomRound, boolean border) {
        this.bottomRound = bottomRound;
        this.border = border;
        if(border) {
            borderPaint = new Paint();
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setColor(Color.parseColor("#B1ADAC"));
            borderPaint.setAlpha(108);
        }
    }

    /**
     * Adds a texture that is drawn on top of the rest of the elements.
     * @param texture image bitmap
     * @param pX offset from point (0,0) on X
     * @param pY offset from point (0,0) on Y
     * @param alpha texture transparency value
     */
    public void setTexture(Bitmap texture, int pX, int pY, int alpha) {

        this.texture      = texture;
        this.textureAlpha = alpha;

        textureDistRect = new Rect();
        bounds.round(textureDistRect);
        textureDistRect.inset(1, 1);

        texturePartRect = new Rect(textureDistRect);
        texturePartRect.offsetTo(0, 0);
        texturePartRect.offset(pX, pY);

    }

}
