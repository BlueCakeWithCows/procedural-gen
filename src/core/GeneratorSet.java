package Core;

import levelBuilder.Generator;
import levelBuilder.GeneratorBag;
import levelBuilder.Player;
import levelBuilder.World;
import levelBuilder.generators.DungeonGenerator;
import tileEngine.Tileset;

import java.util.Map;

import static java.util.Map.entry;

public class GeneratorSet {
    public static GeneratorBag defaultSet = new GeneratorBag() {
        @Override
        public World generate(long l, int depth, Player player) {
            switch (depth) {
                case 1:
                    break;
                case 2:
                    break;
                default:
                    return dungeonGenerator1.generate(l, player,
                        Map.ofEntries(entry("width", 100), entry("height", 100),
                            entry("floor_tile", Tileset.FLOOR), entry("wall_tile1", Tileset.WALL),
                            entry("wall_tile2", Tileset.WALL2)));
            }

            return dungeonGenerator1.generate(l, player,
                Map.ofEntries(entry("width", 100), entry("height", 100),
                    entry("floor_tile", Tileset.FLOOR), entry("wall_tile1", Tileset.WALL),
                    entry("wall_tile2", Tileset.WALL2)));
        }
    };

    private static final Generator dungeonGenerator1 =
        new DungeonGenerator("Default Generator", .3, 1, .01, 12, 2.5, 4, 3, 10,
            new int[]{100, 100}, 50, 50);

}
