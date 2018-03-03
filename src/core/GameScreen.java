package core;

import geometry.Rectangle;
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
    private Rectangle camera;

    public GameScreen(Player player, Dungeon dungeon) {
        this.player = player;
        this.dungeon = dungeon;
    }

    @Override
    public boolean graphicsReady() {
        return ready;
    }

    volatile boolean ready = false;

    @Override
    public String getLabel() {
        return "Game Screen";
    }

    @Override
    public void update(Game game, double dt) {
        world.update();
        world.recalculateDynamicLightMap();
        world.calculateTotalLightLevel();
    }

    @Override
    public void close(Game game) {
        game.removeInputHandler(this);
    }

    @Override
    public void show(Game game) {
        game.registerInputHandler(this);
        world = dungeon.getFloor(player);
        camera = new Rectangle(0, 0, Game.WIDTH, Game.HEIGHT);
        camera.center(player.getPosition());
        camera.bound(0, 0, world.getRegion().getWidth(), world.getRegion().getHeight());
        ready = true;
    }


    @Override
    public void doInput(char c) {
        player.tryMove(c, world);
        camera.center(player.getPosition());
        camera.bound(0, 0, world.getRegion().getWidth(), world.getRegion().getHeight());
        cached = false;
    }

    private boolean cached = false;
    private DrawBatchCommand cmd;

    @Override
    public DrawBatchCommand getDrawBatch(View view) {
        if (!cached) {
            List<DrawCommand> commands = new ArrayList<>();
            for (int col = camera.getX(); col < camera.getX2(); col++) {
                for (int row = camera.getY(); row < camera.getY2(); row++) {
                    TETile tile = world.getRegion().getTile(col, row);
                    DrawTextureCommand cmd =
                        new DrawTextureCommand(tile.getTexture(), col - camera.getX(),
                            row - camera.getY(), 1, 1, 255, world.getVisibleLightLevel(col, row),
                            1);
                    commands.add(cmd);
                }
            }

            for (Entity e : world.getEntities()) {
                TETile tile = e.getTile();
                DrawTextureCommand cmd =
                    new DrawTextureCommand(tile.getTexture(), e.getX() - camera.getX(),
                        e.getY() - camera.getY(), 1, 1, 255,
                        world.getVisibleLightLevel(e.getX(), e.getY()), 10);
                commands.add(cmd);
            }
            cmd = new DrawBatchCommand(commands);
            cached = true;
        }
        return cmd;
    }
}
