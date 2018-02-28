package geometry;


public class ImmutablePoint extends IPoint {

    private final int x;
    private final int y;

    private ImmutablePoint(int x, int y) {
        this.x = x;
        this.y = y;
        Integer i;
    }

    public ImmutablePoint add(int x, int y) {
        return null;
    }

    public void add(int[] point) { add(point[0], point[1]); }

    public void add(ImmutablePoint point) { add(point.getX(), point.getY()); }

    public static int distanceSq(int x1, int y1, int x2, int y2) {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
    }

    public int distanceSq(int x, int y) {
        return ImmutablePoint.distanceSq(this.x, this.y, x, y);
    }

    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }


    public static ImmutablePoint valueOf(int x, int y) {
        if (x >= PointCache.low && x <= PointCache.high && y >= PointCache.low && y <= PointCache.high) {
            return PointCache.cache[y * PointCache.size + x];
        }
        return new ImmutablePoint(x, y);
    }

    @Override
    public int hashCode() {
        return y * PointCache.size + x;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ImmutablePoint) {
            return x == ((ImmutablePoint) object).getX() && y == ((ImmutablePoint) object).getY();
        }
        if (object instanceof int[]) {
            return x == ((int[]) object)[0] && y == ((int[]) object)[1];
        }
        return false;
    }

    /**
     * The cache is initialized on first usage.
     * Currently set to not be used :)
     */

    private static class PointCache {
        static final int low = 0;
        static final int high = -1;
        static final int size = high - low + 1;
        static final ImmutablePoint cache[];

        static {
            // high value may be configured by property
            cache = new ImmutablePoint[size * size];
            for (int i = 0; i < cache.length; i++) {
                cache[i] = new ImmutablePoint(i % size, i / size);
            }
        }

        private PointCache() {}
    }

}
