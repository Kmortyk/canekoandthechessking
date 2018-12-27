package com.kmortyk.canekoandthechessking.game;

import android.support.annotation.NonNull;
import android.util.Log;

import com.kmortyk.canekoandthechessking.game.object.PathNode;
import com.kmortyk.canekoandthechessking.game.steps.Step;
import com.kmortyk.canekoandthechessking.resources.GameResources;

import java.util.LinkedList;

/**
 * Created by user1 on 05.11.2018.
 */

public class GameMap {

    private GameResources gameResources;
    private Step[][] map;

    public int width, height;

    public GameMap(GameResources gameResources, int[][] map) {
        this.gameResources = gameResources;
        height = map.length;
        width = map[0].length;
        this.map = new Step[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                this.map[i][j] = gameResources.getStep(map[i][j], i, j);
            }
        }

    }

    public void reload(int[][] map) {
        height = map.length;
        width = map[0].length;
        this.map = new Step[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                this.map[i][j] = gameResources.getStep(map[i][j], i, j);
            }
        }
    }

    public Step get(int i, int j) {
        return map[i][j];
    }

    /**
     * @return touched tile if it can be touched
     */
    public PathNode touch(float x, float y) {

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                if(map[i][j] != null &&
                   map[i][j].contains(x, y) &&
                   map[i][j].isTouchable()) {

                    return new PathNode(map[i][j], i, j);

                }

            }
        }

        return null;

    }

    /**
     * Uses A Star algorithm.
     */
    @NonNull
    public LinkedList<PathNode> findPath(PathNode start, PathNode end) {

        start.clearWeights();
        end.clearWeights();

        LinkedList<PathNode> closed = new LinkedList<>();
        LinkedList<PathNode> open = new LinkedList<>();

        start.g = 0;
        start.h = PathNode.distance(start, end);
        start.f = start.h;

        open.add(start);

        while (open.size() > 0) {
            PathNode current = null;

            // вершина из open, имеющая самую низкую оценку
            for (PathNode node : open) { if(current == null || node.f < current.f) current = node; }

            // конец, нашли путь от начала до конца
            if(current != null && current.equals(end)) return reconstructPath(start, current);

            open.remove(current);
            closed.add(current);

            for(PathNode neighbor: neighbors(current)) {

                if(closed.contains(neighbor)) continue;

                // idea warning
                assert current != null;

                int next_g = current.g + neighbor.cost;
                boolean tentativeIsBetter;

                if(!open.contains(neighbor)) {
                    open.add(neighbor);
                    tentativeIsBetter = true;
                }else{
                    tentativeIsBetter = next_g < neighbor.g;
                }

                if(tentativeIsBetter) {
                    neighbor.parent = current;
                    neighbor.g = next_g;
                    neighbor.h = PathNode.distance(neighbor, end);
                    neighbor.f = neighbor.g + neighbor.h;
                }
            }
        }

        return new LinkedList<>();
    }

    private LinkedList<PathNode> reconstructPath(PathNode start, PathNode end) {
        LinkedList<PathNode> result = new LinkedList<>();
        // reconstruct path from end to start
        PathNode current = end;
        while(current.parent != null) { // do not include start
            if(map[current.i][current.j].isTouchable())
                result.add(current);
            current = current.parent;
        }

        return result;
    }

    @NonNull //each neighbor also non null
    private LinkedList<PathNode> neighbors(PathNode n) {
        LinkedList<PathNode> neighbors = new LinkedList<>();

        if(n == null) {
            Log.d("findPath", "attempt to find neighbors for null. return.");
            return neighbors;
        }

        for (int k = -1; k <= 1; k++) {
            for (int l = -1; l <= 1; l++) {

                if( (k == 0) == (l == 0) ) continue;

                /*
                * Look only 4 cells around,
                * not in k=0 l=0 also
                * or k or l not equals 0
                *
                *  -1,-1  |0,-1|  1,-1
                * |-1, 0|  0, 0  |1, 0|
                *  -1, 1  |0, 1|  1, 1
                */

                int next_i = n.i + k;
                int next_j = n.j + l;

                if(next_i < 0 || next_j < 0 || next_i >= map.length || next_j >= map[0].length) continue;

                Step neighbor = map[next_i][next_j];

                if(neighbor != null && neighbor.isStepable())
                    neighbors.add(new PathNode(neighbor.centerX(), neighbor.centerY(), next_i, next_j));
            }
        }

        return neighbors;

    }

}
