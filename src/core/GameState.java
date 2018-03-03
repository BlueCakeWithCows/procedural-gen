package Core;

import renderer.DrawBatchCommand;
import renderer.View;

public interface GameState extends InputHandler {
    boolean graphicsReady();
    String getLabel();

    void update(Game game, double dt);

    void close(Game game);

    void show(Game game);

    DrawBatchCommand getDrawBatch(View view);

    @Override
    boolean doInput(char c);
}
