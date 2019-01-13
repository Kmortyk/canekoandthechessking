package com.kmortyk.canekoandthechessking.thread;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.kmortyk.canekoandthechessking.game.math.Vector2;

public abstract class DrawThread extends Thread {

    protected SurfaceHolder surfaceHolder;
    protected Context context;

    protected Vector2 viewOffset = new Vector2();

    public void initialize(Context context, SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        this.context = context;
    }

    /* --- run ------------------------------------------------------------------------------------*/

    private boolean isRun    = true; // FIXME use interrupted instead

    private boolean firstRun = true;

    protected boolean isRun() { return isRun; }

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

    public abstract void run();

    public void dispose() { isRun = false; }

    /* --- delta time -----------------------------------------------------------------------------*/

    private long lastTime = System.nanoTime(); // TODO use timer instead
    private static final float LOCK_DELTA = 20;
    protected float delta = 0;

    protected final void calculateDelta() {
        long curTime = System.nanoTime(); // TODO elapsed time
        delta = (curTime - lastTime) / 1000000;
        lastTime = curTime;

        if(delta > LOCK_DELTA) // FIXME lock delta
            delta = LOCK_DELTA;
    }

    /* --- event ----------------------------------------------------------------------------------*/

    public abstract void onTouch(MotionEvent event);

    public void onTouchDown(MotionEvent event) { }

    public abstract void onSwipe(float dx, float dy);

    /* --------------------------------------------------------------------------------------------*/

    public Vector2 getViewOffset() { return viewOffset; }

}
