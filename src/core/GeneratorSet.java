package core;

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
        public World generate(long l, Player player) {
            DungeonGenerator generator = new DungeonGenerator("Default Generator", .3, 7, .01, 12,
                2, 4, 3, 10, new int[]{100, 100}, 50, 50);
            return generator.generate(l, player,
                Map.ofEntries(entry("width", 200), entry("height", 200),
                    entry("floor_tile", Tileset.FLOOR), entry("wall_tile1", Tileset.WALL),
                    entry("wall_tile2", Tileset.WALL2)));
        }
    };
    /**
     * int width = (Integer) param.getOrDefault("width", this.width);
     int height = (Integer) param.getOrDefault("height", this.height);

     TETile floorTile = (TETile) param.get("floor_tile");
     TETile wallTile1 = (TETile) param.get("wall_tile1");
     TETile wallTile2 = (TETile) param.get("wall_tile2");
     */
}
