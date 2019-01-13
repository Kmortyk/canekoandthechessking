package com.kmortyk.canekoandthechessking.resources;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.SparseArray;

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

    private static final float decorationScaleFactor = 0.3f;
    private static final float heroScaleFactor = 0.2f;
    private static final float scaleFactor = 0.1f;

    private GameThread gameThread;

    private SparseArray<Bitmap> bitmaps = new SparseArray<>();
    private static final String[] maps = {"map1", "map2"};
    private static int curMapIndex = 0;

    private static final Bitmap stepTexture = ResourceManager.getInstance().loadDrawable(R.drawable.step, scaleFactor);

    // 0 - main, steps
    private Step emptyStep = new Step(Step.getFlags(false, false), getDrawable(R.drawable.empty, 1/ResourceManager.getInstance().density()));
    private Step step  = new Step(Step.getFlags(true, true),  getDrawable(R.drawable.step, scaleFactor));
    private Step pathH = new Step(Step.getFlags(true, false), getDrawable(R.drawable.path_h, scaleFactor));
    private Step pathV = new Step(Step.getFlags(true, false), getDrawable(R.drawable.path_v, scaleFactor));
    private Step pathExit;

    // 200 - decorations
    private Step plant      = new Step(0, getDrawable(R.drawable.plant, decorationScaleFactor));
    private Step chessboard = new Step(0, getDrawable(R.drawable.chessboard2, decorationScaleFactor));
    private Step jug = new Step(0, getDrawable(R.drawable.jug, decorationScaleFactor));
    private Step table = new Step(0, getDrawable(R.drawable.table, decorationScaleFactor));

    private Step err = new Step(0, getDrawable(R.drawable.err, decorationScaleFactor));

    public GameResources(GameThread gameThread) {
        this.gameThread = gameThread;
        pathExit = new ExitStep(gameThread, Step.getFlags(true, true), getDrawable(R.drawable.exit, scaleFactor));
    }

    /* --- metrics ------------------------------------------------------------------------------- */

    public static int getSectionWidth() { return stepTexture.getWidth() * 2; }

    public static int getSectionHeight() { return stepTexture.getHeight() * 4; }

    public static int getStepHeight() { return stepTexture.getHeight(); }

    public static int getStepWidth() { return stepTexture.getWidth(); }

    /* --- get object ---------------------------------------------------------------------------- */

    public Hero getHero(GameWorld gameWorld, int i, int j) {
        return new Hero(gameThread, i, j, getDrawable(R.drawable.caneko, heroScaleFactor));
    }

    public Enemy getEnemy(GameWorld gameWorld, String name, int i, int j) {

        switch(name) {
            case "p": return new Pawn(gameWorld, i, j, getDrawable(R.drawable.pawn, heroScaleFactor));
            case "h": return new Horse(gameWorld, i, j, getDrawable(R.drawable.chum_ol, heroScaleFactor));
            case "b": return new Bishop(gameWorld, i, j, getDrawable(R.drawable.bishop2, heroScaleFactor));
        }

        return null;

    }

    @NonNull
    public Step getStep(int id, int i, int j) {

        switch (id) {

            case 0: return emptyStep;
            // 0 - main, steps
            case 1: return step.clone(i, j);
            case 2: return pathH.clone(i, j);
            case 3: return pathV.clone(i, j);
            case 4: return pathExit.clone(i, j);
            // 200 - decorations
            case 204: return plant.clone(i, j, 0.5f, 0.3f);
            case 205: return chessboard.clone(i, j, 0.25f, 0.0625f);
            case 206: return jug.clone(i, j, 0.5f, 0.25f);
            case 207: return table.clone(i, j, 0.25f, 0.0625f);

            default: return err.clone(i, j);

        }

    }

    /* --- map ----------------------------------------------------------------------------------- */

    public String currentMap() { return maps[curMapIndex]; }

    public String nextMap() {
        curMapIndex = (curMapIndex + 1) % maps.length;
        return currentMap();
    }

    public boolean isLastMap() { return  curMapIndex == maps.length - 1; }

    /* --- get resource -------------------------------------------------------------------------- */

    public Bitmap getDrawable(int id, float scaleFactor) {
        Bitmap value = bitmaps.get(id);
        if(value != null) { return value; }
        Bitmap btm = ResourceManager.getInstance().loadDrawable(id, scaleFactor);
        bitmaps.put(id, btm);
        return btm;
    }

    public static String getHeroName() { return ResourceManager.getInstance().getString(R.string.hero_name); }

}
