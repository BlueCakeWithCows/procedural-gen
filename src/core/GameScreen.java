package core;

import levelBuilder.Dungeon;
import levelBuilder.Entity;
import levelBuilder.Player;
import levelBuilder.World;
import renderer.DrawBatchCommand;
import renderer.DrawCommand;
import renderer.DrawTextureCommand;
import renderer.View;
import tileEngine.TETile;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements GameState {

    private Dungeon dungeon;
    private World world;
    private Player player;

    public GameScreen(Player player, Dungeon dungeon) {
        this.player = player;
        this.dungeon = dungeon;
    }

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
        world = dungeon.getFloor(player);
    }


    @Override
    public void doInput(char c) {

    }

    private boolean cached = false;
    private DrawBatchCommand cmd;

    @Override
    public DrawBatchCommand getDrawBatch(View view) {
        if (!cached) {
            List<DrawCommand> commands = new ArrayList<>();
            for (int col = 0; col < world.getRegion().getWidth(); col++) {
                for (int row = 0; row < world.getRegion().getHeight(); row++) {
                    TETile tile = world.getRegion().getTile(col, row);
                    DrawTextureCommand cmd = new DrawTextureCommand(tile.getTexture(), col, row, 1,
                        1, 255, 255
                    );
                    commands.add(cmd);
                }
            }

            for (Entity e : world.getEntities()) {
                TETile tile = e.getTile();
                DrawTextureCommand cmd = new DrawTextureCommand(tile.getTexture(), e.getX(),
                    e.getY(), 1, 1, 255, 255
                );
                commands.add(cmd);
            }
            cmd = new DrawBatchCommand(commands);
            cached = true;
        }
        return cmd;
    }
}
