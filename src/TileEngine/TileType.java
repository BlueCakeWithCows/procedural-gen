package tileEngine;

public enum TileType {
    WALL(true), NOTHING(false), FLOOR(false);

    private final boolean opaque;

    TileType(boolean opaque) {
        this.opaque = opaque;
    }

    public boolean isOpaque() {
        return opaque;
    }
}