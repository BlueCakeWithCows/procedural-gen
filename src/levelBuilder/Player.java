package levelBuilder;

import geometry.IPoint;
import geometry.Point;
import tileEngine.TETile;
import tileEngine.TileType;
import tileEngine.Tileset;

import java.util.HashSet;
import java.util.Set;

/**
 * TODO Player class
 */
public class Player extends Entity implements LightSource {

    private Set<IPoint> lastSeen = new HashSet<>();
    private volatile int lightPoints;
    private int health;

    public Player(Point position) {
        super(position);
    }

    public Player() {
        this.setTile(Tileset.PLAYER);
        this.setLOSRadius(30);
        lightPoints = 100;
        health = 100;
    }

    @Override
    public void update(World world, double dt) {
        if (dt == 0) {
            updateVision(world);
            return;
        }
        lightPoints -= 1;
        updateVision(world);
    }

    public boolean[] getPossibleMoves(World world) {
        boolean[] wasdMoves = new boolean[4];
        Point[] possibleMoves = getPosition().getAdjacent();
        for (int i = 0; i < 4; i++) {
            TETile potentialTarget =
                world.getTile(possibleMoves[i].getX(), possibleMoves[i].getY());
            if (potentialTarget.getType().equals(TileType.FLOOR)) {
                wasdMoves[i] = true;
            }
        }
        return wasdMoves;
    }

    @Override
    public Entity getCopy() {
        throw new RuntimeException("Calling getCopy on Player object. Why?");
    }

    public boolean tryMove(char c, World world) {
        Point newPosition = new Point(this.getPosition());
        if (c == 'w') {newPosition.add(0, 1);}
        if (c == 'a') {newPosition.add(-1, 0);}
        if (c == 's') {newPosition.add(0, -1);}
        if (c == 'd') {newPosition.add(1, 0);}
        if (!newPosition.equals(this.getPosition())) {
            TETile target = world.getTile(newPosition.getX(), newPosition.getY());
            if (!target.getType().isSolid()) {
                world.getPlayer().setPosition(newPosition);
                return true;
            }
            reduceHealth();
        }
        return false;
    }

    private void updateVision(World world) {
        Set<IPoint> los = this.getLOS(world);
        lastSeen.removeAll(los);
        for (IPoint p : lastSeen) {
            world.setVision(p.getX(), p.getY(), false);
        }
        for (IPoint p : los) {
            if (world.getLightLevel(p.getX(), p.getY()) > 0) {
                world.setVision(p.getX(), p.getY(), true);
            }
        }
        this.lastSeen = los;
    }

    @Override
    public int getLightValue() {
        return Math.max(1, lightPoints / 10);
    }

    public void addLightPoints(int i) {
        this.lightPoints = Math.max(0, lightPoints);
        this.lightPoints += i;
    }

    public void setLightPoints(int i) {
        this.lightPoints = i;
    }

    public int getLightPoints() {
        return this.lightPoints;
    }

    private void reduceHealth() {
        health -= 5;
    }

    public int getHealth() {
        return health;
    }

}
