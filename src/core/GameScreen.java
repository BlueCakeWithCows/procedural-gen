package Core;

import geometry.Rectangle;
import levelBuilder.Dungeon;
import levelBuilder.Entity;
import levelBuilder.Player;
import levelBuilder.World;
import renderer.DrawBatchCommand;
import renderer.DrawCommand;
import renderer.DrawTextCommand;
import renderer.DrawTextureCommand;
import renderer.Fonts;
import renderer.View;
import tileEngine.TETile;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class GameScreen implements GameState {

    volatile boolean ready = false;
    private Dungeon dungeon;
    private World world;
    private Player player;
    private Rectangle camera;
    private Game game;
    private boolean cached = false;
    private DrawBatchCommand cmd;

    public GameScreen(Game game, Player player, Dungeon dungeon) {
        this.game = game;
        this.player = player;
        this.dungeon = dungeon;
    }

    @Override
    public boolean graphicsReady() {
        return ready;
    }

    @Override
    public String getLabel() {
        return "Game Screen";
    }

    @Override
    public void update(Game game, double dt) {
        world.update(dt);
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
        world.update(0);
        ready = true;
    }

    @Override
    public boolean doInput(char c) {
        player.tryMove(c, world);
        camera.boundCenter(player.getPosition(), 0, 0, world.getRegion().getWidth(),
            world.getRegion().getHeight());
        return true;
    }

    @Override
    public DrawBatchCommand getDrawBatch(View view) {
        List<DrawCommand> commands = new ArrayList<>();
        for (int col = camera.getX(); col < camera.getX2(); col++) {
            for (int row = camera.getY(); row < camera.getY2() - 1; row++) {
                TETile tile = world.getRegion().getTile(col, row);
                DrawTextureCommand cmd =
                    new DrawTextureCommand(tile.getTexture(), col - camera.getX(),
                        row - camera.getY(), 1, 1, 255, world.getVisibleLightLevel(col, row), 1);
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

        Point2D point = this.game.getInput().pollMouse();
        commands.add(
            new DrawTextCommand(Fonts.ARIAL, getTileType((int) point.getX(), (int) point.getY()), 4,
                game.TOTAL_HEIGHT - 1.2));
        commands.add(
            new DrawTextCommand(Fonts.ARIAL, getEntityType((int) point.getX(), (int) point.getY()),
                4, game.TOTAL_HEIGHT - 2.8));

        cmd = new DrawBatchCommand(commands);
        return cmd;
    }

    private String getTileType(int x, int y) {
        TETile target = world.getTile(x + camera.getX(), y + camera.getY());
        if (target == null
                || world.getVisibleLightLevel(x + camera.getX(), y + camera.getY()) < 1) {
            return "";
        }
        return target.getDescription();
    }

    private String getEntityType(int x, int y) {
        Entity targetEntity = world.getEntityAt(x + camera.getX(), y + camera.getY());
        if (targetEntity == null || targetEntity.getTile() == null
                || world.getVisibleLightLevel(x + camera.getX(), y + camera.getY()) < 1) {
            return "";
        }
        return targetEntity.getTile().getDescription();
    }
}
