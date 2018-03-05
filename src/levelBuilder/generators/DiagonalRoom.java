package levelBuilder.generators;

import Core.Util;
import geometry.Point;
import levelBuilder.*;
import tileEngine.TETile;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import static tileEngine.TileType.NOTHING;

public class DiagonalRoom implements Generator {
    private final String label;
    private final int width, height;

    public DiagonalRoom(String label, int width, int height) {
        this.label = label;
        this.width = width;
        this.height = height;
    }

    @Override
    public String getDess() {
        return "Diagonal Room";
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

            for (int x = 1; x < width; x++) {
                for (int y = 1; y < height; y++) {
                    {
                        if (x % 3 == 0 && y % 3 == 0) {
                            region.setTile(x, y, wallTile1);
                        }
                        else if (x % 3 == 1 && y % 3 == 1) {
                            region.setTile(x, y, wallTile1);
                        }
                        else if (x % 3 == 2 && y % 3 == 2) {
                            region.setTile(x, y, wallTile1);
                        }
                        else {
                            region.setTile(x, y, floorTile);
                        }
                    }
                }
            }
            Util.generateWalls(region, wallTile1, wallTile2);
            player.setPositionRef(new Point(width / 2, height / 2));
            entities.add(player);
            Point delta = Util.getOffCenter(region);
            Util.shiftRegion(region, delta.getX(), delta.getY());
            Util.shiftEntities(entities, delta.getX(), delta.getY());
            currentDensity = 1d - Util.getDensity(region, NOTHING);
        } while (false);
        World world = new World(grid, entities, player);
        return world;
    }
}
