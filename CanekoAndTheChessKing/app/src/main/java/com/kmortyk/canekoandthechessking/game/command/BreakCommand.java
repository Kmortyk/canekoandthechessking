package com.kmortyk.canekoandthechessking.game.command;

import com.kmortyk.canekoandthechessking.game.decoration.Decoration;
import com.kmortyk.canekoandthechessking.game.item.GameItem;
import com.kmortyk.canekoandthechessking.resources.ResourceManager;

public class BreakCommand implements Command {

    @Override
    public boolean canBeExecuted(Decoration dec) { return !dec.isStatic(); }

    @Override
    public void execute(GameItem it, Decoration dec) {
        // FIXME
        dec.texture = ResourceManager.emptyBitmap;
    }

}
