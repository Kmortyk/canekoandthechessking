package com.kmortyk.canekoandthechessking.thread;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.kmortyk.canekoandthechessking.GameActivity;
import com.kmortyk.canekoandthechessking.game.GameMap;
import com.kmortyk.canekoandthechessking.game.effects.MoveCamera;
import com.kmortyk.canekoandthechessking.game.gameinterface.GameInterface;
import com.kmortyk.canekoandthechessking.game.gameinterface.model.InterfaceElement;
import com.kmortyk.canekoandthechessking.resources.GameResources;
import com.kmortyk.canekoandthechessking.game.GameWorld;
import com.kmortyk.canekoandthechessking.game.creatures.Enemy;
import com.kmortyk.canekoandthechessking.game.graphics.Shaders;
import com.kmortyk.canekoandthechessking.game.math.Vector2;
import com.kmortyk.canekoandthechessking.game.creatures.Hero;
import com.kmortyk.canekoandthechessking.game.steps.Step;

import java.util.ArrayList;

/**
 * Created by kmortyk on 05.11.2018.
 */

public class GameThread extends DrawThread {

    private static final boolean DEBUG_MODE = false;
    private ArrayList<Vector2> debugTouches;
    private Paint debugPaint;

    private Paint bckPaint;
    private GameActivity gameActivity;
    private GameWorld gameWorld;
    private GameResources gameResources;
    private GameInterface gameInterface;

    private int width, height;

    public GameThread(GameActivity gameActivity, String level) {

        this.gameActivity = gameActivity;

        bckPaint = new Paint();

        if(DEBUG_MODE) {
            debugTouches = new ArrayList<>();
            debugPaint = new Paint();
            debugPaint.setColor(Color.RED);
            debugPaint.setStyle(Paint.Style.STROKE);
        }

        gameResources = new GameResources(this);
        gameWorld = new GameWorld(gameResources);
        gameWorld.create(level);

    }

    @Override
    public void run() {

        while(isRun()) {

            calculateDelta();

            Canvas canvas = surfaceHolder.lockCanvas();
            if(canvas != null) {

                if(firstRun()) {
                    width = canvas.getWidth();
                    height = canvas.getHeight();
                    gameInterface = new GameInterface(this, width, height);
                    viewToHero();
                }

                bckPaint.setShader(Shaders.getBackgroundGradient(canvas.getWidth(), canvas.getHeight(), viewOffset));
                canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), bckPaint);

                drawMap(canvas);
                drawHero(canvas);
                drawEnemies(canvas);
                drawEffects(canvas);
                drawInterface(canvas);

                if(DEBUG_MODE) drawDebug(canvas);

                gameWorld.update(delta);
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }

    }

    private void drawHero(Canvas canvas) {
        Hero hero = gameWorld.getHero();
        canvas.drawBitmap(hero.texture, hero.pos.x + viewOffset.x, hero.pos.y + viewOffset.y, null);
        if(DEBUG_MODE) canvas.drawRect(hero.getDebugRect(viewOffset), debugPaint);
    }

    private void drawEnemies(Canvas canvas) {
        for (Enemy e: gameWorld.getEnemies()) {
            canvas.drawBitmap(e.texture, e.pos.x + viewOffset.x, e.pos.y + viewOffset.y, null);
            if(DEBUG_MODE) canvas.drawRect(e.getDebugRect(viewOffset), debugPaint);
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

    private void drawMap(Canvas canvas) {

        GameMap gameMap = gameWorld.getGameMap();

        for (int i = 0; i < gameMap.height; i++) {
            for (int j = 0; j < gameMap.width; j++) {

                Step cur = gameMap.get(i, j);
                canvas.drawBitmap(cur.texture, cur.pos.x + viewOffset.x, cur.pos.y + viewOffset.y, null);

                if(DEBUG_MODE) canvas.drawRect(cur.getDebugRect(viewOffset), debugPaint);

            }
        }

    }

    private void drawInterface(Canvas canvas) {
        gameInterface.draw(canvas);
        if(DEBUG_MODE) {
            for (InterfaceElement e: gameInterface.getElements()) {
                canvas.drawRect(e.bounds, debugPaint);
            }
        }
    }

    @Override
    public void onTouchDown(MotionEvent event) { gameInterface.onTouchDown(event.getX(), event.getY()); }

    @Override
    public void onTouch(MotionEvent event) {

        if(gameInterface.onTouch(event.getX(), event.getY())) return;

        if(gameWorld.onTouch(event.getX() - viewOffset.x, event.getY() - viewOffset.y) != null) {
            gameInterface.increaseSteps();
        }

        if(DEBUG_MODE) debugTouches.add(new Vector2(event.getX() - viewOffset.x, event.getY() - viewOffset.y));

    }

    @Override
    public void onSwipe(float dx, float dy) { viewOffset.add(dx, dy); }

    public void viewToHero() { gameWorld.addEffect(new MoveCamera(this, width, height, gameWorld.getHero())); }

    public GameWorld getGameWorld() {
        if(gameWorld == null) throw new RuntimeException("Try to get null game world.");
        return gameWorld;
    }

    public void nextMap() {

        if(gameResources.isLastMap()) { returnToMap(); }
        else {
            gameWorld.nextMap();
            gameInterface.nextMap();
            viewToHero();
        }

    }

    @Override
    public void dispose() {
        super.dispose();
        gameWorld.dispose();
    }

    public GameResources getResources() { return gameResources; }

    public void returnToMap() { gameActivity.openWorldMap(); }

    public int getOrientation() { return gameActivity.getResources().getConfiguration().orientation; }

}
