package com.kmortyk.canekoandthechessking.game.item;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.kmortyk.canekoandthechessking.game.decoration.Decoration;
import com.kmortyk.canekoandthechessking.game.command.Command;
import com.kmortyk.canekoandthechessking.util.Vector2;
import com.kmortyk.canekoandthechessking.game.object.GameObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameItem extends GameObject {

    public static final String TYPE_STANDARD = "none";

    public static final int STACKABLE = 2;
    public static final int USABLE = 4;

    public String fullTextureName;

    private int count = 1;
    private String name = TYPE_STANDARD;
    private String type = TYPE_STANDARD;

    private int flags;

    private List<Command> commands;

    public GameItem(int flags, @NonNull Bitmap texture, int i, int j) {
        super(new Vector2(), texture);
        moveTo(i, j);
        this.flags = flags;
        commands = new ArrayList<>();
    }

    /**
     * Data constructor
     */
    public GameItem(int flags, Bitmap texture) {
        super(texture);
        this.flags = flags;
    }

    public GameItem clone(int i, int j) { return new GameItem(flags, texture, i, j); }

    /* --- use ----------------------------------------------------------------------------------- */

    public void addCommand(Command ... command) { commands.addAll(Arrays.asList(command)); }

    public void useOn(Decoration dec) {
        for (Command command: commands) {
            if(command.canBeExecuted(dec)) { command.execute(this, dec); }
        }
    }

    // TODO abilities???
    public void add(int count) { this.count += count; }

    public int getCount() { return count; }

    /* --- flags --------------------------------------------------------------------------------- */

    public static int getFlags(boolean isStackable, boolean isUsable) {
        int flags = 0;

        if(isStackable) flags |= STACKABLE;
        if(isUsable) flags |= USABLE;

        return flags;
    }

    public boolean is(int flag) { return (flags & flag) != 0; }

    /* ------------------------------------------------------------------------------------------- */

    public void setName(String name) { this.name = name; }

    public String getName() { return name; }

    public void setType(String type) { this.type = type; }

    public boolean equalsByName(GameItem it) { return name.equals((it).getName()); }

}
