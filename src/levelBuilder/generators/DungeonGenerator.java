package levelBuilder.generators;

import core.MailDistanceSort;
import core.Util;
import geometry.Point;
import geometry.Rectangle;
import levelBuilder.Entity;
import levelBuilder.Generator;
import levelBuilder.Player;
import levelBuilder.World;
import levelBuilder.WorldGenerator;
import levelBuilder.ugly.LevelBuilder;
import levelBuilder.ugly.RegionFunctions;
import levelBuilder.ugly.RegionUtils;
import levelBuilder.ugly.TileRegion;
import tileEngine.TETile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static core.RandomUtils.uniform;

public class DungeonGenerator implements Generator {


    private final double minDensity, maxDensity;
    private final double nodeDensity;
    private final double hallwayDensity;
    private final int hallwayNodeDistance;
    private final int minSize, maxSize;
    private final int[] roomFrequencies;
    private final WorldGenerator[] rooms;
    private final String label;
    private final int width, height;

    public DungeonGenerator(String label, double minDensity, double maxDensity, double nodeDensity,
        double hallwayDensity, int hallwayNodeDistance, int minSize, int maxSize,
        int[] roomFrequencies, int width, int height) {
        this.minDensity = minDensity;
        this.maxDensity = maxDensity;
        this.nodeDensity = nodeDensity;
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

    public TileRegion genRegion(TileRegion r, double difficulty) {
        LevelBuilder region = (LevelBuilder) r;
        double currentDensity;
        do {
            LevelFunctions.clear(region);
            LevelFunctions.populateNodes(getRandom(), region, nodeDensity, nodeMinSpacing,
                nodeDistanceFromWalls
            );
            generateRoomSpace(r, difficulty);
            LevelFunctions.fillRoomSpace(getRandom(), region, rooms, roomFrequencies, difficulty);
            LevelFunctions
                .generateHallways(getRandom(), region, hallwayDensity, hallwayNodeDistance);
            LevelFunctions.prune(getRandom(), region);
            RegionFunctions.generateWalls(region, Tileset.WALL1, Tileset.WALL1FRONT);
            RegionFunctions.generatePuddles(getRandom(), region, puddleDensity, Tileset.WATER);
            RegionFunctions.mark(region, region.getNodes(), Tileset.TREE);
            RegionFunctions.centerRegion(region);

            currentDensity = 1d - RegionUtils.getDensity(region, TileType.NOTHING);
        } while (currentDensity < minDensity || currentDensity > maxDensity);
        return region;
    }

    @Override
    public String getDess() {
        return label;
    }

    @Override
    public World generate(long seed, Player player, HashMap<String, Object> param) {
        TETile[][] grid;
        Random random = new Random(seed);
        int width = (Integer) param.getOrDefault("width", this.width);
        int height = (Integer) param.getOrDefault("height", this.height);
        double roomChance = (Double) param.get("room_chance");
        int roomSpacing = (Integer) param.get("room_spacing");
        TETile floorTile = (TETile) param.get("floor_tile");
        double currentDensity;
        do {
            List<Point> nodes = Util.getRandomPointsInRange(random, 3, 3, width - 3, height - 3,
                roomChance, roomSpacing
            );
            List<Rectangle> rectangles = Util.getRandomRectanglesAround(random, nodes, minSize,
                maxSize, 1, 1, width - 1, height - 1
            );
            grid = LevelBuilder.createEmptyWorld(width, height);
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


        } while (currentDensity < minDensity || currentDensity > maxDensity);
        World world = new World(grid, new ArrayList<Entity>(), player);
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
