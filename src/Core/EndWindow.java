package Core;

import renderer.*;

import java.util.ArrayList;
import java.util.List;

public class EndWindow implements GameState {
    private DrawBatchCommand endCachedDrawBatch;
    private boolean wonGame;

    public EndWindow(boolean wonGame) {
        this.wonGame = wonGame;
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

    public DrawBatchCommand getDrawBatch(View view) {
            double hW = view.getWidth() / 2;
            double hH = view.getHeight() / 2;
            List<DrawCommand> commands = new ArrayList<>(4);
            Fonts font = Fonts.MONACO;
            if (!wonGame) {
                commands.add(new DrawTextCommand(font, "Game Over", hW, view.getHeight() / 9 * 7));
                commands.add(new DrawTextCommand(font, "You ran into too many walls!", hW, hH / 9 * 5));
            }
            else {
                commands.add(new DrawTextCommand(font, "Congratulations", hW, view.getHeight() / 9 * 7));
                commands.add(new DrawTextCommand(font, "You successfully exited the dark rooms!", hW, hH / 9 * 5));
            }
        return new DrawBatchCommand(commands);

    }

    @Override
    public boolean doInput(char c) {
        return false;
    }
}
