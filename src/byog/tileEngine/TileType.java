package byog.tileEngine;

public enum TileType {
    WALL(true, true), NOTHING(false, true), FLOOR(false, false), PORTAL(false, false);

    private final boolean opaque;
    private final boolean solid;

    TileType(boolean opaque, boolean solid) {
        this.opaque = opaque;
        this.solid = solid;
    }

    public boolean isOpaque() {
        return opaque;
    }

    public boolean isSolid() {
        return solid;
    }
}
