package com.kmortyk.canekoandthechessking.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;

import java.util.LinkedList;
import java.util.List;

/**
 * Date class for storing a bitmap part.
 */

class BitmapPart{

    float x, y;
    Bitmap btm;

    BitmapPart(Bitmap btm, float x, float y) {
        this.x = x;
        this.y = y;
        this.btm = btm;
    }

}

/**
 * It is a class that contains parts of bitmaps.
 *
 * United bitmap canBeExecuted be obtained
 * through the method execute.
 */
public class ExtendableBitmap {

    private static final Bitmap.Config config = Bitmap.Config.ARGB_8888;

    private List<BitmapPart> parts;
    private RectF bounds;

    public ExtendableBitmap() {
        parts = new LinkedList<>();
        bounds = new RectF();
    }

    /**
     * Add another bitmap to list of parts.
     */
    public void addPart(Bitmap btm, float x, float y) {
        parts.add(new BitmapPart(btm, x, y));
        bounds.union(x, y, x + btm.getWidth(), y + btm.getHeight());
    }

    /**
     * Draw all parts in one bitmap and returns.
     */
    public Bitmap apply() {

        Bitmap btm = Bitmap.createBitmap((int) bounds.width(), (int) bounds.height(), config);
        Matrix matrix = new Matrix();
        Canvas canvas = new Canvas(btm);

        for (BitmapPart part: parts) {
            matrix.reset();
            matrix.setTranslate(part.x - bounds.left, part.y - bounds.top);
            // it should be a matrix, do not touch
            canvas.drawBitmap(part.btm, matrix, null);
        }

        return btm;

    }

    /**
     * Delete all parts and set bounds to 0.
     */
    public void clear() {
        bounds.set(0, 0, 0, 0);
        parts.clear();
    }

    /* --- get - set ------------------------------------------------------------------------------*/

    public boolean isEmpty() { return parts.isEmpty(); }

    public RectF getBounds() { return bounds; }

    /**
     * Return x and y of general bitmap.
     * @return vector with x=left and y=top.
     */
    public Vector2 getPos() { return new Vector2(bounds.left, bounds.top); }

    public float getX() { return bounds.left; }

    public float getY() { return bounds.top; }

}
