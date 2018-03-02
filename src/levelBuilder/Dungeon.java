package levelBuilder;

import core.Util;
import levelBuilder.World;

public class Dungeon {

    private final long seed;

    public Dungeon(long seed, ) {
        this.seed = seed;
    }



    private World getFloor(int depth, Player player) {
        long floorSeed = Util.cantorPair(seed, depth);

    }
}
