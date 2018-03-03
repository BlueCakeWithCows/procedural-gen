package core;

import levelBuilder.Dungeon;
import levelBuilder.Player;
import renderer.DrawBatchCommand;
import renderer.DrawCommand;
import renderer.DrawTextCommand;
import renderer.Fonts;
import renderer.View;

import java.util.ArrayList;
import java.util.List;

public class RandomSeedMenu implements GameState {

    private StringBuilder seed;
    private Game game;

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
    public void update(Game game, double dt) {

    }

    @Override
    public void close(Game game) {
        game.removeInputHandler(this);
    }

    @Override
    public void show(Game game) {
        seed = new StringBuilder();
        game.registerInputHandler(this);
    }

    private DrawBatchCommand cachedDrawBatch;
    private boolean cached = false;

    @Override
    public DrawBatchCommand getDrawBatch(View view) {
        if (!cached) {
            double hW = view.getWidth() / 2;
            double hH = view.getHeight() / 2;
            List<DrawCommand> commands = new ArrayList<>(4);
            Fonts font = Fonts.ARIAL;
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
    public void doInput(char c) {
        cached = false;

        if (c == ('\b')) {
            if (seed.length() > 0) {
                seed.deleteCharAt(seed.length() - 1);
            }
            return;
        }

        if (c == '0' && seed.length() == 0) {
            return;
        }
        if (Character.isDigit(c)) {
            if (seed.length() < 19) {
                seed.append(c);
            }
            return;
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
                new GameScreen(game, player, new Dungeon(lSeed, GeneratorSet.defaultSet)));
            return;
        }
    }
}
