package com.kmortyk.canekoandthechessking.game.creatures;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.kmortyk.canekoandthechessking.game.effects.PopUpText;
import com.kmortyk.canekoandthechessking.resources.GameResources;
import com.kmortyk.canekoandthechessking.game.GameWorld;
import com.kmortyk.canekoandthechessking.util.MovingPath;
import com.kmortyk.canekoandthechessking.util.Vector2;
import com.kmortyk.canekoandthechessking.game.object.GameObject;
import com.kmortyk.canekoandthechessking.game.object.PathNode;
import com.kmortyk.canekoandthechessking.game.tiles.Tile;
import com.kmortyk.canekoandthechessking.resources.ResourceManager;

import java.util.LinkedList;

public abstract class Creature extends GameObject {

    protected LinkedList<PathNode> traveledPath;
    protected LinkedList<PathNode> path;
    protected GameWorld gameWorld;

    public PathNode node;
    private boolean isArrived;
    private MovingPath movingPath;

    // private final static float movingSpeed = 150f * ResourceManager.getInstance().density();

    public Creature(GameWorld gameWorld, int i, int j, Bitmap texture) {
        super(texture);
        this.gameWorld = gameWorld;

        int prc = (int) ResourceManager.pxFromDp(3.3333f);
        movingPath = new MovingPath(prc);

        path = new LinkedList<>();
        traveledPath = new LinkedList<>();

        moveTo(i, j);
    }

    @Override
    public void moveTo(int i, int j) {

        float x = GameResources.getSectionWidth() * i;
        float y = GameResources.getSectionHeight() * j - GameResources.getStepHeight();

        this.pos = Vector2.toIsometric(new Vector2(x, y));

        // centering
        pos.x -= (getWidth() - GameResources.getStepWidth()) / 2;
        pos.y -= (getHeight() - GameResources.getStepHeight()) / 2 + getHeight()/2;

        node = new PathNode(centerX(), bottomY(), i, j);
        traveledPath.add(node);

        setArrived(true);

    }

    public final void update(float delta) {

        if(isArrived()) {
            if(path.size() > 0) {
                node = path.pollLast();
                setArrived(false);
            } else { return; }
        }

        updatePos();

    }

    private void updatePos() {

        if(!movingPath.hasPath()) { movingPath.createPath(pos.x, pos.y, node.x - getWidth()*0.5f, node.y - getHeight()); }
        else{

             if(movingPath.hasNext()) { pos.set(movingPath.next()); }
             else{ // no points anymore

                pos.set(movingPath.getLast());
                movingPath.clear();

                Tile tile = gameWorld.get(node.i, node.j);
                tile.onStep(this);

                if(lastNode() && tile.hasCreature()) { onAttack(tile.getCreature()); }
                onArrived();

            }

        }

    }

    public void addPathNode(PathNode node) { if(!this.node.equals(node)) path.add(node); }

    public final void addAllPathNodes(LinkedList<PathNode> nodes) { for (PathNode node: nodes) addPathNode(node); }

    private boolean lastNode() { return path.size() == 0; }

    protected void onAttack(Creature creature) {
        PopUpText.addTo(gameWorld, creature.pos.x, creature.pos.y, "Attack...");
    }

    protected void onArrived() {
        gameWorld.move(this, traveledPath.peekLast(), node);
        traveledPath.add(node);
        setArrived(true);
    }

    private void setArrived(boolean isArrived) { this.isArrived = isArrived; }

    public boolean isArrived() { return isArrived; }

    /**
     *  For a better looking debug, examples:
     *  Hero   -> Hero,
     *  Pawn   -> Pawn,
     *  Horse  -> Hors,
     *  Bishop -> Bish etc.
     */
    @Override @NonNull
    public String toString() { return getClass().getSimpleName().substring(0, 4); }

}
