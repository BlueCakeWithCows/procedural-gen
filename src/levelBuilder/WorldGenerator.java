package levelBuilder;

import Core.MyLogger;

import java.util.Map;
import java.util.Random;

public abstract class WorldGenerator {
    private final Random random;
    private final String label;
    protected Map<String, String> parameters;

    public WorldGenerator(Random random, Map<String, String> parameters) {
        this.label = "";
        this.random = random;
        this.parameters = parameters;
    }

    public WorldGenerator(String label, Random random, Map<String, String> parameters) {
        this.label = label;
        this.random = random;
        this.parameters = parameters;

    }

    public TileRegion genRegion(TileRegion region) {
        return this.genRegion(region, 0);
    }

    public abstract TileRegion genRegion(TileRegion region, double difficulty);

    public int getInt(String key, int def) {
        if (parameters.containsKey(key)) {
            return Integer.parseInt(parameters.get(key));
        }
        MyLogger.log(
            "[" + getLabel() + "]" + " Key:[" + key + "] not found. Using default " + "value" + " ["
                + def + "]");
        return def;
    }

    public boolean getBool(String key, boolean def) {
        if (parameters.containsKey(key)) {
            return Boolean.parseBoolean(parameters.get(key));
        }
        MyLogger.log(
            "[" + getLabel() + "]" + " Key:[" + key + "] not found. Using default " + "value" + " ["
                + def + "]");
        return def;
    }

    public double getDouble(String key, double def) {
        if (parameters.containsKey(key)) {
            return Double.parseDouble(parameters.get(key));
        }
        MyLogger.log(
            "[" + getLabel() + "]" + " Key:[" + key + "] not found. Using default " + "value" + " ["
                + def + "]");
        return def;
    }

    public Random getRandom() {
        return random;
    }

    public String getLabel() {
        return label;
    }
}
