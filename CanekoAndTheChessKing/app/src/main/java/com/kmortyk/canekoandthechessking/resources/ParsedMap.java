package com.kmortyk.canekoandthechessking.resources;

/**
 * Data class which is obtained by reading the map file.
 */
public class ParsedMap {

    public int[][] map;
    public String[][] enemies;

    // distance between active steps
    public int spaces = 1;

    // hero pos
    public int heroI = 4, heroJ = 0;

}
