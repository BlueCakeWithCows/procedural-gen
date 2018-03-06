package byog.Core;

import byog.geometry.Rectangle;
import byog.levelBuilder.Dungeon;
import byog.levelBuilder.Entity;
import byog.levelBuilder.Player;
import byog.levelBuilder.World;
import byog.renderer.DrawBatchCommand;
import byog.renderer.DrawCommand;
import byog.renderer.DrawTextCommand;
import byog.renderer.DrawTextureCommand;
import byog.renderer.Fonts;
import byog.renderer.View;
import byog.tileEngine.TETile;
import byog.tileEngine.TileType;

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
    public void update(Game gameInstance, double dt) {
        world.update(dt);
    }

    @Override
    public void close(Game gameInstance) {
        gameInstance.removeInputHandler(this);
    }

    @Override
    public void show(Game gameInstance) {
        gameInstance.registerInputHandler(this);
        genWorld(gameInstance);
    }

    public void genWorld(Game gameInstance) {
        ready = false;
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
        if (player.getHealth() <= 0) {
            game.setWonGameStatus(false);
            game.setGameState(new EndWindow(game, false));
        }
        camera.boundCenter(player.getPosition(), 0, 0, world.getRegion().getWidth(),
            world.getRegion().getHeight());
        if (c == 'p' && world.getTile(player.getPosition()).getType() == TileType.PORTAL) {
            if (dungeon.getDepth() > 5) {
                game.setGameState(new EndWindow(game, true));
            } else {
                genWorld(game);
            }
        }
        return true;
    }

    @Override
    public DrawBatchCommand getDrawBatch(View view) {
        List<DrawCommand> commands = new ArrayList<>();
        for (int col = camera.getX(); col < camera.getX2(); col++) {
            for (int row = camera.getY(); row < camera.getY2() - 1; row++) {
                TETile tile = world.getRegion().getTile(col, row);
                DrawTextureCommand textureCommand =
                    new DrawTextureCommand(tile.getTexture(), col - camera.getX(),
                        row - camera.getY(), 1, 1, 255, world.getVisibleLightLevel(col, row), 1);
                commands.add(textureCommand);
            }
        }


        for (Entity e : world.getEntities()) {
            TETile tile = e.getTile();
            if (!world.getIsVisible(e.getX(), e.getY())) {
                continue;
            }
            DrawTextureCommand textureCommand =
                new DrawTextureCommand(tile.getTexture(), e.getX() - camera.getX(),
                    e.getY() - camera.getY(), 1, 1, 255,
                    world.getVisibleLightLevel(e.getX(), e.getY()), 10);
            commands.add(textureCommand);
        }

        Point2D point = this.game.getInput().pollMouse();
        commands.add(
            new DrawTextCommand(Fonts.MONACO, getTileType((int) point.getX(), (int) point.getY()),
                4, game.TOTAL_HEIGHT - 1.2));
        commands.add(
            new DrawTextCommand(Fonts.MONACO, getEntityType((int) point.getX(), (int) point.getY()),
                4, game.TOTAL_HEIGHT - 2.8));
        commands.add(
            new DrawTextCommand(Fonts.MONACO, player.getHealth() + "% health", game.TOTAL_WIDTH - 7,
                game.TOTAL_HEIGHT - 1.2));
        commands.add(new DrawTextCommand(Fonts.MONACO, player.getLightValue() + "0% light",
            game.TOTAL_WIDTH - 7, game.TOTAL_HEIGHT - 2.8));
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
