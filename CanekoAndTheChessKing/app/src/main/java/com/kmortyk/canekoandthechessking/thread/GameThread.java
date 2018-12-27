package com.kmortyk.canekoandthechessking.thread;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.kmortyk.canekoandthechessking.GameActivity;
import com.kmortyk.canekoandthechessking.game.GameMap;
import com.kmortyk.canekoandthechessking.resources.GameResources;
import com.kmortyk.canekoandthechessking.game.GameWorld;
import com.kmortyk.canekoandthechessking.game.creatures.Enemy;
import com.kmortyk.canekoandthechessking.game.graphics.Shaders;
import com.kmortyk.canekoandthechessking.game.math.Vector2;
import com.kmortyk.canekoandthechessking.game.creatures.Hero;
import com.kmortyk.canekoandthechessking.game.steps.Step;
import com.kmortyk.canekoandthechessking.resources.ResourceManager;

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
        gameWorld.createWorld(level);

    }

    @Override
    public void run() {

        while(isRun) {

            calculateDelta();

            Canvas canvas = surfaceHolder.lockCanvas();
            if(canvas != null) {

                if(firstRun()) { viewToHero(canvas); }

                bckPaint.setShader(Shaders.getBackgroundGradient(canvas.getWidth(), canvas.getHeight(), viewOffset));
                canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), bckPaint);

                drawMap(canvas);
                drawHero(canvas);
                drawEnemies(canvas);
                drawEffects(canvas);

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
                if(cur == null) continue;

                canvas.drawBitmap(cur.texture, cur.pos.x + viewOffset.x, cur.pos.y + viewOffset.y, null);
                if(DEBUG_MODE) canvas.drawRect(cur.getDebugRect(viewOffset), debugPaint);

            }
        }

    }

    @Override
    public void onTouch(MotionEvent event) {

        gameWorld.onTouch(event.getX() - viewOffset.x, event.getY() - viewOffset.y);

        if(DEBUG_MODE) debugTouches.add(new Vector2(event.getX() - viewOffset.x, event.getY() - viewOffset.y));

    }

    @Override
    public void onSwipe(float dx, float dy) { viewOffset.add(dx, dy); }

    private void viewToHero(Canvas canvas) {
        Hero hero = gameWorld.getHero();
        viewOffset.x = canvas.getWidth() / 2 - hero.centerX();
        viewOffset.y = canvas.getHeight() / 2 - hero.centerY();
    }

    public GameWorld getGameWorld() {
        if(gameWorld == null) throw new RuntimeException("Try to get null game world.");
        return gameWorld;
    }

    public void nextMap() {
        if(gameResources.isLastMap()) {
            gameActivity.openWorldMap();
        }else {
            gameWorld.nextMap();
        }
    }

}
