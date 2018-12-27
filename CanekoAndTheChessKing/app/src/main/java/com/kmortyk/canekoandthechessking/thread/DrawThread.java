package com.kmortyk.canekoandthechessking.thread;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.kmortyk.canekoandthechessking.game.math.Vector2;

public abstract class DrawThread extends Thread {

    protected SurfaceHolder surfaceHolder;
    protected Context context;

    protected boolean isRun  = true; // TODO use interrupted instead

    protected Vector2 viewOffset = new Vector2();

    public void initialize(Context context, SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        this.context = context;
    }

    public abstract void run();

    public abstract void onTouch(MotionEvent event);

    public abstract void onSwipe(float dx, float dy);

    public void dispose() { isRun = false; }

    /* --- delta time -----------------------------------------------------------------------------*/

    private long lastTime = System.nanoTime(); // TODO use timer instead
    protected float delta = 0;

    protected final void calculateDelta() {
        /*long curTime = System.nanoTime();
        delta = (curTime - lastTime) / 1000000;
        lastTime = curTime;*/

        delta = 15; // TODO lock delta
    }

    /* --------------------------------------------------------------------------------------------*/

    private boolean firstRun = true;

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

}
