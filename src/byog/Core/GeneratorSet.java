package byog.Core;

import byog.levelBuilder.Generator;
import byog.levelBuilder.GeneratorBag;
import byog.levelBuilder.Player;
import byog.levelBuilder.World;
import byog.levelBuilder.generators.CavernGenerator;
import byog.levelBuilder.generators.DiagonalRoom;
import byog.levelBuilder.generators.DungeonGenerator;
import byog.levelBuilder.generators.QuantumDrunkard;
import byog.tileEngine.Tileset;

import java.util.Map;

import static java.util.Map.entry;

public class GeneratorSet {
    private static Generator dungeonGenerator1 =
        new DungeonGenerator("Default Generator", .3, .7, .005, 14, 2.5,  3, 10);
    private static Generator sparseGenerator1 =
        new DungeonGenerator("Sparse Generator", .2, .5, .005, 20, 1.5, 5, 10);
    private static Generator caveGenerator1 =
        new CavernGenerator("Cave Generator", 100, 100, .01, 1, 3);
    private static Generator quantumWalk = new QuantumDrunkard("Quantum Generator", 100, 100);
    private static Generator diagonal = new DiagonalRoom("Diagonal Room Generator", 100, 100);
    public static final GeneratorBag DEFAULT_SET = new GeneratorBag() {
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

                case 4:
                    return diagonal.generate(l, player,
                        Map.ofEntries(entry("width", 100), entry("height", 100),
                            entry("floor_tile", Tileset.FLOOR), entry("wall_tile1", Tileset.WALL),
                            entry("wall_tile2", Tileset.WALL2)));

                case 5:
                    return quantumWalk.generate(l, player,
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

}
