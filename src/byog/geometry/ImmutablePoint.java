package byog.geometry;


public class ImmutablePoint extends IPoint {

    private final int x;
    private final int y;

    private ImmutablePoint(int x, int y) {
        this.x = x;
        this.y = y;
        Integer i;
    }

    public static int distanceSq(int x1, int y1, int x2, int y2) {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
    }

    public static ImmutablePoint valueOf(int x, int y) {
        if (x >= PointCache.LOW && x <= PointCache.HIGH && y >= PointCache.LOW
                && y <= PointCache.HIGH) {
            return PointCache.CACHE[y * PointCache.SIZE + x];
        }
        return new ImmutablePoint(x, y);
    }

    public ImmutablePoint add(int x1, int y1) {
        return null;
    }

    public void add(int[] point) {
        add(point[0], point[1]);
    }

    public void add(ImmutablePoint point) {
        add(point.getX(), point.getY());
    }

    public int distanceSq(int x1, int y1) {
        return ImmutablePoint.distanceSq(this.x, this.y, x1, y1);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int hashCode() {
        return y * PointCache.SIZE + x;
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

    @Override
    public Point[] getAdjacent() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * The CACHE is initialized on first usage.
     * Currently set to not be used :)
     */

    private static class PointCache {
        static final int LOW = 0;
        static final int HIGH = -1;
        static final int SIZE = HIGH - LOW + 1;
        static final ImmutablePoint[] CACHE;

        static {
            // HIGH value may be configured by property
            CACHE = new ImmutablePoint[SIZE * SIZE];
            for (int i = 0; i < CACHE.length; i++) {
                CACHE[i] = new ImmutablePoint(i % SIZE, i / SIZE);
            }
        }

        private PointCache() {
        }
    }

}
