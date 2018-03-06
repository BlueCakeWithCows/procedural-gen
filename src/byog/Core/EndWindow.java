package byog.Core;

import byog.renderer.DrawBatchCommand;
import byog.renderer.DrawCommand;
import byog.renderer.DrawTextCommand;
import byog.renderer.Fonts;
import byog.renderer.View;

import java.util.ArrayList;
import java.util.List;

public class EndWindow implements GameState {
    private DrawBatchCommand endCachedDrawBatch;
    private boolean wonGame;
    private Game game;

    public EndWindow(Game game, boolean wonGame) {
        this.wonGame = wonGame;
        this.game = game;
    }

    @Override
    public boolean graphicsReady() {
        return true;
    }

    @Override
    public String getLabel() {
        return null;
    }

    @Override
    public void update(Game g, double dt) {

    }

    @Override
    public void close(Game g) {
        g.removeInputHandler(this);
    }

    @Override
    public void show(Game g) {
        g.registerInputHandler(this);
    }

    public DrawBatchCommand getDrawBatch(View view) {
        double hW = view.getWidth() / 2;
        double hH = view.getHeight() / 2;
        List<DrawCommand> commands = new ArrayList<>(4);
        Fonts font = Fonts.MONACO;
        if (!wonGame) {
            commands.add(new DrawTextCommand(font, "Game Over", hW, view.getHeight() / 9 * 7));
            commands.add(new DrawTextCommand(font, "You ran into too many walls!", hW, hH / 9 * 6));
            commands.add(new DrawTextCommand(font, "Press N to start a new game.", hW, hH / 9 * 4));
        } else {
            commands
                .add(new DrawTextCommand(font, "Congratulations", hW, view.getHeight() / 9 * 7));
            commands.add(new DrawTextCommand(font, "You successfully exited the dark rooms!", hW,
                hH / 9 * 6));
            commands.add(new DrawTextCommand(font, "Press N to start a new game.", hW, hH / 9 * 4));
        }
        return new DrawBatchCommand(commands);

    }

    @Override
    public boolean doInput(char c) {
        if (c == 'n') {
            game.setGameState(new StartMenu(game));
        }
        return true;
    }
}
