package com.kmortyk.canekoandthechessking.game.gameinterface;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Backing extends InterfaceElement {

    private static final int   grey = 0;
    private static final int   alpha = 240;
    private static final float round = 4;

    private final Paint paint;

    public Backing(float cx, float cy, float w, float h) {
        super(cx, cy, w, h);

        paint = new Paint();
        paint.setARGB(alpha, grey, grey, grey);
    }

    @Override
    public void draw(Canvas canvas) { canvas.drawRoundRect(bounds, round, round, paint); }

    @Override
    public void dispose() { /* empty */ }

    @Override
    public void onClick() { /* empty */ }

}
