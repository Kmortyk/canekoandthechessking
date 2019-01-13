package com.kmortyk.canekoandthechessking.thread;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import com.kmortyk.canekoandthechessking.MapActivity;
import com.kmortyk.canekoandthechessking.game.gameinterface.model.InterfaceElement;
import com.kmortyk.canekoandthechessking.game.gameinterface.model.TextElement;
import com.kmortyk.canekoandthechessking.R;
import com.kmortyk.canekoandthechessking.game.object.MapObject;
import com.kmortyk.canekoandthechessking.resources.ResourceManager;
import com.kmortyk.canekoandthechessking.game.effects.Effect;
import com.kmortyk.canekoandthechessking.game.effects.JumpOnSpot;
import com.kmortyk.canekoandthechessking.game.math.Vector2;
import com.kmortyk.canekoandthechessking.game.object.GameObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class MapThread extends DrawThread {

    private static final boolean DEBUG_TOUCHES = false;
    private static final boolean DEBUG_MODE = false;

    private ArrayList<Vector2> debugTouches;

    private MapActivity mapActivity;

    private LinkedList<Effect> effects;
    private ArrayList<InterfaceElement> interfaceElements;
    private TextElement mapText;

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
        effects = new LinkedList<>();
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

        while (isRun()) {

            Canvas canvas = surfaceHolder.lockCanvas();

            calculateDelta();
            update(delta);

            if(canvas != null) {

                if(firstRun()) {
                    width  = canvas.getWidth();
                    height = canvas.getHeight();

                    mapText = new TextElement("", width / 2, height / 8);
                    mapText.setAlignCenter(true);

                    interfaceElements.add(mapText);
                    viewOffset.set(0, height-worldMap.getHeight());
                    heroToObject(activeObjects.get(0));
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
        Iterator<Effect> it = effects.iterator();
        while (it.hasNext()) {
            Effect e = it.next();
            if (!e.extend(delta)) { e.dispose(); it.remove(); }
        }
    }

    private void heroToObject(MapObject o) {
        littleCaneko.pos.set(o.pos.x,
                             o.pos.y - littleCaneko.getHeight() + o.texture.getHeight() / 2);

        Log.d("Compare", "\"" + mapText.getText() + "\" and \"" + o.label + "\"");

        if(mapText.getText().equals(o.label)) startLevel(o.level);
        mapText.setText(o.label);
    }

    @Override
    public void onTouch(MotionEvent event) {
        if(DEBUG_TOUCHES) debugTouches.add(new Vector2(event.getX() - viewOffset.x, event.getY() - viewOffset.y));

        for (final MapObject o: activeObjects) {
            if(o.contains(event.getX() - viewOffset.x, event.getY() - viewOffset.y)) {

                heroToObject(o);

                effects.add(new JumpOnSpot(o, 2));
                effects.add(new JumpOnSpot(littleCaneko, 2));

                return;
            }
        }

    }

    private void startLevel(String level) { mapActivity.startLevel(level); }

    @Override
    public void onSwipe(float dx, float dy) {

        float px = viewOffset.x + dx;
        float py = viewOffset.y + dy;

        if(px >= width  - worldMap.getWidth()  && px <= 0)  viewOffset.add(dx, 0);
        if(py >= height - worldMap.getHeight() && py <= 0) viewOffset.add(0, dy);

    }

    private void drawEffects(Canvas canvas) { for(int i = 0; i < effects.size(); i++) effects.get(i).draw(canvas, viewOffset); }

    private void drawInterface(Canvas canvas) {
        for (int i = 0; i < interfaceElements.size(); i++) {
            interfaceElements.get(i).draw(canvas);
            if(DEBUG_MODE) canvas.drawRect(interfaceElements.get(i).bounds, debugPaint);
        }
    }

    private void drawHero(Canvas canvas) {
        canvas.drawBitmap(littleCaneko.texture, littleCaneko.pos.x + viewOffset.x, littleCaneko.pos.y + viewOffset.y, null);
        if(DEBUG_MODE) canvas.drawRect(littleCaneko.getDebugRect(viewOffset), debugPaint);
    }

    private void drawMap(Canvas canvas) { canvas.drawBitmap(worldMap, viewOffset.x, viewOffset.y, null); }

    private void drawObjects(Canvas canvas) {
        for (GameObject o: activeObjects) {
            canvas.drawBitmap(o.texture, o.pos.x + viewOffset.x, o.pos.y + viewOffset.y, null);
            if(DEBUG_MODE) canvas.drawRect(o.getDebugRect(viewOffset), debugPaint);
        }
    }

    private void drawDebug(Canvas canvas) {
        for (int i = 0; i < debugTouches.size(); i++) {
            Vector2 touch = debugTouches.get(i);

            float x = touch.x + viewOffset.x;
            float y = touch.y + viewOffset.y;

            canvas.drawCircle(x, y, 2, debugPaint);
            canvas.drawText(touch.x + " " + touch.y, x, y, debugPaint);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        worldMap.recycle();
        littleCaneko.texture.recycle();
    }

}
