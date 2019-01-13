package com.kmortyk.canekoandthechessking.game.creatures;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.kmortyk.canekoandthechessking.resources.GameResources;
import com.kmortyk.canekoandthechessking.game.GameWorld;
import com.kmortyk.canekoandthechessking.game.math.Vector2;
import com.kmortyk.canekoandthechessking.game.object.GameObject;
import com.kmortyk.canekoandthechessking.game.object.PathNode;
import com.kmortyk.canekoandthechessking.game.steps.Step;

import java.util.LinkedList;

public abstract class Creature extends GameObject {

    protected LinkedList<PathNode> traveledPath;
    protected LinkedList<PathNode> path;
    protected GameWorld gameWorld;

    public PathNode node;
    private boolean isArrived;

    private final static float movingSpeed = 0.16f;
    private final static float err = 3f;

    public Creature(GameWorld gameWorld, int i, int j, Bitmap texture) {
        super(texture);
        this.gameWorld = gameWorld;

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
        pos.y -= (getHeight() - GameResources.getStepHeight());

        node = new PathNode(centerX(), bottomY(), i, j);

        setArrived(true);

    }

    public final void update(float delta) {

        if(isArrived()) {
            if(path.size() > 0) {
                node = path.pollLast();
                setArrived(false);
            } else { return; }
        }

        updatePos(delta);

    }

    private void updatePos(float delta) {

        int speed = (int) (movingSpeed * delta);

        float cx = centerX();
        float cy = bottomY();

        if(cx < node.x) this.pos.x += speed * 2;
        if(cx > node.x) this.pos.x -= speed * 2;

        if(cy < node.y) this.pos.y += speed;
        if(cy > node.y) this.pos.y -= speed;

        // Log.d("Distance", Vector2.dst2(cx, cy, node.x, node.y) + "");

        if(Vector2.dst2(cx, cy, node.x, node.y) < err*err) {

            Step step = gameWorld.get(node.i, node.j);
            step.onStep(this);

            if(step.hasCreature()) { onAttack(step.getCreature()); }

            onArrived();

        }

    }

    public void addPathNode(PathNode node) { if(!this.node.equals(node)) path.add(node); }

    public final void addPathNode(int i, int j) { addPathNode(new PathNode(gameWorld.get(i, j), i, j)); }

    public final void addAllPathNodes(LinkedList<PathNode> nodes) { for (PathNode node: nodes) addPathNode(node); }

    protected void onAttack(Creature creature) { /* Override */ }

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
