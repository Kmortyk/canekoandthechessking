package com.kmortyk.canekoandthechessking.game.command;

import com.kmortyk.canekoandthechessking.game.decoration.Decoration;
import com.kmortyk.canekoandthechessking.game.item.GameItem;

public interface Command {

    /**
     * Checks that all conditions are met so that the command can be executed.
     */
    boolean canBeExecuted(Decoration dec);

    /**
     * Executes the command.
     * More often (it === this) for GameItem.
     */
    void execute(GameItem it, Decoration dec);

}
