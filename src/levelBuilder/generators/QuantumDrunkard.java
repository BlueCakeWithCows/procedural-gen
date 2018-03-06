package levelBuilder.generators;

import Core.Util;
import geometry.Point;
import levelBuilder.Entity;
import levelBuilder.Generator;
import levelBuilder.Player;
import levelBuilder.TileRegion;
import levelBuilder.World;
import tileEngine.TETile;
import tileEngine.Tileset;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import static tileEngine.TileType.NOTHING;

/**
 * @Source https://codegolf.stackexchange.com/questions/148206/the-quantum-drunkards-walk
 */

public class QuantumDrunkard implements Generator {

    private final String label;
    private final int width, height;

    public QuantumDrunkard(String label, int width, int height) {
        this.label = label;
        this.width = width;
        this.height = height;
    }


    @Override
    public String getDess() {
        return "Drunkards Walk";
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
            java.util.function.IntFunction<char[][]> f = n -> {
                int s = n * 2 + 3, i = 0, x, y;
                char[][] a = new char[s][s], b;
                for (a[s / 2][s-- / 2] = 61; i++ < n; a = b) {
                    for (b = new char[s + 1][s + 1], x = 0; ++x < s; ) {
                        for (y = 0; ++y < s; ) {
                            b[x][y] = a[x][y] > 32 ? '0'
                                          : (a[x][y - 1] + a[x][y + 1] + a[x - 1][y] + a[x + 1][y])
                                                % 8 == 5 ? 61 : ' ';
                        }
                    }
                }
                return a;
            };
            char[][] a = null;
            int iMax = Math.min(width, height) / 2 - 1;
            for (int i = 0; i <= iMax; i++) {
                a = f.apply(i);
            }
            for (int col = 0; col < a.length; col++) {
                for (int row = 0; row < a.length; row++) {
                    {
                        if (a[col][row] == '0') {
                            region.setTile(col, row, floorTile);
                        }
                    }
                }
            }
            Util.generateWalls(region, wallTile1, wallTile2);
            region.setTile((int) (width * .9), (int) (height * .9), Tileset.PORTAL);
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
