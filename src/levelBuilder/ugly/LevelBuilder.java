package levelBuilder.ugly;

import byog.Core.Game;
import byog.Core.Util.Util;
import byog.TileEngine.TETile;
import byog.TileEngine.TileType;
import byog.TileEngine.Tileset;
import levelBuilder.Entity;
import levelBuilder.Player;
import levelBuilder.Room;
import levelBuilder.World;
import levelBuilder.WorldGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Top level class for managing floor generation. Simply create and call
 * build() to return a 'world' - a functioning TE grid and associated entities.
 */
public class LevelBuilder extends TileRegion {

    private List<Room> rooms = new ArrayList<>();
    private List<int[]> nodes = new ArrayList<>();
    private int[] spawn = new int[]{0, 0};
    private double difficulty;
    private List<Entity> entities;
    private int[] position;
    private Player player;

    public LevelBuilder(int[] pos, Player player) {
        super(createEmptyWorld(Game.WIDTH, Game.HEIGHT));
        setEntities(new ArrayList<Entity>());
        this.setPosition(pos);
        this.setOffsetX(0);
        this.setOffsetY(0);
    }

    public LevelBuilder(int[] pos, int width, int height, Player player) {
        super(createEmptyWorld(width, height));
        setEntities(new ArrayList<Entity>());
        this.setPosition(pos);
        this.setOffsetX(0);
        this.setOffsetY(0);
    }

    public LevelBuilder(int[] pos) {
        this(pos, null);
    }


    public LevelBuilder(LevelBuilder levelBuilder, int i, int i1, int width, int height) {
        super(levelBuilder.getGrid(), i, i1, width, height);
        this.setPosition(levelBuilder.getPosition());
        this.setPlayer(levelBuilder.getPlayer());
        this.setNodes(levelBuilder.getNodes());
        this.setSpawn(levelBuilder.getSpawn());
        this.setDifficulty(levelBuilder.getDifficulty());
        this.setEntities(levelBuilder.getEntities());
        this.setRooms(levelBuilder.getRooms());
    }

    public static TETile[][] createEmptyWorld(int width, int height) {
        TETile[][] world = new TETile[width][height];
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        return world;
    }


    public World build() {
        return build(getGenerator());
    }

    public World build(WorldGenerator generator) {
        this.setDifficulty(Util.getDistance(getPosition()));
        generator.genRegion(this, getDifficulty());
        if (getPlayer() == null) {
            setPlayer(new Player());
        }
        getPlayer().setPosition(getNodes().get(0));
        getEntities().add(getPlayer());

        int[][] lightMap = getLightMap();

        World world = new World(getGrid(), getEntities(), getPlayer(), lightMap);
        return world;
    }

    private WorldGenerator getGenerator() {
        return LevelGeneratorSet.getDungeonGenerator1();
    }

    public int[] getPosition() {
        return position;
    }

    public void setPosition(int[] position) {
        this.position = position;
    }

    public void addRoom(Room room) {
        this.getRooms().add(room);
    }

    public void addNode(int[] ints) {
        this.getNodes().add(ints);
    }

    public TileRegion getSubRegionByDimension(int x1, int y1, int tWidth, int tHeight) {
        if (x1 >= getWidth()) {
            x1 = getWidth() - 1;
        }
        if (y1 >= getHeight()) {
            y1 = getHeight() - 1;
        }
        if (x1 < 0) {
            x1 = 0;
        }
        if (y1 < 0) {
            y1 = 0;
        }
        if (tWidth < 0) {
            tWidth = 0;
        }
        if (tHeight < 0) {
            tHeight = 0;
        }
        if (x1 + tWidth > getWidth()) {
            tWidth = getWidth() - x1;
        }
        if (y1 + tHeight > getHeight()) {
            tHeight = getHeight() - y1;
        }
        return new LevelBuilder(this, getOffsetX() + x1, getOffsetY() + y1, tWidth, tHeight);
    }

    /**
     * Returns true if tile as location is floor and no entities already there occupy
     *
     * @param pos
     * @return
     */
    public boolean isOpen(int[] pos) {
        if (this.getTile(pos) != null && this.getTile(pos).getType() == TileType.FLOOR) {
            for (Entity e : getEntities()) {
                if (e.getPosition().equals(pos)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public void addEntity(Entity e) {
        this.getEntities().add(e);
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<int[]> getNodes() {
        return nodes;
    }

    public void setNodes(List<int[]> nodes) {
        this.nodes = nodes;
    }

    public int[] getSpawn() {
        return spawn;
    }

    public void setSpawn(int[] spawn) {
        this.spawn = spawn;
    }

    public double getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(double difficulty) {
        this.difficulty = difficulty;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
