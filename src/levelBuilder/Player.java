package levelBuilder;

import geometry.Point;
import tileEngine.TETile;
import tileEngine.TileType;

/**
 * TODO Player class
 */
public class Player extends Entity {
    public Player(Point position) {
        super(position);
    }

    public Player() {

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
}
