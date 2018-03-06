package byog.geometry;

import java.util.ArrayList;

public class Line {

    private Point start, end;


    public static boolean linesIntersect(int x1, int y1, int x2, int y2, int x3, int y3, int x4,
        int y4) {
        return ((relativeCCW(x1, y1, x2, y2, x3, y3) * relativeCCW(x1, y1, x2, y2, x4, y4) <= 0)
                    && (relativeCCW(x3, y3, x4, y4, x1, y1) * relativeCCW(x3, y3, x4, y4, x2, y2)
                            <= 0));
    }

    public static boolean linesIntersect(Line line1, Line line2) {
        return linesIntersect(line1.getX1(), line1.getY1(), line1.getX2(), line1.getY2(),
            line2.getX1(), line2.getY1(), line2.getX2(), line2.getY2());
    }

    /**
     * @Source https://gamedev.stackexchange
     * .com/questions/20103/finding-which-tiles-are-intersected-by-a-line-without
     * -looping-through-all-of-th
     */
    public static ArrayList<ImmutablePoint> raytrace(int x0, int y0, int x1, int y1) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int x = x0;
        int y = y0;
        int n = 1 + dx + dy;
        int xInc = (x1 > x0) ? 1 : -1;
        int yInc = (y1 > y0) ? 1 : -1;
        int error = dx - dy;
        dx *= 2;
        dy *= 2;
        ArrayList<ImmutablePoint> list = new ArrayList();
        for (; n > 0; --n) {
            list.add(ImmutablePoint.valueOf(x, y));
            if (error > 0) {
                x += xInc;
                error -= dy;
            } else {
                y += yInc;
                error += dx;
            }
        }
        return list;
    }

    public static ArrayList<ImmutablePoint> raytrace(Line line) {
        return raytrace(line.getX1(), line.getX2(), line.getY1(), line.getY2());
    }

    public static ArrayList<ImmutablePoint> raytrace(Point pos1, Point pos2) {
        return raytrace(pos1.getX(), pos1.getY(), pos2.getX(), pos2.getY());
    }

    /*
     * @Source Line2D class
     */
    public static int relativeCCW(int x1, int y1, int x2, int y2, int px, int py) {
        x2 -= x1;
        y2 -= y1;
        px -= x1;
        py -= y1;
        int ccw = px * y2 - py * x2;
        if (ccw == 0) {
            // The point is colinear, classify based on which side of
            // the segment the point falls on.  We can calculate a
            // relative value using the projection of px,py onto the
            // segment - a negative value indicates the point projects
            // outside of the segment in the direction of the particular
            // endpoint used as the origin for the projection.
            ccw = px * x2 + py * y2;
            if (ccw > 0) {
                // Reverse the projection to be relative to the original x2,y2
                // x2 and y2 are simply negated.
                // px and py need to have (x2 - x1) or (y2 - y1) subtracted
                //    from them (based on the original values)
                // Since we really want to get a positive answer when the
                //    point is "beyond (x2,y2)", then we want to calculate
                //    the inverse anyway - thus we leave x2 & y2 negated.
                px -= x2;
                py -= y2;
                ccw = px * x2 + py * y2;
                if (ccw < 0) {
                    ccw = 0;
                }
            }
        }
        return (ccw < 0) ? -1 : ((ccw > 0) ? 1 : 0);
    }

    public boolean intersects(Line line) {
        return linesIntersect(this, line);
    }

    public int getX1() {
        return start.getX();
    }

    public int getX2() {
        return end.getX();
    }

    public int getY1() {
        return start.getY();
    }

    public int getY2() {
        return end.getY();
    }

    public Point getP1() {
        return start;
    }

    public Point getp2() {
        return end;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public void setEnd(Point end) {
        this.end = end;
    }
}
