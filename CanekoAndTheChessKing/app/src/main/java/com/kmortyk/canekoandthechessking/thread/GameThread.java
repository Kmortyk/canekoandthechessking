package com.kmortyk.canekoandthechessking.thread;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.kmortyk.canekoandthechessking.GameActivity;
import com.kmortyk.canekoandthechessking.game.GameMap;
import com.kmortyk.canekoandthechessking.game.effects.MoveCamera;
import com.kmortyk.canekoandthechessking.game.effects.PopUpText;
import com.kmortyk.canekoandthechessking.game.object.GameObject;
import com.kmortyk.canekoandthechessking.util.ExtendableBitmap;
import com.kmortyk.canekoandthechessking.util.LayersManager;
import com.kmortyk.canekoandthechessking.gameinterface.GameInterface;
import com.kmortyk.canekoandthechessking.gameinterface.model.InterfaceElement;
import com.kmortyk.canekoandthechessking.resources.GameResources;
import com.kmortyk.canekoandthechessking.game.GameWorld;
import com.kmortyk.canekoandthechessking.game.graphics.Shaders;
import com.kmortyk.canekoandthechessking.util.Vector2;
import com.kmortyk.canekoandthechessking.game.tiles.Tile;

import java.util.ArrayList;

/**
 * Created by kmortyk on 05.11.2018.
 */

public class GameThread extends DrawThread {

    private static final boolean DEBUG_MODE = false;
    private ArrayList<Vector2> debugTouches;
    private Paint debugPaint;

    private Bitmap bckBitmap;

    private GameActivity gameActivity;
    private GameWorld gameWorld;
    private GameResources gameResources;
    private GameInterface gameInterface;
    private LayersManager layersManager;

    private int width, height;

    public GameThread(GameActivity gameActivity, String level) {

        this.gameActivity = gameActivity;
        this.context = gameActivity.getApplicationContext();

        debugPaint = new Paint();
        debugPaint.setStyle(Paint.Style.STROKE);
        debugPaint.setColor(Color.RED);

        if(DEBUG_MODE) {
            debugTouches = new ArrayList<>();
        }

        gameResources = new GameResources(context, this);
        gameWorld = new GameWorld(gameResources);
        gameWorld.create(level);

        layersManager = createLayers();

    }

    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {

        if(firstRun()) {
            width  = canvas.getWidth();
            height = canvas.getHeight();
            gameInterface = new GameInterface(this, width, height);

            // background
            Paint bckPaint = new Paint();
            bckPaint.setShader(Shaders.getBackgroundGradient(canvas.getWidth(), canvas.getHeight(), viewOffset));
            bckBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas bckCanvas = new Canvas(bckBitmap);
            bckCanvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), bckPaint);

            viewToHero();
        }

        canvas.drawBitmap(bckBitmap, 0, 0, null);

        layersManager.drawLayers(canvas, viewOffset);
        drawObjects(canvas);
        drawEffects(canvas);
        drawInterface(canvas);

        if(DEBUG_MODE) {
            drawDebug(canvas);
        }

        gameWorld.update(delta);

    }

    // draw all static objects at one bitmap
    private LayersManager createLayers() {

        ExtendableBitmap btm = new ExtendableBitmap();
        LayersManager ls = new LayersManager();
        GameMap gameMap = gameWorld.getGameMap();

        // ground
        for (int i = 0; i < gameMap.height; i++) {
            for (int j = 0; j < gameMap.width; j++) {
                Tile cur = gameMap.get(i, j);
                if(cur.hasBackTexture()) {
                    btm.addPart(cur.backTexture, cur.pos.x, cur.pos.y);
                }
                btm.addPart(cur.texture, cur.pos.x, cur.pos.y);
            }
        }
        ls.addLayer(btm.apply(), btm.getX(), btm.getY());
        btm.clear();

        // static
        for(GameObject o: gameWorld.getObjects()) { if(o.isStatic()) btm.addPart(o.texture, o.pos.x, o.pos.y); }
        if(!btm.isEmpty()){
            ls.addLayer(btm.apply(), btm.getX(), btm.getY());
            btm.clear();
        }

        return ls;
    }

    /* --- draw ---------------------------------------------------------------------------------- */

    private void drawObjects(Canvas canvas) {
        for (int i = 0; i < gameWorld.getObjects().size(); i++) { // safely
            GameObject o = gameWorld.getObjects().get(i);
            if(!o.isStatic()) canvas.drawBitmap(o.texture, o.pos.x + viewOffset.x, o.pos.y + viewOffset.y, null);
        }
    }

    private void drawEffects(Canvas canvas) {
        for(int i = 0; i < gameWorld.getEffects().size(); i++) {
            gameWorld.getEffects().get(i).draw(canvas, viewOffset);
        }
    }

    private void drawDebug(Canvas canvas) {
        for (int i = 0; i < debugTouches.size(); i++) {
            Vector2 touch = debugTouches.get(i);
            canvas.drawCircle(touch.x + viewOffset.x, touch.y + viewOffset.y, 2, debugPaint);
        }
    }

    private void drawInterface(Canvas canvas) {
        gameInterface.draw(canvas);
        if(DEBUG_MODE) {
            for (InterfaceElement e: gameInterface.getElements()) { canvas.drawRect(e.bounds, debugPaint); }
        }
    }

    /* --- event --------------------------------------------------------------------------------- */

    @Override
    public void onTouchDown(MotionEvent event) {
        if(gameInterface == null) return; // sometimes fall with NullPointerException FIXME bug
        gameInterface.onTouchDown(event.getX(), event.getY());
    }

    @Override
    public void onTouch(MotionEvent event) {

        if(gameInterface.onTouch(event.getX(), event.getY())) return;

        if(gameWorld.onTouch(event.getX() - viewOffset.x, event.getY() - viewOffset.y) != null) {
            gameInterface.increaseSteps();
        }

        if(DEBUG_MODE) debugTouches.add(new Vector2(event.getX() - viewOffset.x, event.getY() - viewOffset.y));

    }

    @Override
    public void onSwipe(float dx, float dy) {
        if(gameInterface == null || gameInterface.isActive()) return;
        viewOffset.add(dx, dy);
    }

    @Override
    public void onSwipeTop(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(e1.getY() > gameInterface.getPanelOpenTopY())
            gameInterface.openInventoryPanel();
    }

    @Override
    public void onSwipeBottom(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(e1.getY() > gameInterface.getPanelCloseTopY())
            gameInterface.closeInventoryPanel();
    }

    /* ------------------------------------------------------------------------------------------- */

    public void viewToHero() { gameWorld.addEffect(new MoveCamera(this, width, height, gameWorld.getHero())); }

    public GameWorld getGameWorld() {
        if(gameWorld == null) throw new RuntimeException("Try to get null game world.");
        return gameWorld;
    }

    public void nextMap() {
        if(gameWorld.isLastMap()) { returnToMap(); }
        else {
            gameWorld.nextMap();
            gameInterface.nextMap();
            viewToHero();
        }

        layersManager.clear();
        layersManager = createLayers();
    }

    public void returnToMap() { gameActivity.openWorldMap(); }

    /* --- get ----------------------------------------------------------------------------------- */

    public GameResources getResources() { return gameResources; }

    public int getOrientation() { return gameActivity.getResources().getConfiguration().orientation; }

    /* ------------------------------------------------------------------------------------------- */

    @Override
    public void dispose() {
        gameResources.recycle();
        layersManager.clear();
        System.gc();
    }

}
