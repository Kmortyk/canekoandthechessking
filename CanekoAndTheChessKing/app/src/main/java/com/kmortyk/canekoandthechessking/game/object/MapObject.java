package com.kmortyk.canekoandthechessking.game.object;

import android.graphics.Rect;

import com.kmortyk.canekoandthechessking.game.math.Vector2;
import com.kmortyk.canekoandthechessking.resources.ResourceManager;

public class MapObject extends GameObject {

    public String name, label, level;

    public MapObject(String name, float x, float y, String label, String level) {

        super(new Vector2(x, y),
              ResourceManager.getInstance().loadDrawable(name));

        this.name = name;
        this.label = label;
        this.level = level;

    }

}
