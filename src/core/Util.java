package core;

import java.util.Arrays;
import java.util.List;

public class Util {

    public static long cantorPair(long seed, long id) {
        long pair = (seed + id) * Long.divideUnsigned(seed + id + 1, 2) + seed;
        return pair;
    }

    /*
     * @Source https://stackoverflow.com/questions/4849051/using-contains-on-an-arraylist-with
     * -integer-arrays
     */
    public static boolean isInList(final List<int[]> list, final int[] candidate) {
        return list.stream().anyMatch(a -> Arrays.equals(a, candidate));
    }

    public static int[][] getAdjacentPositions(int[] pos) {
        return new int[][]{
            Util.add(pos, 0, 1), Util.add(pos, -1, 0), Util.add(pos, 0, -1), Util.add(pos, 1, 0)
        };
    }

    public static int[] add(int[] pos, int x, int y) {
        return new int[]{pos[0] + x, pos[1] + y};
    }

}
