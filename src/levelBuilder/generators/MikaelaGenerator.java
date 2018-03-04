package levelBuilder.generators;

import Core.MailDistanceSort;
import Core.Util;
import geometry.Point;
import levelBuilder.Entity;
import levelBuilder.Generator;
import levelBuilder.Player;
import levelBuilder.TileRegion;
import levelBuilder.World;
import tileEngine.TETile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static Core.RandomUtils.uniform;
import static tileEngine.TileType.NOTHING;

public class MikaelaGenerator implements Generator {

    private final String label;
    private final int width, height;

    public MikaelaGenerator(String label, int width, int height) {
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
        TileRegion region;
        Random random = new Random(seed);
        int width = (Integer) param.getOrDefault("width", this.width);
        int height = (Integer) param.getOrDefault("height", this.height);
        TETile floorTile = (TETile) param.get("floor_tile");
        TETile wallTile1 = (TETile) param.get("wall_tile1");
        TETile wallTile2 = (TETile) param.get("wall_tile2");

        ArrayList<Entity> entities = new ArrayList<Entity>();
        double currentDensity;
        do {
            entities.clear();
            grid = Util.createEmptyWorld(width, height);
            region = new TileRegion(grid);

            //region.setTile()
            //Util.generateWalls()
            //Util.prune()
            //Util.populateRandomNodes()
            //Util.isNear
            //drawHall
            //genRandomNearNode


            entities.add(player);
            Point delta = Util.getOffCenter(region);
            Util.shiftRegion(region, delta.getX(), delta.getY());
            Util.shiftEntities(entities, delta.getX(), delta.getY());
            currentDensity = 1d - Util.getDensity(region, NOTHING);
        } while (false);
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
