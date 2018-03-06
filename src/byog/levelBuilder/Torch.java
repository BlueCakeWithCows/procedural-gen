package byog.levelBuilder;

import byog.geometry.Point;
import byog.tileEngine.Tileset;

public class Torch extends Entity implements LightSource {
    public Torch(Point p) {
        super(p);
        this.setTile(Tileset.TORCH);
    }

    @Override
    public void update(World world, double dt) {
        if (world.getPlayer().getPosition().equals(this.getPosition())) {
            world.destroy(this);
            world.getPlayer().setLightPoints(110);
        }
    }

    @Override
    public Entity getCopy() {
        throw new RuntimeException("In Torch");
    }

    @Override
    public int getLightValue() {
        return 10;
    }
}
