package Core;

import renderer.DrawBatchCommand;
import renderer.DrawCommand;
import renderer.DrawTextCommand;
import renderer.Fonts;
import renderer.View;

import java.util.ArrayList;
import java.util.List;

public class StartMenu implements GameState, InputHandler {

    private Game game;
    private DrawBatchCommand cachedDrawBatch;

    public StartMenu(Game gameInstance) {
        this.game = gameInstance;
    }

    @Override
    public boolean graphicsReady() {
        return true;
    }

    @Override
    public String getLabel() {
        return "Start Screen";
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
        gameInstance.registerInputHandler(this);
    }

    @Override
    public DrawBatchCommand getDrawBatch(View view) {
        if (cachedDrawBatch == null) {
            double hW = view.getWidth() / 2;
            double hH = view.getHeight() / 2;
            List<DrawCommand> commands = new ArrayList<>(4);
            Fonts font = Fonts.MONACO;
            commands.add(new DrawTextCommand(font, "Dark Room", hW, view.getHeight() / 9 * 7));
            commands.add(new DrawTextCommand(font, "New Game (N)", hW, view.getHeight() / 9 * 5));
            commands.add(new DrawTextCommand(font, "Load Game (L)", hW, view.getHeight() / 9 * 4));
            commands.add(new DrawTextCommand(font, "Quit (Q)", hW, view.getHeight() / 9 * 3));
            cachedDrawBatch = new DrawBatchCommand(commands);
        }
        return cachedDrawBatch;
    }

    @Override
    public boolean doInput(char c) {
        switch (Character.toLowerCase(c)) {
            case 'l':
                String save = SaveGame.loadData();
                if (save == null) {
                    System.exit(0);
                }
                game.addToQueue(save);
                break;

            case 'n':
                game.setGameState(new RandomSeedMenu(game));
                break;

            default:
                return false;

        }
        return true;
    }
}
