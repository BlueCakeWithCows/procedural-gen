package core;

import renderer.DrawBatchCommand;
import renderer.DrawCommand;
import renderer.DrawTextCommand;
import renderer.Drawable;
import renderer.Fonts;
import renderer.View;

import java.util.ArrayList;
import java.util.List;

public class StartMenu implements GameState, InputHandler {

    private Game game;

    public StartMenu(Game game) {
        this.game = game;
    }

    @Override
    public String getLabel() {
        return "Start Screen";
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
        if (cachedDrawBatch == null) {
            double hW = view.getWidth() / 2;
            double hH = view.getHeight() / 2;
            List<DrawCommand> commands = new ArrayList<>(4);
            Fonts font = Fonts.ARIAL;
            commands.add(new DrawTextCommand(font, "Castle Adventure", hW, hH));
            commands.add(new DrawTextCommand(font, "New Game (N)", hW, view.getHeight() / 9 * 5));
            commands.add(new DrawTextCommand(font, "Load Game (L)", hW, view.getHeight() / 9 * 4));
            commands.add(new DrawTextCommand(font, "Quit (Q)", hW, view.getHeight() / 9 * 3));
            cachedDrawBatch = new DrawBatchCommand(commands);
        }
        return cachedDrawBatch;
    }

    @Override
    public void doInput(char c) {
        switch (Character.toLowerCase(c)) {
            case 'l':

                break;
            case 'n':
                game.setGameState(new RandomSeedMenu());
                break;
            default:
                break;
        }
    }

    private DrawBatchCommand cachedDrawBatch;
}
