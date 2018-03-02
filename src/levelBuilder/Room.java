package levelBuilder;

import levelBuilder.ugly.TileRegion;

import java.util.List;

public class Room extends TileRegion {

    private final String TAG;
    private List<int[]> connectionPoints;

    public Room(String tag, TileRegion region) {
        super(region);
        this.TAG = tag;
    }

    public List<int[]> getConnectionPoints() {
        return connectionPoints;
    }

    public String toString() {
        return getTAG() + ": " + getOffsetX() + ", " + getOffsetY() + ", " + getWidth() + ", "
                       + getHeight();
    }

    public String getTAG() {
        return TAG;
    }
}
