package byog.Core;

import byog.levelBuilder.Dungeon;
import byog.levelBuilder.Player;
import byog.renderer.DrawBatchCommand;
import byog.renderer.DrawCommand;
import byog.renderer.DrawTextCommand;
import byog.renderer.Fonts;
import byog.renderer.View;

import java.util.ArrayList;
import java.util.List;

public class RandomSeedMenu implements GameState {

    private StringBuilder seed;
    private Game game;
    private DrawBatchCommand cachedDrawBatch;
    private boolean cached = false;

    public RandomSeedMenu(Game game) {
        this.game = game;
    }

    @Override
    public boolean graphicsReady() {
        return true;
    }

    @Override
    public String getLabel() {
        return "Random Seed Menu";
    }

    @Override
    public void update(Game gameInstance, double dt) {

    }

    @Override
    public void close(Game gameInstance) {
        gameInstance.removeInputHandler(this);
    }

    @Override
    public void show(Game gameInstance) {
        seed = new StringBuilder();
        gameInstance.registerInputHandler(this);
    }

    @Override
    public DrawBatchCommand getDrawBatch(View view) {
        if (!cached) {
            double hW = view.getWidth() / 2;
            double hH = view.getHeight() / 2;
            List<DrawCommand> commands = new ArrayList<>(4);
            Fonts font = Fonts.MONACO;
            commands.add(new DrawTextCommand(font, "Input Seed", hW, hH * 1.2));
            String outSeed = seed.toString();
            if (outSeed.length() == 0) {
                outSeed = "0";
            }
            commands.add(new DrawTextCommand(font, outSeed, hW, hH));
            cachedDrawBatch = new DrawBatchCommand(commands);
            cached = true;
        }
        return cachedDrawBatch;
    }

    @Override
    public boolean doInput(char c) {
        cached = false;

        if (c == ('\b')) {
            if (seed.length() > 0) {
                seed.deleteCharAt(seed.length() - 1);
            }
            return true;
        }

        if (c == '0' && seed.length() == 0) {
            return true;
        }
        if (Character.isDigit(c)) {
            if (seed.length() < 19) {
                seed.append(c);
            }
            return true;
        }

        if (c == 's') {
            long lSeed;
            if (seed.length() == 0) {
                lSeed = 0;
            } else {
                lSeed = Long.parseUnsignedLong(seed.toString());
            }
            Player player = new Player();
            game.setGameState(
                new GameScreen(game, player, new Dungeon(lSeed, GeneratorSet.DEFAULT_SET)));
            return true;
        }
        return false;
    }
}
