package tileEngine;

import java.awt.Color;

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
    public static final TETile WALL = new TETile(TileType.WALL, ' ', new Color(216, 128, 128),
        Color.white, "wall", "wall.png", 0
    );
    public static final TETile FLOOR = new TETile(TileType.FLOOR, 0, ' ', new Color(160, 23, 23),
        new Color(160, 23, 23), "floor"
    );
    public static final TETile NOTHING = new TETile(TileType.NOTHING, ' ', Color.black, Color.black,
        "nothing", "background.png", 0
    );
    public static final TETile GRASS = new TETile(TileType.FLOOR, 0, '"', Color.green, Color.black,
        "grass"
    );
    public static final TETile WATER = new TETile(TileType.FLOOR, 0, '≈', Color.blue,
        new Color(160, 23, 23), "water"
    );
    public static final TETile FLOWER = new TETile(TileType.FLOOR, 0, '❀', Color.magenta,
        new Color(160, 23, 23), "flower"
    );
    public static final TETile LOCKED_DOOR = new TETile(TileType.WALL, 0, '█', Color.orange,
        Color.black, "locked door"
    );
    public static final TETile UNLOCKED_DOOR = new TETile(TileType.FLOOR, 0, '▢', Color.orange,
        Color.black, "unlocked door"
    );
    public static final TETile SAND = new TETile(TileType.FLOOR, 0, '▒', Color.yellow, Color.black,
        "sand"
    );
    public static final TETile MOUNTAIN = new TETile(TileType.FLOOR, 0, '▲', Color.gray,
        Color.black, "mountain"
    );
    public static final TETile TREE = new TETile(TileType.FLOOR, 0, '♠', Color.green,
        new Color(160, 23, 23), "tree"
    );
    public static final TETile ZOMBIE = new TETile(TileType.NOTHING, 'Z', Color.GREEN, Color.black,
        "zombie", "res/zombie.png", 0
    );
    public static final TETile PLAYER = new TETile(null, 0, '@', Color.white, Color.black,
        "player"
    );
    public static final TETile WALL1 = new TETile(TileType.WALL, '@', Color.white, Color.black,
        "wall", "res/basic_wall" + ".png", 100
    );
    public static final TETile WALL1FRONT = new TETile(TileType.WALL, '@', Color.white, Color.black,
        "wall", "res/basic_wall_front.png", 0
    );
    public static final TETile GLOW_MOSS = new TETile(TileType.FLOOR, '*', Color.white, Color.black,
        "glowshroom", "res/glowing_mushroom.png", 10
    );

}


