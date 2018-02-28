package geometry;

public abstract class IPoint {

    public abstract int getX();

    public abstract int getY();

    public int[] toInt() {
        return new int[]{getX(), getY()};
    }

    public static int mailDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    public int mailDistance(int x, int y) {
        return mailDistance(getX(), getY(), x, y);
    }

    public int mailDistance(int[] point) { return mailDistance(getX(), getY(), point[0], point[1]); }

    public int mailDistance(IPoint IPoint) { return mailDistance(getX(), getY(), IPoint.getX(), getY()); }
    

    public static int distanceSq(int x1, int y1, int x2, int y2) {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
    }

    public int distanceSq(int x, int y) {
        return distanceSq(this.getX(), this.getY(), x, y);
    }

    public int distanceSq(int[] point) { return distanceSq(this.getX(), this.getY(), point[0], point[1]); }

    public int distanceSq(IPoint IPoint) {
        return distanceSq(this.getX(), this.getY(), IPoint.getX(), IPoint.getY());
    }


    @Override
    public int hashCode() {
        return getY() * 46340 + getX();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Point) {
            return getX() == ((IPoint) object).getX() && getY() == ((Point) object).getY();
        }
        if (object instanceof int[]) {
            return getX() == ((int[]) object)[0] && getY() == ((int[]) object)[1];
        }
        return false;
    }

}
