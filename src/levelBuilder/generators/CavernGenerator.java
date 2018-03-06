package levelBuilder.generators;

import Core.RandomUtils;
import Core.Util;
import geometry.Point;
import levelBuilder.Entity;
import levelBuilder.Generator;
import levelBuilder.Player;
import levelBuilder.TileRegion;
import levelBuilder.Torch;
import levelBuilder.World;
import tileEngine.TETile;
import tileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static tileEngine.TileType.FLOOR;
import static tileEngine.TileType.NOTHING;

public class CavernGenerator implements Generator {

    private final String label;
    private final int width, height;
    private final double minDensity, maxDensity;
    private final int iterations;

    public CavernGenerator(String label, int width, int height, double minDensity,
        double maxDensity, int iterations) {
        this.label = label;
        this.width = width;
        this.height = height;
        this.minDensity = minDensity;
        this.maxDensity = maxDensity;
        this.iterations = iterations;
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


        do {
            entities.clear();
            grid = Util.createEmptyWorld(width, height);
            region = new TileRegion(grid);
            initialFill(region.getShrunkRegion(1), random, .45d, floorTile);
            for (int i = 0; i < iterations; i++) {
                caveSmooth(region);
            }
            Util.prune(Tileset.FLOOR, new Point(width / 2, height / 2), region);

            List<Point> torchPoints =
                Util.populateNodesRandom(random, 1, 1, region.getWidth(), region.getHeight(),
                    (int) (.01 * region.getWidth() * region.getHeight()), 10);
            List<Point> portalPoints =
                Util.populateNodesRandom(random, 1, 1, region.getWidth(), region.getHeight(),
                    (int) (.01 * region.getWidth() * region.getHeight()), 10);
            TileRegion finalRegion = region;
            torchPoints.stream().filter(point -> finalRegion.getTile(point).getType() == FLOOR)
                .forEach(e -> entities.add(new Torch(e)));
            portalPoints.stream().filter(point -> finalRegion.getTile(point).getType() == FLOOR)
                .forEach(e -> finalRegion.setTile(e.getX(), e.getY(), Tileset.PORTAL));
            region = finalRegion;

            do {
                player.setPositionRef(new Point(random.nextInt(region.getWidth()),
                    random.nextInt(region.getHeight())));
            } while (region.getTile(player.getPosition()).getType() != FLOOR);
            entities.add(player);
            Util.generateWalls(region, wallTile1, wallTile2);
            Point delta = Util.getOffCenter(region);
            Util.shiftRegion(region, delta.getX(), delta.getY());
            Util.shiftEntities(entities, delta.getX(), delta.getY());
            currentDensity = 1d - Util.getDensity(region, NOTHING);
        } while (currentDensity < minDensity || currentDensity > maxDensity);
        World world = new World(grid, entities, player);
        return world;
    }

    private void initialFill(TileRegion region, Random random, double chance, TETile tile) {
        for (int col = 0; col < region.getWidth(); col++) {
            for (int row = 0; row < region.getHeight(); row++) {
                if (RandomUtils.chance(random, chance)) { region.setTile(col, row, tile); }
            }
        }
    }

    private static void caveSmooth(TileRegion region) {
        TileRegion tmp = region.getGridForm();
        for (int col = 0; col < region.getWidth(); col++) {
            for (int row = 0; row < region.getHeight(); row++) {
                int neighbors = count(tmp.getEightAdjacent(col, row), Tileset.FLOOR);
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

    private static int count(List<?> lst, Object obj) {
        int i = 0;
        for (Object o : lst) {
            if (obj.equals(o)) {
                i++;
            }
        }
        return i;
    }
}
