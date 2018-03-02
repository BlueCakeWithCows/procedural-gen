package core;

import renderer.DrawBatchCommand;
import renderer.View;

public class GameScreen implements GameState {


    @Override
    public String getLabel() {
        return "Game Screen";
    }

    @Override
    public void update(Game game, double dt) {

    }

    @Override
    public void close(Game game) {
        game.removeInputHandler(this);
    }

    @Override
    public void show(Game game) {
        game.registerInputHandler(this);
    }


    @Override
    public void doInput(char c) {

    }

    private boolean cached;

    @Override
    public DrawBatchCommand getDrawBatch(View view) {
        return null;
    }


}
