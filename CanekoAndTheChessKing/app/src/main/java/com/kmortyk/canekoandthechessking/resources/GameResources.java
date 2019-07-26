package com.kmortyk.canekoandthechessking.resources;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;

import com.kmortyk.canekoandthechessking.database.DataAdapter;
import com.kmortyk.canekoandthechessking.game.GameWorld;
import com.kmortyk.canekoandthechessking.game.decoration.Decoration;
import com.kmortyk.canekoandthechessking.game.item.GameItem;
import com.kmortyk.canekoandthechessking.game.command.BreakCommand;
import com.kmortyk.canekoandthechessking.thread.GameThread;
import com.kmortyk.canekoandthechessking.R;
import com.kmortyk.canekoandthechessking.game.creatures.Bishop;
import com.kmortyk.canekoandthechessking.game.creatures.Enemy;
import com.kmortyk.canekoandthechessking.game.creatures.Hero;
import com.kmortyk.canekoandthechessking.game.creatures.Horse;
import com.kmortyk.canekoandthechessking.game.creatures.Pawn;
import com.kmortyk.canekoandthechessking.game.tiles.ExitTile;
import com.kmortyk.canekoandthechessking.game.tiles.Tile;

public class GameResources {

    private static final float scaleFactor     = 0.3f;
    private static final float heroScaleFactor = 0.2f;

    private static final Bitmap tileTexture = ResourceManager.getInstance().loadDrawable(R.drawable.tile, scaleFactor);

    private final GameThread gameThread;
    private final SparseArray<Bitmap> bitmaps = new SparseArray<>();

    private final Tile err = new Tile(0, getDrawable(R.drawable.err, scaleFactor));
    private final Decoration errDecoration = new Decoration(getDrawable(R.drawable.err, scaleFactor));
    private final GameItem errItem = new GameItem(0, getDrawable(R.drawable.err, scaleFactor));

    private DataAdapter dbHelper;

    public GameResources(Context context, GameThread gameThread) {
        this.gameThread = gameThread;

        dbHelper = new DataAdapter(context);
        dbHelper.createDatabase();
        dbHelper.open();
    }

    /* --- game metrics -------------------------------------------------------------------------- */

    public static int getSectionWidth() { return tileTexture.getWidth()/2; }

    public static int getSectionHeight() { return tileTexture.getHeight(); }

    public static int getStepHeight() { return tileTexture.getHeight(); }

    public static int getStepWidth() { return tileTexture.getWidth(); }

    /* --- get object ---------------------------------------------------------------------------- */

    public Hero getHero(int i, int j) { return new Hero(gameThread, i, j, getDrawable(R.drawable.caneko, heroScaleFactor)); }

    public Enemy getEnemy(GameWorld gameWorld, String name, int i, int j) {

        switch(name) {
            case "p": return new Pawn(gameWorld, i, j, getDrawable(R.drawable.pawn, heroScaleFactor));
            case "h": return new Horse(gameWorld, i, j, getDrawable(R.drawable.chum_ol, heroScaleFactor));
            case "b": return new Bishop(gameWorld, i, j, getDrawable(R.drawable.bishop2, heroScaleFactor));
        }

        return null;

    }

    public GameItem getItem(int id, int i, int j) {

        Cursor c = dbHelper.getItemData(id);

        if(c == null || !c.moveToFirst()) return errItem.clone(i, j);

        String type             = c.getString(c.getColumnIndex("type"));
        String drawableName     = c.getString(c.getColumnIndex("drawableName"));
        String fullDrawableName = c.getString(c.getColumnIndex("fullDrawableName"));
        String name = ResourceManager.getInstance().getString(drawableName);

        boolean stackable  = c.getInt(c.getColumnIndex("stackable"))  > 0;
        boolean usable     = c.getInt(c.getColumnIndex("usable"))     > 0;
        boolean centering  = c.getInt(c.getColumnIndex("centering"))  > 0;

        GameItem it = new GameItem(GameItem.getFlags(stackable, usable), getDrawable(drawableName, scaleFactor), i, j);
        it.fullTextureName = fullDrawableName;
        it.setName(name);
        it.setType(type);

        switch (type) {
            case "hoe": it.addCommand(new BreakCommand());
                break;
        }

        if(centering) { it.centerAtTile(); }

        return it;

    }

    public Decoration getDecoration(int id, int i, int j) {

        Cursor c = dbHelper.getDecorationData(id);

        if(c == null || !c.moveToFirst()) { return errDecoration.clone(i, j); }

        String type = c.getString(c.getColumnIndex("type"));
        String drawableName = c.getString(c.getColumnIndex("drawableName"));

        float height = c.getFloat(c.getColumnIndex("height"));
              height *= getStepHeight();

        boolean isStatic = c.getInt(c.getColumnIndex("static")) > 0;

        Decoration d = new Decoration(getDrawable(drawableName, scaleFactor), i, j);
        d.setStatic(isStatic);
        if(!type.equals("wall")) { d.centerAtTile();  }
        d.setHeight(height);

        return d;

    }

    @NonNull
    public Tile getTile(int id, int i, int j) {

        Cursor c = dbHelper.getTileData(id);

        if(c == null || !c.moveToFirst()) { return err.clone(i, j); }

        String type = c.getString(c.getColumnIndex("type"));
        String drawableName = c.getString(c.getColumnIndex("drawableName"));
        String back = c.getString(c.getColumnIndex("back"));

        if(drawableName.equals("empty")) {
            return new Tile(0, getDrawable(R.drawable.empty, 1), ResourceManager.emptyBitmap, i, j);
        }

        Bitmap backTexture = ResourceManager.emptyBitmap;
        if(back != null) { backTexture = getDrawable(back, scaleFactor); }

        boolean stepable  = c.getInt(c.getColumnIndex("stepable"))  > 0;
        boolean touchable = c.getInt(c.getColumnIndex("touchable")) > 0;

        Bitmap texture = getDrawable(drawableName, scaleFactor);

        if(texture == null) {
            Log.d("GameResources", "no such drawableName.");
            return err.clone(i, j);
        }

        c.close();

        switch (type) {
            case "tile": return new Tile(Tile.getFlags(stepable, touchable), texture, backTexture, i, j);
            case "exit": return new ExitTile(gameThread, Tile.getFlags(stepable, touchable), texture, backTexture, i, j);
            default:
                Log.d("GameResources", "no such tile type.");
                return err.clone(i, j);
        }

    }

    /* --- get resource -------------------------------------------------------------------------- */

    public Bitmap getDrawable(String drawableName, float scaleFactor) {
        int id = ResourceManager.getInstance().getResourceId(drawableName, "drawable");
        if(id == 0) {
            Log.e("GameResources", "#getDrawable id is 0.");
            return ResourceManager.emptyBitmap;
        }
        return getDrawable(id, scaleFactor);
    }

    public Bitmap getDrawable(int id, float scaleFactor) {
        Bitmap value = bitmaps.get(id);
        if(value != null && !value.isRecycled()) { return value; }
        Bitmap btm = ResourceManager.getInstance().loadDrawable(id, scaleFactor);
        bitmaps.put(id, btm);
        return btm;
    }

    /* --- recycle ------------------------------------------------------------------------------- */

    public void recycle() {
        for(int i = 0; i < bitmaps.size(); i++) {
            int key = bitmaps.keyAt(i);
            Bitmap btm = bitmaps.get(key);
            if(!btm.isRecycled()) {
                btm.recycle();
                bitmaps.put(key, null);
            }
        }
        bitmaps.clear();
        dbHelper.close();
    }

}
