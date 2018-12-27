package com.kmortyk.canekoandthechessking;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import com.kmortyk.canekoandthechessking.game.math.Vector2;
import com.kmortyk.canekoandthechessking.thread.DrawThread;

/**
 * Created by user1 on 05.11.2018.
 */

public class AppView extends SurfaceView implements SurfaceHolder.Callback {

    DrawThread drawThread;

    public AppView(Context context, DrawThread drawThread) {
        super(context);

        getHolder().addCallback(this);
        // smooth graphics
        getHolder().setFormat(PixelFormat.RGBA_8888);
        this.drawThread = drawThread;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread.initialize(getContext(), getHolder());
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        drawThread.dispose();
        boolean retry = true;
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    private Vector2 startTouchPos = new Vector2();
    private boolean isMoved = false;
    private static final float movingDelta = 0.1f;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch(event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                startTouchPos.set(event.getX(), event.getY());
                break;

            case MotionEvent.ACTION_UP:
                if(!isMoved) drawThread.onTouch(event);
                else isMoved = false;
                performClick();
                break;

            case MotionEvent.ACTION_MOVE:

                float swipeX = event.getX() - startTouchPos.x;
                float swipeY = event.getY() - startTouchPos.y;
                drawThread.onSwipe(swipeX, swipeY);
                if(swipeX > movingDelta || swipeY > movingDelta) isMoved = true;
                startTouchPos.set(event.getX(), event.getY());

                break;
        }
        return true;
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }



}
