package levelBuilder;

import Core.Game;
import geometry.Point;
import tileEngine.TETile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * TODO World class : feel free to edit parameters on update and draw
 */
public class World {

    private Player player;
    private List<Entity> entities;
    private TileRegion region;
    private int[][] staticLightMap;
    private int[][] dynamicLightMap;
    private byte[][] fogLightMap;
    private TETile[][] tiles;
    private int[][] totalLightLevel = null;
    private ArrayList<Entity> destroyEntityList;

    public World(TETile[][] tiles, List<Entity> entities, Player player) {
        this(tiles, entities, player, new int[tiles.length][tiles[0].length]);
        for (int row = 0; row < getRegion().getHeight(); row++) {
            for (int col = 0; col < getRegion().getWidth(); col++) {
                getStaticLightMap()[col][row] = 7;
            }
        }
        if (!Game.RENDER_MAP) { this.recalculateStaticLightMap(); }
        this.recalculateDynamicLightMap();
        this.calculateTotalLightLevel();
    }

    public World(TETile[][] tiles, List<Entity> entities, Player player, int[][] staticLightMap) {
        this.setTiles(tiles);
        this.setEntities(entities);
        this.setPlayer(player);
        this.setRegion(new TileRegion(tiles));
        this.setDynamicLightMap(new int[getRegion().getWidth()][getRegion().getHeight()]);
        this.setStaticLightMap(staticLightMap);
        this.fogLightMap = (new byte[getRegion().getWidth()][getRegion().getHeight()]);
        this.destroyEntityList = new ArrayList<>();
        for (int row = 0; row < getRegion().getHeight(); row++) {
            for (int col = 0; col < getRegion().getWidth(); col++) {
                getFogLightMap()[col][row] = 0;
            }
        }
        this.calculateTotalLightLevel();
    }

    public void recalculateStaticLightMap() {
        staticLightMap = region.getLightMap();
    }

    public void recalculateDynamicLightMap() {
        int[][] newMap = new int[region.getWidth()][region.getHeight()];
        List<Point> points = new ArrayList<>();
        for (Entity e : getEntities()) {
            Point pos = e.getPosition();
            if (e instanceof LightSource) {
                newMap[pos.getX()][pos.getY()] = ((LightSource) e).getLightValue();
                points.add(pos);
            }
        }
        dynamicLightMap = getLightMap(newMap, points);
    }

    public int[][] getTotalLightLevel() {
        return totalLightLevel;
    }

    public void setTotalLightLevel(int[][] totalLightLevel) {
        this.totalLightLevel = totalLightLevel;
    }

    //can make intsream parallel... but may be slower/
    public void calculateTotalLightLevel() {
        int[][] result = new int[getRegion().getWidth()][getRegion().getHeight()];
        IntStream.range(0, result.length).forEach(x -> Arrays.setAll(result[x],
            y -> Math.min(11, getStaticLightMap()[x][y] + getDynamicLightMap()[x][y])));
        this.setTotalLightLevel(result);
    }

    public int getVisibleLightLevel(int x, int y) {
        switch (getFogLightMap()[x][y]) {
            case 2:
                return Math.max(1, getLightLevel(x, y));
            case 1:
                return 1;
            default:
                return 0;
        }
    }

    public int[][] getVisibleLightLevels() {
        int[][] result = new int[getRegion().getWidth()][getRegion().getHeight()];
        IntStream.range(0, result.length)
            .forEach(x -> Arrays.setAll(result[x], y -> getVisibleLightLevel(x, y)));
        return result;
    }

    public void update(double dt) {
        entities.removeAll(destroyEntityList);
        destroyEntityList.clear();
        this.recalculateDynamicLightMap();
        this.calculateTotalLightLevel();
        for (Entity e : entities) {
            e.update(this, dt);
        }
    }


    public int getLightLevel(int i, int i1) {
        return getTotalLightLevel()[i][i1];
    }

    /**
     * Sets whether or not player can currently see a tile. Automatically handles fog of war
     * affects;
     * that is if setting to false after true it keeps track that tile has been seen before.
     *
     * @param x
     * @param y
     * @param canSee
     */
    public void setVision(int x, int y, boolean canSee) {
        if (!region.isValid(x, y)) {
            return;
        }
        if (canSee) {
            getFogLightMap()[x][y] = 2;
        } else {
            if (getFogLightMap()[x][y] == 2) {
                getFogLightMap()[x][y] = 1;
            }
        }
    }

    public Entity getEntityAt(int x, int y) {
        return getEntityAt(new Point(x, y));
    }

    public Entity getEntityAt(Point pos) {
        for (Entity e : entities) {
            if (e.getPosition().equals(pos)) {
                return e;
            }
        }
        return null;
    }

    public boolean getIsVisible(int x, int y) {
        return getFogLightMap()[x][y] == 2;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public TETile getTile(int x, int y) {
        return region.getTile(x, y);
    }

    public void setTile(int x, int y, TETile tile) {
        region.setTile(x, y, tile);
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public TileRegion getRegion() {
        return region;
    }

    public void setRegion(TileRegion region) {
        this.region = region;
    }

    public int[][] getStaticLightMap() {
        return staticLightMap;
    }

    public void setStaticLightMap(int[][] staticLightMap) {
        this.staticLightMap = staticLightMap;
    }

    public int[][] getDynamicLightMap() {
        return dynamicLightMap;
    }

    public void setDynamicLightMap(int[][] dynamicLightMap) {
        this.dynamicLightMap = dynamicLightMap;
    }

    public byte[][] getFogLightMap() {
        return fogLightMap;
    }

    public TETile[][] getTiles() {
        return tiles;
    }

    public void setTiles(TETile[][] tiles) {
        this.tiles = tiles;
    }

    public int getWidth() {
        return region.getWidth();
    }

    public int getHeight() {
        return region.getHeight();
    }

    private int[][] getLightMap(int[][] ray, List<Point> startingPoints) {
        List<Point> points = new ArrayList(startingPoints);
        while (!points.isEmpty()) {
            List<Point> newNodes = new ArrayList<>();
            for (Point node : points) {
                for (Point pos : Point.getAdjacent(node)) {
                    if (pos.getX() >= 0 && pos.getX() < ray.length && pos.getY() >= 0
                            && pos.getY() < ray[0].length) {
                        if (ray[pos.getX()][pos.getY()] + 1 < ray[node.getX()][node.getY()]
                                && 1 < ray[node.getX()][node.getY()]) {
                            ray[pos.getX()][pos.getY()] = ray[node.getX()][node.getY()] - 1;
                            if (!getTile(pos.getX(), pos.getY()).getType().isOpaque()) {
                                newNodes.add(pos);
                            }
                        }
                    }
                }
            }
            points = newNodes;
        }
        return ray;
    }

    public void destroy(Entity e) {
        this.destroyEntityList.add(e);
    }

    public TETile getTile(Point position) {
        return getTile(position.getX(), position.getY());
    }
}
