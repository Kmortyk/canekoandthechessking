package com.kmortyk.canekoandthechessking;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import com.kmortyk.canekoandthechessking.util.Vector2;
import com.kmortyk.canekoandthechessking.thread.DrawThread;

/**
 * Created by user1 on 05.11.2018.
 */

public class AppView extends SurfaceView implements SurfaceHolder.Callback {

    private DrawThread drawThread;
    private GameTouchListener gameTouchListener;

    private AppView(Context context) { super(context); }

    public AppView(Context context, DrawThread drawThread) {
        this(context);

        getHolder().addCallback(this);
        // smooth graphics
        getHolder().setFormat(PixelFormat.RGBA_8888);
        this.drawThread = drawThread;

        gameTouchListener = new GameTouchListener(context, drawThread);
        setOnTouchListener(gameTouchListener);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread.initialize(getContext(), getHolder());
        drawThread.setRun(true);

        if(drawThread.getState() == Thread.State.NEW) drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                drawThread.setRun(false);
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) { Log.e("AppView", "Interrupt exception while destroying thread."); }
        }
    }

    public void setDrawThread(DrawThread drawThread) {
        this.drawThread = drawThread;
        gameTouchListener.setDrawThread(drawThread);
    }

}
