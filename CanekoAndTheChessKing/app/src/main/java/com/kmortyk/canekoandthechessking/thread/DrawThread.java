package com.kmortyk.canekoandthechessking.thread;

import android.content.Context;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.kmortyk.canekoandthechessking.util.Vector2;

public abstract class DrawThread extends Thread {

    private SurfaceHolder surfaceHolder;
    protected Context context;

    protected Vector2 viewOffset = new Vector2();

    public void initialize(Context context, SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        this.context = context;
        firstRun = true;
    }

    /* --- run ------------------------------------------------------------------------------------*/

    private boolean isRun    = true; // FIXME use interrupted instead

    private boolean firstRun = true;

    private boolean isRun() { return isRun; }

    public void setRun(boolean isRun) { this.isRun = isRun; }

    /**
     * @return true one time.
     */
    protected final boolean firstRun() {
        if(firstRun) {
            firstRun = false;
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        while (isRun()) {
            calculateDelta();
            Canvas canvas = surfaceHolder.lockCanvas();
            if(canvas != null) {
                // guaranteed to be drawn in one thread
                onDraw(canvas);
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    protected abstract void onDraw(Canvas canvas);

    public abstract void dispose();

    /* --- delta time ---------------------------------------------------------------------------- */

    private long lastTime = SystemClock.elapsedRealtimeNanos(); // TODO use timer instead
    protected float delta = 0;

    private void calculateDelta() {
        //
        //  60 fps <=> delta ~ 0.016f
        //
        long curTime = SystemClock.elapsedRealtimeNanos();
        delta = (curTime - lastTime) / 1_000_000_000.0f;
        lastTime = curTime;
    }

    /* --- event ----------------------------------------------------------------------------------*/

    public abstract void onTouch(MotionEvent event);

    public abstract void onSwipe(float dx, float dy);

    public void onSwipeRight(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) { }

    public void onSwipeLeft(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) { }

    public void onSwipeTop(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) { }

    public void onSwipeBottom(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) { }

    public void onTouchDown(MotionEvent event) { }

    /* --------------------------------------------------------------------------------------------*/

    // for effects
    public Vector2 getViewOffset() { return viewOffset; }

}
