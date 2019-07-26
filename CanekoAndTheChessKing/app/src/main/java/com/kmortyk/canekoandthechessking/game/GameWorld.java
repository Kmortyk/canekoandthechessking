package com.kmortyk.canekoandthechessking.game;

import android.support.annotation.NonNull;
import android.util.Log;

import com.kmortyk.canekoandthechessking.game.decoration.Decoration;
import com.kmortyk.canekoandthechessking.game.effects.PopUpText;
import com.kmortyk.canekoandthechessking.game.item.GameItem;
import com.kmortyk.canekoandthechessking.game.item.Inventory;
import com.kmortyk.canekoandthechessking.game.object.GameObject;
import com.kmortyk.canekoandthechessking.resources.GameResources;
import com.kmortyk.canekoandthechessking.resources.ParsedMap;
import com.kmortyk.canekoandthechessking.game.creatures.Creature;
import com.kmortyk.canekoandthechessking.game.creatures.Enemy;
import com.kmortyk.canekoandthechessking.game.creatures.Hero;
import com.kmortyk.canekoandthechessking.game.effects.Effect;
import com.kmortyk.canekoandthechessking.game.object.PathNode;
import com.kmortyk.canekoandthechessking.game.tiles.Tile;
import com.kmortyk.canekoandthechessking.resources.ResourceManager;
import com.kmortyk.canekoandthechessking.database.Scores;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GameWorld {

    private GameResources gameResources;
    private Inventory inventory;

    private List<Effect> effects;
    private List<Enemy> enemies;

    private List<GameObject> drawObjects;

    private GameMap gameMap;
    private Hero hero;

    private boolean isTurnBased = false;
    private boolean heroTurn = true;

    private int curEnemy;
    private int pastTime;
    private int steps;

    private ParsedMap parsedMap;
    private String curMapName;

    public GameWorld(GameResources gameResources) {
        this.gameResources = gameResources;
        gameMap = new GameMap(gameResources);
        effects = new ArrayList<>();
        enemies = new LinkedList<>();
        inventory = new Inventory();

        // debug purposes
        inventory.addItem(gameResources.getItem(500, 0, 0));
        inventory.addItem(gameResources.getItem(500, 0, 0));
        inventory.addItem(gameResources.getItem(501, 0, 0));

        drawObjects = new LinkedList<>();
    }

    public void create(String mapName) {

        if(mapName == null || !ResourceManager.getInstance().mapExists(mapName)) mapName = "err";
        curMapName = mapName;

        parsedMap = ResourceManager.getInstance().loadMapFromAssets(mapName);
        gameMap.setMap(parsedMap.map);

        if(hero == null) { hero = gameResources.getHero(parsedMap.heroI, parsedMap.heroJ); }
        else             { hero.moveTo(parsedMap.heroI, parsedMap.heroJ); }
        moveTo(hero, parsedMap.heroI, parsedMap.heroJ);

        for (String[] parts: parsedMap.items) {
            int i  = Integer.valueOf(parts[1]);
            int j  = Integer.valueOf(parts[2]);
            GameItem it = gameResources.getItem(Integer.valueOf(parts[0]), i, j);
            drawObjects.add(it);
            moveTo(it, i, j);
        }

        for (String[] parts: parsedMap.decorations) {
            int i  = Integer.valueOf(parts[1]);
            int j  = Integer.valueOf(parts[2]);
            Decoration d = gameResources.getDecoration(Integer.valueOf(parts[0]), i, j);
            drawObjects.add(d);
            moveTo(d, i, j);
        }


        for (String[] parts: parsedMap.enemies) {
            int i = Integer.valueOf(parts[1]);
            int j = Integer.valueOf(parts[2]);
            Enemy e = gameResources.getEnemy(this, parts[0], i, j);
            drawObjects.add(e);
            enemies.add(e);
            moveTo(e, i, j);
        }

        drawObjects.add(hero);

    }

    public void update(float delta) {

        pastTime += delta;

        hero.update(delta);
        for(Enemy e: enemies) e.update(delta);

        for (int i = 0; i < effects.size(); i++) {
            Effect e = effects.get(i);
            if(!e.extend(delta)) { e.dispose(); effects.remove(i); i++; }
        }

    }

    public void nextMap() {
        enemies.clear();
        effects.clear();
        drawObjects.clear();

        create(parsedMap.nextMap);
        Scores.getInstance().addEntry(pastTime, steps, curMapName);

        curEnemy = pastTime = steps = 0;
    }

    public boolean isLastMap() { return parsedMap == null || parsedMap.nextMap.equals(ParsedMap.GAME_WORLD); }

    public PathNode onTouch(float x, float y) {

        if(!hero.isArrived() || !isHeroTurn()) { return null; }

        // where stay hero
        Tile sameTile = hero.getSameTile();
        if(sameTile.contains(x, y) && sameTile.hasItem()) {
            GameItem it = sameTile.getItem();
            PopUpText.uniqueAddTo(this, hero.centerX(), hero.centerY(), it.getName());
            inventory.addItem(sameTile.getItem());
            drawObjects.remove(it);
            return null;
        }

        // decorations
        List<Tile> activeTiles = hero.getActiveTiles();
        for(Tile tile: activeTiles) {
            if(tile.contains(x, y) && tile.hasActiveDecoration()) {
                makeAction(tile.getDecoration());
                return null;
            }
        }

        PathNode touched = gameMap.touch(x, y);

        if(touched != null) {
            LinkedList<PathNode> path = gameMap.findPath(hero.node, touched);
            hero.addAllPathNodes(path);
            if(path.size() > 0) { return path.get(0); }
        }

        return null;

    }

    // FIXME empty method
    public void makeAction(Decoration decoration) { }

    private void moveTo(GameObject o, int i, int j) {
        if(isValid(i, j)) {
            get(i, j).setObject(o);
        }
    }

    private void moveTo(Creature creature, int i, int j) {
        if(isValid(i, j)) {
            get(i, j).setCreature(creature);
        }
    }

    public void move(Creature creature, PathNode from, PathNode to) {
        get(from.i, from.j).setEmpty();
        get(to.i, to.j).setCreature(creature);
    }

    public boolean isValid(int i, int j) { return i >= 0 && j >= 0 && i < gameMap.height && j < gameMap.width; }

    public boolean isEmpty(int i, int j) { return gameMap.get(i, j).isEmpty(); }

    public boolean hasHero(int i, int j) { return gameMap.get(i, j).getCreature() instanceof Hero; }

    @NonNull
    public Tile get(int i, int j) { return gameMap.get(i, j); }

    public GameMap getGameMap() { return gameMap; }

    public Hero getHero() { return hero; }

    public List<Effect> getEffects() { return effects; }

    public List<GameObject> getObjects() { return drawObjects; }

    public void addEffect(Effect e) { effects.add(e); }

    private boolean isTurnBased() { return isTurnBased; }

    public void setTurnBased(boolean isTurnBased) { this.isTurnBased = isTurnBased; }

    private boolean isHeroTurn() { return heroTurn || !isTurnBased(); }

    public void nextTurn() {
        heroTurn = !heroTurn;
        // FIXME non turn based
        if(isHeroTurn()) {
            if(enemies.size() > 0)
                curEnemy = (curEnemy + 1) % enemies.size();
        }else{
            steps++;
            if(curEnemy < enemies.size()) { enemies.get(curEnemy).nextStep(); }
            else heroTurn = true;
        }

    }

    @SuppressWarnings("unused")
    public void debugMap() {
        // -------------------------------------------------------------------
        StringBuilder map = new StringBuilder();
        map.append(" \n");
        for (int i = 0; i < gameMap.height; i++) {
            for (int j = 0; j < gameMap.width; j++) {

                Tile tile = gameMap.get(i, j);

                Creature creature = tile.getCreature();
                map.append(creature).append(" ");

            }
            map.append("\n");
        }

        Log.d("Map", map.toString());
        // -------------------------------------------------------------------
    }

    public Inventory getInventory() { return inventory; }

}
