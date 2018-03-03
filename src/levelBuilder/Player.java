package levelBuilder;

import geometry.Point;
import tileEngine.TETile;
import tileEngine.TileType;
import tileEngine.Tileset;

/**
 * TODO Player class
 */
public class Player extends Entity {

    public Player(Point position) {
        super(position);
    }

    public Player() {
        this.setTile(Tileset.PLAYER);

    }

    public boolean[] getPossibleMoves(World world) {
        boolean[] wasdMoves = new boolean[4];
        Point[] possibleMoves = getPosition().getAdjacent();
        for (int i = 0; i < 4; i++) {
            TETile potentialTarget = world
                                         .getTile(possibleMoves[i].getX(), possibleMoves[i].getY());
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
            if (target.getType().equals(TileType.FLOOR)) {
                world.getPlayer().setPosition(newPosition);
                return true;
            }
        }
        return false;
    }
}
