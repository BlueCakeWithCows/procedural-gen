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

    public Player(Point position) {
        super(position);
    }

    public Player() {
        this.setTile(Tileset.PLAYER);

    }

    @Override
    public void update(World world, double dt) {
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

    private Set<IPoint> lastSeen = new HashSet<>();

    public boolean tryMove(char c, World world) {
        Point newPosition = new Point(this.getPosition());
        if (c == 'w') {newPosition.add(0, 1);}
        if (c == 'a') {newPosition.add(-1, 0);}
        if (c == 's') {newPosition.add(0, -1);}
        if (c == 'd') {newPosition.add(1, 0);}
        if (!newPosition.equals(this.getPosition())) {
            TETile target = world.getTile(newPosition.getX(), newPosition.getY());
            if (target.getType().equals(TileType.FLOOR)) {
                world.getPlayer().setPosition(newPosition);
                return true;
            }
        }

        return false;
    }

    private void updateVision(World world) {
        Set<IPoint> LOS = this.getLOS(world);
        lastSeen.removeAll(LOS);
        for (IPoint p : lastSeen) {
            world.setVision(p.getX(), p.getY(), false);
        }
        for (IPoint p : LOS) {
            world.setVision(p.getX(), p.getY(), true);
        }
        this.lastSeen = LOS;
    }

    @Override
    public int getLightValue() {
        return 12;
    }
}
