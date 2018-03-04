package levelBuilder;

import geometry.IPoint;
import geometry.Line;
import geometry.Point;
import tileEngine.TETile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * TODO Entity Class
 */
public abstract class Entity {
    private Point position;
    private TETile tile = null; //Tileset.FLOWER;
    private int LOSRadius = 15;

    public Entity(Point point) {
        this.position = point;
    }

    public Entity() {

    }

    public abstract void update(World world, double dt);

    public void setPositionRef(Point positionPointer) {
        this.position = positionPointer;
    }

    public abstract Entity getCopy();

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point newPosition) {
        this.position.setLocation(newPosition.getX(), newPosition.getY());
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

    public Set<IPoint> getLOS(World world) {
        List<Point> edgeTiles = new ArrayList<>();
        Set<IPoint> visibleTiles = new HashSet<>();
        int radius = LOSRadius;
        for (int col = -radius; col < radius + 1; col++) {
            edgeTiles.add(Point.newAdd(position, col, radius));
            edgeTiles.add(Point.newAdd(position, col, -radius));
        }
        for (int row = -radius + 1; row < radius; row++) {
            edgeTiles.add(Point.newAdd(position, radius, row));
            edgeTiles.add(Point.newAdd(position, -radius, row));
        }

        for (Point edge : edgeTiles) {
            for (IPoint p : Line.raytrace(getX(), getY(), edge.getX(), edge.getY())) {
                TETile tile = world.getTile(p.getX(), p.getY());
                if (tile == null) {break;}
                visibleTiles.add(p);
                if (tile.isOpaque()) {
                    break;
                }
            }
        }
        return visibleTiles;
    }

    public void move(int x, int y) {
        this.position.add(x, y);
    }

    public void setLOSRadius(int LOSRadius) {
        this.LOSRadius = LOSRadius;
    }
}
