package com.kmortyk.canekoandthechessking.resources;

/**
 * Data class which is obtained by reading the map file.
 */
public class ParsedMap {

    public final static String GAME_WORLD = "[game_world]";
    private final static String[][] empty = new String[0][0];

    public int[][] map;
    public String[][] enemies     = empty;
    public String[][] decorations = empty;
    public String[][] items       = empty;

    // hero pos
    public int heroI = 4, heroJ = 0;

    public String backType;
    public String mapName;
    public String nextMap = GAME_WORLD;

}
