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

public class MazeGenerator implements Generator {

    private final String label;
    private final int width, height;

    public MazeGenerator(String label, int width, int height) {
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
        int width1 = (Integer) param.getOrDefault("width", this.width);
        int height1 = (Integer) param.getOrDefault("height", this.height);
        TETile floorTile = (TETile) param.get("floor_tile");
        TETile wallTile1 = (TETile) param.get("wall_tile1");
        TETile wallTile2 = (TETile) param.get("wall_tile2");

        ArrayList<Entity> entities = new ArrayList<Entity>();
        double currentDensity;
        do {
            entities.clear();
            grid = Util.createEmptyWorld(width1, height1);
            region = new TileRegion(grid);

//            for (int x = 0; x < width; x++) {
//                for (int y = 0; y < height; y++) {
//                    region.setTile(x, y, floorTile);
//                }
//            }
//
//            for (int x = 0; x < width; x++) {
//                region.setTile(x, 0, wallTile1);
//                region.setTile(x, height, wallTile1);
//            }
//
//            for (int y = 0; y < height; y++) {
//                region.setTile(0, y, wallTile1);
//                region.setTile(width, y, wallTile1);
//            }
//
//            // create random diamond shaped blocks
//
//            for (int x = 0; x < width; x++) {
//                for (int y = 0; y < height; y++) {
//                    if (y % (x + 1) == 5) {
//                        region.setTile(x, y, wallTile1);
//                        region.setTile(x + 1, y, wallTile1);
//                        region.setTile(x - 1, y, wallTile1);
//                        region.setTile(x, y + 1, wallTile1);
//                        region.setTile(x, y - 1, wallTile1);
//                    }
//                }
//            }

            for (int x = 0; x < width1; x++) {
                for (int y = 0; y < height1; y++) {
                    region.setTile(x, y, floorTile);
                }
            }

            for (int x = 0; x < width1; x++) {
                region.setTile(x, 0, wallTile1);
                region.setTile(x, height1, wallTile1);
            }

            for (int y = 0; y < height1; y++) {
                region.setTile(0, y, wallTile1);
                region.setTile(width1, y, wallTile1);
            }

            for (int y = 0; y < height1; y++) {
                if (y % 6 == 3) {
                    for (int x = 0; x < width1 - 3; x++) {
//                        if (x % 40 == 0) {
//                            region.setTile(x, y, floorTile);
//                        }
//                        else {
                            region.setTile(x, y, wallTile1);
                        //}
                    }
                } else if (y % 6 == 0) {
                    for (int x = 3; x < width1; x++) {
//                        if (y % 18 == 0 && x % 20 == 0) {
//                            region.setTile(x, y, floorTile);
//                        }
//                        else if (y % 30 == 0 && x % 50 == 0) {
//                            region.setTile(x, y, floorTile);
//                        }
//                        else {
                        region.setTile(x, y, wallTile1);
                        //}
                    }
                }
            }
            Util.generateWalls(region, wallTile1, wallTile2);
            player.setPositionRef(new Point(width1 / 2, height1 / 2));
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
