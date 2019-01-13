package com.kmortyk.canekoandthechessking.game;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.kmortyk.canekoandthechessking.game.item.Inventory;
import com.kmortyk.canekoandthechessking.resources.GameResources;
import com.kmortyk.canekoandthechessking.resources.ParsedMap;
import com.kmortyk.canekoandthechessking.game.creatures.Creature;
import com.kmortyk.canekoandthechessking.game.creatures.Enemy;
import com.kmortyk.canekoandthechessking.game.creatures.Hero;
import com.kmortyk.canekoandthechessking.game.effects.Effect;
import com.kmortyk.canekoandthechessking.game.effects.PopUpText;
import com.kmortyk.canekoandthechessking.game.object.PathNode;
import com.kmortyk.canekoandthechessking.game.steps.Step;
import com.kmortyk.canekoandthechessking.resources.ResourceManager;
import com.kmortyk.canekoandthechessking.scores.database.Scores;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class GameWorld {

    private GameResources gameResources;
    private Inventory inventory;

    private LinkedList<Effect> effects;
    private ArrayList<Enemy> enemies;
    private GameMap gameMap;
    private Hero hero;

    private boolean isTurnBased = true;
    private boolean heroTurn = true;

    private int spaces;
    private int curEnemy;
    private int pastTime;
    private int steps;

    public GameWorld(GameResources gameResources) {
        this.gameResources = gameResources;
        gameMap = new GameMap(gameResources);
        effects = new LinkedList<>();
        enemies = new ArrayList<>();
        inventory = new Inventory();
    }

    public void create(String mapName) {

        if(mapName == null) mapName = gameResources.currentMap();

        ParsedMap parsedMap = ResourceManager.getInstance().loadMapFromAssets(mapName);

        gameMap.setMap(parsedMap.map);

        if(hero == null) { hero = gameResources.getHero(this, parsedMap.heroI, parsedMap.heroJ); }
        else             { hero.moveTo(parsedMap.heroI, parsedMap.heroJ); }

        for (String[] parts: parsedMap.enemies) {
            String name =           parts[0];
            int i = Integer.valueOf(parts[1]);
            int j = Integer.valueOf(parts[2]);
            enemies.add(gameResources.getEnemy(this, name, i, j));
        }

        spaces = parsedMap.spaces;

    }

    public void update(float delta) {

        pastTime += delta;

        hero.update(delta);
        for(Enemy e: enemies) e.update(delta);

        Iterator<Effect> it = effects.iterator();
        while (it.hasNext()) {
            Effect e = it.next();
            if (!e.extend(delta)) { e.dispose(); it.remove(); }
        }

    }

    public void nextMap() {
        enemies.clear();
        effects.clear();

        create(gameResources.nextMap());
        Scores.getInstance().addEntry(pastTime, steps, gameResources.currentMap());

        curEnemy = pastTime = steps = 0;
    }

    public PathNode onTouch(float x, float y) {

        if(!hero.isArrived() || !isHeroTurn()) { return null; }

        PathNode touched = gameMap.touch(x, y);

        if(touched != null) {
            LinkedList<PathNode> path = gameMap.findPath(hero.node, touched);
            hero.addAllPathNodes(path);
            if(path.size() > 0) return path.get(0);
        }else{
            if(hero.contains(x, y)) PopUpText.uniqueAddTo(this, hero.centerX(), hero.centerY(), GameResources.getHeroName());
        }

        return null;

    }

    public void move(Creature creature, @Nullable PathNode from, PathNode to) {
        if(from != null) { get(from.i, from.j).setEmpty(); }
        get(to.i, to.j).setCurrentObject(creature);
    }

    public boolean isValid(int i, int j) { return i >= 0 && j >= 0 && i < gameMap.height && j < gameMap.width; }

    public boolean isEmpty(int i, int j) { return gameMap.get(i, j).isEmpty(); }

    public boolean hasHero(int i, int j) { return gameMap.get(i, j).getCreature() instanceof Hero; }

    @NonNull
    public Step get(int i, int j) { return gameMap.get(i, j); }

    public GameMap getGameMap() {
        return gameMap;
    }

    public Hero getHero() { return hero; }

    public ArrayList<Enemy> getEnemies() { return enemies; }

    public LinkedList<Effect> getEffects() { return effects; }

    public void addEffect(Effect e) { effects.add(e); }

    private boolean isTurnBased() { return isTurnBased; }

    public void setTurnBased(boolean isTurnBased) { this.isTurnBased = isTurnBased; }

    public int getSpaces() { return spaces; }

    private boolean isHeroTurn() { return heroTurn || !isTurnBased(); }

    public void nextTurn() {
        heroTurn = !heroTurn;

        if(heroTurn) {
            curEnemy = (curEnemy + 1) % enemies.size();
        }else{
            steps++;
            enemies.get(curEnemy).nextStep();
        }

    }

    public void debugMap() {
        // -------------------------------------------------------------------
        StringBuilder map = new StringBuilder();
        map.append(" \n");
        for (int i = 0; i < gameMap.height; i++) {
            for (int j = 0; j < gameMap.width; j++) {

                Step step = gameMap.get(i, j);

                Creature creature = step.getCreature();
                map.append(creature).append(" ");

            }
            map.append("\n");
        }

        Log.d("Map", map.toString());
        // -------------------------------------------------------------------
    }

    public void dispose() {
        for(Effect e: effects) e.dispose();
        for(Enemy e: enemies) e.texture.recycle();
        gameMap.dispose();
        hero.texture.recycle();
    }

}
