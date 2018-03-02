package tileEngine;

import java.awt.Color;

public class TETile {
    private final TileType type;
    private final char character; // Do not rename character or the autograder will break.
    private final Color textColor;
    private final Color backgroundColor;
    private final String description;
    private final String filepath;
    private final int naturalLightLevel;

    public TETile(TileType type, char character, Color textColor, Color backgroundColor,
        String description, String filepath, int naturalLightLevel) {
        this.type = type;
        this.character = character;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
        this.description = description;
        this.filepath = filepath;
        this.naturalLightLevel = naturalLightLevel;
    }

    public TETile(TileType type, int i, char c, Color textColor, Color backGroundColor,
        String description) {
        this(type, c, textColor, backGroundColor, description, null, i);
    }

    public boolean isOpaque() {
        return type.isOpaque();
    }

    public TileType getType() {
        return type;
    }

    public char getCharacter() {
        return character;
    }

    public String getDescription() {
        return description;
    }

    public String getFilepath() {
        return filepath;
    }

    public int getNaturalLightLevel() {
        return naturalLightLevel;
    }

    public String toString() {
        return String.valueOf(getCharacter());
    }


    public static String toString(TETile[][] world) {
        int width = world.length;
        int height = world[0].length;
        StringBuilder sb = new StringBuilder();

        for (int y = height - 1; y >= 0; y -= 1) {
            for (int x = 0; x < width; x += 1) {
                if (world[x][y] == null) {
                    throw new IllegalArgumentException(
                        "Tile at position x=" + x + ", y=" + y + "" + " is null.");
                }
                sb.append(world[x][y].getCharacter());
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
