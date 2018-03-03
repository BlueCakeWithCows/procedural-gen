package tileEngine;

import renderer.Textures;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 * <p>
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 * <p>
 * Ex:
 * world[x][y] = Tileset.FLOOR;
 * <p>
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final TETile WALL = new TETile(TileType.WALL, Textures.BASIC_WALL, "wall", 5);
    public static final TETile WALL2 = new TETile(TileType.WALL, Textures.BASIC_WALL2, "wall", 2);

    public static final TETile NOTHING = new TETile(TileType.NOTHING, Textures.NOTHING, "", 0);
    public static final TETile FLOOR = new TETile(TileType.FLOOR, Textures.FLOOR, "floor", 0);
    public static final TETile PLAYER = new TETile(TileType.NOTHING, Textures.PLAYER, "player", 0);

}


