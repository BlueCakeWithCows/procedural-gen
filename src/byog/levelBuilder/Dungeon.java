package byog.levelBuilder;

import byog.Core.Util;

public class Dungeon {

    private final long seed;
    private final GeneratorBag set;
    private int depth;

    public Dungeon(long seed, GeneratorBag set) {
        this.seed = seed;
        this.set = set;
        depth = 0;
    }


    public World getFloor(int d, Player player) {
        long floorSeed = Util.cantorPair(seed, d);
        return set.generate(Util.cantorPair(seed, d), d, player);
    }

    public World getFloor(Player player) {
        depth++;
        long floorSeed = Util.cantorPair(seed, depth);
        return set.generate(Util.cantorPair(seed, depth), depth, player);
    }

    public int getDepth() {
        return depth;
    }
}
