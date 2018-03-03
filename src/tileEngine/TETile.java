package tileEngine;

import renderer.Textures;

public class TETile {
    private final TileType type;
    private final String description;
    private final Textures texture;
    private final int naturalLightLevel;

    public TETile(TileType type, Textures texture, String description, int naturalLightLevel) {
        this.type = type;
        this.description = description;
        this.texture = texture;
        this.naturalLightLevel = naturalLightLevel;
    }


    public boolean isOpaque() {
        return type.isOpaque();
    }

    public TileType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public int getNaturalLightLevel() {
        return naturalLightLevel;
    }

    public Textures getTexture() {
        return texture;
    }
}
