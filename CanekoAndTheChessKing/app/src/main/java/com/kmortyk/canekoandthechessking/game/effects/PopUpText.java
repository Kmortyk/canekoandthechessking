package com.kmortyk.canekoandthechessking.game.effects;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.kmortyk.canekoandthechessking.game.GameWorld;
import com.kmortyk.canekoandthechessking.game.math.Vector2;

import java.util.List;

public class PopUpText extends Effect {

    private Paint paint;

    private RectF bounds;
    private String text;

    // rect corners round radius
    private final static float round = 30;

    // fade alpha
    private final static float alphaSpeed = 0.04f;
    private float alphaLevel = 255;

    // vertical moving
    private final static float heightSpeed = 0.01f;
    private float heightOffset = 0;

    // time before fade
    private final static float maxDelay = 4_000;
    private float deltaDelay = 0;

    public PopUpText(float x, float y, String text) {
        this.text = text;

        bounds = new RectF();

        paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(20);

        init(x, y);
    }

    @Override
    public boolean extend(float delta) {

        if( (deltaDelay += delta) > maxDelay) {
            if(alphaLevel > 0) alphaLevel -= delta * alphaSpeed;
            heightOffset = -delta * heightSpeed;
        }

        return alphaLevel > 0;

    }

    @Override
    public void draw(Canvas canvas, Vector2 viewOffset) {

        bounds.offset(0, heightOffset);

        paint.setARGB((int)alphaLevel, 0, 0, 0);

        bounds.offset(viewOffset.x, viewOffset.y);
        canvas.drawRoundRect(bounds, round, round, paint);
        bounds.offset(-viewOffset.x, -viewOffset.y);

        paint.setARGB((int)alphaLevel, 191, 191, 191);
        canvas.drawText(text, bounds.centerX() + viewOffset.x,
                              bounds.bottom - (bounds.height() / 5) + viewOffset.y, paint);

    }

    @Override
    public void dispose() { }

    /**
     * Set popUp text to direct position
     */
    private void init(float x, float y) {
        alphaLevel = 255;
        heightOffset = 0;
        deltaDelay = 0;

        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        // ~ equals
        int letterWidth = bounds.width() / text.length();

        float width = (text.length() + 6) * letterWidth;
        float height = bounds.height() * 1.5f;

        float half_w = width / 2;
        float half_h = height / 2;

        this.bounds.set(x - half_w, y - half_h, x + half_w, y + half_h);
    }

    /**
     * Create new pop-up message effect and add it to scene.
     */
    public static void addTo(GameWorld gameWorld, float x, float y, String text) {
        gameWorld.addEffect(new PopUpText(x, y, text));
    }

    /**
     * If scene has pop-up message with the same text, simply move it,
     * otherwise it creates a new one.
     */
    public static void uniqueAddTo(GameWorld gameWorld, float x, float y, String text) {
        uniqueAddTo(gameWorld.getEffects(), x, y, text);
    }

    public static void uniqueAddTo(List<Effect> effects, float x, float y, String text) {

        for (int i = 0; i < effects.size(); i++) {

            Effect e = effects.get(i);

            if(e instanceof PopUpText) {
                PopUpText popUp = (PopUpText) e;
                // if matches
                if(popUp.text.equals(text)) {
                    // recreate in pos (x, y)
                    popUp.init(x, y);
                    return;
                }
            }

        }

        // else
        effects.add(new PopUpText(x, y, text));

    }

}
