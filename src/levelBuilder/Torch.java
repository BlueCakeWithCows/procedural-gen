package levelBuilder;

import geometry.Point;
import tileEngine.Tileset;

public class Torch extends Entity implements LightSource {



    public Torch(Point p) {
        super(p);
        this.setTile(Tileset.TORCH);
    }

    @Override
    public void update(World world, double dt) {
        if (world.getPlayer().getPosition().equals(this.getPosition())) {
            world.destroy(this);
            world.getPlayer().setLightPoints(100);
        }
    }

    @Override
    public Entity getCopy() {
        throw new RuntimeException("Fuck off: In Torch");
    }

    @Override
    public int getLightValue() {
        return 10;
    }
}
