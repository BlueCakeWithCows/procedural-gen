package geometry;

public class Rectangle {
    private int x1;
    private int y1;
    private int width;
    private int height;


    public Rectangle(int x1, int y1, int width, int height) {
        this.x1 = x1;
        this.y1 = y1;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getX2() {
        return x1 + width;
    }

    public int getY2() {
        return y1 + height;
    }

    public int getY() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void center(Point position) {
        this.x1 = position.getX() - width / 2;
        this.y1 = position.getY() - height / 2;
    }

    public void bound(int minX, int minY, int maxX, int maxY) {
        if (x1 < minX) {
            x1 = minX;
        }
        if (y1 < minY) {
            y1 = minY;
        }
        if (getX2() >= maxX) {
            x1 = maxX - width;
        }
        if (getY2() >= maxY) {
            y1 = maxY - height;
        }
    }

    public void boundCenter(Point position, int minX, int minY, int maxX, int maxY) {
        int newX = position.getX() - width / 2;
        int newY = position.getY() - height / 2;

        if (newX < minX) {
            newX = minX;
        }
        if (newY < minY) {
            newY = minY;
        }
        if (newX + width >= maxX) {
            newX = maxX - width;
        }
        if (newY + height >= maxY) {
            newY = maxY - height;
        }
        this.y1 = newY;
        this.x1 = newX;

    }
}
