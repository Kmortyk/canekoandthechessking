package com.kmortyk.canekoandthechessking.thread;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.kmortyk.canekoandthechessking.MapActivity;
import com.kmortyk.canekoandthechessking.game.gameinterface.InterfaceElement;
import com.kmortyk.canekoandthechessking.game.gameinterface.TextElement;
import com.kmortyk.canekoandthechessking.R;
import com.kmortyk.canekoandthechessking.game.object.MapObject;
import com.kmortyk.canekoandthechessking.resources.ResourceManager;
import com.kmortyk.canekoandthechessking.game.effects.Effect;
import com.kmortyk.canekoandthechessking.game.effects.JumpOnSpot;
import com.kmortyk.canekoandthechessking.game.math.Vector2;
import com.kmortyk.canekoandthechessking.game.object.GameObject;

import java.util.ArrayList;

public class MapThread extends DrawThread {

    private static final boolean DEBUG_MODE = false;
    private ArrayList<Vector2> debugTouches;

    private MapActivity mapActivity;

    private ArrayList<Effect> effects;
    private ArrayList<InterfaceElement> interfaceElements;
    private TextElement mapText = null;

    private Paint debugPaint;

    private ArrayList<MapObject> activeObjects;
    private GameObject littleCaneko;
    private Bitmap worldMap;
    private int width, height;

    public MapThread(MapActivity mapActivity) {
        this.mapActivity = mapActivity;
        worldMap = ResourceManager.getInstance().loadDrawable(R.drawable.world_map);
        activeObjects = new ArrayList<>();
        littleCaneko = new GameObject(new Vector2(0, 0), ResourceManager.getInstance().loadDrawable(R.drawable.little_caneko));
        effects = new ArrayList<>();
        interfaceElements = new ArrayList<>();

        if(DEBUG_MODE) {
            debugTouches = new ArrayList<>();
            debugPaint = new Paint();
            debugPaint.setColor(Color.RED);
            debugPaint.setStyle(Paint.Style.STROKE);
        }

        activeObjects = ResourceManager.getInstance().loadGameWorld(worldMap);
    }

    @Override
    public void run() {

        while (isRun) {

            calculateDelta();
            update(delta);

            Canvas canvas = surfaceHolder.lockCanvas();

            if(canvas != null) {

                if(firstRun()) {
                    width  = canvas.getWidth();
                    height = canvas.getHeight();
                    heroToCenterOfObject(activeObjects.get(0));
                }

                drawMap(canvas);
                drawObjects(canvas);
                drawHero(canvas);
                drawEffects(canvas);
                drawInterface(canvas);

                if(DEBUG_MODE) drawDebug(canvas);

                surfaceHolder.unlockCanvasAndPost(canvas);
            }

        }

    }

    private void update(float delta) {
        for(int i = 0; i < effects.size(); i++) {
            if(!effects.get(i).extend(delta)) {
                effects.get(i).dispose();
                effects.remove(i); // ugh...
                i = 0;
            }
        }
    }

    private void heroToCenterOfObject(GameObject t) {
        littleCaneko.pos.set(t.pos.x + littleCaneko.bounds.width()  / 2,
                             t.pos.y - littleCaneko.bounds.height() / 2 + t.bounds.height() / 4 );
    }

    @Override
    public void onTouch(MotionEvent event) {
        if(DEBUG_MODE) debugTouches.add(new Vector2(event.getX() - viewOffset.x, event.getY() - viewOffset.y));

        for (final MapObject o: activeObjects) {
            if(o.contains(event.getX() - viewOffset.x, event.getY() - viewOffset.y)) {
                effects.add(new JumpOnSpot(o, 2));
                heroToCenterOfObject(o);

                Effect e = new JumpOnSpot(littleCaneko, 2);
                e.setCallback( new Runnable() {
                    @Override
                    public void run() {
                        if(mapText == null) {
                            mapText = new TextElement(width / 2, height / 8, o.label);
                            interfaceElements.add(mapText);
                        }else{
                            if(mapText.getText().equals(o.label)) startLevel(o.level);
                            mapText.setText(o.label);
                        }
                    }
                });
                effects.add(e);

                return;
            }
        }

    }

    private void startLevel(String level) { mapActivity.startLevel(level); }

    @Override
    public void onSwipe(float dx, float dy) {

        float px = viewOffset.x + dx;
        float py = viewOffset.y + dy;

        if(px <= 0 && px >= -worldMap.getWidth()  + width)  viewOffset.add(dx, 0);
        if(py >= 0 && py <=  worldMap.getHeight() - height) viewOffset.add(0, dy);

    }

    private void drawEffects(Canvas canvas) { for(int i = 0; i < effects.size(); i++) effects.get(i).draw(canvas, viewOffset); }

    private void drawInterface(Canvas canvas) {
        for (int i = 0; i < interfaceElements.size(); i++) {
            interfaceElements.get(i).draw(canvas);
            if(DEBUG_MODE) canvas.drawRect(interfaceElements.get(i).getDebugRect(), debugPaint);
        }
    }

    private void drawHero(Canvas canvas) { canvas.drawBitmap(littleCaneko.texture, littleCaneko.pos.x + viewOffset.x, littleCaneko.pos.y + viewOffset.y, null); }

    private void drawMap(Canvas canvas) { canvas.drawBitmap(worldMap, viewOffset.x, viewOffset.y + height - worldMap.getHeight(), null); }

    private void drawObjects(Canvas canvas) { for (GameObject o: activeObjects) canvas.drawBitmap(o.texture, o.pos.x + viewOffset.x, o.pos.y + viewOffset.y, null); }

    private void drawDebug(Canvas canvas) {
        for (int i = 0; i < debugTouches.size(); i++) {
            Vector2 touch = debugTouches.get(i);

            float x = touch.x + viewOffset.x;
            float y = touch.y + viewOffset.y;

            canvas.drawCircle(x, y, 2, debugPaint);
            canvas.drawText(touch.x + " " + touch.y, x, y, debugPaint);
        }
    }

}
