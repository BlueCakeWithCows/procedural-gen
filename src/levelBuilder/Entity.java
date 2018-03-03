package levelBuilder;

import geometry.Point;
import tileEngine.TETile;

/**
 * TODO Entity Class
 */
public abstract class Entity {
    private Point position;
    private TETile tile = null; //Tileset.FLOWER;

    public Entity(Point point) {
        this.position = point;
    }

    public Entity() {

    }


    public void setPositionRef(Point positionPointer) {
        this.position = positionPointer;
    }

    public abstract Entity getCopy();

    public Point getPosition() {
        return position;
    }

    public void setPosition(int x, int y) {
        this.position.setLocation(x, y);
    }

    public TETile getTile() {
        return tile;
    }

    public void setTile(TETile tile) {
        this.tile = tile;
    }

    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }
}
