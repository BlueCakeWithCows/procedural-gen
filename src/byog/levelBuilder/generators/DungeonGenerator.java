package byog.levelBuilder.generators;

import byog.Core.MailDistanceSort;
import byog.Core.RandomUtils;
import byog.Core.Util;
import byog.geometry.Point;
import byog.geometry.Rectangle;
import byog.levelBuilder.Entity;
import byog.levelBuilder.Generator;
import byog.levelBuilder.Player;
import byog.levelBuilder.TileRegion;
import byog.levelBuilder.Torch;
import byog.levelBuilder.World;
import byog.tileEngine.TETile;
import byog.tileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static byog.Core.RandomUtils.uniform;
import static byog.tileEngine.TileType.FLOOR;
import static byog.tileEngine.TileType.NOTHING;

public class DungeonGenerator implements Generator {


    private final double minDensity, maxDensity;
    private final double nodeDensity;
    private final double hallwayDensity;
    private final int minSize, maxSize;
    private final int nodeSpacing;
    private final String label;

    public DungeonGenerator(String label, double minDensity, double maxDensity, double nodeDensity,
        int nodeSpacing, double hallwayDensity, int minSize, int maxSize) {
        this.minDensity = minDensity;
        this.maxDensity = maxDensity;
        this.nodeDensity = nodeDensity;
        this.nodeSpacing = nodeSpacing;
        this.hallwayDensity = hallwayDensity;
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.label = label;
    }


    @Override
    public String getDess() {
        return label;
    }

    @Override
    public World generate(long seed, Player player, Map<String, Object> param) {
        TETile[][] grid;
        Random random = new Random(seed);
        int width1 = (Integer) param.getOrDefault("width", 50);
        int height1 = (Integer) param.getOrDefault("height", 50);

        TETile floorTile = (TETile) param.get("floor_tile");
        TETile wallTile1 = (TETile) param.get("wall_tile1");
        TETile wallTile2 = (TETile) param.get("wall_tile2");
        ArrayList<Entity> entities = new ArrayList<Entity>();
        double currentDensity;
        do {
            entities.clear();
            List<Point> nodes = Util.populateNodesRandom(random, 3, 3, width1 - 3, height1 - 3,
                (int) (nodeDensity * width1 * height1), nodeSpacing);
            List<Rectangle> rectangles =
                Util.getRandomRectanglesAround(random, nodes, minSize, maxSize, 1, 1, width1 - 1,
                    height1 - 1);
            grid = Util.createEmptyWorld(width1, height1);
            TileRegion region = new TileRegion(grid);
            for (Rectangle r : rectangles) {
                region.fillRect(r, floorTile);
            }

            for (int i = 0; i < nodes.size() * hallwayDensity; i++) {
                Point pos1 = nodes.get(uniform(random, 0, nodes.size()));
                Point pos2 = getRandomNearNode(random, 3, pos1, nodes);
                drawHall(random, region, floorTile, pos1, pos2);
            }


            Util.prune(floorTile, nodes.get(uniform(random, 0, nodes.size())), region);
            Util.generateWalls(region, wallTile1, wallTile2);
            for (Point p : nodes) {
                player.setPositionRef(new Point(p));
                if (region.getTile(p.getX(), p.getY()).getType() == FLOOR) {
                    break;
                }
            }


            for (Point p : nodes) {
                if (RandomUtils.chance(random, .90f)) {
                    continue;
                }
                region.setTile(p.getX(), p.getY(), Tileset.PORTAL);
            }

            for (Point p : nodes) {
                if (RandomUtils.chance(random, .76f)) {
                    continue;
                }
                if (region.getTile(p.getX(), p.getY()).getType() == FLOOR) {
                    entities.add(new Torch(new Point(p)));
                }
            }


            Point delta = Util.getOffCenter(region);
            entities.add(player);
            Util.shiftRegion(region, delta.getX(), delta.getY());
            Util.shiftEntities(entities, delta.getX(), delta.getY());
            currentDensity = 1d - Util.getDensity(region, NOTHING);
        } while (currentDensity < minDensity || currentDensity > maxDensity);
        World world = new World(grid, entities, player);
        return world;
    }

    private void drawHall(Random rand, TileRegion region, TETile tile, Point pos1, Point pos2) {
        if (rand.nextBoolean()) {
            region.fillBox(pos1.toInt(), new int[]{pos2.getX(), pos1.getY()}, tile);
            region.fillBox(pos2.toInt(), new int[]{pos2.getX(), pos1.getY()}, tile);
        } else {
            region.fillBox(pos1.toInt(), new int[]{pos1.getX(), pos2.getY()}, tile);
            region.fillBox(pos2.toInt(), new int[]{pos1.getX(), pos2.getY()}, tile);
        }
    }

    private Point getRandomNearNode(Random random, int nearestOf, Point start, List<Point> nodes) {
        MailDistanceSort.sortByMailDistance(start, nodes);
        int max = Math.min(nearestOf + 1, nodes.size());
        if (max < 2) {
            return start;
        }
        return nodes.get(uniform(random, 1, max));
    }
}
