package geometry;


public class Point extends IPoint {

    private int x;
    private int y;
    private final int MAX_ROW_SIZE = 46340; //Square Root of MAX_INT

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point point) {
        this(point.x, point.y);
    }

    public Point(int[] point) {
        this(point[0], point[1]);
    }


    public static Point newAdd(Point point, int x, int y) {
        return new Point(point.x + x, point.y + y);
    }

    public void add(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public void add(int[] point) { add(point[0], point[1]); }

    public void add(IPoint IPoint) { add(IPoint.getX(), IPoint.getY()); }
    
    public static Point[] getAdjacent(Point pos) {
        return new Point[]{newAdd(pos, 0, 1), newAdd(pos, -1, 0), newAdd(pos, 0, -1), newAdd(pos, 1, 0)};
    }

    public int[] toInt() {
        return new int[]{x, y};
    }

    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }

}
