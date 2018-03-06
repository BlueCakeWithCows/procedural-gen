package Core;

import geometry.Point;
import geometry.Rectangle;
import levelBuilder.Entity;
import levelBuilder.TileRegion;
import tileEngine.TETile;
import tileEngine.TileType;
import tileEngine.Tileset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Util {

    public static long cantorPair(long seed, long id) {
        long pair = (seed + id) * Long.divideUnsigned(seed + id + 1, 2) + seed;
        return pair;
    }

    public static ArrayList<Point> populateNodesRandom(Random random, int x1, int y1, int x2,
        int y2, int nodes, int minSpacing) {
        int loopFail = 0;
        int i = 0;
        ArrayList<Point> points = new ArrayList<>();
        while (i < nodes) {
            Point pos =
                new Point(RandomUtils.uniform(random, x1, x2), RandomUtils.uniform(random, y1, y2));
            if (!isNear(pos.getX(), pos.getY(), points, minSpacing)) {
                points.add(pos);
                i++;
            }
            if (loopFail > nodes * 10) {
                MyLogger.log("Only  " + i + " of " + nodes + " nodes placed.");
                break;
            }
            loopFail++;
        }
        return points;
    }

    /*
     * @Source https://stackoverflow.com/questions/4849051/using-contains-on-an-arraylist-with
     * -integer-arrays
     */
    public static boolean isInList(final List<int[]> list, final int[] candidate) {
        return list.stream().anyMatch(a -> Arrays.equals(a, candidate));
    }

    public static int[][] getAdjacentPositions(int[] pos) {
        return new int[][]{
            Util.add(pos, 0, 1), Util.add(pos, -1, 0), Util.add(pos, 0, -1), Util.add(pos, 1, 0)
        };
    }

    public static int[] add(int[] pos, int x, int y) {
        return new int[]{pos[0] + x, pos[1] + y};
    }


    public static List<Point> getRandomPointsInRange(Random random, int x1, int y1, int x2, int y2,
        double nodeChance, int nodeMinSpacing) {
        List<Point> list = new ArrayList<Point>();
        for (int col = x1; col < x2; col++) {
            for (int row = y1; row < y2; row++) {
                if (RandomUtils.chance(random, nodeChance) && !isNear(col, row, list,
                    nodeMinSpacing)) {
                    list.add(new Point(col, row));
                }
            }
        }
        return list;
    }

    public static List<Rectangle> getRandomRectanglesAround(Random random, List<Point> points,
        int minSize, int maxSize, int x1, int y1, int x2, int y2) {
        List<Rectangle> rects = new ArrayList<>(points.size());
        for (Point pos : points) {
            int width = RandomUtils.uniform(random, minSize, maxSize + 1);
            int height = RandomUtils.uniform(random, minSize, maxSize + 1);
            int x = pos.getX() - width / 2;
            int y = pos.getY() - height / 2;
            Rectangle rect = new Rectangle(x, y, width, height);
            if (x < x1 || y < y1 || x + width >= x2 || y + height >= y2) {
                continue;
            }
            rects.add(rect);
        }
        return rects;
    }

    public static boolean isNear(int x, int y, List<Point> positions, int distance) {
        for (Point pos : positions) {
            if (pos.mailDistance(x, y) <= distance) {
                return true;
            }
        }
        return false;
    }

    /**
     * Destructively deletes unconnected tiles room nodes starting from random node position.
     *
     * @param region LevelBuilder instance used for pruning
     * @return Modified LevelBuilder
     */
    public static TileRegion prune(TETile tile, Point start, TileRegion region) {
        List<Point> positions = region.getByFloodFill(start, tile);

        for (int col = 0; col < region.getWidth(); col++) {
            for (int row = 0; row < region.getHeight(); row++) {
                if (!positions.contains(new Point(col, row))) {
                    region.setTile(col, row, Tileset.NOTHING);
                }
            }
        }
        return region;
    }

    public static double getDensity(TileRegion region, TileType type) {
        List<TETile> tiles = region.getAllTiles();
        return (double) tiles.stream().filter(obj -> TileType.NOTHING.equals(obj.getType())).count()
                   / (double) tiles.size();
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

    public static TETile[][] createEmptyWorld(int width, int height) {
        TETile[][] world = new TETile[width][height];
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        return world;
    }


    public static Point getOffCenter(TileRegion region) {
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
        return new Point(shiftX, shiftY);
    }

    public static void shiftRegion(TileRegion region, int shiftX, int shiftY) {
        TileRegion tiles = region.getGridForm();
        region.fill(Tileset.NOTHING);
        for (int col = 0; col < region.getWidth(); col++) {
            for (int row = 0; row < region.getHeight(); row++) {
                region.setTile(col + shiftX, row + shiftY, tiles.getTile(col, row));
            }
        }
    }

    public static void shiftEntities(ArrayList<Entity> entities, int x, int y) {
        for (Entity e: entities) {
            e.move(x, y);
        }
    }
}
