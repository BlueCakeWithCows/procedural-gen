package core;

import geometry.Point;
import geometry.Rectangle;
import levelBuilder.ugly.TileRegion;
import tileEngine.TETile;
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
                    nodeMinSpacing
                )) {
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
            if (x < x1 || y < y1 || x + width >= x2 || y + width >= y2) { continue; }
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
}
