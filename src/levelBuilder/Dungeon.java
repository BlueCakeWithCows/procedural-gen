package levelBuilder;

import core.Util;

public class Dungeon {

    private final long seed;
    private final GeneratorBag set;

    public Dungeon(long seed, GeneratorBag set) {
        this.seed = seed;
        this.set = set;
    }


    private World getFloor(int depth, Player player) {
        long floorSeed = Util.cantorPair(seed, depth);
        return set.generate(Util.cantorPair(seed, depth), player);
    }
}
