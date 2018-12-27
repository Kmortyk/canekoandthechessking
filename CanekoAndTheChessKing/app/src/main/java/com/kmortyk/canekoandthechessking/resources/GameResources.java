package com.kmortyk.canekoandthechessking.resources;

import android.graphics.Bitmap;

import com.kmortyk.canekoandthechessking.game.GameWorld;
import com.kmortyk.canekoandthechessking.thread.GameThread;
import com.kmortyk.canekoandthechessking.R;
import com.kmortyk.canekoandthechessking.game.creatures.Bishop;
import com.kmortyk.canekoandthechessking.game.creatures.Enemy;
import com.kmortyk.canekoandthechessking.game.creatures.Hero;
import com.kmortyk.canekoandthechessking.game.creatures.Horse;
import com.kmortyk.canekoandthechessking.game.creatures.Pawn;
import com.kmortyk.canekoandthechessking.game.steps.ExitStep;
import com.kmortyk.canekoandthechessking.game.steps.Step;

public class GameResources {

    private GameThread gameThread;

    private static final float scaleFactor = 0.1f;
    private static final float heroScaleFactor = 0.2f;
    private static final float decorationScaleFactor = 0.3f;

    private static final String[] maps = {"map1", "map2"};
    private static int curMapIndex = 0;

    private static final Bitmap stepTexture = ResourceManager.getInstance().loadDrawable(R.drawable.step, scaleFactor);

    public static int getSectionWidth() { return stepTexture.getWidth() * 2; }

    public static int getSectionHeight() { return stepTexture.getHeight() * 4; }

    public static int getStepHeight() { return stepTexture.getHeight(); }

    public static int getStepWidth() { return stepTexture.getWidth(); }

    private static final Step err = new Step(0, ResourceManager.getInstance().loadDrawable(R.drawable.err,   decorationScaleFactor));

    // 0 - main, steps
    private static final Step step  = new Step(Step.getFlags(true, true),  ResourceManager.getInstance().loadDrawable(R.drawable.step, scaleFactor));
    private static final Step pathH = new Step(Step.getFlags(true, false), ResourceManager.getInstance().loadDrawable(R.drawable.path_h, scaleFactor));
    private static final Step pathV = new Step(Step.getFlags(true, false), ResourceManager.getInstance().loadDrawable(R.drawable.path_v, scaleFactor));
    private static Step pathExit;

    // 200 - decorations
    private static final Step plant = new Step(0, ResourceManager.getInstance().loadDrawable(R.drawable.plant, decorationScaleFactor));

    public GameResources(GameThread gameThread) {
        this.gameThread = gameThread;
        pathExit = new ExitStep(gameThread, Step.getFlags(true, true), ResourceManager.getInstance().loadDrawable(R.drawable.exit, scaleFactor));
    }

    public Hero getHero(GameWorld gameWorld, int i, int j) {
        return new Hero(gameWorld, i, j, ResourceManager.getInstance().loadDrawable(R.drawable.caneko, heroScaleFactor));
    }

    public Enemy getEnemy(GameWorld gameWorld, String name, int i, int j) {

        switch(name) {
            case "p":
                return new Pawn(gameWorld, i, j, ResourceManager.getInstance().loadDrawable(R.drawable.pawn, heroScaleFactor));
            case "h":
                return new Horse(gameWorld, i, j, ResourceManager.getInstance().loadDrawable(R.drawable.chum_ol, heroScaleFactor));
            case "b":
                return new Bishop(gameWorld, i, j, ResourceManager.getInstance().loadDrawable(R.drawable.bishop2, heroScaleFactor));
        }

        return null;

    }

    public Step getStep(int id, int i, int j) {

        switch (id) {

            case 0: return null; // no object to 0
            // 0 - main, steps
            case 1: return step.clone(i, j);
            case 2: return pathH.clone(i, j);
            case 3: return pathV.clone(i, j);
            case 4: return pathExit.clone(i, j);
            // 200 - decorations
            case 204: return plant.clone(i, j);

            default: return err.clone(i, j);

        }

    }

    public String currentMap() { return maps[curMapIndex]; }

    public void nextMap() { curMapIndex = (curMapIndex + 1) % maps.length; }

    public boolean isLastMap() { return  curMapIndex == maps.length - 1; }

    public static String getHeroName() { return ResourceManager.getInstance().getString(R.string.hero_name); }

}
