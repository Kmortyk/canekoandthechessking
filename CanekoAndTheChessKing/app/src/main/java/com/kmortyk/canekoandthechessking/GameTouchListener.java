package com.kmortyk.canekoandthechessking;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.kmortyk.canekoandthechessking.thread.DrawThread;

public class GameTouchListener implements View.OnTouchListener {

    public static final int SWIPE_THRESHOLD = 100;
    public static final int SWIPE_VELOCITY_THRESHOLD = 100;

    private GestureDetector gestureDetector;
    private DrawThread drawThread;

    public GameTouchListener(Context context, DrawThread drawThread) {
        this.drawThread = drawThread;
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    public void setDrawThread(DrawThread drawThread) { this.drawThread = drawThread; }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            drawThread.onTouch(e);
            return true;
        }

        @Override
        public boolean onDown(MotionEvent event) {
            drawThread.onTouchDown(event);
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            drawThread.onSwipe(-distanceX, -distanceY);
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            float dx = e2.getX() - e1.getX();
            float dy = e2.getY() - e1.getY();

            if (Math.abs(dx) > Math.abs(dy)) {
                if (Math.abs(dx) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (dx > 0) { drawThread.onSwipeRight(e1, e2, velocityX, velocityY); }
                    else        { drawThread.onSwipeLeft (e1, e2, velocityX, velocityY); }
                }
            } else {
                if (Math.abs(dy) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (dy > 0) { drawThread.onSwipeBottom(e1, e2, velocityX, velocityY); }
                    else        { drawThread.onSwipeTop   (e1, e2, velocityX, velocityY); }
                }
            }

            return true;
        }
    }

}
