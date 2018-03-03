package levelBuilder;

import Core.Util;

public class Dungeon {

    private final long seed;
    private final GeneratorBag set;
    private int depth;

    public Dungeon(long seed, GeneratorBag set) {
        this.seed = seed;
        this.set = set;
        depth = 0;
    }


    public World getFloor(int depth, Player player) {
        long floorSeed = Util.cantorPair(seed, depth);
        return set.generate(Util.cantorPair(seed, depth), player);
    }

    public World getFloor(Player player) {
        depth++;
        long floorSeed = Util.cantorPair(seed, depth);
        return set.generate(Util.cantorPair(seed, depth), player);
    }

    private int getDepth() {
        return depth;
    }
}
