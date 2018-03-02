package levelBuilder.ugly;

import levelBuilder.WorldGenerator;

import java.util.Map;
import java.util.Random;

public class RoomGenerator {

    public static class RectangleRoomGenerator extends WorldGenerator {
        public RectangleRoomGenerator(Random random, Map<String, String> parameters) {
            super(random, parameters);
        }

        @Override
        public TileRegion genRegion(TileRegion region, double difficulty) {
            region.fill(Tileset.FLOOR);
            return region;
        }
    }

    public static class HexagonRoomGenerator extends WorldGenerator {
        public HexagonRoomGenerator(Random random, Map<String, String> parameters) {
            super(random, parameters);
        }

        @Override
        public TileRegion genRegion(TileRegion region, double difficulty) {
            //TODO set doorways
            //TODO this doesn't work

            double size = Math.min((double) (region.getWidth()) / 2.0 - 2,
                (double) region.getHeight() / 2.0
            );
            if (size < 3) {
                return null;
            } else {
                TileRegion.fillHex(region, new int[]{0, 0}, (int) size, Tileset.FLOOR);
                return region;
            }
        }
    }
}
