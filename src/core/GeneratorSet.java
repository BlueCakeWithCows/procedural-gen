package Core;

import levelBuilder.Generator;
import levelBuilder.GeneratorBag;
import levelBuilder.Player;
import levelBuilder.World;
import levelBuilder.generators.CavernGenerator;
import levelBuilder.generators.DungeonGenerator;
import levelBuilder.generators.MikaelaGenerator;
import levelBuilder.generators.QuantumDrunkard;
import tileEngine.Tileset;

import java.util.Map;

import static java.util.Map.entry;

public class GeneratorSet {
    public static GeneratorBag defaultSet = new GeneratorBag() {
        @Override
        public World generate(long l, int depth, Player player) {
            switch (depth) {
                case 1:
                    return sparseGenerator1.generate(l, player,
                        Map.ofEntries(entry("width", 100), entry("height", 100),
                            entry("floor_tile", Tileset.FLOOR), entry("wall_tile1", Tileset.WALL),
                            entry("wall_tile2", Tileset.WALL2)));
                case 2:
                    return dungeonGenerator1.generate(l, player,
                        Map.ofEntries(entry("width", 100), entry("height", 100),
                            entry("floor_tile", Tileset.FLOOR), entry("wall_tile1", Tileset.WALL),
                            entry("wall_tile2", Tileset.WALL2)));
                case 3:
                    return caveGenerator1.generate(l, player,
                        Map.ofEntries(entry("width", 100), entry("height", 100),
                            entry("floor_tile", Tileset.FLOOR), entry("wall_tile1", Tileset.WALL),
                            entry("wall_tile2", Tileset.WALL2)));
                case 5:
                    return quantumWalk.generate(l, player,
                        Map.ofEntries(entry("width", 100), entry("height", 100),
                            entry("floor_tile", Tileset.FLOOR), entry("wall_tile1", Tileset.WALL),
                            entry("wall_tile2", Tileset.WALL2)));
                case 100:
                    return mazeGenerator1.generate(l, player,
                        Map.ofEntries(entry("width", 100), entry("height", 100),
                            entry("floor_tile", Tileset.FLOOR), entry("wall_tile1", Tileset.WALL),
                            entry("wall_tile2", Tileset.WALL2)));

                default:
                    return dungeonGenerator1.generate(l, player,
                        Map.ofEntries(entry("width", 100), entry("height", 100),
                            entry("floor_tile", Tileset.FLOOR), entry("wall_tile1", Tileset.WALL),
                            entry("wall_tile2", Tileset.WALL2)));
            }
        }
    };

    private static final Generator dungeonGenerator1 =
        new DungeonGenerator("Default Generator", .3, .7, .005, 14, 2.5, 3, 3, 10,
            new int[]{100, 100}, 100, 100);

    private static final Generator sparseGenerator1 =
        new DungeonGenerator("Sparse Generator", .2, .5, .005, 20, 1.5, 5, 5, 10,
            new int[]{100, 100}, 100, 100);
    private static final Generator caveGenerator1 =
        new CavernGenerator("Cave Generator", 100, 100, .01, 1, 3);

    private static final Generator mazeGenerator1 =
        new MikaelaGenerator("Maze Generator", 100, 100);

    private static final Generator quantumWalk = new QuantumDrunkard("Quantum Generator", 100, 100);
}
