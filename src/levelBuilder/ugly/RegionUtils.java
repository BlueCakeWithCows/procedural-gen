package levelBuilder.ugly;

import byog.TileEngine.TETile;
import byog.TileEngine.TileType;

import java.util.List;

public class RegionUtils {
    public static double getDensity(TileRegion region, TileType type) {
        List<TETile> tiles = region.getAllTiles();
        return (double) tiles.stream().filter(obj -> TileType.NOTHING.equals(obj.getType())).count()
                       / (double) tiles.size();
    }
}
