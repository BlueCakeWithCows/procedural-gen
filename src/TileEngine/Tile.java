package TileEngine;

import java.awt.Color;

public class Tile {
    private final TileType type;
    private final char character; // Do not rename character or the autograder will break.
    private final Color textColor;
    private final Color backgroundColor;
    private final String description;
    private final String filepath;
    private final int naturalLightLevel;

    public Tile(TileType type, char character, Color textColor, Color backgroundColor, String description, String filepath, int naturalLightLevel) {
        this.type = type;
        this.character = character;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
        this.description = description;
        this.filepath = filepath;
        this.naturalLightLevel = naturalLightLevel;
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
}
