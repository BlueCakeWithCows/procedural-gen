package core;

import renderer.DrawBatchCommand;
import renderer.DrawCommand;
import renderer.Drawable;
import renderer.View;

public class RandomSeedMenu implements GameState, Drawable {
    @Override
    public String getLabel() {
        return null;
    }

    @Override
    public void update(Game game, double dt) {

    }

    @Override
    public void close(Game game) {

    }

    @Override
    public void show(Game game) {

    }

    @Override
    public DrawBatchCommand getDrawBatch(View view) {
        return null;
    }

    @Override
    public DrawCommand getDrawCommand(View view) {
        return null;
    }
}
