package levelBuilder.ugly;

import byog.Core.Level.LevelBuilder;
import byog.Core.Level.Room;
import byog.Core.Util.RandomUtils;
import byog.Core.Util.Util;
import byog.TileEngine.TETile;
import byog.TileEngine.TileType;
import byog.TileEngine.Tileset;
import levelBuilder.Entity;
import levelBuilder.Room;

import java.util.List;
import java.util.Random;

public class RegionFunctions {

    /**
     * Centers tiles within TileRegion.
     *
     * @param region to be centered
     * @return Modified TileRegion
     */
    public static TileRegion centerRegion(TileRegion region) {
        int x1 = region.getWidth();
        int y1 = region.getHeight();
        int x2 = 0;
        int y2 = 0;
        for (int col = 0; col < region.getWidth(); col++) {
            for (int row = 0; row < region.getHeight(); row++) {
                if (region.getTile(col, row) != null
                        && region.getTile(col, row).getType() != TileType.NOTHING) {
                    if (col < x1) {
                        x1 = col;
                    }
                    if (col > x2) {
                        x2 = col;
                    }
                    if (row < y1) {
                        y1 = row;
                    }
                    if (row > y2) {
                        y2 = row;
                    }
                }
            }
        }
        int width = x2 - x1;
        int height = y2 - y1;
        int shiftX = (region.getWidth() - width - 1) / 2 - x1;
        int shiftY = (region.getHeight() - height - 1) / 2 - y1;
        TileRegion tiles = region.getGridForm();
        region.fill(Tileset.NOTHING);
        for (int col = 0; col < region.getWidth(); col++) {
            for (int row = 0; row < region.getHeight(); row++) {
                region.setTile(col + shiftX, row + shiftY, tiles.getTile(col, row));
            }
        }
        if (region instanceof LevelBuilder) {
            for (int[] pos : ((LevelBuilder) region).getNodes()) {
                pos[0] += shiftX;
                pos[1] += shiftY;
            }
            for (Entity e : ((LevelBuilder) region).getEntities()) {
                e.getPosition()[0] += shiftX;
                e.getPosition()[0] += shiftY;
            }
            for (Room r : ((LevelBuilder) region).getRooms()) {
                r.setOffsetX(r.getOffsetX() + shiftX);
                r.setOffsetY(r.getOffsetY() + shiftY);
            }
        }
        return region;
    }

    /**
     * Generates small pools of tile on floor tiles in region.
     *
     * @param random  random generator
     * @param region  region
     * @param density chance per tile
     * @param tile    puddle tile
     * @return Modified region
     */
    public static TileRegion generatePuddles(Random random, TileRegion region, double density,
        TETile tile) {
        for (int col = 0; col < region.getWidth(); col++) {
            for (int row = 0; row < region.getHeight(); row++) {
                if (RandomUtils.chance(random, density)) {
                    tryPuddle(region, col, row, tile);
                    tryPuddle(region, col + RandomUtils.uniform(random, -1, 1), row, tile);
                    tryPuddle(region, col, row + RandomUtils.uniform(random, -1, 1), tile);
                }
            }
        }
        return region;
    }

    /**
     * Helper function for generatePuddles.
     */
    private static void tryPuddle(TileRegion region, int col, int row, TETile newTile) {
        TETile tile = region.getTile(col, row);
        if (tile != null && tile.getType() == TileType.FLOOR) {
            region.setTile(col, row, newTile);
        }
    }

    /**
     * Outlines non-nothing tiles in region.
     *
     * @param region   region
     * @param wallTile tile to outline
     * @return Modified region
     */
    public static TileRegion generateWalls(TileRegion region, TETile wallTile,
        TETile wallTileFront) {
        for (int col = 0; col < region.getWidth(); col++) {
            for (int row = 0; row < region.getHeight(); row++) {
                if (Tileset.NOTHING.equals(region.getTile(col, row))) {
                    for (TETile tile : region.getEightAdjacent(col, row)) {
                        if (!Tileset.NOTHING.equals(tile) && !tile.getType()
                                                                  .equals(TileType.WALL)) {
                            wallHelper(wallTileFront, wallTile, region, col, row);
                            break;
                        }

                    }
                }
            }
        }
        return region;
    }

    private static void wallHelper(TETile wallTileFront, TETile wallTile, TileRegion region,
        int col, int row) {
        if (wallTileFront != null && (region.getTile(col, row - 1) == null
                                          || region.getTile(col, row - 1).getType()
                                                 != TileType.WALL)) {
            region.setTile(col, row, wallTileFront);
        } else {
            region.setTile(col, row, wallTile);
        }
    }

    public static TileRegion generateWalls(TileRegion region, TETile wallTile) {
        return generateWalls(region, wallTile, null);
    }

    /**
     * Sets tile at each position to tile.
     *
     * @param region    region to be modified
     * @param positions list of tiles to mark
     * @param tile      new tile
     * @return modified region
     */
    public static TileRegion mark(TileRegion region, List<int[]> positions, TETile tile) {
        for (int[] pos : positions) {
            region.setTile(pos[0], pos[1], tile);
        }
        return region;
    }

    /**
     * Runs a single iteration of a smoothing algorithm based on conways game of life.
     * Primarily used for cave generation.
     *
     * @param region region to be modifed
     */
    public static void caveSmooth(TileRegion region) {
        TileRegion tmp = region.getGridForm();
        for (int col = 0; col < region.getWidth(); col++) {
            for (int row = 0; row < region.getHeight(); row++) {
                int neighbors = Util.count(tmp.getEightAdjacent(col, row), Tileset.FLOOR);
                if (neighbors < 3) {
                    region.setTile(col, row, Tileset.NOTHING);
                } else {
                    if (neighbors > 4) {
                        region.setTile(col, row, Tileset.FLOOR);
                    }
                }
            }
        }
    }
}
