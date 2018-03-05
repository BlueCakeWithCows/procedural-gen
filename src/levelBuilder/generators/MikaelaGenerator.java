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
    private boolean[][] checked; // already checked positions
    private boolean[][] up;
    private boolean[][] down;
    private boolean[][] left;
    private boolean[][] right;

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
        grid = Util.createEmptyWorld(width, height);
        region = new TileRegion(grid);

        ArrayList<Entity> entities = new ArrayList<Entity>();
        double currentDensity;

        // set outer boundary as already visited so they wont get touched
//        checked[0][height] = true;
//        checked[width][height] = true;
//        checked[0][0] = true;
//        checked[width][0] = true;
        // initially set everything to true
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                checked[x][y] = true;
                up[x][y] = true;
                down[x][y] = true;
                left[x][y] = true;
                right[x][y] = true;
            }
        }

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
            //Util.getAdjacentPositions()

            createPaths(1, 1);
            showWalls(region, wallTile1);

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

    /**
     * @Source: https://algs4.cs.princeton.edu/41graph/Maze.java.html
     */
    private void createPaths(int x, int y) {
        while (!checked[x][y+1] || !checked[x][y-1] || !checked[x+1][y] || !checked[x-1][y]) {
            Random rand = new Random();
            int random = rand.nextInt(4);

            if (random == 0 && !checked[x][y+1]) {
                up[x][y] = false;
                down[x][y+1] = false;
                createPaths(x, y+1);
                //return;
            }
            else if (random == 1 && !checked[x][y-1]) {
                up[x][y - 1] = false;
                down[x][y] = false;
                createPaths(x, y-1);
                //return;
            }
            else if (random == 2 && !checked[x+1][y]) {
                right[x][y] = false;
                left[x+1][y] = false;
                createPaths(x+1, y);
                //return;
            }
            else if (random == 3 && !checked[x-1][y]) {
                right[x-1][y] = false;
                left[x][y] = false;
                createPaths(x-1, y);
                //return;
            }
        }
    }

    private void showWalls(TileRegion region, TETile WALL) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (up[x][y]) {
                    region.setTile(x+1, y, WALL);
                    region.setTile(x+1, y+1, WALL);
                }
                if (down[x][y]) {
                    region.setTile(x, y, WALL);
                    region.setTile(x+1, y, WALL);
                }
                if (right[x][y]) {
                    region.setTile(x+1, y, WALL);
                    region.setTile(x+1, y+1, WALL);
                }
                if (left[x][y]) {
                    region.setTile(x, y, WALL);
                    region.setTile(x, y+1, WALL);
                }
            }
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
