package com.kmortyk.canekoandthechessking.game.effects;

import android.graphics.Canvas;

import com.kmortyk.canekoandthechessking.resources.ResourceManager;
import com.kmortyk.canekoandthechessking.util.Vector2;
import com.kmortyk.canekoandthechessking.game.object.GameObject;
import com.kmortyk.canekoandthechessking.thread.DrawThread;

public class MoveCamera extends Effect {

    private final DrawThread drawThread;
    private final Vector2 to;

    private final static float speed = ResourceManager.pxFromDp(4.266f);
    private final static float err   = 5f;

    private Vector2 lastOffset = new Vector2();
    private float lastDst;

    /**
     * Create smooth camera effect for DrawThread.
     * @param drawThread main thread (exm: GameThread)
     * @param width of canvas, to center object on screen
     * @param height for centering
     * @param o object which should be in the center
     */
    public MoveCamera(DrawThread drawThread, int width, int height, GameObject o) { this(drawThread, width*0.5f - o.pos.x, height*0.5f - o.pos.y); }

    /**
     * @param drawThread main thread (exm: GameThread)
     * @param toX direct position X
     * @param toY direct position Y
     */
    public MoveCamera(DrawThread drawThread, float toX, float toY) {
        this.drawThread = drawThread;
        this.to = new Vector2(toX, toY);

        // save start values
        lastOffset.set(drawThread.getViewOffset());
        lastDst = Vector2.dst2(drawThread.getViewOffset(), to);
    }

    @Override
    public boolean extend(float delta) {

        Vector2 viewOffset = drawThread.getViewOffset();

        // if changed by player
           if(!lastOffset.equals(viewOffset)) return false;

        float speed = getSpeed(Vector2.dst(viewOffset, to));

        if(viewOffset.x < to.x) viewOffset.x += speed * 1.5;
        if(viewOffset.x > to.x) viewOffset.x -= speed * 1.5;

        if(viewOffset.y < to.y) viewOffset.y += speed;
        if(viewOffset.y > to.y) viewOffset.y -= speed;

        float dst = Vector2.dst2(viewOffset, to);

        // if the distance change was too small, complete
        if(lastDst - dst < err) { return false; }
        else {
            lastOffset.set(viewOffset); // save method value
            lastDst = dst;              // save dst
            return true;
        }

    }

    @Override
    public void draw(Canvas canvas, Vector2 viewOffset) { /* none */ }

    // dst2, dst - the closer, the lower the speed
    // speed, delta - smooth
    private float getSpeed(float dst) {

        // linear
        float v  = speed * dst;
              v /= 100;

        // quadratic
        // float v2 = speed * delta * dst * dst - dst;
        //       v2 /= 10000;

        return v;

    }

}
