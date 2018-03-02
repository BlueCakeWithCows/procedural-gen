package levelBuilder;

import byog.Core.Generators.LevelFunctions;
import byog.Core.Generators.RegionFunctions;
import byog.Core.Generators.RegionUtils;
import byog.Core.Generators.RoomGenerator;
import byog.Core.Level.LevelBuilder;
import byog.Core.World.Zombie;
import byog.TileEngine.TileType;
import byog.TileEngine.Tileset;
import levelBuilder.ugly.LevelBuilder;
import levelBuilder.ugly.RegionFunctions;
import levelBuilder.ugly.RegionUtils;
import levelBuilder.ugly.TileRegion;

import java.util.Map;
import java.util.Random;

public class DungeonGenerator extends WorldGenerator {

    private double minDensity, maxDensity;
    private double nodeDensity;
    private int nodeMinSpacing;
    private int nodeDistanceFromWalls;
    private double gaussianRoomspaceDeviation;
    private double gaussianRoomspaceAverage;
    private int roomspaceMinRadius;
    private int roomspaceMaxRadius;
    private int[] roomFrequencies;
    private WorldGenerator[] rooms;
    private int hallwayNodeDistance;
    private double hallwayDensity;
    private double puddleDensity;
    private boolean gaussRoomspace;
    private Entity[] monsters;
    private int[] monsterFrequency;


    public DungeonGenerator(String dungeonGen1, Random random, Map<String, String> parameters) {
        super(dungeonGen1, random, parameters);
        this.setParameters(this.parameters);
    }

    public void setParameters(Map<String, String> param) {
        if (param != null) {
            this.parameters = param;
        } else {
            this.parameters = Map.ofEntries();
        }
        this.minDensity = getDouble("minDensity", .2d);
        this.maxDensity = getDouble("maxDensity", 1d);

        this.nodeDensity = getDouble("nodeDensity", 05);
        this.nodeMinSpacing = getInt("nodeMinSpacing", 8);
        this.nodeDistanceFromWalls = getInt("nodeMinDistanceFromWalls", 2);
        this.gaussianRoomspaceAverage = getDouble("gaussianRoomspaceAverage", 9);
        this.gaussianRoomspaceDeviation = getDouble("gaussianRoomspaceDeviation", 9);

        this.roomspaceMinRadius = getInt("roomspaceMinRadius", 2);
        this.roomspaceMaxRadius = getInt("roomspaceMaxRadius", 4);


        this.rooms = new WorldGenerator[]{
            new RoomGenerator.RectangleRoomGenerator(getRandom(), parameters),
            new RoomGenerator.HexagonRoomGenerator(getRandom(), parameters)
        };
        this.roomFrequencies = new int[]{
                getInt("rectangleRoomFrequency", 50), getInt("roomHexFrequency", 50)
        };

        this.monsters = new Entity[]{new Zombie()};
        this.monsterFrequency = new int[]{getInt("zombieFrequency", 30)};

        this.hallwayDensity = getDouble("hallwayDensity", 3.0);
        this.hallwayNodeDistance = getInt("hallwayNodeDistance", 3);
        this.puddleDensity = getDouble("puddleDensity", 0.0);
        this.gaussRoomspace = getBool("gaussRoomspace", false);
    }


    @Override
    public TileRegion genRegion(TileRegion r, double difficulty) {
        LevelBuilder region = (LevelBuilder) r;
        double currentDensity;
        do {
            LevelFunctions.clear(region);
            LevelFunctions.populateNodes(getRandom(), region, nodeDensity, nodeMinSpacing,
                    nodeDistanceFromWalls
            );
            generateRoomSpace(r, difficulty);
            LevelFunctions.fillRoomSpace(getRandom(), region, rooms, roomFrequencies, difficulty);
            LevelFunctions.generateHallways(getRandom(), region, hallwayDensity,
                    hallwayNodeDistance
            );
            LevelFunctions.prune(getRandom(), region);
            RegionFunctions.generateWalls(region, Tileset.WALL1, Tileset.WALL1FRONT);
            RegionFunctions.generatePuddles(getRandom(), region, puddleDensity, Tileset.WATER);
            RegionFunctions.mark(region, region.getNodes(), Tileset.TREE);
            RegionFunctions.centerRegion(region);

            currentDensity = 1d - RegionUtils.getDensity(region, TileType.NOTHING);
        } while (currentDensity < minDensity || currentDensity > maxDensity);
        return region;
    }

    private void generateRoomSpace(TileRegion region, double difficulty) {
        if (gaussRoomspace) {
            LevelFunctions.generateRectangularRoomSpaceGaussian(getRandom(),
                    (LevelBuilder) region.getShrunkRegion(1), gaussianRoomspaceAverage,
                    gaussianRoomspaceDeviation
            );
        } else {
            LevelFunctions.generateRectangularRoomSpace(getRandom(),
                    (LevelBuilder) region.getShrunkRegion(1), roomspaceMinRadius, roomspaceMaxRadius
            );
        }
    }
}
