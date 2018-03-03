package levelBuilder.generators;

import Core.MailDistanceSort;
import Core.Util;
import geometry.Point;
import geometry.Rectangle;
import levelBuilder.Entity;
import levelBuilder.Generator;
import levelBuilder.Player;
import levelBuilder.TileRegion;
import levelBuilder.World;
import levelBuilder.WorldGenerator;
import tileEngine.TETile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static Core.RandomUtils.uniform;
import static tileEngine.TileType.FLOOR;
import static tileEngine.TileType.NOTHING;

public class DungeonGenerator implements Generator {


    private final double minDensity, maxDensity;
    private final double nodeDensity;
    private final double hallwayDensity;
    private final int hallwayNodeDistance;
    private final int minSize, maxSize;
    private final int nodeSpacing;
    private final int[] roomFrequencies;
    private final WorldGenerator[] rooms;
    private final String label;
    private final int width, height;

    public DungeonGenerator(String label, double minDensity, double maxDensity, double nodeDensity,
        int node_spacing, double hallwayDensity, int hallwayNodeDistance, int minSize, int maxSize,
        int[] roomFrequencies, int width, int height) {
        this.minDensity = minDensity;
        this.maxDensity = maxDensity;
        this.nodeDensity = nodeDensity;
        this.nodeSpacing = node_spacing;
        this.hallwayDensity = hallwayDensity;
        this.hallwayNodeDistance = hallwayNodeDistance;
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.roomFrequencies = roomFrequencies;
        this.rooms = null;
        this.label = label;
        this.width = width;
        this.height = height;
    }


    @Override
    public String getDess() {
        return label;
    }

    @Override
    public World generate(long seed, Player player, Map<String, Object> param) {
        TETile[][] grid;
        Random random = new Random(seed);
        int width = (Integer) param.getOrDefault("width", this.width);
        int height = (Integer) param.getOrDefault("height", this.height);

        TETile floorTile = (TETile) param.get("floor_tile");
        TETile wallTile1 = (TETile) param.get("wall_tile1");
        TETile wallTile2 = (TETile) param.get("wall_tile2");
        ArrayList<Entity> entities = new ArrayList<Entity>();
        double currentDensity;
        do {
            List<Point> nodes =
                Util.getRandomPointsInRange(random, 3, 3, width - 3, height - 3, nodeDensity,
                    nodeSpacing);
            List<Rectangle> rectangles =
                Util.getRandomRectanglesAround(random, nodes, minSize, maxSize, 1, 1, width - 1,
                    height - 1);
            grid = Util.createEmptyWorld(width, height);
            TileRegion region = new TileRegion(grid);
            for (Rectangle r : rectangles) {
                region.fillRect(r, floorTile);
            }

            for (int i = 0; i < nodes.size() * hallwayDensity; i++) {
                Point pos1 = nodes.get(uniform(random, 0, nodes.size()));
                Point pos2 = getRandomNearNode(random, hallwayNodeDistance, pos1, nodes);
                drawHall(random, region, floorTile, pos1, pos2);
            }


            Util.prune(floorTile, nodes.get(uniform(random, 0, nodes.size())), region);
            Util.generateWalls(region, wallTile1, wallTile2);
            Point delta = Util.getOffCenter(region);
            for (Point p : nodes) {
                player.setPositionRef(new Point(p));
                if (region.getTile(p.getX(), p.getY()).getType() == FLOOR) {
                    break;
                }
            }

            Util.shiftRegion(region, delta.getX(), delta.getY());
            player.getPosition().add(delta);
            currentDensity = 1d - Util.getDensity(region, NOTHING);
            entities.clear();
            entities.add(player);
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
