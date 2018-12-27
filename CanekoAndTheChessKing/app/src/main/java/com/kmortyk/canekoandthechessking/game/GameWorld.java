package com.kmortyk.canekoandthechessking.game;

import android.support.annotation.Nullable;
import android.util.Log;

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
import java.util.LinkedList;

public class GameWorld {

    private GameResources gameResources;
    private GameMap gameMap;
    private ArrayList<Effect> effects = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private Hero hero;

    private boolean isTurnBased = true;
    private boolean hasSpaces = true;
    private boolean heroTurn = true;
    private int curEnemy = 0;

    private int pastTime = 0;
    private int steps = 0;

    public GameWorld(GameResources gameResources) { this.gameResources = gameResources; }

    public void update(float delta) {

        pastTime += delta;

        hero.update(delta);

        if(isTurnBased()) {

            if(isHeroTurn()) { hero.update(delta); }
            else { enemies.get(curEnemy).update(delta); }

        }else{

            hero.update(delta);
            for(Enemy e: enemies) e.update(delta);

        }

        for(int i = 0; i < effects.size(); i++) {
            if(!effects.get(i).extend(delta)) {
                effects.get(i).dispose();
                effects.remove(i); // ugh...
                i = 0;
            }
        }

    }

    public void createWorld(String level) {

        ParsedMap parsedMap;

        if(level != null) { parsedMap = ResourceManager.getInstance().loadMapFromAssets(level); }
        else              { parsedMap = ResourceManager.getInstance().loadMapFromAssets(gameResources.currentMap()); }

        gameMap = new GameMap(gameResources, parsedMap.map);
        loadEnemies(parsedMap.enemies);
        hero = gameResources.getHero(this,4, 0);

    }

    private void loadEnemies(String[][] estr) {
        for (String[] parts: estr) {
            enemies.add(gameResources.getEnemy(this, parts[0], Integer.valueOf(parts[1]), Integer.valueOf(parts[2])));
        }
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public void nextMap() {

        enemies.clear();
        effects.clear();
        curEnemy = 0;

        gameResources.nextMap();

        ParsedMap parsedMap = ResourceManager.getInstance().loadMapFromAssets(gameResources.currentMap());

        gameMap.reload(parsedMap.map);
        loadEnemies(parsedMap.enemies);

        hero.moveTo(4, 0);

        Scores.getInstance().addEntry(pastTime, steps, gameResources.currentMap());
        pastTime = 0;
        steps = 0;

    }

    public void onTouch(float x, float y) {

        if(!hero.isArrived() || !isHeroTurn()) { return; }

        PathNode touched = gameMap.touch(x, y);

        if(touched != null) {
            LinkedList<PathNode> path = gameMap.findPath(hero.node, touched);
            hero.addAllPathNodes(path);
        }else{
            if(hero.contains(x, y)) PopUpText.uniqueAddTo(this, hero.centerX(), hero.centerY(), GameResources.getHeroName());
        }

    }

    public void move(Creature creature, @Nullable PathNode from, PathNode to) {
        if(from != null) get(from.i, from.j).unsetCreature();
        get(to.i, to.j).setCreature(creature);
    }

    /**
     * Will be removed in future releases
     * @deprecated
     */
    public void move(Creature creature, int fromI, int fromJ, int toI, int toJ) {
        get(fromI, fromJ).unsetCreature();
        get(toI, toJ).setCreature(creature);
    }

    public boolean isValid(int i, int j) { return i >= 0 && j >= 0 && i < gameMap.height && j < gameMap.width && get(i, j) != null; }

    public boolean isEmpty(int i, int j) { return gameMap.get(i, j).isEmpty(); }

    public boolean hasHero(int i, int j) { return gameMap.get(i, j).getCreature() instanceof Hero; }

    @Nullable
    public Step get(int i, int j) { return gameMap.get(i, j); }

    public Hero getHero() { return hero; }

    public ArrayList<Enemy> getEnemies() { return enemies; }

    public ArrayList<Effect> getEffects() { return effects; }

    public void addEffect(Effect e) { effects.add(e); }

    public boolean isTurnBased() { return isTurnBased; }

    public boolean hasSpaces() { return hasSpaces; }

    public boolean isHeroTurn() { return heroTurn || !isTurnBased(); }

    public void nextTurn() {
        if(heroTurn) steps++;
        heroTurn = !heroTurn;
    }

    public void nextEnemy() { curEnemy = (curEnemy + 1) % enemies.size(); }

    /**
     * @deprecated
     */
    public void debugMap() {
        // -------------------------------------------------------------------
        StringBuilder map = new StringBuilder();
        map.append(" \n");
        for (int i = 0; i < gameMap.height; i++) {
            for (int j = 0; j < gameMap.width; j++) {

                Step step = gameMap.get(i, j);

                if(step != null) {
                    Creature creature = step.getCreature();
                    map.append(creature).append(" ");
                }

                else { map.append("     "); }

            }
            map.append("\n");
        }

        Log.d("Map", map.toString());
        // -------------------------------------------------------------------
    }

}
